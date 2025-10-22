package com.example.roboranger.domain.model

import android.net.Uri

sealed class ImageState {
    data object NotAvailable : ImageState()
    data class Available(val uri: Uri) : ImageState()
}