package org.bytedeco.javacpp;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoOffset;
import org.bytedeco.javacpp.annotation.StdVector;
import org.bytedeco.javacpp.opencv_core.StringVector;
import org.bytedeco.javacpp.presets.opencv_core.Str;

@Namespace("cv::flann")
@NoOffset
public class opencv_flann$IndexParams extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native void getAll(@ByRef StringVector stringVector, @StdVector IntBuffer intBuffer, @ByRef StringVector stringVector2, @StdVector DoubleBuffer doubleBuffer);

    public native void getAll(@ByRef StringVector stringVector, @StdVector IntPointer intPointer, @ByRef StringVector stringVector2, @StdVector DoublePointer doublePointer);

    public native void getAll(@ByRef StringVector stringVector, @StdVector int[] iArr, @ByRef StringVector stringVector2, @StdVector double[] dArr);

    public native double getDouble(@Str String str);

    public native double getDouble(@Str String str, double d);

    public native double getDouble(@Str BytePointer bytePointer);

    public native double getDouble(@Str BytePointer bytePointer, double d);

    public native int getInt(@Str String str);

    public native int getInt(@Str String str, int i);

    public native int getInt(@Str BytePointer bytePointer);

    public native int getInt(@Str BytePointer bytePointer, int i);

    @Str
    public native String getString(@Str String str);

    @Str
    public native String getString(@Str String str, @Str String str2);

    @Str
    public native BytePointer getString(@Str BytePointer bytePointer);

    @Str
    public native BytePointer getString(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    public native Pointer params();

    public native opencv_flann$IndexParams params(Pointer pointer);

    public native void setAlgorithm(int i);

    public native void setBool(@Str String str, @Cast({"bool"}) boolean z);

    public native void setBool(@Str BytePointer bytePointer, @Cast({"bool"}) boolean z);

    public native void setDouble(@Str String str, double d);

    public native void setDouble(@Str BytePointer bytePointer, double d);

    public native void setFloat(@Str String str, float f);

    public native void setFloat(@Str BytePointer bytePointer, float f);

    public native void setInt(@Str String str, int i);

    public native void setInt(@Str BytePointer bytePointer, int i);

    public native void setString(@Str String str, @Str String str2);

    public native void setString(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    static {
        Loader.load();
    }

    public opencv_flann$IndexParams(Pointer p) {
        super(p);
    }

    public opencv_flann$IndexParams(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_flann$IndexParams position(long position) {
        return (opencv_flann$IndexParams) super.position(position);
    }

    public opencv_flann$IndexParams() {
        super((Pointer) null);
        allocate();
    }
}
