package org.bytedeco.javacpp;

public class avutil$AVRational extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native int den();

    public native avutil$AVRational den(int i);

    public native int num();

    public native avutil$AVRational num(int i);

    static {
        Loader.load();
    }

    public avutil$AVRational() {
        super((Pointer) null);
        allocate();
    }

    public avutil$AVRational(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public avutil$AVRational(Pointer p) {
        super(p);
    }

    public avutil$AVRational position(long position) {
        return (avutil$AVRational) super.position(position);
    }
}
