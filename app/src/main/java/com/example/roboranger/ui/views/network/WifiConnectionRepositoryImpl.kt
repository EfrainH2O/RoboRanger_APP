package com.example.roboranger.ui.views.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiNetworkSuggestion
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class WifiConnectionRepositoryImpl @Inject constructor (
    @ApplicationContext private val context: Context
) : WifiConnectionRepository {

    private val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * Suggests a Wi-Fi network to the system. This is the recommended
     * approach for Android 10 (Q) and above.
     */
    override fun connectToRobotWifi(ssid: String, passphrase: String) {
        // --- REPOSITORY CORRECTION: HANDLE OPEN VS. SECURE NETWORKS ---
        // This logic now checks if the passphrase is empty.
        // - If it's empty, it builds a suggestion for an OPEN network.
        // - Otherwise, it builds a suggestion for a WPA2/WPA3 network.
        val suggestion = if (passphrase.isEmpty()) {
            WifiNetworkSuggestion.Builder()
                .setSsid(ssid)
                .build() // No passphrase method called for open networks
        } else {
            WifiNetworkSuggestion.Builder()
                .setSsid(ssid)
                .setWpa2Passphrase(passphrase)
                .build()
        }

        val suggestions = listOf(suggestion)
        val status = wifiManager.addNetworkSuggestions(suggestions)

        if (status != WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
            throw Exception("Failed to add network suggestion. Error code: $status")
        }
    }

    /**
     * Observes the currently connected Wi-Fi network's SSID.
     * Emits the SSID name when a connection is available and null when disconnected.
     */
    override fun observeCurrentWifiConnection(): Flow<String?> = callbackFlow {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            fun getConnectedSsid(): String? {
                val caps = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true) {
                    val wifiInfo = wifiManager.connectionInfo
                    // SSID can be <unknown ssid> if location is off, or quoted.
                    return wifiInfo.ssid?.takeIf { it != "<unknown ssid>" }?.removeSurrounding("\"")
                }
                return null
            }

            override fun onAvailable(network: Network) {
                trySend(getConnectedSsid())
            }

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                trySend(getConnectedSsid())
            }

            override fun onLost(network: Network) {
                trySend(null)
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        // Send the initial state
        trySend(networkCallback.getConnectedSsid())

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }
}
