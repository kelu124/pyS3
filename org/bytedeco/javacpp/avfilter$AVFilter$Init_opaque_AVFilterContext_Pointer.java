package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avfilter.AVFilterContext;

public class avfilter$AVFilter$Init_opaque_AVFilterContext_Pointer extends FunctionPointer {
    private native void allocate();

    public native int call(AVFilterContext aVFilterContext, Pointer pointer);

    static {
        Loader.load();
    }

    public avfilter$AVFilter$Init_opaque_AVFilterContext_Pointer(Pointer p) {
        super(p);
    }

    protected avfilter$AVFilter$Init_opaque_AVFilterContext_Pointer() {
        allocate();
    }
}
