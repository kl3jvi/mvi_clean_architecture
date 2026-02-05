package com.kl3jvi.feature_home.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.domain.repository.RestaurantRepository
import com.kl3jvi.model.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for restaurant details screen.
 * 
 * Uses SavedStateHandle to persist the restaurant name across process death,
 * and observes the repository directly to get current favorite state.
 */
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: RestaurantRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsState())
    val state: StateFlow<DetailsState> = _state.asStateFlow()

    init {
        observeFavoriteState()
    }

    /**
     * Initialize with restaurant data from navigation args.
     * This should be called once when the fragment is created.
     */
    fun initialize(restaurant: Restaurant) {
        savedStateHandle[KEY_RESTAURANT_NAME] = restaurant.name
        _state.update { it.copy(restaurant = restaurant) }
    }

    /**
     * Toggle favorite status of the current restaurant.
     */
    fun toggleFavorite() {
        val restaurant = _state.value.restaurant ?: return
        viewModelScope.launch {
            val newFavoriteState = repository.toggleRestaurantFavorite(restaurant)
            _state.update { currentState ->
                currentState.copy(
                    restaurant = currentState.restaurant?.copy(isFavorite = newFavoriteState)
                )
            }
        }
    }

    /**
     * Observe repository for favorite state changes.
     * This ensures we always show the correct state from the source of truth.
     */
    private fun observeFavoriteState() {
        viewModelScope.launch {
            repository.getRestaurants().collectLatest { restaurants ->
                val restaurantName = savedStateHandle.get<String>(KEY_RESTAURANT_NAME)
                    ?: _state.value.restaurant?.name
                    ?: return@collectLatest

                val currentRestaurant = restaurants.find { it.name == restaurantName }
                if (currentRestaurant != null) {
                    _state.update { it.copy(restaurant = currentRestaurant) }
                }
            }
        }
    }

    companion object {
        private const val KEY_RESTAURANT_NAME = "restaurant_name"
    }
}

/**
 * UI state for details screen.
 */
data class DetailsState(
    val restaurant: Restaurant? = null
) {
    val isFavorite: Boolean get() = restaurant?.isFavorite ?: false
}
