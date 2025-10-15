package com.example.roboranger.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CarRental
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.roboranger.ui.theme.PrimaryForestGreen
import com.example.roboranger.ui.theme.PrimaryGrassGreen
import com.example.roboranger.ui.theme.PrimaryWhite

@Composable
fun RoboRangerBottomAppBar(
    modifier: Modifier = Modifier,
    navigateToControl: () -> Unit,
) {
    Box(
        modifier = Modifier.height(100.dp)
    ) {
        BottomAppBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
            containerColor = PrimaryGrassGreen
        ) {

        }

        // Button
        RoboRangerStartButton(
            modifier = Modifier.align(Alignment.Center).offset(y = (-48).dp),
            navigateToControl = navigateToControl
        )
    }
}

@Composable
fun RoboRangerStartButton(
    modifier: Modifier = Modifier,
    navigateToControl: () -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.97f else 1f)
    val topsize = 96.dp

    Box(
        modifier = modifier
            .size(topsize)
            .scale((scale - 0.1f))
            .background(PrimaryForestGreen, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        FilledIconButton(
            onClick = navigateToControl,
            modifier = Modifier
                .size(topsize - 16.dp)
                //Animacion con scale
                .scale(scale)
                .border(
                    width = 3.dp,
                    color = PrimaryWhite,
                    shape = CircleShape
                ),
            shape = CircleShape,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = PrimaryForestGreen,
                contentColor = PrimaryWhite
            ),
            interactionSource = interactionSource
        ) {
            Icon(
                imageVector = Icons.Filled.CarRental,
                contentDescription = "StartExploration",
                modifier = Modifier.size(topsize - 32.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoboRangerBottomAppBarPreview() {
    RoboRangerBottomAppBar(
        navigateToControl = {}
    )
}