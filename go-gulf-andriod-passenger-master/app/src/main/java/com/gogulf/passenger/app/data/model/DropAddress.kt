package com.gogulf.passenger.app.data.model

import java.io.Serializable

data class DropAddress(
    val lat: Double? = null,
    val lng: Double? = null,
    val name: String? = null
): Serializable