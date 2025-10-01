package com.gogulf.passenger.app.ui.getaride

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.gogulf.passenger.app.ui.shortcuts.ShortcutAddModel
import com.gogulf.passenger.app.utils.interfaces.ShortcutMapListener
import com.gogulf.passenger.app.utils.objects.GetDrawableIcons
import com.gogulf.passenger.app.databinding.LayoutCardShortcutSearchDestinationBinding

class ShortcutAdapter(
    private val context: Context,
    private var mList: ArrayList<ShortcutAddModel>?,
    private val shortcutMapListener: ShortcutMapListener,
    private val showAll: Boolean = false
) : RecyclerView.Adapter<ShortcutAdapter.ShortcutViewHolder>() {

    inner class ShortcutViewHolder(items: LayoutCardShortcutSearchDestinationBinding) :
        ViewHolder(items.root) {
        val bind = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortcutViewHolder {
        return ShortcutViewHolder(
            LayoutCardShortcutSearchDestinationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShortcutViewHolder, position: Int) {
        if (mList != null) {
            val data = mList!![position]
            holder.bind.shortcutTitle.text = data.title


            holder.bind.shortcutImage.setImageDrawable(
                data.title?.let {
                    GetDrawableIcons.getDrawables(
                        context,
                        it
                    )
                }
            )



            holder.bind.layoutContainer.setOnClickListener {
                shortcutMapListener.onClicked(data)
            }
        }

    }

    override fun getItemCount(): Int {
        if (mList != null) {
            if (showAll) {
                return mList!!.size
            } else {
                return if (mList!!.size > 3) 3 else mList!!.size
            }
        }
       return 0
    }


    fun submitList(mList: List<ShortcutAddModel>) {
        this.mList = mList as ArrayList<ShortcutAddModel>
        notifyDataSetChanged()
    }
}