package com.example.roboranger.ui.views.form

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roboranger.data.local.Room.Forms_1
import com.example.roboranger.data.local.Room.Forms_2
import com.example.roboranger.data.remote.dto.Form1RequestDto
import com.example.roboranger.data.remote.dto.Form7RequestDto
import com.example.roboranger.domain.model.FormUiState
import com.example.roboranger.domain.model.ImageState
import com.example.roboranger.domain.usecase.GetLatestImageStateUseCase
import com.example.roboranger.domain.usecase.SubmitForm1UseCase
import com.example.roboranger.domain.usecase.SubmitForm7UseCase
import com.example.roboranger.domain.usecase.control.GetLocationUseCase
import com.example.roboranger.domain.usecase.local_data.InsertForm1UseCase
import com.example.roboranger.domain.usecase.local_data.InsertForm2UseCase
import com.example.roboranger.domain.usecase.local_data.MarkFormAsSentUseCase
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor(
    private val submitForm1UseCase: SubmitForm1UseCase,
    private val submitForm7UseCase: SubmitForm7UseCase,
    private val insertForm1UseCase: InsertForm1UseCase,
    private val insertForm2UseCase: InsertForm2UseCase,
    private val getLatestImageStateUseCase: GetLatestImageStateUseCase,
    private val getLocationUseCase: GetLocationUseCase,
    private val markFormAsSentUseCase: MarkFormAsSentUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<FormUiState>(FormUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _imageState = MutableStateFlow<ImageState>(ImageState.NotAvailable)
    val imageState: StateFlow<ImageState> = _imageState.asStateFlow()

    //Forms use tstate
    private val _sharedFormState = MutableStateFlow(SharedFormState())
    val sharedFormState = _sharedFormState.asStateFlow()
    fun onSharedFormChange(newState: SharedFormState) {
        _sharedFormState.value = newState
    }


    private val _form1State = MutableStateFlow(Form1State())
    val form1State = _form1State.asStateFlow()
    fun onForm1Change(newState: Form1State) {
        _form1State.value = newState
    }

    private val _form7State = MutableStateFlow(Form7State())
    val form7State = _form7State.asStateFlow()
    fun onForm7Change(newState: Form7State) {
        _form7State.value = newState
    }


    private fun updateLocation(lat: String, lon: String) {
        _sharedFormState.update { it.copy(latitude = lat, longitude = lon) }
    }

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
    fun submitForm1() {
        val currentImageState = _imageState.value

        when (currentImageState) {
            is ImageState.NotAvailable -> {
                _uiState.value = FormUiState.Error("No se ha capturado ninguna imagen.")
            }

            is ImageState.Available -> {
                val imageUri: Uri = currentImageState.uri

                viewModelScope.launch {
                    try {
                        _uiState.value = FormUiState.Loading
                        val imageBytes = getBytesFromUri(imageUri)
                            ?: throw Exception("No se pudo procesar la imagen.")
                        val metaData = sharedFormState.value.toForm1RequestDto(form1State.value)
                        val form1 = sharedFormState.value.toForms1(form1State.value, imageUri)
                        form1.hora = getCurrentTime()
                        val newID = insertForm1UseCase.invoke(form1)
                        val resp = submitForm1UseCase(imageBytes, metaData)
                        _uiState.value = FormUiState.Success(resp)
                        markFormAsSentUseCase.invoke(newID.toInt(), 1)

                    } catch (e: Exception) {
                        _uiState.value = FormUiState.Error(e.message ?: "Error desconocido")
                    }
                }


            }
        }
    }

    fun saveForm1(){
        val currentImageState = _imageState.value
        when (currentImageState) {
            is ImageState.NotAvailable -> {
                _uiState.value = FormUiState.Error("No se ha capturado ninguna imagen.")
            }

            is ImageState.Available -> {
                val imageUri: Uri = currentImageState.uri

                viewModelScope.launch {
                    try {
                        _uiState.value = FormUiState.Loading
                        val form1 = sharedFormState.value.toForms1(form1State.value, imageUri)
                        form1.hora = getCurrentTime()
                        val newID = insertForm1UseCase.invoke(form1)
                        _uiState.value = FormUiState.Saved
                    } catch (e: Exception) {
                        _uiState.value = FormUiState.Error(e.message ?: "Error desconocido")
                    }
                }
            }
        }
    }

    fun saveForm7(){
        val currentImageState = _imageState.value
        when (currentImageState) {
            is ImageState.NotAvailable -> {
                _uiState.value = FormUiState.Error("No se ha capturado ninguna imagen.")
            }
            is ImageState.Available -> {
                val imageUri: Uri = currentImageState.uri

                viewModelScope.launch {
                    try {
                        _uiState.value = FormUiState.Loading
                        val form7 = sharedFormState.value.toForms2(form7State.value, imageUri)
                        form7.hora = getCurrentTime()
                        val newID = insertForm2UseCase.invoke(form7)

                        _uiState.value = FormUiState.Saved
                    } catch (e: Exception) {
                        _uiState.value = FormUiState.Error(e.message ?: "Error desconocido")
                    }
                }
            }
        }
    }

    // Funcion para enviar el Formulario 7 construyendolo a partir de los datos de la UI con strings/unidades
    fun submitForm7() {
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

                        val metaData = sharedFormState.value.toForm7RequestDto(form7State.value)
                        val form7 = sharedFormState.value.toForms2(form7State.value, imageUri)
                        form7.hora = getCurrentTime()
                        val newID = insertForm2UseCase.invoke(form7)
                        val resp = submitForm7UseCase(imageBytes, metaData)
                        _uiState.value = FormUiState.Success(resp)
                        markFormAsSentUseCase.invoke(newID.toInt(), 2)

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

    private val _isFetchingLocation = MutableStateFlow(false)
    val isFetchingLocation: StateFlow<Boolean> = _isFetchingLocation.asStateFlow()

    fun fetchCurrentLocation() {
        viewModelScope.launch {
            _isFetchingLocation.value = true
            try {
                val location: LatLng? = getLocationUseCase().first()

                if (location != null) {
                    // Actualizar los estados de latitude y longitude con los valores obtenidos
                    updateLocation(location.latitude.toString(), location.longitude.toString())
                } else {
                    // Manejar el caso de error donde no se pudo obtener la ubicacion
                    _uiState.value = FormUiState.Error("No se pudo obtener la ubicacion. Comprueba los permisos y la configuracion del GPS")
                }
            } catch (e: Exception) {
                _uiState.value = FormUiState.Error("Error al obtener la ubicación: ${e.message}")
            } finally {
                _isFetchingLocation.value = false
            }
        }
    }
    private fun getCurrentTime(): String {
        // Get the current time
        val currentTime = LocalTime.now()
        // Define the desired format (e.g., "14:30")
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        // Return the formatted time as a String
        return currentTime.format(formatter)
    }
}

data class SharedFormState(
    val nombre: String = "",
    val season: SeasonOptions? = null,
    val weather: WeatherOptions? = null,
    val date: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val maxTemp: String = "",
    val minTemp: String = "",
    val maxHum: String = ""
)

// A data class for Form 1 specific fields
data class Form1State(
    val transect: String = "",
    val animalType: AnimalOptions? = null,
    val commonName: String = "",
    val scientificName: String = "",
    val individuals: String = "",
    val observationsType: ObservationsTypeOptions? = null,
    val observations: String = ""
)

// A data class for Form 7 specific fields
data class Form7State(
    val zone: ZoneOptions? = null,
    val pluviosity: String = "",
    val ravineLevel: String = ""
)


fun SharedFormState.toForms1(form1State: Form1State, imageUri: Uri): Forms_1 {
    // Basic validation to prevent crashes. Consider more robust validation.
    if (this.season == null || this.weather == null || form1State.animalType == null || form1State.observationsType == null) {
        throw IllegalStateException("Cannot create Forms_1 entity with null enum values.")
    }

    return Forms_1(
        nombre = this.nombre,
        transecto = form1State.transect,
        clima = this.weather,
        temporada = this.season,
        tipo_animal = form1State.animalType,
        nombre_comun = form1State.commonName,
        nombre_cientifico = form1State.scientificName,
        numero_individuo = form1State.individuals.toIntOrNull() ?: 0,
        tipo_observacion = form1State.observationsType,
        observaciones = form1State.observations,
        latitude = this.latitude.toDoubleOrNull() ?: 0.0,
        longitude = this.longitude.toDoubleOrNull() ?: 0.0,
        fecha = this.date,
        hora = "",
        temp_maxima = this.maxTemp.toDoubleOrNull() ?: 0.0,
        temp_minima = this.minTemp.toDoubleOrNull() ?: 0.0,
        humedad_max = this.maxHum.toDoubleOrNull() ?: 0.0,
        enviado = false,
        imagen = imageUri
    )
}

fun SharedFormState.toForm1RequestDto(form1State: Form1State): Form1RequestDto {
    // Basic validation to prevent crashes
    if (this.season == null || this.weather == null || form1State.animalType == null || form1State.observationsType == null) {
        throw IllegalStateException("Cannot create Form1RequestDto with null enum values.")
    }

    // Helper functions to format strings for the backend
    fun tempStr(value: String) = "%.2f °C".format(value.toDoubleOrNull() ?: 0.0)
    fun humStr(value: String)  = "%.2f %%".format(value.toDoubleOrNull() ?: 0.0)

    return Form1RequestDto(
        transect = form1State.transect,
        weather = this.weather.label,
        season = this.season.label,
        animalType = form1State.animalType.label,
        commonName = form1State.commonName,
        scientificName = form1State.scientificName,
        individuals = form1State.individuals,
        observationsType = form1State.observationsType.label,
        observations = form1State.observations,
        latitude = this.latitude.toDoubleOrNull() ?: 0.0,
        longitude = this.longitude.toDoubleOrNull() ?: 0.0,
        maxTemp = tempStr(this.maxTemp),
        maxHum = humStr(this.maxHum),
        minTemp = tempStr(this.minTemp),
        date = this.date
    )
}

fun SharedFormState.toForms2(form7State: Form7State, imageUri: Uri): Forms_2 {
    if (this.season == null || this.weather == null || form7State.zone == null) {
        throw IllegalStateException("Cannot create Forms_2 entity with null enum values.")
    }
    return Forms_2(
        nombre = this.nombre,
        clima = this.weather,
        temporada = this.season,
        zona = form7State.zone,
        pluviosidad = form7State.pluviosity.toDoubleOrNull() ?: 0.0,
        temperaturamaxima = this.maxTemp.toDoubleOrNull() ?: 0.0,
        humedadmaxima = this.maxHum.toDoubleOrNull() ?: 0.0,
        temperaturaminima = this.minTemp.toDoubleOrNull() ?: 0.0,
        nivelquebrada = form7State.ravineLevel.toDoubleOrNull() ?: 0.0,
        latitude = this.latitude.toDoubleOrNull() ?: 0.0,
        longitude = this.longitude.toDoubleOrNull() ?: 0.0,
        fecha = this.date,
        hora = "",
        enviado = false,
        imagen = imageUri
    )
}

fun SharedFormState.toForm7RequestDto(form7State: Form7State): Form7RequestDto {
    if (this.season == null || this.weather == null || form7State.zone == null) {
        throw IllegalStateException("Cannot create Form7RequestDto with null enum values.")
    }
    fun tempStr(value: String) = "%.2f °C".format(value.toDoubleOrNull() ?: 0.0)
    fun humStr(value: String)  = "%.2f %%".format(value.toDoubleOrNull() ?: 0.0)
    fun pluStr(value: String)  = "%.2f mm".format(value.toDoubleOrNull() ?: 0.0)
    fun lvlStr(value: String)  = "%.0f mt".format(value.toDoubleOrNull() ?: 0.0)

    return Form7RequestDto(
        weather = this.weather.label,
        season = this.season.label,
        zone = form7State.zone.label,
        pluviosity = pluStr(form7State.pluviosity),
        maxTemp = tempStr(this.maxTemp),
        maxHum = humStr(this.maxHum),
        minTemp = tempStr(this.minTemp),
        ravineLevel = lvlStr(form7State.ravineLevel),
        latitude = this.latitude.toDoubleOrNull() ?: 0.0,
        longitude = this.longitude.toDoubleOrNull() ?: 0.0,
        date = this.date
    )
}
