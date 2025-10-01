package com.gogulf.passenger.app.ui.legals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.gogulf.passenger.app.databinding.LayoutTermsAndConditionItemsBinding

class LegalAdapters(
    private val mList: ArrayList<LegalModels>
) : RecyclerView.Adapter<LegalAdapters.LegalViewHolder>() {

    inner class LegalViewHolder(items: LayoutTermsAndConditionItemsBinding) :
        ViewHolder(items.root) {
        val bind = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegalViewHolder {
        return LegalViewHolder(
            LayoutTermsAndConditionItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(holder: LegalViewHolder, position: Int) {
        val model = mList[position]

        if (model.mainTitles == "") {
            holder.bind.mainTitle.visibility = View.GONE
        } else {
            holder.bind.mainTitle.visibility = View.VISIBLE
        }
        if (model.subTitles == "") {
            holder.bind.subTitle.visibility = View.GONE
        } else {
            holder.bind.subTitle.visibility = View.VISIBLE
        }
        if (model.contents == "") {
            holder.bind.content.visibility = View.GONE
        } else {
            holder.bind.content.visibility = View.VISIBLE
        }
        holder.bind.models = model
    }
}