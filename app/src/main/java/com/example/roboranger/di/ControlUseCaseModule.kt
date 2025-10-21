package com.example.roboranger.di

import com.example.roboranger.domain.ImageSaver
import com.example.roboranger.domain.RobotControlRepository
import com.example.roboranger.domain.RobotStreamRepository
import com.example.roboranger.domain.usecase.StreamFramesUseCase
import com.example.roboranger.domain.usecase.control.CapturePhotoUseCase
import com.example.roboranger.domain.usecase.control.SetLightUseCase
import com.example.roboranger.domain.usecase.control.StopUseCase
import com.example.roboranger.domain.usecase.control.TryGoBackUseCase
import com.example.roboranger.domain.usecase.control.TryGoFrontUseCase
import com.example.roboranger.domain.usecase.control.TryGoLeftUseCase
import com.example.roboranger.domain.usecase.control.TryGoRightUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ControlUseCaseModule {
    @Provides
    @Singleton
    fun provideTryGoFrontUseCase(repo: RobotControlRepository) = TryGoFrontUseCase(repo)

    @Provides
    @Singleton
    fun provideTryGoBackUseCase(repo: RobotControlRepository) = TryGoBackUseCase(repo)

    @Provides
    @Singleton
    fun provideTryGoLeftUseCase(repo: RobotControlRepository) = TryGoLeftUseCase(repo)

    @Provides
    @Singleton
    fun provideTryGoRightUseCase(repo: RobotControlRepository) = TryGoRightUseCase(repo)

    @Provides
    @Singleton
    fun provideStopUseCase(repo: RobotControlRepository) = StopUseCase(repo)

    @Provides
    @Singleton
    fun provideSetLightUseCase(repo: RobotControlRepository) = SetLightUseCase(repo)

    @Provides
    @Singleton
    fun provideStreamFramesUseCase(streamRepo: RobotStreamRepository) =
        StreamFramesUseCase(streamRepo)

    @Provides
    @Singleton
    fun provideCapturePhotoUseCase(imageSaver: ImageSaver) =
        CapturePhotoUseCase(imageSaver)
}