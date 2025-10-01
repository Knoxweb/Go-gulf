package com.gogulf.passenger.app.ui.currentridenew

import com.google.android.gms.maps.GoogleMap
import com.gogulf.passenger.app.data.model.CurrentBookingResponseData
import com.gogulf.passenger.app.data.model.Error

data class DriverUiState(
    val isLoading: Boolean = false,
    val bookingResponse: CurrentBookingResponseData? = null,
    val error: Error? = null,
    var mMap: GoogleMap? = null,
    val isCameraBoundsUpdated: Boolean = false


)
