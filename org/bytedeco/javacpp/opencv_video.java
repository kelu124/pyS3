package org.bytedeco.javacpp;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.bytedeco.javacpp.annotation.ByPtrPtr;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoOffset;
import org.bytedeco.javacpp.helper.opencv_core$CvArr;
import org.bytedeco.javacpp.opencv_core.Algorithm;
import org.bytedeco.javacpp.opencv_core.CvBox2D;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.CvPoint2D32f;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.CvTermCriteria;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.RotatedRect;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_core.TermCriteria;
import org.bytedeco.javacpp.opencv_core.UMat;
import org.bytedeco.javacpp.opencv_core.UMatVector;
import org.bytedeco.javacpp.opencv_imgproc.CvConnectedComp;
import org.bytedeco.javacpp.presets.opencv_core.Ptr;

public class opencv_video extends org.bytedeco.javacpp.helper.opencv_video {
    public static final int CV_LKFLOW_GET_MIN_EIGENVALS = 8;
    public static final int CV_LKFLOW_INITIAL_GUESSES = 4;
    public static final int CV_LKFLOW_PYR_A_READY = 1;
    public static final int CV_LKFLOW_PYR_B_READY = 2;
    public static final int MOTION_AFFINE = 2;
    public static final int MOTION_EUCLIDEAN = 1;
    public static final int MOTION_HOMOGRAPHY = 3;
    public static final int MOTION_TRANSLATION = 0;
    public static final int OPTFLOW_FARNEBACK_GAUSSIAN = 256;
    public static final int OPTFLOW_LK_GET_MIN_EIGENVALS = 8;
    public static final int OPTFLOW_USE_INITIAL_FLOW = 4;

    @Namespace("cv")
    public static class BackgroundSubtractor extends Algorithm {
        public native void apply(@ByVal Mat mat, @ByVal Mat mat2);

        public native void apply(@ByVal Mat mat, @ByVal Mat mat2, double d);

        public native void apply(@ByVal UMat uMat, @ByVal UMat uMat2);

        public native void apply(@ByVal UMat uMat, @ByVal UMat uMat2, double d);

        public native void getBackgroundImage(@ByVal Mat mat);

        public native void getBackgroundImage(@ByVal UMat uMat);

        static {
            Loader.load();
        }

