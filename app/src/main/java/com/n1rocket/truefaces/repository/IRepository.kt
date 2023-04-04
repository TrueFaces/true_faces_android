package com.n1rocket.truefaces.repository

import com.n1rocket.truefaces.api.ApiResult
import com.n1rocket.truefaces.models.ImagesResponse
import com.n1rocket.truefaces.models.MeResponse
import com.n1rocket.truefaces.models.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

internal interface IRepository {
    suspend fun getImages(): ApiResult<List<ImagesResponse>>
    suspend fun uploadImage(name: String, byteArray: ByteArray): ApiResult<String>
    suspend fun login(user: String, password: String): ApiResult<LoginResponse>
    suspend fun saveToken(accessToken: String)
    suspend fun me(): ApiResult<MeResponse>
    fun getTokenFlow(viewModelScope: CoroutineScope): StateFlow<String>
    fun isLogged(): Boolean
    fun logout()
}
