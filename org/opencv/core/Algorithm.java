package org.opencv.core;

import java.util.ArrayList;
import java.util.List;
import org.opencv.utils.Converters;

public class Algorithm {
    protected final long nativeObj;

    private static native void delete(long j);

    private static native boolean getBool_0(long j, String str);

    private static native double getDouble_0(long j, String str);

    private static native int getInt_0(long j, String str);

    private static native long getMatVector_0(long j, String str);

    private static native long getMat_0(long j, String str);

    private static native String getString_0(long j, String str);

    private static native String paramHelp_0(long j, String str);

    private static native int paramType_0(long j, String str);

    private static native void setBool_0(long j, String str, boolean z);

    private static native void setDouble_0(long j, String str, double d);

    private static native void setInt_0(long j, String str, int i);

    private static native void setMatVector_0(long j, String str, long j2);

    private static native void setMat_0(long j, String str, long j2);

    private static native void setString_0(long j, String str, String str2);

    protected Algorithm(long addr) {
        this.nativeObj = addr;
    }

    public boolean getBool(String name) {
        return getBool_0(this.nativeObj, name);
    }

    public double getDouble(String name) {
        return getDouble_0(this.nativeObj, name);
    }

    public int getInt(String name) {
        return getInt_0(this.nativeObj, name);
    }

    public Mat getMat(String name) {
        return new Mat(getMat_0(this.nativeObj, name));
    }

    public List<Mat> getMatVector(String name) {
        List<Mat> retVal = new ArrayList();
        Converters.Mat_to_vector_Mat(new Mat(getMatVector_0(this.nativeObj, name)), retVal);
        return retVal;
    }

    public String getString(String name) {
        return getString_0(this.nativeObj, name);
    }

    public String paramHelp(String name) {
        return paramHelp_0(this.nativeObj, name);
    }

    public int paramType(String name) {
        return paramType_0(this.nativeObj, name);
    }

    public void setBool(String name, boolean value) {
        setBool_0(this.nativeObj, name, value);
    }

    public void setDouble(String name, double value) {
        setDouble_0(this.nativeObj, name, value);
    }

    public void setInt(String name, int value) {
        setInt_0(this.nativeObj, name, value);
    }

    public void setMat(String name, Mat value) {
        setMat_0(this.nativeObj, name, value.nativeObj);
    }

    public void setMatVector(String name, List<Mat> value) {
        setMatVector_0(this.nativeObj, name, Converters.vector_Mat_to_Mat(value).nativeObj);
    }

    public void setString(String name, String value) {
        setString_0(this.nativeObj, name, value);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
