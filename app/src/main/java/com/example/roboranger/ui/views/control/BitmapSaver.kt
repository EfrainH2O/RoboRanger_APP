package com.example.roboranger.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import java.io.OutputStream


sealed class SaveState{
    object Idle: SaveState()
    object Saving: SaveState()
    data class Success(val msg: String): SaveState()
    data class Error (val errorMsg : String ): SaveState()
}

object BitmapSaver {
    fun saveBitmap(context: Context, bitmap: Bitmap, displayName: String) : Flow<SaveState> = flow{

        val collection =
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.IS_PENDING, 1)
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/RoboRanger")
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(collection, contentValues)

        if (uri == null) {
            emit(SaveState.Error("Failed to create MediaStore entry."))
            return@flow
        }

        uri.let { imageUri ->
            try {
                val outputStream: OutputStream? = resolver.openOutputStream(imageUri)
                outputStream?.use { stream ->
                    if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                        throw IOException("Failed to save bitmap.")
                    }
                }

                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(imageUri, contentValues, null, null)
                emit(SaveState.Success("Image saved to gallery!"))

            } catch (e: Exception) {
                resolver.delete(imageUri, null, null)
                e.printStackTrace()
                emit(SaveState.Error("Failed to save image: ${e.message}"))
            }
        }
    }.flowOn(Dispatchers.IO)

}
