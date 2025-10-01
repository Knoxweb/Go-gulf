package com.gogulf.passenger.app.ui.bookingdetail

import CollectionInterface
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.gogulf.passenger.app.data.repository.MainRepository
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.base.BaseViewModel
import com.gogulf.passenger.app.utils.others.NetworkHelper
import com.gogulf.passenger.app.utils.others.Resource
import com.gogulf.passenger.app.data.model.CurrentBookingResponseData
import com.gogulf.passenger.app.data.repository.FirebaseRepository
import com.gogulf.passenger.app.data.repository.FirestoreCollectionLiveRepository
import kotlinx.coroutines.launch

class BookingDetailVM(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel<BaseNavigation>() {
    private var pendingRequest = MutableLiveData<Resource<CurrentBookingResponseData>>()
    val pendingResponse: MutableLiveData<Resource<CurrentBookingResponseData>>
        get() = pendingRequest

//    fun getBookingDetail(bookingID: String) {
////        val requestModel = DefaultRequestModel()
////        requestModel.url = UrlName.get_bookingDetail + bookingID
////        viewModelScope.launch {
//////            pendingRequest.postValue(Resource.loading(null))
////            if (networkHelper.isNetworkConnected()) {
////                mainRepository.getMethod(requestModel, pendingRequest)
////            } else {
////                pendingRequest.postValue(Resource.error("No internet connection", null))
////            }
////        }
//    }


    var currentRideListener: ListenerRegistration? = null

    fun getBookingDetail(bookingID: String) {
        viewModelScope.launch {
            pendingRequest.postValue(Resource.loading(null))

            try {
                val document = FirebaseRepository().getBaseDoc().collection("bookings") .whereEqualTo("id", bookingID.toInt())
                FirestoreCollectionLiveRepository().get<CurrentBookingResponseData>(object :
                    CollectionInterface {
                    override fun listeners(listener: ListenerRegistration?) {
                        currentRideListener = listener
                    }
                }, orderQuery = document).collect { response ->
                    response.onSuccess { onSuccessData ->
                        if (onSuccessData.isNotEmpty()) {
                            pendingRequest.postValue(Resource.success(onSuccessData[0]))
                        } else {
                            pendingRequest.postValue(Resource.error("Not found", null))
                        }
                    }
                    response.onError { onErrorData ->
                        pendingRequest.postValue(Resource.error("Not found", null))

                    }
                }

            } catch (e: Exception) {
                pendingRequest.postValue(Resource.error("Not found", null))
            }

        }
    }
}