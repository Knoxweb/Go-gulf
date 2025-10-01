package com.gogulf.passenger.app.data.model.request

import com.google.gson.annotations.SerializedName


class CreateQuote : java.io.Serializable {
    @SerializedName("booking_type")
    var bookingType: String = "" //scheduled or on_demand

    @SerializedName("from_location")
    var fromLocation: String = ""

    @SerializedName("from_lat")
    var fromLat: String = ""

    @SerializedName("from_lng")
    var fromLng: String = ""

/*    @SerializedName("via_location")
    var viaLocation: String? = null

    @SerializedName("via_lat")
    var viaLat: String? = null*/

    @SerializedName("to_location")
    var toLocation: String = ""

    @SerializedName("to_lat")
    var toLat: String = ""

    @SerializedName("to_lng")
    var toLng: String = ""

    var discount: Int = 0
/*
    @SerializedName("pickup_date")
    var pickupDate: String = "" //2023-02-04

    @SerializedName("pickup_time")
    var pickupTime: String = "" //24hour*/


    @SerializedName("pickup_datetime")
    var pickupDateTime: String = "" //2023-02-03 08:15:00 +0000

    @SerializedName("return_pickup_datetime")
    var returnDateTime: String = "" //2023-02-03 08:15:00 +0000
    override fun toString(): String {
        return "CreateQuote(bookingType='$bookingType', fromLocation='$fromLocation', fromLat='$fromLat', fromLng='$fromLng', toLocation='$toLocation', toLat='$toLat', toLng='$toLng', pickupDateTime='$pickupDateTime', returnDateTime='$returnDateTime')"
    }

/*
    @SerializedName("hourly_duration")
    var hourlyDuration: String? = null //3*/





}