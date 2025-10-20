package com.example.roboranger.ui.views.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class WifiConnectionRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : WifiConnectionRepository {

    private val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    companion object {
        private const val TAG = "WifiConnectionRepo"
    }

    /**
     * Connects to a robot's Wi-Fi network using NetworkRequest and WifiNetworkSpecifier.
     * This method is designed for Android 10 (Q) and above.
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun connectToRobotWifi(ssid: String, passphrase: String): Flow<ConnectionState> =
        callbackFlow {
            trySend(ConnectionState.Searching)
            Log.d(TAG, "Attempting to connect to SSID: $ssid")

            val specifierBuilder = WifiNetworkSpecifier.Builder().setSsid(ssid)

            if (passphrase.isNotEmpty()) {
                specifierBuilder.setWpa2Passphrase(passphrase)
            }

            val specifier = specifierBuilder.build()

            val request = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .setNetworkSpecifier(specifier)
                .build()

            networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Log.d(TAG, "✅ Network available! Successfully connected to $ssid.")
                    connectivityManager.bindProcessToNetwork(network)
                    trySend(ConnectionState.Connected(ssid))
                    // Do NOT close the channel here, to keep the connection alive.
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    Log.e(TAG, "❌ Network unavailable. Could not connect to $ssid.")
                    trySend(ConnectionState.Error("Failed to connect. Please check the name and password."))
                    channel.close() // Close on failure.
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    Log.w(TAG, "⚠️ Connection to the network was lost.")
                    trySend(ConnectionState.Error("Connection to the network was lost."))
                    channel.close() // Close on connection loss.
                }
            }

            connectivityManager.requestNetwork(request, networkCallback!!)

            awaitClose {
                Log.d(TAG, "✅ Temporary connection for $ssid removed.")
                networkCallback?.let {
                    connectivityManager.unregisterNetworkCallback(it)
                    connectivityManager.bindProcessToNetwork(null)
                }
            }
        }

    /**
     * Manually unregisters the network callback and unbinds the process from the network.
     */
    override fun disconnect() {
        Log.d(TAG, "Disconnect requested.")
        networkCallback?.let {
            try {
                connectivityManager.unregisterNetworkCallback(it)
                Log.d(TAG, "Network callback unregistered.")
            } catch (e: IllegalArgumentException) {
                Log.w(TAG, "Network callback was not registered or already unregistered.")
            }
        }
        connectivityManager.bindProcessToNetwork(null)
        Log.d(TAG, "Process unbound from network.")
        networkCallback = null
    }


    override fun observeCurrentWifiConnection(): Flow<String?> = callbackFlow {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            fun getConnectedSsid(): String? {
                val caps =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true) {
                    val wifiInfo = wifiManager.connectionInfo
                    return wifiInfo.ssid?.takeIf { it != "<unknown ssid>" }
                }
                return null
            }

            override fun onAvailable(network: Network) {
                trySend(getConnectedSsid())
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
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

        trySend(networkCallback.getConnectedSsid())

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }
}

