package com.n1rocket.truefaces.ui.screens.main

import androidx.lifecycle.ViewModel
import com.n1rocket.truefaces.api.ApiError
import com.n1rocket.truefaces.api.ApiException
import com.n1rocket.truefaces.api.ApiSuccess
import com.n1rocket.truefaces.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    // UI state
    private var _currentUiState: UiMainState = UiMainState.EmptyState
    private val _uiState = MutableStateFlow(_currentUiState)
    val uiState: StateFlow<UiMainState> = _uiState.asStateFlow()

    fun getImages() {
        _uiState.value = UiMainState.LoadingState
        CoroutineScope(Dispatchers.IO).launch {
            val message = when (val result = repository.getImages()) {
                is ApiSuccess -> result.data
                is ApiError -> "Result code: ${result.code} message: ${result.message}"
                is ApiException -> "Exception: ${result.e.localizedMessage}"
            }
            _uiState.value = UiMainState.FinishState(message = message)
        }
    }

    fun uploadImage(name: String, byteArray: ByteArray) {
        _uiState.value = UiMainState.UploadingState
        CoroutineScope(Dispatchers.IO).launch {
            val message = when (val result = repository.uploadImage(name, byteArray)) {
                is ApiSuccess -> result.data
                is ApiError -> "Result code: ${result.code} message: ${result.message}"
                is ApiException -> "Exception: ${result.e.localizedMessage}"
            }
            _uiState.value = UiMainState.FinishState(message = message)
            getImages()
        }
    }
}
