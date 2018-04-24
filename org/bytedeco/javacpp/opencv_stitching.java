package org.bytedeco.javacpp;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.MemberGetter;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoOffset;
import org.bytedeco.javacpp.annotation.StdVector;
import org.bytedeco.javacpp.opencv_core.DMatchVector;
import org.bytedeco.javacpp.opencv_core.KeyPointVector;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Point2f;
import org.bytedeco.javacpp.opencv_core.PointVector;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.RectVector;
import org.bytedeco.javacpp.opencv_core.RectVectorVector;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_core.SizeVector;
import org.bytedeco.javacpp.opencv_core.StringVector;
import org.bytedeco.javacpp.opencv_core.TermCriteria;
import org.bytedeco.javacpp.opencv_core.UMat;
import org.bytedeco.javacpp.opencv_core.UMatBytePairVector;
import org.bytedeco.javacpp.opencv_core.UMatVector;
import org.bytedeco.javacpp.presets.opencv_core.Ptr;
import org.bytedeco.javacpp.presets.opencv_core.Str;

public class opencv_stitching extends org.bytedeco.javacpp.presets.opencv_stitching {
    public static final int ENABLE_LOG = 0;
    public static final int WAVE_CORRECT_HORIZ = 0;
    public static final int WAVE_CORRECT_VERT = 1;

    @Namespace("cv::detail")
    @NoOffset
    public static class FeaturesMatcher extends Pointer {
        @Name({"operator ()"})
        public native void apply(@ByRef @Const ImageFeatures imageFeatures, @ByRef @Const ImageFeatures imageFeatures2, @ByRef MatchesInfo matchesInfo);

        @Name({"operator ()"})
        public native void apply(@StdVector ImageFeatures imageFeatures, @StdVector MatchesInfo matchesInfo);

        @Name({"operator ()"})
        public native void apply(@StdVector ImageFeatures imageFeatures, @StdVector MatchesInfo matchesInfo, @ByRef(nullValue = "cv::UMat()") @Const UMat uMat);

        public native void collectGarbage();

        @Cast({"bool"})
        public native boolean isThreadSafe();

        static {
            Loader.load();
        }

