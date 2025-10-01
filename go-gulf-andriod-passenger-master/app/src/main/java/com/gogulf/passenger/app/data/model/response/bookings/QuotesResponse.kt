package com.gogulf.passenger.app.data.model.response.bookings

import com.google.gson.annotations.SerializedName


data class QuotesResponse(
    @SerializedName("quote_id")
    val quoteID: String,

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

    @SerializedName("card_number")
    val cardNumber: String,
    @SerializedName("polyline_points")
    val polylinePoints: PolylinePoints,

    val fleets: ArrayList<Fleet>
) : java.io.Serializable

data class Fleet(
    val title: String,

    @SerializedName("class_name")
    val className: String,

    @SerializedName("type_name")
    val typeName: String,

    @SerializedName("image_link")
    val imageLink: String,

    @SerializedName("image_1_link")
    val image1_Link: String,

    @SerializedName("icon_link")
    val iconLink: String,

    val passenger: String,
    val luggage: String,

    val pet: String,

    @SerializedName("wheel_chair")
    val wheelChair: String,

    @SerializedName("infant_seat")
    val infantSeat: String,

    @SerializedName("child_seat")
    val childSeat: String,

    @SerializedName("booster_seat")
    val boosterSeat: String,

    val fare: Double,

    @SerializedName("offer_fare")
    val offerFare: Double,

    @SerializedName("vehicle_make")
    val vehicleMake: String,

    @SerializedName("vehicle_model")
    val vehicleModel: String,

    @SerializedName("vehicle_registration_number")
    val vehicleRegistrationNumber: String,


    @SerializedName("fleet_id")
    val fleetID: String,

    var isSelected: Boolean = false

) : java.io.Serializable

data class PolylinePoints (
    val bounds: Bounds?=null,
    val points: String?=null
) : java.io.Serializable


data class Bounds (
    val northeast: Northeast?=null,
    val southwest: Northeast?=null
) : java.io.Serializable


data class Northeast (
    val lat: Double?=null,
    val lng: Double?=null
) : java.io.Serializable