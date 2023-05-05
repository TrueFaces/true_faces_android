package com.n1rocket.truefaces.ui.screens.avatar

import androidx.lifecycle.ViewModel
import com.n1rocket.truefaces.api.ApiError
import com.n1rocket.truefaces.api.ApiException
import com.n1rocket.truefaces.api.ApiSuccess
import com.n1rocket.truefaces.repository.IRepository
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
class AvatarViewModel @Inject constructor(private val repository: IRepository) : ViewModel() {
    // UI state
    private var _currentUiState: UiAvatarState = UiAvatarState(true)
    private val _uiState = MutableStateFlow(_currentUiState)
    val uiState: StateFlow<UiAvatarState> = _uiState.asStateFlow()

    fun uploadAvatar(byteArray: ByteArray) {
        avatarBody(byteArray)
    }

    private fun avatarBinary(byteArray: ByteArray) {
        updateState(_currentUiState.copy(isLoading = true))
        CoroutineScope(Dispatchers.IO).launch {
            val newState = when (val result = repository.uploadAvatar(byteArray)) {
                is ApiSuccess -> {
                    _currentUiState.copy(
                        isLoading = false,
                        avatar = "avatar",
                    )
                }

                is ApiError -> _currentUiState.copy(
                    isLoading = false,
                    message = "Result code: ${result.code} message: ${result.message}",
                )

                is ApiException -> _currentUiState.copy(
                    isLoading = false,
                    message = "Exception: ${result.e.localizedMessage}",
                )
            }
            updateState(newState)
        }
    }

    private fun avatarBody(byteArray: ByteArray) {
        updateState(_currentUiState.copy(isLoading = true))
        CoroutineScope(Dispatchers.IO).launch {
            val newState = when (val result = repository.uploadAvatarBody(byteArray)) {
                is ApiSuccess -> {
                    _currentUiState.copy(
                        isLoading = false,
                        success = true,
                        avatar = "avatar",
                    )
                }

                is ApiError -> _currentUiState.copy(
                    isLoading = false,
                    message = "Result code: ${result.code} message: ${result.message}",
                )

                is ApiException -> _currentUiState.copy(
                    isLoading = false,
                    message = "Exception: ${result.e.localizedMessage}",
                )
            }
            updateState(newState)
        }
    }

    private fun updateState(state: UiAvatarState) {
        _currentUiState = state
        _uiState.value = state
    }

    fun getToken(): String = repository.getToken()
}
