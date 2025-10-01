package com.gogulf.passenger.app.data.model

import java.io.Serializable

data class LocationModel(
    val address: String,
    val latitude: Double,
    val longitude: Double
): Serializable

