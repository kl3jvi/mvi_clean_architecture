package com.kl3jvi.domain.use_cases

import com.kl3jvi.common.AppDispatchers.DEFAULT
import com.kl3jvi.common.Dispatcher
import com.kl3jvi.domain.repository.RestaurantRepository
import com.kl3jvi.model.Restaurant
import com.kl3jvi.model.Status
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Use case for retrieving and sorting restaurants.
 * Contains the business logic for restaurant sorting that was previously in the ViewModel.
 */
class GetSortedRestaurantsUseCase @Inject constructor(
    private val repository: RestaurantRepository,
    @Dispatcher(DEFAULT) private val defaultDispatcher: CoroutineDispatcher
) {
    /**
     * Get restaurants sorted by the specified option.
     * Sorting prioritizes: favorites > status (open > order ahead > closed) > sorting option
     *
     * @param sortOption The sorting criteria to apply
     * @return Flow of sorted restaurant list
     */
    operator fun invoke(sortOption: SortOption): Flow<List<Restaurant>> {
        return repository.getRestaurants().map { restaurants ->
            withContext(defaultDispatcher) {
                restaurants.sortedWith(buildComparator(sortOption))
            }
        }
    }

    private fun buildComparator(option: SortOption): Comparator<Restaurant> {
        val sortFunction: Comparator<Restaurant> = when (option) {
            SortOption.BEST_MATCH -> compareBy { it.sortingValues?.bestMatch }
            SortOption.NEWEST -> compareByDescending { it.sortingValues?.newest }
            SortOption.RATING_AVERAGE -> compareByDescending { it.sortingValues?.ratingAverage }
            SortOption.DISTANCE -> compareBy { it.sortingValues?.distance }
            SortOption.POPULARITY -> compareByDescending { it.sortingValues?.popularity }
            SortOption.AVERAGE_PRODUCT_PRICE -> compareBy { it.sortingValues?.averageProductPrice }
            SortOption.DELIVERY_COST -> compareBy { it.sortingValues?.deliveryCosts }
            SortOption.MIN_COST -> compareBy { it.sortingValues?.minCost }
        }

        // Priority: Favorites first, then by status, then by sort option
        return compareByDescending<Restaurant> { it.isFavorite }
            .thenByDescending { it.status == Status.OPEN }
            .thenByDescending { it.status == Status.ORDER_AHEAD }
            .thenByDescending { it.status == Status.CLOSED }
            .then(sortFunction)
    }
}

/**
 * Domain-level sort options.
 * Decoupled from presentation layer's SortOptions enum.
 */
enum class SortOption {
    BEST_MATCH,
    NEWEST,
    RATING_AVERAGE,
    DISTANCE,
    POPULARITY,
    AVERAGE_PRODUCT_PRICE,
    DELIVERY_COST,
    MIN_COST
}
