package com.n1rocket.truefaces.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class LoginResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String
)