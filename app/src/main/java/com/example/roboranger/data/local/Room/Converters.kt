package com.example.roboranger.data.local.Room

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter

@ProvidedTypeConverter
class Converters {
    @TypeConverter
    fun fromAnimalType(value: AnimalType): Int{
        return value.ordinal
    }
    @TypeConverter
    fun toAnimalType(value: Int): AnimalType {
        return enumValues<AnimalType>()[value]
    }

    @TypeConverter
    fun fromObservationType(value: ObservationType): Int {
        return value.ordinal
    }

    @TypeConverter
    fun toObservationType(value: Int): ObservationType {
        return enumValues<ObservationType>()[value]
    }

    @TypeConverter
    fun fromUri(uri: Uri): String? {
        return uri.toString()
    }

    @TypeConverter
    fun toUri(uriString: String): Uri? {
        return uriString.toUri()
    }

    // --- TypeConverter para ClimaType ---
    @TypeConverter
    fun fromClimaType(value: ClimaType): Int {
        return value.ordinal
    }

    @TypeConverter
    fun toClimaType(value: Int): ClimaType {
        return enumValues<ClimaType>()[value]
    }

    // --- TypeConverter para EpocaType ---
    @TypeConverter
    fun fromEpocaType(value: EpocaType): Int {
        return value.ordinal
    }

    @TypeConverter
    fun toEpocaType(value: Int): EpocaType {
        return enumValues<EpocaType>()[value]
    }

    // --- TypeConverter para ZonaType ---
    @TypeConverter
    fun fromZonaType(value: ZonaType): Int {
        return value.ordinal
    }

    @TypeConverter
    fun toZonaType(value: Int): ZonaType {
        return enumValues<ZonaType>()[value]
    }

}