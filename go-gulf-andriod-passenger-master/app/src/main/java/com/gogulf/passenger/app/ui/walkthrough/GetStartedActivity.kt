package com.gogulf.passenger.app.ui.walkthrough

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.ui.auth.login.LoginActivityV2
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.objects.*
import com.gogulf.passenger.app.databinding.ActivityGetStartedBinding

class GetStartedActivity : AppCompatActivity() {
    private lateinit var mViewDataBinding: ActivityGetStartedBinding
    private val TAG = "GetStartedActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        SystemBarUtil.enableEdgeToEdge(this)

        super.onCreate(savedInstanceState)
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_get_started)
        LocationPermissions.checkLocation(this@GetStartedActivity)
        PermissionCheckApp.isNotificationPermissionGranted(this@GetStartedActivity)
        mViewDataBinding.btnGetStarted.setOnClickListener {
            startActivity(Intent(this, LoginActivityV2::class.java))
        }

    }
}