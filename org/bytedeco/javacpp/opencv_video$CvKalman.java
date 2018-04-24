package org.bytedeco.javacpp;

import org.bytedeco.javacpp.helper.opencv_video.AbstractCvKalman;
import org.bytedeco.javacpp.opencv_core.CvMat;

public class opencv_video$CvKalman extends AbstractCvKalman {
    private native void allocate();

    private native void allocateArray(long j);

    public native int CP();

    public native opencv_video$CvKalman CP(int i);

    public native int DP();

    public native opencv_video$CvKalman DP(int i);

    public native FloatPointer DynamMatr();

    public native opencv_video$CvKalman DynamMatr(FloatPointer floatPointer);

    public native FloatPointer KalmGainMatr();

    public native opencv_video$CvKalman KalmGainMatr(FloatPointer floatPointer);

    public native FloatPointer MNCovariance();

    public native opencv_video$CvKalman MNCovariance(FloatPointer floatPointer);

    public native int MP();

    public native opencv_video$CvKalman MP(int i);

    public native FloatPointer MeasurementMatr();

    public native opencv_video$CvKalman MeasurementMatr(FloatPointer floatPointer);

    public native FloatPointer PNCovariance();

    public native opencv_video$CvKalman PNCovariance(FloatPointer floatPointer);

    public native FloatPointer PosterErrorCovariance();

    public native opencv_video$CvKalman PosterErrorCovariance(FloatPointer floatPointer);

    public native FloatPointer PosterState();

    public native opencv_video$CvKalman PosterState(FloatPointer floatPointer);

    public native FloatPointer PriorErrorCovariance();

    public native opencv_video$CvKalman PriorErrorCovariance(FloatPointer floatPointer);

    public native FloatPointer PriorState();

    public native opencv_video$CvKalman PriorState(FloatPointer floatPointer);

    public native FloatPointer Temp1();

    public native opencv_video$CvKalman Temp1(FloatPointer floatPointer);

    public native FloatPointer Temp2();

    public native opencv_video$CvKalman Temp2(FloatPointer floatPointer);

    public native CvMat control_matrix();

    public native opencv_video$CvKalman control_matrix(CvMat cvMat);

    public native CvMat error_cov_post();

    public native opencv_video$CvKalman error_cov_post(CvMat cvMat);

    public native CvMat error_cov_pre();

    public native opencv_video$CvKalman error_cov_pre(CvMat cvMat);

    public native CvMat gain();

    public native opencv_video$CvKalman gain(CvMat cvMat);

    public native CvMat measurement_matrix();

    public native opencv_video$CvKalman measurement_matrix(CvMat cvMat);

    public native CvMat measurement_noise_cov();

    public native opencv_video$CvKalman measurement_noise_cov(CvMat cvMat);

    public native CvMat process_noise_cov();

    public native opencv_video$CvKalman process_noise_cov(CvMat cvMat);

    public native CvMat state_post();

    public native opencv_video$CvKalman state_post(CvMat cvMat);

    public native CvMat state_pre();

    public native opencv_video$CvKalman state_pre(CvMat cvMat);

    public native CvMat temp1();

    public native opencv_video$CvKalman temp1(CvMat cvMat);

    public native CvMat temp2();

    public native opencv_video$CvKalman temp2(CvMat cvMat);

    public native CvMat temp3();

    public native opencv_video$CvKalman temp3(CvMat cvMat);

    public native CvMat temp4();

    public native opencv_video$CvKalman temp4(CvMat cvMat);

    public native CvMat temp5();

    public native opencv_video$CvKalman temp5(CvMat cvMat);

    public native CvMat transition_matrix();

    public native opencv_video$CvKalman transition_matrix(CvMat cvMat);

    static {
        Loader.load();
    }

    public opencv_video$CvKalman() {
        super((Pointer) null);
        allocate();
    }

    public opencv_video$CvKalman(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_video$CvKalman(Pointer p) {
        super(p);
    }

    public opencv_video$CvKalman position(long position) {
        return (opencv_video$CvKalman) super.position(position);
    }
}
