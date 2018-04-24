package com.lee.ultrascan.utils;

import android.content.Context;
import android.net.wifi.WifiManager;

public class WifiUtils {
    public static void setWifiEnabled(Context context, boolean enable) {
        ((WifiManager) context.getSystemService("wifi")).setWifiEnabled(enable);
    }

    public static int getWifiState(Context context) {
        return ((WifiManager) context.getSystemService("wifi")).getWifiState();
    }
}
