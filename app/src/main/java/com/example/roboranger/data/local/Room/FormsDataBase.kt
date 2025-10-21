package com.example.roboranger.data.local.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [Forms_1::class, Forms_2::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class FormsDataBase: RoomDatabase() {
    abstract fun getIFormsDao(): IFormsDao
}