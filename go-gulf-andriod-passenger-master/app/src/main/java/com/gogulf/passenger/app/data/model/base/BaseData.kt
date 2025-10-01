package com.gogulf.passenger.app.data.model.base

data class BaseData<T>(
    val title: String,
    val message: String,
    val mode: String,
    val data: T
)

data class BaseRide(
    val title: String? = "",
    val message: String? = "",
    val mode: String? = "",
    val driver_message: String? = "",
    val passenger_amount: String? = "",
    val driver_title: String? = "",
    val driver_amount: String? = "",
    val client_secret: String? = "",
    val invoice_id: String? = "",
    val booking_id: String? = "",
//    val data: CurrentRideModel? = null

) : java.io.Serializable

data class BaseArray<T>(
    val title: String,
    val message: String,
    val data: ArrayList<T>
)

data class Empty(
    val data: String
)