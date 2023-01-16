package com.kl3jvi.data.datasource

import com.kl3jvi.data.api_data.RestaurantJson
import com.kl3jvi.domain.datasource.TakeAwayRemoteDataSource
import com.kl3jvi.model.Restaurant
import com.kl3jvi.model.SortingValues
import com.kl3jvi.model.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class TakeAwayRemoteDataSourceTest {

    private lateinit var subject: TakeAwayRemoteDataSource
    private val testDispatcher = StandardTestDispatcher()
    private val jsonProperty = Json { ignoreUnknownKeys = false }

    @Before
    fun setup() {
        subject = TakeAwayRemoteDataSourceImpl(
            ioDispatcher = testDispatcher, networkJson = jsonProperty
        )
    }

    @Test
    fun testDeserializationOfRestaurants() = runTest(testDispatcher) {
        assertEquals(
            fakeDataSourceRestaurant(), subject.getRestaurants().first().first()
        )
    }


    @Test
    fun testInvalidJsonThrowsException() = runTest(testDispatcher) {
        val invalidJson = """{"invalid": "json"}"""
        RestaurantJson.restaurantData = invalidJson
        assertFailsWith<SerializationException> {
            jsonProperty.decodeFromString(invalidJson)
        }
    }

    @Test
    fun testNullRestaurantsEmitsEmptyList() = runTest(testDispatcher) {
        val json = """
    {
        "restaurants": null
    }
    """
        val subject = TakeAwayRemoteDataSourceImpl(ioDispatcher = testDispatcher,
            networkJson = Json { ignoreUnknownKeys = true })
        RestaurantJson.restaurantData = json
        assertEquals(
            emptyList(), subject.getRestaurants().first()
        )
    }

    @Test
    fun testMultipleRestaurants() = runTest(testDispatcher) {
        val json = """
    {
        "restaurants": [
            {
                "name": "Restaurant 1",
                "status": "open",
                "sortingValues": {
                    "bestMatch": 0.0,
                    "newest": 96.0,
                    "ratingAverage": 4.5,
                    "distance": 1190,
                    "popularity": 17.0,
                    "averageProductPrice": 1536,
                    "deliveryCosts": 200,
                    "minCost": 1000
                }
            },
            {
                "name": "Restaurant 2",
                "status": "open",
                "sortingValues": {
                    "bestMatch": 0.0,
                    "newest": 96.0,
                    "ratingAverage": 4.5,
                    "distance": 1190,
                    "popularity": 17.0,
                    "averageProductPrice": 1536,
                    "deliveryCosts": 200,
                    "minCost": 1000
                }
            }
        ]
    }
    """
        RestaurantJson.restaurantData = json
        assertEquals(
            2, subject.getRestaurants().first().size
        )
    }

    @Test
    fun testStatusOpen() = runTest(testDispatcher) {
        val json = """
    {
        "restaurants": [
            {
                "name": "Restaurant 1",
                "status": "open",
                "sortingValues": {
                    "bestMatch": 0.0,
                    "newest": 96.0,
                    "ratingAverage": 4.5,
                    "distance": 1190,
                    "popularity": 17.0,
                    "averageProductPrice": 1536,
                    "deliveryCosts": 200,
                    "minCost": 1000
                }
            }
        ]
    }
    """
        RestaurantJson.restaurantData = json
        assertEquals(
            Status.OPEN, subject.getRestaurants().first().first().status
        )
    }

    @Test
    fun testStatusClosed() = runTest(testDispatcher) {
        val json = """
    {
        "restaurants": [
            {
                "name": "Restaurant 1",
                "status": "closed",
                "sortingValues": {
                    "bestMatch": 0.0,
                    "newest": 96.0,
                    "ratingAverage": 4.5,
                    "distance": 1190,
                    "popularity": 17.0,
                    "averageProductPrice": 1536,
                    "deliveryCosts": 200,
                    "minCost": 1000
                }
            }
        ]
    }
    """
        RestaurantJson.restaurantData = json

        val expectedRestaurant = fakeDataSourceRestaurant(
            name = "Restaurant 1",
            status = Status.CLOSED,
            bestMatch = 0.0F,
            newest = 96.0F,
            ratingAverage = 4.5F,
            distance = 1190L,
            popularity = 17.0F,
            averageProductPrice = 1536L,
            deliveryCosts = 200L,
            minCost = 1000L
        )
        assertEquals(
            expectedRestaurant.status, subject.getRestaurants().first().first().status
        )
    }


    @Test
    fun testSortingValues() = runTest(testDispatcher) {
        val json = """
    {
        "restaurants": [
            {
                "name": "Restaurant 1",
                "status": "open",
                "sortingValues": {
                    "bestMatch": 0.0,
                    "newest": 96.0,
                    "ratingAverage": 4.5,
                    "distance": 1190,
                    "popularity": 17.0,
                    "averageProductPrice": 1536,
                    "deliveryCosts": 200,
                    "minCost": 1000
                }
            }
        ]
    }
    """
        RestaurantJson.restaurantData = json
        val expectedRestaurant = fakeDataSourceRestaurant(
            name = "Restaurant 1",
            status = Status.OPEN,
            bestMatch = 0.0F,
            newest = 96.0F,
            ratingAverage = 4.5F,
            distance = 1190L,
            popularity = 17.0F,
            averageProductPrice = 1536L,
            deliveryCosts = 200L,
            minCost = 1000L
        )

        assertEquals(
            expectedRestaurant.sortingValues, subject.getRestaurants().first().first().sortingValues
        )
    }


}

private fun fakeDataSourceRestaurant(
    name: String = "Tanoshii Sushi",
    status: Status = Status.OPEN,
    bestMatch: Float = 0.0F,
    newest: Float = 96.0F,
    ratingAverage: Float = 4.5F,
    distance: Long = 1190L,
    popularity: Float = 17.0F,
    averageProductPrice: Long = 1536L,
    deliveryCosts: Long = 200L,
    minCost: Long = 1000L
): Restaurant {
    return Restaurant(
        name = name, status = status, sortingValues = SortingValues(
            bestMatch = bestMatch,
            newest = newest,
            ratingAverage = ratingAverage,
            distance = distance,
            popularity = popularity,
            averageProductPrice = averageProductPrice,
            deliveryCosts = deliveryCosts,
            minCost = minCost
        )
    )
}

