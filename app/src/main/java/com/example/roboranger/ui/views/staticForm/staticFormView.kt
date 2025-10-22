package com.example.roboranger.ui.views.staticForm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.roboranger.R
import com.example.roboranger.data.local.Room.Forms_1
import com.example.roboranger.data.local.Room.Forms_2
import com.example.roboranger.domain.model.FormRetrieveState
import com.example.roboranger.navigation.NavigationDestination
import com.example.roboranger.ui.components.RoboRangerButton
import com.example.roboranger.ui.components.RoboRangerFormMultiLineTextField
import com.example.roboranger.ui.components.RoboRangerFormSelectionRadioButton
import com.example.roboranger.ui.components.RoboRangerTextField
import com.example.roboranger.ui.components.RoboRangerTopAppBar
import com.example.roboranger.ui.components.SelectableCardVertical
import com.example.roboranger.ui.views.form.AnimalOptions
import com.example.roboranger.ui.views.form.ObservationsTypeOptions
import com.example.roboranger.ui.views.form.SeasonOptions
import com.example.roboranger.ui.views.form.WeatherOptions
import com.example.roboranger.ui.views.form.ZoneOptions

object FormDetailDestination : NavigationDestination {
    override val route = "form_detail"
    override val titleRes = R.string.form_detail_title
    const val FORM_ID_ARG = "formId"
    const val FORM_TYPE_ARG = "formType"
    val routeWithArgs = "$route/{$FORM_TYPE_ARG}/{$FORM_ID_ARG}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaticFormScreen(
    onNavigateUp: () -> Unit,
    onNavigateSettings: () -> Unit,
    viewModel: StaticFormViewModel = hiltViewModel()
) {
    val formState by viewModel.formState.collectAsState()
    val uploadedState by viewModel.uploadedState

    Scaffold(
        topBar = {
            RoboRangerTopAppBar(
                title = stringResource(FormDetailDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp,
                canNavigateSettings = true,
            )
        },
        bottomBar = { if (!uploadedState){
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp)
            ) {
                RoboRangerButton(
                    text = "Subir",
                    enabled = true,
                    modifier = Modifier.weight(0.5f)
                ) {
                    viewModel.submitCurrentForm()
                }
            }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (val state = formState) {
                is FormRetrieveState.Loading -> CircularProgressIndicator()
                is FormRetrieveState.Error -> Text(state.message)
                is FormRetrieveState.SuccessForm1 -> {
                    Form1DetailBody(state.form)
                }
                is FormRetrieveState.SuccessForm2 -> {
                    Form2DetailedBody(state.form)
                }
            }
        }
    }
}

@Composable
fun Form1DetailBody(form: Forms_1) {
    Column(
        Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Datos Generales",
            style = MaterialTheme.typography.titleLarge
        )
        RoboRangerTextField(
            label = "Nombre",
            value = form.nombre,
            onValueChange = {},
            enabled = false,
            placeholder = "Nombre",
        )
        // Clima con tarjetas horizontales
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
                    selected = form.clima == opt,
                    onClick = {},
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
                    selected = form.temporada == opt,
                    onClick = {}

                )
            }
        }
        // Fecha con textfield
        Spacer(Modifier.height(8.dp))
        RoboRangerTextField(
            label = "Fecha",
            value = form.fecha,
            onValueChange = {},
            enabled = false,
            placeholder = "Fecha"
        )
        // Ubicacion con una fila que contiene dos textfields numericos de latitud y longitud
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            RoboRangerTextField(
                label = "Latitud",
                value = form.latitude.toString(),
                onValueChange = {},
                enabled = false,
                modifier = Modifier.weight(1f),
                placeholder = "Latitud"
            )
            RoboRangerTextField(
                label = "Longitud",
                value = form.longitude.toString(),
                onValueChange = {},
                enabled = false,
                modifier = Modifier.weight(1f),
                placeholder = "Latitud"
            )
        }
        // Mediciones de temperatura con una fila que contiene dos textfields de min y max
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            RoboRangerTextField(
                label = "T. Máx (°C)",
                value = form.temp_maxima.toString(),
                onValueChange = {},
                enabled = false,
                modifier = Modifier.weight(1f),
                placeholder = "Temperatura Maxima"
            )
            RoboRangerTextField(
                label = "T. Mín (°C)",
                value = form.temp_minima.toString(),
                onValueChange = {},
                enabled = false,
                modifier = Modifier.weight(1f),
                placeholder = "Temperatura minima"
            )
        }
        // Mediciones de humedad con textfield
        RoboRangerTextField(
            label = "Humedad Máx (%)",
            value = form.humedad_max.toString(),
            onValueChange = {},
            enabled = false,
            placeholder = "Humedad maxima"
        )

        Text(
            text = "Fauna en Transectos",
            style = MaterialTheme.typography.titleLarge
        )
        RoboRangerTextField(
            label = "Número de Transecto",
            value = form.transecto,
            onValueChange = {},
            enabled = false,
            placeholder = "Transecto"
        )
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
                    selected = form.tipo_animal == opt,
                    onClick = {},
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
            value = form.nombre_comun,
            onValueChange = {},
            enabled = false,
            placeholder = "Nombre Comun"
        )
        RoboRangerTextField(
            label = "Nombre Científico (opcional)",
            value = form.nombre_cientifico,
            onValueChange = {},
            enabled = false,
            placeholder = "Nombre Cientifico"
        )
        RoboRangerTextField(
            label = "Número de Individuos",
            value = form.numero_individuo.toString(),
            onValueChange = {},
            enabled = false,
            placeholder = "Cantidad de individuos"
        )
        Spacer(Modifier.height(8.dp))
        Text("Tipo de Observación", style = MaterialTheme.typography.titleMedium)
        Column {
            ObservationsTypeOptions.entries.forEach { opt ->
                RoboRangerFormSelectionRadioButton(
                    text = opt.label,
                    selected = form.tipo_observacion == opt,
                    onClick = {}
                )
            }
        }
        RoboRangerFormMultiLineTextField(
            label = "Observaciones",
            value = form.observaciones,
            onValueChange = {},
            enabled = false ,
            placeholder = "Obervacion"
        )
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
                AsyncImage(
                    model = form.imagen,
                    contentDescription = "Imagen del formulario",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

    }
}

