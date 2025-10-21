package com.example.roboranger.di

import android.content.Context
import androidx.room.Room
import com.example.roboranger.data.FormsRepositoryImpl
import com.example.roboranger.data.local.Room.Converters
import com.example.roboranger.data.local.Room.FormsDataBase
import com.example.roboranger.data.local.Room.FormsRepository
import com.example.roboranger.data.local.Room.IFormsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FormsDataBaseModule {

    @Singleton
    @Provides
    fun provideConverters(): Converters {
        return Converters()
    }
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app: Context,
        converters: Converters
    ) = Room.databaseBuilder(app, FormsDataBase::class.java, "RoboRanger_DB")
        .addTypeConverter(converters)
        .build()

    @Singleton
    @Provides
    fun provideFormsDao(roomDataBase: FormsDataBase): IFormsDao {
        return roomDataBase.getIFormsDao()
    }
    @Singleton
    @Provides
    fun provideFormsRepository(dao: IFormsDao): FormsRepository {
        return FormsRepositoryImpl(dao)
    }

}