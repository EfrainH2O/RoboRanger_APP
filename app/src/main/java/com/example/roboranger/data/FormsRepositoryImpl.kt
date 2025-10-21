package com.example.roboranger.data

import com.example.roboranger.data.remote.ApiService
import com.example.roboranger.data.remote.dto.Form1RequestDto
import com.example.roboranger.data.remote.dto.Form7RequestDto
import com.example.roboranger.data.remote.dto.SubmissionResponseDto
import com.example.roboranger.domain.FormsRepository
import javax.inject.Inject

class FormsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : FormsRepository {

    // Subir a la nube el formulario 1 llamando al endpoint y si es exitoso el request, se recibe respuesta
    // del servidor, mientras que si falla, lanza exception
    override suspend fun submitForm1(body: Form1RequestDto) : SubmissionResponseDto {
        val response = apiService.submitForm1(body = body)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Respuesta vacia del servidor")
        } else {
            throw Exception("Error ${response.code()}: ${response.message()}")
        }
    }

    // Subir a la nube el formulario 7 llamando al endpoint y si es exitoso el request, se recibe respuesta
    // del servidor, mientras que si falla, lanza exception
    override suspend fun submitForm7(body: Form7RequestDto) : SubmissionResponseDto {
        val response = apiService.submitForm7(body = body)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Respuesta vacia del servidor")
        } else {
            throw Exception("Error ${response.code()}: ${response.message()}")
        }
    }
}