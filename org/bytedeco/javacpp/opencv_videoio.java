package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.ByPtrPtr;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.MemberGetter;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoOffset;
import org.bytedeco.javacpp.annotation.Opaque;
import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_core.UMat;
import org.bytedeco.javacpp.presets.opencv_core.Str;

public class opencv_videoio extends org.bytedeco.javacpp.presets.opencv_videoio {
    public static final int CAP_ANDROID = 1000;
    public static final int CAP_ANY = 0;
    public static final int CAP_AVFOUNDATION = 1200;
    public static final int CAP_CMU1394 = 300;
    public static final int CAP_DC1394 = 300;
    public static final int CAP_DSHOW = 700;
    public static final int CAP_FFMPEG = 1900;
    public static final int CAP_FIREWARE = 300;
    public static final int CAP_FIREWIRE = 300;
    public static final int CAP_GIGANETIX = 1300;
    public static final int CAP_GPHOTO2 = 1700;
    public static final int CAP_GSTREAMER = 1800;
    public static final int CAP_IEEE1394 = 300;
    public static final int CAP_IMAGES = 2000;
    public static final int CAP_INTELPERC = 1500;
    public static final int CAP_INTELPERC_DEPTH_GENERATOR = 536870912;
    public static final int CAP_INTELPERC_DEPTH_MAP = 0;
    public static final int CAP_INTELPERC_GENERATORS_MASK = 805306368;
    public static final int CAP_INTELPERC_IMAGE = 3;
    public static final int CAP_INTELPERC_IMAGE_GENERATOR = 268435456;
    public static final int CAP_INTELPERC_IR_MAP = 2;
    public static final int CAP_INTELPERC_UVDEPTH_MAP = 1;
    public static final int CAP_MODE_BGR = 0;
    public static final int CAP_MODE_GRAY = 2;
    public static final int CAP_MODE_RGB = 1;
    public static final int CAP_MODE_YUYV = 3;
    public static final int CAP_MSMF = 1400;
    public static final int CAP_OPENNI = 900;
    public static final int CAP_OPENNI2 = 1600;
    public static final int CAP_OPENNI2_ASUS = 1610;
    public static final int CAP_OPENNI_ASUS = 910;
    public static final int CAP_OPENNI_BGR_IMAGE = 5;
    public static final int CAP_OPENNI_DEPTH_GENERATOR = Integer.MIN_VALUE;
    public static final int CAP_OPENNI_DEPTH_GENERATOR_BASELINE = -2147483546;
    public static final int CAP_OPENNI_DEPTH_GENERATOR_FOCAL_LENGTH = -2147483545;
    public static final int CAP_OPENNI_DEPTH_GENERATOR_REGISTRATION = -2147483544;
    public static final int CAP_OPENNI_DEPTH_GENERATOR_REGISTRATION_ON = -2147483544;
    public static final int CAP_OPENNI_DEPTH_MAP = 0;
    public static final int CAP_OPENNI_DISPARITY_MAP = 2;
    public static final int CAP_OPENNI_DISPARITY_MAP_32F = 3;
    public static final int CAP_OPENNI_GENERATORS_MASK = -1073741824;
    public static final int CAP_OPENNI_GRAY_IMAGE = 6;
    public static final int CAP_OPENNI_IMAGE_GENERATOR = 1073741824;
    public static final int CAP_OPENNI_IMAGE_GENERATOR_OUTPUT_MODE = 1073741924;
    public static final int CAP_OPENNI_IMAGE_GENERATOR_PRESENT = 1073741933;
    public static final int CAP_OPENNI_POINT_CLOUD_MAP = 1;
    public static final int CAP_OPENNI_QVGA_30HZ = 3;
    public static final int CAP_OPENNI_QVGA_60HZ = 4;
    public static final int CAP_OPENNI_SXGA_15HZ = 1;
    public static final int CAP_OPENNI_SXGA_30HZ = 2;
    public static final int CAP_OPENNI_VALID_DEPTH_MASK = 4;
    public static final int CAP_OPENNI_VGA_30HZ = 0;
    public static final int CAP_PROP_APERTURE = 17008;
    public static final int CAP_PROP_AUTOFOCUS = 39;
    public static final int CAP_PROP_AUTO_EXPOSURE = 21;
    public static final int CAP_PROP_BACKLIGHT = 32;
    public static final int CAP_PROP_BRIGHTNESS = 10;
    public static final int CAP_PROP_BUFFERSIZE = 38;
    public static final int CAP_PROP_CONTRAST = 11;
    public static final int CAP_PROP_CONVERT_RGB = 16;
    public static final int CAP_PROP_DC1394_MAX = 31;
    public static final int CAP_PROP_DC1394_MODE_AUTO = -2;
    public static final int CAP_PROP_DC1394_MODE_MANUAL = -3;
    public static final int CAP_PROP_DC1394_MODE_ONE_PUSH_AUTO = -1;
    public static final int CAP_PROP_DC1394_OFF = -4;
    public static final int CAP_PROP_EXPOSURE = 15;
    public static final int CAP_PROP_EXPOSUREPROGRAM = 17009;
    public static final int CAP_PROP_FOCUS = 28;
    public static final int CAP_PROP_FORMAT = 8;
    public static final int CAP_PROP_FOURCC = 6;
    public static final int CAP_PROP_FPS = 5;
    public static final int CAP_PROP_FRAME_COUNT = 7;
    public static final int CAP_PROP_FRAME_HEIGHT = 4;
    public static final int CAP_PROP_FRAME_WIDTH = 3;
    public static final int CAP_PROP_GAIN = 14;
    public static final int CAP_PROP_GAMMA = 22;
    public static final int CAP_PROP_GIGA_FRAME_HEIGH_MAX = 10004;
    public static final int CAP_PROP_GIGA_FRAME_OFFSET_X = 10001;
    public static final int CAP_PROP_GIGA_FRAME_OFFSET_Y = 10002;
    public static final int CAP_PROP_GIGA_FRAME_SENS_HEIGH = 10006;
    public static final int CAP_PROP_GIGA_FRAME_SENS_WIDTH = 10005;
    public static final int CAP_PROP_GIGA_FRAME_WIDTH_MAX = 10003;
    public static final int CAP_PROP_GPHOTO2_COLLECT_MSGS = 17005;
    public static final int CAP_PROP_GPHOTO2_FLUSH_MSGS = 17006;
    public static final int CAP_PROP_GPHOTO2_PREVIEW = 17001;
    public static final int CAP_PROP_GPHOTO2_RELOAD_CONFIG = 17003;
    public static final int CAP_PROP_GPHOTO2_RELOAD_ON_CHANGE = 17004;
    public static final int CAP_PROP_GPHOTO2_WIDGET_ENUMERATE = 17002;
    public static final int CAP_PROP_GSTREAMER_QUEUE_LENGTH = 200;
    public static final int CAP_PROP_GUID = 29;
    public static final int CAP_PROP_HUE = 13;
    public static final int CAP_PROP_INTELPERC_DEPTH_CONFIDENCE_THRESHOLD = 11005;
    public static final int CAP_PROP_INTELPERC_DEPTH_FOCAL_LENGTH_HORZ = 11006;
    public static final int CAP_PROP_INTELPERC_DEPTH_FOCAL_LENGTH_VERT = 11007;
    public static final int CAP_PROP_INTELPERC_DEPTH_LOW_CONFIDENCE_VALUE = 11003;
    public static final int CAP_PROP_INTELPERC_DEPTH_SATURATION_VALUE = 11004;
    public static final int CAP_PROP_INTELPERC_PROFILE_COUNT = 11001;
    public static final int CAP_PROP_INTELPERC_PROFILE_IDX = 11002;
    public static final int CAP_PROP_IOS_DEVICE_EXPOSURE = 9002;
    public static final int CAP_PROP_IOS_DEVICE_FLASH = 9003;
    public static final int CAP_PROP_IOS_DEVICE_FOCUS = 9001;
    public static final int CAP_PROP_IOS_DEVICE_TORCH = 9005;
    public static final int CAP_PROP_IOS_DEVICE_WHITEBALANCE = 9004;
    public static final int CAP_PROP_IRIS = 36;
    public static final int CAP_PROP_ISO_SPEED = 30;
    public static final int CAP_PROP_MODE = 9;
    public static final int CAP_PROP_MONOCHROME = 19;
    public static final int CAP_PROP_OPENNI2_MIRROR = 111;
    public static final int CAP_PROP_OPENNI2_SYNC = 110;
    public static final int CAP_PROP_OPENNI_APPROX_FRAME_SYNC = 105;
    public static final int CAP_PROP_OPENNI_BASELINE = 102;
    public static final int CAP_PROP_OPENNI_CIRCLE_BUFFER = 107;
    public static final int CAP_PROP_OPENNI_FOCAL_LENGTH = 103;
    public static final int CAP_PROP_OPENNI_FRAME_MAX_DEPTH = 101;
    public static final int CAP_PROP_OPENNI_GENERATOR_PRESENT = 109;
    public static final int CAP_PROP_OPENNI_MAX_BUFFER_SIZE = 106;
    public static final int CAP_PROP_OPENNI_MAX_TIME_DURATION = 108;
    public static final int CAP_PROP_OPENNI_OUTPUT_MODE = 100;
    public static final int CAP_PROP_OPENNI_REGISTRATION = 104;
    public static final int CAP_PROP_OPENNI_REGISTRATION_ON = 104;
    public static final int CAP_PROP_PAN = 33;
    public static final int CAP_PROP_POS_AVI_RATIO = 2;
    public static final int CAP_PROP_POS_FRAMES = 1;
    public static final int CAP_PROP_POS_MSEC = 0;
    public static final int CAP_PROP_PVAPI_BINNINGX = 304;
    public static final int CAP_PROP_PVAPI_BINNINGY = 305;
    public static final int CAP_PROP_PVAPI_DECIMATIONHORIZONTAL = 302;
    public static final int CAP_PROP_PVAPI_DECIMATIONVERTICAL = 303;
    public static final int CAP_PROP_PVAPI_FRAMESTARTTRIGGERMODE = 301;
    public static final int CAP_PROP_PVAPI_MULTICASTIP = 300;
    public static final int CAP_PROP_PVAPI_PIXELFORMAT = 306;
    public static final int CAP_PROP_RECTIFICATION = 18;
    public static final int CAP_PROP_ROLL = 35;
    public static final int CAP_PROP_SATURATION = 12;
    public static final int CAP_PROP_SETTINGS = 37;
    public static final int CAP_PROP_SHARPNESS = 20;
    public static final int CAP_PROP_SPEED = 17007;
    public static final int CAP_PROP_TEMPERATURE = 23;
    public static final int CAP_PROP_TILT = 34;
    public static final int CAP_PROP_TRIGGER = 24;
    public static final int CAP_PROP_TRIGGER_DELAY = 25;
    public static final int CAP_PROP_VIEWFINDER = 17010;
    public static final int CAP_PROP_WHITE_BALANCE_BLUE_U = 17;
    public static final int CAP_PROP_WHITE_BALANCE_RED_V = 26;
    public static final int CAP_PROP_XI_AEAG = 415;
    public static final int CAP_PROP_XI_AEAG_LEVEL = 419;
    public static final int CAP_PROP_XI_AE_MAX_LIMIT = 417;
    public static final int CAP_PROP_XI_AG_MAX_LIMIT = 418;
    public static final int CAP_PROP_XI_AUTO_WB = 414;
    public static final int CAP_PROP_XI_DATA_FORMAT = 401;
    public static final int CAP_PROP_XI_DOWNSAMPLING = 400;
    public static final int CAP_PROP_XI_EXP_PRIORITY = 416;
    public static final int CAP_PROP_XI_GPI_LEVEL = 408;
    public static final int CAP_PROP_XI_GPI_MODE = 407;
    public static final int CAP_PROP_XI_GPI_SELECTOR = 406;
    public static final int CAP_PROP_XI_GPO_MODE = 410;
    public static final int CAP_PROP_XI_GPO_SELECTOR = 409;
    public static final int CAP_PROP_XI_LED_MODE = 412;
    public static final int CAP_PROP_XI_LED_SELECTOR = 411;
    public static final int CAP_PROP_XI_MANUAL_WB = 413;
    public static final int CAP_PROP_XI_OFFSET_X = 402;
    public static final int CAP_PROP_XI_OFFSET_Y = 403;
    public static final int CAP_PROP_XI_TIMEOUT = 420;
    public static final int CAP_PROP_XI_TRG_SOFTWARE = 405;
    public static final int CAP_PROP_XI_TRG_SOURCE = 404;
    public static final int CAP_PROP_ZOOM = 27;
    public static final int CAP_PVAPI = 800;
    public static final int CAP_PVAPI_DECIMATION_2OUTOF16 = 8;
    public static final int CAP_PVAPI_DECIMATION_2OUTOF4 = 2;
    public static final int CAP_PVAPI_DECIMATION_2OUTOF8 = 4;
    public static final int CAP_PVAPI_DECIMATION_OFF = 1;
    public static final int CAP_PVAPI_FSTRIGMODE_FIXEDRATE = 3;
    public static final int CAP_PVAPI_FSTRIGMODE_FREERUN = 0;
    public static final int CAP_PVAPI_FSTRIGMODE_SOFTWARE = 4;
    public static final int CAP_PVAPI_FSTRIGMODE_SYNCIN1 = 1;
    public static final int CAP_PVAPI_FSTRIGMODE_SYNCIN2 = 2;
    public static final int CAP_PVAPI_PIXELFORMAT_BAYER16 = 4;
    public static final int CAP_PVAPI_PIXELFORMAT_BAYER8 = 3;
    public static final int CAP_PVAPI_PIXELFORMAT_BGR24 = 6;
    public static final int CAP_PVAPI_PIXELFORMAT_BGRA32 = 8;
    public static final int CAP_PVAPI_PIXELFORMAT_MONO16 = 2;
    public static final int CAP_PVAPI_PIXELFORMAT_MONO8 = 1;
    public static final int CAP_PVAPI_PIXELFORMAT_RGB24 = 5;
    public static final int CAP_PVAPI_PIXELFORMAT_RGBA32 = 7;
    public static final int CAP_QT = 500;
    public static final int CAP_UNICAP = 600;
    public static final int CAP_V4L = 200;
    public static final int CAP_V4L2 = 200;
    public static final int CAP_VFW = 200;
    public static final int CAP_WINRT = 1410;
    public static final int CAP_XIAPI = 1100;
    public static final int CV_CAP_ANDROID = 1000;
    public static final int CV_CAP_ANDROID_BACK = 1099;
    public static final int CV_CAP_ANDROID_FRONT = 1098;
    public static final int CV_CAP_ANY = 0;
    public static final int CV_CAP_AVFOUNDATION = 1200;
    public static final int CV_CAP_CMU1394 = 300;
    public static final int CV_CAP_DC1394 = 300;
    public static final int CV_CAP_DSHOW = 700;
    public static final int CV_CAP_FFMPEG = 1900;
    public static final int CV_CAP_FIREWARE = 300;
    public static final int CV_CAP_FIREWIRE = 300;
    public static final int CV_CAP_GIGANETIX = 1300;
    public static final int CV_CAP_GPHOTO2 = 1700;
    public static final int CV_CAP_GSTREAMER = 1800;
    public static final int CV_CAP_GSTREAMER_QUEUE_LENGTH = 200;
    public static final int CV_CAP_IEEE1394 = 300;
    public static final int CV_CAP_IMAGES = 2000;
    public static final int CV_CAP_INTELPERC = 1500;
    public static final int CV_CAP_INTELPERC_DEPTH_GENERATOR = 536870912;
    public static final int CV_CAP_INTELPERC_DEPTH_MAP = 0;
    public static final int CV_CAP_INTELPERC_GENERATORS_MASK = 805306368;
    public static final int CV_CAP_INTELPERC_IMAGE = 3;
    public static final int CV_CAP_INTELPERC_IMAGE_GENERATOR = 268435456;
    public static final int CV_CAP_INTELPERC_IR_MAP = 2;
    public static final int CV_CAP_INTELPERC_UVDEPTH_MAP = 1;
    public static final int CV_CAP_MIL = 100;
    public static final int CV_CAP_MODE_BGR = 0;
    public static final int CV_CAP_MODE_GRAY = 2;
    public static final int CV_CAP_MODE_RGB = 1;
    public static final int CV_CAP_MODE_YUYV = 3;
    public static final int CV_CAP_MSMF = 1400;
    public static final int CV_CAP_OPENNI = 900;
    public static final int CV_CAP_OPENNI2 = 1600;
    public static final int CV_CAP_OPENNI_ASUS = 910;
    public static final int CV_CAP_OPENNI_BGR_IMAGE = 5;
    public static final int CV_CAP_OPENNI_DEPTH_GENERATOR = Integer.MIN_VALUE;
    public static final int CV_CAP_OPENNI_DEPTH_GENERATOR_BASELINE = -2147483546;
    public static final int CV_CAP_OPENNI_DEPTH_GENERATOR_FOCAL_LENGTH = -2147483545;
    public static final int CV_CAP_OPENNI_DEPTH_GENERATOR_REGISTRATION = -2147483544;
    public static final int CV_CAP_OPENNI_DEPTH_GENERATOR_REGISTRATION_ON = -2147483544;
    public static final int CV_CAP_OPENNI_DEPTH_MAP = 0;
    public static final int CV_CAP_OPENNI_DISPARITY_MAP = 2;
    public static final int CV_CAP_OPENNI_DISPARITY_MAP_32F = 3;
    public static final int CV_CAP_OPENNI_GENERATORS_MASK = -1073741824;
    public static final int CV_CAP_OPENNI_GRAY_IMAGE = 6;
    public static final int CV_CAP_OPENNI_IMAGE_GENERATOR = 1073741824;
    public static final int CV_CAP_OPENNI_IMAGE_GENERATOR_OUTPUT_MODE = 1073741924;
    public static final int CV_CAP_OPENNI_IMAGE_GENERATOR_PRESENT = 1073741933;
    public static final int CV_CAP_OPENNI_POINT_CLOUD_MAP = 1;
    public static final int CV_CAP_OPENNI_QVGA_30HZ = 3;
    public static final int CV_CAP_OPENNI_QVGA_60HZ = 4;
    public static final int CV_CAP_OPENNI_SXGA_15HZ = 1;
    public static final int CV_CAP_OPENNI_SXGA_30HZ = 2;
    public static final int CV_CAP_OPENNI_VALID_DEPTH_MASK = 4;
    public static final int CV_CAP_OPENNI_VGA_30HZ = 0;
    public static final int CV_CAP_PROP_ANDROID_ANTIBANDING = 8004;
    public static final int CV_CAP_PROP_ANDROID_EXPOSE_LOCK = 8009;
    public static final int CV_CAP_PROP_ANDROID_FLASH_MODE = 8001;
    public static final int CV_CAP_PROP_ANDROID_FOCAL_LENGTH = 8005;
    public static final int CV_CAP_PROP_ANDROID_FOCUS_DISTANCE_FAR = 8008;
    public static final int CV_CAP_PROP_ANDROID_FOCUS_DISTANCE_NEAR = 8006;
    public static final int CV_CAP_PROP_ANDROID_FOCUS_DISTANCE_OPTIMAL = 8007;
    public static final int CV_CAP_PROP_ANDROID_FOCUS_MODE = 8002;
    public static final int CV_CAP_PROP_ANDROID_WHITEBALANCE_LOCK = 8010;
    public static final int CV_CAP_PROP_ANDROID_WHITE_BALANCE = 8003;
    public static final int CV_CAP_PROP_APERTURE = 17008;
    public static final int CV_CAP_PROP_AUTOFOCUS = 39;
    public static final int CV_CAP_PROP_AUTOGRAB = 1024;
    public static final int CV_CAP_PROP_AUTO_EXPOSURE = 21;
    public static final int CV_CAP_PROP_BACKLIGHT = 32;
    public static final int CV_CAP_PROP_BRIGHTNESS = 10;
    public static final int CV_CAP_PROP_BUFFERSIZE = 38;
    public static final int CV_CAP_PROP_CONTRAST = 11;
    public static final int CV_CAP_PROP_CONVERT_RGB = 16;
    public static final int CV_CAP_PROP_DC1394_MODE_AUTO = -2;
    public static final int CV_CAP_PROP_DC1394_MODE_MANUAL = -3;
    public static final int CV_CAP_PROP_DC1394_MODE_ONE_PUSH_AUTO = -1;
    public static final int CV_CAP_PROP_DC1394_OFF = -4;
    public static final int CV_CAP_PROP_EXPOSURE = 15;
    public static final int CV_CAP_PROP_EXPOSUREPROGRAM = 17009;
    public static final int CV_CAP_PROP_FOCUS = 28;
    public static final int CV_CAP_PROP_FORMAT = 8;
    public static final int CV_CAP_PROP_FOURCC = 6;
    public static final int CV_CAP_PROP_FPS = 5;
    public static final int CV_CAP_PROP_FRAME_COUNT = 7;
    public static final int CV_CAP_PROP_FRAME_HEIGHT = 4;
    public static final int CV_CAP_PROP_FRAME_WIDTH = 3;
    public static final int CV_CAP_PROP_GAIN = 14;
    public static final int CV_CAP_PROP_GAMMA = 22;
    public static final int CV_CAP_PROP_GIGA_FRAME_HEIGH_MAX = 10004;
    public static final int CV_CAP_PROP_GIGA_FRAME_OFFSET_X = 10001;
    public static final int CV_CAP_PROP_GIGA_FRAME_OFFSET_Y = 10002;
    public static final int CV_CAP_PROP_GIGA_FRAME_SENS_HEIGH = 10006;
    public static final int CV_CAP_PROP_GIGA_FRAME_SENS_WIDTH = 10005;
    public static final int CV_CAP_PROP_GIGA_FRAME_WIDTH_MAX = 10003;
    public static final int CV_CAP_PROP_GPHOTO2_COLLECT_MSGS = 17005;
    public static final int CV_CAP_PROP_GPHOTO2_FLUSH_MSGS = 17006;
    public static final int CV_CAP_PROP_GPHOTO2_PREVIEW = 17001;
    public static final int CV_CAP_PROP_GPHOTO2_RELOAD_CONFIG = 17003;
    public static final int CV_CAP_PROP_GPHOTO2_RELOAD_ON_CHANGE = 17004;
    public static final int CV_CAP_PROP_GPHOTO2_WIDGET_ENUMERATE = 17002;
    public static final int CV_CAP_PROP_GUID = 29;
    public static final int CV_CAP_PROP_HUE = 13;
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
    public static final int CV_CAP_PROP_MAX_DC1394 = 31;
    public static final int CV_CAP_PROP_MODE = 9;
    public static final int CV_CAP_PROP_MONOCHROME = 19;
    public static final int CV_CAP_PROP_OPENNI2_MIRROR = 111;
    public static final int CV_CAP_PROP_OPENNI2_SYNC = 110;
    public static final int CV_CAP_PROP_OPENNI_APPROX_FRAME_SYNC = 105;
    public static final int CV_CAP_PROP_OPENNI_BASELINE = 102;
    public static final int CV_CAP_PROP_OPENNI_CIRCLE_BUFFER = 107;
    public static final int CV_CAP_PROP_OPENNI_FOCAL_LENGTH = 103;
    public static final int CV_CAP_PROP_OPENNI_FRAME_MAX_DEPTH = 101;
    public static final int CV_CAP_PROP_OPENNI_GENERATOR_PRESENT = 109;
    public static final int CV_CAP_PROP_OPENNI_MAX_BUFFER_SIZE = 106;
    public static final int CV_CAP_PROP_OPENNI_MAX_TIME_DURATION = 108;
    public static final int CV_CAP_PROP_OPENNI_OUTPUT_MODE = 100;
    public static final int CV_CAP_PROP_OPENNI_REGISTRATION = 104;
    public static final int CV_CAP_PROP_OPENNI_REGISTRATION_ON = 104;
    public static final int CV_CAP_PROP_PAN = 33;
    public static final int CV_CAP_PROP_POS_AVI_RATIO = 2;
    public static final int CV_CAP_PROP_POS_FRAMES = 1;
    public static final int CV_CAP_PROP_POS_MSEC = 0;
    public static final int CV_CAP_PROP_PREVIEW_FORMAT = 1026;
    public static final int CV_CAP_PROP_PVAPI_BINNINGX = 304;
    public static final int CV_CAP_PROP_PVAPI_BINNINGY = 305;
    public static final int CV_CAP_PROP_PVAPI_DECIMATIONHORIZONTAL = 302;
    public static final int CV_CAP_PROP_PVAPI_DECIMATIONVERTICAL = 303;
    public static final int CV_CAP_PROP_PVAPI_FRAMESTARTTRIGGERMODE = 301;
    public static final int CV_CAP_PROP_PVAPI_MULTICASTIP = 300;
    public static final int CV_CAP_PROP_PVAPI_PIXELFORMAT = 306;
    public static final int CV_CAP_PROP_RECTIFICATION = 18;
    public static final int CV_CAP_PROP_ROLL = 35;
    public static final int CV_CAP_PROP_SAR_DEN = 41;
    public static final int CV_CAP_PROP_SAR_NUM = 40;
    public static final int CV_CAP_PROP_SATURATION = 12;
    public static final int CV_CAP_PROP_SETTINGS = 37;
    public static final int CV_CAP_PROP_SHARPNESS = 20;
    public static final int CV_CAP_PROP_SPEED = 17007;
    public static final int CV_CAP_PROP_SUPPORTED_PREVIEW_SIZES_STRING = 1025;
    public static final int CV_CAP_PROP_TEMPERATURE = 23;
    public static final int CV_CAP_PROP_TILT = 34;
    public static final int CV_CAP_PROP_TRIGGER = 24;
    public static final int CV_CAP_PROP_TRIGGER_DELAY = 25;
    public static final int CV_CAP_PROP_VIEWFINDER = 17010;
    public static final int CV_CAP_PROP_WHITE_BALANCE_BLUE_U = 17;
    public static final int CV_CAP_PROP_WHITE_BALANCE_RED_V = 26;
    public static final int CV_CAP_PROP_XI_ACQ_BUFFER_SIZE = 548;
    public static final int CV_CAP_PROP_XI_ACQ_BUFFER_SIZE_UNIT = 549;
    public static final int CV_CAP_PROP_XI_ACQ_FRAME_BURST_COUNT = 499;
    public static final int CV_CAP_PROP_XI_ACQ_TIMING_MODE = 538;
    public static final int CV_CAP_PROP_XI_ACQ_TRANSPORT_BUFFER_COMMIT = 552;
    public static final int CV_CAP_PROP_XI_ACQ_TRANSPORT_BUFFER_SIZE = 550;
    public static final int CV_CAP_PROP_XI_AEAG = 415;
    public static final int CV_CAP_PROP_XI_AEAG_LEVEL = 419;
    public static final int CV_CAP_PROP_XI_AEAG_ROI_HEIGHT = 442;
    public static final int CV_CAP_PROP_XI_AEAG_ROI_OFFSET_X = 439;
    public static final int CV_CAP_PROP_XI_AEAG_ROI_OFFSET_Y = 440;
    public static final int CV_CAP_PROP_XI_AEAG_ROI_WIDTH = 441;
    public static final int CV_CAP_PROP_XI_AE_MAX_LIMIT = 417;
    public static final int CV_CAP_PROP_XI_AG_MAX_LIMIT = 418;
    public static final int CV_CAP_PROP_XI_APPLY_CMS = 471;
    public static final int CV_CAP_PROP_XI_AUTO_BANDWIDTH_CALCULATION = 573;
    public static final int CV_CAP_PROP_XI_AUTO_WB = 414;
    public static final int CV_CAP_PROP_XI_AVAILABLE_BANDWIDTH = 539;
    public static final int CV_CAP_PROP_XI_BINNING_HORIZONTAL = 429;
    public static final int CV_CAP_PROP_XI_BINNING_PATTERN = 430;
    public static final int CV_CAP_PROP_XI_BINNING_SELECTOR = 427;
    public static final int CV_CAP_PROP_XI_BINNING_VERTICAL = 428;
    public static final int CV_CAP_PROP_XI_BPC = 445;
    public static final int CV_CAP_PROP_XI_BUFFERS_QUEUE_SIZE = 551;
    public static final int CV_CAP_PROP_XI_BUFFER_POLICY = 540;
    public static final int CV_CAP_PROP_XI_CC_MATRIX_00 = 479;
    public static final int CV_CAP_PROP_XI_CC_MATRIX_01 = 480;
    public static final int CV_CAP_PROP_XI_CC_MATRIX_02 = 481;
    public static final int CV_CAP_PROP_XI_CC_MATRIX_03 = 482;
    public static final int CV_CAP_PROP_XI_CC_MATRIX_10 = 483;
    public static final int CV_CAP_PROP_XI_CC_MATRIX_11 = 484;
    public static final int CV_CAP_PROP_XI_CC_MATRIX_12 = 485;
    public static final int CV_CAP_PROP_XI_CC_MATRIX_13 = 486;
    public static final int CV_CAP_PROP_XI_CC_MATRIX_20 = 487;
    public static final int CV_CAP_PROP_XI_CC_MATRIX_21 = 488;
    public static final int CV_CAP_PROP_XI_CC_MATRIX_22 = 489;
    public static final int CV_CAP_PROP_XI_CC_MATRIX_23 = 490;
    public static final int CV_CAP_PROP_XI_CC_MATRIX_30 = 491;
    public static final int CV_CAP_PROP_XI_CC_MATRIX_31 = 492;
    public static final int CV_CAP_PROP_XI_CC_MATRIX_32 = 493;
    public static final int CV_CAP_PROP_XI_CC_MATRIX_33 = 494;
    public static final int CV_CAP_PROP_XI_CHIP_TEMP = 468;
    public static final int CV_CAP_PROP_XI_CMS = 470;
    public static final int CV_CAP_PROP_XI_COLOR_FILTER_ARRAY = 475;
    public static final int CV_CAP_PROP_XI_COLUMN_FPN_CORRECTION = 555;
    public static final int CV_CAP_PROP_XI_COOLING = 466;
    public static final int CV_CAP_PROP_XI_COUNTER_SELECTOR = 536;
    public static final int CV_CAP_PROP_XI_COUNTER_VALUE = 537;
    public static final int CV_CAP_PROP_XI_DATA_FORMAT = 401;
    public static final int CV_CAP_PROP_XI_DEBOUNCE_EN = 507;
    public static final int CV_CAP_PROP_XI_DEBOUNCE_POL = 510;
    public static final int CV_CAP_PROP_XI_DEBOUNCE_T0 = 508;
    public static final int CV_CAP_PROP_XI_DEBOUNCE_T1 = 509;
    public static final int CV_CAP_PROP_XI_DEBUG_LEVEL = 572;
    public static final int CV_CAP_PROP_XI_DECIMATION_HORIZONTAL = 433;
    public static final int CV_CAP_PROP_XI_DECIMATION_PATTERN = 434;
    public static final int CV_CAP_PROP_XI_DECIMATION_SELECTOR = 431;
    public static final int CV_CAP_PROP_XI_DECIMATION_VERTICAL = 432;
    public static final int CV_CAP_PROP_XI_DEFAULT_CC_MATRIX = 495;
    public static final int CV_CAP_PROP_XI_DEVICE_MODEL_ID = 521;
    public static final int CV_CAP_PROP_XI_DEVICE_RESET = 554;
    public static final int CV_CAP_PROP_XI_DEVICE_SN = 522;
    public static final int CV_CAP_PROP_XI_DOWNSAMPLING = 400;
    public static final int CV_CAP_PROP_XI_DOWNSAMPLING_TYPE = 426;
    public static final int CV_CAP_PROP_XI_EXPOSURE = 421;
    public static final int CV_CAP_PROP_XI_EXPOSURE_BURST_COUNT = 422;
    public static final int CV_CAP_PROP_XI_EXP_PRIORITY = 416;
    public static final int CV_CAP_PROP_XI_FFS_ACCESS_KEY = 583;
    public static final int CV_CAP_PROP_XI_FRAMERATE = 535;
    public static final int CV_CAP_PROP_XI_FREE_FFS_SIZE = 581;
    public static final int CV_CAP_PROP_XI_GAIN = 424;
    public static final int CV_CAP_PROP_XI_GAIN_SELECTOR = 423;
    public static final int CV_CAP_PROP_XI_GAMMAC = 477;
    public static final int CV_CAP_PROP_XI_GAMMAY = 476;
    public static final int CV_CAP_PROP_XI_GPI_LEVEL = 408;
    public static final int CV_CAP_PROP_XI_GPI_MODE = 407;
    public static final int CV_CAP_PROP_XI_GPI_SELECTOR = 406;
    public static final int CV_CAP_PROP_XI_GPO_MODE = 410;
    public static final int CV_CAP_PROP_XI_GPO_SELECTOR = 409;
    public static final int CV_CAP_PROP_XI_HDR = 559;
    public static final int CV_CAP_PROP_XI_HDR_KNEEPOINT_COUNT = 560;
    public static final int CV_CAP_PROP_XI_HDR_T1 = 561;
    public static final int CV_CAP_PROP_XI_HDR_T2 = 562;
    public static final int CV_CAP_PROP_XI_HEIGHT = 452;
    public static final int CV_CAP_PROP_XI_HOUS_TEMP = 469;
    public static final int CV_CAP_PROP_XI_HW_REVISION = 571;
    public static final int CV_CAP_PROP_XI_IMAGE_BLACK_LEVEL = 565;
    public static final int CV_CAP_PROP_XI_IMAGE_DATA_BIT_DEPTH = 462;
    public static final int CV_CAP_PROP_XI_IMAGE_DATA_FORMAT = 435;
    public static final int CV_CAP_PROP_XI_IMAGE_DATA_FORMAT_RGB32_ALPHA = 529;
    public static final int CV_CAP_PROP_XI_IMAGE_IS_COLOR = 474;
    public static final int CV_CAP_PROP_XI_IMAGE_PAYLOAD_SIZE = 530;
    public static final int CV_CAP_PROP_XI_IS_COOLED = 465;
    public static final int CV_CAP_PROP_XI_IS_DEVICE_EXIST = 547;
    public static final int CV_CAP_PROP_XI_KNEEPOINT1 = 563;
    public static final int CV_CAP_PROP_XI_KNEEPOINT2 = 564;
    public static final int CV_CAP_PROP_XI_LED_MODE = 412;
    public static final int CV_CAP_PROP_XI_LED_SELECTOR = 411;
    public static final int CV_CAP_PROP_XI_LENS_APERTURE_VALUE = 512;
    public static final int CV_CAP_PROP_XI_LENS_FEATURE = 518;
    public static final int CV_CAP_PROP_XI_LENS_FEATURE_SELECTOR = 517;
    public static final int CV_CAP_PROP_XI_LENS_FOCAL_LENGTH = 516;
    public static final int CV_CAP_PROP_XI_LENS_FOCUS_DISTANCE = 515;
    public static final int CV_CAP_PROP_XI_LENS_FOCUS_MOVE = 514;
    public static final int CV_CAP_PROP_XI_LENS_FOCUS_MOVEMENT_VALUE = 513;
    public static final int CV_CAP_PROP_XI_LENS_MODE = 511;
    public static final int CV_CAP_PROP_XI_LIMIT_BANDWIDTH = 459;
    public static final int CV_CAP_PROP_XI_LUT_EN = 541;
    public static final int CV_CAP_PROP_XI_LUT_INDEX = 542;
    public static final int CV_CAP_PROP_XI_LUT_VALUE = 543;
    public static final int CV_CAP_PROP_XI_MANUAL_WB = 413;
    public static final int CV_CAP_PROP_XI_OFFSET_X = 402;
    public static final int CV_CAP_PROP_XI_OFFSET_Y = 403;
    public static final int CV_CAP_PROP_XI_OUTPUT_DATA_BIT_DEPTH = 461;
    public static final int CV_CAP_PROP_XI_OUTPUT_DATA_PACKING = 463;
    public static final int CV_CAP_PROP_XI_OUTPUT_DATA_PACKING_TYPE = 464;
    public static final int CV_CAP_PROP_XI_RECENT_FRAME = 553;
    public static final int CV_CAP_PROP_XI_SENSOR_CLOCK_FREQ_HZ = 532;
    public static final int CV_CAP_PROP_XI_SENSOR_CLOCK_FREQ_INDEX = 533;
    public static final int CV_CAP_PROP_XI_SENSOR_DATA_BIT_DEPTH = 460;
    public static final int CV_CAP_PROP_XI_SENSOR_FEATURE_SELECTOR = 585;
    public static final int CV_CAP_PROP_XI_SENSOR_FEATURE_VALUE = 586;
    public static final int CV_CAP_PROP_XI_SENSOR_MODE = 558;
    public static final int CV_CAP_PROP_XI_SENSOR_OUTPUT_CHANNEL_COUNT = 534;
    public static final int CV_CAP_PROP_XI_SENSOR_TAPS = 437;
    public static final int CV_CAP_PROP_XI_SHARPNESS = 478;
    public static final int CV_CAP_PROP_XI_SHUTTER_TYPE = 436;
    public static final int CV_CAP_PROP_XI_TARGET_TEMP = 467;
    public static final int CV_CAP_PROP_XI_TIMEOUT = 420;
    public static final int CV_CAP_PROP_XI_TRANSPORT_PIXEL_FORMAT = 531;
    public static final int CV_CAP_PROP_XI_TRG_DELAY = 544;
    public static final int CV_CAP_PROP_XI_TRG_SELECTOR = 498;
    public static final int CV_CAP_PROP_XI_TRG_SOFTWARE = 405;
    public static final int CV_CAP_PROP_XI_TRG_SOURCE = 404;
    public static final int CV_CAP_PROP_XI_TS_RST_MODE = 545;
    public static final int CV_CAP_PROP_XI_TS_RST_SOURCE = 546;
    public static final int CV_CAP_PROP_XI_USED_FFS_SIZE = 582;
    public static final int CV_CAP_PROP_XI_WB_KB = 450;
    public static final int CV_CAP_PROP_XI_WB_KG = 449;
    public static final int CV_CAP_PROP_XI_WB_KR = 448;
    public static final int CV_CAP_PROP_XI_WIDTH = 451;
    public static final int CV_CAP_PROP_ZOOM = 27;
    public static final int CV_CAP_PVAPI = 800;
    public static final int CV_CAP_QT = 500;
    public static final int CV_CAP_STEREO = 400;
    public static final int CV_CAP_TYZX = 400;
    public static final int CV_CAP_UNICAP = 600;
    public static final int CV_CAP_V4L = 200;
    public static final int CV_CAP_V4L2 = 200;
    public static final int CV_CAP_VFW = 200;
    public static final int CV_CAP_XIAPI = 1100;
    public static final int CV_FOURCC_DEFAULT = CV_FOURCC_DEFAULT();
    public static final int CV_FOURCC_PROMPT = -1;
    public static final int CV_TYZX_COLOR = 402;
    public static final int CV_TYZX_LEFT = 400;
    public static final int CV_TYZX_RIGHT = 401;
    public static final int CV_TYZX_Z = 403;
    public static final int VIDEOWRITER_PROP_FRAMEBYTES = 2;
    public static final int VIDEOWRITER_PROP_NSTRIPES = 3;
    public static final int VIDEOWRITER_PROP_QUALITY = 1;

