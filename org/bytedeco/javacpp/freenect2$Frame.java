package org.bytedeco.javacpp;

import java.nio.ByteBuffer;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoOffset;

@Namespace("libfreenect2")
@NoOffset
public class freenect2$Frame extends Pointer {
    public static final int BGRX = 4;
    public static final int Color = 1;
    public static final int Depth = 4;
    public static final int Float = 2;
    public static final int Gray = 6;
    public static final int Invalid = 0;
    public static final int Ir = 2;
    public static final int RGBX = 5;
    public static final int Raw = 1;

    private native void allocate(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2, @Cast({"size_t"}) long j3);

    private native void allocate(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2, @Cast({"size_t"}) long j3, @Cast({"unsigned char*"}) ByteBuffer byteBuffer);

    private native void allocate(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2, @Cast({"size_t"}) long j3, @Cast({"unsigned char*"}) BytePointer bytePointer);

    private native void allocate(@Cast({"size_t"}) long j, @Cast({"size_t"}) long j2, @Cast({"size_t"}) long j3, @Cast({"unsigned char*"}) byte[] bArr);

    @Cast({"size_t"})
    public native long bytes_per_pixel();

    public native freenect2$Frame bytes_per_pixel(long j);

    @Cast({"unsigned char*"})
    public native BytePointer data();

    public native freenect2$Frame data(BytePointer bytePointer);

    public native float exposure();

    public native freenect2$Frame exposure(float f);

    @Cast({"libfreenect2::Frame::Format"})
    public native int format();

    public native freenect2$Frame format(int i);

    public native float gain();

    public native freenect2$Frame gain(float f);

    public native float gamma();

    public native freenect2$Frame gamma(float f);

    @Cast({"size_t"})
    public native long height();

    public native freenect2$Frame height(long j);

    @Cast({"uint32_t"})
    public native int sequence();

    public native freenect2$Frame sequence(int i);

    @Cast({"uint32_t"})
    public native int status();

    public native freenect2$Frame status(int i);

    @Cast({"uint32_t"})
    public native int timestamp();

    public native freenect2$Frame timestamp(int i);

    @Cast({"size_t"})
    public native long width();

    public native freenect2$Frame width(long j);

    static {
        Loader.load();
    }

    public freenect2$Frame(Pointer p) {
        super(p);
    }

    public freenect2$Frame(@Cast({"size_t"}) long width, @Cast({"size_t"}) long height, @Cast({"size_t"}) long bytes_per_pixel, @Cast({"unsigned char*"}) BytePointer data_) {
        super((Pointer) null);
        allocate(width, height, bytes_per_pixel, data_);
    }

    public freenect2$Frame(@Cast({"size_t"}) long width, @Cast({"size_t"}) long height, @Cast({"size_t"}) long bytes_per_pixel) {
        super((Pointer) null);
        allocate(width, height, bytes_per_pixel);
    }

    public freenect2$Frame(@Cast({"size_t"}) long width, @Cast({"size_t"}) long height, @Cast({"size_t"}) long bytes_per_pixel, @Cast({"unsigned char*"}) ByteBuffer data_) {
        super((Pointer) null);
        allocate(width, height, bytes_per_pixel, data_);
    }

    public freenect2$Frame(@Cast({"size_t"}) long width, @Cast({"size_t"}) long height, @Cast({"size_t"}) long bytes_per_pixel, @Cast({"unsigned char*"}) byte[] data_) {
        super((Pointer) null);
        allocate(width, height, bytes_per_pixel, data_);
    }
}
