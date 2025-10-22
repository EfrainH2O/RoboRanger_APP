package com.example.roboranger.domain

import android.net.Uri
import com.example.roboranger.domain.model.ImageState
import kotlinx.coroutines.flow.Flow

interface LastImageRepository {
    // Funcion para obtener la ultima imagen guardada en el repositorio
    fun getLatestImageState(): Flow<ImageState>

    // Funcion para actualizar la URI desde donde se captura la imagen
    suspend fun updateLatestImageUri(uri: Uri)

    // Funcion para limpiar el estado de la imagen
    suspend fun clearLatestImage()
}