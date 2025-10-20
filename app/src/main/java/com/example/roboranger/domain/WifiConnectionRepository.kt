package com.example.roboranger.domain

import kotlinx.coroutines.flow.Flow

/**
 * Representa el estado de la conexión a la red del robot.
 * Se accede desde el ViewModel y el Repositorio actual.
 */
sealed interface ConnectionState {
    object Idle : ConnectionState
    object Searching : ConnectionState
    data class Connected(val deviceName: String) : ConnectionState
    data class Error(val message: String) : ConnectionState
}

/**
 * Interfaz para el repositorio de conexiones a redes Wi-Fi.
 */
interface WifiConnectionRepository {


    /**
     * Un flujo que emite el estado de la conexión a la red.
     * ViewModels y Repositorios pueden suscribirse para recibir actualizaciones.
     */
    val connectionState: Flow<ConnectionState>

    /**
     * Se conecta a una red Wifi específica usando un NetworkRequest.
     * Se usa para conectar dispositivos IoT.
     * @param ssid El nombre de la red Wifi (SSID) a la que conectarse.
     * @param passphrase La contraseña de la red. Puede estar vacia para redes abiertas.
     * @return Un flujo que emite el estado de la conexión a la red.
     */
    fun connectToRobotWifi(ssid: String, passphrase: String)

    /**
     * Desconecta manualmente la app de la red actual.
     */
    fun disconnect()

}
