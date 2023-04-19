@file:OptIn(ExperimentalCoilApi::class)

package com.n1rocket.truefaces.ui.screens.main

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
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.n1rocket.truefaces.utils.readBytes

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(navController: NavHostController, viewModel: MainViewModel) {

    val uiState = viewModel.uiState.collectAsState()

    val context = LocalContext.current

    val imageLauncher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.readBytes(context)?.let { bytes -> viewModel.uploadImage(bytes) }
    }

    LaunchedEffect(Unit) {
        viewModel.getMyProfile()
        viewModel.getImages()
    }

    Scaffold(
        topBar = {
            TopAppBar {
                Column(verticalArrangement = Arrangement.Center) {
                    TheToolbar(uiState = uiState,
                        avatar = viewModel.getAvatar(),
                        viewModel = viewModel,
                        onAvatarClicked = {

                        },
                        onLogoutClicked = {
                            viewModel.logout()
                        })
                    if (uiState.value.isLoading) {
                        LinearProgressIndicator()
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { imageLauncher.launch("image/*") }
            ) {
                if (uiState.value.isUploading) {
                    CircularProgressIndicator()
                } else {
                    Icon(
                        imageVector = Icons.Default.UploadFile,
                        contentDescription = "Subir foto"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        val pullRefreshState =
            rememberPullRefreshState(uiState.value.isLoading, { viewModel.getImages() })


        ConstraintLayout(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            val (indicator, grid, empty) = createRefs()

            LazyVerticalGrid(
                contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding_xs)),
                columns = GridCells.Adaptive(dimensionResource(id = R.dimen.cell_min_width)),
                modifier = Modifier
                    .pullRefresh(pullRefreshState)
                    .fillMaxSize()
                    .constrainAs(grid) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                items(uiState.value.images) { item ->
                    MediaListItem(viewModel, item, Modifier.padding(dimensionResource(id = R.dimen.padding_xs)))
                }
            }

            if (uiState.value.images.isEmpty()) {
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
                    Button({ viewModel.getImages() }) {
                        Text(
                            text = "Recargar",
                            style = TextStyle(fontSize = 24.sp, textAlign = TextAlign.Center)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button({
                        imageLauncher.launch("image/*")
                    }) {
                        Text(
                            text = "Sube tu primera foto",
                            style = TextStyle(fontSize = 24.sp, textAlign = TextAlign.Center)
                        )
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = uiState.value.isLoading,
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

@Composable
fun TheToolbar(
    onAvatarClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
    uiState: State<UiMainState>,
    avatar: String,
    viewModel: MainViewModel
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(8.dp))

        val imagePainter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .addHeader("Authorization", "Bearer ${viewModel.getToken()}")
                .data(data = avatar)
                .allowHardware(false)
                .build()
        )

        Image(
            painter = imagePainter,
            contentDescription = "Avatar",
            modifier = Modifier.size(50.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Galería de ${uiState.value.owner}",
            style = TextStyle(fontSize = 24.sp),
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onLogoutClicked) {
            Icon(imageVector = Icons.Default.Logout, contentDescription = "Cerrar sesión")
        }
    }
}

@ExperimentalCoilApi
@Composable
fun MediaListItem(viewModel: MainViewModel, item: MediaItem, modifier: Modifier = Modifier) {
    var colorTintDark by rememberSaveable { mutableStateOf(true) }


//    AsyncImage(
//        model = ImageRequest.Builder(LocalContext.current)
//            .data(uri)
//            .addHeader("Authorization", "Bearer ${viewModel.getToken()}")
//            .crossfade(true)
//            .build(),
//        placeholder = painterResource(R.drawable.ic_avatar),
//        error = painterResource(id = R.drawable.ic_error),
//        contentDescription = "Avatar",
//        contentScale = ContentScale.Crop,
//        modifier = Modifier
//            .clip(CircleShape)
//            .size(200.dp)
//    )

    Log.d("MainScreen", "URL: ${item.thumb}")
    Log.d("MainScreen", "Token: ${viewModel.getToken()}")

    val imagePainter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .addHeader("Authorization", "Bearer ${viewModel.getToken()}")
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