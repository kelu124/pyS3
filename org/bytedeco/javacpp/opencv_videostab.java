package org.bytedeco.javacpp;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoOffset;
import org.bytedeco.javacpp.annotation.StdVector;
import org.bytedeco.javacpp.annotation.Virtual;
import org.bytedeco.javacpp.opencv_core.IntIntPair;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_core.UMat;
import org.bytedeco.javacpp.opencv_features2d.Feature2D;
import org.bytedeco.javacpp.presets.opencv_core.Ptr;
import org.bytedeco.javacpp.presets.opencv_core.Str;

public class opencv_videostab extends org.bytedeco.javacpp.presets.opencv_videostab {
    public static final int MM_AFFINE = 5;
    public static final int MM_HOMOGRAPHY = 6;
    public static final int MM_RIGID = 3;
    public static final int MM_ROTATION = 2;
    public static final int MM_SIMILARITY = 4;
    public static final int MM_TRANSLATION = 0;
    public static final int MM_TRANSLATION_AND_SCALE = 1;
    public static final int MM_UNKNOWN = 7;

    @Namespace("cv::videostab")
    @NoOffset
    public static class InpainterBase extends Pointer {
        @ByRef
        @Const
        public native MatVector frames();

        public native void inpaint(int i, @ByRef Mat mat, @ByRef Mat mat2);

        @Cast({"cv::videostab::MotionModel"})
        public native int motionModel();

        @ByRef
        @Const
        public native MatVector motions();

        public native int radius();

        public native void setFrames(@ByRef @Const MatVector matVector);

        public native void setMotionModel(@Cast({"cv::videostab::MotionModel"}) int i);

        public native void setMotions(@ByRef @Const MatVector matVector);

        public native void setRadius(int i);

        public native void setStabilizationMotions(@ByRef @Const MatVector matVector);

        public native void setStabilizedFrames(@ByRef @Const MatVector matVector);

        @ByRef
        @Const
        public native MatVector stabilizationMotions();

        @ByRef
        @Const
        public native MatVector stabilizedFrames();

        static {
            Loader.load();
        }

