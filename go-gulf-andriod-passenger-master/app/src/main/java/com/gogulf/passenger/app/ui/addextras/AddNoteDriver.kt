package com.gogulf.passenger.app.ui.addextras

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.view.LayoutInflater
import com.gogulf.passenger.app.utils.interfaces.NoteInterface
import com.gogulf.passenger.app.databinding.LayoutAddNoteExtrasBinding

class AddNoteDriver(
    activity: Context,
    private var noteInterface: NoteInterface,
    notes: String
) : DialogInterface {
    private var binding: LayoutAddNoteExtrasBinding
    private var dialog: Dialog

    init {
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
        binding = LayoutAddNoteExtrasBinding.inflate(inflater as LayoutInflater, null)
        dialog = Dialog(activity)
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        binding.enterMessage.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        if (notes != "") {
            binding.enterMessage.setText(notes)
            binding.enterMessage.setSelection(binding.enterMessage.text.toString().length)
        }

        binding.doneButton.setOnClickListener {
            noteInterface.onDone(binding.enterMessage.text.toString().trim())
            dismiss()
        }

        binding.close.setOnClickListener {
            cancel()
        }
    }

    override fun cancel() {
        dialog.dismiss()
    }

    override fun dismiss() {
        dialog.dismiss()
    }
}