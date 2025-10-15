package com.example.roboranger.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roboranger.data.FormCard
import com.example.roboranger.ui.theme.RoboRangerTheme

@Composable
fun RoboRangerFormCard(
    formCard: FormCard,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB4D68F))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Place
            InfoRow(
                icon = Icons.Default.LocationOn,
                text = formCard.place,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Date and Hour
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Date
                InfoRow(
                    icon = Icons.Default.CalendarMonth,
                    text = formCard.date,
                    fontWeight = FontWeight.Medium
                )
                // Hour
                InfoRow(
                    icon = Icons.Default.Schedule,
                    text = formCard.hour,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


@Composable
private fun InfoRow(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight? = null,
    fontSize: androidx.compose.ui.unit.TextUnit = 16.sp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = text,
            fontWeight = fontWeight,
            fontSize = fontSize,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RoboRangerFormCardPreview() {
    RoboRangerTheme {
        RoboRangerFormCard(
            formCard = FormCard(
                id = 1,
                place = "Warehouse",
                date = "2024-10-28",
                hour = "15:00",
                status = 0
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
