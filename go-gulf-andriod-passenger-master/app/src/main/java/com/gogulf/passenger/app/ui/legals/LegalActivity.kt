package com.gogulf.passenger.app.ui.legals

import android.text.method.LinkMovementMethod
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.gogulf.passenger.app.data.model.firestore.ContentModel
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.base.BaseObserverListener
import com.gogulf.passenger.app.ui.getaride.GetARideActivity
import com.gogulf.passenger.app.utils.interfaces.AnyApiListeners
import com.gogulf.passenger.app.utils.objects.DebugMode
import com.gogulf.passenger.app.utils.objects.IntentConstant.BUNDLE
import com.gogulf.passenger.app.utils.objects.IntentConstant.TITLE
import com.gogulf.passenger.app.utils.others.Resource
import com.gogulf.passenger.app.databinding.ActivityNewPolicyTermsScreenBinding
import kotlinx.coroutines.launch

class LegalActivity : BaseActivity<ActivityNewPolicyTermsScreenBinding>(), BaseNavigation {

    private lateinit var mViewDataBinding: ActivityNewPolicyTermsScreenBinding

    //    private lateinit var adapter: LegalAdapters
//    private var mList = ArrayList<LegalModels>()
    private var title = ""
    private var tag = "LegalActivity"

    override fun getLayoutId(): Int = R.layout.activity_new_policy_terms_screen

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityNewPolicyTermsScreenBinding
        mViewDataBinding.toolbarViewModel = commonViewModel
        commonViewModel.navigator = this
        try {
            val bundle = intent.getBundleExtra(BUNDLE)
            title = bundle?.getString(TITLE, "") ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            DebugMode.e(tag, e.message.toString(), "$tag-> catch")
        }

        mViewDataBinding.title = title
        mViewDataBinding.toolbar.done = ""
//        mList.clear()
        if (title == "Terms & Conditions") {
//            mList.addAll(LegalData.setData())
            commonViewModel.getTerms()
            valueObserver(commonViewModel.myTerms)

        } else if (title == "Privacy Policy") {
//            mList.addAll(PrivacyData.setData())
            commonViewModel.getPolicy()
            valueObserver(commonViewModel.myPolicy)
        } else if (title == "Accept") {
            commonViewModel.getTerms()
            valueObserver(commonViewModel.myTerms)

            mViewDataBinding.acceptButton.visibility = View.VISIBLE
            mViewDataBinding.acceptButton.setOnClickListener {
                onValidated()
            }
        }




        mViewDataBinding.termsAndConditions.isClickable = true
        mViewDataBinding.termsAndConditions.movementMethod = LinkMovementMethod.getInstance()

//        adapter = LegalAdapters(mList)
//        mViewDataBinding.dataRecyclerView.hasFixedSize()
//        mViewDataBinding.dataRecyclerView.layoutManager = LinearLayoutManager(this@LegalActivity)
//        mViewDataBinding.dataRecyclerView.adapter = adapter

    }

    private fun valueObserver(obs: MutableLiveData<Resource<ContentModel>>) {
        lifecycleScope.launch {
            BaseObserverListener.observe(obs,
                this@LegalActivity,
                object : AnyApiListeners<ContentModel> {
                    override fun onError(title: String?, message: String?) {
                        hideDialog()
                    }

                    override fun onLoading() {
                        showDialog()
                    }

                    override fun onSuccess(data: ContentModel?) {
                        hideDialog()

                        mViewDataBinding.termsAndConditions.text = HtmlCompat.fromHtml(
                            data?.description ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT
                        )
                    }

                })
        }
    }


    override fun onValidated() {

//        gotoClass(DashboardActivity::class.java)
        gotoClass(GetARideActivity::class.java)
    }

    private fun gotoClass(cls: Class<*>) {
        openNewActivity(this, cls, true)
    }

    override fun onBackPress() {
        onBackPressed()
    }

    override fun onSubmit() {
    }
}