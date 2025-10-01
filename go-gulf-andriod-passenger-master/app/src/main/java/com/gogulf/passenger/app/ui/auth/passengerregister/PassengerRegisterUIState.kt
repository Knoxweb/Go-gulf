package com.gogulf.passenger.app.ui.auth.passengerregister

import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.data.model.auth.LoginWithEmailResponseData
import com.gogulf.passenger.app.data.model.auth.PassengerLoginResponseData

data class PassengerRegisterUIState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val error: Error? = null,
    val isLoading: Boolean = false,
    val isRegisterSuccess: Boolean = false,
    val isFirebaseLoginSuccess: Boolean = false,
    val isNumberLoginSuccess: Boolean = false,
    val registerResponseData: LoginWithEmailResponseData? = null,
    val passengerLoginResponseData: PassengerLoginResponseData? = null

)
