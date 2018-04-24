package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.avcodec.AVPacket;
import org.bytedeco.javacpp.avformat.AVFormatContext;

public class avformat$AVOutputFormat$Check_bitstream_AVFormatContext_AVPacket extends FunctionPointer {
    private native void allocate();

    public native int call(AVFormatContext aVFormatContext, @Const AVPacket aVPacket);

    static {
        Loader.load();
    }

    public avformat$AVOutputFormat$Check_bitstream_AVFormatContext_AVPacket(Pointer p) {
        super(p);
    }

    protected avformat$AVOutputFormat$Check_bitstream_AVFormatContext_AVPacket() {
        allocate();
    }
}
