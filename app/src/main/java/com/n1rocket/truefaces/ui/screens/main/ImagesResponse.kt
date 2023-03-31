package com.n1rocket.truefaces.ui.screens.main

@kotlinx.serialization.Serializable
data class ImagesResponse(val thumbnail: String, val url: String, val id: String, val face: Boolean)