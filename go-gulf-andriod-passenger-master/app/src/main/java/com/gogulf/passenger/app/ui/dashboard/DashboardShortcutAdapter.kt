package com.gogulf.passenger.app.ui.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.adapters.ImageViewBindingAdapter.setImageDrawable
import androidx.recyclerview.widget.RecyclerView
import com.gogulf.passenger.app.ui.shortcuts.ShortcutAddModel
import com.gogulf.passenger.app.utils.StringUtils
import com.gogulf.passenger.app.utils.interfaces.DashboardShortcutListener
import com.gogulf.passenger.app.utils.objects.GetDrawableIcons
import com.gogulf.passenger.app.databinding.LayoutShortcutItemsBinding

class DashboardShortcutAdapter(
    private val context: Context,
    private val mList: ArrayList<ShortcutAddModel>,
    private val dashboardShortcutListener: DashboardShortcutListener
) :
    RecyclerView.Adapter<DashboardShortcutAdapter.ShortcutViewHolder>() {

    private var viewIcon = false

    @SuppressLint("NotifyDataSetChanged")
    fun updateIcon(icon: Boolean) {
        viewIcon = icon
        notifyDataSetChanged()
    }

    inner class ShortcutViewHolder(items: LayoutShortcutItemsBinding) :
        RecyclerView.ViewHolder(items.root) {
        val bind = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortcutViewHolder {
        return ShortcutViewHolder(
            LayoutShortcutItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShortcutViewHolder, position: Int) {
        val data = mList[position]
        holder.bind.menuNameTitle.text = StringUtils.capitalizeWords(data.name)
        holder.bind.menuNameLocation.text = data.address

        holder.bind.editIcon.setOnClickListener {
            dashboardShortcutListener.editShortcut(data)
        }

        holder.bind.addImageIcon.setImageDrawable(
            data.name?.let {
                GetDrawableIcons.getDrawables(
                    context,
                    it
                )
            }
        )
        holder.bind.deleteIcon.setOnClickListener {
            dashboardShortcutListener.deleteShortcut(data)
        }

        if (viewIcon) {
            holder.bind.editIcon.visibility = View.VISIBLE
            holder.bind.deleteIcon.visibility = View.VISIBLE
        } else {

            holder.bind.editIcon.visibility = View.GONE
            holder.bind.deleteIcon.visibility = View.GONE
        }


    }

    override fun getItemCount(): Int = mList.size

}