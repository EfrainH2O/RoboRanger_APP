package com.example.roboranger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roboranger.data.ApiRepository
import com.example.roboranger.data.local.TokenManager
import com.example.roboranger.domain.model.AuthState
import com.example.roboranger.domain.usecase.GetAuthUseStateUseCase
import com.example.roboranger.domain.usecase.LogInUseCase
import com.example.roboranger.domain.usecase.LogOutUseCase
import com.example.roboranger.ui.theme.RoboRangerTheme
import com.example.roboranger.ui.views.auth.AuthViewModel
import com.example.roboranger.ui.views.auth.LogInDestination
import com.example.roboranger.ui.views.control.RobotControlViewModel
import com.example.roboranger.ui.views.home.HomeDestination
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoboRangerTheme(darkTheme = false, dynamicColor = false) {
                // Creado sin Hilt
                val controlViewModel: RobotControlViewModel by viewModels()
                // Creado con Hilt
                val authViewModel: AuthViewModel by viewModels()

                val authState by authViewModel.authState.collectAsStateWithLifecycle()

                if (authState is AuthState.InitialLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    RoboRangerApp(
                        startDestination = if (authState is AuthState.Authenticated) HomeDestination.route else LogInDestination.route,
                        controlViewModel = controlViewModel,
                    )
                }
            }
        }
    }
}