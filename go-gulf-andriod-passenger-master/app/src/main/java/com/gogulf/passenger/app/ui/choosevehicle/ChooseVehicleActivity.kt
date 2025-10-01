package com.gogulf.passenger.app.ui.choosevehicle

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.data.model.response.bookings.Fleet
import com.gogulf.passenger.app.data.model.response.bookings.QuotesResponse
import com.gogulf.passenger.app.ui.addextras.AddExtrasActivity
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.utils.carousels.CarouselView
import com.gogulf.passenger.app.utils.carousels.VehicleCarouselSliderAdapter
import com.gogulf.passenger.app.utils.interfaces.VehicleSliderListener
import com.gogulf.passenger.app.utils.maputils.MapTransitions
import com.gogulf.passenger.app.utils.objects.DebugMode
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.utils.objects.LocationPermissions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.gogulf.passenger.app.utils.objects.PolylineUtils
import com.gogulf.passenger.app.databinding.ActivityChooseVehicleBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class ChooseVehicleActivity : BaseActivity<ActivityChooseVehicleBinding>(), OnMapReadyCallback,
    CoroutineScope {

    private lateinit var mViewDataBinding: ActivityChooseVehicleBinding

    private lateinit var carouselView: CarouselView
    private var fleetList = ArrayList<Fleet>()
    private var isCurrently = false
    private val TAG = "ChooseVehicleActivity"
    private var googleMap: GoogleMap? = null
    private var mapFragment: SupportMapFragment? = null
    private var isRecenter = true
    private var counters = 0
    private lateinit var quotesResponse: QuotesResponse

    override fun getLayoutId(): Int = R.layout.activity_choose_vehicle

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityChooseVehicleBinding
        mViewDataBinding.menuBar.setOnClickListener {
//            gotoClass(MenuActivity::class.java, false)
            finish()
        }

        try {
            val bundle = intent.getBundleExtra(IntentConstant.BUNDLE)
            isCurrently = bundle?.getBoolean(IntentConstant.TYPE, false) as Boolean
            quotesResponse = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getSerializable(
                    IntentConstant.SERIAL,
                    QuotesResponse::class.java
                ) as QuotesResponse
            } else {
                bundle.getSerializable(IntentConstant.SERIAL) as QuotesResponse

            }
        } catch (e: Exception) {
            DebugMode.e(TAG, e.message.toString(), "Catch Error -> ")
        }




        fleetList()

        carouselView = CarouselView(
            mViewDataBinding.bottomSheetLayout.carouselContainer.viewPager,
            mViewDataBinding.bottomSheetLayout.carouselContainer.indicatorView,
            this@ChooseVehicleActivity,
            fleetList,
            quotesResponse,
            object : VehicleCarouselSliderAdapter.OnImageSelected {
                override fun onClicked(carouselModel: Fleet) {
//                    chooseVehicleViewModel.getResponse(carouselModel.fleetID)
                    val bundle = Bundle()
                    bundle.putBoolean(IntentConstant.TYPE, isCurrently)
                    bundle.putSerializable(IntentConstant.SERIAL, carouselModel)
                    bundle.putSerializable(IntentConstant.SERIAL2, quotesResponse)
                    gotoClass(AddExtrasActivity::class.java, bundle)
                }
            },
            supportFragmentManager,
            object : VehicleSliderListener {
                override fun count(count: Int) {
                    counters = count
                }

            }
        )

        mViewDataBinding.bottomSheetLayout.silderIcon.nextVehicle.setOnClickListener {
            if (counters < fleetList.size - 1) {
                counters++
                carouselView.changeViewPager(counters)
            }
        }
        mViewDataBinding.bottomSheetLayout.silderIcon.previousVehicle.setOnClickListener {
            if (counters > 0) {
                counters--
                carouselView.changeViewPager(counters)
            }
        }
        if (LocationPermissions.checkPermissionForLocation(this@ChooseVehicleActivity)) {
            if (isLocationEnabled()) {
                getLastLocation()
            } else {
                gotoGPS()
            }
        } else {
//            if (preferenceHelper.getValue(PrefConstant.LOCATION_PERMISSION, true) as Boolean)
            checkPermissionLocation(this@ChooseVehicleActivity)
        }
        mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)



    }

    override fun onResume() {
        super.onResume()
        if (LocationPermissions.checkPermissionForLocation(this@ChooseVehicleActivity)) {
            if (isLocationEnabled()) {
                getLastLocation()
                if (googleMap != null) {
                    reCenterLocation()
                }
            }
        }
    }

    override fun onBackPressed() {

    }

    private fun fleetList() {
        fleetList.clear()
        fleetList.addAll(quotesResponse.fleets)
    }

    private fun gotoClass(cls: Class<*>, finish: Boolean = true) {
        openNewActivity(this@ChooseVehicleActivity, cls, finish)
    }

    private fun gotoClass(cls: Class<*>, bundle: Bundle) {
        openNewActivity(this@ChooseVehicleActivity, cls, bundle, false)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        val mapStyleOptions =
            MapStyleOptions.loadRawResourceStyle(baseContext, R.raw.map_style)
//        googleMap.setMapStyle(mapStyleOptions)
        reCenterLocation()
    }

    var driverMarker: Marker? = null
    var pickupMarker: Marker? = null
    var dropoffMarker: Marker? = null
    private fun reCenterLocation() {
        DebugMode.e(TAG, "Saved Location recenter ->  $latitude , $longitude")

        val curretLocation = LatLng(quotesResponse.fromLat, quotesResponse.fromLng)
        destinationGeoLocation = LatLng(quotesResponse.toLat, quotesResponse.toLng)
        googleMap?.clear()
        pickupMarker = googleMap?.addMarker(
            MarkerOptions()
                .position(curretLocation)
                .icon(
                    MapTransitions.bitmapDescriptorFromVector(
                        this@ChooseVehicleActivity,
                        R.drawable.ic_start_destination_marker
                    )
                )

        )
        dropoffMarker = googleMap?.addMarker(
            MarkerOptions()
                .position(destinationGeoLocation!!)
                .icon(
                    MapTransitions.bitmapDescriptorFromVector(
                        this@ChooseVehicleActivity,
                        R.drawable.ic_end_destination_marker
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
        val padding = 100 // offset from edges of the map in pixels

        try {
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
            googleMap?.moveCamera(cu)
        } catch (e: Exception) {
            log(e.message.toString())
        }



        markerClickListener()
//        plotMarker(curretLocation, destinationGeoLocation!!)
        googleMap?.addPolyline(PolylineUtils.getPolyLines(quotesResponse.polylinePoints.points))

//                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16f))
//        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(curretLocation, 14f))
    }

    private fun plotMarker(
        origin: LatLng,
        dest: LatLng,
    ) {

    }


    @SuppressLint("PotentialBehaviorOverride")
    private fun markerClickListener() {
        googleMap?.setOnMarkerClickListener {
//            Toast.makeText(this, "Clicked location is $it", Toast.LENGTH_SHORT).show()
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}