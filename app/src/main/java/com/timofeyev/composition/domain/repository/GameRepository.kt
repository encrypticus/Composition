package com.timofeyev.composition.domain.repository

import com.timofeyev.composition.domain.entity.GameSettings
import com.timofeyev.composition.domain.entity.Level
import com.timofeyev.composition.domain.entity.Question

interface GameRepository {

  fun generateQuestion(
    maxSumValue: Int,
    countOfOptions: Int
  ): Question

  fun getGameSettings(level: Level): GameSettings
}