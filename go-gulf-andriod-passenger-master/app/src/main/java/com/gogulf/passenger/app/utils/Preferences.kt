package com.gogulf.passenger.app.utils

import android.content.Context
import android.content.SharedPreferences

class Preferences {

    companion object {

        private const val tag = "CSD Mobile App"

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(tag, Context.MODE_PRIVATE)
        }

        fun getPreferenceBoolean(context: Context, key: String): Boolean {
            val prefs: SharedPreferences = getSharedPreferences(context)
            return prefs.getBoolean(key, false)
        }

        fun setPreference(context: Context, key: String, value: Boolean) {
            val settings: SharedPreferences = getSharedPreferences(context)
            val editor = settings.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }

        fun setPreference(context: Context, key: String, value: String) {
            val settings: SharedPreferences = getSharedPreferences(context)
            val editor = settings.edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun getPreference(context: Context, key: String): String {
            val prefs: SharedPreferences = getSharedPreferences(context)
            return prefs.getString(key, "")!!
        }

        fun removePreference(context: Context?, key: String?) {
            val settings = getSharedPreferences(
                context!!
            )
            val editor = settings.edit()
            editor.remove(key)
            editor.apply()
        }

        fun clearPreference(context: Context?) {
            val settings = getSharedPreferences(
                context!!
            )
            val editor = settings.edit()
            editor.clear()
            editor.apply()
        }

    }

}