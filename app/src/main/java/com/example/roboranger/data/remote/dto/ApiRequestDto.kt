package com.example.roboranger.data.remote.dto

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

// Auth Request y Response Dtos
data class AuthRequestDto(
    @SerializedName("user_email")
    val userEmail: String,
    @SerializedName("password")
    val password: String
)

data class UserDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("user_email")
    val userEmail: String,
    @SerializedName("tenant")
    val tenant: String
)

data class AuthResponseDto(
    @SerializedName("user")
    val user: UserDto,
    @SerializedName("token")
    val token: String,
    @SerializedName("message")
    val message: String?,
    @SerializedName("token_type")
    val tokenType: String?,
    @SerializedName("expires_in")
    val expiresIn: Long?
)

// Forms Request y Response Dto
data class Form1RequestDto(
    @SerializedName("transecto")
    val transect: String,
    @SerializedName("clima")
    val weather: String,
    @SerializedName("temporada")
    val season: String,
    @SerializedName("tipoanimal")
    val animalType: String,
    @SerializedName("nombrecomun")
    val commonName: String,
    @SerializedName("nombrecientifico")
    val scientificName: String,
    @SerializedName("numeroindividuos")
    val individuals: String,
    @SerializedName("tipoobservacion")
    val observationsType: String,
    @SerializedName("observaciones")
    val observations: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("temperaturamaxima")
    val maxTemp: String, // "35.6" o "35.6 C"
    @SerializedName("humedadmaxima")
    val maxHum: String, // "80" o "80 %"
    @SerializedName("temperaturaminima")
    val minTemp: String, // "32.4" o "32.4 C"
    @SerializedName("fecha")
    val date: String // yyyy-mm-dd
)

data class Form7RequestDto(
    @SerializedName("clima")
    val weather: String,
    @SerializedName("temporada")
    val season: String,
    @SerializedName("zona")
    val zone: String,
    @SerializedName("pluviosidad")
    val pluviosity: String, // "23.54" o "23.54 mm"
    @SerializedName("temperaturamaxima")
    val maxTemp: String, // "35.6" o "35.6 C"
    @SerializedName("humedadmaxima")
    val maxHum: String, // "80" o "80 %"
    @SerializedName("temperaturaminima")
    val minTemp: String, // "32.4" o "32.4 C"
    @SerializedName("nivelquebrada")
    val ravineLevel: String, // "12" o "12 mt"
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("fecha")
    val date: String // yyyy-mm-dd
)

data class SubmissionResponseDto(
    @SerializedName("message")
    val message: String,
    @SerializedName("tenant")
    val tenant: String,
    @SerializedName("formKey")
    val formKey: String,
    @SerializedName("data")
    val data: JsonObject?
)