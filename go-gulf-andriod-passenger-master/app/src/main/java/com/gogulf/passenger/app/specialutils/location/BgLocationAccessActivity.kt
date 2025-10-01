package com.gogulf.passenger.app.specialutils.location

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.WorkManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.gogulf.passenger.app.R


class BgLocationAccessActivity : AppCompatActivity() {

    private lateinit var locationStatusText: TextView
    private lateinit var locationToggleButton: Button
    private lateinit var workManager: WorkManager
    private var isServiceRunning = false // Track service state

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.entries.all { it.value }
        if (granted) {
            requestBackgroundLocationPermissionIfNeeded()
        } else {
            // Handle permission denied case
        }
    }

    private fun startLocationService() {
        val intent = Intent(this, LocationService::class.java)
        startForegroundService(intent)
        isServiceRunning = true
        updateButtonText()
    }

    private fun stopLocationService() {
        val intent = Intent(this, LocationService::class.java)
        stopService(intent)
        isServiceRunning = false
        updateButtonText()
    }

    private fun updateButtonText() {
        if (isServiceRunning) {
            locationToggleButton.text = "Stop Location Service"
        } else {
            locationToggleButton.text = "Start Location Service"
        }
    }

    private fun isServiceRunning(): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        val services = manager.getRunningServices(Int.MAX_VALUE)
        for (service in services) {
            if (LocationService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bg_location_access)

        locationStatusText = findViewById(R.id.location_status_text)
        locationToggleButton = findViewById(R.id.location_toggle_button)
        workManager = WorkManager.getInstance(this)

//        // Request permissions
//        requestForegroundLocationPermissions()
//
//        // Observe the worker state
//        workManager.getWorkInfosForUniqueWorkLiveData(BgLocationWorker.workName)
//            .observe(this) { workInfos ->
//                val enqueued = workInfos?.find { !it.state.isFinished } != null
//                updateUI(enqueued)
//            }
//
//        setupLocationToggle()
////        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
////        startActivity(intent)
//
//        if (!isGPSEnabled()) {
//            checkLocationSettings()
//        }
//
//        LocationUtils(context = this, true, false)
////        checkLocationSettings()
//        locationToggleButton.setOnClickListener {
//            if (isServiceRunning) {
//                stopLocationService()
//            } else {
//                startLocationService()
//            }
//        }
//        isServiceRunning = isServiceRunning()
//        updateButtonText()


        val locationUtils = LocationUtils(
            this, true, true
        )
        locationUtils.broadcastDeviceLocation()
        locationUtils.broadcastDeviceLocationStream()
        locationUtils.stopBroadcastingDeviceLocationStream()
        print(LocationDataHolder.locationLiveData.value?.latitude)

    }

    private fun requestForegroundLocationPermissions() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        } else {
            requestBackgroundLocationPermissionIfNeeded()
        }
    }

    private fun requestBackgroundLocationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            )
        } else {

        }
    }

    private fun setupLocationToggle() {
        locationToggleButton.setOnClickListener {
//            val workInfos = workManager.getWorkInfosForUniqueWorkLiveData(BgLocationWorker.workName).value
//            val enqueued = workInfos?.find { !it.state.isFinished } != null
//
//            if (enqueued) {
//                workManager.cancelUniqueWork(BgLocationWorker.workName)
//            } else {
//                workManager.enqueueUniquePeriodicWork(
//                    BgLocationWorker.workName,
//                    ExistingPeriodicWorkPolicy.KEEP,
//                    PeriodicWorkRequestBuilder<BgLocationWorker>(
//                        2,
//                        TimeUnit.SECONDS
//                    ).build()
//                )
//            }


        }
    }

    private fun updateUI(enqueued: Boolean) {
        if (enqueued) {
            locationStatusText.text = "Check the logcat for location updates every 15 min"
            locationToggleButton.text = "Disable updates"
        } else {
            locationStatusText.text = "Enable location updates and bring the app to the background."
            locationToggleButton.text = "Enable updates"
        }
    }

    private fun checkLocationSettings() {
        // Create a LocationRequest
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = 5000
        }

        // Build the LocationSettingsRequest
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            .setAlwaysShow(true)

        // Get the SettingsClient
        val settingsClient = LocationServices.getSettingsClient(this)
        val task = settingsClient.checkLocationSettings(builder.build())

        // Handle the task result
        task.addOnCompleteListener { taskResult ->
            try {
                val response = taskResult.getResult(ApiException::class.java)
                // All location settings are satisfied. The client can initialize location requests here.
                Log.i("TAG", "All location settings are satisfied.")
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        // Location settings are not satisfied. But could be fixed by showing the user a dialog.
                        Log.i(
                            "TAG",
                            "Location settings are not satisfied. Attempting to upgrade location settings."
                        )
                        try {
                            val resolvable = exception as ResolvableApiException
                            resolvable.startResolutionForResult(
                                this, REQUEST_CHECK_SETTINGS
                            )
                        } catch (e: IntentSender.SendIntentException) {
                            Log.i("TAG", "PendingIntent unable to execute request.")
                        } catch (e: ClassCastException) {
                            Log.i("TAG", "ClassCastException: ${e.message}")
                        }
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        // Location settings are inadequate, and cannot be fixed here.
                        Log.i(
                            "TAG",
                            "Location settings are inadequate, and cannot be fixed here. Dialog not created."
                        )
                    }
                }
            }
        }


    }

    private fun openGPSSettings() {
        val intent: Intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(intent, REQUEST_CHECK_SETTINGS)
    }

    private fun isGPSEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                // User agreed to enable location settings
            } else {
                // User did not agree to enable location settings
            }
        }
    }

    companion object {
        private const val REQUEST_CHECK_SETTINGS = 1001
    }
}