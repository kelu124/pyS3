package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avcodec.AVBSFContext;

public class avcodec$AVBitStreamFilter$Close_AVBSFContext extends FunctionPointer {
    private native void allocate();

    public native void call(AVBSFContext aVBSFContext);

    static {
        Loader.load();
    }

    public avcodec$AVBitStreamFilter$Close_AVBSFContext(Pointer p) {
        super(p);
    }

    protected avcodec$AVBitStreamFilter$Close_AVBSFContext() {
        allocate();
    }
}
