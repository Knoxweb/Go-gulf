package com.gogulf.passenger.app.data.model.response.mycards

import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName


data class CardModels(
    @SerializedName("card_masked") val cardMasked: String = "",

    val id: Int? = null,
    val brand: String? = null,

    @SerializedName("card_holder_name") val cardHolderName: String = "",

    @SerializedName("card_expiry_month") val cardExpiryMonth: String = "",

    @SerializedName("card_verification_number") val cardVerificationNumber: String = "***",

    @SerializedName("card_expiry_year") val cardExpiryYear: String = "",

    @SerializedName("is_active") val isActive: String = "",

    @SerializedName("image_link") val imageLink: String = "",

    val status: String = "",

    val address: String? = null,
    val brand_image: String? = null,
    val card_masked: String? = null,

    val email: String? = null,
    val exp_month: Int? = null,
    val exp_year: Int? = null,
    @SerializedName("is_active")
    @set:PropertyName("is_active")
    @get:PropertyName("is_active")
    var is_active: Boolean? = null,
    val name: String? = null
) : java.io.Serializable
