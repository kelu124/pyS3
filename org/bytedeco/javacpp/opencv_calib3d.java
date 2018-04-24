package org.bytedeco.javacpp;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.bytedeco.javacpp.annotation.ByPtrPtr;
import org.bytedeco.javacpp.annotation.ByPtrRef;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoOffset;
import org.bytedeco.javacpp.annotation.Opaque;
import org.bytedeco.javacpp.helper.opencv_calib3d.AbstractCvPOSITObject;
import org.bytedeco.javacpp.helper.opencv_calib3d.AbstractCvStereoBMState;
import org.bytedeco.javacpp.helper.opencv_core$CvArr;
import org.bytedeco.javacpp.opencv_core.Algorithm;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvPoint2D32f;
import org.bytedeco.javacpp.opencv_core.CvPoint2D64f;
import org.bytedeco.javacpp.opencv_core.CvPoint3D32f;
import org.bytedeco.javacpp.opencv_core.CvPoint3D64f;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.CvTermCriteria;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Point2d;
import org.bytedeco.javacpp.opencv_core.Point3d;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_core.TermCriteria;
import org.bytedeco.javacpp.opencv_core.UMat;
import org.bytedeco.javacpp.opencv_core.UMatVector;
import org.bytedeco.javacpp.opencv_features2d.Feature2D;
import org.bytedeco.javacpp.presets.opencv_core.Ptr;

public class opencv_calib3d extends org.bytedeco.javacpp.helper.opencv_calib3d {
    public static final int CALIB_CB_ADAPTIVE_THRESH = 1;
    public static final int CALIB_CB_ASYMMETRIC_GRID = 2;
    public static final int CALIB_CB_CLUSTERING = 4;
    public static final int CALIB_CB_FAST_CHECK = 8;
    public static final int CALIB_CB_FILTER_QUADS = 4;
    public static final int CALIB_CB_NORMALIZE_IMAGE = 2;
    public static final int CALIB_CB_SYMMETRIC_GRID = 1;
    public static final int CALIB_FIX_ASPECT_RATIO = 2;
    public static final int CALIB_FIX_FOCAL_LENGTH = 16;
    public static final int CALIB_FIX_INTRINSIC = 256;
    public static final int CALIB_FIX_K1 = 32;
    public static final int CALIB_FIX_K2 = 64;
    public static final int CALIB_FIX_K3 = 128;
    public static final int CALIB_FIX_K4 = 2048;
    public static final int CALIB_FIX_K5 = 4096;
    public static final int CALIB_FIX_K6 = 8192;
    public static final int CALIB_FIX_PRINCIPAL_POINT = 4;
    public static final int CALIB_FIX_S1_S2_S3_S4 = 65536;
    public static final int CALIB_FIX_TAUX_TAUY = 524288;
    public static final int CALIB_RATIONAL_MODEL = 16384;
    public static final int CALIB_SAME_FOCAL_LENGTH = 512;
    public static final int CALIB_THIN_PRISM_MODEL = 32768;
    public static final int CALIB_TILTED_MODEL = 262144;
    public static final int CALIB_USE_INTRINSIC_GUESS = 1;
    public static final int CALIB_USE_LU = 131072;
    public static final int CALIB_ZERO_DISPARITY = 1024;
    public static final int CALIB_ZERO_TANGENT_DIST = 8;
    public static final int CV_CALIB_CB_ADAPTIVE_THRESH = 1;
    public static final int CV_CALIB_CB_FAST_CHECK = 8;
    public static final int CV_CALIB_CB_FILTER_QUADS = 4;
    public static final int CV_CALIB_CB_NORMALIZE_IMAGE = 2;
    public static final int CV_CALIB_FIX_ASPECT_RATIO = 2;
    public static final int CV_CALIB_FIX_FOCAL_LENGTH = 16;
    public static final int CV_CALIB_FIX_INTRINSIC = 256;
    public static final int CV_CALIB_FIX_K1 = 32;
    public static final int CV_CALIB_FIX_K2 = 64;
    public static final int CV_CALIB_FIX_K3 = 128;
    public static final int CV_CALIB_FIX_K4 = 2048;
    public static final int CV_CALIB_FIX_K5 = 4096;
    public static final int CV_CALIB_FIX_K6 = 8192;
    public static final int CV_CALIB_FIX_PRINCIPAL_POINT = 4;
    public static final int CV_CALIB_FIX_S1_S2_S3_S4 = 65536;
    public static final int CV_CALIB_FIX_TAUX_TAUY = 524288;
    public static final int CV_CALIB_RATIONAL_MODEL = 16384;
    public static final int CV_CALIB_SAME_FOCAL_LENGTH = 512;
    public static final int CV_CALIB_THIN_PRISM_MODEL = 32768;
    public static final int CV_CALIB_TILTED_MODEL = 262144;
    public static final int CV_CALIB_USE_INTRINSIC_GUESS = 1;
    public static final int CV_CALIB_ZERO_DISPARITY = 1024;
    public static final int CV_CALIB_ZERO_TANGENT_DIST = 8;
    public static final int CV_DLS = 3;
    public static final int CV_EPNP = 1;
    public static final int CV_FM_7POINT = 1;
    public static final int CV_FM_8POINT = 2;
    public static final int CV_FM_LMEDS = 4;
    public static final int CV_FM_LMEDS_ONLY = 4;
    public static final int CV_FM_RANSAC = 8;
    public static final int CV_FM_RANSAC_ONLY = 8;
    public static final int CV_ITERATIVE = 0;
    public static final int CV_LMEDS = 4;
    public static final int CV_P3P = 2;
    public static final int CV_RANSAC = 8;
    public static final int CV_STEREO_BM_BASIC = 0;
    public static final int CV_STEREO_BM_FISH_EYE = 1;
    public static final int CV_STEREO_BM_NARROW = 2;
    public static final int CV_STEREO_BM_NORMALIZED_RESPONSE = 0;
    public static final int CV_STEREO_BM_XSOBEL = 1;
    public static final int FISHEYE_CALIB_CHECK_COND = 4;
    public static final int FISHEYE_CALIB_FIX_INTRINSIC = 256;
    public static final int FISHEYE_CALIB_FIX_K1 = 16;
    public static final int FISHEYE_CALIB_FIX_K2 = 32;
    public static final int FISHEYE_CALIB_FIX_K3 = 64;
    public static final int FISHEYE_CALIB_FIX_K4 = 128;
    public static final int FISHEYE_CALIB_FIX_SKEW = 8;
    public static final int FISHEYE_CALIB_RECOMPUTE_EXTRINSIC = 2;
    public static final int FISHEYE_CALIB_USE_INTRINSIC_GUESS = 1;
    public static final int FM_7POINT = 1;
    public static final int FM_8POINT = 2;
    public static final int FM_LMEDS = 4;
    public static final int FM_RANSAC = 8;
    public static final int LMEDS = 4;
    public static final int RANSAC = 8;
    public static final int RHO = 16;
    public static final int SOLVEPNP_DLS = 3;
    public static final int SOLVEPNP_EPNP = 1;
    public static final int SOLVEPNP_ITERATIVE = 0;
    public static final int SOLVEPNP_P3P = 2;
    public static final int SOLVEPNP_UPNP = 4;

    @NoOffset
    public static class CvLevMarq extends Pointer {
        public static final int CALC_J = 2;
        public static final int CHECK_ERR = 3;
        public static final int DONE = 0;
        public static final int STARTED = 1;

        private native void allocate();

        private native void allocate(int i, int i2);

        private native void allocate(int i, int i2, @ByVal(nullValue = "CvTermCriteria(cvTermCriteria(CV_TERMCRIT_EPS+CV_TERMCRIT_ITER,30,DBL_EPSILON))") CvTermCriteria cvTermCriteria, @Cast({"bool"}) boolean z);

        private native void allocateArray(long j);

        public native CvLevMarq m61J(CvMat cvMat);

        @Ptr
        public native CvMat m62J();

        public native CvLevMarq JtErr(CvMat cvMat);

        @Ptr
        public native CvMat JtErr();

        public native CvLevMarq JtJ(CvMat cvMat);

