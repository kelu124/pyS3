package org.opencv.features2d;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.utils.Converters;

public class GenericDescriptorMatcher {
    public static final int FERN = 2;
    public static final int ONEWAY = 1;
    protected final long nativeObj;

    private static native void add_0(long j, long j2, long j3);

    private static native void classify_0(long j, long j2, long j3, long j4, long j5);

    private static native void classify_1(long j, long j2, long j3);

    private static native void clear_0(long j);

    private static native long clone_0(long j, boolean z);

    private static native long clone_1(long j);

    private static native long create_0(int i);

    private static native void delete(long j);

    private static native boolean empty_0(long j);

    private static native long getTrainImages_0(long j);

    private static native long getTrainKeypoints_0(long j);

    private static native boolean isMaskSupported_0(long j);

    private static native void knnMatch_0(long j, long j2, long j3, long j4, long j5, long j6, int i, long j7, boolean z);

    private static native void knnMatch_1(long j, long j2, long j3, long j4, long j5, long j6, int i);

    private static native void knnMatch_2(long j, long j2, long j3, long j4, int i, long j5, boolean z);

    private static native void knnMatch_3(long j, long j2, long j3, long j4, int i);

    private static native void match_0(long j, long j2, long j3, long j4, long j5, long j6, long j7);

    private static native void match_1(long j, long j2, long j3, long j4, long j5, long j6);

    private static native void match_2(long j, long j2, long j3, long j4, long j5);

    private static native void match_3(long j, long j2, long j3, long j4);

    private static native void radiusMatch_0(long j, long j2, long j3, long j4, long j5, long j6, float f, long j7, boolean z);

    private static native void radiusMatch_1(long j, long j2, long j3, long j4, long j5, long j6, float f);

    private static native void radiusMatch_2(long j, long j2, long j3, long j4, float f, long j5, boolean z);

    private static native void radiusMatch_3(long j, long j2, long j3, long j4, float f);

    private static native void read_0(long j, String str);

    private static native void train_0(long j);

    private static native void write_0(long j, String str);

    protected GenericDescriptorMatcher(long addr) {
        this.nativeObj = addr;
    }

    public void add(List<Mat> images, List<MatOfKeyPoint> keypoints) {
        add_0(this.nativeObj, Converters.vector_Mat_to_Mat(images).nativeObj, Converters.vector_vector_KeyPoint_to_Mat(keypoints, new ArrayList(keypoints != null ? keypoints.size() : 0)).nativeObj);
    }

    public void classify(Mat queryImage, MatOfKeyPoint queryKeypoints, Mat trainImage, MatOfKeyPoint trainKeypoints) {
        classify_0(this.nativeObj, queryImage.nativeObj, queryKeypoints.nativeObj, trainImage.nativeObj, trainKeypoints.nativeObj);
    }

    public void classify(Mat queryImage, MatOfKeyPoint queryKeypoints) {
        classify_1(this.nativeObj, queryImage.nativeObj, queryKeypoints.nativeObj);
    }

    public void clear() {
        clear_0(this.nativeObj);
    }

    public GenericDescriptorMatcher clone(boolean emptyTrainData) {
        return new GenericDescriptorMatcher(clone_0(this.nativeObj, emptyTrainData));
    }

    public GenericDescriptorMatcher clone() {
        return new GenericDescriptorMatcher(clone_1(this.nativeObj));
    }

    public static GenericDescriptorMatcher create(int matcherType) {
        return new GenericDescriptorMatcher(create_0(matcherType));
    }

    public boolean empty() {
        return empty_0(this.nativeObj);
    }

    public List<Mat> getTrainImages() {
        List<Mat> retVal = new ArrayList();
        Converters.Mat_to_vector_Mat(new Mat(getTrainImages_0(this.nativeObj)), retVal);
        return retVal;
    }

    public List<MatOfKeyPoint> getTrainKeypoints() {
        List<MatOfKeyPoint> retVal = new ArrayList();
        Converters.Mat_to_vector_vector_KeyPoint(new Mat(getTrainKeypoints_0(this.nativeObj)), retVal);
        return retVal;
    }

    public boolean isMaskSupported() {
        return isMaskSupported_0(this.nativeObj);
    }

    public void knnMatch(Mat queryImage, MatOfKeyPoint queryKeypoints, Mat trainImage, MatOfKeyPoint trainKeypoints, List<MatOfDMatch> matches, int k, Mat mask, boolean compactResult) {
        Mat queryKeypoints_mat = queryKeypoints;
        Mat trainKeypoints_mat = trainKeypoints;
        Mat matches_mat = new Mat();
        knnMatch_0(this.nativeObj, queryImage.nativeObj, queryKeypoints_mat.nativeObj, trainImage.nativeObj, trainKeypoints_mat.nativeObj, matches_mat.nativeObj, k, mask.nativeObj, compactResult);
        Converters.Mat_to_vector_vector_DMatch(matches_mat, matches);
    }

    public void knnMatch(Mat queryImage, MatOfKeyPoint queryKeypoints, Mat trainImage, MatOfKeyPoint trainKeypoints, List<MatOfDMatch> matches, int k) {
        Mat queryKeypoints_mat = queryKeypoints;
        Mat trainKeypoints_mat = trainKeypoints;
        Mat matches_mat = new Mat();
        knnMatch_1(this.nativeObj, queryImage.nativeObj, queryKeypoints_mat.nativeObj, trainImage.nativeObj, trainKeypoints_mat.nativeObj, matches_mat.nativeObj, k);
        Converters.Mat_to_vector_vector_DMatch(matches_mat, matches);
    }

