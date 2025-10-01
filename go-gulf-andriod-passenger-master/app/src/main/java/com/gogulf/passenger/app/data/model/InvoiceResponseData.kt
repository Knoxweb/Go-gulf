package com.gogulf.passenger.app.data.model


data class InvoiceResponseData(
    var amount: String? = null,
    val generated_at: String? = null,
    val id: Int? = null,
    val reference: String? = null,
    val status: String? = null,
    val status_title: String? = null,
    val client_secret: String? = null,
    val timestamp: Int? = null
)