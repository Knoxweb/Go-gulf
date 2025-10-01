package com.gogulf.passenger.app.ui.addextras

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.gogulf.passenger.app.data.model.others.FleetType
import com.gogulf.passenger.app.data.model.others.SeatCharge
import com.gogulf.passenger.app.data.model.request.ExtraModel
import com.gogulf.passenger.app.data.model.request.RequestJobModel
//import com.slyyk.passenger.app.data.model.response.bookings.Fleet
import com.gogulf.passenger.app.data.model.Fleet
import com.gogulf.passenger.app.data.model.Quote
//import com.slyyk.passenger.app.data.model.response.bookings.QuotesResponse
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.enums.Status
import com.gogulf.passenger.app.utils.interfaces.CapacityValueListener
import com.gogulf.passenger.app.utils.interfaces.ExtrasInterface
import com.gogulf.passenger.app.utils.interfaces.NoteInterface
import com.gogulf.passenger.app.utils.objects.DebugMode
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.bumptech.glide.Glide
import com.gogulf.passenger.app.data.model.Location
import com.gogulf.passenger.app.data.model.Route
import com.gogulf.passenger.app.data.model.request.ConfirmBookingModel
import com.gogulf.passenger.app.ui.auth.cards.AddCardActivity
import com.gogulf.passenger.app.ui.schedulebooking.activity.NewScheduledBookingActivity
import com.gogulf.passenger.app.ui.search_ride.SearchDriverNewActivity
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.utils.animations.SlideAnimation
import com.gogulf.passenger.app.utils.objects.PrefConstant
import com.gogulf.passenger.app.utils.others.CustomToast
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.ActivityAddExtrasBinding
import org.koin.android.viewmodel.ext.android.viewModel

class AddExtrasActivity : BaseActivity<ActivityAddExtrasBinding>(), BaseNavigation {

    private lateinit var mViewDataBinding: ActivityAddExtrasBinding
    private val addExtrasVM: AddExtrasVM by viewModel()
    private var viewCap = false
    private var viewExtra = false
    private var viewFlight = false
    private var viewLead = false
    private var viewFlightDetail = false
    private var viewFlightNumber = false
    private var notes = ""
    private var passengers = 1
    private var luggages = 0
    private var wheelChair = 0
    private lateinit var passengerData: CapacityLayout
    private lateinit var luggageData: CapacityLayout
    private lateinit var wheelChairData: CapacityLayout

    private var isCurrently = false
    private val TAG = "AddExtrasActivity"
    private lateinit var quotesResponse: Quote
    private lateinit var fleetSelected: Fleet

    private lateinit var infantSeats: ExtrasLayoutVTwo
    private lateinit var childSeats: ExtrasLayoutVTwo
    private lateinit var boosterSeats: ExtrasLayoutVTwo

    private lateinit var pickup_date : String
    private lateinit var pickup_time : String
    private var passengerCount = "1"

    override fun getLayoutId(): Int = R.layout.activity_add_extras

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityAddExtrasBinding
        mViewDataBinding.toolbarViewModel = commonViewModel
        commonViewModel.navigator = this


        try {
            val bundle = intent.getBundleExtra(IntentConstant.BUNDLE)
            isCurrently = bundle?.getBoolean(IntentConstant.TYPE, false) as Boolean

            pickup_date = bundle.getString("pickup_date")!!
            pickup_time = bundle.getString("pickup_time")!!
            passengerCount = bundle.getString(IntentConstant.PASSENGER_COUNT)!!

            quotesResponse = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getSerializable(
                    IntentConstant.SERIAL2, Quote::class.java
                ) as Quote
            } else {
                bundle.getSerializable(IntentConstant.SERIAL2) as Quote

            }
            fleetSelected = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getSerializable(
                    IntentConstant.SERIAL, Fleet::class.java
                ) as Fleet
            } else {
                bundle.getSerializable(IntentConstant.SERIAL) as Fleet

            }
        } catch (e: Exception) {
            DebugMode.e(TAG, e.message.toString(), "Catch Error -> ")
        }


