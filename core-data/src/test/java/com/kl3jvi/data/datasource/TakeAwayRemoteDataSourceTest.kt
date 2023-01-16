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
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class TakeAwayRemoteDataSourceTest {

    private lateinit var subject: TakeAwayRemoteDataSource
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        subject =
            TakeAwayRemoteDataSourceImpl(
                ioDispatcher = testDispatcher,
                networkJson = Json { ignoreUnknownKeys = true }
            )
    }

    @Test
    fun testDeserializationOfRestaurants() = runTest(testDispatcher) {
        assertEquals(
            fakeDataSourceRestaurant(),
            subject.getRestaurants().first().first()
        )
    }

    @Test
    fun testInvalidJsonThrowsException() = runTest(testDispatcher) {
        val invalidJson = "invalid json"
        assertFailsWith<SerializationException> {
            subject.getRestaurants()
        }
    }

    @Test
    fun testNullRestaurantsEmitsEmptyList() = runTest(testDispatcher) {
        val json = """
    {
        "restaurants": null
    }
    """
        val subject = TakeAwayRemoteDataSourceImpl(
            ioDispatcher = testDispatcher,
            networkJson = Json { ignoreUnknownKeys = true }
        )
        RestaurantJson.restaurantData = json
        assertEquals(
            emptyList(),
            subject.getRestaurants().first()
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
        val subject = TakeAwayRemoteDataSourceImpl(
            ioDispatcher = testDispatcher,
            networkJson = Json { ignoreUnknownKeys = true }
        )
        assertEquals(
            2,
            subject.getRestaurants().first().size
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
        val subject = TakeAwayRemoteDataSourceImpl(
            ioDispatcher = testDispatcher,
            networkJson = Json { ignoreUnknownKeys = true })
        assertEquals(
            Status.OPEN,
            subject.getRestaurants().first().first().status
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
        val subject = TakeAwayRemoteDataSourceImpl(
            ioDispatcher = testDispatcher,
            networkJson = Json { ignoreUnknownKeys = true })
        assertEquals(
            Status.CLOSED,
            subject.getRestaurants().first().first().status
        )
    }

}

fun fakeDataSourceRestaurant(): Restaurant {
    return Restaurant(
        name = "Tanoshii Sushi",
        status = Status.OPEN,
        sortingValues = SortingValues(
            bestMatch = 0.0F,
            newest = 96.0F,
            ratingAverage = 4.5F,
            distance = 1190L,
            popularity = 17.0F,
            averageProductPrice = 1536L,
            deliveryCosts = 200L,
            minCost = 1000L
        )
    )
}
