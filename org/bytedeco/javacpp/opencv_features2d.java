package org.bytedeco.javacpp;

import java.nio.ByteBuffer;
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
import org.bytedeco.javacpp.opencv_core.Algorithm;
import org.bytedeco.javacpp.opencv_core.ByteVectorVector;
import org.bytedeco.javacpp.opencv_core.DMatchVector;
import org.bytedeco.javacpp.opencv_core.DMatchVectorVector;
import org.bytedeco.javacpp.opencv_core.FileNode;
import org.bytedeco.javacpp.opencv_core.FileStorage;
import org.bytedeco.javacpp.opencv_core.IntVectorVector;
import org.bytedeco.javacpp.opencv_core.KeyPointVector;
import org.bytedeco.javacpp.opencv_core.KeyPointVectorVector;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Point2fVector;
import org.bytedeco.javacpp.opencv_core.PointVectorVector;
import org.bytedeco.javacpp.opencv_core.RectVector;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_core.TermCriteria;
import org.bytedeco.javacpp.opencv_core.UMat;
import org.bytedeco.javacpp.opencv_core.UMatVector;
import org.bytedeco.javacpp.presets.opencv_core.Ptr;
import org.bytedeco.javacpp.presets.opencv_core.Str;

public class opencv_features2d extends org.bytedeco.javacpp.presets.opencv_features2d {

    @Namespace("cv")
    public static class Feature2D extends Algorithm {
        private native void allocate();

        private native void allocateArray(long j);

        public native void compute(@ByVal Mat mat, @ByRef KeyPointVector keyPointVector, @ByVal Mat mat2);

        public native void compute(@ByVal MatVector matVector, @ByRef KeyPointVectorVector keyPointVectorVector, @ByVal MatVector matVector2);

        public native void compute(@ByVal UMat uMat, @ByRef KeyPointVector keyPointVector, @ByVal UMat uMat2);

        public native void compute(@ByVal UMatVector uMatVector, @ByRef KeyPointVectorVector keyPointVectorVector, @ByVal UMatVector uMatVector2);

        public native int defaultNorm();

        public native int descriptorSize();

        public native int descriptorType();

        public native void detect(@ByVal Mat mat, @ByRef KeyPointVector keyPointVector);

        public native void detect(@ByVal Mat mat, @ByRef KeyPointVector keyPointVector, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat2);

        public native void detect(@ByVal MatVector matVector, @ByRef KeyPointVectorVector keyPointVectorVector);

        public native void detect(@ByVal MatVector matVector, @ByRef KeyPointVectorVector keyPointVectorVector, @ByVal(nullValue = "cv::InputArrayOfArrays(cv::noArray())") MatVector matVector2);

        public native void detect(@ByVal UMat uMat, @ByRef KeyPointVector keyPointVector);

        public native void detect(@ByVal UMat uMat, @ByRef KeyPointVector keyPointVector, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat2);

        public native void detect(@ByVal UMatVector uMatVector, @ByRef KeyPointVectorVector keyPointVectorVector);

        public native void detect(@ByVal UMatVector uMatVector, @ByRef KeyPointVectorVector keyPointVectorVector, @ByVal(nullValue = "cv::InputArrayOfArrays(cv::noArray())") UMatVector uMatVector2);

        public native void detectAndCompute(@ByVal Mat mat, @ByVal Mat mat2, @ByRef KeyPointVector keyPointVector, @ByVal Mat mat3);

        public native void detectAndCompute(@ByVal Mat mat, @ByVal Mat mat2, @ByRef KeyPointVector keyPointVector, @ByVal Mat mat3, @Cast({"bool"}) boolean z);

        public native void detectAndCompute(@ByVal UMat uMat, @ByVal UMat uMat2, @ByRef KeyPointVector keyPointVector, @ByVal UMat uMat3);

        public native void detectAndCompute(@ByVal UMat uMat, @ByVal UMat uMat2, @ByRef KeyPointVector keyPointVector, @ByVal UMat uMat3, @Cast({"bool"}) boolean z);

        @Cast({"bool"})
        public native boolean empty();

        static {
            Loader.load();
        }

        public Feature2D() {
            super((Pointer) null);
            allocate();
        }

        public Feature2D(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Feature2D(Pointer p) {
            super(p);
        }

        public Feature2D position(long position) {
            return (Feature2D) super.position(position);
        }
    }

    @Namespace("cv")
    public static class AKAZE extends Feature2D {
        public static final int DESCRIPTOR_KAZE = 3;
        public static final int DESCRIPTOR_KAZE_UPRIGHT = 2;
        public static final int DESCRIPTOR_MLDB = 5;
        public static final int DESCRIPTOR_MLDB_UPRIGHT = 4;

        @Ptr
        public static native AKAZE create();

        @Ptr
        public static native AKAZE create(int i, int i2, int i3, float f, int i4, int i5, int i6);

        public native int getDescriptorChannels();

        public native int getDescriptorSize();

        public native int getDescriptorType();

