package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoOffset;
import org.bytedeco.javacpp.opencv_core.Algorithm;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.UMat;
import org.bytedeco.javacpp.presets.opencv_core.Ptr;
import org.bytedeco.javacpp.presets.opencv_core.Str;

public class opencv_superres extends org.bytedeco.javacpp.presets.opencv_superres {

    @Namespace("cv::superres")
    public static class DenseOpticalFlowExt extends Algorithm {
        public native void calc(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

        public native void calc(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat4);

        public native void calc(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

        public native void calc(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat4);

        public native void collectGarbage();

        static {
            Loader.load();
        }

        public DenseOpticalFlowExt(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::superres")
    public static class BroxOpticalFlow extends DenseOpticalFlowExt {
        public native double getAlpha();

        public native double getGamma();

        public native int getInnerIterations();

        public native int getOuterIterations();

        public native double getScaleFactor();

        public native int getSolverIterations();

        public native void setAlpha(double d);

        public native void setGamma(double d);

        public native void setInnerIterations(int i);

        public native void setOuterIterations(int i);

        public native void setScaleFactor(double d);

        public native void setSolverIterations(int i);

        static {
            Loader.load();
        }

        public BroxOpticalFlow(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::superres")
    public static class FarnebackOpticalFlow extends DenseOpticalFlowExt {
        public native int getFlags();

        public native int getIterations();

        public native int getLevelsNumber();

        public native int getPolyN();

        public native double getPolySigma();

        public native double getPyrScale();

        public native int getWindowSize();

        public native void setFlags(int i);

        public native void setIterations(int i);

        public native void setLevelsNumber(int i);

        public native void setPolyN(int i);

        public native void setPolySigma(double d);

        public native void setPyrScale(double d);

        public native void setWindowSize(int i);

        static {
            Loader.load();
        }

        public FarnebackOpticalFlow(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::superres")
    public static class FrameSource extends Pointer {
        public native void nextFrame(@ByVal Mat mat);

        public native void nextFrame(@ByVal UMat uMat);

        public native void reset();

        static {
            Loader.load();
        }

        public FrameSource(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::superres")
    public static class PyrLKOpticalFlow extends DenseOpticalFlowExt {
        public native int getIterations();

        public native int getMaxLevel();

        public native int getWindowSize();

        public native void setIterations(int i);

        public native void setMaxLevel(int i);

        public native void setWindowSize(int i);

        static {
            Loader.load();
        }

        public PyrLKOpticalFlow(Pointer p) {
            super(p);
        }
    }

    @Name({"cv::superres::DualTVL1OpticalFlow"})
    public static class SuperResDualTVL1OpticalFlow extends DenseOpticalFlowExt {
        public native double getEpsilon();

        public native int getIterations();

        public native double getLambda();

        public native int getScalesNumber();

        public native double getTau();

        public native double getTheta();

        @Cast({"bool"})
        public native boolean getUseInitialFlow();

        public native int getWarpingsNumber();

        public native void setEpsilon(double d);

        public native void setIterations(int i);

        public native void setLambda(double d);

        public native void setScalesNumber(int i);

        public native void setTau(double d);

        public native void setTheta(double d);

        public native void setUseInitialFlow(@Cast({"bool"}) boolean z);

        public native void setWarpingsNumber(int i);

        static {
            Loader.load();
        }

        public SuperResDualTVL1OpticalFlow(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::superres")
    @NoOffset
    public static class SuperResolution extends Algorithm {
        @Namespace
        @Name({"static_cast<cv::superres::FrameSource*>"})
        public static native FrameSource asFrameSource(SuperResolution superResolution);

        public native void collectGarbage();

        public native double getAlpha();

        public native int getBlurKernelSize();

        public native double getBlurSigma();

        public native int getIterations();

        public native int getKernelSize();

        public native double getLabmda();

        @Ptr
        public native DenseOpticalFlowExt getOpticalFlow();

        public native int getScale();

        public native double getTau();

        public native int getTemporalAreaRadius();

        public native void nextFrame(@ByVal Mat mat);

        public native void nextFrame(@ByVal UMat uMat);

        public native void reset();

        public native void setAlpha(double d);

        public native void setBlurKernelSize(int i);

        public native void setBlurSigma(double d);

        public native void setInput(@Ptr FrameSource frameSource);

        public native void setIterations(int i);

        public native void setKernelSize(int i);

        public native void setLabmda(double d);

        public native void setOpticalFlow(@Ptr DenseOpticalFlowExt denseOpticalFlowExt);

        public native void setScale(int i);

        public native void setTau(double d);

        public native void setTemporalAreaRadius(int i);

        static {
            Loader.load();
        }

        public SuperResolution(Pointer p) {
            super(p);
        }

        public FrameSource asFrameSource() {
            return asFrameSource(this);
        }
    }

    @Namespace("cv::superres")
    @Ptr
    public static native FrameSource createFrameSource_Camera();

    @Namespace("cv::superres")
    @Ptr
    public static native FrameSource createFrameSource_Camera(int i);

    @Namespace("cv::superres")
    @Ptr
    public static native FrameSource createFrameSource_Empty();

    @Namespace("cv::superres")
    @Ptr
    public static native FrameSource createFrameSource_Video(@Str String str);

    @Namespace("cv::superres")
    @Ptr
    public static native FrameSource createFrameSource_Video(@Str BytePointer bytePointer);

    @Namespace("cv::superres")
    @Ptr
    public static native FrameSource createFrameSource_Video_CUDA(@Str String str);

    @Namespace("cv::superres")
    @Ptr
    public static native FrameSource createFrameSource_Video_CUDA(@Str BytePointer bytePointer);

    @Namespace("cv::superres")
    @Ptr
    public static native BroxOpticalFlow createOptFlow_Brox_CUDA();

    @Namespace("cv::superres")
    @Ptr
    public static native SuperResDualTVL1OpticalFlow createOptFlow_DualTVL1();

    @Namespace("cv::superres")
    @Ptr
    public static native SuperResDualTVL1OpticalFlow createOptFlow_DualTVL1_CUDA();

    @Namespace("cv::superres")
    @Ptr
    public static native FarnebackOpticalFlow createOptFlow_Farneback();

    @Namespace("cv::superres")
    @Ptr
    public static native FarnebackOpticalFlow createOptFlow_Farneback_CUDA();

    @Namespace("cv::superres")
    @Ptr
    public static native PyrLKOpticalFlow createOptFlow_PyrLK_CUDA();

    @Namespace("cv::superres")
    @Ptr
    public static native SuperResolution createSuperResolution_BTVL1();

    @Namespace("cv::superres")
    @Ptr
    public static native SuperResolution createSuperResolution_BTVL1_CUDA();

    static {
        Loader.load();
    }
}
