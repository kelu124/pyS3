package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avfilter.AVFilterContext;

public class avfilter$avfilter_action_func extends FunctionPointer {
    private native void allocate();

    public native int call(AVFilterContext aVFilterContext, Pointer pointer, int i, int i2);

    static {
        Loader.load();
    }

    public avfilter$avfilter_action_func(Pointer p) {
        super(p);
    }

    protected avfilter$avfilter_action_func() {
        allocate();
    }
}
