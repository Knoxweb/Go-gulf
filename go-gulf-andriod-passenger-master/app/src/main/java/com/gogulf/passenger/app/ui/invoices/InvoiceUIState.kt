package com.gogulf.passenger.app.ui.invoices

import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.data.model.InvoiceResponseData
import com.gogulf.passenger.app.data.model.response.mycards.CardModels


data class InvoiceUIState(
    val isLoading: Boolean = false,
    val error: Error? = null,
    val invoiceResponseData: List<InvoiceResponseData>? = null,
    val dateFrom: String = "",
    val dateTo: String = "",
    val cardOfUsers: ArrayList<CardModels>? = ArrayList(),
    )