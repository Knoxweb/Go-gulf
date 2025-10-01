package com.gogulf.passenger.app.ui.schedulebooking.activity

import CollectionInterface
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.model.CurrentBookingResponseData
import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.data.model.MainApiResponseData
import com.gogulf.passenger.app.data.repository.ApiRepository
import com.gogulf.passenger.app.data.repository.FirebaseRepository
import com.gogulf.passenger.app.data.repository.FirestoreCollectionLiveRepository
import com.gogulf.passenger.app.ui.schedulebooking.adapter.ScheduledBookingAdapter
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.ArrayList

class ScheduledBookingViewModel : ViewModel() {


    private val _scheduledAdapter = MutableLiveData<ScheduledBookingAdapter>()
    val scheduledAdapter: LiveData<ScheduledBookingAdapter>
        get() = _scheduledAdapter

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    var customLoader: CustomLoader? = null
    val shouldShowEmptyStatement = MutableLiveData(false)

    init {
        fetchSchedules()
    }

    var scheduleListener: ListenerRegistration? = null

    private fun fetchSchedules() {
        viewModelScope.launch {
            val collectionReference = FirebaseRepository().getBaseDoc().collection("schedules").orderBy(
                "pickup_date_timestamp", Query.Direction.ASCENDING
            )
            FirestoreCollectionLiveRepository().get<CurrentBookingResponseData>(
                object : CollectionInterface {
                    override fun listeners(listener: ListenerRegistration?) {
                        scheduleListener = listener
                    }
                }, orderQuery = collectionReference
            ).collect { response ->
                response.onSuccess { onSuccessData ->
                    shouldShowEmptyStatement.postValue(onSuccessData.isEmpty())

                    if (onSuccessData.isNotEmpty()) {
                        _scheduledAdapter.postValue(
                            ScheduledBookingAdapter(
                                this@ScheduledBookingViewModel, ArrayList(onSuccessData)
                            )
                        )
                    }
                }
                response.onError { error ->
                    Log.e("NotificationResponseDataError", error.message.toString())
                }
            }
        }

    }


    fun hitCancelBooking(id: Int?) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            val requestModel = DefaultRequestModel()
            requestModel.url = "${UrlName.CANCEL_BOOKING_REQUEST}/$id"

            ApiRepository().post(requestModel, MainApiResponseData::class.java).onSuccess {onSuccessData ->
                _uiState.update {
                    it.copy(isLoading = false, error = Error(onSuccessData?.title?:"",onSuccessData?.message?:"" ))
                }
            }.onError { onError ->
                _uiState.update {
                    it.copy(isLoading = false, error = Error("", onError.message.toString()))
                }
            }.onFailure { onFailure ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = Error(onFailure.title ?: "", onFailure.message ?: "")
                    )
                }
            }
        }
    }


    fun validate(activity: NewScheduledBookingActivity) {
        activity.validate()
    }

    override fun onCleared() {
        super.onCleared()
        scheduleListener?.remove()
    }

    fun clearError() {
        _uiState.update {
            it.copy(error = null)
        }
    }

}