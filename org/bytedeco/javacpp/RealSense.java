package org.bytedeco.javacpp;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
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
import org.bytedeco.javacpp.annotation.StdString;

public class RealSense extends org.bytedeco.javacpp.presets.RealSense {
    public static final int RS_API_MAJOR_VERSION = 1;
    public static final int RS_API_MINOR_VERSION = 9;
    public static final int RS_API_PATCH_VERSION = 6;
    public static final int RS_API_VERSION = 10906;
    public static final String RS_API_VERSION_STR = "1.9.6";
    public static final int RS_BLOB_TYPE_COUNT = 1;
    public static final int RS_BLOB_TYPE_MAX_ENUM = Integer.MAX_VALUE;
    public static final int RS_BLOB_TYPE_MOTION_MODULE_FIRMWARE_UPDATE = 0;
    public static final int RS_CAMERA_INFO_ADAPTER_BOARD_FIRMWARE_VERSION = 3;
    public static final int RS_CAMERA_INFO_CAMERA_FIRMWARE_VERSION = 2;
    public static final int RS_CAMERA_INFO_COUNT = 5;
    public static final int RS_CAMERA_INFO_DEVICE_NAME = 0;
    public static final int RS_CAMERA_INFO_DEVICE_SERIAL_NUMBER = 1;
    public static final int RS_CAMERA_INFO_MAX_ENUM = Integer.MAX_VALUE;
    public static final int RS_CAMERA_INFO_MOTION_MODULE_FIRMWARE_VERSION = 4;
    public static final int RS_CAPABILITIES_ADAPTER_BOARD = 7;
    public static final int RS_CAPABILITIES_COLOR = 1;
    public static final int RS_CAPABILITIES_COUNT = 9;
    public static final int RS_CAPABILITIES_DEPTH = 0;
    public static final int RS_CAPABILITIES_ENUMERATION = 8;
    public static final int RS_CAPABILITIES_FISH_EYE = 4;
    public static final int RS_CAPABILITIES_INFRARED = 2;
    public static final int RS_CAPABILITIES_INFRARED2 = 3;
    public static final int RS_CAPABILITIES_MAX_ENUM = Integer.MAX_VALUE;
    public static final int RS_CAPABILITIES_MOTION_EVENTS = 5;
    public static final int RS_CAPABILITIES_MOTION_MODULE_FW_UPDATE = 6;
    public static final int RS_DISTORTION_COUNT = 4;
    public static final int RS_DISTORTION_FTHETA = 3;
    public static final int RS_DISTORTION_INVERSE_BROWN_CONRADY = 2;
    public static final int RS_DISTORTION_MAX_ENUM = Integer.MAX_VALUE;
    public static final int RS_DISTORTION_MODIFIED_BROWN_CONRADY = 1;
    public static final int RS_DISTORTION_NONE = 0;
    public static final int RS_EVENT_G0_SYNC = 4;
    public static final int RS_EVENT_G1_SYNC = 5;
    public static final int RS_EVENT_G2_SYNC = 6;
    public static final int RS_EVENT_IMU_ACCEL = 0;
    public static final int RS_EVENT_IMU_DEPTH_CAM = 2;
    public static final int RS_EVENT_IMU_GYRO = 1;
    public static final int RS_EVENT_IMU_MOTION_CAM = 3;
    public static final int RS_EVENT_SOURCE_COUNT = 7;
    public static final int RS_EVENT_SOURCE_MAX_ENUM = Integer.MAX_VALUE;
    public static final int RS_FORMAT_ANY = 0;
    public static final int RS_FORMAT_BGR8 = 6;
    public static final int RS_FORMAT_BGRA8 = 8;
    public static final int RS_FORMAT_COUNT = 14;
    public static final int RS_FORMAT_DISPARITY16 = 2;
    public static final int RS_FORMAT_MAX_ENUM = Integer.MAX_VALUE;
    public static final int RS_FORMAT_RAW10 = 11;
    public static final int RS_FORMAT_RAW16 = 12;
    public static final int RS_FORMAT_RAW8 = 13;
    public static final int RS_FORMAT_RGB8 = 5;
    public static final int RS_FORMAT_RGBA8 = 7;
    public static final int RS_FORMAT_XYZ32F = 3;
    public static final int RS_FORMAT_Y16 = 10;
    public static final int RS_FORMAT_Y8 = 9;
    public static final int RS_FORMAT_YUYV = 4;
    public static final int RS_FORMAT_Z16 = 1;
    public static final int RS_IVCAM_PRESET_BACKGROUND_SEGMENTATION = 2;
    public static final int RS_IVCAM_PRESET_DEFAULT = 8;
    public static final int RS_IVCAM_PRESET_FACE_ANALYTICS = 5;
    public static final int RS_IVCAM_PRESET_FACE_LOGIN = 6;
    public static final int RS_IVCAM_PRESET_GESTURE_RECOGNITION = 3;
    public static final int RS_IVCAM_PRESET_GR_CURSOR = 7;
    public static final int RS_IVCAM_PRESET_IR_ONLY = 10;
    public static final int RS_IVCAM_PRESET_LONG_RANGE = 1;
    public static final int RS_IVCAM_PRESET_MAX_ENUM = Integer.MAX_VALUE;
    public static final int RS_IVCAM_PRESET_MID_RANGE = 9;
    public static final int RS_IVCAM_PRESET_OBJECT_SCANNING = 4;
    public static final int RS_IVCAM_PRESET_SHORT_RANGE = 0;
    public static final int RS_LOG_SEVERITY_DEBUG = 0;
    public static final int RS_LOG_SEVERITY_ERROR = 3;
    public static final int RS_LOG_SEVERITY_FATAL = 4;
    public static final int RS_LOG_SEVERITY_INFO = 1;
    public static final int RS_LOG_SEVERITY_MAX_ENUM = Integer.MAX_VALUE;
    public static final int RS_LOG_SEVERITY_NONE = 5;
    public static final int RS_LOG_SEVERITY_WARN = 2;
    public static final int RS_OPTION_COLOR_BACKLIGHT_COMPENSATION = 0;
    public static final int RS_OPTION_COLOR_BRIGHTNESS = 1;
    public static final int RS_OPTION_COLOR_CONTRAST = 2;
    public static final int RS_OPTION_COLOR_ENABLE_AUTO_EXPOSURE = 10;
    public static final int RS_OPTION_COLOR_ENABLE_AUTO_WHITE_BALANCE = 11;
    public static final int RS_OPTION_COLOR_EXPOSURE = 3;
    public static final int RS_OPTION_COLOR_GAIN = 4;
    public static final int RS_OPTION_COLOR_GAMMA = 5;
    public static final int RS_OPTION_COLOR_HUE = 6;
    public static final int RS_OPTION_COLOR_SATURATION = 7;
    public static final int RS_OPTION_COLOR_SHARPNESS = 8;
    public static final int RS_OPTION_COLOR_WHITE_BALANCE = 9;
    public static final int RS_OPTION_COUNT = 67;
    public static final int RS_OPTION_F200_ACCURACY = 13;
    public static final int RS_OPTION_F200_CONFIDENCE_THRESHOLD = 16;
    public static final int RS_OPTION_F200_DYNAMIC_FPS = 17;
    public static final int RS_OPTION_F200_FILTER_OPTION = 15;
    public static final int RS_OPTION_F200_LASER_POWER = 12;
    public static final int RS_OPTION_F200_MOTION_RANGE = 14;
    public static final int RS_OPTION_FISHEYE_AUTO_EXPOSURE_ANTIFLICKER_RATE = 62;
    public static final int RS_OPTION_FISHEYE_AUTO_EXPOSURE_MODE = 61;
    public static final int RS_OPTION_FISHEYE_AUTO_EXPOSURE_PIXEL_SAMPLE_RATE = 63;
    public static final int RS_OPTION_FISHEYE_AUTO_EXPOSURE_SKIP_FRAMES = 64;
    public static final int RS_OPTION_FISHEYE_ENABLE_AUTO_EXPOSURE = 60;
    public static final int RS_OPTION_FISHEYE_EXPOSURE = 56;
    public static final int RS_OPTION_FISHEYE_EXTERNAL_TRIGGER = 59;
    public static final int RS_OPTION_FISHEYE_GAIN = 57;
    public static final int RS_OPTION_FISHEYE_STROBE = 58;
    public static final int RS_OPTION_FRAMES_QUEUE_SIZE = 65;
    public static final int RS_OPTION_HARDWARE_LOGGER_ENABLED = 66;
    public static final int RS_OPTION_MAX_ENUM = Integer.MAX_VALUE;
    public static final int RS_OPTION_R200_AUTO_EXPOSURE_BOTTOM_EDGE = 43;
    public static final int RS_OPTION_R200_AUTO_EXPOSURE_BRIGHT_RATIO_SET_POINT = 38;
    public static final int RS_OPTION_R200_AUTO_EXPOSURE_KP_DARK_THRESHOLD = 41;
    public static final int RS_OPTION_R200_AUTO_EXPOSURE_KP_EXPOSURE = 40;
    public static final int RS_OPTION_R200_AUTO_EXPOSURE_KP_GAIN = 39;
    public static final int RS_OPTION_R200_AUTO_EXPOSURE_LEFT_EDGE = 44;
    public static final int RS_OPTION_R200_AUTO_EXPOSURE_MEAN_INTENSITY_SET_POINT = 37;
    public static final int RS_OPTION_R200_AUTO_EXPOSURE_RIGHT_EDGE = 45;
    public static final int RS_OPTION_R200_AUTO_EXPOSURE_TOP_EDGE = 42;
    public static final int RS_OPTION_R200_DEPTH_CLAMP_MAX = 34;
    public static final int RS_OPTION_R200_DEPTH_CLAMP_MIN = 33;
    public static final int RS_OPTION_R200_DEPTH_CONTROL_ESTIMATE_MEDIAN_DECREMENT = 46;
    public static final int RS_OPTION_R200_DEPTH_CONTROL_ESTIMATE_MEDIAN_INCREMENT = 47;
    public static final int RS_OPTION_R200_DEPTH_CONTROL_LR_THRESHOLD = 55;
    public static final int RS_OPTION_R200_DEPTH_CONTROL_MEDIAN_THRESHOLD = 48;
    public static final int RS_OPTION_R200_DEPTH_CONTROL_NEIGHBOR_THRESHOLD = 54;
    public static final int RS_OPTION_R200_DEPTH_CONTROL_SCORE_MAXIMUM_THRESHOLD = 50;
    public static final int RS_OPTION_R200_DEPTH_CONTROL_SCORE_MINIMUM_THRESHOLD = 49;
    public static final int RS_OPTION_R200_DEPTH_CONTROL_SECOND_PEAK_THRESHOLD = 53;
    public static final int RS_OPTION_R200_DEPTH_CONTROL_TEXTURE_COUNT_THRESHOLD = 51;
    public static final int RS_OPTION_R200_DEPTH_CONTROL_TEXTURE_DIFFERENCE_THRESHOLD = 52;
    public static final int RS_OPTION_R200_DEPTH_UNITS = 32;
    public static final int RS_OPTION_R200_DISPARITY_MULTIPLIER = 35;
    public static final int RS_OPTION_R200_DISPARITY_SHIFT = 36;
    public static final int RS_OPTION_R200_EMITTER_ENABLED = 31;
    public static final int RS_OPTION_R200_LR_AUTO_EXPOSURE_ENABLED = 28;
    public static final int RS_OPTION_R200_LR_EXPOSURE = 30;
    public static final int RS_OPTION_R200_LR_GAIN = 29;
    public static final int RS_OPTION_SR300_AUTO_RANGE_ENABLE_LASER = 19;
    public static final int RS_OPTION_SR300_AUTO_RANGE_ENABLE_MOTION_VERSUS_RANGE = 18;
    public static final int RS_OPTION_SR300_AUTO_RANGE_LOWER_THRESHOLD = 27;
    public static final int RS_OPTION_SR300_AUTO_RANGE_MAX_LASER = 24;
    public static final int RS_OPTION_SR300_AUTO_RANGE_MAX_MOTION_VERSUS_RANGE = 21;
    public static final int RS_OPTION_SR300_AUTO_RANGE_MIN_LASER = 23;
    public static final int RS_OPTION_SR300_AUTO_RANGE_MIN_MOTION_VERSUS_RANGE = 20;
    public static final int RS_OPTION_SR300_AUTO_RANGE_START_LASER = 25;
    public static final int RS_OPTION_SR300_AUTO_RANGE_START_MOTION_VERSUS_RANGE = 22;
    public static final int RS_OPTION_SR300_AUTO_RANGE_UPPER_THRESHOLD = 26;
    public static final int RS_OUTPUT_BUFFER_FORMAT_CONTINOUS = 0;
    public static final int RS_OUTPUT_BUFFER_FORMAT_COUNT = 2;
    public static final int RS_OUTPUT_BUFFER_FORMAT_ENUM = Integer.MAX_VALUE;
    public static final int RS_OUTPUT_BUFFER_FORMAT_NATIVE = 1;
    public static final int RS_PRESET_BEST_QUALITY = 0;
    public static final int RS_PRESET_COUNT = 3;
    public static final int RS_PRESET_HIGHEST_FRAMERATE = 2;
    public static final int RS_PRESET_LARGEST_IMAGE = 1;
    public static final int RS_PRESET_MAX_ENUM = Integer.MAX_VALUE;
    public static final int RS_SOURCE_ALL = 3;
    public static final int RS_SOURCE_COUNT = 4;
    public static final int RS_SOURCE_MAX_ENUM = Integer.MAX_VALUE;
    public static final int RS_SOURCE_MOTION_TRACKING = 2;
    public static final int RS_SOURCE_VIDEO = 1;
    public static final int RS_STREAM_COLOR = 1;
    public static final int RS_STREAM_COLOR_ALIGNED_TO_DEPTH = 7;
    public static final int RS_STREAM_COUNT = 12;
    public static final int RS_STREAM_DEPTH = 0;
    public static final int RS_STREAM_DEPTH_ALIGNED_TO_COLOR = 9;
    public static final int RS_STREAM_DEPTH_ALIGNED_TO_INFRARED2 = 11;
    public static final int RS_STREAM_DEPTH_ALIGNED_TO_RECTIFIED_COLOR = 10;
    public static final int RS_STREAM_FISHEYE = 4;
    public static final int RS_STREAM_INFRARED = 2;
    public static final int RS_STREAM_INFRARED2 = 3;
    public static final int RS_STREAM_INFRARED2_ALIGNED_TO_DEPTH = 8;
    public static final int RS_STREAM_MAX_ENUM = Integer.MAX_VALUE;
    public static final int RS_STREAM_POINTS = 5;
    public static final int RS_STREAM_RECTIFIED_COLOR = 6;
    public static final int RS_TIMESTAMP_DOMAIN_CAMERA = 0;
    public static final int RS_TIMESTAMP_DOMAIN_COUNT = 3;
    public static final int RS_TIMESTAMP_DOMAIN_MAX_ENUM = Integer.MAX_VALUE;
    public static final int RS_TIMESTAMP_DOMAIN_MICROCONTROLLER = 1;
    public static final int adapter_board = 7;
    public static final int adapter_board_firmware_version = 3;
    public static final byte all_sources = (byte) 3;
    public static final int any = 0;
    public static final int best_quality = 0;
    public static final int bgr8 = 6;
    public static final int bgra8 = 8;
    public static final int camera = 0;
    public static final int camera_firmware_version = 2;
    public static final int color = 1;
    public static final int color_aligned_to_depth = 7;
    public static final int color_backlight_compensation = 0;
    public static final int color_brightness = 1;
    public static final int color_contrast = 2;
    public static final int color_enable_auto_exposure = 10;
    public static final int color_enable_auto_white_balance = 11;
    public static final int color_exposure = 3;
    public static final int color_gain = 4;
    public static final int color_gamma = 5;
    public static final int color_hue = 6;
    public static final int color_saturation = 7;
    public static final int color_sharpness = 8;
    public static final int color_white_balance = 9;
    public static final int debug = 0;
    public static final int depth = 0;
    public static final int depth_aligned_to_color = 9;
    public static final int depth_aligned_to_infrared2 = 11;
    public static final int depth_aligned_to_rectified_color = 10;
    public static final int device_name = 0;
    public static final int disparity16 = 2;
    public static final int distortion_ftheta = 3;
    public static final int enumeration = 8;
    public static final int error = 3;
    public static final byte event_imu_accel = (byte) 0;
    public static final byte event_imu_depth_cam = (byte) 2;
    public static final byte event_imu_g0_sync = (byte) 4;
    public static final byte event_imu_g1_sync = (byte) 5;
    public static final byte event_imu_g2_sync = (byte) 6;
    public static final byte event_imu_gyro = (byte) 1;
    public static final byte event_imu_motion_cam = (byte) 3;
    public static final int f200_accuracy = 13;
    public static final int f200_confidence_threshold = 16;
    public static final int f200_dynamic_fps = 17;
    public static final int f200_filter_option = 15;
    public static final int f200_laser_power = 12;
    public static final int f200_motion_range = 14;
    public static final int fatal = 4;
    public static final int fish_eye = 4;
    public static final int fisheye = 4;
    public static final int fisheye_color_auto_exposure = 60;
    public static final int fisheye_color_auto_exposure_mode = 61;
    public static final int fisheye_color_auto_exposure_rate = 62;
    public static final int fisheye_color_auto_exposure_sample_rate = 63;
    public static final int fisheye_color_auto_exposure_skip_frames = 64;
    public static final int fisheye_exposure = 56;
    public static final int fisheye_external_trigger = 59;
    public static final int fisheye_gain = 57;
    public static final int fisheye_strobe = 58;
    public static final int frames_queue_size = 65;
    public static final int hardware_logger_enabled = 66;
    public static final int highest_framerate = 2;
    public static final int info = 1;
    public static final int infrared = 2;
    public static final int infrared2 = 3;
    public static final int infrared2_aligned_to_depth = 8;
    public static final int inverse_brown_conrady = 2;
    public static final int largest_image = 1;
    public static final int microcontroller = 1;
    public static final int modified_brown_conrady = 1;
    public static final byte motion_data = (byte) 2;
    public static final int motion_events = 5;
    public static final int motion_module_firmware_update = 0;
    public static final int motion_module_firmware_version = 4;
    public static final int motion_module_fw_update = 6;
    public static final int none = 0;
    public static final int output_buffer_continous = 0;
    public static final int output_buffer_native = 1;
    public static final int points = 5;
    public static final int r200_auto_exposure_bottom_edge = 43;
    public static final int r200_auto_exposure_bright_ratio_set_point = 38;
    public static final int r200_auto_exposure_kp_dark_threshold = 41;
    public static final int r200_auto_exposure_kp_exposure = 40;
    public static final int r200_auto_exposure_kp_gain = 39;
    public static final int r200_auto_exposure_left_edge = 44;
    public static final int r200_auto_exposure_mean_intensity_set_point = 37;
    public static final int r200_auto_exposure_right_edge = 45;
    public static final int r200_auto_exposure_top_edge = 42;
    public static final int r200_depth_clamp_max = 34;
    public static final int r200_depth_clamp_min = 33;
    public static final int r200_depth_control_estimate_median_decrement = 46;
    public static final int r200_depth_control_estimate_median_increment = 47;
    public static final int r200_depth_control_lr_threshold = 55;
    public static final int r200_depth_control_median_threshold = 48;
    public static final int r200_depth_control_neighbor_threshold = 54;
    public static final int r200_depth_control_score_maximum_threshold = 50;
    public static final int r200_depth_control_score_minimum_threshold = 49;
    public static final int r200_depth_control_second_peak_threshold = 53;
    public static final int r200_depth_control_texture_count_threshold = 51;
    public static final int r200_depth_control_texture_difference_threshold = 52;
    public static final int r200_depth_units = 32;
    public static final int r200_disparity_multiplier = 35;
    public static final int r200_disparity_shift = 36;
    public static final int r200_emitter_enabled = 31;
    public static final int r200_lr_auto_exposure_enabled = 28;
    public static final int r200_lr_exposure = 30;
    public static final int r200_lr_gain = 29;
    public static final int raw10 = 11;
    public static final int raw16 = 12;
    public static final int raw8 = 13;
    public static final int rectified_color = 6;
    public static final int rgb8 = 5;
    public static final int rgba8 = 7;
    public static final int serial_number = 1;
    public static final int sr300_auto_range_enable_laser = 19;
    public static final int sr300_auto_range_enable_motion_versus_range = 18;
    public static final int sr300_auto_range_lower_threshold = 27;
    public static final int sr300_auto_range_max_laser = 24;
    public static final int sr300_auto_range_max_motion_versus_range = 21;
    public static final int sr300_auto_range_min_laser = 23;
    public static final int sr300_auto_range_min_motion_versus_range = 20;
    public static final int sr300_auto_range_start_laser = 25;
    public static final int sr300_auto_range_start_motion_versus_range = 22;
    public static final int sr300_auto_range_upper_threshold = 26;
    public static final byte video = (byte) 1;
    public static final int warn = 2;
    public static final int xyz32f = 3;
    public static final int y16 = 10;
    public static final int y8 = 9;
    public static final int yuyv = 4;
    public static final int z16 = 1;

