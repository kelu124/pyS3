package org.opencv.video;

public class BackgroundSubtractorMOG2 extends BackgroundSubtractor {
    private static native long BackgroundSubtractorMOG2_0();

    private static native long BackgroundSubtractorMOG2_1(int i, float f, boolean z);

    private static native long BackgroundSubtractorMOG2_2(int i, float f);

    private static native void delete(long j);

    protected BackgroundSubtractorMOG2(long addr) {
        super(addr);
    }

    public BackgroundSubtractorMOG2() {
        super(BackgroundSubtractorMOG2_0());
    }

    public BackgroundSubtractorMOG2(int history, float varThreshold, boolean bShadowDetection) {
        super(BackgroundSubtractorMOG2_1(history, varThreshold, bShadowDetection));
    }

    public BackgroundSubtractorMOG2(int history, float varThreshold) {
        super(BackgroundSubtractorMOG2_2(history, varThreshold));
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
