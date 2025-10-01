package com.gogulf.passenger.app.ui.auth.forgotpassword

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.model.MessageResponseMainData
import com.gogulf.passenger.app.data.repository.ApiRepository
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.gogulf.passenger.app.data.model.Error

class ForgotPasswordViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUIState())
    val uiState: StateFlow<ForgotPasswordUIState> = _uiState.asStateFlow()

    val email = ObservableField("")
    var customLoader: CustomLoader? = null


    fun updateEmail() {
        _uiState.update {
            it.copy(email = email.get() ?: "")
        }
    }


    fun hitForgotPassword() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val model = DefaultRequestModel()
            model.url = UrlName.FORGET_PASSWORD
            model.body.addProperty("email", uiState.value.email)
            model.body.addProperty("domain_id", "1")
            ApiRepository().post(
                model, MessageResponseMainData::class.java
            ).onSuccess { onSuccessData ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        messageResponseData = onSuccessData.data,
                        isSuccess = true
                    )
                }
                Log.d("ForgotPasswordViewModel", "hitForgotPassword: ${onSuccessData.data}")
            }.onFailure { onFailureData ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = Error(onFailureData.title ?: "", onFailureData.message ?: "")
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(error = null)
        }
    }

    fun clearSuccess() {
        _uiState.update {
            it.copy(isSuccess = false)
        }
    }
}