package com.gogulf.passenger.app.ui.ratings

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
import com.gogulf.passenger.app.data.model.MainApiResponseData
import com.gogulf.passenger.app.data.repository.ApiRepository
import kotlinx.coroutines.launch

class RatingVM(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel<BaseNavigation>() {
    private var ratingRequest = MutableLiveData<Resource<MainApiResponseData>>()
    val ratingResponse: MutableLiveData<Resource<MainApiResponseData>>
        get() = ratingRequest

//    fun postRating(jsonObject: JsonObject) {
//        val requestModel = DefaultRequestModel()
//        requestModel.url = UrlName.post_rating
//        requestModel.body = jsonObject
//        viewModelScope.launch {
//            ratingRequest.postValue(Resource.loading(null))
//            if (networkHelper.isNetworkConnected()) {
//                mainRepository.postMethod(requestModel, ratingRequest)
//            } else {
//                ratingRequest.postValue(Resource.error("No internet connection", null))
//            }
//        }
//    }


    fun postRating(jsonObject: JsonObject, id: String?) {
        val requestModel = DefaultRequestModel()
        requestModel.url = "${UrlName.BOOKING_REVIEW}/${id}"
        requestModel.body = jsonObject
        viewModelScope.launch {
            ratingRequest.postValue(Resource.loading(null))
            ApiRepository().post(requestModel, MainApiResponseData::class.java).onSuccess {
                ratingRequest.postValue(Resource.success(it))
            }.onError {
                ratingRequest.postValue(Resource.error(it.localizedMessage, null))
            }.onFailure {
                ratingRequest.postValue(
                    Resource.error(
                        it.message ?: "Something went wrong", null
                    )
                )
            }
        }
    }


    fun postSkip(id: String?) {
        val requestModel = DefaultRequestModel()
        requestModel.url = "${UrlName.BOOKING_SKIP_REVIEW}/${id}"
        viewModelScope.launch {
            ratingRequest.postValue(Resource.loading(null))
            ApiRepository().post(requestModel, MainApiResponseData::class.java).onSuccess {
                ratingRequest.postValue(Resource.success(it))
            }.onError {
                ratingRequest.postValue(Resource.error(it.localizedMessage, null))
            }.onFailure {
                ratingRequest.postValue(
                    Resource.error(
                        it.message ?: "Something went wrong", null
                    )
                )
            }
        }
    }


}