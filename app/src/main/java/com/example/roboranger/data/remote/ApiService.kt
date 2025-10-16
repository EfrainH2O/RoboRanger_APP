package com.example.roboranger.data.remote

import com.example.roboranger.data.remote.dto.AuthRequestDto
import com.example.roboranger.data.remote.dto.AuthResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("No-Auth: true") // evita usar bearer en login mientras que apiKey es global
    @POST("users/login")
    suspend fun signIn(@Body request: AuthRequestDto): Response<AuthResponseDto>

    @POST("users/logout")
    suspend fun signOut(): Response<Unit>
}