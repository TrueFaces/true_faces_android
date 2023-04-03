package com.n1rocket.truefaces.datasources

import android.util.Log
import com.n1rocket.truefaces.managers.PrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

class PreferencesDataSource(private val prefManager: PrefManager) : IPreferencesDataSource {
    override suspend fun saveToken(token: String?) {
        prefManager.token = token ?: ""
    }

    override fun clear() {
        prefManager.clear()
    }

    override fun isLogged(): Boolean {
        val token = prefManager.token
        Log.d("PreferencesDataSource", "Token: $token")
        return token.isNotEmpty()
    }
    override fun getToken(): String = prefManager.token
    override fun getTokenFlow(viewModelScope: CoroutineScope): StateFlow<String> =
        prefManager.getTokenFlow(viewModelScope)
}
