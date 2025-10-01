package com.gogulf.passenger.app.data.model.auth.firebase

import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken

data class OtpResponseModel(

    val verificationId: String?,
    val resendingToken: ForceResendingToken?

)