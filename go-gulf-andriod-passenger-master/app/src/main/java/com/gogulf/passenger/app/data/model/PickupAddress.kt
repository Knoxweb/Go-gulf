package com.gogulf.passenger.app.data.model

import java.io.Serializable

data class PickupAddress(
    val lat: Double? = null,
    val lng: Double? = null,
    val name: String? = null
): Serializable