        public native int getDiffusivity();

        public native int getNOctaveLayers();

        public native int getNOctaves();

        public native double getThreshold();

        public native void setDescriptorChannels(int i);

        public native void setDescriptorSize(int i);

        public native void setDescriptorType(int i);

        public native void setDiffusivity(int i);

        public native void setNOctaveLayers(int i);

        public native void setNOctaves(int i);

        public native void setThreshold(double d);

        static {
            Loader.load();
        }

        public AKAZE(Pointer p) {
            super(p);
        }
    }

    @Name({"cv::Accumulator<unsigned char>"})
    public static class Accumulator extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        static {
            Loader.load();
        }

        public Accumulator() {
            super((Pointer) null);
            allocate();
        }

        public Accumulator(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Accumulator(Pointer p) {
            super(p);
        }

        public Accumulator position(long position) {
            return (Accumulator) super.position(position);
        }
    }

    @Namespace("cv")
    public static class AgastFeatureDetector extends Feature2D {
        public static final int AGAST_5_8 = 0;
        public static final int AGAST_7_12d = 1;
        public static final int AGAST_7_12s = 2;
        public static final int NONMAX_SUPPRESSION = 10001;
        public static final int OAST_9_16 = 3;
        public static final int THRESHOLD = 10000;

        @Ptr
        public static native AgastFeatureDetector create();

        @Ptr
        public static native AgastFeatureDetector create(int i, @Cast({"bool"}) boolean z, int i2);

        @Cast({"bool"})
        public native boolean getNonmaxSuppression();

        public native int getThreshold();

        public native int getType();

        public native void setNonmaxSuppression(@Cast({"bool"}) boolean z);

        public native void setThreshold(int i);

        public native void setType(int i);

        static {
            Loader.load();
        }

