package com.gogulf.passenger.app.ui.choosevehicle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gogulf.passenger.app.data.model.CalculateFareResponseData

class ChooseFleetViewModelFactory(
    private val quoteResponseData: CalculateFareResponseData? = null
): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChooseFleetViewModel(
            quoteResponseData
        ) as T
    }
}