    @Opaque
    public static class CvCapture extends Pointer {
        public CvCapture() {
            super((Pointer) null);
        }

        public CvCapture(Pointer p) {
            super(p);
        }
    }

    @Opaque
    public static class CvVideoWriter extends Pointer {
        public CvVideoWriter() {
            super((Pointer) null);
        }

        public CvVideoWriter(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    @Opaque
    public static class IVideoCapture extends Pointer {
        public IVideoCapture() {
            super((Pointer) null);
        }

        public IVideoCapture(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    @Opaque
    public static class IVideoWriter extends Pointer {
        public IVideoWriter() {
            super((Pointer) null);
        }

        public IVideoWriter(Pointer p) {
            super(p);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class VideoCapture extends Pointer {
        private native void allocate();

        private native void allocate(int i);

        private native void allocate(@Str String str);

        private native void allocate(@Str String str, int i);

        private native void allocate(@Str BytePointer bytePointer);

        private native void allocate(@Str BytePointer bytePointer, int i);

        private native void allocateArray(long j);

        public native double get(int i);

        @Cast({"bool"})
        public native boolean grab();

        @Cast({"bool"})
        public native boolean isOpened();

        @Cast({"bool"})
        public native boolean open(int i);

        @Cast({"bool"})
        public native boolean open(@Str String str);

        @Cast({"bool"})
        public native boolean open(@Str String str, int i);

        @Cast({"bool"})
        public native boolean open(@Str BytePointer bytePointer);

        @Cast({"bool"})
        public native boolean open(@Str BytePointer bytePointer, int i);

        @Cast({"bool"})
        public native boolean read(@ByVal Mat mat);

        @Cast({"bool"})
        public native boolean read(@ByVal UMat uMat);

        public native void release();

        @Cast({"bool"})
        public native boolean retrieve(@ByVal Mat mat);

        @Cast({"bool"})
        public native boolean retrieve(@ByVal Mat mat, int i);

        @Cast({"bool"})
        public native boolean retrieve(@ByVal UMat uMat);

        @Cast({"bool"})
        public native boolean retrieve(@ByVal UMat uMat, int i);

        @Cast({"bool"})
        public native boolean set(int i, double d);

        @ByRef
        @Name({"operator >>"})
        public native VideoCapture shiftRight(@ByRef Mat mat);

        @ByRef
        @Name({"operator >>"})
        public native VideoCapture shiftRight(@ByRef UMat uMat);

        static {
            Loader.load();
        }

        public VideoCapture(Pointer p) {
            super(p);
        }

        public VideoCapture(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public VideoCapture position(long position) {
            return (VideoCapture) super.position(position);
        }

        public VideoCapture() {
            super((Pointer) null);
            allocate();
        }

        public VideoCapture(@Str BytePointer filename) {
            super((Pointer) null);
            allocate(filename);
        }

        public VideoCapture(@Str String filename) {
            super((Pointer) null);
            allocate(filename);
        }

        public VideoCapture(@Str BytePointer filename, int apiPreference) {
            super((Pointer) null);
            allocate(filename, apiPreference);
        }

        public VideoCapture(@Str String filename, int apiPreference) {
            super((Pointer) null);
            allocate(filename, apiPreference);
        }

        public VideoCapture(int index) {
            super((Pointer) null);
            allocate(index);
        }
    }

    @Namespace("cv")
    @NoOffset
    public static class VideoWriter extends Pointer {
        private native void allocate();

        private native void allocate(@Str String str, int i, double d, @ByVal Size size);

        private native void allocate(@Str String str, int i, double d, @ByVal Size size, @Cast({"bool"}) boolean z);

        private native void allocate(@Str BytePointer bytePointer, int i, double d, @ByVal Size size);

        private native void allocate(@Str BytePointer bytePointer, int i, double d, @ByVal Size size, @Cast({"bool"}) boolean z);

        private native void allocateArray(long j);

        public static native int fourcc(@Cast({"char"}) byte b, @Cast({"char"}) byte b2, @Cast({"char"}) byte b3, @Cast({"char"}) byte b4);

        public native double get(int i);

        @Cast({"bool"})
        public native boolean isOpened();

        @Cast({"bool"})
        public native boolean open(@Str String str, int i, double d, @ByVal Size size);

        @Cast({"bool"})
        public native boolean open(@Str String str, int i, double d, @ByVal Size size, @Cast({"bool"}) boolean z);

        @Cast({"bool"})
        public native boolean open(@Str BytePointer bytePointer, int i, double d, @ByVal Size size);

        @Cast({"bool"})
        public native boolean open(@Str BytePointer bytePointer, int i, double d, @ByVal Size size, @Cast({"bool"}) boolean z);

        public native void release();

        @Cast({"bool"})
        public native boolean set(int i, double d);

        @ByRef
        @Name({"operator <<"})
        public native VideoWriter shiftLeft(@ByRef @Const Mat mat);

        public native void write(@ByRef @Const Mat mat);

        static {
            Loader.load();
        }

        public VideoWriter(Pointer p) {
            super(p);
        }

        public VideoWriter(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public VideoWriter position(long position) {
            return (VideoWriter) super.position(position);
        }

        public VideoWriter() {
            super((Pointer) null);
            allocate();
        }

        public VideoWriter(@Str BytePointer filename, int fourcc, double fps, @ByVal Size frameSize, @Cast({"bool"}) boolean isColor) {
            super((Pointer) null);
            allocate(filename, fourcc, fps, frameSize, isColor);
        }

        public VideoWriter(@Str BytePointer filename, int fourcc, double fps, @ByVal Size frameSize) {
            super((Pointer) null);
            allocate(filename, fourcc, fps, frameSize);
        }

        public VideoWriter(@Str String filename, int fourcc, double fps, @ByVal Size frameSize, @Cast({"bool"}) boolean isColor) {
            super((Pointer) null);
            allocate(filename, fourcc, fps, frameSize, isColor);
        }

        public VideoWriter(@Str String filename, int fourcc, double fps, @ByVal Size frameSize) {
            super((Pointer) null);
            allocate(filename, fourcc, fps, frameSize);
        }
    }

    public static native int CV_FOURCC(@Cast({"char"}) byte b, @Cast({"char"}) byte b2, @Cast({"char"}) byte b3, @Cast({"char"}) byte b4);

    @MemberGetter
    public static native int CV_FOURCC_DEFAULT();

    public static native CvCapture cvCaptureFromAVI(String str);

    public static native CvCapture cvCaptureFromAVI(@Cast({"const char*"}) BytePointer bytePointer);

    public static native CvCapture cvCaptureFromCAM(int i);

    public static native CvCapture cvCaptureFromFile(String str);

    public static native CvCapture cvCaptureFromFile(@Cast({"const char*"}) BytePointer bytePointer);

    public static native CvVideoWriter cvCreateAVIWriter(String str, int i, double d, @ByVal CvSize cvSize, int i2);

    public static native CvVideoWriter cvCreateAVIWriter(@Cast({"const char*"}) BytePointer bytePointer, int i, double d, @ByVal CvSize cvSize, int i2);

    public static native CvCapture cvCreateCameraCapture(int i);

    public static native CvCapture cvCreateFileCapture(String str);

    public static native CvCapture cvCreateFileCapture(@Cast({"const char*"}) BytePointer bytePointer);

    public static native CvCapture cvCreateFileCaptureWithPreference(String str, int i);

    public static native CvCapture cvCreateFileCaptureWithPreference(@Cast({"const char*"}) BytePointer bytePointer, int i);

    public static native CvVideoWriter cvCreateVideoWriter(String str, int i, double d, @ByVal CvSize cvSize);

    public static native CvVideoWriter cvCreateVideoWriter(String str, int i, double d, @ByVal CvSize cvSize, int i2);

    public static native CvVideoWriter cvCreateVideoWriter(@Cast({"const char*"}) BytePointer bytePointer, int i, double d, @ByVal CvSize cvSize);

    public static native CvVideoWriter cvCreateVideoWriter(@Cast({"const char*"}) BytePointer bytePointer, int i, double d, @ByVal CvSize cvSize, int i2);

    public static native int cvGetCaptureDomain(CvCapture cvCapture);

    public static native double cvGetCaptureProperty(CvCapture cvCapture, int i);

    public static native int cvGrabFrame(CvCapture cvCapture);

    public static native IplImage cvQueryFrame(CvCapture cvCapture);

    public static native void cvReleaseCapture(@Cast({"CvCapture**"}) PointerPointer pointerPointer);

    public static native void cvReleaseCapture(@ByPtrPtr CvCapture cvCapture);

    public static native void cvReleaseVideoWriter(@Cast({"CvVideoWriter**"}) PointerPointer pointerPointer);

    public static native void cvReleaseVideoWriter(@ByPtrPtr CvVideoWriter cvVideoWriter);

    public static native IplImage cvRetrieveFrame(CvCapture cvCapture);

    public static native IplImage cvRetrieveFrame(CvCapture cvCapture, int i);

    public static native int cvSetCaptureProperty(CvCapture cvCapture, int i, double d);

    public static native int cvWriteFrame(CvVideoWriter cvVideoWriter, @Const IplImage iplImage);

    public static native int cvWriteToAVI(CvVideoWriter cvVideoWriter, IplImage iplImage);

    static {
        Loader.load();
    }
}
