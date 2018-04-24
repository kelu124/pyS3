package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.avformat.AVFormatContext;

public class C1218xbf1a4c18 extends FunctionPointer {
    private native void allocate();

    public native void call(AVFormatContext aVFormatContext, int i, @Cast({"int64_t*"}) LongPointer longPointer, @Cast({"int64_t*"}) LongPointer longPointer2);

    static {
        Loader.load();
    }

    public C1218xbf1a4c18(Pointer p) {
        super(p);
    }

    protected C1218xbf1a4c18() {
        allocate();
    }
}
