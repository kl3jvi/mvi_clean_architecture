package com.kl3jvi.feature_home.testdoubles

import com.kl3jvi.domain.repository.RestaurantRepository
import com.kl3jvi.model.Restaurant
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class TestRestaurantRepository : RestaurantRepository {

    private val restaurantsFlow =
        MutableSharedFlow<List<Restaurant>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    private var toggleFavoriteResult: Boolean = true
    private var lastToggledRestaurant: Restaurant? = null

    /**
     * A test-only API to allow controlling the list of restaurants from tests.
     */
    fun sendRestaurantList(restaurants: List<Restaurant>) {
        restaurantsFlow.tryEmit(restaurants)
    }

    /**
     * Set the result that toggleRestaurantFavorite should return.
     */
    fun setToggleFavoriteResult(result: Boolean) {
        toggleFavoriteResult = result
    }

    /**
     * Get the last restaurant that was toggled.
     */
    fun getLastToggledRestaurant(): Restaurant? = lastToggledRestaurant

    override fun getRestaurants(): Flow<List<Restaurant>> = restaurantsFlow

    override suspend fun toggleRestaurantFavorite(restaurant: Restaurant): Boolean {
        lastToggledRestaurant = restaurant
        return toggleFavoriteResult
    }
}
