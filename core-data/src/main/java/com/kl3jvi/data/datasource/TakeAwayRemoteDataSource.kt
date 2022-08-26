package com.kl3jvi.data.datasource

import com.kl3jvi.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface TakeAwayRemoteDataSource {
    fun getRestaurants(): Flow<List<Restaurant>>
}