package com.n1rocket.truefaces.repository

import com.n1rocket.truefaces.api.ApiResult
import com.n1rocket.truefaces.models.MeResponse
import com.n1rocket.truefaces.ui.screens.login.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

internal interface IRepository {
    suspend fun getImages(): ApiResult<String>
    suspend fun uploadImage(name: String, byteArray: ByteArray): ApiResult<String>
    fun isLogged(): Boolean
    suspend fun login(user: String, password: String): ApiResult<LoginResponse>
    suspend fun saveToken(accessToken: String)
    suspend fun me(): ApiResult<MeResponse>
    fun getTokenFlow(viewModelScope: CoroutineScope): StateFlow<String>
    fun logout()
}
