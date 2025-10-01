package com.gogulf.passenger.app.ui.auth.loginwithemail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.gogulf.passenger.app.ui.auth.forgotpassword.ForgotPasswordActivity
import com.gogulf.passenger.app.ui.auth.passengerregister.PassengerRegisterActivity
import com.gogulf.passenger.app.ui.auth.register.RegisterActivity
import com.gogulf.passenger.app.ui.getaridev2.GetARideActivityV2
import com.gogulf.passenger.app.utils.PrefEntity
import com.gogulf.passenger.app.utils.Preferences
import com.gogulf.passenger.app.utils.PreferencesAction
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import com.gogulf.passenger.app.utils.logDeviceToCrashlytics
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityLoginWithEmailBinding
import kotlinx.coroutines.launch

class LoginWithEmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginWithEmailBinding
    private lateinit var viewModel: LoginWithEmailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
        DataBindingUtil.setContentView<ActivityLoginWithEmailBinding>(
            this, R.layout.activity_login_with_email
        ).let {
            binding = it
            binding.lifecycleOwner = this
            viewModel = ViewModelProvider(this)[LoginWithEmailViewModel::class.java]
            binding.viewModel = viewModel.also { viewmodel ->
                val emailCallBack = object : Observable.OnPropertyChangedCallback() {
                    override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                        viewmodel.updateEmail()
                    }
                }
                val passwordCallBack = object : Observable.OnPropertyChangedCallback() {
                    override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                        viewmodel.updatePassword()
                    }
                }


                viewmodel.email.addOnPropertyChangedCallback(emailCallBack)
                viewmodel.password.addOnPropertyChangedCallback(passwordCallBack)
            }
            viewModel.customLoader = CustomLoader(this@LoginWithEmailActivity)
        }



        binding.btnBack.setOnClickListener {
            finish()
        }


        binding.btnContinue.setOnClickListener {
            viewModel.hitEmailLogin()
        }
        binding.btnForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            if (!viewModel.email.get().isNullOrEmpty()) {
                intent.putExtra("email_address", viewModel.email.get())
            }
            startActivity(intent)
        }
        binding.btnCreateAccount.setOnClickListener {
            val intent = Intent(this, PassengerRegisterActivity::class.java)
            startActivity(intent)
        }



        lifecycleScope.launch {
            viewModel.uiState.collect {
                if (it.isLoading) {
                    viewModel.customLoader?.show()
                } else {
                    viewModel.customLoader?.dismiss()
                }
                if (it.error != null) {
                    CustomAlertDialog(this@LoginWithEmailActivity).setTitle(it.error.title)
                        .setMessage(it.error.message)
                        .setPositiveText("OK") { dialog, _ -> dialog.dismiss() }
                        .setCancellable(false).show()
                    viewModel.clearError()
                }
                if (it.isLoginSuccess) {
                    if (it.loginWithEmailResponseData != null) {
                        Preferences.setPreference(
                            this@LoginWithEmailActivity,
                            PrefEntity.FIREBASE_AUTH_TOKEN,
                            it.loginWithEmailResponseData.firebaseAuthToken ?: ""
                        )
                    }
                    if (it.isFirebaseLoginSuccess) {
                        if (it.isNumberLoginSuccess) {
                            val response = viewModel.uiState.value.passengerLoginResponseData
                            if (response != null) {

                                logDeviceToCrashlytics()


                                PreferencesAction().setLoginPreferences(
                                    this@LoginWithEmailActivity, response
                                )

                                if (!response.profileStatus.isNullOrEmpty()) {
                                    if (response.profileStatus == "new") {
                                        val intent = Intent(
                                            this@LoginWithEmailActivity,
                                            RegisterActivity::class.java
                                        )
                                        startActivity(intent)
                                        finishAffinity()
                                    } else {
                                        val intent = Intent(
                                            this@LoginWithEmailActivity,
                                            GetARideActivityV2::class.java
                                        )
                                        startActivity(intent)
                                        finishAffinity()
                                    }
                                }
                            }

                        }
                    }
                }


            }
        }

    }

}