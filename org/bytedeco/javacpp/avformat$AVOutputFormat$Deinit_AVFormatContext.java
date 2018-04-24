package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avformat.AVFormatContext;

public class avformat$AVOutputFormat$Deinit_AVFormatContext extends FunctionPointer {
    private native void allocate();

    public native void call(AVFormatContext aVFormatContext);

    static {
        Loader.load();
    }

    public avformat$AVOutputFormat$Deinit_AVFormatContext(Pointer p) {
        super(p);
    }

    protected avformat$AVOutputFormat$Deinit_AVFormatContext() {
        allocate();
    }
}
