package com.example.roboranger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.roboranger.ui.theme.RoboRangerTheme
import com.example.roboranger.ui.views.control.RobotControlViewModel

class MainActivity : ComponentActivity() {
    // Omitida por el momento en lo que se define el enruteado todas las vistas
    private val controlViewModel: RobotControlViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoboRangerTheme(darkTheme = false, dynamicColor = false) {
                RoboRangerApp(
                    controlViewModel = controlViewModel
                )
            }
        }
    }
}