package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoOffset;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.UMat;
import org.bytedeco.javacpp.presets.opencv_core.Str;

@Namespace("cv::flann")
@NoOffset
public class opencv_flann$Index extends Pointer {
    private native void allocate();

    private native void allocate(@ByVal Mat mat, @ByRef @Const opencv_flann$IndexParams org_bytedeco_javacpp_opencv_flann_IndexParams);

    private native void allocate(@ByVal Mat mat, @ByRef @Const opencv_flann$IndexParams org_bytedeco_javacpp_opencv_flann_IndexParams, @Cast({"cvflann::flann_distance_t"}) int i);

    private native void allocate(@ByVal UMat uMat, @ByRef @Const opencv_flann$IndexParams org_bytedeco_javacpp_opencv_flann_IndexParams);

    private native void allocate(@ByVal UMat uMat, @ByRef @Const opencv_flann$IndexParams org_bytedeco_javacpp_opencv_flann_IndexParams, @Cast({"cvflann::flann_distance_t"}) int i);

    private native void allocateArray(long j);

    public native void build(@ByVal Mat mat, @ByRef @Const opencv_flann$IndexParams org_bytedeco_javacpp_opencv_flann_IndexParams);

    public native void build(@ByVal Mat mat, @ByRef @Const opencv_flann$IndexParams org_bytedeco_javacpp_opencv_flann_IndexParams, @Cast({"cvflann::flann_distance_t"}) int i);

    public native void build(@ByVal UMat uMat, @ByRef @Const opencv_flann$IndexParams org_bytedeco_javacpp_opencv_flann_IndexParams);

    public native void build(@ByVal UMat uMat, @ByRef @Const opencv_flann$IndexParams org_bytedeco_javacpp_opencv_flann_IndexParams, @Cast({"cvflann::flann_distance_t"}) int i);

    @Cast({"cvflann::flann_algorithm_t"})
    public native int getAlgorithm();

    @Cast({"cvflann::flann_distance_t"})
    public native int getDistance();

    public native void knnSearch(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i);

    public native void knnSearch(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, int i, @ByRef(nullValue = "cv::flann::SearchParams()") @Const opencv_flann$SearchParams org_bytedeco_javacpp_opencv_flann_SearchParams);

    public native void knnSearch(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i);

    public native void knnSearch(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, int i, @ByRef(nullValue = "cv::flann::SearchParams()") @Const opencv_flann$SearchParams org_bytedeco_javacpp_opencv_flann_SearchParams);

    @Cast({"bool"})
    public native boolean load(@ByVal Mat mat, @Str String str);

    @Cast({"bool"})
    public native boolean load(@ByVal Mat mat, @Str BytePointer bytePointer);

    @Cast({"bool"})
    public native boolean load(@ByVal UMat uMat, @Str String str);

    @Cast({"bool"})
    public native boolean load(@ByVal UMat uMat, @Str BytePointer bytePointer);

    public native int radiusSearch(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, double d, int i);

    public native int radiusSearch(@ByVal Mat mat, @ByVal Mat mat2, @ByVal Mat mat3, double d, int i, @ByRef(nullValue = "cv::flann::SearchParams()") @Const opencv_flann$SearchParams org_bytedeco_javacpp_opencv_flann_SearchParams);

    public native int radiusSearch(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, double d, int i);

    public native int radiusSearch(@ByVal UMat uMat, @ByVal UMat uMat2, @ByVal UMat uMat3, double d, int i, @ByRef(nullValue = "cv::flann::SearchParams()") @Const opencv_flann$SearchParams org_bytedeco_javacpp_opencv_flann_SearchParams);

    public native void release();

    public native void save(@Str String str);

    public native void save(@Str BytePointer bytePointer);

    static {
        Loader.load();
    }

    public opencv_flann$Index(Pointer p) {
        super(p);
    }

    public opencv_flann$Index(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public opencv_flann$Index position(long position) {
        return (opencv_flann$Index) super.position(position);
    }

    public opencv_flann$Index() {
        super((Pointer) null);
        allocate();
    }

    public opencv_flann$Index(@ByVal Mat features, @ByRef @Const opencv_flann$IndexParams params, @Cast({"cvflann::flann_distance_t"}) int distType) {
        super((Pointer) null);
        allocate(features, params, distType);
    }

    public opencv_flann$Index(@ByVal Mat features, @ByRef @Const opencv_flann$IndexParams params) {
        super((Pointer) null);
        allocate(features, params);
    }

    public opencv_flann$Index(@ByVal UMat features, @ByRef @Const opencv_flann$IndexParams params, @Cast({"cvflann::flann_distance_t"}) int distType) {
        super((Pointer) null);
        allocate(features, params, distType);
    }

    public opencv_flann$Index(@ByVal UMat features, @ByRef @Const opencv_flann$IndexParams params) {
        super((Pointer) null);
        allocate(features, params);
    }
}
