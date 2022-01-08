package org.saulmm.marvel.app.utils.ext

import android.view.View
import android.view.ViewGroup

fun View.updateMargins(
    start: Int? = null,
    top: Int? = null,
    end: Int? = null,
    bottom: Int? = null,
) {
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val params = this.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(
            start ?: params.leftMargin,
            top ?: params.topMargin,
            end ?: params.rightMargin,
            bottom ?: params.bottomMargin
        )
    }
}