package com.example.roboranger.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.roboranger.ui.views.auth.AuthViewModel
import com.example.roboranger.ui.views.auth.LogInDestination
import com.example.roboranger.ui.views.auth.LogInScreen
import com.example.roboranger.ui.views.control.ControlDestination
import com.example.roboranger.ui.views.control.ControlScreen
import com.example.roboranger.ui.views.control.RobotControlViewModel
import com.example.roboranger.ui.views.form.FormDetailsDestination
import com.example.roboranger.ui.views.form.FormDetailsScreen
import com.example.roboranger.ui.views.form.FormEntryDestination
import com.example.roboranger.ui.views.form.FormEntryScreen
import com.example.roboranger.ui.views.home.HomeDestination
import com.example.roboranger.ui.views.home.HomeScreen
import com.example.roboranger.ui.views.network.NetworkSearchDestination
import com.example.roboranger.ui.views.network.NetworkSearchScreen
import com.example.roboranger.ui.views.settings.SettingsDestination
import com.example.roboranger.ui.views.settings.SettingsScreen


@Composable
fun RoboRangerNavHost (
    startDestination: String,
    navController: NavHostController,
    controlViewModel: RobotControlViewModel,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = ControlDestination.route) {
            ControlScreen(
                onNavigateUp = { navController.navigateUp() },
                navigateToFormEntry = { navController.navigate(FormEntryDestination.route) },
                onNavigateSettings = { navController.navigate(SettingsDestination.route) },
                controlViewModel = controlViewModel
            )
        }
        composable(route = FormEntryDestination.route) {
            FormEntryScreen(
                onNavigateUp = { navController.navigateUp() },
                onNavigateSettings = { navController.navigate(SettingsDestination.route) },
                navigateToHome = { navController.navigate(HomeDestination.route) }
            )
        }
        composable(route = SettingsDestination.route) {
            SettingsScreen(
                onNavigateUp = { navController.navigateUp() },
                authViewModel = authViewModel
            )
        }
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToControl = { navController.navigate(ControlDestination.route) },
                onNavigateSettings = { navController.navigate(SettingsDestination.route) },
                navigateToFormDetails = { navController.navigate(FormDetailsDestination.route) }
            )
        }
        composable(route = FormDetailsDestination.route) {
            FormDetailsScreen(
                onNavigateUp = { navController.navigateUp() },
                onNavigateSettings = { navController.navigate(SettingsDestination.route) }
            )
        }
        composable(route = LogInDestination.route) {
            LogInScreen(authViewModel = authViewModel)
        }
        composable(route = NetworkSearchDestination.route) {
            NetworkSearchScreen(
                // Pendiente
                navigateToHome = { navController.navigate(HomeDestination.route) }
            )
        }
    }
}