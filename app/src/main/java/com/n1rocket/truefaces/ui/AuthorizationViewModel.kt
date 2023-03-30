package com.n1rocket.truefaces.ui

import androidx.lifecycle.ViewModel
import com.n1rocket.truefaces.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    fun isLogged() = repository.isLogged()
}
