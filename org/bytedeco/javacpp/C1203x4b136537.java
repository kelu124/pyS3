package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.avcodec.AVCodecContext;
import org.bytedeco.javacpp.avutil.AVFrame;

public class C1203x4b136537 extends FunctionPointer {
    private native void allocate();

    public native void call(AVCodecContext aVCodecContext, @Const AVFrame aVFrame, IntPointer intPointer, int i, int i2, int i3);

    static {
        Loader.load();
    }

    public C1203x4b136537(Pointer p) {
        super(p);
    }

    protected C1203x4b136537() {
        allocate();
    }
}
