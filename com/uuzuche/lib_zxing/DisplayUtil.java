package com.uuzuche.lib_zxing;

import android.content.Context;

public class DisplayUtil {
    public static float density;
    public static int densityDPI;
    public static float screenHightDip;
    public static float screenWidthDip;
    public static int screenWidthPx;
    public static int screenhightPx;

    public static int dip2px(Context context, float dpValue) {
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) ((pxValue / context.getResources().getDisplayMetrics().density) + 0.5f);
    }
}
