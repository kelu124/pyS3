package org.opencv.gpu;

public class TargetArchs {
    protected final long nativeObj;

    private static native boolean builtWith_0(int i);

    private static native void delete(long j);

    private static native boolean hasBin_0(int i, int i2);

    private static native boolean hasEqualOrGreaterBin_0(int i, int i2);

    private static native boolean hasEqualOrGreaterPtx_0(int i, int i2);

    private static native boolean hasEqualOrGreater_0(int i, int i2);

    private static native boolean hasEqualOrLessPtx_0(int i, int i2);

    private static native boolean hasPtx_0(int i, int i2);

    private static native boolean has_0(int i, int i2);

    protected TargetArchs(long addr) {
        this.nativeObj = addr;
    }

    public static boolean builtWith(int feature_set) {
        return builtWith_0(feature_set);
    }

    public static boolean has(int major, int minor) {
        return has_0(major, minor);
    }

    public static boolean hasBin(int major, int minor) {
        return hasBin_0(major, minor);
    }

    public static boolean hasEqualOrGreater(int major, int minor) {
        return hasEqualOrGreater_0(major, minor);
    }

    public static boolean hasEqualOrGreaterBin(int major, int minor) {
        return hasEqualOrGreaterBin_0(major, minor);
    }

    public static boolean hasEqualOrGreaterPtx(int major, int minor) {
        return hasEqualOrGreaterPtx_0(major, minor);
    }

    public static boolean hasEqualOrLessPtx(int major, int minor) {
        return hasEqualOrLessPtx_0(major, minor);
    }

    public static boolean hasPtx(int major, int minor) {
        return hasPtx_0(major, minor);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
