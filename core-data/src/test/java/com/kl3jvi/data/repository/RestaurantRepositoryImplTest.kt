package com.kl3jvi.data.repository

import com.kl3jvi.data.datasource.TakeAwayRemoteDataSource
import com.kl3jvi.data.testdoubles.TestRestaurantDao
import com.kl3jvi.data.testdoubles.TestTakeAwayRemoteDataSource
import com.kl3jvi.model.Restaurant
import com.kl3jvi.model.SortingValues
import com.kl3jvi.model.Status

import com.kl3jvi.persistence.dao.RestaurantDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class RestaurantRepositoryImplTest {

    private lateinit var subject: com.kl3jvi.domain.repository.RestaurantRepository

    private lateinit var restaurantDao: RestaurantDao

    private lateinit var network: TakeAwayRemoteDataSource

    private val testDispatcher = StandardTestDispatcher()


    @Before
    fun setup() {
        restaurantDao = TestRestaurantDao()
        network = TestTakeAwayRemoteDataSource(
            listOf(
                Restaurant(
                    name = "Fake Restaurant",
                    sortingValues = SortingValues(
                        1.3F,
                        2.3F,
                        4.3F,
                        1234,
                        3.4F,
                        123,
                        15,
                        1,
                    ), status = Status.OPEN
                )
            )
        )
        subject = RestaurantRepositoryImpl(
            network = network,
            local = restaurantDao,
            ioDispatcher = testDispatcher
        )
    }

    @Test
    fun get_restaurants_should_return_combined() = runTest(testDispatcher) {

        val response = listOf(
            Restaurant(
                "Fast Food",
                Status.CLOSED,
                isFavorite = true,
                sortingValues = null
            ), Restaurant(
                "Fake Restaurant",
                Status.OPEN,
                isFavorite = false,
                sortingValues = SortingValues(
                    1.3F,
                    2.3F,
                    4.3F,
                    1234,
                    3.4F,
                    123,
                    15,
                    1,
                ),
            )
        )

        val actual = subject.getRestaurants().first()

        assertTrue { actual.all { it.name in response.map { it.name } } }
        assertTrue { actual.all { it.status in response.map { it.status } } }
        assertTrue { actual.all { it.isFavorite in response.map { it.isFavorite } } }
        assertTrue { actual.first().isFavorite }
        assertEquals(
            actual.size, response.size
        )

    }

    @Test
    fun toggleRestaurantFavoriteRemove() = runTest(testDispatcher) {
        val data = Restaurant("Fast Food", Status.OPEN, false, null)
        val actual = subject.toggleRestaurantFavorite(data)
        assertFalse(actual)
    }

    @Test
    fun toggleRestaurantFavoriteAdd() = runTest(testDispatcher) {
        val data = Restaurant("Fast Food1", Status.OPEN, false, null)
        val actual = subject.toggleRestaurantFavorite(data)
        assertTrue(actual)
    }


}