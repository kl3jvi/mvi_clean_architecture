package com.kl3jvi.domain.use_cases

import app.cash.turbine.test
import com.kl3jvi.model.Restaurant
import com.kl3jvi.model.SortingValues
import com.kl3jvi.model.Status
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetSortedRestaurantsUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()

    // ==================== Sorting by Best Match ====================

    @Test
    fun `sort by BEST_MATCH orders restaurants by bestMatch ascending`() = runTest(testDispatcher) {
        val restaurants = listOf(
            createRestaurant("Low", bestMatch = 1.0f),
            createRestaurant("High", bestMatch = 10.0f),
            createRestaurant("Medium", bestMatch = 5.0f)
        )
        val repository = FakeRepository(restaurants)
        val useCase = GetSortedRestaurantsUseCase(repository, testDispatcher)

        useCase(SortOption.BEST_MATCH).test {
            val result = awaitItem()
            // Lower bestMatch = better match (ascending order)
            assertEquals("Low", result[0].name)
            assertEquals("Medium", result[1].name)
            assertEquals("High", result[2].name)
            awaitComplete()
        }
    }

    // ==================== Sorting by Newest ====================

    @Test
    fun `sort by NEWEST orders restaurants by newest descending`() = runTest(testDispatcher) {
        val restaurants = listOf(
            createRestaurant("Old", newest = 1.0f),
            createRestaurant("New", newest = 100.0f),
            createRestaurant("Mid", newest = 50.0f)
        )
        val repository = FakeRepository(restaurants)
        val useCase = GetSortedRestaurantsUseCase(repository, testDispatcher)

        useCase(SortOption.NEWEST).test {
            val result = awaitItem()
            assertEquals("New", result[0].name)
            assertEquals("Mid", result[1].name)
            assertEquals("Old", result[2].name)
            awaitComplete()
        }
    }

    // ==================== Sorting by Rating ====================

    @Test
    fun `sort by RATING_AVERAGE orders restaurants by rating descending`() = runTest(testDispatcher) {
        val restaurants = listOf(
            createRestaurant("Bad", rating = 2.0f),
            createRestaurant("Great", rating = 5.0f),
            createRestaurant("Good", rating = 4.0f)
        )
        val repository = FakeRepository(restaurants)
        val useCase = GetSortedRestaurantsUseCase(repository, testDispatcher)

        useCase(SortOption.RATING_AVERAGE).test {
            val result = awaitItem()
            assertEquals("Great", result[0].name)
            assertEquals("Good", result[1].name)
            assertEquals("Bad", result[2].name)
            awaitComplete()
        }
    }

    // ==================== Sorting by Distance ====================

    @Test
    fun `sort by DISTANCE orders restaurants by distance ascending`() = runTest(testDispatcher) {
        val restaurants = listOf(
            createRestaurant("Far", distance = 5000L),
            createRestaurant("Near", distance = 100L),
            createRestaurant("Medium", distance = 2000L)
        )
        val repository = FakeRepository(restaurants)
        val useCase = GetSortedRestaurantsUseCase(repository, testDispatcher)

        useCase(SortOption.DISTANCE).test {
            val result = awaitItem()
            assertEquals("Near", result[0].name)
            assertEquals("Medium", result[1].name)
            assertEquals("Far", result[2].name)
            awaitComplete()
        }
    }

    // ==================== Sorting by Popularity ====================

    @Test
    fun `sort by POPULARITY orders restaurants by popularity descending`() = runTest(testDispatcher) {
        val restaurants = listOf(
            createRestaurant("Unpopular", popularity = 10.0f),
            createRestaurant("Popular", popularity = 1000.0f),
            createRestaurant("Average", popularity = 500.0f)
        )
        val repository = FakeRepository(restaurants)
        val useCase = GetSortedRestaurantsUseCase(repository, testDispatcher)

        useCase(SortOption.POPULARITY).test {
            val result = awaitItem()
            assertEquals("Popular", result[0].name)
            assertEquals("Average", result[1].name)
            assertEquals("Unpopular", result[2].name)
            awaitComplete()
        }
    }

    // ==================== Sorting by Price ====================

    @Test
    fun `sort by AVERAGE_PRODUCT_PRICE orders restaurants by price ascending`() = runTest(testDispatcher) {
        val restaurants = listOf(
            createRestaurant("Expensive", price = 3000L),
            createRestaurant("Cheap", price = 500L),
            createRestaurant("Normal", price = 1500L)
        )
        val repository = FakeRepository(restaurants)
        val useCase = GetSortedRestaurantsUseCase(repository, testDispatcher)

        useCase(SortOption.AVERAGE_PRODUCT_PRICE).test {
            val result = awaitItem()
            assertEquals("Cheap", result[0].name)
            assertEquals("Normal", result[1].name)
            assertEquals("Expensive", result[2].name)
            awaitComplete()
        }
    }

    // ==================== Sorting by Delivery Cost ====================

    @Test
    fun `sort by DELIVERY_COST orders restaurants by delivery cost ascending`() = runTest(testDispatcher) {
        val restaurants = listOf(
            createRestaurant("High Delivery", deliveryCosts = 500L),
            createRestaurant("Free Delivery", deliveryCosts = 0L),
            createRestaurant("Normal Delivery", deliveryCosts = 200L)
        )
        val repository = FakeRepository(restaurants)
        val useCase = GetSortedRestaurantsUseCase(repository, testDispatcher)

        useCase(SortOption.DELIVERY_COST).test {
            val result = awaitItem()
            assertEquals("Free Delivery", result[0].name)
            assertEquals("Normal Delivery", result[1].name)
            assertEquals("High Delivery", result[2].name)
            awaitComplete()
        }
    }

    // ==================== Sorting by Min Cost ====================

    @Test
    fun `sort by MIN_COST orders restaurants by min cost ascending`() = runTest(testDispatcher) {
        val restaurants = listOf(
            createRestaurant("High Min", minCost = 2000L),
            createRestaurant("Low Min", minCost = 500L),
            createRestaurant("Medium Min", minCost = 1000L)
        )
        val repository = FakeRepository(restaurants)
        val useCase = GetSortedRestaurantsUseCase(repository, testDispatcher)

        useCase(SortOption.MIN_COST).test {
            val result = awaitItem()
            assertEquals("Low Min", result[0].name)
            assertEquals("Medium Min", result[1].name)
            assertEquals("High Min", result[2].name)
            awaitComplete()
        }
    }

    // ==================== Status Priority Tests ====================

    @Test
    fun `restaurants are sorted by status first then by sort option`() = runTest(testDispatcher) {
        val restaurants = listOf(
            createRestaurant("Closed High", status = Status.CLOSED, bestMatch = 100.0f),
            createRestaurant("Open Low", status = Status.OPEN, bestMatch = 1.0f),
            createRestaurant("OrderAhead High", status = Status.ORDER_AHEAD, bestMatch = 50.0f)
        )
        val repository = FakeRepository(restaurants)
        val useCase = GetSortedRestaurantsUseCase(repository, testDispatcher)

        useCase(SortOption.BEST_MATCH).test {
            val result = awaitItem()
            // OPEN first, then ORDER_AHEAD, then CLOSED
            assertEquals(Status.OPEN, result[0].status)
            assertEquals(Status.ORDER_AHEAD, result[1].status)
            assertEquals(Status.CLOSED, result[2].status)
            awaitComplete()
        }
    }

    // ==================== Favorite Priority Tests ====================

    @Test
    fun `favorites are sorted before non-favorites within same status`() = runTest(testDispatcher) {
        val restaurants = listOf(
            createRestaurant("Not Favorite", isFavorite = false, bestMatch = 100.0f),
            createRestaurant("Favorite Low", isFavorite = true, bestMatch = 1.0f),
            createRestaurant("Favorite High", isFavorite = true, bestMatch = 50.0f)
        )
        val repository = FakeRepository(restaurants)
        val useCase = GetSortedRestaurantsUseCase(repository, testDispatcher)

        useCase(SortOption.BEST_MATCH).test {
            val result = awaitItem()
            // Favorites first
            assertTrue(result[0].isFavorite)
            assertTrue(result[1].isFavorite)
            // Non-favorite last
            assertEquals("Not Favorite", result[2].name)
            awaitComplete()
        }
    }

    // ==================== Empty List Test ====================

    @Test
    fun `empty restaurant list returns empty`() = runTest(testDispatcher) {
        val repository = FakeRepository(emptyList())
        val useCase = GetSortedRestaurantsUseCase(repository, testDispatcher)

        useCase(SortOption.BEST_MATCH).test {
            val result = awaitItem()
            assertTrue(result.isEmpty())
            awaitComplete()
        }
    }

    // ==================== Helper Classes ====================

    private class FakeRepository(
        private val restaurants: List<Restaurant>
    ) : com.kl3jvi.domain.repository.RestaurantRepository {
        override fun getRestaurants() = flowOf(restaurants)
        override suspend fun toggleRestaurantFavorite(restaurant: Restaurant) = !restaurant.isFavorite
    }

    private fun createRestaurant(
        name: String,
        status: Status = Status.OPEN,
        isFavorite: Boolean = false,
        bestMatch: Float = 1.0f,
        newest: Float = 1.0f,
        rating: Float = 4.0f,
        distance: Long = 1000L,
        popularity: Float = 100.0f,
        price: Long = 1500L,
        deliveryCosts: Long = 200L,
        minCost: Long = 1000L
    ) = Restaurant(
        name = name,
        status = status,
        isFavorite = isFavorite,
        sortingValues = SortingValues(
            bestMatch = bestMatch,
            newest = newest,
            ratingAverage = rating,
            distance = distance,
            popularity = popularity,
            averageProductPrice = price,
            deliveryCosts = deliveryCosts,
            minCost = minCost
        )
    )
}
