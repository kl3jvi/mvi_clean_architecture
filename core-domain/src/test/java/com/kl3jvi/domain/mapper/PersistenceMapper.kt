package com.kl3jvi.domain.mapper


import com.kl3jvi.persistence.models.RestaurantEntity
import com.kl3jvi.persistence.models.SortingValuesEntity
import org.junit.Test
import kotlin.test.assertEquals

class PersistenceMapper {

//    @Test
//    fun restaurant_entity_can_be_mapped_to_domain_model() {
//        val entity = RestaurantEntity(
//            id = 1,
//            name = "Fast Food",
//            status = "OPEN",
//            sortingValues = null
//        )
//
//        val domainModel = com.kl3jvi.model.mapper.toDomainModel()
//
//        assertEquals("Fast Food", domainModel.name)
//        assertEquals(null, domainModel.sortingValues)
//        assertEquals(Status.OPEN, domainModel.status)
//    }
//
//    @Test
//    fun restaurant_entity_can_be_constructed_from_domain_model() {
//        val restaurant = Restaurant(
//            name = "Fast Food",
//            isFavorite = false,
//            status = Status.CLOSED,
//            sortingValues = null
//        )
//
//        val entity = restaurant.toEntityModel()
//
//        assertEquals("Fast Food", entity.name)
//        assertEquals(null, entity.sortingValues)
//        assertEquals(Status.CLOSED.name, entity.status)
//    }
//
//    @Test
//    fun sorting_values_entity_can_be_mapped_to_domain() {
//        val sortingValuesEntity = SortingValuesEntity(
//            1.3F,
//            2.3F,
//            4.3F,
//            1234,
//            3.4F,
//            123,
//            15,
//            1,
//        )
//
//        val domainModel = com.kl3jvi.model.mapper.toDomainModel()
//
//        assertEquals(1.3F, domainModel.bestMatch)
//        assertEquals(2.3F, domainModel.newest)
//        assertEquals(4.3F, domainModel.ratingAverage)
//        assertEquals(1234, domainModel.distance)
//        assertEquals(3.4F, domainModel.popularity)
//        assertEquals(123, domainModel.averageProductPrice)
//        assertEquals(15, domainModel.deliveryCosts)
//        assertEquals(1, domainModel.minCost)
//    }
//
//    @Test
//    fun sorting_values_entity_can_be_constructed_from_domain_model() {
//        val sortingValues = SortingValues(
//            1.3F,
//            2.3F,
//            4.3F,
//            1234,
//            3.4F,
//            123,
//            15,
//            1,
//        )
//        val entity = sortingValues.toEntityModel()
//
//        assertEquals(1.3F, entity.bestMatch)
//        assertEquals(2.3F, entity.newest)
//        assertEquals(4.3F, entity.ratingAverage)
//        assertEquals(1234, entity.distance)
//        assertEquals(3.4F, entity.popularity)
//        assertEquals(123, entity.averageProductPrice)
//        assertEquals(15, entity.deliveryCosts)
//        assertEquals(1, entity.minCost)
//
//
//    }

}