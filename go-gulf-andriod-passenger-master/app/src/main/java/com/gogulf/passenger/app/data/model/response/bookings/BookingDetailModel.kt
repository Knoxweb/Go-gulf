package com.gogulf.passenger.app.data.model.response.bookings

import com.google.gson.annotations.SerializedName

data class BookingDetailModel(
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

    val status: String,
    val label: String,

    @SerializedName("pickup_datetime_std")
    val pickupDatetimeStd: String,

    @SerializedName("pickup_datetime")
    val pickupDatetime: String,

    val fleet: Fleet,
    val name: String,

    @SerializedName("phone_cc")
    val phoneCc: String,

    @SerializedName("phone_no")
    val phoneNo: String,

    val email: String,

    @SerializedName("lead_passenger_name")
    val leadPassengerName: String,

    @SerializedName("lead_passenger_phone")
    val leadPassengerPhone: String,

    @SerializedName("flight_number")
    val flightNumber: String,

    @SerializedName("driver_note")
    val driverNote: String,

    val passenger: Long,
    val luggage: Long,
    val pet: Long,

    @SerializedName("wheel_chair")
    val wheelChair: Long,
    @SerializedName("infant_seat")
    val infantSeat: Long,

    @SerializedName("child_seat")
    val childSeat: Long,

    @SerializedName("booster_seat")
    val boosterSeat: Long,

    val distance: String,
    val duration: String,

    @SerializedName("to_cancel")
    val toCancel: Long,

    val invoice: Invoice,

    @SerializedName("passenger_user")
    val passengerUser: ErUser,

    @SerializedName("driver_user")
    val driverUser: ErUser,

    val card: Card
):java.io.Serializable


data class Card(
    @SerializedName("image_link")
    val imageLink: String,

    @SerializedName("card_mask")
    val cardMask: String

):java.io.Serializable


data class ErUser(
    val name: String,
    val email: String,
    val phone: String,

    @SerializedName("image_link")
    val imageLink: String,

    @SerializedName("avg_rating")
    val avgRating: Int

):java.io.Serializable


data class Invoice(
    @SerializedName("invoice_id")
    val invoiceID: String,

    @SerializedName("created_at")
    val createdAt: String,

    val fare: Double,

    @SerializedName("charge_details")
    val chargeDetails: ArrayList<ChargeDetail>,

    @SerializedName("sub_total")
    val subTotal: Double,

    @SerializedName("discount_title")
    val discountTitle: String,

    @SerializedName("discount_percentage")
    val discountPercentage: String,

    @SerializedName("discount_amount")
    val discountAmount: Long,

    @SerializedName("surcharge_title")
    val surchargeTitle: String,

    @SerializedName("surcharge_percentage")
    val surchargePercentage: String,

    @SerializedName("surcharge_amount")
    val surchargeAmount: Double,

    @SerializedName("final_amount")
    val finalAmount: Double,

    @SerializedName("is_paid")
    val isPaid: Long

):java.io.Serializable

data class ChargeDetail(
    val key: String,
    val value: Double

):java.io.Serializable
