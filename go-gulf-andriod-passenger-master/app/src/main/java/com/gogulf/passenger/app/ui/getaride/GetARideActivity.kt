package com.gogulf.passenger.app.ui.getaride


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.data.model.base.BaseData
import com.gogulf.passenger.app.data.model.response.currentride.CurrentRideApiModel
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.currentride.CurrentRideActivity
import com.gogulf.passenger.app.ui.menu.MenuActivity
import com.gogulf.passenger.app.ui.search_ride.SearchDriverActivity
import com.gogulf.passenger.app.ui.shortcuts.ShortcutAddModel
import com.gogulf.passenger.app.ui.shortcuts.ShortcutsActivity
import com.gogulf.passenger.app.ui.trips.TripDestinationActivity
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.enums.Status
import com.gogulf.passenger.app.utils.interfaces.FinalMapLatLng
import com.gogulf.passenger.app.utils.interfaces.ShortcutMapListener
import com.gogulf.passenger.app.utils.maputils.MapTransitions
import com.gogulf.passenger.app.utils.objects.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gogulf.passenger.app.ui.base.BaseObserverListener
import com.gogulf.passenger.app.utils.interfaces.AnyApiListeners
import com.gogulf.passenger.app.databinding.ActivityGetARideScreenBinding
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


class GetARideActivity : BaseActivity<ActivityGetARideScreenBinding>(), OnMapReadyCallback {

    private lateinit var mViewDataBinding: ActivityGetARideScreenBinding
    private var googleMap: GoogleMap? = null
    private var mapFragment: SupportMapFragment? = null
    private var isRecenter = true
    private val TAG = "GetARideActivity"
    private var fullName = ""
    private val getARideVM: GetARideVM by viewModel()

    private var initialsDrivers = ArrayList<DriverLocationModel>()
    private var driverList = ArrayList<DriverLocationModel>()

    private var initialIdentityDriver = ArrayList<DriverModelIdentity>()
    private var identityDriver = ArrayList<DriverModelIdentityMarker>()


    private val animateLive = MutableLiveData<MutableDriverMarker>()
    private var mShortcutList = ArrayList<ShortcutAddModel>()
    private lateinit var adapter: ShortcutAdapter
    override fun getLayoutId(): Int = R.layout.activity_get_a_ride_screen

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityGetARideScreenBinding
        fullName = preferenceHelper.getValue(PrefConstant.FULL_NAME, "") as String
        if (fullName.isNullOrEmpty()) {
            commonViewModel.getDashboard()
            userFullName.observe(this) {
                mViewDataBinding.name = it
            }
        } else
            mViewDataBinding.name = fullName

        currentRideObserver()
        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.app_name)
        )
        /*val myView = mViewDataBinding.bottomSheetConstraint
        ViewCompat.setElevation(myView, resources.getDimension(R.dimen.m_20dp))*/
        mViewDataBinding.bottomSheetConstraint.elevation =
            resources.getDimension(R.dimen.m_20dp)
        mViewDataBinding.bottomSheetLayout.searchDestinationContainer.hintTextView.setOnClickListener {
            gotoClass(TripDestinationActivity::class.java)
        }
        mViewDataBinding.menuBar.setOnClickListener {
            gotoClass(MenuActivity::class.java)
        }
        if (LocationPermissions.checkPermissionForLocation(this@GetARideActivity)) {
            if (isLocationEnabled()) {
                getLastLocation()
            } else {
                gotoGPS()
            }
        } else {
//            if (preferenceHelper.getValue(PrefConstant.LOCATION_PERMISSION, true) as Boolean)
            checkPermissionLocation(this@GetARideActivity)
        }
        mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        myCurrentLocation.observe(this) {
            DebugMode.e(TAG, "location changed redirection ->  $latitude , $longitude")
            reCenterLocation()
        }

        mViewDataBinding.navigationBtn.setOnClickListener {
            if (isLocationEnabled()) {
                getLastLocation()
            }
        }