    @Namespace("rs")
    @NoOffset
    public static class context extends Pointer {
        private native void allocate();

        private native void allocate(rs_context org_bytedeco_javacpp_RealSense_rs_context);

        private native void allocateArray(long j);

        public native device get_device(int i);

        public native int get_device_count();

        static {
            Loader.load();
        }

        public context(Pointer p) {
            super(p);
        }

        public context(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public context position(long position) {
            return (context) super.position(position);
        }

        public context() {
            super((Pointer) null);
            allocate();
        }

        public context(rs_context handle) {
            super((Pointer) null);
            allocate(handle);
        }
    }

    @Namespace("rs")
    public static class device extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native void disable_motion_tracking();

        public native void disable_stream(@Cast({"rs::stream"}) int i);

        public native void enable_motion_tracking(@Cast({"std::function<void(rs::motion_data)>*"}) @ByVal Pointer pointer);

        public native void enable_motion_tracking(@Cast({"std::function<void(rs::motion_data)>*"}) @ByVal Pointer pointer, @Cast({"std::function<void(rs::timestamp_data)>*"}) @ByVal Pointer pointer2);

        public native void enable_stream(@Cast({"rs::stream"}) int i, @Cast({"rs::preset"}) int i2);

        public native void enable_stream(@Cast({"rs::stream"}) int i, int i2, int i3, @Cast({"rs::format"}) int i4, int i5);

