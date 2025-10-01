package com.gogulf.passenger.app.ui.auth.forgotpassword.changepassword

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.gogulf.passenger.app.ui.auth.loginwithemail.LoginWithEmailActivity
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityChangePasswordBinding
import kotlinx.coroutines.launch

class ChangePasswordActivity : AppCompatActivity() {


    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var viewModel: ChangePasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
        DataBindingUtil.setContentView<ActivityChangePasswordBinding>(this, R.layout.activity_change_password).let {
            binding = it
            viewModel = ViewModelProvider(this)[ChangePasswordViewModel::class.java]
            viewModel.otp = intent.getStringExtra("otp")
            viewModel.email = intent.getStringExtra("email_address")
            viewModel.customLoader = CustomLoader(this)
            binding.viewModel = viewModel
            binding.lifecycleOwner = this
        }
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        lifecycleScope.launch {
            viewModel.uiState.collect{
                if (it.isLoading) {
                    viewModel.customLoader?.show()
                } else {
                    viewModel.customLoader?.dismiss()
                }

                if (it.isSuccess) {
                    val intent = Intent(this@ChangePasswordActivity, LoginWithEmailActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }

        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnContinue.setOnClickListener {
            if (checkValidInput()) {
                viewModel.hitForgotPasswordReset()
            }
        }


    }
    private fun checkValidInput(): Boolean {
        var returnBoolean = true

        if (binding.etNewPassword.getText().trim().isEmpty()) {
            binding.etNewPassword.setError("New password cannot be empty")
            returnBoolean = false

        }
        if (binding.etConfirmPassword.getText().trim().isEmpty()) {
            binding.etConfirmPassword.setError("Confirm password cannot be empty")
            returnBoolean = false
        }

        if (binding.etNewPassword.getText().trim()
                .isNotEmpty() && binding.etConfirmPassword.getText().trim().isNotEmpty()
        ) {
            if (binding.etNewPassword.getText().trim() != binding.etConfirmPassword.getText()
                    .trim()
            ) {
                binding.etConfirmPassword.setError("Confirm password should be same as new password")
                returnBoolean = false
            }
        }
        return returnBoolean

    }
}
