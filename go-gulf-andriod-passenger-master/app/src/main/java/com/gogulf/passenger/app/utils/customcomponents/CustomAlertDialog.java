package com.gogulf.passenger.app.utils.customcomponents;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.card.MaterialCardView;
import com.gogulf.passenger.app.R;
import com.bumptech.glide.Glide;
import com.gogulf.passenger.app.databinding.LayoutAlertDialogBinding;

public class CustomAlertDialog implements DialogInterface {
    private final Context context;
    private final Dialog dialog;
//    private final LayoutAlertDialogBinding binding;
    private String negativeText = "";
    private boolean cancellable = true;

    private final View layoutView;

    private final ImageView alertIcon;
    private final TextView alertTitle;
    private final TextView alertText;
    private final ConstraintLayout buttonLayout;

    private final MaterialCardView negativeTextBtn;
    private final TextView negativeTextView;
    private final MaterialCardView positiveTextBtn;
    private final TextView positiveText;


    public CustomAlertDialog(Context context) {
        this.context = context;
        dialog = new Dialog(context);
        Rect displayRectangle = new Rect();
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.getWindow().getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutView = inflater.inflate(R.layout.layout_alert_dialog, null);


        alertIcon = layoutView.findViewById(R.id.alertIcon);
        alertTitle = layoutView.findViewById(R.id.alertTitle);
        alertText = layoutView.findViewById(R.id.alertText);
        buttonLayout = layoutView.findViewById(R.id.buttonLayout);
        negativeTextBtn = layoutView.findViewById(R.id.negativeTextBtn);
        negativeTextView = layoutView.findViewById(R.id.negativeText);
        positiveTextBtn = layoutView.findViewById(R.id.positiveTextBtn);
        positiveText = layoutView.findViewById(R.id.positiveText);


        // Set the minimum width to 80% of the screen width
//        layoutView.setMinimumWidth((int) (displayRectangle.width() * 0.8));
//        layoutView.setMinimumHeight(0);
        // Setup the dialog

        dialog.getWindow().setLayout(
                (int) (displayRectangle.width() * 0.88f),
                dialog.getWindow().getAttributes().height);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutView);
        dialog.setCancelable(cancellable);
        dialog.setCanceledOnTouchOutside(cancellable);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public CustomAlertDialog setIcon(int icon) {
        alertIcon.setVisibility(View.VISIBLE);
        Glide.with(context).load(icon).into(alertIcon);
        return this;
    }

    public CustomAlertDialog show() {
        if (negativeText.isEmpty()) {
            negativeTextBtn.setVisibility(View.GONE);
        }
        dialog.setCancelable(cancellable);



        dialog.setCanceledOnTouchOutside(cancellable);
        dialog.show();
        return this;
    }

    public CustomAlertDialog setTitle(String title) {
        alertTitle.setVisibility(View.VISIBLE);
        alertTitle.setText(title);
        return this;
    }

    public CustomAlertDialog setMessage(String message) {
        alertText.setVisibility(View.VISIBLE);
        alertText.setText(message);
        return this;
    }

    public CustomAlertDialog setPositiveText(String title, DialogInterface.OnClickListener clickListener) {
        if (title.isEmpty()) {
            positiveText.setVisibility(View.GONE);
            positiveTextBtn.setVisibility(View.GONE);
//            horizontalLine.setVisibility(View.GONE);
        } else {
            positiveText.setText(title);
            positiveTextBtn.setOnClickListener(view -> {
                clickListener.onClick(this, 0);
                dismiss();
            });
        }

        return this;
    }

    public CustomAlertDialog setCancellable(boolean cancellable) {
        this.cancellable = cancellable;
        dialog.setCancelable(cancellable);
        dialog.setCanceledOnTouchOutside(cancellable);
        return this;
    }

    public CustomAlertDialog setNegativeText(String title, DialogInterface.OnClickListener clickListener) {
        negativeText = title;
        negativeTextBtn.setVisibility(View.VISIBLE);
        negativeTextView.setText(title);
        negativeTextBtn.setOnClickListener(view -> {
            clickListener.onClick(this, 0);
            dismiss();
        });
        return this;
    }

    @Override
    public void cancel() {
        dialog.dismiss();
    }

    @Override
    public void dismiss() {
        dialog.dismiss();
    }

    public CustomAlertDialog dismissDialog() {
        dialog.dismiss();
        return this;
    }

    public CustomAlertDialog removePrimaryButton() {
        positiveText.setVisibility(View.GONE);
        positiveTextBtn.setVisibility(View.GONE);
//        binding.horizontalLine.setVisibility(View.GONE);
        return this;
    }
}