package com.kl3jvi.feature_home.shared

import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.PRIVATE
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.common.AppDispatchers.DEFAULT
import com.kl3jvi.common.AppDispatchers.IO
import com.kl3jvi.common.Dispatcher
import com.kl3jvi.common.result.Result
import com.kl3jvi.common.result.asResult
import com.kl3jvi.domain.use_cases.GetRestaurantsUseCase
import com.kl3jvi.domain.use_cases.ToggleFavoriteUseCase
import com.kl3jvi.feature_home.adapter.OnFavoriteButtonClickListener
import com.kl3jvi.feature_home.home.SortOptions
import com.kl3jvi.feature_home.home.SortOptions.*
import com.kl3jvi.model.Restaurant
import com.kl3jvi.model.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    @Dispatcher(DEFAULT) private val defaultDispatcher: CoroutineDispatcher
) : ViewModel(), OnFavoriteButtonClickListener {

    /* A private variable that is only visible for testing. */
    @VisibleForTesting(otherwise = PRIVATE)
    var sortOption = MutableStateFlow(BEST_MATCH)
    var checkedItem = MutableStateFlow(0)

    val uiRestaurantState: StateFlow<RestaurantUiState> = sortOption.flatMapLatest { sortOption ->
        getRestaurantsUseCase().asResult().map { result ->
            when (result) {
                is Result.Error -> RestaurantUiState.Error("Error Occurred")
                Result.Loading -> RestaurantUiState.Loading
                is Result.Success -> {
                    val restaurantList = result.data
                    RestaurantUiState.Success(restaurantList.sortedInBg(sortOption))
                }
            }
        }
    }.stateIn(
        scope = viewModelScope, /*Keeps the upstream flow active for 5 seconds more after the disappearance of the last collector.
            That avoids restarting the upstream flow in certain situations such as configuration changes. */
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = RestaurantUiState.Loading
    )

    /**
     * > When the user selects a sort option, update the sort option in the view model
     *
     * @param optionSelected The sort option that was selected by the user.
     */
    fun onSortOptionSelected(optionSelected: SortOptions) {
        sortOption.value = optionSelected
    }

    /* Sorting the list of restaurants based on the sort option. */
    private suspend fun List<Restaurant>.sortedInBg(option: SortOptions) =
        withContext(viewModelScope.coroutineContext.plus(defaultDispatcher)) {
            /* A function that returns a comparator based on the sort option. */
            val sortFunction: Comparator<Restaurant> = when (option) {
                BEST_MATCH -> compareBy { it.sortingValues?.bestMatch }
                NEWEST -> compareByDescending { it.sortingValues?.newest }
                RATING_AVERAGE -> compareBy { it.sortingValues?.ratingAverage }
                DISTANCE -> compareByDescending { it.sortingValues?.distance }
                POPULARITY -> compareBy { it.sortingValues?.popularity }
                AVERAGE_PRODUCT_PRICE -> compareByDescending { it.sortingValues?.averageProductPrice }
                DELIVERY_COST -> compareByDescending { it.sortingValues?.deliveryCosts }
                MIN_COST -> compareByDescending { it.sortingValues?.minCost }
            }

            /* Sorting the list of restaurants based on the sort option. */
            sortedWith(
                compareBy<Restaurant> { it.isFavorite }
                    .thenBy { it.status == Status.OPEN }
                    .thenBy { it.status == Status.ORDER_AHEAD }
                    .thenBy { it.status == Status.CLOSED }
                    .then(sortFunction)
                    .reversed()
            )
        }

    /**
     * "When the user taps the favorite button, toggle the favorite state of the restaurant in the
     * database."
     *
     * @param restaurant Restaurant - The restaurant to be toggled
     */
    override fun toggleRestaurantFavoriteState(restaurant: Restaurant) {
        viewModelScope.launch(ioDispatcher) { toggleFavoriteUseCase(restaurant) }
    }
}

sealed interface RestaurantUiState {
    data class Success(val restaurantList: List<Restaurant>) : RestaurantUiState
    data class Error(val errorMessage: String) : RestaurantUiState
    object Loading : RestaurantUiState
}
