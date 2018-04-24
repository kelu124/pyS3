package org.bytedeco.javacpp;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.bytedeco.javacpp.annotation.ByPtrPtr;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.Convention;
import org.bytedeco.javacpp.annotation.MemberGetter;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.helper.opencv_core$CvArr;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.Texture2D;
import org.bytedeco.javacpp.opencv_core.UMat;
import org.bytedeco.javacpp.opencv_imgproc.CvFont;
import org.bytedeco.javacpp.presets.opencv_core.Str;

public class opencv_highgui extends org.bytedeco.javacpp.presets.opencv_highgui {
    public static final int CV_CHECKBOX = 1;
    public static final int CV_EVENT_FLAG_ALTKEY = 32;
    public static final int CV_EVENT_FLAG_CTRLKEY = 8;
    public static final int CV_EVENT_FLAG_LBUTTON = 1;
    public static final int CV_EVENT_FLAG_MBUTTON = 4;
    public static final int CV_EVENT_FLAG_RBUTTON = 2;
    public static final int CV_EVENT_FLAG_SHIFTKEY = 16;
    public static final int CV_EVENT_LBUTTONDBLCLK = 7;
    public static final int CV_EVENT_LBUTTONDOWN = 1;
    public static final int CV_EVENT_LBUTTONUP = 4;
    public static final int CV_EVENT_MBUTTONDBLCLK = 9;
    public static final int CV_EVENT_MBUTTONDOWN = 3;
    public static final int CV_EVENT_MBUTTONUP = 6;
    public static final int CV_EVENT_MOUSEHWHEEL = 11;
    public static final int CV_EVENT_MOUSEMOVE = 0;
    public static final int CV_EVENT_MOUSEWHEEL = 10;
    public static final int CV_EVENT_RBUTTONDBLCLK = 8;
    public static final int CV_EVENT_RBUTTONDOWN = 2;
    public static final int CV_EVENT_RBUTTONUP = 5;
    public static final int CV_FONT_BLACK = 87;
    public static final int CV_FONT_BOLD = 75;
    public static final int CV_FONT_DEMIBOLD = 63;
    public static final int CV_FONT_LIGHT = 25;
    public static final int CV_FONT_NORMAL = 50;
    public static final int CV_GUI_EXPANDED = 0;
    public static final int CV_GUI_NORMAL = 16;
    public static final int CV_PUSH_BUTTON = 0;
    public static final int CV_RADIOBOX = 2;
    public static final int CV_STYLE_ITALIC = 1;
    public static final int CV_STYLE_NORMAL = 0;
    public static final int CV_STYLE_OBLIQUE = 2;
    public static final int CV_WINDOW_AUTOSIZE = 1;
    public static final int CV_WINDOW_FREERATIO = 256;
    public static final int CV_WINDOW_FULLSCREEN = 1;
    public static final int CV_WINDOW_KEEPRATIO = 0;
    public static final int CV_WINDOW_NORMAL = 0;
    public static final int CV_WINDOW_OPENGL = 4096;
    public static final int CV_WND_PROP_ASPECTRATIO = 2;
    public static final int CV_WND_PROP_AUTOSIZE = 1;
    public static final int CV_WND_PROP_FULLSCREEN = 0;
    public static final int CV_WND_PROP_OPENGL = 3;
    public static final int EVENT_FLAG_ALTKEY = 32;
    public static final int EVENT_FLAG_CTRLKEY = 8;
    public static final int EVENT_FLAG_LBUTTON = 1;
    public static final int EVENT_FLAG_MBUTTON = 4;
    public static final int EVENT_FLAG_RBUTTON = 2;
    public static final int EVENT_FLAG_SHIFTKEY = 16;
    public static final int EVENT_LBUTTONDBLCLK = 7;
    public static final int EVENT_LBUTTONDOWN = 1;
    public static final int EVENT_LBUTTONUP = 4;
    public static final int EVENT_MBUTTONDBLCLK = 9;
    public static final int EVENT_MBUTTONDOWN = 3;
    public static final int EVENT_MBUTTONUP = 6;
    public static final int EVENT_MOUSEHWHEEL = 11;
    public static final int EVENT_MOUSEMOVE = 0;
    public static final int EVENT_MOUSEWHEEL = 10;
    public static final int EVENT_RBUTTONDBLCLK = 8;
    public static final int EVENT_RBUTTONDOWN = 2;
    public static final int EVENT_RBUTTONUP = 5;
    public static final int HG_AUTOSIZE = 1;
    public static final int QT_CHECKBOX = 1;
    public static final int QT_FONT_BLACK = 87;
    public static final int QT_FONT_BOLD = 75;
    public static final int QT_FONT_DEMIBOLD = 63;
    public static final int QT_FONT_LIGHT = 25;
    public static final int QT_FONT_NORMAL = 50;
    public static final int QT_PUSH_BUTTON = 0;
    public static final int QT_RADIOBOX = 2;
    public static final int QT_STYLE_ITALIC = 1;
    public static final int QT_STYLE_NORMAL = 0;
    public static final int QT_STYLE_OBLIQUE = 2;
    public static final int WINDOW_AUTOSIZE = 1;
    public static final int WINDOW_FREERATIO = 256;
    public static final int WINDOW_FULLSCREEN = 1;
    public static final int WINDOW_KEEPRATIO = 0;
    public static final int WINDOW_NORMAL = 0;
    public static final int WINDOW_OPENGL = 4096;
    public static final int WND_PROP_ASPECT_RATIO = 2;
    public static final int WND_PROP_AUTOSIZE = 1;
    public static final int WND_PROP_FULLSCREEN = 0;
    public static final int WND_PROP_OPENGL = 3;

