package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoOffset;
import org.bytedeco.javacpp.opencv_calib3d.StereoMatcher;
import org.bytedeco.javacpp.opencv_core.Algorithm;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Scalar4i;
import org.bytedeco.javacpp.opencv_core.UMat;
import org.bytedeco.javacpp.presets.opencv_core.Ptr;
import org.bytedeco.javacpp.presets.opencv_core.Str;

public class opencv_ximgproc extends org.bytedeco.javacpp.presets.opencv_ximgproc {
    public static final int AM_FILTER = 4;
    public static final int ARO_0_45 = 0;
    public static final int ARO_315_0 = 3;
    public static final int ARO_315_135 = 6;
    public static final int ARO_315_45 = 4;
    public static final int ARO_45_135 = 5;
    public static final int ARO_45_90 = 1;
    public static final int ARO_90_135 = 2;
    public static final int ARO_CTR_HOR = 7;
    public static final int ARO_CTR_VER = 8;
    public static final int DTF_IC = 1;
    public static final int DTF_NC = 0;
    public static final int DTF_RF = 2;
    public static final int FHT_ADD = 2;
    public static final int FHT_AVE = 3;
    public static final int FHT_MAX = 1;
    public static final int FHT_MIN = 0;
    public static final int GUIDED_FILTER = 3;
    public static final int HDO_DESKEW = 1;
    public static final int HDO_RAW = 0;
    public static final int RO_IGNORE_BORDERS = 1;
    public static final int RO_STRICT = 0;
    public static final int SLIC = 100;
    public static final int SLICO = 101;

    @Namespace("cv::ximgproc")
    public static class AdaptiveManifoldFilter extends Algorithm {
        @Ptr
        public static native AdaptiveManifoldFilter create();

        public native void collectGarbage();

        public native void filter(@ByVal Mat mat, @ByVal Mat mat2);

        public native void filter(@ByVal Mat mat, @ByVal Mat mat2, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat3);

        public native void filter(@ByVal UMat uMat, @ByVal UMat uMat2);

        public native void filter(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat3);

        @Cast({"bool"})
        public native boolean getAdjustOutliers();

        public native int getPCAIterations();

        public native double getSigmaR();

        public native double getSigmaS();

        public native int getTreeHeight();

        @Cast({"bool"})
        public native boolean getUseRNG();

        public native void setAdjustOutliers(@Cast({"bool"}) boolean z);

        public native void setPCAIterations(int i);

        public native void setSigmaR(double d);

        public native void setSigmaS(double d);

        public native void setTreeHeight(int i);

        public native void setUseRNG(@Cast({"bool"}) boolean z);

        static {
            Loader.load();
        }

