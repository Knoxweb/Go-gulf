package com.gogulf.passenger.app.ui.base

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.gogulf.passenger.app.App
import com.gogulf.passenger.app.App.Companion.is403
import com.gogulf.passenger.app.data.internal.PreferenceHelper
import com.gogulf.passenger.app.data.internal.clearAll
import com.gogulf.passenger.app.data.model.base.BaseData
import com.gogulf.passenger.app.data.model.dashboards.ProfileModel
import com.gogulf.passenger.app.ui.walkthrough.GetStartedActivity
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.customcomponents.CustomProgressDialog
import com.gogulf.passenger.app.utils.enums.Status
import com.gogulf.passenger.app.utils.objects.*
import com.gogulf.passenger.app.utils.objects.Constants.ERROR_MESSAGE_LOCATION_PERMISSION
import com.gogulf.passenger.app.utils.objects.LocationPermissions.BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE
import com.gogulf.passenger.app.utils.objects.LocationPermissions.LOCATION_PERMISSION_REQUEST_CODE
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.android.viewmodel.ext.android.viewModel
import com.gogulf.passenger.app.R


abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    protected var baseLiveDataLoading = MutableLiveData<Boolean>()
    private var mViewDataBinding: T? = null
//    var userInformation = MutableLiveData<ProfileModel>()

//    val commonBaseViewModel: CommonBaseViewModel by viewModel()

    //    private var mCustomLoader: CustomLoaderDialog? = null
    lateinit var progressDialog: CustomProgressDialog
    private lateinit var mNoInternetDialog: Dialog
    var savedInstanceState: Bundle? = null
    var preferenceHelper = PreferenceHelper(App.baseApplication)

    val PERMISSION_ID = 22
    var permissionToRequest = ArrayList<String>()
    var permissionRejected = ArrayList<String>()
    var permissions = ArrayList<String>()
    var alertDialog: AlertDialog? = null
    var count = 0
    var pressAccept = 0
    var gotoSettings = 0
    var currentPolyline: Polyline? = null
    var myIdentity = ""
    val commonViewModel: CommonViewModel by viewModel()
    var startGeoLocation: LatLng? = null
    var viaGeoLocation: LatLng? = null
    var destinationGeoLocation: LatLng? = null
    var pickUpGeoLocation: LatLng? = null
    var dropOffGeoLocation: LatLng? = null
    var driverGeoLocation: LatLng? = null

    //LiveTrackingData
    var myCurrentLocation = MutableLiveData<LatLng?>()
    var userFullName = MutableLiveData<String>()

//    var TAGS = ""
//    protected abstract fun getTag(): String
//

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    val loadingObservable: MutableLiveData<Boolean> get() = baseLiveDataLoading

//    val isNetworkConnected: Boolean get() = NetworkUtils.isNetworkConnected(applicationContext)

    protected abstract fun initView(mViewDataBinding: ViewDataBinding?)


    override fun onCreate(savedInstanceState: Bundle?) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState

        latitude = (preferenceHelper.getValue(PrefConstant.LATITUDE, "0.00") as String).toDouble()
        longitude = (preferenceHelper.getValue(PrefConstant.LONGITUDE, "0.00") as String).toDouble()
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        progressDialog = CustomProgressDialog(this)
//        progressDialog.setTitle("Loading")
//        TAGS = getTag()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        myIdentity = preferenceHelper.getValue(PrefConstant.IDENTITY, "") as String
        DebugMode.e(TAGs,"myIdentity ########################### \n $myIdentity \n #############################################")

        initView(mViewDataBinding)


        baseProfileObserver()
        /*logoutObserver()
        profileObserver()*/
