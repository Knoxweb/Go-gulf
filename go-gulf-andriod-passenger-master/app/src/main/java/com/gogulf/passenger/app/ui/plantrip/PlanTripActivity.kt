package com.gogulf.passenger.app.ui.plantrip

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.ViewDataBinding
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.data.model.request.CreateQuote
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.utils.enums.Status
import com.gogulf.passenger.app.utils.interfaces.ScheduledBookingListener
import com.gogulf.passenger.app.utils.objects.CalenderPickerDialog
import com.gogulf.passenger.app.utils.objects.DebugMode
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.data.model.CalculateFareRequestBody
import com.gogulf.passenger.app.data.model.CalculateFareResponseData
import com.gogulf.passenger.app.ui.choosevehicle.ChooseVehicleV2Activity
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.customcomponents.CustomDropDownViews
import com.gogulf.passenger.app.databinding.ActivityPlanTripScreenBinding
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


class PlanTripActivity : BaseActivity<ActivityPlanTripScreenBinding>(), BaseNavigation {

    private lateinit var mViewDataBinding: ActivityPlanTripScreenBinding
    private lateinit var returnCalender: ReturnDateCalenderPicker
    private lateinit var bookingCalender: BookingScheduledComponent
    private var pickedCalenderDate = ""
    private lateinit var createQuote: CreateQuote
    private lateinit var calculateFareRequestBody: CalculateFareRequestBody

    private lateinit var offerDd: CustomDropDownViews
    private lateinit var quotesResponse: CalculateFareResponseData
    private val TAG = "PlanTripActivity"
    private val planTripVM: PlanTripVM by viewModel()

    private var isCurrently = true
    private var passengerCount = "1"

    override fun getLayoutId(): Int = R.layout.activity_plan_trip_screen

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityPlanTripScreenBinding
        mViewDataBinding.toolbarViewModel = commonViewModel
        commonViewModel.navigator = this
        mViewDataBinding.activity = this
        mViewDataBinding.bookNowContainer.timeTextView.text =
            CalenderPickerDialog.getCurrentTime24Hrs()
        mViewDataBinding.bookNowContainer.dateTextView.text = CalenderPickerDialog.getCurrentDate()
        var runnable: Runnable? = null
        val handler = Handler(Looper.getMainLooper())
        var delay = 500
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
            mViewDataBinding.bookNowContainer.timeTextView.text =
                CalenderPickerDialog.getCurrentTime24Hrs()
        }.also { runnable = it }, delay.toLong())

        try {
            val bundle = intent.getBundleExtra(IntentConstant.BUNDLE)
            calculateFareRequestBody =
                bundle?.getSerializable(IntentConstant.SERIAL) as CalculateFareRequestBody
        } catch (e: Exception) {
            DebugMode.e(TAG, e.message.toString())
        }


        bookingCalender = BookingScheduledComponent(
            this@PlanTripActivity,
            mViewDataBinding.bookingScheduleContainer,
            object : ScheduledBookingListener {
                override fun activeScheduled(isActive: Boolean) {
                    if (isActive) {
                        mViewDataBinding.bookNowContainer.planTripMaterialCard.strokeWidth =
                            resources.getDimension(R.dimen.m_0dp).toInt()
                        mViewDataBinding.returnContainer.root.visibility = View.GONE
                    } else {
                        mViewDataBinding.returnContainer.root.visibility = View.GONE
                        mViewDataBinding.bookNowContainer.planTripMaterialCard.strokeWidth =
                            resources.getDimension(R.dimen.m_2dp).toInt()
                    }
                    isCurrently = !isActive
                }

                override fun selectedDate(date: String) {
                    pickedCalenderDate = date
                    returnCalender.scheduleDate(date)
                    returnCalender.setDefaultTime(date)
                }
            })

        returnCalender = ReturnDateCalenderPicker(
            this@PlanTripActivity, mViewDataBinding.returnContainer
        )

        mViewDataBinding.nextButton.setOnClickListener {

            onValidated()
        }

        mViewDataBinding.bookNowContainer.planTripMaterialCard.setOnClickListener {
            bookingCalender.changeState(false)
        }


        createQuoteObserver()
        dropDownInits()
        mViewDataBinding.passengerCountContainer.dropdownContainer.setOnClickListener {
            val popUpMenu = PopupMenu(this, mViewDataBinding.passengerCountContainer.dropdownIcon)
            for (i in 1..20) {
                popUpMenu.menu.add(i.toString())
            }
            popUpMenu.setOnMenuItemClickListener { item ->
                passengerCount = item.title.toString()
                mViewDataBinding.passengerCountContainer.passengerValueText.text = passengerCount
                true
            }
            popUpMenu.show()

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
    }

    private fun dropDownInits() {

        offerDd = CustomDropDownViews(
            this@PlanTripActivity,
            mViewDataBinding.offerContainer.autoCompleteText,
            mViewDataBinding.offerContainer.returnBookText
        )
        val nameValue = ArrayList<CustomDropDownViews.NameValue>()
        nameValue.add(
            CustomDropDownViews.NameValue(
                "-20%", "-20"
            )
        )
        nameValue.add(
            CustomDropDownViews.NameValue(
                "-10%", "-10"
            )
        )
        nameValue.add(
            CustomDropDownViews.NameValue(
                "0%", "00"
            )
        )
        nameValue.add(
            CustomDropDownViews.NameValue(
                "10%", "10"
            )
        )
        nameValue.add(
            CustomDropDownViews.NameValue(
                "20%", "20"
            )
        )

        offerDd.setData(nameValue)
        offerDd.setSelection("00")
    }


    private fun getBookingType(): String = if (isCurrently) {
        "on_demand"
    } else {
        "scheduled"
    }

    private fun checkValidations() {


//        createQuote.bookingType = getBookingType()
        calculateFareRequestBody.trip_type = getBookingType()
        if (isCurrently) {
//            createQuote.pickupDateTime = "${
//                CalenderPickerDialog.localToUTC(
//                    "yyyy-MM-dd HH:mm:ss",
//                    "${CalenderPickerDialog.getCurrentDate()} ${CalenderPickerDialog.getCurrentTime24Hrs()}"
//                )
//            } +0000"

            calculateFareRequestBody.pickup_date = "${
                CalenderPickerDialog.localToUTC(
                    "yyyy-MM-dd", CalenderPickerDialog.getCurrentDate()
                )
            }"
            calculateFareRequestBody.pickup_time = "${
                CalenderPickerDialog.localToUTC(
                    "HH:mm:ss", CalenderPickerDialog.getCurrentTime24Hrs()
                )
            }"

        } else {
//            createQuote.pickupDateTime = "${
//                CalenderPickerDialog.localToUTC(
//                    "yyyy-MM-dd HH:mm:ss",
//                    "${bookingCalender.getSelectedDate()} ${bookingCalender.getSelectedTime()}"
//                )
//            } +0000"


            calculateFareRequestBody.pickup_date =
                formatLocalDate("yyyy-MM-dd", bookingCalender.getSelectedDate())
            calculateFareRequestBody.pickup_time =
                formatLocalTime("HH:mm:ss", bookingCalender.getSelectedTime())
        }
