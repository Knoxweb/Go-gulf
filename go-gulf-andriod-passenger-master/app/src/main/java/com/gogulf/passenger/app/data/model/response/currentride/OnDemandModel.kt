package com.gogulf.passenger.app.data.model.response.currentride

import com.google.gson.annotations.SerializedName
import com.gogulf.passenger.app.data.model.response.bookings.PolylinePoints


data class OnDemandModel(
    val quote: Quote
) : java.io.Serializable

data class Quote(
    @SerializedName("booking_id")
    val bookingID: String,

    @SerializedName("booking_type")
    val bookingType: String,

    @SerializedName("from_location")
    val fromLocation: String,

    @SerializedName("from_lat")
    val fromLat: Double,

    @SerializedName("from_lng")
    val fromLng: Double,

    @SerializedName("to_location")
    val toLocation: String,

    @SerializedName("to_lat")
    val toLat: Double,

    @SerializedName("to_lng")
    val toLng: Double,

    val distance: String,
    val duration: String,

    @SerializedName("est_fare")
    val estFare: Double,

    val passenger: String,
    val luggage: String,

    @SerializedName("infant_seat")
    val infantSeat: String,

    @SerializedName("child_seat")
    val childSeat: String,

    @SerializedName("booster_seat")
    val boosterSeat: String,

    @SerializedName("flight_number")
    val flightNumber: String,

    @SerializedName("driver_note")
    val driverNote: String? = null,

    @SerializedName("pickup_datetime")
    val pickupDatetime: String,

    @SerializedName("remaining_time_sec")
    val remainingTimeSEC: Long,

    @SerializedName("created_at_format")
    val createdAtFormat: String,

    @SerializedName("created_at_format_utc")
    val createdAtFormatUTC: String,

    val fleet: Fleet,

    @SerializedName("passenger_user")
    val passengerUser: PassengerUser,

    @SerializedName("polyline_points")
    val polylinePoints: PolylinePoints,

    @SerializedName("card_number")
    val cardNumber: String
) : java.io.Serializable


data class Fleet(
    val image_link: String? = null,
    val title: String? = null,
    val class_name: String? = null,
    val type_name: String? = null,
    val vehicle_make: String? = null,
    val vehicle_model: String? = null,
    val vehicle_color: String? = null,
    val vehicle_registration_number: String? = null,
    val passenger: Long? = null,
    val luggage: Long? = null,
    val infant_seat: Long? = null,
    val child_seat: Long? = null,
    val booster_seat: Long? = null
)
data class PassengerUser(
    val name: String,

    @SerializedName("image_link")
    val imageLink: String
) : java.io.Serializable
