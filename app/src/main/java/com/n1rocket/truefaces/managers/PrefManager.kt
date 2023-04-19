package com.n1rocket.truefaces.managers

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class PrefManager constructor(context: Context) {

    // ------- Preference Variables
    var token: String
        get() = pref.getString(KEY_TOKEN, "") ?: ""
        set(value) = pref.edit { putString(KEY_TOKEN, value) }

    var avatar: String
        get() = pref.getString(KEY_AVATAR, "") ?: ""
        set(value) = pref.edit { putString(KEY_AVATAR, value) }

    private val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    fun getTokenFlow(viewModelScope: CoroutineScope) = pref
        .getStringFlowForKey(KEY_TOKEN)
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")


    fun clear() = pref.edit {
        putString(KEY_TOKEN, "")
        clear()
    }

    companion object {
        private const val FILE_NAME = "PREFS_FILE_NAME"
        private const val KEY_TOKEN = "KEY_TOKEN"
        private const val KEY_AVATAR = "KEY_AVATAR"
    }
}
