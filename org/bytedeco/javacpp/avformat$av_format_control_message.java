package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.avformat.AVFormatContext;

public class avformat$av_format_control_message extends FunctionPointer {
    private native void allocate();

    public native int call(AVFormatContext aVFormatContext, int i, Pointer pointer, @Cast({"size_t"}) long j);

    static {
        Loader.load();
    }

    public avformat$av_format_control_message(Pointer p) {
        super(p);
    }

    protected avformat$av_format_control_message() {
        allocate();
    }
}
