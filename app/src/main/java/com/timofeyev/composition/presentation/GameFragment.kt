package com.timofeyev.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.timofeyev.composition.R
import com.timofeyev.composition.databinding.FragmentGameBinding
import com.timofeyev.composition.domain.entity.GameResult
import com.timofeyev.composition.domain.entity.GameSettings
import com.timofeyev.composition.domain.entity.Level

class GameFragment : Fragment() {

  private lateinit var level: Level
  private val viewModel by lazy {
    ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(
      requireActivity().application)
    )[GameViewModel::class.java]
  }

  private var _binding: FragmentGameBinding? = null
  private val binding: FragmentGameBinding
  get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    parseArgs()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentGameBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.tvOption1.setOnClickListener {
      launchGameFinishedFragment()
    }
  }

  private fun observeViewModel() {
    viewModel.question.observe(viewLifecycleOwner) {
      binding.tvSum.text = it.sum.toString()
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private fun launchGameFinishedFragment() {
    val gameResult = GameResult(
      winner = true,
      countOfRightAnswers = 10,
      countOfQuestions = 10,
      gameSettings = GameSettings(
        maxSumValue = 10,
        minCountOfRightAnswers = 10,
        minPercentOfRightAnswers = 100,
        gameTimeInSeconds = 60
      )
    )

    requireActivity().supportFragmentManager.beginTransaction()
      .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
      .addToBackStack(null)
      .commit()
  }

  private fun parseArgs() {
    requireArguments().getParcelable<Level>(KEY_LEVEL)?.let {
      level = it
    }
  }

  companion object {
    private const val KEY_LEVEL = "level"
    const val NAME = "GameFragment"

    fun newInstance(level: Level): GameFragment {
      return GameFragment().apply {
        arguments = Bundle().apply {
          putParcelable(KEY_LEVEL, level)
        }
      }
    }
  }
}