package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.avformat.AVFormatContext;

public class C1214x40675743 extends FunctionPointer {
    private native void allocate();

    public native int call(AVFormatContext aVFormatContext, int i, Pointer pointer, @Cast({"size_t"}) long j);

    static {
        Loader.load();
    }

    public C1214x40675743(Pointer p) {
        super(p);
    }

    protected C1214x40675743() {
        allocate();
    }
}
