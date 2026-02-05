package com.kl3jvi.feature_home.adapter

import com.kl3jvi.model.Restaurant

/**
 * Functional interface for favorite button click handling.
 * Can be used as a lambda: { restaurant -> ... }
 */
fun interface OnFavoriteButtonClickListener {
    fun toggleRestaurantFavoriteState(restaurant: Restaurant)
}
