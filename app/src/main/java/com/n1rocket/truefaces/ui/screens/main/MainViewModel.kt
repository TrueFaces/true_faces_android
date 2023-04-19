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

    private var owner = ""
    private var images = listOf<MediaItem>()

    // UI state
    private var _currentUiState: UiMainState = UiMainState(true)
    private val _uiState = MutableStateFlow(_currentUiState)
    val uiState: StateFlow<UiMainState> = _uiState.asStateFlow()

    fun getImages() {
        updateState(_currentUiState.copy(isLoading = true))
        CoroutineScope(Dispatchers.IO).launch {
            val message = when (val result = repository.getImages()) {
                is ApiSuccess -> {
                    images = result.data.map { MediaItem(it.id, it.imageUrl, it.hasFace) }
                    images = images.reversed()
                    result.data.size.toString()
                }

                is ApiError -> "Result code: ${result.code} message: ${result.message}"
                is ApiException -> "Exception: ${result.e.localizedMessage}"
            }
            updateState(
                _currentUiState.copy(
                    isLoading = false,
                    isUploading = false,
                    message = message,
                    owner = owner,
                    images = images
                )
            )
        }
    }

    fun getMyProfile() {
        updateState(_currentUiState.copy(isLoading = true))
        CoroutineScope(Dispatchers.IO).launch {
            val message = when (val result = repository.me()) {
                is ApiSuccess -> {
                    owner = result.data.username
                    result.data.username
                }

                is ApiError -> "Result code: ${result.code} message: ${result.message}"
                is ApiException -> "Exception: ${result.e.localizedMessage}"
            }
            updateState(
                _currentUiState.copy(
                    isLoading = false,
                    isUploading = false,
                    message = message,
                    owner = owner,
                    images = images
                )
            )
        }
    }

    fun uploadImage(byteArray: ByteArray) {
        updateState(_currentUiState.copy(isUploading = true))
        CoroutineScope(Dispatchers.IO).launch {
            val message = when (val result = repository.uploadImage(byteArray)) {
                is ApiSuccess -> result.data
                is ApiError -> "Result code: ${result.code} message: ${result.message}"
                is ApiException -> "Exception: ${result.e.localizedMessage}"
            }
            updateState(
                _currentUiState.copy(
                    isLoading = false,
                    isUploading = false,
                    message = message,
                    owner = owner,
                    images = images
                )
            )
            getImages()
        }
    }

    fun uploadAvatar(byteArray: ByteArray) {
        updateState(_currentUiState.copy(isUploading = true))
        CoroutineScope(Dispatchers.IO).launch {
            val message = when (val result = repository.uploadImage(byteArray)) {
                is ApiSuccess -> result.data
                is ApiError -> "Result code: ${result.code} message: ${result.message}"
                is ApiException -> "Exception: ${result.e.localizedMessage}"
            }
            updateState(
                _currentUiState.copy(
                    isLoading = false,
                    isUploading = false,
                    message = message,
                    owner = owner,
                    images = images
                )
            )
            getImages()
        }
    }

    fun logout() {
        repository.logout()
    }

    fun updateState(state: UiMainState) {
        _currentUiState = state
        _uiState.value = state
    }

    fun getToken(): String = repository.getToken()
    fun getAvatar(): String = repository.getAvatar()
}
