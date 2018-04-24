package org.bytedeco.javacpp;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoOffset;
import org.bytedeco.javacpp.freenect2$Freenect2Device.ColorCameraParams;
import org.bytedeco.javacpp.freenect2$Freenect2Device.IrCameraParams;

@Namespace("libfreenect2")
@NoOffset
public class freenect2$Registration extends Pointer {
    private native void allocate(@ByVal IrCameraParams irCameraParams, @ByVal ColorCameraParams colorCameraParams);

    public native void apply(int i, int i2, float f, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

    public native void apply(int i, int i2, float f, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

    public native void apply(int i, int i2, float f, @ByRef float[] fArr, @ByRef float[] fArr2);

    public native void apply(@Const freenect2$Frame org_bytedeco_javacpp_freenect2_Frame, @Const freenect2$Frame org_bytedeco_javacpp_freenect2_Frame2, freenect2$Frame org_bytedeco_javacpp_freenect2_Frame3, freenect2$Frame org_bytedeco_javacpp_freenect2_Frame4);

    public native void apply(@Const freenect2$Frame org_bytedeco_javacpp_freenect2_Frame, @Const freenect2$Frame org_bytedeco_javacpp_freenect2_Frame2, freenect2$Frame org_bytedeco_javacpp_freenect2_Frame3, freenect2$Frame org_bytedeco_javacpp_freenect2_Frame4, @Cast({"const bool"}) boolean z, freenect2$Frame org_bytedeco_javacpp_freenect2_Frame5, IntBuffer intBuffer);

    public native void apply(@Const freenect2$Frame org_bytedeco_javacpp_freenect2_Frame, @Const freenect2$Frame org_bytedeco_javacpp_freenect2_Frame2, freenect2$Frame org_bytedeco_javacpp_freenect2_Frame3, freenect2$Frame org_bytedeco_javacpp_freenect2_Frame4, @Cast({"const bool"}) boolean z, freenect2$Frame org_bytedeco_javacpp_freenect2_Frame5, IntPointer intPointer);

    public native void apply(@Const freenect2$Frame org_bytedeco_javacpp_freenect2_Frame, @Const freenect2$Frame org_bytedeco_javacpp_freenect2_Frame2, freenect2$Frame org_bytedeco_javacpp_freenect2_Frame3, freenect2$Frame org_bytedeco_javacpp_freenect2_Frame4, @Cast({"const bool"}) boolean z, freenect2$Frame org_bytedeco_javacpp_freenect2_Frame5, int[] iArr);

    public native void getPointXYZ(@Const freenect2$Frame org_bytedeco_javacpp_freenect2_Frame, int i, int i2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2, @ByRef FloatBuffer floatBuffer3);

    public native void getPointXYZ(@Const freenect2$Frame org_bytedeco_javacpp_freenect2_Frame, int i, int i2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2, @ByRef FloatPointer floatPointer3);

    public native void getPointXYZ(@Const freenect2$Frame org_bytedeco_javacpp_freenect2_Frame, int i, int i2, @ByRef float[] fArr, @ByRef float[] fArr2, @ByRef float[] fArr3);

    public native void getPointXYZRGB(@Const freenect2$Frame org_bytedeco_javacpp_freenect2_Frame, @Const freenect2$Frame org_bytedeco_javacpp_freenect2_Frame2, int i, int i2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2, @ByRef FloatBuffer floatBuffer3, @ByRef FloatBuffer floatBuffer4);

    public native void getPointXYZRGB(@Const freenect2$Frame org_bytedeco_javacpp_freenect2_Frame, @Const freenect2$Frame org_bytedeco_javacpp_freenect2_Frame2, int i, int i2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2, @ByRef FloatPointer floatPointer3, @ByRef FloatPointer floatPointer4);

    public native void getPointXYZRGB(@Const freenect2$Frame org_bytedeco_javacpp_freenect2_Frame, @Const freenect2$Frame org_bytedeco_javacpp_freenect2_Frame2, int i, int i2, @ByRef float[] fArr, @ByRef float[] fArr2, @ByRef float[] fArr3, @ByRef float[] fArr4);

    public native void undistortDepth(@Const freenect2$Frame org_bytedeco_javacpp_freenect2_Frame, freenect2$Frame org_bytedeco_javacpp_freenect2_Frame2);

    static {
        Loader.load();
    }

    public freenect2$Registration(Pointer p) {
        super(p);
    }

    public freenect2$Registration(@ByVal IrCameraParams depth_p, @ByVal ColorCameraParams rgb_p) {
        super((Pointer) null);
        allocate(depth_p, rgb_p);
    }
}
