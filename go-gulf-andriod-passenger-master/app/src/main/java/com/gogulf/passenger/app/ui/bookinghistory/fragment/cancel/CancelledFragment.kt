package com.gogulf.passenger.app.ui.bookinghistory.fragment.cancel

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.ui.base.BaseFragment
import com.gogulf.passenger.app.ui.base.BaseObserverListener
import com.gogulf.passenger.app.ui.bookingdetail.BookingDetailActivity
import com.gogulf.passenger.app.ui.bookinghistory.adapter.HistoryBookingAdapter
import com.gogulf.passenger.app.ui.bookinghistory.vms.HistoryVM
import com.gogulf.passenger.app.utils.enums.Status
import com.gogulf.passenger.app.utils.interfaces.AdapterClickListener
import com.gogulf.passenger.app.utils.interfaces.ApiListener
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.google.gson.JsonObject
import com.gogulf.passenger.app.data.model.CurrentBookingResponseData
import com.gogulf.passenger.app.databinding.FragmentBookingLayoutBinding
import org.koin.android.viewmodel.ext.android.viewModel

class CancelledFragment : BaseFragment<FragmentBookingLayoutBinding>() {
    private lateinit var mViewDataBinding: FragmentBookingLayoutBinding
    private val bookingVM: HistoryVM by viewModel()

    //    private var responseModel: ScheduledBookingResponseModel? = null
//    private var dataList = ArrayList<Datum?>()
    private var selectedBookingID = ""
    private var selectedMovementID = ""

    //    private var deleteModel: Datum? = null
    private lateinit var adapter: HistoryBookingAdapter
    private var mList = ArrayList<CurrentBookingResponseData>()

    private var bookingModels: CurrentBookingResponseData? = null
    private var bookingID = ""
    private var detailCounter = 0
    override fun getLayoutId(): Int = R.layout.fragment_booking_layout

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as FragmentBookingLayoutBinding
        bookingVM.getBookingListFromFirebase("cancelled")
        bookingObserver()
        mViewDataBinding.historyRecyclerView.hasFixedSize()
        mViewDataBinding.historyRecyclerView.layoutManager = LinearLayoutManager(context)
//        adapter = HistoryBookingAdapter(requireContext(), mList, object : AdapterClickListener {
//            override fun onDetail(bookingId: String) {
//                if (detailCounter == 0)
//                    bookingVM.getBookingDetail(bookingId)
//                bookingID = bookingId
//                detailCounter++
//            }
//
//        })
        adapter = HistoryBookingAdapter(requireContext(), mList, object : AdapterClickListener {
            override fun onDetail(booking: Int?, model: CurrentBookingResponseData) {
                bookingID = booking.toString()
                bookingModels = model
                detailCounter++
                gotoDetailPage()
            }
        })

        mViewDataBinding.historyRecyclerView.adapter =
            adapter


        bookingDetailObserver()
    }


    private fun bookingDetailObserver() {
        BaseObserverListener.observe(bookingVM.detailResponse, this, object : ApiListener {
            override fun onError(title: String?, message: String?) {
                progressDialog.dismissDialog()
                detailCounter = 0
            }

            override fun onLoading() {
                progressDialog.show()
            }

            override fun onSuccess(data: JsonObject?) {
                detailCounter = 0
                progressDialog.dismissDialog()
//                val response = Gson().fromJson<BaseData<BookingDetailModel>>(
//                    data, object : TypeToken<BaseData<BookingDetailModel>>() {}.type
//                )
//                bookingModels = response.data
//                gotoDetailPage()
            }

        })

    }

    private fun gotoDetailPage() {
//        val bundle = Bundle()
//        bundle.putString(IntentConstant.BOOKING_ID, bookingID)
//        bundle.putSerializable(IntentConstant.SERIAL, bookingModels)
//        gotoClass(BookingDetailActivity::class.java, bundle)
        val bundle = Bundle()
        bundle.putString(IntentConstant.BOOKING_ID, bookingID)
        bundle.putSerializable(IntentConstant.SERIAL, bookingModels)
        val intent = Intent(requireContext(), BookingDetailActivity::class.java)
        intent.putExtra(IntentConstant.BUNDLE, bundle)
        startActivity(intent)
    }

    private fun gotoClass(cls: Class<*>, bundle: Bundle, finish: Boolean = false) {
        openNewActivity(requireActivity(), cls, bundle, finish)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun bookingObserver() {
        bookingVM.pendingResponse.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dismissDialog()


                    mList.clear()
                    it.data?.let { it1 -> mList.addAll(it1) }
                    adapter.notifyDataSetChanged()
                    checkData()

                }

                Status.ERROR -> {
                    progressDialog.dismissDialog()

                    checkData()

                }

                Status.LOADING -> {
                    progressDialog.show()
                }
            }
        }
    }

    private fun checkData() {
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