package com.gogulf.passenger.app.utils.objects

import android.util.Log
import com.gogulf.passenger.app.BuildConfig

object DebugMode {
    fun e(tag: String, message: String, topic: String = "Failed") {
        if (BuildConfig.DEBUG) {
            Log.e("D $tag", "D $topic -> $message")
        }
    }
}