package com.example.roboranger.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun RoboRangerRoundIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    action: () -> Unit,
    containerColor: Color = Color(0xFF4E7029),
    contentColor: Color = Color.White
) {
    Box(
        modifier = modifier.size(56.dp)
            .background(Color(0xFF4E7029), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        FilledIconButton(
            onClick = action,
            modifier = Modifier.size(52.dp)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = CircleShape
                ),
            shape = CircleShape,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = containerColor,
            )
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