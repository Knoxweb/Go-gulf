package com.gogulf.passenger.app.ui.getaride

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.gogulf.passenger.app.data.model.NearbyDriverResponseData
import java.io.Serializable

data class DriverLocationModel(
    var lat: Any? = 0.00,
    var lng: Any? = 0.00,
    val name: String? = "",
    val email: String? = "",
    val phone: String? = "",
    val reg_no: String? = ""
):Serializable

data class DriverModelIdentity(
    val dIdentity: Int? = null,
    val dLocation: NearbyDriverResponseData? = null
):Serializable

data class DriverModelIdentityMarker(
    val dIdentity: Int? = null,
    val marker: Marker? = null,
    val dLocation: NearbyDriverResponseData? = null
):Serializable

data class MutableDriverMarker(
    val pos: LatLng? = null,
    val dMarker: DriverModelIdentityMarker? = null
):Serializable
