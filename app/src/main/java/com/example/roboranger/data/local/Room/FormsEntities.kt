package com.example.roboranger.data.local.Room

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.roboranger.ui.views.form.AnimalOptions
import com.example.roboranger.ui.views.form.ObservationsTypeOptions
import com.example.roboranger.ui.views.form.SeasonOptions
import com.example.roboranger.ui.views.form.WeatherOptions
import com.example.roboranger.ui.views.form.ZoneOptions


@Entity(tableName = "Forms_1")
data class Forms_1(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val transecto: String,
    val clima: WeatherOptions,
    val temporada: SeasonOptions,
    val tipo_animal: AnimalOptions,
    val nombre_comun: String,
    val nombre_cientifico: String,
    val numero_individuo: Int,
    val tipo_observacion: ObservationsTypeOptions,
    val observaciones: String,
    val latitude: Double,
    val longitude: Double,
    val fecha: String,
    var hora: String,
    val imagen: Uri,
    val temp_maxima: Double,
    val temp_minima: Double,
    val humedad_max: Double,
    val enviado: Boolean
)

@Entity(tableName = "Forms_2")
data class Forms_2(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre : String,
    val clima: WeatherOptions,
    val temporada: SeasonOptions,
    val zona: ZoneOptions,
    val pluviosidad: Double,
    val temperaturamaxima: Double,
    val humedadmaxima: Double,
    val temperaturaminima: Double,
    val nivelquebrada: Double,
    val latitude: Double,
    val longitude: Double,
    val fecha: String,
    var hora: String,
    val enviado: Boolean,
    val imagen: Uri
)


