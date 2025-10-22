package com.example.roboranger.ui.views.form

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.roboranger.R
import com.example.roboranger.domain.model.FormUiState
import com.example.roboranger.domain.model.ImageState
import com.example.roboranger.navigation.NavigationDestination
import com.example.roboranger.ui.components.LockScreenOrientation
import com.example.roboranger.ui.components.RoboRangerButton
import com.example.roboranger.ui.components.RoboRangerFormMultiLineTextField
import com.example.roboranger.ui.components.RoboRangerFormSelectionRadioButton
import com.example.roboranger.ui.components.RoboRangerOutlinedButton
import com.example.roboranger.ui.components.RoboRangerTextField
import com.example.roboranger.ui.components.RoboRangerTopAppBar
import com.example.roboranger.ui.components.SelectableCardVertical
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

object FormEntryDestination : NavigationDestination {
    override val route = "form_entry"
    override val titleRes = R.string.form_entry_title
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun FormEntryScreen(
    onNavigateUp: () -> Unit,
    onNavigateSettings: () -> Unit,
    navigateToHome: () -> Unit,
    canNavigateBack: Boolean = true,
    canNavigateSettings: Boolean = true,
    formViewModel: FormViewModel = hiltViewModel()
) {
    val uiState by formViewModel.uiState.collectAsState()
    val isLoading = uiState is FormUiState.Loading
    val errorMessage = (uiState as? FormUiState.Error)?.message

    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val sharedFormState by formViewModel.sharedFormState.collectAsState()
    val form1State by formViewModel.form1State.collectAsState()
    val form7State by formViewModel.form7State.collectAsState()

    var formType by rememberSaveable { mutableStateOf(FormType.NONE) }

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    val imageState by formViewModel.imageState.collectAsState()

    val sharedOk = sharedFormState.nombre.isNotBlank() && sharedFormState.season != null && sharedFormState.weather != null && sharedFormState.date.isNotBlank()
            && sharedFormState.latitude.toDoubleOrNull() != null && sharedFormState.longitude.toDoubleOrNull() != null
            && sharedFormState.maxTemp.toDoubleOrNull() != null && sharedFormState.minTemp.toDoubleOrNull() != null && sharedFormState.maxHum.toDoubleOrNull() != null

    val form1Ok = sharedOk && formType == FormType.FORM1 &&
            form1State.transect.isNotBlank() && form1State.animalType != null && form1State.commonName.isNotBlank() &&
            (form1State.individuals.toIntOrNull() ?: 0) > 0 && form1State.observationsType != null

    val form7Ok = sharedOk && formType == FormType.FORM7 &&
            form7State.zone != null && form7State.pluviosity.toDoubleOrNull() != null && form7State.ravineLevel.toDoubleOrNull() != null

    LaunchedEffect(uiState) {
        if (uiState is FormUiState.Success || uiState is FormUiState.Saved || uiState is FormUiState.Error) {
            navigateToHome()
            formViewModel.resetUiState()
        }
    }

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
        },
        bottomBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RoboRangerOutlinedButton(
                    onClick = {
                        when (formType){
                            FormType.FORM1 -> formViewModel.saveForm1()
                            FormType.FORM7 -> formViewModel.saveForm7()
                            else -> {}
                        }
                    },
                    modifier = Modifier.weight(1f),
                    text = "Guardar"
                )
                RoboRangerButton(
                    text = "Subir",
                    enabled = !isLoading && (form1Ok || form7Ok) && imageState is ImageState.Available,
                    modifier = Modifier.weight(1f)
                ) {
                    when (formType) {
                        FormType.FORM1 -> formViewModel.submitForm1()
                        FormType.FORM7 -> formViewModel.submitForm7()
                        else -> {}
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FormEntryBody(
                sharedState = sharedFormState,
                onStateChange = formViewModel::onSharedFormChange,
                formType = formType,
                onFormType = { formType = it },
                isLoading = isLoading,
                onLocationClick = {
                    if (locationPermissionsState.allPermissionsGranted) {
                        formViewModel.fetchCurrentLocation()
                    } else {
                        locationPermissionsState.launchMultiplePermissionRequest()
                    }
                },
                isFetchingLocation = formViewModel.isFetchingLocation.collectAsState().value
            )
            when (formType) {
                FormType.FORM1 -> Form1EntryBody(
                    form1State = form1State,
                    onForm1Change = formViewModel::onForm1Change
                )
                FormType.FORM7 -> Form7EntryBody(
                    form7State = form7State,
                    onForm7Change = formViewModel::onForm7Change
                )
                else -> Unit
            }
            Text(
                text = "Imagen Capturada",
                style = MaterialTheme.typography.titleMedium
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    when (val state = imageState) {
                        is ImageState.Available -> {
                            AsyncImage(
                                model = state.uri,
                                contentDescription = "Última imagen capturada",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        is ImageState.NotAvailable -> {
                            Text(
                                text = "Ve a la pantalla de control para capturar una imagen",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            if (isLoading) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }

            Spacer(Modifier.height(96.dp))
        }
    }
}

@Composable
fun FormEntryBody(
    sharedState: SharedFormState,
    onStateChange: (SharedFormState) -> Unit,
    formType: FormType,
    onFormType: (FormType) -> Unit,
    isLoading: Boolean,
    onLocationClick: () -> Unit,
    isFetchingLocation: Boolean
) {
    Text(
        text = "Datos Generales",
        style = MaterialTheme.typography.titleLarge
    )
    RoboRangerTextField(
        label = "Nombre",
        value = sharedState.nombre,
        onValueChange = { onStateChange(sharedState.copy(nombre = it)) },
        placeholder = "Introduzca el nombre del Form",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
    Text(
        text = "Estado del tiempo",
        style = MaterialTheme.typography.titleMedium
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        WeatherOptions.entries.forEach { opt ->
            SelectableCardVertical(
                text = opt.label,
                selected = sharedState.weather == opt,
                onClick = { onStateChange(sharedState.copy(weather = opt)) },
                modifier = Modifier
                    .height(168.dp)
                    .width(128.dp)
            ) {
                Text(
                    text = opt.emoji,
                    fontSize = 28.sp
                )
            }
        }
    }
    Spacer(Modifier.height(8.dp))
    Text(
        text = "Época",
        style = MaterialTheme.typography.titleMedium
    )
    Column {
        SeasonOptions.entries.forEach { opt ->
            RoboRangerFormSelectionRadioButton(
                text = opt.label,
                selected = sharedState.season == opt,
                onClick = { onStateChange(sharedState.copy(season = opt)) }
            )
        }
    }
    Spacer(Modifier.height(8.dp))
    RoboRangerTextField(
        label = "Fecha",
        value = sharedState.date,
        onValueChange = { onStateChange(sharedState.copy(date = it)) },
        placeholder = "yyyy-mm-dd"
    )
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        RoboRangerTextField(
            label = "Latitud",
            value = sharedState.latitude,
            onValueChange = {if (it.isEmpty() || it.toDoubleOrNull() != null) onStateChange(sharedState.copy(latitude = it)) },
            placeholder = "Introduzca la latitud",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
        RoboRangerTextField(
            label = "Longitud",
            value = sharedState.longitude,
            onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) onStateChange(sharedState.copy(longitude = it)) },
            placeholder = "Introduzca la longitud",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
        Surface(
            modifier = Modifier.size(56.dp).padding(top = 10.dp),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primary,
            onClick = if (isFetchingLocation) { {} } else onLocationClick
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (isFetchingLocation) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 3.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Ubicación Actual",
                        tint = Color.White
                    )
                }
            }
        }
    }
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        RoboRangerTextField(
            label = "T. Máx (°C)",
            value = sharedState.maxTemp,
            onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) onStateChange(sharedState.copy(maxTemp = it)) },
            placeholder = "Introduzca la temperatura en centigrados",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
        RoboRangerTextField(
            label = "T. Mín (°C)",
            value = sharedState.minTemp,
            onValueChange = {if (it.isEmpty() || it.toDoubleOrNull() != null)  onStateChange(sharedState.copy(minTemp = it)) },
            placeholder = "Introduzca la temperatura en centigrados",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
    }
    RoboRangerTextField(
        label = "Humedad Máx (%)",
        value = sharedState.maxHum,
        onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) onStateChange(sharedState.copy(maxHum = it)) },
        placeholder = "Introduzca la humedad en porcentaje",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    Spacer(Modifier.height(8.dp))
    Text(
        "Tipo de registro",
        style = MaterialTheme.typography.titleMedium
    )
    Column {
        RoboRangerFormSelectionRadioButton(
            text = "Fauna en Transectos",
            selected = formType == FormType.FORM1,
            onClick = { if (!isLoading) onFormType(FormType.FORM1) }
        )
        RoboRangerFormSelectionRadioButton(
            text = "Variables Climáticas",
            selected = formType == FormType.FORM7,
            onClick = { if (!isLoading) onFormType(FormType.FORM7) }
        )
    }
}

