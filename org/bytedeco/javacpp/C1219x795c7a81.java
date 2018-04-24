package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avcodec.AVPacket;
import org.bytedeco.javacpp.avformat.AVFormatContext;

public class C1219x795c7a81 extends FunctionPointer {
    private native void allocate();

    public native int call(AVFormatContext aVFormatContext, AVPacket aVPacket, AVPacket aVPacket2, int i);

    static {
        Loader.load();
    }

    public C1219x795c7a81(Pointer p) {
        super(p);
    }

    protected C1219x795c7a81() {
        allocate();
    }
}
