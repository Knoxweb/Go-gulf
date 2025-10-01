package com.gogulf.passenger.app.ui.auth.cards

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
import com.gogulf.passenger.app.App
import com.gogulf.passenger.app.data.model.MainApiResponseData
import com.gogulf.passenger.app.data.repository.ApiRepository
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.model.CardParams
import com.stripe.android.model.Token
import kotlinx.coroutines.launch

class AddCardsVM(
    private val mainRepository: MainRepository, private val networkHelper: NetworkHelper
) : BaseViewModel<BaseNavigation>() {

    private var addCardRequest = MutableLiveData<Resource<MainApiResponseData>>()
    val addCardResponse: MutableLiveData<Resource<MainApiResponseData>>
        get() = addCardRequest

    private var updateCardRequest = MutableLiveData<Resource<MainApiResponseData>>()
    val updateCardResponse: MutableLiveData<Resource<MainApiResponseData>>
        get() = updateCardRequest


//    fun addCardFunction(requestModel: DefaultRequestModel) {
//        requestModel.url = UrlName.post_addCard
//        viewModelScope.launch {
//            addCardRequest.postValue(Resource.loading(null))
//            if (networkHelper.isNetworkConnected()) {
////                mainRepository.postMethodRegisterWithoutImage(requestModel, addCardRequest)
//                mainRepository.postMethod(requestModel, addCardRequest)
//            } else {
//                addCardRequest.postValue(Resource.error("No internet connection", null))
//            }
//        }
//    }


    fun createCardToken(cardParams: CardParams) {
        addCardRequest.postValue(Resource.loading(null))
        val stripe = Stripe(
            App.baseApplication,
            PaymentConfiguration.getInstance(App.baseApplication).publishableKey
        )
        stripe.createCardToken(cardParams, callback = object : ApiResultCallback<Token> {
            override fun onSuccess(result: Token) {

                val requestModel = DefaultRequestModel()
                requestModel.body.addProperty("card_token", result.id)
                addCardFunction(requestModel)
            }

            override fun onError(e: Exception) {
                addCardRequest.postValue(Resource.error(e.localizedMessage, null))

                if (e.localizedMessage.lowercase() == "could not find payment information") {

                } else {

                }

            }
        })
    }


    fun addCardFunction(requestModel: DefaultRequestModel) {
        viewModelScope.launch {
            requestModel.url = UrlName.ADD_CARD
            addCardRequest.postValue(Resource.loading(null))
            ApiRepository().post(requestModel, MainApiResponseData::class.java).onSuccess {
                addCardRequest.postValue(Resource.success(it))
            }.onError {
                addCardRequest.postValue(Resource.error(it.localizedMessage, null))
            }.onFailure {
                addCardRequest.postValue(
                    Resource.error(
                        it.message ?: "Something went wrong", null, it.title ?: ""
                    )
                )
            }
        }
    }

    fun updateCard(id: Int?) {
        viewModelScope.launch {
            val requestModel = DefaultRequestModel()
            requestModel.url = "${UrlName.ACTIVATE_CARD}/$id"
            updateCardRequest.postValue(Resource.loading(null))
            ApiRepository().post(requestModel, MainApiResponseData::class.java).onSuccess {
                updateCardRequest.postValue(Resource.success(it))
            }.onError {
                updateCardRequest.postValue(Resource.error(it.localizedMessage, null))
            }.onFailure {
                updateCardRequest.postValue(
                    Resource.error(
                        it.message ?: "Something went wrong", null, it.title ?: ""
                    )
                )
            }
        }
    }

//    fun addInvoiceFunction(requestModel: DefaultRequestModel) {
//        requestModel.url = UrlName.post_makePayment
//        viewModelScope.launch {
//            addCardRequest.postValue(Resource.loading(null))
//            if (networkHelper.isNetworkConnected()) {
//                mainRepository.postMethodRegisterWithoutImage(requestModel, addCardRequest)
//            } else {
//                addCardRequest.postValue(Resource.error("No internet connection", null))
//            }
//        }
//    }


//    fun updateCardFunction(requestModel: DefaultRequestModel) {
//        requestModel.url = UrlName.post_updateCard
//        viewModelScope.launch {
//            updateCardRequest.postValue(Resource.loading(null))
//            if (networkHelper.isNetworkConnected()) {
////                mainRepository.postMethodRegisterWithoutImage(requestModel, updateCardRequest)
//                mainRepository.postMethod(requestModel, updateCardRequest)
//            } else {
//                updateCardRequest.postValue(Resource.error("No internet connection", null))
//            }
//        }
//    }


    private var requestCapture = MutableLiveData<Resource<JsonObject>>()
    val captureResponse: MutableLiveData<Resource<JsonObject>>
        get() = requestCapture

    fun postCapture(id: String, clientSecret: String, currentRide: Boolean = false) {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.post_capturePayment
        requestModel.body.addProperty("invoice_id", id)
        requestModel.body.addProperty("client_secret", clientSecret)
        requestModel.body.addProperty("current_ride", false)
        viewModelScope.launch {
            requestCapture.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.postMethod(requestModel, requestCapture)
            } else {
                requestCapture.postValue(Resource.error("No internet connection", null))
            }
        }
    }
}