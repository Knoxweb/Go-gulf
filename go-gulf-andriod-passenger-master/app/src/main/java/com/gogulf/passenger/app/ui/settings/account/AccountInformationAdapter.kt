package com.gogulf.passenger.app.ui.settings.account

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.ui.base.BaseLoadingAdapter
import com.gogulf.passenger.app.utils.interfaces.SimpleClick
import com.gogulf.passenger.app.utils.objects.Constants
import com.gogulf.passenger.app.databinding.LayoutAccountInformationBinding

class AccountInformationAdapter (
    private val accountList: ArrayList<AccountInformationModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var simpleClick: SimpleClick? = null
    private var loading = false

    fun onLayoutClicked(simpleClick: SimpleClick) {
        this.simpleClick = simpleClick
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setLoading(loadings: Boolean) {
        this.loading = loadings
        notifyDataSetChanged()
    }

    inner class AccountInformationViewHolder(itemView: LayoutAccountInformationBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val bind = itemView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = if (viewType == Constants.VIEW_DATA) AccountInformationViewHolder(
        LayoutAccountInformationBinding.inflate(
            LayoutInflater.from(
                parent.context
            ), parent, false
        )

    ) else {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.layout_account_information_loading, parent, false)
        BaseLoadingAdapter(view)
    }


    override fun onBindViewHolder(hold: RecyclerView.ViewHolder, position: Int) {
        if (hold.itemViewType == Constants.VIEW_DATA) {
            val holder = hold as AccountInformationViewHolder
            val accounts = accountList[position]
            holder.bind.labelName.text = accounts.label
            holder.bind.valueName.text = accounts.value

            if (accountList.size - 1 == position) {
                holder.bind.divider.visibility = View.GONE
            }
            holder.bind.wholeThing.setOnClickListener {
                simpleClick?.onClicked()
            }
        }
    }

    override fun getItemCount(): Int = if (!loading) accountList.size else 3

    override fun getItemViewType(position: Int): Int =
        if (loading) {
            Constants.VIEW_LOADING
        } else {
            Constants.VIEW_DATA

        }


}