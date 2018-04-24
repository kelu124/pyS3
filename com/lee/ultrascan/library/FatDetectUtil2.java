package com.lee.ultrascan.library;

import android.graphics.PointF;
import android.graphics.Rect;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class FatDetectUtil2 {
    private static native int nativeDetectFat(long j, int i, int i2, int i3, int i4);

    public static Optional<PointF> detectFat(GrayFrame frame, Rect roi) {
        boolean z;
        boolean z2 = true;
        int l = roi.left;
        int t = roi.top;
        int w = roi.width();
        int h = roi.height();
        if (w < 0 || w > frame.getWidth()) {
            z = false;
        } else {
            z = true;
        }
        Preconditions.checkArgument(z);
        if (h < 0 || w > frame.getHeight()) {
            z2 = false;
        }
        Preconditions.checkArgument(z2);
        int fatHeightIndex = nativeDetectFat(frame.getNativeHandler(), l, t, w, h);
        if (fatHeightIndex == -1) {
            return Optional.absent();
        }
        return Optional.of(new PointF((float) (frame.getWidth() / 2), (float) fatHeightIndex));
    }
}
