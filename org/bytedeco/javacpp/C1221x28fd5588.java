package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;

public class C1221x28fd5588 extends FunctionPointer {
    private native void allocate();

    public native int call(@Cast({"AVOptionRanges**"}) PointerPointer pointerPointer, Pointer pointer, @Cast({"const char*"}) BytePointer bytePointer, int i);

    static {
        Loader.load();
    }

    public C1221x28fd5588(Pointer p) {
        super(p);
    }

    protected C1221x28fd5588() {
        allocate();
    }
}
