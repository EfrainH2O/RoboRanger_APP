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
import com.example.roboranger.view_model.RobotControlViewModel

class MainActivity : ComponentActivity() {
    // Omitida por el momento en lo que se define el enruteado todas las vistas
    // private val viewModel: RobotControlViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoboRangerTheme {
                RoboRangerApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Defaulting(viewModel: RobotControlViewModel){
    val res = viewModel.errorM.value
    Box(Modifier.padding(15.dp)){
        if(res == null){
            Text("Not Going Front")
            viewModel.try_go_front()
        }else  {
            Text("GoingFront")
        }
    }
}