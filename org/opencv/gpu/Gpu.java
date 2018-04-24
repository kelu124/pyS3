package org.opencv.gpu;

public class Gpu {
    public static final int DYNAMIC_PARALLELISM = 35;
    public static final int FEATURE_SET_COMPUTE_10 = 10;
    public static final int FEATURE_SET_COMPUTE_11 = 11;
    public static final int FEATURE_SET_COMPUTE_12 = 12;
    public static final int FEATURE_SET_COMPUTE_13 = 13;
    public static final int FEATURE_SET_COMPUTE_20 = 20;
    public static final int FEATURE_SET_COMPUTE_21 = 21;
    public static final int FEATURE_SET_COMPUTE_30 = 30;
    public static final int FEATURE_SET_COMPUTE_35 = 35;
    public static final int GLOBAL_ATOMICS = 11;
    public static final int NATIVE_DOUBLE = 13;
    public static final int SHARED_ATOMICS = 12;
    public static final int WARP_SHUFFLE_FUNCTIONS = 30;

    private static native boolean deviceSupports_0(int i);

    private static native int getCudaEnabledDeviceCount_0();

    private static native int getDevice_0();

    private static native void printCudaDeviceInfo_0(int i);

    private static native void printShortCudaDeviceInfo_0(int i);

    private static native void resetDevice_0();

    private static native void setDevice_0(int i);

    public static boolean deviceSupports(int feature_set) {
        return deviceSupports_0(feature_set);
    }

    public static int getCudaEnabledDeviceCount() {
        return getCudaEnabledDeviceCount_0();
    }

    public static int getDevice() {
        return getDevice_0();
    }

    public static void printCudaDeviceInfo(int device) {
        printCudaDeviceInfo_0(device);
    }

    public static void printShortCudaDeviceInfo(int device) {
        printShortCudaDeviceInfo_0(device);
    }

    public static void resetDevice() {
        resetDevice_0();
    }

    public static void setDevice(int device) {
        setDevice_0(device);
    }
}
