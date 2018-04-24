package com.lee.ultrascan.utils;

import android.content.Context;
import android.preference.PreferenceManager;

public class PreferenceUtils {
    private PreferenceUtils() {
        throw new RuntimeException("Instantiate util class.");
    }

    public static void putInt(Context context, String key, int value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key, value).apply();
    }

    public static void putFloat(Context context, String key, float value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putFloat(key, value).apply();
    }

    public static void putBoolean(Context context, String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key, value).apply();
    }

    public static void putString(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).apply();
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, defaultValue);
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat(key, defaultValue);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static String getString(Context context, String key, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultValue);
    }

    public static void remove(Context context, String key) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(key).apply();
    }
}
