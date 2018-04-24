package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avcodec.AVCodec;

public class avcodec$AVCodec$Init_static_data_AVCodec extends FunctionPointer {
    private native void allocate();

    public native void call(AVCodec aVCodec);

    static {
        Loader.load();
    }

    public avcodec$AVCodec$Init_static_data_AVCodec(Pointer p) {
        super(p);
    }

    protected avcodec$AVCodec$Init_static_data_AVCodec() {
        allocate();
    }
}
