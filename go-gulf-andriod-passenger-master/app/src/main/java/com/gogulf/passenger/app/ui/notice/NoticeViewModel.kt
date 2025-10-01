package com.gogulf.passenger.app.ui.notice

import CollectionInterface
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.gson.JsonObject
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.model.response.notification.NoticeModel
import com.gogulf.passenger.app.data.repository.FirebaseRepository
import com.gogulf.passenger.app.data.repository.FirestoreCollectionLiveRepository
import com.gogulf.passenger.app.data.repository.MainRepository
import com.gogulf.passenger.app.ui.base.BaseNavigation
import com.gogulf.passenger.app.ui.base.BaseVMNew
import com.gogulf.passenger.app.utils.others.NetworkHelper
import com.gogulf.passenger.app.utils.others.Resource
import kotlinx.coroutines.launch

class NoticeViewModel(
    private val mainRepository: MainRepository,
    private val firebaseRepository: FirebaseRepository,
    private val networkHelper: NetworkHelper
) : BaseVMNew<BaseNavigation>(mainRepository, networkHelper) {

    private val notificationList = MutableLiveData<Resource<List<NoticeModel>>>()
    val myNotificationList: MutableLiveData<Resource<List<NoticeModel>>>
        get() = notificationList

    val readNotification = MutableLiveData<Resource<JsonObject>>()

    var notificationListener: ListenerRegistration? = null

    fun readNotification(id: String?) {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.post_readNotification
        requestModel.body.addProperty("id", id)
        requestPostMethod(requestModel, readNotification)
    }

//    fun getNotifications() {
//        val requestModel = DefaultRequestModel()
//        requestModel.url = UrlName.get_notification
//        requestGetMethodDispose(requestModel, notificationList)
//    }

    fun getNotifications() {
        viewModelScope.launch {
//            val collection = FirebaseRepository().getBaseDoc().collection(NOTIFICATIONS)
            notificationList.postValue(Resource.loading(null))
            val collectionReference =
                FirebaseRepository().getBaseDoc().collection("notifications").orderBy(
                    "timestamp", Query.Direction.DESCENDING
                )
            FirestoreCollectionLiveRepository().get<NoticeModel>(
                object : CollectionInterface {
                    override fun listeners(listener: ListenerRegistration?) {
                        notificationListener = listener
                    }
                }, orderQuery = collectionReference
            ).collect { response ->
                response.onSuccess { onSuccessData ->
                    notificationList.postValue(Resource.success(onSuccessData))
                }
                response.onError { error ->
                    Log.e("NotificationResponseDataError", error.message.toString())
                    notificationList.postValue(Resource.error(error.message.toString(), null))
                }
            }

        }
    }


    init {
        getNotifications()
    }

    override fun onCleared() {
        super.onCleared()
        notificationListener?.remove()
    }
}