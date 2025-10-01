package com.gogulf.passenger.app.ui.support.supports

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.customcomponents.CustomDropDownViews
import com.gogulf.passenger.app.utils.enums.Status
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.databinding.ActivitySupportScreenBinding
import org.koin.android.viewmodel.ext.android.viewModel

class SupportActivity : BaseActivity<ActivitySupportScreenBinding>(), BaseNavigation {

    private lateinit var mViewDataBinding: ActivitySupportScreenBinding
    private lateinit var customDropDownViews: CustomDropDownViews
    private val supportDetailViewModel: SupportVM by viewModel()


    override fun getLayoutId(): Int = R.layout.activity_support_screen

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivitySupportScreenBinding

        mViewDataBinding.toolbarViewModel = commonViewModel
        commonViewModel.navigator = this
        mViewDataBinding.activity = this
        mViewDataBinding.bookingIdET.setHintVisible(View.VISIBLE)
        mViewDataBinding.subjectID.setHintVisible(View.VISIBLE)
        mViewDataBinding.messageID.setHintVisible(View.VISIBLE)

        postSupportObserver()

        customDropDownViews = CustomDropDownViews(
            this@SupportActivity, mViewDataBinding.chooseSupportType, mViewDataBinding.topicHintTV
        )
        val nameValue = ArrayList<CustomDropDownViews.NameValue>()
        nameValue.add(
            CustomDropDownViews.NameValue(
                "Billing", "billing"
            )
        )
        nameValue.add(
            CustomDropDownViews.NameValue(
                "Lost & Found", "lost_and_found"
            )
        )
        nameValue.add(
            CustomDropDownViews.NameValue(
                "Feedback", "feedback"
            )
        )
        nameValue.add(
            CustomDropDownViews.NameValue(
                "Complaint", "complaint"
            )
        )
        nameValue.add(
            CustomDropDownViews.NameValue(
                "Account", "account"
            )
        )

        customDropDownViews.setData(nameValue)
        customDropDownViews.setSelection("billing")

    }

    private fun checkValidations() {
        val supportPostModel = SupportPostModel(
            "",
            mViewDataBinding.bookingIdET.text,
            mViewDataBinding.messageID.text,
            customDropDownViews.selectedItem.Value
        )
        supportDetailViewModel.submitSupport(supportPostModel)
    }

    override fun onValidated() {
        checkValidations()
//        onBackPressed()

    }

    override fun onBackPress() {
        onBackPressed()
    }

    override fun onSubmit() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
    }


    private fun postSupportObserver() {
        supportDetailViewModel.supportResponse.observe(this) {
            when (it.status) {
                Status.ERROR -> {
                    progressDialog.dismissDialog()
                    errorAlertDialog(it.title, it.message)
                }

                Status.SUCCESS -> {
                    progressDialog.dismissDialog()
                    CustomAlertDialog(this).setTitle(it.data?.title).setMessage(it.data?.message)
                        .setCancellable(false).setPositiveText("OK") { dialog, _ ->
                            dialog.dismiss()
                            finish()
                        }.show()

                }

                Status.LOADING -> {
                    progressDialog.show()
                }
            }
        }
    }

}