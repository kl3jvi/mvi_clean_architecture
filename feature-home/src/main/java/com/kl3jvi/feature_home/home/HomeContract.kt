package com.kl3jvi.feature_home.home

import com.kl3jvi.common.mvi.MviEffect
import com.kl3jvi.common.mvi.MviIntent
import com.kl3jvi.common.mvi.MviState
import com.kl3jvi.model.Restaurant

/**
 * MVI Contract for the Home screen.
 * Defines all possible user intentions, UI states, and side effects.
 */

// ============================================
// INTENTS - User Actions
// ============================================

sealed interface HomeIntent : MviIntent {
    /** User selected a sorting option */
    data class SelectSortOption(val option: SortOptions) : HomeIntent

    /** User tapped the favorite button on a restaurant */
    data class ToggleFavorite(val restaurant: Restaurant) : HomeIntent

    /** User pulled to refresh */
    data object Refresh : HomeIntent
}

// ============================================
// STATE - Immutable UI State
// ============================================

data class HomeState(
    val isLoading: Boolean = true,
    val restaurants: List<Restaurant> = emptyList(),
    val sortOption: SortOptions = SortOptions.BEST_MATCH,
    val selectedSortIndex: Int = 0,
    val error: HomeError? = null
) : MviState {

    companion object {
        val Initial = HomeState()
    }
}

/**
 * Typed error for better error handling
 */
sealed interface HomeError {
    data class NetworkError(val message: String, val cause: Throwable? = null) : HomeError
    data class UnknownError(val message: String, val cause: Throwable? = null) : HomeError
}

// ============================================
// EFFECTS - One-Time Events
// ============================================

sealed interface HomeEffect : MviEffect {
    /** Show a toast/snackbar message */
    data class ShowMessage(val message: String) : HomeEffect

    /** Navigate to restaurant details */
    data class NavigateToDetails(val restaurant: Restaurant) : HomeEffect

    /** Show error dialog */
    data class ShowError(val error: HomeError) : HomeEffect
}
