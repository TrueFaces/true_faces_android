package com.n1rocket.truefaces.models

@kotlinx.serialization.Serializable
data class MeResponse(
    val email: String,
    val username: String,
    val id: Int,
    val is_active: Boolean
)