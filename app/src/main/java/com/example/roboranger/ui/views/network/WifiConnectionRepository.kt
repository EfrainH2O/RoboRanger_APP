package com.example.roboranger.ui.views.network

import kotlinx.coroutines.flow.Flow

/**
 * Interface for managing Wi-Fi connections to the robot.
 * This abstraction allows for easier testing and swapping of implementations.
 */
interface WifiConnectionRepository {

    /**
     * Suggests a Wi-Fi network to the system.
     * @param ssid The network name to connect to.
     * @param passphrase The password for the network. Should be empty for an open network.
     */
    fun connectToRobotWifi(ssid: String, passphrase: String)

    /**
     * Observes the currently connected Wi-Fi network's SSID.
     * @return A Flow that emits the SSID name when connected, or null otherwise.
     */
    fun observeCurrentWifiConnection(): Flow<String?>
}