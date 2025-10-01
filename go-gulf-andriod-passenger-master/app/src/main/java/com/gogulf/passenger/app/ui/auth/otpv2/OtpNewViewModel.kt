package com.gogulf.passenger.app.ui.auth.otpv2

import android.app.Activity
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider
import com.google.gson.JsonObject
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.model.auth.PassengerLoginResponseData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.data.model.MainResponseData
import com.gogulf.passenger.app.data.model.OtpResponseModel
import com.gogulf.passenger.app.data.model.PassengerLoginResponseMainData
import com.gogulf.passenger.app.data.model.SignInResponseModel
import com.gogulf.passenger.app.data.repository.ApiRepository
import com.gogulf.passenger.app.data.repository.FirebaseAuthRepository
import com.gogulf.passenger.app.utils.CommonUtils
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader

class OtpNewViewModel : ViewModel() {
    var verificationIdMy: String? = null

    var phoneNumber: String = ""

    private val _uiState = MutableStateFlow(OTPUIState(""))
    val uiState: StateFlow<OTPUIState> = _uiState.asStateFlow()

    var customLoader: CustomLoader? = null

    private val _data = MutableLiveData<PassengerLoginResponseData?>(null)
    val data: LiveData<PassengerLoginResponseData?> = _data


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
        _uiState.update {
            it.copy(otp = letter)
        }
        if (uiState.value.otp.isNotEmpty()) {
            if (uiState.value.otp.length == 6) {
                loginUsingFireBase()
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


    private fun loginUsingFireBase() {
        _uiState.update { it ->
            it.copy(isUILoading = true)
        }
        viewModelScope.launch {
            val credential = PhoneAuthProvider.getCredential(verificationIdMy!!, uiState.value.otp)
            FirebaseAuthRepository().signIn<MainResponseData<SignInResponseModel>>(
                credential
            )?.onSuccess {
                FirebaseAuth.getInstance().currentUser?.let { it1 -> hitLogin(it1) }
            }?.onFailure { failure ->
                _uiState.update { it ->
                    it.copy(
                        isUILoading = false,
                        errorMessage = Error(failure.title ?: "", failure.message ?: "")
                    )
                }
            }

        }
    }

    fun hitLogin(user: FirebaseUser) {
        viewModelScope.launch {
            val body = JsonObject()
            body.addProperty("uid", user.uid)
            body.addProperty("mobile", user.phoneNumber)
            body.addProperty("device_token", CommonUtils.getPrefFirebaseToken())
            val request = DefaultRequestModel()
            request.url = UrlName.LOGIN
            request.body = body

            ApiRepository().post(
                request, PassengerLoginResponseMainData::class.java
            ).onSuccess {
                _uiState.update { it ->
                    it.copy(isUILoading = false, isLoginSuccess = true)
                }
                _data.value = it.data
            }.onFailure { error ->
                _uiState.update { it ->
                    it.copy(
                        isUILoading = false, errorMessage = Error(
                            title = error.title ?: "", message = error.message ?: ""
                        )
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

    fun resendOTP(activity: Activity) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isUILoading = true)
            }
            FirebaseAuthRepository().sendOTP<MainResponseData<OtpResponseModel>>(
                activity, phoneNumber
            )?.onSuccess { data ->
                _uiState.update {
                    it.copy(isUILoading = false, otpResponseModel = data.data, isSuccess = true)
                }
                startTimer()
                verificationIdMy = data.data?.verificationId
            }?.onPhoneVerified { p0 ->
                _uiState.update {
                    it.copy(
                        isUILoading = false,
                        isVerified = true,
                        phoneAuthCredential = p0,
                        isSuccess = true
                    )
                }
            }?.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isUILoading = false,
                        errorMessage = Error(error.title ?: "", error.message ?: "")
                    )
                }
            }
        }
    }

    fun runDone(activity: OTPActivityV2) {
        activity.onDoneClicked()
    }
}