package com.n1rocket.truefaces.ui.screens.main

sealed class UiMainState {
    object EmptyState : UiMainState()
    object LoadingState : UiMainState()
    data class FinishState(val owner: String, val message: String) : UiMainState()
    object UploadingState : UiMainState()
}
