package com.gogulf.passenger.app.ui.schedulebooking.fragment.confirmed

import android.annotation.SuppressLint
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.data.model.base.BaseArray
import com.gogulf.passenger.app.data.model.response.bookings.BookingModel
import com.gogulf.passenger.app.ui.base.BaseFragment
import com.gogulf.passenger.app.ui.schedulebooking.fragment.pending.PendingBookingVM
import com.gogulf.passenger.app.utils.enums.Status
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gogulf.passenger.app.databinding.FragmentBookingLayoutBinding
import org.koin.android.viewmodel.ext.android.viewModel

class ConfirmedBookingFragment : BaseFragment<FragmentBookingLayoutBinding>() {
    private lateinit var mViewDataBinding: FragmentBookingLayoutBinding
    private val bookingVM: PendingBookingVM by viewModel()

    //    private var responseModel: ScheduledBookingResponseModel? = null
//    private var dataList = ArrayList<Datum?>()
    private var selectedBookingID = ""
    private var selectedMovementID = ""

    //    private var deleteModel: Datum? = null
//    private lateinit var adapter: BookingAdapter
    private var mList = ArrayList<BookingModel>()
    override fun getLayoutId(): Int = R.layout.fragment_booking_layout

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as FragmentBookingLayoutBinding
        bookingVM.getBookingList("confirmed")
        bookingObserver()
        mViewDataBinding.historyRecyclerView.hasFixedSize()
        mViewDataBinding.historyRecyclerView.layoutManager = LinearLayoutManager(context)
//        adapter = BookingAdapter(requireContext(), HistoryType.Pending, mList)
//        mViewDataBinding.historyRecyclerView.adapter =
//            adapter
        /*    adapter.onClick(object : HistoryAdapterListener {


                override fun onClicked(movementId: String, bookingId: String) {
                    selectedMovementID = movementId
                    selectedBookingID = bookingId
    //                pendingBookingViewModel.checkCancellation(movementId)
    //                deleteModel = model
                }

            })*/
        /*    mViewDataBinding.historyRecyclerView.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1) && !isLoading) {
    //                    pendingBookingViewModel.getBookings(currentPage, "pending")
                    }
                }

            })*/
//        mViewDataBinding.pullToRefesh.setOnRefreshListener {
//            bookingVM.getBookingList("confirmed")
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
                    mList.clear()
                    mList.addAll(response.data)
                    mList.sortBy {item-> item.pickupDatetime }
//                    adapter.notifyDataSetChanged()
                    checkData()

                }
                Status.ERROR -> {
                    progressDialog.dismissDialog()

//                    mViewDataBinding.pullToRefesh.isRefreshing = false

                }
                Status.LOADING -> {

//                    mViewDataBinding.pullToRefesh.isRefreshing = true
//                    progressDialog.show()
                }
            }
        }
    }

    private fun checkData(){
        if (mList.size > 0) {
            mViewDataBinding.historyRecyclerView.visibility = View.VISIBLE
            mViewDataBinding.noDataLayout.root.visibility = View.GONE
        } else {
            mViewDataBinding.historyRecyclerView.visibility = View.GONE
            mViewDataBinding.noDataLayout.root.visibility = View.VISIBLE
            mViewDataBinding.noData = "No Booking Found"

        }
    }
}