package com.gogulf.passenger.app.utils.customcomponents;


import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gogulf.passenger.app.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import javax.annotation.Nullable;

public class CustomDropDownViews {
    public AutoCompleteTextView inputView;
    public TextView hintText;
    public ImageView clearSelection;
    private List<NameValue> listData;
    private String[] arrayData;
    String imageType;

    private Context context;

    private TextInputLayout textInputLayout;

    private String hint, errorText;
    private boolean isRequired = false;

    private int selectedItemPosition = -1;

    public ItemSelectionListener itemSelectionListener;
    public View.OnClickListener onClickListener;
    public View.OnFocusChangeListener onFocusChangeListener;

    public CustomDropDownViews(Context contexts, AutoCompleteTextView inputViews, @Nullable TextView textView) {
        this.context = contexts;
        this.inputView = inputViews;
        this.inputView.setFocusable(false);
        this.inputView.setInputType(InputType.TYPE_NULL);
        this.hintText = textView;
        this.inputView.addTextChangedListener(watcher);
    }

    public CustomDropDownViews(Context contexts, AutoCompleteTextView inputViews, ImageView imageView, String imageType) {
        this.context = contexts;
        this.inputView = inputViews;
        this.inputView.setFocusable(false);
        this.inputView.setInputType(InputType.TYPE_NULL);
        this.clearSelection = imageView;
        this.imageType = imageType;
        this.inputView.addTextChangedListener(watcher);
    }

    public void clear() {
        inputView.setText("");
        selectedItemPosition = -1;
        this.arrayData = null;
        this.listData = null;
    }

    public void emptyField() {
        inputView.setText("");
        setSelection(null);
        selectedItemPosition = -1;
    }

    public void setData(List<NameValue> data) {
        this.listData = data;
        this.arrayData = null;

        String[] array = NameValue.getNamesFromList(data);
        updateDropdown(array);
    }

    public void setData(String[] data) {
        this.arrayData = data;
        this.listData = null;
        updateDropdown(data);
    }

    public void setHintTextColor(int color) {
        hintText.setTextColor(color);
    }

    public boolean isEmpty() {
        if (inputView.getText().toString().isEmpty()) {
            inputView.setError("Required");
            return true;
        } else {
            inputView.setError(null);
            return false;
        }
    }

    public boolean isEmpty(String type) {
        if (inputView.getText().toString().isEmpty()) {
            inputView.setError(type + " is required");
            return true;
        } else {
            inputView.setError(null);
            return false;
        }
    }

    public void setVisibilityHint(int visiblity) {
        inputView.setVisibility(visiblity);
        hintText.setVisibility(visiblity);
    }

    public void hideHint(int visiblity) {
        hintText.setVisibility(visiblity);
    }

    public boolean getVisibilityHint() {
        return inputView.getVisibility() == View.VISIBLE;
    }

    public boolean isEmptyNoError() {
        return inputView.getText().toString().isEmpty();
    }

    public void setErrorText(String errorText) {
        if (errorText.isEmpty()) {
//            textInputLayout.setError("");
            inputView.setError(null);
//            textInputLayout.setErrorEnabled(false);
        } else {
//            textInputLayout.setError(errorText);
            inputView.setError(errorText);
//            textInputLayout.setErrorEnabled(true);
        }

    }

    /**
     * Update data and adapter
     *
     * @param data
     */
    private void updateDropdown(String[] data) {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.dropdown_view_item, android.R.id.text1, data);
        inputView.setAdapter(adapter);
        inputView.setOnClickListener(v -> {

            // Shows all options
            if (!inputView.getText().toString().equals("")) {
                adapter.getFilter().filter(null);
            }
            inputView.showDropDown();
            if (this.onClickListener != null)
                this.onClickListener.onClick(v);
        });

        inputView.setOnItemClickListener((adapterView, view12, i, l) -> {
            selectedItemPosition = i;

            if (this.itemSelectionListener != null)
                this.itemSelectionListener.onItemSelected(getSelectedItem());

            // Invoke setselectio to remove error
//            setSelection(getSelectedItem().Value);
        });
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
//        this.inputView.setOnClickListener(onClickListener);
        this.onClickListener = onClickListener;
    }
