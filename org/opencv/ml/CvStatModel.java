package org.opencv.ml;

public class CvStatModel {
    protected final long nativeObj;

    private static native void delete(long j);

    private static native void load_0(long j, String str, String str2);

    private static native void load_1(long j, String str);

    private static native void save_0(long j, String str, String str2);

    private static native void save_1(long j, String str);

    protected CvStatModel(long addr) {
        this.nativeObj = addr;
    }

    public void load(String filename, String name) {
        load_0(this.nativeObj, filename, name);
    }

    public void load(String filename) {
        load_1(this.nativeObj, filename);
    }

    public void save(String filename, String name) {
        save_0(this.nativeObj, filename, name);
    }

    public void save(String filename) {
        save_1(this.nativeObj, filename);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
