package org.bytedeco.javacpp;

public class avutil$AVClass$Child_next_Pointer_Pointer extends FunctionPointer {
    private native void allocate();

    public native Pointer call(Pointer pointer, Pointer pointer2);

    static {
        Loader.load();
    }

    public avutil$AVClass$Child_next_Pointer_Pointer(Pointer p) {
        super(p);
    }

    protected avutil$AVClass$Child_next_Pointer_Pointer() {
        allocate();
    }
}
