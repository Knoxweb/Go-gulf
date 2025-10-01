package com.gogulf.passenger.app.ui.auth.registerv2

import androidx.databinding.ObservableField
import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.ui.auth.login.CountryModel
import com.gogulf.passenger.app.ui.auth.login.getCountryByCode
import com.gogulf.passenger.app.ui.auth.login.getCountryCode


data class RegisterUIState (
//    val isLoading: Boolean = false,
//    val isSuccess: Boolean = false,
//    val error: Error? = null,
    val countryModel: CountryModel? = getCountryByCode(getCountryCode()),
    val phoneNumber: String = "",

    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val confirmPassword: ObservableField<String> = ObservableField(""),
    val newPassword: ObservableField<String> = ObservableField(""),
    val error: Error? = null,
    val isUILoading: Boolean = false,
    val isFirstUpdateSuccess: Boolean = false,
    val onLogoutSuccess: Boolean = false


)