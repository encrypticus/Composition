package com.timofeyev.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.timofeyev.composition.R
import com.timofeyev.composition.data.GameRepositoryImpl
import com.timofeyev.composition.domain.entity.GameResult
import com.timofeyev.composition.domain.entity.GameSettings
import com.timofeyev.composition.domain.entity.Level
import com.timofeyev.composition.domain.entity.Question
import com.timofeyev.composition.domain.usecases.GenerateQuestionUseCase
import com.timofeyev.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel(application: Application): AndroidViewModel(application) {
  private val repository = GameRepositoryImpl
  private val context = application

  private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
  private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

  private lateinit var gameSettings: GameSettings
  private lateinit var level: Level

  private val _formattedTime = MutableLiveData<String>()
  val formattedTime: LiveData<String>
  get() = _formattedTime

  private val _question = MutableLiveData<Question>()
  val question: LiveData<Question>
  get() = _question

  private val _percentOfRightAnswers = MutableLiveData<Int>()
  val percentOfRightAnswers: LiveData<Int>
    get() = _percentOfRightAnswers

  private val _progressAnswers = MutableLiveData<String>()
  val progressAnswers: LiveData<String>
    get() = _progressAnswers

  private val _enoughCount = MutableLiveData<Boolean>()
  val enoughCount: LiveData<Boolean>
    get() = _enoughCount

  private val _enoughPercent = MutableLiveData<Boolean>()
  val enoughPercent: LiveData<Boolean>
    get() = _enoughPercent

  private val _minPercent = MutableLiveData<Int>()
  val minPercent: LiveData<Int>
    get() = _minPercent

  private val _gameResult = MutableLiveData<GameResult>()
  val gameResult: LiveData<GameResult>
    get() = _gameResult

  private var timer: CountDownTimer? = null

  private var countOfRightAnswers = 0
  private var countOfQuestions = 0

  private fun generateQuestion() {
    _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
  }

  fun startGame(level: Level) {
    this.level = level
    this.gameSettings = getGameSettingsUseCase(level)
    _minPercent.value = gameSettings.minPercentOfRightAnswers
    startTimer()
    generateQuestion()
  }

  private fun updateProgress() {
    val percent = calculatePercentOfRightAnswers()
    _percentOfRightAnswers.value = percent
    _progressAnswers.value = String.format(
      context.resources.getString(R.string.progress_answers),
      countOfRightAnswers,
      gameSettings.minCountOfRightAnswers
    )
    _enoughCount.value = countOfRightAnswers >= gameSettings.minCountOfRightAnswers
    _enoughPercent.value = percent >= gameSettings.minPercentOfRightAnswers
  }

  private fun calculatePercentOfRightAnswers(): Int {
    if (countOfQuestions == 0) {
      return 0
    }
    return ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
  }

  fun chooseAnswer(number: Int) {
    checkAnswer(number)
    updateProgress()
    generateQuestion()
  }

  private fun checkAnswer(number: Int) {
    val rightAnswer = question.value?.rightAnswer
    if (number == rightAnswer) {
      countOfRightAnswers++
    }
    countOfQuestions++
  }

  private fun formatTime(millis: Long): String {
    val seconds = millis / MILLS_IN_SECONDS
    val minutes = seconds / MILLS_IN_SECONDS
    val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
    return String.format("%02d:%02d", minutes, leftSeconds)
  }

  private fun startTimer() {
    timer = object : CountDownTimer(
      gameSettings.gameTimeInSeconds * MILLS_IN_SECONDS,
      MILLS_IN_SECONDS
    ) {
      override fun onTick(millis: Long) {
        _formattedTime.value = formatTime(millis)
      }

      override fun onFinish() {
        finishGame()
      }

    }
    timer?.start()
  }

  private fun finishGame() {
    _gameResult.value = GameResult(
      enoughCount.value == true && enoughPercent.value == true,
      countOfRightAnswers,
      countOfQuestions,
      gameSettings
    )
  }

  override fun onCleared() {
    super.onCleared()
    timer?.cancel()
  }

  companion object {
    private const val MILLS_IN_SECONDS = 1000L
    private const val SECONDS_IN_MINUTES = 60
  }
}