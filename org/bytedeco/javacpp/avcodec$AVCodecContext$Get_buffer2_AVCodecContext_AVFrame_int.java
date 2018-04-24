package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avcodec.AVCodecContext;
import org.bytedeco.javacpp.avutil.AVFrame;

public class avcodec$AVCodecContext$Get_buffer2_AVCodecContext_AVFrame_int extends FunctionPointer {
    private native void allocate();

    public native int call(AVCodecContext aVCodecContext, AVFrame aVFrame, int i);

    static {
        Loader.load();
    }

    public avcodec$AVCodecContext$Get_buffer2_AVCodecContext_AVFrame_int(Pointer p) {
        super(p);
    }

    protected avcodec$AVCodecContext$Get_buffer2_AVCodecContext_AVFrame_int() {
        allocate();
    }
}
