package com.gogulf.passenger.app.ui.auth.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.gogulf.passenger.app.App.Companion.preferenceHelper
import com.gogulf.passenger.app.ui.auth.loginwithemail.LoginWithEmailActivity
import com.gogulf.passenger.app.ui.auth.otpv2.OTPActivityV2
import com.gogulf.passenger.app.utils.CommonUtils
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import com.gogulf.passenger.app.utils.objects.PrefConstant
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityLoginV2Binding
import kotlinx.coroutines.launch

class LoginActivityV2 : AppCompatActivity() {

    private lateinit var binding: ActivityLoginV2Binding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        SystemBarUtil.enableEdgeToEdge(this)
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityLoginV2Binding>(this, R.layout.activity_login_v2)
            .also {
                binding = it
                it.lifecycleOwner = this
                viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
                viewModel.customLoader = CustomLoader(this)
                binding.loginViewModel = viewModel
                binding.activity = this
                it.loginViewModel = viewModel.also { viewmodel ->
                    val phoneNumberCallback = object : Observable.OnPropertyChangedCallback() {
                        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                            viewmodel.updatePhoneNumber()
                        }
                    }
                    viewmodel.phoneNumber.addOnPropertyChangedCallback(phoneNumberCallback)
                }
            }

        binding.btnChooseCountry.setOnClickListener {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(
                binding.root.windowToken, 0, null
            )
            val modal = CountrySelectBottomSheet(viewModel)
            supportFragmentManager.let { modal.show(it, CountrySelectBottomSheet.TAG) }
        }

        CommonUtils.getNewFCMToken()

        binding.enterPhoneNumber.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.enterPhoneNumber.setOnEditorActionListener(object :
            TextView.OnEditorActionListener {
            override fun onEditorAction(
                p0: TextView?,
                actionId: Int,
                keyEvent: KeyEvent?
            ): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.sendOtp(this@LoginActivityV2)
                    return true
                }
                return false
            }

        })

        lifecycleScope.launch {
            viewModel.uiState.collect {
                if (it.isLoading) {
                    viewModel.customLoader?.show()
                } else {
                    viewModel.customLoader?.dismiss()
                }
                if (it.error != null) {
                    CustomAlertDialog(this@LoginActivityV2).setTitle(it.error.title)
                        .setMessage(it.error.message)
                        .setPositiveText("OK") { dialog, _ -> dialog.dismiss() }
                        .setCancellable(false).show()
                    viewModel.clearError()
                }
                if (it.isSuccess) {
                    val intent = Intent(this@LoginActivityV2, OTPActivityV2::class.java)
                    intent.putExtra("verificationId", it.otpResponseModel?.verificationId ?: "")
                    intent.putExtra(
                        "phoneNumber",
                        viewModel.uiState.value.countryModel?.dialCode + viewModel.uiState.value.phoneNumber.replace(
                            " ", ""
                        )
                    )
                    preferenceHelper.setValue(
                        PrefConstant.MOBILE, viewModel.uiState.value.phoneNumber.replace(
                            " ", ""
                        )
                    )
                    preferenceHelper.setValue(
                        PrefConstant.M_CC, viewModel.uiState.value.countryModel?.dialCode
                    )
                    preferenceHelper.setValue(
                        PrefConstant.MOBILE_NO,
                        viewModel.uiState.value.countryModel?.dialCode + viewModel.uiState.value.phoneNumber.replace(
                            " ", ""
                        )
                    )
                    startActivity(intent)
                    viewModel.resetIsSuccess()

                }

            }
        }

        binding.btnEmail.setOnClickListener {
            startActivity(Intent(this,LoginWithEmailActivity::class.java))

        }

    }
}