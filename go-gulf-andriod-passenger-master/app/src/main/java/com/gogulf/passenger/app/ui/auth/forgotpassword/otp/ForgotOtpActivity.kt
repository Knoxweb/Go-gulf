package com.gogulf.passenger.app.ui.auth.forgotpassword.otp

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
import com.gogulf.passenger.app.ui.auth.forgotpassword.changepassword.ChangePasswordActivity
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityOtp2Binding
import kotlinx.coroutines.launch

class ForgotOtpActivity : AppCompatActivity() {


    private lateinit var binding: ActivityOtp2Binding
    private lateinit var viewModel: ForgotOtpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.setStatusBarColor(this)

        DataBindingUtil.setContentView<ActivityOtp2Binding>(this, R.layout.activity_otp2).let {
            binding = it
            it.lifecycleOwner = this
            binding.activity = this
            viewModel = ViewModelProvider(this)[ForgotOtpViewModel::class.java]

            viewModel.email = intent.getStringExtra("email_address")
            binding.viewModel = viewModel
            binding.userInformationText.text= "${getString(R.string.enter_digit_otp)} ${viewModel.email}"
        }

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

        var shouldAdd = HashMap<String, Boolean>()

        lifecycleScope.launch {
            viewModel.uiState.collect {
                if (it.isUILoading) {
                    viewModel.customLoader?.show()
                } else {
                    viewModel.customLoader?.dismiss()
                }
                if (it.errorMessage != null) {
                    CustomAlertDialog(this@ForgotOtpActivity).setTitle(it.errorMessage.title)
                        .setMessage(it.errorMessage.message)
                        .setPositiveText("OK") { dialog, _ -> dialog.dismiss() }
                        .setCancellable(false).show()
                    viewModel.clearError()
                }
                if (it.isSuccess) {
                    val intent = Intent(this@ForgotOtpActivity, ChangePasswordActivity::class.java)
                    intent.putExtra("email_address", viewModel.email)
                    intent.putExtra("otp", viewModel.uiState.value.otp)
                    startActivity(intent)
                    viewModel.clearIsSuccess()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.resend.collect {

                if (it.messageResponseData != null) {


//                    it.messageResponseData.status?.let { it2 ->
//                        if (it2) {
//                            val intent = Intent(this@ForgotPasswordActivity, ForgotOtpActivity::class.java)
//                            startActivity(intent)
//
//                        } else {
//                        }
//                    }
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
            viewModel.hitForgotPassword()
        }


        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        val margin = resources.getDimensionPixelSize(R.dimen.margin_16dp) * 2


        val itemCount = 6
        val itemSpacing =
            resources.getDimensionPixelSize(R.dimen.pin_view_item_spacing) // use your spacing dimension
        val totalSpacing = itemSpacing * (itemCount - 1)
        val itemWidth = (screenWidth - margin - totalSpacing) / itemCount

        binding.firstPinView.setItemWidth(itemWidth)


    }

    fun onDoneClicked() {
        viewModel.appendOTP(binding.firstPinView.text.toString())
    }
}