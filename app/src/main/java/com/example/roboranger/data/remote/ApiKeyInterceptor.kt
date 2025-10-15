package com.example.roboranger.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(
    private val apiKey: String = "robo-key-789"
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val newReq = req.newBuilder()
            .addHeader("x-api-key", apiKey) // valor requerido por el backend para realizar cualquier request
            .build()
        return chain.proceed(newReq)
    }
}