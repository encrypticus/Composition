package com.timofeyev.composition.domain.usecases

import com.timofeyev.composition.domain.entity.GameSettings
import com.timofeyev.composition.domain.entity.Level
import com.timofeyev.composition.domain.repository.GameRepository

class GetGameSettingsUseCase(private val repository: GameRepository) {

  operator fun invoke(level: Level): GameSettings {
    return repository.getGameSettings(level)
  }
}