    @Convention("CV_CDECL")
    public static class CvButtonCallback extends FunctionPointer {
        private native void allocate();

        public native void call(int i, Pointer pointer);

        static {
            Loader.load();
        }

        public CvButtonCallback(Pointer p) {
            super(p);
        }

        protected CvButtonCallback() {
            allocate();
        }
    }

    @Convention("CV_CDECL")
    public static class CvMouseCallback extends FunctionPointer {
        private native void allocate();

        public native void call(int i, int i2, int i3, int i4, Pointer pointer);

        static {
            Loader.load();
        }

        public CvMouseCallback(Pointer p) {
            super(p);
        }

        protected CvMouseCallback() {
            allocate();
        }
    }

    @Convention("CV_CDECL")
    public static class CvOpenGlDrawCallback extends FunctionPointer {
        private native void allocate();

        public native void call(Pointer pointer);

        static {
            Loader.load();
        }

        public CvOpenGlDrawCallback(Pointer p) {
            super(p);
        }

        protected CvOpenGlDrawCallback() {
            allocate();
        }
    }

    @Convention("CV_CDECL")
    public static class CvTrackbarCallback2 extends FunctionPointer {
        private native void allocate();

        public native void call(int i, Pointer pointer);

        static {
            Loader.load();
        }

        public CvTrackbarCallback2(Pointer p) {
            super(p);
        }

        protected CvTrackbarCallback2() {
            allocate();
        }
    }

    @Convention("CV_CDECL")
    public static class CvTrackbarCallback extends FunctionPointer {
        private native void allocate();

        public native void call(int i);

        static {
            Loader.load();
        }

        public CvTrackbarCallback(Pointer p) {
            super(p);
        }

        protected CvTrackbarCallback() {
            allocate();
        }
    }

    @Namespace("cv")
    public static class QtFont extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @MemberGetter
        @Const
        public native IntPointer ascii();

        @ByRef
        public native Scalar color();

        public native QtFont color(Scalar scalar);

        @MemberGetter
        @Const
        public native IntPointer cyrillic();

        public native float dx();

        public native QtFont dx(float f);

        public native int font_face();

        public native QtFont font_face(int i);

        @MemberGetter
        @Const
        public native IntPointer greek();

        public native float hscale();

        public native QtFont hscale(float f);

        public native int line_type();

        public native QtFont line_type(int i);

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer nameFont();

        public native float shear();

        public native QtFont shear(float f);

        public native int thickness();

        public native QtFont thickness(int i);

        public native float vscale();

        public native QtFont vscale(float f);

        static {
            Loader.load();
        }

        public QtFont() {
            super((Pointer) null);
            allocate();
        }

