package com.example.roboranger.domain.usecase

import android.graphics.Bitmap
import com.example.roboranger.domain.RobotStreamRepository
import kotlinx.coroutines.flow.Flow

class StreamFramesUseCase(private val streamRepository: RobotStreamRepository) {
    operator fun invoke(): Flow<Bitmap> = streamRepository.streamFrames()
}