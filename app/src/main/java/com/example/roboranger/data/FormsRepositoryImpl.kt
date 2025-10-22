package com.example.roboranger.data

import com.example.roboranger.data.remote.ApiService
import com.example.roboranger.data.remote.dto.Form1RequestDto
import com.example.roboranger.data.remote.dto.Form7RequestDto
import com.example.roboranger.data.remote.dto.SubmissionResponseDto
import com.example.roboranger.domain.FormsRepository
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class FormsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val gson: Gson
) : FormsRepository {

    // Subir a la nube el formulario 1 llamando al endpoint y si es exitoso el request, se recibe respuesta
    // del servidor, mientras que si falla, lanza exception
    override suspend fun submitForm1(imageBytes: ByteArray, metaData: Form1RequestDto) : SubmissionResponseDto {
        // Convertir los metadatos (DTO) a Json y luego a RequestBody
        val metaDataJson = gson.toJson(metaData)
        val metaDataPart = metaDataJson.toRequestBody("application/json".toMediaType())

        // Crear MultipartBody.Part para la imagen
        val imagePart = imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull()).let { requestBody ->
            MultipartBody.Part.createFormData("image", "image.jpg", requestBody)
        }

        // Realizar la llamada al endpoint de la API con las partes construidas
        val response = apiService.submitForm1(image = imagePart, metaData = metaDataPart)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Respuesta vacia del servidor")
        } else {
            throw Exception("Error ${response.code()}: ${response.message()}")
        }
    }

    // Subir a la nube el formulario 7 llamando al endpoint y si es exitoso el request, se recibe respuesta
    // del servidor, mientras que si falla, lanza exception
    override suspend fun submitForm7(imageBytes: ByteArray, metaData: Form7RequestDto) : SubmissionResponseDto {
        // Convertir los metadatos (DTO) a Json y luego a RequestBody
        val metaDataJson = gson.toJson(metaData)
        val metaDataPart = metaDataJson.toRequestBody("application/json".toMediaType())

        // Crear MultipartBody.Part para la imagen
        val imagePart = imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull()).let { requestBody ->
            MultipartBody.Part.createFormData("image", "image.jpg", requestBody)
        }

        // Realizar la llamada al endpoint de la API con las partes construidas
        val response = apiService.submitForm1(image = imagePart, metaData = metaDataPart)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Respuesta vacia del servidor")
        } else {
            throw Exception("Error ${response.code()}: ${response.message()}")
        }
    }
}