package com.n1rocket.truefaces.ui.screens.login

sealed class UiLoginState {
    object EmptyState : UiLoginState()
    object LoadingState : UiLoginState()
    object FinishState : UiLoginState()
}
