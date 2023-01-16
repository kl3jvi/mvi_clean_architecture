package com.kl3jvi.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class SortingValues(
    val bestMatch: Float,
    val newest: Float,
    val ratingAverage: Float,
    val distance: Long,
    val popularity: Float,
    val averageProductPrice: Long,
    val deliveryCosts: Long,
    val minCost: Long
) : Parcelable {
    fun getDistanceInKM(): String = (distance / 1000).toDouble().toString()
}
