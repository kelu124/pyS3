package org.bytedeco.javacpp;

import java.nio.FloatBuffer;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.opencv_core.Algorithm;

public class opencv_ml$SVM$Kernel extends Algorithm {
    public native void calc(int i, int i2, @Const FloatBuffer floatBuffer, @Const FloatBuffer floatBuffer2, FloatBuffer floatBuffer3);

    public native void calc(int i, int i2, @Const FloatPointer floatPointer, @Const FloatPointer floatPointer2, FloatPointer floatPointer3);

    public native void calc(int i, int i2, @Const float[] fArr, @Const float[] fArr2, float[] fArr3);

    public native int getType();

    static {
        Loader.load();
    }

    public opencv_ml$SVM$Kernel(Pointer p) {
        super(p);
    }
}
