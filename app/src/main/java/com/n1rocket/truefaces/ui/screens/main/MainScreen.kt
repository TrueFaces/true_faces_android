package com.n1rocket.truefaces.ui.screens.main

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.io.IOException

@Composable
fun MainScreen(navController: NavHostController, viewModel: MainViewModel) {

    val uiState = viewModel.uiState.collectAsState()

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        uri?.let { readBytes(context, it)?.let { bytes -> viewModel.uploadImage("image", bytes) } }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(32.dp)
    ) {
        Text(text = "Puedes hacer o elegir una foto. Comprobaremos si hay una cara en ella.")
        imageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)

            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver, it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }

            bitmap.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(400.dp)
                        .padding(16.dp)
                )
            }
        }
        Button(onClick = { launcher.launch("image/*") }) {
            Text(text = "Elegir foto")
        }
        when (val state = uiState.value) {
            is UiMainState.FinishState -> Text(text = state.message)
            is UiMainState.UploadingState -> Text(text = "Subiendo la foto")
            else -> {}
        }
    }


}

@Throws(IOException::class)
private fun readBytes(context: Context, uri: Uri): ByteArray? {
    context.contentResolver.apply {
        val inputStream = openInputStream(uri)
        val result = inputStream?.buffered()?.use { it.readBytes() }
        inputStream?.close()
        return result
    }
}