//        Log.d("PARASH", pickup_date)
//        Log.d("PARASH", pickup_time)


        mViewDataBinding.pickupDateTime.text  = (pickup_date + " - " + pickup_time)
        mViewDataBinding.scheduleStatus.text = if (quotesResponse.trip_type == "scheduled") "Scheduled" else "On Demand "
        mViewDataBinding.passengerValueText.text = passengerCount


//        Log.d("PARASH", quotesResponse.toString())


        mViewDataBinding.fleetModel = fleetSelected
        mViewDataBinding.quotes = quotesResponse

        mViewDataBinding.cardConstraint.setOnClickListener {
            val modal = ChooseCardBottomSheet(addExtrasVM)
            supportFragmentManager.let { modal.show(it, ChooseCardBottomSheet.TAG) }
//            if (addExtrasVM.cardOfUsers.isEmpty()) {
//                val bundle = Bundle()
//                bundle.putString(IntentConstant.EXTRA, "extra")
//                val intent = Intent(this, AddCardActivity::class.java)
//                intent.putExtra(IntentConstant.BUNDLE, bundle)
//                startActivity(intent)
//            } else {
//                val modal = ChooseCardBottomSheet(addExtrasVM)
//                supportFragmentManager.let { modal.show(it, ChooseCardBottomSheet.TAG) }
//            }
        }

        addExtrasVM.pageCardData.observe(this) {
            if (it == null) {
                mViewDataBinding.cardTextView.text = "Add payment card"
            }
            it?.let {
                mViewDataBinding.cardTextView.text = it.card_masked
            }
        }



        initViews()
        showLayouts()
        setCapacityData(dummyFleet())
