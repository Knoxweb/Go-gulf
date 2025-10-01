package com.gogulf.passenger.app.data.model

import java.io.Serializable


data class CurrentBookingResponseData(
    var current_status: String? = null,
    var description: String? = null,
    var distance: String? = null,
    var drop: Pickup? = null,
    var duration: String? = null,
    var fare: String? = null,
    var offer_fare: String? = null,
    var fare_breakdown: List<Any>? = null,
    var fleet: FleetNew? = null,
    var flight_number: String? = null,
    var id: Int? = null,
    var passenger: Passenger? = null,
    var pickup: Pickup? = null,
    var pickup_date_time: String? = null,
    var reference: String? = null,
    var route: Route? = null,
    var status: String? = null,
    var type: String? = null,
    var wheelchair_count: Long? = null,
    var passenger_count: Long? = null,
    var pet_count: Long? = null,
    var driver: Passenger? = null,
    var card_masked: String? = null,
    var status_title: String? = null,
//    var invoice: Invoice? = null

) : Serializable

data class Passenger(
    var mobile: String? = null,
    var name: String? = null,
    var profile_picture_url: String? = null,
    var rating: String? = null,
) : Serializable


data class FleetNew(
    var class_name: String? = null,
    var image_url: String? = null,
    var name: String? = null,
    var type_name: String? = null,
    var color: String? = null,
    var registration_number: String? = null
) : Serializable

data class Pickup(
    var lat: Double? = null, var lng: Double? = null, var name: String? = null
) : Serializable

data class RouteNew(
    var bounds: BoundsNew? = null, var overview_polyline: Polyline? = null
) : Serializable

data class BoundsNew(
    var northeast: LocationNew? = null, var southwest: LocationNew? = null
) : Serializable

data class LocationNew(
    var lat: Double? = null, var lng: Double? = null
) : Serializable

data class Polyline(
    var points: String? = null
) : Serializable


data class PobOtpResponseData(
    var otp: Int? = null,
    var title: String? = null,
    var message: String? = null
)