package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.avfilter.AVFilterContext;

public class avfilter$AVFilter$Init_dict_AVFilterContext_PointerPointer extends FunctionPointer {
    private native void allocate();

    public native int call(AVFilterContext aVFilterContext, @Cast({"AVDictionary**"}) PointerPointer pointerPointer);

    static {
        Loader.load();
    }

    public avfilter$AVFilter$Init_dict_AVFilterContext_PointerPointer(Pointer p) {
        super(p);
    }

    protected avfilter$AVFilter$Init_dict_AVFilterContext_PointerPointer() {
        allocate();
    }
}
