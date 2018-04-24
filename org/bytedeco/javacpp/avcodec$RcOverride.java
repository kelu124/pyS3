package org.bytedeco.javacpp;

public class avcodec$RcOverride extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native int end_frame();

    public native avcodec$RcOverride end_frame(int i);

    public native int qscale();

    public native avcodec$RcOverride qscale(int i);

    public native float quality_factor();

    public native avcodec$RcOverride quality_factor(float f);

    public native int start_frame();

    public native avcodec$RcOverride start_frame(int i);

    static {
        Loader.load();
    }

    public avcodec$RcOverride() {
        super((Pointer) null);
        allocate();
    }

    public avcodec$RcOverride(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public avcodec$RcOverride(Pointer p) {
        super(p);
    }

    public avcodec$RcOverride position(long position) {
        return (avcodec$RcOverride) super.position(position);
    }
}
