package com.gogulf.passenger.app.utils.objects

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog


object PermissionCheckApp {

    private val TAG = "PermissionCheckApp"
    fun isNotificationPermissionGranted(context: Context) {
        // Notification channels were introduced in API level 26
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        if (!notificationManager.areNotificationsEnabled()) {
            CustomAlertDialog(context)
                .setTitle(InformationString.NotificationAllowTitle)
                .setMessage(InformationString.NotificationAllowMessage)
                .setPositiveText("OK") { dialog, _ ->
                    dialog.dismiss()
                    gotoNotification(context)
                }
                .setCancellable(false)
                .show()

        }


        /*   else {
               // For devices running API level 25 or lower, we need to check the app's permission
               val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
               val appInfo = context.applicationInfo
               val pkg = context.applicationContext.packageName
               val uid = appInfo.uid
               val appOpsMode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_NOTIFICATIONS, uid, pkg)
               return appOpsMode == AppOpsManager.MODE_ALLOWED || appOpsMode == AppOpsManager.MODE_DEFAULT
           }*/
    }


    fun requestNotificationPermission(context: Context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        if (!notificationManager.isNotificationPolicyAccessGranted) {
            // Ask the user to grant permission to access notification policy
            /* val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
             context.startActivity(intent)*/
            CustomAlertDialog(context).setTitle(InformationString.NotificationAllowTitle)
                .setMessage(InformationString.NotificationAllowMessage)
                .setCancellable(false)
                .setPositiveText("OK") { dialog, _ ->
                    dialog.dismiss()
                    gotoNotification(context)
                }
                .setNegativeText("LATER") { dialog, _ -> dialog.dismiss() }
                .show()
        } else {
            // Permission already granted
            // You can now create and show notifications
        }
        /*  } else {
              // Notification channels were introduced in API level 26
              // Permission is granted by default on lower API levels
              // You can now create and show notifications
          }*/
    }

    fun requestNotificationPermissionAlert(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

    }


    fun gotoNotification(context: Context) {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        /*    val uri: Uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            // This will take the user to a page where they have to click twice to drill down to grant the permission
            context.startActivity(intent)*/
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        context.startActivity(intent)
    }

    fun checkPermissionMedia(applicationContext: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            val result = ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            )
            val result1 =
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            DebugMode.e(TAG,"$result $result1 == is value granted ? -> ${PackageManager.PERMISSION_GRANTED} " , "Media Permission Check")

            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        } else {
            val result = ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            )
            DebugMode.e(TAG,"$result  == is value granted ? -> ${PackageManager.PERMISSION_GRANTED} " , "Media Permission Check")

            return result == PackageManager.PERMISSION_GRANTED
        }
    }

    const val MEDIA_PERMISSION_REQUEST_CODE = 99
    fun requestPermissionCamera(applicationContext: Activity) {
        requestPermissions(
            applicationContext,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            MEDIA_PERMISSION_REQUEST_CODE
        )
    }

    fun checkMedias(context: Activity) {
        if (!checkPermissionMedia(context)) {
            requestPermissionCamera(context)
        }
    }
}