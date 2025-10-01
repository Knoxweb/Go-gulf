package com.gogulf.passenger.app.ui.schedulebooking.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gogulf.passenger.app.data.model.CurrentBookingResponseData
import com.gogulf.passenger.app.ui.bookingdetail.BookingDetailActivity
import com.gogulf.passenger.app.ui.schedulebooking.activity.ScheduledBookingViewModel
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.utils.objects.IntentConstant
import com.gogulf.passenger.app.databinding.LayoutScheduledBookingItemsFirebaseBinding

class ScheduledBookingAdapter(
    private val scheduledBookingViewModel: ScheduledBookingViewModel,
    private val mList: ArrayList<CurrentBookingResponseData>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class JobHistoryViewHolder(itemView: LayoutScheduledBookingItemsFirebaseBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val bind = itemView
    }

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder = JobHistoryViewHolder(
            LayoutScheduledBookingItemsFirebaseBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        context = parent.context
        return holder
    }


    override fun onBindViewHolder(hold: RecyclerView.ViewHolder, position: Int) {
        val holder = hold as JobHistoryViewHolder
        val models = mList[position]
        hold.bind.bookingModel = models
        holder.bind.mainContainer.setOnClickListener {
//                listener.onDetail(models.id, models)
            val bundle = Bundle()
            bundle.putString(IntentConstant.BOOKING_ID, models.id.toString())
            bundle.putSerializable(IntentConstant.SERIAL, models)
            val intent = Intent(context, BookingDetailActivity::class.java)
            intent.putExtra(IntentConstant.BUNDLE, bundle)
            context?.startActivity(intent)

        }

        hold.bind.cancelBooking.setOnClickListener {
//                listener.onClicked(models.id.toString())
            var message = "Are you sure you want to cancel this booking to ${models.drop?.name}?"
            if (models.current_status=="stand_by") {
                message = "$message* 50% of fare will be charged."
            }
            CustomAlertDialog(context).setTitle("Cancel Booking?")
                .setMessage(message)
                .setPositiveText("Yes") { dialog, _ ->
                    dialog.dismiss()
                    scheduledBookingViewModel.hitCancelBooking(models.id)
                }.setNegativeText("No") { dialog, _ -> dialog.dismiss() }.setCancellable(true)
                .show()
        }

    }

    override fun getItemCount(): Int = mList.size

}