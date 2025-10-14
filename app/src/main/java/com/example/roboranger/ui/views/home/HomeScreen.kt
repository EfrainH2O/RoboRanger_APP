package com.example.roboranger.ui.views.home

import android.content.pm.ActivityInfo
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FormatListNumberedRtl
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roboranger.R
import com.example.roboranger.data.FormCard
import com.example.roboranger.ui.components.RoboRangerTopAppBar
import com.example.roboranger.navigation.NavigationDestination
import com.example.roboranger.ui.components.LockScreenOrientation
import com.example.roboranger.ui.components.RoboRangerBottomAppBar
import com.example.roboranger.ui.components.RoboRangerButtonIcon
import com.example.roboranger.ui.components.RoboRangerFormCard
import com.example.roboranger.view_model.FormCardViewModel
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
    canNavigateSettings: Boolean = true,
    // Instancia de viewmodel de formcard
    formCardViewModel: FormCardViewModel = viewModel()
) {
    val formList = formCardViewModel.formList.value
    var selectedStatus by remember { mutableIntStateOf(0) }
    val filteredForms = remember(formList, selectedStatus) {
        formList.filter { form ->
            // This assumes your FormCard data class has a 'status' property.
            // e.g., data class FormCard(..., val status: String)
            form.status == selectedStatus
        }
    }

    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    //val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

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
            formList = filteredForms,
            selectedStatus = selectedStatus,
            onStatusSelected = { newStatus ->
                selectedStatus = newStatus
                // Actualizar la lista de formularios según el estado seleccionado
            }
        )
    }
}
@Composable
fun HomeBody(
    modifier: Modifier = Modifier,
    onNavigateControl: () -> Unit,
    onNavigateFormDetails: () -> Unit,
    formList: List<FormCard>,
    selectedStatus: Int,
    onStatusSelected: (Int) -> Unit
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
            FormsText(text = "Totales")
            Spacer(Modifier.padding(horizontal = 10.dp))
            FormsText(text = "Enviados", colorText = Color(0xFF9BC86A))
            Spacer(Modifier.padding(horizontal = 10.dp))
            FormsText(text = "Guardados", colorText = Color(0xFFBC4749))
        }

        // Botones de cambio de lista de formularios
        Row(
            modifier = Modifier
                .padding(bottom = 16.dp)
        ) {
            TextButton(
                onClick = { onStatusSelected(0) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (selectedStatus == 0) Color(0xFF9BC86A) else Color.Black
                )
            ) {
                Text("Enviados",
                    modifier = Modifier,
                    fontWeight = if (selectedStatus == 0) FontWeight.ExtraBold else FontWeight.SemiBold,
                    fontSize = 20.sp,
                )
            }

            Spacer(Modifier.padding(horizontal = 32.dp))

            TextButton(
                onClick = { onStatusSelected(1) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (selectedStatus == 1) Color(0xFFBC4749) else Color.Black
                )
            ) {
                Text("Guardados",
                    modifier = Modifier,
                    fontWeight = if (selectedStatus == 1) FontWeight.ExtraBold else FontWeight.SemiBold,
                    fontSize = 20.sp,
                )
            }
        }

        // Grid que cambia dependiendo de el status seleccionado
        FormsGrid(
            modifier = Modifier,
            formList = formList
        )

        // Botones de navegación
        // Deberian de cambiarse a una bottombar definida
        /*
        Row() {
            RoboRangerButtonIcon(
                icon = Icons.Filled.DirectionsCar,
                onClick = onNavigateControl
            )
            RoboRangerButtonIcon(
                icon = Icons.Filled.FormatListNumberedRtl,
                onClick = onNavigateFormDetails
            )
        }
        */
    }
}


@Composable
fun FormsGrid(
    modifier: Modifier = Modifier,
    formList: List<FormCard>,
) {
    LazyVerticalGrid(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxHeight(1F),
        columns = GridCells.Fixed(1),
    ) {
        items(formList) { form ->
            // Posteriormente volver las cartas botones que lleven
            // a los formularios correspondientes
            RoboRangerFormCard(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
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

@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    HomeScreen(
        navigateToControl = {},
        navigateToFormDetails = {},
        onNavigateSettings = {}

    )
}

