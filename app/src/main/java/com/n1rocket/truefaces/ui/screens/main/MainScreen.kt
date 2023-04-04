@file:OptIn(ExperimentalCoilApi::class)

package com.n1rocket.truefaces.ui.screens.main

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import androidx.palette.graphics.Palette
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.n1rocket.truefaces.R
import com.n1rocket.truefaces.utils.isColorDark
import java.io.IOException

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(navController: NavHostController, viewModel: MainViewModel) {
    var owner by remember { mutableStateOf("") }

    val uiState = viewModel.uiState.collectAsState()

    val context = LocalContext.current

    when (val st = uiState.value) {
        is UiMainState.FinishState -> {
            owner = st.owner
            Log.d("MainScreen", "---> Finish: ${st.message}")
        }
        UiMainState.LoadingState -> Log.d("MainScreen", "---> LoadingState")
        UiMainState.UploadingState -> Log.d("MainScreen", "---> UploadingState")
    }

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { readBytes(context, it)?.let { bytes -> viewModel.uploadImage("image", bytes) } }
    }

    LaunchedEffect(Unit) {
        viewModel.getMyProfile()
        viewModel.getImages()
    }

    Scaffold(
        topBar = {
            TopAppBar {
                Column(verticalArrangement = Arrangement.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Galería de $owner",
                            style = TextStyle(fontSize = 24.sp),
                        )
                        IconButton(onClick = { viewModel.logout() }) {
                            Icon(imageVector = Icons.Default.Logout, contentDescription = "Cerrar sesión")
                        }
                    }
                    if (uiState.value is UiMainState.LoadingState) {
                        LinearProgressIndicator()
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { launcher.launch("image/*") }
            ) {
                if (uiState.value !is UiMainState.UploadingState) {
                    Icon(
                        imageVector = Icons.Default.UploadFile,
                        contentDescription = "Subir foto"
                    )
                } else {
                    CircularProgressIndicator()
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        val pullRefreshState =
            rememberPullRefreshState(uiState.value is UiMainState.LoadingState, { viewModel.getImages() })


        ConstraintLayout(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            val (indicator, grid, empty) = createRefs()

            var mediaItems = listOf<MediaItem>()

            when (val state = uiState.value) {
                is UiMainState.FinishState -> mediaItems =
                    state.images.map { MediaItem(it.id, it.imageUrl, it.hasFace) }
                else -> {}
            }

            LazyVerticalGrid(
                contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding_xs)),
                columns = GridCells.Adaptive(dimensionResource(id = R.dimen.cell_min_width)),
                modifier = Modifier
                    .pullRefresh(pullRefreshState)
                    .constrainAs(grid) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                items(mediaItems) { item ->
                    MediaListItem(item, Modifier.padding(dimensionResource(id = R.dimen.padding_xs)))
                }
            }

            if (mediaItems.isEmpty()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.constrainAs(empty) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Photo,
                        contentDescription = "Foto",
                        modifier = Modifier.size(56.dp)
                    )
                    Text(
                        text = "Parece que aún no tienes fotos",
                        style = TextStyle(fontSize = 28.sp, textAlign = TextAlign.Center)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button({ launcher.launch("image/*") }) {
                        Text(
                            text = "Sube tu primera foto",
                            style = TextStyle(fontSize = 24.sp, textAlign = TextAlign.Center)
                        )
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = uiState.value is UiMainState.LoadingState,
                state = pullRefreshState,
                modifier = Modifier.constrainAs(indicator) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )


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

@ExperimentalCoilApi
@Composable
fun MediaListItem(item: MediaItem, modifier: Modifier = Modifier) {
    var colorTintDark by rememberSaveable { mutableStateOf(true) }

    val imagePainter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(data = item.thumb)
            .allowHardware(false)
            .build(),
        onSuccess = {
            Palette.Builder(it.result.drawable.toBitmap()).generate { palette ->
                val vibrant = palette?.dominantSwatch
                colorTintDark = vibrant?.rgb?.isColorDark(0.7f) == false
            }
        })

    Box(
        modifier = modifier
            .height(dimensionResource(id = R.dimen.cell_thumb_height))
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = imagePainter,
            contentDescription = "Gente",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        if (item.isFace) {
            Icon(
                imageVector = Icons.Default.Face,
                contentDescription = "Tiene una cara",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .size(32.dp),
                tint = if (colorTintDark) Color.Black else Color.White
            )
        }
    }
}