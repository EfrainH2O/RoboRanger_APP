package com.example.roboranger.ui.views.form

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

object FormEntryDestination : NavigationDestination {
    override val route = "form_entry"
    override val titleRes = R.string.form_entry_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormEntryScreen(
    onNavigateUp: () -> Unit,
    onNavigateSettings: () -> Unit,
    navigateToHome: () -> Unit,
    canNavigateBack: Boolean = true,
    canNavigateSettings: Boolean = true
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            RoboRangerTopAppBar(
                title = stringResource(FormEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior,
                canNavigateSettings = canNavigateSettings,
                navigateToSettings = onNavigateSettings
            )
        }
    ) { innerPadding ->
        FormEntryBody(
            modifier = Modifier.padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            onNavigateHome = navigateToHome
        )
    }
}

@Composable
fun FormEntryBody(
    modifier: Modifier = Modifier,
    onNavigateHome: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Aqui va el contenido de la pantalla de llenado de formulario",
            textAlign = TextAlign.Center,
        )
        RoboRangerButton(
            text = "Guardar Formulario",
            onClick = onNavigateHome
        )
    }
}