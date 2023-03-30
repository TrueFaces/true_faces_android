package com.n1rocket.truefaces.datasources

import com.n1rocket.truefaces.ui.screens.login.LoginResponse

interface IRestDataSource {
    suspend fun testRepo(): String
    suspend fun uploadImage(name: String, byteArray: ByteArray): String
    suspend fun login(user: String, password: String): LoginResponse
}
