package com.gogulf.passenger.app.ui.auth.otpv2

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.gogulf.passenger.app.App.Companion.preferenceHelper
import com.gogulf.passenger.app.data.apidata.APIConstants
import com.gogulf.passenger.app.ui.auth.registerv2.RegisterActivityV2
import com.gogulf.passenger.app.ui.getaridev2.GetARideActivityV2
import com.gogulf.passenger.app.utils.PreferencesAction
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import com.gogulf.passenger.app.utils.logDeviceToCrashlytics
import com.gogulf.passenger.app.utils.objects.PrefConstant
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityOtpv2Binding
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class OTPActivityV2 : AppCompatActivity() {
    private lateinit var binding: ActivityOtpv2Binding
    private lateinit var viewModel: OtpNewViewModel
//    private val viewModel: OTPV2ViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
        DataBindingUtil.setContentView<ActivityOtpv2Binding>(this, R.layout.activity_otpv2).also {
            binding = it
            it.lifecycleOwner = this
            viewModel = ViewModelProvider(this)[OtpNewViewModel::class.java]
            binding.viewModel = viewModel
            viewModel.customLoader = CustomLoader(this)
            binding.activity = this
            binding.userInformationText.text =
                buildString {
                    append(getString(R.string.enter_digit_otp))
                    append(" ")
                    append(
                        preferenceHelper.getValue(
                            PrefConstant.MOBILE_NO, APIConstants.TestNumber
                        )
                    )
                }
        }

        viewModel.phoneNumber = intent.getStringExtra("phoneNumber").toString()

        viewModel.verificationIdMy = intent.getStringExtra("verificationId")

        viewModel.customLoader = CustomLoader(this)

        binding.firstPinView.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.firstPinView.setOnEditorActionListener(object :
            TextView.OnEditorActionListener {
            override fun onEditorAction(
                p0: TextView?,
                actionId: Int,
                keyEvent: KeyEvent?
            ): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onDoneClicked()
                    return true
                }
                return false
            }

        })

        lifecycleScope.launch {
            viewModel.uiState.collect {
                if (it.isUILoading) {
                    viewModel.customLoader?.show()
                } else {
                    viewModel.customLoader?.dismiss()
                }
                if (it.errorMessage != null) {
                    CustomAlertDialog(this@OTPActivityV2).setTitle(it.errorMessage.title)
                        .setMessage(it.errorMessage.message)
                        .setPositiveText("OK") { dialog, _ -> dialog.dismiss() }
                        .setCancellable(false).show()
                    viewModel.clearError()
                    preferenceHelper.setValue(PrefConstant.AUTH_TOKEN, "")

                }
            }
        }
        binding.btnResend.visibility = View.GONE
        viewModel.startTimer()

        viewModel.currentTime.observe(this) {
            if (viewModel.isFinished.value == false) {
                binding.didntGetCode.text =
                    getString(R.string.expires_in_code, String.format("%02d:%02d", 0, it))
            } else {
                binding.didntGetCode.text = getString(R.string.didn_t_receive_the_code)
            }
        }

        viewModel.isFinished.observe(this) {
            if (it) {
                binding.btnResend.visibility = View.VISIBLE
                binding.didntGetCode.text = getString(R.string.didn_t_receive_the_code)
            } else {
                binding.btnResend.visibility = View.GONE
            }
        }

        binding.btnResend.setOnClickListener {
            viewModel.resetValues()
            viewModel.resendOTP(this)
        }

//        binding.btnContinue.setOnClickListener {
//
////            val intent = Intent(this, RegisterActivity::class.java)
////            startActivity(intent)
//        }
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        val margin = resources.getDimensionPixelSize(R.dimen.margin_16dp) * 2


        val itemCount = 6
        val itemSpacing =
            resources.getDimensionPixelSize(R.dimen.pin_view_item_spacing) // use your spacing dimension
        val totalSpacing = itemSpacing * (itemCount - 1)
        val itemWidth = (screenWidth - margin - totalSpacing) / itemCount

        binding.firstPinView.setItemWidth(itemWidth)
        viewModel.data.observe(this) {
            it?.let {
                PreferencesAction().setLoginPreferences(this, it)
                if (!it.profileStatus.isNullOrEmpty()) {
                    logDeviceToCrashlytics()

                    if (it.profileStatus == "new") {

                        val intent = Intent(this, RegisterActivityV2::class.java)
                        startActivity(intent)
                        finishAffinity()
                    } else {
                        val intent = Intent(this, GetARideActivityV2::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                }
            }
        }

    }

    fun onDoneClicked() {
        viewModel.appendOTP(binding.firstPinView.text.toString())
    }
}