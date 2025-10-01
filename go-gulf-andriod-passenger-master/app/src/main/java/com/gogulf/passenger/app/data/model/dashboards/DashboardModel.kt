package com.gogulf.passenger.app.data.model.dashboards

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DashboardModel(
    val user: User,

    @SerializedName("completed_booking")
    val completedBooking: String,

    @SerializedName("next_booking")
    val nextBooking: String? = null,

    @SerializedName("is_card_added")
    val isCardAdded: String
) : Serializable


data class NextBooking(
    @SerializedName("booking_id")
    val bookingID: String,

    @SerializedName("pickup_date")
    val pickupDate: String,

    @SerializedName("pickup_time")
    val pickupTime: String
) : Serializable

data class User(
    val name: String,
    val email: String,
    val phone: String,

    @SerializedName("image_link")
    val imageLink: String
) : Serializable


data class ProfileModel (
    val name: String,
    val email: String,
    val phone: String,

    @SerializedName("image_link")
    val imageLink: String,

    val card: Card
):Serializable

data class Card (
    @SerializedName("card_holder_name")
    val cardHolderName: String,

    @SerializedName("card_masked")
    val cardMasked: String
):Serializable