        public QtFont(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public QtFont(Pointer p) {
            super(p);
        }

        public QtFont position(long position) {
            return (QtFont) super.position(position);
        }
    }

    @Namespace("cv")
    public static native void addText(@ByRef @Const Mat mat, @Str String str, @ByVal Point point, @ByRef @Const QtFont qtFont);

    @Namespace("cv")
    public static native void addText(@ByRef @Const Mat mat, @Str BytePointer bytePointer, @ByVal Point point, @ByRef @Const QtFont qtFont);

    @Namespace("cv")
    public static native int createButton(@Str String str, ButtonCallback buttonCallback);

    @Namespace("cv")
    public static native int createButton(@Str String str, ButtonCallback buttonCallback, Pointer pointer, int i, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native int createButton(@Str BytePointer bytePointer, ButtonCallback buttonCallback);

    @Namespace("cv")
    public static native int createButton(@Str BytePointer bytePointer, ButtonCallback buttonCallback, Pointer pointer, int i, @Cast({"bool"}) boolean z);

    @Namespace("cv")
    public static native int createTrackbar(@Str String str, @Str String str2, IntBuffer intBuffer, int i);

    @Namespace("cv")
    public static native int createTrackbar(@Str String str, @Str String str2, IntBuffer intBuffer, int i, TrackbarCallback trackbarCallback, Pointer pointer);

    @Namespace("cv")
    public static native int createTrackbar(@Str String str, @Str String str2, IntPointer intPointer, int i);

    @Namespace("cv")
    public static native int createTrackbar(@Str String str, @Str String str2, IntPointer intPointer, int i, TrackbarCallback trackbarCallback, Pointer pointer);

    @Namespace("cv")
    public static native int createTrackbar(@Str String str, @Str String str2, int[] iArr, int i);

    @Namespace("cv")
    public static native int createTrackbar(@Str String str, @Str String str2, int[] iArr, int i, TrackbarCallback trackbarCallback, Pointer pointer);

    @Namespace("cv")
    public static native int createTrackbar(@Str BytePointer bytePointer, @Str BytePointer bytePointer2, IntBuffer intBuffer, int i);

    @Namespace("cv")
    public static native int createTrackbar(@Str BytePointer bytePointer, @Str BytePointer bytePointer2, IntBuffer intBuffer, int i, TrackbarCallback trackbarCallback, Pointer pointer);

    @Namespace("cv")
    public static native int createTrackbar(@Str BytePointer bytePointer, @Str BytePointer bytePointer2, IntPointer intPointer, int i);

    @Namespace("cv")
    public static native int createTrackbar(@Str BytePointer bytePointer, @Str BytePointer bytePointer2, IntPointer intPointer, int i, TrackbarCallback trackbarCallback, Pointer pointer);

    @Namespace("cv")
    public static native int createTrackbar(@Str BytePointer bytePointer, @Str BytePointer bytePointer2, int[] iArr, int i);

    @Namespace("cv")
    public static native int createTrackbar(@Str BytePointer bytePointer, @Str BytePointer bytePointer2, int[] iArr, int i, TrackbarCallback trackbarCallback, Pointer pointer);

    public static native void cvAddSearchPath(String str);

    public static native void cvAddSearchPath(@Cast({"const char*"}) BytePointer bytePointer);

    @Platform({"linux"})
    public static native void cvAddText(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, String str, @Cast({"CvPoint*"}) @ByVal IntBuffer intBuffer, CvFont cvFont);

    @Platform({"linux"})
    public static native void cvAddText(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, String str, @ByVal CvPoint cvPoint, CvFont cvFont);

    @Platform({"linux"})
    public static native void cvAddText(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, String str, @Cast({"CvPoint*"}) @ByVal int[] iArr, CvFont cvFont);

    @Platform({"linux"})
    public static native void cvAddText(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"CvPoint*"}) @ByVal IntBuffer intBuffer, CvFont cvFont);

    @Platform({"linux"})
    public static native void cvAddText(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Cast({"const char*"}) BytePointer bytePointer, @ByVal CvPoint cvPoint, CvFont cvFont);

    @Platform({"linux"})
    public static native void cvAddText(@Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"CvPoint*"}) @ByVal int[] iArr, CvFont cvFont);

    @Platform({"linux"})
    public static native int cvCreateButton();

    @Platform({"linux"})
    public static native int cvCreateButton(String str, CvButtonCallback cvButtonCallback, Pointer pointer, int i, int i2);

    @Platform({"linux"})
    public static native int cvCreateButton(@Cast({"const char*"}) BytePointer bytePointer, CvButtonCallback cvButtonCallback, Pointer pointer, int i, int i2);

    public static native int cvCreateTrackbar(String str, String str2, IntBuffer intBuffer, int i);

    public static native int cvCreateTrackbar(String str, String str2, IntBuffer intBuffer, int i, CvTrackbarCallback cvTrackbarCallback);

    public static native int cvCreateTrackbar(String str, String str2, IntPointer intPointer, int i);

    public static native int cvCreateTrackbar(String str, String str2, IntPointer intPointer, int i, CvTrackbarCallback cvTrackbarCallback);

    public static native int cvCreateTrackbar(String str, String str2, int[] iArr, int i);

    public static native int cvCreateTrackbar(String str, String str2, int[] iArr, int i, CvTrackbarCallback cvTrackbarCallback);

    public static native int cvCreateTrackbar(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, IntBuffer intBuffer, int i);

    public static native int cvCreateTrackbar(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, IntBuffer intBuffer, int i, CvTrackbarCallback cvTrackbarCallback);

    public static native int cvCreateTrackbar(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, IntPointer intPointer, int i);

    public static native int cvCreateTrackbar(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, IntPointer intPointer, int i, CvTrackbarCallback cvTrackbarCallback);

    public static native int cvCreateTrackbar(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, int[] iArr, int i);

    public static native int cvCreateTrackbar(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, int[] iArr, int i, CvTrackbarCallback cvTrackbarCallback);

    public static native int cvCreateTrackbar2(String str, String str2, IntBuffer intBuffer, int i, CvTrackbarCallback2 cvTrackbarCallback2);

    public static native int cvCreateTrackbar2(String str, String str2, IntBuffer intBuffer, int i, CvTrackbarCallback2 cvTrackbarCallback2, Pointer pointer);

    public static native int cvCreateTrackbar2(String str, String str2, IntPointer intPointer, int i, CvTrackbarCallback2 cvTrackbarCallback2);

    public static native int cvCreateTrackbar2(String str, String str2, IntPointer intPointer, int i, CvTrackbarCallback2 cvTrackbarCallback2, Pointer pointer);

    public static native int cvCreateTrackbar2(String str, String str2, int[] iArr, int i, CvTrackbarCallback2 cvTrackbarCallback2);

    public static native int cvCreateTrackbar2(String str, String str2, int[] iArr, int i, CvTrackbarCallback2 cvTrackbarCallback2, Pointer pointer);

    public static native int cvCreateTrackbar2(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, IntBuffer intBuffer, int i, CvTrackbarCallback2 cvTrackbarCallback2);

    public static native int cvCreateTrackbar2(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, IntBuffer intBuffer, int i, CvTrackbarCallback2 cvTrackbarCallback2, Pointer pointer);

    public static native int cvCreateTrackbar2(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, IntPointer intPointer, int i, CvTrackbarCallback2 cvTrackbarCallback2);

    public static native int cvCreateTrackbar2(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, IntPointer intPointer, int i, CvTrackbarCallback2 cvTrackbarCallback2, Pointer pointer);

    public static native int cvCreateTrackbar2(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, int[] iArr, int i, CvTrackbarCallback2 cvTrackbarCallback2);

    public static native int cvCreateTrackbar2(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, int[] iArr, int i, CvTrackbarCallback2 cvTrackbarCallback2, Pointer pointer);

    public static native void cvDestroyAllWindows();

    public static native void cvDestroyWindow(String str);

    public static native void cvDestroyWindow(@Cast({"const char*"}) BytePointer bytePointer);

    @Platform({"linux"})
    public static native void cvDisplayOverlay(String str, String str2);

    @Platform({"linux"})
    public static native void cvDisplayOverlay(String str, String str2, int i);

    @Platform({"linux"})
    public static native void cvDisplayOverlay(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2);

    @Platform({"linux"})
    public static native void cvDisplayOverlay(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, int i);

    @Platform({"linux"})
    public static native void cvDisplayStatusBar(String str, String str2);

    @Platform({"linux"})
    public static native void cvDisplayStatusBar(String str, String str2, int i);

    @Platform({"linux"})
    public static native void cvDisplayStatusBar(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2);

    @Platform({"linux"})
    public static native void cvDisplayStatusBar(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, int i);

    @Platform({"linux"})
    @ByVal
    public static native CvFont cvFontQt(String str);

    @Platform({"linux"})
    @ByVal
    public static native CvFont cvFontQt(String str, int i, @ByVal(nullValue = "CvScalar(cvScalarAll(0))") CvScalar cvScalar, int i2, int i3, int i4);

    @Platform({"linux"})
    @ByVal
    public static native CvFont cvFontQt(@Cast({"const char*"}) BytePointer bytePointer);

    @Platform({"linux"})
    @ByVal
    public static native CvFont cvFontQt(@Cast({"const char*"}) BytePointer bytePointer, int i, @ByVal(nullValue = "CvScalar(cvScalarAll(0))") CvScalar cvScalar, int i2, int i3, int i4);

    public static native int cvGetTrackbarPos(String str, String str2);

    public static native int cvGetTrackbarPos(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2);

    public static native Pointer cvGetWindowHandle(String str);

    public static native Pointer cvGetWindowHandle(@Cast({"const char*"}) BytePointer bytePointer);

    @Cast({"const char*"})
    public static native BytePointer cvGetWindowName(Pointer pointer);

    public static native double cvGetWindowProperty(String str, int i);

    public static native double cvGetWindowProperty(@Cast({"const char*"}) BytePointer bytePointer, int i);

    public static native int cvInitSystem(int i, @ByPtrPtr @Cast({"char**"}) ByteBuffer byteBuffer);

    public static native int cvInitSystem(int i, @ByPtrPtr @Cast({"char**"}) BytePointer bytePointer);

    public static native int cvInitSystem(int i, @Cast({"char**"}) PointerPointer pointerPointer);

    public static native int cvInitSystem(int i, @ByPtrPtr @Cast({"char**"}) byte[] bArr);

    @Platform({"linux"})
    public static native void cvLoadWindowParameters(String str);

    @Platform({"linux"})
    public static native void cvLoadWindowParameters(@Cast({"const char*"}) BytePointer bytePointer);

    public static native void cvMoveWindow(String str, int i, int i2);

    public static native void cvMoveWindow(@Cast({"const char*"}) BytePointer bytePointer, int i, int i2);

    public static native int cvNamedWindow(String str);

    public static native int cvNamedWindow(String str, int i);

    public static native int cvNamedWindow(@Cast({"const char*"}) BytePointer bytePointer);

    public static native int cvNamedWindow(@Cast({"const char*"}) BytePointer bytePointer, int i);

    public static native void cvResizeWindow(String str, int i, int i2);

    public static native void cvResizeWindow(@Cast({"const char*"}) BytePointer bytePointer, int i, int i2);

    @Platform({"linux"})
    public static native void cvSaveWindowParameters(String str);

    @Platform({"linux"})
    public static native void cvSaveWindowParameters(@Cast({"const char*"}) BytePointer bytePointer);

    public static native void cvSetMouseCallback(String str, CvMouseCallback cvMouseCallback);

    public static native void cvSetMouseCallback(String str, CvMouseCallback cvMouseCallback, Pointer pointer);

    public static native void cvSetMouseCallback(@Cast({"const char*"}) BytePointer bytePointer, CvMouseCallback cvMouseCallback);

    public static native void cvSetMouseCallback(@Cast({"const char*"}) BytePointer bytePointer, CvMouseCallback cvMouseCallback, Pointer pointer);

    public static native void cvSetOpenGlContext(String str);

    public static native void cvSetOpenGlContext(@Cast({"const char*"}) BytePointer bytePointer);

    public static native void cvSetOpenGlDrawCallback(String str, CvOpenGlDrawCallback cvOpenGlDrawCallback);

    public static native void cvSetOpenGlDrawCallback(String str, CvOpenGlDrawCallback cvOpenGlDrawCallback, Pointer pointer);

    public static native void cvSetOpenGlDrawCallback(@Cast({"const char*"}) BytePointer bytePointer, CvOpenGlDrawCallback cvOpenGlDrawCallback);

    public static native void cvSetOpenGlDrawCallback(@Cast({"const char*"}) BytePointer bytePointer, CvOpenGlDrawCallback cvOpenGlDrawCallback, Pointer pointer);

    public static native void cvSetTrackbarMax(String str, String str2, int i);

    public static native void cvSetTrackbarMax(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, int i);

    public static native void cvSetTrackbarMin(String str, String str2, int i);

    public static native void cvSetTrackbarMin(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, int i);

    public static native void cvSetTrackbarPos(String str, String str2, int i);

    public static native void cvSetTrackbarPos(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, int i);

    public static native void cvSetWindowProperty(String str, int i, double d);

    public static native void cvSetWindowProperty(@Cast({"const char*"}) BytePointer bytePointer, int i, double d);

    public static native void cvShowImage(String str, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native void cvShowImage(@Cast({"const char*"}) BytePointer bytePointer, @Const opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    @Platform({"linux"})
    public static native int cvStartLoop(Pt2Func_int_ByteBuffer pt2Func_int_ByteBuffer, int i, @ByPtrPtr @Cast({"char**"}) ByteBuffer byteBuffer);

    @Platform({"linux"})
    public static native int cvStartLoop(Pt2Func_int_BytePointer pt2Func_int_BytePointer, int i, @ByPtrPtr @Cast({"char**"}) BytePointer bytePointer);

    @Platform({"linux"})
    public static native int cvStartLoop(Pt2Func_int_PointerPointer pt2Func_int_PointerPointer, int i, @Cast({"char**"}) PointerPointer pointerPointer);

    @Platform({"linux"})
    public static native int cvStartLoop(Pt2Func_int_byte__ pt2Func_int_byte__, int i, @ByPtrPtr @Cast({"char**"}) byte[] bArr);

    public static native int cvStartWindowThread();

    @Platform({"linux"})
    public static native void cvStopLoop();

    public static native void cvUpdateWindow(String str);

    public static native void cvUpdateWindow(@Cast({"const char*"}) BytePointer bytePointer);

    public static native int cvWaitKey();

    public static native int cvWaitKey(int i);

    public static native void cvvAddSearchPath(String str);

    public static native void cvvAddSearchPath(@Cast({"const char*"}) BytePointer bytePointer);

    public static native int cvvCreateTrackbar(String str, String str2, IntBuffer intBuffer, int i, CvTrackbarCallback cvTrackbarCallback);

    public static native int cvvCreateTrackbar(String str, String str2, IntPointer intPointer, int i, CvTrackbarCallback cvTrackbarCallback);

    public static native int cvvCreateTrackbar(String str, String str2, int[] iArr, int i, CvTrackbarCallback cvTrackbarCallback);

    public static native int cvvCreateTrackbar(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, IntBuffer intBuffer, int i, CvTrackbarCallback cvTrackbarCallback);

    public static native int cvvCreateTrackbar(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, IntPointer intPointer, int i, CvTrackbarCallback cvTrackbarCallback);

    public static native int cvvCreateTrackbar(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"const char*"}) BytePointer bytePointer2, int[] iArr, int i, CvTrackbarCallback cvTrackbarCallback);

    public static native void cvvDestroyWindow(String str);

    public static native void cvvDestroyWindow(@Cast({"const char*"}) BytePointer bytePointer);

    public static native int cvvInitSystem(int i, @ByPtrPtr @Cast({"char**"}) ByteBuffer byteBuffer);

    public static native int cvvInitSystem(int i, @ByPtrPtr @Cast({"char**"}) BytePointer bytePointer);

    public static native int cvvInitSystem(int i, @Cast({"char**"}) PointerPointer pointerPointer);

    public static native int cvvInitSystem(int i, @ByPtrPtr @Cast({"char**"}) byte[] bArr);

    public static native void cvvNamedWindow(String str, int i);

    public static native void cvvNamedWindow(@Cast({"const char*"}) BytePointer bytePointer, int i);

    public static native void cvvResizeWindow(String str, int i, int i2);

    public static native void cvvResizeWindow(@Cast({"const char*"}) BytePointer bytePointer, int i, int i2);

    public static native void cvvShowImage(String str, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native void cvvShowImage(@Cast({"const char*"}) BytePointer bytePointer, opencv_core$CvArr org_bytedeco_javacpp_helper_opencv_core_CvArr);

    public static native int cvvWaitKey(String str);

    public static native int cvvWaitKey(@Cast({"const char*"}) BytePointer bytePointer);

    public static native int cvvWaitKeyEx(String str, int i);

    public static native int cvvWaitKeyEx(@Cast({"const char*"}) BytePointer bytePointer, int i);

    @Namespace("cv")
    public static native void destroyAllWindows();

    @Namespace("cv")
    public static native void destroyWindow(@Str String str);

    @Namespace("cv")
    public static native void destroyWindow(@Str BytePointer bytePointer);

    @Namespace("cv")
    public static native void displayOverlay(@Str String str, @Str String str2);

    @Namespace("cv")
    public static native void displayOverlay(@Str String str, @Str String str2, int i);

    @Namespace("cv")
    public static native void displayOverlay(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Namespace("cv")
    public static native void displayOverlay(@Str BytePointer bytePointer, @Str BytePointer bytePointer2, int i);

    @Namespace("cv")
    public static native void displayStatusBar(@Str String str, @Str String str2);

    @Namespace("cv")
    public static native void displayStatusBar(@Str String str, @Str String str2, int i);

    @Namespace("cv")
    public static native void displayStatusBar(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Namespace("cv")
    public static native void displayStatusBar(@Str BytePointer bytePointer, @Str BytePointer bytePointer2, int i);

    @Namespace("cv")
    @ByVal
    public static native QtFont fontQt(@Str String str);

    @Namespace("cv")
    @ByVal
    public static native QtFont fontQt(@Str String str, int i, @ByVal(nullValue = "cv::Scalar::all(0)") Scalar scalar, int i2, int i3, int i4);

    @Namespace("cv")
    @ByVal
    public static native QtFont fontQt(@Str BytePointer bytePointer);

    @Namespace("cv")
    @ByVal
    public static native QtFont fontQt(@Str BytePointer bytePointer, int i, @ByVal(nullValue = "cv::Scalar::all(0)") Scalar scalar, int i2, int i3, int i4);

    @Namespace("cv")
    public static native int getMouseWheelDelta(int i);

    @Namespace("cv")
    public static native int getTrackbarPos(@Str String str, @Str String str2);

    @Namespace("cv")
    public static native int getTrackbarPos(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Namespace("cv")
    public static native double getWindowProperty(@Str String str, int i);

    @Namespace("cv")
    public static native double getWindowProperty(@Str BytePointer bytePointer, int i);

    @Namespace("cv")
    public static native void imshow(@Str String str, @ByVal Mat mat);

    @Namespace("cv")
    public static native void imshow(@Str String str, @ByRef @Const Texture2D texture2D);

    @Namespace("cv")
    public static native void imshow(@Str String str, @ByVal UMat uMat);

    @Namespace("cv")
    public static native void imshow(@Str BytePointer bytePointer, @ByVal Mat mat);

    @Namespace("cv")
    public static native void imshow(@Str BytePointer bytePointer, @ByRef @Const Texture2D texture2D);

    @Namespace("cv")
    public static native void imshow(@Str BytePointer bytePointer, @ByVal UMat uMat);

    @Namespace("cv")
    public static native void loadWindowParameters(@Str String str);

    @Namespace("cv")
    public static native void loadWindowParameters(@Str BytePointer bytePointer);

    @Namespace("cv")
    public static native void moveWindow(@Str String str, int i, int i2);

    @Namespace("cv")
    public static native void moveWindow(@Str BytePointer bytePointer, int i, int i2);

    @Namespace("cv")
    public static native void namedWindow(@Str String str);

    @Namespace("cv")
    public static native void namedWindow(@Str String str, int i);

    @Namespace("cv")
    public static native void namedWindow(@Str BytePointer bytePointer);

    @Namespace("cv")
    public static native void namedWindow(@Str BytePointer bytePointer, int i);

    @Namespace("cv")
    public static native void resizeWindow(@Str String str, int i, int i2);

    @Namespace("cv")
    public static native void resizeWindow(@Str BytePointer bytePointer, int i, int i2);

    @Namespace("cv")
    public static native void saveWindowParameters(@Str String str);

    @Namespace("cv")
    public static native void saveWindowParameters(@Str BytePointer bytePointer);

    @Namespace("cv")
    public static native void setMouseCallback(@Str String str, MouseCallback mouseCallback);

    @Namespace("cv")
    public static native void setMouseCallback(@Str String str, MouseCallback mouseCallback, Pointer pointer);

    @Namespace("cv")
    public static native void setMouseCallback(@Str BytePointer bytePointer, MouseCallback mouseCallback);

    @Namespace("cv")
    public static native void setMouseCallback(@Str BytePointer bytePointer, MouseCallback mouseCallback, Pointer pointer);

    @Namespace("cv")
    public static native void setOpenGlContext(@Str String str);

    @Namespace("cv")
    public static native void setOpenGlContext(@Str BytePointer bytePointer);

    @Namespace("cv")
    public static native void setOpenGlDrawCallback(@Str String str, OpenGlDrawCallback openGlDrawCallback);

    @Namespace("cv")
    public static native void setOpenGlDrawCallback(@Str String str, OpenGlDrawCallback openGlDrawCallback, Pointer pointer);

    @Namespace("cv")
    public static native void setOpenGlDrawCallback(@Str BytePointer bytePointer, OpenGlDrawCallback openGlDrawCallback);

    @Namespace("cv")
    public static native void setOpenGlDrawCallback(@Str BytePointer bytePointer, OpenGlDrawCallback openGlDrawCallback, Pointer pointer);

    @Namespace("cv")
    public static native void setTrackbarMax(@Str String str, @Str String str2, int i);

    @Namespace("cv")
    public static native void setTrackbarMax(@Str BytePointer bytePointer, @Str BytePointer bytePointer2, int i);

    @Namespace("cv")
    public static native void setTrackbarMin(@Str String str, @Str String str2, int i);

    @Namespace("cv")
    public static native void setTrackbarMin(@Str BytePointer bytePointer, @Str BytePointer bytePointer2, int i);

    @Namespace("cv")
    public static native void setTrackbarPos(@Str String str, @Str String str2, int i);

    @Namespace("cv")
    public static native void setTrackbarPos(@Str BytePointer bytePointer, @Str BytePointer bytePointer2, int i);

    @Namespace("cv")
    public static native void setWindowProperty(@Str String str, int i, double d);

    @Namespace("cv")
    public static native void setWindowProperty(@Str BytePointer bytePointer, int i, double d);

    @Namespace("cv")
    public static native void setWindowTitle(@Str String str, @Str String str2);

    @Namespace("cv")
    public static native void setWindowTitle(@Str BytePointer bytePointer, @Str BytePointer bytePointer2);

    @Namespace("cv")
    public static native int startLoop(Pt2Func_int_ByteBuffer pt2Func_int_ByteBuffer, int i, @ByPtrPtr @Cast({"char**"}) ByteBuffer byteBuffer);

    @Namespace("cv")
    public static native int startLoop(Pt2Func_int_BytePointer pt2Func_int_BytePointer, int i, @ByPtrPtr @Cast({"char**"}) BytePointer bytePointer);

    @Namespace("cv")
    public static native int startLoop(Pt2Func_int_PointerPointer pt2Func_int_PointerPointer, int i, @Cast({"char**"}) PointerPointer pointerPointer);

    @Namespace("cv")
    public static native int startLoop(Pt2Func_int_byte__ pt2Func_int_byte__, int i, @ByPtrPtr @Cast({"char**"}) byte[] bArr);

    @Namespace("cv")
    public static native int startWindowThread();

    @Namespace("cv")
    public static native void stopLoop();

    @Namespace("cv")
    public static native void updateWindow(@Str String str);

    @Namespace("cv")
    public static native void updateWindow(@Str BytePointer bytePointer);

    @Namespace("cv")
    public static native int waitKey();

    @Namespace("cv")
    public static native int waitKey(int i);

    static {
        Loader.load();
    }
}
