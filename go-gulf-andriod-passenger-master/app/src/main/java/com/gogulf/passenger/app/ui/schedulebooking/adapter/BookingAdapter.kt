package com.gogulf.passenger.app.ui.schedulebooking.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gogulf.passenger.app.data.model.CurrentBookingResponseData
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.ui.base.BaseLoadingAdapter
import com.gogulf.passenger.app.ui.bookingdetail.BookingDetailActivity
import com.gogulf.passenger.app.utils.enums.HistoryType
import com.gogulf.passenger.app.utils.interfaces.HistoryAdapterListener
import com.gogulf.passenger.app.utils.objects.Constants.VIEW_DATA
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.databinding.LayoutBookingItemScheduledBinding

class BookingAdapter(
    private val context: Context,
    private val historyType: HistoryType,
    private val mList: ArrayList<CurrentBookingResponseData>
//    ,
//    private val dataList: ArrayList<Datum?>?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var adapterListener: HistoryAdapterListener? = null
    private var loading = true

    fun onClick(adapterListener: HistoryAdapterListener) {
        this.adapterListener = adapterListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setLoading(loading: Boolean) {
        this.loading = loading
        notifyDataSetChanged()
    }

    inner class JobHistoryViewHolder(itemView: LayoutBookingItemScheduledBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val bind = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == VIEW_DATA)
            JobHistoryViewHolder(
                LayoutBookingItemScheduledBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        else {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.layout_account_information_loading, parent, false)
            BaseLoadingAdapter(view)
        }


    override fun onBindViewHolder(hold: RecyclerView.ViewHolder, position: Int) {

        if (hold.itemViewType == VIEW_DATA) {
            val holder = hold as JobHistoryViewHolder
            val models = mList[position]
            hold.bind.bookingModel = models
            holder.bind.mainContainer.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(IntentConstant.BOOKING_ID, models.id.toString())
                gotoClass(BookingDetailActivity::class.java,bundle)
            }

        }


    }

    /*   override fun getItemCount(): Int = if (!loading) dataList.size else {
           if (dataList.size < 4)
               4
           else
               dataList.size
       }*/
    override fun getItemCount(): Int = mList.size

    private fun gotoClass(cls: Class<*>, bundle: Bundle) {
        val intent = Intent(context, cls)
        intent.putExtra("bundle", bundle)
        context.startActivity(intent)
    }

    private fun gotoClass(cls: Class<*>) {
        val intent = Intent(context, cls)
        context.startActivity(intent)
    }

    /* override fun getItemViewType(position: Int): Int =
         if (loading) Constants.VIEW_LOADING else Constants.VIEW_DATA*/
    override fun getItemViewType(position: Int): Int =
        VIEW_DATA
}