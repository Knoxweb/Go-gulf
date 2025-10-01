package com.gogulf.passenger.app.ui.auth.passengerregister

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.dhaval2404.imagepicker.ImagePicker
import com.gogulf.passenger.app.ui.auth.login.CountrySelectBottomSheet
import com.gogulf.passenger.app.ui.auth.registerv2.RegisterActivityV2
import com.gogulf.passenger.app.ui.getaridev2.GetARideActivityV2
import com.gogulf.passenger.app.utils.CommonUtils
import com.gogulf.passenger.app.utils.PrefEntity
import com.gogulf.passenger.app.utils.Preferences
import com.gogulf.passenger.app.utils.PreferencesAction
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import com.gogulf.passenger.app.utils.logDeviceToCrashlytics
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityPassengerRegisterBinding
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.regex.Pattern

class PassengerRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPassengerRegisterBinding
    private lateinit var viewModel: PassengerRegisterViewModel

    private val EMAIL_BEFORE_AMP_PATTERN: Pattern = Pattern.compile(
        "^.*[a-zA-Z]+.*$"
    )

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                assert(data != null)
                val uri = data!!.data
                viewModel.capturedProfilePicURI = uri
                try {
                    // Convert the image to Base64 using CommonUtils
                    val base64String =
                        uri?.let { CommonUtils.encodeImageToBase64(this@PassengerRegisterActivity, it) }
                    if (base64String != null) {
                        viewModel.profileImageBase64 = base64String
                    }
                    viewModel.path = CommonUtils.getRealPathFromURI(
                        viewModel.capturedProfilePicURI!!, this@PassengerRegisterActivity
                    )
                    binding.userPhoto.setImageURI(Uri.parse(viewModel.path!!))

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                // Handle error
            } else {
                // Handle other cases
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
        DataBindingUtil.setContentView<ActivityPassengerRegisterBinding>(
            this, R.layout.activity_passenger_register
        ).let {
            binding = it
            binding.lifecycleOwner = this
            binding.activity = this
            viewModel = ViewModelProvider(this)[PassengerRegisterViewModel::class.java]
            binding.viewModel = viewModel.also { viewmodel ->

                val passwordCallBack = object : Observable.OnPropertyChangedCallback() {
                    override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                        viewmodel.updatePassword()
                    }
                }
                val firstNameCallBack = object : Observable.OnPropertyChangedCallback() {
                    override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                        viewmodel.updateFirstName()
                    }
                }
                val lastNameCallBack = object : Observable.OnPropertyChangedCallback() {
                    override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                        viewmodel.updateLastName()
                    }
                }
                val emailCallBack = object : Observable.OnPropertyChangedCallback() {
                    override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                        viewmodel.updateEmail()
                    }
                }
                val confirmPasswordCallBack = object : Observable.OnPropertyChangedCallback() {
                    override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                        viewmodel.updateConfirmPassword()
                    }
                }
                val phoneNumberCallback = object : Observable.OnPropertyChangedCallback() {
                    override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                        viewmodel.updatePhoneNumber()
                    }
                }

                viewmodel.phoneNumber.addOnPropertyChangedCallback(phoneNumberCallback)
                viewmodel.firstName.addOnPropertyChangedCallback(firstNameCallBack)
                viewmodel.lastName.addOnPropertyChangedCallback(lastNameCallBack)
                viewmodel.email.addOnPropertyChangedCallback(emailCallBack)
                viewmodel.password.addOnPropertyChangedCallback(passwordCallBack)
                viewmodel.confirmPassword.addOnPropertyChangedCallback(confirmPasswordCallBack)
            }
            viewModel.customLoader = CustomLoader(this@PassengerRegisterActivity)
        }


        binding.btnChooseCountry.setOnClickListener {
            val modal = CountrySelectBottomSheet(passengerRegisterViewModel = viewModel)
            supportFragmentManager.let { modal.show(it, CountrySelectBottomSheet.TAG) }
        }

        viewModel.getPhoneNumberPattern.observe(this) {
//            viewModel.pattern?.let {
//                binding.enterPhoneNumber.removeTextChangedListener(it)
//            }
//            viewModel.pattern = PatternedTextWatcher.Builder(it).respectPatternLength(false).build()
//            binding.enterPhoneNumber.addTextChangedListener(viewModel.pattern)
        }
        binding.btnUploadPhoto.setOnClickListener {
            CustomAlertDialog(
                this
            ).setTitle("Choose From")
                .setMessage("Choose to capture a new photo or Select one from your gallery")
                .setPositiveText("Capture") { dialog, _ ->
                    ImagePicker.with(this@PassengerRegisterActivity).crop().maxResultSize(620, 620)
                        .cameraOnly().cropSquare().compress(512).galleryMimeTypes(
                            mimeTypes = arrayOf(
                                "image/png", "image/jpg", "image/jpeg"
                            )
                        ).createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }
                }.setNegativeText("Gallery") { dialog, _ ->
                    ImagePicker.with(this@PassengerRegisterActivity).crop().maxResultSize(620, 620)
                        .galleryOnly().cropSquare().compress(512).galleryMimeTypes(
                            mimeTypes = arrayOf(
                                "image/png", "image/jpg", "image/jpeg"
                            )
                        ).createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }
                }.show()
        }



        lifecycleScope.launch {
            viewModel.uiState.collect {
                if (it.isLoading) {
                    viewModel.customLoader?.show()
                } else {
                    viewModel.customLoader?.dismiss()
                }
                if (it.error != null) {
                    CustomAlertDialog(this@PassengerRegisterActivity).setTitle(it.error.title)
                        .setMessage(it.error.message)
                        .setPositiveText("OK") { dialog, _ -> dialog.dismiss() }
                        .setCancellable(false).show()

                    viewModel.clearError()
                }
                if (it.isRegisterSuccess) {
                    if (it.registerResponseData != null) {
                        Preferences.setPreference(
                            this@PassengerRegisterActivity,
                            PrefEntity.FIREBASE_AUTH_TOKEN,
                            it.registerResponseData.firebaseAuthToken ?: ""
                        )
                    }
                    if (it.isFirebaseLoginSuccess) {
                        if (it.isNumberLoginSuccess) {
                            val response = viewModel.uiState.value.passengerLoginResponseData
                            if (response != null) {

                                PreferencesAction().setLoginPreferences(
                                    this@PassengerRegisterActivity, response
                                )

                                if (!response.profileStatus.isNullOrEmpty()) {
                                    logDeviceToCrashlytics()

                                    if (response.profileStatus == "new") {
                                        val intent = Intent(
                                            this@PassengerRegisterActivity,
                                            RegisterActivityV2::class.java
                                        )
                                        startActivity(intent)
                                        finishAffinity()
                                    } else {
                                        val intent = Intent(
                                            this@PassengerRegisterActivity, GetARideActivityV2::class.java
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

//    private fun checkValidInput(): Boolean {
//        var returnBoolean = true
//
//        if (binding.etEmail.getText().trim().isEmpty()) {
//            binding.etEmail.setError("Email cannot be empty")
//            returnBoolean = false
//        }
//        if (binding.etEmail.getText().trim().isNotEmpty()) {
//            val generatedString: String = binding.etEmail.getText().substringBefore("@")
//            if (!EMAIL_BEFORE_AMP_PATTERN.matcher(generatedString).matches()) {
//                binding.etEmail.setError("Enter the valid email")
//                returnBoolean = false
//            }
//            if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText().trim()).matches()) {
//                binding.etEmail.setError("Enter the valid email")
//                returnBoolean = false
//
//            }
//        }
//        if (binding.etFirstName.getText().trim().isEmpty()) {
//            binding.etFirstName.setError("First name cannot be empty")
//            returnBoolean = false
//
//        }
//        if (binding.etLastName.getText().trim().isEmpty()) {
//            binding.etLastName.setError("Last name cannot be empty")
//            returnBoolean = false
//        }
//
//        if (binding.etPassword.getText().trim().isEmpty()) {
//            binding.etPassword.setError("New password cannot be empty")
//            returnBoolean = false
//
//        }
//        if (binding.etConfirmPassword.getText().trim().isEmpty()) {
//            binding.etConfirmPassword.setError("Confirm password cannot be empty")
//            returnBoolean = false
//        }
//
//        if (binding.etPassword.getText().trim().isNotEmpty() && binding.etConfirmPassword.getText()
//                .trim().isNotEmpty()
//        ) {
//            if (binding.etPassword.getText().trim() != binding.etConfirmPassword.getText().trim()) {
//                binding.etConfirmPassword.setError("Confirm password should be same as new password")
//                returnBoolean = false
//            }
//        }
//        return returnBoolean
//
//    }

}