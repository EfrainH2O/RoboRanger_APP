package com.example.roboranger.data

import android.net.Uri
import com.example.roboranger.domain.LastImageRepository
import com.example.roboranger.domain.model.ImageState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class LastImageRepositoryImpl @Inject constructor() : LastImageRepository {
    // Estado de la ultima imagen guardada
    private val _latestImageState = MutableStateFlow<ImageState>(ImageState.NotAvailable)

    override fun getLatestImageState(): Flow<ImageState> {
        return _latestImageState.asStateFlow()
    }

    override suspend fun updateLatestImageUri(uri: Uri) {
        _latestImageState.value = ImageState.Available(uri)
    }

    override suspend fun clearLatestImage() {
        _latestImageState.value = ImageState.NotAvailable
    }
}