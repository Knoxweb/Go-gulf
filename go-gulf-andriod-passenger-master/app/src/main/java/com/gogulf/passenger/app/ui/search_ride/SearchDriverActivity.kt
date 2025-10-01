package com.gogulf.passenger.app.ui.search_ride

import android.annotation.SuppressLint
import android.os.*
import android.util.Log
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.data.model.base.BaseData
import com.gogulf.passenger.app.data.model.others.ErrorModel
import com.gogulf.passenger.app.data.model.response.currentride.CurrentRideApiModel
import com.gogulf.passenger.app.data.model.response.currentride.OnDemandModel
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.currentride.CurrentRideActivity
import com.gogulf.passenger.app.ui.currentride.CurrentRideVM
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.enums.Status
import com.gogulf.passenger.app.utils.maputils.*
import com.gogulf.passenger.app.utils.objects.DebugMode
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.utils.objects.LocationPermissions
import com.gogulf.passenger.app.utils.objects.PrefConstant
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gogulf.passenger.app.ui.getaride.GetARideActivity
import com.gogulf.passenger.app.utils.objects.PolylineUtils
import com.gogulf.passenger.app.databinding.ActivitySearchDriverScreenBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext


class SearchDriverActivity : BaseActivity<ActivitySearchDriverScreenBinding>(), OnMapReadyCallback,
    CoroutineScope {

    private lateinit var mViewDataBinding: ActivitySearchDriverScreenBinding
    private val TAG = "SearchDriverActivity"
    private var googleMap: GoogleMap? = null
    private var mapFragment: SupportMapFragment? = null
    private var isRecenter = true
    private val currentRideVM: CurrentRideVM by viewModel()
    private var bookingID = ""
    private lateinit var pickUpDestination: LatLng
    private lateinit var dropOffDestination: LatLng
    var handler = Handler(Looper.getMainLooper())
    var runnable: Runnable? = null
    var delay = 3000
    private var onDemandModel: OnDemandModel? = null

    private var isActive = true

    var vCounter = 0

    override fun getLayoutId(): Int = R.layout.activity_search_driver_screen

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivitySearchDriverScreenBinding

        mViewDataBinding.bottomSheetLayout.cancelButton.setOnClickListener {
            onCancel()
        }

        if (LocationPermissions.checkPermissionForLocation(this@SearchDriverActivity)) {
            if (isLocationEnabled()) {
                getLastLocation()
            } else {
                gotoGPS()
            }
        } else {
            checkPermissionLocation(this@SearchDriverActivity)
        }
        mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?


        currentRideVM.getCurrentRide()


        currentRideObserver()


        cancelRideObserver()


        realTimeDatabase()



    }


    private fun vibrate() {
        val v = getSystemService(VIBRATOR_SERVICE) as Vibrator
// Vibrate for 500 milliseconds
// Vibrate for 500 milliseconds
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.EFFECT_HEAVY_CLICK))
    }

    private fun onCancel() {
        CustomAlertDialog(this@SearchDriverActivity)
            .setTitle("Cancel Booking")
            .setMessage("Are you sure you want to cancel booking?")
            .setPositiveText("OK") { dialog, _ ->
                dialog.dismiss()
                currentRideVM.cancelCurrentRequest(bookingID, false)
            }
            .setCancellable(false)
            .show()
    }

    private fun gotoCurrentRide() {
        gotoClass(CurrentRideActivity::class.java)
    }

    override fun onResume() {
        super.onResume()
        if (LocationPermissions.checkPermissionForLocation(this@SearchDriverActivity)) {
            if (isLocationEnabled()) {
                getLastLocation()
                if (googleMap != null) {
                    reCenterLocation()
                }
            }
        }


        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())

            vibrate()
            vCounter++
            if (vCounter > 5) {
                handler.removeCallbacks(runnable!!)
            }
        }.also { runnable = it }, delay.toLong())
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable!!)
//        cancelTimer()
    }


    private fun gotoClass(cls: Class<*>) {
        openNewActivity(this@SearchDriverActivity, cls, true)
    }

    private fun gotoClass(cls: Class<*>, bundle: Bundle) {
        openNewActivity(this@SearchDriverActivity, cls, bundle, true)
    }

    private fun gotoClass() {
//        openNewActivity(this@CurrentRideActivity, DriverInformationActivity::class.java, false)
//        openNewActivity(this@CurrentRideActivity, CurrentRideActivity::class.java, false)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        val mapStyleOptions =
            MapStyleOptions.loadRawResourceStyle(baseContext, R.raw.map_style)
//        googleMap.setMapStyle(mapStyleOptions)
        reCenterLocation()
    }

    override fun onBackPressed() {

    }

    var driverMarker: Marker? = null
    var pickupMarker: Marker? = null
    var dropoffMarker: Marker? = null
    private fun reCenterLocation() {
        DebugMode.e(TAG, "Saved Location recenter ->  $latitude , $longitude")

        googleMap?.clear()
        pickupMarker = googleMap?.addMarker(
            MarkerOptions()
                .position(pickUpDestination)
                .icon(
                    MapTransitions.bitmapDescriptorFromVector(
                        this@SearchDriverActivity,
                        R.drawable.ic_start_destination_marker
                    )
                )

        )
        dropoffMarker = googleMap?.addMarker(
            MarkerOptions()
                .position(dropOffDestination)
                .icon(
                    MapTransitions.bitmapDescriptorFromVector(
                        this@SearchDriverActivity,
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

        val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        try {
            googleMap?.moveCamera(cu)
        } catch (e: Exception) {
            DebugMode.e(TAG, "catch ${e.message.toString()}", "Catch map camera ")
        }

        markerClickListener()

        googleMap?.addPolyline(PolylineUtils.getPolyLines(onDemandModel?.quote?.polylinePoints?.points))
        plotMarker(pickUpDestination, dropOffDestination)

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

    private fun setTimerText(text: String?) {
        mViewDataBinding.waitingDriver.timeScreen.text = text
    }

    private var remainingTime: CountDownTimer? = null

    private fun remainingTimerCounter(remainTime: Long) {
        remainingTime = object : CountDownTimer(remainTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                setTimerText(seconds.toString())
            }

            override fun onFinish() {
                showCancelAndRetryDialog()

            }

        }
        remainingTime?.start()
    }

    private fun showCancelAndRetryDialog() {
        CustomAlertDialog(this@SearchDriverActivity).setTitle("Driver Not Available")
            .setMessage("Retry booking request again")
            .setPositiveText("Re-Try") { dialog, _ ->
                currentRideVM.reTryCurrentRideRequest(bookingID)

            }
            .setNegativeText("Cancel") { dialog, _ ->
                currentRideVM.cancelCurrentRequest(bookingID, true)
            }
            .setCancellable(false)
            .show()
    }


    private fun currentRideObserver() {
        currentRideVM.currentResponse.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    hideDialog()
                    cancelTimer()
                    try {
                        val currentRideModel = Gson().fromJson<BaseData<OnDemandModel>>(
                            it.data,
                            object : TypeToken<BaseData<OnDemandModel>>() {}.type
                        )

                        val quote = currentRideModel.data.quote
                        onDemandModel = currentRideModel.data
                        bookingID = currentRideModel.data.quote.bookingID
                        mViewDataBinding.modeJR = currentRideModel.data.quote

                        remainingTimerCounter(currentRideModel.data.quote.remainingTimeSEC * 1000)
                        pickUpDestination = LatLng(quote.fromLat, quote.fromLng)
                        dropOffDestination = LatLng(quote.toLat, quote.toLng)
                        Glide.with(this@SearchDriverActivity)
                            .load(currentRideModel.data.quote.passengerUser.imageLink)
                            .placeholder(
                                R.drawable.ic_slyyk_s
                            ).into(mViewDataBinding.waitingDriver.defaultIcon)
                        mapFragment?.getMapAsync(this)
                    } catch (e: Exception) {
                        log(e.message.toString(), "Catch for onDemand ")
                        val currentRideModel = Gson().fromJson<BaseData<CurrentRideApiModel>>(
                            it.data,
                            object : TypeToken<BaseData<CurrentRideApiModel>>() {}.type
                        )
                        if (currentRideModel.mode == "JA") {
                            bookingID = currentRideModel.data.booking?.booking_id?:""

                            log("Booking Id $bookingID from catch data", "Catch for onDemand ")
                            val bundle = Bundle()
                            bundle.putString(IntentConstant.BOOKING_ID, bookingID)
                            gotoClass(CurrentRideActivity::class.java, bundle)
                        }
                    }

                }

                Status.ERROR -> {
                    hideDialog()

                }

                Status.LOADING -> {
                    showDialog()
                }
            }
        }
    }

    private fun cancelRideObserver() {
        currentRideVM.cancelCurrentResponse.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    hideDialog()
                    cancelTimer()
                    val cancelModel = Gson().fromJson<ErrorModel>(
                        it.data,
                        object : TypeToken<ErrorModel>() {}.type
                    )
                    showCancelFunction(cancelModel.title, cancelModel.message)


                }

                Status.ERROR -> {
                    hideDialog()
                    cancelTimer()
                    showCancelFunction(it.title, it.message)

                }

                Status.LOADING -> {
                    showDialog()
                }
            }
        }
    }

    private fun cancelTimer() {
        remainingTime?.cancel()
    }

    private fun showCancelFunction(title: String?, message: String?) {
        CustomAlertDialog(this@SearchDriverActivity).setTitle(title)
            .setMessage(message)
            .setPositiveText("OK") { dialog, _ ->
                dialog.dismiss()

                gotoClass(GetARideActivity::class.java)
            }
            .setCancellable(false)
            .show()
    }

    private var currentDatabaseRef: DatabaseReference? = null
    private fun realTimeDatabase() {
        val database = Firebase.database
        val identify = preferenceHelper.getValue(PrefConstant.IDENTITY, "") as String
        currentDatabaseRef = database.getReference("notifications").child(identify).child("current")
        currentDatabaseRef?.addValueEventListener(currentBookingEventListener)


    }

    private val currentBookingEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (!snapshot.exists()) {
                isActive = true
                return
            }
            try {
                DebugMode.e(TAG, "Current Ride accepted from onDemand Firebase ${snapshot.value}")
                val datas = snapshot.value as HashMap<*, *>
                bookingID = datas["booking_id"] as String
                isActive = false
                DebugMode.e(TAG, "Booking ID from firebase value $bookingID")
                cancelTimer()
                val bundle = Bundle()
                bundle.putString(IntentConstant.BOOKING_ID, bookingID)
                gotoClass(CurrentRideActivity::class.java, bundle)


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

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        currentDatabaseRef?.removeEventListener(currentBookingEventListener)
    }

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

}