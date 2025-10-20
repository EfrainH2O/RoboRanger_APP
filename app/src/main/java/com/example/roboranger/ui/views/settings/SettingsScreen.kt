package com.example.roboranger.ui.views.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.roboranger.R
import com.example.roboranger.data.local.TokenManager
import com.example.roboranger.ui.components.RoboRangerTopAppBar
import com.example.roboranger.navigation.NavigationDestination
import com.example.roboranger.ui.components.RoboRangerLastConnectionCard
import com.example.roboranger.ui.views.auth.AuthViewModel
import com.example.roboranger.ui.views.network.NetworkSearchViewModel
import androidx.compose.runtime.collectAsState
import com.example.roboranger.ui.components.ReconnectDialog
import com.example.roboranger.ui.views.network.ConnectionState

object SettingsDestination : NavigationDestination {
    override val route = "settings"
    override val titleRes = R.string.settings
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateUp: () -> Unit,
    navigateToNetworkSearch : () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel(),
    networkSearchViewModel: NetworkSearchViewModel  = hiltViewModel(),
    canNavigateBack: Boolean = true,
    canNavigateSettings: Boolean = false,
) {

    val networkUiState by networkSearchViewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // The dialog is now part of the screen, and its visibility is controlled by the ViewModel state.
    ReconnectDialog(
        status = networkUiState.reconnectionStatus,
        onDismiss = { networkSearchViewModel.dismissReconnectDialog() }
    )

    Scaffold(
        topBar = {
            RoboRangerTopAppBar(
                title = stringResource(SettingsDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior,
                canNavigateSettings = canNavigateSettings,
                navigateToSettings = {}
            )
        }
    ) { innerPadding ->
        SettingsBody(
            modifier = Modifier.padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            onLogOut = { authViewModel.signOut() },
            navigateToNetworkSearch = navigateToNetworkSearch
        )
    }
}

@Composable
fun SettingsBody(
    modifier: Modifier = Modifier,
    onLogOut: () -> Unit,
    networkSearchViewModel: NetworkSearchViewModel = hiltViewModel(),
    navigateToNetworkSearch: () -> Unit
) {

    // Importar directamente username y email del usuario desde DataStore (version antes de SettingsViewModel)
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context.applicationContext) }
    val username by tokenManager.username.collectAsStateWithLifecycle(initialValue = null)
    val userEmail by tokenManager.userEmail.collectAsStateWithLifecycle(initialValue = null)

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))

        // Avatar circular grande
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFCDE4B4)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.AccountCircle,
                contentDescription = null,
                tint = Color(0xFF4E7029),
                modifier = Modifier.size(88.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        // Nombre del usuario
        Text(
            text = username ?: stringResource(R.string.placeholder_username),
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF2C2C2C),
            textAlign = TextAlign.Center
        )

        // Email del usuario
        Spacer(Modifier.height(6.dp))
        Text(
            text = userEmail ?: stringResource(R.string.placeholder_user_email),
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF7A7A7A),
            textAlign = TextAlign.Center
        )

        val networkUiState by networkSearchViewModel.uiState.collectAsState()
        val connectedDeviceName = (networkUiState.connectionState as? ConnectionState.Connected)?.deviceName
        val isConnectedToLastSsid = connectedDeviceName == networkUiState.lastConnectedSsid

        // Only show the card if there is a saved network
        if (networkUiState.lastConnectedSsid.isNotBlank()) {
            val descriptor = if (isConnectedToLastSsid) {
                "Connected network"
            } else {
                "Last connected network"
            }

            // Use the new and improved card component
            RoboRangerLastConnectionCard(
                lastSsid = networkUiState.lastConnectedSsid,
                descriptor = descriptor,
                isConnected = isConnectedToLastSsid,
                onConnectClicked = { networkSearchViewModel.reconnectToLastNetwork() },
                onDisconnectClicked = { networkSearchViewModel.disconnect() }
            )
        }

        // Boton de Emparejar Robot, navega a vista correspondiente
        Spacer(Modifier.height(6.dp))
        Button(
            onClick = navigateToNetworkSearch,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4E7029),
                contentColor = Color.White
            )
        ) {
            Text(
                text = stringResource(R.string.action_pair_robot),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Boton de Cerrar Sesion
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = onLogOut,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4E7029),
                contentColor = Color.White
            )
        ) {
            Text(
                text = stringResource(R.string.action_logout),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}