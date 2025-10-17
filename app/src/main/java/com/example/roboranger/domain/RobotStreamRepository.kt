package com.example.roboranger.domain

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow

interface RobotStreamRepository {
    fun streamFrames() : Flow<Bitmap>
}