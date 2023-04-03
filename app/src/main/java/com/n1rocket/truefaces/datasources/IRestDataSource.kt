package com.n1rocket.truefaces.datasources

import com.n1rocket.truefaces.models.MeResponse
import com.n1rocket.truefaces.ui.screens.login.LoginResponse

interface IRestDataSource {
    suspend fun getImages(token: String): String
    suspend fun uploadImage(name: String, byteArray: ByteArray, token: String): String
    suspend fun login(user: String, password: String): LoginResponse
    suspend fun me(token: String): MeResponse
}
