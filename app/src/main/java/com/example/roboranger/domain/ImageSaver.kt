package com.example.roboranger.domain

import android.graphics.Bitmap
import com.example.roboranger.domain.model.SaveState
import kotlinx.coroutines.flow.Flow

interface ImageSaver {
    fun save(bitmap: Bitmap, fileName: String) : Flow<SaveState>
}