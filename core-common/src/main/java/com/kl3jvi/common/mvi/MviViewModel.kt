package com.kl3jvi.common.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Base MVI ViewModel that enforces unidirectional data flow.
 *
 * @param I Intent - User actions/events that trigger state changes
 * @param S State - Immutable UI state that the View renders
 * @param E Effect - One-time events (navigation, toasts, dialogs)
 */
abstract class MviViewModel<I : MviIntent, S : MviState, E : MviEffect>(
    initialState: S
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _effects = Channel<E>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    private val intents = MutableSharedFlow<I>(extraBufferCapacity = 64)

    init {
        viewModelScope.launch {
            intents.collect { intent ->
                handleIntent(intent)
            }
        }
    }

    /**
     * Process a user intent. Call this from the View layer.
     */
    fun processIntent(intent: I) {
        viewModelScope.launch {
            intents.emit(intent)
        }
    }

    /**
     * Handle the intent and produce state changes or effects.
     * Implement this in your concrete ViewModel.
     */
    protected abstract suspend fun handleIntent(intent: I)

    /**
     * Update the UI state atomically using a reducer function.
     */
    protected fun reduce(reducer: S.() -> S) {
        _state.value = _state.value.reducer()
    }

    /**
     * Post a one-time effect (navigation, snackbar, etc.)
     */
    protected suspend fun postEffect(effect: E) {
        _effects.send(effect)
    }

    /**
     * Current state value for immediate access
     */
    protected val currentState: S get() = _state.value
}

/**
 * Marker interface for MVI Intents (user actions)
 */
interface MviIntent

/**
 * Marker interface for MVI State (immutable UI state)
 */
interface MviState

/**
 * Marker interface for MVI Effects (one-time events)
 */
interface MviEffect
