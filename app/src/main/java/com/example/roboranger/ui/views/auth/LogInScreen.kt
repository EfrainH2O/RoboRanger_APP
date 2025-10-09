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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.roboranger.R
import com.example.roboranger.ui.components.RoboRangerTopAppBar
import com.example.roboranger.navigation.NavigationDestination
import com.example.roboranger.ui.components.LockScreenOrientation
import com.example.roboranger.ui.components.RoboRangerButton
import com.example.roboranger.ui.components.RoboRangerPasswordField
import com.example.roboranger.ui.components.RoboRangerTextField
import com.example.roboranger.ui.views.form.FormEntryDestination

object LogInDestination : NavigationDestination {
    override val route = "login"
    override val titleRes = R.string.login_title
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(
    onSignIn: () -> Unit,
    ) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT)

    Scaffold() { innerPadding ->
         LogInBody(
             onSignIn = onSignIn,
             modifier = Modifier
                 .fillMaxSize()
                 .padding(innerPadding)
         )
    }
}

@Composable
fun LogInBody(
    modifier: Modifier = Modifier,
    onSignIn: () -> Unit,
) {
    var userEmail by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    // Validacion simple para poder habilitar el boton de inicio de sesion
    val emailValid = remember(userEmail) { "@" in userEmail && "." in userEmail && userEmail.length >= 5 }
    val enabled = emailValid && password.length >= 15

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
            onValueChange = { userEmail = it },
            placeholder = stringResource(R.string.placeholder_email),
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
            onValueChange = { password = it },
            placeholder = stringResource(R.string.placeholder_password),
            modifier = Modifier.padding(top = 8.dp),
        )
        Spacer(Modifier.height(32.dp))

        // Boton de inicio de sesion
        RoboRangerButton(
            onClick = onSignIn,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 8.dp),
            text = stringResource(R.string.login_title)
        )
        Spacer(Modifier.height(24.dp))
    }

}