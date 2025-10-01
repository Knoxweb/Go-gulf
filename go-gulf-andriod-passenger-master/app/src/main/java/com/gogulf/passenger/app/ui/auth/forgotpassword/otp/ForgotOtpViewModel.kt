package com.gogulf.passenger.app.ui.auth.forgotpassword.otp

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.ui.auth.otpv2.OTPUIState
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.data.model.MessageResponseMainData
import com.gogulf.passenger.app.data.repository.ApiRepository

class ForgotOtpViewModel : ViewModel() {
    var customLoader: CustomLoader? = null
    private val _uiState = MutableStateFlow(OTPUIState(""))
    val uiState: StateFlow<OTPUIState> = _uiState.asStateFlow()

    private val _resend = MutableStateFlow(ForgetOtpUIState())
    val resend: StateFlow<ForgetOtpUIState> = _resend.asStateFlow()

    var email: String? = ""

    private var _timerStartingValue: Long = 60000
    private var _currentTime = MutableLiveData<Long>()

    private var _isFinished = MutableLiveData<Boolean>(false)
    private var isTimerStarted = false

    fun resetValues() {
        isTimerStarted = false
    }

    val currentTime: LiveData<Long>
        get() = _currentTime

    val isFinished: LiveData<Boolean>
        get() = _isFinished


    fun startTimer() {
        if (!isTimerStarted) {
            _isFinished.value = false
            object : CountDownTimer(_timerStartingValue, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    _currentTime.value = millisUntilFinished / 1000
                }

                override fun onFinish() {
                    _isFinished.value = true
                }
            }.start()
            isTimerStarted = true
        }

    }


    fun appendOTP(letter: String) {
        _uiState.value = OTPUIState(letter)

        if (uiState.value.otp.isNotEmpty()) {
            if (uiState.value.otp.length == 6) {
                hitForgotPasswordReset()
            } else {
                _uiState.update {
                    it.copy(
                        errorMessage = Error(
                            title = "Invalid", message = "Enter 6 digit otp"
                        )
                    )
                }
            }
        } else {
            _uiState.update {
                it.copy(
                    errorMessage = Error(
                        title = "Invalid", message = "Enter 6 digit otp"
                    )
                )
            }
        }
    }

    fun hitForgotPasswordReset() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isUILoading = true)
            }
            val model = DefaultRequestModel()
            model.url = UrlName.FORGET_PASSWORD_OTP_CHECK
            model.body.addProperty("email", email)
            model.body.addProperty("code", uiState.value.otp)
            ApiRepository().post(
                model, MessageResponseMainData::class.java
            ).onSuccess { onSuccessData ->
                _uiState.update {
                    it.copy(
                        isUILoading = false,
                        isSuccess = true
                    )
                }
            }.onFailure { onFailureData ->
                _uiState.update {
                    it.copy(
                        isUILoading = false,
                        errorMessage = Error(onFailureData.title ?: "", onFailureData.message ?: "")
                    )
                }
            }.onError { onErrorData ->
                _uiState.update {
                    it.copy(
                        isUILoading = false,
                        errorMessage = Error("Error", onErrorData.message ?: "")
                    )
                }
            }
        }
    }


    fun hitForgotPassword() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isUILoading = true)
            }
            val model = DefaultRequestModel()
            model.url = UrlName.FORGET_PASSWORD
            model.body.addProperty("email", email)
            model.body.addProperty("domain_id", "1")
            ApiRepository().post(
                model, MessageResponseMainData::class.java
            ).onSuccess { onSuccessData ->
                _uiState.update {
                    it.copy(
                        isUILoading = false)
                }
                _resend.update {
                    it.copy(
                        messageResponseData = onSuccessData.data
                    )
                }
                Log.d("ForgotPasswordViewModel", "hitForgotPassword: ${onSuccessData.data}")
            }.onFailure { onFailureData ->
                _uiState.update {
                    it.copy(
                        isUILoading = false,
                        errorMessage = Error(onFailureData.title ?: "", onFailureData.message ?: "")
                    )
                }
            }
        }
    }


    fun clearError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

    fun clearIsSuccess() {
        _uiState.update {
            it.copy(isSuccess = false)
        }
    }

    fun runDone(activity: ForgotOtpActivity) {
        activity.onDoneClicked()
    }

}