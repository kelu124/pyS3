package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.ByPtrPtr;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.avformat.AVFormatContext;
import org.bytedeco.javacpp.avformat.AVIOContext;
import org.bytedeco.javacpp.avutil.AVDictionary;

public class avformat$AVOpenCallback extends FunctionPointer {
    private native void allocate();

    public native int call(AVFormatContext aVFormatContext, @ByPtrPtr AVIOContext aVIOContext, @Cast({"const char*"}) BytePointer bytePointer, int i, @Const avformat$AVIOInterruptCB org_bytedeco_javacpp_avformat_AVIOInterruptCB, @ByPtrPtr AVDictionary aVDictionary);

    static {
        Loader.load();
    }

    public avformat$AVOpenCallback(Pointer p) {
        super(p);
    }

    protected avformat$AVOpenCallback() {
        allocate();
    }
}