//        setExtrasQuantity()
        mViewDataBinding.doneButton.setOnClickListener { onValidated() }
        requestJobObserver()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
    }

    private fun dummyFleet(): FleetType = FleetType(
        fleetSelected.passengers.toString(),
        fleetSelected.pet.toString(),
        fleetSelected.wheelchair.toString(),
        "1",
        "",
        "name"
    )

    private fun infantSeat(): SeatCharge =
        SeatCharge("4", "2", "5", "1", "", fleetSelected.pet.toString())

    private fun childSeat(): SeatCharge =
        SeatCharge("4", "2", "5", "1", "", fleetSelected.pet.toString())

    private fun boosterSeat(): SeatCharge =
        SeatCharge("4", "2", "5", "1", "", fleetSelected.pet.toString())


    private fun setCapacityData(capacity: FleetType) {
        passengerData = CapacityLayout(mViewDataBinding.passengerLayout)
        passengerData.setQuantity(this@AddExtrasActivity, capacity.passengerCount.toInt(), 1)
        luggageData = CapacityLayout(mViewDataBinding.luggageLayout)
        luggageData.setQuantity(this@AddExtrasActivity, capacity.luggageCount.toInt(), 0)
        wheelChairData = CapacityLayout(mViewDataBinding.wheellayout)
        wheelChairData.setQuantity(this@AddExtrasActivity, capacity.suitcaseCount.toInt(), 0)
    }

    private fun setExtrasQuantity() {
        infantSeats = ExtrasLayoutVTwo(mViewDataBinding.infantContainer,
            infantSeat(),
            object : ExtrasInterface {
                override fun addData(id: String, value: String) {

                }

            })
        childSeats = ExtrasLayoutVTwo(mViewDataBinding.childContainer,
            childSeat(),
            object : ExtrasInterface {
                override fun addData(id: String, value: String) {

                }

            })
        boosterSeats = ExtrasLayoutVTwo(mViewDataBinding.boosterContainer,
            boosterSeat(),
            object : ExtrasInterface {
                override fun addData(id: String, value: String) {

                }

            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun showLayouts() {
        mViewDataBinding.capacityContainer.setOnClickListener {
            viewCap = !viewCap
//            initViews()
            viewCapacities()
        }
        mViewDataBinding.topExtraSwitchLayout.setOnClickListener {
            viewExtra = !viewExtra
//            initViews()
            mViewDataBinding.addExtraSwitch.isChecked = viewExtra
        }

        mViewDataBinding.addExtraSwitch.setOnCheckedChangeListener { _, visible ->
            viewExtra = visible
//            initViews()
            viewExtras()
        }
        mViewDataBinding.flightInformationContainer.setOnClickListener {
            viewFlight = !viewFlight
//            initViews()
            viewFlightDetail = false
            viewFlightNumber = true

            mViewDataBinding.flightSwitch.isChecked = viewFlight
        }
        mViewDataBinding.flightSwitch.setOnCheckedChangeListener { _, visible ->
            viewFlight = visible
            viewFlightDetail = false
            viewFlightNumber = true
//            initViews()
            viewFlights()
        }

        mViewDataBinding.topBookingLayout.setOnClickListener {
            viewLead = !viewLead
//            initViews()
            mViewDataBinding.bookSwitch.isChecked = viewLead
        }

        mViewDataBinding.bookSwitch.setOnCheckedChangeListener { _, visible ->
            viewLead = visible
//            initViews()
            viewLeadPassengers()
        }


        mViewDataBinding.addNoteConstraint.setOnClickListener {
            AddNoteDriver(this@AddExtrasActivity, object : NoteInterface {
                @SuppressLint("SetTextI18n")
                override fun onDone(note: String) {
                    notes = note
                    if (note != "") {
                        mViewDataBinding.addNoteText.text = note
                        mViewDataBinding.imageViewAddNote.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@AddExtrasActivity, R.drawable.ic_support
                            )
                        )
                    } else {
                        mViewDataBinding.addNoteText.text = "Add Note"

                        mViewDataBinding.imageViewAddNote.setImageDrawable(
                            ContextCompat.getDrawable(
                                this@AddExtrasActivity, R.drawable.ic_menu_add
                            )
                        )
                    }
                }

            }, notes)
        }
    }

    private fun initViews() {/*viewCapacities()
        viewExtras()
        viewFlights()
        viewLeadPassengers()*/
        if (fleetSelected.full_name == "") mViewDataBinding.vehicleName.visibility = View.GONE
        else mViewDataBinding.vehicleName.visibility = View.VISIBLE

        Glide.with(this@AddExtrasActivity).load(fleetSelected.image_path)
            .into(mViewDataBinding.carImageView)

        when (fleetSelected.id) {
            1 -> {
                mViewDataBinding.vehicleImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@AddExtrasActivity, R.drawable.choose_vehice_1_2
                    )
                )
                mViewDataBinding.bookingSummaryLayout.setPadding(40, 100, 40, 20)

            }

            2 -> {
                mViewDataBinding.vehicleImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@AddExtrasActivity, R.drawable.choose_vehice_1_2
                    )
                )
                mViewDataBinding.bookingSummaryLayout.setPadding(30, 100, 30, 20)

            }

            3 -> {
                mViewDataBinding.vehicleImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@AddExtrasActivity, R.drawable.choose_vehicle_3
                    )
                )
                mViewDataBinding.bookingSummaryLayout.setPadding(40, 150, 40, 20)

            }

            else -> {
                mViewDataBinding.vehicleImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@AddExtrasActivity, R.drawable.choose_vehice_1_2
                    )
                )
                mViewDataBinding.bookingSummaryLayout.setPadding(30, 100, 30, 20)

            }
        }
    }

    private fun viewCapacities() {
        if (viewCap) {
            mViewDataBinding.nextArrowBtnCapacity.setImageDrawable(
                ContextCompat.getDrawable(
                    this@AddExtrasActivity, R.drawable.ic_down_arrow
                )
            )
//            mViewDataBinding.capacityContainerUpdateLayout.visibility = View.VISIBLE
            SlideAnimation.expand(mViewDataBinding.capacityContainerUpdateLayout)
//            mViewDataBinding.capacityContainerUpdateLayout.visibility = View.VISIBLE

        } else {
            mViewDataBinding.nextArrowBtnCapacity.setImageDrawable(
                ContextCompat.getDrawable(
                    this@AddExtrasActivity, R.drawable.ic_button_next_new
                )
            )
//            mViewDataBinding.capacityContainerUpdateLayout.visibility = View.GONE
            SlideAnimation.collapse(mViewDataBinding.capacityContainerUpdateLayout)

//            mViewDataBinding.capacityContainerUpdateLayout.visibility = View.GONE

        }

    }

    private fun viewLeadPassengers() {
        if (viewLead)
//            mViewDataBinding.someOneContainerLayout.visibility = View.VISIBLE
            SlideAnimation.expand(mViewDataBinding.someOneContainerLayout)
        else
//            mViewDataBinding.someOneContainerLayout.visibility = View.GONE
            SlideAnimation.collapse(mViewDataBinding.someOneContainerLayout)

    }

    private fun viewExtras() {
        if (viewExtra) mViewDataBinding.extraContainerLayout.visibility = View.VISIBLE
        else mViewDataBinding.extraContainerLayout.visibility = View.GONE

    }

    private fun viewFlights() {
        if (viewFlight) {
            if (viewFlightDetail) {

                mViewDataBinding.flightContainerDetailLayout.visibility = View.VISIBLE
                mViewDataBinding.flightContainerLayout.visibility = View.GONE
            }

            if (viewFlightNumber) {
                mViewDataBinding.flightContainerDetailLayout.visibility = View.GONE
//                mViewDataBinding.flightContainerLayout.visibility = View.VISIBLE
                SlideAnimation.expand(mViewDataBinding.flightContainerLayout)
            }
        } else {
//            mViewDataBinding.flightContainerLayout.visibility = View.GONE
            mViewDataBinding.flightContainerDetailLayout.visibility = View.GONE
            SlideAnimation.collapse(mViewDataBinding.flightContainerLayout)
            viewFlightDetail = false
            viewFlightNumber = true
        }

    }

    private fun checkValidations(): Boolean {
        if (viewLead) {
            if (getLeadName().isEmpty()) {
                CustomToast.show(
                    mViewDataBinding.mainContainer, this@AddExtrasActivity, "Enter full name"
                )
                return false
            }
            if (getLeadNumber().isEmpty()) {
                CustomToast.show(
                    mViewDataBinding.mainContainer, this@AddExtrasActivity, "Enter phone number"
                )
                return false
            }
        }
        return true
    }

    private fun getLeadName(): String = mViewDataBinding.bookNameText.text.toString().trim()
    private fun getLeadNumber(): String = mViewDataBinding.bookNumberText.text.toString().trim()


    private fun checkFinalValidations() {

        Validations.checkValidationsForViewCapacity(viewCap,
            passengerData,
            luggageData,
            object : CapacityValueListener {
                override fun values(passenger: Int, luggage: Int) {
                    passengers = passenger
                    luggages = luggage

                }

            })


        val requestJobModel = RequestJobModel()
        val confirmBooking = ConfirmBookingModel()

        requestJobModel.quoteId = quotesResponse.id.toString()
        requestJobModel.fleetId = fleetSelected.id.toString()

        confirmBooking.pet_count = luggages.toString()
//        confirmBooking.passenger_count = passengers.toString()
        confirmBooking.passenger_count = passengerCount
        confirmBooking.wheelchair_count = wheelChairData.getValue().toString()

        val extras = ExtraModel()
        extras.passenger = passengers
        extras.pet = luggages
        extras.wheelchair = wheelChairData.getValue()

        if (viewExtra) {
            extras.infantSeat = infantSeats.getValue()
            extras.childSeat = childSeats.getValue()
            extras.boosterSeat = boosterSeats.getValue()
        }
        if (viewLead) {
            requestJobModel.leadPassengerName = mViewDataBinding.bookNameText.text.toString().trim()
            requestJobModel.leadPassengerPhone =
                mViewDataBinding.bookNumberText.text.toString().trim()
        }

        confirmBooking.flight_number = mViewDataBinding.flightNumberText.text.toString()

        /*if (viewFlight){

        }*/

        requestJobModel.extras = extras
        if (notes.isNotEmpty()) {
            requestJobModel.driverNote = notes
            confirmBooking.special_insctruction = notes
            confirmBooking.description = notes

        }

        if (addExtrasVM.pageCardData.value?.id == null && addExtrasVM.cardOfUsers.isNullOrEmpty()) {
            CustomAlertDialog(this@AddExtrasActivity).setTitle("No Card")
                .setMessage("Please add the card").setCancellable(false)
                .setPositiveText("OK") { dialog, _ ->
                    val bundle = Bundle()
                    bundle.putString(IntentConstant.EXTRA, "extra")
                    val intent = Intent(this, AddCardActivity::class.java)
                    intent.putExtra(IntentConstant.BUNDLE, bundle)
                    startActivity(intent)
                    dialog.dismiss()
                }.show()

            return
        }

//        if (checkValidations())
//            addExtrasVM.requestJob(requestJobModel)

//        confirmBooking.passenger_card_id = quotesResponse.card_id.toString()
        if (checkValidations()) addExtrasVM.requestJob(confirmBooking, fleetSelected.id)


    }

    override fun onValidated() {

        checkFinalValidations()
//        onSubmit()
    }


    private fun gotoClass(cls: Class<*>, finish: Boolean = true) {
        openNewActivity(this@AddExtrasActivity, cls, finish)
    }

    private fun gotoClass(cls: Class<*>, bundle: Bundle, finish: Boolean = true) {
        openNewActivity(this@AddExtrasActivity, cls, bundle, finish)
    }


    override fun onBackPress() {
        onBackPressed()
    }

    override fun onSubmit() {

    }

    private fun onCompleted(
        title: String,
        message: String,
        status: String?,
        id: Int?,
        pickupAddress: Location,
        dropOffAddress: Location,
        route: Route
    ) {
        preferenceHelper.setValue(PrefConstant.CARD_NUMBER, "")
        if (!isCurrently) {
            CustomAlertDialog(this).setTitle(title).setMessage(
                    message
                ).setCancellable(false).setPositiveText("OK") { dialog, _ ->
                    dialog.dismiss()
                    val i = Intent(this, NewScheduledBookingActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)
                }.show()
        } else {
            val bundle = Bundle()
            bundle.putSerializable(IntentConstant.SERIAL, route)
            val i = Intent(this, SearchDriverNewActivity::class.java)
            i.putExtra("status", status)
            i.putExtra("id", id)
            i.putExtra("pickup", pickupAddress)
            i.putExtra("drop", dropOffAddress)
            i.putExtra(IntentConstant.BUNDLE, bundle)
            startActivity(i)
        }
    }


    private fun requestJobObserver() {
        addExtrasVM.jobResponse.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dismissDialog()
//                    val response = Gson().fromJson<BaseData<Empty>>(
//                        it.data,
//                        object : TypeToken<BaseData<Empty>>() {}.type
//                    )
                    onCompleted(
                        it.data?.title ?: "",
                        it.data?.message ?: "",
                        it.data?.data?.status,
                        it.data?.data?.id,
                        it.data?.data?.pickup!!,
                        it.data?.data?.drop!!,
                        it.data?.data?.route!!
                    )

                }

                Status.ERROR -> {
                    progressDialog.dismissDialog()
                    if (it.title == "No Card") {
                        CustomAlertDialog(this@AddExtrasActivity).setTitle(it.title)
                            .setMessage(it.message).setCancellable(false)
                            .setPositiveText("OK") { dialog, _ ->
                                val bundle = Bundle()
                                bundle.putString(IntentConstant.EXTRA, "extra")
                                gotoClass(AddCardActivity::class.java, bundle, false)
                                dialog.dismiss()
                            }.show()


                    } else errorAlertDialog(it.title, it.message)
                }

                Status.LOADING -> {
                    progressDialog.show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }
}