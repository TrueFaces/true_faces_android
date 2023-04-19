package com.n1rocket.truefaces.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class MeResponse(
    val avatar: String,
    val email: String,
    val username: String,
    val id: Int,
    @SerialName("is_active") val isActive: Boolean
)