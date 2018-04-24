package org.bytedeco.javacpp;

public class freenect$freenect_sample_51 extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native freenect$freenect_sample_51 center(short s);

    public native short center();

    public native freenect$freenect_sample_51 left(short s);

    public native short left();

    public native freenect$freenect_sample_51 lfe(short s);

    public native short lfe();

    public native freenect$freenect_sample_51 right(short s);

    public native short right();

    public native freenect$freenect_sample_51 surround_left(short s);

    public native short surround_left();

    public native freenect$freenect_sample_51 surround_right(short s);

    public native short surround_right();

    static {
        Loader.load();
    }

    public freenect$freenect_sample_51() {
        super((Pointer) null);
        allocate();
    }

    public freenect$freenect_sample_51(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public freenect$freenect_sample_51(Pointer p) {
        super(p);
    }

    public freenect$freenect_sample_51 position(long position) {
        return (freenect$freenect_sample_51) super.position(position);
    }
}
