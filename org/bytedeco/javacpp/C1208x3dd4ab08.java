package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.avformat.AVFormatContext;

public class C1208x3dd4ab08 extends FunctionPointer {
    private native void allocate();

    public native int call(AVFormatContext aVFormatContext, @Cast({"AVIOContext**"}) PointerPointer pointerPointer, @Cast({"const char*"}) BytePointer bytePointer, int i, @Cast({"AVDictionary**"}) PointerPointer pointerPointer2);

    static {
        Loader.load();
    }

    public C1208x3dd4ab08(Pointer p) {
        super(p);
    }

    protected C1208x3dd4ab08() {
        allocate();
    }
}
