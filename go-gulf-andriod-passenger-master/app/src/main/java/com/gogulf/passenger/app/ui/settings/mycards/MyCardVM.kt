package com.gogulf.passenger.app.ui.settings.mycards

import CollectionInterface
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.repository.MainRepository
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.base.BaseViewModel
import com.gogulf.passenger.app.utils.others.NetworkHelper
import com.gogulf.passenger.app.utils.others.Resource
import com.google.gson.JsonObject
import com.gogulf.passenger.app.data.model.MainApiResponseData
import com.gogulf.passenger.app.data.model.response.mycards.CardModels
import com.gogulf.passenger.app.data.repository.ApiRepository
import com.gogulf.passenger.app.data.repository.FirebaseRepository
import com.gogulf.passenger.app.data.repository.FirestoreCollectionLiveRepository
import kotlinx.coroutines.launch

class MyCardVM(
    private val mainRepository: MainRepository, private val networkHelper: NetworkHelper
) : BaseViewModel<BaseNavigation>() {
//    private var cardRequest = MutableLiveData<Resource<JsonObject>>()
//    val cardResponse: MutableLiveData<Resource<JsonObject>>
//        get() = cardRequest

    private var profileUpdateRequest = MutableLiveData<Resource<JsonObject>>()
    val profileUpdateResponse: MutableLiveData<Resource<JsonObject>>
        get() = profileUpdateRequest
    private var cardRequest = MutableLiveData<Resource<List<CardModels>>>()
    val cardResponse: MutableLiveData<Resource<List<CardModels>>>
        get() = cardRequest

//    fun getCardsInformation() {
//        val requestModel = DefaultRequestModel()
//        requestModel.url = UrlName.get_card
//        viewModelScope.launch {
//            cardRequest.postValue(Resource.loading(null))
//            if (networkHelper.isNetworkConnected()) {
//                mainRepository.getMethod(requestModel, cardRequest)
//            } else {
//                cardRequest.postValue(Resource.error("No internet connection", null))
//            }
//        }
//    }

    fun deleteCardFunction(requestModel: DefaultRequestModel) {
//        requestModel.url = UrlName.post_deleteCard
//        viewModelScope.launch {
//            cardRequest.postValue(Resource.loading(null))
//            if (networkHelper.isNetworkConnected()) {
////                mainRepository.postMethodRegisterWithoutImage(requestModel, updateCardRequest)
//                mainRepository.postMethod(requestModel, cardRequest)
//            } else {
//                cardRequest.postValue(Resource.error("No internet connection", null))
//            }
//        }
    }


    fun deleteCard(id: Int?) {
        viewModelScope.launch {
            val requestModel = DefaultRequestModel()
            requestModel.url = "${UrlName.DELETE_CARD}/$id"
            cardRequest.postValue(Resource.loading(null))
            ApiRepository().post(requestModel, MainApiResponseData::class.java)
                .onSuccess {

                }.onError {
                    cardRequest.postValue(Resource.error(it.localizedMessage, null))
                }.onFailure {
                    cardRequest.postValue(Resource.error(it.message?:"Something went wrong", null))
                }
        }
    }

    var cardListener: ListenerRegistration? = null


    fun getCards() {
        viewModelScope.launch {
            cardRequest.postValue(Resource.loading(null))

            val document = FirebaseRepository().getBaseDoc().collection("cards")
            FirestoreCollectionLiveRepository().get<CardModels>(object :
                CollectionInterface {
                override fun listeners(listener: ListenerRegistration?) {
                    cardListener = listener
                }
            }, document).collect { response ->
                response.onSuccess { onSuccessData ->
                    cardRequest.postValue(Resource.success(onSuccessData))
                }
                response.onError { onErrorData ->
                    Log.e("Error", onErrorData.localizedMessage ?: "")
                }
            }
        }
    }

    fun updateProfilePersonal(body: JsonObject) {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.post_updateProfile
        requestModel.body = body
        viewModelScope.launch {
            profileUpdateRequest.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.postMethod(requestModel, profileUpdateRequest)
            } else {
                profileUpdateRequest.postValue(Resource.error("No internet connection", null))
            }
        }
    }

    init {
        getCards()
    }

    /* init {
         getCardsInformation()
     }*/
}