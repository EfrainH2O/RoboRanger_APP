package com.example.roboranger.ui.views.form

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
import com.example.roboranger.R
import com.example.roboranger.ui.components.RoboRangerTopAppBar
import com.example.roboranger.navigation.NavigationDestination

object FormDetailsDestination: NavigationDestination {
    override val route = "form_details"
    override val titleRes = R.string.form_details_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDetailsScreen(
    onNavigateUp: () -> Unit,
    onNavigateSettings: () -> Unit,
    canNavigateBack: Boolean = true,
    canNavigateSettings: Boolean = true
) {
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
        FormDetailsBody(
            modifier = Modifier.padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
        )
    }
}

@Composable
fun FormDetailsBody(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Aqui va el contenido de la pantalla de visualizacion de formulario",
            textAlign = TextAlign.Center,
        )
    }
}