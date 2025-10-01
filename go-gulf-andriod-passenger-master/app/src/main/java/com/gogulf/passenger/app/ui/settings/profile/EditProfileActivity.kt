package com.gogulf.passenger.app.ui.settings.profile

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.enums.DefaultInputType
import com.gogulf.passenger.app.utils.enums.Status
import com.google.gson.JsonObject
import com.gogulf.passenger.app.data.model.ProfileResponseData
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.databinding.ActivityProfileEditScreenBinding
import org.koin.android.viewmodel.ext.android.viewModel

class EditProfileActivity : BaseActivity<ActivityProfileEditScreenBinding>(), BaseNavigation {

    private lateinit var mViewDataBinding: ActivityProfileEditScreenBinding

    private val editProfileVM: EditProfileVM by viewModel()
    override fun getLayoutId(): Int = R.layout.activity_profile_edit_screen
    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityProfileEditScreenBinding
        mViewDataBinding.toolbarViewModel = commonViewModel
        mViewDataBinding.activity = this
        commonViewModel.navigator = this

        initials()
        getProfileObserver()
        getProfileUpdateObserver()

    }

    private fun initials() {
        mViewDataBinding.nameET.setHintVisible(View.VISIBLE)
        mViewDataBinding.emailET.setHintVisible(View.VISIBLE)
        mViewDataBinding.phoneET.setHintVisible(View.VISIBLE)

        mViewDataBinding.nameET.setInputType(DefaultInputType.FullName)
        mViewDataBinding.emailET.setInputType(DefaultInputType.Email)
        mViewDataBinding.phoneET.setInputType(DefaultInputType.Number)

        mViewDataBinding.phoneET.setFocus(false)

    }


    private fun getProfileObserver() {
        editProfileVM.profileResponse.observe(this) {
            when (it.status) {
                Status.ERROR -> {
                    progressDialog.dismissDialog()
                    CustomAlertDialog(this).setTitle(it.title).setMessage(it.message)
                        .setPositiveText("RETRY") { dialog, _ ->
                            dialog.dismiss()
                            editProfileVM.getProfile()
                        }.setNegativeText("GO BACK") { dialog, _ ->
                            dialog.dismiss()
                            onBackPress()
                        }.setCancellable(false).show()

                }

                Status.SUCCESS -> {
                    progressDialog.dismissDialog()
//                    val response = Gson().fromJson<BaseData<ProfileModel>>(
//                        it.data,
//                        object : TypeToken<BaseData<ProfileModel>>() {}.type
//                    )
                    val response = it.data!!
                    updateViews(response)
                }

                Status.LOADING -> {
                    progressDialog.show()

                }
            }
        }

    }

    private fun personalValidation(): Boolean {
        return if (mViewDataBinding.nameET.isValid("Name")) false
        else if (mViewDataBinding.emailET.isValid("Email")) false
        else if (!mViewDataBinding.emailET.isValidEmail) {
            mViewDataBinding.emailET.setErrorText("Invalid email address")
            false
        } else true
    }

    private fun updateViews(profileModel: ProfileResponseData) {
        mViewDataBinding.nameET.text = profileModel.name
        mViewDataBinding.emailET.text = profileModel.email
        mViewDataBinding.phoneET.text = profileModel.mobile
        mViewDataBinding.phoneET.setFocus(false)
    }


    override fun onValidated() {
        if (personalValidation()) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("name", mViewDataBinding.nameET.text)
            jsonObject.addProperty("email", mViewDataBinding.emailET.text)
            editProfileVM.updateProfilePersonal(jsonObject)
        }
    }

    override fun onBackPress() {
        onBackPressed()
    }

    override fun onSubmit() {
        onBackPress()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
    }


    private fun getProfileUpdateObserver() {
        editProfileVM.profileUpdateResponse.observe(this) {
            when (it.status) {
                Status.ERROR -> {
                    progressDialog.dismissDialog()
                    errorAlertDialog(it.title, it.message)
                }

                Status.SUCCESS -> {
                    progressDialog.dismissDialog()
//                    val response = Gson().fromJson<BaseData<ProfileModel>>(
//                        it.data,
//                        object : TypeToken<BaseData<ProfileModel>>() {}.type
//                    )
//                    preferenceHelper.setValue(PrefConstant.FULL_NAME, response.data.name)
//                    App.passengerName.postValue(response.data.name)
//                    preferenceHelper.setValue(PrefConstant.IMAGE, response.data.imageLink)

                    CustomAlertDialog(this).setTitle(it.data?.title).setMessage(it.data?.message)
                        .setPositiveText("OK") { dialog, _ ->
                            dialog.dismiss()
                            finish()
                        }.setCancellable(false).show()


                }

                Status.LOADING -> {
                    progressDialog.show()

                }
            }
        }
    }

}