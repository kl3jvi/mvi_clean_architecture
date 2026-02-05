package com.kl3jvi.data.datasource

import com.kl3jvi.model.Restaurant
import kotlinx.coroutines.flow.Flow

/**
 * Data source interface for restaurant data.
 * This is a data layer concern - implementations handle network/local data retrieval.
 */
interface RestaurantDataSource {
    /**
     * Get restaurants from the data source as a Flow.
     * @return Flow emitting list of restaurants
     */
    fun getRestaurants(): Flow<List<Restaurant>>
}
