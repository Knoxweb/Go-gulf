package com.gogulf.passenger.app.data.model.auths

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Authentications(
    @SerializedName("is_provisional")
    val isProvisional: String,

    val identity: String,
    val name: String,
    val email: String,
    val phone: String,

    @SerializedName("image_link")
    val imageLink: String,

    @SerializedName("avg_rating")
    val avgRating: Long,

    @SerializedName("api_token")
    val apiToken: String
) : Serializable


data class RegisterModel(
    val identity: String,
    val name: String,
    val email: String,
    val phone: String,

    @SerializedName("image_link")
    val imageLink: String,

    @SerializedName("avg_rating")
    val avgRating: String,

    @SerializedName("api_token")
    val apiToken: String
) : Serializable

data class Card(
    @SerializedName("image_link")
    val imageLink: String,

    @SerializedName("card_mask")
    val cardMask: String,

    @SerializedName("card_message")
    val cardMessage: String
) : Serializable