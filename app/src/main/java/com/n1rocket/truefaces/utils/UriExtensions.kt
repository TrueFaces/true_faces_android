package com.n1rocket.truefaces.utils

import android.content.Context
import android.net.Uri
import java.io.IOException

@Throws(IOException::class)
fun Uri.readBytes(context: Context): ByteArray? {
    context.contentResolver.apply {
        val inputStream = openInputStream(this@readBytes)
        val result = inputStream?.buffered()?.use { it.readBytes() }
        inputStream?.close()
        return result
    }
}