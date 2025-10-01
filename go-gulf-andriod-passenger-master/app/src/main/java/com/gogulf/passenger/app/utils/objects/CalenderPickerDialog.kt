package com.gogulf.passenger.app.utils.objects

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.TextView
import com.gogulf.passenger.app.utils.interfaces.DatePickedListener
import com.gogulf.passenger.app.utils.interfaces.TimePickedListener
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

object CalenderPickerDialog {
    private var mYear = 0
    private var mDay = 0
    private var mMonth = 0

    private val TAG = "CalenderPickerDialog"
    fun showCalender(
        context: Context,
        textView: TextView,
        onDatePicked: DatePickedListener,
        minDate: Long = 0,
        maxDate: Long = 0
    ) {
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, p2, p3 ->
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


                val dates = "$day/$month/$year"
                val apiDate = "$year-$month-$day"
                textView.text = dates
                onDatePicked.selectedDate(apiDate)
            },
            mYear,
            mMonth, mDay
        )

        datePickerDialog.show()

        if (minDate > 0) {
            val dates = datePickerDialog.datePicker
//            val currentDate = Calendar.getInstance().timeInMillis
            dates.minDate = minDate
        }
        if (maxDate > 0) {
            val dates = datePickerDialog.datePicker
//            val currentDate = Calendar.getInstance().timeInMillis
            dates.maxDate = maxDate
        }

    }

    fun getCurrentDate(): String {
        val currentTime = Calendar.getInstance().time
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        return sdf.format(currentTime)
    }

    fun getCurrentDatedmy(): String {
        val currentTime = Calendar.getInstance().time
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        return sdf.format(currentTime)
    }

    fun getCalculatedDateDay(dateFormat: String, days: Int): String {
        val cal = Calendar.getInstance()
        val s = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        cal.add(Calendar.DAY_OF_YEAR, days)
        return s.format(Date(cal.timeInMillis))
    }

    fun getCalculatedDateDayInMillis(days: Int): Long {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, days)
        return cal.timeInMillis
    }

    fun getCalculatedDateCustomTime(date: String, dateFormat: String, days: Int): String {
        val msdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

        val dates = msdf.parse(date)!!
//        dates.time
        val cal = Calendar.getInstance()
        cal.time = dates
        val s = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        cal.add(Calendar.DAY_OF_YEAR, days)
        return s.format(Date(cal.timeInMillis))
    }

    fun getCalculatedDateCustomTimeMillis(date: String): Long {
        val msdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

        val dates = msdf.parse(date)!!
//        dates.time
        val cal = Calendar.getInstance()
        cal.time = dates
        return cal.timeInMillis
    }

    fun getCalculatedDateCustomTimeMillis(date: String, days: Int): Long {
        val msdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

        val dates = msdf.parse(date)!!
//        dates.time
        val cal = Calendar.getInstance()
        cal.time = dates
        cal.add(Calendar.DAY_OF_YEAR, days)
        return cal.timeInMillis
    }

    fun getCalculatedDate(date: String, dateFormat: String, days: Int): String? {
        val cal = Calendar.getInstance()
        val s = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        cal.add(Calendar.DAY_OF_YEAR, days)
        try {
            return s.format(Date(s.parse(date)!!.time))
        } catch (e: ParseException) {
            Log.e("TAG", "Error in Parsing Date : " + e.message)
        }
        return null
    }

    fun timePickerDialog(context: Context, timePickedListener: TimePickedListener) {
        val timePickerDialog = TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                // logic to properly handle
                // the picked timings by user
                val formattedTime: String = when {
                    hourOfDay == 0 -> {
                        if (minute < 10) {
                            "${hourOfDay + 12}:0${minute} AM"
                        } else {
                            "${hourOfDay + 12}:${minute} AM"
                        }
                    }
                    hourOfDay > 12 -> {
                        if ((hourOfDay - 12) < 12) {
                            if (minute < 10) {
                                "0${hourOfDay - 12}:0${minute} PM"
                            } else {
                                "0${hourOfDay - 12}:${minute} PM"
                            }
                        } else {
                            if (minute < 10) {
                                "${hourOfDay - 12}:0${minute} PM"
                            } else {
                                "${hourOfDay - 12}:${minute} PM"
                            }

                        }

                    }
                    hourOfDay == 12 -> {
                        if (minute < 10) {
                            "${hourOfDay}:0${minute} PM"
                        } else {
                            "${hourOfDay}:${minute} PM"
                        }
                    }
                    else -> {
                        if (hourOfDay < 10) {
                            if (minute < 10) {
                                "0${hourOfDay}:${minute} AM"
                            } else {
                                "0${hourOfDay}:${minute} AM"
                            }
                        } else {
                            if (minute < 10) {
                                "${hourOfDay}:${minute} AM"
                            } else {
                                "${hourOfDay}:${minute} AM"
                            }

                        }

                    }
                }
//                val selectedTime = "$hourOfDay:$minute"
                var newHour = if (hourOfDay < 10) {
                    "0$hourOfDay"
                } else {
                    "$hourOfDay"
                }
                var newMin = if (minute < 10) {
                    "0$minute"
                } else {
                    "$minute"
                }
                val selectedTime = "$newHour:$newMin:00"

                timePickedListener.selectedDate(formattedTime, selectedTime)

                //                previewSelectedTimeTextView.text = formattedTime
            },
            getCurrentHour().toInt(), getCurrentMinute().toInt(),
            false
        )
        timePickerDialog.show()
    }

    fun timePickerDialog24(context: Context, timePickedListener: TimePickedListener) {
        val timePickerDialog = TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                // logic to properly handle
                // the picked timings by user
                val formattedTime: String = when {
                    hourOfDay == 0 -> {
                        if (minute < 10) {
                            "${hourOfDay + 12}:0${minute} AM"
                        } else {
                            "${hourOfDay + 12}:${minute} AM"
                        }
                    }
                    hourOfDay > 12 -> {
                        if ((hourOfDay - 12) < 12) {
                            if (minute < 10) {
                                "0${hourOfDay - 12}:0${minute} PM"
                            } else {
                                "0${hourOfDay - 12}:${minute} PM"
                            }
                        } else {
                            if (minute < 10) {
                                "${hourOfDay - 12}:0${minute} PM"
                            } else {
                                "${hourOfDay - 12}:${minute} PM"
                            }

                        }

                    }
                    hourOfDay == 12 -> {
                        if (minute < 10) {
                            "${hourOfDay}:0${minute} PM"
                        } else {
                            "${hourOfDay}:${minute} PM"
                        }
                    }
                    else -> {
                        if (hourOfDay < 10) {
                            if (minute < 10) {
                                "0${hourOfDay}:${minute} AM"
                            } else {
                                "0${hourOfDay}:${minute} AM"
                            }
                        } else {
                            if (minute < 10) {
                                "${hourOfDay}:${minute} AM"
                            } else {
                                "${hourOfDay}:${minute} AM"
                            }

                        }

                    }
                }
                var newHour = if (hourOfDay < 10) {
                    "0$hourOfDay"
                } else {
                    "$hourOfDay"
                }
                var newMin = if (minute < 10) {
                    "0$minute"
                } else {
                    "$minute"
                }
                val selectedTime = "$newHour:$newMin:00"
                timePickedListener.selectedDate(formattedTime, selectedTime)
            },
            getCurrentHour().toInt(), getCurrentMinute().toInt(),
            true
        )
        timePickerDialog.show()
    }

    fun getCurrentTime(): String {
        val currentTime = Calendar.getInstance().time
        val sdf = SimpleDateFormat("hh:mm aa", Locale.ENGLISH)
        return sdf.format(currentTime)
    }

    fun getCurrentHour(): String {
        val currentTime = Calendar.getInstance().time
        val sdf = SimpleDateFormat("HH", Locale.ENGLISH)
        return sdf.format(currentTime)
    }

    fun getCurrentMinute(): String {
        val currentTime = Calendar.getInstance().time
        val sdf = SimpleDateFormat("mm", Locale.ENGLISH)
        return sdf.format(currentTime)
    }

    fun getCurrentTime24Hrs(): String {
        val currentTime = Calendar.getInstance().time
        val sdf24 = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
        return sdf24.format(currentTime)
    }

    fun getCurrentDateInMillis(): Long = Calendar.getInstance().timeInMillis

    fun getDateTimeFormatted(minutesToAdd: Long, pattern: String): String {
        // get current time in UTC, no millis needed
        val nowInUtc = OffsetDateTime.now(ZoneOffset.UTC)
        // add some minutes to it
        val someMinutesLater = nowInUtc.plusMinutes(minutesToAdd)
        // return the result in the given pattern
        return someMinutesLater.format(
            DateTimeFormatter.ofPattern(pattern)
        )
    }

    fun localToUTC(dateFormat: String?, datesToConvert: String?): String? {
        var dateToReturn = datesToConvert
        val sdf = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        sdf.timeZone = TimeZone.getDefault()
     /*   DebugMode.e(
            TAG,
            sdf.timeZone.toZoneId().toString(),
            "TimeZone"
        )

        val nowInUtc = OffsetDateTime.now(sdf.timeZone.toZoneId())
        DebugMode.e(
            TAG,
            nowInUtc.toString(),
            "TimeZone"
        )*/
        val gmt: Date?
        val sdfOutPutToSend = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        sdfOutPutToSend.timeZone = TimeZone.getTimeZone("UTC")
        try {
            gmt = sdf.parse(datesToConvert!!)
            dateToReturn = sdfOutPutToSend.format(gmt!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return dateToReturn
    }

    fun uTCToLocal(
        dateFormatInPut: String?,
        dateFomratOutPut: String?,
        datesToConvert: String?
    ): String? {
        var dateToReturn = datesToConvert
        val sdf = SimpleDateFormat(dateFormatInPut, Locale.ENGLISH)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val gmt: Date?
        val sdfOutPutToSend = SimpleDateFormat(dateFomratOutPut, Locale.ENGLISH)
        sdfOutPutToSend.timeZone = TimeZone.getDefault()
        try {
            gmt = sdf.parse(datesToConvert!!)
            dateToReturn = sdfOutPutToSend.format(gmt!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return dateToReturn
    }

}