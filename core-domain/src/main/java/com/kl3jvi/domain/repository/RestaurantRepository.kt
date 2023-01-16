package com.kl3jvi.domain.repository

import com.kl3jvi.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {
    fun getRestaurants(): Flow<List<Restaurant>>
    suspend fun toggleRestaurantFavorite(restaurant: Restaurant): Boolean
}
