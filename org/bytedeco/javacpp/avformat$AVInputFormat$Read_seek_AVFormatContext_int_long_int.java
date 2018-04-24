package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.avformat.AVFormatContext;

public class avformat$AVInputFormat$Read_seek_AVFormatContext_int_long_int extends FunctionPointer {
    private native void allocate();

    public native int call(AVFormatContext aVFormatContext, int i, @Cast({"int64_t"}) long j, int i2);

    static {
        Loader.load();
    }

    public avformat$AVInputFormat$Read_seek_AVFormatContext_int_long_int(Pointer p) {
        super(p);
    }

    protected avformat$AVInputFormat$Read_seek_AVFormatContext_int_long_int() {
        allocate();
    }
}