//        realTimeBaseActivity()
        is403.observe(this) {
            if (it) {
                Toast.makeText(
                    this@BaseActivity,
                    "Sorry! Logged in form another device.",
                    Toast.LENGTH_LONG
                ).show()
                logOutCode()
                is403.postValue(false)
            }
        }



    }

    fun showDialog() {
        progressDialog.show()
    }

    fun log(message: String, tag: String = TAGs) {
        DebugMode.e(TAGs, message, tag)
    }

    fun hideDialog() {
        progressDialog.dismissDialog()
    }

    fun noCurrentRide() {
        CustomAlertDialog(this).setTitle("Not Available")
            .setMessage("You don't have current ride.")
            .setPositiveText("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancellable(false)
            .show()
    }

    fun showKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun openNewActivity(activity: AppCompatActivity, cls: Class<*>, finishCurrent: Boolean) {
        val intent = Intent(activity, cls)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        if (finishCurrent) activity.finish()
    }

    /*  fun locationPermissionGranted() {
          LocationHelper().startListeningUserLocation(this, object : MyLocationListener {
              override fun onLocationChanged(location: Location?) {
                  latitude = location?.latitude ?: 0.00
                  preferenceHelper.setValue(PrefConstant.LATITUDE, latitude.toString())
                  longitude = location?.longitude ?: 0.00
                  preferenceHelper.setValue(PrefConstant.LONGITUDE, longitude.toString())

              }

          })
      }*/

    fun openNewActivity(
        activity: AppCompatActivity,
        cls: Class<*>,
        bundle: Bundle,
        finishCurrent: Boolean
    ) {
        val intent = Intent(activity, cls)
        intent.putExtra(IntentConstant.BUNDLE, bundle)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        if (finishCurrent) activity.finish()
    }

    protected fun replaceFragment(
        @IdRes id: Int,
        fragmentName: Fragment,
        fragmentTag: String,
        addToBackStack: Boolean
    ) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(id, fragmentName, fragmentTag)
        if (addToBackStack)
            transaction.addToBackStack(fragmentTag)
        transaction.commit()
    }

    override fun onBackPressed() {

        super.onBackPressed()
        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }

    fun backAnimation() {
        overridePendingTransition(
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }

    fun nextAnimation() {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    /** Ask Permissions for Media */
    fun checkPermissionMedia(activity: AppCompatActivity) {

        alertDialog?.dismiss()
        permissions.clear()
        permissions.add(Manifest.permission.CAMERA)
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
//        permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        permissionToRequest.clear()
        permissionToRequest = ImagePicker.findUnAskedPermissions(this, permissions)
        if (permissionToRequest.size > 0) {
            if (!ImagePicker.checkPermissionMedia(this)) {
                requestPermissionCamera()
            }

        }

    }

    /** Ask Permissions for Media */
    fun askPermissionMedia(activity: AppCompatActivity) {

        alertDialog?.dismiss()
        permissions.clear()
        permissions.add(Manifest.permission.CAMERA)
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
//        permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        permissionToRequest.clear()
        permissionRejected.clear()
        permissionToRequest = ImagePicker.findUnAskedPermissions(this, permissions)
        if (permissionToRequest.size > 0) {
            requestPermissionCamera()


        }

    }


    fun isLocationEnabled(): Boolean {
        val locationManager =
            getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    var mFusedLocationClient: FusedLocationProviderClient? = null
    var longitude = 0.00
    var latitude = 0.00


    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        if (LocationPermissions.checkPermissionForLocation(this@BaseActivity)) {
            DebugMode.e(
                "BaseActivity",
                "Get Last Location called ->  $latitude , $longitude"
            )
            if (isLocationEnabled()) {
                mFusedLocationClient!!.lastLocation.addOnCompleteListener { task: Task<Location?> ->
                    val location = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        latitude = location.latitude
                        longitude = location.longitude
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
                gotoGPS()

            }
        } else {
            LocationPermissions.requestLocationPermission(this@BaseActivity)
        }
    }

    fun gotoGPS() {
        CustomAlertDialog(this@BaseActivity)
            .setTitle("Notice")
            .setMessage("To continue, turn on device location, which uses Google's location service")
            .setPositiveText("OK") { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            .setNegativeText("No Thanks") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancellable(false)
            .show()
    }


    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        // Initializing LocationRequest
        // object with appropriate methods
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }


    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
            latitude = mLastLocation!!.latitude
            longitude = mLastLocation.longitude

            DebugMode.e("BaseActivity", "Requesting new Location ->  $latitude , $longitude")
            myCurrentLocation.postValue(LatLng(latitude, longitude))
            preferenceHelper.setValue(PrefConstant.LATITUDE, latitude.toString())
            preferenceHelper.setValue(PrefConstant.LONGITUDE, longitude.toString())

        }
    }

    /** Ask Permissions for locations */
    fun checkPermissionLocation(activity: AppCompatActivity) {
        if (!LocationPermissions.checkPermissionForLocation(activity)) {
            LocationPermissions.requestLocationPermission(activity)
        }

    }


    fun showErrorMessage(activity: AppCompatActivity) {
        ImagePicker.showMessageOkCancel(
            activity,
            ERROR_MESSAGE_LOCATION_PERMISSION
        ) { dialog, _ ->
            dialog.dismiss()
            permissionRejected.clear()

            alertDialog?.dismiss()

        }
    }

    fun openLocationSettings() {
        LocationPermissions.requestBackgroundPermission(this@BaseActivity)
    }

    fun settingNavigation() {
//        ImagePicker.checkBackgroundPermission(this@BaseActivity)
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//        ACTION_APP_NOTIFICATION_SETTINGS
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        // This will take the user to a page where they have to click twice to drill down to grant the permission
        startActivity(intent)
    }

    fun accessLocationDialog(activity: AppCompatActivity) {
        CustomAlertDialog(activity)
            .setIcon(R.drawable.baseline_location_on_24)
            .setTitle("Location Permission Required")
            .setMessage("Slyyk Driver App collects location data to track driver’s current location even when the app is not in use.")
//            .setMessage("Please set location permission to allow all the time from the setting to use the app.")
            .setPositiveText("Goto Settings") { dialog, _ ->
                dialog.dismiss()
                openLocationSettings()
            }
            .setCancellable(false)
            /* .setNegativeText("No thanks") { dialog, _ ->
                 ImagePicker.showMessageOkCancel(
                     this,
                     ERROR_MESSAGE_LOCATION_PERMISSION
                 ) { dialog, _ ->
                     dialog.dismiss()
                     permissionRejected.clear()
 //                    settingNavigation()

                 }
                 dialog.dismiss()
             }*/
            .show()
    }

    fun accessCameraDialog(activity: AppCompatActivity) {
        CustomAlertDialog(activity)
            .setIcon(R.drawable.ic_launcher_foreground)
            .setTitle("Update Files And Media settings")
            .setMessage("Allow us to access your files and media so you can upload the media.")
            .setPositiveText("UPDATE SETTINGS") { dialog, _ ->
                dialog.dismiss()
                settingNavigation()
            }
            .setCancellable(false)

            .show()
    }

    fun requestPermissionCamera() {
        requestPermissions(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_ID -> {
                preferenceHelper.setValue(PrefConstant.LOCATION_PERMISSION, false)
//                DebugMode.e("BaseActivity", "permissions ${permissions[0]} ${grantResults[0]}")
                permissionRejected.clear()
                for (perms in permissionToRequest) {

                    if (!ImagePicker.hasPermission(this, perms))
                        permissionRejected.add(perms)
                    DebugMode.e("BaseActivity", "${permissionRejected.size}  $perms")
                }


            }
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // Location permission denied
                }
            }
            BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Background location permission granted
                } else {
                    // Background location permission denied
                }
            }


        }
    }

    fun errorAlertDialog(title: String?, message: String?) {
        CustomAlertDialog(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveText("OK") { dialog, _ -> dialog.dismiss() }
            .setCancellable(false)
            .show()
    }

    fun successAlertDialog(
        title: String?,
        message: String?,
        listener: DialogInterface.OnClickListener
    ) {
        CustomAlertDialog(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveText("OK", listener)
            .setCancellable(false)
            .show()
    }


    fun logOutCode() {
        val intent = Intent(this@BaseActivity, GetStartedActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finishAffinity()
        preferenceHelper.clearAll()
        preferenceHelper.setValue(PrefConstant.SLIDER_INTRO, true)
    }
/*

    fun logoutObserver() {
        commonBaseViewModel.logoutResponse.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dismissDialog()


                }
                Status.LOADING -> {
                    logOutCode()
//                    progressDialog.show()

                }
                Status.ERROR -> {
                    progressDialog.dismissDialog()
//                    errorAlertDialog(it.title, it.message)

                }
            }
        }
    }


   */
    private val TAGs = "BaseActivity"
    /* private fun realTimeBaseActivity() {
         val database = Firebase.database
         val identity = preferenceHelper.getValue(PrefConstant.IDENTITY, "") as String
         val myRef = database.getReference("notifications").child(identity).child("current")
         myRef.addValueEventListener(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {

                 try {
                     val map = snapshot.value as HashMap<*, *>
                     Log.e(TAGs, "Value is: $map")
                     val bookingID = map["booking_id"] as String
                     val toLocation = map["to_location"] as String
                     val totalFare = map["fare"] as String
                     val fromLocation = map["from_location"] as String
                     val pickupDateTime = map["pickup_datetime"] as String
                     val duration = map["duration"] as String
                     val extended_hour = map["extended_hour"] as String

                     val model = CurrentRideShortModel(
                         bookingID = bookingID,
                         toLocation = toLocation,
                         fare = totalFare,
                         fromLocation = fromLocation,
                         pickupDateTime = pickupDateTime,
                         extendedHour = extended_hour,
                         duration = duration
                     )
                     preferenceHelper.setValue(
                         PrefConstant.BOOKING_ID,
                         bookingID
                     )


                 } catch (e: Exception) {
                     DebugMode.e(TAGs, e.message.toString(), "Catch firebase Error ")
                     Log.e(TAGs, "Value is: ${snapshot.value}")
                     preferenceHelper.setValue(
                         PrefConstant.BOOKING_ID,
                         ""
                     )


                 }


             }

             override fun onCancelled(error: DatabaseError) {
                 // Failed to read value
                 Log.w(TAGs, "Failed to read value.", error.toException())
             }

         })

     }*/

    fun baseProfileObserver() {
        commonViewModel.dashResponse.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
//                    progressDialog.dismissDialog()
                    val response = Gson().fromJson<BaseData<ProfileModel>>(
                        it.data,
                        object : TypeToken<BaseData<ProfileModel>>() {}.type
                    )
                    preferenceHelper.setValue(PrefConstant.FULL_NAME, response.data.name)
                    preferenceHelper.setValue(PrefConstant.IMAGE, response.data.imageLink)
                    userFullName.postValue(response.data.name)


                }
                Status.LOADING -> {
//                    progressDialog.show()

                }
                Status.ERROR -> {
//                    progressDialog.dismissDialog()
//                    errorAlertDialog(it.title, it.message)

                }
            }
        }
    }
}