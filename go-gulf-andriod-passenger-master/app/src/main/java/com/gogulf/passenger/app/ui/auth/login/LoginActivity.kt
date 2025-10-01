package com.gogulf.passenger.app.ui.auth.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.gogulf.passenger.app.ui.auth.otp.OTPActivity
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.utils.interfaces.FirebaseLoginListener
import com.gogulf.passenger.app.utils.objects.FirebaseLoginHandler
import com.gogulf.passenger.app.utils.objects.PrefConstant
import com.gogulf.passenger.app.BuildConfig
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityLoginScreenBinding

class LoginActivity : BaseActivity<ActivityLoginScreenBinding>(), BaseNavigation {
    private lateinit var mViewDataBinding: ActivityLoginScreenBinding
    private lateinit var mAuth: FirebaseAuth
    override fun getLayoutId(): Int = R.layout.activity_login_screen

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityLoginScreenBinding
        mViewDataBinding.toolbarViewModel = commonViewModel
        commonViewModel.navigator = this
        FirebaseApp.initializeApp(this)
        if (BuildConfig.DEBUG) {
            Firebase.appCheck.installAppCheckProviderFactory(
                DebugAppCheckProviderFactory.getInstance()
            )
        } else {
            Firebase.appCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance(),
            )

        }
        mAuth = FirebaseAuth.getInstance()

        mViewDataBinding.mobileNumber.addTextChangedListener(watcher)
        FirebaseLoginHandler.getFirebaseToken()
        //play integrity token initialize

        mViewDataBinding.mobileNumber.imeOptions = EditorInfo.IME_ACTION_GO
        mViewDataBinding.mobileNumber.setOnEditorActionListener(object :
            TextView.OnEditorActionListener {
            override fun onEditorAction(
                p0: TextView?,
                actionId: Int,
                keyEvent: KeyEvent?
            ): Boolean {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    onValidated()
                    return true
                }
                return false
            }

        })


    }


    private fun getCountryCode(): String = "+${mViewDataBinding.countryPicker.selectedCountryCode}"
    private fun getMobile(): String =
        "+${mViewDataBinding.countryPicker.selectedCountryCode}${getText()}"

    private fun getText(): String = mViewDataBinding.mobileNumber.text.toString().trim()


    private fun isValid(): Boolean {
        return getText().length >= 9
    }

    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            mViewDataBinding.mobileNumber.error = null
        }

    }

    override fun onValidated() {
        if (isValid()) {
            showDialog()
            mViewDataBinding.mobileNumber.error = null
            FirebaseLoginHandler.loginFirebase(this@LoginActivity, getMobile(), mAuth,
                object : FirebaseLoginListener {
                    override fun onError(error: String) {
                        hideDialog()
                        Log.d("Firebase", "onCodeAutoRetrievalTimeOut: $error")
                        errorAlertDialog("Invalid", error)
                    }

                    override fun onSuccess(success: PhoneAuthCredential) {
                        hideDialog()
                    }

                    override fun onCodeSent() {
                        hideDialog()
                        onSubmit()
                    }

                    override fun onUserBlock(error: String) {
                        hideDialog()
                        errorAlertDialog("Account Block", error)
                    }

                    override fun onTimeOut(error: String) {
                        hideDialog()
                        Log.d("Firebase", "onCodeAutoRetrievalTimeOut: $error")
                        errorAlertDialog("Time out", error)
                    }
                })

        } else {
            mViewDataBinding.mobileNumber.error = "Invalid mobile number"
        }
    }

    override fun onBackPress() {
        onBackPressed()
    }

    override fun onSubmit() {
        preferenceHelper.setValue(PrefConstant.MOBILE, getText())
        preferenceHelper.setValue(
            PrefConstant.M_CC,
            getCountryCode()
        )
        preferenceHelper.setValue(
            PrefConstant.MOBILE_NO,
            getMobile()
        )
        gotoClass(OTPActivity::class.java)
    }

    private fun gotoClass(cls: Class<*>) {
        openNewActivity(this@LoginActivity, cls, false)
    }

    private fun gotoClass(cls: Class<*>, bundle: Bundle) {
        openNewActivity(this@LoginActivity, cls, bundle, false)
    }


}