package com.example.roboranger.ui.views.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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

data class NetworkSearchUiState(
    val networkName: String = "",
    val password: String = "",
    val isOpenNetwork: Boolean = false,
    val connectionState: ConnectionState = ConnectionState.Idle
)

@HiltViewModel
class NetworkSearchViewModel @Inject constructor(
    private val repository: WifiConnectionRepository // Now depends on the interface
) : ViewModel() {

    private val _uiState = MutableStateFlow(NetworkSearchUiState())
    val uiState = _uiState.asStateFlow()

    private var connectionJob: Job? = null

    fun onNetworkNameChange(newName: String) {
        _uiState.update { it.copy(networkName = newName) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun onOpenNetworkChange(isOpen: Boolean) {
        _uiState.update { it.copy(isOpenNetwork = isOpen, password = if (isOpen) "" else it.password) }
    }

    fun searchAndConnect() {
        if (_uiState.value.connectionState is ConnectionState.Searching) return

        val isInvalid = _uiState.value.networkName.isBlank() ||
                (!_uiState.value.isOpenNetwork && _uiState.value.password.isBlank())

        if (isInvalid) {
            _uiState.update { it.copy(connectionState = ConnectionState.Error("Network name is required. Password is required for secure networks.")) }
            return
        }

        connectionJob?.cancel()

        val nameToSearch = _uiState.value.networkName
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
        _uiState.update { NetworkSearchUiState() }
    }
}