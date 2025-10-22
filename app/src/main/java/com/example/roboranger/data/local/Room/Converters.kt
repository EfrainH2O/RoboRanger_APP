package com.example.roboranger.data.local.Room

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.roboranger.ui.views.form.AnimalOptions
import com.example.roboranger.ui.views.form.ObservationsTypeOptions
import com.example.roboranger.ui.views.form.SeasonOptions
import com.example.roboranger.ui.views.form.WeatherOptions
import com.example.roboranger.ui.views.form.ZoneOptions

@ProvidedTypeConverter
class Converters {
    @TypeConverter
    fun fromAnimalOptions(value: AnimalOptions): Int{
        return value.ordinal
    }
    @TypeConverter
    fun toAnimalOptions(value: Int): AnimalOptions {
        return enumValues<AnimalOptions>()[value]
    }

    @TypeConverter
    fun fromObservationsTypeOptions(value: ObservationsTypeOptions): Int {
        return value.ordinal
    }

    @TypeConverter
    fun toObservationsTypeOptions(value: Int): ObservationsTypeOptions {
        return enumValues<ObservationsTypeOptions>()[value]
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
    fun fromWeatherOptions(value: WeatherOptions): Int {
        return value.ordinal
    }

    @TypeConverter
    fun toWeatherOptions(value: Int): WeatherOptions {
        return enumValues<WeatherOptions>()[value]
    }

    // --- TypeConverter para EpocaType ---
    @TypeConverter
    fun fromSeasonOptions(value: SeasonOptions): Int {
        return value.ordinal
    }

    @TypeConverter
    fun toSeasonOptions(value: Int): SeasonOptions {
        return enumValues<SeasonOptions>()[value]
    }

    // --- TypeConverter para ZonaType ---
    @TypeConverter
    fun fromZoneOptions(value: ZoneOptions): Int {
        return value.ordinal
    }

    @TypeConverter
    fun toZoneOptions(value: Int): ZoneOptions {
        return enumValues<ZoneOptions>()[value]
    }

}