//
//        if (returnCalender.isActive()) {
//            createQuote.returnDateTime = "${
//                CalenderPickerDialog.localToUTC(
//                    "yyyy-MM-dd HH:mm:ss",
//                    "${returnCalender.getDate()} ${returnCalender.getTime()}"
//                )
//            } +0000"
//        }

//        createQuote.discount = offerDd.selectedItem.Value.toInt()
        calculateFareRequestBody.exp_discount = offerDd.selectedItem.Value.toInt()
//        DebugMode.e(TAG, createQuote.toString())


        try {
            calculateFareRequestBody.passenger_count = passengerCount.toInt()
        } catch (e: Exception) {
            print(e.stackTrace)
        }

        planTripVM.hitCalculateFare(calculateFareRequestBody)
//        planTripVM.requestQuote(createQuote)
    }


    override fun onValidated() {
        checkValidations()
//        onSubmit()

    }

    override fun onBackPress() {
        onBackPressed()
    }


    fun formatLocalDate(format: String, dateString: String): String {
        // First, parse the date string into a Date object
        val inputFormat = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ) // Adjust this to match the input date format
        inputFormat.timeZone = TimeZone.getDefault() // This ensures local time

        val date = inputFormat.parse(dateString) // Convert string to Date

        // Now format the Date object back to string in the desired format
        val outputFormat = SimpleDateFormat(format, Locale.getDefault())
        outputFormat.timeZone = TimeZone.getDefault()

        return date?.let { outputFormat.format(it) }
            ?: "" // If date parsing fails, return an empty string
    }

    fun formatLocalTime(format: String, timeString: String): String {
        // Parse the time string into a Date object
        val inputFormat = SimpleDateFormat(
            "HH:mm:ss",
            Locale.getDefault()
        ) // Adjust to match the input time format
        inputFormat.timeZone = TimeZone.getDefault() // Ensure it uses the local time zone

        val time = inputFormat.parse(timeString) // Convert string to Date (time)

        // Format the Date object back to string in the desired format
        val outputFormat = SimpleDateFormat(format, Locale.getDefault())
        outputFormat.timeZone = TimeZone.getDefault()

        return time?.let { outputFormat.format(it) } ?: "" // Return empty string if parsing fails
    }


    override fun onSubmit() {

//        Log.d("PARASH", calculateFareRequestBody.pickup_date.toString())
//        Log.d("PARASH", calculateFareRequestBody.pickup_time.toString())
        val bundle = Bundle()
        bundle.putBoolean(IntentConstant.TYPE, isCurrently)
        bundle.putSerializable(IntentConstant.SERIAL, quotesResponse)


        bundle.putString("pickup_date", calculateFareRequestBody.pickup_date)
        bundle.putString("pickup_time", calculateFareRequestBody.pickup_time)
        bundle.putString(IntentConstant.PASSENGER_COUNT, passengerCount)


        val intent = Intent(this, ChooseVehicleV2Activity::class.java)
//        intent.addFlags(
//            Intent.FLAG_ACTIVITY_NEW_TASK or
//                    Intent.FLAG_ACTIVITY_CLEAR_TASK
//        )
        intent.putExtra(IntentConstant.BUNDLE, bundle)
        startActivity(intent)
        finish()
    }


    private fun gotoClass(cls: Class<*>) {
        openNewActivity(this@PlanTripActivity, cls, false)
    }

    private fun gotoClass(cls: Class<*>, bundle: Bundle) {
        openNewActivity(this@PlanTripActivity, cls, bundle, false)
    }


    private fun createQuoteObserver() {
        planTripVM.quoteResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.show()

                }

                Status.SUCCESS -> {
                    progressDialog.dismissDialog()
//                    val response = Gson().fromJson<BaseData<QuotesResponse>>(
//                        it.data,
//                        object : TypeToken<BaseData<QuotesResponse>>() {}.type
//                    )

                    quotesResponse = it.data!!
                    onSubmit()

                }

                Status.ERROR -> {
                    progressDialog.dismissDialog()
                    errorAlertDialog(it.title, it.message)
                }
            }
        }
    }
}