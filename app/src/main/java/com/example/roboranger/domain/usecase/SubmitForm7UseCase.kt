package com.example.roboranger.domain.usecase

import com.example.roboranger.data.remote.dto.Form7RequestDto
import com.example.roboranger.data.remote.dto.SubmissionResponseDto
import com.example.roboranger.domain.FormsRepository

class SubmitForm7UseCase(private val repo: FormsRepository) {
    suspend operator fun invoke(imageBytes: ByteArray, metaData: Form7RequestDto): SubmissionResponseDto {
        return repo.submitForm7(imageBytes, metaData)
    }
}