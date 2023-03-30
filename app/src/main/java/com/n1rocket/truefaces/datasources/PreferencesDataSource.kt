package com.n1rocket.truefaces.datasources

import com.n1rocket.truefaces.managers.PrefManager

class PreferencesDataSource(private val prefManager: PrefManager) : IPreferencesDataSource {
    override suspend fun saveToken(token: String?) {
        prefManager.token = token ?: ""
    }

    override fun isLogged(): Boolean = prefManager.token.isNotEmpty()
}
