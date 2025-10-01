package com.gogulf.passenger.app.ui.currentridenew

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.gogulf.passenger.app.data.model.LocationModel
import com.gogulf.passenger.app.ui.currentride.CustomOTPAlertDialog
import com.gogulf.passenger.app.ui.getaridev2.GetARideActivityV2
import com.gogulf.passenger.app.ui.ratings.RatingActivity
import com.gogulf.passenger.app.utils.MapsUtils
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityNewCurrentRideBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewCurrentRideActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var binding: ActivityNewCurrentRideBinding
    private lateinit var viewModel: NewCurrentRideViewModel

    var customAlertDialog: CustomOTPAlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        SystemBarUtil.enableEdgeToEdge(this)
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityNewCurrentRideBinding>(
            this, R.layout.activity_new_current_ride
        ).also {
            binding = it
            viewModel = ViewModelProvider(this)[NewCurrentRideViewModel::class.java]
            binding.viewModel = viewModel
            it.lifecycleOwner = this
        }

        binding.menuBar.setOnClickListener {
            startActivity(Intent(this@NewCurrentRideActivity, GetARideActivityV2::class.java))
            finishAffinity()

        }
        binding.callButton.setOnClickListener {
            try {
                val i = Intent(Intent.ACTION_DIAL)
                i.data =
                    Uri.parse("tel:${viewModel.currentRideResponseData.value?.driver?.mobile}");
                startActivity(i)
            } catch (e: Exception) {
                Toast.makeText(
                    this@NewCurrentRideActivity,
                    "Error Occured! Phone Number is Copied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.messageButton.setOnClickListener {
            try {
                val i = Intent(Intent.ACTION_VIEW)
                i.data =
                    Uri.parse("sms:${viewModel.currentRideResponseData.value?.driver?.mobile}");
                startActivity(i);
            } catch (e: Exception) {
                Toast.makeText(
                    this@NewCurrentRideActivity,
                    "Error Occured! Phone Number is Copied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.currentRideResponseData.observe(this) {
            if (it != null) {
                if (viewModel.currentRideResponseData.value?.current_status == "pob") {
                    customAlertDialog?.dismissDialog()
                    binding.btnShowOtp.visibility = View.GONE
                }

                if (viewModel.currentRideResponseData.value?.current_status == "completed") {
                    val bundle = Bundle()
                    bundle.putString(
                        IntentConstant.BOOKING_ID,
                        viewModel.currentRideResponseData.value?.id.toString()
                    )
                    val intent = Intent(this, RatingActivity::class.java)
                    intent.putExtra(IntentConstant.BUNDLE, bundle)
                    startActivity(intent)
                    finishAffinity()
                }
            } else {
                val intent = Intent(this@NewCurrentRideActivity, GetARideActivityV2::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }

        viewModel.pobOtpResponseData.observe(this) {
            if (it != null) {
                if (viewModel.currentRideResponseData.value?.current_status == "dod") {
                    customAlertDialog =
                        CustomOTPAlertDialog(this@NewCurrentRideActivity).setMessage(
                            it.otp.toString() ?: ""
                        ).setCancellable(true)

                    if (viewModel.currentRideResponseData.value?.current_status == "dod") {
                        binding.btnShowOtp.visibility = View.VISIBLE
                        if (customAlertDialog?.isShowing == false) {
                            customAlertDialog?.show()
                        }
                    } else {
                        binding.btnShowOtp.visibility = View.GONE
                        customAlertDialog?.dismissDialog()
                    }
                } else {
                    customAlertDialog?.dismissDialog()
                    binding.btnShowOtp.visibility = View.GONE
                }
            } else {
                customAlertDialog?.dismissDialog()
                binding.btnShowOtp.visibility = View.GONE
            }
        }

        binding.btnShowOtp.setOnClickListener {
            if (customAlertDialog == null) {
                customAlertDialog = CustomOTPAlertDialog(this@NewCurrentRideActivity).setMessage(
                    viewModel.pobOtpResponseData.value?.otp.toString() ?: ""
                ).setCancellable(true)
            }
            if (customAlertDialog?.isShowing == false) {
                customAlertDialog?.show()
            }
        }


        val mapFragment = supportFragmentManager.findFragmentByTag("map") as SupportMapFragment

        mapFragment.getMapAsync(this)

        lifecycleScope.launch {
            viewModel.driverState.collect {
                if (it.driverPositionSuccessData != null) {
                    reDrawDriverMarker()
                }
            }
        }


        lifecycleScope.launch {
            viewModel.currentRideUiState.collect {
                if (!it.isCameraBoundsUpdated) {

                    if (it.bookingResponse?.route?.overview_polyline?.points != null) {
                        drawPolyline()
                    }

                    if (it.bookingResponse?.drop != null) {
                        drawDropOffLocation(
                            LocationModel(
                                it.bookingResponse.drop?.name ?: "",
                                it.bookingResponse.drop?.lat ?: 0.0,
                                it.bookingResponse.drop?.lng ?: 0.0
                            )
                        )
                    }

                    if (it.bookingResponse?.pickup != null) {
                        drawPickUpLocation(
                            LocationModel(
                                it.bookingResponse.pickup?.name ?: "",
                                it.bookingResponse.pickup?.lat ?: 0.0,
                                it.bookingResponse.pickup?.lng ?: 0.0
                            )
                        )
                    }

                    if (it.bookingResponse?.route?.bounds != null) {
                        val southWestLatLng = LatLng(
                            it.bookingResponse.route?.bounds?.southwest?.lat ?: 0.0,
                            it.bookingResponse.route?.bounds?.southwest?.lng ?: 0.0
                        )
                        val northEastLatLng = LatLng(
                            it.bookingResponse.route?.bounds?.northeast?.lat ?: 0.0,
                            it.bookingResponse.route?.bounds?.northeast?.lng ?: 0.0
                        )
                        val bounds = LatLngBounds(southWestLatLng, northEastLatLng)
                        val width = resources.displayMetrics.widthPixels
                        if (viewModel.currentRideUiState.value.mMap != null) {
                            viewModel.clearIsCameraBoundsUpdated()
                        }
                        viewModel.currentRideUiState.value.mMap?.moveCamera(
                            CameraUpdateFactory.newLatLngBounds(
                                bounds, width, dpToPx(this@NewCurrentRideActivity, 514f), 150
                            )
                        )
                    }
                    if (viewModel.driverState.value.driverPositionSuccessData != null) {
                        reDrawDriverMarker()
                    }

                }


//                if (it.error != null) {
//                    ErrorDialog(this@CurrentRideActivity, it.error).show()
//                    viewModel.resetError()
//                }

            }
        }



    }


    override fun onMapReady(googleMap: GoogleMap) {

//        val style = R.raw.map_style
//
//        val success = googleMap.setMapStyle(
//            MapStyleOptions.loadRawResourceStyle(
//                this, style
//            )
//        )
//
//        if (!success) {
//            Log.e("MapStyle", "Style parsing failed.")
//        }

        viewModel.updateMap(googleMap)
    }

    private suspend fun drawPolyline(result: List<List<LatLng>>) {
        withContext(Dispatchers.Main) {
            val lineOption = PolylineOptions()
            for (i in result.indices) {
                lineOption.addAll(result[i])
                lineOption.width(5f)
                lineOption.color(resources.getColor(R.color.defaulRippleColor))
                lineOption.geodesic(true)
            }
            viewModel.currentRideUiState.value.mMap?.addPolyline(lineOption)
        }
    }

    private fun drawPolyline() {
        val result = ArrayList<List<LatLng>>()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val path = ArrayList<LatLng>()
                path.addAll(MapsUtils.decodePolyline(viewModel.currentRideUiState.value.bookingResponse?.route?.overview_polyline?.points!!))

                result.add(path)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            drawPolyline(result)
        }
    }

    private fun drawDropOffLocation(location: LocationModel) {
        val originLocation = LatLng(location.latitude, location.longitude)
        viewModel.currentRideUiState.value.mMap?.addMarker(
            MarkerOptions().position(originLocation)
                .icon(MapsUtils.vectorToBitmap(this, R.drawable.ic_end_destination_circle))
        )
    }

    private fun drawPickUpLocation(location: LocationModel) {
        val originLocation = LatLng(location.latitude, location.longitude)
        viewModel.currentRideUiState.value.mMap?.addMarker(
            MarkerOptions().position(originLocation)
                .icon(MapsUtils.vectorToBitmap(this, R.drawable.ic_start_destination_circle))
        )
    }

    private fun reDrawDriverMarker() {
        val driverLocation = LatLng(
            viewModel.driverState.value.driverPositionSuccessData?.lat ?: 48.864716,
            viewModel.driverState.value.driverPositionSuccessData?.lng ?: 2.349014
        )
        val bearing = if (viewModel.driverLastLocation != null) {
            MapsUtils.calculateBearing(viewModel.driverLastLocation!!, driverLocation)
        } else {
            MapsUtils.calculateBearing(LatLng(0.0, 0.0), driverLocation)
        }

        if (viewModel.driverLocationMarker == null) {
            viewModel.driverLocationMarker = viewModel.currentRideUiState.value.mMap?.addMarker(
                MarkerOptions().position(driverLocation)
                    .icon(MapsUtils.vectorToBitmap(this, R.drawable.ic_vehicle_icon))
                    .rotation(bearing.toFloat()).anchor(0.5f, 0.5f)
            )
        } else {
            animateMarker(Location("").apply {
                latitude = driverLocation.latitude
                longitude = driverLocation.longitude
                setBearing(bearing.toFloat())
            }, viewModel.driverLocationMarker)
        }

        viewModel.driverLastLocation = driverLocation

    }

    fun animateMarker(destination: Location, marker: Marker?) {
        if (marker != null) {
            val startPosition = marker.position
            val endPosition = LatLng(destination.latitude, destination.longitude)

            val startRotation = marker.rotation

            val latLngInterpolator = LatLngInterpolator.LinearFixed()
            val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
            valueAnimator.duration = 1000 // duration 1 second
            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.addUpdateListener { animation ->
                try {
                    val v = animation.animatedFraction
                    val newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition)
                    marker.position = newPosition
                    marker.rotation = computeRotation(
                        v, startRotation, destination.bearing
                    )

                } catch (ex: Exception) {

                }
            }

            valueAnimator.start()
        }
    }

    private fun computeRotation(fraction: Float, start: Float, end: Float): Float {
        val normalizeEnd = end - start // rotate start to 0
        val normalizedEndAbs = (normalizeEnd + 360) % 360

        val direction = if (normalizedEndAbs > 180) -1 else 1 // -1 = anticlockwise, 1 = clockwise
        val rotation = if (direction > 0) {
            normalizedEndAbs
        } else {
            normalizedEndAbs - 360
        }

        val result = fraction * rotation + start
        return (result + 360) % 360
    }

    private interface LatLngInterpolator {
        fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng

        class LinearFixed : LatLngInterpolator {
            override fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng {
                val lat = (b.latitude - a.latitude) * fraction + a.latitude
                var lngDelta = b.longitude - a.longitude
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360
                }
                val lng = lngDelta * fraction + a.longitude
                return LatLng(lat, lng)
            }
        }
    }

    private fun dpToPx(context: Context, dp: Float): Int {
        val resources = context.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics
        ).toInt()
    }

}