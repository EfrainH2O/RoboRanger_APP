package com.example.roboranger.ui.views.home

import android.content.pm.ActivityInfo
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.roboranger.R
import com.example.roboranger.data.FormCard
import com.example.roboranger.navigation.NavigationDestination
import com.example.roboranger.ui.components.LockScreenOrientation
import com.example.roboranger.ui.components.RoboRangerBottomAppBar
import com.example.roboranger.ui.components.RoboRangerFormCard
import com.example.roboranger.ui.components.RoboRangerTopAppBar

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToControl: () -> Unit,
    navigateToFormDetails: (Int, Int) -> Unit,
    onNavigateSettings: () -> Unit,
    canNavigateBack: Boolean = false,
    canNavigateSettings: Boolean = true,
    formCardViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState = formCardViewModel.uiState.collectAsState().value
    var selectedStatus by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        formCardViewModel.loadData()
    }
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    Scaffold(
        topBar = {
            RoboRangerTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = {},
                canNavigateSettings = canNavigateSettings,
                navigateToSettings = onNavigateSettings
            )
        },
        bottomBar = {
            RoboRangerBottomAppBar(
                navigateToControl = navigateToControl
            )
        }
    ) { innerPadding ->
        HomeBody(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            onNavigateControl = navigateToControl,
            onNavigateFormDetails = navigateToFormDetails,
            uiState = uiState,
            selectedStatus = selectedStatus,
            onStatusSelected = { newStatus ->
                selectedStatus = newStatus
            }
        )
    }
}
@Composable
fun HomeBody(
    modifier: Modifier = Modifier,
    onNavigateControl: () -> Unit,
    onNavigateFormDetails: (Int, Int) -> Unit,
    uiState: UiState,
    selectedStatus: Boolean,
    onStatusSelected: (Boolean) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Indicadores de formularios
        Row(
            modifier = Modifier
                .padding(vertical = 32.dp),
        ) {
            FormsText(text = "Totales", numForms = uiState.totalFormularios)
            Spacer(Modifier.padding(horizontal = 10.dp))
            FormsText(text = "Enviados", colorText = Color(0xFF9BC86A), numForms = uiState.totalFormulariosEnviados)
            Spacer(Modifier.padding(horizontal = 10.dp))
            FormsText(text = "Guardados", colorText = Color(0xFFBC4749), numForms =  uiState.totalFormularios - uiState.totalFormulariosEnviados )
        }

        // Botones de cambio de lista de formularios
        Row(
            modifier = Modifier
                .padding(bottom = 16.dp)
        ) {
            TextButton(
                onClick = { onStatusSelected(false) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (!selectedStatus) Color(0xFF9BC86A) else Color.Black
                )
            ) {
                Text("Enviados",
                    modifier = Modifier,
                    fontWeight = if (!selectedStatus) FontWeight.ExtraBold else FontWeight.SemiBold,
                    fontSize = 20.sp,
                )
            }

            Spacer(Modifier.padding(horizontal = 32.dp))

            TextButton(
                onClick = { onStatusSelected(true) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (selectedStatus) Color(0xFFBC4749) else Color.Black
                )
            ) {
                Text("Guardados",
                    modifier = Modifier,
                    fontWeight = if (selectedStatus) FontWeight.ExtraBold else FontWeight.SemiBold,
                    fontSize = 20.sp,
                )
            }
        }

        FormsGrid(
            modifier = Modifier,
            formList = if(!selectedStatus) uiState.formulariosEnviados else uiState.formulariosNoEnviados,
            openScreen = {data ->
                onNavigateFormDetails(data.formType, data.id)
            }
        )
    }
}


@Composable
fun FormsGrid(
    modifier: Modifier = Modifier,
    formList: List<FormCard>,
    openScreen: (FormCard)-> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxHeight(1F),
        columns = GridCells.Fixed(1),
    ) {
        items(formList) { form ->
            RoboRangerFormCard(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp).clickable(onClick = {openScreen(form)}),
                formCard = form
            )
        }
    }
}

@Composable
fun FormsText(
    modifier: Modifier = Modifier,
    numForms: Int = 0,
    colorText: Color = Color.Black,
    text: String = "Tipo"
) {
    Column(
    ) {
        Text("$numForms Forms",
            Modifier.align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp
        )
        Text(text,
            Modifier.align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = colorText
        )
    }
}

