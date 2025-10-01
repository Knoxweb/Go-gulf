package com.gogulf.passenger.app.ui.plantrip

import android.content.Context
import android.view.View
import com.gogulf.passenger.app.utils.interfaces.ScheduledBookingListener
import com.gogulf.passenger.app.utils.interfaces.TimePickedListener
import com.gogulf.passenger.app.utils.objects.CalenderPickerDialog
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.LayoutBookingCalenderviewBinding

class BookingScheduledComponent(
    private val context: Context,
    private val bind: LayoutBookingCalenderviewBinding,
    private val datePickedListener: ScheduledBookingListener
) {
    private var viewCalender = false
    private var isCalenderPick = false
    private var isTimePicked = false
    private var selectedDate = ""
    private var selectedTime = ""
    private var scheduleDate = ""

    init {
        initViews()
        bind.bookingCalender.calenderCardView.visibility = View.GONE
        bind.bookingScheduledCalender.setOnClickListener {
            viewCalender = !viewCalender
            initViews()
            bind.addBookingSwitch.isChecked = viewCalender
            datePickedListener.activeScheduled(viewCalender)
        }

        bind.addBookingSwitch.setOnCheckedChangeListener { _, visible ->
            viewCalender = visible
            initViews()
            datePickedListener.activeScheduled(viewCalender)
        }
        onTimeClicked()

        bind.bookingCalender.timeTextView.text = CalenderPickerDialog.getCurrentTime24Hrs()
        selectedTime = CalenderPickerDialog.getCurrentTime24Hrs()
        selectedDate = CalenderPickerDialog.getCurrentDate()

        bind.bookingCalender.scheduleCalenderView.minDate =
            CalenderPickerDialog.getCurrentDateInMillis()
        onCalenderClicked()

    }

    private fun onTimeClicked() {
        bind.bookingCalender.timerCardView.setOnClickListener {
            CalenderPickerDialog.timePickerDialog24(context, object : TimePickedListener {
                override fun selectedDate(timeFormatted: String, time24: String) {
                    selectedTime = time24
                    bind.bookingCalender.timeTextView.text = time24
                    isTimePicked = true
                }
            })
        }
    }
//    private fun onCalenderClicked() {
//        bind.bookingCalender.scheduleCalenderView.setOnDateChangeListener { _, year, month, dayOfMonth ->
//
//            // Log the date to ensure the listener is triggered
//            Log.d("CalendarView", "Date selected: $year-${month + 1}-$dayOfMonth")
//
//            // Format day and month to two digits
//            val day = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()
//            val monthFormatted = if (month + 1 < 10) "0${month + 1}" else (month + 1).toString()
//
//            isCalenderPick = true
//            isTimePicked = true
//            selectedDate = "$year-$monthFormatted-$day"
//
//            // Notify the listener
//            datePickedListener.selectedDate(selectedDate)
//
//            // Log the selected date
//            Log.d("CalendarView", "Formatted Date: $selectedDate")
//        }
//
//        // Additional debug: Log if the listener is being set
//        Log.d("CalendarView", "OnDateChangeListener is set")
//    }


    private fun onCalenderClicked() {
        bind.bookingCalender.scheduleCalenderView.setOnDateChangeListener { _, p1, p2, p3 ->

            //year - p1, month - p2, day - p3

            val day = if (p3 < 10) {
                "0$p3"
            } else {
                p3.toString()
            }

            val month = when (p2) {

                in 0..9 -> {
                    val date = p2 + 1
                    "0$date"
                }
                else -> {
                    val date = p2 + 1
                    "$date"
                }
            }
            isCalenderPick = true
            isTimePicked = true
            selectedDate = "$p1-$month-$day"
            //Change Date format
            datePickedListener.selectedDate(selectedDate)


        }
    }

    fun getSelectedDate(): String = selectedDate
    fun getSelectedTime(): String = selectedTime

    private fun initViews() {
        if (viewCalender) {
            viewVisible()
        } else {
            viewGone()
        }
    }

    private fun viewVisible() {

//        SlideAnimation.expand(bind.bookingCalender.calenderCardView)
        bind.bookingCalender.root.visibility = View.VISIBLE


        bind.cardMaterialCalender.strokeWidth = context.getResources().getDimension(R.dimen.m_2dp).toInt()

//        SlideAnimation.expand(bind.bookingCalender.calenderCardView)
//        bind.bookingCalender.scheduleCalenderView.minDate =
//            CalenderPickerDialog.getCurrentDateInMillis()
    }

    private fun viewGone() {
        bind.bookingCalender.root.visibility = View.GONE
        bind.cardMaterialCalender.strokeWidth = context.getResources().getDimension(R.dimen.m_0dp).toInt()
//        SlideAnimation.collapse(bind.bookingCalender.calenderCardView)

//        SlideAnimation.collapse(bind.bookingCalender.root)

//        SlideAnimation.collapse(bind.bookingCalender.calenderCardView)

    }


    fun changeState(boo: Boolean) {
        bind.addBookingSwitch.isChecked = boo

    }

}