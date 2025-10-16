package com.example.roboranger.di

import android.content.Context
import com.example.roboranger.data.ApiRepository
import com.example.roboranger.data.local.TokenManager
import com.example.roboranger.data.remote.ApiKeyInterceptor
import com.example.roboranger.data.remote.ApiService
import com.example.roboranger.data.remote.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Configuracion de valores base para ApiService
    @Provides
    @Singleton
    @Named("BASE_URL")
    fun provideBaseUrl() : String ="https://ekgcss8ww8o4ok480g08soo4.91.98.193.75.sslip.io/api/robo/"

    @Provides
    @Singleton
    @Named("API_KEY")
    fun provideApiKey() : String = "robo-key-789"

    // Configuracion del tokenManager
    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context) : TokenManager =
        TokenManager(context)

    // Configuracion de los interceptores para ApiService
    @Provides
    @Singleton
    fun provideApiKeyInterceptor(@Named("API_KEY") apiKey: String) : ApiKeyInterceptor =
        ApiKeyInterceptor(apiKey)

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager) : AuthInterceptor =
        AuthInterceptor(tokenManager)

    @Provides
    @Singleton
    fun provideLoggingInterceptor() : HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    // Configuracion de OkHttp y Retrofit para ApiService
    @Provides
    @Singleton
    fun provideOkHttpClient(
        apiKeyInterceptor: ApiKeyInterceptor,
        authInterceptor: AuthInterceptor,
        logging: HttpLoggingInterceptor
    ) : OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor) // Aqui se agrega el interceptor api key global
            .addInterceptor(authInterceptor) // Aqui se agrega bearer si existe un token almacenado
            .addInterceptor(logging)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideGson() : Gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson) : GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun provideRetrofit(
        @Named("BASE_URL") baseUrl: String,
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ) : Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) : ApiService =
        retrofit.create(ApiService::class.java)

    // Configuracion del Repositorio de ApiRepository
    @Provides
    @Singleton
    fun provideApiRepository(
        apiService: ApiService,
        tokenManager: TokenManager
    ) : ApiRepository = ApiRepository(apiService, tokenManager)
}