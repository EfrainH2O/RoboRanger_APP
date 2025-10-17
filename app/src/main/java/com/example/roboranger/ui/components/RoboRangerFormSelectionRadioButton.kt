package com.example.roboranger.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource


@Composable
fun RoboRangerFormSelectionRadioButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true, name = "Radio Button - Selected")
@Composable
private fun RoboRangerFormSelectionRadioButtonSelectedPreview() {
    RoboRangerFormSelectionRadioButton(
        text = "",
        selected = true,
        onClick = {},
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Preview(showBackground = true, name = "Radio Button - Not Selected")
@Composable
private fun RoboRangerFormSelectionRadioButtonNotSelectedPreview() {
    RoboRangerFormSelectionRadioButton(
        text = "",
        selected = false,
        onClick = {},
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

// Tipo 1: Tarjeta Vertical (Tipo de Animal)
@Composable
fun SelectableCardVertical(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .border(
                width = 2.dp,
                color = if (selected) Color(0xFF4CAF50) else Color(0xFFCCCCCC),
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = if (selected) Color(0xFFF1F8F4) else Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}

// Tipo 2: Tarjeta Horizontal (Estado del Tiempo)
@Composable
fun SelectableCardHorizontal(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .border(
                width = 2.dp,
                color = if (selected) Color(0xFF4CAF50) else Color(0xFFCCCCCC),
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = if (selected) Color(0xFFF1F8F4) else Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(80.dp),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Preview(showBackground = true, name = "Card Vertical - Selected")
@Composable
private fun SelectableCardVerticalSelectedPreview() {
    SelectableCardVertical(
        text = "Mamífero",
        selected = true,
        onClick = {},
        modifier = Modifier.padding(8.dp)
    ) {
        Text("🐼", fontSize = 48.sp)
    }
}

@Preview(showBackground = true, name = "Card Vertical - Not Selected")
@Composable
private fun SelectableCardVerticalNotSelectedPreview() {
    SelectableCardVertical(
        text = "Ave",
        selected = false,
        onClick = {},
        modifier = Modifier.padding(8.dp)
    ) {
        Text("🦅", fontSize = 48.sp)
    }
}

@Preview(showBackground = true, name = "Card Horizontal - Selected")
@Composable
private fun SelectableCardHorizontalSelectedPreview() {
    SelectableCardHorizontal(
        text = "Época",
        selected = true,
        onClick = {},
        modifier = Modifier.padding(8.dp)
    ) {
        Text("☀️", fontSize = 40.sp)
    }
}

