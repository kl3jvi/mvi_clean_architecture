package com.kl3jvi.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.math.roundToInt


@Parcelize
@Serializable
data class Restaurant(
    val name: String?,
    val status: Status,
    /* A marker annotation that indicates that a property or a field is not part of the serialized form of an object. */
    @Transient
    val isFavorite: Boolean = false,
    val sortingValues: SortingValues?
) : Parcelable {
    fun getRatingValue() = (sortingValues?.ratingAverage?.times(2))?.roundToInt() ?: 0
}