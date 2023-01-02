package com.kl3jvi.data.di


import com.kl3jvi.data.repository.RestaurantRepositoryImpl
import com.kl3jvi.domain.repository.RestaurantRepository

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