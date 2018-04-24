package org.bytedeco.javacpp;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import org.bytedeco.javacpp.annotation.ByPtrPtr;
import org.bytedeco.javacpp.annotation.ByPtrRef;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.MemberGetter;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoOffset;
import org.bytedeco.javacpp.annotation.StdString;
import org.bytedeco.javacpp.annotation.StdVector;

public class ARToolKitPlus extends org.bytedeco.javacpp.presets.ARToolKitPlus {
    public static final int ARTOOLKITPLUS_VERSION_MAJOR = 2;
    public static final int ARTOOLKITPLUS_VERSION_MINOR = 2;
    public static final int AR_AREA_MAX = 100000;
    public static final int AR_AREA_MIN = 70;
    public static final int AR_CHAIN_MAX = 10000;
    public static final int AR_FITTING_TO_IDEAL = 0;
    public static final int AR_FITTING_TO_INPUT = 1;
    public static final double AR_GET_TRANS_CONT_MAT_MAX_FIT_ERROR = 1.0d;
    public static final double AR_GET_TRANS_MAT_MAX_FIT_ERROR = 1.0d;
    public static final int AR_GET_TRANS_MAT_MAX_LOOP_COUNT = 5;
    public static final int AR_IMAGE_PROC_IN_FULL = 0;
    public static final int AR_IMAGE_PROC_IN_HALF = 1;
    public static final int AR_MATCHING_WITHOUT_PCA = 0;
    public static final int AR_MATCHING_WITH_PCA = 1;
    public static final int AR_TEMPLATE_MATCHING_BW = 1;
    public static final int AR_TEMPLATE_MATCHING_COLOR = 0;
    public static final int BCH_DEFAULT_K = 12;
    public static final int BCH_DEFAULT_LENGTH = 36;
    public static final int BCH_DEFAULT_M = 6;
    public static final int BCH_DEFAULT_T = 4;
    public static final int BCH_MAX_LUT = 64;
    public static final int BCH_MAX_M = 6;
    public static final int BCH_MAX_P = 7;
    public static final int BCH_MAX_SQ = 8;
    public static final int DEFAULT_FITTING_MODE = 0;
    public static final int DEFAULT_IMAGE_PROC_MODE = 1;
    public static final int DEFAULT_MATCHING_PCA_MODE = 0;
    public static final int DEFAULT_TEMPLATE_MATCHING_MODE = 0;
    public static final int EVEC_MAX = 10;
    public static final int HULL_FOUR = 1;
    public static final int HULL_FULL = 2;
    public static final int HULL_OFF = 0;
    public static final int IMAGE_FULL_RES = 1;
    public static final int IMAGE_HALF_RES = 0;
    public static final int MARKER_ID_BCH = 2;
    public static final int MARKER_ID_SIMPLE = 1;
    public static final int MARKER_TEMPLATE = 0;
    public static final int MAX_PATTERNS = 256;
    public static final int PIXEL_FORMAT_ABGR = 1;
    public static final int PIXEL_FORMAT_BGR = 3;
    public static final int PIXEL_FORMAT_BGRA = 2;
    public static final int PIXEL_FORMAT_LUM = 7;
    public static final int PIXEL_FORMAT_RGB = 5;
    public static final int PIXEL_FORMAT_RGB565 = 6;
    public static final int PIXEL_FORMAT_RGBA = 4;
    public static final int POSE_ESTIMATOR_ORIGINAL = 0;
    public static final int POSE_ESTIMATOR_ORIGINAL_CONT = 1;
    public static final int POSE_ESTIMATOR_RPP = 2;
    public static final int P_MAX = 500;
    public static final int UNDIST_LUT = 2;
    public static final int UNDIST_NONE = 0;
    public static final int UNDIST_STD = 1;
    public static final int VERSION_MAJOR = 2;
    public static final int VERSION_MINOR = 2;
    public static final int idBits = 9;
    public static final int idMask = 511;
    public static final int idMax = 511;
    public static final int idPattHeight = 6;
    public static final int idPattWidth = 6;
    public static final int pattBits = 36;

    @Namespace("ARToolKitPlus")
    public static class ARMarkerInfo2 extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native int area();

        public native ARMarkerInfo2 area(int i);

        public native int coord_num();

        public native ARMarkerInfo2 coord_num(int i);

        @Cast({"ARFloat"})
        public native float pos(int i);

        public native ARMarkerInfo2 pos(int i, float f);

        @MemberGetter
        @Cast({"ARFloat*"})
        public native FloatPointer pos();

        public native int vertex(int i);

        public native ARMarkerInfo2 vertex(int i, int i2);

        @MemberGetter
        public native IntPointer vertex();

        public native int x_coord(int i);

        public native ARMarkerInfo2 x_coord(int i, int i2);

        @MemberGetter
        public native IntPointer x_coord();

        public native int y_coord(int i);

        public native ARMarkerInfo2 y_coord(int i, int i2);

        @MemberGetter
        public native IntPointer y_coord();

        static {
            Loader.load();
        }

        public ARMarkerInfo2() {
            super((Pointer) null);
            allocate();
        }

        public ARMarkerInfo2(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public ARMarkerInfo2(Pointer p) {
            super(p);
        }

        public ARMarkerInfo2 position(long position) {
            return (ARMarkerInfo2) super.position(position);
        }
    }

    @Namespace("ARToolKitPlus")
    public static class ARMarkerInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native int area();

        public native ARMarkerInfo area(int i);

        @Cast({"ARFloat"})
        public native float cf();

        public native ARMarkerInfo cf(float f);

        public native int dir();

        public native ARMarkerInfo dir(int i);

        public native int id();

        public native ARMarkerInfo id(int i);

        @Cast({"ARFloat"})
        public native float line(int i, int i2);

        public native ARMarkerInfo line(int i, int i2, float f);

        @MemberGetter
        @Cast({"ARFloat(* /*[4]*/ )[3]"})
        public native FloatPointer line();

        @Cast({"ARFloat"})
        public native float pos(int i);

        public native ARMarkerInfo pos(int i, float f);

        @MemberGetter
        @Cast({"ARFloat*"})
        public native FloatPointer pos();

        @Cast({"ARFloat"})
        public native float vertex(int i, int i2);

        public native ARMarkerInfo vertex(int i, int i2, float f);

        @MemberGetter
        @Cast({"ARFloat(* /*[4]*/ )[2]"})
        public native FloatPointer vertex();

        static {
            Loader.load();
        }

        public ARMarkerInfo() {
            super((Pointer) null);
            allocate();
        }

        public ARMarkerInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public ARMarkerInfo(Pointer p) {
            super(p);
        }

