package com.gogulf.passenger.app.ui.addextras

import com.gogulf.passenger.app.utils.interfaces.CapacityValueListener

object Validations {

    fun checkValidationsForViewCapacity(
        viewCap: Boolean,
        passengerCapacityLayout: CapacityLayout,
        luggageCapacityLayout: CapacityLayout,
        capacityValueListener: CapacityValueListener
    ): Boolean {
        capacityValueListener.values(
            passengerCapacityLayout.getValue(),
            luggageCapacityLayout.getValue()
        )
        return true


    }

    fun checkValidationsForViewExtras(
        viewCap: Boolean,
        passengerCapacityLayout: CapacityLayout,
        luggageCapacityLayout: CapacityLayout,
        capacityValueListener: CapacityValueListener
    ): Boolean {
        return if (viewCap) {
            capacityValueListener.values(
                passengerCapacityLayout.getValue(),
                luggageCapacityLayout.getValue()
            )
            false
        } else {
            capacityValueListener.values(1, 1)
            true
        }

    }
}