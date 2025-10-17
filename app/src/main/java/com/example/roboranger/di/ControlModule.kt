package com.example.roboranger.di

import com.example.roboranger.data.RobotControlRepositoryImpl
import com.example.roboranger.data.RobotStreamRepositoryImpl
import com.example.roboranger.data.local.ImageSaverImpl
import com.example.roboranger.data.remote.RobotApi
import com.example.roboranger.data.remote.StreamApi
import com.example.roboranger.domain.ImageSaver
import com.example.roboranger.domain.RobotControlRepository
import com.example.roboranger.domain.RobotStreamRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
object ControlModule {

    // Configuracion de OkHttp para RobotApi y StreamApi
    @Provides
    @Singleton
    @Named("CONTROL_OKHTTP")
    fun provideControlOkHttp(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    @Named("STREAM_OKHTTP")
    fun provideStreamOkHttp(): OkHttpClient =
        OkHttpClient.Builder()
            // MJPEG: mantener lectura “infinita”
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()

    // Configuracion de Retrofit para RobotApi y StreamApi
    @Provides
    @Singleton
    @Named("CONTROL_RETROFIT")
    fun provideControlRetrofit(
        @Named("CONTROL_OKHTTP") okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.4.1/") // BASE_URL de CONTROL
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @Provides
    @Singleton
    @Named("STREAM_RETROFIT")
    fun provideStreamRetrofit(
        @Named("STREAM_OKHTTP") okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.4.1:81/") // BASE_URL de STREAM
        .client(okHttpClient)
        .build()

    // Configuracion de APIs para RobotApi y StreamApi
    @Provides
    @Singleton
    fun provideRobotApi(@Named("CONTROL_RETROFIT") retrofit: Retrofit): RobotApi =
        retrofit.create(RobotApi::class.java)

    @Provides @Singleton
    fun provideStreamApi(@Named("STREAM_RETROFIT") retrofit: Retrofit): StreamApi =
        retrofit.create(StreamApi::class.java)

    // Configuracion de Repositorios RobotControlRepository y RobotStreamRepository
    @Provides
    @Singleton
    fun provideRobotControlRepository(api: RobotApi): RobotControlRepository =
        RobotControlRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideRobotStreamRepository(api: StreamApi): RobotStreamRepository =
        RobotStreamRepositoryImpl(api)

    // Configuracion de ImageSaver
    @Provides
    @Singleton
    fun provideImageSaver(impl: ImageSaverImpl) : ImageSaver = impl
}