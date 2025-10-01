package com.gogulf.passenger.app.utils.customcomponents

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import com.gogulf.passenger.app.R

class CustomLoader(
    private val context: Activity,
    ): Dialog(context) {

    private var layoutView: View

    init {
        val displayRectangle: Rect = Rect()
        window!!.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        window!!.decorView.setBackgroundResource(android.R.color.transparent)

        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutView = inflater.inflate(R.layout.custom_loader, null)
        layoutView.minimumWidth = (displayRectangle.width() * 0.8f).toInt()
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(layoutView)
//        setView(layoutView)
//        setCancelable(false)
        setCanceledOnTouchOutside(false)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        context.finish()
    }




}