package com.kl3jvi.feature_home.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * "Launch a coroutine that repeats a block of code until the view lifecycle is destroyed."
 *
 * The function takes a `minActiveState` parameter that defaults to `STARTED`. This is the minimum
 * lifecycle state that the view must be in for the block of code to be executed
 *
 * @param minActiveState The minimum state the lifecycle must be in for the block to be executed.
 * @param block The block of code to run.
 */
inline fun Fragment.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        println("${this.coroutineContext} I'm working in thread ${Thread.currentThread().name}")
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}
