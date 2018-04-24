package org.bytedeco.javacpp;

public class avutil$Func_Pointer_Pointer_int extends FunctionPointer {
    private native void allocate();

    public native void call(Pointer pointer, Pointer pointer2, int i);

    static {
        Loader.load();
    }

    public avutil$Func_Pointer_Pointer_int(Pointer p) {
        super(p);
    }

    protected avutil$Func_Pointer_Pointer_int() {
        allocate();
    }
}
