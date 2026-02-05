package com.kl3jvi.data.repository

import com.kl3jvi.common.AppDispatchers.IO
import com.kl3jvi.common.Dispatcher
import com.kl3jvi.data.datasource.RestaurantDataSource
import com.kl3jvi.domain.repository.RestaurantRepository
import com.kl3jvi.model.Restaurant
import com.kl3jvi.persistence.dao.RestaurantDao
import com.kl3jvi.persistence.mapper.toDomainModel
import com.kl3jvi.persistence.mapper.toEntityModel
import com.kl3jvi.persistence.models.RestaurantEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val dataSource: RestaurantDataSource,
    private val local: RestaurantDao,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : RestaurantRepository {

    /**
     * > We're getting all the restaurants from the database and the network and combining them into a
     * single list without duplicates.
     *
     * @return A list of restaurants.
     */
    override fun getRestaurants(): Flow<List<Restaurant>> {
        /* Getting the favorite restaurants from the database and converting them to domain models. */
        val favoriteRestaurantsStream =
            local.getFavoriteRestaurants().mapNotNull { it?.map(RestaurantEntity::toDomainModel) }

        /* Getting the restaurants from the network and converting them to domain models. */
        val networkRestaurantsStream =
            dataSource.getRestaurants()

        /* Combining the two streams into a single stream. */
        return combine(
            favoriteRestaurantsStream,
            networkRestaurantsStream
        ) { favoriteRestaurants, networkRestaurants ->
            favoriteRestaurants
                .plus(networkRestaurants)
                .distinctBy(Restaurant::name)
        }
    }

    /**
     * > The function first gets the restaurant id from the local database, if the id is null, it
     * inserts the restaurant into the database, otherwise it deletes the restaurant from the database
     *
     * @param restaurant Restaurant - The restaurant to be toggled
     */
    override suspend fun toggleRestaurantFavorite(restaurant: Restaurant) =
        withContext(ioDispatcher) {
            val restaurantEntity = restaurant.toEntityModel()
            val id = local.getRestaurantIdByName(restaurantEntity.name)
            if (id == null) {
                local.insertRestaurant(restaurantEntity).let { true }
            } else {
                local.deleteRestaurant(id).let { false }
            }
        }
}
