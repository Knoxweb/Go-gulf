package com.gogulf.passenger.app.ui.plantrip

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.repository.MainRepository
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.base.BaseViewModel
import com.gogulf.passenger.app.utils.others.NetworkHelper
import com.gogulf.passenger.app.utils.others.Resource
import com.google.gson.Gson
import com.gogulf.passenger.app.data.model.CalculateFareRequestBody
import com.gogulf.passenger.app.data.model.CalculateFareResponseData
import com.gogulf.passenger.app.data.repository.ApiRepository
import kotlinx.coroutines.launch

class PlanTripVM(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel<BaseNavigation>() {
    private var quoteRequest = MutableLiveData<Resource<CalculateFareResponseData>>()
    val quoteResponse: MutableLiveData<Resource<CalculateFareResponseData>>
        get() = quoteRequest

//    fun requestQuote(createQuote: CreateQuote) {
//        val requestModel = DefaultRequestModel()
//        requestModel.url = UrlName.post_quote
//        requestModel.body = Gson().toJsonTree(createQuote).asJsonObject
//        viewModelScope.launch {
//            quoteRequest.postValue(Resource.loading(null))
//            if (networkHelper.isNetworkConnected()) {
//                mainRepository.postMethod(requestModel, quoteRequest)
//            } else {
//                quoteRequest.postValue(Resource.error("No internet connection", null))
//            }
//        }
//    }


    fun hitCalculateFare(calculateFareRequestBody: CalculateFareRequestBody) {
        viewModelScope.launch {
            val requestModel = DefaultRequestModel()
            requestModel.url = UrlName.CALCULATE_FARE
            requestModel.body = Gson().toJsonTree(calculateFareRequestBody).asJsonObject
            quoteRequest.postValue(Resource.loading(null))
            ApiRepository().post(requestModel, CalculateFareResponseData::class.java)
                .onSuccess {
                    quoteRequest.postValue(Resource.success(it))
                }.onError {
                    quoteRequest.postValue(Resource.error(it.localizedMessage, null))
                }.onFailure {
                    quoteRequest.postValue(Resource.error(it.message?:"Something went wrong", null))
                }
        }
    }

}