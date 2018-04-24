package org.bytedeco.javacpp;

public class swscale$SwsFilter extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native swscale$SwsFilter chrH(swscale$SwsVector org_bytedeco_javacpp_swscale_SwsVector);

    public native swscale$SwsVector chrH();

    public native swscale$SwsFilter chrV(swscale$SwsVector org_bytedeco_javacpp_swscale_SwsVector);

    public native swscale$SwsVector chrV();

    public native swscale$SwsFilter lumH(swscale$SwsVector org_bytedeco_javacpp_swscale_SwsVector);

    public native swscale$SwsVector lumH();

    public native swscale$SwsFilter lumV(swscale$SwsVector org_bytedeco_javacpp_swscale_SwsVector);

    public native swscale$SwsVector lumV();

    static {
        Loader.load();
    }

    public swscale$SwsFilter() {
        super((Pointer) null);
        allocate();
    }

    public swscale$SwsFilter(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public swscale$SwsFilter(Pointer p) {
        super(p);
    }

    public swscale$SwsFilter position(long position) {
        return (swscale$SwsFilter) super.position(position);
    }
}
