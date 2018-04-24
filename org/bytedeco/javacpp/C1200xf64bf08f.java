package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.avcodec.AVCodecContext;
import org.bytedeco.javacpp.avcodec.AVPacket;
import org.bytedeco.javacpp.avutil.AVFrame;

public class C1200xf64bf08f extends FunctionPointer {
    private native void allocate();

    public native int call(AVCodecContext aVCodecContext, AVPacket aVPacket, @Const AVFrame aVFrame, IntPointer intPointer);

    static {
        Loader.load();
    }

    public C1200xf64bf08f(Pointer p) {
        super(p);
    }

    protected C1200xf64bf08f() {
        allocate();
    }
}
