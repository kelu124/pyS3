package org.bytedeco.javacpp;

import org.bytedeco.javacpp.avfilter.AVFilterContext;

public class avfilter$avfilter_execute_func extends FunctionPointer {
    private native void allocate();

    public native int call(AVFilterContext aVFilterContext, avfilter$avfilter_action_func org_bytedeco_javacpp_avfilter_avfilter_action_func, Pointer pointer, IntPointer intPointer, int i);

    static {
        Loader.load();
    }

    public avfilter$avfilter_execute_func(Pointer p) {
        super(p);
    }

    protected avfilter$avfilter_execute_func() {
        allocate();
    }
}
