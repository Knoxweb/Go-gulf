package com.gogulf.passenger.app.ui.settings.profile

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
import com.gogulf.passenger.app.data.model.ProfileResponseData
import com.gogulf.passenger.app.data.model.response.mycards.CardModels
import com.gogulf.passenger.app.data.repository.ApiRepository
import com.gogulf.passenger.app.data.repository.FirebaseRepository
import com.gogulf.passenger.app.data.repository.FirestoreCollectionLiveRepository
import com.gogulf.passenger.app.data.repository.FirestoreDocumentLiveRepository
import kotlinx.coroutines.launch

class EditProfileVM(
    private val mainRepository: MainRepository, private val networkHelper: NetworkHelper
) : BaseViewModel<BaseNavigation>() {
    private var profileRequest = MutableLiveData<Resource<ProfileResponseData>>()
    val profileResponse: MutableLiveData<Resource<ProfileResponseData>>
        get() = profileRequest

    private var profileUpdateRequest = MutableLiveData<Resource<MainApiResponseData>>()
    val profileUpdateResponse: MutableLiveData<Resource<MainApiResponseData>>
        get() = profileUpdateRequest

    var profileListener: ListenerRegistration? = null
    var cardListener: ListenerRegistration? = null
    private var cardRequest = MutableLiveData<Resource<List<CardModels>>>()
    val cardResponse: MutableLiveData<Resource<List<CardModels>>>
        get() = cardRequest

    fun getCards() {
        viewModelScope.launch {
            profileRequest.postValue(Resource.loading(null))

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

    fun getProfile() {

        viewModelScope.launch {
            profileRequest.postValue(Resource.loading(null))

            val document = FirebaseRepository().getBaseDoc().collection("data").document(
                "profile"
            )
            FirestoreDocumentLiveRepository().get<ProfileResponseData>(object :
                CollectionInterface {
                override fun listeners(listener: ListenerRegistration?) {
                    profileListener = listener
                }
            }, document).collect { response ->
                response.onSuccess { onSuccessData ->
                    val pic = onSuccessData.profile_picture_url?.split("?")?.first()
                    if (pic != null) {
                        onSuccessData.profile_picture_url = pic
                    }

                    profileRequest.postValue(Resource.success(onSuccessData))
                }
                response.onError { onErrorData ->
                    profileRequest.postValue(Resource.error(onErrorData.localizedMessage, null))
                    Log.e("Error", onErrorData.localizedMessage ?: "")
                }
            }
        }
    }


//    fun updateProfilePersonal(body: JsonObject) {
//        val requestModel = DefaultRequestModel()
//        requestModel.url = UrlName.post_updateProfile
//        requestModel.body = body
//        viewModelScope.launch {
//            profileUpdateRequest.postValue(Resource.loading(null))
//            if (networkHelper.isNetworkConnected()) {
//                mainRepository.postMethod(requestModel, profileUpdateRequest)
//            } else {
//                profileUpdateRequest.postValue(Resource.error("No internet connection", null))
//            }
//        }
//    }


    fun updateProfilePersonal(body: JsonObject) {
        viewModelScope.launch {
            profileUpdateRequest.postValue(Resource.loading(null))
            val requestModel = DefaultRequestModel()
            requestModel.url = UrlName.UPDATE
            requestModel.body = body
            ApiRepository().post(requestModel, MainApiResponseData::class.java).onSuccess {
                profileUpdateRequest.postValue(Resource.success(it))
            }.onFailure { onFailure ->
                profileUpdateRequest.postValue(
                    Resource.error(
                        onFailure.message ?: "", null, onFailure.title ?: ""
                    )
                )
            }.onError {
                profileUpdateRequest.postValue(Resource.error(it.localizedMessage, null))
            }
        }
    }




    init {
        getProfile()
        getCards()
    }
}