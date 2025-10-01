package com.gogulf.passenger.app.ui.search_ride

import CollectionInterface
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.model.ConfirmBookingSearchDriverResponseData
import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.data.model.Location
import com.gogulf.passenger.app.data.model.MainApiResponseData
import com.gogulf.passenger.app.data.model.Route
import com.gogulf.passenger.app.data.repository.ApiRepository
import com.gogulf.passenger.app.data.repository.FirebaseRepository
import com.gogulf.passenger.app.data.repository.FirestoreDocumentLiveRepository
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask

data class TimerUIState(
    val timeStamp: Long = 0L,
    val timerToShow: String ? = null,

    )


class SearchDriverViewModel(
    status: String? = null,
    id: Int? = null,
    route: Route? = null,
    pickup: Location? = null,
    drop: Location? = null,

    ) : ViewModel() {

    var bookingStatus: String = "pending"
    var bookingId: Int? = null
    var bookingRoute: Route? = null
    var bookingPickup: Location? = null
    var bookingDrop: Location? = null

    private val _uiState = MutableStateFlow(SearchDriverUIState())
    val uiState: StateFlow<SearchDriverUIState> = _uiState.asStateFlow()

    private val _retryLoadingState = MutableStateFlow(LoadingUiState())
    val retryLoadingState: StateFlow<LoadingUiState> = _retryLoadingState.asStateFlow()

    private val _timerState = MutableStateFlow(TimerUIState())
    val timerState: StateFlow<TimerUIState> = _timerState.asStateFlow()

    init {
        status?.let {
            bookingStatus = it
        }
        id?.let {
            bookingId = it
        }
        route?.let {
            bookingRoute = it
        }
        pickup?.let {
            bookingPickup = it
        }
        drop?.let {
            bookingDrop = it
        }
        fetchRequestFromFirebase()
    }


    var requestListener: ListenerRegistration? = null
    private var timer: Timer? = null

    fun fetchRequestFromFirebase() {
        viewModelScope.launch {
            val document = FirebaseRepository().getBaseDoc().collection("data").document(
                "request"
            )
            FirestoreDocumentLiveRepository().get<ConfirmBookingSearchDriverResponseData>(object :
                CollectionInterface {
                override fun listeners(listener: ListenerRegistration?) {
                    requestListener = listener
                }
            }, document).collect { response ->
                response.onSuccess { onSuccessData ->
                    _uiState.update {
                        it.copy(confirmBookingSearchDriverResponseData = onSuccessData)
                    }

                    stopTimer()
                    onSuccessData.expire_at?.let { startRunningTimer(it) }
                }
                response.onError { onErrorData ->
                    Log.e("Error", onErrorData.localizedMessage ?: "")
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        requestListener?.remove()
    }

    var customLoader: CustomLoader? = null

    fun hitRetryBooking() {

        viewModelScope.launch {
            _retryLoadingState.update {
                it.copy(isLoading = true, isSuccess = false)
            }
            val requestModel = DefaultRequestModel()
            requestModel.url =
                "${UrlName.RETRY_BOOKING}/${uiState.value.confirmBookingSearchDriverResponseData?.id}"
//            requestModel.body.addProperty("second", "120")
            ApiRepository().post(requestModel, MainApiResponseData::class.java).onSuccess {
                _retryLoadingState.update {
                    it.copy(isLoading = false, isSuccess = true)
                }
            }.onError { onError ->
                _retryLoadingState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = false,
                        error = Error("Invalid", onError.localizedMessage ?: "")
                    )
                }
            }.onFailure { onFailure ->
                _retryLoadingState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = false,
                        error = Error(onFailure.title ?: "", onFailure.message ?: "")
                    )
                }
            }
        }

    }


    fun hitCancelBooking() {

        viewModelScope.launch {
            _retryLoadingState.update {
                it.copy(isLoading = true, isSuccess = false, isCancelSuccess = false)
            }

            if (uiState.value.confirmBookingSearchDriverResponseData?.status == "failed" || uiState.value.confirmBookingSearchDriverResponseData?.status == "cancelled") {
                _retryLoadingState.update {
                    it.copy(isLoading = false, isSuccess = false, isCancelSuccess = true)
                }
                return@launch
            }

            val requestModel = DefaultRequestModel()
            requestModel.url =
                "${UrlName.CANCEL_BOOKING}/${uiState.value.confirmBookingSearchDriverResponseData?.id}"

            ApiRepository().post(requestModel, MainApiResponseData::class.java).onSuccess {
                _retryLoadingState.update {
                    it.copy(isLoading = false, isSuccess = false, isCancelSuccess = true)
                }
            }.onError { onError ->
                _retryLoadingState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = false,
                        isCancelSuccess = false,
                        error = Error("Invalid", onError.localizedMessage ?: "")
                    )
                }
            }.onFailure { onFailure ->
                _retryLoadingState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = false,
                        isCancelSuccess = false,
                        error = Error(onFailure.title ?: "", onFailure.message ?: "")
                    )
                }
            }
        }

    }

    fun resetIsSuccess() {
        _retryLoadingState.update {
            it.copy(isLoading = false, isSuccess = false, error = null, isCancelSuccess = false)
        }
    }

    fun convertToMinSec(timestamp: Long): String {
        val absTimestamp = kotlin.math.abs(timestamp)
        val minutes = absTimestamp / 60
        val seconds = absTimestamp % 60
        val formattedTime = String.format("%02d:%02d", minutes, seconds)
        if (timestamp < 0){
            stopTimer()
        }
        return formattedTime
    }

    fun subtractTimestamps(timestamp1: Long): Long {
        // 1 is running time
        // 2 is my time
        return timestamp1 - (Calendar.getInstance().timeInMillis) / 1000
    }


//    fun renderTime(expires_at: Long): String {
//        return convertToMinSec(subtractTimestamps(expires_at))
//    }

    fun startRunningTimer(expires_at: Long) {
        timer = Timer() // Create a new Timer instance

        timer?.schedule(object : TimerTask() {
            override fun run() {
                viewModelScope.launch {
                    updateTimerState(subtractTimestamps(expires_at))
                }
            }
        }, 0, 1000)
    }

    fun updateTimerState(timeStamp: Long) {
        _timerState.update {
            it.copy(timeStamp = timeStamp, timerToShow = convertToMinSec(timeStamp))
        }
    }

    fun stopTimer() {
        timer?.cancel() // Cancel the timer
        timer = null // Reset the timer to avoid reuse
    }




}