package com.example.roboranger.ui.views.network

import android.Manifest
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roboranger.R
import com.example.roboranger.navigation.NavigationDestination
import com.example.roboranger.ui.components.LockScreenOrientation
import com.example.roboranger.ui.components.RoboRangerBottomAppBar
import com.example.roboranger.ui.components.RoboRangerTopAppBar
import com.example.roboranger.ui.views.home.HomeBody
import com.example.roboranger.ui.views.home.HomeDestination
import kotlinx.coroutines.delay

object NetworkSearchDestination : NavigationDestination {
    override val route = "network_search"
    override val titleRes = R.string.network_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkSearchScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    navigateToHome: () -> Unit,
    networkSearchViewModel: NetworkSearchViewModel,
    canNavigateBack: Boolean = true,
    canNavigateSettings: Boolean = false,
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            RoboRangerTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                canNavigateSettings = canNavigateSettings,
            )
        },
    ) { innerPadding ->
        NetworkSearchBody(
            modifier = modifier.padding(innerPadding),
            navigateToHome = navigateToHome,
            viewModel = networkSearchViewModel,
        )
    }
}
@Composable
fun NetworkSearchBody(
    modifier: Modifier,
    navigateToHome: () -> Unit,
    viewModel: NetworkSearchViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // --- COLOR PALETTE DEFINITION ---
    val primaryGreen = Color(0xFF81C784) // A pleasant, medium green
    val darkGreenText = Color(0xFF388E3C) // A darker green for success text
    val darkCharcoal = Color(0xFF333333) // Dark text color for readability

    LaunchedEffect(uiState.connectionState) {
        if (uiState.connectionState is ConnectionState.Connected) {
            delay(1500)
            navigateToHome()
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                viewModel.searchAndConnect()
            } else {
                viewModel.onPermissionDenied()
            }
        }
    )

    val searchAction = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            viewModel.searchAndConnect()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Connect to a Robot",
            style = MaterialTheme.typography.headlineMedium,
            color = darkCharcoal,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = uiState.networkName,
            onValueChange = viewModel::onNetworkNameChange,
            label = { Text("Robot Name (SSID)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = uiState.connectionState !is ConnectionState.Searching,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = primaryGreen,
                focusedLabelColor = primaryGreen,
                cursorColor = primaryGreen,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(visible = !uiState.isOpenNetwork) {
            OutlinedTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                enabled = uiState.connectionState !is ConnectionState.Searching,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = primaryGreen,
                    focusedLabelColor = primaryGreen,
                    cursorColor = primaryGreen,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                )
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
                onCheckedChange = viewModel::onOpenNetworkChange,
                enabled = uiState.connectionState !is ConnectionState.Searching,
                colors = CheckboxDefaults.colors(
                    checkedColor = primaryGreen
                )
            )
            Text(text = "This is an open network (no password)")
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
                containerColor = primaryGreen,
                contentColor = Color.White
            )
        ) {
            val buttonText = when (uiState.connectionState) {
                is ConnectionState.Connected -> "Search for another"
                is ConnectionState.Error -> "Try Again"
                else -> "Search & Connect"
            }
            Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
            Text(buttonText)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(modifier = Modifier.height(80.dp), contentAlignment = Alignment.Center) {
            when (val state = uiState.connectionState) {
                is ConnectionState.Searching -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = primaryGreen)
                        Text("Searching...", modifier = Modifier.padding(top = 8.dp))
                    }
                }
                is ConnectionState.Connected -> {
                    StatusMessage(
                        text = "Successfully connected to ${state.deviceName}!",
                        icon = Icons.Default.CheckCircle,
                        color = darkGreenText
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
                    Text("Enter robot name to begin.")
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

@Preview(showBackground = true)
@Composable
fun NetworkSearchPreview() {
    MaterialTheme {

    }
}

