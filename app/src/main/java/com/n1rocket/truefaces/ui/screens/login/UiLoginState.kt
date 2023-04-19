package com.n1rocket.truefaces.ui.screens.login

sealed class UiLoginState {
    object EmptyState : UiLoginState()
    object LoadingState : UiLoginState()
    data class FinishState(val hasAvatar: Boolean) : UiLoginState()
    data class ErrorState(val code:Int?, val message: String?) : UiLoginState()
}
