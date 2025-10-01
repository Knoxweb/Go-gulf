package com.gogulf.passenger.app.data.model

data class NearbyDriverResponseData(
    val id: Int? = null,
    val image: String? = null,
    var lat: Double? = null,
    var lng: Double? = null,
    val name: String? = null,
    val status: String? = null
)