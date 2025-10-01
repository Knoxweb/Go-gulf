package com.gogulf.passenger.app.ui.menu

import android.content.Intent
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.bookinghistory.activity.BookingHistoryActivity
import com.gogulf.passenger.app.ui.dashboard.DashboardActivity
import com.gogulf.passenger.app.ui.getaride.GetARideActivity
import com.gogulf.passenger.app.ui.help.helps.HelpActivity
import com.gogulf.passenger.app.ui.invoices.InvoiceActivity
import com.gogulf.passenger.app.ui.notice.NoticeActivity
import com.gogulf.passenger.app.ui.schedulebooking.activity.ScheduledBookingActivity
import com.gogulf.passenger.app.ui.settings.setting.SettingActivity
import com.gogulf.passenger.app.ui.support.supports.SupportActivity
import com.gogulf.passenger.app.utils.objects.ListUtilsClass
import com.gogulf.passenger.app.databinding.ActivityDefaultMenuScreenBinding


class MenuActivity : BaseActivity<ActivityDefaultMenuScreenBinding>(), BaseNavigation {
    private lateinit var mViewDataBinding: ActivityDefaultMenuScreenBinding

    private var menuList = ArrayList<MenuModels>()
    private lateinit var adapters: MenuListAdapter


    override fun getLayoutId(): Int = R.layout.activity_default_menu_screen

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityDefaultMenuScreenBinding
        mViewDataBinding.toolbarViewModel = commonViewModel
        commonViewModel.navigator = this


        mViewDataBinding.title = "Menus"
        insertData()
        mViewDataBinding.menuContainer.menuRecyclerView.hasFixedSize()
        mViewDataBinding.menuContainer.menuRecyclerView.layoutManager =
            LinearLayoutManager(this@MenuActivity)
        adapters = MenuListAdapter(this@MenuActivity, menuList)
        mViewDataBinding.menuContainer.menuRecyclerView.adapter = adapters
        adapters.onMenuClick(object : MenuListAdapter.OnMenuClicked {
            override fun onClicked(menuModels: MenuModels) {
                Log.e("MenuActivity", "menuID = ${menuModels.menuId}   ${menuModels.menuName}")
                when (menuModels.menuName) {
                    "My Bookings" -> {
//                        gotoClass(ScheduledBookingActivity::class.java)
                        val intent = Intent(this@MenuActivity, ScheduledBookingActivity::class.java)
                        startActivity(intent)
                    }

                    "Booking History" -> {
                        gotoClass(BookingHistoryActivity::class.java)
                    }

                    "Account" -> {
                        gotoClass(SettingActivity::class.java)
                    }

                    "Help" -> {
                        gotoClass(HelpActivity::class.java)
                    }

                    "Invoice" -> {
                        gotoClass(InvoiceActivity::class.java)
                    }

                    "Support" -> {
                        gotoClass(SupportActivity::class.java)
                    }

                    "Book Now" -> {
                        gotoClass(GetARideActivity::class.java)
                    }

                    "Notification" -> {
                        gotoClass(NoticeActivity::class.java)
                    }
                    /* "Current Ride" -> {
                         gotoClass(CurrentRideActivity::class.java)
                     }*/
                    "Dashboard" -> {
                        gotoClass(DashboardActivity::class.java)
                        finish()
                    }
                }
            }

        })

    }

    private fun gotoClass(cls: Class<*>) {
        openNewActivity(this@MenuActivity, cls, false)
    }

    override fun onBackPressed() {
        if (isTaskRoot) {
            startActivity(Intent(this@MenuActivity, GetARideActivity::class.java))
            // using finish() is optional, use it if you do not want to keep currentActivity in stack
            finish()
        } else {
            super.onBackPressed()
        }

    }


    private fun insertData() {
        menuList.clear()
        menuList.add(ListUtilsClass.addData(R.drawable.ic_menu_dashboard, "Dashboard", "1"))
        menuList.add(ListUtilsClass.addData(R.drawable.ic_menu_get_a_ride, "Book Now", "2"))
        menuList.add(ListUtilsClass.addData(R.drawable.ic_menu_calender, "My Bookings", "3"))
        menuList.add(ListUtilsClass.addData(R.drawable.ic_menu_scheduled, "Booking History", "4"))
//        menuList.add(ListUtilsClass.addData(R.drawable.ic_dashboard, "Invoice", "5"))
//        menuList.add(ListUtilsClass.addData(R.drawable.ic_dashboard, "Help", "6"))
        menuList.add(ListUtilsClass.addData(R.drawable.ic_menu_support, "Support", "7"))
        menuList.add(ListUtilsClass.addData(R.drawable.ic_menu_settings, "Account", "8"))
        menuList.add(
            ListUtilsClass.addData(
                R.drawable.baseline_notifications_24,
                "Notification",
                "9"
            )
        )
//        menuList.add(ListUtilsClass.addData(R.drawable.ic_menu_get_a_ride, "Current Ride", "8"))

    }

    override fun onValidated() {
    }

    override fun onBackPress() {
        onBackPressed()
    }

    override fun onSubmit() {
    }

}