package com.gogulf.passenger.app.data.model

import com.google.firebase.auth.PhoneAuthProvider
import com.gogulf.passenger.app.data.model.auth.LoginWithEmailResponseData
import com.gogulf.passenger.app.data.model.auth.PassengerLoginResponseData
import com.gogulf.passenger.app.data.model.auths.Authentications
import com.gogulf.passenger.app.data.model.auths.RegisterModel
import com.gogulf.passenger.app.data.model.dashboards.DashboardModel


data class MainResponseData<T>(
    val title: String?, val message: String?, val data: T? = null
)

data class OtpResponseModel(

    val verificationId: String?, val resendingToken: PhoneAuthProvider.ForceResendingToken?

)

data class MainWithAuthentications(
    val title: String? = null,
    val message: String? = null,
    val mode: String? = null,
    val data: Authentications? = null

)

data class MainApiResponseData(
    val title: String?, val message: String?
)


data class MainWithRegisterModel(
    val title: String? = null,
    val message: String? = null,
    val mode: String? = null,
    val data: RegisterModel? = null
)

data class MainWithDashboardModel(
    val title: String? = null,
    val message: String? = null,
    val mode: String? = null,
    val data: DashboardModel? = null
)
data class LoginWithEmailResponseMainData(
    val message: String?, val title: String?, val data: LoginWithEmailResponseData? = null
)

data class PassengerLoginResponseMainData(
    val message: String?, val title: String?, val data: PassengerLoginResponseData? = null
)

data class MessageResponseMainData(
    val message: String?, val title: String?, val data: MessageResponseData? = null
)

data class MessageResponseData(
    val status: Boolean?, val message: String?
)

data class ConfirmBookingSearchDriverResponseMainData(
    val title: String?, val message: String?, val data: ConfirmBookingSearchDriverResponseData? = null
)

