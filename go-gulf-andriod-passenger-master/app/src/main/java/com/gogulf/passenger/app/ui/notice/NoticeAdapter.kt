package com.gogulf.passenger.app.ui.notice

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.gogulf.passenger.app.data.model.response.notification.NoticeModel
import com.gogulf.passenger.app.ui.base.BaseDataViewHolder
import com.gogulf.passenger.app.ui.base.BaseLoadingAdapter
import com.gogulf.passenger.app.utils.interfaces.NotificationClickListener
import com.gogulf.passenger.app.utils.objects.Constants.VIEW_DATA
import com.gogulf.passenger.app.utils.objects.Constants.VIEW_LOADING
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.LayoutNoticeItemBinding


class NoticeAdapter(
    private val mList: ArrayList<NoticeModel>,
    private val menuClickListener: NotificationClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var loading = true

    @SuppressLint("NotifyDataSetChanged")
    fun setLoading(loading: Boolean) {
        this.loading = loading
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == VIEW_DATA) {
            val inflater = LayoutInflater.from(parent.context)
            val vi = LayoutNoticeItemBinding.inflate(inflater, parent, false)
            BaseDataViewHolder(vi as ViewDataBinding)
        } else {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.layout_shimmer_notifcation, parent, false)
            BaseLoadingAdapter(view)
        }

    override fun getItemCount(): Int = if (loading) 2 else mList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_DATA) {
            val holds = holder as BaseDataViewHolder
            val hold = holds.binding as LayoutNoticeItemBinding
            val data = mList[position]
            hold.notification = data
            if (data.is_seen == false) {
                hold.notificationDot.visibility = View.VISIBLE
            } else {
                hold.notificationDot.visibility = View.GONE
            }
            hold.mainContainer.setOnClickListener {
                hold.notificationDot.visibility = View.GONE
                menuClickListener.onClicked(data)
            }
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (loading)
            VIEW_LOADING
        else
            VIEW_DATA
}