package com.example.roboranger.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object StreamClient {

    private const val BASE_URL = "http://192.168.4.1:81/"


    // Creamos un cliente OkHttp y le añadimos el interceptor
    private val httpClient = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .build()

    // Creamos la instancia de Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient) // Usamos el cliente OkHttp personalizado
        .build()

    // Creamos la implementación de nuestra ApiService
    val api: StreamApi by lazy {
        retrofit.create(StreamApi::class.java)
    }
}