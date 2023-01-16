package com.kl3jvi.data.testdoubles

import com.kl3jvi.persistence.dao.RestaurantDao
import com.kl3jvi.persistence.models.RestaurantEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestRestaurantDao : RestaurantDao {

    private val restaurants = mutableListOf(
        RestaurantEntity(
            id = 1,
            name = "Fast Food",
            status = "open",
            sortingValues = null
        )
    )

    override fun getFavoriteRestaurants(): Flow<List<RestaurantEntity>> =
        MutableStateFlow(restaurants)

    override suspend fun insertRestaurant(restaurant: RestaurantEntity): Long {
        restaurants.add(restaurant)
        return restaurant.id
    }

    override suspend fun getRestaurantIdByName(name: String): Long? {
        return restaurants.find { it.name == name }?.id
    }

    override suspend fun getRestaurantById(id: Long): RestaurantEntity {
        return restaurants.find {
            it.id == id
        } ?: throw Exception("No restaurant found")
    }

    override suspend fun deleteRestaurant(id: Long) {
        restaurants.removeIf { it.id == id }
    }
}
