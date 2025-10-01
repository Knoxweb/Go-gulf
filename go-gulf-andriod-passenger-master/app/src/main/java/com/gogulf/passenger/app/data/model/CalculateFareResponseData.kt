package com.gogulf.passenger.app.data.model

import java.io.Serializable

data class CalculateFareResponseData(
    val title: String?,
    val message: String?,
    val fleets: List<Fleet>? = null,
    val quote: Quote? = null
): Serializable

data class Fleet(
    val id: Int? = null,
    val full_name: String? = null,
    val class_name: String? = null,
    val type_name: String?,
    val image_path: String? = null,
    val small_image_path: String? = null,
    val passengers: Int? = null,
    val pet: Int? = null,
    val wheelchair: Int? = null,
    val fare: String? = null,
    val offer_fare: String? = null,
    var isSelected: Boolean = false
) : Serializable {
    fun getDiscountVisibility(): Boolean {
        var returnBoolean = false
        if (fare != null && offer_fare !=  null) {
            if (fare != offer_fare) {
                returnBoolean = true
            }
        }
        return returnBoolean
    }

}