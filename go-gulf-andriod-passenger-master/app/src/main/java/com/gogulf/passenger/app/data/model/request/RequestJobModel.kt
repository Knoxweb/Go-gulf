package com.gogulf.passenger.app.data.model.request

import com.google.gson.annotations.SerializedName

class RequestJobModel : java.io.Serializable {

    @SerializedName("quote_id")
    var quoteId: String = ""

    @SerializedName("fleet_id")
    var fleetId: String = ""

    @SerializedName("driver_note")
    var driverNote: String = ""

    @SerializedName("extras")
    var extras: ExtraModel? = null

    @SerializedName("lead_passenger_name")
    var leadPassengerName: String = ""

    @SerializedName("lead_passenger_phone")
    var leadPassengerPhone: String = ""

}



class ConfirmBookingModel: java.io.Serializable {
    var flight_number: String = ""
    var passenger_count: String = ""
    var pet_count: String = ""
    var wheelchair_count: String = ""
    var passenger_card_id: String = ""
    var special_insctruction: String = ""
    var description: String = ""
//    var second: String = "120"
}

class ExtraModel : java.io.Serializable {
    var passenger: Int = 0
    var luggage: Int = 0
    var pet: Int = 0

    @SerializedName("wheel_chair")
    var wheelchair: Int = 0

    @SerializedName("infant_seat")
    var infantSeat: Int = 0

    @SerializedName("child_seat")
    var childSeat: Int = 0

    @SerializedName("booster_seat")
    var boosterSeat: Int = 0

}