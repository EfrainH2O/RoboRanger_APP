package com.example.roboranger.domain.usecase

import com.example.roboranger.data.ApiRepository

class LogInUseCase(private val repo: ApiRepository) {
    suspend operator fun invoke(userEmail: String, password: String) {
        try {
            repo.signIn(userEmail, password)
        } catch (e: Exception) {
            throw e
        }
    }
}