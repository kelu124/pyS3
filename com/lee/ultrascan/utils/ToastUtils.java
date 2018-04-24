package com.lee.ultrascan.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static void show(Context context, String message, int length) {
        Toast.makeText(context, message, length).show();
    }

    public static void showLong(Context context, String message) {
        Toast.makeText(context, message, 1).show();
    }

    public static void showShort(Context context, String message) {
        Toast.makeText(context, message, 0).show();
    }

    public static void showLong(Context context, int messageId) {
        Toast.makeText(context, messageId, 1).show();
    }

    public static void showShort(Context context, int messageId) {
        Toast.makeText(context, messageId, 0).show();
    }
}
