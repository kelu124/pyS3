package com.uuzuche.lib_zxing.activity;

import android.content.Context;
import android.util.DisplayMetrics;
import com.uuzuche.lib_zxing.DisplayUtil;

public class ZXingLibrary {
    public static void initDisplayOpinion(Context context) {
        if (context != null) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            DisplayUtil.density = dm.density;
            DisplayUtil.densityDPI = dm.densityDpi;
            DisplayUtil.screenWidthPx = dm.widthPixels;
            DisplayUtil.screenhightPx = dm.heightPixels;
            DisplayUtil.screenWidthDip = (float) DisplayUtil.px2dip(context, (float) dm.widthPixels);
            DisplayUtil.screenHightDip = (float) DisplayUtil.px2dip(context, (float) dm.heightPixels);
        }
    }
}
