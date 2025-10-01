package com.gogulf.passenger.app.utils

import com.google.firebase.auth.PhoneAuthProvider
import java.util.Locale

object TOKEN {

    var resendingToken: PhoneAuthProvider.ForceResendingToken? = null

}
fun isLocaleFrench(): Boolean {
    val currentLocale = Locale.getDefault()
    return currentLocale.language == "fr"
}
