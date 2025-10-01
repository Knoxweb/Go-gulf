package com.gogulf.passenger.app.ui.currentridenew

import CollectionInterface
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.firebase.firestore.ListenerRegistration
import com.gogulf.passenger.app.data.model.CurrentBookingResponseData
import com.gogulf.passenger.app.data.model.DriverPositionData
import com.gogulf.passenger.app.data.repository.FirebaseRepository
import com.gogulf.passenger.app.data.repository.FirestoreDocumentLiveRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.gogulf.passenger.app.data.model.PobOtpResponseData
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NewCurrentRideViewModel: ViewModel() {
    private val _currentRideUiState = MutableStateFlow(DriverUiState())
    val currentRideUiState: StateFlow<DriverUiState> = _currentRideUiState.asStateFlow()

    private val _driverState = MutableStateFlow(DriverPositionUIState())
    val driverState: StateFlow<DriverPositionUIState> = _driverState.asStateFlow()


    val currentRideResponseData: MutableLiveData<CurrentBookingResponseData?> =
        MutableLiveData()
    val pobOtpResponseData: MutableLiveData<PobOtpResponseData?> =
        MutableLiveData(null)


    var driverLocationMarker: Marker? = null
    var driverLastLocation: LatLng? = null



    init {
        getCurrentRide()
        getListenForOtp()
        getDriverPosition()
    }
    var currentOtpListener: ListenerRegistration? = null

    var driverStateListener: ListenerRegistration? = null

    private fun getDriverPosition() {
        viewModelScope.launch {
            val document = FirebaseRepository().getBaseDoc().collection("data").document(
                "driver_position"
            )
            FirestoreDocumentLiveRepository().get<DriverPositionData>(object :
                CollectionInterface {
                override fun listeners(listener: ListenerRegistration?) {
                    driverStateListener = listener
                }
            }, document).collect { response ->
                response.onSuccess { onSuccessData ->
                    Log.e("Rohan Paudel", "getDriverPosition: $onSuccessData")
                    _driverState.update {
                        it.copy(
                            driverPositionSuccessData = onSuccessData,
                        )
                    }
                }
                response.onError { onErrorData ->
//                    _uiState.update {
//                        it.copy(
//                            isLoading = false,
//                            error = Error("Error", onErrorData.message ?: ""),
//                            isSuccess = true
//                        )
//                    }
                }
            }
        }
    }


    private fun getListenForOtp() {
        viewModelScope.launch {
            val document = FirebaseRepository().getBaseDoc().collection("data").document(
                "pob_otp"
            )
            FirestoreDocumentLiveRepository().get<PobOtpResponseData>(object :
                CollectionInterface {
                override fun listeners(listener: ListenerRegistration?) {
                    currentOtpListener = listener
                }
            }, document).collect { response ->
                response.onSuccess { onSuccessData ->
                    pobOtpResponseData.postValue(onSuccessData)
                }
                response.onError { onErrorData ->
                    Log.e("Error", onErrorData.localizedMessage ?: "")
                }
            }
        }
    }

    var currentRideListener: ListenerRegistration? = null


    private fun getCurrentRide() {
        viewModelScope.launch {
            val document = FirebaseRepository().getBaseDoc().collection("data").document(
                "current_booking"
            )
            FirestoreDocumentLiveRepository().get<CurrentBookingResponseData>(object :
                CollectionInterface {
                override fun listeners(listener: ListenerRegistration?) {
                    currentRideListener = listener
                }
            }, document).collect { response ->
                response.onSuccess { onSuccessData ->
                    currentRideResponseData.postValue(onSuccessData)
                    _currentRideUiState.update {
                        it.copy(bookingResponse = onSuccessData)
                    }
                }
                response.onError { onErrorData ->
                    Log.e("Error", onErrorData.localizedMessage ?: "")
                    currentRideResponseData.postValue(null)
                }
            }
        }
    }

    fun updateMap(mMap: GoogleMap) {
        _currentRideUiState.update {
            it.copy( mMap = mMap)
        }
    }

    fun clearIsCameraBoundsUpdated() {
        _currentRideUiState.update {
            it.copy(isCameraBoundsUpdated = true)
        }
    }



    override fun onCleared() {
        super.onCleared()
        currentOtpListener?.remove()
        currentRideListener?.remove()
    }
}