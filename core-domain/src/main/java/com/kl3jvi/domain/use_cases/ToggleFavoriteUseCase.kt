package com.kl3jvi.domain.use_cases

import com.kl3jvi.domain.repository.RestaurantRepository
import com.kl3jvi.model.Restaurant
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: RestaurantRepository
) {
    suspend operator fun invoke(restaurant: Restaurant) = repository.toggleRestaurantFavorite(restaurant)
}