        public native void enable_stream(@Cast({"rs::stream"}) int i, int i2, int i3, @Cast({"rs::format"}) int i4, int i5, @Cast({"rs::output_buffer_format"}) int i6);

        public native float get_depth_scale();

        @ByVal
        public native extrinsics get_extrinsics(@Cast({"rs::stream"}) int i, @Cast({"rs::stream"}) int i2);

        @Cast({"const char*"})
        public native BytePointer get_firmware_version();

        @Const
        public native Pointer get_frame_data(@Cast({"rs::stream"}) int i);

        @Cast({"unsigned long long"})
        public native long get_frame_number(@Cast({"rs::stream"}) int i);

        public native double get_frame_timestamp(@Cast({"rs::stream"}) int i);

        @Cast({"const char*"})
        public native BytePointer get_info(@Cast({"rs::camera_info"}) int i);

        @ByVal
        public native extrinsics get_motion_extrinsics_from(@Cast({"rs::stream"}) int i);

        @ByVal
        public native motion_intrinsics get_motion_intrinsics();

        @Cast({"const char*"})
        public native BytePointer get_name();

        public native double get_option(@Cast({"rs::option"}) int i);

        @Cast({"const char*"})
        public native BytePointer get_option_description(@Cast({"rs::option"}) int i);

        public native void get_option_range(@Cast({"rs::option"}) int i, @ByRef DoubleBuffer doubleBuffer, @ByRef DoubleBuffer doubleBuffer2, @ByRef DoubleBuffer doubleBuffer3);

        public native void get_option_range(@Cast({"rs::option"}) int i, @ByRef DoubleBuffer doubleBuffer, @ByRef DoubleBuffer doubleBuffer2, @ByRef DoubleBuffer doubleBuffer3, @ByRef DoubleBuffer doubleBuffer4);

        public native void get_option_range(@Cast({"rs::option"}) int i, @ByRef DoublePointer doublePointer, @ByRef DoublePointer doublePointer2, @ByRef DoublePointer doublePointer3);

        public native void get_option_range(@Cast({"rs::option"}) int i, @ByRef DoublePointer doublePointer, @ByRef DoublePointer doublePointer2, @ByRef DoublePointer doublePointer3, @ByRef DoublePointer doublePointer4);

        public native void get_option_range(@Cast({"rs::option"}) int i, @ByRef double[] dArr, @ByRef double[] dArr2, @ByRef double[] dArr3);

        public native void get_option_range(@Cast({"rs::option"}) int i, @ByRef double[] dArr, @ByRef double[] dArr2, @ByRef double[] dArr3, @ByRef double[] dArr4);

        public native void get_options(@Cast({"const rs::option*"}) IntBuffer intBuffer, @Cast({"size_t"}) long j, DoubleBuffer doubleBuffer);

        public native void get_options(@Cast({"const rs::option*"}) IntPointer intPointer, @Cast({"size_t"}) long j, DoublePointer doublePointer);

        public native void get_options(@Cast({"const rs::option*"}) int[] iArr, @Cast({"size_t"}) long j, double[] dArr);

        @Cast({"const char*"})
        public native BytePointer get_serial();

        @Cast({"rs::format"})
        public native int get_stream_format(@Cast({"rs::stream"}) int i);

