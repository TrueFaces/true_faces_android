package com.n1rocket.truefaces.ui.screens.main

data class MediaItem(
    val id: Int,
    val thumb : String,
    val isFace : Boolean,
)

fun getMedia() = (1..20).map {
    MediaItem(
        id = it,
        thumb = "https://picsum.photos/400?random=$it",
        isFace = it % 3 == 0
    )
}