package com.n1rocket.truefaces.datasources

interface IRestDataSource {
    suspend fun testRepo(): String
    suspend fun uploadImage(name: String, byteArray: ByteArray): String
}
