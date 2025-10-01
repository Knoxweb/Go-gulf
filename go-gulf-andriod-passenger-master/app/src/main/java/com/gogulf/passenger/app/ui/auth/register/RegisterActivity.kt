package com.gogulf.passenger.app.ui.auth.register

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ViewDataBinding
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.data.apidata.APIConstants
import com.gogulf.passenger.app.data.model.auths.RegisterModel
import com.gogulf.passenger.app.data.model.base.BaseData
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.utils.enums.DefaultInputType
import com.gogulf.passenger.app.utils.enums.Status
import com.gogulf.passenger.app.utils.objects.Constants
import com.gogulf.passenger.app.utils.objects.ImagePicker
import com.gogulf.passenger.app.utils.objects.ImagePicker.getPickImageChooserIntent
import com.gogulf.passenger.app.utils.objects.ImagePicker.getPickImageResultUri
import com.gogulf.passenger.app.utils.objects.ImagePicker.getResizedBitmap
import com.gogulf.passenger.app.utils.objects.PermissionCheckApp
import com.gogulf.passenger.app.utils.objects.PrefConstant
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.gogulf.passenger.app.ui.legals.LegalActivity
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.databinding.ActivityRegisterScreenBinding
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.IOException

class RegisterActivity : BaseActivity<ActivityRegisterScreenBinding>(), BaseNavigation {
    private lateinit var mViewDataBinding: ActivityRegisterScreenBinding
    private val TAG = "RegisterActivity"
    private val registerViewModel: RegisterVM by viewModel()
    private var isImageSelected = false
    private var encodedImage = ""


    private lateinit var myBitmap: Bitmap
    private lateinit var picUri: Uri


    override fun getLayoutId(): Int = R.layout.activity_register_screen

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityRegisterScreenBinding
        mViewDataBinding.toolbarViewModel = commonViewModel
        commonViewModel.navigator = this
        mViewDataBinding.fullname.setHintVisible(View.VISIBLE)
        mViewDataBinding.email.setHintVisible(View.VISIBLE)
        mViewDataBinding.userPhoto.setOnClickListener {
            checkPhotoPermission()
        }
        inits()
        getProfileUpdateObserver()

    }


    private fun validations(): Boolean {
        return if (mViewDataBinding.fullname.isValid("Full name")) {
            false
        } else if (mViewDataBinding.email.isValid("Email")) {
            false
        } else if (!mViewDataBinding.email.isValidEmail) {
            mViewDataBinding.email.setErrorText("Invalid email")
            false
        } else true
    }

    private fun inits() {
        mViewDataBinding.fullname.setInputType(DefaultInputType.FullName)
        mViewDataBinding.email.setInputType(DefaultInputType.Email)
    }


    private fun checkPhotoPermission() {
        if (!PermissionCheckApp.checkPermissionMedia(this@RegisterActivity)) {
            PermissionCheckApp.requestPermissionCamera(this@RegisterActivity)
        } else {
            activityResult.launch(getPickImageChooserIntent(this@RegisterActivity))
        }
    }

    private var activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            var bitmap: Bitmap? = null
            Log.e(TAG, "on activity Result ${result!!}")
            if (result.resultCode == Activity.RESULT_OK) {

                if (getPickImageResultUri(this@RegisterActivity, result.data) != null) {
                    picUri = getPickImageResultUri(this@RegisterActivity, result.data)!!

                    try {
                        myBitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, picUri)
                        myBitmap = getResizedBitmap(myBitmap, 500)

                        mViewDataBinding.userPhoto.setImageBitmap(myBitmap)
                        try {
                            isImageSelected = true
                            encodedImage = ImagePicker.encodeImage(myBitmap)

                        } catch (e: Exception) {
                            isImageSelected = false
                            e.printStackTrace();
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {

                    val photo = result.data?.extras?.get("data") as Bitmap
                    mViewDataBinding.userPhoto.setImageBitmap(photo)
//                    picUri = getImageUri(this, photo!!)!!
                    try {
                        isImageSelected = true
                        encodedImage = ImagePicker.encodeImage(photo)
                    } catch (e: Exception) {
                        isImageSelected = false
                        e.printStackTrace();
                    }


                }

            }
        }

    private fun checkValidations(){
        if (validations()) {
            val jsonObject = JsonObject()
            jsonObject.addProperty(
                "name",
                mViewDataBinding.fullname.text
            )
            jsonObject.addProperty("email", mViewDataBinding.email.text)
            if (isImageSelected)
                jsonObject.addProperty("image", "${Constants.IMAGE_64}$encodedImage")
//            jsonObject.addProperty("type", getAccountType())

            jsonObject.addProperty(
                "uid",
                preferenceHelper.getValue(PrefConstant.FIREBASE_UID, APIConstants.TestUI) as String
            )
            jsonObject.addProperty(
                "phone_cc",
                preferenceHelper.getValue(PrefConstant.M_CC, APIConstants.TestCountry) as String
            )
            jsonObject.addProperty(
                "phone",
                preferenceHelper.getValue(PrefConstant.MOBILE, APIConstants.TestNumber) as String
            )

//            preferenceHelper.setValue(PrefConstant.TYPE, getAccountType())
            registerViewModel.registerUser(jsonObject)
        }
    }
    override fun onValidated() {
     checkValidations()
//        onSubmit()
    }

    override fun onBackPress() {
        onBackPressed()
    }

    override fun onSubmit() {
        val bundle = Bundle()
        bundle.putString(IntentConstant.TITLE, "Terms & Conditions")
        gotoClass(LegalActivity::class.java, bundle)
//        gotoClass(DashboardActivity::class.java)
    }


    private fun gotoClass(cls: Class<*>) {
        openNewActivity(this, cls, true)
    }

    private fun gotoClass(cls: Class<*>, bundle: Bundle) {
        openNewActivity(this, cls, bundle, true)
    }


    private fun getProfileUpdateObserver() {
        registerViewModel.profileUpdateResponse.observe(this) {
            when (it.status) {
                Status.ERROR -> {
                    progressDialog.dismissDialog()
                    errorAlertDialog(it.title, it.message)
                    preferenceHelper.setValue(PrefConstant.AUTH_TOKEN, "")
                }
                Status.SUCCESS -> {
                    progressDialog.dismissDialog()
                    val response = Gson().fromJson<BaseData<RegisterModel>>(
                        it.data,
                        object : TypeToken<BaseData<RegisterModel>>() {}.type
                    )

                    preferenceHelper.setValue(PrefConstant.AUTH_TOKEN, response.data.apiToken)
//                    preferenceHelper.setValue(PrefConstant.TYPE, response.data.type)
                    preferenceHelper.setValue(PrefConstant.IDENTITY, response.data.identity)
                   /* CustomAlertDialog(this)
                        .setTitle(response.title)
                        .setMessage(response.message)
                        .setPositiveText("OK") { dialog, _ ->
                            dialog.dismiss()
                            onSubmit()
                        }
                        .setCancellable(false)
                        .show()*/
                    onSubmit()

                    registerViewModel.postDeviceToken()

                }
                Status.LOADING -> {
                    progressDialog.show()
                    preferenceHelper.setValue(PrefConstant.AUTH_TOKEN, "")

                }
            }
        }
    }

}