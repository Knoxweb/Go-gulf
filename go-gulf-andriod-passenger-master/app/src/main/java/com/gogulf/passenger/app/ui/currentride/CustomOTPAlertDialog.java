package com.gogulf.passenger.app.ui.currentride;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.gogulf.passenger.app.R;
import com.gogulf.passenger.app.databinding.LayoutOtpAlertDialogBinding;

public class CustomOTPAlertDialog implements DialogInterface {
    private Activity activity;
    private Context context;
    private final Dialog dialog;
    private final LayoutOtpAlertDialogBinding binding;
    String negativeText = "";
    boolean cancellable = true;

    public CustomOTPAlertDialog(Context activity) {
        this.context = activity;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_otp_alert_dialog, null);

        binding = LayoutOtpAlertDialogBinding.inflate(inflater, null);
        dialog = new Dialog(activity);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(cancellable);
        dialog.setCanceledOnTouchOutside(cancellable);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    public CustomOTPAlertDialog setIcon(int icon) {
        binding.alertIcon.setVisibility(View.VISIBLE);
        Glide.with(context).load(icon).into(binding.alertIcon);
        return this;
    }

    public CustomOTPAlertDialog show() {
        dialog.setCancelable(cancellable);
        dialog.setCanceledOnTouchOutside(cancellable);
        dialog.show();
        return this;
    }

//    public CustomOTPAlertDialog setTitle(String title) {
//        binding.alertTitle.setVisibility(View.VISIBLE);
//        binding.alertTitle.setText(title);
//        return this;
//    }

    public CustomOTPAlertDialog setMessage(String title) {
        binding.alertText.setVisibility(View.VISIBLE);
        binding.alertText.setText(title);
        return this;
    }



    public CustomOTPAlertDialog setCancellable(boolean cancellable) {
        this.cancellable = cancellable;
        return this;
    }

    public boolean isShowing() {
       return dialog.isShowing();
    }



    @Override
    public void cancel() {
        dialog.dismiss();
    }

    @Override
    public void dismiss() {
        dialog.dismiss();
    }

    public CustomOTPAlertDialog dismissDialog() {
        dialog.dismiss();
        return this;
    }
}
