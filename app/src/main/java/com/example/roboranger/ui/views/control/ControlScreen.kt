package com.example.roboranger.ui.views.control

import android.content.pm.ActivityInfo
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.roboranger.R
import com.example.roboranger.navigation.NavigationDestination
import com.example.roboranger.ui.components.LockScreenOrientation
import com.example.roboranger.ui.components.RoboRangerFAB
import com.example.roboranger.ui.components.RoboRangerRoundIconButton
import com.example.roboranger.ui.components.RoboRangerSquareIconButton
import com.example.roboranger.ui.components.RoboRangerTopAppBar
import com.example.roboranger.ui.theme.RoboRangerTheme

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
    canNavigateSettings: Boolean = true
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var flashlightState by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            RoboRangerTopAppBar(
                title = stringResource(ControlDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior,
                canNavigateSettings = canNavigateSettings,
                navigateToSettings = onNavigateSettings
            )
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
            flashlightState = flashlightState,
            toggleFlashlight = { flashlightState = !flashlightState }
        )
    }
}

@Composable
fun ControlBody(
    modifier: Modifier = Modifier,
    flashlightState: Boolean,
    toggleFlashlight: () -> Unit
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
                .fillMaxSize()
        )
        CenterVideo(
            modifier = Modifier
                .weight(2f)
                .fillMaxSize()
        )
        RightActions(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            flashlightState = flashlightState,
            toggleFlashlight = toggleFlashlight
        )
    }
}

@Composable
private fun LeftControls(
    modifier: Modifier = Modifier
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
        DPad()
    }
}

@Composable
private fun DPad(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            RoboRangerSquareIconButton(
                icon = Icons.Filled.ArrowUpward,
                action = {},
                label = stringResource(R.string.arrow_upward_icon)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            RoboRangerSquareIconButton(
                icon = Icons.Filled.ArrowBack,
                action = {},
                label = stringResource(R.string.arrow_back_icon)
            )
            RoboRangerSquareIconButton(
                icon = Icons.Filled.Stop,
                action = {},
                label = stringResource(R.string.stop_icon),
                containerColor = Color.White,
                contentColor = Color(0xFFBC4749)
            )
            RoboRangerSquareIconButton(
                icon = Icons.Filled.ArrowForward,
                action = {},
                label = stringResource(R.string.arrow_forward_icon)
            )
        }
        Row(horizontalArrangement = Arrangement.Center) {
            RoboRangerSquareIconButton(
                icon = Icons.Filled.ArrowDownward,
                action = {},
                label = stringResource(R.string.arrow_downward_icon)
            )
        }
    }
}

@Composable
private fun CenterVideo(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(12.dp))
            .background(Color.Black)
            .border(
                BorderStroke(2.dp, Color(0xFF222222)),
                RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Vista en vivo del robot",
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RightActions(
    modifier: Modifier = Modifier,
    flashlightState: Boolean,
    toggleFlashlight: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RoboRangerRoundIconButton(
            icon = Icons.Filled.LocationOn,
            action = {},
            label = stringResource(R.string.location_icon)
        )
        RoboRangerRoundIconButton(
            icon = Icons.Filled.PhotoCamera,
            action = {},
            label = stringResource(R.string.photos_icon)
        )
        RoboRangerRoundIconButton(
            icon = if (flashlightState) Icons.Filled.FlashlightOff else Icons.Filled.FlashlightOn,
            action = toggleFlashlight,
            label = stringResource(if (flashlightState) R.string.flashlight_on_icon else R.string.flashlight_off_icon),
            containerColor = if (flashlightState) Color.White else Color(0xFF4E7029),
            contentColor = if (flashlightState) Color(0xFF4E7029) else Color.White
        )
    }
}

@Preview(showSystemUi = true, device = Devices.PIXEL_4)
@Composable
fun ControlScreenPreview() {
    RoboRangerTheme {
        ControlScreen(onNavigateUp = {}, navigateToFormEntry = {}, onNavigateSettings = {})
    }
}