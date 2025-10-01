package com.gogulf.passenger.app.data.model

import java.io.Serializable

class CalculateFareRequestBody : Serializable{
    var drop_address: String? = null
    var drop_address_lat: String? = null
    var drop_address_lng: String? = null
    var exp_discount: Int? = null
    var pickup_address: String? = null
    var pickup_address_lat: String? = null
    var pickup_address_lng: String? = null
    var pickup_date: String? = null
    var pickup_time: String? = null
    var trip_type: String? = null
    var passenger_count: Int? = null
}