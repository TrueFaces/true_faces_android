package com.n1rocket.truefaces.datasources

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface IPreferencesDataSource {
    suspend fun saveToken(token: String?)
    fun clear()
    fun isLogged() : Boolean
    fun getToken(): String
    fun getTokenFlow(viewModelScope: CoroutineScope): StateFlow<String>
    fun saveAvatar(avatar: String)
    fun getAvatar(): String
}