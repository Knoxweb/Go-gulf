package com.gogulf.passenger.app.utils.customcomponents

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.utils.interfaces.DateListener
import com.gogulf.passenger.app.databinding.LayoutMonthYearPickerBinding
import java.text.SimpleDateFormat
import java.util.*


class MonthYearPicker
    (
    context: Context,
    dateListener: DateListener,
    private val filterDate: String = ""
) {

    private var years = ArrayList<String>()
    private var monthList = ArrayList<String>()
    var numbers = arrayOf("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12")

    var selectYear = ""
    var selectMonth = ""
    var yearIndex = 0
    var monthIndex = 0
    var expiryDate = ""

    init {

        val currentTime = Calendar.getInstance().time
        val sdf = SimpleDateFormat("yyyy", Locale.ENGLISH)
        val sdf2 = SimpleDateFormat("MM", Locale.ENGLISH)
        val curTime = sdf.format(currentTime)
        val curMON = sdf2.format(currentTime)


        val alertDialog = AlertDialog.Builder(context)
        val alertBinding: LayoutMonthYearPickerBinding =
            LayoutMonthYearPickerBinding.inflate(
                LayoutInflater.from(context),
                null
            )
        alertDialog.setView(alertBinding.root)
        val builder = alertDialog.create()
        builder.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        builder.show()

        createFiftyYearArray(curTime.toInt())
        //Year Pickers
        alertBinding.yearPicker.textColor = ContextCompat.getColor(context, R.color.white)
        alertBinding.yearPicker.minValue = 0
        alertBinding.yearPicker.maxValue = years.size - 1
        alertBinding.yearPicker.displayedValues = years.toTypedArray()
        alertBinding.yearPicker.wrapSelectorWheel = true
        if (filterDate != "") {
            alertBinding.yearPicker.value = getValueYear()
            selectYear = years[getValueYear()]
        }
        alertBinding.yearPicker.setOnValueChangedListener { num, _, newVal ->
            selectYear = years[newVal]
            yearIndex = newVal
//            alertBinding.monthPicker.isEnabled = !checkValidation(selectYear.toInt(), selectMonth.toInt())

        }
        //monthPickers

        alertBinding.monthPicker.textColor = ContextCompat.getColor(context, R.color.white)
        alertBinding.monthPicker.minValue = 0
        alertBinding.monthPicker.maxValue = numbers.size - 1
        alertBinding.monthPicker.displayedValues = numbers
        alertBinding.monthPicker.wrapSelectorWheel = true
        if (filterDate != "") {
            alertBinding.monthPicker.value = getValueMonth()
            selectMonth = numbers[getValueMonth()]
        }
        alertBinding.monthPicker.setOnValueChangedListener { num, _, newVal ->
            selectMonth = numbers[newVal]
            monthIndex = newVal
//            alertBinding.monthPicker.isEnabled = !checkValidation(selectYear.toInt(), selectMonth.toInt())

        }

        alertBinding.okButton.setOnClickListener {
            if (selectYear == "") selectYear = years[0]
            if (selectMonth == "") selectMonth = numbers[0]
//            expiryDate = "$selectMonth-$selectYear"
            expiryDate = "$selectMonth/$selectYear"
            if (checkValidation(curTime.toInt(), curMON.toInt())) {
                dateListener.value(expiryDate)
                builder.dismiss()
            } else {
                Toast.makeText(context, "Invalid Date Selected", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun getValueMonth(): Int {
        val months = if (filterDate.contains("/")) {
            filterDate.split("/")[0]
        } else if (filterDate.contains("-")) {
            filterDate.split("-")[0]
        } else {
            filterDate
        }

        var indexs = 0

        numbers.forEachIndexed { index, s ->
            if (s == months) {
                indexs = index
                return@forEachIndexed
            }
        }
        return indexs

    }

    private fun monthValidations(currentYear: Int, currentMonth: Int) {
        monthList.clear()
        if (currentYear == selectYear.toInt()) {
            if (currentMonth >= selectMonth.toInt()) {


                for (i in selectMonth.toInt()..12) {
                    monthList.add(i.toString())
                }
            }
        } else {

        }
    }

    private fun getValueYear(): Int {
//        val year = filterDate.split("/")[1]
        val year = if (filterDate.contains("/")) {
            filterDate.split("/")[1]
        } else if (filterDate.contains("-")) {
            filterDate.split("-")[1]
        } else {
            filterDate
        }

        var indexs = 0

        years.forEachIndexed { index, s ->
            if (s == year) {
                indexs = index
                return@forEachIndexed
            }
        }
        return indexs
    }

    private fun createFiftyYearArray(currentYear: Int) {
        years.clear()
        val fiftyYear = currentYear + 40

        for (i in currentYear..fiftyYear) {
            years.add(i.toString())
        }

    }

    private fun createMonthArray(currentYear: Int) {
        monthList.clear()
        val fiftyYear = currentYear + 40

        for (i in currentYear..1) {
            years.add(i.toString())
        }

    }

    private fun checkValidation(currentYear: Int, currentMonth: Int): Boolean {

        if (currentYear == selectYear.toInt()) {
            if (currentMonth >= selectMonth.toInt()) {
                return false
            }
        }
        return true
    }

}