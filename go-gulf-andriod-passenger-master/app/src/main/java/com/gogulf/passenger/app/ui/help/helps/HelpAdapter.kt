package com.gogulf.passenger.app.ui.help.helps

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.ui.menu.MenuModels
import com.gogulf.passenger.app.utils.interfaces.OnDialogClicked
import com.gogulf.passenger.app.databinding.LayoutMenuItemsCardBinding

class HelpAdapter(
    private val context: Context,
    private val mList: ArrayList<MenuModels>,
    private val simpleClick: OnDialogClicked
) : RecyclerView.Adapter<HelpAdapter.FAQViewHolder>() {
    inner class FAQViewHolder(itemView: LayoutMenuItemsCardBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val bind = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder =
        FAQViewHolder(
            LayoutMenuItemsCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        val data = mList[position]
        holder.bind.displayName.text = data.menuName
        holder.bind.displayIcon.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_faq
            )
        )
        holder.bind.container.setOnClickListener {
            simpleClick.onClicked(holder.adapterPosition)
        }
        if (mList.size - 1 == position) {
            holder.bind.divider.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = mList.size
}