        public BackgroundSubtractor(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    public static class BackgroundSubtractorKNN extends BackgroundSubtractor {
        @Cast({"bool"})
        public native boolean getDetectShadows();

        public native double getDist2Threshold();

        public native int getHistory();

        public native int getNSamples();

        public native double getShadowThreshold();

        public native int getShadowValue();

        public native int getkNNSamples();

        public native void setDetectShadows(@Cast({"bool"}) boolean z);

        public native void setDist2Threshold(double d);

        public native void setHistory(int i);

        public native void setNSamples(int i);

        public native void setShadowThreshold(double d);

        public native void setShadowValue(int i);

        public native void setkNNSamples(int i);

        static {
            Loader.load();
        }

        public BackgroundSubtractorKNN(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    public static class BackgroundSubtractorMOG2 extends BackgroundSubtractor {
        public native double getBackgroundRatio();

        public native double getComplexityReductionThreshold();

        @Cast({"bool"})
        public native boolean getDetectShadows();

        public native int getHistory();

        public native int getNMixtures();

        public native double getShadowThreshold();

        public native int getShadowValue();

        public native double getVarInit();

        public native double getVarMax();

        public native double getVarMin();

        public native double getVarThreshold();

        public native double getVarThresholdGen();

        public native void setBackgroundRatio(double d);

        public native void setComplexityReductionThreshold(double d);

        public native void setDetectShadows(@Cast({"bool"}) boolean z);

        public native void setHistory(int i);

        public native void setNMixtures(int i);

        public native void setShadowThreshold(double d);

        public native void setShadowValue(int i);

        public native void setVarInit(double d);

        public native void setVarMax(double d);

        public native void setVarMin(double d);

        public native void setVarThreshold(double d);

        public native void setVarThresholdGen(double d);

        static {
            Loader.load();
        }

        public BackgroundSubtractorMOG2(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    public static class DenseOpticalFlow extends Algorithm {
        public native void calc(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

        public native void calc(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

        public native void collectGarbage();

        static {
            Loader.load();
        }

        public DenseOpticalFlow(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    public static class DualTVL1OpticalFlow extends DenseOpticalFlow {
        public native double getEpsilon();

        public native double getGamma();

        public native int getInnerIterations();

        public native double getLambda();

        public native int getMedianFiltering();

        public native int getOuterIterations();

        public native double getScaleStep();

        public native int getScalesNumber();

        public native double getTau();

        public native double getTheta();

        @Cast({"bool"})
        public native boolean getUseInitialFlow();

        public native int getWarpingsNumber();

        public native void setEpsilon(double d);

        public native void setGamma(double d);

        public native void setInnerIterations(int i);

        public native void setLambda(double d);

        public native void setMedianFiltering(int i);

        public native void setOuterIterations(int i);

        public native void setScaleStep(double d);

        public native void setScalesNumber(int i);

        public native void setTau(double d);

        public native void setTheta(double d);

        public native void setUseInitialFlow(@Cast({"bool"}) boolean z);

        public native void setWarpingsNumber(int i);

        static {
            Loader.load();
        }

        public DualTVL1OpticalFlow(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class KalmanFilter extends Pointer {
        private native void allocate();

        private native void allocate(int i, int i2);

        private native void allocate(int i, int i2, int i3, int i4);

        private native void allocateArray(long j);

        @ByRef
        public native Mat controlMatrix();

        public native KalmanFilter controlMatrix(Mat mat);

        @ByRef
        @Const
        public native Mat correct(@ByRef @Const Mat mat);

        @ByRef
        public native Mat errorCovPost();

        public native KalmanFilter errorCovPost(Mat mat);

        @ByRef
        public native Mat errorCovPre();

        public native KalmanFilter errorCovPre(Mat mat);

        @ByRef
        public native Mat gain();

        public native KalmanFilter gain(Mat mat);

        public native void init(int i, int i2);

        public native void init(int i, int i2, int i3, int i4);

        @ByRef
        public native Mat measurementMatrix();

        public native KalmanFilter measurementMatrix(Mat mat);

        @ByRef
        public native Mat measurementNoiseCov();

        public native KalmanFilter measurementNoiseCov(Mat mat);

        @ByRef
        @Const
        public native Mat predict();

        @ByRef
        @Const
        public native Mat predict(@ByRef(nullValue = "cv::Mat()") @Const Mat mat);

        @ByRef
        public native Mat processNoiseCov();

        public native KalmanFilter processNoiseCov(Mat mat);

        @ByRef
        public native Mat statePost();

        public native KalmanFilter statePost(Mat mat);

        @ByRef
        public native Mat statePre();

        public native KalmanFilter statePre(Mat mat);

        @ByRef
        public native Mat temp1();

        public native KalmanFilter temp1(Mat mat);

        @ByRef
        public native Mat temp2();

        public native KalmanFilter temp2(Mat mat);

        @ByRef
        public native Mat temp3();

        public native KalmanFilter temp3(Mat mat);

        @ByRef
        public native Mat temp4();

        public native KalmanFilter temp4(Mat mat);

        @ByRef
        public native Mat temp5();

        public native KalmanFilter temp5(Mat mat);

        @ByRef
        public native Mat transitionMatrix();

        public native KalmanFilter transitionMatrix(Mat mat);

        static {
            Loader.load();
        }

        public KalmanFilter(Pointer p) {
            super(p);
        }

        public KalmanFilter(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public KalmanFilter position(long position) {
            return (KalmanFilter) super.position(position);
        }

        public KalmanFilter() {
            super((Pointer) null);
            allocate();
        }

        public KalmanFilter(int dynamParams, int measureParams, int controlParams, int type) {
            super((Pointer) null);
            allocate(dynamParams, measureParams, controlParams, type);
        }

        public KalmanFilter(int dynamParams, int measureParams) {
            super((Pointer) null);
            allocate(dynamParams, measureParams);
        }
    }

    @Namespace("cv")
    @ByVal
    public static native RotatedRect CamShift(@ByVal Mat mat, @ByRef Rect rect, @ByVal TermCriteria termCriteria);

    @Namespace("cv")
    @ByVal
    public static native RotatedRect CamShift(@ByVal UMat uMat, @ByRef Rect rect, @ByVal TermCriteria termCriteria);

    @Namespace("cv")
    public static native int buildOpticalFlowPyramid(@ByVal Mat mat, @ByVal MatVector matVector, @ByVal Size size, int i);

    @Namespace("cv")
    public static native int buildOpticalFlowPyramid(@ByVal Mat mat, @ByVal MatVector matVector, @ByVal Size size, int i, @Cast({"bool"}) boolean z, int i2, int i3, @Cast({"bool"}) boolean z2);

    @Namespace("cv")
    public static native int buildOpticalFlowPyramid(@ByVal Mat mat, @ByVal UMatVector uMatVector, @ByVal Size size, int i);

    @Namespace("cv")
    public static native int buildOpticalFlowPyramid(@ByVal Mat mat, @ByVal UMatVector uMatVector, @ByVal Size size, int i, @Cast({"bool"}) boolean z, int i2, int i3, @Cast({"bool"}) boolean z2);

    @Namespace("cv")
    public static native int buildOpticalFlowPyramid(@ByVal UMat uMat, @ByVal MatVector matVector, @ByVal Size size, int i);

    @Namespace("cv")
    public static native int buildOpticalFlowPyramid(@ByVal UMat uMat, @ByVal MatVector matVector, @ByVal Size size, int i, @Cast({"bool"}) boolean z, int i2, int i3, @Cast({"bool"}) boolean z2);

    @Namespace("cv")
    public static native int buildOpticalFlowPyramid(@ByVal UMat uMat, @ByVal UMatVector uMatVector, @ByVal Size size, int i);

    @Namespace("cv")
    public static native int buildOpticalFlowPyramid(@ByVal UMat uMat, @ByVal UMatVector uMatVector, @ByVal Size size, int i, @Cast({"bool"}) boolean z, int i2, int i3, @Cast({"bool"}) boolean z2);

    @Namespace("cv")
    public static native void calcOpticalFlowFarneback(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, double d, int i, int i2, int i3, int i4, double d2, int i5);

    @Namespace("cv")
    public static native void calcOpticalFlowFarneback(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, double d, int i, int i2, int i3, int i4, double d2, int i5);

    @Namespace("cv")
    public static native void calcOpticalFlowPyrLK(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5, @ByVal Mat mat6);

    @Namespace("cv")
    public static native void calcOpticalFlowPyrLK(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, @ByVal Mat mat4, @ByVal Mat mat5, @ByVal Mat mat6, @ByVal(nullValue = "cv::Size(21,21)") Size size, int i, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::COUNT+cv::TermCriteria::EPS, 30, 0.01)") TermCriteria termCriteria, int i2, double d);

    @Namespace("cv")
    public static native void calcOpticalFlowPyrLK(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5, @ByVal UMat uMat6);

    @Namespace("cv")
    public static native void calcOpticalFlowPyrLK(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, @ByVal UMat uMat4, @ByVal UMat uMat5, @ByVal UMat uMat6, @ByVal(nullValue = "cv::Size(21,21)") Size size, int i, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::COUNT+cv::TermCriteria::EPS, 30, 0.01)") TermCriteria termCriteria, int i2, double d);

    @Namespace("cv")
    @Ptr
    public static native BackgroundSubtractorKNN createBackgroundSubtractorKNN();

    @Namespace("cv")
    @Ptr
    public static native BackgroundSubtractorKNN createBackgroundSubtractorKNN(int i, double d, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    @Ptr
    public static native BackgroundSubtractorMOG2 createBackgroundSubtractorMOG2();

    @Namespace("cv")
    @Ptr
    public static native BackgroundSubtractorMOG2 createBackgroundSubtractorMOG2(int i, double d, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    @Ptr
    public static native DualTVL1OpticalFlow createOptFlow_DualTVL1();

    public static native void cvCalcOpticalFlowFarneback(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, double d, int i, int i2, int i3, int i4, double d2, int i5);

    public static native void cvCalcOpticalFlowPyrLK(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4, @Cast({"const CvPoint2D32f*"}) FloatBuffer floatBuffer, @Cast({"CvPoint2D32f*"}) FloatBuffer floatBuffer2, int i, @ByVal CvSize cvSize, int i2, @Cast({"char*"}) ByteBuffer byteBuffer, FloatBuffer floatBuffer3, @ByVal CvTermCriteria cvTermCriteria, int i3);

    public static native void cvCalcOpticalFlowPyrLK(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4, @Const CvPoint2D32f cvPoint2D32f, CvPoint2D32f cvPoint2D32f2, int i, @ByVal CvSize cvSize, int i2, @Cast({"char*"}) BytePointer bytePointer, FloatPointer floatPointer, @ByVal CvTermCriteria cvTermCriteria, int i3);

    public static native void cvCalcOpticalFlowPyrLK(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr3, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr4, @Cast({"const CvPoint2D32f*"}) float[] fArr, @Cast({"CvPoint2D32f*"}) float[] fArr2, int i, @ByVal CvSize cvSize, int i2, @Cast({"char*"}) byte[] bArr, float[] fArr3, @ByVal CvTermCriteria cvTermCriteria, int i3);

    public static native int cvCamShift(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvRect cvRect, @ByVal CvTermCriteria cvTermCriteria, CvConnectedComp cvConnectedComp);

    public static native int cvCamShift(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvRect cvRect, @ByVal CvTermCriteria cvTermCriteria, CvConnectedComp cvConnectedComp, CvBox2D cvBox2D);

    public static native CvKalman cvCreateKalman(int i, int i2);

    public static native CvKalman cvCreateKalman(int i, int i2, int i3);

    public static native int cvEstimateRigidTransform(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, CvMat cvMat, int i);

    @Const
    public static native CvMat cvKalmanCorrect(CvKalman cvKalman, @Const CvMat cvMat);

    @Const
    public static native CvMat cvKalmanPredict(CvKalman cvKalman);

    @Const
    public static native CvMat cvKalmanPredict(CvKalman cvKalman, @Const CvMat cvMat);

    @Const
    public static native CvMat cvKalmanUpdateByMeasurement(CvKalman cvKalman, CvMat cvMat);

    @Const
    public static native CvMat cvKalmanUpdateByTime(CvKalman cvKalman, CvMat cvMat);

    public static native int cvMeanShift(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @ByVal CvRect cvRect, @ByVal CvTermCriteria cvTermCriteria, CvConnectedComp cvConnectedComp);

    public static native void cvReleaseKalman(@Cast({"CvKalman**"}) PointerPointer pointerPointer);

    public static native void cvReleaseKalman(@ByPtrPtr CvKalman cvKalman);

    @Namespace("cv")
    @ByVal
    public static native Mat estimateRigidTransform(@ByVal Mat mat, @ByVal Mat mat2, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    @ByVal
    public static native Mat estimateRigidTransform(@ByVal UMat uMat, @ByVal UMat uMat2, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native double findTransformECC(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3);

    @Namespace("cv")
    public static native double findTransformECC(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::COUNT+cv::TermCriteria::EPS, 50, 0.001)") TermCriteria termCriteria, @ByVal(nullValue = "cv::InputArray(cv::noArray())") Mat mat4);

    @Namespace("cv")
    public static native double findTransformECC(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3);

    @Namespace("cv")
    public static native double findTransformECC(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i, @ByVal(nullValue = "cv::TermCriteria(cv::TermCriteria::COUNT+cv::TermCriteria::EPS, 50, 0.001)") TermCriteria termCriteria, @ByVal(nullValue = "cv::InputArray(cv::noArray())") UMat uMat4);

    @Namespace("cv")
    public static native int meanShift(@ByVal Mat mat, @ByRef Rect rect, @ByVal TermCriteria termCriteria);

    @Namespace("cv")
    public static native int meanShift(@ByVal UMat uMat, @ByRef Rect rect, @ByVal TermCriteria termCriteria);

    static {
        Loader.load();
    }
}
