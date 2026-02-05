package com.kl3jvi.feature_home.viewModel

import app.cash.turbine.test
import com.kl3jvi.feature_home.common.MainDispatcherRule
import com.kl3jvi.feature_home.details.DetailsState
import com.kl3jvi.feature_home.details.DetailsViewModel
import com.kl3jvi.feature_home.testdoubles.TestRestaurantRepository
import com.kl3jvi.model.Restaurant
import com.kl3jvi.model.SortingValues
import com.kl3jvi.model.Status
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DetailsViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var repository: TestRestaurantRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: DetailsViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        repository = TestRestaurantRepository()
        savedStateHandle = SavedStateHandle()
        viewModel = DetailsViewModel(repository, savedStateHandle)
    }

    // ==================== Initial State Tests ====================

    @Test
    fun `initial state should have null restaurant`() = runTest(testDispatcher) {
        assertEquals(DetailsState(), viewModel.state.value)
        assertNull(viewModel.state.value.restaurant)
    }

    @Test
    fun `initial state isFavorite should be false`() = runTest(testDispatcher) {
        assertFalse(viewModel.state.value.isFavorite)
    }

    // ==================== Initialize Tests ====================

    @Test
    fun `initialize should set restaurant in state`() = runTest(testDispatcher) {
        val restaurant = createTestRestaurant("Test Restaurant")

        viewModel.state.test {
            val initial = awaitItem()
            assertNull(initial.restaurant)

            viewModel.initialize(restaurant)

            val updated = awaitItem()
            assertNotNull(updated.restaurant)
            assertEquals("Test Restaurant", updated.restaurant?.name)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `initialize should preserve favorite status from passed restaurant`() = runTest(testDispatcher) {
        val favoriteRestaurant = createTestRestaurant("Favorite", isFavorite = true)

        viewModel.initialize(favoriteRestaurant)
        
        assertTrue(viewModel.state.value.isFavorite)
    }

    @Test
    fun `initialize with non-favorite should have isFavorite false`() = runTest(testDispatcher) {
        val restaurant = createTestRestaurant("Regular", isFavorite = false)

        viewModel.initialize(restaurant)
        
        assertFalse(viewModel.state.value.isFavorite)
    }

    // ==================== Toggle Favorite Tests ====================

    @Test
    fun `toggleFavorite should update state to new favorite value`() = runTest(testDispatcher) {
        val restaurant = createTestRestaurant("Test", isFavorite = false)
        repository.setToggleFavoriteResult(true) // Will become favorite
        repository.sendRestaurantList(listOf(restaurant))
        
        viewModel.initialize(restaurant)

        viewModel.state.test {
            val initial = awaitItem()
            assertFalse(initial.isFavorite)

            viewModel.toggleFavorite()
            advanceUntilIdle()

            val updated = awaitItem()
            assertTrue(updated.isFavorite)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleFavorite should call repository with correct restaurant`() = runTest(testDispatcher) {
        val restaurant = createTestRestaurant("Test Restaurant")
        viewModel.initialize(restaurant)

        viewModel.toggleFavorite()

        val toggled = repository.getLastToggledRestaurant()
        assertNotNull(toggled)
        assertEquals("Test Restaurant", toggled.name)
    }

    @Test
    fun `toggleFavorite without initialization should not crash`() = runTest(testDispatcher) {
        // Should not throw exception
        viewModel.toggleFavorite()
        
        // Repository should not have been called
        assertNull(repository.getLastToggledRestaurant())
    }

    // ==================== Repository Observation Tests ====================

    @Test
    fun `state should update when repository emits new data`() = runTest(testDispatcher) {
        val restaurant = createTestRestaurant("Test", isFavorite = false)
        viewModel.initialize(restaurant)

        viewModel.state.test {
            awaitItem() // Initial

            // Repository emits updated restaurant with favorite = true
            val updatedRestaurant = restaurant.copy(isFavorite = true)
            repository.sendRestaurantList(listOf(updatedRestaurant))

            val updated = awaitItem()
            assertTrue(updated.isFavorite)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `state should find restaurant by name from repository`() = runTest(testDispatcher) {
        val restaurant = createTestRestaurant("Target Restaurant", isFavorite = false)
        val otherRestaurant = createTestRestaurant("Other Restaurant", isFavorite = true)
        
        viewModel.initialize(restaurant)

        viewModel.state.test {
            awaitItem() // Initial

            // Repository emits list with multiple restaurants
            repository.sendRestaurantList(listOf(otherRestaurant, restaurant.copy(isFavorite = true)))

            val updated = awaitItem()
            assertEquals("Target Restaurant", updated.restaurant?.name)
            assertTrue(updated.isFavorite)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // ==================== Helper Functions ====================

    private fun createTestRestaurant(
        name: String,
        isFavorite: Boolean = false
    ) = Restaurant(
        name = name,
        status = Status.OPEN,
        isFavorite = isFavorite,
        sortingValues = SortingValues(
            bestMatch = 1.0f,
            newest = 1.0f,
            ratingAverage = 4.5f,
            distance = 1000L,
            popularity = 100.0f,
            averageProductPrice = 1500L,
            deliveryCosts = 200L,
            minCost = 1000L
        )
    )
}
