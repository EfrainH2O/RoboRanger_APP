package com.example.roboranger.ui.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RoboRangerFormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(labelText) },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true
    )
}

@Composable
fun RoboRangerFormMultiLineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(labelText) },
        singleLine = false,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 120.dp)
    )
}

// Posibles estados con Preview

@Preview(showBackground = true, name = "Single-Line Text Field")
@Composable
private fun RoboRangerFormTextField() {

    RoboRangerFormTextField(
        value = "Campo vacio",
        onValueChange = {},
        labelText = "Nombre de etiqueta",
        modifier = Modifier.padding(16.dp)
    )
}



@Preview(showBackground = true, name = "Single-Line Text Field")
@Composable
private fun RoboRangerFormTextFieldEmpty() {
    RoboRangerFormTextField(
        value = "",
        onValueChange = {},
        labelText = "Campo vacío",
        modifier = Modifier.padding(16.dp)
    )
}


@Preview(showBackground = true, name = "Multi-Line Text Field")
@Composable
private fun RoboRangerFormMultiLineTextField() {
    RoboRangerFormMultiLineTextField(
        value = "",
        onValueChange = {},
        labelText = "Campo vacío",
        modifier = Modifier.padding(16.dp)
    )
}

