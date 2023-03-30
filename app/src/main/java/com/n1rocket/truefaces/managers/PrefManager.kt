package com.n1rocket.truefaces.managers

import android.content.Context
import androidx.core.content.edit

class PrefManager constructor(context: Context) {

    // ------- Preference Variables
    var token: String
        get() = pref.getString(KEY_TOKEN, "") ?: ""
        set(value) = pref.edit { putString(KEY_TOKEN, value) }

    private val pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    fun clear() = pref.edit { clear() }

    companion object {
        private const val FILE_NAME = "PREFS_FILE_NAME"
        private const val KEY_TOKEN = "KEY_TOKEN"
    }
}
