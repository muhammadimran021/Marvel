package org.saulmm.marvel.app.utils.ext

import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

var RecyclerView.isScrollable: Boolean
    set(value) {
        ViewCompat.setNestedScrollingEnabled(this, value)
    }
    get() = ViewCompat.isNestedScrollingEnabled(this)