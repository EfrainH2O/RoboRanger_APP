package com.example.roboranger.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.ui.tooling.preview.Preview
import com.example.roboranger.ui.theme.RoboRangerTheme

@Composable
fun MovementActionButton(
    icon: ImageVector,
    label: String,
    onEntryAction: () -> Unit,
    onEndAction: () -> Unit,
    containerColor: Color = Color(0xFF4E7029),
    contentColor: Color = Color.White,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val isPressed by interactionSource.collectIsPressedAsState()

    if(isPressed) {
        DisposableEffect(
            Unit
        ) {
            onEntryAction()
            onDispose {
                onEndAction()
            }
        }
    }
    RoboRangerSquareIconButton(
        icon = icon,
        label = label,
        action = {},
        containerColor = containerColor,
        contentColor = contentColor,
        interactionSource = interactionSource
    )
}


@Preview(showBackground = true)
@Composable
fun MovementActionButtonPreview() {
    RoboRangerTheme {
        MovementActionButton(
            icon = Icons.Default.ArrowUpward,
            label = "Adelante",
            onEntryAction = {
            },
            onEndAction = { }
        )
    }
}