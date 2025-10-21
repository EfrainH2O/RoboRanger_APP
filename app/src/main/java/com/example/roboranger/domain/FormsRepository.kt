package com.example.roboranger.domain

import com.example.roboranger.data.remote.dto.Form1RequestDto
import com.example.roboranger.data.remote.dto.Form7RequestDto
import com.example.roboranger.data.remote.dto.SubmissionResponseDto

interface FormsRepository {
    suspend fun submitForm1(body: Form1RequestDto) : SubmissionResponseDto
    suspend fun submitForm7(body: Form7RequestDto) : SubmissionResponseDto
}