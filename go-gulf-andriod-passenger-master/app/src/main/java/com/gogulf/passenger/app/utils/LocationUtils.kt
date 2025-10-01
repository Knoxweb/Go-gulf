package com.gogulf.passenger.app.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.gogulf.passenger.app.App
import com.gogulf.passenger.app.App.Companion.preferenceHelper
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.objects.DebugMode
import com.gogulf.passenger.app.utils.objects.LocationPermissions
import com.gogulf.passenger.app.utils.objects.PrefConstant
import java.io.IOException
import java.util.Locale

object LocationUtils {
    var mFusedLocationClient: FusedLocationProviderClient? = null
    var myCurrentLocation = MutableLiveData<LatLng?>()

    @SuppressLint("MissingPermission")
    fun getLastLocation(context: AppCompatActivity) {
        if (LocationPermissions.checkPermissionForLocation(context)) {

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            if (isLocationEnabled(context)) {
                mFusedLocationClient!!.lastLocation.addOnCompleteListener { task: Task<Location?> ->
                    val location = task.result
                    if (location == null) {
                        requestNewLocationData(context)
                    } else {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        DebugMode.e(
                            "BaseActivity",
                            "LocationIsSaved Location recenter ->  $latitude , $longitude"
                        )
                        myCurrentLocation.postValue(LatLng(latitude, longitude))
                        preferenceHelper.setValue(PrefConstant.LATITUDE, latitude.toString())
                        preferenceHelper.setValue(PrefConstant.LONGITUDE, longitude.toString())

                    }
                }
            } else {
                gotoGPS(context)

            }
        } else {
            LocationPermissions.requestLocationPermission(context)
        }
    }

    fun gotoGPS(context: AppCompatActivity) {
        CustomAlertDialog(context)
            .setTitle("Notice")
            .setMessage("To continue, turn on device location, which uses Google's location service")
            .setPositiveText("OK") { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
            }
            .setNegativeText("No Thanks") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancellable(false)
            .show()
    }


    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
            val latitude = mLastLocation!!.latitude
            val longitude = mLastLocation.longitude
            myCurrentLocation.postValue(LatLng(latitude, longitude))
            preferenceHelper.setValue(PrefConstant.LATITUDE, latitude.toString())
            preferenceHelper.setValue(PrefConstant.LONGITUDE, longitude.toString())

        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData(context: AppCompatActivity) {
        // Initializing LocationRequest
        // object with appropriate methods
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }


    fun isLocationEnabled(context: Context): Boolean {
        val locationManager =
            context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun getFullAddress(lat: Double, long: Double, isNeededZipCode: Boolean = false): String {
        if (!Geocoder.isPresent()) {
            return "Geocoder not available"
        }
        val geoCoder = Geocoder(App.baseApplication, Locale.getDefault())
        return try {
            val addresses: MutableList<Address>? = geoCoder.getFromLocation(lat, long, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val addressLines = StringBuilder()
                if (isNeededZipCode) {
                    val postalCode = address.postalCode
                    if (!postalCode.isNullOrEmpty()) {
                        addressLines.append(postalCode).append(" - ")
                    }
                }
                for (i in 0..address.maxAddressLineIndex) {
                    addressLines.append(address.getAddressLine(i)).append("\n")
                }

                addressLines.toString().trim()
            } else {
                ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    fun vectorToBitmap(context: Context, @DrawableRes id: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(context.resources, id, null)
        val bitmap = Bitmap.createBitmap(
            vectorDrawable!!.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)

        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


    fun checkPermissionLocation(activity: AppCompatActivity) {
        if (!LocationPermissions.checkPermissionForLocation(activity)) {
            LocationPermissions.requestLocationPermission(activity)
        }

    }

}