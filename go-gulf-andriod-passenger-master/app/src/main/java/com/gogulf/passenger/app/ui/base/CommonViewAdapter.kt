package com.gogulf.passenger.app.ui.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.ui.settings.account.AccountInformationModel
import com.gogulf.passenger.app.utils.objects.Constants
import com.gogulf.passenger.app.databinding.LayoutTextViewFaresBinding

class CommonViewAdapter(
    private val mList: ArrayList<AccountInformationModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var loading = false

    @SuppressLint("NotifyDataSetChanged")
    fun setLoading(loading: Boolean) {
        this.loading = loading
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == Constants.VIEW_DATA) {
            val inflater = LayoutInflater.from(parent.context)
            val vi = LayoutTextViewFaresBinding.inflate(inflater, parent, false)
            BaseDataViewHolder(vi as ViewDataBinding)
        } else {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.layout_shimmer_notifcation, parent, false)
            BaseLoadingAdapter(view)
        }

    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == Constants.VIEW_DATA) {
            val holds = holder as BaseDataViewHolder
            val hold = holds.binding as LayoutTextViewFaresBinding
            val data = mList[position]
            hold.label = data

        }
    }

    override fun getItemViewType(position: Int): Int =
        if (loading)
            Constants.VIEW_LOADING
        else
            Constants.VIEW_DATA
}