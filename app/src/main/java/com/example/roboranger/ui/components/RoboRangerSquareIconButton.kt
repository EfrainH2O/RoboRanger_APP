package com.example.roboranger.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun RoboRangerSquareIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    action: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    interactionSource: MutableInteractionSource
) {
    Box(
        modifier = modifier.size(56.dp)
            .background(Color(0xFF4E7029), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        FilledIconButton(
            onClick = action,
            modifier = Modifier.size(52.dp)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp)
            ),
            shape = RoundedCornerShape(10.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
            interactionSource = interactionSource
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(36.dp),
                tint = contentColor
            )
        }
    }
}