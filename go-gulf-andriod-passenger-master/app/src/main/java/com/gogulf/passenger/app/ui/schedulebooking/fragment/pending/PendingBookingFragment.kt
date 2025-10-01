package com.gogulf.passenger.app.ui.schedulebooking.fragment.pending

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.gogulf.passenger.app.App.Companion.preferenceHelper
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.data.model.base.BaseArray
import com.gogulf.passenger.app.data.model.response.bookings.BookingModel
import com.gogulf.passenger.app.data.model.response.bookings.BookingModelFirebase
import com.gogulf.passenger.app.ui.base.BaseFragment
import com.gogulf.passenger.app.ui.schedulebooking.adapter.ScheduledBookingAdapter
import com.gogulf.passenger.app.utils.enums.Status
import com.gogulf.passenger.app.utils.objects.DebugMode
import com.gogulf.passenger.app.utils.objects.PrefConstant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gogulf.passenger.app.databinding.FragmentBookingLayoutBinding
import org.koin.android.viewmodel.ext.android.viewModel

class PendingBookingFragment : BaseFragment<FragmentBookingLayoutBinding>() {
    private lateinit var mViewDataBinding: FragmentBookingLayoutBinding
    private val bookingVM: PendingBookingVM by viewModel()
    private val TAG = "PendingBookingFragment"

    //    private var responseModel: ScheduledBookingResponseModel? = null
//    private var dataList = ArrayList<Datum?>()
    private var selectedBookingID = ""
    private var selectedMovementID = ""

    //    private var deleteModel: Datum? = null
//    private lateinit var adapter: BookingAdapter
    private lateinit var scheduledBookingAdapter: ScheduledBookingAdapter
//    private var mList = ArrayList<BookingModel>()
    private var mListFirebase = ArrayList<BookingModelFirebase>()
    override fun getLayoutId(): Int = R.layout.fragment_booking_layout

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as FragmentBookingLayoutBinding
//        bookingVM.getBookingList("pending")
//        bookingObserver()
        mViewDataBinding.historyRecyclerView.hasFixedSize()
        mViewDataBinding.historyRecyclerView.layoutManager = LinearLayoutManager(context)
//        adapter = BookingAdapter(requireContext(), HistoryType.Pending, mList)



//        mViewDataBinding.pullToRefesh.setOnRefreshListener {
//            mViewDataBinding.pullToRefesh.isRefreshing = false
//        }
    }

    var isLoading = false
    var currentPage = 1

    @SuppressLint("NotifyDataSetChanged")
    private fun bookingObserver() {
        bookingVM.pendingResponse.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dismissDialog()

//                    mViewDataBinding.pullToRefesh.isRefreshing = false
                    val response = Gson().fromJson<BaseArray<BookingModel>>(
                        it.data,
                        object : TypeToken<BaseArray<BookingModel>>() {}.type
                    )
//                    mList.clear()
//                    mList.addAll(response.data)
//                    mList.sortBy {item-> item.pickupDatetime }
//                    adapter.notifyDataSetChanged()
                    checkData()

                }
                Status.ERROR -> {
                    progressDialog.dismissDialog()

//                    mViewDataBinding./.isRefreshing = false

                }
                Status.LOADING -> {

//                    mViewDataBinding.pullToRefesh.isRefreshing = true
//                    progressDialog.show()
                }
            }
        }
    }

    private fun checkData() {
//        if (mList.size > 0) {
//            mViewDataBinding.historyRecyclerView.visibility = View.VISIBLE
//            mViewDataBinding.noDataLayout.root.visibility = View.GONE
//        } else {
//            mViewDataBinding.historyRecyclerView.visibility = View.GONE
//            mViewDataBinding.noDataLayout.root.visibility = View.VISIBLE
//            mViewDataBinding.noData = "No Booking Found"
//
//        }
    }

    var myRef: DatabaseReference? = null
    override fun onPause() {
        super.onPause()
        log("onPauseDestroy Listener")
        myRef?.removeEventListener(databaseListener)
    }

    override fun onResume() {
        super.onResume()
        log("openListener on Resume Listener")
        realTimeDatabase()
    }

    private fun realTimeDatabase() {
        val database = Firebase.database
        val identity = preferenceHelper.getValue(PrefConstant.IDENTITY, "") as String
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
//                adapter.setLoading(false)
//                adapter.notifyDataSetChanged()
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

}