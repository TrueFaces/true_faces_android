package com.n1rocket.truefaces.datasources

import com.n1rocket.truefaces.api.ApiResult
import com.n1rocket.truefaces.models.ImagesResponse
import com.n1rocket.truefaces.models.MeResponse
import com.n1rocket.truefaces.models.LoginResponse

interface IRestDataSource {
    suspend fun getImages(token: String): List<ImagesResponse>
    suspend fun uploadImage(name: String, byteArray: ByteArray, token: String): String
    suspend fun login(user: String, password: String): LoginResponse
    suspend fun me(token: String): MeResponse
}
