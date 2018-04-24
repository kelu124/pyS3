package com.lee.ultrascan.service.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.lee.ultrascan.utils.LogUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class WifiHelper {
    public static final String DEFAULT_PROBE_AP_NAME = "AscannerDAP";
    public static final String DEFAULT_PROBE_AP_PWD = "12345678";
    public static final int WIFI_AP_STATE_DISABLED = 11;
    public static final int WIFI_AP_STATE_DISABLING = 10;
    public static final int WIFI_AP_STATE_ENABLED = 13;
    public static final int WIFI_AP_STATE_ENABLING = 12;
    public static final int WIFI_AP_STATE_FAILED = 14;
    private Context context;
    private LocationManager locationManager;
    private WifiManager wifiManager;

    @SuppressLint({"WifiManagerPotentialLeak"})
    public WifiHelper(Context context) {
        this.context = context.getApplicationContext();
        this.wifiManager = (WifiManager) context.getSystemService("wifi");
        this.locationManager = (LocationManager) context.getSystemService("location");
    }

    public boolean isLocationServiceEnabled() {
        return this.locationManager.isProviderEnabled("network") || this.locationManager.isProviderEnabled("gps");
    }

    public void openWifi() {
        if (this.wifiManager.isWifiEnabled()) {
            this.wifiManager.setWifiEnabled(false);
        }
        this.wifiManager.setWifiEnabled(true);
    }

    public void closeWifi() {
        if (this.wifiManager.isWifiEnabled()) {
            this.wifiManager.setWifiEnabled(false);
        }
    }

    public boolean isWifiEnabled() {
        return this.wifiManager.isWifiEnabled();
    }

    public int getWifiState() {
        return this.wifiManager.getWifiState();
    }

    public boolean openWifiAP(String apName, String password) {
        if (isWifiEnabled()) {
            closeWifi();
        }
        if (isWifiApEnabled()) {
            closeWifiAP();
        }
        WifiConfiguration apConfig = new WifiConfiguration();
        apConfig.SSID = apName;
        apConfig.preSharedKey = password;
        apConfig.allowedAuthAlgorithms.set(0);
        apConfig.allowedKeyManagement.set(1);
        try {
            return ((Boolean) this.wifiManager.getClass().getMethod("setWifiApEnabled", new Class[]{WifiConfiguration.class, Boolean.TYPE}).invoke(this.wifiManager, new Object[]{apConfig, Boolean.valueOf(true)})).booleanValue();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
            return false;
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
            return false;
        }
    }

    public boolean closeWifiAP() {
        try {
            Method method = this.wifiManager.getClass().getMethod("setWifiApEnabled", new Class[]{WifiConfiguration.class, Boolean.TYPE});
            WifiConfiguration apConfig = new WifiConfiguration();
            return ((Boolean) method.invoke(this.wifiManager, new Object[]{apConfig, Boolean.valueOf(false)})).booleanValue();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
            return false;
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
            return false;
        }
    }

    public boolean isWifiApEnabled() {
        return getWifiApState() == 13;
    }

    public int getWifiApState() {
        int i = 14;
        try {
            i = ((Integer) this.wifiManager.getClass().getMethod("getWifiApState", new Class[0]).invoke(this.wifiManager, new Object[0])).intValue();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
        return i;
    }

    public NetworkInfo getActiveNetworkInfo() {
        return ((ConnectivityManager) this.context.getSystemService("connectivity")).getActiveNetworkInfo();
    }

    public String getActiveApName() {
        String ssid = this.wifiManager.getConnectionInfo().getSSID();
        if (TextUtils.isEmpty(ssid)) {
            return "";
        }
        if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
            ssid = ssid.substring(1, ssid.length() - 1);
        }
        return ssid;
    }

    public WifiInfo getConnectionInfo() {
        return this.wifiManager.getConnectionInfo();
    }

    public boolean isNetworkConnected() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private String stringContentToWifiConfigurationString(String stringContent) {
        return "\"" + stringContent + "\"";
    }

    private WifiConfiguration getWifiConfiguration(String apName, String password) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = stringContentToWifiConfigurationString(apName);
        wifiConfiguration.preSharedKey = stringContentToWifiConfigurationString(password);
        return wifiConfiguration;
    }

    public boolean enableNetwork(@NonNull String apName, @NonNull String password) {
        String targetSSID = stringContentToWifiConfigurationString(apName);
        LogUtils.LOGI("ssid", "wifihelper enableNetwork apName:" + apName + "  pwd:" + password);
        if (getConnectionInfo() != null) {
            String ssid = getActiveApName();
            LogUtils.LOGI("ssid", "wifiInfo ssid:" + ssid);
            LogUtils.LOGI("ssid", "apName:" + apName);
            if (targetSSID.contains(ssid) && !TextUtils.isEmpty(ssid)) {
                return true;
            }
        }
        boolean needAddConfig = true;
        int netId = -1;
        for (WifiConfiguration config : this.wifiManager.getConfiguredNetworks()) {
            WifiConfiguration config2;
            LogUtils.LOGI("ssid", "apName:" + config2.SSID + "  id:" + config2.networkId);
            if (targetSSID.contains(config2.SSID) && !TextUtils.isEmpty(config2.SSID)) {
                LogUtils.LOGI("ssid", "found SSID equal.");
                netId = config2.networkId;
                needAddConfig = false;
            }
        }
        if (needAddConfig) {
            config2 = getWifiConfiguration(apName, password);
            netId = this.wifiManager.addNetwork(config2);
            LogUtils.LOGI("ssid", "addNetwork:" + config2.SSID + "  id:" + config2.networkId);
        }
        if (!this.wifiManager.enableNetwork(netId, true)) {
            return this.wifiManager.reconnect();
        }
        LogUtils.LOGI("ssid", "enableNetwork id:" + netId);
        return false;
    }

    public boolean startScan() {
        return this.wifiManager.startScan();
    }

    public List<ScanResult> getScanResults() {
        List<ScanResult> results = this.wifiManager.getScanResults();
        return results != null ? results : new ArrayList();
    }
}
