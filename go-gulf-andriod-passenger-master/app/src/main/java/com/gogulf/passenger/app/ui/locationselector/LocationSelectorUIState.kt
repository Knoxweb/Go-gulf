package com.gogulf.passenger.app.ui.locationselector

import java.io.Serializable

data class LocationSelectorUIState (
    val selectedAddress: String = "",
    val selectedLat: Double = 0.0,
    val selectedLog: Double = 0.0,
    val unable: Boolean = false
): Serializable