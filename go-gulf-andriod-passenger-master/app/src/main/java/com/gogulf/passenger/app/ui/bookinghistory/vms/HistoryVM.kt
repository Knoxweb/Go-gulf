package com.gogulf.passenger.app.ui.bookinghistory.vms

import CollectionInterface
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.repository.MainRepository
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.base.BaseViewModel
import com.gogulf.passenger.app.utils.others.NetworkHelper
import com.gogulf.passenger.app.utils.others.Resource
import com.google.gson.JsonObject
import com.gogulf.passenger.app.data.model.CurrentBookingResponseData
import com.gogulf.passenger.app.data.repository.FirebaseRepository
import com.gogulf.passenger.app.data.repository.FirestoreCollectionLiveRepository
import kotlinx.coroutines.launch

class HistoryVM (
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel<BaseNavigation>() {
    private var pendingRequest = MutableLiveData<Resource<List<CurrentBookingResponseData>>>()
    val pendingResponse: MutableLiveData<Resource<List<CurrentBookingResponseData>>>
        get() = pendingRequest

//    fun getBookingList(type: String) {
//        val requestModel = DefaultRequestModel()
//        requestModel.url = UrlName.get_bookings + type
//        viewModelScope.launch {
//            pendingRequest.postValue(Resource.loading(null))
//            if (networkHelper.isNetworkConnected()) {
//                mainRepository.getMethod(requestModel, pendingRequest)
//            } else {
//                pendingRequest.postValue(Resource.error("No internet connection", null))
//            }
//        }
//    }


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

    var bookingListListener: ListenerRegistration? = null

    fun getBookingListFromFirebase(type: String) {
        viewModelScope.launch {
            pendingRequest.postValue(Resource.loading(null))
            val collectionReference = FirebaseRepository().getBaseDoc().collection("bookings").orderBy(
                "id", Query.Direction.DESCENDING
            )
            FirestoreCollectionLiveRepository().get<CurrentBookingResponseData>(
                object : CollectionInterface {
                    override fun listeners(listener: ListenerRegistration?) {
                        bookingListListener = listener
                    }
                }, orderQuery = collectionReference
            ).collect { response ->
                response.onSuccess { onSuccessData ->
                    pendingRequest.postValue(Resource.success(onSuccessData.filter { it.status == type }))
                }
                response.onError { error ->
                    Log.e("NotificationResponseDataError", error.message.toString())
                    pendingRequest.postValue(Resource.error(error.message.toString(), null))
                }
            }

        }
    }

}