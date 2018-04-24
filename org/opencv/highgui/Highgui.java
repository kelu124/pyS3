package org.opencv.highgui;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;

public class Highgui {
    public static final int CV_CAP_ANDROID = 1000;
    public static final int CV_CAP_ANDROID_ANTIBANDING_50HZ = 0;
    public static final int CV_CAP_ANDROID_ANTIBANDING_60HZ = 1;
    public static final int CV_CAP_ANDROID_ANTIBANDING_AUTO = 2;
    public static final int CV_CAP_ANDROID_ANTIBANDING_OFF = 3;
    public static final int CV_CAP_ANDROID_BACK = 1099;
    public static final int CV_CAP_ANDROID_COLOR_FRAME = 0;
    public static final int CV_CAP_ANDROID_COLOR_FRAME_BGR = 0;
    public static final int CV_CAP_ANDROID_COLOR_FRAME_BGRA = 3;
    public static final int CV_CAP_ANDROID_COLOR_FRAME_RGB = 2;
    public static final int CV_CAP_ANDROID_COLOR_FRAME_RGBA = 4;
    public static final int CV_CAP_ANDROID_FLASH_MODE_AUTO = 0;
    public static final int CV_CAP_ANDROID_FLASH_MODE_OFF = 1;
    public static final int CV_CAP_ANDROID_FLASH_MODE_ON = 2;
    public static final int CV_CAP_ANDROID_FLASH_MODE_RED_EYE = 3;
    public static final int CV_CAP_ANDROID_FLASH_MODE_TORCH = 4;
    public static final int CV_CAP_ANDROID_FOCUS_MODE_AUTO = 0;
    public static final int CV_CAP_ANDROID_FOCUS_MODE_CONTINUOUS_VIDEO = 1;
    public static final int CV_CAP_ANDROID_FOCUS_MODE_EDOF = 2;
    public static final int CV_CAP_ANDROID_FOCUS_MODE_FIXED = 3;
    public static final int CV_CAP_ANDROID_FOCUS_MODE_INFINITY = 4;
    public static final int CV_CAP_ANDROID_FOCUS_MODE_MACRO = 5;
    public static final int CV_CAP_ANDROID_FRONT = 1098;
    public static final int CV_CAP_ANDROID_GREY_FRAME = 1;
    public static final int CV_CAP_ANDROID_WHITE_BALANCE_AUTO = 0;
    public static final int CV_CAP_ANDROID_WHITE_BALANCE_CLOUDY_DAYLIGHT = 1;
    public static final int CV_CAP_ANDROID_WHITE_BALANCE_DAYLIGHT = 2;
    public static final int CV_CAP_ANDROID_WHITE_BALANCE_FLUORESCENT = 3;
    public static final int CV_CAP_ANDROID_WHITE_BALANCE_INCANDESCENT = 4;
    public static final int CV_CAP_ANDROID_WHITE_BALANCE_SHADE = 5;
    public static final int CV_CAP_ANDROID_WHITE_BALANCE_TWILIGHT = 6;
    public static final int CV_CAP_ANDROID_WHITE_BALANCE_WARM_FLUORESCENT = 7;
    public static final int CV_CAP_AVFOUNDATION = 1200;
    public static final int CV_CAP_GIGANETIX = 1300;
    public static final int CV_CAP_MSMF = 1400;
    public static final int CV_CAP_PROP_ANDROID_ANTIBANDING = 8004;
    public static final int CV_CAP_PROP_ANDROID_FLASH_MODE = 8001;
    public static final int CV_CAP_PROP_ANDROID_FOCAL_LENGTH = 8005;
    public static final int CV_CAP_PROP_ANDROID_FOCUS_DISTANCE_FAR = 8008;
    public static final int CV_CAP_PROP_ANDROID_FOCUS_DISTANCE_NEAR = 8006;
    public static final int CV_CAP_PROP_ANDROID_FOCUS_DISTANCE_OPTIMAL = 8007;
    public static final int CV_CAP_PROP_ANDROID_FOCUS_MODE = 8002;
    public static final int CV_CAP_PROP_ANDROID_WHITE_BALANCE = 8003;
    public static final int CV_CAP_PROP_AUTOGRAB = 1024;
    public static final int CV_CAP_PROP_BACKLIGHT = 32;
    public static final int CV_CAP_PROP_FOCUS = 28;
    public static final int CV_CAP_PROP_FRAME_HEIGHT = 4;
    public static final int CV_CAP_PROP_FRAME_WIDTH = 3;
    public static final int CV_CAP_PROP_GIGA_FRAME_HEIGH_MAX = 10004;
    public static final int CV_CAP_PROP_GIGA_FRAME_OFFSET_X = 10001;
    public static final int CV_CAP_PROP_GIGA_FRAME_OFFSET_Y = 10002;
    public static final int CV_CAP_PROP_GIGA_FRAME_SENS_HEIGH = 10006;
    public static final int CV_CAP_PROP_GIGA_FRAME_SENS_WIDTH = 10005;
    public static final int CV_CAP_PROP_GIGA_FRAME_WIDTH_MAX = 10003;
    public static final int CV_CAP_PROP_GUID = 29;
    public static final int CV_CAP_PROP_INTELPERC_DEPTH_CONFIDENCE_THRESHOLD = 11005;
    public static final int CV_CAP_PROP_INTELPERC_DEPTH_FOCAL_LENGTH_HORZ = 11006;
    public static final int CV_CAP_PROP_INTELPERC_DEPTH_FOCAL_LENGTH_VERT = 11007;
    public static final int CV_CAP_PROP_INTELPERC_DEPTH_LOW_CONFIDENCE_VALUE = 11003;
    public static final int CV_CAP_PROP_INTELPERC_DEPTH_SATURATION_VALUE = 11004;
    public static final int CV_CAP_PROP_INTELPERC_PROFILE_COUNT = 11001;
    public static final int CV_CAP_PROP_INTELPERC_PROFILE_IDX = 11002;
    public static final int CV_CAP_PROP_IOS_DEVICE_EXPOSURE = 9002;
    public static final int CV_CAP_PROP_IOS_DEVICE_FLASH = 9003;
    public static final int CV_CAP_PROP_IOS_DEVICE_FOCUS = 9001;
    public static final int CV_CAP_PROP_IOS_DEVICE_TORCH = 9005;
    public static final int CV_CAP_PROP_IOS_DEVICE_WHITEBALANCE = 9004;
    public static final int CV_CAP_PROP_IRIS = 36;
    public static final int CV_CAP_PROP_ISO_SPEED = 30;
    public static final int CV_CAP_PROP_PAN = 33;
    public static final int CV_CAP_PROP_PREVIEW_FORMAT = 1026;
    public static final int CV_CAP_PROP_ROLL = 35;
    public static final int CV_CAP_PROP_SETTINGS = 37;
    public static final int CV_CAP_PROP_TILT = 34;
    public static final int CV_CAP_PROP_XI_AEAG = 415;
    public static final int CV_CAP_PROP_XI_AEAG_LEVEL = 419;
    public static final int CV_CAP_PROP_XI_AE_MAX_LIMIT = 417;
    public static final int CV_CAP_PROP_XI_AG_MAX_LIMIT = 418;
    public static final int CV_CAP_PROP_XI_AUTO_WB = 414;
    public static final int CV_CAP_PROP_XI_DATA_FORMAT = 401;
    public static final int CV_CAP_PROP_XI_DOWNSAMPLING = 400;
    public static final int CV_CAP_PROP_XI_EXP_PRIORITY = 416;
    public static final int CV_CAP_PROP_XI_GPI_LEVEL = 408;
    public static final int CV_CAP_PROP_XI_GPI_MODE = 407;
    public static final int CV_CAP_PROP_XI_GPI_SELECTOR = 406;
    public static final int CV_CAP_PROP_XI_GPO_MODE = 410;
    public static final int CV_CAP_PROP_XI_GPO_SELECTOR = 409;
    public static final int CV_CAP_PROP_XI_LED_MODE = 412;
    public static final int CV_CAP_PROP_XI_LED_SELECTOR = 411;
    public static final int CV_CAP_PROP_XI_MANUAL_WB = 413;
    public static final int CV_CAP_PROP_XI_OFFSET_X = 402;
    public static final int CV_CAP_PROP_XI_OFFSET_Y = 403;
    public static final int CV_CAP_PROP_XI_TIMEOUT = 420;
    public static final int CV_CAP_PROP_XI_TRG_SOFTWARE = 405;
    public static final int CV_CAP_PROP_XI_TRG_SOURCE = 404;
    public static final int CV_CAP_PROP_ZOOM = 27;
    public static final int CV_CAP_XIAPI = 1100;
    public static final int CV_CVTIMG_FLIP = 1;
    public static final int CV_CVTIMG_SWAP_RB = 2;
    public static final int CV_FONT_BLACK = 87;
    public static final int CV_FONT_BOLD = 75;
    public static final int CV_FONT_DEMIBOLD = 63;
    public static final int CV_FONT_LIGHT = 25;
    public static final int CV_FONT_NORMAL = 50;
    public static final int CV_IMWRITE_JPEG_QUALITY = 1;
    public static final int CV_IMWRITE_PNG_BILEVEL = 18;
    public static final int CV_IMWRITE_PNG_COMPRESSION = 16;
    public static final int CV_IMWRITE_PNG_STRATEGY = 17;
    public static final int CV_IMWRITE_PNG_STRATEGY_DEFAULT = 0;
    public static final int CV_IMWRITE_PNG_STRATEGY_FILTERED = 1;
    public static final int CV_IMWRITE_PNG_STRATEGY_FIXED = 4;
    public static final int CV_IMWRITE_PNG_STRATEGY_HUFFMAN_ONLY = 2;
    public static final int CV_IMWRITE_PNG_STRATEGY_RLE = 3;
    public static final int CV_IMWRITE_PXM_BINARY = 32;
    public static final int CV_LOAD_IMAGE_ANYCOLOR = 4;
    public static final int CV_LOAD_IMAGE_ANYDEPTH = 2;
    public static final int CV_LOAD_IMAGE_COLOR = 1;
    public static final int CV_LOAD_IMAGE_GRAYSCALE = 0;
    public static final int CV_LOAD_IMAGE_UNCHANGED = -1;
    public static final int CV_STYLE_ITALIC = 1;
    public static final int CV_STYLE_NORMAL = 0;
    public static final int CV_STYLE_OBLIQUE = 2;
    public static final int IMREAD_ANYCOLOR = 4;
    public static final int IMREAD_ANYDEPTH = 2;
    public static final int IMREAD_COLOR = 1;
    public static final int IMREAD_GRAYSCALE = 0;
    public static final int IMREAD_UNCHANGED = -1;
    public static final int IMWRITE_JPEG_QUALITY = 1;
    public static final int IMWRITE_PNG_BILEVEL = 18;
    public static final int IMWRITE_PNG_COMPRESSION = 16;
    public static final int IMWRITE_PNG_STRATEGY = 17;
    public static final int IMWRITE_PNG_STRATEGY_DEFAULT = 0;
    public static final int IMWRITE_PNG_STRATEGY_FILTERED = 1;
    public static final int IMWRITE_PNG_STRATEGY_FIXED = 4;
    public static final int IMWRITE_PNG_STRATEGY_HUFFMAN_ONLY = 2;
    public static final int IMWRITE_PNG_STRATEGY_RLE = 3;
    public static final int IMWRITE_PXM_BINARY = 32;

