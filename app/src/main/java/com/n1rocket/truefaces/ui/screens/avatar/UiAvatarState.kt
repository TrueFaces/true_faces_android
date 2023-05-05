package com.n1rocket.truefaces.ui.screens.avatar

data class UiAvatarState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val message: String = "",
    val avatar: String = "",
)
