package com.gogulf.passenger.app.ui.auth.otpv2

import androidx.databinding.ObservableField
import com.google.firebase.auth.PhoneAuthCredential
import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.data.model.OtpResponseModel

data class OTPUIState(
    val otp: String = "",
    val otpObservable: ObservableField<String> = ObservableField(""),
    val isUILoading: Boolean = false,
    val isOTPVerified: Boolean = false,
    val errorMessage: Error? = null,
    val phoneNumber: String = "",
    val isLoginSuccess: Boolean = false,
    val otpResponseModel: OtpResponseModel? = null,
    val isSuccess: Boolean = false,
    val isVerified: Boolean = false,
    val phoneAuthCredential: PhoneAuthCredential? = null
)

