package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.avformat.AVFormatContext;

public class C1213x36aba536 extends FunctionPointer {
    private native void allocate();

    public native int call(AVFormatContext aVFormatContext, int i, @Cast({"int64_t"}) long j, @Cast({"int64_t"}) long j2, @Cast({"int64_t"}) long j3, int i2);

    static {
        Loader.load();
    }

    public C1213x36aba536(Pointer p) {
        super(p);
    }

    protected C1213x36aba536() {
        allocate();
    }
}
