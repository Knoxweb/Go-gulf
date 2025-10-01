package com.gogulf.passenger.app.data.model

import java.io.Serializable

data class ConfirmBookingSearchDriverResponseData(
    val id: Int? = null,
    val reference: String? = null,
    val pickup: Location? = null,
    val drop: Location? = null,
    val route: Route? = null,
    val distance: String? = null,
    val duration: String? = null,
    val pickup_date_time: String? = null,
    val fare: String? = null,
    val offer_fare: String? = null,
    val status: String? = null,
    val expire_at: Long? = null,
    val title: String? = null,
    val message: String? = null
): Serializable

data class Location(
    val name: String? = null,
    val lat: Double? = null,
    val lng: Double? = null
): Serializable

data class LatLng(
    val lat: Double? = null,
    val lng: Double? = null
): Serializable

