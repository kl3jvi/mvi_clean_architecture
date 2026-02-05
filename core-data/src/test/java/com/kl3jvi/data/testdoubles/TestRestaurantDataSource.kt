package com.kl3jvi.data.testdoubles

import com.kl3jvi.data.datasource.RestaurantDataSource
import com.kl3jvi.model.Restaurant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class TestRestaurantDataSource(
    private val listRestaurant: List<Restaurant>
) : RestaurantDataSource {

    override fun getRestaurants(): Flow<List<Restaurant>> = flowOf(listRestaurant)
}
