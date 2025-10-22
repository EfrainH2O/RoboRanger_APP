package com.example.roboranger.domain.usecase

import android.net.Uri
import com.example.roboranger.domain.LastImageRepository
import javax.inject.Inject

class UpdateLatestImageUriUseCase @Inject constructor(private val repository: LastImageRepository) {
    suspend operator fun invoke(uri: Uri) {
        try {
            repository.updateLatestImageUri(uri)
        } catch (e: Exception) {
            throw e
        }
    }
}