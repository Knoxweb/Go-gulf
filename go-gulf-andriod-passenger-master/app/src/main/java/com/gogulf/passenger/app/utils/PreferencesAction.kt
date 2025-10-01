package com.gogulf.passenger.app.utils

import android.content.Context
import com.gogulf.passenger.app.data.model.auth.PassengerLoginResponseData

class PreferencesAction {

    fun setLoginPreferences(context: Context, model: PassengerLoginResponseData) {
        Preferences.setPreference(context, PrefEntity.FIREBASE_REFERENCE, model.firebaseReference?:"")
        Preferences.setPreference(context, PrefEntity.AUTH_TOKEN, model.authToken?:"")
    }

    fun setFirebaseUid(context: Context, firebaseUid: String) {
        Preferences.setPreference(context, PrefEntity.FIREBASE_UID, firebaseUid)
    }


    companion object {
        fun clearAll(context: Context) {
            val firstTime = Preferences.getPreferenceBoolean(
                context, PrefEntity.FIRST_TIME
            )
            Preferences.clearPreference(context)
            Preferences.setPreference(context, PrefEntity.FIRST_TIME, firstTime)
        }
    }

}