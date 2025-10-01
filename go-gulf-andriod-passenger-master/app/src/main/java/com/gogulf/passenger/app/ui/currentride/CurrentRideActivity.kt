package com.gogulf.passenger.app.ui.currentride

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gogulf.passenger.app.data.model.base.BaseData
import com.gogulf.passenger.app.data.model.others.ErrorModel
import com.gogulf.passenger.app.data.model.response.currentride.CurrentRideApiModel
import com.gogulf.passenger.app.data.model.response.currentride.CurrentRideBaseModels
import com.gogulf.passenger.app.data.model.response.currentride.DriverApi
import com.gogulf.passenger.app.data.model.response.currentride.FleetApi
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.dashboard.DashboardActivity
import com.gogulf.passenger.app.ui.getaride.GetARideActivity
import com.gogulf.passenger.app.ui.menu.MenuActivity
import com.gogulf.passenger.app.ui.ratings.RatingActivity
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.enums.Status
import com.gogulf.passenger.app.utils.interfaces.FinalMapLatLng
import com.gogulf.passenger.app.utils.maputils.MapTransitions
import com.gogulf.passenger.app.utils.maputils.MapTransitions.animateMarker
import com.gogulf.passenger.app.utils.objects.DebugMode
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.utils.objects.LocationPermissions
import com.gogulf.passenger.app.utils.objects.PolylineUtils
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityCurrentRideScreenBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

