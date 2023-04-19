package com.n1rocket.truefaces.ui.screens.avatar

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.n1rocket.truefaces.R
import com.n1rocket.truefaces.utils.readBytes

@Composable
fun AvatarScreen(
    navController: NavHostController,
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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
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

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            imageLauncher.launch("image/*")
        }) {
            Text(text = "Elegir imagen")
        }
        
        Text(text = uiState.value.message)
    }
}
