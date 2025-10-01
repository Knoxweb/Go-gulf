package com.gogulf.passenger.app.ui.schedulebooking.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.gogulf.passenger.app.App
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.data.model.base.BaseData
import com.gogulf.passenger.app.data.model.response.bookings.BookingDetailModel
import com.gogulf.passenger.app.data.model.response.bookings.BookingModel
import com.gogulf.passenger.app.data.model.response.bookings.BookingModelFirebase
import com.gogulf.passenger.app.ui.base.BaseActivity
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.base.BaseObserverListener
import com.gogulf.passenger.app.ui.bookingdetail.BookingDetailActivity
import com.gogulf.passenger.app.ui.getaride.GetARideActivity
import com.gogulf.passenger.app.ui.schedulebooking.adapter.BookingAdapter
import com.gogulf.passenger.app.ui.schedulebooking.adapter.ScheduledBookingAdapter
import com.gogulf.passenger.app.ui.schedulebooking.fragment.pending.PendingBookingVM
import com.gogulf.passenger.app.utils.enums.Status
import com.gogulf.passenger.app.utils.interfaces.ApiListener
import com.gogulf.passenger.app.utils.objects.DebugMode
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.utils.objects.PrefConstant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.gogulf.passenger.app.utils.SystemBarUtil
import com.gogulf.passenger.app.databinding.ActivityScheduledBookingNewBinding
import org.koin.android.viewmodel.ext.android.viewModel

