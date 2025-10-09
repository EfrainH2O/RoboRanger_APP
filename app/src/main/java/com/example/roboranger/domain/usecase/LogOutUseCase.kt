package com.example.roboranger.domain.usecase

import com.example.roboranger.data.ApiRepository

class LogOutUseCase(private val repo: ApiRepository) {
    suspend operator fun invoke() {
        try {
            repo.signOut()
        } catch (e: Exception) {
            throw e
        }
    }
}