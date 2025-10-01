package com.gogulf.passenger.app.utils.objects

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


object LocationPermissions {
    private val TAG = "LocationPermissions"


    const val LOCATION_PERMISSION_REQUEST_CODE = 100
    const val BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 101
    fun checkPermissionForLocation(applicationContext: Context): Boolean {
        val result = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val result1 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        DebugMode.e(TAG ,"$result $result1 == is value granted ? -> ${PackageManager.PERMISSION_GRANTED} " , "Location Permission Check")
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionBackground(applicationContext: Context): Boolean {
        val result = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
        DebugMode.e(TAG ,"$result == is value granted ? -> ${PackageManager.PERMISSION_GRANTED} " , "Background Permission Check")

        return result == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermission(applicationContext: Activity) {
        ActivityCompat.requestPermissions(
            applicationContext,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    fun requestBackgroundPermission(applicationContext: Activity) {
        ActivityCompat.requestPermissions(
            applicationContext, arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
            BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    fun isGPSAvailable(context: Context): Boolean {
        val mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun checkLocation(context: Activity) {
        if (!checkPermissionForLocation(context)) {
            requestLocationPermission(context)
        }
    }

}