package com.gogulf.passenger.app.ui.help.helpdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gogulf.passenger.app.data.model.response.FAQ
import com.gogulf.passenger.app.databinding.LayoutFaqDetailPageBinding

class HelpDetailAdapter (
    private val mList: ArrayList<FAQ>
) : RecyclerView.Adapter<HelpDetailAdapter.FAQDetailViewHolder>() {

    inner class FAQDetailViewHolder(itemView: LayoutFaqDetailPageBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val bind = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQDetailViewHolder =
        FAQDetailViewHolder(
            LayoutFaqDetailPageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: FAQDetailViewHolder, position: Int) {
//        val data = mList[position]
//        holder.bind.textTitle.text = data.title
//        holder.bind.textSubTitle.text = HtmlCompat.fromHtml(data.description, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    override fun getItemCount(): Int = 2
}