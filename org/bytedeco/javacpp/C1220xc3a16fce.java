package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.avformat.AVFormatContext;

public class C1220xc3a16fce extends FunctionPointer {
    private native void allocate();

    public native int call(AVFormatContext aVFormatContext, int i, @Cast({"AVFrame**"}) PointerPointer pointerPointer, @Cast({"unsigned"}) int i2);

    static {
        Loader.load();
    }

    public C1220xc3a16fce(Pointer p) {
        super(p);
    }

    protected C1220xc3a16fce() {
        allocate();
    }
}
