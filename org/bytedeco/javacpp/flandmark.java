package org.bytedeco.javacpp;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import org.bytedeco.javacpp.annotation.ByPtrPtr;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.MemberGetter;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.IplImage;

public class flandmark extends org.bytedeco.javacpp.presets.flandmark {
    public static final int ERROR_BW = 2;
    public static final int ERROR_BW_MARGIN = 3;
    public static final int ERROR_DATA_IMAGES = 5;
    public static final int ERROR_DATA_LBP = 7;
    public static final int ERROR_DATA_MAPTABLE = 6;
    public static final int ERROR_DATA_OPTIONS_PSIG = 9;
    public static final int ERROR_DATA_OPTIONS_S = 8;
    public static final int ERROR_M = 1;
    public static final int ERROR_W = 4;
    public static final int NO_ERR = 0;
    public static final int UNKNOWN_ERROR = 100;

    public static class FLANDMARK_Data extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native int imSize(int i);

        @MemberGetter
        public native IntPointer imSize();

        public native FLANDMARK_Data imSize(int i, int i2);

        public native FLANDMARK_Data lbp(FLANDMARK_LBP flandmark_lbp);

        public native FLANDMARK_LBP lbp();

        public native IntPointer mapTable();

        public native FLANDMARK_Data mapTable(IntPointer intPointer);

        public native FLANDMARK_Data options(FLANDMARK_Options fLANDMARK_Options);

        @ByRef
        public native FLANDMARK_Options options();

        static {
            Loader.load();
        }

        public FLANDMARK_Data() {
            super((Pointer) null);
            allocate();
        }

        public FLANDMARK_Data(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FLANDMARK_Data(Pointer p) {
            super(p);
        }

        public FLANDMARK_Data position(long position) {
            return (FLANDMARK_Data) super.position(position);
        }
    }

    public static class FLANDMARK_LBP extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native int WINS_COLS();

        public native FLANDMARK_LBP WINS_COLS(int i);

        public native int WINS_ROWS();

        public native FLANDMARK_LBP WINS_ROWS(int i);

        @Cast({"uint8_t"})
        public native byte hop();

        public native FLANDMARK_LBP hop(byte b);

        public native int winSize(int i);

        @MemberGetter
        public native IntPointer winSize();

        public native FLANDMARK_LBP winSize(int i, int i2);

        @Cast({"uint32_t*"})
        public native IntPointer wins();

        public native FLANDMARK_LBP wins(IntPointer intPointer);

        static {
            Loader.load();
        }

        public FLANDMARK_LBP() {
            super((Pointer) null);
            allocate();
        }

        public FLANDMARK_LBP(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FLANDMARK_LBP(Pointer p) {
            super(p);
        }

        public FLANDMARK_LBP position(long position) {
            return (FLANDMARK_LBP) super.position(position);
        }
    }

    public static class FLANDMARK_Model extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native DoublePointer m55W();

        public native FLANDMARK_Model m56W(DoublePointer doublePointer);

        public native int W_COLS();

        public native FLANDMARK_Model W_COLS(int i);

        public native int W_ROWS();

        public native FLANDMARK_Model W_ROWS(int i);

        public native DoublePointer bb();

        public native FLANDMARK_Model bb(DoublePointer doublePointer);

        @ByRef
        public native FLANDMARK_Data data();

        public native FLANDMARK_Model data(FLANDMARK_Data fLANDMARK_Data);

        @Cast({"uint8_t*"})
        public native BytePointer normalizedImageFrame();

        public native FLANDMARK_Model normalizedImageFrame(BytePointer bytePointer);

        public native FloatPointer sf();

        public native FLANDMARK_Model sf(FloatPointer floatPointer);

        static {
            Loader.load();
        }

        public FLANDMARK_Model() {
            super((Pointer) null);
            allocate();
        }

        public FLANDMARK_Model(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FLANDMARK_Model(Pointer p) {
            super(p);
        }

        public FLANDMARK_Model position(long position) {
            return (FLANDMARK_Model) super.position(position);
        }
    }

