package com.gogulf.passenger.app.ui.auth.login

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gogulf.passenger.app.databinding.RecyclerChooseCountryBinding

class RecyclerChooseCountryAdapter(
    private val countryModels: List<CountryModel>,
    private val chooseCountryListener: ChooseCountryListener
) : RecyclerView.Adapter<RecyclerChooseCountryAdapter.ViewHolder>() {

    private lateinit var context: Context


    inner class ViewHolder(private val binding: RecyclerChooseCountryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(countryModel: CountryModel, onClickListener: View.OnClickListener) {
            binding.countryModel = countryModel
            binding.countryMain.setOnClickListener(onClickListener)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val binding = RecyclerChooseCountryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = countryModels[position]
        holder.bind(item) {
            chooseCountryListener.onChooseCountry(item)
        }
    }

    override fun getItemCount(): Int {
        return countryModels.size
    }

    interface ChooseCountryListener {
        fun onChooseCountry(countryModel: CountryModel)
    }

}