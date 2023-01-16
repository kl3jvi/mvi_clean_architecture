package com.kl3jvi.domain.use_cases

import com.kl3jvi.domain.repository.RestaurantRepository
import javax.inject.Inject

class GetRestaurantsUseCase @Inject constructor(
    private val repository: RestaurantRepository
) {
    operator fun invoke() = repository.getRestaurants()
}