        public FeaturesMatcher(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class BestOf2NearestMatcher extends FeaturesMatcher {
        private native void allocate();

        private native void allocate(@Cast({"bool"}) boolean z, float f, int i, int i2);

        private native void allocateArray(long j);

        public native void collectGarbage();

        static {
            Loader.load();
        }

        public BestOf2NearestMatcher(Pointer p) {
            super(p);
        }

        public BestOf2NearestMatcher(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public BestOf2NearestMatcher position(long position) {
            return (BestOf2NearestMatcher) super.position(position);
        }

        public BestOf2NearestMatcher(@Cast({"bool"}) boolean try_use_gpu, float match_conf, int num_matches_thresh1, int num_matches_thresh2) {
            super((Pointer) null);
            allocate(try_use_gpu, match_conf, num_matches_thresh1, num_matches_thresh2);
        }

        public BestOf2NearestMatcher() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class BestOf2NearestRangeMatcher extends BestOf2NearestMatcher {
        private native void allocate();

        private native void allocate(int i, @Cast({"bool"}) boolean z, float f, int i2, int i3);

        private native void allocateArray(long j);

        @Name({"operator ()"})
        public native void apply(@StdVector ImageFeatures imageFeatures, @StdVector MatchesInfo matchesInfo);

        @Name({"operator ()"})
        public native void apply(@StdVector ImageFeatures imageFeatures, @StdVector MatchesInfo matchesInfo, @ByRef(nullValue = "cv::UMat()") @Const UMat uMat);

        static {
            Loader.load();
        }

        public BestOf2NearestRangeMatcher(Pointer p) {
            super(p);
        }

        public BestOf2NearestRangeMatcher(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public BestOf2NearestRangeMatcher position(long position) {
            return (BestOf2NearestRangeMatcher) super.position(position);
        }

        public BestOf2NearestRangeMatcher(int range_width, @Cast({"bool"}) boolean try_use_gpu, float match_conf, int num_matches_thresh1, int num_matches_thresh2) {
            super((Pointer) null);
            allocate(range_width, try_use_gpu, match_conf, num_matches_thresh1, num_matches_thresh2);
        }

        public BestOf2NearestRangeMatcher() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::detail")
    public static class Blender extends Pointer {
        public static final int FEATHER = 1;
        public static final int MULTI_BAND = 2;
        public static final int NO = 0;

        private native void allocate();

        private native void allocateArray(long j);

        @Ptr
        public static native Blender createDefault(int i);

        @Ptr
        public static native Blender createDefault(int i, @Cast({"bool"}) boolean z);

        public native void blend(@ByVal Mat mat, @ByVal Mat mat2);

        public native void blend(@ByVal UMat uMat, @ByVal UMat uMat2);

        public native void feed(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Point point);

        public native void feed(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal Point point);

        public native void prepare(@ByRef @Const PointVector pointVector, @ByRef @Const SizeVector sizeVector);

        public native void prepare(@ByVal Rect rect);

        static {
            Loader.load();
        }

        public Blender() {
            super((Pointer) null);
            allocate();
        }

        public Blender(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Blender(Pointer p) {
            super(p);
        }

        public Blender position(long position) {
            return (Blender) super.position(position);
        }
    }

    @Namespace("cv::detail")
    public static class ExposureCompensator extends Pointer {
        public static final int GAIN = 1;
        public static final int GAIN_BLOCKS = 2;
        public static final int NO = 0;

        @Ptr
        public static native ExposureCompensator createDefault(int i);

        public native void apply(int i, @ByVal Point point, @ByVal Mat mat, @ByVal Mat mat2);

        public native void apply(int i, @ByVal Point point, @ByVal UMat uMat, @ByVal UMat uMat2);

        public native void feed(@ByRef @Const PointVector pointVector, @ByRef @Const UMatVector uMatVector, @ByRef @Const UMatBytePairVector uMatBytePairVector);

        public native void feed(@ByRef @Const PointVector pointVector, @ByRef @Const UMatVector uMatVector, @ByRef @Const UMatVector uMatVector2);

        static {
            Loader.load();
        }

        public ExposureCompensator(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class BlocksGainCompensator extends ExposureCompensator {
        private native void allocate();

        private native void allocate(int i, int i2);

        private native void allocateArray(long j);

        public native void apply(int i, @ByVal Point point, @ByVal Mat mat, @ByVal Mat mat2);

        public native void apply(int i, @ByVal Point point, @ByVal UMat uMat, @ByVal UMat uMat2);

        public native void feed(@ByRef @Const PointVector pointVector, @ByRef @Const UMatVector uMatVector, @ByRef @Const UMatBytePairVector uMatBytePairVector);

        static {
            Loader.load();
        }

        public BlocksGainCompensator(Pointer p) {
            super(p);
        }

        public BlocksGainCompensator(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public BlocksGainCompensator position(long position) {
            return (BlocksGainCompensator) super.position(position);
        }

        public BlocksGainCompensator(int bl_width, int bl_height) {
            super((Pointer) null);
            allocate(bl_width, bl_height);
        }

        public BlocksGainCompensator() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::detail")
    public static class Estimator extends Pointer {
        @Cast({"bool"})
        @Name({"operator ()"})
        public native boolean apply(@StdVector ImageFeatures imageFeatures, @StdVector MatchesInfo matchesInfo, @StdVector CameraParams cameraParams);

        static {
            Loader.load();
        }

        public Estimator(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class BundleAdjusterBase extends Estimator {
        public native double confThresh();

        @Const
        @ByVal
        public native Mat refinementMask();

        public native void setConfThresh(double d);

        public native void setRefinementMask(@ByRef @Const Mat mat);

        public native void setTermCriteria(@ByRef @Const TermCriteria termCriteria);

        @ByVal
        public native TermCriteria termCriteria();

        static {
            Loader.load();
        }

        public BundleAdjusterBase(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class BundleAdjusterRay extends BundleAdjusterBase {
        private native void allocate();

        private native void allocateArray(long j);

        static {
            Loader.load();
        }

        public BundleAdjusterRay(Pointer p) {
            super(p);
        }

        public BundleAdjusterRay(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public BundleAdjusterRay position(long position) {
            return (BundleAdjusterRay) super.position(position);
        }

        public BundleAdjusterRay() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class BundleAdjusterReproj extends BundleAdjusterBase {
        private native void allocate();

        private native void allocateArray(long j);

        static {
            Loader.load();
        }

        public BundleAdjusterReproj(Pointer p) {
            super(p);
        }

        public BundleAdjusterReproj(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public BundleAdjusterReproj position(long position) {
            return (BundleAdjusterReproj) super.position(position);
        }

        public BundleAdjusterReproj() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class CameraParams extends Pointer {
        private native void allocate();

        private native void allocate(@ByRef @Const CameraParams cameraParams);

        private native void allocateArray(long j);

        @ByVal
        public native Mat m164K();

        @ByRef
        public native Mat m165R();

        public native CameraParams m166R(Mat mat);

        public native double aspect();

        public native CameraParams aspect(double d);

        public native double focal();

        public native CameraParams focal(double d);

        public native double ppx();

        public native CameraParams ppx(double d);

        public native double ppy();

        public native CameraParams ppy(double d);

        @ByRef
        @Const
        @Name({"operator ="})
        public native CameraParams put(@ByRef @Const CameraParams cameraParams);

        @ByRef
        public native Mat m167t();

        public native CameraParams m168t(Mat mat);

        static {
            Loader.load();
        }

        public CameraParams(Pointer p) {
            super(p);
        }

        public CameraParams(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CameraParams position(long position) {
            return (CameraParams) super.position(position);
        }

        public CameraParams() {
            super((Pointer) null);
            allocate();
        }

        public CameraParams(@ByRef @Const CameraParams other) {
            super((Pointer) null);
            allocate(other);
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class CompressedRectilinearPortraitProjector extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float m169a();

        public native CompressedRectilinearPortraitProjector m170a(float f);

        public native float m171b();

        public native CompressedRectilinearPortraitProjector m172b(float f);

        public native void mapBackward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapBackward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapBackward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        public native void mapForward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapForward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapForward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        static {
            Loader.load();
        }

        public CompressedRectilinearPortraitProjector() {
            super((Pointer) null);
            allocate();
        }

        public CompressedRectilinearPortraitProjector(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CompressedRectilinearPortraitProjector(Pointer p) {
            super(p);
        }

        public CompressedRectilinearPortraitProjector position(long position) {
            return (CompressedRectilinearPortraitProjector) super.position(position);
        }
    }

    @Namespace("cv")
    public static class WarperCreator extends Pointer {
        @Ptr
        public native RotationWarper create(float f);

        static {
            Loader.load();
        }

        public WarperCreator(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class CompressedRectilinearPortraitWarper extends WarperCreator {
        private native void allocate();

        private native void allocate(float f, float f2);

        private native void allocateArray(long j);

        @Ptr
        public native RotationWarper create(float f);

        static {
            Loader.load();
        }

        public CompressedRectilinearPortraitWarper(Pointer p) {
            super(p);
        }

        public CompressedRectilinearPortraitWarper(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CompressedRectilinearPortraitWarper position(long position) {
            return (CompressedRectilinearPortraitWarper) super.position(position);
        }

        public CompressedRectilinearPortraitWarper(float A, float B) {
            super((Pointer) null);
            allocate(A, B);
        }

        public CompressedRectilinearPortraitWarper() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class CompressedRectilinearProjector extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float m173a();

        public native CompressedRectilinearProjector m174a(float f);

        public native float m175b();

        public native CompressedRectilinearProjector m176b(float f);

        public native void mapBackward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapBackward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapBackward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        public native void mapForward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapForward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapForward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        static {
            Loader.load();
        }

        public CompressedRectilinearProjector() {
            super((Pointer) null);
            allocate();
        }

        public CompressedRectilinearProjector(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CompressedRectilinearProjector(Pointer p) {
            super(p);
        }

        public CompressedRectilinearProjector position(long position) {
            return (CompressedRectilinearProjector) super.position(position);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class CompressedRectilinearWarper extends WarperCreator {
        private native void allocate();

        private native void allocate(float f, float f2);

        private native void allocateArray(long j);

        @Ptr
        public native RotationWarper create(float f);

        static {
            Loader.load();
        }

        public CompressedRectilinearWarper(Pointer p) {
            super(p);
        }

        public CompressedRectilinearWarper(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CompressedRectilinearWarper position(long position) {
            return (CompressedRectilinearWarper) super.position(position);
        }

        public CompressedRectilinearWarper(float A, float B) {
            super((Pointer) null);
            allocate(A, B);
        }

        public CompressedRectilinearWarper() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::detail")
    public static class CylindricalPortraitProjector extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native void mapBackward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapBackward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapBackward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        public native void mapForward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapForward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapForward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        static {
            Loader.load();
        }

        public CylindricalPortraitProjector() {
            super((Pointer) null);
            allocate();
        }

        public CylindricalPortraitProjector(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CylindricalPortraitProjector(Pointer p) {
            super(p);
        }

        public CylindricalPortraitProjector position(long position) {
            return (CylindricalPortraitProjector) super.position(position);
        }
    }

    @Namespace("cv::detail")
    public static class RotationWarper extends Pointer {
        @ByVal
        public native Rect buildMaps(@ByVal Size size, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4);

        @ByVal
        public native Rect buildMaps(@ByVal Size size, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4);

        public native float getScale();

        public native void setScale(float f);

        @ByVal
        public native Point warp(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i, int i2, @ByVal Mat mat4);

        @ByVal
        public native Point warp(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i, int i2, @ByVal UMat uMat4);

        public native void warpBackward(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i, int i2, @ByVal Size size, @ByVal Mat mat4);

        public native void warpBackward(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i, int i2, @ByVal Size size, @ByVal UMat uMat4);

        @ByVal
        public native Point2f warpPoint(@ByRef @Const Point2f point2f, @ByVal Mat mat, @ByVal Mat mat2);

        @ByVal
        public native Point2f warpPoint(@ByRef @Const Point2f point2f, @ByVal UMat uMat, @ByVal UMat uMat2);

        @ByVal
        public native Rect warpRoi(@ByVal Size size, @ByVal Mat mat, @ByVal Mat mat2);

        @ByVal
        public native Rect warpRoi(@ByVal Size size, @ByVal UMat uMat, @ByVal UMat uMat2);

        static {
            Loader.load();
        }

        public RotationWarper(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::detail")
    public static class CylindricalPortraitWarper extends RotationWarper {
        private native void allocate(float f);

        static {
            Loader.load();
        }

        public CylindricalPortraitWarper(Pointer p) {
            super(p);
        }

        public CylindricalPortraitWarper(float scale) {
            super((Pointer) null);
            allocate(scale);
        }
    }

    @Namespace("cv::detail")
    public static class CylindricalProjector extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native void mapBackward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapBackward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapBackward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        public native void mapForward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapForward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapForward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        static {
            Loader.load();
        }

        public CylindricalProjector() {
            super((Pointer) null);
            allocate();
        }

        public CylindricalProjector(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CylindricalProjector(Pointer p) {
            super(p);
        }

        public CylindricalProjector position(long position) {
            return (CylindricalProjector) super.position(position);
        }
    }

    @Namespace("cv")
    public static class CylindricalWarper extends WarperCreator {
        private native void allocate();

        private native void allocateArray(long j);

        @Ptr
        public native RotationWarper create(float f);

        static {
            Loader.load();
        }

        public CylindricalWarper() {
            super((Pointer) null);
            allocate();
        }

        public CylindricalWarper(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CylindricalWarper(Pointer p) {
            super(p);
        }

        public CylindricalWarper position(long position) {
            return (CylindricalWarper) super.position(position);
        }
    }

    @Name({"cv::detail::CompressedRectilinearPortraitWarper"})
    public static class DetailCompressedRectilinearPortraitWarper extends RotationWarper {
        private native void allocate(float f);

        private native void allocate(float f, float f2, float f3);

        static {
            Loader.load();
        }

        public DetailCompressedRectilinearPortraitWarper(Pointer p) {
            super(p);
        }

        public DetailCompressedRectilinearPortraitWarper(float scale, float A, float B) {
            super((Pointer) null);
            allocate(scale, A, B);
        }

        public DetailCompressedRectilinearPortraitWarper(float scale) {
            super((Pointer) null);
            allocate(scale);
        }
    }

    @Name({"cv::detail::CompressedRectilinearWarper"})
    public static class DetailCompressedRectilinearWarper extends RotationWarper {
        private native void allocate(float f);

        private native void allocate(float f, float f2, float f3);

        static {
            Loader.load();
        }

        public DetailCompressedRectilinearWarper(Pointer p) {
            super(p);
        }

        public DetailCompressedRectilinearWarper(float scale, float A, float B) {
            super((Pointer) null);
            allocate(scale, A, B);
        }

        public DetailCompressedRectilinearWarper(float scale) {
            super((Pointer) null);
            allocate(scale);
        }
    }

    @Name({"cv::detail::CylindricalWarper"})
    public static class DetailCylindricalWarper extends RotationWarper {
        private native void allocate(float f);

        @ByVal
        public native Rect buildMaps(@ByVal Size size, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4);

        @ByVal
        public native Rect buildMaps(@ByVal Size size, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4);

        @ByVal
        public native Point warp(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i, int i2, @ByVal Mat mat4);

        @ByVal
        public native Point warp(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i, int i2, @ByVal UMat uMat4);

        static {
            Loader.load();
        }

        public DetailCylindricalWarper(Pointer p) {
            super(p);
        }

        public DetailCylindricalWarper(float scale) {
            super((Pointer) null);
            allocate(scale);
        }
    }

    @Name({"cv::detail::CylindricalWarperGpu"})
    public static class DetailCylindricalWarperGpu extends RotationWarper {
        private native void allocate(float f);

        @ByVal
        public native Rect buildMaps(@ByVal Size size, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4);

        @ByVal
        public native Rect buildMaps(@ByVal Size size, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4);

        @ByVal
        public native Point warp(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i, int i2, @ByVal Mat mat4);

        @ByVal
        public native Point warp(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i, int i2, @ByVal UMat uMat4);

        static {
            Loader.load();
        }

        public DetailCylindricalWarperGpu(Pointer p) {
            super(p);
        }

        public DetailCylindricalWarperGpu(float scale) {
            super((Pointer) null);
            allocate(scale);
        }
    }

    @Name({"cv::detail::FisheyeWarper"})
    public static class DetailFisheyeWarper extends RotationWarper {
        private native void allocate(float f);

        static {
            Loader.load();
        }

        public DetailFisheyeWarper(Pointer p) {
            super(p);
        }

        public DetailFisheyeWarper(float scale) {
            super((Pointer) null);
            allocate(scale);
        }
    }

    @Name({"cv::detail::MercatorWarper"})
    public static class DetailMercatorWarper extends RotationWarper {
        private native void allocate(float f);

        static {
            Loader.load();
        }

        public DetailMercatorWarper(Pointer p) {
            super(p);
        }

        public DetailMercatorWarper(float scale) {
            super((Pointer) null);
            allocate(scale);
        }
    }

    @Name({"cv::detail::PaniniPortraitWarper"})
    public static class DetailPaniniPortraitWarper extends RotationWarper {
        private native void allocate(float f);

        private native void allocate(float f, float f2, float f3);

        static {
            Loader.load();
        }

        public DetailPaniniPortraitWarper(Pointer p) {
            super(p);
        }

        public DetailPaniniPortraitWarper(float scale, float A, float B) {
            super((Pointer) null);
            allocate(scale, A, B);
        }

        public DetailPaniniPortraitWarper(float scale) {
            super((Pointer) null);
            allocate(scale);
        }
    }

    @Name({"cv::detail::PaniniWarper"})
    public static class DetailPaniniWarper extends RotationWarper {
        private native void allocate(float f);

        private native void allocate(float f, float f2, float f3);

        static {
            Loader.load();
        }

        public DetailPaniniWarper(Pointer p) {
            super(p);
        }

        public DetailPaniniWarper(float scale, float A, float B) {
            super((Pointer) null);
            allocate(scale, A, B);
        }

        public DetailPaniniWarper(float scale) {
            super((Pointer) null);
            allocate(scale);
        }
    }

    @Name({"cv::detail::PlaneWarper"})
    public static class DetailPlaneWarper extends RotationWarper {
        private native void allocate();

        private native void allocate(float f);

        private native void allocateArray(long j);

        @ByVal
        public native Rect buildMaps(@ByVal Size size, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4);

        @ByVal
        public native Rect buildMaps(@ByVal Size size, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5);

        @ByVal
        public native Rect buildMaps(@ByVal Size size, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4);

        @ByVal
        public native Rect buildMaps(@ByVal Size size, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5);

        @ByVal
        public native Point warp(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i, int i2, @ByVal Mat mat4);

        @ByVal
        public native Point warp(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, int i, int i2, @ByVal Mat mat5);

        @ByVal
        public native Point warp(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i, int i2, @ByVal UMat uMat4);

        @ByVal
        public native Point warp(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, int i, int i2, @ByVal UMat uMat5);

        @ByVal
        public native Point2f warpPoint(@ByRef @Const Point2f point2f, @ByVal Mat mat, @ByVal Mat mat2);

        @ByVal
        public native Point2f warpPoint(@ByRef @Const Point2f point2f, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

        @ByVal
        public native Point2f warpPoint(@ByRef @Const Point2f point2f, @ByVal UMat uMat, @ByVal UMat uMat2);

        @ByVal
        public native Point2f warpPoint(@ByRef @Const Point2f point2f, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

        @ByVal
        public native Rect warpRoi(@ByVal Size size, @ByVal Mat mat, @ByVal Mat mat2);

        @ByVal
        public native Rect warpRoi(@ByVal Size size, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

        @ByVal
        public native Rect warpRoi(@ByVal Size size, @ByVal UMat uMat, @ByVal UMat uMat2);

        @ByVal
        public native Rect warpRoi(@ByVal Size size, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

        static {
            Loader.load();
        }

        public DetailPlaneWarper(Pointer p) {
            super(p);
        }

        public DetailPlaneWarper(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public DetailPlaneWarper position(long position) {
            return (DetailPlaneWarper) super.position(position);
        }

        public DetailPlaneWarper(float scale) {
            super((Pointer) null);
            allocate(scale);
        }

        public DetailPlaneWarper() {
            super((Pointer) null);
            allocate();
        }
    }

    @Name({"cv::detail::PlaneWarperGpu"})
    public static class DetailPlaneWarperGpu extends RotationWarper {
        private native void allocate();

        private native void allocate(float f);

        private native void allocateArray(long j);

        @ByVal
        public native Rect buildMaps(@ByVal Size size, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4);

        @ByVal
        public native Rect buildMaps(@ByVal Size size, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5);

        @ByVal
        public native Rect buildMaps(@ByVal Size size, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4);

        @ByVal
        public native Rect buildMaps(@ByVal Size size, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5);

        @ByVal
        public native Point warp(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i, int i2, @ByVal Mat mat4);

        @ByVal
        public native Point warp(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, int i, int i2, @ByVal Mat mat5);

        @ByVal
        public native Point warp(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i, int i2, @ByVal UMat uMat4);

        @ByVal
        public native Point warp(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, int i, int i2, @ByVal UMat uMat5);

        static {
            Loader.load();
        }

        public DetailPlaneWarperGpu(Pointer p) {
            super(p);
        }

        public DetailPlaneWarperGpu(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public DetailPlaneWarperGpu position(long position) {
            return (DetailPlaneWarperGpu) super.position(position);
        }

        public DetailPlaneWarperGpu(float scale) {
            super((Pointer) null);
            allocate(scale);
        }

        public DetailPlaneWarperGpu() {
            super((Pointer) null);
            allocate();
        }
    }

    @Name({"cv::detail::SphericalWarper"})
    public static class DetailSphericalWarper extends RotationWarper {
        private native void allocate(float f);

        @ByVal
        public native Rect buildMaps(@ByVal Size size, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4);

        @ByVal
        public native Rect buildMaps(@ByVal Size size, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4);

        @ByVal
        public native Point warp(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i, int i2, @ByVal Mat mat4);

        @ByVal
        public native Point warp(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i, int i2, @ByVal UMat uMat4);

        static {
            Loader.load();
        }

        public DetailSphericalWarper(Pointer p) {
            super(p);
        }

        public DetailSphericalWarper(float scale) {
            super((Pointer) null);
            allocate(scale);
        }
    }

    @Name({"cv::detail::SphericalWarperGpu"})
    public static class DetailSphericalWarperGpu extends RotationWarper {
        private native void allocate(float f);

        @ByVal
        public native Rect buildMaps(@ByVal Size size, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4);

        @ByVal
        public native Rect buildMaps(@ByVal Size size, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4);

        @ByVal
        public native Point warp(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i, int i2, @ByVal Mat mat4);

        @ByVal
        public native Point warp(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i, int i2, @ByVal UMat uMat4);

        static {
            Loader.load();
        }

        public DetailSphericalWarperGpu(Pointer p) {
            super(p);
        }

        public DetailSphericalWarperGpu(float scale) {
            super((Pointer) null);
            allocate(scale);
        }
    }

    @Name({"cv::detail::StereographicWarper"})
    public static class DetailStereographicWarper extends RotationWarper {
        private native void allocate(float f);

        static {
            Loader.load();
        }

        public DetailStereographicWarper(Pointer p) {
            super(p);
        }

        public DetailStereographicWarper(float scale) {
            super((Pointer) null);
            allocate(scale);
        }
    }

    @Name({"cv::detail::TransverseMercatorWarper"})
    public static class DetailTransverseMercatorWarper extends RotationWarper {
        private native void allocate(float f);

        static {
            Loader.load();
        }

        public DetailTransverseMercatorWarper(Pointer p) {
            super(p);
        }

        public DetailTransverseMercatorWarper(float scale) {
            super((Pointer) null);
            allocate(scale);
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class DisjointSets extends Pointer {
        private native void allocate();

        private native void allocate(int i);

        private native void allocateArray(long j);

        public native void createOneElemSets(int i);

        public native int findSetByElem(int i);

        public native int mergeSets(int i, int i2);

        @StdVector
        public native IntPointer parent();

        public native DisjointSets parent(IntPointer intPointer);

        @StdVector
        public native IntPointer size();

        public native DisjointSets size(IntPointer intPointer);

        static {
            Loader.load();
        }

        public DisjointSets(Pointer p) {
            super(p);
        }

        public DisjointSets(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public DisjointSets position(long position) {
            return (DisjointSets) super.position(position);
        }

        public DisjointSets(int elem_count) {
            super((Pointer) null);
            allocate(elem_count);
        }

        public DisjointSets() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::detail")
    public static class SeamFinder extends Pointer {
        public native void find(@ByRef @Const UMatVector uMatVector, @ByRef @Const PointVector pointVector, @ByRef UMatVector uMatVector2);

        static {
            Loader.load();
        }

        public SeamFinder(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class DpSeamFinder extends SeamFinder {
        public static final int COLOR = 0;
        public static final int COLOR_GRAD = 1;

        private native void allocate();

        private native void allocate(@Cast({"cv::detail::DpSeamFinder::CostFunction"}) int i);

        private native void allocateArray(long j);

        @Cast({"cv::detail::DpSeamFinder::CostFunction"})
        public native int costFunction();

        public native void find(@ByRef @Const UMatVector uMatVector, @ByRef @Const PointVector pointVector, @ByRef UMatVector uMatVector2);

        public native void setCostFunction(@Cast({"cv::detail::DpSeamFinder::CostFunction"}) int i);

        static {
            Loader.load();
        }

        public DpSeamFinder(Pointer p) {
            super(p);
        }

        public DpSeamFinder(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public DpSeamFinder position(long position) {
            return (DpSeamFinder) super.position(position);
        }

        public DpSeamFinder(@Cast({"cv::detail::DpSeamFinder::CostFunction"}) int costFunc) {
            super((Pointer) null);
            allocate(costFunc);
        }

        public DpSeamFinder() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class FeatherBlender extends Blender {
        private native void allocate();

        private native void allocate(float f);

        private native void allocateArray(long j);

        public native void blend(@ByVal Mat mat, @ByVal Mat mat2);

        public native void blend(@ByVal UMat uMat, @ByVal UMat uMat2);

        @ByVal
        public native Rect createWeightMaps(@ByRef @Const UMatVector uMatVector, @ByRef @Const PointVector pointVector, @ByRef UMatVector uMatVector2);

        public native void feed(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Point point);

        public native void feed(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal Point point);

        public native void prepare(@ByVal Rect rect);

        public native void setSharpness(float f);

        public native float sharpness();

        static {
            Loader.load();
        }

        public FeatherBlender(Pointer p) {
            super(p);
        }

        public FeatherBlender(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FeatherBlender position(long position) {
            return (FeatherBlender) super.position(position);
        }

        public FeatherBlender(float sharpness) {
            super((Pointer) null);
            allocate(sharpness);
        }

        public FeatherBlender() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::detail")
    public static class FeaturesFinder extends Pointer {
        @Name({"operator ()"})
        public native void apply(@ByVal Mat mat, @ByRef ImageFeatures imageFeatures);

        @Name({"operator ()"})
        public native void apply(@ByVal Mat mat, @ByRef ImageFeatures imageFeatures, @ByRef @Const RectVector rectVector);

        @Name({"operator ()"})
        public native void apply(@ByVal UMat uMat, @ByRef ImageFeatures imageFeatures);

        @Name({"operator ()"})
        public native void apply(@ByVal UMat uMat, @ByRef ImageFeatures imageFeatures, @ByRef @Const RectVector rectVector);

        public native void collectGarbage();

        static {
            Loader.load();
        }

        public FeaturesFinder(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::detail")
    public static class FisheyeProjector extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native void mapBackward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapBackward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapBackward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        public native void mapForward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapForward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapForward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        static {
            Loader.load();
        }

        public FisheyeProjector() {
            super((Pointer) null);
            allocate();
        }

        public FisheyeProjector(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FisheyeProjector(Pointer p) {
            super(p);
        }

        public FisheyeProjector position(long position) {
            return (FisheyeProjector) super.position(position);
        }
    }

    @Namespace("cv")
    public static class FisheyeWarper extends WarperCreator {
        private native void allocate();

        private native void allocateArray(long j);

        @Ptr
        public native RotationWarper create(float f);

        static {
            Loader.load();
        }

        public FisheyeWarper() {
            super((Pointer) null);
            allocate();
        }

        public FisheyeWarper(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FisheyeWarper(Pointer p) {
            super(p);
        }

        public FisheyeWarper position(long position) {
            return (FisheyeWarper) super.position(position);
        }
    }

    @Namespace("cv::detail")
    public static class GainCompensator extends ExposureCompensator {
        private native void allocate();

        private native void allocateArray(long j);

        public native void apply(int i, @ByVal Point point, @ByVal Mat mat, @ByVal Mat mat2);

        public native void apply(int i, @ByVal Point point, @ByVal UMat uMat, @ByVal UMat uMat2);

        public native void feed(@ByRef @Const PointVector pointVector, @ByRef @Const UMatVector uMatVector, @ByRef @Const UMatBytePairVector uMatBytePairVector);

        @StdVector
        public native DoublePointer gains();

        static {
            Loader.load();
        }

        public GainCompensator() {
            super((Pointer) null);
            allocate();
        }

        public GainCompensator(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public GainCompensator(Pointer p) {
            super(p);
        }

        public GainCompensator position(long position) {
            return (GainCompensator) super.position(position);
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class Graph extends Pointer {
        private native void allocate();

        private native void allocate(int i);

        private native void allocateArray(long j);

        public native void addEdge(int i, int i2, float f);

        public native void create(int i);

        public native int numVertices();

        static {
            Loader.load();
        }

        public Graph(Pointer p) {
            super(p);
        }

        public Graph(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Graph position(long position) {
            return (Graph) super.position(position);
        }

        public Graph(int num_vertices) {
            super((Pointer) null);
            allocate(num_vertices);
        }

        public Graph() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::detail")
    public static class GraphCutSeamFinderBase extends Pointer {
        public static final int COST_COLOR = 0;
        public static final int COST_COLOR_GRAD = 1;

        private native void allocate();

        private native void allocateArray(long j);

        static {
            Loader.load();
        }

        public GraphCutSeamFinderBase() {
            super((Pointer) null);
            allocate();
        }

        public GraphCutSeamFinderBase(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public GraphCutSeamFinderBase(Pointer p) {
            super(p);
        }

        public GraphCutSeamFinderBase position(long position) {
            return (GraphCutSeamFinderBase) super.position(position);
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class GraphCutSeamFinder extends GraphCutSeamFinderBase {
        private native void allocate();

        private native void allocate(int i, float f, float f2);

        private native void allocateArray(long j);

        @Namespace
        @Name({"static_cast<cv::detail::SeamFinder*>"})
        public static native SeamFinder asSeamFinder(GraphCutSeamFinder graphCutSeamFinder);

        public native void find(@ByRef @Const UMatVector uMatVector, @ByRef @Const PointVector pointVector, @ByRef UMatVector uMatVector2);

        static {
            Loader.load();
        }

        public GraphCutSeamFinder(Pointer p) {
            super(p);
        }

        public GraphCutSeamFinder(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public GraphCutSeamFinder position(long position) {
            return (GraphCutSeamFinder) super.position(position);
        }

        public SeamFinder asSeamFinder() {
            return asSeamFinder(this);
        }

        public GraphCutSeamFinder(int cost_type, float terminal_cost, float bad_region_penalty) {
            super((Pointer) null);
            allocate(cost_type, terminal_cost, bad_region_penalty);
        }

        public GraphCutSeamFinder() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class GraphEdge extends Pointer {
        private native void allocate(int i, int i2, float f);

        public native int from();

        public native GraphEdge from(int i);

        @Cast({"bool"})
        @Name({"operator >"})
        public native boolean greaterThan(@ByRef @Const GraphEdge graphEdge);

        @Cast({"bool"})
        @Name({"operator <"})
        public native boolean lessThan(@ByRef @Const GraphEdge graphEdge);

        public native int to();

        public native GraphEdge to(int i);

        public native float weight();

        public native GraphEdge weight(float f);

        static {
            Loader.load();
        }

        public GraphEdge(Pointer p) {
            super(p);
        }

        public GraphEdge(int from, int to, float weight) {
            super((Pointer) null);
            allocate(from, to, weight);
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class HomographyBasedEstimator extends Estimator {
        private native void allocate();

        private native void allocate(@Cast({"bool"}) boolean z);

        private native void allocateArray(long j);

        static {
            Loader.load();
        }

        public HomographyBasedEstimator(Pointer p) {
            super(p);
        }

        public HomographyBasedEstimator(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public HomographyBasedEstimator position(long position) {
            return (HomographyBasedEstimator) super.position(position);
        }

        public HomographyBasedEstimator(@Cast({"bool"}) boolean is_focals_estimated) {
            super((Pointer) null);
            allocate(is_focals_estimated);
        }

        public HomographyBasedEstimator() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::detail")
    public static class ImageFeatures extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @ByRef
        public native UMat descriptors();

        public native ImageFeatures descriptors(UMat uMat);

        public native int img_idx();

        public native ImageFeatures img_idx(int i);

        @ByRef
        public native Size img_size();

        public native ImageFeatures img_size(Size size);

        @ByRef
        public native KeyPointVector keypoints();

        public native ImageFeatures keypoints(KeyPointVector keyPointVector);

        static {
            Loader.load();
        }

        public ImageFeatures() {
            super((Pointer) null);
            allocate();
        }

        public ImageFeatures(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public ImageFeatures(Pointer p) {
            super(p);
        }

        public ImageFeatures position(long position) {
            return (ImageFeatures) super.position(position);
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class MatchesInfo extends Pointer {
        private native void allocate();

        private native void allocate(@ByRef @Const MatchesInfo matchesInfo);

        private native void allocateArray(long j);

        @ByRef
        public native Mat m177H();

        public native MatchesInfo m178H(Mat mat);

        public native double confidence();

        public native MatchesInfo confidence(double d);

        public native int dst_img_idx();

        public native MatchesInfo dst_img_idx(int i);

        @Cast({"uchar*"})
        @StdVector
        public native BytePointer inliers_mask();

        public native MatchesInfo inliers_mask(BytePointer bytePointer);

        @ByRef
        public native DMatchVector matches();

        public native MatchesInfo matches(DMatchVector dMatchVector);

        public native int num_inliers();

        public native MatchesInfo num_inliers(int i);

        @ByRef
        @Const
        @Name({"operator ="})
        public native MatchesInfo put(@ByRef @Const MatchesInfo matchesInfo);

        public native int src_img_idx();

        public native MatchesInfo src_img_idx(int i);

        static {
            Loader.load();
        }

        public MatchesInfo(Pointer p) {
            super(p);
        }

        public MatchesInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public MatchesInfo position(long position) {
            return (MatchesInfo) super.position(position);
        }

        public MatchesInfo() {
            super((Pointer) null);
            allocate();
        }

        public MatchesInfo(@ByRef @Const MatchesInfo other) {
            super((Pointer) null);
            allocate(other);
        }
    }

    @Namespace("cv::detail")
    public static class MercatorProjector extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native void mapBackward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapBackward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapBackward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        public native void mapForward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapForward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapForward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        static {
            Loader.load();
        }

        public MercatorProjector() {
            super((Pointer) null);
            allocate();
        }

        public MercatorProjector(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public MercatorProjector(Pointer p) {
            super(p);
        }

        public MercatorProjector position(long position) {
            return (MercatorProjector) super.position(position);
        }
    }

    @Namespace("cv")
    public static class MercatorWarper extends WarperCreator {
        private native void allocate();

        private native void allocateArray(long j);

        @Ptr
        public native RotationWarper create(float f);

        static {
            Loader.load();
        }

        public MercatorWarper() {
            super((Pointer) null);
            allocate();
        }

        public MercatorWarper(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public MercatorWarper(Pointer p) {
            super(p);
        }

        public MercatorWarper position(long position) {
            return (MercatorWarper) super.position(position);
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class MultiBandBlender extends Blender {
        private native void allocate();

        private native void allocate(int i, int i2, int i3);

        private native void allocateArray(long j);

        public native void blend(@ByVal Mat mat, @ByVal Mat mat2);

        public native void blend(@ByVal UMat uMat, @ByVal UMat uMat2);

        public native void feed(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Point point);

        public native void feed(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal Point point);

        public native int numBands();

        public native void prepare(@ByVal Rect rect);

        public native void setNumBands(int i);

        static {
            Loader.load();
        }

        public MultiBandBlender(Pointer p) {
            super(p);
        }

        public MultiBandBlender(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public MultiBandBlender position(long position) {
            return (MultiBandBlender) super.position(position);
        }

        public MultiBandBlender(int try_gpu, int num_bands, int weight_type) {
            super((Pointer) null);
            allocate(try_gpu, num_bands, weight_type);
        }

        public MultiBandBlender() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::detail")
    public static class NoExposureCompensator extends ExposureCompensator {
        private native void allocate();

        private native void allocateArray(long j);

        public native void apply(int i, @ByVal Point point, @ByVal Mat mat, @ByVal Mat mat2);

        public native void apply(int i, @ByVal Point point, @ByVal UMat uMat, @ByVal UMat uMat2);

        public native void feed(@ByRef @Const PointVector pointVector, @ByRef @Const UMatVector uMatVector, @ByRef @Const UMatBytePairVector uMatBytePairVector);

        static {
            Loader.load();
        }

        public NoExposureCompensator() {
            super((Pointer) null);
            allocate();
        }

        public NoExposureCompensator(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public NoExposureCompensator(Pointer p) {
            super(p);
        }

        public NoExposureCompensator position(long position) {
            return (NoExposureCompensator) super.position(position);
        }
    }

    @Namespace("cv::detail")
    public static class NoSeamFinder extends SeamFinder {
        private native void allocate();

        private native void allocateArray(long j);

        public native void find(@ByRef @Const UMatVector uMatVector, @ByRef @Const PointVector pointVector, @ByRef UMatVector uMatVector2);

        static {
            Loader.load();
        }

        public NoSeamFinder() {
            super((Pointer) null);
            allocate();
        }

        public NoSeamFinder(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public NoSeamFinder(Pointer p) {
            super(p);
        }

        public NoSeamFinder position(long position) {
            return (NoSeamFinder) super.position(position);
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class OrbFeaturesFinder extends FeaturesFinder {
        private native void allocate();

        private native void allocate(@ByVal(nullValue = "cv::Size(3,1)") Size size, int i, float f, int i2);

        private native void allocateArray(long j);

        static {
            Loader.load();
        }

        public OrbFeaturesFinder(Pointer p) {
            super(p);
        }

        public OrbFeaturesFinder(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public OrbFeaturesFinder position(long position) {
            return (OrbFeaturesFinder) super.position(position);
        }

        public OrbFeaturesFinder(@ByVal(nullValue = "cv::Size(3,1)") Size _grid_size, int nfeatures, float scaleFactor, int nlevels) {
            super((Pointer) null);
            allocate(_grid_size, nfeatures, scaleFactor, nlevels);
        }

        public OrbFeaturesFinder() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class PairwiseSeamFinder extends SeamFinder {
        public native void find(@ByRef @Const UMatVector uMatVector, @ByRef @Const PointVector pointVector, @ByRef UMatVector uMatVector2);

        static {
            Loader.load();
        }

        public PairwiseSeamFinder(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class PaniniPortraitProjector extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float m179a();

        public native PaniniPortraitProjector m180a(float f);

        public native float m181b();

        public native PaniniPortraitProjector m182b(float f);

        public native void mapBackward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapBackward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapBackward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        public native void mapForward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapForward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapForward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        static {
            Loader.load();
        }

        public PaniniPortraitProjector() {
            super((Pointer) null);
            allocate();
        }

        public PaniniPortraitProjector(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public PaniniPortraitProjector(Pointer p) {
            super(p);
        }

        public PaniniPortraitProjector position(long position) {
            return (PaniniPortraitProjector) super.position(position);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class PaniniPortraitWarper extends WarperCreator {
        private native void allocate();

        private native void allocate(float f, float f2);

        private native void allocateArray(long j);

        @Ptr
        public native RotationWarper create(float f);

        static {
            Loader.load();
        }

        public PaniniPortraitWarper(Pointer p) {
            super(p);
        }

        public PaniniPortraitWarper(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public PaniniPortraitWarper position(long position) {
            return (PaniniPortraitWarper) super.position(position);
        }

        public PaniniPortraitWarper(float A, float B) {
            super((Pointer) null);
            allocate(A, B);
        }

        public PaniniPortraitWarper() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class PaniniProjector extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float m183a();

        public native PaniniProjector m184a(float f);

        public native float m185b();

        public native PaniniProjector m186b(float f);

        public native void mapBackward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapBackward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapBackward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        public native void mapForward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapForward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapForward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        static {
            Loader.load();
        }

        public PaniniProjector() {
            super((Pointer) null);
            allocate();
        }

        public PaniniProjector(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public PaniniProjector(Pointer p) {
            super(p);
        }

        public PaniniProjector position(long position) {
            return (PaniniProjector) super.position(position);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class PaniniWarper extends WarperCreator {
        private native void allocate();

        private native void allocate(float f, float f2);

        private native void allocateArray(long j);

        @Ptr
        public native RotationWarper create(float f);

        static {
            Loader.load();
        }

        public PaniniWarper(Pointer p) {
            super(p);
        }

        public PaniniWarper(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public PaniniWarper position(long position) {
            return (PaniniWarper) super.position(position);
        }

        public PaniniWarper(float A, float B) {
            super((Pointer) null);
            allocate(A, B);
        }

        public PaniniWarper() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::detail")
    public static class PlanePortraitProjector extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native void mapBackward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapBackward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapBackward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        public native void mapForward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapForward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapForward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        static {
            Loader.load();
        }

        public PlanePortraitProjector() {
            super((Pointer) null);
            allocate();
        }

        public PlanePortraitProjector(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public PlanePortraitProjector(Pointer p) {
            super(p);
        }

        public PlanePortraitProjector position(long position) {
            return (PlanePortraitProjector) super.position(position);
        }
    }

    @Namespace("cv::detail")
    public static class PlanePortraitWarper extends RotationWarper {
        private native void allocate(float f);

        static {
            Loader.load();
        }

        public PlanePortraitWarper(Pointer p) {
            super(p);
        }

        public PlanePortraitWarper(float scale) {
            super((Pointer) null);
            allocate(scale);
        }
    }

    @Namespace("cv::detail")
    public static class PlaneProjector extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native void mapBackward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapBackward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapBackward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        public native void mapForward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapForward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapForward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        static {
            Loader.load();
        }

        public PlaneProjector() {
            super((Pointer) null);
            allocate();
        }

        public PlaneProjector(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public PlaneProjector(Pointer p) {
            super(p);
        }

        public PlaneProjector position(long position) {
            return (PlaneProjector) super.position(position);
        }
    }

    @Namespace("cv")
    public static class PlaneWarper extends WarperCreator {
        private native void allocate();

        private native void allocateArray(long j);

        @Ptr
        public native RotationWarper create(float f);

        static {
            Loader.load();
        }

        public PlaneWarper() {
            super((Pointer) null);
            allocate();
        }

        public PlaneWarper(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public PlaneWarper(Pointer p) {
            super(p);
        }

        public PlaneWarper position(long position) {
            return (PlaneWarper) super.position(position);
        }
    }

    @Namespace("cv::detail")
    public static class ProjectorBase extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float m187k(int i);

        @MemberGetter
        public native FloatPointer m188k();

        public native ProjectorBase m189k(int i, float f);

        public native float k_rinv(int i);

        @MemberGetter
        public native FloatPointer k_rinv();

        public native ProjectorBase k_rinv(int i, float f);

        public native float r_kinv(int i);

        @MemberGetter
        public native FloatPointer r_kinv();

        public native ProjectorBase r_kinv(int i, float f);

        public native float rinv(int i);

        @MemberGetter
        public native FloatPointer rinv();

        public native ProjectorBase rinv(int i, float f);

        public native float scale();

        public native ProjectorBase scale(float f);

        public native void setCameraParams();

        public native void setCameraParams(@ByVal(nullValue = "cv::InputArray(cv::Mat::eye(3, 3, CV_32F))") Mat mat, @ByVal(nullValue = "cv::InputArray(cv::Mat::eye(3, 3, CV_32F))") Mat mat2, @ByVal(nullValue = "cv::InputArray(cv::Mat::zeros(3, 1, CV_32F))") Mat mat3);

        public native void setCameraParams(@ByVal(nullValue = "cv::InputArray(cv::Mat::eye(3, 3, CV_32F))") UMat uMat, @ByVal(nullValue = "cv::InputArray(cv::Mat::eye(3, 3, CV_32F))") UMat uMat2, @ByVal(nullValue = "cv::InputArray(cv::Mat::zeros(3, 1, CV_32F))") UMat uMat3);

        public native float m190t(int i);

        @MemberGetter
        public native FloatPointer m191t();

        public native ProjectorBase m192t(int i, float f);

        static {
            Loader.load();
        }

        public ProjectorBase() {
            super((Pointer) null);
            allocate();
        }

        public ProjectorBase(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public ProjectorBase(Pointer p) {
            super(p);
        }

        public ProjectorBase position(long position) {
            return (ProjectorBase) super.position(position);
        }
    }

    @Namespace("cv::detail")
    public static class SphericalPortraitProjector extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native void mapBackward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapBackward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapBackward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        public native void mapForward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapForward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapForward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        static {
            Loader.load();
        }

        public SphericalPortraitProjector() {
            super((Pointer) null);
            allocate();
        }

        public SphericalPortraitProjector(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public SphericalPortraitProjector(Pointer p) {
            super(p);
        }

        public SphericalPortraitProjector position(long position) {
            return (SphericalPortraitProjector) super.position(position);
        }
    }

    @Namespace("cv::detail")
    public static class SphericalPortraitWarper extends RotationWarper {
        private native void allocate(float f);

        static {
            Loader.load();
        }

        public SphericalPortraitWarper(Pointer p) {
            super(p);
        }

        public SphericalPortraitWarper(float scale) {
            super((Pointer) null);
            allocate(scale);
        }
    }

    @Namespace("cv::detail")
    public static class SphericalProjector extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native void mapBackward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapBackward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapBackward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        public native void mapForward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapForward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapForward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        static {
            Loader.load();
        }

        public SphericalProjector() {
            super((Pointer) null);
            allocate();
        }

        public SphericalProjector(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public SphericalProjector(Pointer p) {
            super(p);
        }

        public SphericalProjector position(long position) {
            return (SphericalProjector) super.position(position);
        }
    }

    @Namespace("cv")
    public static class SphericalWarper extends WarperCreator {
        private native void allocate();

        private native void allocateArray(long j);

        @Ptr
        public native RotationWarper create(float f);

        static {
            Loader.load();
        }

        public SphericalWarper() {
            super((Pointer) null);
            allocate();
        }

        public SphericalWarper(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public SphericalWarper(Pointer p) {
            super(p);
        }

        public SphericalWarper position(long position) {
            return (SphericalWarper) super.position(position);
        }
    }

    @Namespace("cv::detail")
    public static class StereographicProjector extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native void mapBackward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapBackward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapBackward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        public native void mapForward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapForward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapForward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        static {
            Loader.load();
        }

        public StereographicProjector() {
            super((Pointer) null);
            allocate();
        }

        public StereographicProjector(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public StereographicProjector(Pointer p) {
            super(p);
        }

        public StereographicProjector position(long position) {
            return (StereographicProjector) super.position(position);
        }
    }

    @Namespace("cv")
    public static class StereographicWarper extends WarperCreator {
        private native void allocate();

        private native void allocateArray(long j);

        @Ptr
        public native RotationWarper create(float f);

        static {
            Loader.load();
        }

        public StereographicWarper() {
            super((Pointer) null);
            allocate();
        }

        public StereographicWarper(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public StereographicWarper(Pointer p) {
            super(p);
        }

        public StereographicWarper position(long position) {
            return (StereographicWarper) super.position(position);
        }
    }

    @Namespace("cv")
    public static class Stitcher extends Pointer {
        public static final int ERR_CAMERA_PARAMS_ADJUST_FAIL = 3;
        public static final int ERR_HOMOGRAPHY_EST_FAIL = 2;
        public static final int ERR_NEED_MORE_IMGS = 1;
        public static final int OK = 0;
        public static final int ORIG_RESOL = -1;

        private native void allocate();

        private native void allocateArray(long j);

        @ByVal
        public static native Stitcher createDefault();

        @ByVal
        public static native Stitcher createDefault(@Cast({"bool"}) boolean z);

        @Ptr
        public native Blender blender();

        @Ptr
        public native BundleAdjusterBase bundleAdjuster();

        @StdVector
        public native CameraParams cameras();

        @StdVector
        public native IntPointer component();

        @Cast({"cv::Stitcher::Status"})
        public native int composePanorama(@ByVal Mat mat);

        @Cast({"cv::Stitcher::Status"})
        public native int composePanorama(@ByVal MatVector matVector, @ByVal Mat mat);

        @Cast({"cv::Stitcher::Status"})
        public native int composePanorama(@ByVal MatVector matVector, @ByVal UMat uMat);

        @Cast({"cv::Stitcher::Status"})
        public native int composePanorama(@ByVal UMat uMat);

        @Cast({"cv::Stitcher::Status"})
        public native int composePanorama(@ByVal UMatVector uMatVector, @ByVal Mat mat);

        @Cast({"cv::Stitcher::Status"})
        public native int composePanorama(@ByVal UMatVector uMatVector, @ByVal UMat uMat);

        public native double compositingResol();

        @Cast({"cv::Stitcher::Status"})
        public native int estimateTransform(@ByVal MatVector matVector);

        @Cast({"cv::Stitcher::Status"})
        public native int estimateTransform(@ByVal MatVector matVector, @ByRef @Const RectVectorVector rectVectorVector);

        @Cast({"cv::Stitcher::Status"})
        public native int estimateTransform(@ByVal UMatVector uMatVector);

        @Cast({"cv::Stitcher::Status"})
        public native int estimateTransform(@ByVal UMatVector uMatVector, @ByRef @Const RectVectorVector rectVectorVector);

        @Ptr
        public native ExposureCompensator exposureCompensator();

        @Ptr
        public native FeaturesFinder featuresFinder();

        @Ptr
        public native FeaturesMatcher featuresMatcher();

        @ByRef
        @Const
        public native UMat matchingMask();

        public native double panoConfidenceThresh();

        public native double registrationResol();

        public native double seamEstimationResol();

        @Ptr
        public native SeamFinder seamFinder();

        public native void setBlender(@Ptr Blender blender);

        public native void setBundleAdjuster(@Ptr BundleAdjusterBase bundleAdjusterBase);

        public native void setCompositingResol(double d);

        public native void setExposureCompensator(@Ptr ExposureCompensator exposureCompensator);

        public native void setFeaturesFinder(@Ptr FeaturesFinder featuresFinder);

        public native void setFeaturesMatcher(@Ptr FeaturesMatcher featuresMatcher);

        public native void setMatchingMask(@ByRef @Const UMat uMat);

        public native void setPanoConfidenceThresh(double d);

        public native void setRegistrationResol(double d);

        public native void setSeamEstimationResol(double d);

        public native void setSeamFinder(@Ptr SeamFinder seamFinder);

        public native void setWarper(@Ptr WarperCreator warperCreator);

        public native void setWaveCorrectKind(@Cast({"cv::detail::WaveCorrectKind"}) int i);

        public native void setWaveCorrection(@Cast({"bool"}) boolean z);

        @Cast({"cv::Stitcher::Status"})
        public native int stitch(@ByVal MatVector matVector, @ByVal Mat mat);

        @Cast({"cv::Stitcher::Status"})
        public native int stitch(@ByVal MatVector matVector, @ByRef @Const RectVectorVector rectVectorVector, @ByVal Mat mat);

        @Cast({"cv::Stitcher::Status"})
        public native int stitch(@ByVal MatVector matVector, @ByRef @Const RectVectorVector rectVectorVector, @ByVal UMat uMat);

        @Cast({"cv::Stitcher::Status"})
        public native int stitch(@ByVal MatVector matVector, @ByVal UMat uMat);

        @Cast({"cv::Stitcher::Status"})
        public native int stitch(@ByVal UMatVector uMatVector, @ByVal Mat mat);

        @Cast({"cv::Stitcher::Status"})
        public native int stitch(@ByVal UMatVector uMatVector, @ByRef @Const RectVectorVector rectVectorVector, @ByVal Mat mat);

        @Cast({"cv::Stitcher::Status"})
        public native int stitch(@ByVal UMatVector uMatVector, @ByRef @Const RectVectorVector rectVectorVector, @ByVal UMat uMat);

        @Cast({"cv::Stitcher::Status"})
        public native int stitch(@ByVal UMatVector uMatVector, @ByVal UMat uMat);

        @Ptr
        public native WarperCreator warper();

        @Cast({"cv::detail::WaveCorrectKind"})
        public native int waveCorrectKind();

        @Cast({"bool"})
        public native boolean waveCorrection();

        public native double workScale();

        static {
            Loader.load();
        }

        public Stitcher() {
            super((Pointer) null);
            allocate();
        }

        public Stitcher(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Stitcher(Pointer p) {
            super(p);
        }

        public Stitcher position(long position) {
            return (Stitcher) super.position(position);
        }
    }

    @Namespace("cv::detail")
    @NoOffset
    public static class SurfFeaturesFinder extends FeaturesFinder {
        private native void allocate();

        private native void allocate(double d, int i, int i2, int i3, int i4);

        private native void allocateArray(long j);

        static {
            Loader.load();
        }

        public SurfFeaturesFinder(Pointer p) {
            super(p);
        }

        public SurfFeaturesFinder(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public SurfFeaturesFinder position(long position) {
            return (SurfFeaturesFinder) super.position(position);
        }

        public SurfFeaturesFinder(double hess_thresh, int num_octaves, int num_layers, int num_octaves_descr, int num_layers_descr) {
            super((Pointer) null);
            allocate(hess_thresh, num_octaves, num_layers, num_octaves_descr, num_layers_descr);
        }

        public SurfFeaturesFinder() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv::detail")
    public static class Timelapser extends Pointer {
        public static final int AS_IS = 0;
        public static final int CROP = 1;

        private native void allocate();

        private native void allocateArray(long j);

        @Ptr
        public static native Timelapser createDefault(int i);

        @ByRef
        @Const
        public native UMat getDst();

        public native void initialize(@ByRef @Const PointVector pointVector, @ByRef @Const SizeVector sizeVector);

        public native void process(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Point point);

        public native void process(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal Point point);

        static {
            Loader.load();
        }

        public Timelapser() {
            super((Pointer) null);
            allocate();
        }

        public Timelapser(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Timelapser(Pointer p) {
            super(p);
        }

        public Timelapser position(long position) {
            return (Timelapser) super.position(position);
        }
    }

    @Namespace("cv::detail")
    public static class TimelapserCrop extends Timelapser {
        private native void allocate();

        private native void allocateArray(long j);

        public native void initialize(@ByRef @Const PointVector pointVector, @ByRef @Const SizeVector sizeVector);

        static {
            Loader.load();
        }

        public TimelapserCrop() {
            super((Pointer) null);
            allocate();
        }

        public TimelapserCrop(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public TimelapserCrop(Pointer p) {
            super(p);
        }

        public TimelapserCrop position(long position) {
            return (TimelapserCrop) super.position(position);
        }
    }

    @Namespace("cv::detail")
    public static class TransverseMercatorProjector extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native void mapBackward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapBackward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapBackward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        public native void mapForward(float f, float f2, @ByRef FloatBuffer floatBuffer, @ByRef FloatBuffer floatBuffer2);

        public native void mapForward(float f, float f2, @ByRef FloatPointer floatPointer, @ByRef FloatPointer floatPointer2);

        public native void mapForward(float f, float f2, @ByRef float[] fArr, @ByRef float[] fArr2);

        static {
            Loader.load();
        }

        public TransverseMercatorProjector() {
            super((Pointer) null);
            allocate();
        }

        public TransverseMercatorProjector(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public TransverseMercatorProjector(Pointer p) {
            super(p);
        }

        public TransverseMercatorProjector position(long position) {
            return (TransverseMercatorProjector) super.position(position);
        }
    }

    @Namespace("cv")
    public static class TransverseMercatorWarper extends WarperCreator {
        private native void allocate();

        private native void allocateArray(long j);

        @Ptr
        public native RotationWarper create(float f);

        static {
            Loader.load();
        }

        public TransverseMercatorWarper() {
            super((Pointer) null);
            allocate();
        }

        public TransverseMercatorWarper(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public TransverseMercatorWarper(Pointer p) {
            super(p);
        }

        public TransverseMercatorWarper position(long position) {
            return (TransverseMercatorWarper) super.position(position);
        }
    }

    @Namespace("cv::detail")
    public static class VoronoiSeamFinder extends PairwiseSeamFinder {
        private native void allocate();

        private native void allocateArray(long j);

        public native void find(@ByRef @Const SizeVector sizeVector, @ByRef @Const PointVector pointVector, @ByRef UMatVector uMatVector);

        public native void find(@ByRef @Const UMatVector uMatVector, @ByRef @Const PointVector pointVector, @ByRef UMatVector uMatVector2);

        static {
            Loader.load();
        }

        public VoronoiSeamFinder() {
            super((Pointer) null);
            allocate();
        }

        public VoronoiSeamFinder(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public VoronoiSeamFinder(Pointer p) {
            super(p);
        }

        public VoronoiSeamFinder position(long position) {
            return (VoronoiSeamFinder) super.position(position);
        }
    }

    @Namespace("cv::detail")
    @Cast({"bool"})
    public static native boolean calibrateRotatingCamera(@ByRef @Const MatVector matVector, @ByRef Mat mat);

    @Namespace("cv::detail")
    public static native void createLaplacePyr(@ByVal Mat mat, int i, @ByRef UMatVector uMatVector);

    @Namespace("cv::detail")
    public static native void createLaplacePyr(@ByVal UMat uMat, int i, @ByRef UMatVector uMatVector);

    @Namespace("cv::detail")
    public static native void createLaplacePyrGpu(@ByVal Mat mat, int i, @ByRef UMatVector uMatVector);

    @Namespace("cv::detail")
    public static native void createLaplacePyrGpu(@ByVal UMat uMat, int i, @ByRef UMatVector uMatVector);

    @Namespace("cv")
    @Ptr
    public static native Stitcher createStitcher();

    @Namespace("cv")
    @Ptr
    public static native Stitcher createStitcher(@Cast({"bool"}) boolean z);

    @Namespace("cv::detail")
    public static native void createWeightMap(@ByVal Mat mat, float f, @ByVal Mat mat2);

    @Namespace("cv::detail")
    public static native void createWeightMap(@ByVal UMat uMat, float f, @ByVal UMat uMat2);

    @Namespace("cv::detail")
    public static native void estimateFocal(@StdVector ImageFeatures imageFeatures, @StdVector MatchesInfo matchesInfo, @StdVector DoubleBuffer doubleBuffer);

    @Namespace("cv::detail")
    public static native void estimateFocal(@StdVector ImageFeatures imageFeatures, @StdVector MatchesInfo matchesInfo, @StdVector DoublePointer doublePointer);

    @Namespace("cv::detail")
    public static native void estimateFocal(@StdVector ImageFeatures imageFeatures, @StdVector MatchesInfo matchesInfo, @StdVector double[] dArr);

    @Namespace("cv::detail")
    public static native void findMaxSpanningTree(int i, @StdVector MatchesInfo matchesInfo, @ByRef Graph graph, @StdVector IntBuffer intBuffer);

    @Namespace("cv::detail")
    public static native void findMaxSpanningTree(int i, @StdVector MatchesInfo matchesInfo, @ByRef Graph graph, @StdVector IntPointer intPointer);

    @Namespace("cv::detail")
    public static native void findMaxSpanningTree(int i, @StdVector MatchesInfo matchesInfo, @ByRef Graph graph, @StdVector int[] iArr);

    @Namespace("cv::detail")
    public static native void focalsFromHomography(@ByRef @Const Mat mat, @ByRef DoubleBuffer doubleBuffer, @ByRef DoubleBuffer doubleBuffer2, @ByRef @Cast({"bool*"}) BoolPointer boolPointer, @ByRef @Cast({"bool*"}) BoolPointer boolPointer2);

    @Namespace("cv::detail")
    public static native void focalsFromHomography(@ByRef @Const Mat mat, @ByRef DoubleBuffer doubleBuffer, @ByRef DoubleBuffer doubleBuffer2, @ByRef @Cast({"bool*"}) boolean[] zArr, @ByRef @Cast({"bool*"}) boolean[] zArr2);

    @Namespace("cv::detail")
    public static native void focalsFromHomography(@ByRef @Const Mat mat, @ByRef DoublePointer doublePointer, @ByRef DoublePointer doublePointer2, @ByRef @Cast({"bool*"}) BoolPointer boolPointer, @ByRef @Cast({"bool*"}) BoolPointer boolPointer2);

    @Namespace("cv::detail")
    public static native void focalsFromHomography(@ByRef @Const Mat mat, @ByRef DoublePointer doublePointer, @ByRef DoublePointer doublePointer2, @ByRef @Cast({"bool*"}) boolean[] zArr, @ByRef @Cast({"bool*"}) boolean[] zArr2);

    @Namespace("cv::detail")
    public static native void focalsFromHomography(@ByRef @Const Mat mat, @ByRef double[] dArr, @ByRef double[] dArr2, @ByRef @Cast({"bool*"}) BoolPointer boolPointer, @ByRef @Cast({"bool*"}) BoolPointer boolPointer2);

    @Namespace("cv::detail")
    public static native void focalsFromHomography(@ByRef @Const Mat mat, @ByRef double[] dArr, @ByRef double[] dArr2, @ByRef @Cast({"bool*"}) boolean[] zArr, @ByRef @Cast({"bool*"}) boolean[] zArr2);

    @Namespace("cv::detail")
    @StdVector
    public static native IntPointer leaveBiggestComponent(@StdVector ImageFeatures imageFeatures, @StdVector MatchesInfo matchesInfo, float f);

    @Namespace("cv::detail")
    @Str
    public static native BytePointer matchesGraphAsString(@ByRef StringVector stringVector, @StdVector MatchesInfo matchesInfo, float f);

    @Namespace("cv::detail")
    public static native void normalizeUsingWeightMap(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv::detail")
    public static native void normalizeUsingWeightMap(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv::detail")
    @Cast({"bool"})
    public static native boolean overlapRoi(@ByVal Point point, @ByVal Point point2, @ByVal Size size, @ByVal Size size2, @ByRef Rect rect);

    @Namespace("cv::detail")
    public static native void restoreImageFromLaplacePyr(@ByRef UMatVector uMatVector);

    @Namespace("cv::detail")
    public static native void restoreImageFromLaplacePyrGpu(@ByRef UMatVector uMatVector);

    @Namespace("cv::detail")
    @ByVal
    public static native Rect resultRoi(@ByRef @Const PointVector pointVector, @ByRef @Const SizeVector sizeVector);

    @Namespace("cv::detail")
    @ByVal
    public static native Rect resultRoi(@ByRef @Const PointVector pointVector, @ByRef @Const UMatVector uMatVector);

    @Namespace("cv::detail")
    @ByVal
    public static native Rect resultRoiIntersection(@ByRef @Const PointVector pointVector, @ByRef @Const SizeVector sizeVector);

    @Namespace("cv::detail")
    @ByVal
    public static native Point resultTl(@ByRef @Const PointVector pointVector);

    @Namespace("cv::detail")
    public static native void selectRandomSubset(int i, int i2, @StdVector IntBuffer intBuffer);

    @Namespace("cv::detail")
    public static native void selectRandomSubset(int i, int i2, @StdVector IntPointer intPointer);

    @Namespace("cv::detail")
    public static native void selectRandomSubset(int i, int i2, @StdVector int[] iArr);

    @Namespace("cv::detail")
    @ByRef
    public static native IntPointer stitchingLogLevel();

    @Namespace("cv::detail")
    public static native void waveCorrect(@ByRef MatVector matVector, @Cast({"cv::detail::WaveCorrectKind"}) int i);

    static {
        Loader.load();
    }
}
