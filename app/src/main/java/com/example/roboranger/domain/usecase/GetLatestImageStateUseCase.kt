package com.example.roboranger.domain.usecase

import com.example.roboranger.domain.LastImageRepository
import com.example.roboranger.domain.model.ImageState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLatestImageStateUseCase @Inject constructor(private val repository: LastImageRepository) {
    operator fun invoke(): Flow<ImageState> {
        return repository.getLatestImageState()
    }
}