package com.gogulf.passenger.app.ui.base

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BaseLoadingAdapter(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
}
class BaseDataViewHolder(itemView: ViewDataBinding) : RecyclerView.ViewHolder(itemView.root) {
    val binding = itemView
}