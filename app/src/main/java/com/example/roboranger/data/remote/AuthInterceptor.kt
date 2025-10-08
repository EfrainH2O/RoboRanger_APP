package com.example.roboranger.data.remote

import com.example.roboranger.data.local.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenManager.token.first()
        }
        val request = chain.request().newBuilder()
        if (token != null) {
            request.addHeader("Authorization", token)
        }
        return chain.proceed(request.build())
    }
}