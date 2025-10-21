package com.example.roboranger.ui.views.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roboranger.data.remote.dto.Form1RequestDto
import com.example.roboranger.data.remote.dto.Form7RequestDto
import com.example.roboranger.domain.model.FormUiState
import com.example.roboranger.domain.usecase.SubmitForm1UseCase
import com.example.roboranger.domain.usecase.SubmitForm7UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor(
    private val submitForm1UseCase: SubmitForm1UseCase,
    private val submitForm7UseCase: SubmitForm7UseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FormUiState>(FormUiState.Idle)
    val uiState = _uiState.asStateFlow()

    // Helpers para formatear unidades de los campos numericos
    private fun tempStr(value: Double) = "%.2f °C".format(value) // 32.43 °C
    private fun humStr(value: Double)  = "%.2f %%".format(value) // 80.00 %
    private fun pluStr(value: Double)  = "%.2f mm".format(value) // 23.54 mm
    private fun lvlStr(value: Double)  = "%.0f mt".format(value) // 12 mt

    // Funcion para enviar el Formulario 1 construyendolo a partir de los datos de la UI con strings/unidades
    fun submitForm1(
        transect: String,
        weather: String,
        season: String,
        animalType: String,
        commonName: String,
        scientificName: String,
        individuals: Int,
        observationsType: String,
        observations: String,
        latitude: Double,
        longitude: Double,
        maxTemp: Double,
        maxHum: Double,
        minTemp: Double,
        date: String
    ) {
        val body = Form1RequestDto(
            transect = transect,
            weather = weather,
            season = season,
            animalType = animalType,
            commonName = commonName,
            scientificName = scientificName,
            individuals = individuals.toString(),
            observationsType = observationsType,
            observations = observations,
            latitude = latitude,
            longitude = longitude,
            maxTemp = tempStr(maxTemp),
            maxHum = humStr(maxHum),
            minTemp = tempStr(minTemp),
            date = date
        )

        _uiState.value = FormUiState.Loading
        viewModelScope.launch {
            try {
                val resp = submitForm1UseCase(body)
                _uiState.value = FormUiState.Success(resp)
            } catch (e: Exception) {
                _uiState.value = FormUiState.Error(e.message ?: "Error de red")
            }
        }
    }

    // Funcion para enviar el Formulario 7 construyendolo a partir de los datos de la UI con strings/unidades
    fun submitForm7(
        weather: String,
        season: String,
        zone: String,
        pluviosity: Double,
        maxTemp: Double,
        maxHum: Double,
        minTemp: Double,
        ravineLevel: Double,
        latitude: Double,
        longitude: Double,
        date: String
    ) {
        val body = Form7RequestDto(
            weather = weather,
            season = season,
            zone = zone,
            pluviosity = pluStr(pluviosity),
            maxTemp = tempStr(maxTemp),
            maxHum = humStr(maxHum),
            minTemp = tempStr(minTemp),
            ravineLevel = lvlStr(ravineLevel),
            latitude = latitude,
            longitude = longitude,
            date = date
        )

        _uiState.value = FormUiState.Loading
        viewModelScope.launch {
            try {
                val resp = submitForm7UseCase(body)
                _uiState.value = FormUiState.Success(resp)
            } catch (e: Exception) {
                _uiState.value = FormUiState.Error(e.message ?: "Error de red")
            }
        }
    }

    // Funcion para resetear el estado del formulario
    fun resetUiState() {
        _uiState.value = FormUiState.Idle
    }
}