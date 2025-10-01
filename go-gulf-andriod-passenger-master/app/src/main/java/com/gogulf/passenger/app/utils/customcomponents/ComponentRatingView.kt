package com.gogulf.passenger.app.utils.customcomponents

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.LayoutRatingDetailContainerBinding

class ComponentRatingView(
    private val context: Context,
    private val bind: LayoutRatingDetailContainerBinding
) {


    private var ratings = 0
    fun rate(rate: Int) {
        ratings = rate
        rateLayout()
    }
    init {
        rate(0)
    }

    private fun rateLayout() {
        when (ratings) {
            0 -> {
                unRate(bind.rate1)
                unRate(bind.rate2)
                unRate(bind.rate3)
                unRate(bind.rate4)
                unRate(bind.rate5)
            }
            1 -> {
                rate(bind.rate1)
                unRate(bind.rate2)
                unRate(bind.rate3)
                unRate(bind.rate4)
                unRate(bind.rate5)
            }
            2 -> {
                rate(bind.rate1)
                rate(bind.rate2)
                unRate(bind.rate3)
                unRate(bind.rate4)
                unRate(bind.rate5)
            }
            3 -> {
                rate(bind.rate1)
                rate(bind.rate2)
                rate(bind.rate3)
                unRate(bind.rate4)
                unRate(bind.rate5)
            }
            4 -> {
                rate(bind.rate1)
                rate(bind.rate2)
                rate(bind.rate3)
                rate(bind.rate4)
                unRate(bind.rate5)

            }
            5 -> {
                rate(bind.rate1)
                rate(bind.rate2)
                rate(bind.rate3)
                rate(bind.rate4)
                rate(bind.rate5)

            }
        }
    }

    private fun rate(imageView: ImageView) {
        imageView.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_rating_rate_star
            )
        )
    }

    private fun unRate(imageView: ImageView) {
        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_rating_outline))

    }
}