package com.gogulf.passenger.app.data.model.response.bookings

import com.google.gson.annotations.SerializedName

data class BookingModel(
    @SerializedName("booking_id")
    val bookingID: String,

    @SerializedName("from_location")
    val fromLocation: String,

    @SerializedName("to_location")
    val toLocation: String,

    @SerializedName("pickup_datetime")
    val pickupDatetime: String,

    @SerializedName("total_fare")
    val totalFare: Double,

    @SerializedName("to_cancel")
    val toCancel: String
)


data class BookingModelFirebase(
    val booking_id: String? = "",

    val from_location: String? = "",

    val pickup_datetime: String? = "",

    val to_location: String? = "",

    val total_fare: Double? = 0.00
)