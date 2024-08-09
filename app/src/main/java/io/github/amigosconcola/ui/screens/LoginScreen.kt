package io.github.amigosconcola.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.github.amigosconcola.R
import io.github.amigosconcola.ui.viewmodel.LoginEvent
import io.github.amigosconcola.ui.viewmodel.LoginScreenEvent.PasswordChanged
import io.github.amigosconcola.ui.viewmodel.LoginScreenEvent.Submit
import io.github.amigosconcola.ui.viewmodel.LoginScreenEvent.UsernameChanged
import io.github.amigosconcola.ui.viewmodel.LoginViewModel
import io.github.amigosconcola.ui.viewmodel.TokenEvent
import io.github.amigosconcola.ui.viewmodel.TokenUiEvent
import io.github.amigosconcola.ui.viewmodel.TokenViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    tokensViewModel: TokenViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val state by loginViewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(true) {
        loginViewModel.errors.collect {
            Toast.makeText(
                context,
                it,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(true) {
        loginViewModel.events.collect {
            when (it) {
                is LoginEvent.Success -> {
                    tokensViewModel.onEvent(TokenUiEvent.Store(it.tokens))
                }
            }
        }
    }

    LaunchedEffect(true) {
        tokensViewModel.events.collect {
            if (it is TokenEvent.Stored) {
                navController.navigate(Screen.Home) {
                    popUpTo(Screen.Login) {
                        inclusive = true
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Amigos con Cola",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        ElevatedCard {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .width(IntrinsicSize.Max)
            ) {
                Text(
                    text = "Entra con tu cuenta",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = state.username,
                    onValueChange = { loginViewModel.onEvent(UsernameChanged(it)) },
                    label = { Text("Nombre de usuario") },
                    isError = state.usernameError != null,
                    singleLine = true,
                    placeholder = { Text("Ingrese su nombre de usuario") },
                )
                if (state.usernameError != null) {
                    Text(
                        text = state.usernameError!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = state.password,
                    onValueChange = { loginViewModel.onEvent(PasswordChanged(it)) },
                    label = { Text("Contraseña") },
                    isError = state.passwordError != null,
                    singleLine = true,
                    placeholder = { Text("Ingrese su constraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                )
                if (state.passwordError != null) {
                    Text(
                        text = state.passwordError!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { loginViewModel.onEvent(Submit) }
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.width(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    } else {
                        Text("Ingresar")
                    }
                }
            }
        }
    }
}
