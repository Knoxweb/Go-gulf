package com.gogulf.passenger.app.ui.addextras

import com.gogulf.passenger.app.App
import com.gogulf.passenger.app.data.model.others.SeatCharge
import com.gogulf.passenger.app.utils.interfaces.ExtrasInterface
import com.gogulf.passenger.app.utils.others.CustomToast
import com.gogulf.passenger.app.databinding.LayoutAddExtrasCounterBinding
import com.gogulf.passenger.app.databinding.LayoutAddExtrasCounterOldDesignBinding


class ExtrasLayout(
    private val bind: LayoutAddExtrasCounterBinding,
    seatCharge: SeatCharge,
    extrasInterface: ExtrasInterface
) {

    private var counter = 0
    private val seats = "Seats"

    init {
        bind.increaseButton.setOnClickListener {
            if (counter < seatCharge.max.toInt()) {
                counter++
                val data = "$seats $counter"
                bind.seatValue.text = data

                /*val totalPrice = seatCharge.rate.toInt() * counter
                val format = seatCharge.name + "  \$" + totalPrice
                bind.seatPrice.text = format*/
                extrasInterface.addData(seatCharge.id, counter.toString())
            } else {
                CustomToast.show(bind.extraDataLayout, App.baseApplication, "Max Seat Reached")
            }
        }

        bind.decreaseButton.setOnClickListener {
            if (counter > 0) {
                counter--
                val data = "$seats $counter"
                bind.seatValue.text = data

/*                val totalPrice = seatCharge.rate.toInt() * counter
                val format = seatCharge.name + "  \$" + totalPrice
                bind.seatPrice.text = format*/

                extrasInterface.addData(seatCharge.id, counter.toString())

            } else {
                CustomToast.show(bind.extraDataLayout, App.baseApplication, "Min Seat Reached")
            }
        }
        val format = seatCharge.name + " \$" + seatCharge.rate
        bind.seatPrice.text = format
        bind.seatInfoText.text = seatCharge.info

        val data = "$seats $counter"
        bind.seatValue.text = data

        extrasInterface.addData(seatCharge.id, counter.toString())

    }

    fun setTitle(text: String) {
        bind.seatPrice.text = text
    }

    fun setInfo(info: String) {
        bind.seatInfoText.text = info
    }
}

class ExtrasLayoutVTwo(
    private val bind: LayoutAddExtrasCounterOldDesignBinding,
    seatCharge: SeatCharge,
    extrasInterface: ExtrasInterface
) {

    private var counter = 0
    private val seats = "Seats"

    init {
        bind.increaseButton.setOnClickListener {
            if (counter < seatCharge.max.toInt()) {
                counter++
                val data = "$seats $counter"
                bind.seatValue.text = data
                extrasInterface.addData(seatCharge.id, counter.toString())
            } else {
                CustomToast.show(bind.extraDataLayout, App.baseApplication, "Max Seat Reached")
            }
        }

        bind.decreaseButton.setOnClickListener {
            if (counter > 0) {
                counter--
                val data = "$seats $counter"
                bind.seatValue.text = data

/*                val totalPrice = seatCharge.rate.toInt() * counter
                val format = seatCharge.name + "  \$" + totalPrice
                bind.seatPrice.text = format*/

                extrasInterface.addData(seatCharge.id, counter.toString())

            } else {
                CustomToast.show(bind.extraDataLayout, App.baseApplication, "Min Seat Reached")
            }
        }
        val format = seatCharge.name + " \$" + seatCharge.rate
        bind.seatPrice.text = format
        bind.seatInfoText.text = seatCharge.info

        val data = "$seats $counter"
        bind.seatValue.text = data

        extrasInterface.addData(seatCharge.id, counter.toString())

    }

    fun setTitle(text: String) {
        bind.seatPrice.text = text
    }

    fun setInfo(info: String) {
        bind.seatInfoText.text = info
    }
    fun getValue():Int = counter
}