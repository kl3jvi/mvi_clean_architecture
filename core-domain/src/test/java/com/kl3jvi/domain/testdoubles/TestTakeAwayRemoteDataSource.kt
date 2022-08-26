package com.kl3jvi.domain.testdoubles

import com.kl3jvi.data.datasource.TakeAwayRemoteDataSource
import com.kl3jvi.model.Restaurant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestTakeAwayRemoteDataSource(
    private val listRestaurant: List<Restaurant>,
) : TakeAwayRemoteDataSource {
    override fun getRestaurants(): Flow<List<Restaurant>> = flow {
        emit(listRestaurant)
    }
}