package com.gogulf.passenger.app.ui.support.supports

import com.google.gson.annotations.SerializedName

data class SupportPostModel(
    val subject: String,
    @SerializedName("booking_id")
    val bookingID: String,
    val comment: String,
    val type: String
)