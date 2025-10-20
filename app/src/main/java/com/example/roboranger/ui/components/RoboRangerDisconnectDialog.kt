package com.example.roboranger.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.roboranger.ui.views.network.ReconnectionStatus

/**
 * A dialog that provides visual feedback for the reconnection process.
 *
 * @param status The current state of the reconnection attempt.
 * @param onDismiss The action to perform when the dialog is dismissed.
 */
@Composable
fun ReconnectDialog(
    status: ReconnectionStatus,
    onDismiss: () -> Unit
) {
    // The dialog is only shown when the status is not Idle.
    if (status !is ReconnectionStatus.Idle) {
        Dialog(onDismissRequest = onDismiss) {
            Card {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    when (status) {
                        is ReconnectionStatus.InProgress -> {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Connecting...")
                        }
                        is ReconnectionStatus.Success -> {
                            Text("Successfully Reconnected!")
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = onDismiss) {
                                Text("OK")
                            }
                        }
                        is ReconnectionStatus.Failed -> {
                            Text("Connection Failed")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(status.message)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = onDismiss) {
                                Text("Close")
                            }
                        }
                        is ReconnectionStatus.Idle -> {
                            // This case is handled by the outer if-statement.
                        }
                    }
                }
            }
        }
    }
}
