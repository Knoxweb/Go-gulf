package com.gogulf.passenger.app.ui.shortcuts

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.utils.interfaces.ShortcutClickListener
import com.gogulf.passenger.app.databinding.LayoutShortcutItemsDashboardBinding

class ShortcutAdapters(
    private val context: Context,
    private val mList: ArrayList<ShortcutModel>,
    private val shortcutClickListener: ShortcutClickListener
) : RecyclerView.Adapter<ShortcutAdapters.ShortcutViewHolder>() {

    private var selectedIndex = 0
    private var selectedTitle = "Home"

    inner class ShortcutViewHolder(itemView: LayoutShortcutItemsDashboardBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val bind = itemView
    }

    @SuppressLint("NotifyDataSetChanged")
    fun highLightSelected(title: String) {
        selectedTitle = title
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortcutViewHolder =
        ShortcutViewHolder(
            LayoutShortcutItemsDashboardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ShortcutViewHolder, position: Int) {
        val data = mList[position]
        holder.bind.shortcutName.text = data.title
        holder.bind.shortcutImage.setImageDrawable(ContextCompat.getDrawable(context, data.icon))
        holder.bind.mainContainer.setOnClickListener {
            shortcutClickListener.onClicked(data.title, holder.adapterPosition)
            selectedIndex = holder.adapterPosition
            selectedTitle = data.title
            notifyDataSetChanged()
        }
        if (holder.adapterPosition == selectedIndex) {
            holder.bind.mainContainer.background =
                ContextCompat.getDrawable(context, R.drawable.background_shortcut_selection)
        } else {
            holder.bind.mainContainer.background = null
        }
        if (selectedTitle.isEmpty()) {
            holder.bind.mainContainer.background = null
        } else if (selectedTitle.lowercase() == data.title.lowercase()) {
            holder.bind.mainContainer.background =
                ContextCompat.getDrawable(context, R.drawable.background_shortcut_selection)
        }
        else{
            holder.bind.mainContainer.background = null
        }

    }

    override fun getItemCount(): Int = mList.size
}