package com.example.roboranger.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiNetworkSpecifier
import android.util.Log
import com.example.roboranger.domain.ConnectionState
import com.example.roboranger.domain.WifiConnectionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Ensure this is a Singleton
class WifiConnectionRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : WifiConnectionRepository {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Idle)
    override val connectionState: Flow<ConnectionState> = _connectionState.asStateFlow()

    override fun connectToRobotWifi(ssid: String, passphrase: String) {
        // Prevent new connections if one is active or in progress
        if (_connectionState.value !is ConnectionState.Idle) {
            Log.d("WifiConnectionRepo", "Already connected or connecting. Call disconnect() first.")
            return
        }

        // Ensure any previous callback is unregistered before starting a new one.
        disconnect()

        _connectionState.value = ConnectionState.Searching

        val specifierBuilder = WifiNetworkSpecifier.Builder().setSsid(ssid)
        if (passphrase.isNotBlank()) {
            specifierBuilder.setWpa2Passphrase(passphrase)
        }

        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .setNetworkSpecifier(specifierBuilder.build())
            .build()

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.d("WifiConnectionRepo", "✅ Network available: $network")
                Log.d("WifiConnectionRepo", "Connecting to $ssid with passphrase $passphrase")
                connectivityManager.bindProcessToNetwork(network)
                _connectionState.value = ConnectionState.Connected(ssid)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                Log.e("WifiConnectionRepo", "❌ Network unavailable.")
                _connectionState.value = ConnectionState.Error("Failed to connect. Network unavailable.")
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.w("WifiConnectionRepo", "Connection to $ssid lost.")
                connectivityManager.bindProcessToNetwork(null)
                // We transition to Idle, allowing for reconnection attempts.
                _connectionState.value = ConnectionState.Idle
            }
        }
        connectivityManager.requestNetwork(request, networkCallback!!)
    }

    override fun disconnect() {
        networkCallback?.let {
            try {
                connectivityManager.bindProcessToNetwork(null)
                connectivityManager.unregisterNetworkCallback(it)
                Log.d("WifiConnectionRepo", "Network callback unregistered.")
            } catch (e: Exception) {
                Log.e("WifiConnectionRepo", "Error unregistering network callback", e)
            } finally {
                networkCallback = null
                _connectionState.value = ConnectionState.Idle
            }
        }
    }
}