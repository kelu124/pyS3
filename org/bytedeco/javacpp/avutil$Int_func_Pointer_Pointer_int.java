package org.bytedeco.javacpp;

public class avutil$Int_func_Pointer_Pointer_int extends FunctionPointer {
    private native void allocate();

    public native int call(Pointer pointer, Pointer pointer2, int i);

    static {
        Loader.load();
    }

    public avutil$Int_func_Pointer_Pointer_int(Pointer p) {
        super(p);
    }

    protected avutil$Int_func_Pointer_Pointer_int() {
        allocate();
    }
}
