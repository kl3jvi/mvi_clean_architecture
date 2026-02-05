package com.kl3jvi.feature_home.viewModel

import app.cash.turbine.test
import com.kl3jvi.domain.use_cases.GetSortedRestaurantsUseCase
import com.kl3jvi.domain.use_cases.SortOption
import com.kl3jvi.domain.use_cases.ToggleFavoriteUseCase
import com.kl3jvi.feature_home.common.MainDispatcherRule
import com.kl3jvi.feature_home.home.HomeIntent
import com.kl3jvi.feature_home.home.HomeState
import com.kl3jvi.feature_home.home.HomeViewModel
import com.kl3jvi.feature_home.home.SortOptions
import com.kl3jvi.feature_home.testdoubles.TestRestaurantRepository
import com.kl3jvi.model.Restaurant
import com.kl3jvi.model.SortingValues
import com.kl3jvi.model.Status
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class HomeViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val repository = TestRestaurantRepository()
    private lateinit var getSortedRestaurantsUseCase: GetSortedRestaurantsUseCase
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase
    private lateinit var viewModel: HomeViewModel
    private var testDispatcher: TestDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        getSortedRestaurantsUseCase = GetSortedRestaurantsUseCase(repository, testDispatcher)
        toggleFavoriteUseCase = ToggleFavoriteUseCase(repository)

        viewModel = HomeViewModel(
            getSortedRestaurantsUseCase = getSortedRestaurantsUseCase,
            toggleFavoriteUseCase = toggleFavoriteUseCase,
            ioDispatcher = testDispatcher
        )
    }

    @Test
    fun `initial state should be loading`() = runTest(testDispatcher) {
        assertEquals(HomeState.Initial.isLoading, true)
    }

    @Test
    fun `state should have empty restaurants initially`() = runTest(testDispatcher) {
        assertEquals(HomeState.Initial.restaurants, emptyList())
    }

    @Test
    fun `processIntent SelectSortOption should update sortOption in state`() = runTest(testDispatcher) {
        viewModel.state.test {
            // Initial state
            val initial = awaitItem()
            assertEquals(SortOptions.BEST_MATCH, initial.sortOption)

            // Process intent to change sort option
            viewModel.processIntent(HomeIntent.SelectSortOption(SortOptions.RATING_AVERAGE))

            // Await state update
            val updated = awaitItem()
            assertEquals(SortOptions.RATING_AVERAGE, updated.sortOption)
            assertEquals(2, updated.selectedSortIndex)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `state should show restaurants after loading`() = runTest(testDispatcher) {
        val testRestaurants = listOf(
            createTestRestaurant("Test Restaurant 1"),
            createTestRestaurant("Test Restaurant 2")
        )

        viewModel.state.test {
            // Skip initial loading state
            awaitItem()

            // Send test data
            repository.sendRestaurantList(testRestaurants)

            // Should eventually show success state with restaurants
            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertEquals(2, successState.restaurants.size)

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createTestRestaurant(name: String) = Restaurant(
        name = name,
        status = Status.OPEN,
        isFavorite = false,
        sortingValues = SortingValues(
            bestMatch = 1.0,
            newest = 1.0,
            ratingAverage = 4.5,
            distance = 1000,
            popularity = 100.0,
            averageProductPrice = 1500,
            deliveryCosts = 200,
            minCost = 1000
        )
    )
}
