package com.kl3jvi.data.datasource


import com.kl3jvi.model.Restaurant
import com.kl3jvi.model.SortingValues
import com.kl3jvi.model.Status
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


class TakeAwayRemoteDataSourceTest {

    private lateinit var subject: TakeAwayRemoteDataSource
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        subject =
            TakeAwayRemoteDataSourceImpl(ioDispatcher = testDispatcher, networkJson = Json { ignoreUnknownKeys = true })
    }


    @Test
    fun testDeserializationOfRestaurants() = runTest(testDispatcher) {
        assertEquals(
            fakeDataSourceRestaurant(), subject.getRestaurants().first().first()
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