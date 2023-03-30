package com.n1rocket.truefaces.ui.screens.login

@kotlinx.serialization.Serializable
data class LoginResponse(val access_token: String, val token_type: String)