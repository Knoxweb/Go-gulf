package com.gogulf.passenger.app.ui.auth.otpv2

import android.app.Activity
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.gson.JsonObject
import com.gogulf.passenger.app.App.Companion.preferenceHelper
import com.gogulf.passenger.app.data.apidata.APIConstants
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.data.model.MainResponseData
import com.gogulf.passenger.app.data.model.MainWithAuthentications
import com.gogulf.passenger.app.data.model.OtpResponseModel
import com.gogulf.passenger.app.data.model.SignInResponseModel
import com.gogulf.passenger.app.data.repository.ApiRepository
import com.gogulf.passenger.app.data.repository.FirebaseAuthRepository
import com.gogulf.passenger.app.data.repository.MainRepository
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import com.gogulf.passenger.app.utils.objects.PrefConstant
import com.gogulf.passenger.app.utils.others.NetworkHelper
import com.gogulf.passenger.app.utils.others.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OTPV2ViewModel(
    private val mainRepository: MainRepository, private val networkHelper: NetworkHelper
) : ViewModel() {
    private var otpRequest = MutableLiveData<Resource<JsonObject>>()
    val otpResponse: MutableLiveData<Resource<JsonObject>>
        get() = otpRequest


    var verificationIdMy: String? = null

    var phoneNumber: String = ""

    private val _uiState = MutableStateFlow(OTPUIState(""))
    val uiState: StateFlow<OTPUIState> = _uiState.asStateFlow()


//    private val _data = MutableLiveData<PassengerLoginResponseData?>(null)
//    val data: LiveData<PassengerLoginResponseData?> = _data

    var customLoader: CustomLoader? = null

    private var _timerStartingValue: Long = 10000
    private var _currentTime = MutableLiveData<Long>()

    private var _isFinished = MutableLiveData<Boolean>(false)
    private var isTimerStarted = false
    private val _data = MutableLiveData<MainWithAuthentications?>(null)
    val data: LiveData<MainWithAuthentications?> = _data


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

    private fun loginUsingFireBase() {
        _uiState.update { it ->
            it.copy(isUILoading = true)
        }
        Log.e("OTPV2ViewModel Login in using firebase", "called this method")
        viewModelScope.launch {
            val credential = PhoneAuthProvider.getCredential(verificationIdMy!!, uiState.value.otp)
            FirebaseAuthRepository().signIn<MainResponseData<SignInResponseModel>>(
                credential
            )?.onSuccess {
                Log.e("OTPV2ViewModel Login in using firebase", "success")
                FirebaseAuth.getInstance().currentUser?.let { it1 -> hitLogin() }
            }?.onFailure { failure ->
                Log.e("OTPV2ViewModel Login in using firebase", "failure")
                _uiState.update { it ->
                    it.copy(
                        isUILoading = false,
                        errorMessage = Error(failure.title ?: "", failure.message ?: "")
                    )
                }
            }

        }
    }

    private fun hitLogin() {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.post_authentication
        requestModel.body.addProperty(
            "uid",
            preferenceHelper.getValue(PrefConstant.FIREBASE_UID, APIConstants.TestUI) as String
        )
        requestModel.body.addProperty(
            "phone_cc", preferenceHelper.getValue(
                PrefConstant.M_CC, APIConstants.TestCountry
            ) as String
        )
        requestModel.body.addProperty(
            "phone", preferenceHelper.getValue(
                PrefConstant.MOBILE, APIConstants.TestNumber
            ) as String
        )
        preferenceHelper.setValue(PrefConstant.AUTH_TOKEN, APIConstants.static_token)

        Log.e("OTPV2ViewModel hitLogin", "called")
        viewModelScope.launch {
            ApiRepository().post(requestModel, MainWithAuthentications::class.java).onSuccess {
                Log.e("OTPV2ViewModel hitLogin", "success")
                _uiState.update {
                    it.copy(isUILoading = false, isSuccess = true)
                }
                _data.value = it
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isUILoading = false,
                        errorMessage = Error(error.title ?: "", error.message ?: "")
                    )
                }
            }.onError { error ->
                _uiState.update {
                    it.copy(
                        isUILoading = false, errorMessage = Error("Invalid", error.message ?: "")
                    )
                }
            }

        }

    }

    fun runDone(activity: OTPActivityV2) {
        activity.onDoneClicked()
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

    fun postDeviceToken() {
        val requestModel = DefaultRequestModel()
        requestModel.url = UrlName.post_deviceToken
        requestModel.body.addProperty(
            "device_token", preferenceHelper.getValue(PrefConstant.DEVICE_TOKEN, "") as String
        )
        requestModel.body.addProperty(
            "device_type", "android"
        )


        viewModelScope.launch {
//            _uiState.update {
//                it.copy(isUILoading = true)
//            }

            if (networkHelper.isNetworkConnected()) {

                ApiRepository().post(requestModel, MainWithAuthentications::class.java).onSuccess {
                    _uiState.update {
                        it.copy(isUILoading = false, isSuccess = true)
                    }
                }

//                mainRepository.postMethod(requestModel, otpRequest)
            } else {
                _uiState.update {
                    it.copy(
                        isUILoading = false,
                        errorMessage = Error("Invalid", "No internet connection")
                    )
                }
            }
//            mainRepository.postMethod(requestModel, otpRequest)
        }
//
//        viewModelScope.launch {
//            deviceRequest.postValue(Resource.loading(null))
//            if (networkHelper.isNetworkConnected()) {
//                mainRepository.postMethod(requestModel, deviceRequest)
//            } else {
//                deviceRequest.postValue(Resource.error("No internet connection", null))
//            }
//        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

    fun validateOtp() {

    }
}