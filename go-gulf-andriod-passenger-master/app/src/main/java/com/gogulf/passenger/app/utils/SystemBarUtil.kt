package com.gogulf.passenger.app.utils

import android.animation.LayoutTransition
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.TypedValue
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.gogulf.passenger.app.R

object SystemBarUtil {
    fun enableEdgeToEdge(activity: Activity) {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        val controller = WindowInsetsControllerCompat(activity.window, activity.window.decorView)
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller.isAppearanceLightStatusBars = false
        controller.isAppearanceLightNavigationBars = false
        activity.window.isStatusBarContrastEnforced = false
        activity.window.isNavigationBarContrastEnforced = false
        activity.window.navigationBarDividerColor =
            activity.resources.getColor(R.color.gsBackground)
        activity.window.navigationBarColor = 0x00000000
        activity.window.statusBarColor =  0x00000000

    }


    fun setStatusBarColor(activity: Activity, differentBottom: Boolean = false) {
        val controller = WindowInsetsControllerCompat(activity.window, activity.window.decorView)
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller.isAppearanceLightStatusBars = false
        controller.isAppearanceLightNavigationBars = false


        activity.window.statusBarColor = activity.getColor(R.color.gsBackground)
        activity.window.navigationBarColor = activity.getColor(R.color.gsBackground)


    }

    fun ViewGroup.forceLayoutChanges() {
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    fun enableEdgeToEdge(dialog: Dialog?) {
        dialog?.window?.let { window ->
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val controller = WindowInsetsControllerCompat(window, window.decorView)
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            window.isStatusBarContrastEnforced = false
            window.isNavigationBarContrastEnforced = false
            window.navigationBarColor = 0x00000000
        }
    }


}

fun getExtraBottomPadding(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, 16f, context.resources.displayMetrics
    ).toInt()
}

object StringUtils {
    @JvmStatic
    fun capitalizeWords(input: String?): String {
        return input?.split(" ")?.joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        } ?: ""
    }
}
