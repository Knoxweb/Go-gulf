package com.gogulf.passenger.app.ui.getaridev2

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gogulf.passenger.app.ui.menu.MenuModels
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.RecyclerMenuBinding

class RecyclerMenuAdapter(
    private val menuItems: List<MenuModels>
) : RecyclerView.Adapter<RecyclerMenuAdapter.ViewHolder>() {

    private var listener: onMenuClicked? = null

    private lateinit var context: Context

    inner class ViewHolder(private val binding: RecyclerMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(availableOption: MenuModels, onClickListener: View.OnClickListener) {
            binding.availableOption = availableOption
            binding.cardMaterial.setOnClickListener(onClickListener)

            binding.mainContainerThing.background = AppCompatResources.getDrawable(context, R.drawable.background_middle_in_list)
            val tint = ContextCompat.getColor(context, R.color.primary_new) // Replace with your desired color
            binding.mainContainerThing.backgroundTintList = ColorStateList.valueOf(tint)


//            if (adapterPosition == 0) {
//                binding.mainContainerThing.background = AppCompatResources.getDrawable(context, R.drawable.background_top_in_list)
//            } else if (adapterPosition == menuItems.size - 1) {
//                binding.mainContainerThing.background = AppCompatResources.getDrawable(context, R.drawable.background_bottom_in_list)
//            } else {
//                binding.mainContainerThing.background = AppCompatResources.getDrawable(context, R.drawable.background_middle_in_list)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerMenuBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        context = parent.context
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(menuItems[position]) {
            listener?.onMenuClicked(position)
        }
    }

    interface onMenuClicked {
        fun onMenuClicked(position: Int)
    }


    fun setOnMenuClickListener(listener: onMenuClicked) {
        this.listener = listener
    }
}
