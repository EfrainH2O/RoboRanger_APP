package com.example.roboranger.data.remote

import com.example.roboranger.data.local.TokenManager
import okhttp3.logging.HttpLoggingInterceptor

object ApiClient {
    private const val BASE_URL = "api.ecoranger.org/robo/"

    fun create(tokenManager: TokenManager): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }


    }
}