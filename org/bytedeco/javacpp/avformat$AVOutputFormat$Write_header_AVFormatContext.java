package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avformat.AVFormatContext;

public class avformat$AVOutputFormat$Write_header_AVFormatContext extends FunctionPointer {
    private native void allocate();

    public native int call(AVFormatContext aVFormatContext);

    static {
        Loader.load();
    }

    public avformat$AVOutputFormat$Write_header_AVFormatContext(Pointer p) {
        super(p);
    }

    protected avformat$AVOutputFormat$Write_header_AVFormatContext() {
        allocate();
    }
}
