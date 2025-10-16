package com.example.roboranger.di

import com.example.roboranger.data.ApiRepository
import com.example.roboranger.domain.usecase.GetAuthUseStateUseCase
import com.example.roboranger.domain.usecase.LogInUseCase
import com.example.roboranger.domain.usecase.LogOutUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideLogInUseCase(repo: ApiRepository) : LogInUseCase =
        LogInUseCase(repo)

    @Provides
    @Singleton
    fun provideLogOutUseCase(repo: ApiRepository) : LogOutUseCase =
        LogOutUseCase(repo)

    @Provides
    @Singleton
    fun provideGetAuthUseStateUseCase(repo: ApiRepository) : GetAuthUseStateUseCase =
        GetAuthUseStateUseCase(repo)
}