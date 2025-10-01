package com.gogulf.passenger.app.data.model.auth

import com.google.gson.annotations.SerializedName

data class PassengerLoginResponseData(
    @SerializedName("auth_token")
    val authToken: String?,
    @SerializedName("ref")
    val firebaseReference: String?,
    @SerializedName("profile_status")
    val profileStatus: String?
)