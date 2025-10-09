package com.example.roboranger.ui.views.home

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.roboranger.R
import com.example.roboranger.ui.components.RoboRangerTopAppBar
import com.example.roboranger.navigation.NavigationDestination
import com.example.roboranger.ui.components.LockScreenOrientation
import com.example.roboranger.ui.components.RoboRangerButton

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToControl: () -> Unit,
    navigateToFormDetails: () -> Unit,
    onNavigateSettings: () -> Unit,
    canNavigateBack: Boolean = false,
    canNavigateSettings: Boolean = true
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            RoboRangerTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = {},
                scrollBehavior = scrollBehavior,
                canNavigateSettings = canNavigateSettings,
                navigateToSettings = onNavigateSettings
            )
        }
    ) { innerPadding ->
        HomeBody(
            modifier = modifier.padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            onNavigateControl = navigateToControl,
            onNavigateFormDetails = navigateToFormDetails
        )
    }
}
@Composable
fun HomeBody(
    modifier: Modifier = Modifier,
    onNavigateControl: () -> Unit,
    onNavigateFormDetails: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Aqui va el contenido de la pantalla de inicio",
            textAlign = TextAlign.Center,
        )
        RoboRangerButton(
            text = "Abrir Sesion de Exploracion",
            onClick = onNavigateControl
        )
        RoboRangerButton(
            text = "Abrir Formulario Guardado",
            onClick = onNavigateFormDetails
        )
    }
}