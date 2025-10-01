package com.gogulf.passenger.app.utils.customcomponents;


import static com.gogulf.passenger.app.utils.objects.Constants.ERROR_MESSAGE_CODE;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.gogulf.passenger.app.R;
import com.gogulf.passenger.app.utils.interfaces.PinViewActionListener;

public class PinView extends FrameLayout {
    private EditText inputValueOne, inputValueTwo, inputValueThree, inputValueFour, inputValueFifth, inputValueSix;
    private TextView errorText;
    private String valueOne, valueTwo, valueThree, valueFour, valueFive, valueSix;
    int twoCount = 0, threeCount = 0, fourCount = 0, fiveCount = 0, sixCount = 0;

    public PinView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PinView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_otp_edit_text, this, true);
        inputValueOne = findViewById(R.id.inputValueOne);
        inputValueTwo = findViewById(R.id.inputValueTwo);
        inputValueThree = findViewById(R.id.inputValueThree);
        inputValueFour = findViewById(R.id.inputValueFour);
        inputValueFifth = findViewById(R.id.inputValueFifth);
        inputValueSix = findViewById(R.id.inputValueSix);
        errorText = findViewById(R.id.errors);
        init();

    }


    private void init() {

        inputFieldOne();
        inputFieldTwo();
        inputFieldThree();
        inputFieldFour();
        inputFieldFive();
        inputFieldSix();
        inputValueOne.setSelection(inputValueOne.length());
        inputValueTwo.setSelection(inputValueTwo.length());
        inputValueThree.setSelection(inputValueThree.length());
        inputValueFour.setSelection(inputValueFour.length());
        inputValueFifth.setSelection(inputValueFifth.length());
        inputValueSix.setSelection(inputValueSix.length());

    }

    private void inputFieldOne() {
        inputValueOne.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (inputValueOne.getText().toString().length() == 1) {
                    inputValueTwo.requestFocus();
                    valueOne = inputValueOne.getText().toString().trim();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }

        });


        inputValueOne.setOnKeyListener((v, keyCode, event) -> {
            //You can identify which key pressed by checking keyCode value with KeyEvent.KEYCODE_
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && inputValueOne.getText().toString().trim().length() == 1
                    && keyCode != KeyEvent.KEYCODE_DEL
                    && keyCode != KeyEvent.KEYCODE_SPACE) {
                //this is for backspace
                inputValueTwo.requestFocus();

            }
            return false;
        });
    }

    private void inputFieldTwo() {
        inputValueTwo.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before == 0) {
                    if (inputValueTwo.getText().toString().length() == 1) {
                        inputValueThree.requestFocus();
                        valueTwo = inputValueTwo.getText().toString().trim();
                    } else if (inputValueTwo.getText().toString().length() == 0) {
                        inputValueOne.requestFocus();
                    }
                }

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }

        });

        inputValueTwo.setOnKeyListener((v, keyCode, event) -> {
            //You can identify which key pressed by checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                //this is for backspace
                twoCount++;
                if (twoCount >= 2 && inputValueTwo.getText().toString().trim().length() == 0) {
                    inputValueOne.requestFocus();
                    twoCount = 0;
                }
                if (inputValueTwo.getText().toString().trim().length() == 0) {
                    inputValueOne.requestFocus();
                    twoCount = 0;
                }
            }
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && inputValueTwo.getText().toString().trim().length() == 1
                    && keyCode != KeyEvent.KEYCODE_DEL
                    && keyCode != KeyEvent.KEYCODE_SPACE) {
                //this is for backspace
                inputValueThree.requestFocus();

            }
            return false;
        });
        inputValueTwo.setOnFocusChangeListener((view, b) -> {
            if (b) {
                if (inputValueOne.getText().toString().length() == 0) {
                    inputValueOne.requestFocus();
                    showKeyboard();
                }
            }
        });
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

    }

    private void inputFieldThree() {
        inputValueThree.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before == 0) {
                    if (inputValueThree.getText().toString().length() == 1) {
                        inputValueFour.requestFocus();
                        valueThree = inputValueThree.getText().toString().trim();
                    } else if (inputValueThree.getText().toString().length() == 0) {
                        inputValueTwo.requestFocus();
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }

        });
        inputValueThree.setOnKeyListener((v, keyCode, event) -> {
            //You can identify which key pressed by checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                //this is for backspace
                threeCount++;
                if (threeCount >= 2 && inputValueThree.getText().toString().trim().length() == 0) {
                    inputValueTwo.requestFocus();
                    threeCount = 0;
                }
                if (inputValueThree.getText().toString().trim().length() == 0) {
                    inputValueTwo.requestFocus();
                    threeCount = 0;
                }
            }
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && inputValueThree.getText().toString().trim().length() == 1
                    && keyCode != KeyEvent.KEYCODE_DEL
                    && keyCode != KeyEvent.KEYCODE_SPACE) {
                //this is for backspace
                inputValueFour.requestFocus();

            }
            return false;
        });

        inputValueThree.setOnFocusChangeListener((view, b) -> {
            if (b) {
                if (inputValueTwo.getText().toString().length() == 0) {
                    inputValueTwo.requestFocus();
                    showKeyboard();
                }
            }
        });
    }

    private void inputFieldFour() {
        inputValueFour.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before == 0) {
                    if (inputValueFour.getText().toString().length() == 1) {
                        inputValueFifth.requestFocus();
                        valueFour = inputValueFour.getText().toString().trim();

                    } else if (inputValueFour.getText().toString().length() == 0) {
                        inputValueThree.requestFocus();
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }

        });

        inputValueFour.setOnKeyListener((v, keyCode, event) -> {
            //You can identify which key pressed by checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                //this is for backspace
                fourCount++;
                if (fourCount >= 2 && inputValueFour.getText().toString().trim().length() == 0) {
                    inputValueThree.requestFocus();
                    fourCount = 0;
                }
                if (inputValueFour.getText().toString().trim().length() == 0) {
                    inputValueThree.requestFocus();
                    fourCount = 0;
                }
            }
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && inputValueFour.getText().toString().trim().length() == 1
                    && keyCode != KeyEvent.KEYCODE_DEL
                    && keyCode != KeyEvent.KEYCODE_SPACE) {
                //this is for backspace
                inputValueFifth.requestFocus();

            }
            return false;
        });

        inputValueFour.setOnFocusChangeListener((view, b) -> {
            if (b) {
                if (inputValueThree.getText().toString().length() == 0) {
                    inputValueThree.requestFocus();
                    showKeyboard();
                }
            }
        });
    }

    private void inputFieldFive() {
        inputValueFifth.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before == 0) {
                    if (inputValueFifth.getText().toString().length() == 1) {
                        inputValueSix.requestFocus();
                        valueFive = inputValueFifth.getText().toString().trim();
                    } else if (inputValueFifth.getText().toString().length() == 0) {
                        inputValueFour.requestFocus();
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {

            }

        });
        inputValueFifth.setOnKeyListener((v, keyCode, event) -> {
            //You can identify which key pressed by checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                //this is for backspace
                fiveCount++;
                if (fiveCount >= 2 && inputValueFifth.getText().toString().trim().length() == 0) {
                    inputValueFour.requestFocus();
                    fiveCount = 0;
                }
                if (inputValueFifth.getText().toString().trim().length() == 0) {
                    inputValueFour.requestFocus();
                    fiveCount = 0;
                }
            }
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && inputValueFifth.getText().toString().trim().length() == 1
                    && keyCode != KeyEvent.KEYCODE_DEL
                    && keyCode != KeyEvent.KEYCODE_SPACE) {
                //this is for backspace
                inputValueSix.requestFocus();

            }
            return false;
        });
        inputValueFifth.setOnFocusChangeListener((view, b) -> {
            if (b) {
                if (inputValueFour.getText().toString().length() == 0) {
                    inputValueFour.requestFocus();
                    showKeyboard();
                }
            }
        });
    }

    private void inputFieldSix() {
        inputValueSix.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before == 0) {
                    if (inputValueSix.getText().toString().length() == 0) {
                        inputValueFifth.requestFocus();
                    }
                    valueSix = inputValueSix.getText().toString().trim();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }

        });

        inputValueSix.setOnKeyListener((v, keyCode, event) -> {
            //You can identify which key pressed by checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                //this is for backspace
                sixCount++;
                if (sixCount >= 2 && inputValueSix.getText().toString().trim().length() == 0) {
                    inputValueFifth.requestFocus();
                    sixCount = 0;
                }
                if (inputValueSix.getText().toString().trim().length() == 0) {
                    inputValueFifth.requestFocus();
                    sixCount = 0;
                }
            }
            return false;
        });
        inputValueSix.setOnFocusChangeListener((view, b) -> {
            if (b) {
                if (inputValueFifth.getText().toString().length() == 0) {
                    inputValueFifth.requestFocus();
                    showKeyboard();
                }
            }
        });

    }

    public boolean isValid() {
        if (TextUtils.isEmpty(inputValueOne.getText().toString().trim())) {
            setErr(ERROR_MESSAGE_CODE);
            inputValueOne.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(inputValueTwo.getText().toString().trim())) {
            setErr(ERROR_MESSAGE_CODE);
            inputValueTwo.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(inputValueThree.getText().toString().trim())) {
            setErr(ERROR_MESSAGE_CODE);
            inputValueThree.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(inputValueFour.getText().toString().trim())) {
            setErr(ERROR_MESSAGE_CODE);
            inputValueFour.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(inputValueFifth.getText().toString().trim())) {
            setErr(ERROR_MESSAGE_CODE);
            inputValueFifth.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(inputValueSix.getText().toString().trim())) {
            setErr(ERROR_MESSAGE_CODE);
            inputValueSix.requestFocus();
            return false;
        } else {
            setErr("");
            return true;
        }
    }

    public void setErr(String err) {
        if (err.isEmpty()) {
            errorText.setVisibility(View.GONE);
            errorText.setText("");
        } else {

            errorText.setVisibility(View.VISIBLE);
            errorText.setText(err);
        }
    }

    public String getText() {
        return valueOne.concat(valueTwo).concat(valueThree).concat(valueFour).concat(valueFive).concat(valueSix);

    }

    public void actionGo(PinViewActionListener listener) {
        inputValueSix.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_GO) {
                if (isValid()) {
                    listener.onValid();
                }
                return true;
            }
            return false;
        });
    }
}