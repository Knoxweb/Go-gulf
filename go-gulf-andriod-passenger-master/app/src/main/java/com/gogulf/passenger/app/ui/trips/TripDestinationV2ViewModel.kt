package com.gogulf.passenger.app.ui.trips

import CollectionInterface
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.gogulf.passenger.app.data.model.LocationModel
import com.gogulf.passenger.app.data.model.ShortcutModelResponse
import com.gogulf.passenger.app.data.repository.FirebaseRepository
import com.gogulf.passenger.app.data.repository.FirestoreCollectionLiveRepository
import com.gogulf.passenger.app.ui.getaride.ShortcutAdapter
import com.gogulf.passenger.app.ui.getaridev2.RecyclerShortcutAdapter
import com.gogulf.passenger.app.ui.shortcuts.ShortcutAddModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TripDestinationV2ViewModel : ViewModel() {

    var activeLocation = 1


    private val _uiState = MutableStateFlow(BookingLocationSelectionUIState())
    val uiState: StateFlow<BookingLocationSelectionUIState> = _uiState.asStateFlow()
    var shortcutAddModel: ShortcutAddModel? = null

    fun runDone(activity: TripDestinationActivityV2) {
        activity.onDoneClicked()
    }

    private var shortcutListener: ListenerRegistration? = null



    var mShortcutList = ArrayList<ShortcutModelResponse>()
    lateinit var adapter: ShortcutAdapter

    private val _shortcutAdapter = MutableLiveData<RecyclerShortcutAdapter>()
    val shortcutAdapter: LiveData<RecyclerShortcutAdapter>
        get() = _shortcutAdapter

    init {
        _shortcutAdapter.value = RecyclerShortcutAdapter(this)
        getShortcuts()
    }

    fun updateSelectedPickUpLocation(selectedLocation: LocationModel?) {
        _uiState.update {
            it.copy(
                selectedPickupLocation = selectedLocation,
                selectedPickupOnScreen = selectedLocation?.address ?: "Select pick up location"
            )
        }

    }

    fun updateSelectedDropOffLocation(selectedLocation: LocationModel?) {
        _uiState.update {
            it.copy(
                selectedDropOffLocation = selectedLocation,
                selectedDropOffLocationOnScreen = selectedLocation?.address
                    ?: "Select drop off location"
            )
        }
    }

    fun getDestination(shortcut: ShortcutModelResponse) {
        if(activeLocation == 1){
            updateSelectedDropOffLocation(
                LocationModel(
                    address = shortcut?.address!!,
                    latitude = shortcut?.lat!!,
                    longitude = shortcut?.lng!!
                )
            )
        }
        else{
            updateSelectedPickUpLocation(
                LocationModel(
                    address = shortcut?.address!!,
                    latitude = shortcut?.lat!!,
                    longitude = shortcut?.lng!!
                )
            )
        }

    }




    private fun getShortcuts() {
        viewModelScope.launch {
            val collectionReference = FirebaseRepository().getBaseDoc().collection("addresses")
            FirestoreCollectionLiveRepository().get<ShortcutModelResponse>(
                object : CollectionInterface {
                    override fun listeners(listener: ListenerRegistration?) {
                        shortcutListener = listener
                    }
                }, collectionReference = collectionReference
            ).collect { response ->
                response.onSuccess { onSuccessData ->
                    mShortcutList.clear()
                    mShortcutList.addAll(onSuccessData)
                    mShortcutList.add(ShortcutModelResponse(id = 0, name = "Add"))
                    _shortcutAdapter.value?.submitList(mShortcutList.toMutableList())
                }
                response.onError { error ->
                    Log.e("ChauffeursViewModel", "hitDrivers: $error")
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        shortcutListener?.remove()
    }
}