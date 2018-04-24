package org.opencv.gpu;

public class DeviceInfo {
    protected final long nativeObj;

    private static native long DeviceInfo_0();

    private static native long DeviceInfo_1(int i);

    private static native void delete(long j);

    private static native int deviceID_0(long j);

    private static native long freeMemory_0(long j);

    private static native boolean isCompatible_0(long j);

    private static native int majorVersion_0(long j);

    private static native int minorVersion_0(long j);

    private static native int multiProcessorCount_0(long j);

    private static native String name_0(long j);

    private static native void queryMemory_0(long j, double[] dArr, double[] dArr2);

    private static native long sharedMemPerBlock_0(long j);

    private static native boolean supports_0(long j, int i);

    private static native long totalMemory_0(long j);

    protected DeviceInfo(long addr) {
        this.nativeObj = addr;
    }

    public DeviceInfo() {
        this.nativeObj = DeviceInfo_0();
    }

    public DeviceInfo(int device_id) {
        this.nativeObj = DeviceInfo_1(device_id);
    }

    public int deviceID() {
        return deviceID_0(this.nativeObj);
    }

    public long freeMemory() {
        return freeMemory_0(this.nativeObj);
    }

    public boolean isCompatible() {
        return isCompatible_0(this.nativeObj);
    }

    public int majorVersion() {
        return majorVersion_0(this.nativeObj);
    }

    public int minorVersion() {
        return minorVersion_0(this.nativeObj);
    }

    public int multiProcessorCount() {
        return multiProcessorCount_0(this.nativeObj);
    }

    public String name() {
        return name_0(this.nativeObj);
    }

    public void queryMemory(long totalMemory, long freeMemory) {
        double[] totalMemory_out = new double[1];
        double[] freeMemory_out = new double[1];
        queryMemory_0(this.nativeObj, totalMemory_out, freeMemory_out);
        totalMemory = (long) totalMemory_out[0];
        freeMemory = (long) freeMemory_out[0];
    }

    public long sharedMemPerBlock() {
        return sharedMemPerBlock_0(this.nativeObj);
    }

    public boolean supports(int feature_set) {
        return supports_0(this.nativeObj, feature_set);
    }

    public long totalMemory() {
        return totalMemory_0(this.nativeObj);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
