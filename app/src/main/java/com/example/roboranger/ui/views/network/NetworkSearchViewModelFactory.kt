package com.example.roboranger.ui.views.network

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NetworkSearchViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NetworkSearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NetworkSearchViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}