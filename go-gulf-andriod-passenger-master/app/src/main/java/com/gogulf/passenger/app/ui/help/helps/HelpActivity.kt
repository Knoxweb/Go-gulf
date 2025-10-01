package com.gogulf.passenger.app.ui.help.helps

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.help.helpdetail.HelpDetailActivity
import com.gogulf.passenger.app.ui.menu.MenuModels
import com.gogulf.passenger.app.utils.interfaces.OnDialogClicked
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.utils.objects.ListUtilsClass
import com.gogulf.passenger.app.databinding.ActivityMenuScreenBinding

class HelpActivity : BaseActivity<ActivityMenuScreenBinding>(), BaseNavigation {


    private var menuList = ArrayList<MenuModels>()

    private lateinit var adapters: HelpAdapter


    lateinit var mViewDataBinding: ActivityMenuScreenBinding

    override fun getLayoutId(): Int = R.layout.activity_menu_screen

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityMenuScreenBinding
        mViewDataBinding.toolbarViewModel = commonViewModel
        commonViewModel.navigator = this
        insertData()
        mViewDataBinding.title = "Help"
        mViewDataBinding.menuContainer.menuRecyclerView.hasFixedSize()
        mViewDataBinding.menuContainer.menuRecyclerView.layoutManager =
            LinearLayoutManager(this@HelpActivity)
        adapters = HelpAdapter(this@HelpActivity, menuList, object : OnDialogClicked {
            override fun onClicked(id: Int) {
                val bundle = Bundle()
                bundle.putSerializable(IntentConstant.SERIAL, menuList[id])
                gotoClass(HelpDetailActivity::class.java)
            }

        })
        mViewDataBinding.menuContainer.menuRecyclerView.adapter = adapters

    }

    private fun gotoClass(cls: Class<*>) {
        openNewActivity(this, cls, false)
    }

    private fun gotoClass(cls: Class<*>, bundle: Bundle) {
        openNewActivity(this, cls, bundle, false)
    }

    private fun insertData() {
        menuList.clear()
        menuList.add(ListUtilsClass.addData(R.drawable.ic_dashboard, "Profile and Account", "1"))
        menuList.add(ListUtilsClass.addData(R.drawable.ic_car, "Bookings", "2"))
        menuList.add(ListUtilsClass.addData(R.drawable.ic_calender, "Policies and Other info", "3"))

    }

    override fun onValidated() {
    }

    override fun onBackPress() {
        onBackPressed()
    }

    override fun onSubmit() {
    }

}