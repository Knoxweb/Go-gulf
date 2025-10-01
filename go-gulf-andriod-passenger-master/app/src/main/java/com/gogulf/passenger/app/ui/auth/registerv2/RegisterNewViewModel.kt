package com.gogulf.passenger.app.ui.auth.registerv2

import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.model.MainApiResponseData
import com.gogulf.passenger.app.data.repository.ApiRepository
import com.gogulf.passenger.app.ui.auth.login.CountryModel
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.utils.CommonUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class RegisterNewViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUIState())
    val uiState: StateFlow<RegisterUIState> = _uiState.asStateFlow()
    val phoneNumber = ObservableField("")

    //    var pattern: PatternedTextWatcher? = null
    private val _phoneNumberPattern = MutableLiveData<String>().apply {
        value = ""
    }
    val fullName: ObservableField<String> = ObservableField("")
    var profileImageBase64: String = ""

    var path: String? = null

    var capturedProfilePicURI: Uri? = null

    var passengerType: String = ""
    val firstName = ObservableField("")
    val lastName = ObservableField("")
    val email = ObservableField("")
    var customLoader: CustomLoader? = null


    init {
        setPhoneNumberPattern()
    }

    fun updateCountrySelected(countryModel: CountryModel) {
        phoneNumber.set("")
        _uiState.update {
            it.copy(countryModel = countryModel, phoneNumber = "")
        }
        setPhoneNumberPattern()
    }

    fun updatePhoneNumber() {
        _uiState.update {
            it.copy(phoneNumber = phoneNumber.get()!!)
        }
    }


    val getPhoneNumberPattern: LiveData<String>
        get() = _phoneNumberPattern

    private fun setPhoneNumberPattern() {
        _phoneNumberPattern.value = uiState.value.countryModel?.pattern
    }

    fun hitFirstUpdate() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isUILoading = true)
            }
            val model = DefaultRequestModel()
            model.url = UrlName.FIRST_UPDATE
            if (path != null) {
                val builder = MultipartBody.Builder()
                builder.setType(MultipartBody.FORM)
                val fileCoverImage = File(path)
                builder.addFormDataPart(
                    "profile_image",
                    fileCoverImage.name,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), fileCoverImage)
                )
                builder.addFormDataPart("name", fullName.get() ?: "")
                builder.addFormDataPart("email", email.get() ?: "")
                model.multipartBody = builder.build()
            } else {
                val body = JsonObject()
                body.addProperty("name", fullName.get())
                body.addProperty("email", email.get())
                model.body = body
            }
            ApiRepository().post(model, MainApiResponseData::class.java).onSuccess {
                _uiState.update {
                    it.copy(isUILoading = false, isFirstUpdateSuccess = true)
                }
            }.onFailure { onFailureData ->
                _uiState.update {
                    it.copy(
                        isUILoading = false,
                        isFirstUpdateSuccess = false,
                        error = Error(onFailureData.title ?: "", onFailureData.message ?: "")
                    )
                }
            }.onError { onErrorData ->
                _uiState.update {
                    it.copy(
                        isUILoading = false,
                        isFirstUpdateSuccess = false,
                        error = Error("Error", onErrorData.localizedMessage ?: "")
                    )
                }
            }
        }
    }

//    fun hitFirstUpdate() {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isUILoading = true) }
//            val requestBody = DefaultRequestModel()
//            requestBody.url = UrlName.FIRST_UPDATE
//            requestBody.body.addProperty("name", fullName.get())
//            requestBody.body.addProperty("email", email.get())
////            requestBody.body.addProperty("password", uiState.value.newPassword.get())
//
//            Log.d("TAG", "hitFirstUpdate: ${requestBody.body}")
//            ApiRepository().post(requestBody, MainApiResponseData::class.java).onSuccess {
//                _uiState.update {
//                    it.copy(isUILoading = false, isFirstUpdateSuccess = true)
//                }
//            }.onFailure {onFailureData ->
//                _uiState.update {
//                    it.copy(isUILoading = false, isFirstUpdateSuccess = false, error = Error(onFailureData.title?:"", onFailureData.message?:""))
//                }
//            }.onError {onErrorData ->
//                _uiState.update {
//                    it.copy(isUILoading = false, isFirstUpdateSuccess = false, error = Error("Error", onErrorData.localizedMessage?:""))
//                }
//            }
//        }
//
//    }

    fun updateFirstName() {
        _uiState.update {
            it.copy(firstName = firstName.get() ?: "")
        }
    }

    fun updateLastName() {
        _uiState.update {
            it.copy(lastName = lastName.get() ?: "")
        }
    }

    fun updateEmail() {
        _uiState.update {
            it.copy(email = email.get() ?: "")
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(error = null)
        }
    }

    fun hitLogout() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isUILoading = true)
            }
            val body = JsonObject()
            body.addProperty("device_token", CommonUtils.getPrefFirebaseToken())
            val request = DefaultRequestModel()
            request.url = UrlName.LOGOUT
            request.body = body

            ApiRepository().post(
                request, MainApiResponseData::class.java
            ).onSuccess { onSuccessData ->
                _uiState.update { it ->
                    it.copy(
                        isUILoading = false, onLogoutSuccess = true
                    )
                }

            }.onFailure { error ->
                _uiState.update { it ->
                    it.copy(
                        isUILoading = false, error = Error(
                            title = error.title ?: "", message = error.message ?: ""
                        )
                    )
                }
            }.onError { error ->
                _uiState.update { it ->
                    it.copy(
                        isUILoading = false, error = Error(
                            title = "Error", message = error.message ?: ""
                        )
                    )
                }
            }
        }

    }


}