package com.n1rocket.truefaces.ui.screens.login

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
class LoginViewModel @Inject constructor(
    private val repository: IRepository,
) : ViewModel() {

    // UI state
    private var _currentUiState: UiLoginState = UiLoginState.EmptyState
    private val _uiState = MutableStateFlow(_currentUiState)
    val uiState: StateFlow<UiLoginState> = _uiState.asStateFlow()

    fun login(user: String, password: String) {
        _uiState.value = UiLoginState.LoadingState
        CoroutineScope(Dispatchers.IO).launch {
            when (val result = repository.login(user, password)) {
                is ApiSuccess -> {
                    repository.saveToken(result.data.accessToken)

                    val avatar = when (val me = repository.me()) {
                        is ApiSuccess -> me.data.avatar
                        is ApiError -> ""
                        is ApiException -> ""
                    }

                    repository.saveAvatar(avatar)

                    _uiState.value = UiLoginState.FinishState(avatar.isNotEmpty())
                }

                is ApiError -> _uiState.value = UiLoginState.ErrorState(result.code, result.message)
                is ApiException -> _uiState.value = UiLoginState.ErrorState(500, result.e.localizedMessage)
            }
        }
    }
}
