package com.n1rocket.truefaces.repository

import com.n1rocket.truefaces.api.ApiResult

internal interface IRepository {
    suspend fun testRepo(): ApiResult<String>
    suspend fun uploadImage(name: String, byteArray: ByteArray): ApiResult<String>
    fun isLogged(): Boolean
    suspend fun login(user: String, password: String): ApiResult<String>
}
