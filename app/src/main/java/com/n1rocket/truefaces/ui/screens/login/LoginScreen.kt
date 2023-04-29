package com.n1rocket.truefaces.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.n1rocket.truefaces.api.ApiResult
import com.n1rocket.truefaces.models.ImagesResponse
import com.n1rocket.truefaces.models.LoginResponse
import com.n1rocket.truefaces.models.MeResponse
import com.n1rocket.truefaces.repository.IRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

@Composable
fun LoginScreen(onLoggedSuccess: (Boolean) -> Unit, viewModel: LoginViewModel) {

    val uiState = viewModel.uiState.collectAsState()

    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (uiState.value is UiLoginState.FinishState) {
        LaunchedEffect(Unit) {
            onLoggedSuccess((uiState.value as UiLoginState.FinishState).hasAvatar)
        }
    }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .padding(vertical = 32.dp)) {
            Text(
                text = "Inicia sesión en\ntu cuenta",
                style = TextStyle(fontSize = 32.sp, color = Color.White),
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tu galería con inteligencia artificial te está esperando...",
                style = TextStyle(fontSize = 16.sp, color = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            OutlinedTextField(
                value = user,
                onValueChange = { user = it },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )

            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),

                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = { viewModel.login(user, password) }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Iniciar sesión")
            }

            when (val st = uiState.value) {
                is UiLoginState.FinishState -> Text(text = "Ha iniciado sesión correctamente")
                is UiLoginState.ErrorState -> Text(text = "Ha ocurrido un error: ${st.code} ${st.message}")
                UiLoginState.LoadingState -> CircularProgressIndicator()
                else -> {}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen(onLoggedSuccess = {}, viewModel = LoginViewModel(object : IRepository {
        override suspend fun getImages(): ApiResult<List<ImagesResponse>> {
            TODO("Not yet implemented")
        }

        override suspend fun uploadImage(byteArray: ByteArray): ApiResult<String> {
            TODO("Not yet implemented")
        }

        override suspend fun uploadAvatar(byteArray: ByteArray): ApiResult<String> {
            TODO("Not yet implemented")
        }

        override suspend fun uploadAvatarBody(byteArray: ByteArray): ApiResult<String> {
            TODO("Not yet implemented")
        }

        override suspend fun login(user: String, password: String): ApiResult<LoginResponse> {
            TODO("Not yet implemented")
        }

        override suspend fun saveToken(accessToken: String) {
            TODO("Not yet implemented")
        }

        override suspend fun me(): ApiResult<MeResponse> {
            TODO("Not yet implemented")
        }

        override fun getTokenFlow(viewModelScope: CoroutineScope): StateFlow<String> {
            TODO("Not yet implemented")
        }

        override fun isLogged(): Boolean {
            TODO("Not yet implemented")
        }

        override fun logout() {
            TODO("Not yet implemented")
        }

        override fun saveAvatar(avatar: String) {
            TODO("Not yet implemented")
        }

        override fun getToken(): String {
            TODO("Not yet implemented")
        }

        override fun getAvatar(): String {
            TODO("Not yet implemented")
        }

        override suspend fun deleteImage(id: Int): ApiResult<String> {
            TODO("Not yet implemented")
        }
    }))
}
