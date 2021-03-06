package org.saulmm.marvel.app.utils.ext

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.saulmm.marvel.app.utils.FragmentViewBindingDelegate

@Suppress("unused")
fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T)
    : FragmentViewBindingDelegate<T> {
    return FragmentViewBindingDelegate(this, viewBindingFactory)
}

@Suppress("unused")
inline fun <reified T : Any> Fragment.argument(key: String, default: T? = null): Lazy<T> {
    return lazy {
        val value = arguments?.get(key)
        requireNotNull(if (value is T) value else default) { key }
    }
}

/**
 * Launches a new coroutine and repeats `block` every time the Fragment's viewLifecycleOwner
 * is in and out of `minActiveState` lifecycle state.
 * @see https://github.com/google/iosched/blob/main/mobile/src/main/java/com/google/samples/apps/iosched/util/UiUtils.kt#L60
 */
@Suppress("unused")
inline fun Fragment.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}