package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.avfilter.AVFilterContext;

public class C1207x3a460b40 extends FunctionPointer {
    private native void allocate();

    public native int call(AVFilterContext aVFilterContext, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"char*"}) BytePointer bytePointer3, int i, int i2);

    static {
        Loader.load();
    }

    public C1207x3a460b40(Pointer p) {
        super(p);
    }

    protected C1207x3a460b40() {
        allocate();
    }
}
