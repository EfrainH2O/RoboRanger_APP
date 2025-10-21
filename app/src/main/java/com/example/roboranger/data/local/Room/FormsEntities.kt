package com.example.roboranger.data.local.Room

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class AnimalType {
    MAMMAL,
    AVE,
    REPTIL,
    AMPHIBIO,
    INSECTO
}

enum class ObservationType {
    VISTA_DIRECTA,
    HUELLAS,
    RASTRO,
    CACERIA,
    INFORMACION_DE_TERCERO
}

enum class ClimaType{
    SOLEADO,
    NUBLADO,
    LLUVIA
}

enum class EpocaType{
    VERANO,
    INVIERNO
}

enum class ZonaType{
    BOSQUE,
    ARREGLO_AGROFORESTAL,
    CULTIVOS_TRANSITORIOS,
    CULTIVOS_PERMANENTES
}

@Entity(tableName = "Forms_1")
data class Forms_1(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val transecto: Int,
    val clima: ClimaType,
    val temporada: EpocaType,
    val tipo_animal: AnimalType,
    val nombre_comun: String,
    val nombre_cientifico: String,
    val numero_individuo: Int,
    val tipo_observacion: ObservationType,
    val observaciones: String,
    val latitude: Double,
    val longitude: Double,
    val fecha: String,
    val imagenen: Uri,
    val temp_maxima: Int,
    val temp_minima: Int,
    val humedad_min: Int,
    val enviado: Boolean
)

@Entity(tableName = "Forms_2")
data class Forms_2(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val clima: ClimaType,
    val temporada: EpocaType,
    val zona: ZonaType,
    val pluviosidad: String,
    val temperaturamaxima: String,
    val humedadmaxima: String,
    val temperaturaminima: String,
    val nivelquebrada: String,
    val latitude: Double,
    val longitude: Double,
    val fecha: String,
    val enviado: Boolean
)


