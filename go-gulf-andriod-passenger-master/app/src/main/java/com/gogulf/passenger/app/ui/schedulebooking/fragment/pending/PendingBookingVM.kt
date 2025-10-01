package com.gogulf.passenger.app.ui.schedulebooking.fragment.pending

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

class PendingBookingVM(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel<BaseNavigation>() {
    private var pendingRequest = MutableLiveData<Resource<JsonObject>>()
    val pendingResponse: MutableLiveData<Resource<JsonObject>>
        get() = pendingRequest

    fun getBookingList(type: String) {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.get_schedule + type
        viewModelScope.launch {
            pendingRequest.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getMethod(requestModel, pendingRequest)
            } else {
                pendingRequest.postValue(Resource.error("No internet connection", null))
            }
        }
    }

    private var cancelRequest = MutableLiveData<Resource<JsonObject>>()
    val cancelResponse: MutableLiveData<Resource<JsonObject>>
        get() = cancelRequest

    fun cancelBooking(bookingId: String) {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.post_bookingCancel
        requestModel.body.addProperty("booking_id", bookingId)
        viewModelScope.launch {
            cancelRequest.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.postMethod(requestModel, cancelRequest)
            } else {
                cancelRequest.postValue(Resource.error("No internet connection", null))
            }
        }
    }

    private var detailRequest = MutableLiveData<Resource<JsonObject>>()
    val detailResponse: MutableLiveData<Resource<JsonObject>>
        get() = detailRequest

    fun getBookingDetail(bookingID: String) {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.get_bookingDetail + bookingID
        viewModelScope.launch {
            detailRequest.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getMethod(requestModel, detailRequest)
            } else {
                detailRequest.postValue(Resource.error("No internet connection", null))
            }
        }
    }
}