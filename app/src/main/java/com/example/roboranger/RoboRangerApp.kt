package com.example.roboranger

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.roboranger.navigation.RoboRangerNavHost
import com.example.roboranger.ui.views.auth.AuthViewModel
import com.example.roboranger.ui.views.control.RobotControlViewModel


// Componente composable que se encuntra lo mas alto dentro de la jerarquia que representa las diferentes vistas para la aplicacion
@Composable
fun RoboRangerApp(
    navController: NavHostController = rememberNavController(),
    startDestination: String,
    controlViewModel: RobotControlViewModel,
) {
    RoboRangerNavHost(
        navController = navController,
        controlViewModel = controlViewModel,
        startDestination = startDestination,
    )
}