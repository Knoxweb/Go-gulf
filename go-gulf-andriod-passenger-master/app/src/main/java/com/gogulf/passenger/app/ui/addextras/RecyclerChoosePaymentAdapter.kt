package com.gogulf.passenger.app.ui.addextras

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gogulf.passenger.app.data.model.response.mycards.CardModels
import com.gogulf.passenger.app.ui.invoices.InvoiceViewModel
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.RecyclerChooseCardBinding

class RecyclerChoosePaymentAdapter(
    val viewModel: AddExtrasVM? = null,
    val invoiceViewModel: InvoiceViewModel? = null
) : ListAdapter<CardModels, RecyclerChoosePaymentAdapter.ViewHolder>(DiffUtil()) {

    private lateinit var context: Context
    private var oldSelectedPosition = 0

    private var onCardClickListener: OnCardClickListener? = null

    inner class ViewHolder(private val binding: RecyclerChooseCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CardModels, position: Int) {
            binding.card = item
            if (viewModel != null) {
                if (item.id.toString() == viewModel.passengerCardId) {
                    binding.trailingIcon.setImageResource(R.drawable.icon_radio_selected)
                } else {
                    if (item.id == viewModel.pageCardData.value?.id) {
                        binding.trailingIcon.setImageResource(R.drawable.icon_radio_selected)

                    } else {
                        binding.trailingIcon.setImageResource(R.drawable.icon_radio_unselected)

                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RecyclerChooseCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, holder.absoluteAdapterPosition)
        if (viewModel != null) {
            holder.itemView.setOnClickListener {
                viewModel.updateSelectedCard(item)
                notifyItemChanged(position)
                notifyItemChanged(oldSelectedPosition)
                oldSelectedPosition = holder.absoluteAdapterPosition
                onCardClickListener?.onCardClick(item)
            }
        } else {
            holder.itemView.setOnClickListener {
                onCardClickListener?.onCardClick(item)
            }
        }

    }

    interface OnCardClickListener {
        fun onCardClick(card: CardModels)
    }

    fun setCardClickListener(listener: OnCardClickListener) {
        this.onCardClickListener = listener
    }


    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<CardModels>() {
        override fun areItemsTheSame(
            oldItem: CardModels, newItem: CardModels
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CardModels, newItem: CardModels
        ): Boolean {
            return oldItem == newItem
        }

    }


}