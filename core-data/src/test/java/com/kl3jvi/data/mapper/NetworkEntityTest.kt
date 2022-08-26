package com.kl3jvi.data.mapper

import com.kl3jvi.model.Restaurant
import com.kl3jvi.model.SortingValues
import com.kl3jvi.model.Status
import com.kl3jvi.persistence.mapper.toEntityModel
import org.junit.Test
import kotlin.test.assertEquals

class NetworkEntityTest {

    @Test
    fun network_restaurant_can_be_mapped_to_restaurant_entity() {
        val networkModel = Restaurant(
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
            ),
            status = Status.OPEN
        )
        val entity = networkModel.toEntityModel()

        assertEquals("Fake Restaurant", entity.name)
        assertEquals(1.3F, entity.sortingValues?.bestMatch)
        assertEquals(Status.OPEN.name, entity.status)
    }


    @Test
    fun network_sorting_values_can_be_mapped_to_sorting_values_entity() {
        val networkModel = SortingValues(
            1.3F,
            2.3F,
            4.3F,
            1234,
            3.4F,
            123,
            15,
            1,
        )
        val entity = networkModel.toEntityModel()

        assertEquals(1.3F, entity.bestMatch)
        assertEquals(2.3F, entity.newest)
        assertEquals(4.3F, entity.ratingAverage)
        assertEquals(1234, entity.distance)
        assertEquals(3.4F, entity.popularity)
        assertEquals(123, entity.averageProductPrice)
        assertEquals(15, entity.deliveryCosts)
        assertEquals(1, entity.minCost)
    }
}