        public InpainterBase(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class ColorAverageInpainter extends InpainterBase {
        private native void allocate();

        private native void allocateArray(long j);

        public native void inpaint(int i, @ByRef Mat mat, @ByRef Mat mat2);

        static {
            Loader.load();
        }

        public ColorAverageInpainter() {
            super((Pointer) null);
            allocate();
        }

        public ColorAverageInpainter(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public ColorAverageInpainter(Pointer p) {
            super(p);
        }

        public ColorAverageInpainter position(long position) {
            return (ColorAverageInpainter) super.position(position);
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class ColorInpainter extends InpainterBase {
        private native void allocate();

        private native void allocate(int i, double d);

        private native void allocateArray(long j);

        public native void inpaint(int i, @ByRef Mat mat, @ByRef Mat mat2);

        static {
            Loader.load();
        }

        public ColorInpainter(Pointer p) {
            super(p);
        }

        public ColorInpainter(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public ColorInpainter position(long position) {
            return (ColorInpainter) super.position(position);
        }

        public ColorInpainter(int method, double radius) {
            super((Pointer) null);
            allocate(method, radius);
        }

        public ColorInpainter() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class ConsistentMosaicInpainter extends InpainterBase {
        private native void allocate();

        private native void allocateArray(long j);

        public native void inpaint(int i, @ByRef Mat mat, @ByRef Mat mat2);

        public native void setStdevThresh(float f);

        public native float stdevThresh();

        static {
            Loader.load();
        }

        public ConsistentMosaicInpainter(Pointer p) {
            super(p);
        }

        public ConsistentMosaicInpainter(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public ConsistentMosaicInpainter position(long position) {
            return (ConsistentMosaicInpainter) super.position(position);
        }

        public ConsistentMosaicInpainter() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class DeblurerBase extends Pointer {
        @StdVector
        public native FloatPointer blurrinessRates();

        public native void deblur(int i, @ByRef Mat mat);

        @ByRef
        @Const
        public native MatVector frames();

        @ByRef
        @Const
        public native MatVector motions();

        public native int radius();

        public native void setBlurrinessRates(@StdVector FloatBuffer floatBuffer);

        public native void setBlurrinessRates(@StdVector FloatPointer floatPointer);

        public native void setBlurrinessRates(@StdVector float[] fArr);

        public native void setFrames(@ByRef @Const MatVector matVector);

        public native void setMotions(@ByRef @Const MatVector matVector);

        public native void setRadius(int i);

        static {
            Loader.load();
        }

        public DeblurerBase(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class FastMarchingMethod extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @ByVal
        public native Mat distanceMap();

        static {
            Loader.load();
        }

        public FastMarchingMethod(Pointer p) {
            super(p);
        }

        public FastMarchingMethod(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FastMarchingMethod position(long position) {
            return (FastMarchingMethod) super.position(position);
        }

        public FastMarchingMethod() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class ImageMotionEstimatorBase extends Pointer {
        @ByVal
        public native Mat estimate(@ByRef @Const Mat mat, @ByRef @Const Mat mat2);

        @ByVal
        public native Mat estimate(@ByRef @Const Mat mat, @ByRef @Const Mat mat2, @Cast({"bool*"}) BoolPointer boolPointer);

        @ByVal
        public native Mat estimate(@ByRef @Const Mat mat, @ByRef @Const Mat mat2, @Cast({"bool*"}) boolean[] zArr);

        @Cast({"cv::videostab::MotionModel"})
        public native int motionModel();

        public native void setMotionModel(@Cast({"cv::videostab::MotionModel"}) int i);

        static {
            Loader.load();
        }

        public ImageMotionEstimatorBase(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class FromFileMotionReader extends ImageMotionEstimatorBase {
        private native void allocate(@Str String str);

        private native void allocate(@Str BytePointer bytePointer);

        @ByVal
        public native Mat estimate(@ByRef @Const Mat mat, @ByRef @Const Mat mat2);

        @ByVal
        public native Mat estimate(@ByRef @Const Mat mat, @ByRef @Const Mat mat2, @Cast({"bool*"}) BoolPointer boolPointer);

        @ByVal
        public native Mat estimate(@ByRef @Const Mat mat, @ByRef @Const Mat mat2, @Cast({"bool*"}) boolean[] zArr);

        static {
            Loader.load();
        }

        public FromFileMotionReader(Pointer p) {
            super(p);
        }

        public FromFileMotionReader(@Str BytePointer path) {
            super((Pointer) null);
            allocate(path);
        }

        public FromFileMotionReader(@Str String path) {
            super((Pointer) null);
            allocate(path);
        }
    }

    @Namespace("cv::videostab")
    public static class IMotionStabilizer extends Pointer {
        public native void stabilize(int i, @ByRef @Const MatVector matVector, @ByVal IntIntPair intIntPair, Mat mat);

        static {
            Loader.load();
        }

        public IMotionStabilizer(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::videostab")
    public static class MotionFilterBase extends IMotionStabilizer {
        @ByVal
        public native Mat stabilize(int i, @ByRef @Const MatVector matVector, @ByVal IntIntPair intIntPair);

        public native void stabilize(int i, @ByRef @Const MatVector matVector, @ByVal IntIntPair intIntPair, Mat mat);

        static {
            Loader.load();
        }

        public MotionFilterBase(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class GaussianMotionFilter extends MotionFilterBase {
        private native void allocate();

        private native void allocate(int i, float f);

        private native void allocateArray(long j);

        public native int radius();

        public native void setParams(int i);

        public native void setParams(int i, float f);

        @ByVal
        public native Mat stabilize(int i, @ByRef @Const MatVector matVector, @ByVal IntIntPair intIntPair);

        public native float stdev();

        static {
            Loader.load();
        }

        public GaussianMotionFilter(Pointer p) {
            super(p);
        }

        public GaussianMotionFilter(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public GaussianMotionFilter position(long position) {
            return (GaussianMotionFilter) super.position(position);
        }

        public GaussianMotionFilter(int radius, float stdev) {
            super((Pointer) null);
            allocate(radius, stdev);
        }

        public GaussianMotionFilter() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::videostab")
    public static class IDenseOptFlowEstimator extends Pointer {
        public native void run(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5);

        public native void run(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5);

        static {
            Loader.load();
        }

        public IDenseOptFlowEstimator(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::videostab")
    public static class IFrameSource extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Virtual(true)
        @ByVal
        public native Mat nextFrame();

        @Virtual(true)
        public native void reset();

        static {
            Loader.load();
        }

        public IFrameSource() {
            super((Pointer) null);
            allocate();
        }

        public IFrameSource(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public IFrameSource(Pointer p) {
            super(p);
        }

        public IFrameSource position(long position) {
            return (IFrameSource) super.position(position);
        }
    }

    @Namespace("cv::videostab")
    public static class ILog extends Pointer {
        public native void print(String str);

        public native void print(@Cast({"const char*"}) BytePointer bytePointer);

        static {
            Loader.load();
        }

        public ILog(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::videostab")
    public static class IOutlierRejector extends Pointer {
        public native void process(@ByVal Size size, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

        public native void process(@ByVal Size size, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

        static {
            Loader.load();
        }

        public IOutlierRejector(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::videostab")
    public static class ISparseOptFlowEstimator extends Pointer {
        public native void run(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5, @ByVal Mat mat6);

        public native void run(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5, @ByVal UMat uMat6);

        static {
            Loader.load();
        }

        public ISparseOptFlowEstimator(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::videostab")
    public static class InpaintingPipeline extends InpainterBase {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"bool"})
        public native boolean empty();

        public native void inpaint(int i, @ByRef Mat mat, @ByRef Mat mat2);

        public native void pushBack(@Ptr InpainterBase inpainterBase);

        public native void setFrames(@ByRef @Const MatVector matVector);

        public native void setMotionModel(@Cast({"cv::videostab::MotionModel"}) int i);

        public native void setMotions(@ByRef @Const MatVector matVector);

        public native void setRadius(int i);

        public native void setStabilizationMotions(@ByRef @Const MatVector matVector);

        public native void setStabilizedFrames(@ByRef @Const MatVector matVector);

        static {
            Loader.load();
        }

        public InpaintingPipeline() {
            super((Pointer) null);
            allocate();
        }

        public InpaintingPipeline(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public InpaintingPipeline(Pointer p) {
            super(p);
        }

        public InpaintingPipeline position(long position) {
            return (InpaintingPipeline) super.position(position);
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class KeypointBasedMotionEstimator extends ImageMotionEstimatorBase {
        private native void allocate(@Ptr MotionEstimatorBase motionEstimatorBase);

        @Ptr
        @Cast({"cv::FeatureDetector*"})
        public native Feature2D detector();

        @ByVal
        public native Mat estimate(@ByRef @Const Mat mat, @ByRef @Const Mat mat2);

        @ByVal
        public native Mat estimate(@ByRef @Const Mat mat, @ByRef @Const Mat mat2, @Cast({"bool*"}) BoolPointer boolPointer);

        @ByVal
        public native Mat estimate(@ByRef @Const Mat mat, @ByRef @Const Mat mat2, @Cast({"bool*"}) boolean[] zArr);

        @Cast({"cv::videostab::MotionModel"})
        public native int motionModel();

        @Ptr
        public native ISparseOptFlowEstimator opticalFlowEstimator();

        @Ptr
        public native IOutlierRejector outlierRejector();

        public native void setDetector(@Ptr @Cast({"cv::FeatureDetector*"}) Feature2D feature2D);

        public native void setMotionModel(@Cast({"cv::videostab::MotionModel"}) int i);

        public native void setOpticalFlowEstimator(@Ptr ISparseOptFlowEstimator iSparseOptFlowEstimator);

        public native void setOutlierRejector(@Ptr IOutlierRejector iOutlierRejector);

        static {
            Loader.load();
        }

        public KeypointBasedMotionEstimator(Pointer p) {
            super(p);
        }

        public KeypointBasedMotionEstimator(@Ptr MotionEstimatorBase estimator) {
            super((Pointer) null);
            allocate(estimator);
        }
    }

    @Namespace("cv::videostab")
    public static class LogToStdout extends ILog {
        private native void allocate();

        private native void allocateArray(long j);

        public native void print(String str);

        public native void print(@Cast({"const char*"}) BytePointer bytePointer);

        static {
            Loader.load();
        }

        public LogToStdout() {
            super((Pointer) null);
            allocate();
        }

        public LogToStdout(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public LogToStdout(Pointer p) {
            super(p);
        }

        public LogToStdout position(long position) {
            return (LogToStdout) super.position(position);
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class LpMotionStabilizer extends IMotionStabilizer {
        private native void allocate();

        private native void allocate(@Cast({"cv::videostab::MotionModel"}) int i);

        private native void allocateArray(long j);

        @ByVal
        public native Size frameSize();

        @Cast({"cv::videostab::MotionModel"})
        public native int motionModel();

        public native void setFrameSize(@ByVal Size size);

        public native void setMotionModel(@Cast({"cv::videostab::MotionModel"}) int i);

        public native void setTrimRatio(float f);

        public native void setWeight1(float f);

        public native void setWeight2(float f);

        public native void setWeight3(float f);

        public native void setWeight4(float f);

        public native void stabilize(int i, @ByRef @Const MatVector matVector, @ByVal IntIntPair intIntPair, Mat mat);

        public native float trimRatio();

        public native float weight1();

        public native float weight2();

        public native float weight3();

        public native float weight4();

        static {
            Loader.load();
        }

        public LpMotionStabilizer(Pointer p) {
            super(p);
        }

        public LpMotionStabilizer(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public LpMotionStabilizer position(long position) {
            return (LpMotionStabilizer) super.position(position);
        }

        public LpMotionStabilizer(@Cast({"cv::videostab::MotionModel"}) int model) {
            super((Pointer) null);
            allocate(model);
        }

        public LpMotionStabilizer() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class WobbleSuppressorBase extends Pointer {
        public native int frameCount();

        @Ptr
        public native ImageMotionEstimatorBase motionEstimator();

        @ByRef
        @Const
        public native MatVector motions();

        @ByRef
        @Const
        public native MatVector motions2();

        public native void setFrameCount(int i);

        public native void setMotionEstimator(@Ptr ImageMotionEstimatorBase imageMotionEstimatorBase);

        public native void setMotions(@ByRef @Const MatVector matVector);

        public native void setMotions2(@ByRef @Const MatVector matVector);

        public native void setStabilizationMotions(@ByRef @Const MatVector matVector);

        @ByRef
        @Const
        public native MatVector stabilizationMotions();

        public native void suppress(int i, @ByRef @Const Mat mat, @ByRef Mat mat2);

        static {
            Loader.load();
        }

        public WobbleSuppressorBase(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class MoreAccurateMotionWobbleSuppressorBase extends WobbleSuppressorBase {
        public native int period();

        public native void setPeriod(int i);

        static {
            Loader.load();
        }

        public MoreAccurateMotionWobbleSuppressorBase(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::videostab")
    public static class MoreAccurateMotionWobbleSuppressor extends MoreAccurateMotionWobbleSuppressorBase {
        private native void allocate();

        private native void allocateArray(long j);

        public native void suppress(int i, @ByRef @Const Mat mat, @ByRef Mat mat2);

        static {
            Loader.load();
        }

        public MoreAccurateMotionWobbleSuppressor() {
            super((Pointer) null);
            allocate();
        }

        public MoreAccurateMotionWobbleSuppressor(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public MoreAccurateMotionWobbleSuppressor(Pointer p) {
            super(p);
        }

        public MoreAccurateMotionWobbleSuppressor position(long position) {
            return (MoreAccurateMotionWobbleSuppressor) super.position(position);
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class MotionEstimatorBase extends Pointer {
        @ByVal
        public native Mat estimate(@ByVal Mat mat, @ByVal Mat mat2);

        @ByVal
        public native Mat estimate(@ByVal Mat mat, @ByVal Mat mat2, @Cast({"bool*"}) BoolPointer boolPointer);

        @ByVal
        public native Mat estimate(@ByVal Mat mat, @ByVal Mat mat2, @Cast({"bool*"}) boolean[] zArr);

        @ByVal
        public native Mat estimate(@ByVal UMat uMat, @ByVal UMat uMat2);

        @ByVal
        public native Mat estimate(@ByVal UMat uMat, @ByVal UMat uMat2, @Cast({"bool*"}) BoolPointer boolPointer);

        @ByVal
        public native Mat estimate(@ByVal UMat uMat, @ByVal UMat uMat2, @Cast({"bool*"}) boolean[] zArr);

        @Cast({"cv::videostab::MotionModel"})
        public native int motionModel();

        public native void setMotionModel(@Cast({"cv::videostab::MotionModel"}) int i);

        static {
            Loader.load();
        }

        public MotionEstimatorBase(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class MotionEstimatorL1 extends MotionEstimatorBase {
        private native void allocate();

        private native void allocate(@Cast({"cv::videostab::MotionModel"}) int i);

        private native void allocateArray(long j);

        @ByVal
        public native Mat estimate(@ByVal Mat mat, @ByVal Mat mat2);

        @ByVal
        public native Mat estimate(@ByVal Mat mat, @ByVal Mat mat2, @Cast({"bool*"}) BoolPointer boolPointer);

        @ByVal
        public native Mat estimate(@ByVal Mat mat, @ByVal Mat mat2, @Cast({"bool*"}) boolean[] zArr);

        @ByVal
        public native Mat estimate(@ByVal UMat uMat, @ByVal UMat uMat2);

        @ByVal
        public native Mat estimate(@ByVal UMat uMat, @ByVal UMat uMat2, @Cast({"bool*"}) BoolPointer boolPointer);

        @ByVal
        public native Mat estimate(@ByVal UMat uMat, @ByVal UMat uMat2, @Cast({"bool*"}) boolean[] zArr);

        static {
            Loader.load();
        }

        public MotionEstimatorL1(Pointer p) {
            super(p);
        }

        public MotionEstimatorL1(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public MotionEstimatorL1 position(long position) {
            return (MotionEstimatorL1) super.position(position);
        }

        public MotionEstimatorL1(@Cast({"cv::videostab::MotionModel"}) int model) {
            super((Pointer) null);
            allocate(model);
        }

        public MotionEstimatorL1() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class MotionEstimatorRansacL2 extends MotionEstimatorBase {
        private native void allocate();

        private native void allocate(@Cast({"cv::videostab::MotionModel"}) int i);

        private native void allocateArray(long j);

        @ByVal
        public native Mat estimate(@ByVal Mat mat, @ByVal Mat mat2);

        @ByVal
        public native Mat estimate(@ByVal Mat mat, @ByVal Mat mat2, @Cast({"bool*"}) BoolPointer boolPointer);

        @ByVal
        public native Mat estimate(@ByVal Mat mat, @ByVal Mat mat2, @Cast({"bool*"}) boolean[] zArr);

        @ByVal
        public native Mat estimate(@ByVal UMat uMat, @ByVal UMat uMat2);

        @ByVal
        public native Mat estimate(@ByVal UMat uMat, @ByVal UMat uMat2, @Cast({"bool*"}) BoolPointer boolPointer);

        @ByVal
        public native Mat estimate(@ByVal UMat uMat, @ByVal UMat uMat2, @Cast({"bool*"}) boolean[] zArr);

        public native float minInlierRatio();

        @ByVal
        public native RansacParams ransacParams();

        public native void setMinInlierRatio(float f);

        public native void setRansacParams(@ByRef @Const RansacParams ransacParams);

        static {
            Loader.load();
        }

        public MotionEstimatorRansacL2(Pointer p) {
            super(p);
        }

        public MotionEstimatorRansacL2(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public MotionEstimatorRansacL2 position(long position) {
            return (MotionEstimatorRansacL2) super.position(position);
        }

        public MotionEstimatorRansacL2(@Cast({"cv::videostab::MotionModel"}) int model) {
            super((Pointer) null);
            allocate(model);
        }

        public MotionEstimatorRansacL2() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class MotionInpainter extends InpainterBase {
        private native void allocate();

        private native void allocateArray(long j);

        public native int borderMode();

        public native float distThresh();

        public native float flowErrorThreshold();

        public native void inpaint(int i, @ByRef Mat mat, @ByRef Mat mat2);

        @Ptr
        public native IDenseOptFlowEstimator optFlowEstimator();

        public native void setBorderMode(int i);

        public native void setDistThreshold(float f);

        public native void setFlowErrorThreshold(float f);

        public native void setOptFlowEstimator(@Ptr IDenseOptFlowEstimator iDenseOptFlowEstimator);

        static {
            Loader.load();
        }

        public MotionInpainter(Pointer p) {
            super(p);
        }

        public MotionInpainter(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public MotionInpainter position(long position) {
            return (MotionInpainter) super.position(position);
        }

        public MotionInpainter() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::videostab")
    public static class MotionStabilizationPipeline extends IMotionStabilizer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"bool"})
        public native boolean empty();

        public native void pushBack(@Ptr IMotionStabilizer iMotionStabilizer);

        public native void stabilize(int i, @ByRef @Const MatVector matVector, @ByVal IntIntPair intIntPair, Mat mat);

        static {
            Loader.load();
        }

        public MotionStabilizationPipeline() {
            super((Pointer) null);
            allocate();
        }

        public MotionStabilizationPipeline(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public MotionStabilizationPipeline(Pointer p) {
            super(p);
        }

        public MotionStabilizationPipeline position(long position) {
            return (MotionStabilizationPipeline) super.position(position);
        }
    }

    @Namespace("cv::videostab")
    public static class NullDeblurer extends DeblurerBase {
        private native void allocate();

        private native void allocateArray(long j);

        public native void deblur(int i, @ByRef Mat mat);

        static {
            Loader.load();
        }

        public NullDeblurer() {
            super((Pointer) null);
            allocate();
        }

        public NullDeblurer(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public NullDeblurer(Pointer p) {
            super(p);
        }

        public NullDeblurer position(long position) {
            return (NullDeblurer) super.position(position);
        }
    }

    @Namespace("cv::videostab")
    public static class NullFrameSource extends IFrameSource {
        private native void allocate();

        private native void allocateArray(long j);

        @ByVal
        public native Mat nextFrame();

        public native void reset();

        static {
            Loader.load();
        }

        public NullFrameSource() {
            super((Pointer) null);
            allocate();
        }

        public NullFrameSource(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public NullFrameSource(Pointer p) {
            super(p);
        }

        public NullFrameSource position(long position) {
            return (NullFrameSource) super.position(position);
        }
    }

    @Namespace("cv::videostab")
    public static class NullInpainter extends InpainterBase {
        private native void allocate();

        private native void allocateArray(long j);

        public native void inpaint(int i, @ByRef Mat mat, @ByRef Mat mat2);

        static {
            Loader.load();
        }

        public NullInpainter() {
            super((Pointer) null);
            allocate();
        }

        public NullInpainter(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public NullInpainter(Pointer p) {
            super(p);
        }

        public NullInpainter position(long position) {
            return (NullInpainter) super.position(position);
        }
    }

    @Namespace("cv::videostab")
    public static class NullLog extends ILog {
        private native void allocate();

        private native void allocateArray(long j);

        public native void print(String str);

        public native void print(@Cast({"const char*"}) BytePointer bytePointer);

        static {
            Loader.load();
        }

        public NullLog() {
            super((Pointer) null);
            allocate();
        }

        public NullLog(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public NullLog(Pointer p) {
            super(p);
        }

        public NullLog position(long position) {
            return (NullLog) super.position(position);
        }
    }

    @Namespace("cv::videostab")
    public static class NullOutlierRejector extends IOutlierRejector {
        private native void allocate();

        private native void allocateArray(long j);

        public native void process(@ByVal Size size, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

        public native void process(@ByVal Size size, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

        static {
            Loader.load();
        }

        public NullOutlierRejector() {
            super((Pointer) null);
            allocate();
        }

        public NullOutlierRejector(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public NullOutlierRejector(Pointer p) {
            super(p);
        }

        public NullOutlierRejector position(long position) {
            return (NullOutlierRejector) super.position(position);
        }
    }

    @Namespace("cv::videostab")
    public static class NullWobbleSuppressor extends WobbleSuppressorBase {
        private native void allocate();

        private native void allocateArray(long j);

        public native void suppress(int i, @ByRef @Const Mat mat, @ByRef Mat mat2);

        static {
            Loader.load();
        }

        public NullWobbleSuppressor() {
            super((Pointer) null);
            allocate();
        }

        public NullWobbleSuppressor(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public NullWobbleSuppressor(Pointer p) {
            super(p);
        }

        public NullWobbleSuppressor position(long position) {
            return (NullWobbleSuppressor) super.position(position);
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class StabilizerBase extends Pointer {
        public native int borderMode();

        @Ptr
        public native DeblurerBase deblurrer();

        @Cast({"bool"})
        public native boolean doCorrectionForInclusion();

        @Ptr
        public native IFrameSource frameSource();

        @Ptr
        public native InpainterBase inpainter();

        @Ptr
        public native ILog log();

        @Ptr
        public native ImageMotionEstimatorBase motionEstimator();

        public native int radius();

        public native void setBorderMode(int i);

        public native void setCorrectionForInclusion(@Cast({"bool"}) boolean z);

        public native void setDeblurer(@Ptr DeblurerBase deblurerBase);

        public native void setFrameSource(@Ptr IFrameSource iFrameSource);

        public native void setInpainter(@Ptr InpainterBase inpainterBase);

        public native void setLog(@Ptr ILog iLog);

        public native void setMotionEstimator(@Ptr ImageMotionEstimatorBase imageMotionEstimatorBase);

        public native void setRadius(int i);

        public native void setTrimRatio(float f);

        public native float trimRatio();

        static {
            Loader.load();
        }

        public StabilizerBase(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class OnePassStabilizer extends StabilizerBase {
        private native void allocate();

        private native void allocateArray(long j);

        @Namespace
        @Name({"static_cast<cv::videostab::IFrameSource*>"})
        public static native IFrameSource asIFrameSource(OnePassStabilizer onePassStabilizer);

        @Ptr
        public native MotionFilterBase motionFilter();

        @ByVal
        public native Mat nextFrame();

        public native void reset();

        public native void setMotionFilter(@Ptr MotionFilterBase motionFilterBase);

        static {
            Loader.load();
        }

        public OnePassStabilizer(Pointer p) {
            super(p);
        }

        public OnePassStabilizer(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public OnePassStabilizer position(long position) {
            return (OnePassStabilizer) super.position(position);
        }

        public IFrameSource asIFrameSource() {
            return asIFrameSource(this);
        }

        public OnePassStabilizer() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class PyrLkOptFlowEstimatorBase extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native int maxLevel();

        public native void setMaxLevel(int i);

        public native void setWinSize(@ByVal Size size);

        @ByVal
        public native Size winSize();

        static {
            Loader.load();
        }

        public PyrLkOptFlowEstimatorBase(Pointer p) {
            super(p);
        }

        public PyrLkOptFlowEstimatorBase(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public PyrLkOptFlowEstimatorBase position(long position) {
            return (PyrLkOptFlowEstimatorBase) super.position(position);
        }

        public PyrLkOptFlowEstimatorBase() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class RansacParams extends Pointer {
        private native void allocate();

        private native void allocate(int i, float f, float f2, float f3);

        private native void allocateArray(long j);

        @ByVal
        public static native RansacParams default2dMotion(@Cast({"cv::videostab::MotionModel"}) int i);

        public native float eps();

        public native RansacParams eps(float f);

        public native int niters();

        public native float prob();

        public native RansacParams prob(float f);

        public native int size();

        public native RansacParams size(int i);

        public native float thresh();

        public native RansacParams thresh(float f);

        static {
            Loader.load();
        }

        public RansacParams(Pointer p) {
            super(p);
        }

        public RansacParams(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public RansacParams position(long position) {
            return (RansacParams) super.position(position);
        }

        public RansacParams() {
            super((Pointer) null);
            allocate();
        }

        public RansacParams(int size, float thresh, float eps, float prob) {
            super((Pointer) null);
            allocate(size, thresh, eps, prob);
        }
    }

    @Namespace("cv::videostab")
    public static class SparsePyrLkOptFlowEstimator extends PyrLkOptFlowEstimatorBase {
        private native void allocate();

        private native void allocateArray(long j);

        @Namespace
        @Name({"static_cast<cv::videostab::ISparseOptFlowEstimator*>"})
        public static native ISparseOptFlowEstimator asISparseOptFlowEstimator(SparsePyrLkOptFlowEstimator sparsePyrLkOptFlowEstimator);

        public native void run(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5, @ByVal Mat mat6);

        public native void run(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5, @ByVal UMat uMat6);

        static {
            Loader.load();
        }

        public SparsePyrLkOptFlowEstimator() {
            super((Pointer) null);
            allocate();
        }

        public SparsePyrLkOptFlowEstimator(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public SparsePyrLkOptFlowEstimator(Pointer p) {
            super(p);
        }

        public SparsePyrLkOptFlowEstimator position(long position) {
            return (SparsePyrLkOptFlowEstimator) super.position(position);
        }

        public ISparseOptFlowEstimator asISparseOptFlowEstimator() {
            return asISparseOptFlowEstimator(this);
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class ToFileMotionWriter extends ImageMotionEstimatorBase {
        private native void allocate(@Str String str, @Ptr ImageMotionEstimatorBase imageMotionEstimatorBase);

        private native void allocate(@Str BytePointer bytePointer, @Ptr ImageMotionEstimatorBase imageMotionEstimatorBase);

        @ByVal
        public native Mat estimate(@ByRef @Const Mat mat, @ByRef @Const Mat mat2);

        @ByVal
        public native Mat estimate(@ByRef @Const Mat mat, @ByRef @Const Mat mat2, @Cast({"bool*"}) BoolPointer boolPointer);

        @ByVal
        public native Mat estimate(@ByRef @Const Mat mat, @ByRef @Const Mat mat2, @Cast({"bool*"}) boolean[] zArr);

        @Cast({"cv::videostab::MotionModel"})
        public native int motionModel();

        public native void setMotionModel(@Cast({"cv::videostab::MotionModel"}) int i);

        static {
            Loader.load();
        }

        public ToFileMotionWriter(Pointer p) {
            super(p);
        }

        public ToFileMotionWriter(@Str BytePointer path, @Ptr ImageMotionEstimatorBase estimator) {
            super((Pointer) null);
            allocate(path, estimator);
        }

        public ToFileMotionWriter(@Str String path, @Ptr ImageMotionEstimatorBase estimator) {
            super((Pointer) null);
            allocate(path, estimator);
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class TranslationBasedLocalOutlierRejector extends IOutlierRejector {
        private native void allocate();

        private native void allocateArray(long j);

        @ByVal
        public native Size cellSize();

        public native void process(@ByVal Size size, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

        public native void process(@ByVal Size size, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

        @ByVal
        public native RansacParams ransacParams();

        public native void setCellSize(@ByVal Size size);

        public native void setRansacParams(@ByVal RansacParams ransacParams);

        static {
            Loader.load();
        }

        public TranslationBasedLocalOutlierRejector(Pointer p) {
            super(p);
        }

        public TranslationBasedLocalOutlierRejector(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public TranslationBasedLocalOutlierRejector position(long position) {
            return (TranslationBasedLocalOutlierRejector) super.position(position);
        }

        public TranslationBasedLocalOutlierRejector() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class TwoPassStabilizer extends StabilizerBase {
        private native void allocate();

        private native void allocateArray(long j);

        @Namespace
        @Name({"static_cast<cv::videostab::IFrameSource*>"})
        public static native IFrameSource asIFrameSource(TwoPassStabilizer twoPassStabilizer);

        @Ptr
        public native IMotionStabilizer motionStabilizer();

        @Cast({"bool"})
        public native boolean mustEstimateTrimaRatio();

        @ByVal
        public native Mat nextFrame();

        public native void reset();

        public native void setEstimateTrimRatio(@Cast({"bool"}) boolean z);

        public native void setMotionStabilizer(@Ptr IMotionStabilizer iMotionStabilizer);

        public native void setWobbleSuppressor(@Ptr WobbleSuppressorBase wobbleSuppressorBase);

        @Ptr
        public native WobbleSuppressorBase wobbleSuppressor();

        static {
            Loader.load();
        }

        public TwoPassStabilizer(Pointer p) {
            super(p);
        }

        public TwoPassStabilizer(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public TwoPassStabilizer position(long position) {
            return (TwoPassStabilizer) super.position(position);
        }

        public IFrameSource asIFrameSource() {
            return asIFrameSource(this);
        }

        public TwoPassStabilizer() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class VideoFileSource extends IFrameSource {
        private native void allocate(@Str String str);

        private native void allocate(@Str String str, @Cast({"bool"}) boolean z);

        private native void allocate(@Str BytePointer bytePointer);

        private native void allocate(@Str BytePointer bytePointer, @Cast({"bool"}) boolean z);

        public native int count();

        public native double fps();

        public native int height();

        @ByVal
        public native Mat nextFrame();

        public native void reset();

        public native int width();

        static {
            Loader.load();
        }

        public VideoFileSource(Pointer p) {
            super(p);
        }

        public VideoFileSource(@Str BytePointer path, @Cast({"bool"}) boolean volatileFrame) {
            super((Pointer) null);
            allocate(path, volatileFrame);
        }

        public VideoFileSource(@Str BytePointer path) {
            super((Pointer) null);
            allocate(path);
        }

        public VideoFileSource(@Str String path, @Cast({"bool"}) boolean volatileFrame) {
            super((Pointer) null);
            allocate(path, volatileFrame);
        }

        public VideoFileSource(@Str String path) {
            super((Pointer) null);
            allocate(path);
        }
    }

    @Namespace("cv::videostab")
    @NoOffset
    public static class WeightingDeblurer extends DeblurerBase {
        private native void allocate();

        private native void allocateArray(long j);

        public native void deblur(int i, @ByRef Mat mat);

        public native float sensitivity();

        public native void setSensitivity(float f);

        static {
            Loader.load();
        }

        public WeightingDeblurer(Pointer p) {
            super(p);
        }

        public WeightingDeblurer(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public WeightingDeblurer position(long position) {
            return (WeightingDeblurer) super.position(position);
        }

        public WeightingDeblurer() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::videostab")
    public static native float calcBlurriness(@ByRef @Const Mat mat);

    @Namespace("cv::videostab")
    public static native void calcFlowMask(@ByRef @Const Mat mat, @ByRef @Const Mat mat2, @ByRef @Const Mat mat3, float f, @ByRef @Const Mat mat4, @ByRef @Const Mat mat5, @ByRef Mat mat6);

    @Namespace("cv::videostab")
    public static native void completeFrameAccordingToFlow(@ByRef @Const Mat mat, @ByRef @Const Mat mat2, @ByRef @Const Mat mat3, @ByRef @Const Mat mat4, @ByRef @Const Mat mat5, float f, @ByRef Mat mat6, @ByRef Mat mat7);

    @Namespace("cv::videostab")
    @ByVal
    public static native Mat ensureInclusionConstraint(@ByRef @Const Mat mat, @ByVal Size size, float f);

    @Namespace("cv::videostab")
    @ByVal
    public static native Mat estimateGlobalMotionLeastSquares(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv::videostab")
    @ByVal
    public static native Mat estimateGlobalMotionLeastSquares(@ByVal Mat mat, @ByVal Mat mat2, int i, FloatBuffer floatBuffer);

    @Namespace("cv::videostab")
    @ByVal
    public static native Mat estimateGlobalMotionLeastSquares(@ByVal Mat mat, @ByVal Mat mat2, int i, FloatPointer floatPointer);

    @Namespace("cv::videostab")
    @ByVal
    public static native Mat estimateGlobalMotionLeastSquares(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv::videostab")
    @ByVal
    public static native Mat estimateGlobalMotionLeastSquares(@ByVal UMat uMat, @ByVal UMat uMat2, int i, FloatPointer floatPointer);

    @Namespace("cv::videostab")
    @ByVal
    public static native Mat estimateGlobalMotionLeastSquares(@ByVal UMat uMat, @ByVal UMat uMat2, int i, float[] fArr);

    @Namespace("cv::videostab")
    @ByVal
    public static native Mat estimateGlobalMotionRansac(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv::videostab")
    @ByVal
    public static native Mat estimateGlobalMotionRansac(@ByVal Mat mat, @ByVal Mat mat2, int i, @ByRef(nullValue = "cv::videostab::RansacParams::default2dMotion(cv::videostab::MM_AFFINE)") @Const RansacParams ransacParams, FloatBuffer floatBuffer, IntBuffer intBuffer);

    @Namespace("cv::videostab")
    @ByVal
    public static native Mat estimateGlobalMotionRansac(@ByVal Mat mat, @ByVal Mat mat2, int i, @ByRef(nullValue = "cv::videostab::RansacParams::default2dMotion(cv::videostab::MM_AFFINE)") @Const RansacParams ransacParams, FloatPointer floatPointer, IntPointer intPointer);

    @Namespace("cv::videostab")
    @ByVal
    public static native Mat estimateGlobalMotionRansac(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv::videostab")
    @ByVal
    public static native Mat estimateGlobalMotionRansac(@ByVal UMat uMat, @ByVal UMat uMat2, int i, @ByRef(nullValue = "cv::videostab::RansacParams::default2dMotion(cv::videostab::MM_AFFINE)") @Const RansacParams ransacParams, FloatPointer floatPointer, IntPointer intPointer);

    @Namespace("cv::videostab")
    @ByVal
    public static native Mat estimateGlobalMotionRansac(@ByVal UMat uMat, @ByVal UMat uMat2, int i, @ByRef(nullValue = "cv::videostab::RansacParams::default2dMotion(cv::videostab::MM_AFFINE)") @Const RansacParams ransacParams, float[] fArr, int[] iArr);

    @Namespace("cv::videostab")
    public static native float estimateOptimalTrimRatio(@ByRef @Const Mat mat, @ByVal Size size);

    @Namespace("cv::videostab")
    @ByVal
    public static native Mat getMotion(int i, int i2, @ByRef @Const MatVector matVector);

    static {
        Loader.load();
    }
}
