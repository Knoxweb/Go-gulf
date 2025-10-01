package com.gogulf.passenger.app.utils.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.ui.currentride.CurrentRideActivity
import com.gogulf.passenger.app.ui.splash.SplashActivity
import com.gogulf.passenger.app.utils.objects.DebugMode
import com.gogulf.passenger.app.utils.objects.IntentConstant


// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

fun NotificationManager.sendNotification(
    title: String,
    messageBody: String,
    type: String,
    bookingId: String,
    applicationContext: Context
) {

    DebugMode.e(
        "NotificationManager",
        "title-> $title , messageBody-> $messageBody , type -> $type", "NotificationManager"
    )

    // TODO: Step 1.11 create intent
    /*
    val contentIntent = Intent(applicationContext, SplashActivity::class.java)
       contentIntent.putExtra(IntentConstant.CLASSES, type)*/

    val contentIntent =
        when (type) {
            "Unknown" -> {
                Intent(applicationContext, CurrentRideActivity::class.java)
            }
            else -> {
                val intent = Intent(applicationContext, SplashActivity::class.java)
                /* val intent = Intent(applicationContext, NotificationBroadcastReceiver::class.java)*/
                /*  val bundle = Bundle()
                  bundle.putString(IntentConstant.CLASSES, type)
                  intent.putExtra(IntentConstant.BUNDLE, bundle)
                  intent.action = IntentConstant.BUNDLE*/
                intent.putExtra(IntentConstant.TYPE, type)
                intent.putExtra(IntentConstant.BOOKING_ID, bookingId)
                intent
            }
        }
//    val contentIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.journaldev.com/"))
//    val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

    // TODO: Step 1.12 create PendingIntent
    val contentPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    } else {
        PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


//    "this is [\n$messageBody \n\n$type ] ends here"

    // TODO: Step 1.2 get an instance of NotificationCompat.Builder
    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        // TODO: Step 1.8 use the new 'breakfast' notification channel
        applicationContext.getString(R.string.notification_channel_id)
    )
        // TODO: Step 1.3 set title, text and icon to builder
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(title)
        .setContentText(messageBody)
        // TODO: Step 1.13 set content intent
        .setContentIntent(contentPendingIntent)
        // TODO: Step 2.1 add style to builder
        // TODO: Step 2.3 add snooze action
        /* .addAction(
             R.mipmap.ic_app_icon_round,
             applicationContext.getString(R.string.app_name),
             snoozePendingIntent
         )*/
        // TODO: Step 2.5 set priority
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)

    // TODO Step 1.4 call notify
    // Deliver the notification
    notify(NOTIFICATION_ID, builder.build())
/*
    try {
        val builder = AlertDialog.Builder(applicationContext)
        builder.setTitle(title)
        builder.setMessage(messageBody)
        builder.setPositiveButton(
            "OK"
        ) { dialog, _ -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()



    } catch (e: Exception) {
        DebugMode.e("FirebaseMessaging", e.message!!)
    }*/
}

// TODO: Step 1.14 Cancel all notifications
/**
 * Cancels all notifications.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}

