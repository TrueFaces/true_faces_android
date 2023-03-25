package com.n1rocket.truefaces

sealed class UiState {

    object EmptyState : UiState()
    object UploadingState : UiState()
    data class FinishState(val message: String) : UiState()
}
