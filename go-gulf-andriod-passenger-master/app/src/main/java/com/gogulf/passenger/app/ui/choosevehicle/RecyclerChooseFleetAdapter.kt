package com.gogulf.passenger.app.ui.choosevehicle

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gogulf.passenger.app.data.model.Fleet
import com.gogulf.passenger.app.databinding.RecyclerChooseFleetBinding

class RecyclerChooseFleetAdapter(
    private val viewModel: ChooseFleetViewModel,
    private val fleetModels: List<Fleet>,
    private val distance: String,
    private val duration: String
) : RecyclerView.Adapter<RecyclerChooseFleetAdapter.ViewHolder>() {


    private lateinit var context: Context
    private var activatedPosition = 0


    inner class ViewHolder(private val binding: RecyclerChooseFleetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(fleetModel: Fleet, onClick: (Boolean, Int) -> Unit) {
            binding.fleetModel = fleetModel

            if (fleetModel.getDiscountVisibility()) {
                binding.tvDiscountedPrice.visibility = View.VISIBLE
                binding.tvPrice.paintFlags =
                    binding.tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.tvPrice.alpha = 0.6f
                binding.tvDiscountedPrice.alpha =1f

            } else {
                binding.tvDiscountedPrice.visibility = View.GONE
                binding.tvPrice.alpha = 1f
                binding.tvDiscountedPrice.alpha = 0.6f
            }
            binding.distance = distance
            binding.duration = duration
            if (fleetModel.isSelected) {
                binding.card.strokeWidth = 2
            } else {
                binding.card.strokeWidth = 0
            }
            binding.card.setOnClickListener {
                onClick(!fleetModel.isSelected, position)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val binding = RecyclerChooseFleetBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = fleetModels[position]
        holder.bind(item) { isSelected, positionMain ->
            if (positionMain != activatedPosition) {
                fleetModels[positionMain].isSelected = true
                viewModel.selectedFleetModel = fleetModels[positionMain]
                notifyItemChanged(positionMain, 1)
                if (activatedPosition != -1) {
                    fleetModels[activatedPosition].isSelected = false
                    notifyItemChanged(activatedPosition, 1)
                }
                activatedPosition = positionMain
            }
        }
    }

    override fun getItemCount(): Int {
        return fleetModels.size
    }

}
