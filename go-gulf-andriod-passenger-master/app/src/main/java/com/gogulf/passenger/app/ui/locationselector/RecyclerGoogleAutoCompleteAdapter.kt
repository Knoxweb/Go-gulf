package com.gogulf.passenger.app.ui.locationselector

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.gogulf.passenger.app.data.model.GoogleAutoCompleteData
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.RecyclerGoogleAutoCompleteBinding

class RecyclerGoogleAutoCompleteAdapter(
    private val viewModel: LocationSelectorViewModel? = null
) : ListAdapter<AutocompletePrediction, RecyclerGoogleAutoCompleteAdapter.ViewHolder>(DiffUtil()) {

    private lateinit var context: Context
    private var onAddressClickListener: OnAddressClickListener? = null


    inner class ViewHolder(private val binding: RecyclerGoogleAutoCompleteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AutocompletePrediction, onClickListener: OnClickListener) {
            val data = GoogleAutoCompleteData(
                title = item.getPrimaryText(null).toString(),
                detail = item.getFullText(null).toString()
            )
            binding.address = data
            binding.listItem.setOnClickListener(onClickListener)


            binding.listItem.background =
                AppCompatResources.getDrawable(context, R.drawable.background_middle_in_list)
            // Set background tint
            val tint = ContextCompat.getColor(context, R.color.background_top_new) // Replace R.color.tint_color with your actual color resource
            binding.listItem.backgroundTintList = ColorStateList.valueOf(tint)


//            if (adapterPosition == 0) {
//                binding.listItem.background =
//                    AppCompatResources.getDrawable(context, R.drawable.background_top_in_list)
//            }

//            else if (adapterPosition == 4) {
//                binding.listItem.background =
//                    AppCompatResources.getDrawable(context, R.drawable.background_bottom_in_list)
//            }


//            else {
//                binding.listItem.background =
//                    AppCompatResources.getDrawable(context, R.drawable.background_middle_in_list)
//                // Set background tint
//                val tint = ContextCompat.getColor(context, R.color.primary_new) // Replace R.color.tint_color with your actual color resource
//                binding.listItem.backgroundTintList = ColorStateList.valueOf(tint)
//
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerGoogleAutoCompleteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item) {
            viewModel?.onAutoCompleteClicked(item)
            onAddressClickListener?.onAddressClick()
        }
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<AutocompletePrediction>() {
        override fun areItemsTheSame(
            oldItem: AutocompletePrediction, newItem: AutocompletePrediction
        ): Boolean {
            return oldItem.placeId == newItem.placeId
        }

        override fun areContentsTheSame(
            oldItem: AutocompletePrediction, newItem: AutocompletePrediction
        ): Boolean {
            return oldItem.getFullText(null) == newItem.getFullText(null)
        }

    }

    interface OnAddressClickListener {
        fun onAddressClick()
    }

    fun setAddressClickListener(listener: OnAddressClickListener) {
        this.onAddressClickListener = listener
    }

}