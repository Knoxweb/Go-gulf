package com.gogulf.passenger.app.ui.auth.forgotpassword

import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.data.model.MessageResponseData

data class ForgotPasswordUIState(
    val email: String = "",
    val isLoading: Boolean = false,
    val error: Error? = null,
    val isSuccess: Boolean = false,
    val messageResponseData: MessageResponseData? = null


)