package com.gogulf.passenger.app.utils.customcomponents

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.gogulf.passenger.app.R

class SecondaryButton  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var textView: TextView

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.secondary_button, this, true)
        textView = findViewById(R.id.text)
        attrs?.let { setAttributes(context, it) }
    }


    fun setLabel(label: String?) {
        setText(label!!)
    }


    fun setText(text: String) {
        textView.text = text
    }


    private fun setAttributes(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SecondaryButton)
        val labelText = typedArray.getString(R.styleable.SecondaryButton_secondaryButtonLabel)

        typedArray.recycle()

        if (labelText != null) {
            this.textView.text = labelText
        }


    }

    fun getLabel(): String {
        return textView.text.toString()
    }


}