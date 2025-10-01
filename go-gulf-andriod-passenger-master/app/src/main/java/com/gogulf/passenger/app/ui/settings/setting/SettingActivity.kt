package com.gogulf.passenger.app.ui.settings.setting

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.data.internal.clearAll
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.legals.LegalActivity
import com.gogulf.passenger.app.ui.menu.MenuListAdapter
import com.gogulf.passenger.app.ui.menu.MenuModels
import com.gogulf.passenger.app.ui.settings.account.AccountActivity
import com.gogulf.passenger.app.ui.walkthrough.GetStartedActivity
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.enums.Status
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.utils.objects.ListUtilsClass
import com.gogulf.passenger.app.utils.objects.PrefConstant
import com.gogulf.passenger.app.databinding.ActivitySettingsBinding
import org.koin.android.viewmodel.ext.android.viewModel

class SettingActivity : BaseActivity<ActivitySettingsBinding>(), BaseNavigation {
    private lateinit var mViewDataBinding: ActivitySettingsBinding
    private var menuList = ArrayList<MenuModels>()
    private val settingViewModel: SettingVM by viewModel()

    private var mobile = ""
    private lateinit var adapters: MenuListAdapter
    override fun getLayoutId(): Int = R.layout.activity_settings

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivitySettingsBinding
        mViewDataBinding.toolbarViewModel = commonViewModel
        commonViewModel.navigator = this
        mobile = preferenceHelper.getValue(PrefConstant.MOBILE_NO, "") as String
        insertData()
        mViewDataBinding.menuContainer.menuRecyclerView.hasFixedSize()
        mViewDataBinding.menuContainer.menuRecyclerView.layoutManager =
            LinearLayoutManager(this@SettingActivity)
        adapters = MenuListAdapter(this@SettingActivity, menuList)
        mViewDataBinding.menuContainer.menuRecyclerView.adapter = adapters
        adapters.onMenuClick(object : MenuListAdapter.OnMenuClicked {
            override fun onClicked(menuModels: MenuModels) {


                when (menuModels.menuName) {
                    "Profile" -> {
                        gotoClass(AccountActivity::class.java)
                    }
                    "Privacy Policy" -> {
                        val bundle = Bundle()
                        bundle.putString(IntentConstant.TITLE, "Privacy Policy")
                        gotoClass(LegalActivity::class.java, bundle)
                    }
                    "Terms & Conditions" -> {
//                        val bundle = Bundle()
//                        bundle.putString(IntentConstant.TITLE, "Terms & Conditions")
//                        gotoClass(LegalActivity::class.java, bundle)

                        val bundle = Bundle()
                        bundle.putString(IntentConstant.TITLE, "Accept")
                        gotoClass(LegalActivity::class.java, bundle)
                    }

                }
            }

        })

        mViewDataBinding.logoutContainer.setOnClickListener {
//            logoutDialog()
            settingViewModel.logoutUser()
            logoutNavigation()
        }
        mViewDataBinding.deleteContainer.setOnClickListener {
            deleteDialog()
        }

        logOutObserver()
        deleteObserver()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
    }

    private fun logoutDialog() {
        CustomAlertDialog(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveText("Yes") { dialog, _ ->
                settingViewModel.logoutUser()
                logoutNavigation()
                dialog.dismiss()
            }
            .setNegativeText("No") { dialog, _ -> dialog.dismiss() }
            .show()

    }

    private fun deleteDialog() {
        CustomAlertDialog(this)
            .setTitle("Delete?")
            .setMessage("Are you sure you want to delete account?")
            .setPositiveText("Yes") { dialog, _ ->
//                onSubmit()
                settingViewModel.deleteUser()
//                logoutNavigation()
                dialog.dismiss()
            }
            .setNegativeText("No") { dialog, _ -> dialog.dismiss() }
            .show()

    }

    private fun logOutObserver() {
        settingViewModel.logOutResponse.observe(this) {
            when (it.status) {
                Status.ERROR -> {
//                    progressDialog.dismissDialog()
//                    CustomToast.show(mViewDataBinding.mainContainer, this, it.message)
                }
                Status.SUCCESS -> {
//                    progressDialog.dismissDialog()
//                    logoutNavigation()

                }
                Status.LOADING -> {
//                    progressDialog.show()

                }
            }
        }
    }

    private fun deleteObserver() {
        settingViewModel.deleteResponse.observe(this) {
            when (it.status) {
                Status.ERROR -> {
                    progressDialog.dismissDialog()
                    errorAlertDialog(it.title, it.message)
                }
                Status.SUCCESS -> {
                    progressDialog.dismissDialog()
                    logoutNavigation()

                }
                Status.LOADING -> {
                    progressDialog.show()

                }
            }
        }
    }


    private fun logoutNavigation() {
        preferenceHelper.clearAll()
        gotoClass(GetStartedActivity::class.java, true)
        preferenceHelper.setValue(PrefConstant.SLIDER_INTRO, true)
        preferenceHelper.setValue(PrefConstant.LOCATION_PERMISSION, false)
        finishAffinity()
    }

    private fun gotoClass(cls: Class<*>, finish: Boolean = false) {
        openNewActivity(this@SettingActivity, cls, finish)
    }

    private fun gotoClass(cls: Class<*>, bundle: Bundle, finish: Boolean = false) {
        openNewActivity(this@SettingActivity, cls, bundle, finish)
    }

    private fun insertData() {
        menuList.clear()
        menuList.add(ListUtilsClass.addData(R.drawable.ic_accounts, "Profile", "1"))
        menuList.add(ListUtilsClass.addData(R.drawable.ic_privacy, "Privacy Policy", "2"))
        menuList.add(ListUtilsClass.addData(R.drawable.ic_legals, "Terms & Conditions", "3"))

    }

    override fun onValidated() {

    }

    override fun onBackPress() {
        onBackPressed()
    }

    override fun onSubmit() {
    }

}