package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Index;
import org.bytedeco.javacpp.annotation.MemberGetter;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.NoOffset;

@Name({"std::map<libfreenect2::Frame::Type,libfreenect2::Frame*>"})
public class freenect2$FrameMap extends Pointer {

    @Name({"iterator"})
    @NoOffset
    public static class Iterator extends Pointer {
        @Name({"operator=="})
        public native boolean equals(@ByRef Iterator iterator);

        @MemberGetter
        @Cast({"libfreenect2::Frame::Type"})
        @Name({"operator*().first"})
        public native int first();

        @ByRef
        @Name({"operator++"})
        public native Iterator increment();

        @MemberGetter
        @Name({"operator*().second"})
        public native freenect2$Frame second();

        public Iterator(Pointer p) {
            super(p);
        }
    }

    private native void allocate();

    @ByVal
    public native Iterator begin();

    @ByVal
    public native Iterator end();

    @Index
    public native freenect2$Frame get(@Cast({"libfreenect2::Frame::Type"}) int i);

    public native freenect2$FrameMap put(@Cast({"libfreenect2::Frame::Type"}) int i, freenect2$Frame org_bytedeco_javacpp_freenect2_Frame);

    @ByRef
    @Name({"operator="})
    public native freenect2$FrameMap put(@ByRef freenect2$FrameMap org_bytedeco_javacpp_freenect2_FrameMap);

    public native long size();

    static {
        Loader.load();
    }

    public freenect2$FrameMap(Pointer p) {
        super(p);
    }

    public freenect2$FrameMap() {
        allocate();
    }
}
