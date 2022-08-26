package com.kl3jvi.persistence.mapper

import com.kl3jvi.model.SortingValues
import com.kl3jvi.persistence.models.SortingValuesEntity

fun SortingValuesEntity.toDomainModel() = SortingValues(
    bestMatch = bestMatch,
    newest = newest,
    ratingAverage = ratingAverage,
    distance = distance,
    popularity = popularity,
    averageProductPrice = averageProductPrice,
    deliveryCosts = deliveryCosts,
    minCost = minCost
)

fun SortingValues.toEntityModel() = SortingValuesEntity(
    bestMatch = bestMatch,
    newest = newest,
    ratingAverage = ratingAverage,
    distance = distance,
    popularity = popularity,
    averageProductPrice = averageProductPrice,
    deliveryCosts = deliveryCosts,
    minCost = minCost
)