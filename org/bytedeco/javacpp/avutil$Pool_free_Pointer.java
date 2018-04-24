package org.bytedeco.javacpp;

public class avutil$Pool_free_Pointer extends FunctionPointer {
    private native void allocate();

    public native void call(Pointer pointer);

    static {
        Loader.load();
    }

    public avutil$Pool_free_Pointer(Pointer p) {
        super(p);
    }

    protected avutil$Pool_free_Pointer() {
        allocate();
    }
}
