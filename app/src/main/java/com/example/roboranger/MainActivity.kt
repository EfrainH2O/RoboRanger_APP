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
import com.example.roboranger.data.remote.ApiClient
import com.example.roboranger.domain.model.AuthState
import com.example.roboranger.domain.usecase.GetAuthUseStateUseCase
import com.example.roboranger.domain.usecase.LogInUseCase
import com.example.roboranger.domain.usecase.LogOutUseCase
import com.example.roboranger.ui.theme.RoboRangerTheme
import com.example.roboranger.ui.views.auth.AuthViewModel
import com.example.roboranger.ui.views.auth.AuthViewModelFactory
import com.example.roboranger.ui.views.auth.LogInDestination
import com.example.roboranger.ui.views.control.RobotControlViewModel
import com.example.roboranger.ui.views.home.HomeDestination

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Declaracion de elementos de la capa de datos
        val tokenManager = TokenManager(applicationContext)
        val apiService = ApiClient.create(tokenManager)
        val apiRepository = ApiRepository(apiService, tokenManager)

        // Declaracion de elementos de la capa de dominio
        val logInUseCase = LogInUseCase(apiRepository)
        val logOutUseCase = LogOutUseCase(apiRepository)
        val getAuthUseStateUseCase = GetAuthUseStateUseCase(apiRepository)

        // Declaracion del factory con los usecases apropiados
        val authViewModelFactory = AuthViewModelFactory(
            logInUseCase = logInUseCase,
            logOutUseCase = logOutUseCase,
            getAuthUseStateUseCase = getAuthUseStateUseCase
        )

        enableEdgeToEdge()
        setContent {
            RoboRangerTheme(darkTheme = false, dynamicColor = false) {
                val controlViewModel: RobotControlViewModel by viewModels()
                val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)

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
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}