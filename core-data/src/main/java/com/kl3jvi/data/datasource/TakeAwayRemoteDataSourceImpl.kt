package com.kl3jvi.data.datasource

import com.kl3jvi.common.AppDispatchers.IO
import com.kl3jvi.common.Dispatcher
import com.kl3jvi.data.api_data.RestaurantJson
import com.kl3jvi.data.model.JsonNetworkResponse
import com.kl3jvi.model.Restaurant
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class TakeAwayRemoteDataSourceImpl @Inject constructor(
    private val networkJson: Json,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : TakeAwayRemoteDataSource {
    /**
     * "Get the restaurants from the "network" and emit them as a flow."
     */
    override fun getRestaurants(): Flow<List<Restaurant>> = flow {
        val parsedJson =
            networkJson.decodeFromString<JsonNetworkResponse>(RestaurantJson.restaurantData).restaurants
                ?: emptyList()

        emit(parsedJson)
    }.flowOn(ioDispatcher)

}

