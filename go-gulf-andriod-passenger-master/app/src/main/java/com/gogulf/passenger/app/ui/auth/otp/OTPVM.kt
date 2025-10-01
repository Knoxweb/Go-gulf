package com.gogulf.passenger.app.ui.auth.otp

import android.util.Log
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

class OTPVM(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel<BaseNavigation>() {
    private var otpRequest = MutableLiveData<Resource<JsonObject>>()
    val otpResponse: MutableLiveData<Resource<JsonObject>>
        get() = otpRequest


    fun authenticateLogin() {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.post_authentication
        requestModel.body.addProperty(
            "uid",
            preferenceHelper.getValue(PrefConstant.FIREBASE_UID, APIConstants.TestUI) as String
        )
        requestModel.body.addProperty(
            "phone_cc",
            preferenceHelper.getValue(
                PrefConstant.M_CC,
                APIConstants.TestCountry
            ) as String
        )
        requestModel.body.addProperty(
            "phone",
            preferenceHelper.getValue(
                PrefConstant.MOBILE,
                APIConstants.TestNumber
            ) as String
        )
        preferenceHelper.setValue(PrefConstant.AUTH_TOKEN, APIConstants.static_token)

        viewModelScope.launch {
            otpRequest.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.postMethod(requestModel, otpRequest)
            } else {
                otpRequest.postValue(Resource.error("No internet connection", null))
            }
        }
    }

    fun authenticateLoginRohan() {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.post_authentication
        requestModel.body.addProperty(
            "uid",
            "QpSkvtfhdTUdcs4227ILBRqXxNV2"
        )
        requestModel.body.addProperty(
            "phone_cc",
            "+977"
        )
        requestModel.body.addProperty(
            "phone",
            "9881459445"
        )
        preferenceHelper.setValue(PrefConstant.AUTH_TOKEN, APIConstants.static_token)

        viewModelScope.launch {
            otpRequest.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.postMethod(requestModel, otpRequest)
                Log.e("Rohan", "Rohan")
            } else {
                otpRequest.postValue(Resource.error("No internet connection", null))
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