package com.example.roboranger.di

import com.example.roboranger.ui.views.network.WifiConnectionRepository
import com.example.roboranger.ui.views.network.WifiConnectionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WifiConnectionModule {

    @Binds
    @Singleton
    abstract fun bindWifiConnectionRepository(
        impl: WifiConnectionRepositoryImpl
    ): WifiConnectionRepository
}