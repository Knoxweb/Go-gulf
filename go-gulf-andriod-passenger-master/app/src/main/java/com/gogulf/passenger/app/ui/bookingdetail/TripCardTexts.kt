package com.gogulf.passenger.app.ui.bookingdetail

import com.gogulf.passenger.app.databinding.LayoutTripCardTextViewBinding


class TripCardTexts(private val bind: LayoutTripCardTextViewBinding) {

    fun setTextId1(text: String) {
        bind.idLabel.text = text
    }

    fun setTextId2(text: String) {
        bind.name.text = text
    }

    fun setTextValue1(text: String) {
        bind.idValue.text = text
    }

    fun setTextValue2(text: String) {
        bind.value.text = text
    }

    fun value1Color(color: Int) {
        bind.idValue.setTextColor(color)
    }

    fun value2Color(color: Int) {
        bind.value.setTextColor(color)
    }
}