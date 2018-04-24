package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;

public class C1209x467b45e0 extends FunctionPointer {
    private native void allocate();

    public native int call(Pointer pointer, @Cast({"uint8_t*"}) BytePointer bytePointer, int i, @Cast({"AVIODataMarkerType"}) int i2, @Cast({"int64_t"}) long j);

    static {
        Loader.load();
    }

    public C1209x467b45e0(Pointer p) {
        super(p);
    }

    protected C1209x467b45e0() {
        allocate();
    }
}
