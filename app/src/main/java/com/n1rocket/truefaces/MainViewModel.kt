package com.n1rocket.truefaces

import android.util.Log
import androidx.lifecycle.ViewModel
import com.n1rocket.truefaces.api.ApiError
import com.n1rocket.truefaces.api.ApiException
import com.n1rocket.truefaces.api.ApiSuccess
import com.n1rocket.truefaces.datasources.RestDataSource
import com.n1rocket.truefaces.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    //private val repository = Repository(RestDataSource(url = "https://apifast-2-r0282118.deta.app"))
    private val repository = Repository(RestDataSource(url = "https://apifast-1-r0282118.deta.app"))

    // UI state
    private var _currentUiState: UiState = UiState.EmptyState
    private val _uiState = MutableStateFlow(_currentUiState)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun testRepo() {
        CoroutineScope(Dispatchers.IO).launch {
            when (val result = repository.testRepo()) {
                is ApiSuccess -> Log.d("MainViewModel", "Result: ${result.data}")
                is ApiError -> Log.d("MainViewModel", "Result code: ${result.code} message: ${result.message}")
                is ApiException -> Log.d("MainViewModel", "Result Exc: ${result.e.localizedMessage}")
            }
        }
    }

    fun uploadImage(name: String, byteArray: ByteArray) {
        _uiState.value = UiState.UploadingState
        CoroutineScope(Dispatchers.IO).launch {
            val message = when (val result = repository.uploadImage(name, byteArray)) {
                is ApiSuccess -> result.data
                is ApiError -> "Result code: ${result.code} message: ${result.message}"
                is ApiException -> "Exception: ${result.e.localizedMessage}"
            }
            _uiState.value = UiState.FinishState(message = message)
        }
    }
}