    private static native long imdecode_0(long j, int i);

    private static native boolean imencode_0(String str, long j, long j2, long j3);

    private static native boolean imencode_1(String str, long j, long j2);

    private static native long imread_0(String str, int i);

    private static native long imread_1(String str);

    private static native boolean imwrite_0(String str, long j, long j2);

    private static native boolean imwrite_1(String str, long j);

    public static Mat imdecode(Mat buf, int flags) {
        return new Mat(imdecode_0(buf.nativeObj, flags));
    }

    public static boolean imencode(String ext, Mat img, MatOfByte buf, MatOfInt params) {
        return imencode_0(ext, img.nativeObj, buf.nativeObj, params.nativeObj);
    }

    public static boolean imencode(String ext, Mat img, MatOfByte buf) {
        return imencode_1(ext, img.nativeObj, buf.nativeObj);
    }

    public static Mat imread(String filename, int flags) {
        return new Mat(imread_0(filename, flags));
    }

    public static Mat imread(String filename) {
        return new Mat(imread_1(filename));
    }

    public static boolean imwrite(String filename, Mat img, MatOfInt params) {
        return imwrite_0(filename, img.nativeObj, params.nativeObj);
    }

    public static boolean imwrite(String filename, Mat img) {
        return imwrite_1(filename, img.nativeObj);
    }
}
