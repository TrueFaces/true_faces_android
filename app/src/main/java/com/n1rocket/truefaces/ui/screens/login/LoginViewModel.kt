package com.n1rocket.truefaces.ui.screens.login

import android.util.Log
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
class LoginViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    // UI state
    private var _currentUiState: UiLoginState = UiLoginState.EmptyState
    private val _uiState = MutableStateFlow(_currentUiState)
    val uiState: StateFlow<UiLoginState> = _uiState.asStateFlow()

    fun login(user: String, password: String) {
        _uiState.value = UiLoginState.LoadingState
        CoroutineScope(Dispatchers.IO).launch {
            val message = when (val result = repository.login(user, password)) {
                is ApiSuccess -> result.data
                is ApiError -> "Result code: ${result.code} message: ${result.message}"
                is ApiException -> "Exception: ${result.e.localizedMessage}"
            }
            Log.d("LoginViewModel", message)
            _uiState.value = UiLoginState.FinishState
        }
    }
}