        @Ptr
        public native CvMat JtJ();

        public native CvLevMarq JtJN(CvMat cvMat);

        @Ptr
        public native CvMat JtJN();

        public native CvLevMarq JtJV(CvMat cvMat);

        @Ptr
        public native CvMat JtJV();

        public native CvLevMarq JtJW(CvMat cvMat);

        @Ptr
        public native CvMat JtJW();

        public native void clear();

        public native CvLevMarq completeSymmFlag(boolean z);

        @Cast({"bool"})
        public native boolean completeSymmFlag();

        public native CvLevMarq criteria(CvTermCriteria cvTermCriteria);

        @ByRef
        public native CvTermCriteria criteria();

        public native CvLevMarq err(CvMat cvMat);

        @Ptr
        public native CvMat err();

        public native double errNorm();

        public native CvLevMarq errNorm(double d);

        public native void init(int i, int i2);

        public native void init(int i, int i2, @ByVal(nullValue = "CvTermCriteria(cvTermCriteria(CV_TERMCRIT_EPS+CV_TERMCRIT_ITER,30,DBL_EPSILON))") CvTermCriteria cvTermCriteria, @Cast({"bool"}) boolean z);

        public native int iters();

        public native CvLevMarq iters(int i);

        public native int lambdaLg10();

        public native CvLevMarq lambdaLg10(int i);

        public native CvLevMarq mask(CvMat cvMat);

        @Ptr
        public native CvMat mask();

        public native CvLevMarq param(CvMat cvMat);

        @Ptr
        public native CvMat param();

        public native double prevErrNorm();

        public native CvLevMarq prevErrNorm(double d);

        public native CvLevMarq prevParam(CvMat cvMat);

        @Ptr
        public native CvMat prevParam();

        public native int solveMethod();

        public native CvLevMarq solveMethod(int i);

        public native int state();

        public native CvLevMarq state(int i);

        public native void step();

        @Cast({"bool"})
        public native boolean update(@ByPtrRef @Const CvMat cvMat, @ByPtrRef CvMat cvMat2, @ByPtrRef CvMat cvMat3);

        @Cast({"bool"})
        public native boolean updateAlt(@ByPtrRef @Const CvMat cvMat, @ByPtrRef CvMat cvMat2, @ByPtrRef CvMat cvMat3, @ByPtrRef DoubleBuffer doubleBuffer);

        @Cast({"bool"})
        public native boolean updateAlt(@ByPtrRef @Const CvMat cvMat, @ByPtrRef CvMat cvMat2, @ByPtrRef CvMat cvMat3, @ByPtrRef DoublePointer doublePointer);

        @Cast({"bool"})
        public native boolean updateAlt(@ByPtrRef @Const CvMat cvMat, @ByPtrRef CvMat cvMat2, @ByPtrRef CvMat cvMat3, @ByPtrRef double[] dArr);

        static {
            Loader.load();
        }

        public CvLevMarq(Pointer p) {
            super(p);
        }

        public CvLevMarq(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvLevMarq position(long position) {
            return (CvLevMarq) super.position(position);
        }

        public CvLevMarq() {
            super((Pointer) null);
            allocate();
        }

        public CvLevMarq(int nparams, int nerrs, @ByVal(nullValue = "CvTermCriteria(cvTermCriteria(CV_TERMCRIT_EPS+CV_TERMCRIT_ITER,30,DBL_EPSILON))") CvTermCriteria criteria, @Cast({"bool"}) boolean completeSymmFlag) {
            super((Pointer) null);
            allocate(nparams, nerrs, criteria, completeSymmFlag);
        }

        public CvLevMarq(int nparams, int nerrs) {
            super((Pointer) null);
            allocate(nparams, nerrs);
        }
    }

    @Opaque
    public static class CvPOSITObject extends AbstractCvPOSITObject {
        public CvPOSITObject() {
            super((Pointer) null);
        }

        public CvPOSITObject(Pointer p) {
            super(p);
        }
    }

    public static class CvStereoBMState extends AbstractCvStereoBMState {
        private native void allocate();

        private native void allocateArray(long j);

        public native int SADWindowSize();

        public native CvStereoBMState SADWindowSize(int i);

        public native CvStereoBMState cost(CvMat cvMat);

        public native CvMat cost();

        public native CvStereoBMState disp(CvMat cvMat);

        public native CvMat disp();

        public native int disp12MaxDiff();

        public native CvStereoBMState disp12MaxDiff(int i);

        public native int minDisparity();

        public native CvStereoBMState minDisparity(int i);

        public native int numberOfDisparities();

        public native CvStereoBMState numberOfDisparities(int i);

        public native int preFilterCap();

        public native CvStereoBMState preFilterCap(int i);

        public native int preFilterSize();

        public native CvStereoBMState preFilterSize(int i);

        public native int preFilterType();

        public native CvStereoBMState preFilterType(int i);

        public native CvStereoBMState preFilteredImg0(CvMat cvMat);

        public native CvMat preFilteredImg0();

        public native CvStereoBMState preFilteredImg1(CvMat cvMat);

        public native CvMat preFilteredImg1();

        public native CvStereoBMState roi1(CvRect cvRect);

        @ByRef
        public native CvRect roi1();

        public native CvStereoBMState roi2(CvRect cvRect);

        @ByRef
        public native CvRect roi2();

        public native CvStereoBMState slidingSumBuf(CvMat cvMat);

        public native CvMat slidingSumBuf();

        public native int speckleRange();

        public native CvStereoBMState speckleRange(int i);

        public native int speckleWindowSize();

        public native CvStereoBMState speckleWindowSize(int i);

        public native int textureThreshold();

        public native CvStereoBMState textureThreshold(int i);

        public native int trySmallerWindows();

        public native CvStereoBMState trySmallerWindows(int i);

        public native int uniquenessRatio();

        public native CvStereoBMState uniquenessRatio(int i);

        static {
            Loader.load();
        }

        public CvStereoBMState() {
            super((Pointer) null);
            allocate();
        }

        public CvStereoBMState(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CvStereoBMState(Pointer p) {
            super(p);
        }

        public CvStereoBMState position(long position) {
            return super.position(position);
        }
    }

    @Namespace("cv")
    public static class StereoMatcher extends Algorithm {
        public static final int DISP_SCALE = 16;
        public static final int DISP_SHIFT = 4;

