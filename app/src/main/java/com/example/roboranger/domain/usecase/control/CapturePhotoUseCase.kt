package com.example.roboranger.domain.usecase.control

import android.graphics.Bitmap
import com.example.roboranger.domain.ImageSaver
import com.example.roboranger.domain.model.SaveState
import kotlinx.coroutines.flow.Flow

class CapturePhotoUseCase(
    private val imageSaver: ImageSaver
) {
    operator fun invoke(bitmap: Bitmap, fileName: String) : Flow<SaveState> =
        imageSaver.save(bitmap, fileName)
}