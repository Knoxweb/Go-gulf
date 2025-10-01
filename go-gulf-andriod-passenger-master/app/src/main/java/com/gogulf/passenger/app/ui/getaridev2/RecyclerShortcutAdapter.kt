package com.gogulf.passenger.app.ui.getaridev2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gogulf.passenger.app.data.model.ShortcutModelResponse
import com.gogulf.passenger.app.ui.shortcuts.ShortcutsActivity
import com.gogulf.passenger.app.ui.trips.TripDestinationV2ViewModel
import com.gogulf.passenger.app.databinding.RecyclerShortcutBinding

class RecyclerShortcutAdapter(private val viewModel: TripDestinationV2ViewModel) :
    ListAdapter<ShortcutModelResponse, RecyclerShortcutAdapter.ViewHolder>(DiffUtil()) {

    private lateinit var context: Context


    inner class ViewHolder(private val binding: RecyclerShortcutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(x: ShortcutModelResponse, onClickListener: OnClickListener) {
            binding.data = x
            binding.mainContainer.setOnClickListener(onClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerShortcutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item) {
            if (item.name == "Add") {
                context.startActivity(
                    Intent(
                        context, ShortcutsActivity::class.java
                    )
                )
            } else {
                viewModel.getDestination(item)
//                context.startActivity(
//                    Intent(
//                        context, ShortcutsActivity::class.java
//                    )
//                )
            }
        }

    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<ShortcutModelResponse>() {
        override fun areItemsTheSame(
            oldItem: ShortcutModelResponse, newItem: ShortcutModelResponse
        ): Boolean {
            return newItem.name == oldItem.name
        }

        override fun areContentsTheSame(
            oldItem: ShortcutModelResponse, newItem: ShortcutModelResponse
        ): Boolean {
            return oldItem == newItem
        }

    }





}