package com.kl3jvi.persistence

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.kl3jvi.persistence.dao.RestaurantDao
import com.kl3jvi.persistence.database.AppDatabase
import com.kl3jvi.persistence.models.RestaurantEntity
import com.kl3jvi.persistence.models.SortingValuesEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test



class RestaurantDaoTest {

    private lateinit var restaurantDao: RestaurantDao
    private lateinit var db: AppDatabase


    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()
        restaurantDao = db.restaurantDao()
    }

    private val entity = testRestaurantEntity(
        id = 1,
        name = "Testy Food :P",
        status = "open",
        sortingValuesEntity = testSortingValuesEntity(
            1.3F,
            2.3F,
            4.3F,
            1234,
            3.4F,
            123,
            15,
            1,
        )
    )

    /**
     * > We insert a restaurant into the database, then we get the restaurant's id by name, and then we assert that the id
     * is equal to the entity's id and that the id is not null
     */
    @Test
    fun entityInsertedSuccessfully() = runTest {
        restaurantDao.insertRestaurant(entity)
        val insertedRestaurantId = restaurantDao.getRestaurantIdByName(entity.name)
        assertThat(insertedRestaurantId).isEqualTo(entity.id)
        assertThat(insertedRestaurantId).isNotNull()
    }

    /**
     * `duplicateEntitiesShouldBeIgnored` tests that when a duplicate restaurant is inserted, it is
     * ignored
     */
    @Test
    fun duplicateEntitiesShouldBeIgnored() = runTest {
        restaurantDao.insertRestaurant(entity)

        val duplicateRestaurantId = restaurantDao.insertRestaurant(
            RestaurantEntity(
                id = 1,
                name = "Testy Food :P",
                status = "open",
                sortingValues = testSortingValuesEntity(
                    1.3F,
                    2.3F,
                    4.3F,
                    1234,
                    3.4F,
                    123,
                    15,
                    1,
                )
            )
        )!!

        assertThat(restaurantDao.getRestaurantById(entity.id)).isNotNull()
        assertThat(restaurantDao.getRestaurantById(duplicateRestaurantId)).isNull()

    }

    /* Testing that when a restaurant is inserted, it can be deleted. */
    @Test
    fun insertAndDeleteRestaurant() = runTest {
        restaurantDao.insertRestaurant(entity)
        restaurantDao.deleteRestaurant(entity.id)

        assertThat(restaurantDao.getRestaurantIdByName(entity.name)).isNull()
    }

    @Test
    fun getAllRestaurantsReturnSuccess() = runTest {
        restaurantDao.insertRestaurant(entity)

        val restaurants = restaurantDao.getFavoriteRestaurants()

        restaurants.test {
            assertThat(awaitItem()?.first()).isNotNull()
        }
    }

    /* Closing the database after the test is done. */
    @After
    fun closeDb() {
        db.close()
    }
}

private fun testRestaurantEntity(
    id: Long = 0L,
    name: String,
    status: String,
    sortingValuesEntity: SortingValuesEntity
) = RestaurantEntity(
    id = id,
    name = name,
    status = status,
    sortingValues = sortingValuesEntity
)

private fun testSortingValuesEntity(
    bestMatch: Float,
    newest: Float,
    ratingAverage: Float,
    distance: Long,
    popularity: Float,
    averageProductPrice: Long,
    deliveryCosts: Long,
    minCost: Long,
) = SortingValuesEntity(
    bestMatch,
    newest,
    ratingAverage,
    distance,
    popularity,
    averageProductPrice,
    deliveryCosts,
    minCost
)