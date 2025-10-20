package com.example.roboranger.ui.views.network

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roboranger.domain.ConnectionState
import com.example.roboranger.domain.UserPreferencesRepository
import com.example.roboranger.domain.WifiConnectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NetworkSearchUiState(
    val networkName: String = "",
    val password: String = "",
    val isOpenNetwork: Boolean = false,
    val connectionState: ConnectionState = ConnectionState.Idle,
    val lastConnectedSsid: String = "",
    val lastConnectedPassword: String = "",
    val reconnectionStatus: ReconnectionStatus = ReconnectionStatus.Idle
)

sealed interface ReconnectionStatus {
    object Idle : ReconnectionStatus
    object InProgress : ReconnectionStatus
    object Success : ReconnectionStatus
    data class Failed(val message: String) : ReconnectionStatus
}

@HiltViewModel
class NetworkSearchViewModel @Inject constructor(
    private val repository: WifiConnectionRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NetworkSearchUiState())
    val uiState = _uiState.asStateFlow()

    // Variables to hold the credentials for the current connection attempt, preventing race conditions.
    private var ssidForCurrentConnectionAttempt: String = ""
    private var passwordForCurrentConnectionAttempt: String = ""

    // Flag to track if the success/fail popup should be shown.
    private var isReconnectionAttemptInProgress: Boolean = false

    init {
        // Observe the shared connection state from the singleton repository.
        viewModelScope.launch {
            repository.connectionState.collect { state ->
                _uiState.update { it.copy(connectionState = state) }

                if (state is ConnectionState.Connected) {
                    // Upon success, save the credentials that were used for THIS connection attempt.
                    val ssidToSave = ssidForCurrentConnectionAttempt
                    val passwordToSave = passwordForCurrentConnectionAttempt

                    // Sanity check: ensure the connected device matches the one we intended to connect to.
                    if (ssidToSave.isNotEmpty() && ssidToSave == state.deviceName) {
                        Log.d("ViewModelData", "✅ SAVING credentials -> SSID: $ssidToSave, Password: $passwordToSave")
                        userPreferencesRepository.saveLastConnectedNetwork(ssidToSave, passwordToSave)
                    } else if (ssidToSave.isNotEmpty()) {
                        Log.w("ViewModelData", "⚠️ Mismatch on save! Attempted SSID '$ssidToSave' but connected to '${state.deviceName}'. Not saving credentials.")
                    }
                }


                if (isReconnectionAttemptInProgress) {
                    when (state) {
                        is ConnectionState.Connected -> {
                            _uiState.update { it.copy(reconnectionStatus = ReconnectionStatus.Success) }
                            isReconnectionAttemptInProgress = false // Reset the flag
                        }
                        is ConnectionState.Error -> {
                            _uiState.update { it.copy(reconnectionStatus = ReconnectionStatus.Failed(state.message)) }
                            isReconnectionAttemptInProgress = false // Reset the flag
                        }
                        else -> { /* Wait for a final state */ }
                    }
                }

                // Clear temporary credentials if the connection is lost or fails, to prevent outdated saves.
                if (state is ConnectionState.Error || state is ConnectionState.Idle) {
                    ssidForCurrentConnectionAttempt = ""
                    passwordForCurrentConnectionAttempt = ""
                }
            }
        }

        // Check for the last connected network to offer reconnection.
        viewModelScope.launch {
            val lastNetwork = userPreferencesRepository.lastConnectedNetwork.first()
            if (lastNetwork.ssid.isNotEmpty()) {
                Log.d("ViewModelData", "📦 LOADING credentials -> SSID: ${lastNetwork.ssid}, Password: ${lastNetwork.password}")
                _uiState.update {
                    it.copy(
                        lastConnectedSsid = lastNetwork.ssid,
                        lastConnectedPassword = lastNetwork.password
                    )
                }
            }
        }
    }

    fun onNetworkNameChange(newName: String) {
        _uiState.update { it.copy(networkName = newName) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun onOpenNetworkChecked(isChecked: Boolean) {
        _uiState.update {
            it.copy(
                isOpenNetwork = isChecked,
                password = if (isChecked) "" else it.password
            )
        }
    }

    fun connect() {
        val networkName = _uiState.value.networkName
        val password = _uiState.value.password
        // Store the credentials being used for this specific attempt.
        ssidForCurrentConnectionAttempt = networkName
        passwordForCurrentConnectionAttempt = password
        repository.connectToRobotWifi(networkName, password)
    }

    fun reconnectToLastNetwork() {
        val lastSsid = _uiState.value.lastConnectedSsid
        val lastPassword = _uiState.value.lastConnectedPassword
        if (lastSsid.isNotEmpty()) {
            // Set the flag to indicate a reconnection attempt has started.
            isReconnectionAttemptInProgress = true
            _uiState.update {
                it.copy(
                    networkName = lastSsid,
                    password = lastPassword,
                    reconnectionStatus = ReconnectionStatus.InProgress
                )
            }
            // Store the credentials being used for this specific attempt.
            ssidForCurrentConnectionAttempt = lastSsid
            passwordForCurrentConnectionAttempt = lastPassword
            repository.connectToRobotWifi(lastSsid, lastPassword)
        }
    }

    fun disconnect() {
        repository.disconnect()
        // Clear the temporary credential holders on manual disconnect.
        ssidForCurrentConnectionAttempt = ""
        passwordForCurrentConnectionAttempt = ""
        // Also, clear the current search input fields to reset the UI state.
        _uiState.update {
            it.copy(
                networkName = "",
                password = "",
                isOpenNetwork = false
            )
        }
    }

    fun dismissReconnectDialog() {
        _uiState.update { it.copy(reconnectionStatus = ReconnectionStatus.Idle) }
    }

    fun onPermissionDenied() {
        _uiState.update {
            it.copy(connectionState = ConnectionState.Error("Location permission is required to find Wi-Fi networks."))
        }
    }

    fun resetSearch() {
        disconnect()
    }

    // The onCleared method is now empty regarding the connection.
    // This prevents the network from disconnecting when navigating away from the screen.
    override fun onCleared() {
        super.onCleared()
        // The connection now persists beyond this ViewModel's lifecycle.
    }
}

