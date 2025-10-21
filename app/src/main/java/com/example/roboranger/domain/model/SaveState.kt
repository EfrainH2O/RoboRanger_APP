package com.example.roboranger.domain.model

import android.net.Uri

sealed class SaveState{
    object Idle: SaveState()
    object Saving: SaveState()
    data class Success(val savedURI: Uri): SaveState()
    data class Error (val errorMsg : String ): SaveState()
}