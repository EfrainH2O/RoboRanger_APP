package com.example.roboranger.ui.views.staticForm

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roboranger.data.local.Room.Forms_1
import com.example.roboranger.data.local.Room.Forms_2
import com.example.roboranger.data.remote.dto.Form1RequestDto
import com.example.roboranger.data.remote.dto.Form7RequestDto
import com.example.roboranger.domain.model.FormRetrieveState
import com.example.roboranger.domain.usecase.SubmitForm1UseCase
import com.example.roboranger.domain.usecase.SubmitForm7UseCase
import com.example.roboranger.domain.usecase.local_data.GetFormByIdUseCase
import com.example.roboranger.domain.usecase.local_data.MarkFormAsSentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StaticFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getFormByIdUseCase: GetFormByIdUseCase,
    private val submitForm1UseCase: SubmitForm1UseCase,
    private val submitForm7UseCase: SubmitForm7UseCase,
    private val markFormAsSentUseCase: MarkFormAsSentUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _formState = MutableStateFlow<FormRetrieveState>(FormRetrieveState.Loading)
    val formState = _formState.asStateFlow()
    var formId :Int? = null
    var formType : Int? = null
    val uploadedState = mutableStateOf(false)


    init {
        // Retrieve the arguments from SavedStateHandle
        formId = savedStateHandle[FormDetailDestination.FORM_ID_ARG]
        formType = savedStateHandle[FormDetailDestination.FORM_TYPE_ARG]

        if (formId != null && formType != null) {
            loadFormDetails(formId!!, formType!!)
        } else {
            _formState.value = FormRetrieveState.Error("ID o tipo de formulario no válidos.")
        }
    }

    private fun loadFormDetails(id: Int, type: Int) {
        viewModelScope.launch {
            _formState.value = FormRetrieveState.Loading
            getFormByIdUseCase.invoke(id, type).collect { update ->
                _formState.value = update
            }
            val currentState = _formState.value
            when(currentState){
                is FormRetrieveState.SuccessForm1 -> uploadedState.value = currentState.form.enviado
                is FormRetrieveState.SuccessForm2 -> uploadedState.value = currentState.form.enviado
                is FormRetrieveState.Error -> {}
                FormRetrieveState.Loading -> {}
            }
        }
    }

    fun submitCurrentForm() {
        val currentState = _formState.value
        if (formId == null) {
            _formState.update { FormRetrieveState.Error("ID de formulario no disponible.") }
            return
        }

        viewModelScope.launch {
            _formState.update { FormRetrieveState.Loading } // Show loading state
            try {
                when (currentState) {
                    is FormRetrieveState.SuccessForm1 -> {
                        val form = currentState.form
                        val imageBytes = context.contentResolver.openInputStream(form.imagen)?.use { it.readBytes() }
                            ?: throw Exception("No se pudo leer la imagen local.")

                        // Convert entity to DTO and submit
                        val metaData = form.toRequestDto()
                        val response = submitForm1UseCase(imageBytes, metaData)

                        // Mark as sent in the database
                        markFormAsSentUseCase(formId!!, 1)

                        // Reload the details to show the updated "enviado" status
                        loadFormDetails(formId!!, 1)
                    }

                    is FormRetrieveState.SuccessForm2 -> {
                        val form = currentState.form
                        val imageBytes = context.contentResolver.openInputStream(form.imagen)?.use { it.readBytes() }
                            ?: throw Exception("No se pudo leer la imagen local.")

                        // Convert entity to DTO and submit
                        val metaData = form.toRequestDto()
                        val response = submitForm7UseCase(imageBytes, metaData)

                        // Mark as sent in the database
                        markFormAsSentUseCase(formId!!, 2)

                        // Reload the details to show the updated "enviado" status
                        loadFormDetails(formId!!, 2)
                    }
                    else -> {
                        // This case handles if the button is clicked during Loading/Error state
                        throw IllegalStateException("No se puede enviar un formulario que no se ha cargado correctamente.")
                    }
                }
            } catch (e: Exception) {
                _formState.update { FormRetrieveState.Error("Error al enviar: ${e.message}") }
                Log.e("FormsSender", "Error al enviar: ${e.message}")
            }
        }
    }

}

private fun tempStr(value: Double) = "%.2f °C".format(value)
private fun humStr(value: Double) = "%.2f %%".format(value)
private fun pluStr(value: Double) = "%.2f mm".format(value)
private fun lvlStr(value: Double) = "%.0f mt".format(value)



fun Forms_1.toRequestDto(): Form1RequestDto {
    return Form1RequestDto(
        transect = this.transecto,
        weather = this.clima.label,
        season = this.temporada.label,
        animalType = this.tipo_animal.label,
        commonName = this.nombre_comun,
        scientificName = this.nombre_cientifico,
        individuals = this.numero_individuo.toString(),
        observationsType = this.tipo_observacion.label,
        observations = this.observaciones,
        latitude = this.latitude,
        longitude = this.longitude,
        maxTemp = tempStr(this.temp_maxima),
        maxHum = humStr(this.humedad_max),
        minTemp = tempStr(this.temp_minima),
        date = this.fecha
    )
}

// Converts a Forms_2 entity into a Form7RequestDto for the API
fun Forms_2.toRequestDto(): Form7RequestDto {
    return Form7RequestDto(
        weather = this.clima.label,
        season = this.temporada.label,
        zone = this.zona.label,
        pluviosity = pluStr(this.pluviosidad),
        maxTemp = tempStr(this.temperaturamaxima),
        maxHum = humStr(this.humedadmaxima),
        minTemp = tempStr(this.temperaturaminima),
        ravineLevel = lvlStr(this.nivelquebrada),
        latitude = this.latitude,
        longitude = this.longitude,
        date = this.fecha
    )
}
