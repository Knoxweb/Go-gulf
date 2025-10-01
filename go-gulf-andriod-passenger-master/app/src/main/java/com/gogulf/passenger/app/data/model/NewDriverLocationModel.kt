package com.gogulf.passenger.app.data.model

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
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
    val dIdentity: String? = "",
    val dLocation: DriverLocationModel? = null
):Serializable

data class DriverModelIdentityMarker(
    val dIdentity: String? = "",
    val marker: Marker? = null,
    val dLocation: DriverLocationModel? = null
):Serializable

data class MutableDriverMarker(
    val pos: LatLng? = null,
    val dMarker: DriverModelIdentityMarker? = null
):Serializable