class CurrentRideActivity : BaseActivity<ActivityCurrentRideScreenBinding>(), OnMapReadyCallback,
    CoroutineScope {

    private lateinit var mViewDataBinding: ActivityCurrentRideScreenBinding
    private val TAG = "CurrentRideActivity"
    private var googleMap: GoogleMap? = null
    private var mapFragment: SupportMapFragment? = null
    private var isRecenter = true
    private var bookingId = ""
    private var driverNumber = ""
    private var mode = ""
    private var currentRideMode: CurrentRideApiModel? = null
    var currentLocation: LatLng? = null //pickupLocation
    private var otpRideCode: String? = null


    private val currentVM: CurrentRideVM by viewModel()


    override fun getLayoutId(): Int = R.layout.activity_current_ride_screen

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityCurrentRideScreenBinding
        mViewDataBinding.menuBar.setOnClickListener {
            gotoClass(MenuActivity::class.java)
        }

        try {
            val bundle = intent.getBundleExtra(IntentConstant.BUNDLE)
            bookingId = bundle?.getString(IntentConstant.BOOKING_ID, "") ?: ""
        } catch (e: Exception) {
            log(e.message.toString())
        }

        if (LocationPermissions.checkPermissionForLocation(this@CurrentRideActivity)) {
            if (isLocationEnabled()) {
                getLastLocation()
            } else {
                gotoGPS()
            }
        } else {
//            if (preferenceHelper.getValue(PrefConstant.LOCATION_PERMISSION, true) as Boolean)
            checkPermissionLocation(this@CurrentRideActivity)
        }
        mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        currentRideObserver()

        if (bookingId != "") {
            realTimeDatabase()
            realTimeTrackData()
            Log.d("bookingIdbookingId","bookingId $bookingId")
        } else {
            currentVM.getCurrentRide()
        }

        cancelCurrentRide()
        mViewDataBinding.bottomSheetLayout.cancelRide.setOnClickListener {
            currentVM.cancelCurRequest(bookingId)
        }

        log("onCreated is now active", "LifeCycle")


    }

    override fun onResume() {
        super.onResume()
        if (LocationPermissions.checkPermissionForLocation(this@CurrentRideActivity)) {
            if (isLocationEnabled()) {
                getLastLocation()
                if (googleMap != null) {
                    reCenterLocation()
                }
            }
        }


        //currentRideApiCall
    }

    override fun onPause() {
        super.onPause()

        log("onPause is now active", "LifeCycle")

//        dismissListener()
    }

    private fun dismissListener() {
        currentDatabaseRef?.removeEventListener(currentBookingEventListener)
        driverReference?.removeEventListener(drLocationListener)
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


    private fun addMarker() {

        DebugMode.e(TAG, "ADD MARKERE", "ADASDJAGDJASGDHJDADS")
        if (driverGeoLocation != null) {
            driverMarker = googleMap?.addMarker(
                MarkerOptions()
                    .position(driverGeoLocation!!)
//                    .title("Driver Location")
                    .icon(
                        MapTransitions.bitMapFromVector(
                            this@CurrentRideActivity,
                            R.drawable.ic_vehicle_icon
                        )
                    )
            )

//            animateMarker(driverMarker!!,driverGeoLocation!!,false)
        }

    }

    private fun removeMaker() {
        DebugMode.e(TAG, "REMOVE MARKERE", "ADASDJAGDJASGDHJDADS")
        driverMarker?.remove()

    }


    private var driverReference: DatabaseReference? = null
    private fun realTimeTrackData() {
        val database = Firebase.database
        driverReference =
            database.getReference("currentRide").child("driverTrackData").child(bookingId)
        driverReference?.addValueEventListener(drLocationListener)


    }

    private var beforeTravelling: LatLng? = null
    private val drLocationListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (!snapshot.exists()) {
                return
            }
            try {

                val map = snapshot.value as HashMap<*, *>
                DebugMode.e(TAG, map.toString(), "asl;kldnasklhdkashdkjasdjkasgdjasds")
                val driverLat = map["lat"] as Double
                val driverLng = map["lng"] as Double
                driverGeoLocation = LatLng(driverLat, driverLng)

                /* removeMaker()
                 addMarker()*/
                if (beforeTravelling == null) {
                    beforeTravelling = driverGeoLocation
                    removeMaker()
                    addMarker()
                } else {
                    animateMarker(
                        driverMarker!!,
                        driverGeoLocation!!,
                        false,
                        googleMap!!,
                        object : FinalMapLatLng {
                            override fun values(finalDestination: LatLng) {
                                beforeTravelling = finalDestination
                            }

                        })
                }
            } catch (e: Exception) {
                DebugMode.e(TAG, e.message.toString(), "Catch error firebase")
                DebugMode.e(TAG, "CATCH VALUE IS ${snapshot.value}")
            }


        }

        override fun onCancelled(error: DatabaseError) {
            // Failed to read value
            Log.w(TAG, "Failed to read value.", error.toException())
        }

    }


    private fun reCenterLocation() {
        DebugMode.e(TAG, "Saved Location recenter ->  $latitude , $longitude")


        googleMap?.clear()
        pickupMarker = googleMap?.addMarker(
            MarkerOptions()
                .position(currentLocation!!)
                .icon(
                    MapTransitions.bitmapDescriptorFromVector(
                        this@CurrentRideActivity,
                        R.drawable.ic_start_destination_marker
                    )
                )

        )
        dropoffMarker = googleMap?.addMarker(
            MarkerOptions()
                .position(destinationGeoLocation!!)
                .icon(
                    MapTransitions.bitmapDescriptorFromVector(
                        this@CurrentRideActivity,
                        R.drawable.ic_end_destination_marker
                    )
                )

        )
        removeMaker()
        addMarker()
        val builder: LatLngBounds.Builder = LatLngBounds.Builder()
        if (pickupMarker != null) {
            builder.include(pickupMarker?.position!!)
        }
        if (dropoffMarker != null) {
            builder.include(dropoffMarker?.position!!)
        }


        val bounds: LatLngBounds = builder.build()
        val padding = 100 // offset from edges of the map in pixels

        val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        try {
            googleMap?.moveCamera(cu)
        } catch (e: Exception) {
            log(e.message.toString(), TAG)
        }

        markerClickListener()
        googleMap?.addPolyline(PolylineUtils.getPolyLines(currentRideMode?.polyline_points?.points))
    }


    private fun markerClickListener() {
        googleMap?.setOnMarkerClickListener {
//            Toast.makeText(this, "Clicked location is $it", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun gotoClass(cls: Class<*>) {
        openNewActivity(this@CurrentRideActivity, cls, true)
    }

    private fun gotoClass(cls: Class<*>, bundle: Bundle) {
        openNewActivity(this@CurrentRideActivity, cls, bundle, true)
    }


    private var currentDatabaseRef: DatabaseReference? = null
    private fun realTimeDatabase() {
        val database = Firebase.database
        currentDatabaseRef = database.getReference("currentRide").child(bookingId)
        currentDatabaseRef?.addValueEventListener(currentBookingEventListener)


    }

    private val currentBookingEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (!snapshot.exists()) {
                return
            }
            try {
                DebugMode.e(TAG, "TRY VALUE IS ${snapshot.value}")
                val currentRideModel = snapshot.getValue(CurrentRideBaseModels::class.java)
//                val currentRideModel = snapshot.getValue(CurrentRideApiModel::class.java)
                currentRideModel?.let { model ->
                    model.data?.let {
                        setInitial(model,it)
                    }
                }
                otpRideCode = currentRideModel?.data?.booking?.ride_otp
                otpRideCode?.let {
                    val customAlertDialog = CustomOTPAlertDialog(this@CurrentRideActivity)
                        .setMessage("$otpRideCode")
                        .setCancellable(false)



                    if (currentRideModel?.mode == "DOD") {
                        customAlertDialog.show()
                    } else {
                        customAlertDialog.dismissDialog()
                    }

                }
                if (currentRideModel!!.mode != "PAID" && currentRideModel.mode != "ET" &&
                    currentRideModel.data != null
                )
                    if (currentRideModel.mode == "ET") {
                        progressDialog.setTitle("Payment Processing")
                        progressDialog.show()
                    }
                if (currentRideModel.mode == "DJC") {
                    gotoClass(DashboardActivity::class.java)
                }
                if (currentRideModel.mode == "PAID") {
                    progressDialog.dismissDialog()
                    val bundle = Bundle()
                    bundle.putString(IntentConstant.BOOKING_ID, bookingId)
                    gotoClass(RatingActivity::class.java, bundle)
                }


            } catch (e: Exception) {
                DebugMode.e(TAG, e.message.toString(), "Catch error firebase")
                DebugMode.e(TAG, "CATCH VALUE IS ${snapshot.value}")
            }


        }

        override fun onCancelled(error: DatabaseError) {
            // Failed to read value
            Log.w(TAG, "Failed to read value.", error.toException())
        }

    }
    fun setInitial(model: CurrentRideBaseModels, currentRideApiModel: CurrentRideApiModel) {
        val data = BaseData(
            title = model.title,
            message = model.message,
            mode = model.mode,
            data = currentRideApiModel
        )
        initialCalls(data)
    }

    override fun onBackPressed() {

    }

    private fun checkDriverValidation(driver: DriverApi?) {
        if (driver != null) {
            driverNumber = driver.phone?:""
            Glide.with(this@CurrentRideActivity).load(driver.image_link)
                .into(mViewDataBinding.bottomSheetLayout.driverImageView)
            mViewDataBinding.bottomSheetLayout.driverDivider.visibility = View.VISIBLE
            mViewDataBinding.bottomSheetLayout.driverInformation.visibility = View.VISIBLE
        } else {
            mViewDataBinding.bottomSheetLayout.driverDivider.visibility = View.GONE
            mViewDataBinding.bottomSheetLayout.driverInformation.visibility = View.GONE

        }

    }

    private fun vehicleImages(fleet: FleetApi?) {
        Glide.with(this@CurrentRideActivity).load(fleet?.image_link).into(
            mViewDataBinding.bottomSheetLayout.carImageView
        )

    }


    private fun callMessageButton() {
        mViewDataBinding.bottomSheetLayout.callButton.setOnClickListener {
            try {

                val i = Intent(Intent.ACTION_DIAL);
                i.data = Uri.parse("tel:$driverNumber");
                startActivity(i);
            } catch (e: Exception) {
                Toast.makeText(
                    this@CurrentRideActivity,
                    "Error Occured! Phone Number is Copied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        mViewDataBinding.bottomSheetLayout.messageButton.setOnClickListener {
            try {

                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("sms:$driverNumber");
                startActivity(i);
            } catch (e: Exception) {
                Toast.makeText(
                    this@CurrentRideActivity,
                    "Error Occured! Phone Number is Copied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun initialCalls(response: BaseData<CurrentRideApiModel>) {
        DebugMode.e(TAG, response.data.toString(), "data response API")
        DebugMode.e("Opt Ride", "${response.data?.booking?.ride_otp}", "data response API")
        mViewDataBinding.currentRide = response.data
        currentRideMode = response.data
        mViewDataBinding.bottomSheetLayout.driverStatus.text = response.message
        checkDriverValidation(response.data.driver)
        vehicleImages(response.data.booking?.fleet)
        callMessageButton()

        changeLocationAccordingToMode(response.mode, response)

        if (mode != response.mode) {

            mapFragment?.getMapAsync(this)
        }
        mode = response.mode

        showOrHideViewAccordingToMode(response.mode)


    }



    private fun changeLocationAccordingToMode(
        mode: String,
        response: BaseData<CurrentRideApiModel>
    ) {
        when (mode) {
            "JA", "DOD" -> {
                currentLocation =
                    LatLng(response.data.pickup_location?.lat?:0.0, response.data.pickup_location?.lng?:0.0)
                destinationGeoLocation = LatLng(
                    response.data.driver_current_location?.lat?:0.0,
                    response.data.driver_current_location?.lng?:0.0
                )


            }

            "POB" -> {
                currentLocation =
                    LatLng(response.data.pickup_location?.lat?:0.0, response.data.pickup_location?.lng?:0.0)

                destinationGeoLocation =
                    LatLng(response.data.dropoff_location?.lat?:0.0, response.data.dropoff_location?.lng?:0.0)


            }

            else -> {
                currentLocation =
                    LatLng(response.data.pickup_location?.lat?:0.0, response.data.pickup_location?.lng?:0.0)

                destinationGeoLocation = LatLng(
                    response.data.driver_current_location?.lat?:0.0,
                    response.data.driver_current_location?.lng?:0.0
                )


            }
        }


    }

    private fun showOrHideViewAccordingToMode(mode: String) {
        when (mode) {
            "JA" -> {

                mViewDataBinding.bottomSheetLayout.bottomLayoutLinear.visibility = View.VISIBLE
                mViewDataBinding.bottomSheetLayout.bottomLayoutLinear2.visibility = View.VISIBLE
                mViewDataBinding.bottomSheetLayout.bottomLayoutLinear3.visibility = View.VISIBLE
            }

            "DOD" -> {
                mViewDataBinding.bottomSheetLayout.bottomLayoutLinear.visibility = View.GONE
                mViewDataBinding.bottomSheetLayout.bottomLayoutLinear2.visibility = View.GONE
                mViewDataBinding.bottomSheetLayout.bottomLayoutLinear3.visibility = View.GONE

            }

            "POB" -> {
                mViewDataBinding.bottomSheetLayout.driverDivider.visibility = View.GONE
                mViewDataBinding.bottomSheetLayout.vehicleInformation.visibility = View.GONE
                mViewDataBinding.bottomSheetLayout.driverInformation.visibility = View.GONE
                mViewDataBinding.bottomSheetLayout.bottomLayoutLinear.visibility = View.GONE
                mViewDataBinding.bottomSheetLayout.bottomLayoutLinear2.visibility = View.VISIBLE
                mViewDataBinding.bottomSheetLayout.bottomLayoutLinear3.visibility = View.GONE

            }

            else -> {

                mViewDataBinding.bottomSheetLayout.bottomLayoutLinear.visibility = View.GONE
                mViewDataBinding.bottomSheetLayout.cancelRide.visibility = View.GONE
            }
        }

    }

    private fun currentRideObserver() {
        currentVM.currentResponse.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    hideDialog()
                    val currentRideModel = Gson().fromJson<BaseData<CurrentRideApiModel>>(
                        it.data,
                        object : TypeToken<BaseData<CurrentRideApiModel>>() {}.type
                    )

                    try {
                        initialCalls(currentRideModel)
                        log("${currentRideModel.data.booking?.ride_otp}", "Otp Ride")
                        if (bookingId.isEmpty()) {
                            bookingId = currentRideModel.data.booking?.booking_id?:""
                            dismissListener()
                            realTimeDatabase()
                            realTimeTrackData()
                        }

                    } catch (e: Exception) {
                        CustomAlertDialog(this@CurrentRideActivity).setTitle(currentRideModel.title)
                            .setMessage(currentRideModel.message)
                            .setPositiveText("OK") { dialog, _ ->
                                dialog.dismiss()
                                gotoClass(GetARideActivity::class.java)
                            }
                            .setCancellable(false)
                            .show()
                    }

                }

                Status.ERROR -> {
                    hideDialog()
                    CustomAlertDialog(this@CurrentRideActivity).setTitle(it.title)
                        .setMessage(it.message)
                        .setPositiveText("OK") { dialog, _ ->
                            dialog.dismiss()
                            gotoClass(GetARideActivity::class.java)
                        }
                        .setCancellable(false)
                        .show()

                }

                Status.LOADING -> {
                    showDialog()
                }
            }
        }
    }

    private fun cancelCurrentRide() {
        currentVM.cancelCurResponse.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    hideDialog()

                    val cancels = Gson().fromJson<ErrorModel>(
                        it.data,
                        object : TypeToken<ErrorModel>() {}.type
                    )
                    CustomAlertDialog(this@CurrentRideActivity).setTitle(cancels.title)
                        .setMessage(cancels.message)
                        .setPositiveText("OK") { dialog, _ ->
                            dialog.dismiss()
                            gotoClass(GetARideActivity::class.java)
                        }
                        .setCancellable(false)
                        .show()


                }

                Status.ERROR -> {
                    hideDialog()
                    errorAlertDialog(it.title, it.message)

                }

                Status.LOADING -> {
                    showDialog()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        log("OnDestory is now active", "LifeCycle")
        job.cancel()
        dismissListener()
    }

    override fun onStart() {
        super.onStart()

        log("onStart is now active", "LifeCycle")
    }

    override fun onStop() {
        super.onStop()
        log("onStop is now active", "LifeCycle")
    }

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


}