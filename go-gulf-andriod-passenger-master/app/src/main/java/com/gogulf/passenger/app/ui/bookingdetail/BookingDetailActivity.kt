package com.gogulf.passenger.app.ui.bookingdetail

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.data.model.response.bookings.*
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.base.CommonViewAdapter
import com.gogulf.passenger.app.ui.settings.account.AccountInformationModel
import com.gogulf.passenger.app.utils.customcomponents.ComponentRatingView
import com.gogulf.passenger.app.utils.enums.Status
import com.gogulf.passenger.app.utils.objects.DebugMode
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.bumptech.glide.Glide
import com.gogulf.passenger.app.data.model.CurrentBookingResponseData
import com.gogulf.passenger.app.data.model.Passenger
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.imageFromGlide
import com.gogulf.passenger.app.databinding.ActivityDetailPageBookingBinding
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class BookingDetailActivity : BaseActivity<ActivityDetailPageBookingBinding>(), BaseNavigation {
    private lateinit var mViewDataBinding: ActivityDetailPageBookingBinding
    private val bookingDetailVM: BookingDetailVM by viewModel()
    private var bookingId = ""
    private var fareList = ArrayList<AccountInformationModel>()
    private lateinit var commonAdapter: CommonViewAdapter


    private val TAG = "BookingDetailActivity"
    private lateinit var destinationLayout: TripCardTexts
    private lateinit var transitionLayout: TripCardTexts
    private lateinit var leadPassengerLayout: TripCardTexts
    private lateinit var ratingStars: ComponentRatingView
    private var bookingModels: CurrentBookingResponseData? = null

    override fun getLayoutId(): Int = R.layout.activity_detail_page_booking

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityDetailPageBookingBinding
        mViewDataBinding.toolbarViewModel = commonViewModel
        commonViewModel.navigator = this
        mViewDataBinding.activity = this


//        bookingDetailVM.getBookingDetail(bookingId)

        destinationLayout = TripCardTexts(mViewDataBinding.containerLayoutDistance)
        destinationLayout.setTextId1("Start")
        destinationLayout.setTextId2("Destination")
        transitionLayout = TripCardTexts(mViewDataBinding.containerLayoutTrans)
        transitionLayout.setTextId1("TransId")
        transitionLayout.setTextId2("Date/Time")
        leadPassengerLayout = TripCardTexts(mViewDataBinding.containerLeadPassenger)
        leadPassengerLayout.setTextId1("Booked for")
        leadPassengerLayout.setTextId2("Phone no")
        ratingStars = ComponentRatingView(
            this@BookingDetailActivity,
            mViewDataBinding.driverInformationLayout.driverRatings
        )

        bookingObserver()

        commonAdapter = CommonViewAdapter(fareList)
//        mViewDataBinding.fareLayouts.recyclerViewCharge.hasFixedSize()
//        mViewDataBinding.fareLayouts.recyclerViewCharge.layoutManager =
//            LinearLayoutManager(this@BookingDetailActivity)
//        mViewDataBinding.fareLayouts.recyclerViewCharge.adapter = commonAdapter

        try {
            val bundle = intent.getBundleExtra(IntentConstant.BUNDLE)
            bookingId = bundle?.getString(IntentConstant.BOOKING_ID, "") ?: ""
            bookingModels = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle?.getSerializable(
                    IntentConstant.SERIAL,
                    CurrentBookingResponseData::class.java
                )
            } else {
                bundle?.getSerializable(IntentConstant.SERIAL) as CurrentBookingResponseData
            }
        } catch (e: Exception) {
            DebugMode.e(TAG, e.message.toString(), "Catch Error Message")
        }

        if (bookingModels != null) {
            initialsData(bookingModels!!)
        } else {
            bookingDetailVM.getBookingDetail(bookingId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
    }

    override fun onValidated() {
    }

//    private fun checkDriverValidations(driverModel: ErUser) {
//        if (driverModel.name == "") {
//            mViewDataBinding.driverInformationLayout.root.visibility = View.GONE
//            mViewDataBinding.driverDivider.visibility = View.GONE
//        } else {
//            mViewDataBinding.driverInformationLayout.root.visibility = View.VISIBLE
//            mViewDataBinding.driverDivider.visibility = View.VISIBLE
//        }
//        Glide.with(this@BookingDetailActivity).load(driverModel.imageLink).into(
//            mViewDataBinding.driverInformationLayout.driverImageView
//        )
//        ratingStars.rate(driverModel.avgRating)
//
//
//    }

    private fun checkDriverValidations(driverModel: Passenger) {
        if (driverModel.name == "") {
            mViewDataBinding.driverInformationLayout.root.visibility = View.GONE
            mViewDataBinding.driverDivider.visibility = View.GONE
        } else {
            mViewDataBinding.driverInformationLayout.root.visibility = View.VISIBLE
            mViewDataBinding.driverDivider.visibility = View.VISIBLE
        }
//        Glide.with(this@BookingDetailActivity).load(driverModel.profile_picture_url).into(
//            mViewDataBinding.driverInformationLayout.driverImageView
//        )
        mViewDataBinding.driverInformationLayout.driverImageView.imageFromGlide(driverModel.profile_picture_url)
        driverModel.rating?.toFloat()?.roundToInt()?.let { ratingStars.rate(it) }


    }


    private fun checkVehicleValidation(vModel: Fleet) {
        Glide.with(this@BookingDetailActivity).load(vModel.imageLink).into(
            mViewDataBinding.vehicleInformationLayout.carImageView
        )
        if (vModel.title.isEmpty()) {
            mViewDataBinding.vehicleInformationLayout.vehicleType.visibility = View.GONE
        } else {
            mViewDataBinding.vehicleInformationLayout.vehicleType.visibility = View.VISIBLE
        }
    }

    private fun checkDriverNote(note: String) {
        if (note.isNullOrEmpty()) {
            mViewDataBinding.containerDriverNote.root.visibility = View.GONE
        } else {
            mViewDataBinding.containerDriverNote.root.visibility = View.VISIBLE
        }
    }

    private fun leadPassengers(name: String) {
        if (name.isNullOrEmpty()) {
            mViewDataBinding.containerLeadPassenger.root.visibility = View.GONE
        } else {
            mViewDataBinding.containerLeadPassenger.root.visibility = View.VISIBLE
        }

    }

    override fun onBackPress() {
        onBackPressed()
    }

    override fun onSubmit() {
    }

    private fun initialsData(data: CurrentBookingResponseData) {
//
        mViewDataBinding.bookingModel = data
        data.driver?.let { checkDriverValidations(it) }
//        addInvoiceInfo(data.invoice)
//        checkDriverValidations(data.driverUser)
//        checkVehicleValidation(data.fleet)
        data.description?.let { checkDriverNote(it) }
//        leadPassengers(data.leadPassengerName)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun bookingObserver() {
        bookingDetailVM.pendingResponse.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dismissDialog()

//                    val response = Gson().fromJson<BaseData<BookingDetailModel>>(
//                        it.data,
//                        object : TypeToken<BaseData<BookingDetailModel>>() {}.type
//                    )
//                    initialsData(response.data)

                    it.data?.let { it1 -> initialsData(it1) }

                }
                Status.ERROR -> {
                    progressDialog.dismissDialog()

                }
                Status.LOADING -> {

                    progressDialog.show()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addInvoiceInfo(model: Invoice) {
        fareList.clear()
//        addFares("Fare", String.format("€ %.2f", model.fare))

       /* for (data in model.chargeDetails) {
            addFares(data.key, String.format("€ %.2f", data.value))
        }*/

        addFares("Sub total", String.format("$ %.2f", model.subTotal))
        addFares(
            model.surchargeTitle,
            "(" + model.surchargePercentage + ") " + String.format("$ %.2f", model.surchargeAmount)
        )

        commonAdapter.notifyDataSetChanged()


    }

    private fun addFares(label: String, data: String) {
        fareList.add(AccountInformationModel(label, data))
    }
}