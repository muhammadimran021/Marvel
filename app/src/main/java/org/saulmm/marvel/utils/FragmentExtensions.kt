package org.saulmm.marvel.utils

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

@Suppress("unused")
fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T)
    : FragmentViewBindingDelegate<T> {
    return FragmentViewBindingDelegate(this, viewBindingFactory)
}