@Composable
fun Form1EntryBody(
    form1State: Form1State,
    onForm1Change: (Form1State) -> Unit
) {
    Text(
        text = "Fauna en Transectos",
        style = MaterialTheme.typography.titleLarge
    )
    RoboRangerTextField(
        label = "Número de Transecto",
        value = form1State.transect,
        onValueChange = { onForm1Change(form1State.copy(transect = it)) },
        placeholder = "Introduzca el transecto recorrido"
    )
    Spacer(Modifier.height(8.dp))
    Text("Tipo de Animal", style = MaterialTheme.typography.titleMedium)
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AnimalOptions.entries.forEach { opt ->
            SelectableCardVertical(
                text = opt.label,
                selected = form1State.animalType == opt,
                onClick = { onForm1Change(form1State.copy(animalType = opt)) },
                modifier = Modifier
                    .height(168.dp)
                    .width(128.dp)
            ) {
                Text(
                    text = opt.emoji,
                    fontSize = 28.sp
                )
            }
        }
    }
    RoboRangerTextField(
        label = "Nombre Común",
        value = form1State.commonName,
        onValueChange = { onForm1Change(form1State.copy(commonName = it)) },
        placeholder = "Introduzca el nombre del animal"
    )
    RoboRangerTextField(
        label = "Nombre Científico (opcional)",
        value = form1State.scientificName,
        onValueChange = { onForm1Change(form1State.copy(scientificName = it)) },
        placeholder = "Introduzca el nombre científico"
    )
    RoboRangerTextField(
        label = "Número de Individuos",
        value = form1State.individuals,
        onValueChange = { onForm1Change(form1State.copy(individuals = it)) },
        placeholder = "Introduzca el numero de individuos",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
    )
    Spacer(Modifier.height(8.dp))
    Text("Tipo de Observación", style = MaterialTheme.typography.titleMedium)
    Column {
        ObservationsTypeOptions.entries.forEach { opt ->
            RoboRangerFormSelectionRadioButton(
                text = opt.label,
                selected = form1State.observationsType == opt,
                onClick = { onForm1Change(form1State.copy(observationsType = opt)) }
            )
        }
    }
    RoboRangerFormMultiLineTextField(
        label = "Observaciones",
        value = form1State.observations,
        onValueChange = { onForm1Change(form1State.copy(observations = it)) },
        placeholder = "Introduzca las observaciones realizadas...",
        modifier = Modifier.heightIn(min = 120.dp)
    )
}

@Composable
fun Form7EntryBody(
    form7State: Form7State,
    onForm7Change: (Form7State) -> Unit
) {
    Text(
        text = "Variables Climáticas",
        style = MaterialTheme.typography.titleLarge
    )
    Text(
        text = "Zona",
        style = MaterialTheme.typography.titleMedium
    )
    Column {
        ZoneOptions.entries.forEach { opt ->
            RoboRangerFormSelectionRadioButton(
                text = opt.label,
                selected = form7State.zone == opt,
                onClick = { onForm7Change(form7State.copy(zone = opt)) }
            )
        }
    }
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        RoboRangerTextField(
            label = "Pluviosidad (mm)",
            value = form7State.pluviosity,
            onValueChange = {if (it.isEmpty() || it.toDoubleOrNull() != null)  onForm7Change(form7State.copy(pluviosity = it)) },
            placeholder = "Introduzca el rango de pluviosidad",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
        RoboRangerTextField(
            label = "Nivel quebrada (mt)",
            value = form7State.ravineLevel,
            onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) onForm7Change(form7State.copy(ravineLevel = it)) },
            placeholder = "Introduzca el nivel de quebrada",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
    }
}
