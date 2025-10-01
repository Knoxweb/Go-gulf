package com.gogulf.passenger.app.ui.currentride

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.repository.FirebaseRepository
import com.gogulf.passenger.app.data.repository.MainRepository
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.base.BaseViewModel
import com.gogulf.passenger.app.utils.others.NetworkHelper
import com.gogulf.passenger.app.utils.others.Resource
import kotlinx.coroutines.launch

class CurrentRideVM(
    private val mainRepository: MainRepository,
    private val firebaseRepository: FirebaseRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel<BaseNavigation>() {
    private var currentRequest = MutableLiveData<Resource<JsonObject>>()
    val currentResponse: MutableLiveData<Resource<JsonObject>>
        get() = currentRequest



    fun getCurrentRide() {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.get_currentRide
        viewModelScope.launch {
            currentRequest.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.postMethod(requestModel, currentRequest)
            } else {
                currentRequest.postValue(Resource.error("No internet connection", null))
            }
        }
    }



    /** Api for canceling requested Job for OnDemandService */

    private var cancelCurrentRequest = MutableLiveData<Resource<JsonObject>>()
    val cancelCurrentResponse: MutableLiveData<Resource<JsonObject>>
        get() = cancelCurrentRequest


    fun cancelCurrentRequest(bookingID: String, auto: Boolean = true) {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.post_cancelRequestedJob
        requestModel.body.addProperty("booking_id", bookingID)
        requestModel.body.addProperty("auto", auto)
        requestModel.body.addProperty("reason", "")
        viewModelScope.launch {
            cancelCurrentRequest.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.postMethod(requestModel, cancelCurrentRequest)
            } else {
                cancelCurrentRequest.postValue(Resource.error("No internet connection", null))
            }
        }
    }

    fun reTryCurrentRideRequest(bookingID: String) {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.post_re_requestJob
        requestModel.body.addProperty("booking_id", bookingID)
        viewModelScope.launch {
            currentRequest.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.postMethod(requestModel, currentRequest)
            } else {
                currentRequest.postValue(Resource.error("No internet connection", null))
            }
        }
    }

    /** Api for canceling requested Job for CurrentRide */

    private var cancelCurRequest = MutableLiveData<Resource<JsonObject>>()
    val cancelCurResponse: MutableLiveData<Resource<JsonObject>>
        get() = cancelCurRequest


    fun cancelCurRequest(bookingID: String) {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.post_dodCancelBooking
        requestModel.body.addProperty("booking_id", bookingID)
        requestModel.body.addProperty("reason", "No Show Up")
        viewModelScope.launch {
            cancelCurRequest.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.postMethod(requestModel, cancelCurRequest)
            } else {
                cancelCurRequest.postValue(Resource.error("No internet connection", null))
            }
        }
    }
}