package com.gogulf.passenger.app.data.model.others

import com.google.gson.annotations.SerializedName
import java.io.Serializable


/*data class Fleet(
    @SerializedName("fleet_id")
    val fleetID: String,

    val name: String,
    val total: String,
    val image: String,

    @SerializedName("front_image")
    val frontImage: String,

    @SerializedName("flight_number_require")
    val flightNumberRequire: Boolean,
    val distance: String,
    @SerializedName("distance_text")
    val distanceText: String,
    val duration: String,
    @SerializedName("duration_text")
    val durationText: String,

) : Serializable*/

data class FleetType(
    @SerializedName("passenger_count")
    val passengerCount: String,

    @SerializedName("luggage_count")
    val luggageCount: String,

    @SerializedName("suitcase_count")
    val suitcaseCount: String,

    val info: String,

    @SerializedName("default_image")
    val defaultImage: String? = null,

    val name: String
) : Serializable

data class SeatCharge(
    val id: String,
    val name: String,
    val img: String,
    val rate: String,
    val info: String,
    val max: String
) : Serializable

