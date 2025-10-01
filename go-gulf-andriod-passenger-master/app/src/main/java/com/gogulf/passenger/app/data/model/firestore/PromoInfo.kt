package com.gogulf.passenger.app.data.model.firestore

import com.google.firebase.firestore.PropertyName

data class PromoInfo(

    var discount: String = "",

    @set:PropertyName("discount_message")
    @get:PropertyName("discount_message")
    var discountMessage: String = "",

    @set:PropertyName("discount_status")
    @get:PropertyName("discount_status")
    var discountStatus: String = "",

    @set:PropertyName("discount_title")
    @get:PropertyName("discount_title")
    var discountTitle: String = "",

    @set:PropertyName("promotional_message")
    @get:PropertyName("promotional_message")
    var promotionalMessage: String = "",

    @set:PropertyName("promotional_status")
    @get:PropertyName("promotional_status")
    var promotionalStatus: String = "",

    @set:PropertyName("promotional_title")
    @get:PropertyName("promotional_title")
    var promotionalTitle: String = ""
)
