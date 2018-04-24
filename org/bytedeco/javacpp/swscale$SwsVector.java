package org.bytedeco.javacpp;

public class swscale$SwsVector extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native DoublePointer coeff();

    public native swscale$SwsVector coeff(DoublePointer doublePointer);

    public native int length();

    public native swscale$SwsVector length(int i);

    static {
        Loader.load();
    }

    public swscale$SwsVector() {
        super((Pointer) null);
        allocate();
    }

    public swscale$SwsVector(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public swscale$SwsVector(Pointer p) {
        super(p);
    }

    public swscale$SwsVector position(long position) {
        return (swscale$SwsVector) super.position(position);
    }
}
