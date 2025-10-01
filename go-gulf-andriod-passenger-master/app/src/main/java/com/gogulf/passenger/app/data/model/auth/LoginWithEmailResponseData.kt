package com.gogulf.passenger.app.data.model.auth

import com.google.gson.annotations.SerializedName

data class LoginWithEmailResponseData(
    @SerializedName("firebase_auth_token")
    val firebaseAuthToken: String?
)