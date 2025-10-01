package com.gogulf.passenger.app.ui.bookinghistory.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.viewpager2.widget.ViewPager2
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.google.android.material.tabs.TabLayoutMediator
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.databinding.ActivityScheduledBookingsBinding

class BookingHistoryActivity : BaseActivity<ActivityScheduledBookingsBinding>(), BaseNavigation {
    private lateinit var mViewDataBinding: ActivityScheduledBookingsBinding


    override fun getLayoutId(): Int = R.layout.activity_scheduled_bookings

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityScheduledBookingsBinding

        mViewDataBinding.toolbarViewModel = commonViewModel
        commonViewModel.navigator = this
        mViewDataBinding.activity = this
        val viewPagerAdapter = HistoryViewPagerAdapter(this)
        mViewDataBinding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(mViewDataBinding.tabLayout, mViewDataBinding.viewPager) { tab, position ->
            val tabNames = listOf("Completed", "Cancelled")
            tab.text = tabNames[position]
        }.attach()


        mViewDataBinding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
        })


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
    }




    private fun gotoClass(cls: Class<*>) {
        openNewActivity(this@BookingHistoryActivity, cls, true)

    }


    override fun onValidated() {
    }

    override fun onBackPress() {
        onBackPressed()
    }

    override fun onSubmit() {
    }
}