package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.avcodec.AVCodecContext;
import org.bytedeco.javacpp.avcodec.AVSubtitle;

public class C1201xba535bb6 extends FunctionPointer {
    private native void allocate();

    public native int call(AVCodecContext aVCodecContext, @Cast({"uint8_t*"}) BytePointer bytePointer, int i, @Const AVSubtitle aVSubtitle);

    static {
        Loader.load();
    }

    public C1201xba535bb6(Pointer p) {
        super(p);
    }

    protected C1201xba535bb6() {
        allocate();
    }
}
