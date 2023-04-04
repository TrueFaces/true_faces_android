package com.n1rocket.truefaces.ui.screens.main

import com.n1rocket.truefaces.models.ImagesResponse

sealed class UiMainState {
    object LoadingState : UiMainState()
    data class FinishState(
        val images: List<ImagesResponse>,
        val owner: String,
        val message: String
    ) : UiMainState()

    object UploadingState : UiMainState()
}
