package com.example.roboranger.ui.views.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.roboranger.data.ApiRepository
import com.example.roboranger.domain.usecase.GetAuthUseStateUseCase
import com.example.roboranger.domain.usecase.LogInUseCase
import com.example.roboranger.domain.usecase.LogOutUseCase

class AuthViewModelFactory(private val apiRepo: ApiRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            val logInUseCase = LogInUseCase(apiRepo)
            val logOutUseCase = LogOutUseCase(apiRepo)
            val getAuthUseStateUseCase = GetAuthUseStateUseCase(apiRepo)
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(logInUseCase, logOutUseCase, getAuthUseStateUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}