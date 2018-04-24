package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;

public class avformat$AVOutputFormat$Query_codec_int_int extends FunctionPointer {
    private native void allocate();

    public native int call(@Cast({"AVCodecID"}) int i, int i2);

    static {
        Loader.load();
    }

    public avformat$AVOutputFormat$Query_codec_int_int(Pointer p) {
        super(p);
    }

    protected avformat$AVOutputFormat$Query_codec_int_int() {
        allocate();
    }
}
