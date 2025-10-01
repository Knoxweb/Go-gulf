package com.gogulf.passenger.app.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.bookingapp.connectsmartdrive.utils.livemodels.LiveLocationModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.gogulf.passenger.app.App
import com.gogulf.passenger.app.ui.locationselector.LocationSelectorUIState
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.IOException
import java.io.Serializable
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


object MapsUtils {

    val secret = "AIzaSyCuiw7shEGM2iXXCAXNBcdGdLJ2045terk"
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


    fun getDirectionURL(origin: LatLng, dest: LatLng): String {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" + "&destination=${dest.latitude},${dest.longitude}" + "&sensor=false" + "&mode=driving" + "&key=$secret"
    }


    fun getDirectionURL(origin: LatLng, via1: LatLng, dest: LatLng): String {
//        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" + "&destination=${dest.latitude},${dest.longitude}" + "&waypoints=${via1.latitude},${via1.longitude}" + "&sensor=false" + "&mode=driving" + "&key=$secret";
        val waypoints = "${via1.latitude},${via1.longitude}"
        val encodedWaypoints = URLEncoder.encode(waypoints, StandardCharsets.UTF_8.toString())

        return "https://maps.googleapis.com/maps/api/directions/json?" + "origin=${origin.latitude},${origin.longitude}&" + "destination=${dest.latitude},${dest.longitude}&" + "waypoints=$encodedWaypoints&" + "sensor=false&" + "mode=driving&" + "key=$secret"
    }

//    fun getDirectionURL(origin: LatLng, via1: LatLng, via2: LatLng, dest: LatLng): String {
//        return "https://maps.googleapis.com/maps/api/directions/json?" + "origin=${origin.latitude},${origin.longitude}" + "&destination=${dest.latitude},${dest.longitude}" + "&waypoints=${via1.latitude},${via1.longitude}|${via2.latitude},${via2.longitude}" + "&sensor=false" + "&mode=driving" + "&key=$secret"
//    }

    fun getDirectionURL(
        origin: LatLng, via1: LatLng, via2: LatLng, dest: LatLng
    ): String {
        return "https://maps.googleapis.com/maps/api/directions/json?" + "origin=${origin.latitude},${origin.longitude}&" + "destination=${dest.latitude},${dest.longitude}&" + "waypoints=${via1.latitude},${via1.longitude}|${via2.latitude},${via2.longitude}&" + "sensor=false&" + "mode=driving&optimizeWaypoints=true&" + "key=$secret"
    }

    fun getDirectionURL(
        origin: LatLng, via1: LatLng, via2: LatLng, via3: LatLng, dest: LatLng
    ): String {
        return "https://maps.googleapis.com/maps/api/directions/json?" + "origin=${origin.latitude},${origin.longitude}&" + "destination=${dest.latitude},${dest.longitude}&" + "waypoints=${via1.latitude},${via1.longitude}|${via2.latitude},${via2.longitude}|${via3.latitude},${via3.longitude}&" + "sensor=false&" + "mode=driving&" + "key=$secret"
    }

    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }

    private fun degreesToRadians(degrees: Double): Double {
        return degrees * Math.PI / 180.0
    }

    private fun radiansToDegrees(radians: Double): Double {
        return radians * 180.0 / Math.PI
    }

    fun calculateBearing(from: LatLng, to: LatLng): Double {
        val fromLatitude = degreesToRadians(from.latitude)
        val fromLongitude = degreesToRadians(from.longitude)
        val toLatitude = degreesToRadians(to.latitude)
        val toLongitude = degreesToRadians(to.longitude)
        val differenceLongitude = toLongitude - fromLongitude
        val y = sin(differenceLongitude) * cos(toLatitude)
        val x = cos(fromLatitude) * sin(toLatitude) - sin(fromLatitude) * cos(toLatitude) * cos(
            differenceLongitude
        )
        val radiansBearing = atan2(y, x)
        val degree = radiansToDegrees(radiansBearing)
        return if (degree >= 0) degree else 360 + degree
    }
}

class MapData : Serializable {
    var routes = ArrayList<Routes>()
}

class Routes : Serializable {
    var legs = ArrayList<Legs>()
}

class Legs : Serializable {
    var distance = Distance()
    var duration = Duration()
    var end_address = ""
    var start_address = ""
    var end_location = Location()
    var start_location = Location()
    var steps = ArrayList<Steps>()
}

class Steps : Serializable {
    var distance = Distance()
    var duration = Duration()
    var end_address = ""
    var start_address = ""
    var end_location = Location()
    var start_location = Location()
    var polyline = PolyLine()
    var travel_mode = ""
    var maneuver = ""
}

