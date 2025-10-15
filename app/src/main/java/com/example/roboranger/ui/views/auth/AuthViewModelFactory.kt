package com.example.roboranger.ui.views.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.roboranger.data.ApiRepository
import com.example.roboranger.domain.usecase.GetAuthUseStateUseCase
import com.example.roboranger.domain.usecase.LogInUseCase
import com.example.roboranger.domain.usecase.LogOutUseCase

class AuthViewModelFactory(
    private val logInUseCase: LogInUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val getAuthUseStateUseCase: GetAuthUseStateUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(logInUseCase,
                logOutUseCase,
                getAuthUseStateUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}