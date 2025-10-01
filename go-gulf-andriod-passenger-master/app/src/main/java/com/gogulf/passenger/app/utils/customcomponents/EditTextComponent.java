package com.gogulf.passenger.app.utils.customcomponents;


import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.gogulf.passenger.app.R;
import com.gogulf.passenger.app.utils.enums.DefaultInputType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditTextComponent extends FrameLayout {

    CardView cardView;
    EditText editText;
    TextView hintView;
    ConstraintLayout mainContainer;
    ConstraintLayout inputContainer;
    TextView errorText;
    String label;
    boolean required = false;

    public EditTextComponent(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public EditTextComponent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditTextComponent(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public EditTextComponent(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_edittext_component, this, true);
        editText = findViewById(R.id.textField);
        hintView = findViewById(R.id.hintTextView);
        errorText = findViewById(R.id.errorText);
        mainContainer = findViewById(R.id.mainContainer);
        inputContainer = findViewById(R.id.inputContainer);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextComponent, 0, 0);
        try {
            label = a.getString(R.styleable.EditTextComponent_hint);
        } finally {
            a.recycle();
        }

        hintView.setText(label);
        editText.addTextChangedListener(watcher);
//        editText.setHint(label);
    }

    public String getText() {
        return editText.getText().toString().trim();
    }

    public void setHints(String hint) {
        if (hint == null) return;
    /*    if (required)
            editText.setHint(hint + "*");
        else
            editText.setHint(hint + " (optional)");*/
        hintView.setText(hint);
    }

    public void setHintsEdit(String hint) {
        if (hint == null) return;
        editText.setHint(hint);
    }

    public void setVisible(int visible) {
        mainContainer.setVisibility(visible);
    }

    public void setHintVisible(int visible) {
        hintView.setVisibility(visible);
    }


    public void setText(String text) {
        editText.setText(text);
        editText.setSelection(getText().length());
    }

    public void setErrorText(@Nullable String text) {
        editText.requestFocus();
        if (text == null) {
            errorText.setVisibility(GONE);
        } else
            errorText.setVisibility(VISIBLE);
        errorText.setText(text);
//        editText.setError(text);
    }

    public void onClicked(View.OnClickListener onClickListener) {
        editText.setOnClickListener(onClickListener);
    }

    public void checkWatcher(TextWatcher watcher) {
        editText.addTextChangedListener(watcher);
    }

    public void isRequired(boolean isReq) {
        required = isReq;
    }

    public void setMaxLength(int maxLength) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    public boolean isEmpty() {
        return editText.getText() == null || TextUtils.isEmpty(getText());
    }


    public void setInputType(int type) {
//        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_TEXT_FLAG_MULTI_LINE
        editText.setInputType(type);
    }

    public void setFocus(boolean focus) {
        editText.setFocusable(focus);
    }


    public void requestFocuses() {
        editText.requestFocus();
    }

    public void setEnabled(boolean enabled) {editText.setEnabled(enabled);}

    public void setActivated(boolean enabled) {editText.setEnabled(enabled);}

    public boolean isValid(String field) {
        if (editText.getText() == null || TextUtils.isEmpty(getText())) {
            setErrorText(field + " is required");
            requestFocuses();
        } else {
            setErrorText(null);
        }
        return editText.getText() == null || TextUtils.isEmpty(getText());
    }


    public void setInputType(DefaultInputType inputType) {
        switch (inputType) {
            case Email: {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            }
            case FullName: {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                break;
            }
            case Number: {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            }
            case PassNumber: {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                break;
            }
            case Password: {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            }
            case CapSentence: {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                break;
            }
            default: {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                break;
            }
        }
    }

    public boolean isValidEmail() {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(getText());

        return matcher.matches();
    }

    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            editText.setError(null);
            errorText.setVisibility(GONE);
        }
    };

    public boolean isValidName() {
        String nameReg = "^[a-zA-Z ,.'-]+$";
        Pattern pattern = Pattern.compile(nameReg, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(getText());
        return matcher.matches();
    }

    public int selectionStart() {
        return editText.getSelectionStart();
    }

    public void setSelection(int selection) {
        editText.setSelection(selection);
    }

    public void creditCardWatcher() {
        editText.addTextChangedListener(new TextWatcher() {

            private static final int TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
            private static final int TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
            private static final int DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
            private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
            private static final char DIVIDER = '-';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // noop
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // noop
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    s.replace(0, s.length(), buildCorrectString(getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));
                }
            }

            private boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
                boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
                for (int i = 0; i < s.length(); i++) { // check that every element is right
                    if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect &= divider == s.charAt(i);
                    } else {
                        isCorrect &= Character.isDigit(s.charAt(i));
                    }
                }
                return isCorrect;
            }

            private String buildCorrectString(char[] digits, int dividerPosition, char divider) {
                final StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < digits.length; i++) {
                    if (digits[i] != 0) {
                        formatted.append(digits[i]);
                        if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                            formatted.append(divider);
                        }
                    }
                }

                return formatted.toString();
            }

            private char[] getDigitArray(final Editable s, final int size) {
                char[] digits = new char[size];
                int index = 0;
                for (int i = 0; i < s.length() && index < size; i++) {
                    char current = s.charAt(i);
                    if (Character.isDigit(current)) {
                        digits[index] = current;
                        index++;
                    }
                }
                return digits;
            }
        });
    }


}