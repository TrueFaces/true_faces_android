package com.n1rocket.truefaces.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1rocket.truefaces.repository.IRepository
import com.n1rocket.truefaces.repository.Repository
import com.n1rocket.truefaces.ui.screens.main.UiMainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(private val repository: IRepository) : ViewModel() {
    val state: StateFlow<String> = repository.getTokenFlow(viewModelScope)
    fun isLogged() = repository.isLogged()
    fun hasAvatar() = repository.getAvatar().isNotEmpty()
}
