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
      val body = response.body()!!
      val token = body.token

      // Guardar el token con dataStore
      tokenManager.saveToken(token)

      // Guardar datos basicos de usuario para settings
      tokenManager.saveUser(
        username = body.user.username,
        userEmail = body.user.userEmail
      )
    } else {
      throw Exception("Credenciales invalidas o error en el servidor")
    }
  }

  // Cerrar sesion llamando al endpoint y si es exitoso el request, borra el token y datos de usuario
  // de DataStore, mientras que si falla, lanza exception
  suspend fun signOut() {
    val response = apiService.signOut()
    if (response.isSuccessful) {
      // Borrar el token
      tokenManager.deleteToken()
      // Borrar datos de usuario
      tokenManager.clearUser()
    } else {
      throw Exception("Error al cerrar sesion")
    }
  }

  // Obtener el token de autenticacion guardado en dataStore para observar cambios en capa superior si
  // se requiere
  fun getAuthToken(): Flow<String?> = tokenManager.token

  // Obtener el nombre y correo del usuario guardados en dataStore para visualizarlos en capa superior,
  // En este caso, estos datos se mostraran en la vista de settings
  fun getUserName(): Flow<String?> = tokenManager.username
  fun getUserEmail(): Flow<String?> = tokenManager.userEmail

  // Checar dentro del viewmodel si el token aun esta persistido dentro de la aplicacion
  suspend fun isAuthenticated(): Boolean = (tokenManager.token.firstOrNull()?.isNotBlank() == true)
}