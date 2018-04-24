package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avformat.AVFormatContext;
import org.bytedeco.javacpp.avformat.AVIOContext;

public class avformat$AVFormatContext$Io_close_AVFormatContext_AVIOContext extends FunctionPointer {
    private native void allocate();

    public native void call(AVFormatContext aVFormatContext, AVIOContext aVIOContext);

    static {
        Loader.load();
    }

    public avformat$AVFormatContext$Io_close_AVFormatContext_AVIOContext(Pointer p) {
        super(p);
    }

    protected avformat$AVFormatContext$Io_close_AVFormatContext_AVIOContext() {
        allocate();
    }
}
