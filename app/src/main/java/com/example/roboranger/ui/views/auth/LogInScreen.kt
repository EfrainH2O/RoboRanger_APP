package com.example.roboranger.ui.views.auth

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
import androidx.compose.ui.tooling.preview.Preview
import com.example.roboranger.R
import com.example.roboranger.ui.components.RoboRangerTopAppBar
import com.example.roboranger.navigation.NavigationDestination
import com.example.roboranger.ui.components.RoboRangerButton
import com.example.roboranger.ui.views.form.FormEntryDestination

object LogInDestination : NavigationDestination {
    override val route = "login"
    override val titleRes = R.string.login_title
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(
    navigateToHome: () -> Unit,
    canNavigateBack: Boolean = false,
    canNavigateSettings: Boolean = false
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            RoboRangerTopAppBar(
                title = stringResource(LogInDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = {},
                scrollBehavior = scrollBehavior,
                canNavigateSettings = canNavigateSettings,
                navigateToSettings = {}
            )
        }
    ) { innerPadding ->
        LogInBody(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            onNavigateHome = navigateToHome
        )
    }
}

@Composable
fun LogInBody(
    modifier: Modifier = Modifier,
    onNavigateHome: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Aqui va el contenido de la pantalla de inicio de sesion",
            textAlign = TextAlign.Center,
        )
        RoboRangerButton(
            text = "Iniciar Sesion",
            onClick = onNavigateHome
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LogInBodyPreview(){
    LogInBody(
        onNavigateHome = {}
    )
}