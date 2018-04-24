package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.avcodec.AVCodecContext;
import org.bytedeco.javacpp.avcodec.AVCodecParserContext;

public class C1206x3307722d extends FunctionPointer {
    private native void allocate();

    public native int call(AVCodecParserContext aVCodecParserContext, AVCodecContext aVCodecContext, @Cast({"const uint8_t**"}) PointerPointer pointerPointer, IntPointer intPointer, @Cast({"const uint8_t*"}) BytePointer bytePointer, int i);

    static {
        Loader.load();
    }

    public C1206x3307722d(Pointer p) {
        super(p);
    }

    protected C1206x3307722d() {
        allocate();
    }
}
