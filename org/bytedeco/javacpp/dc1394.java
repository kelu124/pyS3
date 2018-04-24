package org.bytedeco.javacpp;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import org.bytedeco.javacpp.annotation.ByPtrPtr;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.MemberGetter;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Opaque;
import org.bytedeco.javacpp.presets.dc1394.dc1394video_frame_t_abstract;

public class dc1394 extends org.bytedeco.javacpp.presets.dc1394 {
    public static final int DC1394_BASLER_CORRUPTED_SFF_CHUNK = -38;
    public static final int DC1394_BASLER_NO_MORE_SFF_CHUNKS = -37;
    public static final int DC1394_BASLER_UNKNOWN_SFF_CHUNK = -39;
    public static final int DC1394_BAYER_METHOD_AHD = 7;
    public static final int DC1394_BAYER_METHOD_BILINEAR = 2;
    public static final int DC1394_BAYER_METHOD_DOWNSAMPLE = 4;
    public static final int DC1394_BAYER_METHOD_EDGESENSE = 5;
    public static final int DC1394_BAYER_METHOD_HQLINEAR = 3;
    public static final int DC1394_BAYER_METHOD_MAX = 7;
    public static final int DC1394_BAYER_METHOD_MIN = 0;
    public static final int DC1394_BAYER_METHOD_NEAREST = 0;
    public static final int DC1394_BAYER_METHOD_NUM = 8;
    public static final int DC1394_BAYER_METHOD_SIMPLE = 1;
    public static final int DC1394_BAYER_METHOD_VNG = 6;
    public static final int DC1394_BYTE_ORDER_MAX = 801;
    public static final int DC1394_BYTE_ORDER_MIN = 800;
    public static final int DC1394_BYTE_ORDER_NUM = 2;
    public static final int DC1394_BYTE_ORDER_UYVY = 800;
    public static final int DC1394_BYTE_ORDER_YUYV = 801;
    public static final int DC1394_CAMERA_NOT_INITIALIZED = -4;
    public static final int DC1394_CAPTURE_FLAGS_AUTO_ISO = 8;
    public static final int DC1394_CAPTURE_FLAGS_BANDWIDTH_ALLOC = 2;
    public static final int DC1394_CAPTURE_FLAGS_CHANNEL_ALLOC = 1;
    public static final int DC1394_CAPTURE_FLAGS_DEFAULT = 4;
    public static final int DC1394_CAPTURE_IS_NOT_SET = -10;
    public static final int DC1394_CAPTURE_IS_RUNNING = -11;
    public static final int DC1394_CAPTURE_POLICY_MAX = 673;
    public static final int DC1394_CAPTURE_POLICY_MIN = 672;
    public static final int DC1394_CAPTURE_POLICY_NUM = 2;
    public static final int DC1394_CAPTURE_POLICY_POLL = 673;
    public static final int DC1394_CAPTURE_POLICY_WAIT = 672;
    public static final int DC1394_COLOR_CODING_MAX = 362;
    public static final int DC1394_COLOR_CODING_MIN = 352;
    public static final int DC1394_COLOR_CODING_MONO16 = 357;
    public static final int DC1394_COLOR_CODING_MONO16S = 359;
    public static final int DC1394_COLOR_CODING_MONO8 = 352;
    public static final int DC1394_COLOR_CODING_NUM = 11;
    public static final int DC1394_COLOR_CODING_RAW16 = 362;
    public static final int DC1394_COLOR_CODING_RAW8 = 361;
    public static final int DC1394_COLOR_CODING_RGB16 = 358;
    public static final int DC1394_COLOR_CODING_RGB16S = 360;
    public static final int DC1394_COLOR_CODING_RGB8 = 356;
    public static final int DC1394_COLOR_CODING_YUV411 = 353;
    public static final int DC1394_COLOR_CODING_YUV422 = 354;
    public static final int DC1394_COLOR_CODING_YUV444 = 355;
    public static final int DC1394_COLOR_FILTER_BGGR = 515;
    public static final int DC1394_COLOR_FILTER_GBRG = 513;
    public static final int DC1394_COLOR_FILTER_GRBG = 514;
    public static final int DC1394_COLOR_FILTER_MAX = 515;
    public static final int DC1394_COLOR_FILTER_MIN = 512;
    public static final int DC1394_COLOR_FILTER_NUM = 4;
    public static final int DC1394_COLOR_FILTER_RGGB = 512;
    public static final int DC1394_ERROR_MAX = 0;
    public static final int DC1394_ERROR_MIN = -39;
    public static final int DC1394_ERROR_NUM = 40;
    public static final int DC1394_FAILURE = -1;
    public static final int DC1394_FALSE = 0;
    public static final int DC1394_FEATURE_BRIGHTNESS = 416;
    public static final int DC1394_FEATURE_CAPTURE_QUALITY = 437;
    public static final int DC1394_FEATURE_CAPTURE_SIZE = 436;
    public static final int DC1394_FEATURE_EXPOSURE = 417;
    public static final int DC1394_FEATURE_FOCUS = 426;
    public static final int DC1394_FEATURE_FRAME_RATE = 431;
    public static final int DC1394_FEATURE_GAIN = 424;
    public static final int DC1394_FEATURE_GAMMA = 422;
    public static final int DC1394_FEATURE_HUE = 420;
    public static final int DC1394_FEATURE_IRIS = 425;
    public static final int DC1394_FEATURE_MAX = 437;
    public static final int DC1394_FEATURE_MIN = 416;
    public static final int DC1394_FEATURE_MODE_AUTO = 737;
    public static final int DC1394_FEATURE_MODE_MANUAL = 736;
    public static final int DC1394_FEATURE_MODE_MAX = 738;
    public static final int DC1394_FEATURE_MODE_MIN = 736;
    public static final int DC1394_FEATURE_MODE_NUM = 3;
    public static final int DC1394_FEATURE_MODE_ONE_PUSH_AUTO = 738;
    public static final int DC1394_FEATURE_NUM = 22;
    public static final int DC1394_FEATURE_OPTICAL_FILTER = 435;
    public static final int DC1394_FEATURE_PAN = 433;
    public static final int DC1394_FEATURE_SATURATION = 421;
    public static final int DC1394_FEATURE_SHARPNESS = 418;
    public static final int DC1394_FEATURE_SHUTTER = 423;
    public static final int DC1394_FEATURE_TEMPERATURE = 427;
    public static final int DC1394_FEATURE_TILT = 434;
    public static final int DC1394_FEATURE_TRIGGER = 428;
    public static final int DC1394_FEATURE_TRIGGER_DELAY = 429;
    public static final int DC1394_FEATURE_WHITE_BALANCE = 419;
    public static final int DC1394_FEATURE_WHITE_SHADING = 430;
    public static final int DC1394_FEATURE_ZOOM = 432;
    public static final int DC1394_FORMAT7_ERROR_FLAG_1 = -13;
    public static final int DC1394_FORMAT7_ERROR_FLAG_2 = -14;
    public static final int DC1394_FRAMERATE_120 = 38;
    public static final int DC1394_FRAMERATE_15 = 35;
    public static final int DC1394_FRAMERATE_1_875 = 32;
    public static final int DC1394_FRAMERATE_240 = 39;
    public static final int DC1394_FRAMERATE_30 = 36;
    public static final int DC1394_FRAMERATE_3_75 = 33;
    public static final int DC1394_FRAMERATE_60 = 37;
    public static final int DC1394_FRAMERATE_7_5 = 34;
    public static final int DC1394_FRAMERATE_MAX = 39;
    public static final int DC1394_FRAMERATE_MIN = 32;
    public static final int DC1394_FRAMERATE_NUM = 8;
    public static final int DC1394_FUNCTION_NOT_SUPPORTED = -3;
    public static final int DC1394_IIDC_VERSION_1_04 = 544;
    public static final int DC1394_IIDC_VERSION_1_20 = 545;
    public static final int DC1394_IIDC_VERSION_1_30 = 547;
    public static final int DC1394_IIDC_VERSION_1_31 = 548;
    public static final int DC1394_IIDC_VERSION_1_32 = 549;
    public static final int DC1394_IIDC_VERSION_1_33 = 550;
    public static final int DC1394_IIDC_VERSION_1_34 = 551;
    public static final int DC1394_IIDC_VERSION_1_35 = 552;
    public static final int DC1394_IIDC_VERSION_1_36 = 553;
    public static final int DC1394_IIDC_VERSION_1_37 = 554;
    public static final int DC1394_IIDC_VERSION_1_38 = 555;
    public static final int DC1394_IIDC_VERSION_1_39 = 556;
    public static final int DC1394_IIDC_VERSION_MAX = 556;
    public static final int DC1394_IIDC_VERSION_MIN = 544;
    public static final int DC1394_IIDC_VERSION_NUM = 13;
    public static final int DC1394_IIDC_VERSION_PTGREY = 546;
    public static final int DC1394_INVALID_ARGUMENT_VALUE = -15;
    public static final int DC1394_INVALID_BAYER_METHOD = -29;
    public static final int DC1394_INVALID_BYTE_ORDER = -35;
    public static final int DC1394_INVALID_CAPTURE_POLICY = -27;
    public static final int DC1394_INVALID_COLOR_CODING = -25;
    public static final int DC1394_INVALID_COLOR_FILTER = -26;
    public static final int DC1394_INVALID_ERROR_CODE = -28;
    public static final int DC1394_INVALID_FEATURE = -17;
    public static final int DC1394_INVALID_FEATURE_MODE = -33;
    public static final int DC1394_INVALID_FRAMERATE = -20;
    public static final int DC1394_INVALID_IIDC_VERSION = -24;
    public static final int DC1394_INVALID_ISO_SPEED = -23;
    public static final int DC1394_INVALID_LOG_TYPE = -34;
    public static final int DC1394_INVALID_OPERATION_MODE = -31;
    public static final int DC1394_INVALID_STEREO_METHOD = -36;
    public static final int DC1394_INVALID_TRIGGER_MODE = -21;
    public static final int DC1394_INVALID_TRIGGER_POLARITY = -32;
    public static final int DC1394_INVALID_TRIGGER_SOURCE = -22;
    public static final int DC1394_INVALID_VIDEO1394_DEVICE = -30;
    public static final int DC1394_INVALID_VIDEO_FORMAT = -18;
    public static final int DC1394_INVALID_VIDEO_MODE = -19;
    public static final int DC1394_IOCTL_FAILURE = -9;
    public static final int DC1394_ISO_SPEED_100 = 0;
    public static final int DC1394_ISO_SPEED_1600 = 4;
    public static final int DC1394_ISO_SPEED_200 = 1;
    public static final int DC1394_ISO_SPEED_3200 = 5;
    public static final int DC1394_ISO_SPEED_400 = 2;
    public static final int DC1394_ISO_SPEED_800 = 3;
    public static final int DC1394_ISO_SPEED_MAX = 5;
    public static final int DC1394_ISO_SPEED_MIN = 0;
    public static final int DC1394_ISO_SPEED_NUM = 6;
    public static final int DC1394_LOG_DEBUG = 770;
    public static final int DC1394_LOG_ERROR = 768;
    public static final int DC1394_LOG_MAX = 770;
    public static final int DC1394_LOG_MIN = 768;
    public static final int DC1394_LOG_NUM = 3;
    public static final int DC1394_LOG_WARNING = 769;
    public static final int DC1394_MEMORY_ALLOCATION_FAILURE = -5;
    public static final int DC1394_NOT_A_CAMERA = -2;
    public static final int DC1394_NO_BANDWIDTH = -8;
    public static final int DC1394_NO_ISO_CHANNEL = -7;
    public static final int DC1394_OFF = 0;
    public static final int DC1394_ON = 1;
    public static final int DC1394_OPERATION_MODE_1394B = 481;
    public static final int DC1394_OPERATION_MODE_LEGACY = 480;
    public static final int DC1394_OPERATION_MODE_MAX = 481;
    public static final int DC1394_OPERATION_MODE_MIN = 480;
    public static final int DC1394_OPERATION_MODE_NUM = 2;
    public static final int DC1394_PHY_DELAY_MAX = 641;
    public static final int DC1394_PHY_DELAY_MAX_144_NS = 640;
    public static final int DC1394_PHY_DELAY_MIN = 640;
    public static final int DC1394_PHY_DELAY_NUM = 2;
    public static final int DC1394_PHY_DELAY_UNKNOWN_0 = 641;
    public static final int DC1394_PHY_DELAY_UNKNOWN_1 = 642;
    public static final int DC1394_PHY_DELAY_UNKNOWN_2 = 643;
    public static final int DC1394_POWER_CLASS_MAX = 615;
    public static final int DC1394_POWER_CLASS_MIN = 608;
    public static final int DC1394_POWER_CLASS_NONE = 608;
    public static final int DC1394_POWER_CLASS_NUM = 8;
    public static final int DC1394_POWER_CLASS_PROV_MIN_15W = 609;
    public static final int DC1394_POWER_CLASS_PROV_MIN_30W = 610;
    public static final int DC1394_POWER_CLASS_PROV_MIN_45W = 611;
    public static final int DC1394_POWER_CLASS_USES_MAX_10W = 615;
    public static final int DC1394_POWER_CLASS_USES_MAX_1W = 612;
    public static final int DC1394_POWER_CLASS_USES_MAX_3W = 613;
    public static final int DC1394_POWER_CLASS_USES_MAX_6W = 614;
    public static final int DC1394_QUERY_FROM_CAMERA = -1;
    public static final int DC1394_RAW1394_FAILURE = -12;
    public static final int DC1394_REQ_VALUE_OUTSIDE_RANGE = -16;
    public static final int DC1394_STEREO_METHOD_FIELD = 1;
    public static final int DC1394_STEREO_METHOD_INTERLACED = 0;
    public static final int DC1394_STEREO_METHOD_MAX = 1;
    public static final int DC1394_STEREO_METHOD_MIN = 0;
    public static final int DC1394_STEREO_METHOD_NUM = 2;
    public static final int DC1394_SUCCESS = 0;
    public static final int DC1394_TAGGED_REGISTER_NOT_FOUND = -6;
    public static final int DC1394_TRIGGER_ACTIVE_HIGH = 705;
    public static final int DC1394_TRIGGER_ACTIVE_LOW = 704;
    public static final int DC1394_TRIGGER_ACTIVE_MAX = 705;
    public static final int DC1394_TRIGGER_ACTIVE_MIN = 704;
    public static final int DC1394_TRIGGER_ACTIVE_NUM = 2;
    public static final int DC1394_TRIGGER_MODE_0 = 384;
    public static final int DC1394_TRIGGER_MODE_1 = 385;
    public static final int DC1394_TRIGGER_MODE_14 = 390;
    public static final int DC1394_TRIGGER_MODE_15 = 391;
    public static final int DC1394_TRIGGER_MODE_2 = 386;
    public static final int DC1394_TRIGGER_MODE_3 = 387;
    public static final int DC1394_TRIGGER_MODE_4 = 388;
    public static final int DC1394_TRIGGER_MODE_5 = 389;
    public static final int DC1394_TRIGGER_MODE_MAX = 391;
    public static final int DC1394_TRIGGER_MODE_MIN = 384;
    public static final int DC1394_TRIGGER_MODE_NUM = 8;
    public static final int DC1394_TRIGGER_SOURCE_0 = 576;
    public static final int DC1394_TRIGGER_SOURCE_1 = 577;
    public static final int DC1394_TRIGGER_SOURCE_2 = 578;
    public static final int DC1394_TRIGGER_SOURCE_3 = 579;
    public static final int DC1394_TRIGGER_SOURCE_MAX = 580;
    public static final int DC1394_TRIGGER_SOURCE_MIN = 576;
    public static final int DC1394_TRIGGER_SOURCE_NUM = 5;
    public static final int DC1394_TRIGGER_SOURCE_SOFTWARE = 580;
    public static final int DC1394_TRUE = 1;
    public static final int DC1394_USE_MAX_AVAIL = -2;
    public static final int DC1394_USE_RECOMMENDED = -3;
    public static final int DC1394_VIDEO_MODE_1024x768_MONO16 = 78;
    public static final int DC1394_VIDEO_MODE_1024x768_MONO8 = 76;
    public static final int DC1394_VIDEO_MODE_1024x768_RGB8 = 75;
    public static final int DC1394_VIDEO_MODE_1024x768_YUV422 = 74;
    public static final int DC1394_VIDEO_MODE_1280x960_MONO16 = 85;
    public static final int DC1394_VIDEO_MODE_1280x960_MONO8 = 81;
    public static final int DC1394_VIDEO_MODE_1280x960_RGB8 = 80;
    public static final int DC1394_VIDEO_MODE_1280x960_YUV422 = 79;
    public static final int DC1394_VIDEO_MODE_1600x1200_MONO16 = 86;
    public static final int DC1394_VIDEO_MODE_1600x1200_MONO8 = 84;
    public static final int DC1394_VIDEO_MODE_1600x1200_RGB8 = 83;
    public static final int DC1394_VIDEO_MODE_1600x1200_YUV422 = 82;
    public static final int DC1394_VIDEO_MODE_160x120_YUV444 = 64;
    public static final int DC1394_VIDEO_MODE_320x240_YUV422 = 65;
    public static final int DC1394_VIDEO_MODE_640x480_MONO16 = 70;
    public static final int DC1394_VIDEO_MODE_640x480_MONO8 = 69;
    public static final int DC1394_VIDEO_MODE_640x480_RGB8 = 68;
    public static final int DC1394_VIDEO_MODE_640x480_YUV411 = 66;
    public static final int DC1394_VIDEO_MODE_640x480_YUV422 = 67;
    public static final int DC1394_VIDEO_MODE_800x600_MONO16 = 77;
    public static final int DC1394_VIDEO_MODE_800x600_MONO8 = 73;
    public static final int DC1394_VIDEO_MODE_800x600_RGB8 = 72;
    public static final int DC1394_VIDEO_MODE_800x600_YUV422 = 71;
    public static final int DC1394_VIDEO_MODE_EXIF = 87;
    public static final int DC1394_VIDEO_MODE_FORMAT7_0 = 88;
    public static final int DC1394_VIDEO_MODE_FORMAT7_1 = 89;
    public static final int DC1394_VIDEO_MODE_FORMAT7_2 = 90;
    public static final int DC1394_VIDEO_MODE_FORMAT7_3 = 91;
    public static final int DC1394_VIDEO_MODE_FORMAT7_4 = 92;
    public static final int DC1394_VIDEO_MODE_FORMAT7_5 = 93;
    public static final int DC1394_VIDEO_MODE_FORMAT7_6 = 94;
    public static final int DC1394_VIDEO_MODE_FORMAT7_7 = 95;
    public static final int DC1394_VIDEO_MODE_FORMAT7_MAX = 95;
    public static final int DC1394_VIDEO_MODE_FORMAT7_MIN = 88;
    public static final int DC1394_VIDEO_MODE_FORMAT7_NUM = 8;
    public static final int DC1394_VIDEO_MODE_MAX = 95;
    public static final int DC1394_VIDEO_MODE_MIN = 64;
    public static final int DC1394_VIDEO_MODE_NUM = 32;

