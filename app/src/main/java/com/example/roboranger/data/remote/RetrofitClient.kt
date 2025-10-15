package com.example.roboranger.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://192.168.4.1/"

    // Creamos un interceptor para loguear las peticiones y respuestas
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Creamos un cliente OkHttp y le añadimos el interceptor
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Creamos la instancia de Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient) // Usamos el cliente OkHttp personalizado
        .build()

    // Creamos la implementación de nuestra ApiService
    val api: RobotApi by lazy {
        retrofit.create(RobotApi::class.java)
    }
}