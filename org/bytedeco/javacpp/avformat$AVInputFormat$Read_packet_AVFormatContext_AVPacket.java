package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avcodec.AVPacket;
import org.bytedeco.javacpp.avformat.AVFormatContext;

public class avformat$AVInputFormat$Read_packet_AVFormatContext_AVPacket extends FunctionPointer {
    private native void allocate();

    public native int call(AVFormatContext aVFormatContext, AVPacket aVPacket);

    static {
        Loader.load();
    }

    public avformat$AVInputFormat$Read_packet_AVFormatContext_AVPacket(Pointer p) {
        super(p);
    }

    protected avformat$AVInputFormat$Read_packet_AVFormatContext_AVPacket() {
        allocate();
    }
}
