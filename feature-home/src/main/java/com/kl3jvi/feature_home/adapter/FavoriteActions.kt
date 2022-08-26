package com.kl3jvi.feature_home.adapter

import com.kl3jvi.model.Restaurant


interface OnFavoriteButtonClickListener {
    fun toggleRestaurantFavoriteState(restaurant: Restaurant)
}