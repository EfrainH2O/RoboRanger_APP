package com.example.roboranger.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.roboranger.ui.theme.RoboRangerTheme

@Composable
fun RoboRangerActionButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    backgroundColor: Color = Color(0xFF5C7A3D)
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = Color.White,
            disabledContainerColor = backgroundColor.copy(alpha = 0.5f),
            disabledContentColor = Color.White.copy(alpha = 0.5f)
        ),
        modifier = modifier.height(48.dp)
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}


// --- Previews para visualizar los componentes ---

@Preview(showBackground = true, name = "Botón Guardar")
@Composable
fun SaveButtonPreview() {
    RoboRangerTheme {
        RoboRangerActionButton(
            text = "GUARDAR",
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Botón Enviar")
@Composable
fun SendButtonPreview() {
    RoboRangerTheme {
        RoboRangerActionButton(
            text = "ENVIAR",
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Botón Atrás")
@Composable
fun BackButtonPreview() {
    RoboRangerTheme {
        // Ahora el botón "ATRÁS" usa el mismo estilo que los demás
        RoboRangerActionButton(
            text = "ATRÁS",
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Botón Deshabilitado")
@Composable
fun DisabledButtonPreview() {
    RoboRangerTheme {
        RoboRangerActionButton(
            text = "GUARDAR",
            onClick = {},
            enabled = false
        )
    }
}

@Preview(showBackground = true, name = "Combinación en una Fila")
@Composable
fun ButtonCombinationPreview() {
    RoboRangerTheme {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            // Usamos RoboRangerActionButton para ambos botones
            RoboRangerActionButton(
                modifier = Modifier.weight(1f),
                text = "ATRÁS",
                onClick = {}
            )
            RoboRangerActionButton(
                modifier = Modifier.weight(1f).padding(start = 8.dp),
                text = "CONTINUAR",
                onClick = {}
            )
        }
    }
}
