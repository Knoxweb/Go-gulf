package com.gogulf.passenger.app.ui.auth.forgotpassword.changepassword

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.model.MainApiResponseData
import com.gogulf.passenger.app.data.repository.ApiRepository
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.gogulf.passenger.app.data.model.Error

class ChangePasswordViewModel : ViewModel() {

    var customLoader: CustomLoader? = null
    private val _uiState = MutableStateFlow(ChangePasswordUIState())
    val uiState: StateFlow<ChangePasswordUIState> = _uiState.asStateFlow()

    var otp: String? = ""
    var email: String? = ""

    val confirmPassword: ObservableField<String> = ObservableField("")
    val newPassword: ObservableField<String> = ObservableField("")


    fun hitForgotPasswordReset() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val model = DefaultRequestModel()
            model.url = UrlName.FORGET_PASSWORD_RESET
            model.body.addProperty("email", email)
            model.body.addProperty("code", otp)
            model.body.addProperty("password", newPassword.get())
            ApiRepository().post(
                model, MainApiResponseData::class.java
            ).onSuccess { onSuccessData ->
                _uiState.update {
                    it.copy(
                        isLoading = false, isSuccess = true
                    )
                }
            }.onFailure { onFailureData ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = Error(onFailureData.title ?: "", onFailureData.message ?: "")
                    )
                }
            }.onError { onErrorData ->
                _uiState.update {
                    it.copy(
                        isLoading = false, error = Error("Error", onErrorData.message ?: "")
                    )
                }
            }
        }
    }

}