    @Opaque
    public static class dc1394_t extends Pointer {
        public dc1394_t() {
            super((Pointer) null);
        }

        public dc1394_t(Pointer p) {
            super(p);
        }
    }

    public static class dc1394camera_id_t extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"uint64_t"})
        public native long guid();

        public native dc1394camera_id_t guid(long j);

        public native dc1394camera_id_t unit(short s);

        @Cast({"uint16_t"})
        public native short unit();

        static {
            Loader.load();
        }

        public dc1394camera_id_t() {
            super((Pointer) null);
            allocate();
        }

        public dc1394camera_id_t(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public dc1394camera_id_t(Pointer p) {
            super(p);
        }

        public dc1394camera_id_t position(long position) {
            return (dc1394camera_id_t) super.position(position);
        }
    }

    public static class dc1394camera_list_t extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native dc1394camera_id_t ids();

        public native dc1394camera_list_t ids(dc1394camera_id_t org_bytedeco_javacpp_dc1394_dc1394camera_id_t);

        @Cast({"uint32_t"})
        public native int num();

        public native dc1394camera_list_t num(int i);

        static {
            Loader.load();
        }

        public dc1394camera_list_t() {
            super((Pointer) null);
            allocate();
        }

        public dc1394camera_list_t(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public dc1394camera_list_t(Pointer p) {
            super(p);
        }

        public dc1394camera_list_t position(long position) {
            return (dc1394camera_list_t) super.position(position);
        }
    }

    public static class dc1394camera_t extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"uint64_t"})
        public native long PIO_control_csr();

        public native dc1394camera_t PIO_control_csr(long j);

        @Cast({"uint64_t"})
        public native long SIO_control_csr();

        public native dc1394camera_t SIO_control_csr(long j);

        @Cast({"uint64_t"})
        public native long advanced_features_csr();

        public native dc1394camera_t advanced_features_csr(long j);

        @Cast({"dc1394bool_t"})
        public native int bmode_capable();

        public native dc1394camera_t bmode_capable(int i);

        @Cast({"dc1394bool_t"})
        public native int can_switch_on_off();

        public native dc1394camera_t can_switch_on_off(int i);

        @Cast({"uint32_t"})
        public native int command_registers_base();

        public native dc1394camera_t command_registers_base(int i);

        @Cast({"uint32_t"})
        public native int flags();

        public native dc1394camera_t flags(int i);

        @Cast({"uint64_t"})
        public native long format7_csr(int i);

        @MemberGetter
        @Cast({"uint64_t*"})
        public native LongPointer format7_csr();

        public native dc1394camera_t format7_csr(int i, long j);

        @Cast({"uint64_t"})
        public native long guid();

        public native dc1394camera_t guid(long j);

        @Cast({"dc1394bool_t"})
        public native int has_feature_error_status();

        public native dc1394camera_t has_feature_error_status(int i);

        @Cast({"dc1394bool_t"})
        public native int has_vmode_error_status();

        public native dc1394camera_t has_vmode_error_status(int i);

        @Cast({"dc1394iidc_version_t"})
        public native int iidc_version();

        public native dc1394camera_t iidc_version(int i);

        public native int max_mem_channel();

        public native dc1394camera_t max_mem_channel(int i);

        @Cast({"char*"})
        public native BytePointer model();

        public native dc1394camera_t model(BytePointer bytePointer);

        @Cast({"uint32_t"})
        public native int model_id();

        public native dc1394camera_t model_id(int i);

        @Cast({"dc1394bool_t"})
        public native int multi_shot_capable();

        public native dc1394camera_t multi_shot_capable(int i);

        @Cast({"dc1394bool_t"})
        public native int one_shot_capable();

        public native dc1394camera_t one_shot_capable(int i);

        @Cast({"uint64_t"})
        public native long strobe_control_csr();

        public native dc1394camera_t strobe_control_csr(long j);

        public native int unit();

        public native dc1394camera_t unit(int i);

        @Cast({"uint32_t"})
        public native int unit_dependent_directory();

        public native dc1394camera_t unit_dependent_directory(int i);

        @Cast({"uint32_t"})
        public native int unit_directory();

        public native dc1394camera_t unit_directory(int i);

        @Cast({"uint32_t"})
        public native int unit_spec_ID();

        public native dc1394camera_t unit_spec_ID(int i);

        @Cast({"uint32_t"})
        public native int unit_sub_sw_version();

        public native dc1394camera_t unit_sub_sw_version(int i);

        @Cast({"uint32_t"})
        public native int unit_sw_version();

        public native dc1394camera_t unit_sw_version(int i);

        @Cast({"char*"})
        public native BytePointer vendor();

        public native dc1394camera_t vendor(BytePointer bytePointer);

        @Cast({"uint32_t"})
        public native int vendor_id();

        public native dc1394camera_t vendor_id(int i);

        static {
            Loader.load();
        }

        public dc1394camera_t() {
            super((Pointer) null);
            allocate();
        }

        public dc1394camera_t(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public dc1394camera_t(Pointer p) {
            super(p);
        }

        public dc1394camera_t position(long position) {
            return (dc1394camera_t) super.position(position);
        }
    }

    public static class dc1394color_codings_t extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"dc1394color_coding_t"})
        public native int codings(int i);

        @MemberGetter
        @Cast({"dc1394color_coding_t*"})
        public native IntPointer codings();

        public native dc1394color_codings_t codings(int i, int i2);

        @Cast({"uint32_t"})
        public native int num();

        public native dc1394color_codings_t num(int i);

        static {
            Loader.load();
        }

        public dc1394color_codings_t() {
            super((Pointer) null);
            allocate();
        }

        public dc1394color_codings_t(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public dc1394color_codings_t(Pointer p) {
            super(p);
        }

        public dc1394color_codings_t position(long position) {
            return (dc1394color_codings_t) super.position(position);
        }
    }

    public static class dc1394feature_info_t extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"uint32_t"})
        public native int BU_value();

        public native dc1394feature_info_t BU_value(int i);

        @Cast({"uint32_t"})
        public native int B_value();

        public native dc1394feature_info_t B_value(int i);

        @Cast({"uint32_t"})
        public native int G_value();

        public native dc1394feature_info_t G_value(int i);

        @Cast({"uint32_t"})
        public native int RV_value();

        public native dc1394feature_info_t RV_value(int i);

        @Cast({"uint32_t"})
        public native int R_value();

        public native dc1394feature_info_t R_value(int i);

        @Cast({"dc1394switch_t"})
        public native int abs_control();

        public native dc1394feature_info_t abs_control(int i);

        public native float abs_max();

        public native dc1394feature_info_t abs_max(float f);

        public native float abs_min();

        public native dc1394feature_info_t abs_min(float f);

        public native float abs_value();

        public native dc1394feature_info_t abs_value(float f);

        @Cast({"dc1394bool_t"})
        public native int absolute_capable();

        public native dc1394feature_info_t absolute_capable(int i);

        @Cast({"dc1394bool_t"})
        public native int available();

        public native dc1394feature_info_t available(int i);

        @Cast({"dc1394feature_mode_t"})
        public native int current_mode();

        public native dc1394feature_info_t current_mode(int i);

        @Cast({"dc1394feature_t"})
        public native int id();

        public native dc1394feature_info_t id(int i);

        @Cast({"dc1394switch_t"})
        public native int is_on();

        public native dc1394feature_info_t is_on(int i);

        @Cast({"uint32_t"})
        public native int max();

        public native dc1394feature_info_t max(int i);

        @Cast({"uint32_t"})
        public native int min();

        public native dc1394feature_info_t min(int i);

        public native dc1394feature_info_t modes(dc1394feature_modes_t org_bytedeco_javacpp_dc1394_dc1394feature_modes_t);

        @ByRef
        public native dc1394feature_modes_t modes();

        @Cast({"dc1394bool_t"})
        public native int on_off_capable();

        public native dc1394feature_info_t on_off_capable(int i);

        @Cast({"dc1394bool_t"})
        public native int polarity_capable();

        public native dc1394feature_info_t polarity_capable(int i);

        @Cast({"dc1394bool_t"})
        public native int readout_capable();

        public native dc1394feature_info_t readout_capable(int i);

        @Cast({"uint32_t"})
        public native int target_value();

        public native dc1394feature_info_t target_value(int i);

        @Cast({"dc1394trigger_mode_t"})
        public native int trigger_mode();

        public native dc1394feature_info_t trigger_mode(int i);

        public native dc1394feature_info_t trigger_modes(dc1394trigger_modes_t org_bytedeco_javacpp_dc1394_dc1394trigger_modes_t);

        @ByRef
        public native dc1394trigger_modes_t trigger_modes();

        @Cast({"dc1394trigger_polarity_t"})
        public native int trigger_polarity();

        public native dc1394feature_info_t trigger_polarity(int i);

        @Cast({"dc1394trigger_source_t"})
        public native int trigger_source();

        public native dc1394feature_info_t trigger_source(int i);

        public native dc1394feature_info_t trigger_sources(dc1394trigger_sources_t org_bytedeco_javacpp_dc1394_dc1394trigger_sources_t);

        @ByRef
        public native dc1394trigger_sources_t trigger_sources();

        @Cast({"uint32_t"})
        public native int value();

        public native dc1394feature_info_t value(int i);

        static {
            Loader.load();
        }

        public dc1394feature_info_t() {
            super((Pointer) null);
            allocate();
        }

        public dc1394feature_info_t(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public dc1394feature_info_t(Pointer p) {
            super(p);
        }

        public dc1394feature_info_t position(long position) {
            return (dc1394feature_info_t) super.position(position);
        }
    }

    public static class dc1394feature_modes_t extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"dc1394feature_mode_t"})
        public native int modes(int i);

        @MemberGetter
        @Cast({"dc1394feature_mode_t*"})
        public native IntPointer modes();

        public native dc1394feature_modes_t modes(int i, int i2);

        @Cast({"uint32_t"})
        public native int num();

        public native dc1394feature_modes_t num(int i);

        static {
            Loader.load();
        }

        public dc1394feature_modes_t() {
            super((Pointer) null);
            allocate();
        }

        public dc1394feature_modes_t(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public dc1394feature_modes_t(Pointer p) {
            super(p);
        }

        public dc1394feature_modes_t position(long position) {
            return (dc1394feature_modes_t) super.position(position);
        }
    }

    public static class dc1394featureset_t extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @MemberGetter
        public native dc1394feature_info_t feature();

        @ByRef
        public native dc1394feature_info_t feature(int i);

        public native dc1394featureset_t feature(int i, dc1394feature_info_t org_bytedeco_javacpp_dc1394_dc1394feature_info_t);

        static {
            Loader.load();
        }

        public dc1394featureset_t() {
            super((Pointer) null);
            allocate();
        }

        public dc1394featureset_t(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public dc1394featureset_t(Pointer p) {
            super(p);
        }

        public dc1394featureset_t position(long position) {
            return (dc1394featureset_t) super.position(position);
        }
    }

    public static class dc1394format7mode_t extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"dc1394color_coding_t"})
        public native int color_coding();

        public native dc1394format7mode_t color_coding(int i);

        @ByRef
        public native dc1394color_codings_t color_codings();

        public native dc1394format7mode_t color_codings(dc1394color_codings_t org_bytedeco_javacpp_dc1394_dc1394color_codings_t);

        @Cast({"dc1394color_filter_t"})
        public native int color_filter();

        public native dc1394format7mode_t color_filter(int i);

        @Cast({"uint32_t"})
        public native int max_packet_size();

        public native dc1394format7mode_t max_packet_size(int i);

        @Cast({"uint32_t"})
        public native int max_size_x();

        public native dc1394format7mode_t max_size_x(int i);

        @Cast({"uint32_t"})
        public native int max_size_y();

        public native dc1394format7mode_t max_size_y(int i);

        @Cast({"uint32_t"})
        public native int packet_size();

        public native dc1394format7mode_t packet_size(int i);

        @Cast({"uint32_t"})
        public native int pixnum();

        public native dc1394format7mode_t pixnum(int i);

        @Cast({"uint32_t"})
        public native int pos_x();

        public native dc1394format7mode_t pos_x(int i);

        @Cast({"uint32_t"})
        public native int pos_y();

        public native dc1394format7mode_t pos_y(int i);

        @Cast({"dc1394bool_t"})
        public native int present();

        public native dc1394format7mode_t present(int i);

        @Cast({"uint32_t"})
        public native int size_x();

        public native dc1394format7mode_t size_x(int i);

        @Cast({"uint32_t"})
        public native int size_y();

        public native dc1394format7mode_t size_y(int i);

        @Cast({"uint64_t"})
        public native long total_bytes();

        public native dc1394format7mode_t total_bytes(long j);

        @Cast({"uint32_t"})
        public native int unit_packet_size();

        public native dc1394format7mode_t unit_packet_size(int i);

        @Cast({"uint32_t"})
        public native int unit_pos_x();

        public native dc1394format7mode_t unit_pos_x(int i);

        @Cast({"uint32_t"})
        public native int unit_pos_y();

        public native dc1394format7mode_t unit_pos_y(int i);

        @Cast({"uint32_t"})
        public native int unit_size_x();

        public native dc1394format7mode_t unit_size_x(int i);

        @Cast({"uint32_t"})
        public native int unit_size_y();

        public native dc1394format7mode_t unit_size_y(int i);

        static {
            Loader.load();
        }

        public dc1394format7mode_t() {
            super((Pointer) null);
            allocate();
        }

        public dc1394format7mode_t(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public dc1394format7mode_t(Pointer p) {
            super(p);
        }

        public dc1394format7mode_t position(long position) {
            return (dc1394format7mode_t) super.position(position);
        }
    }

    public static class dc1394format7modeset_t extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @MemberGetter
        public native dc1394format7mode_t mode();

        @ByRef
        public native dc1394format7mode_t mode(int i);

        public native dc1394format7modeset_t mode(int i, dc1394format7mode_t org_bytedeco_javacpp_dc1394_dc1394format7mode_t);

        static {
            Loader.load();
        }

        public dc1394format7modeset_t() {
            super((Pointer) null);
            allocate();
        }

        public dc1394format7modeset_t(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public dc1394format7modeset_t(Pointer p) {
            super(p);
        }

        public dc1394format7modeset_t position(long position) {
            return (dc1394format7modeset_t) super.position(position);
        }
    }

    public static class dc1394framerates_t extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"dc1394framerate_t"})
        public native int framerates(int i);

        @MemberGetter
        @Cast({"dc1394framerate_t*"})
        public native IntPointer framerates();

        public native dc1394framerates_t framerates(int i, int i2);

        @Cast({"uint32_t"})
        public native int num();

        public native dc1394framerates_t num(int i);

        static {
            Loader.load();
        }

        public dc1394framerates_t() {
            super((Pointer) null);
            allocate();
        }

        public dc1394framerates_t(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public dc1394framerates_t(Pointer p) {
            super(p);
        }

        public dc1394framerates_t position(long position) {
            return (dc1394framerates_t) super.position(position);
        }
    }

    public static class dc1394trigger_modes_t extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"dc1394trigger_mode_t"})
        public native int modes(int i);

        @MemberGetter
        @Cast({"dc1394trigger_mode_t*"})
        public native IntPointer modes();

        public native dc1394trigger_modes_t modes(int i, int i2);

        @Cast({"uint32_t"})
        public native int num();

        public native dc1394trigger_modes_t num(int i);

        static {
            Loader.load();
        }

        public dc1394trigger_modes_t() {
            super((Pointer) null);
            allocate();
        }

        public dc1394trigger_modes_t(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public dc1394trigger_modes_t(Pointer p) {
            super(p);
        }

        public dc1394trigger_modes_t position(long position) {
            return (dc1394trigger_modes_t) super.position(position);
        }
    }

    public static class dc1394trigger_sources_t extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"uint32_t"})
        public native int num();

        public native dc1394trigger_sources_t num(int i);

        @Cast({"dc1394trigger_source_t"})
        public native int sources(int i);

        @MemberGetter
        @Cast({"dc1394trigger_source_t*"})
        public native IntPointer sources();

        public native dc1394trigger_sources_t sources(int i, int i2);

        static {
            Loader.load();
        }

        public dc1394trigger_sources_t() {
            super((Pointer) null);
            allocate();
        }

        public dc1394trigger_sources_t(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public dc1394trigger_sources_t(Pointer p) {
            super(p);
        }

        public dc1394trigger_sources_t position(long position) {
            return (dc1394trigger_sources_t) super.position(position);
        }
    }

    public static class dc1394video_frame_t extends dc1394video_frame_t_abstract {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"uint32_t"})
        @Name({"position"})
        public native int _position(int i);

        @MemberGetter
        @Cast({"uint32_t*"})
        @Name({"position"})
        public native IntPointer _position();

        public native dc1394video_frame_t _position(int i, int i2);

        @Cast({"uint64_t"})
        public native long allocated_image_bytes();

        public native dc1394video_frame_t allocated_image_bytes(long j);

        public native dc1394camera_t camera();

        public native dc1394video_frame_t camera(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t);

        @Cast({"dc1394color_coding_t"})
        public native int color_coding();

        public native dc1394video_frame_t color_coding(int i);

        @Cast({"dc1394color_filter_t"})
        public native int color_filter();

        public native dc1394video_frame_t color_filter(int i);

        @Cast({"uint32_t"})
        public native int data_depth();

        public native dc1394video_frame_t data_depth(int i);

        @Cast({"dc1394bool_t"})
        public native int data_in_padding();

        public native dc1394video_frame_t data_in_padding(int i);

        @Cast({"uint32_t"})
        public native int frames_behind();

        public native dc1394video_frame_t frames_behind(int i);

        @Cast({"uint32_t"})
        public native int id();

        public native dc1394video_frame_t id(int i);

        @Cast({"unsigned char*"})
        public native BytePointer image();

        public native dc1394video_frame_t image(BytePointer bytePointer);

        @Cast({"uint32_t"})
        public native int image_bytes();

        public native dc1394video_frame_t image_bytes(int i);

        @Cast({"dc1394bool_t"})
        public native int little_endian();

        public native dc1394video_frame_t little_endian(int i);

        @Cast({"uint32_t"})
        public native int packet_size();

        public native dc1394video_frame_t packet_size(int i);

        @Cast({"uint32_t"})
        public native int packets_per_frame();

        public native dc1394video_frame_t packets_per_frame(int i);

        @Cast({"uint32_t"})
        public native int padding_bytes();

        public native dc1394video_frame_t padding_bytes(int i);

        @Cast({"uint32_t"})
        public native int size(int i);

        @MemberGetter
        @Cast({"uint32_t*"})
        public native IntPointer size();

        public native dc1394video_frame_t size(int i, int i2);

        @Cast({"uint32_t"})
        public native int stride();

        public native dc1394video_frame_t stride(int i);

        @Cast({"uint64_t"})
        public native long timestamp();

        public native dc1394video_frame_t timestamp(long j);

        @Cast({"uint64_t"})
        public native long total_bytes();

        public native dc1394video_frame_t total_bytes(long j);

        @Cast({"dc1394video_mode_t"})
        public native int video_mode();

        public native dc1394video_frame_t video_mode(int i);

        @Cast({"uint32_t"})
        public native int yuv_byte_order();

        public native dc1394video_frame_t yuv_byte_order(int i);

        static {
            Loader.load();
        }

        public dc1394video_frame_t() {
            super((Pointer) null);
            allocate();
        }

        public dc1394video_frame_t(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public dc1394video_frame_t(Pointer p) {
            super(p);
        }

        public dc1394video_frame_t position(long position) {
            return (dc1394video_frame_t) super.position(position);
        }
    }

    public static class dc1394video_modes_t extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"dc1394video_mode_t"})
        public native int modes(int i);

        @MemberGetter
        @Cast({"dc1394video_mode_t*"})
        public native IntPointer modes();

        public native dc1394video_modes_t modes(int i, int i2);

        @Cast({"uint32_t"})
        public native int num();

        public native dc1394video_modes_t num(int i);

        static {
            Loader.load();
        }

        public dc1394video_modes_t() {
            super((Pointer) null);
            allocate();
        }

        public dc1394video_modes_t(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public dc1394video_modes_t(Pointer p) {
            super(p);
        }

        public dc1394video_modes_t position(long position) {
            return (dc1394video_modes_t) super.position(position);
        }
    }

    public static native void RGB2YUV(int i, int i2, int i3, @ByRef IntBuffer intBuffer, @ByRef IntBuffer intBuffer2, @ByRef IntBuffer intBuffer3);

    public static native void RGB2YUV(int i, int i2, int i3, @ByRef IntPointer intPointer, @ByRef IntPointer intPointer2, @ByRef IntPointer intPointer3);

    public static native void RGB2YUV(int i, int i2, int i3, @ByRef int[] iArr, @ByRef int[] iArr2, @ByRef int[] iArr3);

    public static native void YUV2RGB(int i, int i2, int i3, @ByRef IntBuffer intBuffer, @ByRef IntBuffer intBuffer2, @ByRef IntBuffer intBuffer3);

    public static native void YUV2RGB(int i, int i2, int i3, @ByRef IntPointer intPointer, @ByRef IntPointer intPointer2, @ByRef IntPointer intPointer3);

    public static native void YUV2RGB(int i, int i2, int i3, @ByRef int[] iArr, @ByRef int[] iArr2, @ByRef int[] iArr3);

    @Cast({"dc1394error_t"})
    public static native int dc1394_bayer_decoding_16bit(@Cast({"const uint16_t*"}) ShortBuffer shortBuffer, @Cast({"uint16_t*"}) ShortBuffer shortBuffer2, @Cast({"uint32_t"}) int i, @Cast({"uint32_t"}) int i2, @Cast({"dc1394color_filter_t"}) int i3, @Cast({"dc1394bayer_method_t"}) int i4, @Cast({"uint32_t"}) int i5);

    @Cast({"dc1394error_t"})
    public static native int dc1394_bayer_decoding_16bit(@Cast({"const uint16_t*"}) ShortPointer shortPointer, @Cast({"uint16_t*"}) ShortPointer shortPointer2, @Cast({"uint32_t"}) int i, @Cast({"uint32_t"}) int i2, @Cast({"dc1394color_filter_t"}) int i3, @Cast({"dc1394bayer_method_t"}) int i4, @Cast({"uint32_t"}) int i5);

    @Cast({"dc1394error_t"})
    public static native int dc1394_bayer_decoding_16bit(@Cast({"const uint16_t*"}) short[] sArr, @Cast({"uint16_t*"}) short[] sArr2, @Cast({"uint32_t"}) int i, @Cast({"uint32_t"}) int i2, @Cast({"dc1394color_filter_t"}) int i3, @Cast({"dc1394bayer_method_t"}) int i4, @Cast({"uint32_t"}) int i5);

    @Cast({"dc1394error_t"})
    public static native int dc1394_bayer_decoding_8bit(@Cast({"const uint8_t*"}) ByteBuffer byteBuffer, @Cast({"uint8_t*"}) ByteBuffer byteBuffer2, @Cast({"uint32_t"}) int i, @Cast({"uint32_t"}) int i2, @Cast({"dc1394color_filter_t"}) int i3, @Cast({"dc1394bayer_method_t"}) int i4);

    @Cast({"dc1394error_t"})
    public static native int dc1394_bayer_decoding_8bit(@Cast({"const uint8_t*"}) BytePointer bytePointer, @Cast({"uint8_t*"}) BytePointer bytePointer2, @Cast({"uint32_t"}) int i, @Cast({"uint32_t"}) int i2, @Cast({"dc1394color_filter_t"}) int i3, @Cast({"dc1394bayer_method_t"}) int i4);

    @Cast({"dc1394error_t"})
    public static native int dc1394_bayer_decoding_8bit(@Cast({"const uint8_t*"}) byte[] bArr, @Cast({"uint8_t*"}) byte[] bArr2, @Cast({"uint32_t"}) int i, @Cast({"uint32_t"}) int i2, @Cast({"dc1394color_filter_t"}) int i3, @Cast({"dc1394bayer_method_t"}) int i4);

    @Cast({"dc1394error_t"})
    public static native int dc1394_camera_enumerate(dc1394_t org_bytedeco_javacpp_dc1394_dc1394_t, @Cast({"dc1394camera_list_t**"}) PointerPointer pointerPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_camera_enumerate(dc1394_t org_bytedeco_javacpp_dc1394_dc1394_t, @ByPtrPtr dc1394camera_list_t org_bytedeco_javacpp_dc1394_dc1394camera_list_t);

    public static native void dc1394_camera_free(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t);

    public static native void dc1394_camera_free_list(dc1394camera_list_t org_bytedeco_javacpp_dc1394_dc1394camera_list_t);

    @Cast({"dc1394error_t"})
    public static native int dc1394_camera_get_broadcast(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394bool_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_camera_get_broadcast(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394bool_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_camera_get_broadcast(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394bool_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_camera_get_node(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) IntBuffer intBuffer, @Cast({"uint32_t*"}) IntBuffer intBuffer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_camera_get_node(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) IntPointer intPointer, @Cast({"uint32_t*"}) IntPointer intPointer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_camera_get_node(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) int[] iArr, @Cast({"uint32_t*"}) int[] iArr2);

    public static native dc1394camera_t dc1394_camera_new(dc1394_t org_bytedeco_javacpp_dc1394_dc1394_t, @Cast({"uint64_t"}) long j);

    public static native dc1394camera_t dc1394_camera_new_unit(dc1394_t org_bytedeco_javacpp_dc1394_dc1394_t, @Cast({"uint64_t"}) long j, int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_camera_print_info(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"FILE*"}) Pointer pointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_camera_reset(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t);

    @Cast({"dc1394error_t"})
    public static native int dc1394_camera_set_broadcast(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394bool_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_camera_set_power(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394switch_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_capture_dequeue(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394capture_policy_t"}) int i, @Cast({"dc1394video_frame_t**"}) PointerPointer pointerPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_capture_dequeue(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394capture_policy_t"}) int i, @ByPtrPtr dc1394video_frame_t org_bytedeco_javacpp_dc1394_dc1394video_frame_t);

    @Cast({"dc1394error_t"})
    public static native int dc1394_capture_enqueue(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, dc1394video_frame_t org_bytedeco_javacpp_dc1394_dc1394video_frame_t);

    public static native int dc1394_capture_get_fileno(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t);

    @Cast({"dc1394bool_t"})
    public static native int dc1394_capture_is_frame_corrupt(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, dc1394video_frame_t org_bytedeco_javacpp_dc1394_dc1394video_frame_t);

    public static native void dc1394_capture_set_callback(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, dc1394capture_callback_t org_bytedeco_javacpp_dc1394_dc1394capture_callback_t, Pointer pointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_capture_setup(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t"}) int i, @Cast({"uint32_t"}) int i2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_capture_stop(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t);

    @Cast({"uint16_t"})
    public static native short dc1394_checksum_crc16(@Cast({"const uint8_t*"}) ByteBuffer byteBuffer, @Cast({"uint32_t"}) int i);

    @Cast({"uint16_t"})
    public static native short dc1394_checksum_crc16(@Cast({"const uint8_t*"}) BytePointer bytePointer, @Cast({"uint32_t"}) int i);

    @Cast({"uint16_t"})
    public static native short dc1394_checksum_crc16(@Cast({"const uint8_t*"}) byte[] bArr, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_convert_frames(dc1394video_frame_t org_bytedeco_javacpp_dc1394_dc1394video_frame_t, dc1394video_frame_t org_bytedeco_javacpp_dc1394_dc1394video_frame_t2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_convert_to_MONO8(@Cast({"uint8_t*"}) ByteBuffer byteBuffer, @Cast({"uint8_t*"}) ByteBuffer byteBuffer2, @Cast({"uint32_t"}) int i, @Cast({"uint32_t"}) int i2, @Cast({"uint32_t"}) int i3, @Cast({"dc1394color_coding_t"}) int i4, @Cast({"uint32_t"}) int i5);

    @Cast({"dc1394error_t"})
    public static native int dc1394_convert_to_MONO8(@Cast({"uint8_t*"}) BytePointer bytePointer, @Cast({"uint8_t*"}) BytePointer bytePointer2, @Cast({"uint32_t"}) int i, @Cast({"uint32_t"}) int i2, @Cast({"uint32_t"}) int i3, @Cast({"dc1394color_coding_t"}) int i4, @Cast({"uint32_t"}) int i5);

    @Cast({"dc1394error_t"})
    public static native int dc1394_convert_to_MONO8(@Cast({"uint8_t*"}) byte[] bArr, @Cast({"uint8_t*"}) byte[] bArr2, @Cast({"uint32_t"}) int i, @Cast({"uint32_t"}) int i2, @Cast({"uint32_t"}) int i3, @Cast({"dc1394color_coding_t"}) int i4, @Cast({"uint32_t"}) int i5);

    @Cast({"dc1394error_t"})
    public static native int dc1394_convert_to_RGB8(@Cast({"uint8_t*"}) ByteBuffer byteBuffer, @Cast({"uint8_t*"}) ByteBuffer byteBuffer2, @Cast({"uint32_t"}) int i, @Cast({"uint32_t"}) int i2, @Cast({"uint32_t"}) int i3, @Cast({"dc1394color_coding_t"}) int i4, @Cast({"uint32_t"}) int i5);

    @Cast({"dc1394error_t"})
    public static native int dc1394_convert_to_RGB8(@Cast({"uint8_t*"}) BytePointer bytePointer, @Cast({"uint8_t*"}) BytePointer bytePointer2, @Cast({"uint32_t"}) int i, @Cast({"uint32_t"}) int i2, @Cast({"uint32_t"}) int i3, @Cast({"dc1394color_coding_t"}) int i4, @Cast({"uint32_t"}) int i5);

    @Cast({"dc1394error_t"})
    public static native int dc1394_convert_to_RGB8(@Cast({"uint8_t*"}) byte[] bArr, @Cast({"uint8_t*"}) byte[] bArr2, @Cast({"uint32_t"}) int i, @Cast({"uint32_t"}) int i2, @Cast({"uint32_t"}) int i3, @Cast({"dc1394color_coding_t"}) int i4, @Cast({"uint32_t"}) int i5);

    @Cast({"dc1394error_t"})
    public static native int dc1394_convert_to_YUV422(@Cast({"uint8_t*"}) ByteBuffer byteBuffer, @Cast({"uint8_t*"}) ByteBuffer byteBuffer2, @Cast({"uint32_t"}) int i, @Cast({"uint32_t"}) int i2, @Cast({"uint32_t"}) int i3, @Cast({"dc1394color_coding_t"}) int i4, @Cast({"uint32_t"}) int i5);

    @Cast({"dc1394error_t"})
    public static native int dc1394_convert_to_YUV422(@Cast({"uint8_t*"}) BytePointer bytePointer, @Cast({"uint8_t*"}) BytePointer bytePointer2, @Cast({"uint32_t"}) int i, @Cast({"uint32_t"}) int i2, @Cast({"uint32_t"}) int i3, @Cast({"dc1394color_coding_t"}) int i4, @Cast({"uint32_t"}) int i5);

    @Cast({"dc1394error_t"})
    public static native int dc1394_convert_to_YUV422(@Cast({"uint8_t*"}) byte[] bArr, @Cast({"uint8_t*"}) byte[] bArr2, @Cast({"uint32_t"}) int i, @Cast({"uint32_t"}) int i2, @Cast({"uint32_t"}) int i3, @Cast({"dc1394color_coding_t"}) int i4, @Cast({"uint32_t"}) int i5);

    @Cast({"dc1394error_t"})
    public static native int dc1394_debayer_frames(dc1394video_frame_t org_bytedeco_javacpp_dc1394_dc1394video_frame_t, dc1394video_frame_t org_bytedeco_javacpp_dc1394_dc1394video_frame_t2, @Cast({"dc1394bayer_method_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_deinterlace_stereo(@Cast({"uint8_t*"}) ByteBuffer byteBuffer, @Cast({"uint8_t*"}) ByteBuffer byteBuffer2, @Cast({"uint32_t"}) int i, @Cast({"uint32_t"}) int i2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_deinterlace_stereo(@Cast({"uint8_t*"}) BytePointer bytePointer, @Cast({"uint8_t*"}) BytePointer bytePointer2, @Cast({"uint32_t"}) int i, @Cast({"uint32_t"}) int i2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_deinterlace_stereo(@Cast({"uint8_t*"}) byte[] bArr, @Cast({"uint8_t*"}) byte[] bArr2, @Cast({"uint32_t"}) int i, @Cast({"uint32_t"}) int i2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_deinterlace_stereo_frames(dc1394video_frame_t org_bytedeco_javacpp_dc1394_dc1394video_frame_t, dc1394video_frame_t org_bytedeco_javacpp_dc1394_dc1394video_frame_t2, @Cast({"dc1394stereo_method_t"}) int i);

    @Cast({"const char*"})
    public static native BytePointer dc1394_error_get_string(@Cast({"dc1394error_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_external_trigger_get_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394trigger_mode_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_external_trigger_get_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394trigger_mode_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_external_trigger_get_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394trigger_mode_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_external_trigger_get_polarity(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394trigger_polarity_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_external_trigger_get_polarity(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394trigger_polarity_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_external_trigger_get_polarity(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394trigger_polarity_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_external_trigger_get_power(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394switch_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_external_trigger_get_power(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394switch_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_external_trigger_get_power(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394switch_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_external_trigger_get_source(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394trigger_source_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_external_trigger_get_source(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394trigger_source_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_external_trigger_get_source(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394trigger_source_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_external_trigger_get_supported_sources(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, dc1394trigger_sources_t org_bytedeco_javacpp_dc1394_dc1394trigger_sources_t);

    @Cast({"dc1394error_t"})
    public static native int dc1394_external_trigger_has_polarity(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394bool_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_external_trigger_has_polarity(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394bool_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_external_trigger_has_polarity(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394bool_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_external_trigger_set_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394trigger_mode_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_external_trigger_set_polarity(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394trigger_polarity_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_external_trigger_set_power(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394switch_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_external_trigger_set_source(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394trigger_source_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, dc1394feature_info_t org_bytedeco_javacpp_dc1394_dc1394feature_info_t);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_absolute_boundaries(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, FloatBuffer floatBuffer, FloatBuffer floatBuffer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_absolute_boundaries(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, FloatPointer floatPointer, FloatPointer floatPointer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_absolute_boundaries(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, float[] fArr, float[] fArr2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_absolute_control(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394switch_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_absolute_control(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394switch_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_absolute_control(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394switch_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_absolute_value(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, FloatBuffer floatBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_absolute_value(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, FloatPointer floatPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_absolute_value(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, float[] fArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_all(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, dc1394featureset_t org_bytedeco_javacpp_dc1394_dc1394featureset_t);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_boundaries(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"uint32_t*"}) IntBuffer intBuffer, @Cast({"uint32_t*"}) IntBuffer intBuffer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_boundaries(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"uint32_t*"}) IntPointer intPointer, @Cast({"uint32_t*"}) IntPointer intPointer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_boundaries(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"uint32_t*"}) int[] iArr, @Cast({"uint32_t*"}) int[] iArr2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394feature_mode_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394feature_mode_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394feature_mode_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_modes(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, dc1394feature_modes_t org_bytedeco_javacpp_dc1394_dc1394feature_modes_t);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_power(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394switch_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_power(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394switch_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_power(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394switch_t*"}) int[] iArr);

    @Cast({"const char*"})
    public static native BytePointer dc1394_feature_get_string(@Cast({"dc1394feature_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_value(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"uint32_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_value(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"uint32_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_get_value(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"uint32_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_has_absolute_control(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394bool_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_has_absolute_control(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394bool_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_has_absolute_control(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394bool_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_is_present(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394bool_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_is_present(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394bool_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_is_present(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394bool_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_is_readable(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394bool_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_is_readable(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394bool_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_is_readable(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394bool_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_is_switchable(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394bool_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_is_switchable(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394bool_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_is_switchable(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394bool_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_print(dc1394feature_info_t org_bytedeco_javacpp_dc1394_dc1394feature_info_t, @Cast({"FILE*"}) Pointer pointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_print_all(dc1394featureset_t org_bytedeco_javacpp_dc1394_dc1394featureset_t, @Cast({"FILE*"}) Pointer pointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_set_absolute_control(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394switch_t"}) int i2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_set_absolute_value(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, float f);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_set_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394feature_mode_t"}) int i2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_set_power(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"dc1394switch_t"}) int i2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_set_value(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394feature_t"}) int i, @Cast({"uint32_t"}) int i2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_temperature_get_value(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) IntBuffer intBuffer, @Cast({"uint32_t*"}) IntBuffer intBuffer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_temperature_get_value(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) IntPointer intPointer, @Cast({"uint32_t*"}) IntPointer intPointer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_temperature_get_value(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) int[] iArr, @Cast({"uint32_t*"}) int[] iArr2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_temperature_set_value(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_whitebalance_get_value(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) IntBuffer intBuffer, @Cast({"uint32_t*"}) IntBuffer intBuffer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_whitebalance_get_value(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) IntPointer intPointer, @Cast({"uint32_t*"}) IntPointer intPointer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_whitebalance_get_value(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) int[] iArr, @Cast({"uint32_t*"}) int[] iArr2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_whitebalance_set_value(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t"}) int i, @Cast({"uint32_t"}) int i2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_whiteshading_get_value(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) IntBuffer intBuffer, @Cast({"uint32_t*"}) IntBuffer intBuffer2, @Cast({"uint32_t*"}) IntBuffer intBuffer3);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_whiteshading_get_value(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) IntPointer intPointer, @Cast({"uint32_t*"}) IntPointer intPointer2, @Cast({"uint32_t*"}) IntPointer intPointer3);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_whiteshading_get_value(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) int[] iArr, @Cast({"uint32_t*"}) int[] iArr2, @Cast({"uint32_t*"}) int[] iArr3);

    @Cast({"dc1394error_t"})
    public static native int dc1394_feature_whiteshading_set_value(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t"}) int i, @Cast({"uint32_t"}) int i2, @Cast({"uint32_t"}) int i3);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_color_coding(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"dc1394color_coding_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_color_coding(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"dc1394color_coding_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_color_coding(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"dc1394color_coding_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_color_codings(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, dc1394color_codings_t org_bytedeco_javacpp_dc1394_dc1394color_codings_t);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_color_filter(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"dc1394color_filter_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_color_filter(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"dc1394color_filter_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_color_filter(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"dc1394color_filter_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_data_depth(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_data_depth(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_data_depth(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_frame_interval(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, FloatBuffer floatBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_frame_interval(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, FloatPointer floatPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_frame_interval(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, float[] fArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_image_position(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntBuffer intBuffer, @Cast({"uint32_t*"}) IntBuffer intBuffer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_image_position(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntPointer intPointer, @Cast({"uint32_t*"}) IntPointer intPointer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_image_position(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) int[] iArr, @Cast({"uint32_t*"}) int[] iArr2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_image_size(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntBuffer intBuffer, @Cast({"uint32_t*"}) IntBuffer intBuffer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_image_size(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntPointer intPointer, @Cast({"uint32_t*"}) IntPointer intPointer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_image_size(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) int[] iArr, @Cast({"uint32_t*"}) int[] iArr2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_max_image_size(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntBuffer intBuffer, @Cast({"uint32_t*"}) IntBuffer intBuffer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_max_image_size(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntPointer intPointer, @Cast({"uint32_t*"}) IntPointer intPointer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_max_image_size(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) int[] iArr, @Cast({"uint32_t*"}) int[] iArr2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_mode_info(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, dc1394format7mode_t org_bytedeco_javacpp_dc1394_dc1394format7mode_t);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_modeset(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, dc1394format7modeset_t org_bytedeco_javacpp_dc1394_dc1394format7modeset_t);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_packet_parameters(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntBuffer intBuffer, @Cast({"uint32_t*"}) IntBuffer intBuffer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_packet_parameters(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntPointer intPointer, @Cast({"uint32_t*"}) IntPointer intPointer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_packet_parameters(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) int[] iArr, @Cast({"uint32_t*"}) int[] iArr2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_packet_size(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_packet_size(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_packet_size(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_packets_per_frame(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_packets_per_frame(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_packets_per_frame(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_pixel_number(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_pixel_number(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_pixel_number(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_recommended_packet_size(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_recommended_packet_size(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_recommended_packet_size(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_roi(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"dc1394color_coding_t*"}) IntBuffer intBuffer, @Cast({"uint32_t*"}) IntBuffer intBuffer2, @Cast({"uint32_t*"}) IntBuffer intBuffer3, @Cast({"uint32_t*"}) IntBuffer intBuffer4, @Cast({"uint32_t*"}) IntBuffer intBuffer5, @Cast({"uint32_t*"}) IntBuffer intBuffer6);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_roi(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"dc1394color_coding_t*"}) IntPointer intPointer, @Cast({"uint32_t*"}) IntPointer intPointer2, @Cast({"uint32_t*"}) IntPointer intPointer3, @Cast({"uint32_t*"}) IntPointer intPointer4, @Cast({"uint32_t*"}) IntPointer intPointer5, @Cast({"uint32_t*"}) IntPointer intPointer6);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_roi(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"dc1394color_coding_t*"}) int[] iArr, @Cast({"uint32_t*"}) int[] iArr2, @Cast({"uint32_t*"}) int[] iArr3, @Cast({"uint32_t*"}) int[] iArr4, @Cast({"uint32_t*"}) int[] iArr5, @Cast({"uint32_t*"}) int[] iArr6);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_total_bytes(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint64_t*"}) LongBuffer longBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_total_bytes(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint64_t*"}) LongPointer longPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_total_bytes(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint64_t*"}) long[] jArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_unit_position(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntBuffer intBuffer, @Cast({"uint32_t*"}) IntBuffer intBuffer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_unit_position(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntPointer intPointer, @Cast({"uint32_t*"}) IntPointer intPointer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_unit_position(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) int[] iArr, @Cast({"uint32_t*"}) int[] iArr2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_unit_size(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntBuffer intBuffer, @Cast({"uint32_t*"}) IntBuffer intBuffer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_unit_size(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) IntPointer intPointer, @Cast({"uint32_t*"}) IntPointer intPointer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_get_unit_size(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t*"}) int[] iArr, @Cast({"uint32_t*"}) int[] iArr2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_set_color_coding(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"dc1394color_coding_t"}) int i2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_set_image_position(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t"}) int i2, @Cast({"uint32_t"}) int i3);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_set_image_size(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t"}) int i2, @Cast({"uint32_t"}) int i3);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_set_packet_size(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"uint32_t"}) int i2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_format7_set_roi(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"dc1394color_coding_t"}) int i2, int i3, int i4, int i5, int i6, int i7);

    @Cast({"dc1394error_t"})
    public static native int dc1394_framerate_as_float(@Cast({"dc1394framerate_t"}) int i, FloatBuffer floatBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_framerate_as_float(@Cast({"dc1394framerate_t"}) int i, FloatPointer floatPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_framerate_as_float(@Cast({"dc1394framerate_t"}) int i, float[] fArr);

    public static native void dc1394_free(dc1394_t org_bytedeco_javacpp_dc1394_dc1394_t);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_PIO_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_PIO_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_PIO_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_SIO_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_SIO_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_SIO_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_absolute_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"unsigned int"}) int i, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_absolute_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"unsigned int"}) int i, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_absolute_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"unsigned int"}) int i, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_adv_control_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_adv_control_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_adv_control_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_adv_control_registers(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntBuffer intBuffer, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_adv_control_registers(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntPointer intPointer, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_adv_control_registers(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) int[] iArr, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_color_coding_bit_size(@Cast({"dc1394color_coding_t"}) int i, @Cast({"uint32_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_color_coding_bit_size(@Cast({"dc1394color_coding_t"}) int i, @Cast({"uint32_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_color_coding_bit_size(@Cast({"dc1394color_coding_t"}) int i, @Cast({"uint32_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_color_coding_data_depth(@Cast({"dc1394color_coding_t"}) int i, @Cast({"uint32_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_color_coding_data_depth(@Cast({"dc1394color_coding_t"}) int i, @Cast({"uint32_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_color_coding_data_depth(@Cast({"dc1394color_coding_t"}) int i, @Cast({"uint32_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_color_coding_from_video_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"dc1394color_coding_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_color_coding_from_video_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"dc1394color_coding_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_color_coding_from_video_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, @Cast({"dc1394color_coding_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_control_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_control_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_control_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_control_registers(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntBuffer intBuffer, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_control_registers(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntPointer intPointer, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_control_registers(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) int[] iArr, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_format7_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"unsigned int"}) int i, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_format7_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"unsigned int"}) int i, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_format7_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"unsigned int"}) int i, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_image_size_from_video_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t"}) int i, @Cast({"uint32_t*"}) IntBuffer intBuffer, @Cast({"uint32_t*"}) IntBuffer intBuffer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_image_size_from_video_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t"}) int i, @Cast({"uint32_t*"}) IntPointer intPointer, @Cast({"uint32_t*"}) IntPointer intPointer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_image_size_from_video_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t"}) int i, @Cast({"uint32_t*"}) int[] iArr, @Cast({"uint32_t*"}) int[] iArr2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_registers(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntBuffer intBuffer, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_registers(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntPointer intPointer, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_registers(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) int[] iArr, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_strobe_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_strobe_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_get_strobe_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_is_color(@Cast({"dc1394color_coding_t"}) int i, @Cast({"dc1394bool_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_is_color(@Cast({"dc1394color_coding_t"}) int i, @Cast({"dc1394bool_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_is_color(@Cast({"dc1394color_coding_t"}) int i, @Cast({"dc1394bool_t*"}) int[] iArr);

    @Cast({"dc1394bool_t"})
    public static native int dc1394_is_same_camera(@ByVal dc1394camera_id_t org_bytedeco_javacpp_dc1394_dc1394camera_id_t, @ByVal dc1394camera_id_t org_bytedeco_javacpp_dc1394_dc1394camera_id_t2);

    @Cast({"dc1394bool_t"})
    public static native int dc1394_is_video_mode_scalable(@Cast({"dc1394video_mode_t"}) int i);

    @Cast({"dc1394bool_t"})
    public static native int dc1394_is_video_mode_still_image(@Cast({"dc1394video_mode_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_iso_allocate_bandwidth(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_iso_allocate_channel(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_iso_allocate_channel(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_iso_allocate_channel(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_iso_release_all(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t);

    @Cast({"dc1394error_t"})
    public static native int dc1394_iso_release_bandwidth(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_iso_release_channel(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_iso_set_persist(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t);

    public static native void dc1394_log_debug(String str);

    public static native void dc1394_log_debug(@Cast({"const char*"}) BytePointer bytePointer);

    public static native void dc1394_log_error(String str);

    public static native void dc1394_log_error(@Cast({"const char*"}) BytePointer bytePointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_log_register_handler(@Cast({"dc1394log_t"}) int i, Log_handler_int_BytePointer_Pointer log_handler_int_BytePointer_Pointer, Pointer pointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_log_register_handler(@Cast({"dc1394log_t"}) int i, Log_handler_int_String_Pointer log_handler_int_String_Pointer, Pointer pointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_log_set_default_handler(@Cast({"dc1394log_t"}) int i);

    public static native void dc1394_log_warning(String str);

    public static native void dc1394_log_warning(@Cast({"const char*"}) BytePointer bytePointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_memory_busy(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394bool_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_memory_busy(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394bool_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_memory_busy(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394bool_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_memory_load(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_memory_save(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t"}) int i);

    public static native dc1394_t dc1394_new();

    @Cast({"dc1394error_t"})
    public static native int dc1394_pio_get(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_pio_get(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_pio_get(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_pio_set(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_read_cycle_timer(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) IntBuffer intBuffer, @Cast({"uint64_t*"}) LongBuffer longBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_read_cycle_timer(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) IntPointer intPointer, @Cast({"uint64_t*"}) LongPointer longPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_read_cycle_timer(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) int[] iArr, @Cast({"uint64_t*"}) long[] jArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_reset_bus(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t);

    @Cast({"dc1394error_t"})
    public static native int dc1394_set_PIO_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_set_SIO_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_set_absolute_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"unsigned int"}) int i, @Cast({"uint64_t"}) long j, @Cast({"uint32_t"}) int i2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_set_adv_control_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_set_adv_control_registers(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"const uint32_t*"}) IntBuffer intBuffer, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_set_adv_control_registers(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"const uint32_t*"}) IntPointer intPointer, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_set_adv_control_registers(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"const uint32_t*"}) int[] iArr, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_set_control_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_set_control_registers(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"const uint32_t*"}) IntBuffer intBuffer, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_set_control_registers(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"const uint32_t*"}) IntPointer intPointer, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_set_control_registers(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"const uint32_t*"}) int[] iArr, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_set_format7_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"unsigned int"}) int i, @Cast({"uint64_t"}) long j, @Cast({"uint32_t"}) int i2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_set_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_set_registers(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"const uint32_t*"}) IntBuffer intBuffer, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_set_registers(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"const uint32_t*"}) IntPointer intPointer, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_set_registers(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"const uint32_t*"}) int[] iArr, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_set_strobe_register(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint64_t"}) long j, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_software_trigger_get_power(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394switch_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_software_trigger_get_power(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394switch_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_software_trigger_get_power(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394switch_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_software_trigger_set_power(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394switch_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_bandwidth_usage(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_bandwidth_usage(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_bandwidth_usage(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_data_depth(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_data_depth(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_data_depth(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_framerate(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394framerate_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_framerate(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394framerate_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_framerate(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394framerate_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_iso_channel(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_iso_channel(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_iso_channel(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_iso_speed(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394speed_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_iso_speed(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394speed_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_iso_speed(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394speed_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_multi_shot(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394bool_t*"}) IntBuffer intBuffer, @Cast({"uint32_t*"}) IntBuffer intBuffer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_multi_shot(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394bool_t*"}) IntPointer intPointer, @Cast({"uint32_t*"}) IntPointer intPointer2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_multi_shot(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394bool_t*"}) int[] iArr, @Cast({"uint32_t*"}) int[] iArr2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_one_shot(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394bool_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_one_shot(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394bool_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_one_shot(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394bool_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_operation_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394operation_mode_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_operation_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394operation_mode_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_operation_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394operation_mode_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_supported_framerates(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i, dc1394framerates_t org_bytedeco_javacpp_dc1394_dc1394framerates_t);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_supported_modes(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, dc1394video_modes_t org_bytedeco_javacpp_dc1394_dc1394video_modes_t);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_transmission(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394switch_t*"}) IntBuffer intBuffer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_transmission(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394switch_t*"}) IntPointer intPointer);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_get_transmission(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394switch_t*"}) int[] iArr);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_set_framerate(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394framerate_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_set_iso_channel(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_set_iso_speed(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394speed_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_set_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394video_mode_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_set_multi_shot(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"uint32_t"}) int i, @Cast({"dc1394switch_t"}) int i2);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_set_one_shot(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394switch_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_set_operation_mode(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394operation_mode_t"}) int i);

    @Cast({"dc1394error_t"})
    public static native int dc1394_video_set_transmission(dc1394camera_t org_bytedeco_javacpp_dc1394_dc1394camera_t, @Cast({"dc1394switch_t"}) int i);

    static {
        Loader.load();
    }
}
