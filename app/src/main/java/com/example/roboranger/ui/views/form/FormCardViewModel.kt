package com.example.roboranger.ui.views.form

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.roboranger.data.FormCard

class FormCardViewModel : ViewModel() {

    // Lista mutable de cartas de formularios, se actualiza la ui cuando cambia
    val formList = mutableStateOf<List<FormCard>>(emptyList())

    // Contador para generar ids
    private var formCounter = 0

    // Función para agregar una nueva carta de formulario, se llamaria
    // cuando se termine de llenar un formulario
    fun addForm(place: String, date: String, hour: String, status: Int) {
        val newForm = FormCard(
            id = formCounter++,
            place = place,
            date = date,
            hour = hour,
            status = status // 0 = enviado, 1 = guardado
        )

        // Se añade la nueva carta a la lista
        formList.value = formList.value + newForm
    }


    init {
        loadDummyForms()
    }

    // Función temporal para crear datos de prueba
    private fun loadDummyForms() {
        val dummyData = listOf(
            FormCard(
                id = 1,
                place = "Norte de Hogar",
                date = "2024-09-15",
                hour = "10:00",
                status = 0
            ),
            FormCard(id = 2, place = "Montaña", date = "2024-09-14", hour = "11:00", status = 0),
            FormCard(id = 3, place = "Hogar", date = "2024-09-14", hour = "12:00", status = 1),
            FormCard(id = 4, place = "Parque X", date = "2024-09-12", hour = "13:00", status = 1),
            FormCard(
                id = 5,
                place = "Paraguacutinguimicuaro",
                date = "2024-09-11",
                hour = "14:00",
                status = 0
            ),
            FormCard(
                id = 6,
                place = "Popocatepetl",
                date = "2024-09-10",
                hour = "15:00",
                status = 0
            )
        )

        formList.value = dummyData
    }
}