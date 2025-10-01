package com.example.roboranger.ui.views.home

import android.graphics.Color
import android.graphics.fonts.FontStyle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ControlCamera
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Pages
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roboranger.R
import com.example.roboranger.ui.components.RoboRangerTopAppBar
import com.example.roboranger.navigation.NavigationDestination
import com.example.roboranger.ui.components.RoboRangerButton
import com.example.roboranger.ui.components.RoboRangerButtonIcon

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
            modifier = modifier
                .padding(innerPadding)
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

        Row(
            modifier = Modifier
                .padding(vertical = 32.dp),
        ) {
            FormsText(text = "Totales")
            Spacer(Modifier.padding(horizontal = 10.dp))
            FormsText(text = "Enviados")
            Spacer(Modifier.padding(horizontal = 10.dp))
            FormsText(text = "Guardados")
        }

        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text("Enviados",
                modifier = Modifier,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                )
            Spacer(Modifier.padding(horizontal = 32.dp))
            Text("Guardados",
                modifier = Modifier,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                )
        }





        Row() {
            RoboRangerButtonIcon(
                icon = Icons.Filled.DirectionsCar,
                onClick = onNavigateControl
            )
            RoboRangerButtonIcon(
                icon = Icons.Filled.ListAlt,
                onClick = onNavigateFormDetails
            )
        }
    }
}


@Composable
fun FormsGrid(

) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(vertical = 24.dp),
    ) {

    }
}

@Composable
fun FormsText(
    modifier: Modifier = Modifier,
    numForms: Int = 0,
    colorText: Int = Color.BLACK,
    text: String = "Tipo"
) {
    Column(
    ) {
        Text("$numForms Forms",
            Modifier.align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        )
        Text(text,
            Modifier.align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    HomeScreen(
        navigateToControl = {},
        navigateToFormDetails = {},
        onNavigateSettings = {}

    )
}