        public native void compute(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

        public native void compute(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

        public native int getBlockSize();

        public native int getDisp12MaxDiff();

        public native int getMinDisparity();

        public native int getNumDisparities();

        public native int getSpeckleRange();

        public native int getSpeckleWindowSize();

        public native void setBlockSize(int i);

        public native void setDisp12MaxDiff(int i);

        public native void setMinDisparity(int i);

        public native void setNumDisparities(int i);

        public native void setSpeckleRange(int i);

        public native void setSpeckleWindowSize(int i);

        static {
            Loader.load();
        }

        public StereoMatcher(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    public static class StereoBM extends StereoMatcher {
        public static final int PREFILTER_NORMALIZED_RESPONSE = 0;
        public static final int PREFILTER_XSOBEL = 1;

        @Ptr
        public static native StereoBM create();

        @Ptr
        public static native StereoBM create(int i, int i2);

        public native int getPreFilterCap();

        public native int getPreFilterSize();

        public native int getPreFilterType();

        @ByVal
        public native Rect getROI1();

        @ByVal
        public native Rect getROI2();

        public native int getSmallerBlockSize();

        public native int getTextureThreshold();

        public native int getUniquenessRatio();

        public native void setPreFilterCap(int i);

        public native void setPreFilterSize(int i);

        public native void setPreFilterType(int i);

        public native void setROI1(@ByVal Rect rect);

        public native void setROI2(@ByVal Rect rect);

        public native void setSmallerBlockSize(int i);

        public native void setTextureThreshold(int i);

        public native void setUniquenessRatio(int i);

        static {
            Loader.load();
        }

        public StereoBM(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    public static class StereoSGBM extends StereoMatcher {
        public static final int MODE_HH = 1;
        public static final int MODE_SGBM = 0;
        public static final int MODE_SGBM_3WAY = 2;

        @Ptr
        public static native StereoSGBM create(int i, int i2, int i3);

        @Ptr
        public static native StereoSGBM create(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11);

        public native int getMode();

        public native int getP1();

        public native int getP2();

        public native int getPreFilterCap();

        public native int getUniquenessRatio();

        public native void setMode(int i);

        public native void setP1(int i);

        public native void setP2(int i);

        public native void setPreFilterCap(int i);

        public native void setUniquenessRatio(int i);

        static {
            Loader.load();
        }

        public StereoSGBM(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    @ByVal
    public static native Point3d RQDecomp3x3(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    @ByVal
    public static native Point3d RQDecomp3x3(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat4, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat5, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat6);

    @Namespace("cv")
    @ByVal
    public static native Point3d RQDecomp3x3(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    @ByVal
    public static native Point3d RQDecomp3x3(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat4, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat5, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat6);

    @Namespace("cv")
    public static native void Rodrigues(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void Rodrigues(@ByVal Mat mat, @ByVal Mat mat2, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat3);

    @Namespace("cv")
    public static native void Rodrigues(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    public static native void Rodrigues(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat3);

    @Namespace("cv::fisheye")
    public static native double calibrate(@ByVal MatVector matVector, @ByVal MatVector matVector2, @ByRef @Const Size size, @ByVal Mat mat, @ByVal Mat mat2, @ByVal MatVector matVector3, @ByVal MatVector matVector4);

    @Namespace("cv::fisheye")
    public static native double calibrate(@ByVal MatVector matVector, @ByVal MatVector matVector2, @ByRef @Const Size size, @ByVal Mat mat, @ByVal Mat mat2, @ByVal MatVector matVector3, @ByVal MatVector matVector4, int i, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::COUNT + cv::TermCriteria::EPS, 100, DBL_EPSILON)") TermCriteria termCriteria);

    @Namespace("cv::fisheye")
    public static native double calibrate(@ByVal MatVector matVector, @ByVal MatVector matVector2, @ByRef @Const Size size, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal MatVector matVector3, @ByVal MatVector matVector4);

    @Namespace("cv::fisheye")
    public static native double calibrate(@ByVal MatVector matVector, @ByVal MatVector matVector2, @ByRef @Const Size size, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal MatVector matVector3, @ByVal MatVector matVector4, int i, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::COUNT + cv::TermCriteria::EPS, 100, DBL_EPSILON)") TermCriteria termCriteria);

    @Namespace("cv::fisheye")
    public static native double calibrate(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByRef @Const Size size, @ByVal Mat mat, @ByVal Mat mat2, @ByVal UMatVector uMatVector3, @ByVal UMatVector uMatVector4);

    @Namespace("cv::fisheye")
    public static native double calibrate(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByRef @Const Size size, @ByVal Mat mat, @ByVal Mat mat2, @ByVal UMatVector uMatVector3, @ByVal UMatVector uMatVector4, int i, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::COUNT + cv::TermCriteria::EPS, 100, DBL_EPSILON)") TermCriteria termCriteria);

    @Namespace("cv::fisheye")
    public static native double calibrate(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByRef @Const Size size, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMatVector uMatVector3, @ByVal UMatVector uMatVector4);

    @Namespace("cv::fisheye")
    public static native double calibrate(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByRef @Const Size size, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMatVector uMatVector3, @ByVal UMatVector uMatVector4, int i, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::COUNT + cv::TermCriteria::EPS, 100, DBL_EPSILON)") TermCriteria termCriteria);

    @Namespace("cv")
    public static native double calibrateCamera(@ByVal MatVector matVector, @ByVal MatVector matVector2, @ByVal Size size, @ByVal Mat mat, @ByVal Mat mat2, @ByVal MatVector matVector3, @ByVal MatVector matVector4);

    @Namespace("cv")
    public static native double calibrateCamera(@ByVal MatVector matVector, @ByVal MatVector matVector2, @ByVal Size size, @ByVal Mat mat, @ByVal Mat mat2, @ByVal MatVector matVector3, @ByVal MatVector matVector4, int i, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::COUNT + cv::TermCriteria::EPS, 30, DBL_EPSILON)") TermCriteria termCriteria);

    @Namespace("cv")
    public static native double calibrateCamera(@ByVal MatVector matVector, @ByVal MatVector matVector2, @ByVal Size size, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal MatVector matVector3, @ByVal MatVector matVector4);

    @Namespace("cv")
    public static native double calibrateCamera(@ByVal MatVector matVector, @ByVal MatVector matVector2, @ByVal Size size, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal MatVector matVector3, @ByVal MatVector matVector4, int i, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::COUNT + cv::TermCriteria::EPS, 30, DBL_EPSILON)") TermCriteria termCriteria);

    @Namespace("cv")
    public static native double calibrateCamera(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByVal Size size, @ByVal Mat mat, @ByVal Mat mat2, @ByVal UMatVector uMatVector3, @ByVal UMatVector uMatVector4);

    @Namespace("cv")
    public static native double calibrateCamera(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByVal Size size, @ByVal Mat mat, @ByVal Mat mat2, @ByVal UMatVector uMatVector3, @ByVal UMatVector uMatVector4, int i, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::COUNT + cv::TermCriteria::EPS, 30, DBL_EPSILON)") TermCriteria termCriteria);

    @Namespace("cv")
    public static native double calibrateCamera(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByVal Size size, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMatVector uMatVector3, @ByVal UMatVector uMatVector4);

    @Namespace("cv")
    public static native double calibrateCamera(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByVal Size size, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMatVector uMatVector3, @ByVal UMatVector uMatVector4, int i, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::COUNT + cv::TermCriteria::EPS, 30, DBL_EPSILON)") TermCriteria termCriteria);

    @Namespace("cv")
    public static native void calibrationMatrixValues(@ByVal Mat mat, @ByVal Size size, double d, double d2, @ByRef DoubleBuffer doubleBuffer, @ByRef DoubleBuffer doubleBuffer2, @ByRef DoubleBuffer doubleBuffer3, @ByRef Point2d point2d, @ByRef DoubleBuffer doubleBuffer4);

    @Namespace("cv")
    public static native void calibrationMatrixValues(@ByVal Mat mat, @ByVal Size size, double d, double d2, @ByRef DoublePointer doublePointer, @ByRef DoublePointer doublePointer2, @ByRef DoublePointer doublePointer3, @ByRef Point2d point2d, @ByRef DoublePointer doublePointer4);

    @Namespace("cv")
    public static native void calibrationMatrixValues(@ByVal UMat uMat, @ByVal Size size, double d, double d2, @ByRef DoublePointer doublePointer, @ByRef DoublePointer doublePointer2, @ByRef DoublePointer doublePointer3, @ByRef Point2d point2d, @ByRef DoublePointer doublePointer4);

    @Namespace("cv")
    public static native void calibrationMatrixValues(@ByVal UMat uMat, @ByVal Size size, double d, double d2, @ByRef double[] dArr, @ByRef double[] dArr2, @ByRef double[] dArr3, @ByRef Point2d point2d, @ByRef double[] dArr4);

    @Namespace("cv")
    public static native void composeRT(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5, @ByVal Mat mat6);

    @Namespace("cv")
    public static native void composeRT(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5, @ByVal Mat mat6, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat7, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat8, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat9, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat10, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat11, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat12, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat13, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat14);

    @Namespace("cv")
    public static native void composeRT(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5, @ByVal UMat uMat6);

    @Namespace("cv")
    public static native void composeRT(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5, @ByVal UMat uMat6, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat7, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat8, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat9, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat10, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat11, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat12, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat13, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat14);

    @Namespace("cv")
    public static native void computeCorrespondEpilines(@ByVal Mat mat, int i, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void computeCorrespondEpilines(@ByVal UMat uMat, int i, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native void convertPointsFromHomogeneous(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void convertPointsFromHomogeneous(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    public static native void convertPointsHomogeneous(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void convertPointsHomogeneous(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    public static native void convertPointsToHomogeneous(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    public static native void convertPointsToHomogeneous(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    public static native void correctMatches(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5);

    @Namespace("cv")
    public static native void correctMatches(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5);

    public static native void cvCalcMatMulDeriv(@Const CvMat cvMat, @Const CvMat cvMat2, CvMat cvMat3, CvMat cvMat4);

    public static native double cvCalibrateCamera2(@Const CvMat cvMat, @Const CvMat cvMat2, @Const CvMat cvMat3, @ByVal CvSize cvSize, CvMat cvMat4, CvMat cvMat5);

    public static native double cvCalibrateCamera2(@Const CvMat cvMat, @Const CvMat cvMat2, @Const CvMat cvMat3, @ByVal CvSize cvSize, CvMat cvMat4, CvMat cvMat5, CvMat cvMat6, CvMat cvMat7, int i, @ByVal(nullValue = "CvTermCriteria(cvTermCriteria(CV_TERMCRIT_ITER+CV_TERMCRIT_EPS,30,DBL_EPSILON))") CvTermCriteria cvTermCriteria);

    public static native void cvCalibrationMatrixValues(@Const CvMat cvMat, @ByVal CvSize cvSize);

    public static native void cvCalibrationMatrixValues(@Const CvMat cvMat, @ByVal CvSize cvSize, double d, double d2, DoubleBuffer doubleBuffer, DoubleBuffer doubleBuffer2, DoubleBuffer doubleBuffer3, @Cast({"CvPoint2D64f*"}) DoubleBuffer doubleBuffer4, DoubleBuffer doubleBuffer5);

    public static native void cvCalibrationMatrixValues(@Const CvMat cvMat, @ByVal CvSize cvSize, double d, double d2, DoublePointer doublePointer, DoublePointer doublePointer2, DoublePointer doublePointer3, CvPoint2D64f cvPoint2D64f, DoublePointer doublePointer4);

    public static native void cvCalibrationMatrixValues(@Const CvMat cvMat, @ByVal CvSize cvSize, double d, double d2, double[] dArr, double[] dArr2, double[] dArr3, @Cast({"CvPoint2D64f*"}) double[] dArr4, double[] dArr5);

    public static native int cvCheckChessboard(IplImage iplImage, @ByVal CvSize cvSize);

    public static native void cvComposeRT(@Const CvMat cvMat, @Const CvMat cvMat2, @Const CvMat cvMat3, @Const CvMat cvMat4, CvMat cvMat5, CvMat cvMat6);

    public static native void cvComposeRT(@Const CvMat cvMat, @Const CvMat cvMat2, @Const CvMat cvMat3, @Const CvMat cvMat4, CvMat cvMat5, CvMat cvMat6, CvMat cvMat7, CvMat cvMat8, CvMat cvMat9, CvMat cvMat10, CvMat cvMat11, CvMat cvMat12, CvMat cvMat13, CvMat cvMat14);

    public static native void cvComputeCorrespondEpilines(@Const CvMat cvMat, int i, @Const CvMat cvMat2, CvMat cvMat3);

    public static native void cvConvertPointsHomogeneous(@Const CvMat cvMat, CvMat cvMat2);

    public static native void cvCorrectMatches(CvMat cvMat, CvMat cvMat2, CvMat cvMat3, CvMat cvMat4, CvMat cvMat5);

    public static native CvPOSITObject cvCreatePOSITObject(@Cast({"CvPoint3D32f*"}) FloatBuffer floatBuffer, int i);

    public static native CvPOSITObject cvCreatePOSITObject(CvPoint3D32f cvPoint3D32f, int i);

    public static native CvPOSITObject cvCreatePOSITObject(@Cast({"CvPoint3D32f*"}) float[] fArr, int i);

    public static native CvStereoBMState cvCreateStereoBMState();

    public static native CvStereoBMState cvCreateStereoBMState(int i, int i2);

    public static native void cvDecomposeProjectionMatrix(@Const CvMat cvMat, CvMat cvMat2, CvMat cvMat3, CvMat cvMat4);

    public static native void cvDecomposeProjectionMatrix(@Const CvMat cvMat, CvMat cvMat2, CvMat cvMat3, CvMat cvMat4, CvMat cvMat5, CvMat cvMat6, CvMat cvMat7, @Cast({"CvPoint3D64f*"}) DoubleBuffer doubleBuffer);

    public static native void cvDecomposeProjectionMatrix(@Const CvMat cvMat, CvMat cvMat2, CvMat cvMat3, CvMat cvMat4, CvMat cvMat5, CvMat cvMat6, CvMat cvMat7, CvPoint3D64f cvPoint3D64f);

    public static native void cvDecomposeProjectionMatrix(@Const CvMat cvMat, CvMat cvMat2, CvMat cvMat3, CvMat cvMat4, CvMat cvMat5, CvMat cvMat6, CvMat cvMat7, @Cast({"CvPoint3D64f*"}) double[] dArr);

    public static native void cvDrawChessboardCorners(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvSize cvSize, @Cast({"CvPoint2D32f*"}) FloatBuffer floatBuffer, int i, int i2);

    public static native void cvDrawChessboardCorners(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvSize cvSize, CvPoint2D32f cvPoint2D32f, int i, int i2);

    public static native void cvDrawChessboardCorners(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvSize cvSize, @Cast({"CvPoint2D32f*"}) float[] fArr, int i, int i2);

    public static native int cvFindChessboardCorners(@Const Pointer pointer, @ByVal CvSize cvSize, @Cast({"CvPoint2D32f*"}) FloatBuffer floatBuffer);

    public static native int cvFindChessboardCorners(@Const Pointer pointer, @ByVal CvSize cvSize, @Cast({"CvPoint2D32f*"}) FloatBuffer floatBuffer, IntBuffer intBuffer, int i);

    public static native int cvFindChessboardCorners(@Const Pointer pointer, @ByVal CvSize cvSize, CvPoint2D32f cvPoint2D32f);

    public static native int cvFindChessboardCorners(@Const Pointer pointer, @ByVal CvSize cvSize, CvPoint2D32f cvPoint2D32f, IntPointer intPointer, int i);

    public static native int cvFindChessboardCorners(@Const Pointer pointer, @ByVal CvSize cvSize, @Cast({"CvPoint2D32f*"}) float[] fArr);

    public static native int cvFindChessboardCorners(@Const Pointer pointer, @ByVal CvSize cvSize, @Cast({"CvPoint2D32f*"}) float[] fArr, int[] iArr, int i);

    public static native void cvFindExtrinsicCameraParams2(@Const CvMat cvMat, @Const CvMat cvMat2, @Const CvMat cvMat3, @Const CvMat cvMat4, CvMat cvMat5, CvMat cvMat6);

    public static native void cvFindExtrinsicCameraParams2(@Const CvMat cvMat, @Const CvMat cvMat2, @Const CvMat cvMat3, @Const CvMat cvMat4, CvMat cvMat5, CvMat cvMat6, int i);

    public static native int cvFindFundamentalMat(@Const CvMat cvMat, @Const CvMat cvMat2, CvMat cvMat3);

    public static native int cvFindFundamentalMat(@Const CvMat cvMat, @Const CvMat cvMat2, CvMat cvMat3, int i, double d, double d2, CvMat cvMat4);

    public static native int cvFindHomography(@Const CvMat cvMat, @Const CvMat cvMat2, CvMat cvMat3);

    public static native int cvFindHomography(@Const CvMat cvMat, @Const CvMat cvMat2, CvMat cvMat3, int i, double d, CvMat cvMat4, int i2, double d2);

    public static native void cvFindStereoCorrespondenceBM(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, CvStereoBMState cvStereoBMState);

    public static native void cvGetOptimalNewCameraMatrix(@Const CvMat cvMat, @Const CvMat cvMat2, @ByVal CvSize cvSize, double d, CvMat cvMat3);

    public static native void cvGetOptimalNewCameraMatrix(@Const CvMat cvMat, @Const CvMat cvMat2, @ByVal CvSize cvSize, double d, CvMat cvMat3, @ByVal(nullValue = "CvSize(cvSize(0,0))") CvSize cvSize2, CvRect cvRect, int i);

    @ByVal
    public static native CvRect cvGetValidDisparityROI(@ByVal CvRect cvRect, @ByVal CvRect cvRect2, int i, int i2, int i3);

    public static native void cvInitIntrinsicParams2D(@Const CvMat cvMat, @Const CvMat cvMat2, @Const CvMat cvMat3, @ByVal CvSize cvSize, CvMat cvMat4);

    public static native void cvInitIntrinsicParams2D(@Const CvMat cvMat, @Const CvMat cvMat2, @Const CvMat cvMat3, @ByVal CvSize cvSize, CvMat cvMat4, double d);

    public static native void cvPOSIT(CvPOSITObject cvPOSITObject, @Cast({"CvPoint2D32f*"}) FloatBuffer floatBuffer, double d, @ByVal CvTermCriteria cvTermCriteria, FloatBuffer floatBuffer2, FloatBuffer floatBuffer3);

    public static native void cvPOSIT(CvPOSITObject cvPOSITObject, CvPoint2D32f cvPoint2D32f, double d, @ByVal CvTermCriteria cvTermCriteria, FloatPointer floatPointer, FloatPointer floatPointer2);

    public static native void cvPOSIT(CvPOSITObject cvPOSITObject, @Cast({"CvPoint2D32f*"}) float[] fArr, double d, @ByVal CvTermCriteria cvTermCriteria, float[] fArr2, float[] fArr3);

    public static native void cvProjectPoints2(@Const CvMat cvMat, @Const CvMat cvMat2, @Const CvMat cvMat3, @Const CvMat cvMat4, @Const CvMat cvMat5, CvMat cvMat6);

    public static native void cvProjectPoints2(@Const CvMat cvMat, @Const CvMat cvMat2, @Const CvMat cvMat3, @Const CvMat cvMat4, @Const CvMat cvMat5, CvMat cvMat6, CvMat cvMat7, CvMat cvMat8, CvMat cvMat9, CvMat cvMat10, CvMat cvMat11, double d);

    public static native int cvRANSACUpdateNumIters(double d, double d2, int i, int i2);

    public static native void cvRQDecomp3x3(@Const CvMat cvMat, CvMat cvMat2, CvMat cvMat3);

    public static native void cvRQDecomp3x3(@Const CvMat cvMat, CvMat cvMat2, CvMat cvMat3, CvMat cvMat4, CvMat cvMat5, CvMat cvMat6, @Cast({"CvPoint3D64f*"}) DoubleBuffer doubleBuffer);

    public static native void cvRQDecomp3x3(@Const CvMat cvMat, CvMat cvMat2, CvMat cvMat3, CvMat cvMat4, CvMat cvMat5, CvMat cvMat6, CvPoint3D64f cvPoint3D64f);

    public static native void cvRQDecomp3x3(@Const CvMat cvMat, CvMat cvMat2, CvMat cvMat3, CvMat cvMat4, CvMat cvMat5, CvMat cvMat6, @Cast({"CvPoint3D64f*"}) double[] dArr);

    public static native void cvReleasePOSITObject(@Cast({"CvPOSITObject**"}) PointerPointer pointerPointer);

    public static native void cvReleasePOSITObject(@ByPtrPtr CvPOSITObject cvPOSITObject);

    public static native void cvReleaseStereoBMState(@Cast({"CvStereoBMState**"}) PointerPointer pointerPointer);

    public static native void cvReleaseStereoBMState(@ByPtrPtr CvStereoBMState cvStereoBMState);

    public static native void cvReprojectImageTo3D(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @Const CvMat cvMat);

    public static native void cvReprojectImageTo3D(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, @Const CvMat cvMat, int i);

    public static native int cvRodrigues2(@Const CvMat cvMat, CvMat cvMat2);

    public static native int cvRodrigues2(@Const CvMat cvMat, CvMat cvMat2, CvMat cvMat3);

    public static native double cvStereoCalibrate(@Const CvMat cvMat, @Const CvMat cvMat2, @Const CvMat cvMat3, @Const CvMat cvMat4, CvMat cvMat5, CvMat cvMat6, CvMat cvMat7, CvMat cvMat8, @ByVal CvSize cvSize, CvMat cvMat9, CvMat cvMat10);

    public static native double cvStereoCalibrate(@Const CvMat cvMat, @Const CvMat cvMat2, @Const CvMat cvMat3, @Const CvMat cvMat4, CvMat cvMat5, CvMat cvMat6, CvMat cvMat7, CvMat cvMat8, @ByVal CvSize cvSize, CvMat cvMat9, CvMat cvMat10, CvMat cvMat11, CvMat cvMat12, int i, @ByVal(nullValue = "CvTermCriteria(cvTermCriteria(CV_TERMCRIT_ITER+CV_TERMCRIT_EPS,30,1e-6))") CvTermCriteria cvTermCriteria);

    public static native void cvStereoRectify(@Const CvMat cvMat, @Const CvMat cvMat2, @Const CvMat cvMat3, @Const CvMat cvMat4, @ByVal CvSize cvSize, @Const CvMat cvMat5, @Const CvMat cvMat6, CvMat cvMat7, CvMat cvMat8, CvMat cvMat9, CvMat cvMat10);

    public static native void cvStereoRectify(@Const CvMat cvMat, @Const CvMat cvMat2, @Const CvMat cvMat3, @Const CvMat cvMat4, @ByVal CvSize cvSize, @Const CvMat cvMat5, @Const CvMat cvMat6, CvMat cvMat7, CvMat cvMat8, CvMat cvMat9, CvMat cvMat10, CvMat cvMat11, int i, double d, @ByVal(nullValue = "CvSize(cvSize(0,0))") CvSize cvSize2, CvRect cvRect, CvRect cvRect2);

    public static native int cvStereoRectifyUncalibrated(@Const CvMat cvMat, @Const CvMat cvMat2, @Const CvMat cvMat3, @ByVal CvSize cvSize, CvMat cvMat4, CvMat cvMat5);

    public static native int cvStereoRectifyUncalibrated(@Const CvMat cvMat, @Const CvMat cvMat2, @Const CvMat cvMat3, @ByVal CvSize cvSize, CvMat cvMat4, CvMat cvMat5, double d);

    public static native void cvTriangulatePoints(CvMat cvMat, CvMat cvMat2, CvMat cvMat3, CvMat cvMat4, CvMat cvMat5);

    public static native void cvValidateDisparity(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i, int i2);

    public static native void cvValidateDisparity(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i, int i2, int i3);

    @Namespace("cv")
    public static native void decomposeEssentialMat(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4);

    @Namespace("cv")
    public static native void decomposeEssentialMat(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4);

    @Namespace("cv")
    public static native int decomposeHomographyMat(@ByVal Mat mat, @ByVal Mat mat2, @ByVal MatVector matVector, @ByVal MatVector matVector2, @ByVal MatVector matVector3);

    @Namespace("cv")
    public static native int decomposeHomographyMat(@ByVal Mat mat, @ByVal Mat mat2, @ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByVal UMatVector uMatVector3);

    @Namespace("cv")
    public static native int decomposeHomographyMat(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal MatVector matVector, @ByVal MatVector matVector2, @ByVal MatVector matVector3);

    @Namespace("cv")
    public static native int decomposeHomographyMat(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByVal UMatVector uMatVector3);

    @Namespace("cv")
    public static native void decomposeProjectionMatrix(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4);

    @Namespace("cv")
    public static native void decomposeProjectionMatrix(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat5, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat6, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat7, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat8);

    @Namespace("cv")
    public static native void decomposeProjectionMatrix(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4);

    @Namespace("cv")
    public static native void decomposeProjectionMatrix(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat5, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat6, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat7, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat8);

    @Namespace("cv::fisheye")
    public static native void distortPoints(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4);

    @Namespace("cv::fisheye")
    public static native void distortPoints(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, double d);

    @Namespace("cv::fisheye")
    public static native void distortPoints(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4);

    @Namespace("cv::fisheye")
    public static native void distortPoints(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, double d);

    @Namespace("cv")
    public static native void drawChessboardCorners(@ByVal Mat mat, @ByVal Size size, @ByVal Mat mat2, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native void drawChessboardCorners(@ByVal UMat uMat, @ByVal Size size, @ByVal UMat uMat2, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native int estimateAffine3D(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4);

    @Namespace("cv")
    public static native int estimateAffine3D(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, double d, double d2);

    @Namespace("cv")
    public static native int estimateAffine3D(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4);

    @Namespace("cv")
    public static native int estimateAffine3D(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, double d, double d2);

    @Namespace("cv::fisheye")
    public static native void estimateNewCameraMatrixForUndistortRectify(@ByVal Mat mat, @ByVal Mat mat2, @ByRef @Const Size size, @ByVal Mat mat3, @ByVal Mat mat4);

    @Namespace("cv::fisheye")
    public static native void estimateNewCameraMatrixForUndistortRectify(@ByVal Mat mat, @ByVal Mat mat2, @ByRef @Const Size size, @ByVal Mat mat3, @ByVal Mat mat4, double d, @ByRef(nullValue = "cv::Size()") @Const Size size2, double d2);

    @Namespace("cv::fisheye")
    public static native void estimateNewCameraMatrixForUndistortRectify(@ByVal UMat uMat, @ByVal UMat uMat2, @ByRef @Const Size size, @ByVal UMat uMat3, @ByVal UMat uMat4);

    @Namespace("cv::fisheye")
    public static native void estimateNewCameraMatrixForUndistortRectify(@ByVal UMat uMat, @ByVal UMat uMat2, @ByRef @Const Size size, @ByVal UMat uMat3, @ByVal UMat uMat4, double d, @ByRef(nullValue = "cv::Size()") @Const Size size2, double d2);

    @Namespace("cv")
    public static native void filterSpeckles(@ByVal Mat mat, double d, int i, double d2);

    @Namespace("cv")
    public static native void filterSpeckles(@ByVal Mat mat, double d, int i, double d2, @ByVal(nullValue = "cv::InputOutputArray(cv::noArray())") Mat mat2);

    @Namespace("cv")
    public static native void filterSpeckles(@ByVal UMat uMat, double d, int i, double d2);

    @Namespace("cv")
    public static native void filterSpeckles(@ByVal UMat uMat, double d, int i, double d2, @ByVal(nullValue = "cv::InputOutputArray(cv::noArray())") UMat uMat2);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean find4QuadCornerSubpix(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Size size);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean find4QuadCornerSubpix(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal Size size);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean findChessboardCorners(@ByVal Mat mat, @ByVal Size size, @ByVal Mat mat2);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean findChessboardCorners(@ByVal Mat mat, @ByVal Size size, @ByVal Mat mat2, int i);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean findChessboardCorners(@ByVal UMat uMat, @ByVal Size size, @ByVal UMat uMat2);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean findChessboardCorners(@ByVal UMat uMat, @ByVal Size size, @ByVal UMat uMat2, int i);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean findCirclesGrid(@ByVal Mat mat, @ByVal Size size, @ByVal Mat mat2);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean findCirclesGrid(@ByVal Mat mat, @ByVal Size size, @ByVal Mat mat2, int i, @Ptr @Cast({"cv::FeatureDetector*"}) Feature2D feature2D);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean findCirclesGrid(@ByVal UMat uMat, @ByVal Size size, @ByVal UMat uMat2);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean findCirclesGrid(@ByVal UMat uMat, @ByVal Size size, @ByVal UMat uMat2, int i, @Ptr @Cast({"cv::FeatureDetector*"}) Feature2D feature2D);

    @Namespace("cv")
    @ByVal
    public static native Mat findEssentialMat(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    @ByVal
    public static native Mat findEssentialMat(@ByVal Mat mat, @ByVal Mat mat2, double d, @ByVal(nullValue = "cv::Point2d(0, 0)") Point2d point2d, int i, double d2, double d3, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat3);

    @Namespace("cv")
    @ByVal
    public static native Mat findEssentialMat(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    @ByVal
    public static native Mat findEssentialMat(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i, double d, double d2, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat4);

    @Namespace("cv")
    @ByVal
    public static native Mat findEssentialMat(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    @ByVal
    public static native Mat findEssentialMat(@ByVal UMat uMat, @ByVal UMat uMat2, double d, @ByVal(nullValue = "cv::Point2d(0, 0)") Point2d point2d, int i, double d2, double d3, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat3);

    @Namespace("cv")
    @ByVal
    public static native Mat findEssentialMat(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    @ByVal
    public static native Mat findEssentialMat(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i, double d, double d2, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat4);

    @Namespace("cv")
    @ByVal
    public static native Mat findFundamentalMat(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    @ByVal
    public static native Mat findFundamentalMat(@ByVal Mat mat, @ByVal Mat mat2, int i, double d, double d2, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat3);

    @Namespace("cv")
    @ByVal
    public static native Mat findFundamentalMat(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    @ByVal
    public static native Mat findFundamentalMat(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i, double d, double d2);

    @Namespace("cv")
    @ByVal
    public static native Mat findFundamentalMat(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    @ByVal
    public static native Mat findFundamentalMat(@ByVal UMat uMat, @ByVal UMat uMat2, int i, double d, double d2, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat3);

    @Namespace("cv")
    @ByVal
    public static native Mat findFundamentalMat(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    @ByVal
    public static native Mat findFundamentalMat(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i, double d, double d2);

    @Namespace("cv")
    @ByVal
    public static native Mat findHomography(@ByVal Mat mat, @ByVal Mat mat2);

    @Namespace("cv")
    @ByVal
    public static native Mat findHomography(@ByVal Mat mat, @ByVal Mat mat2, int i, double d, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat3, int i2, double d2);

    @Namespace("cv")
    @ByVal
    public static native Mat findHomography(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    @ByVal
    public static native Mat findHomography(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i, double d);

    @Namespace("cv")
    @ByVal
    public static native Mat findHomography(@ByVal UMat uMat, @ByVal UMat uMat2);

    @Namespace("cv")
    @ByVal
    public static native Mat findHomography(@ByVal UMat uMat, @ByVal UMat uMat2, int i, double d, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat3, int i2, double d2);

    @Namespace("cv")
    @ByVal
    public static native Mat findHomography(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    @ByVal
    public static native Mat findHomography(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i, double d);

    @Namespace("cv")
    @ByVal
    public static native Mat getOptimalNewCameraMatrix(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Size size, double d);

    @Namespace("cv")
    @ByVal
    public static native Mat getOptimalNewCameraMatrix(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Size size, double d, @ByVal(nullValue = "cv::Size()") Size size2, Rect rect, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    @ByVal
    public static native Mat getOptimalNewCameraMatrix(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal Size size, double d);

    @Namespace("cv")
    @ByVal
    public static native Mat getOptimalNewCameraMatrix(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal Size size, double d, @ByVal(nullValue = "cv::Size()") Size size2, Rect rect, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    @ByVal
    public static native Rect getValidDisparityROI(@ByVal Rect rect, @ByVal Rect rect2, int i, int i2, int i3);

    @Namespace("cv")
    @ByVal
    public static native Mat initCameraMatrix2D(@ByVal MatVector matVector, @ByVal MatVector matVector2, @ByVal Size size);

    @Namespace("cv")
    @ByVal
    public static native Mat initCameraMatrix2D(@ByVal MatVector matVector, @ByVal MatVector matVector2, @ByVal Size size, double d);

    @Namespace("cv")
    @ByVal
    public static native Mat initCameraMatrix2D(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByVal Size size);

    @Namespace("cv")
    @ByVal
    public static native Mat initCameraMatrix2D(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByVal Size size, double d);

    @Namespace("cv::fisheye")
    public static native void initUndistortRectifyMap(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByRef @Const Size size, int i, @ByVal Mat mat5, @ByVal Mat mat6);

    @Namespace("cv::fisheye")
    public static native void initUndistortRectifyMap(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByRef @Const Size size, int i, @ByVal UMat uMat5, @ByVal UMat uMat6);

    @Namespace("cv")
    public static native void matMulDeriv(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4);

    @Namespace("cv")
    public static native void matMulDeriv(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4);

    @Namespace("cv::fisheye")
    public static native void projectPoints(@ByVal Mat mat, @ByVal Mat mat2, @ByRef @Const Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5);

    @Namespace("cv::fisheye")
    public static native void projectPoints(@ByVal Mat mat, @ByVal Mat mat2, @ByRef @Const Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5, double d, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat6);

    @Namespace("cv")
    public static native void projectPoints(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5, @ByVal Mat mat6);

    @Namespace("cv::fisheye")
    public static native void projectPoints(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5, @ByVal Mat mat6, double d, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat7);

    @Namespace("cv")
    public static native void projectPoints(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5, @ByVal Mat mat6, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat7, double d);

    @Namespace("cv::fisheye")
    public static native void projectPoints(@ByVal UMat uMat, @ByVal UMat uMat2, @ByRef @Const Mat mat, @ByVal UMat uMat3, @ByVal UMat uMat4);

    @Namespace("cv::fisheye")
    public static native void projectPoints(@ByVal UMat uMat, @ByVal UMat uMat2, @ByRef @Const Mat mat, @ByVal UMat uMat3, @ByVal UMat uMat4, double d, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat5);

    @Namespace("cv")
    public static native void projectPoints(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5, @ByVal UMat uMat6);

    @Namespace("cv::fisheye")
    public static native void projectPoints(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5, @ByVal UMat uMat6, double d, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat7);

    @Namespace("cv")
    public static native void projectPoints(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5, @ByVal UMat uMat6, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat7, double d);

    @Namespace("cv")
    public static native int recoverPose(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5);

    @Namespace("cv")
    public static native int recoverPose(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5, double d, @ByVal(nullValue = "cv::Point2d(0, 0)") Point2d point2d, @ByVal(nullValue = "cv::InputOutputArray(cv::noArray())") Mat mat6);

    @Namespace("cv")
    public static native int recoverPose(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5, @ByVal Mat mat6);

    @Namespace("cv")
    public static native int recoverPose(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5, @ByVal Mat mat6, @ByVal(nullValue = "cv::InputOutputArray(cv::noArray())") Mat mat7);

    @Namespace("cv")
    public static native int recoverPose(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5);

    @Namespace("cv")
    public static native int recoverPose(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5, double d, @ByVal(nullValue = "cv::Point2d(0, 0)") Point2d point2d, @ByVal(nullValue = "cv::InputOutputArray(cv::noArray())") UMat uMat6);

    @Namespace("cv")
    public static native int recoverPose(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5, @ByVal UMat uMat6);

    @Namespace("cv")
    public static native int recoverPose(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5, @ByVal UMat uMat6, @ByVal(nullValue = "cv::InputOutputArray(cv::noArray())") UMat uMat7);

    @Namespace("cv")
    public static native float rectify3Collinear(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5, @ByVal Mat mat6, @ByVal MatVector matVector, @ByVal MatVector matVector2, @ByVal Size size, @ByVal Mat mat7, @ByVal Mat mat8, @ByVal Mat mat9, @ByVal Mat mat10, @ByVal Mat mat11, @ByVal Mat mat12, @ByVal Mat mat13, @ByVal Mat mat14, @ByVal Mat mat15, @ByVal Mat mat16, @ByVal Mat mat17, double d, @ByVal Size size2, Rect rect, Rect rect2, int i);

    @Namespace("cv")
    public static native float rectify3Collinear(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5, @ByVal Mat mat6, @ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByVal Size size, @ByVal Mat mat7, @ByVal Mat mat8, @ByVal Mat mat9, @ByVal Mat mat10, @ByVal Mat mat11, @ByVal Mat mat12, @ByVal Mat mat13, @ByVal Mat mat14, @ByVal Mat mat15, @ByVal Mat mat16, @ByVal Mat mat17, double d, @ByVal Size size2, Rect rect, Rect rect2, int i);

    @Namespace("cv")
    public static native float rectify3Collinear(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5, @ByVal UMat uMat6, @ByVal MatVector matVector, @ByVal MatVector matVector2, @ByVal Size size, @ByVal UMat uMat7, @ByVal UMat uMat8, @ByVal UMat uMat9, @ByVal UMat uMat10, @ByVal UMat uMat11, @ByVal UMat uMat12, @ByVal UMat uMat13, @ByVal UMat uMat14, @ByVal UMat uMat15, @ByVal UMat uMat16, @ByVal UMat uMat17, double d, @ByVal Size size2, Rect rect, Rect rect2, int i);

    @Namespace("cv")
    public static native float rectify3Collinear(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5, @ByVal UMat uMat6, @ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByVal Size size, @ByVal UMat uMat7, @ByVal UMat uMat8, @ByVal UMat uMat9, @ByVal UMat uMat10, @ByVal UMat uMat11, @ByVal UMat uMat12, @ByVal UMat uMat13, @ByVal UMat uMat14, @ByVal UMat uMat15, @ByVal UMat uMat16, @ByVal UMat uMat17, double d, @ByVal Size size2, Rect rect, Rect rect2, int i);

    @Namespace("cv")
    public static native void reprojectImageTo3D(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native void reprojectImageTo3D(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @Cast({"bool"}) boolean z, int i);

    @Namespace("cv")
    public static native void reprojectImageTo3D(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native void reprojectImageTo3D(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @Cast({"bool"}) boolean z, int i);

    @Namespace("cv")
    public static native double sampsonDistance(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native double sampsonDistance(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean solvePnP(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5, @ByVal Mat mat6);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean solvePnP(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5, @ByVal Mat mat6, @Cast({"bool"}) boolean z, int i);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean solvePnP(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5, @ByVal UMat uMat6);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean solvePnP(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5, @ByVal UMat uMat6, @Cast({"bool"}) boolean z, int i);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean solvePnPRansac(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5, @ByVal Mat mat6);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean solvePnPRansac(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5, @ByVal Mat mat6, @Cast({"bool"}) boolean z, int i, float f, double d, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") Mat mat7, int i2);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean solvePnPRansac(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5, @ByVal UMat uMat6);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean solvePnPRansac(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5, @ByVal UMat uMat6, @Cast({"bool"}) boolean z, int i, float f, double d, @ByVal(nullValue = "cv::OutputArray(cv::noArray())") UMat uMat7, int i2);

    @Namespace("cv::fisheye")
    public static native double stereoCalibrate(@ByVal MatVector matVector, @ByVal MatVector matVector2, @ByVal MatVector matVector3, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Size size, @ByVal Mat mat5, @ByVal Mat mat6);

    @Namespace("cv::fisheye")
    public static native double stereoCalibrate(@ByVal MatVector matVector, @ByVal MatVector matVector2, @ByVal MatVector matVector3, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Size size, @ByVal Mat mat5, @ByVal Mat mat6, int i, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::COUNT + cv::TermCriteria::EPS, 100, DBL_EPSILON)") TermCriteria termCriteria);

    @Namespace("cv")
    public static native double stereoCalibrate(@ByVal MatVector matVector, @ByVal MatVector matVector2, @ByVal MatVector matVector3, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Size size, @ByVal Mat mat5, @ByVal Mat mat6, @ByVal Mat mat7, @ByVal Mat mat8);

    @Namespace("cv")
    public static native double stereoCalibrate(@ByVal MatVector matVector, @ByVal MatVector matVector2, @ByVal MatVector matVector3, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Size size, @ByVal Mat mat5, @ByVal Mat mat6, @ByVal Mat mat7, @ByVal Mat mat8, int i, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::COUNT+cv::TermCriteria::EPS, 30, 1e-6)") TermCriteria termCriteria);

    @Namespace("cv::fisheye")
    public static native double stereoCalibrate(@ByVal MatVector matVector, @ByVal MatVector matVector2, @ByVal MatVector matVector3, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal Size size, @ByVal UMat uMat5, @ByVal UMat uMat6);

    @Namespace("cv::fisheye")
    public static native double stereoCalibrate(@ByVal MatVector matVector, @ByVal MatVector matVector2, @ByVal MatVector matVector3, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal Size size, @ByVal UMat uMat5, @ByVal UMat uMat6, int i, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::COUNT + cv::TermCriteria::EPS, 100, DBL_EPSILON)") TermCriteria termCriteria);

    @Namespace("cv")
    public static native double stereoCalibrate(@ByVal MatVector matVector, @ByVal MatVector matVector2, @ByVal MatVector matVector3, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal Size size, @ByVal UMat uMat5, @ByVal UMat uMat6, @ByVal UMat uMat7, @ByVal UMat uMat8);

    @Namespace("cv")
    public static native double stereoCalibrate(@ByVal MatVector matVector, @ByVal MatVector matVector2, @ByVal MatVector matVector3, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal Size size, @ByVal UMat uMat5, @ByVal UMat uMat6, @ByVal UMat uMat7, @ByVal UMat uMat8, int i, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::COUNT+cv::TermCriteria::EPS, 30, 1e-6)") TermCriteria termCriteria);

    @Namespace("cv::fisheye")
    public static native double stereoCalibrate(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByVal UMatVector uMatVector3, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Size size, @ByVal Mat mat5, @ByVal Mat mat6);

    @Namespace("cv::fisheye")
    public static native double stereoCalibrate(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByVal UMatVector uMatVector3, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Size size, @ByVal Mat mat5, @ByVal Mat mat6, int i, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::COUNT + cv::TermCriteria::EPS, 100, DBL_EPSILON)") TermCriteria termCriteria);

    @Namespace("cv")
    public static native double stereoCalibrate(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByVal UMatVector uMatVector3, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Size size, @ByVal Mat mat5, @ByVal Mat mat6, @ByVal Mat mat7, @ByVal Mat mat8);

    @Namespace("cv")
    public static native double stereoCalibrate(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByVal UMatVector uMatVector3, @ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Size size, @ByVal Mat mat5, @ByVal Mat mat6, @ByVal Mat mat7, @ByVal Mat mat8, int i, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::COUNT+cv::TermCriteria::EPS, 30, 1e-6)") TermCriteria termCriteria);

    @Namespace("cv::fisheye")
    public static native double stereoCalibrate(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByVal UMatVector uMatVector3, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal Size size, @ByVal UMat uMat5, @ByVal UMat uMat6);

    @Namespace("cv::fisheye")
    public static native double stereoCalibrate(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByVal UMatVector uMatVector3, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal Size size, @ByVal UMat uMat5, @ByVal UMat uMat6, int i, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::COUNT + cv::TermCriteria::EPS, 100, DBL_EPSILON)") TermCriteria termCriteria);

    @Namespace("cv")
    public static native double stereoCalibrate(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByVal UMatVector uMatVector3, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal Size size, @ByVal UMat uMat5, @ByVal UMat uMat6, @ByVal UMat uMat7, @ByVal UMat uMat8);

    @Namespace("cv")
    public static native double stereoCalibrate(@ByVal UMatVector uMatVector, @ByVal UMatVector uMatVector2, @ByVal UMatVector uMatVector3, @ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal Size size, @ByVal UMat uMat5, @ByVal UMat uMat6, @ByVal UMat uMat7, @ByVal UMat uMat8, int i, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::COUNT+cv::TermCriteria::EPS, 30, 1e-6)") TermCriteria termCriteria);

    @Namespace("cv")
    public static native void stereoRectify(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Size size, @ByVal Mat mat5, @ByVal Mat mat6, @ByVal Mat mat7, @ByVal Mat mat8, @ByVal Mat mat9, @ByVal Mat mat10, @ByVal Mat mat11);

    @Namespace("cv::fisheye")
    public static native void stereoRectify(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByRef @Const Size size, @ByVal Mat mat5, @ByVal Mat mat6, @ByVal Mat mat7, @ByVal Mat mat8, @ByVal Mat mat9, @ByVal Mat mat10, @ByVal Mat mat11, int i);

    @Namespace("cv")
    public static native void stereoRectify(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Size size, @ByVal Mat mat5, @ByVal Mat mat6, @ByVal Mat mat7, @ByVal Mat mat8, @ByVal Mat mat9, @ByVal Mat mat10, @ByVal Mat mat11, int i, double d, @ByVal(nullValue = "cv::Size()") Size size2, Rect rect, Rect rect2);

    @Namespace("cv::fisheye")
    public static native void stereoRectify(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByRef @Const Size size, @ByVal Mat mat5, @ByVal Mat mat6, @ByVal Mat mat7, @ByVal Mat mat8, @ByVal Mat mat9, @ByVal Mat mat10, @ByVal Mat mat11, int i, @ByRef(nullValue = "cv::Size()") @Const Size size2, double d, double d2);

    @Namespace("cv")
    public static native void stereoRectify(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal Size size, @ByVal UMat uMat5, @ByVal UMat uMat6, @ByVal UMat uMat7, @ByVal UMat uMat8, @ByVal UMat uMat9, @ByVal UMat uMat10, @ByVal UMat uMat11);

    @Namespace("cv::fisheye")
    public static native void stereoRectify(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByRef @Const Size size, @ByVal UMat uMat5, @ByVal UMat uMat6, @ByVal UMat uMat7, @ByVal UMat uMat8, @ByVal UMat uMat9, @ByVal UMat uMat10, @ByVal UMat uMat11, int i);

    @Namespace("cv")
    public static native void stereoRectify(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal Size size, @ByVal UMat uMat5, @ByVal UMat uMat6, @ByVal UMat uMat7, @ByVal UMat uMat8, @ByVal UMat uMat9, @ByVal UMat uMat10, @ByVal UMat uMat11, int i, double d, @ByVal(nullValue = "cv::Size()") Size size2, Rect rect, Rect rect2);

    @Namespace("cv::fisheye")
    public static native void stereoRectify(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByRef @Const Size size, @ByVal UMat uMat5, @ByVal UMat uMat6, @ByVal UMat uMat7, @ByVal UMat uMat8, @ByVal UMat uMat9, @ByVal UMat uMat10, @ByVal UMat uMat11, int i, @ByRef(nullValue = "cv::Size()") @Const Size size2, double d, double d2);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean stereoRectifyUncalibrated(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Size size, @ByVal Mat mat4, @ByVal Mat mat5);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean stereoRectifyUncalibrated(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Size size, @ByVal Mat mat4, @ByVal Mat mat5, double d);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean stereoRectifyUncalibrated(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal Size size, @ByVal UMat uMat4, @ByVal UMat uMat5);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean stereoRectifyUncalibrated(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal Size size, @ByVal UMat uMat4, @ByVal UMat uMat5, double d);

    @Namespace("cv")
    public static native void triangulatePoints(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5);

    @Namespace("cv")
    public static native void triangulatePoints(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5);

    @Namespace("cv::fisheye")
    public static native void undistortImage(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4);

    @Namespace("cv::fisheye")
    public static native void undistortImage(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat5, @ByRef(nullValue = "cv::Size()") @Const Size size);

    @Namespace("cv::fisheye")
    public static native void undistortImage(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4);

    @Namespace("cv::fisheye")
    public static native void undistortImage(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat5, @ByRef(nullValue = "cv::Size()") @Const Size size);

    @Namespace("cv::fisheye")
    public static native void undistortPoints(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4);

    @Namespace("cv::fisheye")
    public static native void undistortPoints(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat5, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat6);

    @Namespace("cv::fisheye")
    public static native void undistortPoints(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4);

    @Namespace("cv::fisheye")
    public static native void undistortPoints(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat5, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat6);

    @Namespace("cv")
    public static native void validateDisparity(@ByVal Mat mat, @ByVal Mat mat2, int i, int i2);

    @Namespace("cv")
    public static native void validateDisparity(@ByVal Mat mat, @ByVal Mat mat2, int i, int i2, int i3);

    @Namespace("cv")
    public static native void validateDisparity(@ByVal UMat uMat, @ByVal UMat uMat2, int i, int i2);

    @Namespace("cv")
    public static native void validateDisparity(@ByVal UMat uMat, @ByVal UMat uMat2, int i, int i2, int i3);

    static {
        Loader.load();
    }
}
