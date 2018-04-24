package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.MemberGetter;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.StdString;

@Namespace("libfreenect2")
public class freenect2$Freenect2Device extends Pointer {
    public static final int ProductId = ProductId();
    public static final int ProductIdPreview = ProductIdPreview();
    public static final int VendorId = VendorId();

    public static class Config extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native Config EnableBilateralFilter(boolean z);

        @Cast({"bool"})
        public native boolean EnableBilateralFilter();

        public native Config EnableEdgeAwareFilter(boolean z);

        @Cast({"bool"})
        public native boolean EnableEdgeAwareFilter();

        public native float MaxDepth();

        public native Config MaxDepth(float f);

        public native float MinDepth();

        public native Config MinDepth(float f);

        static {
            Loader.load();
        }

        public Config() {
            super((Pointer) null);
            allocate();
        }

        public Config(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Config(Pointer p) {
            super(p);
        }

        public Config position(long position) {
            return (Config) super.position(position);
        }
    }

    @MemberGetter
    @Cast({"const unsigned int"})
    public static native int ProductId();

    @MemberGetter
    @Cast({"const unsigned int"})
    public static native int ProductIdPreview();

    @MemberGetter
    @Cast({"const unsigned int"})
    public static native int VendorId();

    @Cast({"bool"})
    @Name({"close"})
    public native boolean _close();

    @ByVal
    public native ColorCameraParams getColorCameraParams();

    @StdString
    public native BytePointer getFirmwareVersion();

    @ByVal
    public native IrCameraParams getIrCameraParams();

    @StdString
    public native BytePointer getSerialNumber();

    public native void setColorCameraParams(@ByRef @Const ColorCameraParams colorCameraParams);

    public native void setColorFrameListener(freenect2$FrameListener org_bytedeco_javacpp_freenect2_FrameListener);

    public native void setConfiguration(@ByRef @Const Config config);

    public native void setIrAndDepthFrameListener(freenect2$FrameListener org_bytedeco_javacpp_freenect2_FrameListener);

    public native void setIrCameraParams(@ByRef @Const IrCameraParams irCameraParams);

    @Cast({"bool"})
    public native boolean start();

    @Cast({"bool"})
    public native boolean startStreams(@Cast({"bool"}) boolean z, @Cast({"bool"}) boolean z2);

    @Cast({"bool"})
    public native boolean stop();

    static {
        Loader.load();
    }

    public freenect2$Freenect2Device(Pointer p) {
        super(p);
    }
}
