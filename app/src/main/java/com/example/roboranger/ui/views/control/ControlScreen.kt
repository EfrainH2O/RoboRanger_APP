package com.example.roboranger.ui.views.control

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.roboranger.R
import com.example.roboranger.navigation.NavigationDestination
import com.example.roboranger.ui.components.RoboRangerFAB
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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

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
            modifier = Modifier.padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
        )
    }
}

@Composable
fun ControlBody(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Aqui va el contenido de la pantalla de control",
            textAlign = TextAlign.Center,
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