package com.gogulf.passenger.app.ui.bookinghistory.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gogulf.passenger.app.ui.bookinghistory.fragment.cancel.CancelledFragment
import com.gogulf.passenger.app.ui.bookinghistory.fragment.completed.CompletedFragment

class HistoryViewPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        return if (position == 0) {
            CompletedFragment()
        } else {
            CancelledFragment()
        }
    }
}