/*
    public void setDrawableImage(){
        inputView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_invisible,0);
        clearSelection.setVisibility(View.VISIBLE);
    }*/
   /* public void setDefault(){
    }*/

    public void setImageClickAble(View.OnClickListener listener) {
//        clearSelectedData();
        clearSelection.setOnClickListener(listener);


    }

    public void clearSelectedData() {
        inputView.clearListSelection();
        setSelection(null);
        inputView.setText("");
        inputView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_up_down_arrow, 0);
        clearSelection.setVisibility(View.GONE);
    }

    public void setonImageClick() {
        inputView.setOnClickListener(v -> {
            inputView.dismissDropDown();
        });
    }

    public void setupHint(String hint) {
        if (isRequired)
//            textInputLayout.setHint(hint);
            inputView.setHint(hint);
        else
//            textInputLayout.setHint(hint + " (optional)");
            inputView.setHint(hint);
    }

    public void hints(String hint) {
        if (isRequired)
//            textInputLayout.setHint(hint);
            hintText.setText(hint);
        else
//            textInputLayout.setHint(hint + " (optional)");
            hintText.setText(hint);
    }

    /**
     * Set selection by the value
     * Clear any error
     * Enable error to show margin at bottom
     *
     * @param value
     */
    public void setSelection(String value) {
        if (listData != null && !listData.isEmpty()) {
            int index = NameValue.getIndexOfValue(value, listData);
            if (index == -1 || index > listData.size()) return;

            selectedItemPosition = index;
            inputView.setText(listData.get(index).Name);

            if (this.itemSelectionListener != null)
                this.itemSelectionListener.onItemSelected(listData.get(index));
        }

        if (arrayData != null && arrayData.length != 0) {
            int index = NameValue.findIndexInArray(arrayData, value);
            if (index == -1 || index > arrayData.length) return;

            selectedItemPosition = index;
            inputView.setText(arrayData[index]);

            if (this.itemSelectionListener != null)
                this.itemSelectionListener.onItemSelected(new NameValue(arrayData[index], arrayData[index]));

        }

//        textInputLayout.setErrorEnabled(true);
//        textInputLayout.setError("");
        inputView.setError(null);
    }

    public NameValue getSelectedItem() {

        NameValue selectedItem = new NameValue();

        if (selectedItemPosition == -1) {
            return selectedItem;
        }

        if (this.listData != null) {
            selectedItem.Name = this.listData.get(selectedItemPosition).Name;
            selectedItem.Value = this.listData.get(selectedItemPosition).Value;
        }

        if (this.arrayData != null) {
            selectedItem.Name = this.arrayData[selectedItemPosition];
            selectedItem.Value = this.arrayData[selectedItemPosition];
        }

        return selectedItem;
    }


    /**
     * Name is the display name to be displayed on the list
     * Value is the actual data to be used
     */

    public static class NameValue implements Comparable<NameValue> {
        public String Name;
        public String Value;

        public NameValue() {
            Name = "";
            Value = "";
        }

        public NameValue(String name, String value) {
            Name = name;
            Value = value;
        }

        public static String[] getNamesFromList(List<NameValue> data) {
            String[] array = new String[data.size()];
            for (int i = 0; i < data.size(); i++) {
                array[i] = data.get(i).Name;
            }

            return array;
        }

        /**
         * Find index if item in the list, comparison by value of the item
         *
         * @param item
         * @param list
         * @return
         */
        public static int findIndexOf(NameValue item, List<NameValue> list) {
            if (list == null) return -1;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).Value.equals(item.Value))
                    return i;
            }
            return -1;
        }

        public static String getNameOfValue(String value, List<NameValue> list) {
            for (NameValue nv : list) {
                if (nv.Value.equals(value)) {
                    return nv.Name;
                }
            }
            return null;
        }

        public static String getValueOfName(String name, List<NameValue> list) {
            for (NameValue nv : list) {
                if (nv.Name.equals(name)) {
                    return nv.Value;
                }
            }
            return null;
        }

        public static int getIndexOfValue(String value, List<NameValue> list) {
            if (list == null) return -1;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).Value.equals(value))
                    return i;
            }
            return -1;
        }

        private static int findIndexInArray(String[] a, String s) {
            for (int i = 0; i < a.length; i++) {
                if (a[i].equals(s)) return i;
            }
            return -1;
        }

        public int compareTo(NameValue nv) {
            if (Name == null || nv.Name == null) {
                return 0;
            }
            return Name.compareTo(nv.Name);
        }


    }

    public interface ItemSelectionListener {
        void onItemSelected(NameValue item);
    }

    public void setOnItemSelectedListener(ItemSelectionListener listener) {
        this.itemSelectionListener = listener;

    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            inputView.setError(null);

        }
    };


}