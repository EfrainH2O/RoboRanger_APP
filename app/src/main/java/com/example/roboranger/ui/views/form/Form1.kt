package com.example.roboranger.ui.views.form

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roboranger.R
import com.example.roboranger.data.Formulario1
import com.example.roboranger.data.TipoAnimal
import com.example.roboranger.data.TipoObservacion
import com.example.roboranger.ui.components.RoboRangerFormMultiLineTextField
import com.example.roboranger.ui.components.RoboRangerFormTextField

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Formulario1Content(
    formData: Formulario1,
    onDataChange: (Formulario1) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Número de Transecto
        RoboRangerFormTextField(
            value = formData.transecto,
            onValueChange = { onDataChange(formData.copy(transecto = it)) },
            labelText = "Número de Transecto",
            keyboardType = KeyboardType.Number
        )

        // Tipo de Animal
        Text("Tipo de Animal", fontWeight = FontWeight.Bold)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            maxItemsInEachRow = 3
        ) {
            val animalOptions = listOf("Mamífero", "Ave", "Reptil", "Anfibio", "Insecto")
            val animalIcons = mapOf(
                "Mamífero" to R.drawable.ic_mamifero,
                "Ave" to R.drawable.ic_ave,
                "Reptil" to R.drawable.ic_reptil,
                "Anfibio" to R.drawable.ic_anfibio,
                "Insecto" to R.drawable.ic_insecto
            )

            animalOptions.forEach { animal ->
                Box(
                    modifier = Modifier
                        .size(width = 110.dp, height = 140.dp)
                        .border(
                            width = 2.dp,
                            color = if (formData.tipoAnimal?.name == animal.toAnimalNam()) Color(0xFF4E7029) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.clickable { onDataChange(formData.copy(tipoAnimal = stringToAnimal(animal))) }
                    ) {
                        Image(
                            painter = painterResource(id = animalIcons[animal]!!),
                            contentDescription = animal,
                            modifier = Modifier.size(70.dp)
                        )
                        Text(
                            text = animal,
                            fontSize = 12.sp,
                            fontWeight = if (formData.tipoAnimal?.name == animal.toAnimalNam()) FontWeight.Bold else FontWeight.Normal,
                            color = if (formData.tipoAnimal?.name == animal.toAnimalNam()) Color(0xFF4E7029) else Color.Black,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }

        // Nombre Común
        RoboRangerFormTextField(
            value = formData.nombreComun,
            onValueChange = { onDataChange(formData.copy(nombreComun = it)) },
            labelText = "Nombre Común"
        )

        // Nombre Científico
        RoboRangerFormTextField(
            value = formData.nombreCientifico,
            onValueChange = { onDataChange(formData.copy(nombreCientifico = it)) },
            labelText = "Nombre Científico"
        )

        // Número de Individuos
        RoboRangerFormTextField(
            value = formData.numeroIndividuos,
            onValueChange = { onDataChange(formData.copy(numeroIndividuos = it)) },
            labelText = "Número de Individuos",
            keyboardType = KeyboardType.Number
        )

        // Tipo de Observación
        Text("Tipo de Observación", fontWeight = FontWeight.Bold)
        Column {
            listOf("La Vió", "Huella", "Rastro", "Cacería", "Le dijeron").forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    RadioButton(
                        selected = formData.tipoObservacion?.name == option.toObservacionNam(),
                        onClick = { onDataChange(formData.copy(tipoObservacion = stringToObservacion(option))) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF4E7029),
                            unselectedColor = Color.Gray
                        )
                    )
                    Text(option, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        // Observaciones
        RoboRangerFormMultiLineTextField(
            value = formData.observaciones,
            onValueChange = { onDataChange(formData.copy(observaciones = it)) },
            labelText = "Observaciones"
        )

    }
}

// Helper functions para convertir strings a enums
private fun stringToAnimal(value: String): TipoAnimal? {
    return when (value) {
        "Mamífero" -> TipoAnimal.MAMIFERO
        "Ave" -> TipoAnimal.AVE
        "Reptil" -> TipoAnimal.REPTIL
        "Anfibio" -> TipoAnimal.ANFIBIO
        "Insecto" -> TipoAnimal.INSECTO
        else -> null
    }
}

private fun stringToObservacion(value: String): TipoObservacion? {
    return when (value) {
        "La Vió" -> TipoObservacion.DIRECTA
        "Huella" -> TipoObservacion.HUELLA
        "Rastro" -> TipoObservacion.RASTRO
        "Cacería" -> TipoObservacion.CACERIA
        "Le dijeron" -> TipoObservacion.INDIRECTA
        else -> null
    }
}

private fun String.toAnimalNam(): String = when (this) {
    "Mamífero" -> "MAMIFERO"
    "Ave" -> "AVE"
    "Reptil" -> "REPTIL"
    "Anfibio" -> "ANFIBIO"
    "Insecto" -> "INSECTO"
    else -> ""
}

private fun String.toObservacionNam(): String = when (this) {
    "La Vió" -> "DIRECTA"
    "Huella" -> "HUELLA"
    "Rastro" -> "RASTRO"
    "Cacería" -> "CACERIA"
    "Le dijeron" -> "INDIRECTA"
    else -> ""
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 800,
    showSystemUi = true,
    name = "Formulario 1 Completo"
)
@Composable
fun Formulario1ContentPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        val previewFormData = Formulario1(
            transecto = "123-A",
            tipoAnimal = TipoAnimal.MAMIFERO,
            nombreComun = "Oso de anteojos",
            nombreCientifico = "Tremarctos ornatus",
            numeroIndividuos = "2",
            tipoObservacion = TipoObservacion.DIRECTA,
            observaciones = "Un adulto y una cría."
        )

        Formulario1Content(
            formData = previewFormData,
            onDataChange = { },
        )
    }
}
