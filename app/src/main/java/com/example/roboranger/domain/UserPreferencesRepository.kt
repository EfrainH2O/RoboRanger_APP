package com.example.roboranger.domain

import kotlinx.coroutines.flow.Flow

/**
 * Una clase de datos que representa las credenciales de la última red conectada.
 */
data class LastConnectedNetwork(val ssid: String, val password: String)

/**
 * Interfaz para el repositorio de preferencias de usuario.
 */
interface UserPreferencesRepository {

    /**
     * Un flujo que emite las credenciales de la última red conectada.
     */
    val lastConnectedNetwork: Flow<LastConnectedNetwork>

    /**
     * Salva las credenciales de la última red conectada.
     * @param ssid El nombre de la red Wifi (SSID) conectada.
     * @param password La contraseña de la red conectada.
     */
    suspend fun saveLastConnectedNetwork(ssid: String, password: String)
}

