package com.gogulf.passenger.app.data.model.response.currentride

import com.gogulf.passenger.app.data.model.response.bookings.PolylinePoints


data class CurrentRideApiModel(
    val driver_job_accepted_location: LocationApi?=null,

    val driver_current_location: LocationApi?=null,

    val dropoff_location: LocationApi?=null,

    val pickup_location: LocationApi?=null,

    val driver: DriverApi?=null,
    val booking: BookingApi?=null,
    val card: CardApi?=null,
    val duration: String?=null,
    val polyline_points: PolylinePoints?=null,
    val distance: String?=null,
)


data class BookingApi(
    val booking_id: String?=null,

    val from_location: String?=null,

    val to_location: String?=null,

    val booking_type: String?=null,

    val distance: String?=null,
    val duration: String?=null,
    val fleet: FleetApi?=null,
    val passenger: Long?=null,
    val luggage: Long?=null,
    val pet: Long?=null,
    val ride_otp: String?=null,

    val infant_seat: Long?=null,
    val wheel_chair: Long?=null,

    val child_seat: Long?=null,

    val booster_seat: Long?=null,

    val flight_number: String?=null,

    val driver_note: String?=null,

    val fare: Double?=null,


    )


data class FleetApi(
    val image_link: String?=null,
    val title: String?=null,
    val class_name: String?=null,
    val type_name: String?=null,

    val vehicle_make: String?=null,

    val vehicle_model: String?=null,

    val vehicle_color: String?=null,

    val vehicle_registration_number: String?=null,

    val passenger: Long?=null,
    val luggage: Long?=null,

    val infant_seat: Long?=null,

    val child_seat: Long?=null,

    val booster_seat: Long?=null,
)


data class CardApi(
    val image_link: String?=null,
    val card_mask: String?=null,
)


data class DriverApi(
    val name: String?=null,
    val phone: String?=null,
    val image_link: String?=null,
    val avg_rating: Long?=null,
)


data class LocationApi(
    val lat: Double?=null,
    val lng: Double?=null,
)
