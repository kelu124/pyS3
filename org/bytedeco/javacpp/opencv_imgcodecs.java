package org.bytedeco.javacpp;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.StdVector;
import org.bytedeco.javacpp.helper.opencv_core$CvArr;
import org.bytedeco.javacpp.opencv_core.CvMat;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.UMat;
import org.bytedeco.javacpp.presets.opencv_core.Str;

public class opencv_imgcodecs extends org.bytedeco.javacpp.helper.opencv_imgcodecs {
    public static final int CV_CVTIMG_FLIP = 1;
    public static final int CV_CVTIMG_SWAP_RB = 2;
    public static final int CV_IMWRITE_JPEG_CHROMA_QUALITY = 6;
    public static final int CV_IMWRITE_JPEG_LUMA_QUALITY = 5;
    public static final int CV_IMWRITE_JPEG_OPTIMIZE = 3;
    public static final int CV_IMWRITE_JPEG_PROGRESSIVE = 2;
    public static final int CV_IMWRITE_JPEG_QUALITY = 1;
    public static final int CV_IMWRITE_JPEG_RST_INTERVAL = 4;
    public static final int CV_IMWRITE_PNG_BILEVEL = 18;
    public static final int CV_IMWRITE_PNG_COMPRESSION = 16;
    public static final int CV_IMWRITE_PNG_STRATEGY = 17;
    public static final int CV_IMWRITE_PNG_STRATEGY_DEFAULT = 0;
    public static final int CV_IMWRITE_PNG_STRATEGY_FILTERED = 1;
    public static final int CV_IMWRITE_PNG_STRATEGY_FIXED = 4;
    public static final int CV_IMWRITE_PNG_STRATEGY_HUFFMAN_ONLY = 2;
    public static final int CV_IMWRITE_PNG_STRATEGY_RLE = 3;
    public static final int CV_IMWRITE_PXM_BINARY = 32;
    public static final int CV_IMWRITE_WEBP_QUALITY = 64;
    public static final int CV_LOAD_IMAGE_ANYCOLOR = 4;
    public static final int CV_LOAD_IMAGE_ANYDEPTH = 2;
    public static final int CV_LOAD_IMAGE_COLOR = 1;
    public static final int CV_LOAD_IMAGE_GRAYSCALE = 0;
    public static final int CV_LOAD_IMAGE_UNCHANGED = -1;
    public static final int IMREAD_ANYCOLOR = 4;
    public static final int IMREAD_ANYDEPTH = 2;
    public static final int IMREAD_COLOR = 1;
    public static final int IMREAD_GRAYSCALE = 0;
    public static final int IMREAD_LOAD_GDAL = 8;
    public static final int IMREAD_REDUCED_COLOR_2 = 17;
    public static final int IMREAD_REDUCED_COLOR_4 = 33;
    public static final int IMREAD_REDUCED_COLOR_8 = 65;
    public static final int IMREAD_REDUCED_GRAYSCALE_2 = 16;
    public static final int IMREAD_REDUCED_GRAYSCALE_4 = 32;
    public static final int IMREAD_REDUCED_GRAYSCALE_8 = 64;
    public static final int IMREAD_UNCHANGED = -1;
    public static final int IMWRITE_JPEG_CHROMA_QUALITY = 6;
    public static final int IMWRITE_JPEG_LUMA_QUALITY = 5;
    public static final int IMWRITE_JPEG_OPTIMIZE = 3;
    public static final int IMWRITE_JPEG_PROGRESSIVE = 2;
    public static final int IMWRITE_JPEG_QUALITY = 1;
    public static final int IMWRITE_JPEG_RST_INTERVAL = 4;
    public static final int IMWRITE_PNG_BILEVEL = 18;
    public static final int IMWRITE_PNG_COMPRESSION = 16;
    public static final int IMWRITE_PNG_STRATEGY = 17;
    public static final int IMWRITE_PNG_STRATEGY_DEFAULT = 0;
    public static final int IMWRITE_PNG_STRATEGY_FILTERED = 1;
    public static final int IMWRITE_PNG_STRATEGY_FIXED = 4;
    public static final int IMWRITE_PNG_STRATEGY_HUFFMAN_ONLY = 2;
    public static final int IMWRITE_PNG_STRATEGY_RLE = 3;
    public static final int IMWRITE_PXM_BINARY = 32;
    public static final int IMWRITE_WEBP_QUALITY = 64;

    public static native void cvConvertImage(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2);

    public static native void cvConvertImage(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i);

    public static native IplImage cvDecodeImage(@Const CvMat cvMat);

    public static native IplImage cvDecodeImage(@Const CvMat cvMat, int i);

    public static native CvMat cvDecodeImageM(@Const CvMat cvMat);

    public static native CvMat cvDecodeImageM(@Const CvMat cvMat, int i);

