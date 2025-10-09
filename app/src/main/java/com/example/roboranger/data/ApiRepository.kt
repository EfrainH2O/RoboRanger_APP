package com.example.roboranger.data

import com.example.roboranger.data.local.TokenManager
import com.example.roboranger.data.remote.ApiService
import com.example.roboranger.data.remote.AuthRequest
import kotlinx.coroutines.flow.Flow

class ApiRepository(
  private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
  // Inicio de sesion
  suspend fun signIn(
    userEmail: String,
    password: String
  ) {
    val response = apiService.signIn(AuthRequest(userEmail, password))
    if (response.isSuccessful && response.body() != null) {
      val token = response.body()!!.token
      tokenManager.saveToken(token) // Guardar el token con dataStore
    } else {
      throw Exception("Credenciales invalidas o error en el servidor")
    }
  }

  // Cerrar sesion
  suspend fun signOut() {
    val response = apiService.signOut()
    if (response.isSuccessful) {
      tokenManager.deleteToken()
    } else {
      throw Exception("Error al cerrar sesion")
    }
  }

  // Obtener el token de autenticacion guardado en dataStore
  fun getAuthToken(): Flow<String?> {
    return tokenManager.token
  }
}