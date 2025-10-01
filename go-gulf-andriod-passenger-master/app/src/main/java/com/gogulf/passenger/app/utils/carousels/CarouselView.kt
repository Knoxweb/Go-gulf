package com.gogulf.passenger.app.utils.carousels

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.gogulf.passenger.app.data.model.response.bookings.Fleet
import com.gogulf.passenger.app.data.model.response.bookings.QuotesResponse
import com.gogulf.passenger.app.utils.interfaces.VehicleSliderListener
import java.util.*


class CarouselView(
    private val viewPager: ViewPager,
    private val recyclerView: RecyclerView,
    private val context: Context,
    private var sliderList: ArrayList<Fleet>,
    private var quotesResponse: QuotesResponse,
    private var onImageSelected: VehicleCarouselSliderAdapter.OnImageSelected,
    private var fragmentManger: FragmentManager,
    private val sliderCount: VehicleSliderListener
) {
    private var sliderIndicator: VehicleIndicator? = null
    private var sliderAdapter: VehicleCarouselSliderAdapter? = null
    private var sliderInterval = 3500
    private var swipeTask: SwipeTask? = null
    private var swipeTimer: Timer? = null
    private var autoPlay = true


    fun setData(sliderList: ArrayList<Fleet>) {
        this.sliderList = sliderList
    }


    init {
        sliderAdapter =
            VehicleCarouselSliderAdapter(context, sliderList, quotesResponse, fragmentManger)
        sliderAdapter!!.onImageSelected(onImageSelected)
        viewPager.adapter = sliderAdapter
        sliderIndicator = VehicleIndicator(context, sliderList)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = sliderIndicator
        swipeTask = SwipeTask(viewPager, sliderList.size)
        swipeTimer = Timer()
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

                sliderIndicator?.updateIndicator(position)
                sliderCount.count(position)
            }

            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })
//        setSliderInterval(sliderInterval)
    }

    fun changeViewPager(currentItem: Int) {
        viewPager.currentItem = currentItem
    }

    fun setSliderInterval(sliderInterval: Int) {
        this.sliderInterval = sliderInterval
        autoPlay = true
        playCarousel()
    }

    private fun playCarousel() {
        resetScrollTimer()
        if (autoPlay && sliderInterval > 0 && viewPager.adapter != null && viewPager.adapter!!.count > 1) {
            swipeTimer?.schedule(swipeTask, sliderInterval.toLong(), sliderInterval.toLong())
        }
    }

    private fun resetScrollTimer() {
        stopScrollTimer()
        swipeTask = SwipeTask(viewPager, sliderList.size)
        swipeTimer = Timer()
    }

    private fun stopScrollTimer() {
        swipeTimer?.cancel()
        if (swipeTask != null) {
            swipeTask?.cancel()
        }
    }


}

class SwipeTask(private var viewPager: ViewPager?, private var pageCount: Int) : TimerTask() {

    override fun run() {
        viewPager!!.post {
            val nextPage = (viewPager!!.currentItem + 1) % pageCount
            viewPager!!.setCurrentItem(nextPage, true)
        }
    }
}
