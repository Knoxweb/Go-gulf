package com.gogulf.passenger.app.utils.customcomponents

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseBindingMethod
import androidx.databinding.InverseBindingMethods
import com.gogulf.passenger.app.R


@InverseBindingMethods(
    InverseBindingMethod(
        type = CustomEditText::class,
        attribute = "mainRohanText",
        event = "mainRohanTextAttrChanged",
        method = "getMainRohanText"
    )
)
class CustomEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val editText: EditText
    private val errorText: TextView
    private val defaultTextChangeListener: TextWatcher
    private var defaultFocusListener: OnFocusChangeListener? = null
    private val passwordImageBtn: ImageView
    private var scrollView: NestedScrollView? = null
    private var onEnterIconClickListener: ((View) -> Unit)? = null

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.edit_text, this, true)
        editText = findViewById(R.id.editText)
        errorText = findViewById(R.id.error_message)
        passwordImageBtn = findViewById(R.id.password_toogle_icon)
        defaultTextChangeListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                setError(null)
            }

        }

        editText.addTextChangedListener(defaultTextChangeListener)
        attrs?.let { setAttributes(context, it) }

    }

    private fun setAttributes(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText)
        val hintText = typedArray.getString(R.styleable.CustomEditText_hintLabel)
        val isGravityCenter = typedArray.getBoolean(R.styleable.CustomEditText_isGravityCenter, false)
        val maxLength = typedArray.getInt(R.styleable.CustomEditText_maxLength, 0)
        val inputType = typedArray.getInt(R.styleable.CustomEditText_inputType, 0)
        val customText = typedArray.getString(R.styleable.CustomEditText_mainRohanText)
        val isMonthYear = typedArray.getBoolean(R.styleable.CustomEditText_isMonthYear, false)
        val isEnterIconVisible =
            typedArray.getBoolean(R.styleable.CustomEditText_isEnterIconVisible, false)

        typedArray.recycle()


        if (isGravityCenter) {
            this.editText.gravity = Gravity.CENTER
        }
        if (maxLength != 0) {
            editText.filters = arrayOf<InputFilter>(LengthFilter(maxLength))
        }
        if (inputType == 0) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
            editText.maxLines = 1
        }
        if (inputType == 1) {
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.maxLines = 1
        }
        if (inputType == 2) {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            editText.maxLines = 1
        }
        if (inputType == 3) {
            passwordImageBtn.visibility = View.VISIBLE
            editText.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
            editText.maxLines = 1
        }
        if (inputType == 4) {
            editText.inputType = InputType.TYPE_CLASS_PHONE
            editText.maxLines = 1
        }
        if (inputType == 5) {
            editText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        if (editText.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            passwordImageBtn.setImageResource(R.drawable.icon_hide_password)
        }

        passwordImageBtn.setOnClickListener {
            if (editText.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                passwordImageBtn.setImageResource(R.drawable.icon_show_password)
                editText.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
                editText.setSelection(editText.text.length)
            } else {
                passwordImageBtn.setImageResource(R.drawable.icon_hide_password)
                editText.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                editText.setSelection(editText.text.length)
            }
        }

        if (hintText != null) {
            this.editText.hint = hintText
        }

        if (customText != null) {
            this.editText.setText(customText)
        }

        if (isMonthYear) {

        }
    }

    @SuppressLint("ObjectAnimatorBinding")
    fun setDefaultFocusListener(view: NestedScrollView) {
        this.scrollView = view
        defaultFocusListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                setSelection(this.editText.text.length)
                ObjectAnimator.ofInt(scrollView, "scrollY", editText.bottom + 1000).setDuration(500)
                    .start()
            }
        }
        this.editText.onFocusChangeListener = defaultFocusListener
    }

    fun addTextChangeListener(watcher: TextWatcher) {
        this.editText.removeTextChangedListener(defaultTextChangeListener)
        this.editText.addTextChangedListener(watcher)
    }

    fun getText(): String {
        return this.editText.text.toString()
    }

    fun setHint(s: String) {
        this.editText.hint = s
    }

    fun setText(string: String) {
        this.editText.setText(string)
    }

    fun setText(text: CharSequence?, type: TextView.BufferType?) {
        this.editText.setText(text, TextView.BufferType.EDITABLE)
    }

    override fun setEnabled(enable: Boolean) {
        this.editText.isEnabled = enable
    }

    override fun setClickable(clickable: Boolean) {
        this.editText.isClickable = clickable
    }

    override fun setOnKeyListener(l: OnKeyListener?) {
        super.setOnKeyListener(l)
        this.editText.setOnKeyListener(l)
    }


//    fun getText(): Editable? {
//        val text: CharSequence = editText.getText() ?: return null
//        // This can only happen during construction.
//        if (text is Editable) {
//            return text
//        }
//        editText.setText(text, TextView.BufferType.EDITABLE)
//        return editText.getText() as Editable?
//    }

    fun setError(error: String?) {
        if (error == null) {
            this.errorText.visibility = View.GONE
            this.errorText.text = ""
            return
        } else {
            this.errorText.visibility = View.VISIBLE
            this.errorText.text = error
        }

    }

//    fun setTextChangeListener() {
//        this.editText.addTextChangedListener {
//            setError(null)
//        }
//    }

    fun makeFocus() {
        this.editText.requestFocus()
    }

    override fun hasFocus(): Boolean {
        return this.editText.hasFocus()
    }

    fun getMainRohanText(): String {
        return this.editText.text.toString()
    }

    fun setOnEnterIconClickListener(listener: (View) -> Unit) {
        this.onEnterIconClickListener = listener
    }

    fun setMainRohanText(value: String) {
        if (this.editText.text.toString() != value) {
            this.editText.setText(value)
        }
    }

    fun setSelection(length: Int) {
        this.editText.setSelection(length)
    }

    fun getEditText(): EditText {
        return this.editText
    }

    companion object {
        @JvmStatic
        @BindingAdapter("mainRohanText")
        fun setMainRohanText(
            view: CustomEditText, value: String?
        ) {
            if (view.getMainRohanText() != value) {
                view.setMainRohanText(value ?: "")
            }
        }

        @JvmStatic
        @BindingAdapter("mainRohanTextAttrChanged")
        fun setMainRohanTextListener(
            view: CustomEditText,
            listener: InverseBindingListener?
        ) {
            if (listener != null) {
                view.addTextChangeListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?, start: Int, count: Int, after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?, start: Int, before: Int, count: Int
                    ) {
                        listener.onChange()
                    }

                    override fun afterTextChanged(s: Editable?) {
                        view.setError(null)
                    }
                })
            }
        }
    }


}