        public AdaptiveManifoldFilter(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ximgproc")
    public static class DTFilter extends Algorithm {
        public native void filter(@ByVal Mat mat, @ByVal Mat mat2);

        public native void filter(@ByVal Mat mat, @ByVal Mat mat2, int i);

        public native void filter(@ByVal UMat uMat, @ByVal UMat uMat2);

        public native void filter(@ByVal UMat uMat, @ByVal UMat uMat2, int i);

        static {
            Loader.load();
        }

        public DTFilter(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ximgproc")
    public static class DisparityFilter extends Algorithm {
        public native void filter(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

        public native void filter(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal(nullValue = "cv::InputArray(cv::Mat())") Mat mat4, @ByVal(nullValue = "cv::Rect()") Rect rect, @ByVal(nullValue = "cv::InputArray(cv::Mat())") Mat mat5);

        public native void filter(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

        public native void filter(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal(nullValue = "cv::InputArray(cv::Mat())") UMat uMat4, @ByVal(nullValue = "cv::Rect()") Rect rect, @ByVal(nullValue = "cv::InputArray(cv::Mat())") UMat uMat5);

        static {
            Loader.load();
        }

        public DisparityFilter(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ximgproc")
    public static class DisparityWLSFilter extends DisparityFilter {
        @ByVal
        public native Mat getConfidenceMap();

        public native int getDepthDiscontinuityRadius();

        public native int getLRCthresh();

        public native double getLambda();

        @ByVal
        public native Rect getROI();

        public native double getSigmaColor();

        public native void setDepthDiscontinuityRadius(int i);

        public native void setLRCthresh(int i);

        public native void setLambda(double d);

        public native void setSigmaColor(double d);

        static {
            Loader.load();
        }

        public DisparityWLSFilter(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ximgproc::segmentation")
    public static class Edge extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native int from();

        public native Edge from(int i);

        @Cast({"bool"})
        @Name({"operator <"})
        public native boolean lessThan(@ByRef @Const Edge edge);

        public native int to();

        public native Edge to(int i);

        public native float weight();

        public native Edge weight(float f);

        static {
            Loader.load();
        }

        public Edge() {
            super((Pointer) null);
            allocate();
        }

        public Edge(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Edge(Pointer p) {
            super(p);
        }

        public Edge position(long position) {
            return (Edge) super.position(position);
        }
    }

    @Namespace("cv::ximgproc")
    public static class SparseMatchInterpolator extends Algorithm {
        public native void interpolate(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5);

        public native void interpolate(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5);

        static {
            Loader.load();
        }

        public SparseMatchInterpolator(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ximgproc")
    public static class EdgeAwareInterpolator extends SparseMatchInterpolator {
        public native float getFGSLambda();

        public native float getFGSSigma();

        public native int getK();

        public native float getLambda();

        public native float getSigma();

        @Cast({"bool"})
        public native boolean getUsePostProcessing();

        public native void setFGSLambda(float f);

        public native void setFGSSigma(float f);

        public native void setK(int i);

        public native void setLambda(float f);

        public native void setSigma(float f);

        public native void setUsePostProcessing(@Cast({"bool"}) boolean z);

        static {
            Loader.load();
        }

        public EdgeAwareInterpolator(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ximgproc")
    public static class FastGlobalSmootherFilter extends Algorithm {
        public native void filter(@ByVal Mat mat, @ByVal Mat mat2);

        public native void filter(@ByVal UMat uMat, @ByVal UMat uMat2);

        static {
            Loader.load();
        }

        public FastGlobalSmootherFilter(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ximgproc::segmentation")
    public static class GraphSegmentation extends Algorithm {
        public native float getK();

        public native int getMinSize();

        public native double getSigma();

        public native void processImage(@ByVal Mat mat, @ByVal Mat mat2);

        public native void processImage(@ByVal UMat uMat, @ByVal UMat uMat2);

        public native void setK(float f);

        public native void setMinSize(int i);

        public native void setSigma(double d);

        static {
            Loader.load();
        }

        public GraphSegmentation(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ximgproc")
    public static class GuidedFilter extends Algorithm {
        public native void filter(@ByVal Mat mat, @ByVal Mat mat2);

        public native void filter(@ByVal Mat mat, @ByVal Mat mat2, int i);

        public native void filter(@ByVal UMat uMat, @ByVal UMat uMat2);

        public native void filter(@ByVal UMat uMat, @ByVal UMat uMat2, int i);

        static {
            Loader.load();
        }

        public GuidedFilter(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ximgproc::segmentation")
    @NoOffset
    public static class PointSetElement extends Pointer {
        private native void allocate();

        private native void allocate(int i);

        private native void allocateArray(long j);

        public native int m193p();

        public native PointSetElement m194p(int i);

        public native int size();

        public native PointSetElement size(int i);

        static {
            Loader.load();
        }

        public PointSetElement(Pointer p) {
            super(p);
        }

        public PointSetElement(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public PointSetElement position(long position) {
            return (PointSetElement) super.position(position);
        }

        public PointSetElement() {
            super((Pointer) null);
            allocate();
        }

        public PointSetElement(int p_) {
            super((Pointer) null);
            allocate(p_);
        }
    }

    @Namespace("cv::ximgproc")
    public static class RFFeatureGetter extends Algorithm {
        public native void getFeatures(@ByRef @Const Mat mat, @ByRef Mat mat2, int i, int i2, int i3, int i4, int i5);

        static {
            Loader.load();
        }

        public RFFeatureGetter(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ximgproc")
    public static class StructuredEdgeDetection extends Algorithm {
        public native void detectEdges(@ByRef @Const Mat mat, @ByRef Mat mat2);

        static {
            Loader.load();
        }

        public StructuredEdgeDetection(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ximgproc")
    public static class SuperpixelLSC extends Algorithm {
        public native void enforceLabelConnectivity();

        public native void enforceLabelConnectivity(int i);

        public native void getLabelContourMask(@ByVal Mat mat);

        public native void getLabelContourMask(@ByVal Mat mat, @Cast({"bool"}) boolean z);

        public native void getLabelContourMask(@ByVal UMat uMat);

        public native void getLabelContourMask(@ByVal UMat uMat, @Cast({"bool"}) boolean z);

        public native void getLabels(@ByVal Mat mat);

        public native void getLabels(@ByVal UMat uMat);

        public native int getNumberOfSuperpixels();

        public native void iterate();

        public native void iterate(int i);

        static {
            Loader.load();
        }

        public SuperpixelLSC(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ximgproc")
    public static class SuperpixelSEEDS extends Algorithm {
        public native void getLabelContourMask(@ByVal Mat mat);

        public native void getLabelContourMask(@ByVal Mat mat, @Cast({"bool"}) boolean z);

        public native void getLabelContourMask(@ByVal UMat uMat);

        public native void getLabelContourMask(@ByVal UMat uMat, @Cast({"bool"}) boolean z);

        public native void getLabels(@ByVal Mat mat);

        public native void getLabels(@ByVal UMat uMat);

        public native int getNumberOfSuperpixels();

        public native void iterate(@ByVal Mat mat);

        public native void iterate(@ByVal Mat mat, int i);

        public native void iterate(@ByVal UMat uMat);

        public native void iterate(@ByVal UMat uMat, int i);

        static {
            Loader.load();
        }

        public SuperpixelSEEDS(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ximgproc")
    public static class SuperpixelSLIC extends Algorithm {
        public native void enforceLabelConnectivity();

        public native void enforceLabelConnectivity(int i);

        public native void getLabelContourMask(@ByVal Mat mat);

        public native void getLabelContourMask(@ByVal Mat mat, @Cast({"bool"}) boolean z);

        public native void getLabelContourMask(@ByVal UMat uMat);

        public native void getLabelContourMask(@ByVal UMat uMat, @Cast({"bool"}) boolean z);

        public native void getLabels(@ByVal Mat mat);

        public native void getLabels(@ByVal UMat uMat);

        public native int getNumberOfSuperpixels();

        public native void iterate();

        public native void iterate(int i);

        static {
            Loader.load();
        }

        public SuperpixelSLIC(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv::ximgproc")
    public static native void FastHoughTransform(@ByVal Mat mat, @ByVal Mat mat2, int i);

    @Namespace("cv::ximgproc")
    public static native void FastHoughTransform(@ByVal Mat mat, @ByVal Mat mat2, int i, int i2, int i3, int i4);

    @Namespace("cv::ximgproc")
    public static native void FastHoughTransform(@ByVal UMat uMat, @ByVal UMat uMat2, int i);

    @Namespace("cv::ximgproc")
    public static native void FastHoughTransform(@ByVal UMat uMat, @ByVal UMat uMat2, int i, int i2, int i3, int i4);

    @Namespace("cv::ximgproc")
    @ByVal
    public static native Scalar4i HoughPoint2Line(@ByRef @Const Point point, @ByVal Mat mat);

    @Namespace("cv::ximgproc")
    @ByVal
    public static native Scalar4i HoughPoint2Line(@ByRef @Const Point point, @ByVal Mat mat, int i, int i2, int i3);

    @Namespace("cv::ximgproc")
    @ByVal
    public static native Scalar4i HoughPoint2Line(@ByRef @Const Point point, @ByVal UMat uMat);

    @Namespace("cv::ximgproc")
    @ByVal
    public static native Scalar4i HoughPoint2Line(@ByRef @Const Point point, @ByVal UMat uMat, int i, int i2, int i3);

    @Namespace("cv::ximgproc")
    public static native void amFilter(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, double d, double d2);

    @Namespace("cv::ximgproc")
    public static native void amFilter(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, double d, double d2, @Cast({"bool"}) boolean z);

    @Namespace("cv::ximgproc")
    public static native void amFilter(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, double d, double d2);

    @Namespace("cv::ximgproc")
    public static native void amFilter(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, double d, double d2, @Cast({"bool"}) boolean z);

    @Namespace("cv::ximgproc")
    public static native double computeBadPixelPercent(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Rect rect);

    @Namespace("cv::ximgproc")
    public static native double computeBadPixelPercent(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Rect rect, int i);

    @Namespace("cv::ximgproc")
    public static native double computeBadPixelPercent(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal Rect rect);

    @Namespace("cv::ximgproc")
    public static native double computeBadPixelPercent(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal Rect rect, int i);

    @Namespace("cv::ximgproc")
    public static native double computeMSE(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Rect rect);

    @Namespace("cv::ximgproc")
    public static native double computeMSE(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal Rect rect);

    @Namespace("cv::ximgproc")
    public static native void covarianceEstimation(@ByVal Mat mat, @ByVal Mat mat2, int i, int i2);

    @Namespace("cv::ximgproc")
    public static native void covarianceEstimation(@ByVal UMat uMat, @ByVal UMat uMat2, int i, int i2);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native AdaptiveManifoldFilter createAMFilter(double d, double d2);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native AdaptiveManifoldFilter createAMFilter(double d, double d2, @Cast({"bool"}) boolean z);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native DTFilter createDTFilter(@ByVal Mat mat, double d, double d2);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native DTFilter createDTFilter(@ByVal Mat mat, double d, double d2, int i, int i2);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native DTFilter createDTFilter(@ByVal UMat uMat, double d, double d2);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native DTFilter createDTFilter(@ByVal UMat uMat, double d, double d2, int i, int i2);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native DisparityWLSFilter createDisparityWLSFilter(@Ptr StereoMatcher stereoMatcher);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native DisparityWLSFilter createDisparityWLSFilterGeneric(@Cast({"bool"}) boolean z);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native EdgeAwareInterpolator createEdgeAwareInterpolator();

    @Namespace("cv::ximgproc")
    @Ptr
    public static native FastGlobalSmootherFilter createFastGlobalSmootherFilter(@ByVal Mat mat, double d, double d2);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native FastGlobalSmootherFilter createFastGlobalSmootherFilter(@ByVal Mat mat, double d, double d2, double d3, int i);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native FastGlobalSmootherFilter createFastGlobalSmootherFilter(@ByVal UMat uMat, double d, double d2);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native FastGlobalSmootherFilter createFastGlobalSmootherFilter(@ByVal UMat uMat, double d, double d2, double d3, int i);

    @Namespace("cv::ximgproc::segmentation")
    @Ptr
    public static native GraphSegmentation createGraphSegmentation();

    @Namespace("cv::ximgproc::segmentation")
    @Ptr
    public static native GraphSegmentation createGraphSegmentation(double d, float f, int i);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native GuidedFilter createGuidedFilter(@ByVal Mat mat, int i, double d);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native GuidedFilter createGuidedFilter(@ByVal UMat uMat, int i, double d);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native RFFeatureGetter createRFFeatureGetter();

    @Namespace("cv::ximgproc")
    @Ptr
    public static native StereoMatcher createRightMatcher(@Ptr StereoMatcher stereoMatcher);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native StructuredEdgeDetection createStructuredEdgeDetection(@Str String str);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native StructuredEdgeDetection createStructuredEdgeDetection(@Str String str, @Ptr @Const RFFeatureGetter rFFeatureGetter);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native StructuredEdgeDetection createStructuredEdgeDetection(@Str BytePointer bytePointer);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native StructuredEdgeDetection createStructuredEdgeDetection(@Str BytePointer bytePointer, @Ptr @Const RFFeatureGetter rFFeatureGetter);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native SuperpixelLSC createSuperpixelLSC(@ByVal Mat mat);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native SuperpixelLSC createSuperpixelLSC(@ByVal Mat mat, int i, float f);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native SuperpixelLSC createSuperpixelLSC(@ByVal UMat uMat);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native SuperpixelLSC createSuperpixelLSC(@ByVal UMat uMat, int i, float f);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native SuperpixelSEEDS createSuperpixelSEEDS(int i, int i2, int i3, int i4, int i5);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native SuperpixelSEEDS createSuperpixelSEEDS(int i, int i2, int i3, int i4, int i5, int i6, int i7, @Cast({"bool"}) boolean z);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native SuperpixelSLIC createSuperpixelSLIC(@ByVal Mat mat);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native SuperpixelSLIC createSuperpixelSLIC(@ByVal Mat mat, int i, int i2, float f);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native SuperpixelSLIC createSuperpixelSLIC(@ByVal UMat uMat);

    @Namespace("cv::ximgproc")
    @Ptr
    public static native SuperpixelSLIC createSuperpixelSLIC(@ByVal UMat uMat, int i, int i2, float f);

    @Namespace("cv::ximgproc")
    public static native void dtFilter(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, double d, double d2);

    @Namespace("cv::ximgproc")
    public static native void dtFilter(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, double d, double d2, int i, int i2);

    @Namespace("cv::ximgproc")
    public static native void dtFilter(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, double d, double d2);

    @Namespace("cv::ximgproc")
    public static native void dtFilter(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, double d, double d2, int i, int i2);

    @Namespace("cv::ximgproc")
    public static native void fastGlobalSmootherFilter(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, double d, double d2);

    @Namespace("cv::ximgproc")
    public static native void fastGlobalSmootherFilter(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, double d, double d2, double d3, int i);

    @Namespace("cv::ximgproc")
    public static native void fastGlobalSmootherFilter(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, double d, double d2);

    @Namespace("cv::ximgproc")
    public static native void fastGlobalSmootherFilter(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, double d, double d2, double d3, int i);

    @Namespace("cv::ximgproc")
    public static native void getDisparityVis(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv::ximgproc")
    public static native void getDisparityVis(@ByVal Mat mat, @ByVal Mat mat2, double d);

    @Namespace("cv::ximgproc")
    public static native void getDisparityVis(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv::ximgproc")
    public static native void getDisparityVis(@ByVal UMat uMat, @ByVal UMat uMat2, double d);

    @Namespace("cv::ximgproc")
    public static native void guidedFilter(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i, double d);

    @Namespace("cv::ximgproc")
    public static native void guidedFilter(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i, double d, int i2);

    @Namespace("cv::ximgproc")
    public static native void guidedFilter(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i, double d);

    @Namespace("cv::ximgproc")
    public static native void guidedFilter(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i, double d, int i2);

    @Namespace("cv::ximgproc")
    public static native void jointBilateralFilter(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i, double d, double d2);

    @Namespace("cv::ximgproc")
    public static native void jointBilateralFilter(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i, double d, double d2, int i2);

    @Namespace("cv::ximgproc")
    public static native void jointBilateralFilter(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i, double d, double d2);

    @Namespace("cv::ximgproc")
    public static native void jointBilateralFilter(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i, double d, double d2, int i2);

    @Namespace("cv::ximgproc")
    public static native void l0Smooth(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv::ximgproc")
    public static native void l0Smooth(@ByVal Mat mat, @ByVal Mat mat2, double d, double d2);

    @Namespace("cv::ximgproc")
    public static native void l0Smooth(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv::ximgproc")
    public static native void l0Smooth(@ByVal UMat uMat, @ByVal UMat uMat2, double d, double d2);

    @Namespace("cv::ximgproc")
    public static native void niBlackThreshold(@ByVal Mat mat, @ByVal Mat mat2, double d, int i, int i2, double d2);

    @Namespace("cv::ximgproc")
    public static native void niBlackThreshold(@ByVal UMat uMat, @ByVal UMat uMat2, double d, int i, int i2, double d2);

    @Namespace("cv::ximgproc")
    public static native int readGT(@Str String str, @ByVal Mat mat);

    @Namespace("cv::ximgproc")
    public static native int readGT(@Str String str, @ByVal UMat uMat);

    @Namespace("cv::ximgproc")
    public static native int readGT(@Str BytePointer bytePointer, @ByVal Mat mat);

    @Namespace("cv::ximgproc")
    public static native int readGT(@Str BytePointer bytePointer, @ByVal UMat uMat);

    @Namespace("cv::ximgproc")
    public static native void rollingGuidanceFilter(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv::ximgproc")
    public static native void rollingGuidanceFilter(@ByVal Mat mat, @ByVal Mat mat2, int i, double d, double d2, int i2, int i3);

    @Namespace("cv::ximgproc")
    public static native void rollingGuidanceFilter(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv::ximgproc")
    public static native void rollingGuidanceFilter(@ByVal UMat uMat, @ByVal UMat uMat2, int i, double d, double d2, int i2, int i3);

    static {
        Loader.load();
    }
}
