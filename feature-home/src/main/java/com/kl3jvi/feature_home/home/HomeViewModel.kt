package com.kl3jvi.feature_home.home

import androidx.lifecycle.viewModelScope
import com.kl3jvi.common.AppDispatchers.IO
import com.kl3jvi.common.Dispatcher
import com.kl3jvi.common.mvi.MviViewModel
import com.kl3jvi.common.result.Result
import com.kl3jvi.common.result.asResult
import com.kl3jvi.domain.use_cases.GetSortedRestaurantsUseCase
import com.kl3jvi.domain.use_cases.SortOption
import com.kl3jvi.domain.use_cases.ToggleFavoriteUseCase
import com.kl3jvi.model.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * MVI ViewModel for the Home screen.
 *
 * Implements unidirectional data flow:
 * - View emits [HomeIntent] via processIntent()
 * - ViewModel processes intents and updates [HomeState]
 * - View renders state and reacts to [HomeEffect]
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSortedRestaurantsUseCase: GetSortedRestaurantsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : MviViewModel<HomeIntent, HomeState, HomeEffect>(HomeState.Initial) {

    private var restaurantJob: Job? = null

    init {
        loadRestaurants()
    }

    override suspend fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.SelectSortOption -> handleSortSelection(intent.option)
            is HomeIntent.ToggleFavorite -> handleToggleFavorite(intent.restaurant)
            HomeIntent.Refresh -> handleRefresh()
        }
    }

    private fun handleSortSelection(option: SortOptions) {
        val sortIndex = SortOptions.entries.indexOf(option)
        reduce {
            copy(
                sortOption = option,
                selectedSortIndex = sortIndex
            )
        }
        loadRestaurants()
    }

    private suspend fun handleToggleFavorite(restaurant: Restaurant) {
        viewModelScope.launch(ioDispatcher) {
            try {
                val isFavorite = toggleFavoriteUseCase(restaurant)
                val message = if (isFavorite) {
                    "${restaurant.name} added to favorites"
                } else {
                    "${restaurant.name} removed from favorites"
                }
                postEffect(HomeEffect.ShowMessage(message))
            } catch (e: Exception) {
                postEffect(HomeEffect.ShowError(HomeError.UnknownError("Failed to update favorite", e)))
            }
        }
    }

    private fun handleRefresh() {
        loadRestaurants()
    }

    private fun loadRestaurants() {
        restaurantJob?.cancel()
        restaurantJob = viewModelScope.launch {
            val sortOption = currentState.sortOption.toDomainSortOption()
            getSortedRestaurantsUseCase(sortOption)
                .asResult()
                .collectLatest { result ->
                    when (result) {
                        is Result.Loading -> reduce {
                            copy(isLoading = true, error = null)
                        }
                        is Result.Success -> reduce {
                            copy(
                                isLoading = false,
                                restaurants = result.data,
                                error = null
                            )
                        }
                        is Result.Error -> {
                            reduce {
                                copy(
                                    isLoading = false,
                                    error = HomeError.UnknownError(
                                        message = result.exception?.message ?: "Unknown error",
                                        cause = result.exception
                                    )
                                )
                            }
                            postEffect(
                                HomeEffect.ShowError(
                                    HomeError.UnknownError(
                                        message = "Failed to load restaurants",
                                        cause = result.exception
                                    )
                                )
                            )
                        }
                    }
                }
        }
    }

    /**
     * Maps presentation-layer SortOptions to domain-layer SortOption
     */
    private fun SortOptions.toDomainSortOption(): SortOption = when (this) {
        SortOptions.BEST_MATCH -> SortOption.BEST_MATCH
        SortOptions.NEWEST -> SortOption.NEWEST
        SortOptions.RATING_AVERAGE -> SortOption.RATING_AVERAGE
        SortOptions.DISTANCE -> SortOption.DISTANCE
        SortOptions.POPULARITY -> SortOption.POPULARITY
        SortOptions.AVERAGE_PRODUCT_PRICE -> SortOption.AVERAGE_PRODUCT_PRICE
        SortOptions.DELIVERY_COST -> SortOption.DELIVERY_COST
        SortOptions.MIN_COST -> SortOption.MIN_COST
    }
}
