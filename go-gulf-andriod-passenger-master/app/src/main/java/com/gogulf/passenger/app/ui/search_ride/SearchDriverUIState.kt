package com.gogulf.passenger.app.ui.search_ride

import com.gogulf.passenger.app.data.model.ConfirmBookingSearchDriverResponseData
import com.gogulf.passenger.app.data.model.Error

data class SearchDriverUIState(

    val confirmBookingSearchDriverResponseData: ConfirmBookingSearchDriverResponseData? = null

)

data class LoadingUiState(

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: Error? = null,
    val isCancelSuccess: Boolean = false

)