package com.example.roboranger.ui.views.form

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.roboranger.R
import com.example.roboranger.domain.model.FormUiState
import com.example.roboranger.ui.components.RoboRangerTopAppBar
import com.example.roboranger.navigation.NavigationDestination
import com.example.roboranger.ui.components.LockScreenOrientation
import com.example.roboranger.ui.components.RoboRangerButton
import com.example.roboranger.ui.components.RoboRangerFormMultiLineTextField
import com.example.roboranger.ui.components.RoboRangerFormSelectionRadioButton
import com.example.roboranger.ui.components.RoboRangerTextField
import com.example.roboranger.ui.components.SelectableCardHorizontal
import com.example.roboranger.ui.components.SelectableCardVertical

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
    canNavigateSettings: Boolean = true,
    formViewModel: FormViewModel = hiltViewModel()
) {
    // Estados del viewmodel
    val uiState by formViewModel.uiState.collectAsState()
    val isLoading = uiState is FormUiState.Loading
    val errorMessage = (uiState as? FormUiState.Error)?.message

    // Comportamientos basicos de la vista
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Campos Compartidos
    var seasonOption by rememberSaveable { mutableStateOf<SeasonOptions?>(null) }
    var weatherOption by rememberSaveable { mutableStateOf<WeatherOptions?>(null) }
    var date by rememberSaveable { mutableStateOf("") }
    var latitude by rememberSaveable { mutableStateOf("") }
    var longitude by rememberSaveable { mutableStateOf("") }
    var tMax by rememberSaveable { mutableStateOf("") }
    var tMin by rememberSaveable { mutableStateOf("") }
    var hMax by rememberSaveable { mutableStateOf("") }

    // Campo de Formulario modular
    var formType by rememberSaveable { mutableStateOf(FormType.NONE) }

    // Campos del Formulario 1
    var transect by rememberSaveable { mutableStateOf("") }
    var animalOption by rememberSaveable { mutableStateOf<AnimalOptions?>(null) }
    var commonName by rememberSaveable { mutableStateOf("") }
    var scientificName by rememberSaveable { mutableStateOf("") }
    var individuals by rememberSaveable { mutableStateOf("") }
    var observationsTypeOption by rememberSaveable { mutableStateOf<ObservationsTypeOptions?>(null) }
    var observations by rememberSaveable { mutableStateOf("") }

    // Campos del Formulario 7
    var zoneOption by rememberSaveable { mutableStateOf<ZoneOptions?>(null) }
    var pluviosity by rememberSaveable { mutableStateOf("") }
    var ravineLevel by rememberSaveable { mutableStateOf("") }

    // Validacion de formularios
    val sharedOk = seasonOption != null && weatherOption != null && date.isNotBlank()
            && latitude.toDoubleOrNull() != null && longitude.toDoubleOrNull() != null
            && tMax.toDoubleOrNull() != null && tMin.toDoubleOrNull() != null && hMax.toDoubleOrNull() != null

    val form1Ok = sharedOk && formType == FormType.FORM1 &&
            transect.isNotBlank() && animalOption != null && commonName.isNotBlank() &&
            (individuals.toIntOrNull() ?: 0) > 0 && observationsTypeOption != null

    val form7Ok = sharedOk && formType == FormType.FORM7 &&
            zoneOption != null && pluviosity.toDoubleOrNull() != null && ravineLevel.toDoubleOrNull() != null

    // En la confirmacion del envio del formulario, navegar a Home
    LaunchedEffect(uiState) {
        if (uiState is FormUiState.Success) {
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
                Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar")
                }
                RoboRangerButton(
                    text = "Subir",
                    enabled = !isLoading && (form1Ok || form7Ok),
                    modifier = Modifier.weight(1f)
                ) {
                    when (formType) {
                        FormType.FORM1 -> formViewModel.submitForm1(
                            transect = transect,
                            weather = weatherOption!!.label,
                            season = seasonOption!!.label,
                            animalType = animalOption!!.label,
                            commonName = commonName,
                            scientificName = scientificName,
                            individuals = individuals.toIntOrNull() ?: 0,
                            observationsType = observationsTypeOption!!.label,
                            observations = observations,
                            latitude = latitude.toDouble(),
                            longitude = longitude.toDouble(),
                            maxTemp = tMax.toDouble(),
                            maxHum = hMax.toDouble(),
                            minTemp = tMin.toDouble(),
                            date = date
                        )
                        FormType.FORM7 -> formViewModel.submitForm7(
                            weather = weatherOption!!.label,
                            season = seasonOption!!.label,
                            zone = zoneOption!!.label,
                            pluviosity = pluviosity.toDouble(),
                            maxTemp = tMax.toDouble(),
                            maxHum = hMax.toDouble(),
                            minTemp = tMin.toDouble(),
                            ravineLevel = ravineLevel.toDouble(),
                            latitude = latitude.toDouble(),
                            longitude = longitude.toDouble(),
                            date = date
                        )
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
            // Formulario General con campos compartidos
            FormEntryBody(
                seasonOption = seasonOption, onSeason = { seasonOption = it },
                weatherOption = weatherOption, onWeather = { weatherOption = it },
                date = date, onDate = { date = it },
                latitude = latitude, onLatitude = { latitude = it },
                longitude = longitude, onLongitude = { longitude = it },
                tMax = tMax, onTMax = { tMax = it },
                tMin = tMin, onTMin = { tMin = it },
                hMax = hMax, onHMax = { hMax = it },
                formType = formType, onFormType = { formType = it },
                isLoading = isLoading
            )
            // Formulario Condicional
            when (formType) {
                FormType.FORM1 -> Form1EntryBody(
                    transect = transect, onTransect = { transect = it },
                    animalOption = animalOption, onAnimalOptions = { animalOption = it },
                    commonName = commonName, onCommonName = { commonName = it },
                    scientificName = scientificName, onScientificName = { scientificName = it },
                    individuals = individuals, onIndividuals = { individuals = it },
                    observationsTypeOption = observationsTypeOption, onObservationsTypeOptions = { observationsTypeOption = it },
                    observations = observations, onObservations = { observations = it }
                )
                FormType.FORM7 -> Form7EntryBody(
                    zoneOptions = zoneOption, onZone = { zoneOption = it },
                    pluviosity = pluviosity, onPluviosity = { pluviosity = it },
                    ravineLevel = ravineLevel, onRavineLevel = { ravineLevel = it }
                )
                else -> Unit
            }

            // Visibilidad de estados del formulario
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
    seasonOption: SeasonOptions?, onSeason: (SeasonOptions) -> Unit,
    weatherOption: WeatherOptions?, onWeather: (WeatherOptions) -> Unit,
    date: String, onDate: (String) -> Unit,
    latitude: String, onLatitude: (String) -> Unit,
    longitude: String, onLongitude: (String) -> Unit,
    tMax: String, onTMax: (String) -> Unit,
    hMax: String, onHMax: (String) -> Unit,
    tMin: String, onTMin: (String) -> Unit,
    formType: FormType, onFormType: (FormType) -> Unit,
    isLoading: Boolean
) {
    Text(
        text = "Datos Generales",
        style = MaterialTheme.typography.titleLarge
    )
    // Clima con tarjetas horizontales
    Text(
        text = "Estado del tiempo",
        style = MaterialTheme.typography.titleMedium
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp,
            Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        WeatherOptions.entries.forEach { opt ->
            SelectableCardVertical(
                text = opt.label,
                selected = weatherOption == opt,
                onClick = { onWeather(opt) },
                modifier = Modifier
                    .height(168.dp)
                    .width(128.dp)
            ) {
                // Emoji
                Text(
                    text = opt.emoji,
                    fontSize = 28.sp
                )
            }
        }
    }
    // Temporada con radio unico
    Spacer(Modifier.height(8.dp))
    Text(
        text = "Época",
        style = MaterialTheme.typography.titleMedium
    )
    Column {
        SeasonOptions.entries.forEach { opt ->
            RoboRangerFormSelectionRadioButton(
                text = opt.label,
                selected = seasonOption == opt,
                onClick = { onSeason(opt) }
            )
        }
    }
    // Fecha con textfield
    Spacer(Modifier.height(8.dp))
    RoboRangerTextField(
        label = "Fecha",
        value = date,
        onValueChange = onDate,
        placeholder = "yyyy-mm-dd"
    )
    // Ubicacion con una fila que contiene dos textfields numericos de latitud y longitud
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        RoboRangerTextField(
            label = "Latitud",
            value = latitude,
            onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) onLatitude(it) },
            placeholder = "Introduzca la latitud",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
        RoboRangerTextField(
            label = "Longitud",
            value = longitude,
            onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) onLongitude(it) },
            placeholder = "Introduzca la longitud",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
    }
    // Mediciones de temperatura con una fila que contiene dos textfields de min y max
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        RoboRangerTextField(
            label = "T. Máx (°C)",
            value = tMax,
            onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) onTMax(it) },
            placeholder = "Introduzca la temperatura en centigrados",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
        RoboRangerTextField(
            label = "T. Mín (°C)",
            value = tMin,
            onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) onTMin(it) },
            placeholder = "Introduzca la temperatura en centigrados",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
    }
    // Mediciones de humedad con textfield
    RoboRangerTextField(
        label = "Humedad Máx (%)",
        value = hMax,
        onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) onHMax(it) },
        placeholder = "Introduzca la humedad en porcentaje",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    // Tipo de Registro a llenar con radio unico (Form1 o Form7)
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
    transect: String, onTransect: (String) -> Unit,
    animalOption: AnimalOptions?, onAnimalOptions: (AnimalOptions) -> Unit,
    commonName: String, onCommonName: (String) -> Unit,
    scientificName: String, onScientificName: (String) -> Unit,
    individuals: String, onIndividuals: (String) -> Unit,
    observationsTypeOption: ObservationsTypeOptions?, onObservationsTypeOptions: (ObservationsTypeOptions) -> Unit,
    observations: String, onObservations: (String) -> Unit
) {
    Text(
        text = "Fauna en Transectos",
        style = MaterialTheme.typography.titleLarge
    )
    // Transecto recorrido con textfield
    RoboRangerTextField(
        label = "Número de Transecto",
        value = transect,
        onValueChange = onTransect,
        placeholder = "Introduzca el transecto recorrido"
    )
    // Tipo de animal con tarjetas verticales
    Spacer(Modifier.height(8.dp))
    Text("Tipo de Animal", style = MaterialTheme.typography.titleMedium)
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp,
            Alignment.CenterHorizontally),
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AnimalOptions.entries.forEach { opt ->
            SelectableCardVertical(
                text = opt.label,
                selected = animalOption == opt,
                onClick = { onAnimalOptions(opt) },
                modifier = Modifier
                    .height(168.dp)
                    .width(128.dp)
            ) {
                // Emoji
                Text(
                    text = opt.emoji,
                    fontSize = 28.sp
                )
            }
        }
    }
    // Nombre comun del animal con textfield
    RoboRangerTextField(
        label = "Nombre Común",
        value = commonName,
        onValueChange = onCommonName,
        placeholder = "Introduzca el nombre del animal"
    )
    // Nombre cientifico del animal con textfield
    RoboRangerTextField(
        label = "Nombre Científico (opcional)",
        value = scientificName,
        onValueChange = onScientificName,
        placeholder = "Introduzca el nombre científico"
    )
    // Numero de animales avistados con textfield numerico
    RoboRangerTextField(
        label = "Número de Individuos",
        value = individuals,
        onValueChange = { if (it.isEmpty() || it.toIntOrNull() != null) onIndividuals(it) },
        placeholder = "Introduzca el numero de individuos",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
    )
    // Tipo de observacion con textfield
    Spacer(Modifier.height(8.dp))
    Text("Tipo de Observación", style = MaterialTheme.typography.titleMedium)
    Column {
        ObservationsTypeOptions.entries.forEach { opt ->
            RoboRangerFormSelectionRadioButton(
                text = opt.label,
                selected = observationsTypeOption == opt,
                onClick = { onObservationsTypeOptions(opt) }
            )
        }
    }
    // Observaciones del encuentro con textfield multilineal
    RoboRangerFormMultiLineTextField(
        label = "Observaciones",
        value = observations,
        onValueChange = onObservations,
        placeholder = "Introduzca las observaciones realizadas...",
        modifier = Modifier.heightIn(min = 120.dp)
    )
}

@Composable
fun Form7EntryBody(
    zoneOptions: ZoneOptions?, onZone: (ZoneOptions) -> Unit,
    pluviosity: String, onPluviosity: (String) -> Unit,
    ravineLevel: String, onRavineLevel: (String) -> Unit
) {
    Text(
        text = "Variables Climáticas",
        style = MaterialTheme.typography.titleLarge
    )
    // Zona analizada con radio unico
    Text(
        text = "Zona",
        style = MaterialTheme.typography.titleMedium
    )
    Column {
        ZoneOptions.entries.forEach { opt ->
            RoboRangerFormSelectionRadioButton(
                text = opt.label,
                selected = zoneOptions == opt,
                onClick = { onZone(opt) }
            )
        }
    }
    // Fila que contiene la pluviosidad y nivel de quebrada con textfields numericos
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        RoboRangerTextField(
            label = "Pluviosidad (mm)",
            value = pluviosity,
            onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) onPluviosity(it) },
            placeholder = "Introduzca el rango de pluviosidad",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
        RoboRangerTextField(
            label = "Nivel quebrada (mt)",
            value = ravineLevel,
            onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) onRavineLevel(it) },
            placeholder = "Introduzca el nivel de quebrada",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )
    }
}