    public static class FLANDMARK_Options extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"uint8_t"})
        public native byte m57M();

        public native FLANDMARK_Options m58M(byte b);

        public native int PSIG_COLS(int i);

        @MemberGetter
        public native IntPointer PSIG_COLS();

        public native FLANDMARK_Options PSIG_COLS(int i, int i2);

        public native int PSIG_ROWS(int i);

        @MemberGetter
        public native IntPointer PSIG_ROWS();

        public native FLANDMARK_Options PSIG_ROWS(int i, int i2);

        public native FLANDMARK_Options PsiGS0(FLANDMARK_PSIG flandmark_psig);

        public native FLANDMARK_PSIG PsiGS0();

        public native FLANDMARK_Options PsiGS1(FLANDMARK_PSIG flandmark_psig);

        public native FLANDMARK_PSIG PsiGS1();

        public native FLANDMARK_Options PsiGS2(FLANDMARK_PSIG flandmark_psig);

        public native FLANDMARK_PSIG PsiGS2();

        public native IntPointer m59S();

        public native FLANDMARK_Options m60S(IntPointer intPointer);

        public native int bw(int i);

        @MemberGetter
        public native IntPointer bw();

        public native FLANDMARK_Options bw(int i, int i2);

        public native int bw_margin(int i);

        @MemberGetter
        public native IntPointer bw_margin();

        public native FLANDMARK_Options bw_margin(int i, int i2);

        static {
            Loader.load();
        }

        public FLANDMARK_Options() {
            super((Pointer) null);
            allocate();
        }

        public FLANDMARK_Options(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FLANDMARK_Options(Pointer p) {
            super(p);
        }

        public FLANDMARK_Options position(long position) {
            return (FLANDMARK_Options) super.position(position);
        }
    }

    public static class FLANDMARK_PSI extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"uint32_t"})
        public native int PSI_COLS();

        public native FLANDMARK_PSI PSI_COLS(int i);

        @Cast({"uint32_t"})
        public native int PSI_ROWS();

        public native FLANDMARK_PSI PSI_ROWS(int i);

        @Cast({"char*"})
        public native BytePointer data();

        public native FLANDMARK_PSI data(BytePointer bytePointer);

        static {
            Loader.load();
        }

        public FLANDMARK_PSI() {
            super((Pointer) null);
            allocate();
        }

        public FLANDMARK_PSI(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FLANDMARK_PSI(Pointer p) {
            super(p);
        }

        public FLANDMARK_PSI position(long position) {
            return (FLANDMARK_PSI) super.position(position);
        }
    }

    public static class FLANDMARK_PSI_SPARSE extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"uint32_t"})
        public native int PSI_COLS();

        public native FLANDMARK_PSI_SPARSE PSI_COLS(int i);

        @Cast({"uint32_t"})
        public native int PSI_ROWS();

        public native FLANDMARK_PSI_SPARSE PSI_ROWS(int i);

        @Cast({"uint32_t*"})
        public native IntPointer idxs();

        public native FLANDMARK_PSI_SPARSE idxs(IntPointer intPointer);

        static {
            Loader.load();
        }

        public FLANDMARK_PSI_SPARSE() {
            super((Pointer) null);
            allocate();
        }

        public FLANDMARK_PSI_SPARSE(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FLANDMARK_PSI_SPARSE(Pointer p) {
            super(p);
        }

        public FLANDMARK_PSI_SPARSE position(long position) {
            return (FLANDMARK_PSI_SPARSE) super.position(position);
        }
    }

    public static native void flandmark_argmax(DoubleBuffer doubleBuffer, FLANDMARK_Options fLANDMARK_Options, @Const IntBuffer intBuffer, FLANDMARK_PSI_SPARSE flandmark_psi_sparse, @ByPtrPtr DoubleBuffer doubleBuffer2, @ByPtrPtr DoubleBuffer doubleBuffer3);

    public static native void flandmark_argmax(DoublePointer doublePointer, FLANDMARK_Options fLANDMARK_Options, @Const IntPointer intPointer, FLANDMARK_PSI_SPARSE flandmark_psi_sparse, @ByPtrPtr DoublePointer doublePointer2, @ByPtrPtr DoublePointer doublePointer3);

    public static native void flandmark_argmax(DoublePointer doublePointer, FLANDMARK_Options fLANDMARK_Options, @Const IntPointer intPointer, FLANDMARK_PSI_SPARSE flandmark_psi_sparse, @Cast({"double**"}) PointerPointer pointerPointer, @Cast({"double**"}) PointerPointer pointerPointer2);

    public static native void flandmark_argmax(double[] dArr, FLANDMARK_Options fLANDMARK_Options, @Const int[] iArr, FLANDMARK_PSI_SPARSE flandmark_psi_sparse, @ByPtrPtr double[] dArr2, @ByPtrPtr double[] dArr3);

    @Cast({"EError_T"})
    public static native int flandmark_check_model(FLANDMARK_Model fLANDMARK_Model, FLANDMARK_Model fLANDMARK_Model2);

    public static native int flandmark_detect(IplImage iplImage, IntBuffer intBuffer, FLANDMARK_Model fLANDMARK_Model, DoubleBuffer doubleBuffer);

    public static native int flandmark_detect(IplImage iplImage, IntBuffer intBuffer, FLANDMARK_Model fLANDMARK_Model, DoubleBuffer doubleBuffer, IntBuffer intBuffer2);

    public static native int flandmark_detect(IplImage iplImage, IntPointer intPointer, FLANDMARK_Model fLANDMARK_Model, DoublePointer doublePointer);

    public static native int flandmark_detect(IplImage iplImage, IntPointer intPointer, FLANDMARK_Model fLANDMARK_Model, DoublePointer doublePointer, IntPointer intPointer2);

    public static native int flandmark_detect(IplImage iplImage, int[] iArr, FLANDMARK_Model fLANDMARK_Model, double[] dArr);

    public static native int flandmark_detect(IplImage iplImage, int[] iArr, FLANDMARK_Model fLANDMARK_Model, double[] dArr, int[] iArr2);

    public static native int flandmark_detect_base(@Cast({"uint8_t*"}) ByteBuffer byteBuffer, FLANDMARK_Model fLANDMARK_Model, DoubleBuffer doubleBuffer);

    public static native int flandmark_detect_base(@Cast({"uint8_t*"}) BytePointer bytePointer, FLANDMARK_Model fLANDMARK_Model, DoublePointer doublePointer);

    public static native int flandmark_detect_base(@Cast({"uint8_t*"}) byte[] bArr, FLANDMARK_Model fLANDMARK_Model, double[] dArr);

    public static native void flandmark_free(FLANDMARK_Model fLANDMARK_Model);

    public static native int flandmark_get_normalized_image_frame(IplImage iplImage, @Const IntBuffer intBuffer, DoubleBuffer doubleBuffer, @Cast({"uint8_t*"}) ByteBuffer byteBuffer, FLANDMARK_Model fLANDMARK_Model);

    public static native int flandmark_get_normalized_image_frame(IplImage iplImage, @Const IntPointer intPointer, DoublePointer doublePointer, @Cast({"uint8_t*"}) BytePointer bytePointer, FLANDMARK_Model fLANDMARK_Model);

    public static native int flandmark_get_normalized_image_frame(IplImage iplImage, @Const int[] iArr, double[] dArr, @Cast({"uint8_t*"}) byte[] bArr, FLANDMARK_Model fLANDMARK_Model);

    public static native void flandmark_get_psi_mat(FLANDMARK_PSI flandmark_psi, FLANDMARK_Model fLANDMARK_Model, int i);

    public static native void flandmark_get_psi_mat_sparse(FLANDMARK_PSI_SPARSE flandmark_psi_sparse, FLANDMARK_Model fLANDMARK_Model, int i);

    public static native int flandmark_imcrop(IplImage iplImage, IplImage iplImage2, @Const @ByVal CvRect cvRect);

    public static native FLANDMARK_Model flandmark_init(String str);

    public static native FLANDMARK_Model flandmark_init(@Cast({"const char*"}) BytePointer bytePointer);

    public static native void flandmark_maximize_gdotprod(DoubleBuffer doubleBuffer, DoubleBuffer doubleBuffer2, @Const DoubleBuffer doubleBuffer3, @Const DoubleBuffer doubleBuffer4, @Const IntBuffer intBuffer, int i, int i2);

    public static native void flandmark_maximize_gdotprod(DoublePointer doublePointer, DoublePointer doublePointer2, @Const DoublePointer doublePointer3, @Const DoublePointer doublePointer4, @Const IntPointer intPointer, int i, int i2);

    public static native void flandmark_maximize_gdotprod(double[] dArr, double[] dArr2, @Const double[] dArr3, @Const double[] dArr4, @Const int[] iArr, int i, int i2);

    public static native void flandmark_write_model(String str, FLANDMARK_Model fLANDMARK_Model);

    public static native void flandmark_write_model(@Cast({"const char*"}) BytePointer bytePointer, FLANDMARK_Model fLANDMARK_Model);

    public static native void liblbp_pyr_addvec(@Cast({"int64_t*"}) LongBuffer longBuffer, @Cast({"uint32_t"}) int i, @Cast({"uint32_t*"}) IntBuffer intBuffer, @Cast({"uint16_t"}) short s, @Cast({"uint16_t"}) short s2);

    public static native void liblbp_pyr_addvec(@Cast({"int64_t*"}) LongPointer longPointer, @Cast({"uint32_t"}) int i, @Cast({"uint32_t*"}) IntPointer intPointer, @Cast({"uint16_t"}) short s, @Cast({"uint16_t"}) short s2);

    public static native void liblbp_pyr_addvec(@Cast({"int64_t*"}) long[] jArr, @Cast({"uint32_t"}) int i, @Cast({"uint32_t*"}) int[] iArr, @Cast({"uint16_t"}) short s, @Cast({"uint16_t"}) short s2);

    public static native double liblbp_pyr_dotprod(DoubleBuffer doubleBuffer, @Cast({"uint32_t"}) int i, @Cast({"uint32_t*"}) IntBuffer intBuffer, @Cast({"uint16_t"}) short s, @Cast({"uint16_t"}) short s2);

    public static native double liblbp_pyr_dotprod(DoublePointer doublePointer, @Cast({"uint32_t"}) int i, @Cast({"uint32_t*"}) IntPointer intPointer, @Cast({"uint16_t"}) short s, @Cast({"uint16_t"}) short s2);

    public static native double liblbp_pyr_dotprod(double[] dArr, @Cast({"uint32_t"}) int i, @Cast({"uint32_t*"}) int[] iArr, @Cast({"uint16_t"}) short s, @Cast({"uint16_t"}) short s2);

    public static native void liblbp_pyr_features(@Cast({"char*"}) ByteBuffer byteBuffer, @Cast({"uint32_t"}) int i, @Cast({"uint32_t*"}) IntBuffer intBuffer, @Cast({"uint16_t"}) short s, @Cast({"uint16_t"}) short s2);

    public static native void liblbp_pyr_features(@Cast({"char*"}) BytePointer bytePointer, @Cast({"uint32_t"}) int i, @Cast({"uint32_t*"}) IntPointer intPointer, @Cast({"uint16_t"}) short s, @Cast({"uint16_t"}) short s2);

    public static native void liblbp_pyr_features(@Cast({"char*"}) byte[] bArr, @Cast({"uint32_t"}) int i, @Cast({"uint32_t*"}) int[] iArr, @Cast({"uint16_t"}) short s, @Cast({"uint16_t"}) short s2);

    public static native void liblbp_pyr_features_sparse(@Cast({"t_index*"}) IntBuffer intBuffer, @Cast({"uint32_t"}) int i, @Cast({"uint32_t*"}) IntBuffer intBuffer2, @Cast({"uint16_t"}) short s, @Cast({"uint16_t"}) short s2);

    public static native void liblbp_pyr_features_sparse(@Cast({"t_index*"}) IntPointer intPointer, @Cast({"uint32_t"}) int i, @Cast({"uint32_t*"}) IntPointer intPointer2, @Cast({"uint16_t"}) short s, @Cast({"uint16_t"}) short s2);

    public static native void liblbp_pyr_features_sparse(@Cast({"t_index*"}) int[] iArr, @Cast({"uint32_t"}) int i, @Cast({"uint32_t*"}) int[] iArr2, @Cast({"uint16_t"}) short s, @Cast({"uint16_t"}) short s2);

    @Cast({"uint32_t"})
    public static native int liblbp_pyr_get_dim(@Cast({"uint16_t"}) short s, @Cast({"uint16_t"}) short s2, @Cast({"uint16_t"}) short s3);

    public static native void liblbp_pyr_subvec(@Cast({"int64_t*"}) LongBuffer longBuffer, @Cast({"uint32_t"}) int i, @Cast({"uint32_t*"}) IntBuffer intBuffer, @Cast({"uint16_t"}) short s, @Cast({"uint16_t"}) short s2);

    public static native void liblbp_pyr_subvec(@Cast({"int64_t*"}) LongPointer longPointer, @Cast({"uint32_t"}) int i, @Cast({"uint32_t*"}) IntPointer intPointer, @Cast({"uint16_t"}) short s, @Cast({"uint16_t"}) short s2);

    public static native void liblbp_pyr_subvec(@Cast({"int64_t*"}) long[] jArr, @Cast({"uint32_t"}) int i, @Cast({"uint32_t*"}) int[] iArr, @Cast({"uint16_t"}) short s, @Cast({"uint16_t"}) short s2);

    static {
        Loader.load();
    }
}
