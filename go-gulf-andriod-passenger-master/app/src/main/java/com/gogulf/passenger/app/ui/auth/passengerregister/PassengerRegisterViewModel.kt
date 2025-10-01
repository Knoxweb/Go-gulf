package com.gogulf.passenger.app.ui.auth.passengerregister

import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.gson.JsonObject
import com.gogulf.passenger.app.data.apidata.DefaultRequestModel
import com.gogulf.passenger.app.data.apidata.UrlName
import com.gogulf.passenger.app.ui.auth.login.CountryModel
import com.gogulf.passenger.app.ui.auth.login.LoginUIState
import com.gogulf.passenger.app.ui.auth.login.getCountryByCode
import com.gogulf.passenger.app.ui.auth.login.getCountryCode
import com.gogulf.passenger.app.utils.customcomponents.CustomLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.data.model.LoginWithEmailResponseMainData
import com.gogulf.passenger.app.data.model.MainResponseData
import com.gogulf.passenger.app.data.model.PassengerLoginResponseMainData
import com.gogulf.passenger.app.data.repository.ApiRepository
import com.gogulf.passenger.app.data.repository.FirebaseAuthRepository
import com.gogulf.passenger.app.utils.CommonUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class PassengerRegisterViewModel : ViewModel() {

    var passengerType: String = ""
    val phoneNumber = ObservableField("")
    val email = ObservableField("")
    val password = ObservableField("")
    val confirmPassword = ObservableField("")
    val firstName = ObservableField("")
    val lastName = ObservableField("")
//    var pattern: PatternedTextWatcher? = null


    private val _phoneNumberPattern = MutableLiveData<String>().apply {
        value = ""
    }

    private val _phoneNumberState =
        MutableStateFlow(LoginUIState(getCountryByCode(getCountryCode())))
    val phoneNumberState: StateFlow<LoginUIState> = _phoneNumberState.asStateFlow()

    private val _uiState = MutableStateFlow(PassengerRegisterUIState())
    val uiState: StateFlow<PassengerRegisterUIState> = _uiState.asStateFlow()
    var customLoader: CustomLoader? = null


    fun clearError() {
        _uiState.update {
            it.copy(error = null)
        }
    }

    var profileImageBase64: String = ""

    var path: String? = null

    var capturedProfilePicURI: Uri? = null

    init {
        setPhoneNumberPattern()
    }

    fun updateCountrySelected(countryModel: CountryModel) {
        phoneNumber.set("")
        _phoneNumberState.update {
            it.copy(countryModel = countryModel, phoneNumber = "")
        }
        setPhoneNumberPattern()
    }

    val getPhoneNumberPattern: LiveData<String>
        get() = _phoneNumberPattern

    private fun setPhoneNumberPattern() {
        _phoneNumberPattern.value = phoneNumberState.value.countryModel?.pattern
    }

    fun updatePhoneNumber() {
        _phoneNumberState.update {
            it.copy(phoneNumber = phoneNumber.get()!!)
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

    fun updateConfirmPassword() {
        _uiState.update {
            it.copy(confirmPassword = confirmPassword.get() ?: "")
        }
    }


    fun hitRegister() {
        viewModelScope.launch {
            if (phoneNumberState.value.phoneNumber.replace(" ", "").isEmpty()) {
                _uiState.update {
                    it.copy(
                        isLoading = false, error = Error("Invalid", "Phone number cannot be empty")
                    )
                }
                return@launch
            }

            if (password.get().isNullOrEmpty()) {
                _uiState.update {
                    it.copy(
                        isLoading = false, error = Error("Invalid", "Password cannot be empty")
                    )
                }
                return@launch
            }

            if (confirmPassword.get().isNullOrEmpty()) {
                _uiState.update {
                    it.copy(
                        isLoading = false, error = Error("Invalid", "Confirm password cannot be empty")
                    )
                }
                return@launch
            }


            if (confirmPassword.get() != password.get()) {
                _uiState.update {
                    it.copy(
                        isLoading = false, error = Error("Invalid", "Password and confirm password should be same")
                    )
                }
                return@launch
            }

            _uiState.update {
                it.copy(isLoading = true)
            }
            val model = DefaultRequestModel()
            model.url = UrlName.REGISTER
            if (path != null) {
                val builder = MultipartBody.Builder()
                builder.setType(MultipartBody.FORM)
                val fileCoverImage = File(path)
                builder.addFormDataPart(
                    "profile_image",
                    fileCoverImage.name,
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), fileCoverImage)
                )
                builder.addFormDataPart("name", firstName.get() ?: "")
                builder.addFormDataPart("email", email.get() ?: "")
                builder.addFormDataPart("password", password.get() ?: "")
                builder.addFormDataPart(
                    "mobile",
                    phoneNumberState.value.countryModel?.dialCode + phoneNumberState.value.phoneNumber.replace(
                        " ", ""
                    )
                )
                model.multipartBody = builder.build()
            } else {
                val body = JsonObject()
                body.addProperty("name", firstName.get())
                body.addProperty("email", email.get())
                body.addProperty("password", password.get())
                body.addProperty(
                    "mobile",
                    phoneNumberState.value.countryModel?.dialCode + phoneNumberState.value.phoneNumber.replace(
                        " ", ""
                    )
                )
                model.body = body
            }
            ApiRepository().post(
                model, LoginWithEmailResponseMainData::class.java
            ).onSuccess { onSuccessData ->
                _uiState.update {
                    it.copy(registerResponseData = onSuccessData.data, isRegisterSuccess = true)
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
            }.onError { error ->
                _uiState.update { it ->
                    it.copy(
                        isLoading = false, error = Error(
                            title = "Error", message = error.localizedMessage ?: ""
                        )
                    )
                }
            }
        }
    }

//    fun hitRegister() {
//
//        viewModelScope.launch {
//            if (phoneNumberState.value.phoneNumber.replace(" ", "").isEmpty()) {
//                _uiState.update {
//                    it.copy(
//                        isLoading = false, error = Error("Invalid", "Phone number cannot be empty")
//                    )
//                }
//                return@launch
//            }
//            _uiState.update {
//                it.copy(isLoading = true)
//            }
//            val model = DefaultRequestModel()
//            model.url = UrlName.REGISTER
//            model.body.addProperty("name", uiState.value.firstName)
////            model.body.addProperty("last_name", uiState.value.lastName)
//            model.body.addProperty(
//                "mobile",
//                phoneNumberState.value.countryModel?.dialCode + phoneNumberState.value.phoneNumber.replace(
//                    " ", ""
//                )
//            )
//            model.body.addProperty("email", uiState.value.email)
//            model.body.addProperty("password", uiState.value.password)
//
//            ApiRepository().post(
//                model, LoginWithEmailResponseMainData::class.java
//            ).onSuccess { onSuccessData ->
//                _uiState.update {
//                    it.copy(registerResponseData = onSuccessData.data, isRegisterSuccess = true)
//                }
//                onSuccessData.data?.firebaseAuthToken?.let { firebaseLogin(it) }
//            }.onFailure { onFailedData ->
//                _uiState.update {
//                    it.copy(
//                        isLoading = false, error = Error(
//                            title = onFailedData.title ?: "", message = onFailedData.message ?: ""
//                        )
//                    )
//                }
//            }.onError { error ->
//                _uiState.update { it ->
//                    it.copy(
//                        isLoading = false, error = Error(
//                            title = "Error", message = error.localizedMessage ?: ""
//                        )
//                    )
//                }
//            }
//        }
//
//    }


    private fun firebaseLogin(token: String) {
        viewModelScope.launch {
            FirebaseAuthRepository().signInWithCustomToken<MainResponseData<Any?>>(token)
                .onSuccess { onSuccessData ->
                    FirebaseAuth.getInstance().currentUser?.let { it1 -> hitLogin(it1) }
                    _uiState.update { it.copy(isFirebaseLoginSuccess = true) }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false, error = Error(
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
            body.addProperty("mobile", user.phoneNumber)
            body.addProperty("device_token", CommonUtils.getPrefFirebaseToken())
            val request = DefaultRequestModel()
            request.url = UrlName.LOGIN
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
            }.onError { error ->
                _uiState.update { it ->
                    it.copy(
                        isLoading = false, error = Error(
                            title = "Error", message = error.localizedMessage ?: ""
                        )
                    )
                }
            }

        }

    }
}