package com.example.roboranger.domain.model

sealed class SaveState{
    object Idle: SaveState()
    object Saving: SaveState()
    data class Success(val msg: String): SaveState()
    data class Error (val errorMsg : String ): SaveState()
}