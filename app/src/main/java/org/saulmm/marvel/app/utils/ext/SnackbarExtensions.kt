package org.saulmm.marvel.app.utils.ext

import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar

fun Snackbar.setMarginBottom(
    marginBottom: Int
) {
    val snackbarView = view
    val params = view.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(
        params.leftMargin,
        params.topMargin,
        params.rightMargin,
        marginBottom
    )

    snackbarView.layoutParams = params
}