package com.n1rocket.truefaces.ui.screens.main

data class UiMainState(
    val isLoading: Boolean = false,
    val isUploading: Boolean = false,
    val images: List<MediaItem> = listOf(),
    val owner: String = "",
    val message: String = ""
)
