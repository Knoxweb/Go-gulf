package com.gogulf.passenger.app.ui.trips

import com.google.android.gms.maps.GoogleMap
import com.gogulf.passenger.app.data.model.LocationModel

data class BookingLocationSelectionUIState (

    val selectedPickupLocation: LocationModel? = null,
    val selectedPickupOnScreen: String = "Select pick up location",
    val selectedDropOffLocationOnScreen: String = "Select drop off location",
    val selectedDropOffLocation: LocationModel? = null,
    val selectedVia1Location: LocationModel? = null,
    val selectedVia2Location: LocationModel? = null,
    val selectedVia3Location: LocationModel? = null,
    var updateLiveCurrentLocation: Boolean = true,

    val mMap: GoogleMap? = null


)