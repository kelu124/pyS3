package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avfilter.AVFilterContext;

public class avfilter$AVFilter$Uninit_AVFilterContext extends FunctionPointer {
    private native void allocate();

    public native void call(AVFilterContext aVFilterContext);

    static {
        Loader.load();
    }

    public avfilter$AVFilter$Uninit_AVFilterContext(Pointer p) {
        super(p);
    }

    protected avfilter$AVFilter$Uninit_AVFilterContext() {
        allocate();
    }
}
