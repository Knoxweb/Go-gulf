package com.gogulf.passenger.app.ui.settings.mycards

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.data.model.response.mycards.CardModels
import com.gogulf.passenger.app.ui.base.BaseLoadingAdapter
import com.gogulf.passenger.app.utils.interfaces.CardUpdateListener
import com.gogulf.passenger.app.utils.interfaces.SimpleClick
import com.gogulf.passenger.app.utils.objects.Constants
import com.bumptech.glide.Glide
import com.gogulf.passenger.app.ui.auth.cards.AddCardActivity
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.databinding.LayoutCardListItemBinding

class CardListAdapter(
    private val context: Context,
    private val cardList: ArrayList<CardModels>,
    private val cardUpdateListener: CardUpdateListener
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

    inner class AccountInformationViewHolder(itemView: LayoutCardListItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val bind = itemView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RecyclerView.ViewHolder = if (viewType == Constants.VIEW_DATA) AccountInformationViewHolder(
        LayoutCardListItemBinding.inflate(
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
            val cards = cardList[position]
            holder.bind.cardModel = cards

            var activeStatus = ""
            if (cards.isActive == "1" || cards.is_active == true) {
                holder.bind.makeActiveCard.visibility = View.VISIBLE
                hold.bind.deleteLayout.visibility = View.GONE
            } else {
                holder.bind.makeActiveCard.visibility = View.GONE
                hold.bind.deleteLayout.visibility = View.VISIBLE
            }

            Glide.with(context).load(cards.brand_image).into(holder.bind.visImage)
            holder.bind.mainContainer.setOnClickListener {

                // Added By Rohan after this
                val intent = Intent(context, AddCardActivity::class.java)
                intent.putExtra("isEditing", true)
                intent.putExtra("editViewCardValue", cards.card_masked)
                intent.putExtra("editViewYearValue", cards.exp_year.toString())
                intent.putExtra("editViewMonthsValue", cards.exp_month.toString())
                intent.putExtra("editViewHolderName", cards.name)
                intent.putExtra("editViewCVCValue", "***")
                intent.putExtra("cardId", cards.id)
                intent.putExtra("isCardActive", if (cards.is_active == true) "1" else "0")

//                val options = ActivityOptionsCompat.makeCustomAnimation(
//                    context, R.anim.slide_in_right, R.anim.slide_out_left
//                )

                context.startActivity(intent)

//                ActivityCompat.startActivity(context, intent, options.toBundle())
            }



            holder.bind.deleteLayout.setOnClickListener {
                CustomAlertDialog(context).setTitle("Delete Card?")
                    .setMessage("Are you sure you want to delete this card?")
                    .setPositiveText("Yes") { dialog, _ ->
                        cardUpdateListener.update(cards)
                        dialog.dismiss()
                    }.setNegativeText("No") { dialog, _ ->
                        dialog.dismiss()
                    }.show()
            }

        }
    }

    override fun getItemCount(): Int = if (!loading) cardList.size else 3

    override fun getItemViewType(position: Int): Int = if (loading) {
        Constants.VIEW_LOADING
    } else {
        Constants.VIEW_DATA

    }


}