package com.example.roboranger.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.roboranger.ui.theme.PrimaryBlack
import com.example.roboranger.ui.theme.PrimaryBushGreen
import com.example.roboranger.ui.theme.PrimaryGrassGreen
import com.example.roboranger.ui.theme.PrimaryRed
import com.example.roboranger.ui.theme.PrimaryWhite

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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                    modifier = Modifier,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = descriptor,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = lastSsid,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = 24.dp))

                // The button is now always visible, but its text, color, and action change.
                if (isConnected) {
                    Button(
                        modifier = Modifier,
                        onClick = onDisconnectClicked,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryRed,
                            contentColor = PrimaryBlack
                        )
                    ) {
                        Text("Desconectar")
                    }
                } else {
                    Button(
                        modifier = Modifier,
                        onClick = onConnectClicked,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBushGreen,
                            contentColor = PrimaryWhite
                        )
                    ) {
                        Text("Reconectar")
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
