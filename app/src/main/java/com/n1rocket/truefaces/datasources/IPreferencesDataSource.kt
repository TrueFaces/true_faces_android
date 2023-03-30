package com.n1rocket.truefaces.datasources

interface IPreferencesDataSource {
    suspend fun saveToken(token: String?)
    fun isLogged() : Boolean
}