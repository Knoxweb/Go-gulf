package com.gogulf.passenger.app.utils.maputils


import com.google.android.gms.maps.model.LatLng
import com.gogulf.passenger.app.BuildConfig


object LocationUrl {

    fun getUrl(
        origin: LatLng,
        dest: LatLng,
        directionMode: String
    ): String? {
        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude
        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude
        // Mode
        val mode = "mode=$directionMode"
        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$mode"
        // Output format
        val output = "json"
        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + BuildConfig.WEBAPIKEY
    }
}