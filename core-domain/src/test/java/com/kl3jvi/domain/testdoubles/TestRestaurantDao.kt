package com.kl3jvi.domain.testdoubles

import com.kl3jvi.persistence.dao.RestaurantDao
import com.kl3jvi.persistence.models.RestaurantEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update


class TestRestaurantDao : RestaurantDao {

    private var entitiesStateFlow = MutableStateFlow(
        listOf(
            RestaurantEntity(
                id = 1,
                name = "Fast Food",
                status = "open",
                sortingValues = null
            )
        )
    )

    /**
     * "The getFavoriteRestaurants function returns a Flow of List of RestaurantEntity."
     *
     */
    override fun getFavoriteRestaurants(): Flow<List<RestaurantEntity>> = entitiesStateFlow

    /**
     * > The function returns the id of the restaurant that was passed in
     *
     * @param restaurant RestaurantEntity - This is the restaurant object that we want to insert into the database.
     * @return The id of the restaurant
     */
    override suspend fun insertRestaurant(restaurant: RestaurantEntity): Long {
        return restaurant.id
    }

    /**
     * > It returns the id of the restaurant with the given name, if it exists
     *
     * @param name The name of the restaurant
     * @return The id of the restaurant with the given name.
     */
    override suspend fun getRestaurantIdByName(name: String): Long? {
        return entitiesStateFlow.first().find { it.name == name }?.id
    }

    /**
     * > The function returns the first element of the entitiesStateFlow that matches the given predicate
     *
     * @param id Long - The id of the restaurant to be fetched
     * @return A RestaurantEntity
     */
    override suspend fun getRestaurantById(id: Long): RestaurantEntity {
        return entitiesStateFlow.first().find {
            it.id == id
        } ?: throw Exception("No restaurant found")
    }


    /**
     * "The function deletes a restaurant from the list of restaurants by filtering out the restaurant with the given id."
     *
     * @param id The id of the restaurant to delete.
     */
    override suspend fun deleteRestaurant(id: Long) {
        entitiesStateFlow.update { entities ->
            entities.filterNot { id == it.id }
        }
    }
}