        public native int get_stream_framerate(@Cast({"rs::stream"}) int i);

        public native int get_stream_height(@Cast({"rs::stream"}) int i);

        @ByVal
        public native intrinsics get_stream_intrinsics(@Cast({"rs::stream"}) int i);

        public native void get_stream_mode(@Cast({"rs::stream"}) int i, int i2, @ByRef IntBuffer intBuffer, @ByRef IntBuffer intBuffer2, @ByRef @Cast({"rs::format*"}) IntBuffer intBuffer3, @ByRef IntBuffer intBuffer4);

        public native void get_stream_mode(@Cast({"rs::stream"}) int i, int i2, @ByRef IntPointer intPointer, @ByRef IntPointer intPointer2, @ByRef @Cast({"rs::format*"}) IntPointer intPointer3, @ByRef IntPointer intPointer4);

        public native void get_stream_mode(@Cast({"rs::stream"}) int i, int i2, @ByRef int[] iArr, @ByRef int[] iArr2, @ByRef @Cast({"rs::format*"}) int[] iArr3, @ByRef int[] iArr4);

        public native int get_stream_mode_count(@Cast({"rs::stream"}) int i);

        public native int get_stream_width(@Cast({"rs::stream"}) int i);

        @Cast({"const char*"})
        public native BytePointer get_usb_port_id();

        public native int is_motion_tracking_active();

        @Cast({"bool"})
        public native boolean is_stream_enabled(@Cast({"rs::stream"}) int i);

        @Cast({"bool"})
        public native boolean is_streaming();

        @Cast({"bool"})
        public native boolean poll_for_frames();

        public native void send_blob_to_device(@Cast({"rs::blob_type"}) int i, Pointer pointer, int i2);

        public native void set_frame_callback(@Cast({"rs::stream"}) int i, @Cast({"std::function<void(rs::frame)>*"}) @ByVal Pointer pointer);

        public native void set_option(@Cast({"rs::option"}) int i, double d);

        public native void set_options(@Cast({"const rs::option*"}) IntBuffer intBuffer, @Cast({"size_t"}) long j, @Const DoubleBuffer doubleBuffer);

        public native void set_options(@Cast({"const rs::option*"}) IntPointer intPointer, @Cast({"size_t"}) long j, @Const DoublePointer doublePointer);

        public native void set_options(@Cast({"const rs::option*"}) int[] iArr, @Cast({"size_t"}) long j, @Const double[] dArr);

        public native void start();

        public native void start(@Cast({"rs::source"}) byte b);

        public native void stop();

        public native void stop(@Cast({"rs::source"}) byte b);

        @Cast({"bool"})
        public native boolean supports(@Cast({"rs::capabilities"}) int i);

        @Cast({"bool"})
        public native boolean supports_option(@Cast({"rs::option"}) int i);

        public native void wait_for_frames();

        static {
            Loader.load();
        }

        public device() {
            super((Pointer) null);
            allocate();
        }

        public device(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public device(Pointer p) {
            super(p);
        }

        public device position(long position) {
            return (device) super.position(position);
        }
    }

    @Namespace("rs")
    @NoOffset
    public static class error extends Pointer {
        private native void allocate(rs_error org_bytedeco_javacpp_RealSense_rs_error);

        public static native void handle(rs_error org_bytedeco_javacpp_RealSense_rs_error);

        @StdString
        public native BytePointer get_failed_args();

        @StdString
        public native BytePointer get_failed_function();

        static {
            Loader.load();
        }

        public error(Pointer p) {
            super(p);
        }

        public error(rs_error err) {
            super((Pointer) null);
            allocate(err);
        }
    }

