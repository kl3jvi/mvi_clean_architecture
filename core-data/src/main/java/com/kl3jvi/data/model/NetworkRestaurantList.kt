package com.kl3jvi.data.model

import com.kl3jvi.model.Restaurant
import kotlinx.serialization.Serializable

@Serializable
data class JsonNetworkResponse(
    val restaurants: List<Restaurant>?
)
