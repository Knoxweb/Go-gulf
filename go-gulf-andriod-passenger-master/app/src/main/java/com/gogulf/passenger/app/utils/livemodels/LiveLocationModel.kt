package com.bookingapp.connectsmartdrive.utils.livemodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gogulf.passenger.app.ui.locationselector.LocationSelectorUIState

object LiveLocationModel {


    private var _LocationResponseData = MutableLiveData<LocationSelectorUIState?>()

    val locationLiveData: LiveData<LocationSelectorUIState?>
        get() = _LocationResponseData

    fun setLocationResponseData(locationLiveData: LocationSelectorUIState?) {
        _LocationResponseData.value = locationLiveData
    }
}