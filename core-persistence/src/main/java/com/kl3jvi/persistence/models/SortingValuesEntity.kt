package com.kl3jvi.persistence.models

import androidx.room.ColumnInfo

data class SortingValuesEntity(
    @ColumnInfo(name = "best_match")
    val bestMatch: Float,
    @ColumnInfo(name = "newest")
    val newest: Float,
    @ColumnInfo(name = "rating_average")
    val ratingAverage: Float,
    @ColumnInfo(name = "distance")
    val distance: Long,
    @ColumnInfo(name = "popularity")
    val popularity: Float,
    @ColumnInfo(name = "average_product_price")
    val averageProductPrice: Long,
    @ColumnInfo(name = "delivery_costs")
    val deliveryCosts: Long,
    @ColumnInfo(name = "min_cost")
    val minCost: Long
)
