package com.kl3jvi.persistence.mapper

import com.kl3jvi.model.Restaurant
import com.kl3jvi.model.Status
import com.kl3jvi.persistence.models.RestaurantEntity

fun RestaurantEntity.toDomainModel() = Restaurant(
    name = name,
    isFavorite = true,
    status = Status.getTypeFromString(status) ?: Status.CLOSED,
    sortingValues = sortingValues?.toDomainModel()
)

fun Restaurant.toEntityModel() = RestaurantEntity(
    name = name.orEmpty(),
    status = Status.getStringFromType(status.name),
    sortingValues = sortingValues?.toEntityModel()
)
