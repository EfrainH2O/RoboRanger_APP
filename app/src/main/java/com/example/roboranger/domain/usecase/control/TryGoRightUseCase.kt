package com.example.roboranger.domain.usecase.control

import com.example.roboranger.domain.RobotControlRepository

class TryGoRightUseCase(private val repo: RobotControlRepository) {
    suspend operator fun invoke() = repo.goRight()
}