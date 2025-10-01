package com.gogulf.passenger.app.ui.auth.login

import com.google.firebase.auth.PhoneAuthCredential
import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.data.model.OtpResponseModel

data class LoginUIState(
    val countryModel: CountryModel? = getCountryByCode(getCountryCode()),
    val phoneNumber: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: Error? = null,
    val otpResponseModel: OtpResponseModel? = null,
    val isVerified: Boolean = false,
    val phoneAuthCredential: PhoneAuthCredential? = null,
)
