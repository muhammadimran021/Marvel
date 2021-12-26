package org.saulmm.marvel.utils.ext

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import org.saulmm.marvel.utils.FragmentViewBindingDelegate

@Suppress("unused")
fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T)
    : FragmentViewBindingDelegate<T> {
    return FragmentViewBindingDelegate(this, viewBindingFactory)
}
