package com.gogulf.passenger.app.ui.auth.registerv2

import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gogulf.passenger.app.App.Companion.preferenceHelper
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.model.MainWithRegisterModel
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import com.gogulf.passenger.app.utils.objects.PrefConstant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterV2ViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUIState())
    val uiState: StateFlow<RegisterUIState> = _uiState.asStateFlow()

    val fullName: ObservableField<String> = ObservableField("")
    val email: ObservableField<String> = ObservableField("")
    var profileImageBase64: String = ""

    var path: String? = null
    var capturedProfilePicURI: Uri? = null
    var customLoader: CustomLoader? = null
    private val _data = MutableLiveData<MainWithRegisterModel?>(null)
    val data: LiveData<MainWithRegisterModel?> = _data

//    fun register() {
//        viewModelScope.launch {
//            _uiState.update {
//                it.copy(isUILoading = true)
//            }
//            val requestModel = DefaultRequestModel()
//            requestModel.url = UrlName.post_register
//            val jsonObject = JsonObject()
//            jsonObject.addProperty(
//                "name", fullName.get()
//            )
//            jsonObject.addProperty("email", email.get())
//            if (profileImageBase64.isNotEmpty()) jsonObject.addProperty(
//                "image", "${Constants.IMAGE_64}$profileImageBase64"
//            )
//            jsonObject.addProperty(
//                "uid",
//                preferenceHelper.getValue(PrefConstant.FIREBASE_UID, APIConstants.TestUI) as String
//            )
//            jsonObject.addProperty(
//                "phone_cc",
//                preferenceHelper.getValue(PrefConstant.M_CC, APIConstants.TestCountry) as String
//            )
//            jsonObject.addProperty(
//                "phone",
//                preferenceHelper.getValue(PrefConstant.MOBILE, APIConstants.TestNumber) as String
//            )
//            requestModel.body = jsonObject
//            ApiRepository().post(requestModel, MainWithRegisterModel::class.java).onSuccess {
//                _uiState.update {
//                    it.copy(isUILoading = false, isSuccess = true)
//                }
//                _data.value = it
//            }.onFailure { onFailure ->
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        isSuccess = false,
//                        error = Error(onFailure.title ?: "Invalid", onFailure.message ?: "")
//                    )
//                }
//            }.onError {
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        isSuccess = false,
//                        error = Error(it.error?.title ?: "Invalid", it.error?.message ?: "")
//                    )
//                }
//
//
//            }
//        }
//
//    }

    fun postDeviceToken() {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.post_deviceToken
        requestModel.body.addProperty(
            "device_token", preferenceHelper.getValue(PrefConstant.DEVICE_TOKEN, "") as String
        )
        requestModel.body.addProperty(
            "device_type", "android"
        )


//        viewModelScope.launch {
//            ApiRepository().post(requestModel, MainWithAuthentications::class.java).onSuccess {
//                _uiState.update {
//                    it.copy(isLoading = false, isSuccess = true)
//                }
//            }
//
//        }

    }

    fun clearError() {
        _uiState.update {
            it.copy(error = null)
        }
    }

}