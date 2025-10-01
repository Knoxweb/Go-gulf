package com.gogulf.passenger.app.ui.help.helpdetail

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.data.model.response.FAQ
import com.gogulf.passenger.app.data.model.response.FAQModel
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.databinding.ActivityMenuScreenBinding

class HelpDetailActivity : BaseActivity<ActivityMenuScreenBinding>(),BaseNavigation {

    private lateinit var mViewDataBinding: ActivityMenuScreenBinding
    private var faqList = ArrayList<FAQ>()
    private lateinit var currentFAQ: FAQModel

    private lateinit var adapter: HelpDetailAdapter
    override fun getLayoutId(): Int = R.layout.activity_menu_screen

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityMenuScreenBinding

        mViewDataBinding.toolbarViewModel = commonViewModel
        commonViewModel.navigator = this
        try {
            val bundle = intent.getBundleExtra(IntentConstant.BUNDLE)
//            currentFAQ = bundle?.getSerializable(IntentConstant.SERIAL) as FAQModel
        } catch (e: Exception) {
            e.printStackTrace()
        }
        faqList.clear()
//        faqList.addAll(currentFAQ.faqs)

        mViewDataBinding.title = "Help Detail"
        adapter = HelpDetailAdapter(faqList)
        mViewDataBinding.menuContainer.menuRecyclerView.hasFixedSize()
        mViewDataBinding.menuContainer.menuRecyclerView.layoutManager =
            LinearLayoutManager(this@HelpDetailActivity)
        mViewDataBinding.menuContainer.menuRecyclerView.adapter = adapter

    }

    override fun onValidated() {

    }

    override fun onBackPress() {
        onBackPressed()
    }

    override fun onSubmit() {
    }
}