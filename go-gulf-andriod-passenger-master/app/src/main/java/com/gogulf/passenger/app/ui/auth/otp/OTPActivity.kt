package com.gogulf.passenger.app.ui.auth.otp

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.databinding.ViewDataBinding
import com.gogulf.passenger.app.data.apidata.APIConstants
import com.gogulf.passenger.app.data.model.auths.Authentications
import com.gogulf.passenger.app.data.model.base.BaseData
import com.gogulf.passenger.app.ui.auth.register.RegisterActivity
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.dashboard.DashboardActivity
import com.gogulf.passenger.app.utils.enums.Status
import com.gogulf.passenger.app.utils.interfaces.FirebaseOTPListener
import com.gogulf.passenger.app.utils.interfaces.PinViewActionListener
import com.gogulf.passenger.app.utils.objects.FirebaseLoginHandler
import com.gogulf.passenger.app.utils.objects.PrefConstant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.interfaces.FirebaseLoginListener
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityOtpScreenBinding
import org.koin.android.viewmodel.ext.android.viewModel

class OTPActivity : BaseActivity<ActivityOtpScreenBinding>(), BaseNavigation {
    private lateinit var mViewDataBinding: ActivityOtpScreenBinding
    private val otpViewModel: OTPVM by viewModel()
    private lateinit var mAuth: FirebaseAuth
    override fun getLayoutId(): Int = R.layout.activity_otp_screen

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityOtpScreenBinding
        mViewDataBinding.toolbarViewModel = commonViewModel
        commonViewModel.navigator = this
        mAuth = FirebaseAuth.getInstance()
        mViewDataBinding.otpCode.actionGo(object : PinViewActionListener {
            override fun onValid() {
                onValidated()
            }

        })
        val otpCode = getString(R.string.enter_digit_otp) + " "+ preferenceHelper.getValue(
            PrefConstant.MOBILE_NO,
            APIConstants.TestNumber
        )

        FirebaseLoginHandler.getFirebaseToken()
        mViewDataBinding.userInformationText.text = otpCode
        otpObserver()
        mViewDataBinding.otpCode.actionGo(object : PinViewActionListener {
            override fun onValid() {
                onValidated()
            }

        })

        mViewDataBinding.resendCode.setOnClickListener {
            onResendOtp()
//            otpViewModel.authenticateLoginRohan()

        }
        countDownTimer?.start()

    }
    private fun onResendOtp() {
        countDownTimer?.start()
        FirebaseLoginHandler.loginFirebase(
            this@OTPActivity,
            preferenceHelper.getValue(PrefConstant.MOBILE_NO, "") as String,
            mAuth,
            object : FirebaseLoginListener {
                override fun onError(error: String) {
                    progressDialog.dismissDialog()
                    CustomAlertDialog(this@OTPActivity)
                        .setTitle("Error")
                        .setMessage(error)
                        .setPositiveText("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setCancellable(false)
                        .show()
                }

                override fun onSuccess(success: PhoneAuthCredential) {
                    progressDialog.dismissDialog()
                }

                override fun onCodeSent() {
                    progressDialog.dismissDialog()
                }

                override fun onUserBlock(error: String) {
                    progressDialog.dismissDialog()
                    CustomAlertDialog(this@OTPActivity)
                        .setTitle("Block Account")
                        .setMessage(error)
                        .setPositiveText("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setCancellable(false)
                        .show()
                }

                override fun onTimeOut(error: String) {
                    hideDialog()
                    Log.d("Firebase", "onCodeAutoRetrievalTimeOut: $error")
                    errorAlertDialog("Time out", error)
                }

            })
    }


    private fun isValid(): Boolean =
        mViewDataBinding.otpCode.isValid


    override fun onValidated() {
        hideKeyboard()
//        onSubmit()
        if (isValid()) {
            showDialog()
            FirebaseLoginHandler.otpCheckCredentials(
                mViewDataBinding.otpCode.text,
                mAuth,
                this@OTPActivity,
                object : FirebaseOTPListener {
                    override fun onError(error: String) {
                        hideDialog()
                        errorAlertDialog("Invalid", error)
                    }

                    override fun onSuccess(uid: String) {
                        otpViewModel.authenticateLogin()
                    }

                    override fun onInValidOTPCode(error: String) {
                        hideDialog()
                        errorAlertDialog("Invalid OTP", error)
                    }

                    override fun onBlock(error: String) {
                        hideDialog()
                        errorAlertDialog("Account Block", error)
                    }

                }
            )
        }
    }

    override fun onBackPress() {
        onBackPressed()
    }

    override fun onSubmit() {
        gotoClass(RegisterActivity::class.java)
    }

    private fun gotoClass(cls: Class<*>) {
        openNewActivity(this, cls, true)
    }

    private fun gotoClass(cls: Class<*>, bundle: Bundle) {
        openNewActivity(this, cls, bundle, true)
    }


    private fun otpObserver() {
        otpViewModel.otpResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    preferenceHelper.setValue(PrefConstant.AUTH_TOKEN, "")

                }
                Status.SUCCESS -> {
                    progressDialog.dismissDialog()
                    val response = Gson().fromJson<BaseData<Authentications>>(
                        it.data,
                        object : TypeToken<BaseData<Authentications>>() {}.type
                    )

                    preferenceHelper.setValue(PrefConstant.AUTH_TOKEN, response.data.apiToken)
//                    preferenceHelper.setValue(PrefConstant.TYPE, response.data.type)
                    preferenceHelper.setValue(PrefConstant.IDENTITY, response.data.identity)

                    if (response.data.isProvisional == "1") {
                        onSubmit()
                    }  else {
                        otpViewModel.postDeviceToken()
                        gotoClass(DashboardActivity::class.java)
                    }

                }
                Status.ERROR -> {
                    progressDialog.dismissDialog()
                    errorAlertDialog(it.title, it.message)
                    preferenceHelper.setValue(PrefConstant.AUTH_TOKEN, "")

                }
            }
        }
    }

    private val resend = "Didn't get code"
    private val countDownTimer: CountDownTimer? = object : CountDownTimer(5000, 1000) {
        override fun onTick(p0: Long) {

            val times = p0 / 1000
            val ds = if (times >= 10) {
                times
            } else {
                "0$times"
            }
            val timer = "Expires in 00:$ds seconds"
            mViewDataBinding.didntGetCode.text = timer
            mViewDataBinding.resendCode.isEnabled = false
        }

        override fun onFinish() {
            mViewDataBinding.resendCode.isEnabled = true
            mViewDataBinding.didntGetCode.text = resend

        }

    }
}