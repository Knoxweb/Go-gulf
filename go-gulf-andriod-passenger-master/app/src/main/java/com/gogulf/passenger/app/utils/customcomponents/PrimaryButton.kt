package com.gogulf.passenger.app.utils.customcomponents

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.gogulf.passenger.app.R

class PrimaryButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var textView: TextView
    private val primaryBackground: View
    private var textViewExtraInfo: TextView


    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.primary_button, this, true)
        textView = findViewById(R.id.text)
        primaryBackground = findViewById(R.id.primaryBackground)
        textViewExtraInfo = findViewById(R.id.textViewExtraInfo)

        attrs?.let { setAttributes(context, it) }
    }


    fun setLabel(label: String?) {
        setText(label!!)
    }

    fun extraInfo(text: String? = null) {
        if (text == null) {
            textViewExtraInfo.visibility = View.GONE
            return
        }
        this.textViewExtraInfo.visibility = View.VISIBLE
//        textViewExtraInfo.text = text
    }



    fun setText(text: String) {
        textView.text = text
    }


    private fun setAttributes(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PrimaryButton)
        val labelText = typedArray.getString(R.styleable.PrimaryButton_buttonLabel)
        val primaryBackgroundRes =
            typedArray.getResourceId(R.styleable.PrimaryButton_primaryBackground, 0)
        val labelColor = typedArray.getResourceId(R.styleable.PrimaryButton_labelColor, 0)
        val extraInfo = typedArray.getString(R.styleable.PrimaryButton_extraInfo)

        typedArray.recycle()



        if (primaryBackgroundRes != 0) {
            primaryBackground.setBackgroundResource(primaryBackgroundRes)
            this.textView.setTextColor(resources.getColor(R.color.gsBackground))
        }

        if (labelText != null) {
            this.textView.text = labelText
        }

        if (labelColor != 0) {
            this.textView.setTextColor(resources.getColor(labelColor))
        }

        if (extraInfo != null) {
            this.textViewExtraInfo.visibility = View.VISIBLE
            this.textViewExtraInfo.text = extraInfo
        } else {
            this.textViewExtraInfo.visibility = View.GONE
        }

    }

    fun getLabel(): String {
        return textView.text.toString()
    }


}