        public AgastFeatureDetector(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class DescriptorMatcher extends Algorithm {
        @Ptr
        public static native DescriptorMatcher create(@Str String str);

        @Ptr
        public static native DescriptorMatcher create(@Str BytePointer bytePointer);

        public native void add(@ByVal MatVector matVector);

        public native void add(@ByVal UMatVector uMatVector);

        public native void clear();

        @Ptr
        public native DescriptorMatcher clone();

        @Ptr
        public native DescriptorMatcher clone(@Cast({"bool"}) boolean z);

        @Cast({"bool"})
        public native boolean empty();

        @ByRef
        @Const
        public native MatVector getTrainDescriptors();

        @Cast({"bool"})
        public native boolean isMaskSupported();

        public native void knnMatch(@ByVal Mat mat, @ByRef DMatchVectorVector dMatchVectorVector, int i);

        public native void knnMatch(@ByVal Mat mat, @ByRef DMatchVectorVector dMatchVectorVector, int i, @ByVal(nullValue = "cv::InputArrayOfArrays(cv::noArray())") MatVector matVector, @Cast({"bool"}) boolean z);

        public native void knnMatch(@ByVal Mat mat, @ByRef DMatchVectorVector dMatchVectorVector, int i, @ByVal(nullValue = "cv::InputArrayOfArrays(cv::noArray())") UMatVector uMatVector, @Cast({"bool"}) boolean z);

        public native void knnMatch(@ByVal Mat mat, @ByVal Mat mat2, @ByRef DMatchVectorVector dMatchVectorVector, int i);

        public native void knnMatch(@ByVal Mat mat, @ByVal Mat mat2, @ByRef DMatchVectorVector dMatchVectorVector, int i, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat3, @Cast({"bool"}) boolean z);

        public native void knnMatch(@ByVal UMat uMat, @ByRef DMatchVectorVector dMatchVectorVector, int i);

        public native void knnMatch(@ByVal UMat uMat, @ByRef DMatchVectorVector dMatchVectorVector, int i, @ByVal(nullValue = "cv::InputArrayOfArrays(cv::noArray())") MatVector matVector, @Cast({"bool"}) boolean z);

        public native void knnMatch(@ByVal UMat uMat, @ByRef DMatchVectorVector dMatchVectorVector, int i, @ByVal(nullValue = "cv::InputArrayOfArrays(cv::noArray())") UMatVector uMatVector, @Cast({"bool"}) boolean z);

        public native void knnMatch(@ByVal UMat uMat, @ByVal UMat uMat2, @ByRef DMatchVectorVector dMatchVectorVector, int i);

        public native void knnMatch(@ByVal UMat uMat, @ByVal UMat uMat2, @ByRef DMatchVectorVector dMatchVectorVector, int i, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat3, @Cast({"bool"}) boolean z);

        public native void match(@ByVal Mat mat, @ByRef DMatchVector dMatchVector);

        public native void match(@ByVal Mat mat, @ByRef DMatchVector dMatchVector, @ByVal(nullValue = "cv::InputArrayOfArrays(cv::noArray())") MatVector matVector);

        public native void match(@ByVal Mat mat, @ByRef DMatchVector dMatchVector, @ByVal(nullValue = "cv::InputArrayOfArrays(cv::noArray())") UMatVector uMatVector);

        public native void match(@ByVal Mat mat, @ByVal Mat mat2, @ByRef DMatchVector dMatchVector);

        public native void match(@ByVal Mat mat, @ByVal Mat mat2, @ByRef DMatchVector dMatchVector, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat3);

        public native void match(@ByVal UMat uMat, @ByRef DMatchVector dMatchVector);

        public native void match(@ByVal UMat uMat, @ByRef DMatchVector dMatchVector, @ByVal(nullValue = "cv::InputArrayOfArrays(cv::noArray())") MatVector matVector);

        public native void match(@ByVal UMat uMat, @ByRef DMatchVector dMatchVector, @ByVal(nullValue = "cv::InputArrayOfArrays(cv::noArray())") UMatVector uMatVector);

        public native void match(@ByVal UMat uMat, @ByVal UMat uMat2, @ByRef DMatchVector dMatchVector);

        public native void match(@ByVal UMat uMat, @ByVal UMat uMat2, @ByRef DMatchVector dMatchVector, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat3);

        public native void radiusMatch(@ByVal Mat mat, @ByRef DMatchVectorVector dMatchVectorVector, float f);

        public native void radiusMatch(@ByVal Mat mat, @ByRef DMatchVectorVector dMatchVectorVector, float f, @ByVal(nullValue = "cv::InputArrayOfArrays(cv::noArray())") MatVector matVector, @Cast({"bool"}) boolean z);

        public native void radiusMatch(@ByVal Mat mat, @ByRef DMatchVectorVector dMatchVectorVector, float f, @ByVal(nullValue = "cv::InputArrayOfArrays(cv::noArray())") UMatVector uMatVector, @Cast({"bool"}) boolean z);

        public native void radiusMatch(@ByVal Mat mat, @ByVal Mat mat2, @ByRef DMatchVectorVector dMatchVectorVector, float f);

        public native void radiusMatch(@ByVal Mat mat, @ByVal Mat mat2, @ByRef DMatchVectorVector dMatchVectorVector, float f, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat3, @Cast({"bool"}) boolean z);

        public native void radiusMatch(@ByVal UMat uMat, @ByRef DMatchVectorVector dMatchVectorVector, float f);

        public native void radiusMatch(@ByVal UMat uMat, @ByRef DMatchVectorVector dMatchVectorVector, float f, @ByVal(nullValue = "cv::InputArrayOfArrays(cv::noArray())") MatVector matVector, @Cast({"bool"}) boolean z);

        public native void radiusMatch(@ByVal UMat uMat, @ByRef DMatchVectorVector dMatchVectorVector, float f, @ByVal(nullValue = "cv::InputArrayOfArrays(cv::noArray())") UMatVector uMatVector, @Cast({"bool"}) boolean z);

        public native void radiusMatch(@ByVal UMat uMat, @ByVal UMat uMat2, @ByRef DMatchVectorVector dMatchVectorVector, float f);

        public native void radiusMatch(@ByVal UMat uMat, @ByVal UMat uMat2, @ByRef DMatchVectorVector dMatchVectorVector, float f, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat3, @Cast({"bool"}) boolean z);

        public native void read(@ByRef @Const FileNode fileNode);

        public native void train();

        public native void write(@ByRef FileStorage fileStorage);

        static {
            Loader.load();
        }

        public DescriptorMatcher(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class BFMatcher extends DescriptorMatcher {
        private native void allocate();

        private native void allocate(int i, @Cast({"bool"}) boolean z);

        private native void allocateArray(long j);

        @Ptr
        public native DescriptorMatcher clone();

        @Ptr
        public native DescriptorMatcher clone(@Cast({"bool"}) boolean z);

        @Cast({"bool"})
        public native boolean isMaskSupported();

        static {
            Loader.load();
        }

        public BFMatcher(Pointer p) {
            super(p);
        }

        public BFMatcher(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public BFMatcher position(long position) {
            return (BFMatcher) super.position(position);
        }

        public BFMatcher(int normType, @Cast({"bool"}) boolean crossCheck) {
            super((Pointer) null);
            allocate(normType, crossCheck);
        }

        public BFMatcher() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class BOWImgDescriptorExtractor extends Pointer {
        private native void allocate(@Ptr DescriptorMatcher descriptorMatcher);

        private native void allocate(@Ptr @Cast({"cv::DescriptorExtractor*"}) Feature2D feature2D, @Ptr DescriptorMatcher descriptorMatcher);

        public native void compute(@ByVal Mat mat, @ByRef KeyPointVector keyPointVector, @ByVal Mat mat2);

        public native void compute(@ByVal Mat mat, @ByRef KeyPointVector keyPointVector, @ByVal Mat mat2, IntVectorVector intVectorVector, Mat mat3);

        public native void compute(@ByVal Mat mat, @ByVal Mat mat2);

        public native void compute(@ByVal Mat mat, @ByVal Mat mat2, IntVectorVector intVectorVector);

        public native void compute(@ByVal UMat uMat, @ByRef KeyPointVector keyPointVector, @ByVal UMat uMat2);

        public native void compute(@ByVal UMat uMat, @ByRef KeyPointVector keyPointVector, @ByVal UMat uMat2, IntVectorVector intVectorVector, Mat mat);

        public native void compute(@ByVal UMat uMat, @ByVal UMat uMat2);

        public native void compute(@ByVal UMat uMat, @ByVal UMat uMat2, IntVectorVector intVectorVector);

        public native int descriptorSize();

        public native int descriptorType();

        @ByRef
        @Const
        public native Mat getVocabulary();

        public native void setVocabulary(@ByRef @Const Mat mat);

        static {
            Loader.load();
        }

        public BOWImgDescriptorExtractor(Pointer p) {
            super(p);
        }

        public BOWImgDescriptorExtractor(@Ptr @Cast({"cv::DescriptorExtractor*"}) Feature2D dextractor, @Ptr DescriptorMatcher dmatcher) {
            super((Pointer) null);
            allocate(dextractor, dmatcher);
        }

        public BOWImgDescriptorExtractor(@Ptr DescriptorMatcher dmatcher) {
            super((Pointer) null);
            allocate(dmatcher);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class BOWTrainer extends Pointer {
        public native void add(@ByRef @Const Mat mat);

        public native void clear();

        @ByVal
        public native Mat cluster();

        @ByVal
        public native Mat cluster(@ByRef @Const Mat mat);

        public native int descriptorsCount();

        @ByRef
        @Const
        public native MatVector getDescriptors();

        static {
            Loader.load();
        }

        public BOWTrainer(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class BOWKMeansTrainer extends BOWTrainer {
        private native void allocate(int i);

        private native void allocate(int i, @ByRef(nullValue = "cv::TermCriteria()") @Const TermCriteria termCriteria, int i2, int i3);

        @ByVal
        public native Mat cluster();

        @ByVal
        public native Mat cluster(@ByRef @Const Mat mat);

        static {
            Loader.load();
        }

        public BOWKMeansTrainer(Pointer p) {
            super(p);
        }

        public BOWKMeansTrainer(int clusterCount, @ByRef(nullValue = "cv::TermCriteria()") @Const TermCriteria termcrit, int attempts, int flags) {
            super((Pointer) null);
            allocate(clusterCount, termcrit, attempts, flags);
        }

        public BOWKMeansTrainer(int clusterCount) {
            super((Pointer) null);
            allocate(clusterCount);
        }
    }

    @Namespace("cv")
    public static class BRISK extends Feature2D {
        private native void allocate();

        private native void allocateArray(long j);

        @Ptr
        public static native BRISK create();

        @Ptr
        public static native BRISK create(int i, int i2, float f);

        @Ptr
        public static native BRISK create(@StdVector FloatBuffer floatBuffer, @StdVector IntBuffer intBuffer);

        @Ptr
        public static native BRISK create(@StdVector FloatBuffer floatBuffer, @StdVector IntBuffer intBuffer, float f, float f2, @StdVector IntBuffer intBuffer2);

        @Ptr
        public static native BRISK create(@StdVector FloatPointer floatPointer, @StdVector IntPointer intPointer);

        @Ptr
        public static native BRISK create(@StdVector FloatPointer floatPointer, @StdVector IntPointer intPointer, float f, float f2, @StdVector IntPointer intPointer2);

        @Ptr
        public static native BRISK create(@StdVector float[] fArr, @StdVector int[] iArr);

        @Ptr
        public static native BRISK create(@StdVector float[] fArr, @StdVector int[] iArr, float f, float f2, @StdVector int[] iArr2);

        static {
            Loader.load();
        }

        public BRISK() {
            super((Pointer) null);
            allocate();
        }

        public BRISK(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public BRISK(Pointer p) {
            super(p);
        }

        public BRISK position(long position) {
            return (BRISK) super.position(position);
        }
    }

    @Namespace("cv")
    public static class DrawMatchesFlags extends Pointer {
        public static final int DEFAULT = 0;
        public static final int DRAW_OVER_OUTIMG = 1;
        public static final int DRAW_RICH_KEYPOINTS = 4;
        public static final int NOT_DRAW_SINGLE_POINTS = 2;

        private native void allocate();

        private native void allocateArray(long j);

        static {
            Loader.load();
        }

        public DrawMatchesFlags() {
            super((Pointer) null);
            allocate();
        }

        public DrawMatchesFlags(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public DrawMatchesFlags(Pointer p) {
            super(p);
        }

        public DrawMatchesFlags position(long position) {
            return (DrawMatchesFlags) super.position(position);
        }
    }

    @Namespace("cv")
    public static class FastFeatureDetector extends Feature2D {
        public static final int FAST_N = 10002;
        public static final int NONMAX_SUPPRESSION = 10001;
        public static final int THRESHOLD = 10000;
        public static final int TYPE_5_8 = 0;
        public static final int TYPE_7_12 = 1;
        public static final int TYPE_9_16 = 2;

        @Ptr
        public static native FastFeatureDetector create();

        @Ptr
        public static native FastFeatureDetector create(int i, @Cast({"bool"}) boolean z, int i2);

        @Cast({"bool"})
        public native boolean getNonmaxSuppression();

        public native int getThreshold();

        public native int getType();

        public native void setNonmaxSuppression(@Cast({"bool"}) boolean z);

        public native void setThreshold(int i);

        public native void setType(int i);

        static {
            Loader.load();
        }

        public FastFeatureDetector(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class FlannBasedMatcher extends DescriptorMatcher {
        private native void allocate();

        private native void allocate(@Ptr opencv_flann$IndexParams org_bytedeco_javacpp_opencv_flann_IndexParams, @Ptr opencv_flann$SearchParams org_bytedeco_javacpp_opencv_flann_SearchParams);

        private native void allocateArray(long j);

        public native void add(@ByVal MatVector matVector);

        public native void add(@ByVal UMatVector uMatVector);

        public native void clear();

        @Ptr
        public native DescriptorMatcher clone();

        @Ptr
        public native DescriptorMatcher clone(@Cast({"bool"}) boolean z);

        @Cast({"bool"})
        public native boolean isMaskSupported();

        public native void read(@ByRef @Const FileNode fileNode);

        public native void train();

        public native void write(@ByRef FileStorage fileStorage);

        static {
            Loader.load();
        }

        public FlannBasedMatcher(Pointer p) {
            super(p);
        }

        public FlannBasedMatcher(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FlannBasedMatcher position(long position) {
            return (FlannBasedMatcher) super.position(position);
        }

        public FlannBasedMatcher(@Ptr opencv_flann$IndexParams indexParams, @Ptr opencv_flann$SearchParams searchParams) {
            super((Pointer) null);
            allocate(indexParams, searchParams);
        }

        public FlannBasedMatcher() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv")
    public static class GFTTDetector extends Feature2D {
        @Ptr
        public static native GFTTDetector create();

        @Ptr
        public static native GFTTDetector create(int i, double d, double d2, int i2, @Cast({"bool"}) boolean z, double d3);

        public native int getBlockSize();

        @Cast({"bool"})
        public native boolean getHarrisDetector();

        public native double getK();

        public native int getMaxFeatures();

        public native double getMinDistance();

        public native double getQualityLevel();

        public native void setBlockSize(int i);

        public native void setHarrisDetector(@Cast({"bool"}) boolean z);

        public native void setK(double d);

        public native void setMaxFeatures(int i);

        public native void setMinDistance(double d);

        public native void setQualityLevel(double d);

        static {
            Loader.load();
        }

        public GFTTDetector(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    public static class KAZE extends Feature2D {
        public static final int DIFF_CHARBONNIER = 3;
        public static final int DIFF_PM_G1 = 0;
        public static final int DIFF_PM_G2 = 1;
        public static final int DIFF_WEICKERT = 2;

        @Ptr
        public static native KAZE create();

        @Ptr
        public static native KAZE create(@Cast({"bool"}) boolean z, @Cast({"bool"}) boolean z2, float f, int i, int i2, int i3);

        public native int getDiffusivity();

        @Cast({"bool"})
        public native boolean getExtended();

        public native int getNOctaveLayers();

        public native int getNOctaves();

        public native double getThreshold();

        @Cast({"bool"})
        public native boolean getUpright();

        public native void setDiffusivity(int i);

        public native void setExtended(@Cast({"bool"}) boolean z);

        public native void setNOctaveLayers(int i);

        public native void setNOctaves(int i);

        public native void setThreshold(double d);

        public native void setUpright(@Cast({"bool"}) boolean z);

        static {
            Loader.load();
        }

        public KAZE(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    public static class KeyPointsFilter extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public static native void removeDuplicated(@ByRef KeyPointVector keyPointVector);

        public static native void retainBest(@ByRef KeyPointVector keyPointVector, int i);

        public static native void runByImageBorder(@ByRef KeyPointVector keyPointVector, @ByVal Size size, int i);

        public static native void runByKeypointSize(@ByRef KeyPointVector keyPointVector, float f);

        public static native void runByKeypointSize(@ByRef KeyPointVector keyPointVector, float f, float f2);

        public static native void runByPixelsMask(@ByRef KeyPointVector keyPointVector, @ByRef @Const Mat mat);

        static {
            Loader.load();
        }

        public KeyPointsFilter(Pointer p) {
            super(p);
        }

        public KeyPointsFilter(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public KeyPointsFilter position(long position) {
            return (KeyPointsFilter) super.position(position);
        }

        public KeyPointsFilter() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("cv")
    public static class MSER extends Feature2D {
        @Ptr
        public static native MSER create();

        @Ptr
        public static native MSER create(int i, int i2, int i3, double d, double d2, int i4, double d3, double d4, int i5);

        public native void detectRegions(@ByVal Mat mat, @ByRef PointVectorVector pointVectorVector, @ByRef RectVector rectVector);

        public native void detectRegions(@ByVal UMat uMat, @ByRef PointVectorVector pointVectorVector, @ByRef RectVector rectVector);

        public native int getDelta();

        public native int getMaxArea();

        public native int getMinArea();

        @Cast({"bool"})
        public native boolean getPass2Only();

        public native void setDelta(int i);

        public native void setMaxArea(int i);

        public native void setMinArea(int i);

        public native void setPass2Only(@Cast({"bool"}) boolean z);

        static {
            Loader.load();
        }

        public MSER(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    public static class ORB extends Feature2D {
        public static final int FAST_SCORE = 1;
        public static final int HARRIS_SCORE = 0;
        public static final int kBytes = 32;

        @Ptr
        public static native ORB create();

        @Ptr
        public static native ORB create(int i, float f, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

        public native int getEdgeThreshold();

        public native int getFastThreshold();

        public native int getFirstLevel();

        public native int getMaxFeatures();

        public native int getNLevels();

        public native int getPatchSize();

        public native double getScaleFactor();

        public native int getScoreType();

        public native int getWTA_K();

        public native void setEdgeThreshold(int i);

        public native void setFastThreshold(int i);

        public native void setFirstLevel(int i);

        public native void setMaxFeatures(int i);

        public native void setNLevels(int i);

        public native void setPatchSize(int i);

        public native void setScaleFactor(double d);

        public native void setScoreType(int i);

        public native void setWTA_K(int i);

        static {
            Loader.load();
        }

        public ORB(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    public static class SimpleBlobDetector extends Feature2D {

        @NoOffset
        public static class Params extends Pointer {
            private native void allocate();

            private native void allocateArray(long j);

            @Cast({"uchar"})
            public native byte blobColor();

            public native Params blobColor(byte b);

            public native Params filterByArea(boolean z);

            @Cast({"bool"})
            public native boolean filterByArea();

            public native Params filterByCircularity(boolean z);

            @Cast({"bool"})
            public native boolean filterByCircularity();

            public native Params filterByColor(boolean z);

            @Cast({"bool"})
            public native boolean filterByColor();

            public native Params filterByConvexity(boolean z);

            @Cast({"bool"})
            public native boolean filterByConvexity();

            public native Params filterByInertia(boolean z);

            @Cast({"bool"})
            public native boolean filterByInertia();

            public native float maxArea();

            public native Params maxArea(float f);

            public native float maxCircularity();

            public native Params maxCircularity(float f);

            public native float maxConvexity();

            public native Params maxConvexity(float f);

            public native float maxInertiaRatio();

            public native Params maxInertiaRatio(float f);

            public native float maxThreshold();

            public native Params maxThreshold(float f);

            public native float minArea();

            public native Params minArea(float f);

            public native float minCircularity();

            public native Params minCircularity(float f);

            public native float minConvexity();

            public native Params minConvexity(float f);

            public native float minDistBetweenBlobs();

            public native Params minDistBetweenBlobs(float f);

            public native float minInertiaRatio();

            public native Params minInertiaRatio(float f);

            @Cast({"size_t"})
            public native long minRepeatability();

            public native Params minRepeatability(long j);

            public native float minThreshold();

            public native Params minThreshold(float f);

            public native void read(@ByRef @Const FileNode fileNode);

            public native float thresholdStep();

            public native Params thresholdStep(float f);

            public native void write(@ByRef FileStorage fileStorage);

            static {
                Loader.load();
            }

            public Params(Pointer p) {
                super(p);
            }

            public Params(long size) {
                super((Pointer) null);
                allocateArray(size);
            }

            public Params position(long position) {
                return (Params) super.position(position);
            }

            public Params() {
                super((Pointer) null);
                allocate();
            }
        }

        private native void allocate();

        private native void allocateArray(long j);

        @Ptr
        public static native SimpleBlobDetector create();

        @Ptr
        public static native SimpleBlobDetector create(@ByRef(nullValue = "cv::SimpleBlobDetector::Params()") @Const Params params);

        static {
            Loader.load();
        }

        public SimpleBlobDetector() {
            super((Pointer) null);
            allocate();
        }

        public SimpleBlobDetector(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public SimpleBlobDetector(Pointer p) {
            super(p);
        }

        public SimpleBlobDetector position(long position) {
            return (SimpleBlobDetector) super.position(position);
        }
    }

    @Namespace("cv")
    public static native void AGAST(@ByVal Mat mat, @ByRef KeyPointVector keyPointVector, int i);

    @Namespace("cv")
    public static native void AGAST(@ByVal Mat mat, @ByRef KeyPointVector keyPointVector, int i, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void AGAST(@ByVal Mat mat, @ByRef KeyPointVector keyPointVector, int i, @Cast({"bool"}) boolean z, int i2);

    @Namespace("cv")
    public static native void AGAST(@ByVal UMat uMat, @ByRef KeyPointVector keyPointVector, int i);

    @Namespace("cv")
    public static native void AGAST(@ByVal UMat uMat, @ByRef KeyPointVector keyPointVector, int i, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void AGAST(@ByVal UMat uMat, @ByRef KeyPointVector keyPointVector, int i, @Cast({"bool"}) boolean z, int i2);

    @Namespace("cv")
    public static native void FAST(@ByVal Mat mat, @ByRef KeyPointVector keyPointVector, int i);

    @Namespace("cv")
    public static native void FAST(@ByVal Mat mat, @ByRef KeyPointVector keyPointVector, int i, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void FAST(@ByVal Mat mat, @ByRef KeyPointVector keyPointVector, int i, @Cast({"bool"}) boolean z, int i2);

    @Namespace("cv")
    public static native void FAST(@ByVal UMat uMat, @ByRef KeyPointVector keyPointVector, int i);

    @Namespace("cv")
    public static native void FAST(@ByVal UMat uMat, @ByRef KeyPointVector keyPointVector, int i, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void FAST(@ByVal UMat uMat, @ByRef KeyPointVector keyPointVector, int i, @Cast({"bool"}) boolean z, int i2);

    @Namespace("cv")
    public static native void computeRecallPrecisionCurve(@ByRef @Const DMatchVectorVector dMatchVectorVector, @ByRef @Cast({"const std::vector<std::vector<uchar> >*"}) ByteVectorVector byteVectorVector, @ByRef Point2fVector point2fVector);

    @Namespace("cv")
    public static native void drawKeypoints(@ByVal Mat mat, @ByRef @Const KeyPointVector keyPointVector, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void drawKeypoints(@ByVal Mat mat, @ByRef @Const KeyPointVector keyPointVector, @ByVal Mat mat2, @ByRef(nullValue = "cv::Scalar::all(-1)") @Const Scalar scalar, int i);

    @Namespace("cv")
    public static native void drawKeypoints(@ByVal UMat uMat, @ByRef @Const KeyPointVector keyPointVector, @ByVal UMat uMat2);

    @Namespace("cv")
    public static native void drawKeypoints(@ByVal UMat uMat, @ByRef @Const KeyPointVector keyPointVector, @ByVal UMat uMat2, @ByRef(nullValue = "cv::Scalar::all(-1)") @Const Scalar scalar, int i);

    @Namespace("cv")
    public static native void drawMatches(@ByVal Mat mat, @ByRef @Const KeyPointVector keyPointVector, @ByVal Mat mat2, @ByRef @Const KeyPointVector keyPointVector2, @ByRef @Const DMatchVector dMatchVector, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void drawMatches(@ByVal Mat mat, @ByRef @Const KeyPointVector keyPointVector, @ByVal Mat mat2, @ByRef @Const KeyPointVector keyPointVector2, @ByRef @Const DMatchVector dMatchVector, @ByVal Mat mat3, @ByRef(nullValue = "cv::Scalar::all(-1)") @Const Scalar scalar, @ByRef(nullValue = "cv::Scalar::all(-1)") @Const Scalar scalar2, @Cast({"char*"}) @StdVector ByteBuffer byteBuffer, int i);

    @Namespace("cv")
    public static native void drawMatches(@ByVal Mat mat, @ByRef @Const KeyPointVector keyPointVector, @ByVal Mat mat2, @ByRef @Const KeyPointVector keyPointVector2, @ByRef @Const DMatchVector dMatchVector, @ByVal Mat mat3, @ByRef(nullValue = "cv::Scalar::all(-1)") @Const Scalar scalar, @ByRef(nullValue = "cv::Scalar::all(-1)") @Const Scalar scalar2, @Cast({"char*"}) @StdVector BytePointer bytePointer, int i);

    @Namespace("cv")
    public static native void drawMatches(@ByVal UMat uMat, @ByRef @Const KeyPointVector keyPointVector, @ByVal UMat uMat2, @ByRef @Const KeyPointVector keyPointVector2, @ByRef @Const DMatchVector dMatchVector, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native void drawMatches(@ByVal UMat uMat, @ByRef @Const KeyPointVector keyPointVector, @ByVal UMat uMat2, @ByRef @Const KeyPointVector keyPointVector2, @ByRef @Const DMatchVector dMatchVector, @ByVal UMat uMat3, @ByRef(nullValue = "cv::Scalar::all(-1)") @Const Scalar scalar, @ByRef(nullValue = "cv::Scalar::all(-1)") @Const Scalar scalar2, @Cast({"char*"}) @StdVector BytePointer bytePointer, int i);

    @Namespace("cv")
    public static native void drawMatches(@ByVal UMat uMat, @ByRef @Const KeyPointVector keyPointVector, @ByVal UMat uMat2, @ByRef @Const KeyPointVector keyPointVector2, @ByRef @Const DMatchVector dMatchVector, @ByVal UMat uMat3, @ByRef(nullValue = "cv::Scalar::all(-1)") @Const Scalar scalar, @ByRef(nullValue = "cv::Scalar::all(-1)") @Const Scalar scalar2, @Cast({"char*"}) @StdVector byte[] bArr, int i);

    @Namespace("cv")
    @Name({"drawMatches"})
    public static native void drawMatchesKnn(@ByVal Mat mat, @ByRef @Const KeyPointVector keyPointVector, @ByVal Mat mat2, @ByRef @Const KeyPointVector keyPointVector2, @ByRef @Const DMatchVectorVector dMatchVectorVector, @ByVal Mat mat3);

    @Namespace("cv")
    @Name({"drawMatches"})
    public static native void drawMatchesKnn(@ByVal Mat mat, @ByRef @Const KeyPointVector keyPointVector, @ByVal Mat mat2, @ByRef @Const KeyPointVector keyPointVector2, @ByRef @Const DMatchVectorVector dMatchVectorVector, @ByVal Mat mat3, @ByRef(nullValue = "cv::Scalar::all(-1)") @Const Scalar scalar, @ByRef(nullValue = "cv::Scalar::all(-1)") @Const Scalar scalar2, @ByRef(nullValue = "std::vector<std::vector<char> >()") @Cast({"const std::vector<std::vector<char> >*"}) ByteVectorVector byteVectorVector, int i);

    @Namespace("cv")
    @Name({"drawMatches"})
    public static native void drawMatchesKnn(@ByVal UMat uMat, @ByRef @Const KeyPointVector keyPointVector, @ByVal UMat uMat2, @ByRef @Const KeyPointVector keyPointVector2, @ByRef @Const DMatchVectorVector dMatchVectorVector, @ByVal UMat uMat3);

    @Namespace("cv")
    @Name({"drawMatches"})
    public static native void drawMatchesKnn(@ByVal UMat uMat, @ByRef @Const KeyPointVector keyPointVector, @ByVal UMat uMat2, @ByRef @Const KeyPointVector keyPointVector2, @ByRef @Const DMatchVectorVector dMatchVectorVector, @ByVal UMat uMat3, @ByRef(nullValue = "cv::Scalar::all(-1)") @Const Scalar scalar, @ByRef(nullValue = "cv::Scalar::all(-1)") @Const Scalar scalar2, @ByRef(nullValue = "std::vector<std::vector<char> >()") @Cast({"const std::vector<std::vector<char> >*"}) ByteVectorVector byteVectorVector, int i);

    @Namespace("cv")
    public static native void evaluateFeatureDetector(@ByRef @Const Mat mat, @ByRef @Const Mat mat2, @ByRef @Const Mat mat3, KeyPointVector keyPointVector, KeyPointVector keyPointVector2, @ByRef FloatBuffer floatBuffer, @ByRef IntBuffer intBuffer);

    @Namespace("cv")
    public static native void evaluateFeatureDetector(@ByRef @Const Mat mat, @ByRef @Const Mat mat2, @ByRef @Const Mat mat3, KeyPointVector keyPointVector, KeyPointVector keyPointVector2, @ByRef FloatBuffer floatBuffer, @ByRef IntBuffer intBuffer, @Ptr @Cast({"cv::FeatureDetector*"}) Feature2D feature2D);

    @Namespace("cv")
    public static native void evaluateFeatureDetector(@ByRef @Const Mat mat, @ByRef @Const Mat mat2, @ByRef @Const Mat mat3, KeyPointVector keyPointVector, KeyPointVector keyPointVector2, @ByRef FloatPointer floatPointer, @ByRef IntPointer intPointer);

    @Namespace("cv")
    public static native void evaluateFeatureDetector(@ByRef @Const Mat mat, @ByRef @Const Mat mat2, @ByRef @Const Mat mat3, KeyPointVector keyPointVector, KeyPointVector keyPointVector2, @ByRef FloatPointer floatPointer, @ByRef IntPointer intPointer, @Ptr @Cast({"cv::FeatureDetector*"}) Feature2D feature2D);

    @Namespace("cv")
    public static native void evaluateFeatureDetector(@ByRef @Const Mat mat, @ByRef @Const Mat mat2, @ByRef @Const Mat mat3, KeyPointVector keyPointVector, KeyPointVector keyPointVector2, @ByRef float[] fArr, @ByRef int[] iArr);

    @Namespace("cv")
    public static native void evaluateFeatureDetector(@ByRef @Const Mat mat, @ByRef @Const Mat mat2, @ByRef @Const Mat mat3, KeyPointVector keyPointVector, KeyPointVector keyPointVector2, @ByRef float[] fArr, @ByRef int[] iArr, @Ptr @Cast({"cv::FeatureDetector*"}) Feature2D feature2D);

    @Namespace("cv")
    public static native int getNearestPoint(@ByRef @Const Point2fVector point2fVector, float f);

    @Namespace("cv")
    public static native float getRecall(@ByRef @Const Point2fVector point2fVector, float f);

    static {
        Loader.load();
    }
}
