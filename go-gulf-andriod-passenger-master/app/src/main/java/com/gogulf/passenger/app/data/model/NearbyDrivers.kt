package com.gogulf.passenger.app.data.model

data class NearbyDrivers(
    val id: Int,
    val image: String,
    val lat: Double,
    val lng: Double,
    val name: String,
    val status: String
)