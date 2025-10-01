package com.gogulf.passenger.app.ui.addextras


import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.databinding.LayoutCapacityTextValueBinding
import com.gogulf.passenger.app.databinding.LayoutPopupSpinnerBinding


//class CapacityLayout(private val bind: LayoutCapacityTextValueBinding) {
//    private var currentValue = 0
//    fun setQuantity(context: Context, maxValue: Int, minValue: Int) {
//        bind.passengerValue.text = minValue.toString()
//        val num = ArrayList<String>()
//        if (minValue == 0) {
//            for (i in 0..maxValue) {
//                num.add(i.toString())
//            }
//        } else {
//            for (i in 0 until maxValue) {
//                val count = i + 1
//                num.add(count.toString())
//            }
//        }
//
//
//        bind.capacityDataLayout.setOnClickListener {
//
//            val popupView = LayoutPopupSpinnerBinding.inflate(
//                (context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater),
//                null
//            )
//            /* val popupView =
//            (context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
//            R.layout.layout_popup_spinner,
//            null
//            )*/
//            val popupWindow = PopupWindow(
//                popupView.root,
//                400,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//
//            var isValueChanged = false
//            popupView.spinnerLayout.textColor = ContextCompat.getColor(context, R.color.white)
//            popupView.spinnerLayout.minValue = 0
//            popupView.spinnerLayout.maxValue = num.size - 1
//            popupView.spinnerLayout.displayedValues = num.toTypedArray()
//            popupView.spinnerLayout.wrapSelectorWheel = false
//            popupView.spinnerLayout.value = currentValue
//            popupView.spinnerLayout.setOnValueChangedListener { _, _, newVal ->
//                bind.passengerValue.text = num[newVal]
//                isValueChanged = true
//                currentValue = newVal
//            }
//            popupView.spinnerLayout.setOnClickListener {
//
//            }
//
//            popupView.spinnerLayout.setOnScrollListener { _, p1 ->
//                if (isValueChanged && p1 == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
//                    Handler().postDelayed({
//                        popupWindow.dismiss()
//                    }, 1000)
//                }
//            }
//            popupWindow.isOutsideTouchable = true
//            popupWindow.setOnDismissListener{
//
//            }
//            popupWindow.showAsDropDown(bind.passengerValue, 50, -30)
//
//        }
//    }
//
//    fun getValue(): Int = bind.passengerValue.text.toString().toInt()
//
//
//}


class CapacityLayout(private val bind: LayoutCapacityTextValueBinding) {
    private var currentValue = 0

    fun setQuantity(context: Context, maxValue: Int, minValue: Int) {
        bind.passengerValue.text = minValue.toString()
        val num = ArrayList<String>()
        for (i in minValue..maxValue) {
            num.add(i.toString())
        }

        bind.capacityDataLayout.setOnClickListener {

            val popupView = LayoutPopupSpinnerBinding.inflate(
                (context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater),
                null
            )
            val popupWindow = PopupWindow(
                popupView.root,
                400,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            // Dynamically create a RadioGroup
            val radioGroup = RadioGroup(context).apply {
                orientation = RadioGroup.VERTICAL
            }

            num.forEachIndexed { index, value ->
                val radioButton = RadioButton(context).apply {
                    text = value
                    id = View.generateViewId()
                    isChecked = index == currentValue

                    // Set text color to white
                    setTextColor(ContextCompat.getColor(context, R.color.black))

                    // Make the RadioButton fill the width
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    // Align text to the center
                    gravity = Gravity.CENTER_VERTICAL
                    buttonTintList = ContextCompat.getColorStateList(context, R.color.primaryColor)
                    setCompoundDrawablePadding(16)

                }
                radioGroup.addView(radioButton)
            }

            // Find the container in popupView to add RadioGroup
            val radioGroupContainer = popupView.root.findViewById<LinearLayout>(R.id.radioGroupContainer)
            radioGroupContainer.removeAllViews() // Clear existing views
            radioGroupContainer.addView(radioGroup) // Add RadioGroup

            // Set listener for RadioGroup
            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                val selectedRadioButton = radioGroup.findViewById<RadioButton>(checkedId)
                bind.passengerValue.text = selectedRadioButton.text
                currentValue = num.indexOf(selectedRadioButton.text.toString())
                popupWindow.dismiss()
            }

            popupWindow.isOutsideTouchable = true
            popupWindow.showAsDropDown(bind.passengerValue, 50, -30)
        }
    }

    fun getValue(): Int = bind.passengerValue.text.toString().toInt()
}


