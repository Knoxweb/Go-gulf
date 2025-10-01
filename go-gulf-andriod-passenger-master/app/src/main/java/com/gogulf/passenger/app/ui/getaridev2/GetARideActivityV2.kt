package com.gogulf.passenger.app.ui.getaridev2

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.gogulf.passenger.app.ui.bookinghistory.activity.BookingHistoryActivity
import com.gogulf.passenger.app.ui.dashboard.DashboardActivity
import com.gogulf.passenger.app.ui.getaride.DriverModelIdentity
import com.gogulf.passenger.app.ui.getaride.DriverModelIdentityMarker
import com.gogulf.passenger.app.ui.getaride.MutableDriverMarker
import com.gogulf.passenger.app.ui.invoices.InvoiceActivity
import com.gogulf.passenger.app.ui.notice.NoticeActivity
import com.gogulf.passenger.app.ui.schedulebooking.activity.NewScheduledBookingActivity
import com.gogulf.passenger.app.ui.search_ride.SearchDriverNewActivity
import com.gogulf.passenger.app.ui.settings.setting.SettingsNewActivity
import com.gogulf.passenger.app.ui.support.supports.SupportActivity
import com.gogulf.passenger.app.ui.trips.TripDestinationActivityV2
import com.gogulf.passenger.app.ui.walkthrough.GetStartedActivity
import com.gogulf.passenger.app.utils.LocationUtils
import com.gogulf.passenger.app.utils.LocationUtils.checkPermissionLocation
import com.gogulf.passenger.app.utils.LocationUtils.getLastLocation
import com.gogulf.passenger.app.utils.LocationUtils.gotoGPS
import com.gogulf.passenger.app.utils.LocationUtils.isLocationEnabled
import com.gogulf.passenger.app.utils.PreferencesAction
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import com.gogulf.passenger.app.utils.interfaces.FinalMapLatLng
import com.gogulf.passenger.app.utils.maputils.MapTransitions
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.utils.objects.LocationPermissions
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.data.model.firestore.PromoInfo
import com.gogulf.passenger.app.databinding.ActivityGetArideV2Binding
import com.gogulf.passenger.app.ui.base.BaseObserverListener
import com.gogulf.passenger.app.utils.interfaces.AnyApiListeners
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class GetARideActivityV2 : AppCompatActivity() {

    private lateinit var binding: ActivityGetArideV2Binding

    private var shouldWait: Boolean = false

    private val viewModel: GetARideV2ViewModel by viewModel()
    private var backPressedTime = 0L
    var startGeoLocation: LatLng? = null
    private var mapView: View? = null
    private var identityDriver = ArrayList<DriverModelIdentityMarker>()
    private val animateLive = MutableLiveData<MutableDriverMarker>()
    private var initialIdentityDriver = ArrayList<DriverModelIdentity>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
        val controller = WindowInsetsControllerCompat(
            window, window.decorView
        )

        controller.isAppearanceLightStatusBars = false
        controller.isAppearanceLightNavigationBars = false

        DataBindingUtil.setContentView<ActivityGetArideV2Binding>(
            this, R.layout.activity_get_aride_v2
        ).also {
            binding = it
            it.lifecycleOwner = this
            binding.viewModel = viewModel
        }
        viewModel.customLoader = CustomLoader(this@GetARideActivityV2)

        shouldWait = intent.getBooleanExtra("shouldWait", false)
        viewModel.menuAdapter.value?.setOnMenuClickListener(object :
            RecyclerMenuAdapter.onMenuClicked {
            override fun onMenuClicked(position: Int) {
                when (position) {
                    0 -> {
                        val intent = Intent(this@GetARideActivityV2, DashboardActivity::class.java)
                        startActivity(intent)


                    }

                    1 -> {
                        val intent =
                            Intent(this@GetARideActivityV2, NewScheduledBookingActivity::class.java)
                        startActivity(intent)
                    }

                    2 -> {
                        val intent =
                            Intent(this@GetARideActivityV2, BookingHistoryActivity::class.java)
                        startActivity(intent)
                    }

                    3 -> {
                        val intent = Intent(this@GetARideActivityV2, NoticeActivity::class.java)
                        startActivity(intent)
                    }

                    4 -> {
                        val intent = Intent(this@GetARideActivityV2, InvoiceActivity::class.java)
                        startActivity(intent)
                    }

                    5 -> {
                        val intent = Intent(this@GetARideActivityV2, SupportActivity::class.java)
                        startActivity(intent)
                    }

                    6 -> {
                        val intent =
                            Intent(this@GetARideActivityV2, SettingsNewActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        })

        if (LocationPermissions.checkPermissionForLocation(this@GetARideActivityV2)) {

            if (isLocationEnabled(this@GetARideActivityV2)) {
                getLastLocation(this@GetARideActivityV2)
            } else {
                gotoGPS(this@GetARideActivityV2)
            }


        } else {
            checkPermissionLocation(this@GetARideActivityV2)
        }

        viewModel.gMap.observe(this) {
            reCenterLocation()
        }


        val mapFragment = supportFragmentManager.findFragmentByTag("map") as SupportMapFragment?
        mapView = mapFragment!!.getView()
        mapFragment?.getMapAsync(OnMapReadyCallback { googleMap ->
            try {
                viewModel.gMap.value = googleMap
                mapView?.let {
                    val recenterButton =
                        (mapView!!.findViewById<View>("1".toInt()).parent as View).findViewById<View>(
                            "2".toInt()
                        )

                    recenterButton.post {
                        val rlp = RelativeLayout.LayoutParams(
                            recenterButton.height, recenterButton.height
                        )
                        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                        rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                        val bottomMargin =
                            (64 * Resources.getSystem().displayMetrics.density).toInt()
                        val endMargin = (16 * Resources.getSystem().displayMetrics.density).toInt()
                        rlp.setMargins(0, 0, endMargin, bottomMargin)
                        recenterButton.layoutParams = rlp
                        recenterButton as ImageView
                        recenterButton.setImageResource(R.drawable.location_marker_google_map)
                    }
//                    recenterButton.post {
//
//
//                    }
                }


//                viewModel.gMap.value?.setMapStyle(
//                    MapStyleOptions.loadRawResourceStyle(
//                        this, R.raw.map_style
//                    )
//                )


            } catch (e: Resources.NotFoundException) {
                Log.e("TAG", "Can't find style. Error: ", e)
            }
        })


        lifecycleScope.launch {
            animateLive.observe(this@GetARideActivityV2) {
                animateMarkerLiveData(it.pos!!, it.dMarker!!)
            }
        }


        viewModel.requestedRide.observe(this) {
            it?.let {
                if (it.status == "pending") {
                    val bundle = Bundle()
                    bundle.putSerializable(IntentConstant.SERIAL, it.route)
                    val i = Intent(this, SearchDriverNewActivity::class.java)
                    i.putExtra("status", it.status)
                    i.putExtra("id", it.id)
                    i.putExtra("pickup", it.pickup)
                    i.putExtra("drop", it.drop)
                    i.putExtra(IntentConstant.BUNDLE, bundle)
                    startActivity(i)
                }
            }
        }

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                val gestureExclusionRects = listOf(
                    Rect(0, 400, 200, 600),
                    Rect(0, 600, 200, 800),
                    Rect(0, 800, 200, 1000),
                    Rect(0, 1000, 200, 1200)
                )

                window.systemGestureExclusionRects = gestureExclusionRects
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        val bottomPadding = binding.layoutInside.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(binding.layoutInside) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left, systemBars.top, systemBars.right, bottomPadding + systemBars.bottom
            )
            insets
        }

        viewModel.nearbyDriversData.observe(this) {
            addMarkersIdentity()
        }

        LocationUtils.myCurrentLocation.observe(this) {
            reCenterLocation()
        }

        binding.showHideDrawerButton.setOnClickListener {
            binding.drawer.open()
        }

        viewModel.shouldEnableTheHamburgerIcon.observe(this) {
            if (it) {
                binding.showHideDrawerButton.isEnabled = true
            } else {
                binding.showHideDrawerButton.isEnabled = false
            }
        }

        binding.recyclerMenuItems.layoutManager = LinearLayoutManager(this)

        binding.recyclerMenuItems.adapter = viewModel.menuAdapter.value

        binding.btnStartTrip.setOnClickListener {
            startActivity(Intent(this, TripDestinationActivityV2::class.java))
        }

        lifecycleScope.launch {
            viewModel.logoutState.collect {
                if (it.isLoading) {
                    viewModel.customLoader?.show()
                } else {
                    viewModel.customLoader?.hide()
                }
                if (it.error != null) {
                    Toast.makeText(this@GetARideActivityV2, it.error.message, Toast.LENGTH_SHORT)
                        .show()
                    viewModel.clearError()
                }
                if (it.onLogoutSuccess) {
                    PreferencesAction.clearAll(this@GetARideActivityV2)
                    val intent = Intent(this@GetARideActivityV2, GetStartedActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        }

        binding.btnLogout.setOnClickListener {
            CustomAlertDialog(this@GetARideActivityV2).setTitle("Confirm Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveText("Yes") { dialog, _ ->
                    dialog.dismiss()
                    viewModel.hitLogout()
                }.setNegativeText("No") { dialog, _ -> dialog.dismiss() }.setCancellable(true)
                .show()
        }

        viewModel.nearbyDriversData.observe(this) {
            initialIdentityDriver.clear()
            initialIdentityDriver.addAll(it)

            if (identityDriver.size == 0) {
                removeMarkersCustom()
                addMarkersIdentity()
            } else {
                checkDriverIdentity()
            }
        }

        addBackCB()
        promoTextObserver()

    }

    override fun onResume() {
        super.onResume()
//        viewModel.getCurrentRide()

    }


    private fun addBackCB() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    finish()
                } else {
                    Toast.makeText(
                        this@GetARideActivityV2, "Press back again to exit app", Toast.LENGTH_SHORT
                    ).show()
                    backPressedTime = System.currentTimeMillis()
                }
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)
    }

    @SuppressLint("MissingPermission")
    private fun reCenterLocation() {
        val curretLocation = LatLng(
            LocationUtils.myCurrentLocation.value?.latitude ?: 0.0,
            LocationUtils.myCurrentLocation.value?.longitude ?: 0.0
        )
        startGeoLocation = LatLng(
            LocationUtils.myCurrentLocation.value?.latitude ?: 0.0,
            LocationUtils.myCurrentLocation.value?.longitude ?: 0.0
        )
        viewModel.gMap.value?.clear()

        if (LocationPermissions.checkPermissionForLocation(this@GetARideActivityV2)) {
            viewModel.gMap.value?.isMyLocationEnabled = true
        } else {
            viewModel.gMap.value?.addMarker(
                MarkerOptions().position(curretLocation)
            )
        }
        viewModel.gMap.value?.moveCamera(CameraUpdateFactory.newLatLngZoom(curretLocation, 17f))
        addMarkersIdentity()
    }


    private fun checkDriverIdentity() {

        if (identityDriver.size == 0) {
            addMarkersIdentity()
        } else {
            if (identityDriver.size == initialIdentityDriver.size) {
                identityDriver.forEachIndexed { index, driverModelIdentity ->
                    if (driverModelIdentity.dIdentity == initialIdentityDriver[index].dIdentity) {

                        val position = LatLng(
                            initialIdentityDriver[index].dLocation?.lat!! as Double,
                            initialIdentityDriver[index].dLocation?.lng!! as Double
                        )

                        animateLive.postValue(
                            MutableDriverMarker(
                                pos = position, dMarker = driverModelIdentity
                            )
                        )
                    }
                }
            } else {
                removeMarkersCustom()
                addMarkersIdentity()
            }
        }
    }

    private fun addMarkersIdentity() {
        removeMarkersCustom()
        if (viewModel.nearbyDriversData.value == null) {
            return
        }
        for (i in 0 until viewModel.nearbyDriversData.value?.size!!) {
            val markerOptions = MarkerOptions()
            markerOptions.position(
                LatLng(
                    viewModel.nearbyDriversData.value!![i].dLocation?.lat as Double,
                    viewModel.nearbyDriversData.value!![i].dLocation?.lng as Double
                )
            )
            markerOptions.anchor(0.5f, 0.5f)
            val marker: Marker = viewModel.gMap.value?.addMarker(markerOptions)!!
            marker.setIcon(
                LocationUtils.vectorToBitmap(this@GetARideActivityV2, R.drawable.ic_vehicle_icon)
            )
            identityDriver.add(
                DriverModelIdentityMarker(
                    viewModel.nearbyDriversData.value!![i].dIdentity,
                    marker,
                    viewModel.nearbyDriversData.value!![i].dLocation
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

    private fun animateMarkerLiveData(
        pos: LatLng, driverModelIdentity: DriverModelIdentityMarker,
    ) {
        lifecycleScope.launch {
            MapTransitions.animateMarker(
                driverModelIdentity.marker!!,
                pos,
                false,
                viewModel.gMap.value!!,
                object : FinalMapLatLng {
                    override fun values(finalDestination: LatLng) {
                        driverModelIdentity.dLocation?.lat = finalDestination.latitude
                        driverModelIdentity.dLocation?.lng = finalDestination.longitude
                    }
                })
        }
    }


    private fun promoTextObserver() {
        BaseObserverListener.observe(
            data = viewModel.promoInfo,
            owner = this@GetARideActivityV2,
            listener = object : AnyApiListeners<PromoInfo> {
                override fun onError(title: String?, message: String?) {
                    viewModel.customLoader?.hide()
                }

                override fun onLoading() {
                    viewModel.customLoader?.show()
                }

                override fun onSuccess(data: PromoInfo?) {
                    Log.d("PARASH", data.toString())
                    viewModel.customLoader?.hide()
                    data?.let {
                        if(it.discountStatus == "1"){
                            binding.topPromoCard.visibility = View.VISIBLE
                            binding.promoText.text = it.discountMessage
                        } else {
                            binding.topPromoCard.visibility = View.GONE
                        }
                    }

                }

            }
        )
    }

}