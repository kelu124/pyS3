package org.opencv.video;

public class BackgroundSubtractorMOG extends BackgroundSubtractor {
    private static native long BackgroundSubtractorMOG_0();

    private static native long BackgroundSubtractorMOG_1(int i, int i2, double d, double d2);

    private static native long BackgroundSubtractorMOG_2(int i, int i2, double d);

    private static native void delete(long j);

    protected BackgroundSubtractorMOG(long addr) {
        super(addr);
    }

    public BackgroundSubtractorMOG() {
        super(BackgroundSubtractorMOG_0());
    }

    public BackgroundSubtractorMOG(int history, int nmixtures, double backgroundRatio, double noiseSigma) {
        super(BackgroundSubtractorMOG_1(history, nmixtures, backgroundRatio, noiseSigma));
    }

    public BackgroundSubtractorMOG(int history, int nmixtures, double backgroundRatio) {
        super(BackgroundSubtractorMOG_2(history, nmixtures, backgroundRatio));
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
