package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avcodec.AVBSFContext;

public class avcodec$AVBitStreamFilter$Init_AVBSFContext extends FunctionPointer {
    private native void allocate();

    public native int call(AVBSFContext aVBSFContext);

    static {
        Loader.load();
    }

    public avcodec$AVBitStreamFilter$Init_AVBSFContext(Pointer p) {
        super(p);
    }

    protected avcodec$AVBitStreamFilter$Init_AVBSFContext() {
        allocate();
    }
}
