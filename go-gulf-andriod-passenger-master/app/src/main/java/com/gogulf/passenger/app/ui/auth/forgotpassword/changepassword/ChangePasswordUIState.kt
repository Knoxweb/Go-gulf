package com.gogulf.passenger.app.ui.auth.forgotpassword.changepassword

import com.gogulf.passenger.app.data.model.Error


data class ChangePasswordUIState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: Error? = null
)