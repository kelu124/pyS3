package com.lee.ultrascan.utils;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import java.lang.reflect.Field;

public class ScreenUtils {
    private ScreenUtils() {
        throw new AssertionError();
    }

    public static float dpToPx(Context context, int dp) {
        return TypedValue.applyDimension(1, (float) dp, context.getResources().getDisplayMetrics());
    }

    public static int dpToPxOffset(Context context, int dp) {
        return (int) dpToPx(context, dp);
    }

    public static int dpToPxSize(Context context, int dp) {
        return (int) (0.5f + dpToPx(context, dp));
    }

    public static void changeWindowBrightness(Activity activity, float brightness) {
        Window window = activity.getWindow();
        LayoutParams layoutParams = window.getAttributes();
        layoutParams.screenBrightness = brightness;
        window.setAttributes(layoutParams);
    }

    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext != null) {
            InputMethodManager imm = (InputMethodManager) destContext.getSystemService("input_method");
            if (imm != null) {
                String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
                for (String param : arr) {
                    try {
                        Field f = imm.getClass().getDeclaredField(param);
                        if (!f.isAccessible()) {
                            f.setAccessible(true);
                        }
                        Object obj_get = f.get(imm);
                        if (obj_get != null && (obj_get instanceof View)) {
                            if (((View) obj_get).getContext() == destContext) {
                                f.set(imm, null);
                            } else {
                                return;
                            }
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }
    }
}
