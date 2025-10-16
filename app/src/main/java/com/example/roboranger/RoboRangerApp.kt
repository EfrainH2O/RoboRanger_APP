package com.example.roboranger

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.roboranger.navigation.RoboRangerNavHost
import com.example.roboranger.ui.views.auth.AuthViewModel
import com.example.roboranger.ui.views.control.RobotControlViewModel
import com.example.roboranger.ui.views.network.NetworkSearchViewModel


// Componente composable que se encuntra lo mas alto dentro de la jerarquia que representa las diferentes vistas para la aplicacion
@Composable
fun RoboRangerApp(
    navController: NavHostController = rememberNavController(),
    startDestination: String,
    controlViewModel: RobotControlViewModel,
    authViewModel: AuthViewModel,
    networkSearchViewModel: NetworkSearchViewModel
) {
    RoboRangerNavHost(
        navController = navController,
        controlViewModel = controlViewModel,
        startDestination = startDestination,
        authViewModel = authViewModel,
        networkSearchViewModel = networkSearchViewModel
    )
}