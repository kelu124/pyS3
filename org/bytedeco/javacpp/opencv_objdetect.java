package org.bytedeco.javacpp;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.bytedeco.javacpp.annotation.ByPtrPtr;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoOffset;
import org.bytedeco.javacpp.annotation.Opaque;
import org.bytedeco.javacpp.annotation.StdVector;
import org.bytedeco.javacpp.helper.opencv_core$CvArr;
import org.bytedeco.javacpp.helper.opencv_objdetect.AbstractCvHaarClassifierCascade;
import org.bytedeco.javacpp.opencv_core.Algorithm;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.FileNode;
import org.bytedeco.javacpp.opencv_core.FileStorage;
import org.bytedeco.javacpp.opencv_core.IntIntPair;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.PointVector;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.RectVector;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_core.UMat;
import org.bytedeco.javacpp.presets.opencv_core.Ptr;
import org.bytedeco.javacpp.presets.opencv_core.Str;

public class opencv_objdetect extends org.bytedeco.javacpp.helper.opencv_objdetect {
    public static final int CASCADE_DO_CANNY_PRUNING = 1;
    public static final int CASCADE_DO_ROUGH_SEARCH = 8;
    public static final int CASCADE_FIND_BIGGEST_OBJECT = 4;
    public static final int CASCADE_SCALE_IMAGE = 2;
    public static final int CV_HAAR_DO_CANNY_PRUNING = 1;
    public static final int CV_HAAR_DO_ROUGH_SEARCH = 8;
    public static final int CV_HAAR_FEATURE_MAX = 3;
    public static final int CV_HAAR_FIND_BIGGEST_OBJECT = 4;
    public static final int CV_HAAR_MAGIC_VAL = 1112539136;
    public static final int CV_HAAR_SCALE_IMAGE = 2;
    public static final String CV_TYPE_NAME_HAAR = "opencv-haar-classifier";

    @Namespace("cv")
    public static class BaseCascadeClassifier extends Algorithm {

        public static class MaskGenerator extends Pointer {
            @ByVal
            public native Mat generateMask(@ByRef @Const Mat mat);

            public native void initializeMask(@ByRef @Const Mat mat);

            static {
                Loader.load();
            }

            public MaskGenerator(Pointer p) {
                super(p);
            }
        }

        public native void detectMultiScale(@ByVal Mat mat, @ByRef RectVector rectVector, double d, int i, int i2, @ByVal Size size, @ByVal Size size2);

        public native void detectMultiScale(@ByVal Mat mat, @ByRef RectVector rectVector, @StdVector IntBuffer intBuffer, double d, int i, int i2, @ByVal Size size, @ByVal Size size2);

        public native void detectMultiScale(@ByVal Mat mat, @ByRef RectVector rectVector, @StdVector IntBuffer intBuffer, @StdVector DoubleBuffer doubleBuffer, double d, int i, int i2, @ByVal Size size, @ByVal Size size2, @Cast({"bool"}) boolean z);

        public native void detectMultiScale(@ByVal Mat mat, @ByRef RectVector rectVector, @StdVector IntPointer intPointer, double d, int i, int i2, @ByVal Size size, @ByVal Size size2);

        public native void detectMultiScale(@ByVal Mat mat, @ByRef RectVector rectVector, @StdVector IntPointer intPointer, @StdVector DoublePointer doublePointer, double d, int i, int i2, @ByVal Size size, @ByVal Size size2, @Cast({"bool"}) boolean z);

        public native void detectMultiScale(@ByVal UMat uMat, @ByRef RectVector rectVector, double d, int i, int i2, @ByVal Size size, @ByVal Size size2);

        public native void detectMultiScale(@ByVal UMat uMat, @ByRef RectVector rectVector, @StdVector IntPointer intPointer, double d, int i, int i2, @ByVal Size size, @ByVal Size size2);

        public native void detectMultiScale(@ByVal UMat uMat, @ByRef RectVector rectVector, @StdVector IntPointer intPointer, @StdVector DoublePointer doublePointer, double d, int i, int i2, @ByVal Size size, @ByVal Size size2, @Cast({"bool"}) boolean z);

        public native void detectMultiScale(@ByVal UMat uMat, @ByRef RectVector rectVector, @StdVector int[] iArr, double d, int i, int i2, @ByVal Size size, @ByVal Size size2);

        public native void detectMultiScale(@ByVal UMat uMat, @ByRef RectVector rectVector, @StdVector int[] iArr, @StdVector double[] dArr, double d, int i, int i2, @ByVal Size size, @ByVal Size size2, @Cast({"bool"}) boolean z);

        @Cast({"bool"})
        public native boolean empty();

        public native int getFeatureType();

        @Ptr
        public native MaskGenerator getMaskGenerator();

        public native Pointer getOldCascade();

        @ByVal
        public native Size getOriginalWindowSize();

        @Cast({"bool"})
        public native boolean isOldFormatCascade();