class Duration : Serializable {
    var text = ""
    var value = 0
}

class Distance : Serializable {
    var text = ""
    var value = 0
}

class PolyLine : Serializable {
    var points = ""
}

class Location : Serializable {
    var lat = ""
    var lng = ""
}


//fun checkPermissionsAndGetLocation() {
//    val fineLocationGranted = ContextCompat.checkSelfPermission(
//        App.baseApplication, Manifest.permission.ACCESS_FINE_LOCATION
//    ) == PackageManager.PERMISSION_GRANTED
//    val coarseLocationGranted = ContextCompat.checkSelfPermission(
//        App.baseApplication, Manifest.permission.ACCESS_COARSE_LOCATION
//    ) == PackageManager.PERMISSION_GRANTED
//
//    if (fineLocationGranted || coarseLocationGranted) {
//        getUserLocation(fineLocationGranted)
//    } else {
//        Toast.makeText(
//            App.baseApplication, "Location permissions are not granted", Toast.LENGTH_SHORT
//        ).show()
//    }
//}
//
//private fun getUserLocation(isFineLocation: Boolean) {
//    val fusedLocationClient: FusedLocationProviderClient =
//        LocationServices.getFusedLocationProviderClient(App.baseApplication)
//
//    try {
//        val locationResult: Task<android.location.Location> = fusedLocationClient.lastLocation
//        locationResult.addOnCompleteListener { task ->
//            if (task.isSuccessful && task.result != null) {
//                val location: android.location.Location = task.result
//                val lat = location.latitude
//                val lng = location.longitude
//
//                if (isFineLocation) {
//                    LiveLocationModel.setLocationResponseData(
//                        LocationSelectorUIState(
//                            selectedLat = lat,
//                            selectedLog = lng,
//                            selectedAddress = getFullAddress(lat, lng)
//                        )
//                    )
////                    Toast.makeText(App.baseApplication, "Exact Location: $lat, $lng", Toast.LENGTH_LONG).show()
//                } else {
//                    val approxLat = Math.round(lat * 1000.0) / 1000.0
//                    val approxLng = Math.round(lng * 1000.0) / 1000.0
//                    location.latitude = approxLat
//                    location.longitude = approxLng
//                    LiveLocationModel.setLocationResponseData(
//                        LocationSelectorUIState(
//                            selectedLat = approxLat,
//                            selectedLog = approxLng,
//                            selectedAddress = getFullAddress(lat, lng)
//                        )
//                    )
////                    Toast.makeText(
////                        App.baseApplication,
////                        "Approximate Location: $approxLat, $approxLng",
////                        Toast.LENGTH_LONG
////                    ).show()
//                }
//            } else {
//                Toast.makeText(App.baseApplication, "Unable to get location", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//    } catch (e: SecurityException) {
//        e.printStackTrace()
//    }
//}
//
//private fun getFullAddress(lat: Double, long: Double): String {
//    val geoCoder = Geocoder(App.baseApplication, Locale.getDefault())
//    val addresses: MutableList<Address>? = geoCoder.getFromLocation(lat, long, 1)
//
//    return if (!addresses.isNullOrEmpty()) {
//        val address = addresses[0]
//        var fullAddress = ""
//        for (i in 0..address.maxAddressLineIndex) {
//            fullAddress += address.getAddressLine(i) + "\n"
//        }
//        fullAddress.trim()
//    } else {
//        ""
//    }
//}


private lateinit var fusedLocationClient: FusedLocationProviderClient
private lateinit var locationRequest: LocationRequest
private lateinit var locationCallback: LocationCallback

