package com.gogulf.passenger.app.ui.support.supports

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
import com.gogulf.passenger.app.data.model.MainApiResponseData
import com.gogulf.passenger.app.data.repository.ApiRepository
import kotlinx.coroutines.launch

class SupportVM (
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel<BaseNavigation>() {
    private var supportRequest = MutableLiveData<Resource<MainApiResponseData>>()
    val supportResponse: MutableLiveData<Resource<MainApiResponseData>>
        get() = supportRequest



//    fun postSupport(model: SupportPostModel) {
//        val requestModel = DefaultRequestModel()
//        requestModel.url = UrlName.post_support
//        requestModel.body = Gson().toJsonTree(model).asJsonObject
//        viewModelScope.launch {
//            supportRequest.postValue(Resource.loading(null))
//            if (networkHelper.isNetworkConnected()) {
//                mainRepository.postMethod(requestModel, supportRequest)
//            } else {
//                supportRequest.postValue(Resource.error("No internet connection", null))
//            }
//        }
//    }
//

    fun submitSupport(model: SupportPostModel) {
        viewModelScope.launch {
            val requestModel = DefaultRequestModel()
            requestModel.url = UrlName.SUPPORT
            requestModel.body = Gson().toJsonTree(model).asJsonObject
            supportRequest.postValue(Resource.loading(null))
            ApiRepository().post(requestModel, MainApiResponseData::class.java)
                .onSuccess {
                    supportRequest.postValue(Resource.success(it))
                }.onError {
                    supportRequest.postValue(Resource.error(it.localizedMessage, null))
                }.onFailure {
                    supportRequest.postValue(Resource.error(it.message?:"Something went wrong", null))
                }
        }
    }
}
