package com.example.roboranger.data.remote

import com.example.roboranger.data.local.TokenManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        // Si el request marca "No-Auth: true:, no se agrega el Bearer
        if (original.header("No-Auth") == "true") {
            val clean = original.newBuilder()
                .removeHeader("No-Auth")
                .build()
            return chain.proceed(clean)
        }

        // Se intenta leer el token almacenado con DataStore de forma bloqueante solo aqui
        val token: String? = runBlocking { tokenManager.token.firstOrNull() }

        val newReq = if (!token.isNullOrBlank()) {
            original.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else original

        return chain.proceed(newReq)
    }
}