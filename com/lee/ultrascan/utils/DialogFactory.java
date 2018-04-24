package com.lee.ultrascan.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog.Builder;
import com.lee.ultrascan.C0796R;

public class DialogFactory {
    public static Dialog createSimpleOkErrorDialog(Context context, String title, String message) {
        Dialog dialog = new Builder(context).setTitle(title).setCancelable(false).setMessage(message).setNeutralButton(C0796R.string.dialog_action_ok, null).create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static Dialog createSimpleErrorDialog(Context context, String title, String message, String negativeText, OnClickListener onNegative, String positiveText, OnClickListener onPositive) {
        Dialog dialog = new Builder(context).setTitle(title).setMessage(message).setNegativeButton(negativeText, onNegative).setPositiveButton(positiveText, onPositive).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static ProgressDialog createProgressDialog(Context context, String title, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    public static ProgressDialog createProgressDialog(Context context, @StringRes int titleResource, @StringRes int messageResource) {
        return createProgressDialog(context, context.getString(titleResource), context.getString(messageResource));
    }

    public static Dialog createItemsDialog(Context context, String[] items, String title, OnClickListener onClickListener, String negativeButtonText, OnClickListener onNegative) {
        Dialog dialog = new Builder(context).setTitle(title).setItems(items, onClickListener).setCancelable(false).setNegativeButton(negativeButtonText, onNegative).create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
