package com.example.roboranger.ui.views.network

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roboranger.data.UserPreferencesRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

data class NetworkSearchUiState(
    val networkName: String = "",
    val password: String = "",
    val isOpenNetwork: Boolean = false,
    val connectionState: ConnectionState = ConnectionState.Idle,
    val lastConnectedSsid: String = "",
    val lastConnectedPassword: String = "",
    val reconnectionStatus: ReconnectionStatus = ReconnectionStatus.Idle
)

// New sealed interface for the reconnect dialog's state
sealed interface ReconnectionStatus {
    object Idle : ReconnectionStatus
    object InProgress : ReconnectionStatus
    object Success : ReconnectionStatus
    data class Failed(val message: String) : ReconnectionStatus
}

@HiltViewModel
class NetworkSearchViewModel @Inject constructor(
    private val repository: WifiConnectionRepository,
    private val userPreferencesRepository: UserPreferencesRepository // New dependency
) : ViewModel() {

    private val _uiState = MutableStateFlow(NetworkSearchUiState())
    val uiState = _uiState.asStateFlow()

    private var connectionJob: Job? = null

    init {
        viewModelScope.launch {
            // Now collects the network object with both SSID and password
            userPreferencesRepository.lastConnectedNetwork.collect { network ->
                _uiState.update {
                    it.copy(
                        lastConnectedSsid = network.ssid,
                        lastConnectedPassword = network.password
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

    fun onIsOpenNetworkChange(isOpen: Boolean) {
        _uiState.update { it.copy(isOpenNetwork = isOpen, password = "") }
    }

    fun onSearchClicked() {
        if (_uiState.value.networkName.isBlank()) {
            _uiState.update {
                it.copy(connectionState = ConnectionState.Error("Network name cannot be empty."))
            }
            return
        }

        connectionJob?.cancel()

        val nameToSearch = _uiState.value.networkName
        val robotPassword = if (_uiState.value.isOpenNetwork) "" else _uiState.value.password

        connectionJob = viewModelScope.launch {
            repository.connectToRobotWifi(nameToSearch, robotPassword)
                .collect { state ->
                    _uiState.update { it.copy(connectionState = state) }
                    // When a connection is successful, save the network name.
                    if (state is ConnectionState.Connected) {
                        userPreferencesRepository.saveLastConnectedNetwork(state.deviceName, robotPassword)
                    }
                }
        }
    }

    /*
    * Tries to reconnect to the last saved network, showing a dialog for feedback.
    */
    fun reconnectToLastNetwork() {
        val lastSsid = _uiState.value.lastConnectedSsid
        val lastPassword = _uiState.value.lastConnectedPassword // Use the saved password
        if (lastSsid.isBlank()) {
            _uiState.update { it.copy(reconnectionStatus = ReconnectionStatus.Failed("No saved network found.")) }
            return
        }
        connectionJob?.cancel()
        connectionJob = viewModelScope.launch {
            _uiState.update { it.copy(reconnectionStatus = ReconnectionStatus.InProgress) }
            try {
                // Set a 15-second timeout for the connection attempt
                withTimeout(15_000L) {
                    // Pass the saved password to the connection function
                    repository.connectToRobotWifi(lastSsid, lastPassword).collect { state ->
                        _uiState.update { it.copy(connectionState = state) }
                        if (state is ConnectionState.Connected) {
                            _uiState.update { it.copy(reconnectionStatus = ReconnectionStatus.Success) }
                            userPreferencesRepository.saveLastConnectedNetwork(state.deviceName, lastPassword)
                        }
                        if (state is ConnectionState.Error) {
                            _uiState.update { it.copy(reconnectionStatus = ReconnectionStatus.Failed(state.message)) }
                        }
                    }
                }
            } catch (e: TimeoutCancellationException) {
                Log.e("ViewModel", "Reconnect timed out.")
                disconnect() // Clean up on timeout
                _uiState.update { it.copy(reconnectionStatus = ReconnectionStatus.Failed("Connection timed out.")) }
            }
        }
    }

    /**
     * Disconnects from the current network and cancels any ongoing connection attempts.
     */
    fun disconnect() {
        connectionJob?.cancel()
        repository.disconnect()
        _uiState.update { it.copy(connectionState = ConnectionState.Idle) }
    }

    /**
     * Dismisses the reconnect dialog by resetting its state.
     */
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
        _uiState.update { it.copy(networkName = "", password = "", isOpenNetwork = false, connectionState = ConnectionState.Idle) }
    }
}

