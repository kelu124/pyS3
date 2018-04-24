package org.bytedeco.javacpp;

public class freenect$freenect_chunk_cb extends FunctionPointer {
    private native void allocate();

    public native void call(Pointer pointer, Pointer pointer2, int i, int i2, Pointer pointer3);

    static {
        Loader.load();
    }

    public freenect$freenect_chunk_cb(Pointer p) {
        super(p);
    }

    protected freenect$freenect_chunk_cb() {
        allocate();
    }
}
