package com.gogulf.passenger.app.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.gogulf.passenger.app.R

@BindingAdapter("imageFromDrawable")
fun ImageView.imageFromDrawable(drawable: Int) {
    this.setImageResource(drawable)
}
@BindingAdapter("shortcutIconFromTitle")
fun ImageView.shortcutIconFromTitle(title: String?) {
    val icon =
        when (title?.lowercase()) {
            "home" -> R.drawable.ic_s_home
            "office" -> R.drawable.ic_s_address
            "airport" -> R.drawable.ic_s_flight
            "bank" -> R.drawable.ic_s_bank
            "school" -> R.drawable.ic_s_school
            "restaurant" -> R.drawable.ic_s_restaurant
            "hospital" -> R.drawable.ic_s_hospital
            "doctor" -> R.drawable.ic_s_doctor
            "store" -> R.drawable.ic_s_store
            "college" -> R.drawable.ic_s_college
            "cinema" -> R.drawable.ic_s_cinema
            "meeting" -> R.drawable.ic_s_meeting
            "theatre" -> R.drawable.ic_s_theatre
            "wedding" -> R.drawable.ic_s_wedding
            "fitness" -> R.drawable.ic_s_fitness
            "gas st" -> R.drawable.ic_s_gas_st
            "charge st" -> R.drawable.ic_s_charge
            "mosque" -> R.drawable.ic_s_mosque
            "family" -> R.drawable.ic_s_family
            "pool" -> R.drawable.ic_s_pool
            "others" -> R.drawable.ic_s_default
            "add" ->  R.drawable.ic_menu_add
            else -> R.drawable.ic_s_default

        }
    this.setImageResource(icon)
}

@BindingAdapter("idType")
fun TextView.idType(id: String?) {
    this.text = "#$id"
}

@BindingAdapter("imageFromGlide")
fun ImageView.imageFromGlide(url: String?) {
    if (url != null) {
        if (url.isNotEmpty()) {
            this.visibility = View.VISIBLE
            Glide.with(context).load(url).into(this)
//            Picasso.get().load(url).into(this)
        } else {
            this.visibility = View.GONE
        }
        return
    }

    this.visibility = View.GONE


//    Glide.with(context).asBitmap().load(url).into(this)
}

