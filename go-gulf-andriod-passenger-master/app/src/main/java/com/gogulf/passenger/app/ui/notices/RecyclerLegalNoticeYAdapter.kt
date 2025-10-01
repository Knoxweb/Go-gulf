package com.gogulf.passenger.app.ui.notices
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gogulf.passenger.app.data.model.Y
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.RecyclerPoliciesYBinding

class RecyclerLegalNoticeYAdapter(
    private val listOfY: List<Y>,
) : RecyclerView.Adapter<RecyclerLegalNoticeYAdapter.ViewHolder>() {

    private lateinit var context: Context


    inner class ViewHolder(private val binding: RecyclerPoliciesYBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(y: Y) {
            binding.tvContent.text = y.content + "\n"
            if (y.type == "heading2") {
                binding.tvContent.setTextAppearance(R.style.textStyle16)
            } else if (y.type == "heading3") {
                binding.tvContent.setTextAppearance(R.style.textStyle14)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerPoliciesYBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        context = parent.context
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listOfY.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOfY[position]
        holder.bind(item)
    }

}