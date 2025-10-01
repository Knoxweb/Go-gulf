package com.gogulf.passenger.app.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.crashlytics
import com.google.gson.Gson
import com.scottyab.rootbeer.RootBeer
import com.gogulf.passenger.app.App
import com.gogulf.passenger.app.ui.auth.login.getCountryCode

object SlyykDeviceInfo {

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String? {
        return Settings.Secure.getString(
            context.contentResolver, Settings.Secure.ANDROID_ID
        )
    }

    fun getModel(): String? {
        return Build.MODEL
    }

    fun getId(): String? {
        return Build.ID
    }

    fun getSDK(): Int? {
        return Build.VERSION.SDK_INT
    }

    fun getManufacturer(): String? {
        return Build.MANUFACTURER
    }

    fun getBrand(): String? {
        return Build.BRAND
    }

    fun getUser(): String? {
        return Build.USER
    }

    fun getType(): String? {
        return Build.TYPE
    }

    fun getTag(): String? {
        return Build.TAGS
    }

    fun getBase(): Int? {
        return Build.VERSION_CODES.BASE
    }

    fun getIncremental(): String? {
        return Build.VERSION.INCREMENTAL
    }

    fun getBoard(): String? {
        return Build.BOARD
    }

    fun getHost(): String? {
        return Build.HOST
    }

    fun getFingerPrint(): String? {
        return Build.FINGERPRINT
    }

    fun getVersionCode(): String? {
        return Build.VERSION.RELEASE
    }

    fun getAppVersionName(context: Context): String? {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: "N/A"
        } catch (e: PackageManager.NameNotFoundException) {
            "N/A"
        }
    }

    fun getAppVersionCode(context: Context): Long? {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.longVersionCode
        } catch (e: PackageManager.NameNotFoundException) {
            0L
        }
    }

    fun getInstallerPackageName(context: Context): String? {
        val packageManager = context.packageManager
        return try {
            val installerPackageName = packageManager.getInstallerPackageName(context.packageName)
            installerPackageName.toString()
        } catch (e: IllegalArgumentException) {
            ""
        }
    }

    fun isDeviceRooted(context: Context): String? {
        return try {
            val rootBeer = RootBeer(context)
            if (rootBeer.isRooted) {
                "1" // Return "1" if the device is rooted
            } else {
                "0" // Return "0" if the device is not rooted
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }



}


data class UserInfo(
    val id: String? = getCurrentFirebaseUser()?.uid,
)

fun getCurrentFirebaseUser(): FirebaseUser? {
    val firebaseAuth = FirebaseAuth.getInstance()
    return firebaseAuth.currentUser
}
data class DeviceDetails(
    val i: String? = SlyykDeviceInfo.getDeviceId(App.baseApplication),
//    val model: String? = SlyykDeviceInfo.getModel(),
//    val id: String? = SlyykDeviceInfo.getId(),
//    val sdk: Int? = SlyykDeviceInfo.getSDK(),
//    val manu: String? = SlyykDeviceInfo.getManufacturer(),
//    val brand: String? = SlyykDeviceInfo.getBrand(),
    val d: String? = "${SlyykDeviceInfo.getManufacturer()} ${SlyykDeviceInfo.getBrand()} ${SlyykDeviceInfo.getModel()}",
//    val user: String? = SlyykDeviceInfo.getUser(),
//    val type: String? = SlyykDeviceInfo.getType(),
//    val base: Int? = SlyykDeviceInfo.getBase(),
//    val incremental: String? = SlyykDeviceInfo.getIncremental(),
//    val board: String? = SlyykDeviceInfo.getBoard(),
//    val host: String? = SlyykDeviceInfo.getHost(),
//    val fP: String? = SlyykDeviceInfo.getFingerPrint(),
    val f: String? = "${SlyykDeviceInfo.getType()}/${SlyykDeviceInfo.getTag()}",
    val vs: String? = "${SlyykDeviceInfo.getVersionCode()}/${SlyykDeviceInfo.getSDK()}",
    val v: String? = SlyykDeviceInfo.getAppVersionName(App.baseApplication),
    val a: Long? = SlyykDeviceInfo.getAppVersionCode(App.baseApplication),
//    val p: String? = SlyykDeviceInfo.getInstallerPackageName(App.baseApplication),
//    val r: String? = SlyykDeviceInfo.isDeviceRooted(App.baseApplication),
    val id: String? = getCurrentFirebaseUser()?.uid
)



fun logDeviceToCrashlytics() {
    val crashlytics = Firebase.crashlytics
    crashlytics.setCustomKey("device_id", "${SlyykDeviceInfo.getDeviceId(App.baseApplication)}")
    crashlytics.setCustomKey("device", "${SlyykDeviceInfo.getManufacturer()} ${SlyykDeviceInfo.getBrand()} ${SlyykDeviceInfo.getModel()}")
    crashlytics.setCustomKey("os_version", "${Build.VERSION.RELEASE} (${Build.VERSION.SDK_INT})")
    crashlytics.setCustomKey("app_version", "${SlyykDeviceInfo.getAppVersionName(App.baseApplication)} (${SlyykDeviceInfo.getAppVersionCode(App.baseApplication)})")
    crashlytics.setCustomKey("installer_package", "${SlyykDeviceInfo.getInstallerPackageName(App.baseApplication)}")
    crashlytics.setCustomKey("user_id", "${getCurrentFirebaseUser()?.uid}")
    crashlytics.setCustomKey("fingerprint", "${SlyykDeviceInfo.getFingerPrint()}")
    crashlytics.setCustomKey("country_code", "${getCountryCode()}")
    crashlytics.setCustomKey("is_rooted", "${SlyykDeviceInfo.isDeviceRooted(App.baseApplication)}")
}



private var deviceDetails: DeviceDetails? = null
private var deviceDetailsJson: String? = null

fun getDeviceDetails(): String {
    if (deviceDetails == null) {
        deviceDetails = DeviceDetails()
    }
    if (deviceDetailsJson == null) {
        val gson = Gson()
        deviceDetailsJson = gson.toJson(deviceDetails)
    }
    return deviceDetailsJson!!
}