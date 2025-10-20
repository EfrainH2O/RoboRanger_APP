package com.example.roboranger.ui.views.network

import android.Manifest
import android.content.pm.ActivityInfo
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roboranger.R
import com.example.roboranger.navigation.NavigationDestination
import com.example.roboranger.ui.components.LockScreenOrientation
import com.example.roboranger.ui.components.RoboRangerTopAppBar
import kotlinx.coroutines.delay
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.roboranger.domain.ConnectionState
import com.example.roboranger.ui.components.RoboRangerPasswordField
import com.example.roboranger.ui.components.RoboRangerTextField
import com.example.roboranger.ui.theme.PrimaryBlack
import com.example.roboranger.ui.theme.PrimaryBushGreen
import com.example.roboranger.ui.theme.PrimaryDarkGreen
import com.example.roboranger.ui.theme.PrimaryGrassGreen


object NetworkSearchDestination : NavigationDestination {
    override val route = "network_search"
    override val titleRes = R.string.network_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkSearchScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    viewModel: NetworkSearchViewModel = hiltViewModel(),
    canNavigateBack: Boolean = true,
    canNavigateSettings: Boolean = false,
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    Scaffold(
        topBar = {
            RoboRangerTopAppBar(
                title = stringResource(NetworkSearchDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                canNavigateSettings = canNavigateSettings,
            )
        },
    ) { innerPadding ->
        NetworkSearchBody(
            modifier = modifier.padding(innerPadding),
            navigate = onNavigateUp,
            viewModel = viewModel
        )
    }
}
@Composable
fun NetworkSearchBody(
    modifier: Modifier,
    navigate: () -> Unit,
    viewModel: NetworkSearchViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.connectionState) {
        if (uiState.connectionState is ConnectionState.Connected) {
            delay(1500)
            navigate()
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                viewModel.connect()
            } else {
                viewModel.onPermissionDenied()
            }
        }
    )

    val searchAction = {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(bottom = 24.dp),
            text = "Conecta un Robot",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            color = PrimaryBlack,
        )

        RoboRangerTextField(
            label = "Nombre (SSID)",
            value = uiState.networkName,
            onValueChange = viewModel::onNetworkNameChange,
            placeholder = "Introduce el nombre (SSID) de la red",

        )

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(visible = !uiState.isOpenNetwork) {
            RoboRangerPasswordField(
                label = "Contraseña",
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                placeholder = "Introduce la contraseña de la red"
            )
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = uiState.isOpenNetwork,
                onCheckedChange = viewModel::onOpenNetworkChecked,
                enabled = uiState.connectionState !is ConnectionState.Searching,
                colors = CheckboxDefaults.colors(
                    checkedColor = PrimaryBushGreen
                )
            )
            Text(text = "Es una red abierta (sin contraseña)")
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                when (uiState.connectionState) {
                    is ConnectionState.Connected, is ConnectionState.Error -> viewModel.resetSearch()
                    else -> searchAction()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState.connectionState !is ConnectionState.Searching,
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBushGreen,
                contentColor = Color.White
            )
        ) {
            val buttonText = when (uiState.connectionState) {
                is ConnectionState.Connected -> "Buscar y Conectar"
                is ConnectionState.Error -> "Reintentar"
                else -> "Buscar y Conectar"
            }
            Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
            Text(buttonText)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(modifier = Modifier.height(80.dp), contentAlignment = Alignment.Center) {
            when (val state = uiState.connectionState) {
                is ConnectionState.Searching -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = PrimaryGrassGreen)
                        Text("Buscando...", modifier = Modifier.padding(top = 8.dp))
                    }
                }
                is ConnectionState.Connected -> {
                    StatusMessage(
                        text = "Se conectó exitosamente a ${state.deviceName}!",
                        icon = Icons.Default.CheckCircle,
                        color = PrimaryDarkGreen
                    )
                }
                is ConnectionState.Error -> {
                    StatusMessage(
                        text = state.message,
                        icon = Icons.Default.Error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is ConnectionState.Idle -> {
                    Text("Enlaza un robot para iniciar.")
                }
            }
        }
    }
}

@Composable
fun StatusMessage(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = color)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = color, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}
