package com.gogulf.passenger.app.ui.invoices

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gogulf.passenger.app.data.apidata.UrlName.INVOICE_PDF
import com.gogulf.passenger.app.data.model.InvoiceResponseData
import com.gogulf.passenger.app.BuildConfig
import com.gogulf.passenger.app.databinding.RecyclerInvoiceBinding

class RecyclerInvoiceAdapter(
    private val viewModel: InvoiceViewModel
) : ListAdapter<InvoiceResponseData, RecyclerInvoiceAdapter.ViewHolder>(DiffUtil()) {
    private lateinit var context: Context

    private var onInvoiceClickListener: OnInvoiceClickListener? = null

    var shouldMultipleClick: MutableLiveData<Boolean> = MutableLiveData(false)

    inner class ViewHolder(private val binding: RecyclerInvoiceBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(invoice: InvoiceResponseData, adapterPosition: Int) {
            binding.invoice = invoice
            binding.btnDownload.setOnClickListener {

                viewModel.downloadFile("${BuildConfig.BASE_URL}$INVOICE_PDF/${invoice.id}")

//                val intent = Intent(context, PdfViewActivity::class.java)
//                intent.putExtra(PDF, "$baseUrl$INVOICE_PDF/${invoice.id}")
//                context.startActivity(intent)
            }
//            when (invoice.status?.color) {
//                ApiResponseConstants.YELLOW -> {
//                    binding.tvStatus.setBackgroundTintList(
//                        ColorStateList.valueOf(
//                            ContextCompat.getColor(context, R.color.orange_background)
//                        )
//                    )
//                }
//
//                ApiResponseConstants.GREEN -> {
//                    binding.tvStatus.setBackgroundTintList(
//                        ColorStateList.valueOf(
//                            ContextCompat.getColor(context, R.color.light_green)
//                        )
//                    )
//                }
//
//                else -> {
//                    binding.tvStatus.setBackgroundTintList(
//                        ColorStateList.valueOf(
//                            ContextCompat.getColor(context, R.color.cancelled_background)
//                        )
//                    )
//                }
//            }
//            if (shouldMultipleClick.value == true) {
//                binding.btnSelect.visibility = View.VISIBLE
//                binding.btnDownload.visibility = View.GONE
//                if (viewModel.selectedListId.contains(invoice.id)) {
//                    binding.btnSelect.setIconResource(R.drawable.icon_radio_selected)
//                    binding.btnSelect.setIconTintResource(R.color.pink_linear_end)
//                } else {
//                    binding.btnSelect.setIconResource(R.drawable.icon_radio_unselected)
//                    binding.btnSelect.setIconTintResource(R.color.text_color_white)
//                }
//                binding.btnSelect.setOnClickListener {
//                    if (viewModel.selectedListId.contains(invoice.id!!)) {
//                        viewModel.selectedListId.remove(invoice.id)
//                    } else {
//                        viewModel.selectedListId.add(invoice.id)
//                    }
//                    viewModel.updateCount(viewModel.selectedListId.size)
//                    notifyItemChanged(adapterPosition)
//                }
//            } else {
//                binding.btnSelect.visibility = View.GONE
//                binding.btnDownload.visibility = View.VISIBLE
//            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerInvoiceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, holder.absoluteAdapterPosition)
        holder.itemView.setOnClickListener {
            onInvoiceClickListener?.onInvoiceClick(item)
        }

    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<InvoiceResponseData>() {
        override fun areItemsTheSame(
            oldItem: InvoiceResponseData, newItem: InvoiceResponseData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: InvoiceResponseData, newItem: InvoiceResponseData
        ): Boolean {
            return oldItem == newItem
        }
    }


    interface OnInvoiceClickListener {
        fun onInvoiceClick(invoice: InvoiceResponseData)
    }

    fun setOnInvoiceClickListener(listener: OnInvoiceClickListener) {
        onInvoiceClickListener = listener
    }
}