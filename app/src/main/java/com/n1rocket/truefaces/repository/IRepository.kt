package com.n1rocket.truefaces.repository

import com.n1rocket.truefaces.api.ApiResult

internal interface IRepository {
    suspend fun uploadImage(name: String, byteArray: ByteArray): ApiResult<String>
}
