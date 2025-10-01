package com.gogulf.passenger.app.ui.choosevehicle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gogulf.passenger.app.data.model.CalculateFareResponseData
import com.gogulf.passenger.app.data.model.Fleet

class ChooseFleetViewModel(
    quoteResponseData: CalculateFareResponseData? = null
): ViewModel() {

    var selectedFleetModel: Fleet? = null
    val quoteResponse: CalculateFareResponseData? = quoteResponseData

    private val _fleetAdapter = MutableLiveData<RecyclerChooseFleetAdapter>()
    val fleetAdapter: LiveData<RecyclerChooseFleetAdapter>
        get() = _fleetAdapter
    init {

        if (!quoteResponseData?.fleets.isNullOrEmpty()) {
            if (quoteResponseData != null) {
                quoteResponseData.fleets?.get(0)?.isSelected = true
                selectedFleetModel = quoteResponseData.fleets?.get(0)
                _fleetAdapter.value =
                    quoteResponseData?.fleets?.let { RecyclerChooseFleetAdapter(this, it, quoteResponse?.quote?.distance?:"", quoteResponse?.quote?.duration?:"") }
            }
        }



    }

}