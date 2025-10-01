package com.gogulf.passenger.app.utils.others

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.gogulf.passenger.app.R
import com.google.android.material.snackbar.Snackbar

object CustomToast {
    fun show(
        view: View?,
        context: Context?,
        message: String?,
        type: Boolean = false,
        bottomMargin: Int = 20
    ) {
        val snackbar = Snackbar.make(view!!, message!!, Snackbar.LENGTH_SHORT)
        val viewSnackBar = snackbar.view
        val params = viewSnackBar.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        params.topMargin = bottomMargin
        viewSnackBar.layoutParams = params
        if (type) {
            snackbar.setBackgroundTint(ContextCompat.getColor(context!!, R.color.primaryColor))
        } else {
            snackbar.setBackgroundTint(ContextCompat.getColor(context!!, R.color.cancelColor))
        }
        snackbar.show()
    }

    fun showBottom(
        view: View?,
        context: Context?,
        message: String?,
        type: Boolean = false,
        bottomMargin: Int = 20
    ) {
        val snackbar = Snackbar.make(view!!, message!!, Snackbar.LENGTH_SHORT)
        val viewSnackBar = snackbar.view
        val params = viewSnackBar.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.BOTTOM
        params.bottomMargin = bottomMargin
        viewSnackBar.layoutParams = params
        if (type) {
            snackbar.setBackgroundTint(ContextCompat.getColor(context!!, R.color.primaryColor))
        } else {
            snackbar.setBackgroundTint(ContextCompat.getColor(context!!, R.color.cancelColor))
        }
        snackbar.show()
    }
}