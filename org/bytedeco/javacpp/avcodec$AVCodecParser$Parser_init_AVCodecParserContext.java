package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avcodec.AVCodecParserContext;

public class avcodec$AVCodecParser$Parser_init_AVCodecParserContext extends FunctionPointer {
    private native void allocate();

    public native int call(AVCodecParserContext aVCodecParserContext);

    static {
        Loader.load();
    }

    public avcodec$AVCodecParser$Parser_init_AVCodecParserContext(Pointer p) {
        super(p);
    }

    protected avcodec$AVCodecParser$Parser_init_AVCodecParserContext() {
        allocate();
    }
}
