package com.example.roboranger.domain.usecase

import com.example.roboranger.data.remote.dto.Form1RequestDto
import com.example.roboranger.data.remote.dto.SubmissionResponseDto
import com.example.roboranger.domain.FormsRepository

class SubmitForm1UseCase(private val repo: FormsRepository) {
    suspend operator fun invoke(imageBytes: ByteArray, metaData: Form1RequestDto): SubmissionResponseDto {
        return repo.submitForm1(imageBytes, metaData)
    }
}