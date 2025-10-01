package com.gogulf.passenger.app.ui.search_ride

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
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
import com.gogulf.passenger.app.data.model.Location
import com.gogulf.passenger.app.data.model.Route
import com.gogulf.passenger.app.ui.currentridenew.NewCurrentRideActivity
import com.gogulf.passenger.app.ui.getaridev2.GetARideActivityV2
import com.gogulf.passenger.app.utils.LocationUtils
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.utils.objects.PolylineUtils
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivitySearchDriverNewBinding
import kotlinx.coroutines.launch

class SearchDriverNewActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivitySearchDriverNewBinding
    private lateinit var viewModel: SearchDriverViewModel

    var retryRequestDialog: CustomAlertDialog? = null
    private var mapView: View? = null

    private var googleMap: GoogleMap? = null
    private var mapFragment: SupportMapFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)

        DataBindingUtil.setContentView<ActivitySearchDriverNewBinding>(
            this, R.layout.activity_search_driver_new
        ).also {
            binding = it
            it.lifecycleOwner = this
            val bundle = intent.getBundleExtra(IntentConstant.BUNDLE)

            viewModel = ViewModelProvider(
                this, SearchDriverViewModelFactory(
                    intent.getStringExtra("status"),
                    intent.getIntExtra("id", 0),
                    bundle?.getSerializable(IntentConstant.SERIAL) as Route,
                    intent.getSerializableExtra("pickup") as? Location,
                    intent.getSerializableExtra("drop") as? Location
                )
            )[SearchDriverViewModel::class.java]
            viewModel.customLoader = CustomLoader(this@SearchDriverNewActivity)
            binding.viewModel = viewModel
        }

        retryRequestDialog =
            CustomAlertDialog(this@SearchDriverNewActivity).setTitle(viewModel.uiState.value.confirmBookingSearchDriverResponseData?.title?:"Driver Not Available")
                .setMessage(viewModel.uiState.value.confirmBookingSearchDriverResponseData?.message?:"Retry booking request again.")
                .setPositiveText("Request Again") { dialog, _ ->
                    viewModel.hitRetryBooking()
                }.setNegativeText("Cancel") { dialog, _ ->
                    val intent =
                        Intent(this@SearchDriverNewActivity, GetARideActivityV2::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }.setCancellable(false)


        lifecycleScope.launch {
            viewModel.uiState.collect {
                it.confirmBookingSearchDriverResponseData?.let {
                    if (it.id == viewModel.bookingId) {
                        if (it.status != null) {
                            if (it.status != viewModel.bookingStatus) {
                                viewModel.bookingStatus = it.status

                                if (it.status == "pending") {
                                    retryRequestDialog?.dismiss()
                                } else {
                                    if (it.status == "current") {
                                        //TODO Handle accep  ted job

                                        startActivity(
                                            Intent(
                                                this@SearchDriverNewActivity,
                                                NewCurrentRideActivity::class.java
                                            )
                                        )
                                        finishAffinity()


                                        // status will be "current" as well

                                    } else if (it.status == "cancelled") {
                                        retryRequestDialog?.dismiss()
                                    } else {
                                        retryRequestDialog?.dismiss()

                                        retryRequestDialog =
                                            CustomAlertDialog(this@SearchDriverNewActivity).setTitle(viewModel.uiState.value.confirmBookingSearchDriverResponseData?.title?:"Driver Not Available")
                                                .setMessage(viewModel.uiState.value.confirmBookingSearchDriverResponseData?.message?:"Retry booking request again.")
                                                .setPositiveText("Request Again") { dialog, _ ->
                                                    viewModel.hitRetryBooking()
                                                }.setNegativeText("Cancel") { dialog, _ ->
                                                    val intent =
                                                        Intent(this@SearchDriverNewActivity, GetARideActivityV2::class.java)
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                    startActivity(intent)
                                                }.setCancellable(false)
                                        retryRequestDialog?.show()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.retryLoadingState.collect {
                if (it.isLoading) {
                    viewModel.customLoader?.show()
                } else {
                    viewModel.customLoader?.dismiss()
                }


                if (it.isSuccess) {
                    retryRequestDialog?.dismiss()
                    viewModel.resetIsSuccess()
                }

                if (it.isCancelSuccess) {
//                    CustomAlertDialog(this@SearchDriverNewActivity).setTitle("Booking Cancel")
//                        .setMessage("Booking Request Cancel Successfully").setPositiveText("OK") { dialog, _ ->
//                            dialog.dismiss()
//                            val intent =
//                                Intent(this@SearchDriverNewActivity, GetARideActivityV2::class.java)
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                            startActivity(intent)
//                        }.setCancellable(true).show()

                    val intent =
                        Intent(this@SearchDriverNewActivity, GetARideActivityV2::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    viewModel.resetIsSuccess()
                }

                if (it.error != null) {
                    CustomAlertDialog(this@SearchDriverNewActivity).setTitle(it.error.title)
                        .setMessage(it.error.message).setPositiveText("OK") { dialog, _ ->
                            dialog.dismiss()
                        }.setCancellable(true).show()
                    viewModel.resetIsSuccess()
                }
            }
        }

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapView = mapFragment?.getView();

        mapFragment?.getMapAsync(this)

        binding.btnCancel.setOnClickListener {
            CustomAlertDialog(this@SearchDriverNewActivity).setTitle("Cancel Booking?")
                .setMessage("You booking will be canceled. Are you sure you want to cancel?")
                .setPositiveText("Yes") { dialog, _ ->
                    viewModel.hitCancelBooking()
                }.setNegativeText("No") { dialog, _ ->
                    dialog.dismiss()
                }.setCancellable(false).show()
        }

        addBackCB()


    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(baseContext, R.raw.map_style)
//        googleMap.setMapStyle(mapStyleOptions)
        reCenterLocation()
    }

    var destinationGeoLocation: LatLng? = null
    var pickupMarker: Marker? = null
    var dropoffMarker: Marker? = null
    private fun reCenterLocation() {

        val curretLocation = LatLng(
            viewModel.bookingPickup?.lat
                ?: viewModel.uiState.value.confirmBookingSearchDriverResponseData?.pickup?.lat
                ?: 0.0,
            viewModel.bookingPickup?.lng
                ?: viewModel.uiState.value.confirmBookingSearchDriverResponseData?.pickup?.lng
                ?: 0.0
        )
        destinationGeoLocation = LatLng(
            viewModel.bookingDrop?.lat
                ?: viewModel.uiState.value.confirmBookingSearchDriverResponseData?.drop?.lat ?: 0.0,
            viewModel.bookingDrop?.lng
                ?: viewModel.uiState.value.confirmBookingSearchDriverResponseData?.drop?.lng ?: 0.0
        )
        googleMap?.clear()
        pickupMarker = googleMap?.addMarker(
            MarkerOptions().position(curretLocation).icon(
                LocationUtils.vectorToBitmap(
                    this@SearchDriverNewActivity, R.drawable.icon_start_in_map
                )
            )

        )
        dropoffMarker = googleMap?.addMarker(
            MarkerOptions().position(destinationGeoLocation!!).icon(
                LocationUtils.vectorToBitmap(
                    this@SearchDriverNewActivity, R.drawable.icon_end_in_map
                )
            )

        )
        val builder: LatLngBounds.Builder = LatLngBounds.Builder()
        if (pickupMarker != null) {
            builder.include(pickupMarker?.position!!)
        }
        if (dropoffMarker != null) {
            builder.include(dropoffMarker?.position!!)
        }

        /*   if (pickupMarker != null) {
               builder.include(pickupMarker?.position!!)
           }
           if (dropoffMarker != null) {
               builder.include(dropoffMarker?.position!!)
           }*/


        val bounds: LatLngBounds = builder.build()
        val padding = 400 // offset from edges of the map in pixels

        try {
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
            googleMap?.moveCamera(cu)
        } catch (e: Exception) {

        }

        mapView!!.findViewWithTag<View>("GoogleMapCompass")?.let { compass ->
            compass.post {
                val topMargin = compass.marginTop + 45
                val rlp = RelativeLayout.LayoutParams(compass.height, compass.height)
                rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0)

                val endMargin = (4 * Resources.getSystem().displayMetrics.density).toInt()
                rlp.setMargins(0, topMargin, endMargin, 0)
                compass.layoutParams = rlp
            }
        }


        googleMap?.addPolyline(PolylineUtils.getPolyLines(viewModel.bookingRoute?.overview_polyline?.points))

    }

    private fun addBackCB() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                CustomAlertDialog(this@SearchDriverNewActivity).setTitle("Cancel Booking?")
                    .setMessage("You booking will be canceled. Are you sure you want to cancel?")
                    .setPositiveText("Yes") { dialog, _ ->
                        viewModel.hitCancelBooking()
                    }.setNegativeText("No") { dialog, _ ->
                        dialog.dismiss()
                    }.setCancellable(true).show()
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback);
    }
}