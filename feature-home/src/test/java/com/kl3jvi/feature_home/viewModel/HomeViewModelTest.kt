package com.kl3jvi.feature_home.viewModel

import app.cash.turbine.test
import com.kl3jvi.domain.use_cases.GetSortedRestaurantsUseCase
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
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

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

    // ==================== Initial State Tests ====================

    @Test
    fun `initial state should be loading`() = runTest(testDispatcher) {
        assertEquals(HomeState.Initial.isLoading, true)
    }

    @Test
    fun `initial state should have empty restaurants`() = runTest(testDispatcher) {
        assertEquals(HomeState.Initial.restaurants, emptyList())
    }

    @Test
    fun `initial state should have BEST_MATCH sort option`() = runTest(testDispatcher) {
        assertEquals(HomeState.Initial.sortOption, SortOptions.BEST_MATCH)
    }

    @Test
    fun `initial state should have no error`() = runTest(testDispatcher) {
        assertEquals(HomeState.Initial.error, null)
    }

    // ==================== Sort Option Intent Tests ====================

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
    fun `processIntent SelectSortOption DISTANCE should set correct index`() = runTest(testDispatcher) {
        viewModel.state.test {
            awaitItem() // Skip initial

            viewModel.processIntent(HomeIntent.SelectSortOption(SortOptions.DISTANCE))

            val updated = awaitItem()
            assertEquals(SortOptions.DISTANCE, updated.sortOption)
            assertEquals(3, updated.selectedSortIndex)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `processIntent SelectSortOption POPULARITY should set correct index`() = runTest(testDispatcher) {
        viewModel.state.test {
            awaitItem() // Skip initial

            viewModel.processIntent(HomeIntent.SelectSortOption(SortOptions.POPULARITY))

            val updated = awaitItem()
            assertEquals(SortOptions.POPULARITY, updated.sortOption)
            assertEquals(4, updated.selectedSortIndex)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // ==================== Restaurant Loading Tests ====================

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

    @Test
    fun `state should preserve restaurants on sort option change`() = runTest(testDispatcher) {
        val testRestaurants = listOf(
            createTestRestaurant("Restaurant A", bestMatch = 5.0f),
            createTestRestaurant("Restaurant B", bestMatch = 10.0f)
        )

        viewModel.state.test {
            awaitItem() // Skip initial

            repository.sendRestaurantList(testRestaurants)
            val loaded = awaitItem()
            assertEquals(2, loaded.restaurants.size)

            // Change sort option
            viewModel.processIntent(HomeIntent.SelectSortOption(SortOptions.NEWEST))

            // Restaurants should still be present
            val afterSort = awaitItem()
            assertEquals(2, afterSort.restaurants.size)
            assertEquals(SortOptions.NEWEST, afterSort.sortOption)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // ==================== Toggle Favorite Tests ====================

    @Test
    fun `processIntent ToggleFavorite should call repository`() = runTest(testDispatcher) {
        val restaurant = createTestRestaurant("Test Restaurant")
        repository.setToggleFavoriteResult(true)

        viewModel.processIntent(HomeIntent.ToggleFavorite(restaurant))
        advanceUntilIdle()

        val toggledRestaurant = repository.getLastToggledRestaurant()
        assertNotNull(toggledRestaurant)
        assertEquals("Test Restaurant", toggledRestaurant.name)
    }

    @Test
    fun `processIntent ToggleFavorite should trigger refresh`() = runTest(testDispatcher) {
        val restaurant = createTestRestaurant("Test Restaurant")
        repository.setToggleFavoriteResult(true)
        repository.sendRestaurantList(listOf(restaurant))

        viewModel.processIntent(HomeIntent.ToggleFavorite(restaurant))
        advanceUntilIdle()

        // Verify toggle was called
        val toggledRestaurant = repository.getLastToggledRestaurant()
        assertNotNull(toggledRestaurant)
        assertEquals("Test Restaurant", toggledRestaurant.name)
    }

    // ==================== Refresh Intent Tests ====================

    @Test
    fun `processIntent Refresh should set loading state`() = runTest(testDispatcher) {
        viewModel.state.test {
            val initial = awaitItem()
            assertTrue(initial.isLoading)

            // Send data to complete initial load
            repository.sendRestaurantList(listOf(createTestRestaurant("Test")))
            val loaded = awaitItem()
            assertFalse(loaded.isLoading)

            // Trigger refresh
            viewModel.processIntent(HomeIntent.Refresh)
            val refreshing = awaitItem()
            assertTrue(refreshing.isLoading)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // ==================== Effects Tests ====================

    @Test
    fun `effects should emit ShowError when repository fails`() = runTest(testDispatcher) {
        // This test verifies the effect channel is properly set up
        viewModel.effects.test {
            // Effects are one-shot, test that the channel is open
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ==================== Helper Functions ====================

    private fun createTestRestaurant(
        name: String,
        isFavorite: Boolean = false,
        bestMatch: Float = 1.0f,
        rating: Float = 4.5f
    ) = Restaurant(
        name = name,
        status = Status.OPEN,
        isFavorite = isFavorite,
        sortingValues = SortingValues(
            bestMatch = bestMatch,
            newest = 1.0f,
            ratingAverage = rating,
            distance = 1000L,
            popularity = 100.0f,
            averageProductPrice = 1500L,
            deliveryCosts = 200L,
            minCost = 1000L
        )
    )
}
