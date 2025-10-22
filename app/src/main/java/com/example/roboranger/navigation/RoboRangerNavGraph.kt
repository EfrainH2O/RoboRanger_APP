package com.example.roboranger.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.roboranger.ui.views.auth.LogInDestination
import com.example.roboranger.ui.views.auth.LogInScreen
import com.example.roboranger.ui.views.control.ControlDestination
import com.example.roboranger.ui.views.control.ControlScreen
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
import com.example.roboranger.ui.views.staticForm.FormDetailDestination
import com.example.roboranger.ui.views.staticForm.StaticFormScreen


@Composable
fun RoboRangerNavHost (
    startDestination: String,
    navController: NavHostController,
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
                navigateToNetworkSearch = { navController.navigate(NetworkSearchDestination.route) }
            )
        }
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToControl = { navController.navigate(ControlDestination.route) },
                onNavigateSettings = { navController.navigate(SettingsDestination.route) },
                navigateToFormDetails = { formId, formType ->
                    navController.navigate("${FormDetailDestination.route}/$formId/$formType")
                }

            )
        }
        composable(route = FormDetailsDestination.route) {
            FormDetailsScreen(
                onNavigateUp = { navController.navigateUp() },
                onNavigateSettings = { navController.navigate(SettingsDestination.route) }
            )
        }
        composable(route = LogInDestination.route) {
            LogInScreen()
        }
        composable(route = NetworkSearchDestination.route) {
            NetworkSearchScreen(
                // Pendiente
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = FormDetailDestination.routeWithArgs,
            arguments = listOf(
                navArgument(FormDetailDestination.FORM_ID_ARG) { type = NavType.IntType },
                navArgument(FormDetailDestination.FORM_TYPE_ARG) { type =
                    NavType.IntType }
            )
        ) {
            StaticFormScreen(
                onNavigateUp = { navController.navigateUp() },
                onNavigateSettings = { navController.navigate(SettingsDestination.route) }
            )
        }
    }
}