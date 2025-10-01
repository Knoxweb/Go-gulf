package com.gogulf.passenger.app.ui.auth.loginwithemail

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.gson.JsonObject
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.data.model.LoginWithEmailResponseMainData
import com.gogulf.passenger.app.data.model.MainResponseData
import com.gogulf.passenger.app.data.model.PassengerLoginResponseMainData
import com.gogulf.passenger.app.data.repository.ApiRepository
import com.gogulf.passenger.app.data.repository.FirebaseAuthRepository
import com.gogulf.passenger.app.utils.CommonUtils
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginWithEmailViewModel : ViewModel() {


    val email = ObservableField("")
    val password = ObservableField("")

    private val _uiState = MutableStateFlow(LoginWithEmailUIState())
    val uiState = _uiState.asStateFlow()

    var customLoader: CustomLoader? = null

    fun hitEmailLogin() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val data = DefaultRequestModel()
            data.body.addProperty("email", uiState.value.email)
            data.body.addProperty("password", uiState.value.password)
            data.url = "email-login"
            ApiRepository().post(
                data, LoginWithEmailResponseMainData::class.java
            ).onSuccess { onSuccessData ->
                _uiState.update {
                    it.copy(
                        loginWithEmailResponseData = onSuccessData.data, isLoginSuccess = true
                    )
                }
                onSuccessData.data?.firebaseAuthToken?.let { firebaseLogin(it) }
            }.onFailure { onFailedData ->
                _uiState.update {
                    it.copy(
                        isLoading = false, error = Error(
                            title = onFailedData.title ?: "", message = onFailedData.message ?: ""
                        )
                    )
                }
            }.onError { onErrorData ->
                _uiState.update {
                    it.copy(
                        isLoading = false, error = Error(
                            title = "Invalid", message = onErrorData.localizedMessage ?: ""
                        )
                    )
                }
            }

        }
    }

    private fun firebaseLogin(token: String) {
        viewModelScope.launch {
            FirebaseAuthRepository().signInWithCustomToken<MainResponseData<Any?>>(token)
                .onSuccess { onSuccessData ->
                    FirebaseAuth.getInstance().currentUser?.let { it1 -> hitLogin(it1) }
                    _uiState.update { it.copy(isFirebaseLoginSuccess = true) }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false, error = com.gogulf.passenger.app.data.model.Error(
                                title = error.title ?: "", message = error.message ?: ""
                            )
                        )
                    }
                }
        }
    }

    private fun hitLogin(user: FirebaseUser) {
        viewModelScope.launch {
            val body = JsonObject()
            body.addProperty("uid", user.uid)
            Log.e("Rohan Paudel", "Phone Number "+ user.phoneNumber)
            body.addProperty("mobile", user.phoneNumber)
            body.addProperty("device_token", CommonUtils.getPrefFirebaseToken())

            val request = DefaultRequestModel()
            request.url = "login"
            request.body = body

            ApiRepository().post(
                request, PassengerLoginResponseMainData::class.java
            ).onSuccess { onSuccessData ->
                _uiState.update { it ->
                    it.copy(
                        isLoading = false,
                        isNumberLoginSuccess = true,
                        passengerLoginResponseData = onSuccessData.data
                    )
                }

            }.onFailure { error ->
                _uiState.update { it ->
                    it.copy(
                        isLoading = false, error = Error(
                            title = error.title ?: "", message = error.message ?: ""
                        )
                    )
                }
            }.onError { onErrorData ->
                _uiState.update {
                    it.copy(
                        isLoading = false, error = Error(
                            title = "Invalid", message = onErrorData.localizedMessage ?: ""
                        )
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

    fun updateEmail() {
        _uiState.update {
            it.copy(email = email.get() ?: "")
        }
    }

    fun updatePassword() {
        _uiState.update {
            it.copy(password = password.get() ?: "")
        }
    }


}