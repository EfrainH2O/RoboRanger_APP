package com.example.roboranger.ui.views.control

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.roboranger.R
import com.example.roboranger.data.local.hasLocationPermission
import com.example.roboranger.domain.model.SaveState
import com.example.roboranger.navigation.NavigationDestination
import com.example.roboranger.ui.components.LockScreenOrientation
import com.example.roboranger.ui.components.MovementActionButton
import com.example.roboranger.ui.components.RoboRangerFAB
import com.example.roboranger.ui.components.RoboRangerRoundIconButton
import com.example.roboranger.ui.components.RoboRangerTopAppBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

object ControlDestination : NavigationDestination {
    override val route = "control"
    override val titleRes = R.string.control_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlScreen(
    onNavigateUp: () -> Unit,
    navigateToFormEntry: () -> Unit,
    onNavigateSettings: () -> Unit,
    canNavigateBack: Boolean = true,
    canNavigateSettings: Boolean = true,
    controlViewModel: RobotControlViewModel = hiltViewModel()
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val context = LocalContext.current

    // Streams de estado del viewModel
    val saveState by controlViewModel.savingState.collectAsState()
    val frameBitmap by controlViewModel.videoBitmap.collectAsState()
    val flashlightOn by remember { controlViewModel.lightState } // mutableState<Boolean>
    val vmError by remember { controlViewModel.errorM }           // mutableState<String?>
    var active_ubication_popup by remember {controlViewModel.active_ubication_popup}

    // Arrancar / detener streaming con el ciclo de la pantalla
    DisposableEffect(Unit) {
        controlViewModel.startStreaming()
        onDispose { controlViewModel.stopStreaming() }
    }

    // Toasts por estados de guardado (éxito y error)
    LaunchedEffect(saveState) {
        when (val s = saveState) {
            is SaveState.Success -> {
                Toast.makeText(context, s.savedURI.toString(), Toast.LENGTH_SHORT).show()
                // regresar a Idle tras un pequeño delay para limpiar la barra
                kotlinx.coroutines.delay(1000)
                controlViewModel.resetCameraPhotoState()
            }
            is SaveState.Error -> {
                Toast.makeText(context, s.errorMsg, Toast.LENGTH_SHORT).show()
                controlViewModel.resetCameraPhotoState()
            }
            else -> Unit
        }
    }

    // Toasts por errores generales del viewModel
    LaunchedEffect(vmError) {
        val msg = vmError ?: ""
        if (msg.isNotBlank()) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    // Header: TopBar + barra de progreso si está guardando
    val loadingBar: @Composable () -> Unit = when (saveState) {
        SaveState.Saving -> {
            { LinearProgressIndicator(Modifier.fillMaxWidth()) }
        }
        SaveState.Idle, is SaveState.Success, is SaveState.Error -> {
            { Spacer(Modifier.size(6.dp)) }
        }
        else -> { { } }
    }
    if(active_ubication_popup){
        LocationSetUp(controlViewModel)
    }

    Scaffold(
        topBar = {
            Column {
                RoboRangerTopAppBar(
                    title = stringResource(ControlDestination.titleRes),
                    canNavigateBack = canNavigateBack,
                    navigateUp = onNavigateUp,
                    scrollBehavior = scrollBehavior,
                    canNavigateSettings = canNavigateSettings,
                    navigateToSettings = onNavigateSettings
                )
                loadingBar()
            }
        },
        floatingActionButton = {
            RoboRangerFAB(
                onClick = navigateToFormEntry,
                contentDescription = stringResource(R.string.add_icon)
            )
        }
    ) { innerPadding ->
        ControlBody(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            flashlightOn = flashlightOn,
            toggleFlashlight = { controlViewModel.toggleLight() },
            controlViewModel = controlViewModel
        )
    }
}



@Composable
fun ControlBody(
    modifier: Modifier = Modifier,
    flashlightOn: Boolean,
    toggleFlashlight: () -> Unit,
    controlViewModel: RobotControlViewModel
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LeftControls(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            controlViewModel = controlViewModel
        )
        CenterVideo(
            modifier = Modifier
                .weight(2f)
                .fillMaxSize(),
            bitmap = controlViewModel.videoBitmap.collectAsState().value
        )
        RightActions(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            flashlightOn = flashlightOn,
            toggleFlashlight = toggleFlashlight,
            controlViewModel = controlViewModel
        )
    }
}

@Composable
private fun LeftControls(
    modifier: Modifier = Modifier,
    controlViewModel: RobotControlViewModel
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RoboRangerRoundIconButton(
            icon = Icons.Filled.Sensors,
            action = {},
            label = stringResource(R.string.sensors_icon)
        )
        DPad(
            controlViewModel = controlViewModel
        )
    }
}

