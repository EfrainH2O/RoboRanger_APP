package com.example.roboranger.domain.model

sealed class AuthState {
    object InitialLoading: AuthState()
    object Loading: AuthState()
    object Authenticated: AuthState()
    object Unauthenticated: AuthState()
    data class Error(val message: String) : AuthState()
}