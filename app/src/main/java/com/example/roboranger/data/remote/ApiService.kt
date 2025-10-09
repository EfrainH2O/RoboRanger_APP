package com.example.roboranger.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("users/login")
    suspend fun signIn(@Body request: AuthRequest): Response<AuthResponse>

    @POST("users/logout")
    suspend fun signOut(): Response<Unit>
}