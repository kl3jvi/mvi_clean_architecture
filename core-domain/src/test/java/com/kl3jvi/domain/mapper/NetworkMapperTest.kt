package com.kl3jvi.domain.mapper
//
// import com.kl3jvi.domain.model.Status
// import org.junit.Test
// import kotlin.test.assertEquals
//
// class NetworkMapperTest {
//
//    @Test
//    fun network_restaurant_can_be_mapped_to_domain_model() {
//        val networkModel = NetworkRestaurant(
//            name = "Fake Restaurant",
//            sortingValues = NetworkSortingValues(
//                1.3F,
//                2.3F,
//                4.3F,
//                1234,
//                3.4F,
//                123,
//                15,
//                1,
//            ),
//            status = NetworkStatus.OPEN
//        )
//        val domainModel = networkModel.toDomainModel()
//
//        assertEquals("Fake Restaurant", domainModel.name)
//        assertEquals(1.3F, domainModel.sortingValues?.bestMatch)
//        assertEquals(Status.OPEN, domainModel.status)
//    }
//
//    @Test
//    fun network_status_can_be_mapped_to_status() {
//        val networkStatusOpen = NetworkStatus.OPEN
//        val networkStatusOrderAhead = NetworkStatus.ORDER_AHEAD
//        val networkStatusClosed = NetworkStatus.CLOSED
//
//        assertEquals(networkStatusOpen.toDomainModel(), Status.OPEN)
//        assertEquals(networkStatusOrderAhead.toDomainModel(), Status.ORDER_AHEAD)
//        assertEquals(networkStatusClosed.toDomainModel(), Status.CLOSED)
//    }
//
//    fun network_sorting_values_can_be_mapped_to_domain_model() {
//        val networkModel = NetworkSortingValues(
//            1.3F,
//            2.3F,
//            4.3F,
//            1234,
//            3.4F,
//            123,
//            15,
//            1,
//        )
//        val domainModel = networkModel.toDomainModel()
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
// }
