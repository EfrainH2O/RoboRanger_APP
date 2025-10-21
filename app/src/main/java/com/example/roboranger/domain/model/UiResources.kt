package com.example.roboranger.domain.model

sealed class UIResources<out T> {
    data object Loading : UIResources<Nothing>()
    data class Success<out T>(val data: T) : UIResources<T>()
    data class Error(val message: String) : UIResources<Nothing>()
}