@Composable
fun Form2DetailedBody(form: Forms_2) {
    Column(
        Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Datos Generales",
            style = MaterialTheme.typography.titleLarge
        )
        RoboRangerTextField(
            label = "Nombre",
            value = form.nombre,
            onValueChange = {},
            enabled = false,
            placeholder = "Nombre",
        )
        // Clima con tarjetas horizontales
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
                    selected = form.clima == opt,
                    onClick = {},
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
                    selected = form.temporada == opt,
                    onClick = {}

                )
            }
        }
        // Fecha con textfield
        Spacer(Modifier.height(8.dp))
        RoboRangerTextField(
            label = "Fecha",
            value = form.fecha,
            onValueChange = {},
            enabled = false,
            placeholder = "Fecha"
        )
        // Ubicacion con una fila que contiene dos textfields numericos de latitud y longitud
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            RoboRangerTextField(
                label = "Latitud",
                value = form.latitude.toString(),
                onValueChange = {},
                enabled = false,
                modifier = Modifier.weight(1f),
                placeholder = "Latitud"
            )
            RoboRangerTextField(
                label = "Longitud",
                value = form.longitude.toString(),
                onValueChange = {},
                enabled = false,
                modifier = Modifier.weight(1f),
                placeholder = "Latitud"
            )
        }
        // Mediciones de temperatura con una fila que contiene dos textfields de min y max
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            RoboRangerTextField(
                label = "T. Máx (°C)",
                value = form.temperaturamaxima.toString(),
                onValueChange = {},
                enabled = false,
                modifier = Modifier.weight(1f),
                placeholder = "Temperatura Maxima"
            )
            RoboRangerTextField(
                label = "T. Mín (°C)",
                value = form.temperaturaminima.toString(),
                onValueChange = {},
                enabled = false,
                modifier = Modifier.weight(1f),
                placeholder = "Temperatura minima"
            )
        }
        // Mediciones de humedad con textfield
        RoboRangerTextField(
            label = "Humedad Máx (%)",
            value = form.humedadmaxima.toString(),
            onValueChange = {},
            enabled = false,
            placeholder = "Humedad maxima"
        )

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
                    selected = form.zona == opt,
                    onClick = {}
                )
            }
        }
        // Fila que contiene la pluviosidad y nivel de quebrada con textfields numericos
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            RoboRangerTextField(
                label = "Pluviosidad (mm)",
                value = form.pluviosidad.toString(),
                onValueChange = {},
                enabled = false,
                modifier = Modifier.weight(1f),
                placeholder = "Pluviosidad"
            )
            RoboRangerTextField(
                label = "Nivel quebrada (mt)",
                value = form.nivelquebrada.toString(),
                onValueChange = {},
                enabled = false,
                modifier = Modifier.weight(1f),
                placeholder = "Nivel Quebrada"
            )
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
                AsyncImage(
                    model = form.imagen,
                    contentDescription = "Imagen del formulario",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Spacer(Modifier.height(96.dp))
    }
}
