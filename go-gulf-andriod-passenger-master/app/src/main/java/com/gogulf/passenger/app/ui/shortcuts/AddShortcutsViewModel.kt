package com.gogulf.passenger.app.ui.shortcuts

import CollectionInterface
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.data.model.MainApiResponseData
import com.gogulf.passenger.app.data.model.ShortcutModelResponse
import com.gogulf.passenger.app.data.repository.ApiRepository
import com.gogulf.passenger.app.data.repository.FirebaseRepository
import com.gogulf.passenger.app.data.repository.FirestoreCollectionLiveRepository
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddShortcutsViewModel(
    private val id: Int? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddShortcutsUIState())
    val uiState: StateFlow<AddShortcutsUIState> = _uiState.asStateFlow()

    val address = ObservableField("")
    val lat = ObservableField("")
    val lng = ObservableField("")
    val name = ObservableField("")

    var isSet: Boolean = false
    var customLoader: CustomLoader? = null

    var mShortcutList = ArrayList<ShortcutModelResponse>()
    private var shortcutListener: ListenerRegistration? = null


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
                    if (!isSet) {
                        if (id != null && id != 0) {
                            mShortcutList.find { it.id == id}?.let {
                                name.set(it.name)
                                address.set(it.address)
                                lat.set(it.lat.toString())
                                lng.set(it.lng.toString())
                            }
                        }
                        isSet = true
                    }

                }
                response.onError { error ->
                    Log.e("ChauffeursViewModel", "hitDrivers: $error")
                }
            }
        }
    }

    init {
        getShortcuts()
    }

    fun addAddress() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            val defaultRequestModel = DefaultRequestModel()
            defaultRequestModel.url = UrlName.ADD_ADDRESS
            defaultRequestModel.body.addProperty("name", name.get())
            defaultRequestModel.body.addProperty("address", address.get())
            defaultRequestModel.body.addProperty("lat", lat.get())
            defaultRequestModel.body.addProperty("lng", lng.get())

            ApiRepository().post(defaultRequestModel, MainApiResponseData::class.java)
                .onSuccess { onSuccessData ->
                    _uiState.update {
                        it.copy(
                            isLoading = false, isSuccess = true, successMessage = Error(
                                title = onSuccessData.title ?: "Success",
                                message = onSuccessData.message ?: ""
                            )
                        )
                    }
                }.onError { onErrorData ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = false,
                            error = Error(title = "Invalid", message = onErrorData.localizedMessage)
                        )
                    }
                }.onFailure { onFailureData ->
                    _uiState.update {
                        it.copy(
                            isLoading = false, isSuccess = false, error = Error(
                                title = onFailureData.title ?: "Invalid",
                                message = onFailureData.message ?: ""
                            )
                        )
                    }
                }

        }
    }

    fun editAddress(id: Int?) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            val defaultRequestModel = DefaultRequestModel()
            defaultRequestModel.url = "${UrlName.EDIT_ADDRESS}/$id}"
            defaultRequestModel.body.addProperty("name", name.get())
            defaultRequestModel.body.addProperty("address", address.get())
            defaultRequestModel.body.addProperty("lat", lat.get())
            defaultRequestModel.body.addProperty("lng", lng.get())

            ApiRepository().post(defaultRequestModel, MainApiResponseData::class.java)
                .onSuccess { onSuccessData ->
                    _uiState.update {
                        it.copy(
                            isLoading = false, isSuccess = true, successMessage = Error(
                                title = onSuccessData.title ?: "Success",
                                message = onSuccessData.message ?: ""
                            )
                        )
                    }
                }.onError { onErrorData ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = false,
                            error = Error(title = "Invalid", message = onErrorData.localizedMessage)
                        )
                    }
                }.onFailure { onFailureData ->
                    _uiState.update {
                        it.copy(
                            isLoading = false, isSuccess = false, error = Error(
                                title = onFailureData.title ?: "Invalid",
                                message = onFailureData.message ?: ""
                            )
                        )
                    }
                }

        }
    }



    fun resetError() {
        _uiState.update {
            it.copy(error = null)
        }
    }

    fun resetSuccessMessage() {
        _uiState.update {
            it.copy(isSuccess = false, successMessage = null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        shortcutListener?.remove()
    }

}