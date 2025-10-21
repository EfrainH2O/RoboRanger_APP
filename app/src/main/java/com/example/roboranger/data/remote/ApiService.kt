package com.example.roboranger.data.remote

import com.example.roboranger.data.remote.dto.AuthRequestDto
import com.example.roboranger.data.remote.dto.AuthResponseDto
import com.example.roboranger.data.remote.dto.Form1RequestDto
import com.example.roboranger.data.remote.dto.Form7RequestDto
import com.example.roboranger.data.remote.dto.SubmissionResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    // Auth
    @Headers("No-Auth: true") // evita usar bearer en login mientras que apiKey es global
    @POST("users/login")
    suspend fun signIn(@Body request: AuthRequestDto): Response<AuthResponseDto>

    @POST("users/logout")
    suspend fun signOut(): Response<Unit>

    // Forms
    @POST("forms/{formKey}/submission")
    suspend fun submitForm1(
        @Path("formKey") formKey: String = "1",
        @Body body: Form1RequestDto
    ) : Response<SubmissionResponseDto>

    @POST("forms/{formKey}/submission")
    suspend fun submitForm7(
        @Path("formKey") formKey: String = "7",
        @Body body: Form7RequestDto
    ) : Response<SubmissionResponseDto>
}