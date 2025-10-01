package com.gogulf.passenger.app.utils.carousels

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerAdapter
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.data.model.response.bookings.Fleet
import com.gogulf.passenger.app.data.model.response.bookings.QuotesResponse
import com.bumptech.glide.Glide
import com.gogulf.passenger.app.databinding.LayoutChooseVehicleItemsCardBinding


class VehicleCarouselSliderAdapter(
    private val mContext: Context,
    private val models: ArrayList<Fleet>,
    private val quotesResponse: QuotesResponse,
    private val fragmentManager: FragmentManager
) :
    PagerAdapter() {
    override fun getCount(): Int {
        return models.size
    }

    lateinit var onImageSelected: OnImageSelected

    fun onImageSelected(onImageSelected: OnImageSelected) {
        this.onImageSelected = onImageSelected
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }

    @SuppressLint("SetTextI18n")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = LayoutInflater.from(mContext)
            .inflate(R.layout.layout_choose_vehicle_items_card, container, false)
        val model = models[position]
        val binding = LayoutChooseVehicleItemsCardBinding.bind(view)
        binding.fleetModel = model
        binding.quotes = quotesResponse

        binding.selectButton.setOnClickListener {
            onImageSelected.onClicked(model)
        }
        Glide.with(mContext).load(model.imageLink).placeholder(R.drawable.vehicle)
            .into(binding.carImageView)

        if (model.offerFare == model.fare) {
            binding.totalPrices.visibility = View.GONE
        } else {
            binding.totalPrices.visibility = View.VISIBLE
        }
        /*  try {
              when (model.fleetID) {
                  "1" -> {
                      Glide.with(mContext).load(R.drawable.choose_vehicle_default)
                          .into(binding.carImageView)

                  }
                  "2" -> {
                      Glide.with(mContext).load(R.drawable.choose_vehicle_default_2)
                          .into(binding.carImageView)
                  }
                  "3" -> {
                      Glide.with(mContext).load(R.drawable.choose_vehicle_default_3)
                          .into(binding.carImageView)
                  }
                  else -> {
                      Glide.with(mContext).load(model.image1_Link)
                          .into(binding.carImageView)
                  }
              }


          } catch (e: Exception) {
              DebugMode.e("VehicleCarouselSliderAdapter", e.message.toString(), "image error")
          }*/


        container.addView(view)
        return view
    }


    interface OnImageSelected {
        fun onClicked(carouselModel: Fleet)
    }
}