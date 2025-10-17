package com.example.roboranger.domain.usecase.control

import com.example.roboranger.domain.RobotControlRepository

class TryGoBackUseCase(private val repo: RobotControlRepository) {
    suspend operator fun invoke() = repo.goBack()
}