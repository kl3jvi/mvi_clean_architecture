package com.kl3jvi.feature_home.testdoubles

import com.kl3jvi.domain.repository.RestaurantRepository
import com.kl3jvi.model.Restaurant
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class TestRestaurantRepository : RestaurantRepository {

    private val restaurantsFlow =
        MutableSharedFlow<List<Restaurant>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)


    /**
     * A test-only API to allow controlling the list of restaurants resources from tests.
     */
    fun sendRestaurantList(newsResources: List<Restaurant>) {
        restaurantsFlow.tryEmit(newsResources)
    }

    override fun getRestaurants(): Flow<List<Restaurant>> = restaurantsFlow


    override suspend fun toggleRestaurantFavorite(restaurant: Restaurant): Boolean {
        throw NotImplementedError("Unused in ViewModel tests")
    }
}