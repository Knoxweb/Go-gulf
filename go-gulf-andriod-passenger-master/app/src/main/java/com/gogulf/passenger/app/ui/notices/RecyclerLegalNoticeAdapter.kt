package com.gogulf.passenger.app.ui.notices

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gogulf.passenger.app.data.model.X
import com.gogulf.passenger.app.databinding.RecyclerPoliciesXBinding

class RecyclerLegalNoticeAdapter :
    ListAdapter<X, RecyclerLegalNoticeAdapter.ViewHolder>(DiffUtil()) {

    private lateinit var context: Context
    private var adapter: RecyclerLegalNoticeYAdapter? = null

    inner class ViewHolder(private val binding: RecyclerPoliciesXBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(x: X) {
            binding.tvHeading.text = x.heading + "\n"
            binding.rvY.layoutManager = LinearLayoutManager(context)
            binding.rvY.adapter = adapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerPoliciesXBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        adapter = item.list?.let { RecyclerLegalNoticeYAdapter(it) }
        holder.bind(item)
    }

    class DiffUtil :
        androidx.recyclerview.widget.DiffUtil.ItemCallback<X>() {
        override fun areItemsTheSame(
            oldItem: X, newItem: X
        ): Boolean {
            return newItem.heading == oldItem.heading
        }

        override fun areContentsTheSame(
            oldItem: X, newItem: X
        ): Boolean {
            return oldItem == newItem
        }

    }


}