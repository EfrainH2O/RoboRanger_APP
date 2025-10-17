package com.example.roboranger.domain.usecase.control

import com.example.roboranger.domain.RobotControlRepository

class SetLightUseCase(private val repo: RobotControlRepository) {
    suspend operator fun invoke(on: Boolean) = repo.setLight(on)
}