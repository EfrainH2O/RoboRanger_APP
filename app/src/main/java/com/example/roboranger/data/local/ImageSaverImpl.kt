package com.example.roboranger.data.local

import android.content.Context
import android.graphics.Bitmap
import com.example.roboranger.domain.ImageSaver
import com.example.roboranger.domain.model.SaveState
import com.example.roboranger.ui.views.control.BitmapSaver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImageSaverImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ImageSaver {
    override fun save(bitmap: Bitmap, fileName: String): Flow<SaveState> {
        // Se reutiliza el BitmapSaver
        return BitmapSaver.saveBitmap(context, bitmap, fileName)
    }
}