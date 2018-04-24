package com.lee.ultrascan.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackageUtils {
    private PackageUtils() {
        throw new AssertionError();
    }

    public static String getVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
