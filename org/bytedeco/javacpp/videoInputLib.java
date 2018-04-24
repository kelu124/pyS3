package org.bytedeco.javacpp;

public class videoInputLib extends org.bytedeco.javacpp.presets.videoInputLib {
    public static final int VI_1394 = 4;
    public static final int VI_COMPOSITE = 0;
    public static final int VI_MAX_CAMERAS = 20;
    public static final int VI_MEDIASUBTYPE_AYUV = 14;
    public static final int VI_MEDIASUBTYPE_GREY = 17;
    public static final int VI_MEDIASUBTYPE_IYUV = 7;
    public static final int VI_MEDIASUBTYPE_MJPG = 18;
    public static final int VI_MEDIASUBTYPE_RGB24 = 0;
    public static final int VI_MEDIASUBTYPE_RGB32 = 1;
    public static final int VI_MEDIASUBTYPE_RGB555 = 2;
    public static final int VI_MEDIASUBTYPE_RGB565 = 3;
    public static final int VI_MEDIASUBTYPE_UYVY = 8;
    public static final int VI_MEDIASUBTYPE_Y211 = 13;
    public static final int VI_MEDIASUBTYPE_Y411 = 11;
    public static final int VI_MEDIASUBTYPE_Y41P = 12;
    public static final int VI_MEDIASUBTYPE_Y8 = 16;
    public static final int VI_MEDIASUBTYPE_Y800 = 15;
    public static final int VI_MEDIASUBTYPE_YUY2 = 4;
    public static final int VI_MEDIASUBTYPE_YUYV = 6;
    public static final int VI_MEDIASUBTYPE_YV12 = 9;
    public static final int VI_MEDIASUBTYPE_YVU9 = 10;
    public static final int VI_MEDIASUBTYPE_YVYU = 5;
    public static final int VI_NTSC_433 = 17;
    public static final int VI_NTSC_M = 0;
    public static final int VI_NTSC_M_J = 16;
    public static final int VI_NUM_FORMATS = 18;
    public static final int VI_NUM_TYPES = 19;
    public static final int VI_PAL_B = 1;
    public static final int VI_PAL_D = 2;
    public static final int VI_PAL_G = 3;
    public static final int VI_PAL_H = 4;
    public static final int VI_PAL_I = 5;
    public static final int VI_PAL_M = 6;
    public static final int VI_PAL_N = 7;
    public static final int VI_PAL_NC = 8;
    public static final int VI_SECAM_B = 9;
    public static final int VI_SECAM_D = 10;
    public static final int VI_SECAM_G = 11;
    public static final int VI_SECAM_H = 12;
    public static final int VI_SECAM_K = 13;
    public static final int VI_SECAM_K1 = 14;
    public static final int VI_SECAM_L = 15;
    public static final int VI_S_VIDEO = 1;
    public static final int VI_TUNER = 2;
    public static final int VI_USB = 3;
    public static final double VI_VERSION = 0.2d;

    public static native int comInitCount();

    public static native void comInitCount(int i);

    static {
        Loader.load();
    }
}
