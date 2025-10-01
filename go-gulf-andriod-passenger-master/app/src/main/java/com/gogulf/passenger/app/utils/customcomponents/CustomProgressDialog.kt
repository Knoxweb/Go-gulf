package com.gogulf.passenger.app.utils.customcomponents

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.LayoutProgressDialogBinding


class CustomProgressDialog(activity: Context) {
    private val dialog: Dialog
    private val binding: LayoutProgressDialogBinding
    var cancellable = false

    init {
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_progress_dialog, null)
        binding = LayoutProgressDialogBinding.inflate(inflater, null)
        dialog = Dialog(activity)
        dialog.setContentView(binding.getRoot())
        dialog.setCancelable(cancellable)
        dialog.setCanceledOnTouchOutside(cancellable)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun show(): CustomProgressDialog {
        dialog.setCancelable(cancellable)
        dialog.setCanceledOnTouchOutside(cancellable)
        dialog.show()
        return this
    }

    fun setTitle(title: String?): CustomProgressDialog {
        binding.title.setVisibility(View.VISIBLE)
        binding.title.setText(title)
        return this
    }

    fun setMessage(title: String?): CustomProgressDialog {
        binding.message.setVisibility(View.VISIBLE)
        binding.message.setText(title)
        return this
    }

    fun setCancellable(cancellable: Boolean): CustomProgressDialog {
        this.cancellable = cancellable
        return this
    }

    fun dismissDialog(): CustomProgressDialog {
        dialog.dismiss()
        return this
    }
}