@Composable
private fun DPad(
    modifier: Modifier = Modifier,
    controlViewModel: RobotControlViewModel
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            MovementActionButton(
                icon = Icons.Filled.ArrowUpward,
                label = stringResource(R.string.arrow_upward_icon),
                onEntryAction = { controlViewModel.tryGoFront() },
                onEndAction = { controlViewModel.stop() }
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MovementActionButton(
                icon = Icons.Filled.ArrowBack,
                label = stringResource(R.string.arrow_back_icon),
                onEntryAction = { controlViewModel.tryGoLeft() },
                onEndAction = { controlViewModel.stop() }
            )
            MovementActionButton(
                icon = Icons.Filled.Stop,
                label = stringResource(R.string.stop_icon),
                onEntryAction = { controlViewModel.stop() },
                onEndAction = { controlViewModel.stop() },
                containerColor = Color.White,
                contentColor = Color(0xFFBC4749)
            )
            MovementActionButton(
                icon = Icons.Filled.ArrowForward,
                label = stringResource(R.string.arrow_forward_icon),
                onEntryAction = { controlViewModel.tryGoRight() },
                onEndAction = { controlViewModel.stop() }
            )
        }
        Row(horizontalArrangement = Arrangement.Center) {
            MovementActionButton(
                icon = Icons.Filled.ArrowDownward,
                label = stringResource(R.string.arrow_downward_icon),
                onEntryAction = { controlViewModel.tryGoBack() },
                onEndAction = { controlViewModel.stop() }
            )
        }
    }
}

@Composable
private fun CenterVideo(
    modifier: Modifier = Modifier,
    bitmap: Bitmap?
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (bitmap == null) Color.Black else Color.Transparent)
            .border(
                BorderStroke(2.dp, Color(0xFF222222)),
                RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if(bitmap!= null){
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = stringResource(R.string.stream_on),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }else{
            Text(
                text = "Vista en vivo del robot",
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

    }
}

@Composable
private fun RightActions(
    modifier: Modifier = Modifier,
    flashlightOn: Boolean,
    toggleFlashlight: () -> Unit,
    controlViewModel: RobotControlViewModel
) {
    val context = LocalContext.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RoboRangerRoundIconButton(
            icon = Icons.Filled.LocationOn,
            action = {controlViewModel.active_ubication_popup.value = true},
            label = stringResource(R.string.location_icon)
        )
        RoboRangerRoundIconButton(
            icon = Icons.Filled.PhotoCamera,
            action = { controlViewModel.takePhoto() },
            label = stringResource(R.string.photos_icon)
        )
        RoboRangerRoundIconButton(
            icon = if (flashlightOn) Icons.Filled.FlashlightOff else Icons.Filled.FlashlightOn,
            action = {
                toggleFlashlight()
                controlViewModel.toggleLight()
             },
            label = stringResource(if (flashlightOn) R.string.flashlight_on_icon else R.string.flashlight_off_icon),
            containerColor = if (flashlightOn) Color.White else Color(0xFF4E7029),
            contentColor = if (flashlightOn) Color(0xFF4E7029) else Color.White
        )
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
@SuppressLint("MissingPermission")
fun LocationSetUp(
    controlViewModel: RobotControlViewModel
    ){
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    LaunchedEffect(LocalContext.current.hasLocationPermission()) {
        permissionState.launchMultiplePermissionRequest()
    }

    when {
        permissionState.allPermissionsGranted -> {
            LaunchedEffect(Unit) {
                controlViewModel.handle(PermissionEvent.Granted)
            }
        }

        permissionState.shouldShowRationale -> {
            {
                permissionState.launchMultiplePermissionRequest()
            }

        }

        !permissionState.allPermissionsGranted && !permissionState.shouldShowRationale -> {
            LaunchedEffect(Unit) {
                controlViewModel.handle(PermissionEvent.Revoked)
            }
        }
    }
    val viewState = controlViewModel.viewState.collectAsState()
    Dialog(
        onDismissRequest = {controlViewModel.active_ubication_popup.value = false}
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.8f)
                .clip(RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ){
            with(viewState.value ) {
                when (this) {
                    ViewState.Loading -> {
                        CircularProgressIndicator()
                    }

                    ViewState.RevokedPermissions -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("We need permissions to use show location")
                        }
                    }

                    is ViewState.Success -> {
                        val currentLoc =
                            LatLng(
                                location?.latitude ?: 0.0,
                                location?.longitude ?: 0.0
                            )
                        LocationMap(
                            ubi = currentLoc
                        )
                    }
                }
            }
        }

    }



}

@Composable
fun LocationMap(
    ubi : LatLng
){
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ubi, 17.5f)
    }
    var uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true)) }
    var properties by remember { mutableStateOf(MapProperties(mapType =  MapType.SATELLITE)) }



    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        uiSettings = uiSettings,
        properties = properties,
        cameraPositionState = cameraPositionState
    ){
        Marker(
            state = MarkerState(position = ubi),
            title = "Usuario",
            snippet = "Esta es la posicion del usuario",
            draggable = true
        )

    }


}


