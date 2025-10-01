package com.gogulf.passenger.app.ui.splash

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.gogulf.passenger.app.App.Companion.preferenceHelper
import com.gogulf.passenger.app.data.model.StatusResponseData
import com.gogulf.passenger.app.ui.auth.login.LoginActivityV2
import com.gogulf.passenger.app.ui.auth.registerv2.RegisterActivityV2
import com.gogulf.passenger.app.ui.currentridenew.NewCurrentRideActivity
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.ui.getaridev2.GetARideActivityV2
import com.gogulf.passenger.app.ui.walkthrough.WalkThrough1Activity
import com.gogulf.passenger.app.utils.PrefEntity
import com.gogulf.passenger.app.utils.Preferences
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.checkPermissionsAndGetLocation
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.objects.PrefConstant
import com.gogulf.passenger.app.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var mViewDataBinding: ActivitySplashScreenBinding
    private val TAG = "SplashActivity"
    var bookingId: String? = ""
    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        SystemBarUtil.enableEdgeToEdge(this)
        super.onCreate(savedInstanceState)

        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        viewModel = ViewModelProvider(this)[SplashViewModel::class.java]

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            checkPermissionsAndGetLocation(this)
        }

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.splashLiveData.observe(this) { responseData ->
                getWhereToGo(responseData)
            }
        } else {
            viewModel.splashLiveData.observe(this) { responseData ->
                getWhereToGo(responseData)
            }
        }

        lifecycleScope.launch {
            viewModel.uiState.collect {
                if (it.error != null) {
                    CustomAlertDialog(this@SplashActivity).setTitle(it.error.title)
                        .setMessage(it.error.message).setPositiveText("Retry") { dialog, _ ->
                            dialog.dismiss()
                            triggerRebirth(this@SplashActivity)
                        }.setCancellable(false).show()
                    viewModel.resetError()
                }
            }
        }

    }

    private fun getWhereToGo(responseData: StatusResponseData?) {
        val firstTime = !Preferences.getPreferenceBoolean(
            this@SplashActivity, PrefEntity.FIRST_TIME
        )
        if (firstTime) {
            startActivity(Intent(this, WalkThrough1Activity::class.java))
            preferenceHelper.setValue(PrefConstant.SLIDER_INTRO, true)
            finishAffinity()
        } else {
            if (responseData == null) {
                startActivity(Intent(this, LoginActivityV2::class.java))
                finish()
            } else {
                if (responseData.currentDispatch == 1) {
                    Preferences.setPreference(
                        this, PrefEntity.FIREBASE_REFERENCE, responseData.firebaseReference ?: ""
                    )
                    startActivity(Intent(this, NewCurrentRideActivity::class.java))
                    finishAffinity()
                } else {
                    if (responseData.profileStatus == "new") {
                        Preferences.setPreference(
                            this,
                            PrefEntity.FIREBASE_REFERENCE,
                            responseData.firebaseReference ?: ""
                        )
                        startActivity(Intent(this, RegisterActivityV2::class.java))
                        finish()
                    } else if (responseData.profileStatus == "completed") {
                        Preferences.setPreference(
                            this,
                            PrefEntity.FIREBASE_REFERENCE,
                            responseData.firebaseReference ?: ""
                        )
                        startActivity(Intent(this, GetARideActivityV2::class.java))
                        finishAffinity()
                    } else {
                        Preferences.setPreference(
                            this,
                            PrefEntity.FIREBASE_REFERENCE,
                            responseData.firebaseReference ?: ""
                        )
                        startActivity(Intent(this, GetARideActivityV2::class.java))
                        finishAffinity()
                    }
                }
            }
        }
    }


    fun triggerRebirth(context: Context) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        val componentName = intent!!.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }


    fun getLayoutId(): Int = R.layout.activity_splash_screen

    fun initView() {
        preferenceHelper.setValue(PrefConstant.LOCATION_PERMISSION, true)
    }

}