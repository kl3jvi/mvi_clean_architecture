package com.kl3jvi.domain.di


import com.kl3jvi.data.repository.RestaurantRepository
import com.kl3jvi.data.repository.RestaurantRepositoryImpl

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindsRestaurantRepository(
        restaurantRepository: RestaurantRepositoryImpl
    ): RestaurantRepository
}