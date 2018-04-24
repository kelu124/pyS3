package org.bytedeco.javacpp;

public class avformat$AVIOContext$Read_pause_Pointer_int extends FunctionPointer {
    private native void allocate();

    public native int call(Pointer pointer, int i);

    static {
        Loader.load();
    }

    public avformat$AVIOContext$Read_pause_Pointer_int(Pointer p) {
        super(p);
    }

    protected avformat$AVIOContext$Read_pause_Pointer_int() {
        allocate();
    }
}
