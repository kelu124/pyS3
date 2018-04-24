package com.lee.ultrascan.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import com.google.common.net.HttpHeaders;

public class WifiHintDialogFragment extends DialogFragment {
    public static final String TAG = "WifiHintDialogFragment";

    class C10351 implements OnClickListener {
        C10351() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ((WifiHintDialogListener) WifiHintDialogFragment.this.getActivity()).onWifiHintDialogNegative();
        }
    }

    class C10362 implements OnClickListener {
        C10362() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ((WifiHintDialogListener) WifiHintDialogFragment.this.getActivity()).onWifiHintDialogPositive();
        }
    }

    public interface WifiHintDialogListener {
        void onWifiHintDialogNegative();

        void onWifiHintDialogPositive();
    }

    public static WifiHintDialogFragment newInstance() {
        return new WifiHintDialogFragment();
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog = new Builder(getActivity()).setTitle(HttpHeaders.WARNING).setMessage("This app need wifi opened.").setPositiveButton("OK", new C10362()).setNegativeButton("Quit", new C10351()).create();
        setCancelable(false);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
