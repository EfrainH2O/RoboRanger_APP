package com.example.roboranger.ui.views.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roboranger.domain.model.AuthState
import com.example.roboranger.domain.usecase.GetAuthUseStateUseCase
import com.example.roboranger.domain.usecase.LogInUseCase
import com.example.roboranger.domain.usecase.LogOutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val logInUseCase: LogInUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val getAuthUseStateUseCase: GetAuthUseStateUseCase
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.InitialLoading)
    val authState = _authState.asStateFlow()

    init {
        viewModelScope.launch {
            getAuthUseStateUseCase().collect { state ->
                _authState.value = state
            }
        }
    }

    // Funcion para iniciar sesion
    fun signIn(userEmail: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                logInUseCase(userEmail, password)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Error de red")
            }
        }
    }

    // Funcion para cerrar sesion
    fun signOut() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                logOutUseCase()
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Error de red")
            }
        }
    }
}