        public ARMarkerInfo position(long position) {
            return (ARMarkerInfo) super.position(position);
        }
    }

    @Namespace("ARToolKitPlus")
    public static class ARMat extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native int clm();

        public native ARMat clm(int i);

        public native ARMat m15m(FloatPointer floatPointer);

        @Cast({"ARFloat*"})
        public native FloatPointer m16m();

        public native int row();

        public native ARMat row(int i);

        static {
            Loader.load();
        }

        public ARMat() {
            super((Pointer) null);
            allocate();
        }

        public ARMat(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public ARMat(Pointer p) {
            super(p);
        }

        public ARMat position(long position) {
            return (ARMat) super.position(position);
        }
    }

    @Namespace("ARToolKitPlus")
    public static class ARMultiEachMarkerInfoT extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"ARFloat"})
        public native float center(int i);

        public native ARMultiEachMarkerInfoT center(int i, float f);

        @MemberGetter
        @Cast({"ARFloat*"})
        public native FloatPointer center();

        @Cast({"ARFloat"})
        public native float itrans(int i, int i2);

        public native ARMultiEachMarkerInfoT itrans(int i, int i2, float f);

        @MemberGetter
        @Cast({"ARFloat(* /*[3]*/ )[4]"})
        public native FloatPointer itrans();

        public native int patt_id();

        public native ARMultiEachMarkerInfoT patt_id(int i);

        @Cast({"ARFloat"})
        public native float pos3d(int i, int i2);

        public native ARMultiEachMarkerInfoT pos3d(int i, int i2, float f);

        @MemberGetter
        @Cast({"ARFloat(* /*[4]*/ )[3]"})
        public native FloatPointer pos3d();

        @Cast({"ARFloat"})
        public native float trans(int i, int i2);

        public native ARMultiEachMarkerInfoT trans(int i, int i2, float f);

        @MemberGetter
        @Cast({"ARFloat(* /*[3]*/ )[4]"})
        public native FloatPointer trans();

        public native int visible();

        public native ARMultiEachMarkerInfoT visible(int i);

        public native int visibleR();

        public native ARMultiEachMarkerInfoT visibleR(int i);

        @Cast({"ARFloat"})
        public native float width();

        public native ARMultiEachMarkerInfoT width(float f);

        static {
            Loader.load();
        }

        public ARMultiEachMarkerInfoT() {
            super((Pointer) null);
            allocate();
        }

        public ARMultiEachMarkerInfoT(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public ARMultiEachMarkerInfoT(Pointer p) {
            super(p);
        }

        public ARMultiEachMarkerInfoT position(long position) {
            return (ARMultiEachMarkerInfoT) super.position(position);
        }
    }

    @Namespace("ARToolKitPlus")
    public static class ARMultiMarkerInfoT extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native ARMultiEachMarkerInfoT marker();

        public native ARMultiMarkerInfoT marker(ARMultiEachMarkerInfoT aRMultiEachMarkerInfoT);

        public native int marker_num();

        public native ARMultiMarkerInfoT marker_num(int i);

        public native int prevF();

        public native ARMultiMarkerInfoT prevF(int i);

        @Cast({"ARFloat"})
        public native float trans(int i, int i2);

        public native ARMultiMarkerInfoT trans(int i, int i2, float f);

        @MemberGetter
        @Cast({"ARFloat(* /*[3]*/ )[4]"})
        public native FloatPointer trans();

        @Cast({"ARFloat"})
        public native float transR(int i, int i2);

        public native ARMultiMarkerInfoT transR(int i, int i2, float f);

        @MemberGetter
        @Cast({"ARFloat(* /*[3]*/ )[4]"})
        public native FloatPointer transR();

        static {
            Loader.load();
        }

        public ARMultiMarkerInfoT() {
            super((Pointer) null);
            allocate();
        }

        public ARMultiMarkerInfoT(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public ARMultiMarkerInfoT(Pointer p) {
            super(p);
        }

        public ARMultiMarkerInfoT position(long position) {
            return (ARMultiMarkerInfoT) super.position(position);
        }
    }

    @Namespace("ARToolKitPlus")
    public static class ARVec extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native int clm();

        public native ARVec clm(int i);

        public native ARVec m17v(FloatPointer floatPointer);

        @Cast({"ARFloat*"})
        public native FloatPointer m18v();

        static {
            Loader.load();
        }

        public ARVec() {
            super((Pointer) null);
            allocate();
        }

        public ARVec(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public ARVec(Pointer p) {
            super(p);
        }

        public ARVec position(long position) {
            return (ARVec) super.position(position);
        }
    }

    @Namespace("ARToolKitPlus")
    @NoOffset
    public static class BCH extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"bool"})
        public native boolean decode(@ByRef IntBuffer intBuffer, @ByRef @Cast({"ARToolKitPlus::_64bits*"}) LongBuffer longBuffer, @Cast({"const ARToolKitPlus::_64bits"}) long j);

        @Cast({"bool"})
        public native boolean decode(@ByRef IntBuffer intBuffer, @ByRef @Cast({"ARToolKitPlus::_64bits*"}) LongBuffer longBuffer, @Const IntBuffer intBuffer2);

        @Cast({"bool"})
        public native boolean decode(@ByRef IntPointer intPointer, @ByRef @Cast({"ARToolKitPlus::_64bits*"}) LongPointer longPointer, @Cast({"const ARToolKitPlus::_64bits"}) long j);

        @Cast({"bool"})
        public native boolean decode(@ByRef IntPointer intPointer, @ByRef @Cast({"ARToolKitPlus::_64bits*"}) LongPointer longPointer, @Const IntPointer intPointer2);

        @Cast({"bool"})
        public native boolean decode(@ByRef int[] iArr, @ByRef @Cast({"ARToolKitPlus::_64bits*"}) long[] jArr, @Cast({"const ARToolKitPlus::_64bits"}) long j);

        @Cast({"bool"})
        public native boolean decode(@ByRef int[] iArr, @ByRef @Cast({"ARToolKitPlus::_64bits*"}) long[] jArr, @Const int[] iArr2);

        public native void encode(IntBuffer intBuffer, @Cast({"const ARToolKitPlus::_64bits"}) long j);

        public native void encode(@ByRef @Cast({"ARToolKitPlus::_64bits*"}) LongBuffer longBuffer, @Cast({"const ARToolKitPlus::_64bits"}) long j);

        public native void encode(IntPointer intPointer, @Cast({"const ARToolKitPlus::_64bits"}) long j);

        public native void encode(@ByRef @Cast({"ARToolKitPlus::_64bits*"}) LongPointer longPointer, @Cast({"const ARToolKitPlus::_64bits"}) long j);

        public native void encode(int[] iArr, @Cast({"const ARToolKitPlus::_64bits"}) long j);

        public native void encode(@ByRef @Cast({"ARToolKitPlus::_64bits*"}) long[] jArr, @Cast({"const ARToolKitPlus::_64bits"}) long j);

        static {
            Loader.load();
        }

        public BCH(Pointer p) {
            super(p);
        }

        public BCH(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public BCH position(long position) {
            return (BCH) super.position(position);
        }

        public BCH() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("ARToolKitPlus")
    @NoOffset
    public static class Camera extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"bool"})
        public native boolean changeFrameSize(int i, int i2);

        public native Camera clone();

        @StdString
        public native BytePointer getFileName();

        public native void ideal2Observ(@Cast({"ARFloat"}) float f, @Cast({"ARFloat"}) float f2, @Cast({"ARFloat*"}) FloatBuffer floatBuffer, @Cast({"ARFloat*"}) FloatBuffer floatBuffer2);

        public native void ideal2Observ(@Cast({"ARFloat"}) float f, @Cast({"ARFloat"}) float f2, @Cast({"ARFloat*"}) FloatPointer floatPointer, @Cast({"ARFloat*"}) FloatPointer floatPointer2);

        public native void ideal2Observ(@Cast({"ARFloat"}) float f, @Cast({"ARFloat"}) float f2, @Cast({"ARFloat*"}) float[] fArr, @Cast({"ARFloat*"}) float[] fArr2);

        @Cast({"ARFloat"})
        public native float kc(int i);

        public native Camera kc(int i, float f);

        @MemberGetter
        @Cast({"ARFloat*"})
        public native FloatPointer kc();

        @Cast({"bool"})
        public native boolean loadFromFile(@StdString String str);

        @Cast({"bool"})
        public native boolean loadFromFile(@StdString BytePointer bytePointer);

        @Cast({"ARFloat"})
        public native float mat(int i, int i2);

        public native Camera mat(int i, int i2, float f);

        @MemberGetter
        @Cast({"ARFloat(* /*[3]*/ )[4]"})
        public native FloatPointer mat();

        public native void observ2Ideal(@Cast({"ARFloat"}) float f, @Cast({"ARFloat"}) float f2, @Cast({"ARFloat*"}) FloatBuffer floatBuffer, @Cast({"ARFloat*"}) FloatBuffer floatBuffer2);

        public native void observ2Ideal(@Cast({"ARFloat"}) float f, @Cast({"ARFloat"}) float f2, @Cast({"ARFloat*"}) FloatPointer floatPointer, @Cast({"ARFloat*"}) FloatPointer floatPointer2);

        public native void observ2Ideal(@Cast({"ARFloat"}) float f, @Cast({"ARFloat"}) float f2, @Cast({"ARFloat*"}) float[] fArr, @Cast({"ARFloat*"}) float[] fArr2);

        public native void printSettings();

        public native int xsize();

        public native Camera xsize(int i);

        public native int ysize();

        public native Camera ysize(int i);

        static {
            Loader.load();
        }

        public Camera(Pointer p) {
            super(p);
        }

        public Camera(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Camera position(long position) {
            return (Camera) super.position(position);
        }

        public Camera() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("ARToolKitPlus")
    @NoOffset
    public static class CornerPoint extends Pointer {
        private native void allocate();

        private native void allocate(int i, int i2);

        private native void allocateArray(long j);

        public native CornerPoint m19x(short s);

        public native short m20x();

        public native CornerPoint m21y(short s);

        public native short m22y();

        static {
            Loader.load();
        }

        public CornerPoint(Pointer p) {
            super(p);
        }

        public CornerPoint(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CornerPoint position(long position) {
            return (CornerPoint) super.position(position);
        }

        public CornerPoint() {
            super((Pointer) null);
            allocate();
        }

        public CornerPoint(int nX, int nY) {
            super((Pointer) null);
            allocate(nX, nY);
        }
    }

    @Namespace("ARToolKitPlus")
    public static class MarkerPoint extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native MarkerPoint cornerIdx(short s);

        @Cast({"unsigned short"})
        public native short cornerIdx();

        public native MarkerPoint markerIdx(short s);

        @Cast({"unsigned short"})
        public native short markerIdx();

        @Cast({"ARToolKitPlus::MarkerPoint::coord_type"})
        public native int m23x();

        public native MarkerPoint m24x(int i);

        @Cast({"ARToolKitPlus::MarkerPoint::coord_type"})
        public native int m25y();

        public native MarkerPoint m26y(int i);

        static {
            Loader.load();
        }

        public MarkerPoint() {
            super((Pointer) null);
            allocate();
        }

        public MarkerPoint(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public MarkerPoint(Pointer p) {
            super(p);
        }

        public MarkerPoint position(long position) {
            return (MarkerPoint) super.position(position);
        }
    }

    @Namespace("ARToolKitPlus")
    @NoOffset
    public static class Tracker extends Pointer {
        private native void allocate(int i, int i2);

        private native void allocate(int i, int i2, int i3, int i4, int i5, int i6, int i7);

        @Cast({"bool"})
        public static native boolean calcCameraMatrix(String str, @Cast({"ARFloat"}) float f, @Cast({"ARFloat"}) float f2, @Cast({"ARFloat*"}) FloatBuffer floatBuffer);

        @Cast({"bool"})
        public static native boolean calcCameraMatrix(String str, @Cast({"ARFloat"}) float f, @Cast({"ARFloat"}) float f2, @Cast({"ARFloat*"}) FloatPointer floatPointer);

        @Cast({"bool"})
        public static native boolean calcCameraMatrix(String str, @Cast({"ARFloat"}) float f, @Cast({"ARFloat"}) float f2, @Cast({"ARFloat*"}) float[] fArr);

        @Cast({"bool"})
        public static native boolean calcCameraMatrix(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"ARFloat"}) float f, @Cast({"ARFloat"}) float f2, @Cast({"ARFloat*"}) FloatBuffer floatBuffer);

        @Cast({"bool"})
        public static native boolean calcCameraMatrix(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"ARFloat"}) float f, @Cast({"ARFloat"}) float f2, @Cast({"ARFloat*"}) FloatPointer floatPointer);

        @Cast({"bool"})
        public static native boolean calcCameraMatrix(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"ARFloat"}) float f, @Cast({"ARFloat"}) float f2, @Cast({"ARFloat*"}) float[] fArr);

        public native void activateAutoThreshold(@Cast({"bool"}) boolean z);

        public native void activateBinaryMarker(int i);

        public native void activateVignettingCompensation(@Cast({"bool"}) boolean z);

        public native void activateVignettingCompensation(@Cast({"bool"}) boolean z, int i, int i2, int i3);

        public native int arDetectMarker(@Cast({"const uint8_t*"}) ByteBuffer byteBuffer, int i, @ByPtrPtr ARMarkerInfo aRMarkerInfo, IntBuffer intBuffer);

        public native int arDetectMarker(@Cast({"const uint8_t*"}) BytePointer bytePointer, int i, @ByPtrPtr ARMarkerInfo aRMarkerInfo, IntPointer intPointer);

        public native int arDetectMarker(@Cast({"const uint8_t*"}) BytePointer bytePointer, int i, @Cast({"ARToolKitPlus::ARMarkerInfo**"}) PointerPointer pointerPointer, IntPointer intPointer);

        public native int arDetectMarker(@Cast({"const uint8_t*"}) byte[] bArr, int i, @ByPtrPtr ARMarkerInfo aRMarkerInfo, int[] iArr);

        public native int arDetectMarkerLite(@Cast({"const uint8_t*"}) ByteBuffer byteBuffer, int i, @ByPtrPtr ARMarkerInfo aRMarkerInfo, IntBuffer intBuffer);

        public native int arDetectMarkerLite(@Cast({"const uint8_t*"}) BytePointer bytePointer, int i, @ByPtrPtr ARMarkerInfo aRMarkerInfo, IntPointer intPointer);

        public native int arDetectMarkerLite(@Cast({"const uint8_t*"}) BytePointer bytePointer, int i, @Cast({"ARToolKitPlus::ARMarkerInfo**"}) PointerPointer pointerPointer, IntPointer intPointer);

        public native int arDetectMarkerLite(@Cast({"const uint8_t*"}) byte[] bArr, int i, @ByPtrPtr ARMarkerInfo aRMarkerInfo, int[] iArr);

        public native int arFreePatt(int i);

        @Cast({"ARFloat"})
        public native float arGetTransMat(ARMarkerInfo aRMarkerInfo, @Cast({"ARFloat*"}) FloatBuffer floatBuffer, @Cast({"ARFloat"}) float f, @Cast({"ARFloat(* /*[3]*/ )[4]"}) FloatBuffer floatBuffer2);

        @Cast({"ARFloat"})
        public native float arGetTransMat(ARMarkerInfo aRMarkerInfo, @Cast({"ARFloat*"}) FloatPointer floatPointer, @Cast({"ARFloat"}) float f, @Cast({"ARFloat(* /*[3]*/ )[4]"}) FloatPointer floatPointer2);

        @Cast({"ARFloat"})
        public native float arGetTransMat(ARMarkerInfo aRMarkerInfo, @Cast({"ARFloat*"}) float[] fArr, @Cast({"ARFloat"}) float f, @Cast({"ARFloat(* /*[3]*/ )[4]"}) float[] fArr2);

        @Cast({"ARFloat"})
        public native float arGetTransMatCont(ARMarkerInfo aRMarkerInfo, @Cast({"ARFloat(* /*[3]*/ )[4]"}) FloatBuffer floatBuffer, @Cast({"ARFloat*"}) FloatBuffer floatBuffer2, @Cast({"ARFloat"}) float f, @Cast({"ARFloat(* /*[3]*/ )[4]"}) FloatBuffer floatBuffer3);

        @Cast({"ARFloat"})
        public native float arGetTransMatCont(ARMarkerInfo aRMarkerInfo, @Cast({"ARFloat(* /*[3]*/ )[4]"}) FloatPointer floatPointer, @Cast({"ARFloat*"}) FloatPointer floatPointer2, @Cast({"ARFloat"}) float f, @Cast({"ARFloat(* /*[3]*/ )[4]"}) FloatPointer floatPointer3);

        @Cast({"ARFloat"})
        public native float arGetTransMatCont(ARMarkerInfo aRMarkerInfo, @Cast({"ARFloat(* /*[3]*/ )[4]"}) float[] fArr, @Cast({"ARFloat*"}) float[] fArr2, @Cast({"ARFloat"}) float f, @Cast({"ARFloat(* /*[3]*/ )[4]"}) float[] fArr3);

        public native int arLoadPatt(@Cast({"char*"}) ByteBuffer byteBuffer);

        public native int arLoadPatt(@Cast({"char*"}) BytePointer bytePointer);

        public native int arLoadPatt(@Cast({"char*"}) byte[] bArr);

        public native int arMultiFreeConfig(ARMultiMarkerInfoT aRMultiMarkerInfoT);

        @Cast({"ARFloat"})
        public native float arMultiGetTransMat(ARMarkerInfo aRMarkerInfo, int i, ARMultiMarkerInfoT aRMultiMarkerInfoT);

        @Cast({"ARFloat"})
        public native float arMultiGetTransMatHull(ARMarkerInfo aRMarkerInfo, int i, ARMultiMarkerInfoT aRMultiMarkerInfoT);

        public native ARMultiMarkerInfoT arMultiReadConfigFile(String str);

        public native ARMultiMarkerInfoT arMultiReadConfigFile(@Cast({"const char*"}) BytePointer bytePointer);

        @Cast({"ARFloat"})
        public native float calcOpenGLMatrixFromMarker(ARMarkerInfo aRMarkerInfo, @Cast({"ARFloat*"}) FloatBuffer floatBuffer, @Cast({"ARFloat"}) float f, @Cast({"ARFloat*"}) FloatBuffer floatBuffer2);

        @Cast({"ARFloat"})
        public native float calcOpenGLMatrixFromMarker(ARMarkerInfo aRMarkerInfo, @Cast({"ARFloat*"}) FloatPointer floatPointer, @Cast({"ARFloat"}) float f, @Cast({"ARFloat*"}) FloatPointer floatPointer2);

        @Cast({"ARFloat"})
        public native float calcOpenGLMatrixFromMarker(ARMarkerInfo aRMarkerInfo, @Cast({"ARFloat*"}) float[] fArr, @Cast({"ARFloat"}) float f, @Cast({"ARFloat*"}) float[] fArr2);

        public native void changeCameraSize(int i, int i2);

        @Cast({"ARFloat"})
        public native float executeMultiMarkerPoseEstimator(ARMarkerInfo aRMarkerInfo, int i, ARMultiMarkerInfoT aRMultiMarkerInfoT);

        @Cast({"ARFloat"})
        public native float executeSingleMarkerPoseEstimator(ARMarkerInfo aRMarkerInfo, @Cast({"ARFloat*"}) FloatBuffer floatBuffer, @Cast({"ARFloat"}) float f, @Cast({"ARFloat(* /*[3]*/ )[4]"}) FloatBuffer floatBuffer2);

        @Cast({"ARFloat"})
        public native float executeSingleMarkerPoseEstimator(ARMarkerInfo aRMarkerInfo, @Cast({"ARFloat*"}) FloatPointer floatPointer, @Cast({"ARFloat"}) float f, @Cast({"ARFloat(* /*[3]*/ )[4]"}) FloatPointer floatPointer2);

        @Cast({"ARFloat"})
        public native float executeSingleMarkerPoseEstimator(ARMarkerInfo aRMarkerInfo, @Cast({"ARFloat*"}) float[] fArr, @Cast({"ARFloat"}) float f, @Cast({"ARFloat(* /*[3]*/ )[4]"}) float[] fArr2);

        public native int getBitsPerPixel();

        public native Camera getCamera();

        @Cast({"const ARFloat*"})
        public native FloatPointer getModelViewMatrix();

        public native int getNumLoadablePatterns();

        @Cast({"ARToolKitPlus::PIXEL_FORMAT"})
        public native int getPixelFormat();

        @Cast({"const ARFloat*"})
        public native FloatPointer getProjectionMatrix();

        public native int getThreshold();

        @StdVector
        public native CornerPoint getTrackedCorners();

        @Cast({"bool"})
        public native boolean isAutoThresholdActivated();

        @Cast({"bool"})
        public native boolean loadCameraFile(String str, @Cast({"ARFloat"}) float f, @Cast({"ARFloat"}) float f2);

        @Cast({"bool"})
        public native boolean loadCameraFile(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"ARFloat"}) float f, @Cast({"ARFloat"}) float f2);

        @Cast({"ARFloat"})
        public native float rppGetTransMat(ARMarkerInfo aRMarkerInfo, @Cast({"ARFloat*"}) FloatBuffer floatBuffer, @Cast({"ARFloat"}) float f, @Cast({"ARFloat(* /*[3]*/ )[4]"}) FloatBuffer floatBuffer2);

        @Cast({"ARFloat"})
        public native float rppGetTransMat(ARMarkerInfo aRMarkerInfo, @Cast({"ARFloat*"}) FloatPointer floatPointer, @Cast({"ARFloat"}) float f, @Cast({"ARFloat(* /*[3]*/ )[4]"}) FloatPointer floatPointer2);

        @Cast({"ARFloat"})
        public native float rppGetTransMat(ARMarkerInfo aRMarkerInfo, @Cast({"ARFloat*"}) float[] fArr, @Cast({"ARFloat"}) float f, @Cast({"ARFloat(* /*[3]*/ )[4]"}) float[] fArr2);

        @Cast({"ARFloat"})
        public native float rppMultiGetTransMat(ARMarkerInfo aRMarkerInfo, int i, ARMultiMarkerInfoT aRMultiMarkerInfoT);

        public native void setBorderWidth(@Cast({"ARFloat"}) float f);

        public native void setCamera(Camera camera);

        public native void setCamera(Camera camera, @Cast({"ARFloat"}) float f, @Cast({"ARFloat"}) float f2);

        public native void setHullMode(@Cast({"ARToolKitPlus::HULL_TRACKING_MODE"}) int i);

        public native void setImageProcessingMode(@Cast({"ARToolKitPlus::IMAGE_PROC_MODE"}) int i);

        public native void setLoadUndistLUT(@Cast({"bool"}) boolean z);

        public native void setMarkerMode(@Cast({"ARToolKitPlus::MARKER_MODE"}) int i);

        public native void setNumAutoThresholdRetries(int i);

        @Cast({"bool"})
        public native boolean setPixelFormat(@Cast({"ARToolKitPlus::PIXEL_FORMAT"}) int i);

        @Cast({"bool"})
        public native boolean setPoseEstimator(@Cast({"ARToolKitPlus::POSE_ESTIMATOR"}) int i);

        public native void setThreshold(int i);

        public native void setUndistortionMode(@Cast({"ARToolKitPlus::UNDIST_MODE"}) int i);

        static {
            Loader.load();
        }

        public Tracker(Pointer p) {
            super(p);
        }

        public Tracker(int imWidth, int imHeight, int maxImagePatterns, int pattWidth, int pattHeight, int pattSamples, int maxLoadPatterns) {
            super((Pointer) null);
            allocate(imWidth, imHeight, maxImagePatterns, pattWidth, pattHeight, pattSamples, maxLoadPatterns);
        }

        public Tracker(int imWidth, int imHeight) {
            super((Pointer) null);
            allocate(imWidth, imHeight);
        }
    }

    @Namespace("ARToolKitPlus")
    @NoOffset
    public static class TrackerMultiMarker extends Tracker {
        private native void allocate(int i, int i2);

        private native void allocate(int i, int i2, int i3, int i4, int i5, int i6, int i7);

        public native int calc(@Cast({"const uint8_t*"}) ByteBuffer byteBuffer);

        public native int calc(@Cast({"const uint8_t*"}) BytePointer bytePointer);

        public native int calc(@Cast({"const uint8_t*"}) byte[] bArr);

        public native void getARMatrix(@Cast({"ARFloat(* /*[3]*/ )[4]"}) FloatBuffer floatBuffer);

        public native void getARMatrix(@Cast({"ARFloat(* /*[3]*/ )[4]"}) FloatPointer floatPointer);

        public native void getARMatrix(@Cast({"ARFloat(* /*[3]*/ )[4]"}) float[] fArr);

        @ByRef
        @Const
        public native ARMarkerInfo getDetectedMarker(int i);

        public native void getDetectedMarkers(@ByPtrRef IntBuffer intBuffer);

        public native void getDetectedMarkers(@ByPtrRef IntPointer intPointer);

        public native void getDetectedMarkers(@ByPtrRef int[] iArr);

        @Const
        public native ARMultiMarkerInfoT getMultiMarkerConfig();

        public native int getNumDetectedMarkers();

        @Cast({"bool"})
        public native boolean init(String str, String str2, @Cast({"ARFloat"}) float f, @Cast({"ARFloat"}) float f2);

        @Cast({"bool"})
        public native boolean init(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, @Cast({"ARFloat"}) float f, @Cast({"ARFloat"}) float f2);

        public native void setUseDetectLite(@Cast({"bool"}) boolean z);

        static {
            Loader.load();
        }

        public TrackerMultiMarker(Pointer p) {
            super(p);
        }

        public TrackerMultiMarker(int imWidth, int imHeight, int maxImagePatterns, int pattWidth, int pattHeight, int pattSamples, int maxLoadPatterns) {
            super((Pointer) null);
            allocate(imWidth, imHeight, maxImagePatterns, pattWidth, pattHeight, pattSamples, maxLoadPatterns);
        }

        public TrackerMultiMarker(int imWidth, int imHeight) {
            super((Pointer) null);
            allocate(imWidth, imHeight);
        }
    }

    @Namespace("ARToolKitPlus")
    public static class MultiTracker extends TrackerMultiMarker {
        private native void allocate(int i, int i2);

        static {
            Loader.load();
        }

        public MultiTracker(Pointer p) {
            super(p);
        }

        public MultiTracker(int width, int height) {
            super((Pointer) null);
            allocate(width, height);
        }
    }

    @Namespace("ARToolKitPlus")
    @NoOffset
    public static class TrackerSingleMarker extends Tracker {
        private native void allocate(int i, int i2);

        private native void allocate(int i, int i2, int i3, int i4, int i5, int i6, int i7);

        public native int addPattern(String str);

        public native int addPattern(@Cast({"const char*"}) BytePointer bytePointer);

        @StdVector
        public native IntBuffer calc(@Cast({"const uint8_t*"}) ByteBuffer byteBuffer);

        @StdVector
        public native IntBuffer calc(@Cast({"const uint8_t*"}) ByteBuffer byteBuffer, @ByPtrPtr ARMarkerInfo aRMarkerInfo, IntBuffer intBuffer);

        @StdVector
        public native IntPointer calc(@Cast({"const uint8_t*"}) BytePointer bytePointer);

        @StdVector
        public native IntPointer calc(@Cast({"const uint8_t*"}) BytePointer bytePointer, @ByPtrPtr ARMarkerInfo aRMarkerInfo, IntPointer intPointer);

        @StdVector
        public native IntPointer calc(@Cast({"const uint8_t*"}) BytePointer bytePointer, @Cast({"ARToolKitPlus::ARMarkerInfo**"}) PointerPointer pointerPointer, IntPointer intPointer);

        @StdVector
        public native int[] calc(@Cast({"const uint8_t*"}) byte[] bArr);

        @StdVector
        public native int[] calc(@Cast({"const uint8_t*"}) byte[] bArr, @ByPtrPtr ARMarkerInfo aRMarkerInfo, int[] iArr);

        public native void getARMatrix(@Cast({"ARFloat(* /*[3]*/ )[4]"}) FloatBuffer floatBuffer);

        public native void getARMatrix(@Cast({"ARFloat(* /*[3]*/ )[4]"}) FloatPointer floatPointer);

        public native void getARMatrix(@Cast({"ARFloat(* /*[3]*/ )[4]"}) float[] fArr);

        public native float getConfidence();

        @Cast({"bool"})
        public native boolean init(String str, @Cast({"ARFloat"}) float f, @Cast({"ARFloat"}) float f2);

        @Cast({"bool"})
        public native boolean init(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"ARFloat"}) float f, @Cast({"ARFloat"}) float f2);

        public native int selectBestMarkerByCf();

        public native void selectDetectedMarker(int i);

        public native void setPatternWidth(@Cast({"ARFloat"}) float f);

        static {
            Loader.load();
        }

        public TrackerSingleMarker(Pointer p) {
            super(p);
        }

        public TrackerSingleMarker(int imWidth, int imHeight, int maxImagePatterns, int pattWidth, int pattHeight, int pattSamples, int maxLoadPatterns) {
            super((Pointer) null);
            allocate(imWidth, imHeight, maxImagePatterns, pattWidth, pattHeight, pattSamples, maxLoadPatterns);
        }

        public TrackerSingleMarker(int imWidth, int imHeight) {
            super((Pointer) null);
            allocate(imWidth, imHeight);
        }
    }

    @Namespace("ARToolKitPlus")
    public static class SingleTracker extends TrackerSingleMarker {
        private native void allocate(int i, int i2);

        static {
            Loader.load();
        }

        public SingleTracker(Pointer p) {
            super(p);
        }

        public SingleTracker(int width, int height) {
            super((Pointer) null);
            allocate(width, height);
        }
    }

    @Namespace("ARToolKitPlus")
    public static class arPrevInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native int count();

        public native arPrevInfo count(int i);

        @ByRef
        public native ARMarkerInfo marker();

        public native arPrevInfo marker(ARMarkerInfo aRMarkerInfo);

        static {
            Loader.load();
        }

        public arPrevInfo() {
            super((Pointer) null);
            allocate();
        }

        public arPrevInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public arPrevInfo(Pointer p) {
            super(p);
        }

        public arPrevInfo position(long position) {
            return (arPrevInfo) super.position(position);
        }
    }

    @Namespace("ARToolKitPlus")
    @MemberGetter
    public static native int MAX_HULL_POINTS();

    @Namespace("ARToolKitPlus::Matrix")
    public static native ARMat alloc(int i, int i2);

    @Namespace("ARToolKitPlus::Vector")
    public static native ARVec alloc(int i);

    @Namespace("ARToolKitPlus::Matrix")
    public static native ARMat allocDup(ARMat aRMat);

    @Namespace("rpp")
    public static native void arGetInitRot2_sub(@ByRef @Cast({"rpp_float*"}) DoubleBuffer doubleBuffer, @ByRef @Cast({"double(*)[3][3]"}) PointerPointer pointerPointer, @ByRef @Cast({"rpp_vec*"}) PointerPointer pointerPointer2, @Cast({"const rpp_float*"}) DoubleBuffer doubleBuffer2, @Cast({"const rpp_float*"}) DoubleBuffer doubleBuffer3, @Cast({"const rpp_vec*"}) PointerPointer pointerPointer3, @Cast({"const rpp_vec*"}) PointerPointer pointerPointer4, @Cast({"const unsigned int"}) int i, @Cast({"double(*)[3]"}) DoublePointer doublePointer, @Cast({"const bool"}) boolean z, @Cast({"const rpp_float"}) double d, @Cast({"const rpp_float"}) double d2, @Cast({"const unsigned int"}) int i2);

    @Namespace("rpp")
    public static native void arGetInitRot2_sub(@ByRef @Cast({"rpp_float*"}) DoublePointer doublePointer, @ByRef @Cast({"double(*)[3][3]"}) PointerPointer pointerPointer, @ByRef @Cast({"rpp_vec*"}) PointerPointer pointerPointer2, @Cast({"const rpp_float*"}) DoublePointer doublePointer2, @Cast({"const rpp_float*"}) DoublePointer doublePointer3, @Cast({"const rpp_vec*"}) PointerPointer pointerPointer3, @Cast({"const rpp_vec*"}) PointerPointer pointerPointer4, @Cast({"const unsigned int"}) int i, @Cast({"double(*)[3]"}) DoublePointer doublePointer4, @Cast({"const bool"}) boolean z, @Cast({"const rpp_float"}) double d, @Cast({"const rpp_float"}) double d2, @Cast({"const unsigned int"}) int i2);

    @Namespace("rpp")
    public static native void arGetInitRot2_sub(@ByRef @Cast({"rpp_float*"}) double[] dArr, @ByRef @Cast({"double(*)[3][3]"}) PointerPointer pointerPointer, @ByRef @Cast({"rpp_vec*"}) PointerPointer pointerPointer2, @Cast({"const rpp_float*"}) double[] dArr2, @Cast({"const rpp_float*"}) double[] dArr3, @Cast({"const rpp_vec*"}) PointerPointer pointerPointer3, @Cast({"const rpp_vec*"}) PointerPointer pointerPointer4, @Cast({"const unsigned int"}) int i, @Cast({"double(*)[3]"}) DoublePointer doublePointer, @Cast({"const bool"}) boolean z, @Cast({"const rpp_float"}) double d, @Cast({"const rpp_float"}) double d2, @Cast({"const unsigned int"}) int i2);

    @Namespace("ARToolKitPlus")
    @MemberGetter
    @Cast({"const unsigned int"})
    public static native int bchBits();

    @Namespace("ARToolKitPlus")
    @MemberGetter
    @Cast({"const ARToolKitPlus::IDPATTERN"})
    public static native long bchMask();

    @Namespace("ARToolKitPlus")
    public static native void createImagePattern(@Cast({"ARToolKitPlus::IDPATTERN"}) long j, @Cast({"uint8_t*"}) ByteBuffer byteBuffer);

    @Namespace("ARToolKitPlus")
    public static native void createImagePattern(@Cast({"ARToolKitPlus::IDPATTERN"}) long j, @Cast({"uint8_t*"}) BytePointer bytePointer);

    @Namespace("ARToolKitPlus")
    public static native void createImagePattern(@Cast({"ARToolKitPlus::IDPATTERN"}) long j, @Cast({"uint8_t*"}) byte[] bArr);

    @Namespace("ARToolKitPlus")
    public static native void createImagePatternBCH(int i, @Cast({"uint8_t*"}) ByteBuffer byteBuffer);

    @Namespace("ARToolKitPlus")
    public static native void createImagePatternBCH(int i, @Cast({"uint8_t*"}) BytePointer bytePointer);

    @Namespace("ARToolKitPlus")
    public static native void createImagePatternBCH(int i, @Cast({"uint8_t*"}) byte[] bArr);

    @Namespace("ARToolKitPlus")
    public static native void createImagePatternSimple(int i, @Cast({"uint8_t*"}) ByteBuffer byteBuffer);

    @Namespace("ARToolKitPlus")
    public static native void createImagePatternSimple(int i, @Cast({"uint8_t*"}) BytePointer bytePointer);

    @Namespace("ARToolKitPlus")
    public static native void createImagePatternSimple(int i, @Cast({"uint8_t*"}) byte[] bArr);

    @Namespace("ARToolKitPlus::Matrix")
    public static native int dup(ARMat aRMat, ARMat aRMat2);

    @Namespace("ARToolKitPlus")
    public static native void findFurthestAway(@Const MarkerPoint markerPoint, int i, int i2, int i3, @ByRef IntBuffer intBuffer);

    @Namespace("ARToolKitPlus")
    public static native void findFurthestAway(@Const MarkerPoint markerPoint, int i, int i2, int i3, @ByRef IntPointer intPointer);

    @Namespace("ARToolKitPlus")
    public static native void findFurthestAway(@Const MarkerPoint markerPoint, int i, int i2, int i3, @ByRef int[] iArr);

    @Namespace("ARToolKitPlus")
    public static native void findLongestDiameter(@Const MarkerPoint markerPoint, int i, @ByRef IntBuffer intBuffer, @ByRef IntBuffer intBuffer2);

    @Namespace("ARToolKitPlus")
    public static native void findLongestDiameter(@Const MarkerPoint markerPoint, int i, @ByRef IntPointer intPointer, @ByRef IntPointer intPointer2);

    @Namespace("ARToolKitPlus")
    public static native void findLongestDiameter(@Const MarkerPoint markerPoint, int i, @ByRef int[] iArr, @ByRef int[] iArr2);

    @Namespace("ARToolKitPlus::Matrix")
    public static native int free(ARMat aRMat);

    @Namespace("ARToolKitPlus::Vector")
    public static native int free(ARVec aRVec);

    @Namespace("ARToolKitPlus")
    @MemberGetter
    @Cast({"const ARToolKitPlus::IDPATTERN"})
    public static native long fullMask();

    @Namespace("ARToolKitPlus")
    public static native void generatePatternBCH(int i, @ByRef @Cast({"ARToolKitPlus::IDPATTERN*"}) LongBuffer longBuffer);

    @Namespace("ARToolKitPlus")
    public static native void generatePatternBCH(int i, @ByRef @Cast({"ARToolKitPlus::IDPATTERN*"}) LongPointer longPointer);

    @Namespace("ARToolKitPlus")
    public static native void generatePatternBCH(int i, @ByRef @Cast({"ARToolKitPlus::IDPATTERN*"}) long[] jArr);

    @Namespace("ARToolKitPlus")
    public static native void generatePatternSimple(int i, @ByRef @Cast({"ARToolKitPlus::IDPATTERN*"}) LongBuffer longBuffer);

    @Namespace("ARToolKitPlus")
    public static native void generatePatternSimple(int i, @ByRef @Cast({"ARToolKitPlus::IDPATTERN*"}) LongPointer longPointer);

    @Namespace("ARToolKitPlus")
    public static native void generatePatternSimple(int i, @ByRef @Cast({"ARToolKitPlus::IDPATTERN*"}) long[] jArr);

    @Namespace("ARToolKitPlus::Vector")
    @Cast({"ARFloat"})
    public static native float household(ARVec aRVec);

    @Namespace("ARToolKitPlus")
    public static native int iabs(int i);

    @Namespace("ARToolKitPlus")
    @MemberGetter
    @Cast({"const unsigned int"})
    public static native int idMaxBCH();

    @Namespace("ARToolKitPlus::Vector")
    @Cast({"ARFloat"})
    public static native float innerproduct(ARVec aRVec, ARVec aRVec2);

    @Namespace("ARToolKitPlus")
    @Cast({"bool"})
    public static native boolean isBitSet(@Cast({"ARToolKitPlus::IDPATTERN"}) long j, int i);

    @Namespace("ARToolKitPlus")
    public static native void maximizeArea(@Const MarkerPoint markerPoint, int i, int i2, int i3, int i4, @ByRef IntBuffer intBuffer);

    @Namespace("ARToolKitPlus")
    public static native void maximizeArea(@Const MarkerPoint markerPoint, int i, int i2, int i3, int i4, @ByRef IntPointer intPointer);

    @Namespace("ARToolKitPlus")
    public static native void maximizeArea(@Const MarkerPoint markerPoint, int i, int i2, int i3, int i4, @ByRef int[] iArr);

    @Namespace("ARToolKitPlus::Matrix")
    public static native int mul(ARMat aRMat, ARMat aRMat2, ARMat aRMat3);

    @Namespace("ARToolKitPlus")
    public static native int nearHull_2D(@Const MarkerPoint markerPoint, int i, int i2, MarkerPoint markerPoint2);

    @Namespace("ARToolKitPlus")
    @MemberGetter
    public static native int posMask0();

    @Namespace("ARToolKitPlus")
    @MemberGetter
    public static native int posMask1();

    @Namespace("ARToolKitPlus")
    @MemberGetter
    public static native int posMask2();

    @Namespace("ARToolKitPlus")
    @MemberGetter
    public static native int posMask3();

    public static native void robustPlanarPose(@ByRef @Cast({"rpp_float*"}) DoubleBuffer doubleBuffer, @ByRef @Cast({"double(*)[3][3]"}) PointerPointer pointerPointer, @ByRef @Cast({"rpp_vec*"}) PointerPointer pointerPointer2, @Cast({"const rpp_float*"}) DoubleBuffer doubleBuffer2, @Cast({"const rpp_float*"}) DoubleBuffer doubleBuffer3, @Cast({"const rpp_vec*"}) PointerPointer pointerPointer3, @Cast({"const rpp_vec*"}) PointerPointer pointerPointer4, @Cast({"const unsigned int"}) int i, @Cast({"double(*)[3]"}) DoublePointer doublePointer, @Cast({"const bool"}) boolean z, @Cast({"const rpp_float"}) double d, @Cast({"const rpp_float"}) double d2, @Cast({"const unsigned int"}) int i2);

    public static native void robustPlanarPose(@ByRef @Cast({"rpp_float*"}) DoublePointer doublePointer, @ByRef @Cast({"double(*)[3][3]"}) PointerPointer pointerPointer, @ByRef @Cast({"rpp_vec*"}) PointerPointer pointerPointer2, @Cast({"const rpp_float*"}) DoublePointer doublePointer2, @Cast({"const rpp_float*"}) DoublePointer doublePointer3, @Cast({"const rpp_vec*"}) PointerPointer pointerPointer3, @Cast({"const rpp_vec*"}) PointerPointer pointerPointer4, @Cast({"const unsigned int"}) int i, @Cast({"double(*)[3]"}) DoublePointer doublePointer4, @Cast({"const bool"}) boolean z, @Cast({"const rpp_float"}) double d, @Cast({"const rpp_float"}) double d2, @Cast({"const unsigned int"}) int i2);

    public static native void robustPlanarPose(@ByRef @Cast({"rpp_float*"}) double[] dArr, @ByRef @Cast({"double(*)[3][3]"}) PointerPointer pointerPointer, @ByRef @Cast({"rpp_vec*"}) PointerPointer pointerPointer2, @Cast({"const rpp_float*"}) double[] dArr2, @Cast({"const rpp_float*"}) double[] dArr3, @Cast({"const rpp_vec*"}) PointerPointer pointerPointer3, @Cast({"const rpp_vec*"}) PointerPointer pointerPointer4, @Cast({"const unsigned int"}) int i, @Cast({"double(*)[3]"}) DoublePointer doublePointer, @Cast({"const bool"}) boolean z, @Cast({"const rpp_float"}) double d, @Cast({"const rpp_float"}) double d2, @Cast({"const unsigned int"}) int i2);

    @Namespace("ARToolKitPlus")
    @MemberGetter
    public static native int rotate90(int i);

    @Namespace("ARToolKitPlus")
    @MemberGetter
    @Const
    public static native IntPointer rotate90();

    @Namespace("ARToolKitPlus::Matrix")
    public static native int selfInv(ARMat aRMat);

    @Namespace("ARToolKitPlus")
    public static native void sortInLastInteger(@ByRef IntBuffer intBuffer, @ByRef IntBuffer intBuffer2, @ByRef IntBuffer intBuffer3, @ByRef IntBuffer intBuffer4);

    @Namespace("ARToolKitPlus")
    public static native void sortInLastInteger(@ByRef IntPointer intPointer, @ByRef IntPointer intPointer2, @ByRef IntPointer intPointer3, @ByRef IntPointer intPointer4);

    @Namespace("ARToolKitPlus")
    public static native void sortInLastInteger(@ByRef int[] iArr, @ByRef int[] iArr2, @ByRef int[] iArr3, @ByRef int[] iArr4);

    @Namespace("ARToolKitPlus")
    public static native void sortIntegers(@ByRef IntBuffer intBuffer, @ByRef IntBuffer intBuffer2, @ByRef IntBuffer intBuffer3);

    @Namespace("ARToolKitPlus")
    public static native void sortIntegers(@ByRef IntPointer intPointer, @ByRef IntPointer intPointer2, @ByRef IntPointer intPointer3);

    @Namespace("ARToolKitPlus")
    public static native void sortIntegers(@ByRef int[] iArr, @ByRef int[] iArr2, @ByRef int[] iArr3);

    @Namespace("ARToolKitPlus::Vector")
    public static native int tridiagonalize(ARMat aRMat, ARVec aRVec, ARVec aRVec2);

    @Namespace("ARToolKitPlus")
    @MemberGetter
    @Cast({"const ARToolKitPlus::IDPATTERN"})
    public static native long xorMask0();

    @Namespace("ARToolKitPlus")
    @MemberGetter
    @Cast({"const ARToolKitPlus::IDPATTERN"})
    public static native long xorMask1();

    @Namespace("ARToolKitPlus")
    @MemberGetter
    @Cast({"const ARToolKitPlus::IDPATTERN"})
    public static native long xorMask2();

    @Namespace("ARToolKitPlus")
    @MemberGetter
    @Cast({"const ARToolKitPlus::IDPATTERN"})
    public static native long xorMask3();

    static {
        Loader.load();
    }
}
