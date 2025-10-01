package com.gogulf.passenger.app.utils.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.gogulf.passenger.app.ui.notice.NoticeActivity
import com.gogulf.passenger.app.R
import java.io.File


class AppNotificationUtils {
    companion object {
        fun filterNotification(context: Context, appNotification: AppNotification): PendingIntent {
            return getPendingInent(context, getIntent(context, appNotification))
        }


        fun getIntent(context: Context, appNotification: AppNotification): Intent {
            val intent: Intent = Intent(context, NoticeActivity::class.java)
            intent.putExtra("bookingId", appNotification.target ?: "")
            intent.putExtra("notificationType", appNotification.type?:"")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            return intent
        }


        fun getPendingInent(context: Context, notifyIntent: Intent): PendingIntent {
            notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            return PendingIntent.getActivity(
                context,
                0,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        fun showNotification(
            context: Context,
            appNotification: AppNotification,
            notifyPendingIntent: PendingIntent?
        ) {

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val NOTIFICATION_CHANNEL_ID = "CHANNEL_SLYYK"

            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "SLYYK",
                NotificationManager.IMPORTANCE_HIGH
            )
            // Configure the notification channel.
            notificationChannel.description = "FIELDVISIT"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationManager.createNotificationChannel(notificationChannel)
            val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(ContextCompat.getColor(context, R.color.primaryColor))
                .setTicker(context.resources.getString(R.string.app_name))
//                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(appNotification.title)
                .setContentText(appNotification.body)
                .setContentIntent(notifyPendingIntent)
                .setAutoCancel(true)



            if (!appNotification.smallIconUrl.isNullOrEmpty()) {
                val bitmap = getBitmapIfAvailable(context, appNotification.smallIconUrl!!)
                if (bitmap != null) {
                    notificationBuilder.setLargeIcon(bitmap)
                    notifyNotification(notificationManager, notificationBuilder)
                } else {
                    notifyNotification(notificationManager, notificationBuilder)
                }
            } else {
                notificationBuilder.setSmallIcon(R.drawable.splash_transparent);
                notifyNotification(notificationManager, notificationBuilder)
            }

        }

        private fun getBitmapIfAvailable(context: Context, filePath: String): Bitmap? {
            val file: File? = null
            if (file != null) {
                return BitmapFactory.decodeFile(file.path)
            }
            return null
        }

//        fun showLargeIconNotif(
//            context: Context,
//            appNotification: AppNotification,
//            notificationManager: NotificationManager,
//            notificationBuilder: NotificationCompat.Builder
//        ) {
//            //this will show image at right or left depending on os level...
//            val bitmap = getBitmapIfAvailable(context, appNotification.smallIconUrl!!)
//            if (bitmap != null) {
//                //first check if its avaialable locally...
//                notificationBuilder.setLargeIcon(bitmap)
//                showBigPictureNotif(
//                    context,
//                    appNotification,
//                    notificationManager,
//                    notificationBuilder
//                )
//            }
//        }

//        private fun showBigPictureNotif(
//            context: Context,
//            appNotification: AppNotification,
//            notificationManager: NotificationManager,
//            notificationBuilder: NotificationCompat.Builder
//        ) {
//            if (appNotification.largeIconUrl.isNullOrEmpty()) {
//                notifyNotification(notificationManager, notificationBuilder)
//            } else {
//                val bitmap = getBitmapIfAvailable(context, appNotification.smallIconUrl!!)
//                if (bitmap != null) {
//                    //first check if its avaialable locally...
//                    notificationBuilder.setStyle(
//                        NotificationCompat.BigPictureStyle().bigPicture(bitmap)
//                    )
//                    notifyNotification(notificationManager, notificationBuilder)
//                }
//
//            }
//        }
//

        private fun notifyNotification(
            notificationManager: NotificationManager,
            builder: NotificationCompat.Builder
        ) {
            notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
        }

    }

}