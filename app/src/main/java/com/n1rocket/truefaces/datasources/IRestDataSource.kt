package com.n1rocket.truefaces.datasources

internal interface IRestDataSource {
    suspend fun uploadImage(name: String, byteArray: ByteArray): String
}