    @Namespace("rs")
    public static class extrinsics extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"bool"})
        public native boolean is_identity();

        @ByVal
        public native float3 transform(@ByRef @Const float3 org_bytedeco_javacpp_RealSense_float3);

        static {
            Loader.load();
        }

        public extrinsics() {
            super((Pointer) null);
            allocate();
        }

        public extrinsics(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public extrinsics(Pointer p) {
            super(p);
        }

        public extrinsics position(long position) {
            return (extrinsics) super.position(position);
        }
    }

    @Namespace("rs")
    public static class float2 extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float m27x();

        public native float2 m28x(float f);

        public native float m29y();

        public native float2 m30y(float f);

        static {
            Loader.load();
        }

        public float2() {
            super((Pointer) null);
            allocate();
        }

        public float2(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public float2(Pointer p) {
            super(p);
        }

        public float2 position(long position) {
            return (float2) super.position(position);
        }
    }

    @Namespace("rs")
    public static class float3 extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float m31x();

        public native float3 m32x(float f);

        public native float m33y();

        public native float3 m34y(float f);

        public native float m35z();

        public native float3 m36z(float f);

        static {
            Loader.load();
        }

        public float3() {
            super((Pointer) null);
            allocate();
        }

        public float3(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public float3(Pointer p) {
            super(p);
        }

        public float3 position(long position) {
            return (float3) super.position(position);
        }
    }

    @Namespace("rs")
    @NoOffset
    public static class frame extends Pointer {
        private native void allocate();

        private native void allocate(@ByRef @Const frame org_bytedeco_javacpp_RealSense_frame);

        private native void allocate(rs_device org_bytedeco_javacpp_RealSense_rs_device, rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref);

        private native void allocateArray(long j);

        public native int get_bpp();

        @Const
        public native Pointer get_data();

        @Cast({"rs::format"})
        public native int get_format();

        @Cast({"unsigned long long"})
        public native long get_frame_number();

        @Cast({"rs::timestamp_domain"})
        public native int get_frame_timestamp_domain();

        public native int get_framerate();

        public native int get_height();

        @Cast({"rs::stream"})
        public native int get_stream_type();

        public native int get_stride();

        public native double get_timestamp();

        public native int get_width();

        @ByRef
        @Name({"operator ="})
        public native frame put(@ByVal frame org_bytedeco_javacpp_RealSense_frame);

        public native void swap(@ByRef frame org_bytedeco_javacpp_RealSense_frame);

        static {
            Loader.load();
        }

        public frame(Pointer p) {
            super(p);
        }

        public frame(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public frame position(long position) {
            return (frame) super.position(position);
        }

        public frame(@ByRef @Const frame arg0) {
            super((Pointer) null);
            allocate(arg0);
        }

        public frame() {
            super((Pointer) null);
            allocate();
        }

        public frame(rs_device device, rs_frame_ref frame_ref) {
            super((Pointer) null);
            allocate(device, frame_ref);
        }
    }

    @Namespace("rs")
    @NoOffset
    public static class frame_callback extends rs_frame_callback {
        private native void allocate(@Cast({"std::function<void(rs::frame)>*"}) @ByVal Pointer pointer);

        public native void on_frame(rs_device org_bytedeco_javacpp_RealSense_rs_device, rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref);

        public native void release();

        static {
            Loader.load();
        }

        public frame_callback(@Cast({"std::function<void(rs::frame)>*"}) @ByVal Pointer on_frame) {
            super((Pointer) null);
            allocate(on_frame);
        }
    }

    @Namespace("rs")
    public static class intrinsics extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @ByVal
        public native float3 deproject(@ByRef @Const float2 org_bytedeco_javacpp_RealSense_float2, float f);

        @ByVal
        public native float3 deproject_from_texcoord(@ByRef @Const float2 org_bytedeco_javacpp_RealSense_float2, float f);

        @Cast({"bool"})
        @Name({"operator =="})
        public native boolean equals(@ByRef @Const intrinsics org_bytedeco_javacpp_RealSense_intrinsics);

        public native float hfov();

        @Cast({"rs::distortion"})
        public native int model();

        @ByVal
        public native float2 pixel_to_texcoord(@ByRef @Const float2 org_bytedeco_javacpp_RealSense_float2);

        @ByVal
        public native float2 project(@ByRef @Const float3 org_bytedeco_javacpp_RealSense_float3);

        @ByVal
        public native float2 project_to_texcoord(@ByRef @Const float3 org_bytedeco_javacpp_RealSense_float3);

        @ByVal
        public native float2 texcoord_to_pixel(@ByRef @Const float2 org_bytedeco_javacpp_RealSense_float2);

        public native float vfov();

        static {
            Loader.load();
        }

        public intrinsics() {
            super((Pointer) null);
            allocate();
        }

        public intrinsics(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public intrinsics(Pointer p) {
            super(p);
        }

        public intrinsics position(long position) {
            return (intrinsics) super.position(position);
        }
    }

    @Namespace("rs")
    @NoOffset
    public static class motion_callback extends rs_motion_callback {
        private native void allocate(@Cast({"std::function<void(rs::motion_data)>*"}) @ByVal Pointer pointer);

        public native void on_event(@ByVal rs_motion_data org_bytedeco_javacpp_RealSense_rs_motion_data);

        public native void release();

        static {
            Loader.load();
        }

        public motion_callback(@Cast({"std::function<void(rs::motion_data)>*"}) @ByVal Pointer on_event) {
            super((Pointer) null);
            allocate(on_event);
        }
    }

    @Namespace("rs")
    public static class motion_data extends Pointer {
        private native void allocate();

        private native void allocate(@ByVal rs_motion_data org_bytedeco_javacpp_RealSense_rs_motion_data);

        private native void allocateArray(long j);

        static {
            Loader.load();
        }

        public motion_data(Pointer p) {
            super(p);
        }

        public motion_data(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public motion_data position(long position) {
            return (motion_data) super.position(position);
        }

        public motion_data(@ByVal rs_motion_data orig) {
            super((Pointer) null);
            allocate(orig);
        }

        public motion_data() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("rs")
    public static class motion_intrinsics extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        static {
            Loader.load();
        }

        public motion_intrinsics(Pointer p) {
            super(p);
        }

        public motion_intrinsics(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public motion_intrinsics position(long position) {
            return (motion_intrinsics) super.position(position);
        }

        public motion_intrinsics() {
            super((Pointer) null);
            allocate();
        }
    }

    public static class rs_context extends Pointer {
        public native rs_device get_device(int i);

        @Cast({"size_t"})
        public native long get_device_count();

        static {
            Loader.load();
        }

        public rs_context(Pointer p) {
            super(p);
        }
    }

    public static class rs_device extends Pointer {
        public native rs_frame_ref clone_frame(rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref);

        public native void disable_motion_tracking();

        public native void disable_stream(@Cast({"rs_stream"}) int i);

        public native void enable_motion_tracking();

        public native void enable_stream(@Cast({"rs_stream"}) int i, int i2, int i3, @Cast({"rs_format"}) int i4, int i5, @Cast({"rs_output_buffer_format"}) int i6);

        public native void enable_stream_preset(@Cast({"rs_stream"}) int i, @Cast({"rs_preset"}) int i2);

        @Cast({"const char*"})
        public native BytePointer get_camera_info(@Cast({"rs_camera_info"}) int i);

        public native float get_depth_scale();

        @Cast({"const char*"})
        public native BytePointer get_firmware_version();

        @ByVal
        public native rs_extrinsics get_motion_extrinsics_from(@Cast({"rs_stream"}) int i);

        @ByVal
        public native rs_motion_intrinsics get_motion_intrinsics();

        @Cast({"const char*"})
        public native BytePointer get_name();

        @Cast({"const char*"})
        public native BytePointer get_option_description(@Cast({"rs_option"}) int i);

        public native void get_option_range(@Cast({"rs_option"}) int i, @ByRef DoubleBuffer doubleBuffer, @ByRef DoubleBuffer doubleBuffer2, @ByRef DoubleBuffer doubleBuffer3, @ByRef DoubleBuffer doubleBuffer4);

        public native void get_option_range(@Cast({"rs_option"}) int i, @ByRef DoublePointer doublePointer, @ByRef DoublePointer doublePointer2, @ByRef DoublePointer doublePointer3, @ByRef DoublePointer doublePointer4);

        public native void get_option_range(@Cast({"rs_option"}) int i, @ByRef double[] dArr, @ByRef double[] dArr2, @ByRef double[] dArr3, @ByRef double[] dArr4);

        public native void get_options(@Cast({"const rs_option*"}) IntBuffer intBuffer, @Cast({"size_t"}) long j, DoubleBuffer doubleBuffer);

        public native void get_options(@Cast({"const rs_option*"}) IntPointer intPointer, @Cast({"size_t"}) long j, DoublePointer doublePointer);

        public native void get_options(@Cast({"const rs_option*"}) int[] iArr, @Cast({"size_t"}) long j, double[] dArr);

        @Cast({"const char*"})
        public native BytePointer get_serial();

        @ByRef
        @Const
        public native rs_stream_interface get_stream_interface(@Cast({"rs_stream"}) int i);

        @Cast({"const char*"})
        public native BytePointer get_usb_port_id();

        @Cast({"bool"})
        public native boolean is_capturing();

        public native int is_motion_tracking_active();

        @Cast({"bool"})
        public native boolean poll_all_streams();

        public native void release_frame(rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref);

        public native void set_motion_callback(On_event_rs_device_rs_motion_data_Pointer on_event_rs_device_rs_motion_data_Pointer, Pointer pointer);

        public native void set_motion_callback(rs_motion_callback org_bytedeco_javacpp_RealSense_rs_motion_callback);

        public native void set_options(@Cast({"const rs_option*"}) IntBuffer intBuffer, @Cast({"size_t"}) long j, @Const DoubleBuffer doubleBuffer);

        public native void set_options(@Cast({"const rs_option*"}) IntPointer intPointer, @Cast({"size_t"}) long j, @Const DoublePointer doublePointer);

        public native void set_options(@Cast({"const rs_option*"}) int[] iArr, @Cast({"size_t"}) long j, @Const double[] dArr);

        public native void set_stream_callback(@Cast({"rs_stream"}) int i, On_frame_rs_device_rs_frame_ref_Pointer on_frame_rs_device_rs_frame_ref_Pointer, Pointer pointer);

        public native void set_stream_callback(@Cast({"rs_stream"}) int i, rs_frame_callback org_bytedeco_javacpp_RealSense_rs_frame_callback);

        public native void set_timestamp_callback(On_event_rs_device_rs_timestamp_data_Pointer on_event_rs_device_rs_timestamp_data_Pointer, Pointer pointer);

        public native void set_timestamp_callback(rs_timestamp_callback org_bytedeco_javacpp_RealSense_rs_timestamp_callback);

        public native void start(@Cast({"rs_source"}) int i);

        public native void start_fw_logger(@Cast({"char"}) byte b, int i, @ByRef @Cast({"std::timed_mutex*"}) Pointer pointer);

        public native void stop(@Cast({"rs_source"}) int i);

        public native void stop_fw_logger();

        @Cast({"bool"})
        public native boolean supports(@Cast({"rs_capabilities"}) int i);

        @Cast({"bool"})
        public native boolean supports_option(@Cast({"rs_option"}) int i);

        public native void wait_all_streams();

        static {
            Loader.load();
        }

        public rs_device(Pointer p) {
            super(p);
        }
    }

    @Opaque
    public static class rs_error extends Pointer {
        public rs_error() {
            super((Pointer) null);
        }

        public rs_error(Pointer p) {
            super(p);
        }
    }

    public static class rs_extrinsics extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float rotation(int i);

        @MemberGetter
        public native FloatPointer rotation();

        public native rs_extrinsics rotation(int i, float f);

        public native float translation(int i);

        @MemberGetter
        public native FloatPointer translation();

        public native rs_extrinsics translation(int i, float f);

        static {
            Loader.load();
        }

        public rs_extrinsics() {
            super((Pointer) null);
            allocate();
        }

        public rs_extrinsics(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public rs_extrinsics(Pointer p) {
            super(p);
        }

        public rs_extrinsics position(long position) {
            return (rs_extrinsics) super.position(position);
        }
    }

    public static class rs_frame_ref extends Pointer {
        public native int get_frame_bpp();

        @Cast({"const uint8_t*"})
        public native BytePointer get_frame_data();

        @Cast({"rs_format"})
        public native int get_frame_format();

        public native int get_frame_framerate();

        public native int get_frame_height();

        @Cast({"unsigned long long"})
        public native long get_frame_number();

        public native int get_frame_stride();

        public native long get_frame_system_time();

        public native double get_frame_timestamp();

        @Cast({"rs_timestamp_domain"})
        public native int get_frame_timestamp_domain();

        public native int get_frame_width();

        @Cast({"rs_stream"})
        public native int get_stream_type();

        static {
            Loader.load();
        }

        public rs_frame_ref(Pointer p) {
            super(p);
        }
    }

    @Opaque
    public static class rs_frameset extends Pointer {
        public rs_frameset() {
            super((Pointer) null);
        }

        public rs_frameset(Pointer p) {
            super(p);
        }
    }

    public static class rs_intrinsics extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float coeffs(int i);

        @MemberGetter
        public native FloatPointer coeffs();

        public native rs_intrinsics coeffs(int i, float f);

        public native float fx();

        public native rs_intrinsics fx(float f);

        public native float fy();

        public native rs_intrinsics fy(float f);

        public native int height();

        public native rs_intrinsics height(int i);

        @Cast({"rs_distortion"})
        public native int model();

        public native rs_intrinsics model(int i);

        public native float ppx();

        public native rs_intrinsics ppx(float f);

        public native float ppy();

        public native rs_intrinsics ppy(float f);

        public native int width();

        public native rs_intrinsics width(int i);

        static {
            Loader.load();
        }

        public rs_intrinsics() {
            super((Pointer) null);
            allocate();
        }

        public rs_intrinsics(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public rs_intrinsics(Pointer p) {
            super(p);
        }

        public rs_intrinsics position(long position) {
            return (rs_intrinsics) super.position(position);
        }
    }

    public static class rs_motion_data extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float axes(int i);

        @MemberGetter
        public native FloatPointer axes();

        public native rs_motion_data axes(int i, float f);

        @Cast({"unsigned int"})
        public native int is_valid();

        public native rs_motion_data is_valid(int i);

        public native rs_motion_data timestamp_data(rs_timestamp_data org_bytedeco_javacpp_RealSense_rs_timestamp_data);

        @ByRef
        public native rs_timestamp_data timestamp_data();

        static {
            Loader.load();
        }

        public rs_motion_data() {
            super((Pointer) null);
            allocate();
        }

        public rs_motion_data(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public rs_motion_data(Pointer p) {
            super(p);
        }

        public rs_motion_data position(long position) {
            return (rs_motion_data) super.position(position);
        }
    }

    public static class rs_motion_device_intrinsic extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float bias_variances(int i);

        @MemberGetter
        public native FloatPointer bias_variances();

        public native rs_motion_device_intrinsic bias_variances(int i, float f);

        public native float data(int i, int i2);

        @MemberGetter
        @Cast({"float(* /*[3]*/ )[4]"})
        public native FloatPointer data();

        public native rs_motion_device_intrinsic data(int i, int i2, float f);

        public native float noise_variances(int i);

        @MemberGetter
        public native FloatPointer noise_variances();

        public native rs_motion_device_intrinsic noise_variances(int i, float f);

        static {
            Loader.load();
        }

        public rs_motion_device_intrinsic() {
            super((Pointer) null);
            allocate();
        }

        public rs_motion_device_intrinsic(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public rs_motion_device_intrinsic(Pointer p) {
            super(p);
        }

        public rs_motion_device_intrinsic position(long position) {
            return (rs_motion_device_intrinsic) super.position(position);
        }
    }

    public static class rs_motion_intrinsics extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @ByRef
        public native rs_motion_device_intrinsic acc();

        public native rs_motion_intrinsics acc(rs_motion_device_intrinsic org_bytedeco_javacpp_RealSense_rs_motion_device_intrinsic);

        @ByRef
        public native rs_motion_device_intrinsic gyro();

        public native rs_motion_intrinsics gyro(rs_motion_device_intrinsic org_bytedeco_javacpp_RealSense_rs_motion_device_intrinsic);

        static {
            Loader.load();
        }

        public rs_motion_intrinsics() {
            super((Pointer) null);
            allocate();
        }

        public rs_motion_intrinsics(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public rs_motion_intrinsics(Pointer p) {
            super(p);
        }

        public rs_motion_intrinsics position(long position) {
            return (rs_motion_intrinsics) super.position(position);
        }
    }

    public static class rs_stream_interface extends Pointer {
        public native float get_depth_scale();

        @ByVal
        public native rs_extrinsics get_extrinsics_to(@ByRef @Const rs_stream_interface org_bytedeco_javacpp_RealSense_rs_stream_interface);

        @Cast({"rs_format"})
        public native int get_format();

        public native int get_frame_bpp();

        @Cast({"const uint8_t*"})
        public native BytePointer get_frame_data();

        @Cast({"unsigned long long"})
        public native long get_frame_number();

        public native int get_frame_stride();

        public native long get_frame_system_time();

        public native double get_frame_timestamp();

        public native int get_framerate();

        @ByVal
        public native rs_intrinsics get_intrinsics();

        public native void get_mode(int i, IntBuffer intBuffer, IntBuffer intBuffer2, @Cast({"rs_format*"}) IntBuffer intBuffer3, IntBuffer intBuffer4);

        public native void get_mode(int i, IntPointer intPointer, IntPointer intPointer2, @Cast({"rs_format*"}) IntPointer intPointer3, IntPointer intPointer4);

        public native void get_mode(int i, int[] iArr, int[] iArr2, @Cast({"rs_format*"}) int[] iArr3, int[] iArr4);

        public native int get_mode_count();

        @ByVal
        public native rs_intrinsics get_rectified_intrinsics();

        @Cast({"rs_stream"})
        public native int get_stream_type();

        @Cast({"bool"})
        public native boolean is_enabled();

        static {
            Loader.load();
        }

        public rs_stream_interface(Pointer p) {
            super(p);
        }
    }

    public static class rs_timestamp_data extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned long long"})
        public native long frame_number();

        public native rs_timestamp_data frame_number(long j);

        @Cast({"rs_event_source"})
        public native int source_id();

        public native rs_timestamp_data source_id(int i);

        public native double timestamp();

        public native rs_timestamp_data timestamp(double d);

        static {
            Loader.load();
        }

        public rs_timestamp_data() {
            super((Pointer) null);
            allocate();
        }

        public rs_timestamp_data(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public rs_timestamp_data(Pointer p) {
            super(p);
        }

        public rs_timestamp_data position(long position) {
            return (rs_timestamp_data) super.position(position);
        }
    }

    @Namespace("rs")
    @NoOffset
    public static class timestamp_callback extends rs_timestamp_callback {
        private native void allocate(@Cast({"std::function<void(rs::timestamp_data)>*"}) @ByVal Pointer pointer);

        public native void on_event(@ByVal rs_timestamp_data org_bytedeco_javacpp_RealSense_rs_timestamp_data);

        public native void release();

        static {
            Loader.load();
        }

        public timestamp_callback(@Cast({"std::function<void(rs::timestamp_data)>*"}) @ByVal Pointer on_event) {
            super((Pointer) null);
            allocate(on_event);
        }
    }

    @Namespace("rs")
    public static class timestamp_data extends Pointer {
        private native void allocate();

        private native void allocate(@ByVal rs_timestamp_data org_bytedeco_javacpp_RealSense_rs_timestamp_data);

        private native void allocateArray(long j);

        static {
            Loader.load();
        }

        public timestamp_data(Pointer p) {
            super(p);
        }

        public timestamp_data(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public timestamp_data position(long position) {
            return (timestamp_data) super.position(position);
        }

        public timestamp_data(@ByVal rs_timestamp_data orig) {
            super((Pointer) null);
            allocate(orig);
        }

        public timestamp_data() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("rs")
    public static native void apply_depth_control_preset(device org_bytedeco_javacpp_RealSense_device, int i);

    @Namespace("rs")
    public static native void apply_ivcam_preset(device org_bytedeco_javacpp_RealSense_device, @Cast({"rs_ivcam_preset"}) int i);

    @Namespace("rs")
    public static native void log_to_console(@Cast({"rs::log_severity"}) int i);

    @Namespace("rs")
    public static native void log_to_file(@Cast({"rs::log_severity"}) int i, String str);

    @Namespace("rs")
    public static native void log_to_file(@Cast({"rs::log_severity"}) int i, @Cast({"const char*"}) BytePointer bytePointer);

    public static native void rs_apply_depth_control_preset(rs_device org_bytedeco_javacpp_RealSense_rs_device, int i);

    public static native void rs_apply_ivcam_preset(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_ivcam_preset"}) int i);

    @Cast({"const char*"})
    public static native BytePointer rs_blob_type_to_string(@Cast({"rs_blob_type"}) int i);

    @Cast({"const char*"})
    public static native BytePointer rs_camera_info_to_string(@Cast({"rs_camera_info"}) int i);

    @Cast({"const char*"})
    public static native BytePointer rs_capabilities_to_string(@Cast({"rs_capabilities"}) int i);

    public static native rs_context rs_create_context(int i, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native rs_context rs_create_context(int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_delete_context(rs_context org_bytedeco_javacpp_RealSense_rs_context, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_delete_context(rs_context org_bytedeco_javacpp_RealSense_rs_context, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_deproject_pixel_to_point(FloatBuffer floatBuffer, @Const rs_intrinsics org_bytedeco_javacpp_RealSense_rs_intrinsics, @Const FloatBuffer floatBuffer2, float f);

    public static native void rs_deproject_pixel_to_point(FloatPointer floatPointer, @Const rs_intrinsics org_bytedeco_javacpp_RealSense_rs_intrinsics, @Const FloatPointer floatPointer2, float f);

    public static native void rs_deproject_pixel_to_point(float[] fArr, @Const rs_intrinsics org_bytedeco_javacpp_RealSense_rs_intrinsics, @Const float[] fArr2, float f);

    public static native int rs_device_supports_option(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_option"}) int i, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native int rs_device_supports_option(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_option"}) int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_disable_motion_tracking(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_disable_motion_tracking(rs_device org_bytedeco_javacpp_RealSense_rs_device, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_disable_stream(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_disable_stream(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"const char*"})
    public static native BytePointer rs_distortion_to_string(@Cast({"rs_distortion"}) int i);

    public static native void rs_enable_motion_tracking(rs_device org_bytedeco_javacpp_RealSense_rs_device, On_motion_event_rs_device_rs_motion_data_Pointer on_motion_event_rs_device_rs_motion_data_Pointer, Pointer pointer, On_timestamp_event_rs_device_rs_timestamp_data_Pointer on_timestamp_event_rs_device_rs_timestamp_data_Pointer, Pointer pointer2, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_enable_motion_tracking(rs_device org_bytedeco_javacpp_RealSense_rs_device, On_motion_event_rs_device_rs_motion_data_Pointer on_motion_event_rs_device_rs_motion_data_Pointer, Pointer pointer, On_timestamp_event_rs_device_rs_timestamp_data_Pointer on_timestamp_event_rs_device_rs_timestamp_data_Pointer, Pointer pointer2, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_enable_motion_tracking_cpp(rs_device org_bytedeco_javacpp_RealSense_rs_device, rs_motion_callback org_bytedeco_javacpp_RealSense_rs_motion_callback, rs_timestamp_callback org_bytedeco_javacpp_RealSense_rs_timestamp_callback, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_enable_motion_tracking_cpp(rs_device org_bytedeco_javacpp_RealSense_rs_device, rs_motion_callback org_bytedeco_javacpp_RealSense_rs_motion_callback, rs_timestamp_callback org_bytedeco_javacpp_RealSense_rs_timestamp_callback, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_enable_stream(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, int i2, int i3, @Cast({"rs_format"}) int i4, int i5, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_enable_stream(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, int i2, int i3, @Cast({"rs_format"}) int i4, int i5, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_enable_stream_ex(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, int i2, int i3, @Cast({"rs_format"}) int i4, int i5, @Cast({"rs_output_buffer_format"}) int i6, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_enable_stream_ex(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, int i2, int i3, @Cast({"rs_format"}) int i4, int i5, @Cast({"rs_output_buffer_format"}) int i6, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_enable_stream_preset(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @Cast({"rs_preset"}) int i2, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_enable_stream_preset(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @Cast({"rs_preset"}) int i2, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"const char*"})
    public static native BytePointer rs_event_to_string(@Cast({"rs_event_source"}) int i);

    @Cast({"const char*"})
    public static native BytePointer rs_format_to_string(@Cast({"rs_format"}) int i);

    public static native void rs_free_error(rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native int rs_get_api_version(@Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native int rs_get_api_version(@ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native int rs_get_detached_frame_bpp(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native int rs_get_detached_frame_bpp(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Const
    public static native Pointer rs_get_detached_frame_data(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    @Const
    public static native Pointer rs_get_detached_frame_data(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"rs_format"})
    public static native int rs_get_detached_frame_format(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    @Cast({"rs_format"})
    public static native int rs_get_detached_frame_format(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native int rs_get_detached_frame_height(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native int rs_get_detached_frame_height(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"unsigned long long"})
    public static native long rs_get_detached_frame_number(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    @Cast({"unsigned long long"})
    public static native long rs_get_detached_frame_number(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"rs_stream"})
    public static native int rs_get_detached_frame_stream_type(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    @Cast({"rs_stream"})
    public static native int rs_get_detached_frame_stream_type(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native int rs_get_detached_frame_stride(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native int rs_get_detached_frame_stride(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native double rs_get_detached_frame_timestamp(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native double rs_get_detached_frame_timestamp(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"rs_timestamp_domain"})
    public static native int rs_get_detached_frame_timestamp_domain(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    @Cast({"rs_timestamp_domain"})
    public static native int rs_get_detached_frame_timestamp_domain(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native int rs_get_detached_frame_width(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native int rs_get_detached_frame_width(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native int rs_get_detached_framerate(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native int rs_get_detached_framerate(@Const rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native rs_device rs_get_device(rs_context org_bytedeco_javacpp_RealSense_rs_context, int i, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native rs_device rs_get_device(rs_context org_bytedeco_javacpp_RealSense_rs_context, int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native int rs_get_device_count(@Const rs_context org_bytedeco_javacpp_RealSense_rs_context, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native int rs_get_device_count(@Const rs_context org_bytedeco_javacpp_RealSense_rs_context, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native float rs_get_device_depth_scale(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native float rs_get_device_depth_scale(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_get_device_extrinsics(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @Cast({"rs_stream"}) int i2, rs_extrinsics org_bytedeco_javacpp_RealSense_rs_extrinsics, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_get_device_extrinsics(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @Cast({"rs_stream"}) int i2, rs_extrinsics org_bytedeco_javacpp_RealSense_rs_extrinsics, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"const char*"})
    public static native BytePointer rs_get_device_firmware_version(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    @Cast({"const char*"})
    public static native BytePointer rs_get_device_firmware_version(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"const char*"})
    public static native BytePointer rs_get_device_info(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_camera_info"}) int i, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    @Cast({"const char*"})
    public static native BytePointer rs_get_device_info(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_camera_info"}) int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"const char*"})
    public static native BytePointer rs_get_device_name(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    @Cast({"const char*"})
    public static native BytePointer rs_get_device_name(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native double rs_get_device_option(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_option"}) int i, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native double rs_get_device_option(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_option"}) int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"const char*"})
    public static native BytePointer rs_get_device_option_description(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_option"}) int i, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    @Cast({"const char*"})
    public static native BytePointer rs_get_device_option_description(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_option"}) int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_get_device_option_range(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_option"}) int i, DoubleBuffer doubleBuffer, DoubleBuffer doubleBuffer2, DoubleBuffer doubleBuffer3, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_get_device_option_range(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_option"}) int i, DoublePointer doublePointer, DoublePointer doublePointer2, DoublePointer doublePointer3, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_get_device_option_range(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_option"}) int i, DoublePointer doublePointer, DoublePointer doublePointer2, DoublePointer doublePointer3, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_get_device_option_range(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_option"}) int i, double[] dArr, double[] dArr2, double[] dArr3, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_get_device_option_range_ex(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_option"}) int i, DoubleBuffer doubleBuffer, DoubleBuffer doubleBuffer2, DoubleBuffer doubleBuffer3, DoubleBuffer doubleBuffer4, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_get_device_option_range_ex(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_option"}) int i, DoublePointer doublePointer, DoublePointer doublePointer2, DoublePointer doublePointer3, DoublePointer doublePointer4, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_get_device_option_range_ex(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_option"}) int i, DoublePointer doublePointer, DoublePointer doublePointer2, DoublePointer doublePointer3, DoublePointer doublePointer4, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_get_device_option_range_ex(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_option"}) int i, double[] dArr, double[] dArr2, double[] dArr3, double[] dArr4, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_get_device_options(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"const rs_option*"}) IntBuffer intBuffer, @Cast({"unsigned int"}) int i, DoubleBuffer doubleBuffer, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_get_device_options(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"const rs_option*"}) IntPointer intPointer, @Cast({"unsigned int"}) int i, DoublePointer doublePointer, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_get_device_options(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"const rs_option*"}) IntPointer intPointer, @Cast({"unsigned int"}) int i, DoublePointer doublePointer, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_get_device_options(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"const rs_option*"}) int[] iArr, @Cast({"unsigned int"}) int i, double[] dArr, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"const char*"})
    public static native BytePointer rs_get_device_serial(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    @Cast({"const char*"})
    public static native BytePointer rs_get_device_serial(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"const char*"})
    public static native BytePointer rs_get_device_usb_port_id(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    @Cast({"const char*"})
    public static native BytePointer rs_get_device_usb_port_id(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"const char*"})
    public static native BytePointer rs_get_error_message(@Const rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"const char*"})
    public static native BytePointer rs_get_failed_args(@Const rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"const char*"})
    public static native BytePointer rs_get_failed_function(@Const rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Const
    public static native Pointer rs_get_frame_data(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    @Const
    public static native Pointer rs_get_frame_data(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"unsigned long long"})
    public static native long rs_get_frame_number(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    @Cast({"unsigned long long"})
    public static native long rs_get_frame_number(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native double rs_get_frame_timestamp(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native double rs_get_frame_timestamp(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_get_motion_extrinsics_from(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, rs_extrinsics org_bytedeco_javacpp_RealSense_rs_extrinsics, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_get_motion_extrinsics_from(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, rs_extrinsics org_bytedeco_javacpp_RealSense_rs_extrinsics, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_get_motion_intrinsics(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, rs_motion_intrinsics org_bytedeco_javacpp_RealSense_rs_motion_intrinsics, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_get_motion_intrinsics(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, rs_motion_intrinsics org_bytedeco_javacpp_RealSense_rs_motion_intrinsics, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"rs_format"})
    public static native int rs_get_stream_format(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    @Cast({"rs_format"})
    public static native int rs_get_stream_format(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native int rs_get_stream_framerate(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native int rs_get_stream_framerate(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native int rs_get_stream_height(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native int rs_get_stream_height(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_get_stream_intrinsics(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, rs_intrinsics org_bytedeco_javacpp_RealSense_rs_intrinsics, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_get_stream_intrinsics(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, rs_intrinsics org_bytedeco_javacpp_RealSense_rs_intrinsics, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_get_stream_mode(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, int i2, IntBuffer intBuffer, IntBuffer intBuffer2, @Cast({"rs_format*"}) IntBuffer intBuffer3, IntBuffer intBuffer4, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_get_stream_mode(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, int i2, IntPointer intPointer, IntPointer intPointer2, @Cast({"rs_format*"}) IntPointer intPointer3, IntPointer intPointer4, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_get_stream_mode(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, int i2, IntPointer intPointer, IntPointer intPointer2, @Cast({"rs_format*"}) IntPointer intPointer3, IntPointer intPointer4, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_get_stream_mode(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, int i2, int[] iArr, int[] iArr2, @Cast({"rs_format*"}) int[] iArr3, int[] iArr4, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native int rs_get_stream_mode_count(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native int rs_get_stream_mode_count(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native int rs_get_stream_width(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native int rs_get_stream_width(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native int rs_is_device_streaming(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native int rs_is_device_streaming(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native int rs_is_motion_tracking_active(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native int rs_is_motion_tracking_active(rs_device org_bytedeco_javacpp_RealSense_rs_device, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native int rs_is_stream_enabled(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native int rs_is_stream_enabled(@Const rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_log_to_callback(@Cast({"rs_log_severity"}) int i, On_log_int_BytePointer_Pointer on_log_int_BytePointer_Pointer, Pointer pointer, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_log_to_callback(@Cast({"rs_log_severity"}) int i, On_log_int_BytePointer_Pointer on_log_int_BytePointer_Pointer, Pointer pointer, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_log_to_callback(@Cast({"rs_log_severity"}) int i, On_log_int_String_Pointer on_log_int_String_Pointer, Pointer pointer, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_log_to_callback_cpp(@Cast({"rs_log_severity"}) int i, rs_log_callback org_bytedeco_javacpp_RealSense_rs_log_callback, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_log_to_callback_cpp(@Cast({"rs_log_severity"}) int i, rs_log_callback org_bytedeco_javacpp_RealSense_rs_log_callback, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_log_to_console(@Cast({"rs_log_severity"}) int i, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_log_to_console(@Cast({"rs_log_severity"}) int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_log_to_file(@Cast({"rs_log_severity"}) int i, String str, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_log_to_file(@Cast({"rs_log_severity"}) int i, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_log_to_file(@Cast({"rs_log_severity"}) int i, @Cast({"const char*"}) BytePointer bytePointer, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"const char*"})
    public static native BytePointer rs_option_to_string(@Cast({"rs_option"}) int i);

    public static native int rs_poll_for_frames(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native int rs_poll_for_frames(rs_device org_bytedeco_javacpp_RealSense_rs_device, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"const char*"})
    public static native BytePointer rs_preset_to_string(@Cast({"rs_preset"}) int i);

    public static native void rs_project_point_to_pixel(FloatBuffer floatBuffer, @Const rs_intrinsics org_bytedeco_javacpp_RealSense_rs_intrinsics, @Const FloatBuffer floatBuffer2);

    public static native void rs_project_point_to_pixel(FloatPointer floatPointer, @Const rs_intrinsics org_bytedeco_javacpp_RealSense_rs_intrinsics, @Const FloatPointer floatPointer2);

    public static native void rs_project_point_to_pixel(float[] fArr, @Const rs_intrinsics org_bytedeco_javacpp_RealSense_rs_intrinsics, @Const float[] fArr2);

    public static native void rs_release_frame(rs_device org_bytedeco_javacpp_RealSense_rs_device, rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_release_frame(rs_device org_bytedeco_javacpp_RealSense_rs_device, rs_frame_ref org_bytedeco_javacpp_RealSense_rs_frame_ref, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_reset_device_options_to_default(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"const rs_option*"}) IntBuffer intBuffer, int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_reset_device_options_to_default(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"const rs_option*"}) IntPointer intPointer, int i, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_reset_device_options_to_default(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"const rs_option*"}) IntPointer intPointer, int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_reset_device_options_to_default(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"const rs_option*"}) int[] iArr, int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_send_blob_to_device(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_blob_type"}) int i, Pointer pointer, int i2, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_send_blob_to_device(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_blob_type"}) int i, Pointer pointer, int i2, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_set_device_option(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_option"}) int i, double d, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_set_device_option(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_option"}) int i, double d, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_set_device_options(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"const rs_option*"}) IntBuffer intBuffer, @Cast({"unsigned int"}) int i, @Const DoubleBuffer doubleBuffer, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_set_device_options(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"const rs_option*"}) IntPointer intPointer, @Cast({"unsigned int"}) int i, @Const DoublePointer doublePointer, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_set_device_options(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"const rs_option*"}) IntPointer intPointer, @Cast({"unsigned int"}) int i, @Const DoublePointer doublePointer, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_set_device_options(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"const rs_option*"}) int[] iArr, @Cast({"unsigned int"}) int i, @Const double[] dArr, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_set_frame_callback(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, On_frame_rs_device_rs_frame_ref_Pointer on_frame_rs_device_rs_frame_ref_Pointer, Pointer pointer, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_set_frame_callback(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, On_frame_rs_device_rs_frame_ref_Pointer on_frame_rs_device_rs_frame_ref_Pointer, Pointer pointer, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_set_frame_callback_cpp(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, rs_frame_callback org_bytedeco_javacpp_RealSense_rs_frame_callback, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_set_frame_callback_cpp(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_stream"}) int i, rs_frame_callback org_bytedeco_javacpp_RealSense_rs_frame_callback, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"const char*"})
    public static native BytePointer rs_source_to_string(@Cast({"rs_source"}) int i);

    public static native void rs_start_device(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_start_device(rs_device org_bytedeco_javacpp_RealSense_rs_device, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_start_source(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_source"}) int i, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_start_source(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_source"}) int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_stop_device(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_stop_device(rs_device org_bytedeco_javacpp_RealSense_rs_device, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    public static native void rs_stop_source(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_source"}) int i, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_stop_source(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_source"}) int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"const char*"})
    public static native BytePointer rs_stream_to_string(@Cast({"rs_stream"}) int i);

    public static native int rs_supports(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_capabilities"}) int i, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native int rs_supports(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_capabilities"}) int i, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @Cast({"const char*"})
    public static native BytePointer rs_timestamp_domain_to_string(@Cast({"rs_timestamp_domain"}) int i);

    public static native void rs_transform_point_to_point(FloatBuffer floatBuffer, @Const rs_extrinsics org_bytedeco_javacpp_RealSense_rs_extrinsics, @Const FloatBuffer floatBuffer2);

    public static native void rs_transform_point_to_point(FloatPointer floatPointer, @Const rs_extrinsics org_bytedeco_javacpp_RealSense_rs_extrinsics, @Const FloatPointer floatPointer2);

    public static native void rs_transform_point_to_point(float[] fArr, @Const rs_extrinsics org_bytedeco_javacpp_RealSense_rs_extrinsics, @Const float[] fArr2);

    public static native void rs_wait_for_frames(rs_device org_bytedeco_javacpp_RealSense_rs_device, @Cast({"rs_error**"}) PointerPointer pointerPointer);

    public static native void rs_wait_for_frames(rs_device org_bytedeco_javacpp_RealSense_rs_device, @ByPtrPtr rs_error org_bytedeco_javacpp_RealSense_rs_error);

    @ByRef
    @Cast({"std::ostream*"})
    @Name({"operator <<"})
    @Namespace("rs")
    public static native Pointer shiftLeft(@ByRef @Cast({"std::ostream*"}) Pointer pointer, @Cast({"rs::source"}) byte b);

    @ByRef
    @Cast({"std::ostream*"})
    @Name({"operator <<"})
    @Namespace("rs")
    public static native Pointer shiftLeft(@ByRef @Cast({"std::ostream*"}) Pointer pointer, @Cast({"rs::stream"}) int i);

    static {
        Loader.load();
    }
}
