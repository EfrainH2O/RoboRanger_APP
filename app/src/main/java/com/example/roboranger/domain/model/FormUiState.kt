package com.example.roboranger.domain.model

import com.example.roboranger.data.remote.dto.SubmissionResponseDto

sealed class FormUiState {
    data object Idle: FormUiState()
    data object Loading: FormUiState()
    data class Success(val responseDto: SubmissionResponseDto) : FormUiState()
    data class Error(val message: String) : FormUiState()
}