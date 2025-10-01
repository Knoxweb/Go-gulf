package com.gogulf.passenger.app.ui.choosevehicle

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.gogulf.passenger.app.data.model.CalculateFareResponseData
import com.gogulf.passenger.app.ui.addextras.AddExtrasActivity
import com.gogulf.passenger.app.utils.LocationUtils
import com.gogulf.passenger.app.utils.LocationUtils.checkPermissionLocation
import com.gogulf.passenger.app.utils.LocationUtils.getLastLocation
import com.gogulf.passenger.app.utils.LocationUtils.gotoGPS
import com.gogulf.passenger.app.utils.LocationUtils.isLocationEnabled
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.utils.objects.LocationPermissions
import com.gogulf.passenger.app.utils.objects.PolylineUtils
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityChooseVehicleV2Binding

class ChooseVehicleV2Activity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var binding: ActivityChooseVehicleV2Binding
    private lateinit var viewModel: ChooseFleetViewModel
    private var isCurrently = false
    private var mapView: View? = null

    private lateinit var pickup_date : String
    private lateinit var pickup_time : String

    private var googleMap: GoogleMap? = null
    private var mapFragment: SupportMapFragment? = null

    private var passengerCount = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)

        DataBindingUtil.setContentView<ActivityChooseVehicleV2Binding>(
            this, R.layout.activity_choose_vehicle_v2
        ).also {
            binding = it
            it.lifecycleOwner = this
            val bundle = intent.getBundleExtra(IntentConstant.BUNDLE)
            isCurrently = bundle?.getBoolean(IntentConstant.TYPE, false) as Boolean
            pickup_date = bundle.getString("pickup_date")!!
            pickup_time = bundle.getString("pickup_time")!!
            passengerCount = bundle.getString(IntentConstant.PASSENGER_COUNT)!!
            viewModel = ViewModelProvider(
                this,
                ChooseFleetViewModelFactory(bundle?.getSerializable(IntentConstant.SERIAL) as CalculateFareResponseData)
            )[ChooseFleetViewModel::class.java].let {
                it
            }
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.recyclerChooseFleet.setHasFixedSize(true)
        binding.recyclerChooseFleet.setItemViewCacheSize(5)
        binding.recyclerChooseFleet.layoutManager = LinearLayoutManager(this)
        binding.recyclerChooseFleet.adapter = viewModel.fleetAdapter.value


        if (LocationPermissions.checkPermissionForLocation(this@ChooseVehicleV2Activity)) {
            if (isLocationEnabled(this)) {
                getLastLocation(this)
            } else {
                gotoGPS(this)
            }
        } else {
            checkPermissionLocation(this@ChooseVehicleV2Activity)
        }
        mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapView = mapFragment?.getView();

        mapFragment?.getMapAsync(this)

        binding.btnContinue.setOnClickListener {
            if (viewModel.selectedFleetModel != null) {
                val bundle = Bundle()
                bundle.putBoolean(IntentConstant.TYPE, isCurrently)
                bundle.putSerializable(IntentConstant.SERIAL, viewModel.selectedFleetModel)
                bundle.putSerializable(IntentConstant.SERIAL2, viewModel.quoteResponse?.quote)

                bundle.putString("pickup_date", pickup_date)
                bundle.putString("pickup_time", pickup_time)
                bundle.putString(IntentConstant.PASSENGER_COUNT, passengerCount)
                startActivity(
                    Intent(
                        this, AddExtrasActivity::class.java
                    ).putExtra(IntentConstant.BUNDLE, bundle)
                )
            } else {
                Toast.makeText(this, "Please select a fleet", Toast.LENGTH_SHORT).show()
            }

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.btnBack) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = v.marginLeft
                bottomMargin = v.marginBottom
                rightMargin = v.marginRight
                topMargin = systemBars.top
            }
            WindowInsetsCompat.CONSUMED
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        val mapStyleOptions =
            MapStyleOptions.loadRawResourceStyle(baseContext, R.raw.map_style)
//        googleMap.setMapStyle(mapStyleOptions)
        reCenterLocation()
    }

    var destinationGeoLocation: LatLng? = null
    var pickupMarker: Marker? = null
    var dropoffMarker: Marker? = null

    private fun reCenterLocation() {

        val curretLocation = LatLng(viewModel.quoteResponse!!.quote?.pickup_address?.lat?:0.0, viewModel.quoteResponse!!.quote?.pickup_address?.lng?:0.0)
        destinationGeoLocation = LatLng(viewModel.quoteResponse!!.quote?.drop_address?.lat?:0.0, viewModel.quoteResponse!!.quote?.drop_address?.lng?:0.0)
        googleMap?.clear()
        pickupMarker = googleMap?.addMarker(
            MarkerOptions()
                .position(curretLocation)
                .icon(
                    LocationUtils.vectorToBitmap(
                        this@ChooseVehicleV2Activity,
                        R.drawable.icon_start_in_map
                    )
                )

        )
        dropoffMarker = googleMap?.addMarker(
            MarkerOptions()
                .position(destinationGeoLocation!!)
                .icon(
                    LocationUtils.vectorToBitmap(
                        this@ChooseVehicleV2Activity,
                        R.drawable.icon_end_in_map
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
        val padding = 300 // offset from edges of the map in pixels

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



//        plotMarker(curretLocation, destinationGeoLocation!!)
        googleMap?.addPolyline(PolylineUtils.getPolyLines(viewModel.quoteResponse!!.quote?.route?.overview_polyline?.points))

//                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16f))
//        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(curretLocation, 14f))
    }
}