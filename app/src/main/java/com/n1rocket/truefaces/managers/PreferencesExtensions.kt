package com.n1rocket.truefaces.managers

import android.content.SharedPreferences
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow

fun SharedPreferences.getStringFlowForKey(k: String) = callbackFlow {
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (k == key) {
            trySend(getString(key, "") ?: "")
        }
    }
    registerOnSharedPreferenceChangeListener(listener)
    if (contains(k)) {
        send(getString(k, "") ?: "") // if you want to emit an initial pre-existing value
    }
    awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
}.buffer(Channel.UNLIMITED) // so trySend never fails
