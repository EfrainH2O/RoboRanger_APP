package com.example.roboranger.di

import com.example.roboranger.data.FormsRepositoryImpl
import com.example.roboranger.data.LastImageRepositoryImpl
import com.example.roboranger.domain.FormsRepository
import com.example.roboranger.domain.LastImageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindModule {

    @Binds
    @Singleton
    abstract fun bindFormsRepository(
        impl: FormsRepositoryImpl
    ): FormsRepository

    @Binds
    @Singleton
    abstract fun bindLastImageRepository(
        impl: LastImageRepositoryImpl
    ): LastImageRepository
}