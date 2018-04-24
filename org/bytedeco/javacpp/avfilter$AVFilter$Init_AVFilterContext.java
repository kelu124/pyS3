package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avfilter.AVFilterContext;

public class avfilter$AVFilter$Init_AVFilterContext extends FunctionPointer {
    private native void allocate();

    public native int call(AVFilterContext aVFilterContext);

    static {
        Loader.load();
    }

    public avfilter$AVFilter$Init_AVFilterContext(Pointer p) {
        super(p);
    }

    protected avfilter$AVFilter$Init_AVFilterContext() {
        allocate();
    }
}
