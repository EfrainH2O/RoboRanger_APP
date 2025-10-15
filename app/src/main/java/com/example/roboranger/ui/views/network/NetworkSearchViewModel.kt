package com.example.roboranger.ui.views.network

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface ConnectionState {
    object Idle : ConnectionState
    object Searching : ConnectionState
    data class Connected(val deviceName: String) : ConnectionState
    data class Error(val message: String) : ConnectionState
}

// --- CORRECTION: ADD STATE FOR OPEN NETWORKS ---
// The state now includes a boolean to track if the network is open (no password).
data class NetworkSearchUiState(
    val networkName: String = "",
    val password: String = "",
    val isOpenNetwork: Boolean = false,
    val connectionState: ConnectionState = ConnectionState.Idle
)

class NetworkSearchViewModel(context: Context) : ViewModel() {

    private val _uiState = MutableStateFlow(NetworkSearchUiState())
    val uiState = _uiState.asStateFlow()

    private val repository = WifiConnectionRepository(context)
    private var connectionJob: Job? = null

    fun onNetworkNameChange(newName: String) {
        _uiState.update { it.copy(networkName = newName) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    // --- NEW: HANDLER FOR OPEN NETWORK STATE ---
    fun onOpenNetworkChange(isOpen: Boolean) {
        // Clear password if user marks network as open
        _uiState.update { it.copy(isOpenNetwork = isOpen, password = if (isOpen) "" else it.password) }
    }


    fun searchAndConnect() {
        if (_uiState.value.connectionState is ConnectionState.Searching) return

        // --- CORRECTION: UPDATED VALIDATION LOGIC ---
        // Now, it only requires a password if the network is not marked as open.
        val isInvalid = _uiState.value.networkName.isBlank() ||
                (!_uiState.value.isOpenNetwork && _uiState.value.password.isBlank())

        if (isInvalid) {
            _uiState.update { it.copy(connectionState = ConnectionState.Error("Network name is required. Password is required for secure networks.")) }
            return
        }

        connectionJob?.cancel()

        val nameToSearch = _uiState.value.networkName
        // Send an empty string for the password if it's an open network
        val robotPassword = if (_uiState.value.isOpenNetwork) "" else _uiState.value.password

        viewModelScope.launch {
            _uiState.update { it.copy(connectionState = ConnectionState.Searching) }
            try {
                repository.connectToRobotWifi(nameToSearch, robotPassword)
                monitorConnection(nameToSearch)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(connectionState = ConnectionState.Error(e.message ?: "Failed to suggest network."))
                }
            }
        }
    }

    private fun monitorConnection(expectedSsid: String) {
        connectionJob = viewModelScope.launch {
            repository.observeCurrentWifiConnection().collectLatest { currentSsid ->
                // Ensure the connected SSID matches exactly, removing quotes
                if (currentSsid?.removeSurrounding("\"") == expectedSsid) {
                    _uiState.update { it.copy(connectionState = ConnectionState.Connected(expectedSsid)) }
                    connectionJob?.cancel()
                }
            }
        }
    }

    fun onPermissionDenied() {
        _uiState.update {
            it.copy(connectionState = ConnectionState.Error("Location permission is required to find Wi-Fi networks."))
        }
    }

    fun resetSearch() {
        connectionJob?.cancel()
        // Reset all fields, including the new isOpenNetwork flag
        _uiState.update { NetworkSearchUiState() }
    }
}

