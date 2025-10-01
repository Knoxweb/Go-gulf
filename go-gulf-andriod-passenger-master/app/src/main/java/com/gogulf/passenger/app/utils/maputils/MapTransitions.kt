package com.gogulf.passenger.app.utils.maputils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.location.Location
import android.os.Handler
import android.os.SystemClock
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.gogulf.passenger.app.utils.interfaces.FinalMapLatLng
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.Projection
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker


object MapTransitions {
    fun animateMarker(
        marker: Marker,
        toPosition: LatLng,
        hideMarker: Boolean,
        googleMap: GoogleMap?,
        finalMapLatLng: FinalMapLatLng,
    ) {
        val handler = Handler()
        val start: Long = SystemClock.uptimeMillis()
        val proj: Projection = googleMap?.projection!!
        val startPoint: Point = proj.toScreenLocation(marker.position)
        val startLatLng = proj.fromScreenLocation(startPoint)
        val duration: Long = 500
        val interpolator: Interpolator = LinearInterpolator()
        handler.post(object : Runnable {
            override fun run() {
                val elapsed: Long = SystemClock.uptimeMillis() - start
                val t: Float = interpolator.getInterpolation(elapsed.toFloat() / duration)
                val lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude
                val lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude
                val prevLoc = Location("service Provider")
                prevLoc.latitude = startLatLng.latitude
                prevLoc.longitude = startLatLng.longitude

                val newLoc = Location("service Provider")
                newLoc.latitude = toPosition.latitude
                newLoc.longitude = toPosition.longitude
                val bearing: Float = prevLoc.bearingTo(newLoc)
                marker.rotation = bearing
                marker.isFlat = true
                marker.position = LatLng(lat, lng)
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16)
                } else {
                    marker.isVisible = !hideMarker
                    finalMapLatLng.values(toPosition)

                }
            }
        })
    }

    fun bitmapDescriptorFromVector(
        context: Context,
        @DrawableRes vectorDrawableResourceId: Int
    ): BitmapDescriptor? {
        val background = ContextCompat.getDrawable(context, vectorDrawableResourceId)
        background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
        val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)
        vectorDrawable!!.setBounds(
            40,
            20,
            vectorDrawable.intrinsicWidth + 40,
            vectorDrawable.intrinsicHeight + 20
        )
        val bitmap = Bitmap.createBitmap(
            background.intrinsicWidth,
            background.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        background.draw(canvas)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun bitMapFromVector(context: Context, vectorResID: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResID)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


}