package com.gogulf.passenger.app.utils.objects

import android.graphics.Color
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil


object PolylineUtils {
    fun getString(lines: Any?): String =
        if (lines is String) {
            lines
        } else if (lines is HashMap<*, *>) {
            if (lines != null) {
                // do something with the Points object
                lines["points"] as String
            } else {
                // handle the case where the string cannot be converted to a Points object
                ""
            }
        } else {
            ""
        }

    fun getPolyLines(polyLine: Any?): PolylineOptions {
        val decodedPoints =
            PolyUtil.decode(getString(polyLine))

// Create a PolylineOptions object and add the decoded points
        val polylineOptions = PolylineOptions()
        polylineOptions.addAll(decodedPoints)

// Customize the polyline appearance, if desired
        polylineOptions.color(Color.rgb(77, 204, 134))
        polylineOptions.width(5f)
        return polylineOptions
    }


}