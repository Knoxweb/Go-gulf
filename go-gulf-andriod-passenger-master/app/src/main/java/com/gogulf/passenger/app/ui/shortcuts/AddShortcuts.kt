package com.gogulf.passenger.app.ui.shortcuts

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class AddShortcuts : Serializable {
    @SerializedName("address_id")
    val addressID: String? = null


    var name: String? = null
    var address: String? = null
    var floor: String? = null

    @SerializedName("street_number")
    var streetNumber: String? = null

    var locality: String? = null
    var sublocality: String? = null

    @SerializedName("administrative_area_level_3")
    var administrativeAreaLevel3: String? = null

    @SerializedName("administrative_area_level_2")
    var administrativeAreaLevel2: String? = null

    @SerializedName("administrative_area_level_1")
    var administrativeAreaLevel1: String? = null

    var country: String = ""

    @SerializedName("postal_code")
    var postalCode: String? = null

    var lat: String? = null
    var lng: String? = null

    @SerializedName("created_at")
    val createdAt: String = ""

    @SerializedName("updated_at")
    val updatedAt: String = ""


}