    public void knnMatch(Mat queryImage, MatOfKeyPoint queryKeypoints, List<MatOfDMatch> matches, int k, List<Mat> masks, boolean compactResult) {
        Mat queryKeypoints_mat = queryKeypoints;
        Mat matches_mat = new Mat();
        int i = k;
        knnMatch_2(this.nativeObj, queryImage.nativeObj, queryKeypoints_mat.nativeObj, matches_mat.nativeObj, i, Converters.vector_Mat_to_Mat(masks).nativeObj, compactResult);
        Converters.Mat_to_vector_vector_DMatch(matches_mat, matches);
    }

    public void knnMatch(Mat queryImage, MatOfKeyPoint queryKeypoints, List<MatOfDMatch> matches, int k) {
        Mat queryKeypoints_mat = queryKeypoints;
        Mat matches_mat = new Mat();
        knnMatch_3(this.nativeObj, queryImage.nativeObj, queryKeypoints_mat.nativeObj, matches_mat.nativeObj, k);
        Converters.Mat_to_vector_vector_DMatch(matches_mat, matches);
    }

    public void match(Mat queryImage, MatOfKeyPoint queryKeypoints, Mat trainImage, MatOfKeyPoint trainKeypoints, MatOfDMatch matches, Mat mask) {
        match_0(this.nativeObj, queryImage.nativeObj, queryKeypoints.nativeObj, trainImage.nativeObj, trainKeypoints.nativeObj, matches.nativeObj, mask.nativeObj);
    }

    public void match(Mat queryImage, MatOfKeyPoint queryKeypoints, Mat trainImage, MatOfKeyPoint trainKeypoints, MatOfDMatch matches) {
        match_1(this.nativeObj, queryImage.nativeObj, queryKeypoints.nativeObj, trainImage.nativeObj, trainKeypoints.nativeObj, matches.nativeObj);
    }

    public void match(Mat queryImage, MatOfKeyPoint queryKeypoints, MatOfDMatch matches, List<Mat> masks) {
        match_2(this.nativeObj, queryImage.nativeObj, queryKeypoints.nativeObj, matches.nativeObj, Converters.vector_Mat_to_Mat(masks).nativeObj);
    }

    public void match(Mat queryImage, MatOfKeyPoint queryKeypoints, MatOfDMatch matches) {
        match_3(this.nativeObj, queryImage.nativeObj, queryKeypoints.nativeObj, matches.nativeObj);
    }

    public void radiusMatch(Mat queryImage, MatOfKeyPoint queryKeypoints, Mat trainImage, MatOfKeyPoint trainKeypoints, List<MatOfDMatch> matches, float maxDistance, Mat mask, boolean compactResult) {
        Mat queryKeypoints_mat = queryKeypoints;
        Mat trainKeypoints_mat = trainKeypoints;
        Mat matches_mat = new Mat();
        radiusMatch_0(this.nativeObj, queryImage.nativeObj, queryKeypoints_mat.nativeObj, trainImage.nativeObj, trainKeypoints_mat.nativeObj, matches_mat.nativeObj, maxDistance, mask.nativeObj, compactResult);
        Converters.Mat_to_vector_vector_DMatch(matches_mat, matches);
    }

    public void radiusMatch(Mat queryImage, MatOfKeyPoint queryKeypoints, Mat trainImage, MatOfKeyPoint trainKeypoints, List<MatOfDMatch> matches, float maxDistance) {
        Mat queryKeypoints_mat = queryKeypoints;
        Mat trainKeypoints_mat = trainKeypoints;
        Mat matches_mat = new Mat();
        radiusMatch_1(this.nativeObj, queryImage.nativeObj, queryKeypoints_mat.nativeObj, trainImage.nativeObj, trainKeypoints_mat.nativeObj, matches_mat.nativeObj, maxDistance);
        Converters.Mat_to_vector_vector_DMatch(matches_mat, matches);
    }

    public void radiusMatch(Mat queryImage, MatOfKeyPoint queryKeypoints, List<MatOfDMatch> matches, float maxDistance, List<Mat> masks, boolean compactResult) {
        Mat queryKeypoints_mat = queryKeypoints;
        Mat matches_mat = new Mat();
        float f = maxDistance;
        radiusMatch_2(this.nativeObj, queryImage.nativeObj, queryKeypoints_mat.nativeObj, matches_mat.nativeObj, f, Converters.vector_Mat_to_Mat(masks).nativeObj, compactResult);
        Converters.Mat_to_vector_vector_DMatch(matches_mat, matches);
    }

    public void radiusMatch(Mat queryImage, MatOfKeyPoint queryKeypoints, List<MatOfDMatch> matches, float maxDistance) {
        Mat queryKeypoints_mat = queryKeypoints;
        Mat matches_mat = new Mat();
        radiusMatch_3(this.nativeObj, queryImage.nativeObj, queryKeypoints_mat.nativeObj, matches_mat.nativeObj, maxDistance);
        Converters.Mat_to_vector_vector_DMatch(matches_mat, matches);
    }

    public void read(String fileName) {
        read_0(this.nativeObj, fileName);
    }

    public void train() {
        train_0(this.nativeObj);
    }

    public void write(String fileName) {
        write_0(this.nativeObj, fileName);
    }

    protected void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
