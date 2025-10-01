package com.gogulf.passenger.app.ui.auth.forgotpassword

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.gogulf.passenger.app.ui.auth.forgotpassword.otp.ForgotOtpActivity
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityForgotPasswordBinding
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import kotlinx.coroutines.launch

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var viewModel: ForgotPasswordViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)

        DataBindingUtil.setContentView<ActivityForgotPasswordBinding>(
            this, R.layout.activity_forgot_password
        ).let {
            binding = it
            binding.lifecycleOwner = this
            viewModel = ViewModelProvider(this)[ForgotPasswordViewModel::class.java]
            binding.viewModel = viewModel.also { viewmodel ->
                val emailCallBack =
                    object : androidx.databinding.Observable.OnPropertyChangedCallback() {
                        override fun onPropertyChanged(
                            sender: androidx.databinding.Observable?, propertyId: kotlin.Int
                        ) {
                            viewmodel.updateEmail()
                        }
                    }
                viewmodel.email.addOnPropertyChangedCallback(emailCallBack)
            }
            viewModel.customLoader = CustomLoader(this@ForgotPasswordActivity)
        }


        intent.getStringExtra("email_address")?.let {
            binding.etEmail.setText(it)
            viewModel.email.set(it)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnContinue.setOnClickListener {
            viewModel.hitForgotPassword()
        }

        lifecycleScope.launch {
            viewModel.uiState.collect { it ->
                if (it.isLoading) {
                    viewModel.customLoader?.show()
                } else {
                    viewModel.customLoader?.dismiss()
                }
                if (it.error != null) {
                    CustomAlertDialog(this@ForgotPasswordActivity).setTitle(it.error.title)
                        .setMessage(it.error.message)
                        .setPositiveText("OK") { dialog, _ -> dialog.dismiss() }
                        .setCancellable(false).show()
                    viewModel.clearError()
                }

                if (it.isSuccess) {
                    viewModel.clearSuccess()
                    if (it.messageResponseData != null) {

                        it.messageResponseData.status?.let { it2 ->
                            if (it2) {
                                val intent = Intent(this@ForgotPasswordActivity, ForgotOtpActivity::class.java)
                                intent.putExtra("email_address", viewModel.email.get())
                                startActivity(intent)

                            } else {
                            }
                        }
                    }
                }


            }
        }
    }
}