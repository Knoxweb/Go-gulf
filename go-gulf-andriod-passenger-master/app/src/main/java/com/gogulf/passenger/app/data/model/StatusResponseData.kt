package com.gogulf.passenger.app.data.model

import com.google.gson.annotations.SerializedName

data class StatusResponseData(
    @SerializedName("current_dispatch")
    val currentDispatch: Int?,
    @SerializedName("firebase_reference")
    val firebaseReference: String?,
    @SerializedName("profile_status")
    val profileStatus: String?,

    )