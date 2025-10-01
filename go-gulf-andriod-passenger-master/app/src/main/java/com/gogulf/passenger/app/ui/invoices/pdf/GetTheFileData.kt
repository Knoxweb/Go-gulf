package com.gogulf.passenger.app.ui.invoices.pdf

import java.io.Serializable

data class GetTheFileData(
    val invoices: List<Int>
): Serializable
