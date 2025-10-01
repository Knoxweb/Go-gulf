package com.gogulf.passenger.app.utils.notifications

import android.util.Log
import com.gogulf.passenger.app.utils.objects.DebugMode
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.gogulf.passenger.app.App
import com.gogulf.passenger.app.utils.PrefEntity.FIREBASE_TOKEN
import com.gogulf.passenger.app.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")
//
//        var type = ""
//        var bookingid = ""
//        var title = ""
//        var body = ""

        // TODO Step 3.5 check messages for data
        // Check if message contains a data payload.
        remoteMessage.data.let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            try {
                val hashMap = remoteMessage.data
                val appNotification = AppNotification()
                appNotification.type = hashMap["type"]
                appNotification.title = hashMap["title"]
                appNotification.body = hashMap["body"]
                appNotification.target = hashMap["target"]
                sendNotification(appNotification)

//                type = remoteMessage.data["type"].toString()
//                bookingid = remoteMessage.data["booking_id"].toString()
//                title = remoteMessage.data["title"].toString()
//                body = remoteMessage.data["body"].toString()
//                sendNotification(title, body, type, bookingid)


            } catch (e: Exception) {
                e.printStackTrace()
                DebugMode.e(TAG, "catch error ${e.message!!}")
            }
        }

    }

    private fun sendNotification(appNotification: AppNotification?) {
        val notifyPendingIntent = AppNotificationUtils.filterNotification(this, appNotification!!)
        AppNotificationUtils.showNotification(this, appNotification, notifyPendingIntent)


    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
        Preferences.setPreference(App.baseApplication, FIREBASE_TOKEN, token)

    }

//    private fun sendNotification(
//        title: String,
//        messageBody: String,
//        type: String,
//        bookingid: String = ""
//    ) {
//        val notificationManager = ContextCompat.getSystemService(
//            applicationContext,
//            NotificationManager::class.java
//        ) as NotificationManager
//        notificationManager.sendNotification(
//            title,
//            messageBody,
//            type,
//            bookingid,
//            applicationContext
//        )
//
//    }

    private fun sendRegistrationToServer(token: String?) {
        if (token != null) {
            Preferences.setPreference(
                App.baseApplication, FIREBASE_TOKEN, token
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}