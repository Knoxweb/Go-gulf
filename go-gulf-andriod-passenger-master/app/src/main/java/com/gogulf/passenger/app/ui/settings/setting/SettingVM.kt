package com.gogulf.passenger.app.ui.settings.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.repository.MainRepository
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.base.BaseViewModel
import com.gogulf.passenger.app.utils.others.NetworkHelper
import com.gogulf.passenger.app.utils.others.Resource
import com.google.gson.JsonObject
import kotlinx.coroutines.launch

class SettingVM(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel<BaseNavigation>() {
    private var logOutRequest = MutableLiveData<Resource<JsonObject>>()
    val logOutResponse: MutableLiveData<Resource<JsonObject>>
        get() = logOutRequest


    private var deleteRequest = MutableLiveData<Resource<JsonObject>>()
    val deleteResponse: MutableLiveData<Resource<JsonObject>>
        get() = deleteRequest

    fun logoutUser() {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.get_logout
        viewModelScope.launch {
            logOutRequest.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.postMethod(requestModel, logOutRequest)
            } else {
                logOutRequest.postValue(Resource.error("No internet connection", null))
            }
        }
    }


    fun deleteUser() {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.post_accountDelete
        viewModelScope.launch {
            deleteRequest.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.postMethod(requestModel, deleteRequest)
            } else {
                deleteRequest.postValue(Resource.error("No internet connection", null))
            }
        }
    }
}