fun checkPermissionsAndGetLocation(context: Context) {
    val fineLocationGranted = ContextCompat.checkSelfPermission(
        App.baseApplication, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    val coarseLocationGranted = ContextCompat.checkSelfPermission(
        App.baseApplication, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    if (fineLocationGranted || coarseLocationGranted) {
        getUserLocation(fineLocationGranted, context)
    } else {
        LiveLocationModel.setLocationResponseData(
            null
        )
    }
}

@SuppressLint("MissingPermission")
private fun getUserLocation(isFineLocation: Boolean, context: Context) {
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)


    val priority = if (isFineLocation) {
        Priority.PRIORITY_HIGH_ACCURACY

    } else {
        Priority.PRIORITY_BALANCED_POWER_ACCURACY
    }

    val cancellationTokenSource = CancellationTokenSource()

    fusedLocationClient.getCurrentLocation(priority, cancellationTokenSource.token)
        .addOnSuccessListener { location ->
            location?.let {
                val lat = it.latitude
                val lng = it.longitude

                if (isFineLocation) {
                    LiveLocationModel.setLocationResponseData(
                        LocationSelectorUIState(
                            selectedLat = lat,
                            selectedLog = lng,
                            selectedAddress = ""
                        )
                    )
                } else {
                    val approxLat = Math.round(lat * 100000.0) / 100000.0
                    val approxLng = Math.round(lng * 100000.0) / 100000.0
                    LiveLocationModel.setLocationResponseData(
                        LocationSelectorUIState(
                            selectedLat = approxLat,
                            selectedLog = approxLng,
                            selectedAddress = ""
                        )
                    )
                }

            } ?: run {
                // Use last known location as a fallback
                fusedLocationClient.lastLocation.addOnSuccessListener { lastLocation ->
                    lastLocation?.let {
                        val lat = it.latitude
                        val lng = it.longitude

                        if (isFineLocation) {
                            LiveLocationModel.setLocationResponseData(
                                LocationSelectorUIState(
                                    selectedLat = lat,
                                    selectedLog = lng,
                                    selectedAddress = ""
                                )
                            )
                        } else {
                            val approxLat = Math.round(lat * 1000.0) / 1000.0
                            val approxLng = Math.round(lng * 1000.0) / 1000.0
                            LiveLocationModel.setLocationResponseData(
                                LocationSelectorUIState(
                                    selectedLat = approxLat,
                                    selectedLog = approxLng,
                                    selectedAddress = ""
                                )
                            )
                            Toast.makeText(
                                App.baseApplication,
                                "Failed to get location: last Location",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    } ?: run {
                        LiveLocationModel.setLocationResponseData(
                            LocationSelectorUIState(
                                selectedLat = 48.8566,
                                selectedLog = 2.3522,
                                selectedAddress = "",
                                unable = true
                            )
                        )
//                    Toast.makeText(App.baseApplication, "Unable to get location", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }.addOnFailureListener {
            LiveLocationModel.setLocationResponseData(
                LocationSelectorUIState(
                    selectedLat = 48.8566,
                    selectedLog = 2.3522,
                    selectedAddress = "",
                    unable = true
                )
            )
            Toast.makeText(
                App.baseApplication,
                "Failed to get location: ${it.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
}

@SuppressLint("MissingPermission")
suspend fun getUserLocationForOnce(
    isFineLocation: Boolean, context: Context
): LocationSelectorUIState? {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    checkPermissionsAndGetLocation(context)
    return suspendCancellableCoroutine { continuation ->
        val locationRequest = LocationRequest.create().apply {
            numUpdates = 1
            interval = 1000
            fastestInterval = 500
            priority =
                if (isFineLocation) LocationRequest.PRIORITY_HIGH_ACCURACY else LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                if (location != null) {
                    val lat = location.latitude
                    val lng = location.longitude

                    val locationForReturn = if (isFineLocation) {
                        LocationSelectorUIState(
                            selectedLat = lat,
                            selectedLog = lng,
                            selectedAddress = getFullAddress(lat, lng, true)
                        )
                    } else {
                        val approxLat = Math.round(lat * 1000.0) / 1000.0
                        val approxLng = Math.round(lng * 1000.0) / 1000.0
                        LocationSelectorUIState(
                            selectedLat = approxLat,
                            selectedLog = approxLng,
                            selectedAddress = getFullAddress(lat, lng, true)
                        )
                    }

                    if (!continuation.isCompleted) {
                        continuation.resume(locationForReturn)
                    }
                } else {
                    if (!continuation.isCompleted) {
                        continuation.resumeWithException(Exception("Unable to get location"))
                    }
                    Toast.makeText(context, "Unable to get location", Toast.LENGTH_SHORT).show()
                }
                fusedLocationClient.removeLocationUpdates(this)
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

        continuation.invokeOnCancellation {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
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
                    addressLines.append(postalCode).append(" ")
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

fun getPostalCode(lat: Double, long: Double, isNeededZipCode: Boolean = false): String {
    if (!Geocoder.isPresent()) {
        return "Geocoder not available"
    }
    val geoCoder = Geocoder(App.baseApplication, Locale.getDefault())
    return try {
        val addresses: MutableList<Address>? = geoCoder.getFromLocation(lat, long, 1)
        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            val addressLines = StringBuilder()

            val postalCode = address.postalCode
            if (!postalCode.isNullOrEmpty()) {
                addressLines.append(postalCode)
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