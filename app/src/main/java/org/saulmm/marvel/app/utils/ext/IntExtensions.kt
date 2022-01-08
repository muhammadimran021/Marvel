package org.saulmm.marvel.app.utils.ext

import android.content.res.Resources
import android.util.DisplayMetrics

val Int.dp: Float
    get() {
        if (this == 0) {
            return 0f
        }

        val metrics = Resources.getSystem().displayMetrics
        return this * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }