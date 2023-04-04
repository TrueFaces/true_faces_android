package com.n1rocket.truefaces.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ImagesResponse(
    val id: Int,
    val filesize: Int,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("thumbnail_url") val thumbnailUrl: String,
    @SerialName("has_face") val hasFace: Boolean,
)