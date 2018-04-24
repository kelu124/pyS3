package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avcodec.AVCodecParserContext;

public class avcodec$AVCodecParser$Parser_close_AVCodecParserContext extends FunctionPointer {
    private native void allocate();

    public native void call(AVCodecParserContext aVCodecParserContext);

    static {
        Loader.load();
    }

    public avcodec$AVCodecParser$Parser_close_AVCodecParserContext(Pointer p) {
        super(p);
    }

    protected avcodec$AVCodecParser$Parser_close_AVCodecParserContext() {
        allocate();
    }
}