        @Cast({"bool"})
        public native boolean load(@Str String str);

        @Cast({"bool"})
        public native boolean load(@Str BytePointer bytePointer);

        public native void setMaskGenerator(@Ptr MaskGenerator maskGenerator);

        static {
            Loader.load();
        }

        public BaseCascadeClassifier(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class CascadeClassifier extends Pointer {
        private native void allocate();

        private native void allocate(@Str String str);

        private native void allocate(@Str BytePointer bytePointer);

        private native void allocateArray(long j);

        @Cast({"bool"})
        public static native boolean convert(@Str String str, @Str String str2);

        @Cast({"bool"})
        public static native boolean convert(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

        @Ptr
        public native BaseCascadeClassifier cc();

        public native CascadeClassifier cc(BaseCascadeClassifier baseCascadeClassifier);

        public native void detectMultiScale(@ByVal Mat mat, @ByRef RectVector rectVector);

        public native void detectMultiScale(@ByVal Mat mat, @ByRef RectVector rectVector, double d, int i, int i2, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2);

        public native void detectMultiScale(@ByVal UMat uMat, @ByRef RectVector rectVector);

        public native void detectMultiScale(@ByVal UMat uMat, @ByRef RectVector rectVector, double d, int i, int i2, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2);

        @Name({"detectMultiScale"})
        public native void detectMultiScale2(@ByVal Mat mat, @ByRef RectVector rectVector, @StdVector IntBuffer intBuffer);

        @Name({"detectMultiScale"})
        public native void detectMultiScale2(@ByVal Mat mat, @ByRef RectVector rectVector, @StdVector IntBuffer intBuffer, double d, int i, int i2, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2);

        @Name({"detectMultiScale"})
        public native void detectMultiScale2(@ByVal Mat mat, @ByRef RectVector rectVector, @StdVector IntPointer intPointer);

        @Name({"detectMultiScale"})
        public native void detectMultiScale2(@ByVal Mat mat, @ByRef RectVector rectVector, @StdVector IntPointer intPointer, double d, int i, int i2, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2);

        @Name({"detectMultiScale"})
        public native void detectMultiScale2(@ByVal UMat uMat, @ByRef RectVector rectVector, @StdVector IntPointer intPointer);

        @Name({"detectMultiScale"})
        public native void detectMultiScale2(@ByVal UMat uMat, @ByRef RectVector rectVector, @StdVector IntPointer intPointer, double d, int i, int i2, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2);

        @Name({"detectMultiScale"})
        public native void detectMultiScale2(@ByVal UMat uMat, @ByRef RectVector rectVector, @StdVector int[] iArr);

        @Name({"detectMultiScale"})
        public native void detectMultiScale2(@ByVal UMat uMat, @ByRef RectVector rectVector, @StdVector int[] iArr, double d, int i, int i2, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2);

        @Name({"detectMultiScale"})
        public native void detectMultiScale3(@ByVal Mat mat, @ByRef RectVector rectVector, @StdVector IntBuffer intBuffer, @StdVector DoubleBuffer doubleBuffer);

        @Name({"detectMultiScale"})
        public native void detectMultiScale3(@ByVal Mat mat, @ByRef RectVector rectVector, @StdVector IntBuffer intBuffer, @StdVector DoubleBuffer doubleBuffer, double d, int i, int i2, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2, @Cast({"bool"}) boolean z);

        @Name({"detectMultiScale"})
        public native void detectMultiScale3(@ByVal Mat mat, @ByRef RectVector rectVector, @StdVector IntPointer intPointer, @StdVector DoublePointer doublePointer);

        @Name({"detectMultiScale"})
        public native void detectMultiScale3(@ByVal Mat mat, @ByRef RectVector rectVector, @StdVector IntPointer intPointer, @StdVector DoublePointer doublePointer, double d, int i, int i2, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2, @Cast({"bool"}) boolean z);

        @Name({"detectMultiScale"})
        public native void detectMultiScale3(@ByVal UMat uMat, @ByRef RectVector rectVector, @StdVector IntPointer intPointer, @StdVector DoublePointer doublePointer);

        @Name({"detectMultiScale"})
        public native void detectMultiScale3(@ByVal UMat uMat, @ByRef RectVector rectVector, @StdVector IntPointer intPointer, @StdVector DoublePointer doublePointer, double d, int i, int i2, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2, @Cast({"bool"}) boolean z);

        @Name({"detectMultiScale"})
        public native void detectMultiScale3(@ByVal UMat uMat, @ByRef RectVector rectVector, @StdVector int[] iArr, @StdVector double[] dArr);

        @Name({"detectMultiScale"})
        public native void detectMultiScale3(@ByVal UMat uMat, @ByRef RectVector rectVector, @StdVector int[] iArr, @StdVector double[] dArr, double d, int i, int i2, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2, @Cast({"bool"}) boolean z);

        @Cast({"bool"})
        public native boolean empty();

        public native int getFeatureType();

        @Ptr
        public native MaskGenerator getMaskGenerator();

        public native Pointer getOldCascade();

        @ByVal
        public native Size getOriginalWindowSize();

        @Cast({"bool"})
        public native boolean isOldFormatCascade();

        @Cast({"bool"})
        public native boolean load(@Str String str);

        @Cast({"bool"})
        public native boolean load(@Str BytePointer bytePointer);

        @Cast({"bool"})
        public native boolean read(@ByRef @Const FileNode fileNode);

        public native void setMaskGenerator(@Ptr MaskGenerator maskGenerator);

        static {
            Loader.load();
        }

        public CascadeClassifier(Pointer p) {
            super(p);
        }

        public CascadeClassifier(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CascadeClassifier position(long position) {
            return (CascadeClassifier) super.position(position);
        }

        public CascadeClassifier() {
            super((Pointer) null);
            allocate();
        }

        public CascadeClassifier(@Str BytePointer filename) {
            super((Pointer) null);
            allocate(filename);
        }

        public CascadeClassifier(@Str String filename) {
            super((Pointer) null);
            allocate(filename);
        }
    }

    public static class CvAvgComp extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native int neighbors();

        public native CvAvgComp neighbors(int i);

        @ByRef
        public native CvRect rect();

        public native CvAvgComp rect(CvRect cvRect);

        static {
            Loader.load();
        }

        public CvAvgComp() {
            super((Pointer) null);
            allocate();
        }

        public CvAvgComp(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvAvgComp(Pointer p) {
            super(p);
        }

        public CvAvgComp position(long position) {
            return (CvAvgComp) super.position(position);
        }
    }

    public static class CvHaarClassifierCascade extends AbstractCvHaarClassifierCascade {
        private native void allocate();

        private native void allocateArray(long j);

        public native int count();

        public native CvHaarClassifierCascade count(int i);

        public native int flags();

        public native CvHaarClassifierCascade flags(int i);

        public native CvHaarClassifierCascade hid_cascade(CvHidHaarClassifierCascade cvHidHaarClassifierCascade);

        public native CvHidHaarClassifierCascade hid_cascade();

        @ByRef
        public native CvSize orig_window_size();

        public native CvHaarClassifierCascade orig_window_size(CvSize cvSize);

        @ByRef
        public native CvSize real_window_size();

        public native CvHaarClassifierCascade real_window_size(CvSize cvSize);

        public native double scale();

        public native CvHaarClassifierCascade scale(double d);

        public native CvHaarClassifierCascade stage_classifier(CvHaarStageClassifier cvHaarStageClassifier);

        public native CvHaarStageClassifier stage_classifier();

        static {
            Loader.load();
        }

        public CvHaarClassifierCascade() {
            super((Pointer) null);
            allocate();
        }

        public CvHaarClassifierCascade(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvHaarClassifierCascade(Pointer p) {
            super(p);
        }

        public CvHaarClassifierCascade position(long position) {
            return (CvHaarClassifierCascade) super.position(position);
        }
    }

    public static class CvHaarFeature extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @ByRef
        @Name({"rect", ".r"})
        public native CvRect rect_r(int i);

        public native CvHaarFeature rect_r(int i, CvRect cvRect);

        @Name({"rect", ".weight"})
        public native float rect_weight(int i);

        public native CvHaarFeature rect_weight(int i, float f);

        public native int tilted();

        public native CvHaarFeature tilted(int i);

        static {
            Loader.load();
        }

        public CvHaarFeature() {
            super((Pointer) null);
            allocate();
        }

        public CvHaarFeature(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvHaarFeature(Pointer p) {
            super(p);
        }

        public CvHaarFeature position(long position) {
            return (CvHaarFeature) super.position(position);
        }
    }

    @Opaque
    public static class CvHidHaarClassifierCascade extends Pointer {
        public CvHidHaarClassifierCascade() {
            super((Pointer) null);
        }

        public CvHidHaarClassifierCascade(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class DetectionBasedTracker extends Pointer {
        public static final int DETECTED = 1;
        public static final int DETECTED_NOT_SHOWN_YET = 0;
        public static final int DETECTED_TEMPORARY_LOST = 2;
        public static final int WRONG_OBJECT = 3;

        @NoOffset
        public static class ExtObject extends Pointer {
            private native void allocate(int i, @ByVal Rect rect, @Cast({"cv::DetectionBasedTracker::ObjectStatus"}) int i2);

            public native int id();

            public native ExtObject id(int i);

            @ByRef
            public native Rect location();

            public native ExtObject location(Rect rect);

            @Cast({"cv::DetectionBasedTracker::ObjectStatus"})
            public native int status();

            public native ExtObject status(int i);

            static {
                Loader.load();
            }

            public ExtObject(Pointer p) {
                super(p);
            }

            public ExtObject(int _id, @ByVal Rect _location, @Cast({"cv::DetectionBasedTracker::ObjectStatus"}) int _status) {
                super((Pointer) null);
                allocate(_id, _location, _status);
            }
        }

        @NoOffset
        public static class IDetector extends Pointer {
            public native void detect(@ByRef @Const Mat mat, @ByRef RectVector rectVector);

            @ByVal
            public native Size getMaxObjectSize();

            public native int getMinNeighbours();

            @ByVal
            public native Size getMinObjectSize();

            public native float getScaleFactor();

            public native void setMaxObjectSize(@ByRef @Const Size size);

            public native void setMinNeighbours(int i);

            public native void setMinObjectSize(@ByRef @Const Size size);

            public native void setScaleFactor(float f);

            static {
                Loader.load();
            }

            public IDetector(Pointer p) {
                super(p);
            }
        }

        @NoOffset
        public static class Parameters extends Pointer {
            private native void allocate();

            private native void allocateArray(long j);

            public native int maxTrackLifetime();

            public native Parameters maxTrackLifetime(int i);

            public native int minDetectionPeriod();

            public native Parameters minDetectionPeriod(int i);

            static {
                Loader.load();
            }

            public Parameters(Pointer p) {
                super(p);
            }

            public Parameters(long size) {
                super((Pointer) null);
                allocateArray(size);
            }

            public Parameters position(long position) {
                return (Parameters) super.position(position);
            }

            public Parameters() {
                super((Pointer) null);
                allocate();
            }
        }

        private native void allocate(@Ptr IDetector iDetector, @Ptr IDetector iDetector2, @ByRef @Const Parameters parameters);

        public native int addObject(@ByRef @Const Rect rect);

        public native void getObjects(@Cast({"cv::DetectionBasedTracker::Object*"}) @StdVector IntIntPair intIntPair);

        public native void getObjects(@ByRef RectVector rectVector);

        public native void getObjects(@StdVector ExtObject extObject);

        @ByRef
        @Const
        public native Parameters getParameters();

        public native void process(@ByRef @Const Mat mat);

        public native void resetTracking();

        @Cast({"bool"})
        public native boolean run();

        @Cast({"bool"})
        public native boolean setParameters(@ByRef @Const Parameters parameters);

        public native void stop();

        static {
            Loader.load();
        }

        public DetectionBasedTracker(Pointer p) {
            super(p);
        }

        public DetectionBasedTracker(@Ptr IDetector mainDetector, @Ptr IDetector trackingDetector, @ByRef @Const Parameters params) {
            super((Pointer) null);
            allocate(mainDetector, trackingDetector, params);
        }
    }

    @Namespace("cv")
    public static class DetectionROI extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @StdVector
        public native DoublePointer confidences();

        public native DetectionROI confidences(DoublePointer doublePointer);

        @ByRef
        public native PointVector locations();

        public native DetectionROI locations(PointVector pointVector);

        public native double scale();

        public native DetectionROI scale(double d);

        static {
            Loader.load();
        }

        public DetectionROI() {
            super((Pointer) null);
            allocate();
        }

        public DetectionROI(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public DetectionROI(Pointer p) {
            super(p);
        }

        public DetectionROI position(long position) {
            return (DetectionROI) super.position(position);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class HOGDescriptor extends Pointer {
        public static final int DEFAULT_NLEVELS = 64;
        public static final int L2Hys = 0;

        private native void allocate();

        private native void allocate(@Str String str);

        private native void allocate(@Str BytePointer bytePointer);

        private native void allocate(@ByVal Size size, @ByVal Size size2, @ByVal Size size3, @ByVal Size size4, int i);

        private native void allocate(@ByVal Size size, @ByVal Size size2, @ByVal Size size3, @ByVal Size size4, int i, int i2, double d, int i3, double d2, @Cast({"bool"}) boolean z, int i4, @Cast({"bool"}) boolean z2);

        private native void allocate(@ByRef @Const HOGDescriptor hOGDescriptor);

        private native void allocateArray(long j);

        @StdVector
        public static native FloatPointer getDaimlerPeopleDetector();

        @StdVector
        public static native FloatPointer getDefaultPeopleDetector();

        public native double L2HysThreshold();

        public native HOGDescriptor L2HysThreshold(double d);

        @ByRef
        public native Size blockSize();

        public native HOGDescriptor blockSize(Size size);

        @ByRef
        public native Size blockStride();

        public native HOGDescriptor blockStride(Size size);

        @ByRef
        public native Size cellSize();

        public native HOGDescriptor cellSize(Size size);

        @Cast({"bool"})
        public native boolean checkDetectorSize();

        public native void compute(@ByVal Mat mat, @StdVector FloatBuffer floatBuffer);

        public native void compute(@ByVal Mat mat, @StdVector FloatBuffer floatBuffer, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2, @ByRef(nullValue = "std::vector<cv::Point>()") @Const PointVector pointVector);

        public native void compute(@ByVal Mat mat, @StdVector FloatPointer floatPointer);

        public native void compute(@ByVal Mat mat, @StdVector FloatPointer floatPointer, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2, @ByRef(nullValue = "std::vector<cv::Point>()") @Const PointVector pointVector);

        public native void compute(@ByVal UMat uMat, @StdVector FloatPointer floatPointer);

        public native void compute(@ByVal UMat uMat, @StdVector FloatPointer floatPointer, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2, @ByRef(nullValue = "std::vector<cv::Point>()") @Const PointVector pointVector);

        public native void compute(@ByVal UMat uMat, @StdVector float[] fArr);

        public native void compute(@ByVal UMat uMat, @StdVector float[] fArr, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2, @ByRef(nullValue = "std::vector<cv::Point>()") @Const PointVector pointVector);

        public native void computeGradient(@ByRef @Const Mat mat, @ByRef Mat mat2, @ByRef Mat mat3);

        public native void computeGradient(@ByRef @Const Mat mat, @ByRef Mat mat2, @ByRef Mat mat3, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2);

        public native void copyTo(@ByRef HOGDescriptor hOGDescriptor);

        public native int derivAperture();

        public native HOGDescriptor derivAperture(int i);

        public native void detect(@ByRef @Const Mat mat, @ByRef PointVector pointVector);

        public native void detect(@ByRef @Const Mat mat, @ByRef PointVector pointVector, double d, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2, @ByRef(nullValue = "std::vector<cv::Point>()") @Const PointVector pointVector2);

        public native void detect(@ByRef @Const Mat mat, @ByRef PointVector pointVector, @StdVector DoubleBuffer doubleBuffer);

        public native void detect(@ByRef @Const Mat mat, @ByRef PointVector pointVector, @StdVector DoubleBuffer doubleBuffer, double d, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2, @ByRef(nullValue = "std::vector<cv::Point>()") @Const PointVector pointVector2);

        public native void detect(@ByRef @Const Mat mat, @ByRef PointVector pointVector, @StdVector DoublePointer doublePointer);

        public native void detect(@ByRef @Const Mat mat, @ByRef PointVector pointVector, @StdVector DoublePointer doublePointer, double d, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2, @ByRef(nullValue = "std::vector<cv::Point>()") @Const PointVector pointVector2);

        public native void detect(@ByRef @Const Mat mat, @ByRef PointVector pointVector, @StdVector double[] dArr);

        public native void detect(@ByRef @Const Mat mat, @ByRef PointVector pointVector, @StdVector double[] dArr, double d, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2, @ByRef(nullValue = "std::vector<cv::Point>()") @Const PointVector pointVector2);

        public native void detectMultiScale(@ByVal Mat mat, @ByRef RectVector rectVector);

        public native void detectMultiScale(@ByVal Mat mat, @ByRef RectVector rectVector, double d, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2, double d2, double d3, @Cast({"bool"}) boolean z);

        public native void detectMultiScale(@ByVal Mat mat, @ByRef RectVector rectVector, @StdVector DoubleBuffer doubleBuffer);

        public native void detectMultiScale(@ByVal Mat mat, @ByRef RectVector rectVector, @StdVector DoubleBuffer doubleBuffer, double d, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2, double d2, double d3, @Cast({"bool"}) boolean z);

        public native void detectMultiScale(@ByVal Mat mat, @ByRef RectVector rectVector, @StdVector DoublePointer doublePointer);

        public native void detectMultiScale(@ByVal Mat mat, @ByRef RectVector rectVector, @StdVector DoublePointer doublePointer, double d, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2, double d2, double d3, @Cast({"bool"}) boolean z);

        public native void detectMultiScale(@ByVal UMat uMat, @ByRef RectVector rectVector);

        public native void detectMultiScale(@ByVal UMat uMat, @ByRef RectVector rectVector, double d, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2, double d2, double d3, @Cast({"bool"}) boolean z);

        public native void detectMultiScale(@ByVal UMat uMat, @ByRef RectVector rectVector, @StdVector DoublePointer doublePointer);

        public native void detectMultiScale(@ByVal UMat uMat, @ByRef RectVector rectVector, @StdVector DoublePointer doublePointer, double d, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2, double d2, double d3, @Cast({"bool"}) boolean z);

        public native void detectMultiScale(@ByVal UMat uMat, @ByRef RectVector rectVector, @StdVector double[] dArr);

        public native void detectMultiScale(@ByVal UMat uMat, @ByRef RectVector rectVector, @StdVector double[] dArr, double d, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2, double d2, double d3, @Cast({"bool"}) boolean z);

        public native void detectMultiScaleROI(@ByRef @Const Mat mat, @ByRef RectVector rectVector, @StdVector DetectionROI detectionROI);

        public native void detectMultiScaleROI(@ByRef @Const Mat mat, @ByRef RectVector rectVector, @StdVector DetectionROI detectionROI, double d, int i);

        public native void detectROI(@ByRef @Const Mat mat, @ByRef @Const PointVector pointVector, @ByRef PointVector pointVector2, @StdVector DoubleBuffer doubleBuffer);

        public native void detectROI(@ByRef @Const Mat mat, @ByRef @Const PointVector pointVector, @ByRef PointVector pointVector2, @StdVector DoubleBuffer doubleBuffer, double d, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2);

        public native void detectROI(@ByRef @Const Mat mat, @ByRef @Const PointVector pointVector, @ByRef PointVector pointVector2, @StdVector DoublePointer doublePointer);

        public native void detectROI(@ByRef @Const Mat mat, @ByRef @Const PointVector pointVector, @ByRef PointVector pointVector2, @StdVector DoublePointer doublePointer, double d, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2);

        public native void detectROI(@ByRef @Const Mat mat, @ByRef @Const PointVector pointVector, @ByRef PointVector pointVector2, @StdVector double[] dArr);

        public native void detectROI(@ByRef @Const Mat mat, @ByRef @Const PointVector pointVector, @ByRef PointVector pointVector2, @StdVector double[] dArr, double d, @ByVal(nullValue = "cv::Size()") Size size, @ByVal(nullValue = "cv::Size()") Size size2);

        public native float free_coef();

        public native HOGDescriptor free_coef(float f);

        public native HOGDescriptor gammaCorrection(boolean z);

        @Cast({"bool"})
        public native boolean gammaCorrection();

        @Cast({"size_t"})
        public native long getDescriptorSize();

        public native double getWinSigma();

        public native void groupRectangles(@ByRef RectVector rectVector, @StdVector DoubleBuffer doubleBuffer, int i, double d);

        public native void groupRectangles(@ByRef RectVector rectVector, @StdVector DoublePointer doublePointer, int i, double d);

        public native void groupRectangles(@ByRef RectVector rectVector, @StdVector double[] dArr, int i, double d);

        public native int histogramNormType();

        public native HOGDescriptor histogramNormType(int i);

        @Cast({"bool"})
        public native boolean load(@Str String str);

        @Cast({"bool"})
        public native boolean load(@Str String str, @Str String str2);

        @Cast({"bool"})
        public native boolean load(@Str BytePointer bytePointer);

        @Cast({"bool"})
        public native boolean load(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

        public native int nbins();

        public native HOGDescriptor nbins(int i);

        public native int nlevels();

        public native HOGDescriptor nlevels(int i);

        @ByRef
        public native UMat oclSvmDetector();

        public native HOGDescriptor oclSvmDetector(UMat uMat);

        @Cast({"bool"})
        public native boolean read(@ByRef FileNode fileNode);

        public native void readALTModel(@Str String str);

        public native void readALTModel(@Str BytePointer bytePointer);

        public native void save(@Str String str);

        public native void save(@Str String str, @Str String str2);

        public native void save(@Str BytePointer bytePointer);

        public native void save(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

        public native void setSVMDetector(@ByVal Mat mat);

        public native void setSVMDetector(@ByVal UMat uMat);

        public native HOGDescriptor signedGradient(boolean z);

        @Cast({"bool"})
        public native boolean signedGradient();

        @StdVector
        public native FloatPointer svmDetector();

        public native HOGDescriptor svmDetector(FloatPointer floatPointer);

        public native double winSigma();

        public native HOGDescriptor winSigma(double d);

        @ByRef
        public native Size winSize();

        public native HOGDescriptor winSize(Size size);

        public native void write(@ByRef FileStorage fileStorage, @Str String str);

        public native void write(@ByRef FileStorage fileStorage, @Str BytePointer bytePointer);

        static {
            Loader.load();
        }

        public HOGDescriptor(Pointer p) {
            super(p);
        }

        public HOGDescriptor(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public HOGDescriptor position(long position) {
            return (HOGDescriptor) super.position(position);
        }

        public HOGDescriptor() {
            super((Pointer) null);
            allocate();
        }

        public HOGDescriptor(@ByVal Size _winSize, @ByVal Size _blockSize, @ByVal Size _blockStride, @ByVal Size _cellSize, int _nbins, int _derivAperture, double _winSigma, int _histogramNormType, double _L2HysThreshold, @Cast({"bool"}) boolean _gammaCorrection, int _nlevels, @Cast({"bool"}) boolean _signedGradient) {
            super((Pointer) null);
            allocate(_winSize, _blockSize, _blockStride, _cellSize, _nbins, _derivAperture, _winSigma, _histogramNormType, _L2HysThreshold, _gammaCorrection, _nlevels, _signedGradient);
        }

        public HOGDescriptor(@ByVal Size _winSize, @ByVal Size _blockSize, @ByVal Size _blockStride, @ByVal Size _cellSize, int _nbins) {
            super((Pointer) null);
            allocate(_winSize, _blockSize, _blockStride, _cellSize, _nbins);
        }

        public HOGDescriptor(@Str BytePointer filename) {
            super((Pointer) null);
            allocate(filename);
        }

        public HOGDescriptor(@Str String filename) {
            super((Pointer) null);
            allocate(filename);
        }

        public HOGDescriptor(@ByRef @Const HOGDescriptor d) {
            super((Pointer) null);
            allocate(d);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class SimilarRects extends Pointer {
        private native void allocate(double d);

        @Cast({"bool"})
        @Name({"operator ()"})
        public native boolean apply(@ByRef @Const Rect rect, @ByRef @Const Rect rect2);

        public native double eps();

        public native SimilarRects eps(double d);

        static {
            Loader.load();
        }

        public SimilarRects(Pointer p) {
            super(p);
        }

        public SimilarRects(double _eps) {
            super((Pointer) null);
            allocate(_eps);
        }
    }

    @Namespace("cv")
    @Ptr
    public static native MaskGenerator createFaceDetectionMaskGenerator();

    public static native CvSeq cvHaarDetectObjects(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvHaarClassifierCascade cvHaarClassifierCascade, CvMemStorage cvMemStorage);

    public static native CvSeq cvHaarDetectObjects(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvHaarClassifierCascade cvHaarClassifierCascade, CvMemStorage cvMemStorage, double d, int i, int i2, @ByVal(nullValue = "CvSize(cvSize(0,0))") CvSize cvSize, @ByVal(nullValue = "CvSize(cvSize(0,0))") CvSize cvSize2);

    public static native CvSeq cvHaarDetectObjectsForROC(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvHaarClassifierCascade cvHaarClassifierCascade, CvMemStorage cvMemStorage, @StdVector IntBuffer intBuffer, @StdVector DoubleBuffer doubleBuffer);

    public static native CvSeq cvHaarDetectObjectsForROC(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvHaarClassifierCascade cvHaarClassifierCascade, CvMemStorage cvMemStorage, @StdVector IntBuffer intBuffer, @StdVector DoubleBuffer doubleBuffer, double d, int i, int i2, @ByVal(nullValue = "CvSize(cvSize(0, 0))") CvSize cvSize, @ByVal(nullValue = "CvSize(cvSize(0, 0))") CvSize cvSize2, @Cast({"bool"}) boolean z);

    public static native CvSeq cvHaarDetectObjectsForROC(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvHaarClassifierCascade cvHaarClassifierCascade, CvMemStorage cvMemStorage, @StdVector IntPointer intPointer, @StdVector DoublePointer doublePointer);

    public static native CvSeq cvHaarDetectObjectsForROC(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvHaarClassifierCascade cvHaarClassifierCascade, CvMemStorage cvMemStorage, @StdVector IntPointer intPointer, @StdVector DoublePointer doublePointer, double d, int i, int i2, @ByVal(nullValue = "CvSize(cvSize(0, 0))") CvSize cvSize, @ByVal(nullValue = "CvSize(cvSize(0, 0))") CvSize cvSize2, @Cast({"bool"}) boolean z);

    public static native CvSeq cvHaarDetectObjectsForROC(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvHaarClassifierCascade cvHaarClassifierCascade, CvMemStorage cvMemStorage, @StdVector int[] iArr, @StdVector double[] dArr);

    public static native CvSeq cvHaarDetectObjectsForROC(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, CvHaarClassifierCascade cvHaarClassifierCascade, CvMemStorage cvMemStorage, @StdVector int[] iArr, @StdVector double[] dArr, double d, int i, int i2, @ByVal(nullValue = "CvSize(cvSize(0, 0))") CvSize cvSize, @ByVal(nullValue = "CvSize(cvSize(0, 0))") CvSize cvSize2, @Cast({"bool"}) boolean z);

    public static native CvHaarClassifierCascade cvLoadHaarClassifierCascade(String str, @ByVal CvSize cvSize);

    public static native CvHaarClassifierCascade cvLoadHaarClassifierCascade(@Cast({"const char*"}) BytePointer bytePointer, @ByVal CvSize cvSize);

    public static native void cvReleaseHaarClassifierCascade(@Cast({"CvHaarClassifierCascade**"}) PointerPointer pointerPointer);

    public static native void cvReleaseHaarClassifierCascade(@ByPtrPtr CvHaarClassifierCascade cvHaarClassifierCascade);

    public static native int cvRunHaarClassifierCascade(@Const CvHaarClassifierCascade cvHaarClassifierCascade, @Cast({"CvPoint*"}) @ByVal IntBuffer intBuffer);

    public static native int cvRunHaarClassifierCascade(@Const CvHaarClassifierCascade cvHaarClassifierCascade, @Cast({"CvPoint*"}) @ByVal IntBuffer intBuffer, int i);

    public static native int cvRunHaarClassifierCascade(@Const CvHaarClassifierCascade cvHaarClassifierCascade, @ByVal CvPoint cvPoint);

    public static native int cvRunHaarClassifierCascade(@Const CvHaarClassifierCascade cvHaarClassifierCascade, @ByVal CvPoint cvPoint, int i);

    public static native int cvRunHaarClassifierCascade(@Const CvHaarClassifierCascade cvHaarClassifierCascade, @Cast({"CvPoint*"}) @ByVal int[] iArr);

    public static native int cvRunHaarClassifierCascade(@Const CvHaarClassifierCascade cvHaarClassifierCascade, @Cast({"CvPoint*"}) @ByVal int[] iArr, int i);

    public static native void cvSetImagesForHaarClassifierCascade(CvHaarClassifierCascade cvHaarClassifierCascade, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, double d);

    @Namespace("cv")
    public static native void groupRectangles(@ByRef RectVector rectVector, int i);

    @Namespace("cv")
    public static native void groupRectangles(@ByRef RectVector rectVector, int i, double d);

    @Namespace("cv")
    public static native void groupRectangles(@ByRef RectVector rectVector, int i, double d, @StdVector IntBuffer intBuffer, @StdVector DoubleBuffer doubleBuffer);

    @Namespace("cv")
    public static native void groupRectangles(@ByRef RectVector rectVector, int i, double d, @StdVector IntPointer intPointer, @StdVector DoublePointer doublePointer);

    @Namespace("cv")
    public static native void groupRectangles(@ByRef RectVector rectVector, int i, double d, @StdVector int[] iArr, @StdVector double[] dArr);

    @Namespace("cv")
    public static native void groupRectangles(@ByRef RectVector rectVector, @StdVector IntBuffer intBuffer, int i);

    @Namespace("cv")
    public static native void groupRectangles(@ByRef RectVector rectVector, @StdVector IntBuffer intBuffer, int i, double d);

    @Namespace("cv")
    public static native void groupRectangles(@ByRef RectVector rectVector, @StdVector IntBuffer intBuffer, @StdVector DoubleBuffer doubleBuffer, int i);

    @Namespace("cv")
    public static native void groupRectangles(@ByRef RectVector rectVector, @StdVector IntBuffer intBuffer, @StdVector DoubleBuffer doubleBuffer, int i, double d);

    @Namespace("cv")
    public static native void groupRectangles(@ByRef RectVector rectVector, @StdVector IntPointer intPointer, int i);

    @Namespace("cv")
    public static native void groupRectangles(@ByRef RectVector rectVector, @StdVector IntPointer intPointer, int i, double d);

    @Namespace("cv")
    public static native void groupRectangles(@ByRef RectVector rectVector, @StdVector IntPointer intPointer, @StdVector DoublePointer doublePointer, int i);

    @Namespace("cv")
    public static native void groupRectangles(@ByRef RectVector rectVector, @StdVector IntPointer intPointer, @StdVector DoublePointer doublePointer, int i, double d);

    @Namespace("cv")
    public static native void groupRectangles(@ByRef RectVector rectVector, @StdVector int[] iArr, int i);

    @Namespace("cv")
    public static native void groupRectangles(@ByRef RectVector rectVector, @StdVector int[] iArr, int i, double d);

    @Namespace("cv")
    public static native void groupRectangles(@ByRef RectVector rectVector, @StdVector int[] iArr, @StdVector double[] dArr, int i);

    @Namespace("cv")
    public static native void groupRectangles(@ByRef RectVector rectVector, @StdVector int[] iArr, @StdVector double[] dArr, int i, double d);

    @Namespace("cv")
    public static native void groupRectangles_meanshift(@ByRef RectVector rectVector, @StdVector DoubleBuffer doubleBuffer, @StdVector DoubleBuffer doubleBuffer2);

    @Namespace("cv")
    public static native void groupRectangles_meanshift(@ByRef RectVector rectVector, @StdVector DoubleBuffer doubleBuffer, @StdVector DoubleBuffer doubleBuffer2, double d, @ByVal(nullValue = "cv::Size(64, 128)") Size size);

    @Namespace("cv")
    public static native void groupRectangles_meanshift(@ByRef RectVector rectVector, @StdVector DoublePointer doublePointer, @StdVector DoublePointer doublePointer2);

    @Namespace("cv")
    public static native void groupRectangles_meanshift(@ByRef RectVector rectVector, @StdVector DoublePointer doublePointer, @StdVector DoublePointer doublePointer2, double d, @ByVal(nullValue = "cv::Size(64, 128)") Size size);

    @Namespace("cv")
    public static native void groupRectangles_meanshift(@ByRef RectVector rectVector, @StdVector double[] dArr, @StdVector double[] dArr2);

    @Namespace("cv")
    public static native void groupRectangles_meanshift(@ByRef RectVector rectVector, @StdVector double[] dArr, @StdVector double[] dArr2, double d, @ByVal(nullValue = "cv::Size(64, 128)") Size size);

    static {
        Loader.load();
    }
}
