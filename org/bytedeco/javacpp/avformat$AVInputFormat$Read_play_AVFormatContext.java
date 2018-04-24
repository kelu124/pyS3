package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avformat.AVFormatContext;

public class avformat$AVInputFormat$Read_play_AVFormatContext extends FunctionPointer {
    private native void allocate();

    public native int call(AVFormatContext aVFormatContext);

    static {
        Loader.load();
    }

    public avformat$AVInputFormat$Read_play_AVFormatContext(Pointer p) {
        super(p);
    }

    protected avformat$AVInputFormat$Read_play_AVFormatContext() {
        allocate();
    }
}
