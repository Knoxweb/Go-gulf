package com.gogulf.passenger.app.data.model

import java.io.Serializable

data class Quote(
    val card_number: String? = null,
    val card_id: String? = null,
    val description: String? = null,
    val distance: String? = null,
    val drop_address: DropAddress? = null,
    val duration: String? = null,
    val id: Int? = null,
    val pickup_address: PickupAddress? = null,
    val pickup_date: String? = null,
    val pickup_date_timestamp: Int? = null,
    val pickup_time: String? = null,
    val pickup_type: String? = null,
    val route: Route? = null,
    val trip_type: String? = null
): Serializable