package com.gogulf.passenger.app.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gogulf.passenger.app.data.model.ProfileResponseData

object LiveModels {



    // ProfileResponseData
    private var _profileResponseData = MutableLiveData<ProfileResponseData?>()

    val profileResponseLiveData: LiveData<ProfileResponseData?>
        get() = _profileResponseData

    fun setProfileResponseData(profileResponseData: ProfileResponseData) {
        _profileResponseData.value = profileResponseData
    }

}