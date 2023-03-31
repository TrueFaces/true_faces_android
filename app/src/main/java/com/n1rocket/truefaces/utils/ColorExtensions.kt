package com.n1rocket.truefaces.utils

import android.graphics.Color
import androidx.annotation.FloatRange

/**
 * Determines if this color is dark.
 * @param threshold - min darkness value; the higher the value, the darker the color;
 * float value between 0.0 and 1.0.
 */
fun Int.isColorDark(@FloatRange(from = 0.0, to = 1.0) threshold: Float = 0.9f): Boolean {
    val darkness = 1 - (Color.red(this) * 0.299 + Color.green(this) * 0.587 + Color.blue(this) * 0.114) / 255
    return darkness >= threshold
}