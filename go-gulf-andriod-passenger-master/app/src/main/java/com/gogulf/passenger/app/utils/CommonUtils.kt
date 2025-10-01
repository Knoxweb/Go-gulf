package com.gogulf.passenger.app.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.gogulf.passenger.app.App
import com.gogulf.passenger.app.utils.PrefEntity.FIREBASE_TOKEN
import java.io.IOException

object CommonUtils {

    fun encodeImageToBase64(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val imageBytes = inputStream?.readBytes()
            inputStream?.close()
            imageBytes?.let { android.util.Base64.encodeToString(it, android.util.Base64.DEFAULT) }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun getRealPathFromURI(contentURI: Uri, context: Context): String? {
        val cursor = context.contentResolver.query(contentURI, null, null, null, null)
        return if (cursor == null) {
            contentURI.path
        } else {
            cursor.moveToFirst()
            //   int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(idx)
        }
    }

    fun getNewFCMToken(): String {
        var newToken1: String = ""

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            newToken1 = task.result
            Preferences.setPreference(App.baseApplication, FIREBASE_TOKEN, newToken1)
        })
        return newToken1
    }

    fun getPrefFirebaseToken(): String {
        return Preferences.getPreference(App.baseApplication, FIREBASE_TOKEN)
    }

}