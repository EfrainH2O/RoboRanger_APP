package com.example.roboranger.ui.views.auth

import android.content.pm.ActivityInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.roboranger.R
import com.example.roboranger.domain.model.AuthState
import com.example.roboranger.navigation.NavigationDestination
import com.example.roboranger.ui.components.LockScreenOrientation
import com.example.roboranger.ui.components.RoboRangerButton
import com.example.roboranger.ui.components.RoboRangerPasswordField
import com.example.roboranger.ui.components.RoboRangerTextField

object LogInDestination : NavigationDestination {
    override val route = "login"
    override val titleRes = R.string.login_title
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    ) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT)

    Scaffold() { innerPadding ->
         LogInBody(
             authViewModel = authViewModel,
             modifier = Modifier
                 .fillMaxSize()
                 .padding(innerPadding)
         )
    }
}

@Composable
fun LogInBody(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
) {
    var userEmail by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) } // oculta error al editar
    val authState by authViewModel.authState.collectAsState()

    // Validación de email usando patrón del sistema
    val emailValid = remember(userEmail) {
        android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()
    }
    val canSubmit = emailValid && password.length >= 5
    val isLoading = authState is AuthState.Loading
    val showStateError = (authState is AuthState.Error) && showError

    // Al escribir, si había error, se oculta
    fun onEmailChange(new: String) {
        userEmail = new
        if (showError) showError = false
    }
    fun onPasswordChange(new: String) {
        password = new
        if (showError) showError = false
    }

    // Acción de submit
    fun submitIfPossible() {
        if (!isLoading && canSubmit) {
            showError = true // si falla, se mostrará el mensaje del estado
            authViewModel.signIn(userEmail.trim(), password)
        }
    }

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo de la Aplicacion
        Image(
            painter = painterResource(R.drawable.roboranger_logo),
            contentDescription = stringResource(R.string.roboranger_logo)
        )
        Spacer(Modifier.height(32.dp))

        // Campo de correo electronico
        Text(
            text = stringResource(R.string.email_label),
            color = Color(0xFF3A3A3A),
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.SemiBold
        )
        RoboRangerTextField(
            label = stringResource(R.string.email_label),
            value = userEmail,
            onValueChange = ::onEmailChange,
            placeholder = stringResource(R.string.placeholder_email),
            enabled = !isLoading,
            modifier = Modifier.padding(top = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )
        Spacer(Modifier.height(16.dp))

        // Campo de contrasena
        Text(
            text = stringResource(R.string.password_label),
            color = Color(0xFF3A3A3A),
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.SemiBold
        )
        RoboRangerPasswordField(
            label = stringResource(R.string.password_label),
            value = password,
            onValueChange = ::onPasswordChange,
            placeholder = stringResource(R.string.placeholder_password),
            modifier = Modifier.padding(top = 8.dp),
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { submitIfPossible() })
        )
        Spacer(Modifier.height(32.dp))

        // Boton de inicio de sesion / cargando
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            RoboRangerButton(
                onClick = { submitIfPossible() },
                enabled = canSubmit && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                text = stringResource(R.string.login_title)
            )
        }

        // Error si es que existe
        Spacer(modifier = Modifier.height(8.dp))
        if (showStateError) {
            Text(
                text = (authState as AuthState.Error).message,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(24.dp))
    }

}