//        DebugMode.e(TAG,CalenderPickerDialog.getDateTimeFormatted(0,"yyyy-MM-dd HH:mm:ss Z"),"TimeLog")
        mViewDataBinding.bottomSheetConstraint.elevation = 10f

        adapter = ShortcutAdapter(this@GetARideActivity, mShortcutList,
            object : ShortcutMapListener {
                override fun onClicked(shortcut: ShortcutAddModel) {
                    val bundle = Bundle()
                    bundle.putSerializable(IntentConstant.SERIAL, shortcut)
                    gotoClass(TripDestinationActivity::class.java, bundle)
                }
            })


        mViewDataBinding.bottomSheetLayout.addShortcut.shortcutTitle.text = "Add"
        mViewDataBinding.bottomSheetLayout.addShortcut.shortcutImage.setImageDrawable(
            ContextCompat.getDrawable(
                this@GetARideActivity,
                R.drawable.ic_menu_add
            )
        )

        mViewDataBinding.bottomSheetLayout.addShortcut.layoutContainer.setOnClickListener {
            gotoClass(ShortcutsActivity::class.java)
        }
        trackDriverObservers()


        mViewDataBinding.bottomSheetLayout.shortcutRecyclerView.hasFixedSize()
        mViewDataBinding.bottomSheetLayout.shortcutRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mViewDataBinding.bottomSheetLayout.shortcutRecyclerView.adapter = adapter
        /*
                val animation = TextAnimations(mViewDataBinding.bottomSheetLayout.searchDestinationContainer.hintTextView)
                animation.setCharacterDelay(150)
                animation.animateText("'Sample String'")*/

        lifecycleScope.launch {
            animateLive.observe(this@GetARideActivity) {
                animateMarkerLiveData(it.pos!!, it.dMarker!!)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun reCenterLocation() {
        DebugMode.e(TAG, "Saved Location recenter ->  $latitude , $longitude")

        val curretLocation = LatLng(latitude, longitude)
        startGeoLocation = LatLng(latitude, longitude)
        googleMap?.clear()


        if (LocationPermissions.checkPermissionForLocation(this@GetARideActivity)) {
            googleMap?.isMyLocationEnabled = true
        } else {
            googleMap?.addMarker(
                MarkerOptions()
                    .position(curretLocation)


            )
        }

//                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16f))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(curretLocation, 17f))
//        removeMarkers()
//        addMarkers()
//        addMarkersIdentity()
        removeMarkersCustom()
        addMarkersIdentity()
    }

    override fun onResume() {
        super.onResume()
        if (LocationPermissions.checkPermissionForLocation(this@GetARideActivity)) {
            if (isLocationEnabled()) {
                getLastLocation()
                if (googleMap != null) {
                    reCenterLocation()
                }
            }
        }
//        realTimeDatabase()
//        trackDrivers()
    }

    override fun onStart() {
        super.onStart()

        getShortcuts()
//        getARideVM.trackDrivers()
    }

    override fun onDestroy() {
        super.onDestroy()
        shortcutListener?.remove()
    }


    private fun gotoClass(cls: Class<*>, finish: Boolean = false) {
        openNewActivity(this@GetARideActivity, cls, finish)
    }

    private fun gotoClass(cls: Class<*>, bundle: Bundle, finish: Boolean = false) {
        openNewActivity(this@GetARideActivity, cls, bundle, finish)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        val mapStyleOptions =
            MapStyleOptions.loadRawResourceStyle(
                baseContext,
                R.raw.map_style
            )
//        googleMap.setMapStyle(mapStyleOptions)
        reCenterLocation()
    }

    private fun trackDriverObservers() {
        lifecycleScope.launch {
            BaseObserverListener.observe(
                getARideVM.myDriverModelIdentity,
                this@GetARideActivity,
                object : AnyApiListeners<ArrayList<DriverModelIdentity>> {
                    override fun onError(title: String?, message: String?) {
                        initialIdentityDriver.clear()
                    }

                    override fun onLoading() {
                        initialIdentityDriver.clear()
                    }

                    override fun onSuccess(data: ArrayList<DriverModelIdentity>?) {
                        initialIdentityDriver.clear()
                        initialIdentityDriver.addAll(data!!)

                        DebugMode.e(
                            TAG,
                            "original size ${initialIdentityDriver.size} , ${identityDriver.size}"
                        )

                        if (identityDriver.size == 0) {
                            removeMarkersCustom()
                            addMarkersIdentity()
                        } else {
                            checkDriverIdentity()
                        }
                    }


                }
            )
        }
    }

    private fun currentRideObserver() {
        getARideVM.currentResponse.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    val currentRideModel = Gson().fromJson<BaseData<CurrentRideApiModel>>(
                        it.data,
                        object : TypeToken<BaseData<CurrentRideApiModel>>() {}.type
                    )

                    when (currentRideModel.mode) {
                        "JR" -> {
                            gotoClass(SearchDriverActivity::class.java, true)
                        }

                        "JA", "DOD", "POB" -> {
                            val bundle = Bundle()
                            bundle.putString(
                                IntentConstant.BOOKING_ID,
                                currentRideModel.data.booking?.booking_id
                            )
                            gotoClass(CurrentRideActivity::class.java, true)
                        }
                    }


                }

                Status.ERROR -> {

                }

                Status.LOADING -> {

                }
            }
        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        // TODO: Step 1.6 START create a channel
        // Create channel to show notifications.
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            // TODO: Step 2.4 change importance
            NotificationManager.IMPORTANCE_HIGH
        )
            // TODO: Step 2.6 disable badges for this channel
            .apply {
                setShowBadge(false)
            }

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description =
            getString(R.string.app_name)

        val notificationManager = getSystemService(
            NotificationManager::class.java
        )

        notificationManager.createNotificationChannel(notificationChannel)

    }

    override fun onBackPressed() {
        CustomAlertDialog(this@GetARideActivity).setTitle("Exit App")
            .setMessage("Are you sure you want to exit app?")
            .setPositiveText("Yes") { dialog, _ ->
                dialog.dismiss()
                val a = Intent(Intent.ACTION_MAIN)
                a.addCategory(Intent.CATEGORY_HOME)
                a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(a)
            }
            .setNegativeText("No") { dialog, _ -> dialog.dismiss() }
            .setCancellable(false)
            .show()
    }

    private var shortcutListener: ListenerRegistration? = null

    @SuppressLint("NotifyDataSetChanged")
    private fun getShortcuts() {
        val db = Firebase.firestore
        val shortcutList = db.collection("Shortcuts")
        shortcutListener = shortcutList.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                mShortcutList.clear()
                for (document in snapshot) {
                    val shortcuts = document.data as HashMap<*, *>
                    val identity = shortcuts["identity"].toString()
                    val myIdentity = preferenceHelper.getValue(PrefConstant.IDENTITY, "") as String
                    if (identity == myIdentity) {
                        val shortcutAddModel = ShortcutAddModel(
                            shortcuts["address"].toString(),
                            shortcuts["icon"].toString(),
                            Integer.parseInt(shortcuts["id"].toString()),
                            shortcuts["identity"].toString(),
                            shortcuts["lat"].toString().toDouble(),
                            shortcuts["lng"].toString().toDouble(),
                            shortcuts["title"].toString(),
                        )
                        mShortcutList.add(shortcutAddModel)
                    }
                }
                adapter.notifyDataSetChanged()
            } else {
                Log.d(TAG, "Current data: null")
            }
        }


    }

    override fun onPause() {
        super.onPause()
        log("onPause call vayo ", TAG)

//        currentDatabaseRef?.removeEventListener(currentBookingEventListener)
//        trackDrivers?.removeEventListener(trackDriverListener)
    }

    override fun onStop() {
        super.onStop()
        getARideVM.trackDrivers?.removeEventListener(getARideVM.trackDriverListener!!)
    }
    /*

        private var trackDrivers: DatabaseReference? = null
        private fun trackDrivers() {
            val database = Firebase.database
            trackDrivers = database.getReference("nearbyDrivers")
            trackDrivers?.addValueEventListener(trackDriverListener)


        }

        private val trackDriverListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                driverList.clear()
                initialIdentityDriver.clear()
    //            initialsDrivers.clear()
                if (!snapshot.exists()) {
                    return
                }
                try {
                    for (data in snapshot.children) {
                        val bookingModels = data.getValue(DriverLocationModel::class.java)
                        val key = data.key
    //                    DebugMode.e(TAG, "Value is: $data Key is $key")
                        driverList.add(bookingModels!!)
                        initialIdentityDriver.add(DriverModelIdentity(key, bookingModels))

                    }
    //                DebugMode.e(TAG, "TRY VALUE IS ${snapshot.value}")


    //                removeMarkers()
    //                addMarkers()
                    if (identityDriver.size == 0) {
                        removeMarkersCustom()
                        addMarkersIdentity()
                    } else {

                        checkDriverIdentity()
                    }
    //                checkDriverIdentity()

                } catch (e: Exception) {
                    DebugMode.e(TAG, e.message.toString(), "Catch error firebase")
                    DebugMode.e(TAG, "CATCH VALUE IS ${snapshot.value}")
                }


            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        }*/


    private fun checkDriverIdentity() {
        DebugMode.e(TAG, "ma sanga pailai data cha tesaile yeta xu  ")
        DebugMode.e(
            TAG,
            "original size ${initialIdentityDriver.size} , ${identityDriver.size}"
        )
        if (identityDriver.size == 0) {
            addMarkersIdentity()
        } else {
            if (identityDriver.size == initialIdentityDriver.size) {
                identityDriver.forEachIndexed { index, driverModelIdentity ->
                    if (driverModelIdentity.dIdentity == initialIdentityDriver[index].dIdentity) {
                        if (driverModelIdentity.dLocation?.lat != initialIdentityDriver[index].dLocation?.lat ||
                            driverModelIdentity.dLocation?.lng != initialIdentityDriver[index].dLocation?.lng
                        ) {
                            val position = LatLng(
                                initialIdentityDriver[index].dLocation?.lat!! as Double,
                                initialIdentityDriver[index].dLocation?.lng!! as Double
                            )

                            DebugMode.e(TAG, "$position update")
                            animateLive.postValue(
                                MutableDriverMarker(
                                    pos = position,
                                    dMarker = driverModelIdentity
                                )
                            )

//                            return@forEachIndexed
                        }
                    }


                }
            } else {
                removeMarkersCustom()
                addMarkersIdentity()
            }
        }
    }

    private fun animateMarkerLiveData(
        pos: LatLng,
        driverModelIdentity: DriverModelIdentityMarker
    ) {

        lifecycleScope.launch {
            MapTransitions.animateMarker(
                driverModelIdentity.marker!!,
                pos,
                false,
                googleMap!!,
                object : FinalMapLatLng {
                    override fun values(finalDestination: LatLng) {
                        driverModelIdentity.dLocation?.lat =
                            finalDestination.latitude
                        driverModelIdentity.dLocation?.lng =
                            finalDestination.longitude

                    }

                })
        }
    }

    private fun addMarkersIdentity() {
        DebugMode.e(TAG, "ma sanga data xaina ahile so yeta xu j hunxa")
        DebugMode.e(
            TAG,
            "original size ${initialIdentityDriver.size} , ${identityDriver.size}"
        )
        removeMarkersCustom()
        for (i in 0 until initialIdentityDriver.size) {
            val markerOptions = MarkerOptions()
            markerOptions.position(
                LatLng(
                    initialIdentityDriver[i].dLocation?.lat as Double,
                    initialIdentityDriver[i].dLocation?.lng as Double
                )
            )
            val marker: Marker = googleMap?.addMarker(markerOptions)!!
            marker.setIcon(
                MapTransitions.bitmapDescriptorFromVector(
                    this@GetARideActivity,
                    R.drawable.ic_vehicle_icon
                )
            )

            identityDriver.add(
                DriverModelIdentityMarker(
                    initialIdentityDriver[i].dIdentity,
                    marker,
                    initialIdentityDriver[i].dLocation
                )
            )

        }


    }


    private fun removeMarkersCustom() {
        for (driver in identityDriver) {
            driver.marker?.remove()
        }
        identityDriver.clear()
    }


}