package com.example.roboranger.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.roboranger.ui.theme.PrimaryBlack
import com.example.roboranger.ui.theme.PrimaryLightGreen
import com.example.roboranger.ui.theme.PrimaryRed
import com.example.roboranger.ui.theme.PrimaryWhite
import com.example.roboranger.ui.theme.SecondaryGrassGreen
import com.example.roboranger.ui.theme.SecondaryLightGreen

/**
 * A card that displays information about the last used network and provides
 * a button to either reconnect or disconnect.
 *
 * @param lastSsid The name of the last connected network.
 * @param descriptor A text descriptor for the network (e.g., "Last connected" or "Connected").
 * @param isConnected Whether the app is currently connected to this network.
 * @param onConnectClicked The lambda to execute when the reconnect button is clicked.
 * @param onDisconnectClicked The lambda to execute when the disconnect button is clicked.
 */
@Composable
fun RoboRangerLastConnectionCard(
    lastSsid: String,
    descriptor: String,
    isConnected: Boolean,
    onConnectClicked: () -> Unit,
    onDisconnectClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            if (isConnected) PrimaryLightGreen else SecondaryLightGreen,
        )
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = descriptor,
                        style = MaterialTheme.typography.titleSmall,
                        color = PrimaryBlack,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = lastSsid,
                        style = MaterialTheme.typography.headlineSmall,
                        color = PrimaryBlack,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // The button is now always visible, but its text, color, and action change.
                if (isConnected) {
                    IconButton(
                        modifier = Modifier,
                        onClick = onDisconnectClicked,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = PrimaryRed,
                            contentColor = PrimaryBlack
                        )
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.WifiOff,
                            contentDescription = "Disconnect"
                        )
                    }
                } else {
                    IconButton(
                        modifier = Modifier,
                        onClick = onConnectClicked,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = SecondaryGrassGreen,
                            contentColor = PrimaryWhite
                        )
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Wifi,
                            contentDescription = "Reconnect"
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun RoboRangerLastConnectionCardPreview() {
    RoboRangerLastConnectionCard(
        lastSsid = "MyNetwork",
        descriptor = "Last connected",
        isConnected = false,
        onConnectClicked = {},
        onDisconnectClicked = {}
    )
}
