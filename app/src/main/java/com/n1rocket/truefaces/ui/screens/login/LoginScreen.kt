package com.n1rocket.truefaces.ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.n1rocket.truefaces.ui.Routes

@Composable
fun LoginScreen(navController: NavHostController, viewModel: LoginViewModel) {

    val uiState = viewModel.uiState.collectAsState()

    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (uiState.value is UiLoginState.FinishState) {
        LaunchedEffect(Unit) {
            navController.navigate(Routes.Main.route) {
                popUpTo(Routes.Login.route) {
                    inclusive = true
                }
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(32.dp)
    ) {
        Text(text = "Inicio de sesión")

        OutlinedTextField(
            value = user,
            onValueChange = { user = it },
            label = { Text("Usuario") }
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") }
        )

        Button(onClick = { viewModel.login(user, password) }) {
            Text(text = "Login")
        }
        when (val st = uiState.value) {
            UiLoginState.FinishState -> Text(text = "Ha iniciado sesión correctamente")
            is UiLoginState.ErrorState -> Text(text = "Ha ocurrido un error: ${st.code} ${st.message}")
            UiLoginState.LoadingState -> CircularProgressIndicator()
            else -> {}
        }
    }

}
