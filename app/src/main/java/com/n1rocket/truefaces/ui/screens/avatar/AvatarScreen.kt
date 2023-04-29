package com.n1rocket.truefaces.ui.screens.avatar

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.n1rocket.truefaces.R
import com.n1rocket.truefaces.api.ApiResult
import com.n1rocket.truefaces.models.ImagesResponse
import com.n1rocket.truefaces.models.LoginResponse
import com.n1rocket.truefaces.models.MeResponse
import com.n1rocket.truefaces.repository.IRepository
import com.n1rocket.truefaces.utils.readBytes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AvatarScreen(
    viewModel: AvatarViewModel
) {
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var uri: Uri? by remember { mutableStateOf(null) }

    val imageLauncher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uriGallery: Uri? ->
        uri = uriGallery
        uri?.readBytes(context)?.let { bytes -> viewModel.uploadAvatar(bytes) }
    }

    uiState.value.apply {
        Log.d("AvatarScreen", "Avatar: ${this.avatar}")
        Log.d("AvatarScreen", "IsLoading: ${this.isLoading}")
        Log.d("AvatarScreen", "Message: ${this.message}")
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
                .padding(vertical = 32.dp)
        ) {
            Text(
                text = "Bienvenido a tu galería inteligente",
                style = TextStyle(fontSize = 32.sp, color = Color.White),
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Para conocerte mejor, necesitamos que escojas una foto que aparezcas de frente con tu cara al descubierto." +
                        "\nEsto lo utilizaremos para realizar una búsqueda inteligente en todas las fotos de la galería.",
                style = TextStyle(fontSize = 16.sp, color = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.height(16.dp))

            Button(onClick = {
                imageLauncher.launch("image/*")
            }) {
                Text(text = "Elegir imagen")
            }

            if (uri != null) {

                Spacer(Modifier.height(16.dp))

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(uri)
                        .addHeader("Authorization", "Bearer ${viewModel.getToken()}")
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_avatar),
                    error = painterResource(id = R.drawable.ic_error),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(200.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(text = uiState.value.message, modifier = Modifier.padding(horizontal = 32.dp))
        }
    }
}

@Preview
@Composable
fun AvatarPreview() {
    AvatarScreen(viewModel = AvatarViewModel(object : IRepository {
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