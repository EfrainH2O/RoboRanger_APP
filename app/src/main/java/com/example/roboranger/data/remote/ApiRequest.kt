package com.example.roboranger.data.remote

data class AuthRequest(
    val userEmail: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val message: String
)