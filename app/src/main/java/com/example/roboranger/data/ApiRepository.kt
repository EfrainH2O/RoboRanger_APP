package com.example.roboranger.data

import com.example.roboranger.data.local.TokenManager
import com.example.roboranger.data.remote.ApiService
import com.example.roboranger.data.remote.dto.AuthRequestDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class ApiRepository(
  private val apiService: ApiService,
  private val tokenManager: TokenManager
) {
  // Inicio de sesion enviando AuthRequestDto(user_email, password) y si es exitoso el request, guarda
  // solo el token en DataStore, mientras que si falla, lanza Exception
  suspend fun signIn(
    userEmail: String,
    password: String
  ) {
    val response = apiService.signIn(AuthRequestDto(userEmail = userEmail, password = password))
    if (response.isSuccessful && response.body() != null) {
      val token = response.body()!!.token
      tokenManager.saveToken(token) // Guardar el token con dataStore
    } else {
      throw Exception("Credenciales invalidas o error en el servidor")
    }
  }

  // Cerrar sesion llamando al endpoint y si es exitoso el request, borra el token de DataStore, mientras
  // que si falla, lanza exception
  suspend fun signOut() {
    val response = apiService.signOut()
    if (response.isSuccessful) {
      tokenManager.deleteToken()
    } else {
      throw Exception("Error al cerrar sesion")
    }
  }

  // Obtener el token de autenticacion guardado en dataStore para observar cambios en capa superior si
  // se requiere
  fun getAuthToken(): Flow<String?> = tokenManager.token

  // Checar dentro del viewmodel si el token aun esta persistido dentro de la aplicacion
  suspend fun isAuthenticated(): Boolean = (tokenManager.token.firstOrNull()?.isNotBlank() == true)
}