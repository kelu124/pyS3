package com.lee.ultrascan.library;

public class FrameProcessUtil {
    private static native void frameContrastEnhance(long j, float f);

    private static native void frameCorrelationEnhance(long j, long j2, float f);

    public static void frameCorrelationEnhance(GrayFrame prevFrame, GrayFrame currentFrameInOut, float factor) {
        frameCorrelationEnhance(prevFrame.getNativeHandler(), currentFrameInOut.getNativeHandler(), factor);
    }

    public static void frameContrastEnhance(GrayFrame currentFrameInOut, float factor) {
        frameContrastEnhance(currentFrameInOut.getNativeHandler(), factor);
    }
}
