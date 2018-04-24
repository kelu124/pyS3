package org.bytedeco.javacpp;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoOffset;
import org.bytedeco.javacpp.annotation.StdVector;
import org.bytedeco.javacpp.opencv_core.Algorithm;
import org.bytedeco.javacpp.opencv_core.FileStorage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.UMat;
import org.bytedeco.javacpp.opencv_core.UMatVector;
import org.bytedeco.javacpp.presets.opencv_core.Ptr;
import org.bytedeco.javacpp.presets.opencv_core.Str;

public class opencv_face extends org.bytedeco.javacpp.presets.opencv_face {

    @Namespace("cv::face")
    @NoOffset
    public static class FaceRecognizer extends Algorithm {
        @Str
        public native BytePointer getLabelInfo(int i);

        @StdVector
        public native IntBuffer getLabelsByString(@Str String str);

        @StdVector
        public native IntPointer getLabelsByString(@Str BytePointer bytePointer);

        public native double getThreshold();

        public native void load(@Str String str);

        public native void load(@Str BytePointer bytePointer);

        public native void load(@ByRef @Const FileStorage fileStorage);

        public native int predict(@ByVal Mat mat);

        public native int predict(@ByVal UMat uMat);

        public native void predict(@ByVal Mat mat, @ByRef IntBuffer intBuffer, @ByRef DoubleBuffer doubleBuffer);

        public native void predict(@ByVal Mat mat, @ByRef IntPointer intPointer, @ByRef DoublePointer doublePointer);

        public native void predict(@ByVal Mat mat, @Ptr PredictCollector predictCollector);

        public native void predict(@ByVal Mat mat, @Ptr PredictCollector predictCollector, int i);

        public native void predict(@ByVal UMat uMat, @ByRef IntPointer intPointer, @ByRef DoublePointer doublePointer);

        public native void predict(@ByVal UMat uMat, @Ptr PredictCollector predictCollector);

        public native void predict(@ByVal UMat uMat, @Ptr PredictCollector predictCollector, int i);

        public native void predict(@ByVal UMat uMat, @ByRef int[] iArr, @ByRef double[] dArr);

        public native void save(@Str String str);

        public native void save(@Str BytePointer bytePointer);

        public native void save(@ByRef FileStorage fileStorage);

        public native void setLabelInfo(int i, @Str String str);

        public native void setLabelInfo(int i, @Str BytePointer bytePointer);

        public native void train(@ByVal MatVector matVector, @ByVal Mat mat);

        public native void train(@ByVal MatVector matVector, @ByVal UMat uMat);

        public native void train(@ByVal UMatVector uMatVector, @ByVal Mat mat);

        public native void train(@ByVal UMatVector uMatVector, @ByVal UMat uMat);

        public native void update(@ByVal MatVector matVector, @ByVal Mat mat);

        public native void update(@ByVal MatVector matVector, @ByVal UMat uMat);

        public native void update(@ByVal UMatVector uMatVector, @ByVal Mat mat);

        public native void update(@ByVal UMatVector uMatVector, @ByVal UMat uMat);

        static {
            Loader.load();
        }

        public FaceRecognizer(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::face")
    public static class BasicFaceRecognizer extends FaceRecognizer {
        @ByVal
        public native Mat getEigenValues();

        @ByVal
        public native Mat getEigenVectors();

        @ByVal
        public native Mat getLabels();

        @ByVal
        public native Mat getMean();

        public native int getNumComponents();

        @ByVal
        public native MatVector getProjections();

        public native double getThreshold();

        public native void setNumComponents(int i);

        public native void setThreshold(double d);

        static {
            Loader.load();
        }

        public BasicFaceRecognizer(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::face")
    public static class LBPHFaceRecognizer extends FaceRecognizer {
        public native int getGridX();

        public native int getGridY();

        @ByVal
        public native MatVector getHistograms();

        @ByVal
        public native Mat getLabels();

        public native int getNeighbors();

        public native int getRadius();

        public native double getThreshold();

        public native void setGridX(int i);

        public native void setGridY(int i);

        public native void setNeighbors(int i);

        public native void setRadius(int i);

        public native void setThreshold(double d);

        static {
            Loader.load();
        }

        public LBPHFaceRecognizer(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::face")
    @NoOffset
    public static class PredictCollector extends Pointer {
        private native void allocate();

        private native void allocate(double d);

        private native void allocateArray(long j);

        @Cast({"bool"})
        public native boolean emit(int i, double d);

        @Cast({"bool"})
        public native boolean emit(int i, double d, int i2);

        public native void init(int i);

        public native void init(int i, int i2);

        static {
            Loader.load();
        }

        public PredictCollector(Pointer p) {
            super(p);
        }

        public PredictCollector(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public PredictCollector position(long position) {
            return (PredictCollector) super.position(position);
        }

        public PredictCollector(double threshhold) {
            super((Pointer) null);
            allocate(threshhold);
        }

        public PredictCollector() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::face")
    @NoOffset
    public static class MinDistancePredictCollector extends PredictCollector {
        private native void allocate();

        private native void allocate(double d);

        private native void allocateArray(long j);

        @Ptr
        public static native MinDistancePredictCollector create();

        @Ptr
        public static native MinDistancePredictCollector create(double d);

        @Cast({"bool"})
        public native boolean emit(int i, double d);

        @Cast({"bool"})
        public native boolean emit(int i, double d, int i2);

        public native double getDist();

        public native int getLabel();

        static {
            Loader.load();
        }

        public MinDistancePredictCollector(Pointer p) {
            super(p);
        }

        public MinDistancePredictCollector(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public MinDistancePredictCollector position(long position) {
            return (MinDistancePredictCollector) super.position(position);
        }

        public MinDistancePredictCollector(double threshhold) {
            super((Pointer) null);
            allocate(threshhold);
        }

        public MinDistancePredictCollector() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::face")
    @Ptr
    public static native BasicFaceRecognizer createEigenFaceRecognizer();

    @Namespace("cv::face")
    @Ptr
    public static native BasicFaceRecognizer createEigenFaceRecognizer(int i, double d);

    @Namespace("cv::face")
    @Ptr
    public static native BasicFaceRecognizer createFisherFaceRecognizer();

    @Namespace("cv::face")
    @Ptr
    public static native BasicFaceRecognizer createFisherFaceRecognizer(int i, double d);

    @Namespace("cv::face")
    @Ptr
    public static native LBPHFaceRecognizer createLBPHFaceRecognizer();

    @Namespace("cv::face")
    @Ptr
    public static native LBPHFaceRecognizer createLBPHFaceRecognizer(int i, int i2, int i3, int i4, double d);

    static {
        Loader.load();
    }
}