class ScheduledBookingActivity : BaseActivity<ActivityScheduledBookingNewBinding>(),
    BaseNavigation {
    private lateinit var mViewDataBinding: ActivityScheduledBookingNewBinding
    private val TAG = "ScheduledBookingActivity"
    private val bookingVM: PendingBookingVM by viewModel()

    //    private lateinit var tabLayout: TabLayoutComponent
    private var currentId = "0"
//    private var classType = NavDestination.ClassType.GetARide

    /** New update code for adapters */
    private lateinit var adapter: BookingAdapter
    private lateinit var scheduledBookingAdapter: ScheduledBookingAdapter
    private var mList = ArrayList<BookingModel>()
    private var mListFirebase = ArrayList<BookingModelFirebase>()
    private var bookingModels: BookingDetailModel? = null
    private var bookingID = ""
    private var detailCounter = 0

    override fun getLayoutId(): Int = R.layout.activity_scheduled_booking_new

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityScheduledBookingNewBinding
//        tabLayout = TabLayoutComponent(this, mViewDataBinding.tabLayout)

        mViewDataBinding.toolbarViewModel = commonViewModel
        commonViewModel.navigator = this

        mViewDataBinding.activity = this
        /** Recycler View */
        mViewDataBinding.historyRecyclerView.hasFixedSize()
        mViewDataBinding.historyRecyclerView.layoutManager =
            LinearLayoutManager(this@ScheduledBookingActivity)

//        scheduledBookingAdapter =
//            ScheduledBookingAdapter(
//                this@ScheduledBookingActivity, mListFirebase,
//                object : AdapterListener {
//                    override fun onClicked(id: String) {
//                        bookingVM.cancelBooking(id)
//                    }
//
//                    override fun onDetail(bookingId: String) {
//                        bookingID = bookingId
//                        if (detailCounter == 0)
//                            bookingVM.getBookingDetail(bookingId)
//                        detailCounter++
//                    }
//
//                }
//            )
        mViewDataBinding.historyRecyclerView.adapter =
            scheduledBookingAdapter
        mViewDataBinding.pullToRefesh.setOnRefreshListener {
            mViewDataBinding.pullToRefesh.isRefreshing = false
        }

        log("openListener on Resume Listener")
        realTimeDatabase()
        cancelObserver()
        bookingDetailObserver()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarUtil.enableEdgeToEdge(this)
    }


    private fun gotoClass(cls: Class<*>) {
        openNewActivity(this@ScheduledBookingActivity, cls, true)

    }
    private fun gotoClass(cls: Class<*>, bundle: Bundle, finish: Boolean = false) {
        openNewActivity(this@ScheduledBookingActivity, cls, bundle, finish)
    }
    private fun bookingDetailObserver() {
        BaseObserverListener.observe(bookingVM.detailResponse, this, object : ApiListener {
            override fun onError(title: String?, message: String?) {
                progressDialog.dismissDialog()
                detailCounter = 0
                errorAlertDialog(title,message)
            }

            override fun onLoading() {
                progressDialog.show()
            }

            override fun onSuccess(data: JsonObject?) {
                detailCounter = 0
                progressDialog.dismissDialog()
                val response = Gson().fromJson<BaseData<BookingDetailModel>>(
                    data,
                    object : TypeToken<BaseData<BookingDetailModel>>() {}.type
                )
                /*  val response: BaseData<BookingDetailModel> =
                      GetResponseModel<BookingDetailModel>().model(data)*/

                bookingModels = response.data
                gotoDetailPage()
            }

        })
    }

    private fun gotoDetailPage() {
        val bundle = Bundle()
        bundle.putString(IntentConstant.BOOKING_ID, bookingID)
        bundle.putSerializable(IntentConstant.SERIAL, bookingModels)
        gotoClass(BookingDetailActivity::class.java, bundle)
    }


    override fun onValidated() {
    }

    override fun onBackPress() {
        onBackPressed()
    }

    override fun onBackPressed() {
        if (isTaskRoot) {
            startActivity(Intent(this@ScheduledBookingActivity, GetARideActivity::class.java))
            // using finish() is optional, use it if you do not want to keep currentActivity in stack
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onSubmit() {
    }

    var myRef: DatabaseReference? = null
    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy Listener")
        myRef?.removeEventListener(databaseListener)
    }

    override fun onResume() {
        super.onResume()
    }


    private fun realTimeDatabase() {
        val database = Firebase.database
        val identity = App.preferenceHelper.getValue(PrefConstant.IDENTITY, "") as String
        myRef =
            database.getReference("my_bookings").child(identity)/*.orderByChild("pickup_datetime")
                .limitToLast(20)*/
        myRef?.addValueEventListener(databaseListener)

    }

    private val databaseListener = object : ValueEventListener {
        @SuppressLint("NotifyDataSetChanged")
        override fun onDataChange(snapshot: DataSnapshot) {

            if (!snapshot.exists()) {
                mListFirebase.clear()
//                scheduledBookingAdapter.setLoading(false)
                scheduledBookingAdapter.notifyDataSetChanged()
                checkUI()
                return
            }
            mListFirebase.clear()
            for (data in snapshot.children) {
                val bookingModels = data.getValue(BookingModelFirebase::class.java)
                DebugMode.e(TAG, "Value is: $bookingModels")
                mListFirebase.add(bookingModels!!)


            }
//            scheduledBookingAdapter.setLoading(false)
            scheduledBookingAdapter.notifyDataSetChanged()
            checkUI()


        }

        override fun onCancelled(error: DatabaseError) {
            // Failed to read value
            Log.w(TAG, "Failed to read value.", error.toException())
        }

    }

    private fun checkUI() {
        if (mListFirebase.size > 0) {
            mViewDataBinding.historyRecyclerView.visibility = View.VISIBLE
            mViewDataBinding.noDataLayout.root.visibility = View.GONE
        } else {
            mViewDataBinding.historyRecyclerView.visibility = View.GONE
            mViewDataBinding.noDataLayout.root.visibility = View.VISIBLE
            mViewDataBinding.noData = "No Booking Found"

        }
    }

    private fun cancelObserver() {
        bookingVM.cancelResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showDialog()

                }
                Status.SUCCESS -> {
                    hideDialog()

                }
                Status.ERROR -> {
                    hideDialog()
                    errorAlertDialog(it.title, it.message)

                }
            }
        }
    }
}