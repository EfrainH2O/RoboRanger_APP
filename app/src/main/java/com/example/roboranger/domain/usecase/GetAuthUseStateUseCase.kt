package com.example.roboranger.domain.usecase

import com.example.roboranger.data.ApiRepository
import com.example.roboranger.domain.model.AuthState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAuthUseStateUseCase(private val repo: ApiRepository) {
    operator fun invoke(): Flow<AuthState> {
        return repo.getAuthToken().map { token ->
            if (!token.isNullOrBlank()) AuthState.Authenticated else AuthState.Unauthenticated
        }
    }
}