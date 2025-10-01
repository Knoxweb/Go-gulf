package com.gogulf.passenger.app.utils.objects

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.gogulf.passenger.app.R

object GetDrawableIcons {

    fun getDrawables(context: Context, name: String): Drawable? {
        val icon =
            when (name?.lowercase()) {
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
        return ContextCompat.getDrawable(context, icon)
    }
}