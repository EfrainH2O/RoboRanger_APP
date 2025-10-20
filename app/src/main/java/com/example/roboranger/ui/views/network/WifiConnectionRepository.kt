package com.example.roboranger.ui.views.network

import kotlinx.coroutines.flow.Flow

/**
 * Represents the possible states of a Wi-Fi connection attempt.
 * This sealed interface is now defined here to be accessible by both the
 * repository and the ViewModel, improving layer separation.
 */
sealed interface ConnectionState {
    object Idle : ConnectionState
    object Searching : ConnectionState
    data class Connected(val deviceName: String) : ConnectionState
    data class Error(val message: String) : ConnectionState
}

/**
 * Interface for managing Wi-Fi connections to the robot.
 * This abstraction allows for easier testing and swapping of implementations.
 */
interface WifiConnectionRepository {

    /**
     * Connects to a specific Wi-Fi network using a NetworkRequest.
     * This is the recommended approach for specific, direct connections to devices like IoT hardware.
     * @param ssid The network name (SSID) to connect to.
     * @param passphrase The password for the network. Should be empty for an open network.
     * @return A Flow that emits the current state of the connection attempt.
     */
    fun connectToRobotWifi(ssid: String, passphrase: String): Flow<ConnectionState>

    /**
     * Manually disconnects the app from the current robot Wi-Fi network.
     */
    fun disconnect()

    /**
     * Observes the currently connected Wi-Fi network's SSID.
     * @return A Flow that emits the SSID name when connected, or null otherwise.
     */
    fun observeCurrentWifiConnection(): Flow<String?>
}
