package com.lee.ultrascan.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import com.lee.ultrascan.utils.LogUtils;

public class WifiStateBroadcastReceiver extends BroadcastReceiver {

    public enum WifiState {
        ENABLED,
        DISABLED,
        CONNECTED,
        DISCONNECTED,
        SCAN_FINISHED,
        WRONG_PWD
    }

    public IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        intentFilter.addAction("android.net.wifi.SCAN_RESULTS");
        intentFilter.addAction("android.net.wifi.supplicant.STATE_CHANGE");
        return intentFilter;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtils.LOGI("wifistate", action);
        if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
            int wifiState = intent.getIntExtra("wifi_state", 4);
            if (wifiState == 3) {
                RxBus.publish(WifiState.ENABLED);
            } else if (wifiState == 1) {
                RxBus.publish(WifiState.DISABLED);
            }
        } else if ("android.net.wifi.STATE_CHANGE".equals(action)) {
            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
            DetailedState state = networkInfo.getDetailedState();
            if (networkInfo.isConnected()) {
                RxBus.publish(WifiState.CONNECTED);
            } else {
                RxBus.publish(WifiState.DISCONNECTED);
            }
        } else if ("android.net.wifi.SCAN_RESULTS".equals(action)) {
            RxBus.publish(WifiState.SCAN_FINISHED);
        } else if ("android.net.wifi.supplicant.STATE_CHANGE".equals(action) && intent.getIntExtra("supplicantError", -1) == 1) {
            RxBus.publish(WifiState.WRONG_PWD);
        }
    }
}
