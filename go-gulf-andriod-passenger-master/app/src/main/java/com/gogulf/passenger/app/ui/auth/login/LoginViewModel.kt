package com.gogulf.passenger.app.ui.auth.login

import android.app.Activity
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.data.model.MainResponseData
import com.gogulf.passenger.app.data.model.OtpResponseModel
import com.gogulf.passenger.app.data.repository.FirebaseAuthRepository
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader


class LoginViewModel: ViewModel() {

    val phoneNumber = ObservableField("")
    private val _uiState = MutableStateFlow(LoginUIState(getCountryByCode(getCountryCode())))
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()
    var customLoader: CustomLoader? = null

    fun updateCountrySelected(countryModel: CountryModel) {
        phoneNumber.set("")
        _uiState.update {
            it.copy(countryModel = countryModel)
        }
    }

    fun updatePhoneNumber() {
        _uiState.update {
            it.copy(phoneNumber = phoneNumber.get()!!)
        }
    }

    fun sendOtp(activity: Activity) {
        viewModelScope.launch {
            if (uiState.value.phoneNumber.replace(" ", "").isEmpty()) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = false,
                        error = Error(
                            "Invalid",
                            "Invalid Phone Number")
                    )
                }
                return@launch
            }
            _uiState.update {
                it.copy(isLoading = true)
            }
            FirebaseAuthRepository().sendOTP<MainResponseData<OtpResponseModel>>(
                activity, uiState.value.countryModel?.dialCode + uiState.value.phoneNumber.replace(
                    " ", ""
                )
            )?.onCodeSent { s, forceResendingToken ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true,
                        otpResponseModel = OtpResponseModel(s, forceResendingToken)
                    )
                }
            }?.onSuccess { data ->
                _uiState.update {
                    it.copy(isLoading = false, otpResponseModel = data.data, isSuccess = true)
                }
            }?.onPhoneVerified { p0 ->
                _uiState.update {
                    it.copy(isLoading = false, isVerified = true, phoneAuthCredential = p0)
                }
            }?.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false, error = Error(error.title ?: "", error.message ?: "")
                    )
                }
            }
        }
    }

    fun onBackPressed() {
        onBackPressed()
    }

    fun resetError() {
        TODO("Not yet implemented")
    }

    fun startLoading() {
        _uiState.update {
            it.copy(isLoading = true)
        }
    }

    fun stopLoading() {
        _uiState.update {
            it.copy(isLoading = false)
        }
    }


    fun clearError() {
        _uiState.update {
            it.copy(error = null)
        }
    }

    fun resetIsSuccess() {
        _uiState.update {
            it.copy(isSuccess = false)
        }
    }

}