    public static native CvMat cvEncodeImage(String str, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native CvMat cvEncodeImage(String str, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntBuffer intBuffer);

    public static native CvMat cvEncodeImage(String str, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntPointer intPointer);

    public static native CvMat cvEncodeImage(String str, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const int[] iArr);

    public static native CvMat cvEncodeImage(@Cast({"const char*"}) BytePointer bytePointer, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native CvMat cvEncodeImage(@Cast({"const char*"}) BytePointer bytePointer, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntBuffer intBuffer);

    public static native CvMat cvEncodeImage(@Cast({"const char*"}) BytePointer bytePointer, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntPointer intPointer);

    public static native CvMat cvEncodeImage(@Cast({"const char*"}) BytePointer bytePointer, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const int[] iArr);

    public static native int cvHaveImageReader(String str);

    public static native int cvHaveImageReader(@Cast({"const char*"}) BytePointer bytePointer);

    public static native int cvHaveImageWriter(String str);

    public static native int cvHaveImageWriter(@Cast({"const char*"}) BytePointer bytePointer);

    public static native IplImage cvLoadImage(String str);

    public static native IplImage cvLoadImage(String str, int i);

    public static native IplImage cvLoadImage(@Cast({"const char*"}) BytePointer bytePointer);

    public static native IplImage cvLoadImage(@Cast({"const char*"}) BytePointer bytePointer, int i);

    public static native CvMat cvLoadImageM(String str);

    public static native CvMat cvLoadImageM(String str, int i);

    public static native CvMat cvLoadImageM(@Cast({"const char*"}) BytePointer bytePointer);

    public static native CvMat cvLoadImageM(@Cast({"const char*"}) BytePointer bytePointer, int i);

    public static native int cvSaveImage(String str, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native int cvSaveImage(String str, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntBuffer intBuffer);

    public static native int cvSaveImage(String str, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntPointer intPointer);

    public static native int cvSaveImage(String str, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const int[] iArr);

    public static native int cvSaveImage(@Cast({"const char*"}) BytePointer bytePointer, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native int cvSaveImage(@Cast({"const char*"}) BytePointer bytePointer, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntBuffer intBuffer);

    public static native int cvSaveImage(@Cast({"const char*"}) BytePointer bytePointer, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const IntPointer intPointer);

    public static native int cvSaveImage(@Cast({"const char*"}) BytePointer bytePointer, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Const int[] iArr);

    public static native void cvvConvertImage(opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr2, int i);

    public static native IplImage cvvLoadImage(String str);

    public static native IplImage cvvLoadImage(@Cast({"const char*"}) BytePointer bytePointer);

    public static native int cvvSaveImage(String str, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, IntBuffer intBuffer);

    public static native int cvvSaveImage(String str, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, IntPointer intPointer);

    public static native int cvvSaveImage(String str, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int[] iArr);

    public static native int cvvSaveImage(@Cast({"const char*"}) BytePointer bytePointer, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, IntBuffer intBuffer);

    public static native int cvvSaveImage(@Cast({"const char*"}) BytePointer bytePointer, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, IntPointer intPointer);

    public static native int cvvSaveImage(@Cast({"const char*"}) BytePointer bytePointer, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, int[] iArr);

    @Namespace("cv")
    @ByVal
    public static native Mat imdecode(@ByVal Mat mat, int i);

    @Namespace("cv")
    @ByVal
    public static native Mat imdecode(@ByVal Mat mat, int i, Mat mat2);

    @Namespace("cv")
    @ByVal
    public static native Mat imdecode(@ByVal UMat uMat, int i);

    @Namespace("cv")
    @ByVal
    public static native Mat imdecode(@ByVal UMat uMat, int i, Mat mat);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str String str, @ByVal Mat mat, @Cast({"uchar*"}) @StdVector ByteBuffer byteBuffer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str String str, @ByVal Mat mat, @Cast({"uchar*"}) @StdVector ByteBuffer byteBuffer, @StdVector IntBuffer intBuffer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str String str, @ByVal Mat mat, @Cast({"uchar*"}) @StdVector BytePointer bytePointer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str String str, @ByVal Mat mat, @Cast({"uchar*"}) @StdVector BytePointer bytePointer, @StdVector IntPointer intPointer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str String str, @ByVal Mat mat, @Cast({"uchar*"}) @StdVector byte[] bArr);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str String str, @ByVal Mat mat, @Cast({"uchar*"}) @StdVector byte[] bArr, @StdVector int[] iArr);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str String str, @ByVal UMat uMat, @Cast({"uchar*"}) @StdVector ByteBuffer byteBuffer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str String str, @ByVal UMat uMat, @Cast({"uchar*"}) @StdVector ByteBuffer byteBuffer, @StdVector IntBuffer intBuffer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str String str, @ByVal UMat uMat, @Cast({"uchar*"}) @StdVector BytePointer bytePointer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str String str, @ByVal UMat uMat, @Cast({"uchar*"}) @StdVector BytePointer bytePointer, @StdVector IntPointer intPointer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str String str, @ByVal UMat uMat, @Cast({"uchar*"}) @StdVector byte[] bArr);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str String str, @ByVal UMat uMat, @Cast({"uchar*"}) @StdVector byte[] bArr, @StdVector int[] iArr);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str BytePointer bytePointer, @ByVal Mat mat, @Cast({"uchar*"}) @StdVector ByteBuffer byteBuffer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str BytePointer bytePointer, @ByVal Mat mat, @Cast({"uchar*"}) @StdVector ByteBuffer byteBuffer, @StdVector IntBuffer intBuffer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str BytePointer bytePointer, @ByVal Mat mat, @Cast({"uchar*"}) @StdVector BytePointer bytePointer2);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str BytePointer bytePointer, @ByVal Mat mat, @Cast({"uchar*"}) @StdVector BytePointer bytePointer2, @StdVector IntPointer intPointer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str BytePointer bytePointer, @ByVal Mat mat, @Cast({"uchar*"}) @StdVector byte[] bArr);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str BytePointer bytePointer, @ByVal Mat mat, @Cast({"uchar*"}) @StdVector byte[] bArr, @StdVector int[] iArr);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str BytePointer bytePointer, @ByVal UMat uMat, @Cast({"uchar*"}) @StdVector ByteBuffer byteBuffer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str BytePointer bytePointer, @ByVal UMat uMat, @Cast({"uchar*"}) @StdVector ByteBuffer byteBuffer, @StdVector IntBuffer intBuffer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str BytePointer bytePointer, @ByVal UMat uMat, @Cast({"uchar*"}) @StdVector BytePointer bytePointer2);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str BytePointer bytePointer, @ByVal UMat uMat, @Cast({"uchar*"}) @StdVector BytePointer bytePointer2, @StdVector IntPointer intPointer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str BytePointer bytePointer, @ByVal UMat uMat, @Cast({"uchar*"}) @StdVector byte[] bArr);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imencode(@Str BytePointer bytePointer, @ByVal UMat uMat, @Cast({"uchar*"}) @StdVector byte[] bArr, @StdVector int[] iArr);

    @Namespace("cv")
    @ByVal
    public static native Mat imread(@Str String str);

    @Namespace("cv")
    @ByVal
    public static native Mat imread(@Str String str, int i);

    @Namespace("cv")
    @ByVal
    public static native Mat imread(@Str BytePointer bytePointer);

    @Namespace("cv")
    @ByVal
    public static native Mat imread(@Str BytePointer bytePointer, int i);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imreadmulti(@Str String str, @ByRef MatVector matVector);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imreadmulti(@Str String str, @ByRef MatVector matVector, int i);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imreadmulti(@Str BytePointer bytePointer, @ByRef MatVector matVector);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imreadmulti(@Str BytePointer bytePointer, @ByRef MatVector matVector, int i);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imwrite(@Str String str, @ByVal Mat mat);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imwrite(@Str String str, @ByVal Mat mat, @StdVector IntBuffer intBuffer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imwrite(@Str String str, @ByVal Mat mat, @StdVector IntPointer intPointer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imwrite(@Str String str, @ByVal Mat mat, @StdVector int[] iArr);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imwrite(@Str String str, @ByVal UMat uMat);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imwrite(@Str String str, @ByVal UMat uMat, @StdVector IntBuffer intBuffer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imwrite(@Str String str, @ByVal UMat uMat, @StdVector IntPointer intPointer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imwrite(@Str String str, @ByVal UMat uMat, @StdVector int[] iArr);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imwrite(@Str BytePointer bytePointer, @ByVal Mat mat);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imwrite(@Str BytePointer bytePointer, @ByVal Mat mat, @StdVector IntBuffer intBuffer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imwrite(@Str BytePointer bytePointer, @ByVal Mat mat, @StdVector IntPointer intPointer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imwrite(@Str BytePointer bytePointer, @ByVal Mat mat, @StdVector int[] iArr);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imwrite(@Str BytePointer bytePointer, @ByVal UMat uMat);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imwrite(@Str BytePointer bytePointer, @ByVal UMat uMat, @StdVector IntBuffer intBuffer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imwrite(@Str BytePointer bytePointer, @ByVal UMat uMat, @StdVector IntPointer intPointer);

    @Namespace("cv")
    @Cast({"bool"})
    public static native boolean imwrite(@Str BytePointer bytePointer, @ByVal UMat uMat, @StdVector int[] iArr);

    static {
        Loader.load();
    }
}
