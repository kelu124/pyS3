package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avcodec.AVCodecContext;
import org.bytedeco.javacpp.avutil.AVFrame;

public class avcodec$AVHWAccel$Alloc_frame_AVCodecContext_AVFrame extends FunctionPointer {
    private native void allocate();

    public native int call(AVCodecContext aVCodecContext, AVFrame aVFrame);

    static {
        Loader.load();
    }

    public avcodec$AVHWAccel$Alloc_frame_AVCodecContext_AVFrame(Pointer p) {
        super(p);
    }

    protected avcodec$AVHWAccel$Alloc_frame_AVCodecContext_AVFrame() {
        allocate();
    }
}
