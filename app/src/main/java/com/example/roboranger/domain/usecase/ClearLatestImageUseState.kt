package com.example.roboranger.domain.usecase

import com.example.roboranger.domain.LastImageRepository
import javax.inject.Inject

class ClearLatestImageUseState @Inject constructor(private val repository: LastImageRepository) {
    suspend operator fun invoke() {
        try {
            repository.clearLatestImage()
        } catch (e: Exception) {
            throw e
        }
    }
}