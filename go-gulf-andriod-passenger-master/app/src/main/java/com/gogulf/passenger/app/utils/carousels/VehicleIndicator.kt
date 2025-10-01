package com.gogulf.passenger.app.utils.carousels

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.data.model.response.bookings.Fleet
import com.gogulf.passenger.app.databinding.LayoutIndicatorSecondBinding


class VehicleIndicator(
    private var context: Context,
    private var models:  ArrayList<Fleet>
) : RecyclerView.Adapter<VehicleIndicator.SliderViewHolder>() {

    var currentIndex = 0


    @SuppressLint("NotifyDataSetChanged")
    fun updateIndicator(currentIndex: Int) {
        this.currentIndex = currentIndex
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            LayoutIndicatorSecondBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        if (currentIndex == holder.adapterPosition) {
            holder.bind.selectedIndex.background =
                ContextCompat.getDrawable(
                    context,
                    R.drawable.active_vehicle_cirlce
                )

        } else {
            holder.bind.selectedIndex.background =
                ContextCompat.getDrawable(
                    context,
                    R.drawable.inactive_vehicle_circle
                )

        }
    }

    override fun getItemCount(): Int {
        return models.size
    }

    class SliderViewHolder(binding: LayoutIndicatorSecondBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var bind = binding


    }
}