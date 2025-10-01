package com.gogulf.passenger.app.ui.plantrip

import android.content.Context
import android.view.View
import com.gogulf.passenger.app.utils.interfaces.DatePickedListener
import com.gogulf.passenger.app.utils.interfaces.TimePickedListener
import com.gogulf.passenger.app.utils.objects.CalenderPickerDialog
import com.gogulf.passenger.app.databinding.LayoutReturnCalenderBinding
import java.text.SimpleDateFormat
import java.util.*


class ReturnDateCalenderPicker(
    private val context: Context,
    private val bind: LayoutReturnCalenderBinding,
) {
    private var viewReturn = false
    private var isCalenderPick = false
    private var isTimePicked = false
    private var selectedDate = ""
    private var selectedTime = ""
    private var scheduleDate = ""

    init {
        initViews()
        bind.returnBookingCalender.setOnClickListener {
            viewReturn = !viewReturn
            initViews()
            bind.addReturnSwitch.isChecked = viewReturn
        }

        bind.addReturnSwitch.setOnCheckedChangeListener { _, visible ->
            viewReturn = visible
            initViews()
        }
        onTimeClicked()
        val currentTime = Calendar.getInstance().time

        val sdf = SimpleDateFormat("hh:mm aa", Locale.ENGLISH)
        val sdf24 = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
        val curTime = sdf.format(currentTime)
        val curTime24 = sdf24.format(currentTime)

//        Log.e("TripCalender", "$currentTime   current time-$curTime -> $curTime24")
        /*     for (data in TimeZone.getAvailableIDs()){
                 Log.e("TripCalender", "$data   current time-$curTime -> $curTime24")
             }*/

        val currentDate = Calendar.getInstance().timeInMillis
//        val currentDate = calendar.timeInMillis
        bind.timeText.text = curTime24
        selectedTime = curTime24
//        bind.scheduleCalenderView.minDate = currentDate

        onCalenderClicked()

    }

    fun setDefaultTime(date: String) {
        bind.calenderText.text = getReturnDate(date)
        selectedDate = getReturnDate(date)
        isCalenderPick = true
    }

    fun getReturnDate(date: String): String =
        CalenderPickerDialog.getCalculatedDateCustomTime(date, "yyy-MM-dd", 1)


    fun getMinimumDate(): Long =
        if (scheduleDate.isNotEmpty()) {
            /*val msdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

            val date = msdf.parse(scheduleDate)!!

            date.time*/
            CalenderPickerDialog.getCalculatedDateCustomTimeMillis(scheduleDate, 1)
        } else {
            if (viewReturn) {
                bind.calenderText.text = CalenderPickerDialog.getCalculatedDateDay("yyyy-MM-dd", 1)
            }
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, 1)
            cal.timeInMillis
        }


    fun scheduleDate(scheduleDate: String) {
        this.scheduleDate = scheduleDate

    }

    private fun onCalenderClicked() {
        bind.calenderText.setOnClickListener {
            CalenderPickerDialog.showCalender(
                context,
                bind.calenderText,
                object : DatePickedListener {
                    override fun selectedDate(date: String) {
                        selectedDate = date
                        isCalenderPick = true
                        bind.calenderText.text = date
                    }

                },
                getMinimumDate()

            )
        }
    }

    private fun initViews() {
        if (viewReturn) {
            getMinimumDate()

            viewVisible()
        } else {
            viewGone()
        }
    }

    fun isActive(): Boolean = viewReturn

    private fun viewVisible() {
        bind.divider.visibility = View.VISIBLE
        bind.cardContainerCalender.visibility = View.VISIBLE
//        SlideAnimation.slideDownReturn(bind.cardContainerCalender)
    }

    private fun viewGone() {
        bind.divider.visibility = View.GONE
        bind.cardContainerCalender.visibility = View.GONE
//        SlideAnimation.slideUpReturn(bind.cardContainerCalender)

    }

    private fun onTimeClicked() {
        bind.timeText.setOnClickListener {
            CalenderPickerDialog.timePickerDialog24(context, object : TimePickedListener {
                override fun selectedDate(timeFormatted: String, time24: String) {
                    selectedTime = time24
                    bind.timeText.text = time24
                    isTimePicked = true
                }

            })
        }
    }


    fun getTime(): String = selectedTime
    fun getDate(): String = selectedDate

}