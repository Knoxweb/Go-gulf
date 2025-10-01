package com.gogulf.passenger.app.ui.auth.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gogulf.passenger.app.data.apidata.APIConstants
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.repository.MainRepository
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.base.BaseViewModel
import com.gogulf.passenger.app.utils.objects.PrefConstant
import com.gogulf.passenger.app.utils.others.NetworkHelper
import com.gogulf.passenger.app.utils.others.Resource
import com.google.gson.JsonObject
import kotlinx.coroutines.launch

class RegisterVM(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel<BaseNavigation>() {



    private var profileUpdateRequest = MutableLiveData<Resource<JsonObject>>()
    val profileUpdateResponse: MutableLiveData<Resource<JsonObject>>
        get() = profileUpdateRequest

    fun registerUser(body: JsonObject) {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.post_register
        requestModel.body = body
        preferenceHelper.setValue(PrefConstant.AUTH_TOKEN, APIConstants.static_token)
        viewModelScope.launch {
            profileUpdateRequest.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.postMethod(requestModel, profileUpdateRequest)
            } else {
                profileUpdateRequest.postValue(Resource.error("No internet connection", null))
            }
        }
    }


    private var deviceRequest = MutableLiveData<Resource<JsonObject>>()
    val deviceResponse: MutableLiveData<Resource<JsonObject>>
        get() = deviceRequest

    fun postDeviceToken() {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.post_deviceToken
        requestModel.body.addProperty(
            "device_token",
            preferenceHelper.getValue(PrefConstant.DEVICE_TOKEN, "") as String
        )
        requestModel.body.addProperty(
            "device_type",
            "android"
        )


        viewModelScope.launch {
            deviceRequest.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.postMethod(requestModel, deviceRequest)
            } else {
                deviceRequest.postValue(Resource.error("No internet connection", null))
            }
        }
    }
}