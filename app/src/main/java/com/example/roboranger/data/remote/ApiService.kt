package com.example.roboranger.data.remote

import com.example.roboranger.data.remote.dto.AuthRequestDto
import com.example.roboranger.data.remote.dto.AuthResponseDto
import com.example.roboranger.data.remote.dto.Form1RequestDto
import com.example.roboranger.data.remote.dto.Form7RequestDto
import com.example.roboranger.data.remote.dto.SubmissionResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    // Auth
    @Headers("No-Auth: true") // evita usar bearer en login mientras que apiKey es global
    @POST("users/login")
    suspend fun signIn(@Body request: AuthRequestDto): Response<AuthResponseDto>

    @POST("users/logout")
    suspend fun signOut(): Response<Unit>

    // Forms
    @Multipart
    @POST("forms/{formKey}/submission")
    suspend fun submitForm1(
        @Path("formKey") formKey: String = "1",
        @Part image: MultipartBody.Part,
        @Part("metadata") metaData: RequestBody
    ) : Response<SubmissionResponseDto>

    @POST("forms/{formKey}/submission")
    suspend fun submitForm7(
        @Path("formKey") formKey: String = "7",
        @Part image: MultipartBody.Part,
        @Part("metadata") metaData: RequestBody
    ) : Response<SubmissionResponseDto>
}