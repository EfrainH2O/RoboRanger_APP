package com.example.roboranger.ui.views.form

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roboranger.data.remote.dto.Form1RequestDto
import com.example.roboranger.data.remote.dto.Form7RequestDto
import com.example.roboranger.domain.model.FormUiState
import com.example.roboranger.domain.model.ImageState
import com.example.roboranger.domain.usecase.GetLatestImageStateUseCase
import com.example.roboranger.domain.usecase.SubmitForm1UseCase
import com.example.roboranger.domain.usecase.SubmitForm7UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor(
    private val submitForm1UseCase: SubmitForm1UseCase,
    private val submitForm7UseCase: SubmitForm7UseCase,
    private val getLatestImageStateUseCase: GetLatestImageStateUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<FormUiState>(FormUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _imageState = MutableStateFlow<ImageState>(ImageState.NotAvailable)
    val imageState: StateFlow<ImageState> = _imageState.asStateFlow()

    init {
        viewModelScope.launch {
            getLatestImageStateUseCase().collect { state ->
                _imageState.value = state
            }
        }
    }

    // Helpers para formatear unidades de los campos numericos
    private fun tempStr(value: Double) = "%.2f °C".format(value) // 32.43 °C
    private fun humStr(value: Double)  = "%.2f %%".format(value) // 80.00 %
    private fun pluStr(value: Double)  = "%.2f mm".format(value) // 23.54 mm
    private fun lvlStr(value: Double)  = "%.0f mt".format(value) // 12 mt

    // Helper para convertir URI a ByteArray y validar tamano
    private fun getBytesFromUri(uri: Uri, maxSizeMb: Int = 10) : ByteArray? {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val bytes = inputStream.use { it.readBytes() }

        // Validar el tamano del archivo que sea menor a 10 MB
        if (bytes.size > maxSizeMb * 1024 * 1024) {
            // Lanza una excepcion de tamano
            throw Exception("El archivo no puede superar los $maxSizeMb MB")
        }
        return bytes
    }

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
        date: String,
        imageUri: Uri
    ) {
        val currentImageState = _imageState.value

        when (currentImageState) {
            is ImageState.NotAvailable -> {
                // No hay imagen, no se puede enviar. Muestra un error.
                _uiState.value = FormUiState.Error("No se ha capturado ninguna imagen.")
            }
            is ImageState.Available -> {
                val imageUri: Uri = currentImageState.uri

                viewModelScope.launch {
                    try {
                        _uiState.value = FormUiState.Loading
                        val imageBytes = getBytesFromUri(imageUri)
                            ?: throw Exception("No se pudo procesar la imagen.")

                        val metaData = Form1RequestDto(
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
                        val resp = submitForm1UseCase(imageBytes, metaData)
                        _uiState.value = FormUiState.Success(resp)
                    } catch (e: Exception) {
                        _uiState.value = FormUiState.Error(e.message ?: "Error desconocido")
                    }
                }
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
        date: String,
        imageUri: Uri
    ) {
        val currentImageState = _imageState.value

        when (currentImageState) {
            is ImageState.NotAvailable -> {
                // No hay imagen, no se puede enviar. Muestra un error.
                _uiState.value = FormUiState.Error("No se ha capturado ninguna imagen.")
            }
            is ImageState.Available -> {
                val imageUri: Uri = currentImageState.uri

                viewModelScope.launch {
                    try {
                        _uiState.value = FormUiState.Loading
                        val imageBytes = getBytesFromUri(imageUri)
                            ?: throw Exception("No se pudo procesar la imagen.")

                        val metaData = Form7RequestDto(
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
                        val resp = submitForm7UseCase(imageBytes, metaData)
                        _uiState.value = FormUiState.Success(resp)
                    } catch (e: Exception) {
                        _uiState.value = FormUiState.Error(e.message ?: "Error desconocido")
                    }
                }
            }
        }
    }

    // Funcion para resetear el estado del formulario
    fun resetUiState() {
        _uiState.value = FormUiState.Idle
    }
}