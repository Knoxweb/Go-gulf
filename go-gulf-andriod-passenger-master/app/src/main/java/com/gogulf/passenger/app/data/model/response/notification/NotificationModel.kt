package com.gogulf.passenger.app.data.model.response.notification

import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class NoticeModel(
    @set:PropertyName("booking_id")
    @get:PropertyName("booking_id")
    @SerializedName("booking_id")
    var bookingID: String? = "",

    @set:PropertyName("invoice_id")
    @get:PropertyName("invoice_id")
    @SerializedName("invoice_id")
    var invoiceID: String? = "",

    @set:PropertyName("notification_at")
    @get:PropertyName("notification_at")
    @SerializedName("notification_at")
    var notificationAt: String? = "",

    @set:PropertyName("id")
    @get:PropertyName("id")
    @SerializedName("id")
    var notificationId: String? = "",

    var seen: Int? = 0,

    val is_seen: Boolean? = null,
    val message: String? = null,
    val target: String? = null,
    val timestamp: Long? = null,
    val title: String? = null,
    val type: String? = null
) : Serializable