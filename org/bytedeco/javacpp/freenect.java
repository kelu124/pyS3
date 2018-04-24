package org.bytedeco.javacpp;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.bytedeco.javacpp.annotation.ByPtrPtr;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.MemberGetter;
import org.bytedeco.javacpp.annotation.Opaque;
import org.bytedeco.javacpp.presets.freenect.timeval;

public class freenect extends org.bytedeco.javacpp.presets.freenect {
    public static final int FREENECT_AUTO_EXPOSURE = 16384;
    public static final int FREENECT_AUTO_WHITE_BALANCE = 2;
    public static final int FREENECT_COUNTS_PER_G = 819;
    public static final int FREENECT_DEPTH_10BIT = 1;
    public static final int FREENECT_DEPTH_10BIT_PACKED = 3;
    public static final int FREENECT_DEPTH_11BIT = 0;
    public static final int FREENECT_DEPTH_11BIT_PACKED = 2;
    public static final int FREENECT_DEPTH_DUMMY = Integer.MAX_VALUE;
    public static final int FREENECT_DEPTH_MM = 5;
    public static final int FREENECT_DEPTH_MM_MAX_VALUE = 10000;
    public static final int FREENECT_DEPTH_MM_NO_VALUE = 0;
    public static final int FREENECT_DEPTH_RAW_MAX_VALUE = 2048;
    public static final int FREENECT_DEPTH_RAW_NO_VALUE = 2047;
    public static final int FREENECT_DEPTH_REGISTERED = 4;
    public static final int FREENECT_DEVICE_AUDIO = 4;
    public static final int FREENECT_DEVICE_CAMERA = 2;
    public static final int FREENECT_DEVICE_MOTOR = 1;
    public static final int FREENECT_LOG_DEBUG = 5;
    public static final int FREENECT_LOG_ERROR = 1;
    public static final int FREENECT_LOG_FATAL = 0;
    public static final int FREENECT_LOG_FLOOD = 7;
    public static final int FREENECT_LOG_INFO = 4;
    public static final int FREENECT_LOG_NOTICE = 3;
    public static final int FREENECT_LOG_SPEW = 6;
    public static final int FREENECT_LOG_WARNING = 2;
    public static final int FREENECT_MIRROR_DEPTH = 65536;
    public static final int FREENECT_MIRROR_VIDEO = 131072;
    public static final int FREENECT_NEAR_MODE = 262144;
    public static final int FREENECT_OFF = 0;
    public static final int FREENECT_ON = 1;
    public static final int FREENECT_RAW_COLOR = 16;
    public static final int FREENECT_RESOLUTION_DUMMY = Integer.MAX_VALUE;
    public static final int FREENECT_RESOLUTION_HIGH = 2;
    public static final int FREENECT_RESOLUTION_LOW = 0;
    public static final int FREENECT_RESOLUTION_MEDIUM = 1;
    public static final int FREENECT_VIDEO_BAYER = 1;
    public static final int FREENECT_VIDEO_DUMMY = Integer.MAX_VALUE;
    public static final int FREENECT_VIDEO_IR_10BIT = 3;
    public static final int FREENECT_VIDEO_IR_10BIT_PACKED = 4;
    public static final int FREENECT_VIDEO_IR_8BIT = 2;
    public static final int FREENECT_VIDEO_RGB = 0;
    public static final int FREENECT_VIDEO_YUV_RAW = 6;
    public static final int FREENECT_VIDEO_YUV_RGB = 5;
    public static final int LED_BLINK_GREEN = 4;
    public static final int LED_BLINK_RED_YELLOW = 6;
    public static final int LED_GREEN = 1;
    public static final int LED_OFF = 0;
    public static final int LED_RED = 2;
    public static final int LED_YELLOW = 3;
    public static final int TILT_STATUS_LIMIT = 1;
    public static final int TILT_STATUS_MOVING = 4;
    public static final int TILT_STATUS_STOPPED = 0;

    @Opaque
    public static class _freenect_context extends Pointer {
        public _freenect_context() {
            super((Pointer) null);
        }

        public _freenect_context(Pointer p) {
            super(p);
        }
    }

    @Opaque
    public static class _freenect_device extends Pointer {
        public _freenect_device() {
            super((Pointer) null);
        }

        public _freenect_device(Pointer p) {
            super(p);
        }
    }

    @Opaque
    public static class freenect_context extends Pointer {
        public freenect_context() {
            super((Pointer) null);
        }

        public freenect_context(Pointer p) {
            super(p);
        }
    }

    @Opaque
    public static class freenect_device extends Pointer {
        public freenect_device() {
            super((Pointer) null);
        }

        public freenect_device(Pointer p) {
            super(p);
        }
    }

    public static class freenect_device_attributes extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @MemberGetter
        @Cast({"const char*"})
        public native BytePointer camera_serial();

        public native freenect_device_attributes next();

        public native freenect_device_attributes next(freenect_device_attributes org_bytedeco_javacpp_freenect_freenect_device_attributes);

        static {
            Loader.load();
        }

        public freenect_device_attributes() {
            super((Pointer) null);
            allocate();
        }

        public freenect_device_attributes(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public freenect_device_attributes(Pointer p) {
            super(p);
        }

        public freenect_device_attributes position(long position) {
            return (freenect_device_attributes) super.position(position);
        }
    }

    public static class freenect_frame_mode extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native int bytes();

        public native freenect_frame_mode bytes(int i);

        public native byte data_bits_per_pixel();

        public native freenect_frame_mode data_bits_per_pixel(byte b);

        @Cast({"freenect_depth_format"})
        public native int depth_format();

        public native freenect_frame_mode depth_format(int i);

        public native int dummy();

        public native freenect_frame_mode dummy(int i);

        public native byte framerate();

        public native freenect_frame_mode framerate(byte b);

        public native freenect_frame_mode height(short s);

        public native short height();

        public native byte is_valid();

        public native freenect_frame_mode is_valid(byte b);

        public native byte padding_bits_per_pixel();

        public native freenect_frame_mode padding_bits_per_pixel(byte b);

        @Cast({"uint32_t"})
        public native int reserved();

        public native freenect_frame_mode reserved(int i);

        @Cast({"freenect_resolution"})
        public native int resolution();

        public native freenect_frame_mode resolution(int i);

        @Cast({"freenect_video_format"})
        public native int video_format();

        public native freenect_frame_mode video_format(int i);

        public native freenect_frame_mode width(short s);

        public native short width();

        static {
            Loader.load();
        }

        public freenect_frame_mode() {
            super((Pointer) null);
            allocate();
        }

        public freenect_frame_mode(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public freenect_frame_mode(Pointer p) {
            super(p);
        }

        public freenect_frame_mode position(long position) {
            return (freenect_frame_mode) super.position(position);
        }
    }

    public static class freenect_raw_tilt_state extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native freenect_raw_tilt_state accelerometer_x(short s);

        public native short accelerometer_x();

        public native freenect_raw_tilt_state accelerometer_y(short s);

        public native short accelerometer_y();

        public native freenect_raw_tilt_state accelerometer_z(short s);

        public native short accelerometer_z();

        public native byte tilt_angle();

        public native freenect_raw_tilt_state tilt_angle(byte b);

        @Cast({"freenect_tilt_status_code"})
        public native int tilt_status();

        public native freenect_raw_tilt_state tilt_status(int i);

        static {
            Loader.load();
        }

        public freenect_raw_tilt_state() {
            super((Pointer) null);
            allocate();
        }

        public freenect_raw_tilt_state(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public freenect_raw_tilt_state(Pointer p) {
            super(p);
        }

        public freenect_raw_tilt_state position(long position) {
            return (freenect_raw_tilt_state) super.position(position);
        }
    }

    public static class freenect_reg_pad_info extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native freenect_reg_pad_info cropping_lines(short s);

        @Cast({"uint16_t"})
        public native short cropping_lines();

        public native freenect_reg_pad_info end_lines(short s);

        @Cast({"uint16_t"})
        public native short end_lines();

        public native freenect_reg_pad_info start_lines(short s);

        @Cast({"uint16_t"})
        public native short start_lines();

        static {
            Loader.load();
        }

        public freenect_reg_pad_info() {
            super((Pointer) null);
            allocate();
        }

        public freenect_reg_pad_info(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public freenect_reg_pad_info(Pointer p) {
            super(p);
        }

        public freenect_reg_pad_info position(long position) {
            return (freenect_reg_pad_info) super.position(position);
        }
    }

    public static class freenect_registration extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native double const_shift();

        public native freenect_registration const_shift(double d);

        public native IntPointer depth_to_rgb_shift();

        public native freenect_registration depth_to_rgb_shift(IntPointer intPointer);

        @Cast({"uint16_t*"})
        public native ShortPointer raw_to_mm_shift();

        public native freenect_registration raw_to_mm_shift(ShortPointer shortPointer);

        @ByRef
        public native freenect_reg_info reg_info();

        public native freenect_registration reg_info(freenect_reg_info org_bytedeco_javacpp_freenect_freenect_reg_info);

        @ByRef
        public native freenect_reg_pad_info reg_pad_info();

        public native freenect_registration reg_pad_info(freenect_reg_pad_info org_bytedeco_javacpp_freenect_freenect_reg_pad_info);

        public native int registration_table(int i, int i2);

        @MemberGetter
        @Cast({"int32_t*"})
        public native IntPointer registration_table();

        public native freenect_registration registration_table(int i, int i2, int i3);

        public native freenect_registration zero_plane_info(freenect_zero_plane_info org_bytedeco_javacpp_freenect_freenect_zero_plane_info);

        @ByRef
        public native freenect_zero_plane_info zero_plane_info();

        static {
            Loader.load();
        }

        public freenect_registration() {
            super((Pointer) null);
            allocate();
        }

        public freenect_registration(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public freenect_registration(Pointer p) {
            super(p);
        }

        public freenect_registration position(long position) {
            return (freenect_registration) super.position(position);
        }
    }

    @Opaque
    public static class freenect_usb_context extends Pointer {
        public freenect_usb_context() {
            super((Pointer) null);
        }

        public freenect_usb_context(Pointer p) {
            super(p);
        }
    }

    public static native void freenect_camera_to_world(freenect_device org_bytedeco_javacpp_freenect_freenect_device, int i, int i2, int i3, DoubleBuffer doubleBuffer, DoubleBuffer doubleBuffer2);

    public static native void freenect_camera_to_world(freenect_device org_bytedeco_javacpp_freenect_freenect_device, int i, int i2, int i3, DoublePointer doublePointer, DoublePointer doublePointer2);

    public static native void freenect_camera_to_world(freenect_device org_bytedeco_javacpp_freenect_freenect_device, int i, int i2, int i3, double[] dArr, double[] dArr2);

    public static native int freenect_close_device(freenect_device org_bytedeco_javacpp_freenect_freenect_device);

    @ByVal
    public static native freenect_registration freenect_copy_registration(freenect_device org_bytedeco_javacpp_freenect_freenect_device);

    public static native int freenect_destroy_registration(freenect_registration org_bytedeco_javacpp_freenect_freenect_registration);

    @Cast({"freenect_device_flags"})
    public static native int freenect_enabled_subdevices(freenect_context org_bytedeco_javacpp_freenect_freenect_context);

    @ByVal
    public static native freenect_frame_mode freenect_find_depth_mode(@Cast({"freenect_resolution"}) int i, @Cast({"freenect_depth_format"}) int i2);

    @ByVal
    public static native freenect_frame_mode freenect_find_video_mode(@Cast({"freenect_resolution"}) int i, @Cast({"freenect_video_format"}) int i2);

    public static native void freenect_free_device_attributes(freenect_device_attributes org_bytedeco_javacpp_freenect_freenect_device_attributes);

    @ByVal
    public static native freenect_frame_mode freenect_get_current_depth_mode(freenect_device org_bytedeco_javacpp_freenect_freenect_device);

    @ByVal
    public static native freenect_frame_mode freenect_get_current_video_mode(freenect_device org_bytedeco_javacpp_freenect_freenect_device);

    @ByVal
    public static native freenect_frame_mode freenect_get_depth_mode(int i);

    public static native int freenect_get_depth_mode_count();

    public static native int freenect_get_ir_brightness(freenect_device org_bytedeco_javacpp_freenect_freenect_device);

    public static native void freenect_get_mks_accel(freenect_raw_tilt_state org_bytedeco_javacpp_freenect_freenect_raw_tilt_state, DoubleBuffer doubleBuffer, DoubleBuffer doubleBuffer2, DoubleBuffer doubleBuffer3);

    public static native void freenect_get_mks_accel(freenect_raw_tilt_state org_bytedeco_javacpp_freenect_freenect_raw_tilt_state, DoublePointer doublePointer, DoublePointer doublePointer2, DoublePointer doublePointer3);

    public static native void freenect_get_mks_accel(freenect_raw_tilt_state org_bytedeco_javacpp_freenect_freenect_raw_tilt_state, double[] dArr, double[] dArr2, double[] dArr3);

    public static native double freenect_get_tilt_degs(freenect_raw_tilt_state org_bytedeco_javacpp_freenect_freenect_raw_tilt_state);

    public static native freenect_raw_tilt_state freenect_get_tilt_state(freenect_device org_bytedeco_javacpp_freenect_freenect_device);

    @Cast({"freenect_tilt_status_code"})
    public static native int freenect_get_tilt_status(freenect_raw_tilt_state org_bytedeco_javacpp_freenect_freenect_raw_tilt_state);

    public static native Pointer freenect_get_user(freenect_device org_bytedeco_javacpp_freenect_freenect_device);

    @ByVal
    public static native freenect_frame_mode freenect_get_video_mode(int i);

    public static native int freenect_get_video_mode_count();

    public static native int freenect_init(@Cast({"freenect_context**"}) PointerPointer pointerPointer, freenect_usb_context org_bytedeco_javacpp_freenect_freenect_usb_context);

    public static native int freenect_init(@ByPtrPtr freenect_context org_bytedeco_javacpp_freenect_freenect_context, freenect_usb_context org_bytedeco_javacpp_freenect_freenect_usb_context);

    public static native int freenect_list_device_attributes(freenect_context org_bytedeco_javacpp_freenect_freenect_context, @Cast({"freenect_device_attributes**"}) PointerPointer pointerPointer);

    public static native int freenect_list_device_attributes(freenect_context org_bytedeco_javacpp_freenect_freenect_context, @ByPtrPtr freenect_device_attributes org_bytedeco_javacpp_freenect_freenect_device_attributes);

    public static native void freenect_map_rgb_to_depth(freenect_device org_bytedeco_javacpp_freenect_freenect_device, @Cast({"uint16_t*"}) ShortBuffer shortBuffer, @Cast({"uint8_t*"}) ByteBuffer byteBuffer, @Cast({"uint8_t*"}) ByteBuffer byteBuffer2);

    public static native void freenect_map_rgb_to_depth(freenect_device org_bytedeco_javacpp_freenect_freenect_device, @Cast({"uint16_t*"}) ShortPointer shortPointer, @Cast({"uint8_t*"}) BytePointer bytePointer, @Cast({"uint8_t*"}) BytePointer bytePointer2);

    public static native void freenect_map_rgb_to_depth(freenect_device org_bytedeco_javacpp_freenect_freenect_device, @Cast({"uint16_t*"}) short[] sArr, @Cast({"uint8_t*"}) byte[] bArr, @Cast({"uint8_t*"}) byte[] bArr2);

    public static native int freenect_num_devices(freenect_context org_bytedeco_javacpp_freenect_freenect_context);

    public static native int freenect_open_device(freenect_context org_bytedeco_javacpp_freenect_freenect_context, @Cast({"freenect_device**"}) PointerPointer pointerPointer, int i);

    public static native int freenect_open_device(freenect_context org_bytedeco_javacpp_freenect_freenect_context, @ByPtrPtr freenect_device org_bytedeco_javacpp_freenect_freenect_device, int i);

    public static native int freenect_open_device_by_camera_serial(freenect_context org_bytedeco_javacpp_freenect_freenect_context, @Cast({"freenect_device**"}) PointerPointer pointerPointer, @Cast({"const char*"}) BytePointer bytePointer);

    public static native int freenect_open_device_by_camera_serial(freenect_context org_bytedeco_javacpp_freenect_freenect_context, @ByPtrPtr freenect_device org_bytedeco_javacpp_freenect_freenect_device, String str);

    public static native int freenect_open_device_by_camera_serial(freenect_context org_bytedeco_javacpp_freenect_freenect_context, @ByPtrPtr freenect_device org_bytedeco_javacpp_freenect_freenect_device, @Cast({"const char*"}) BytePointer bytePointer);

    public static native int freenect_process_events(freenect_context org_bytedeco_javacpp_freenect_freenect_context);

    public static native int freenect_process_events_timeout(freenect_context org_bytedeco_javacpp_freenect_freenect_context, timeval org_bytedeco_javacpp_presets_freenect_timeval);

    public static native void freenect_select_subdevices(freenect_context org_bytedeco_javacpp_freenect_freenect_context, @Cast({"freenect_device_flags"}) int i);

    public static native void freenect_set_audio_in_callback(freenect_device org_bytedeco_javacpp_freenect_freenect_device, freenect_audio_in_cb org_bytedeco_javacpp_freenect_freenect_audio_in_cb);

    public static native void freenect_set_audio_out_callback(freenect_device org_bytedeco_javacpp_freenect_freenect_device, freenect_audio_out_cb org_bytedeco_javacpp_freenect_freenect_audio_out_cb);

    public static native int freenect_set_depth_buffer(freenect_device org_bytedeco_javacpp_freenect_freenect_device, Pointer pointer);

    public static native void freenect_set_depth_callback(freenect_device org_bytedeco_javacpp_freenect_freenect_device, freenect_depth_cb org_bytedeco_javacpp_freenect_freenect_depth_cb);

    public static native void freenect_set_depth_chunk_callback(freenect_device org_bytedeco_javacpp_freenect_freenect_device, freenect_chunk_cb org_bytedeco_javacpp_freenect_freenect_chunk_cb);

    public static native int freenect_set_depth_mode(freenect_device org_bytedeco_javacpp_freenect_freenect_device, @Const @ByVal freenect_frame_mode org_bytedeco_javacpp_freenect_freenect_frame_mode);

    public static native int freenect_set_flag(freenect_device org_bytedeco_javacpp_freenect_freenect_device, @Cast({"freenect_flag"}) int i, @Cast({"freenect_flag_value"}) int i2);

    public static native void freenect_set_fw_address_k4w(freenect_context org_bytedeco_javacpp_freenect_freenect_context, @Cast({"unsigned char*"}) ByteBuffer byteBuffer, @Cast({"unsigned int"}) int i);

    public static native void freenect_set_fw_address_k4w(freenect_context org_bytedeco_javacpp_freenect_freenect_context, @Cast({"unsigned char*"}) BytePointer bytePointer, @Cast({"unsigned int"}) int i);

    public static native void freenect_set_fw_address_k4w(freenect_context org_bytedeco_javacpp_freenect_freenect_context, @Cast({"unsigned char*"}) byte[] bArr, @Cast({"unsigned int"}) int i);

    public static native void freenect_set_fw_address_nui(freenect_context org_bytedeco_javacpp_freenect_freenect_context, @Cast({"unsigned char*"}) ByteBuffer byteBuffer, @Cast({"unsigned int"}) int i);

    public static native void freenect_set_fw_address_nui(freenect_context org_bytedeco_javacpp_freenect_freenect_context, @Cast({"unsigned char*"}) BytePointer bytePointer, @Cast({"unsigned int"}) int i);

    public static native void freenect_set_fw_address_nui(freenect_context org_bytedeco_javacpp_freenect_freenect_context, @Cast({"unsigned char*"}) byte[] bArr, @Cast({"unsigned int"}) int i);

    public static native int freenect_set_ir_brightness(freenect_device org_bytedeco_javacpp_freenect_freenect_device, @Cast({"uint16_t"}) short s);

    public static native int freenect_set_led(freenect_device org_bytedeco_javacpp_freenect_freenect_device, @Cast({"freenect_led_options"}) int i);

    public static native void freenect_set_log_callback(freenect_context org_bytedeco_javacpp_freenect_freenect_context, freenect_log_cb org_bytedeco_javacpp_freenect_freenect_log_cb);

    public static native void freenect_set_log_level(freenect_context org_bytedeco_javacpp_freenect_freenect_context, @Cast({"freenect_loglevel"}) int i);

    public static native int freenect_set_tilt_degs(freenect_device org_bytedeco_javacpp_freenect_freenect_device, double d);

    public static native void freenect_set_user(freenect_device org_bytedeco_javacpp_freenect_freenect_device, Pointer pointer);

    public static native int freenect_set_video_buffer(freenect_device org_bytedeco_javacpp_freenect_freenect_device, Pointer pointer);

    public static native void freenect_set_video_callback(freenect_device org_bytedeco_javacpp_freenect_freenect_device, freenect_video_cb org_bytedeco_javacpp_freenect_freenect_video_cb);

    public static native void freenect_set_video_chunk_callback(freenect_device org_bytedeco_javacpp_freenect_freenect_device, freenect_chunk_cb org_bytedeco_javacpp_freenect_freenect_chunk_cb);

    public static native int freenect_set_video_mode(freenect_device org_bytedeco_javacpp_freenect_freenect_device, @ByVal freenect_frame_mode org_bytedeco_javacpp_freenect_freenect_frame_mode);

    public static native int freenect_shutdown(freenect_context org_bytedeco_javacpp_freenect_freenect_context);

    public static native int freenect_start_audio(freenect_device org_bytedeco_javacpp_freenect_freenect_device);

    public static native int freenect_start_depth(freenect_device org_bytedeco_javacpp_freenect_freenect_device);

    public static native int freenect_start_video(freenect_device org_bytedeco_javacpp_freenect_freenect_device);

    public static native int freenect_stop_audio(freenect_device org_bytedeco_javacpp_freenect_freenect_device);

    public static native int freenect_stop_depth(freenect_device org_bytedeco_javacpp_freenect_freenect_device);

    public static native int freenect_stop_video(freenect_device org_bytedeco_javacpp_freenect_freenect_device);

    public static native int freenect_supported_subdevices();

    public static native int freenect_sync_camera_to_world(int i, int i2, int i3, DoubleBuffer doubleBuffer, DoubleBuffer doubleBuffer2, int i4);

    public static native int freenect_sync_camera_to_world(int i, int i2, int i3, DoublePointer doublePointer, DoublePointer doublePointer2, int i4);

    public static native int freenect_sync_camera_to_world(int i, int i2, int i3, double[] dArr, double[] dArr2, int i4);

    public static native int freenect_sync_get_depth(@ByPtrPtr @Cast({"void**"}) Pointer pointer, @Cast({"uint32_t*"}) IntBuffer intBuffer, int i, @Cast({"freenect_depth_format"}) int i2);

    public static native int freenect_sync_get_depth(@ByPtrPtr @Cast({"void**"}) Pointer pointer, @Cast({"uint32_t*"}) IntPointer intPointer, int i, @Cast({"freenect_depth_format"}) int i2);

    public static native int freenect_sync_get_depth(@ByPtrPtr @Cast({"void**"}) Pointer pointer, @Cast({"uint32_t*"}) int[] iArr, int i, @Cast({"freenect_depth_format"}) int i2);

    public static native int freenect_sync_get_depth(@Cast({"void**"}) PointerPointer pointerPointer, @Cast({"uint32_t*"}) IntPointer intPointer, int i, @Cast({"freenect_depth_format"}) int i2);

    public static native int freenect_sync_get_depth_with_res(@ByPtrPtr @Cast({"void**"}) Pointer pointer, @Cast({"uint32_t*"}) IntBuffer intBuffer, int i, @Cast({"freenect_resolution"}) int i2, @Cast({"freenect_depth_format"}) int i3);

    public static native int freenect_sync_get_depth_with_res(@ByPtrPtr @Cast({"void**"}) Pointer pointer, @Cast({"uint32_t*"}) IntPointer intPointer, int i, @Cast({"freenect_resolution"}) int i2, @Cast({"freenect_depth_format"}) int i3);

    public static native int freenect_sync_get_depth_with_res(@ByPtrPtr @Cast({"void**"}) Pointer pointer, @Cast({"uint32_t*"}) int[] iArr, int i, @Cast({"freenect_resolution"}) int i2, @Cast({"freenect_depth_format"}) int i3);

    public static native int freenect_sync_get_depth_with_res(@Cast({"void**"}) PointerPointer pointerPointer, @Cast({"uint32_t*"}) IntPointer intPointer, int i, @Cast({"freenect_resolution"}) int i2, @Cast({"freenect_depth_format"}) int i3);

    public static native int freenect_sync_get_tilt_state(@Cast({"freenect_raw_tilt_state**"}) PointerPointer pointerPointer, int i);

    public static native int freenect_sync_get_tilt_state(@ByPtrPtr freenect_raw_tilt_state org_bytedeco_javacpp_freenect_freenect_raw_tilt_state, int i);

    public static native int freenect_sync_get_video(@ByPtrPtr @Cast({"void**"}) Pointer pointer, @Cast({"uint32_t*"}) IntBuffer intBuffer, int i, @Cast({"freenect_video_format"}) int i2);

    public static native int freenect_sync_get_video(@ByPtrPtr @Cast({"void**"}) Pointer pointer, @Cast({"uint32_t*"}) IntPointer intPointer, int i, @Cast({"freenect_video_format"}) int i2);

    public static native int freenect_sync_get_video(@ByPtrPtr @Cast({"void**"}) Pointer pointer, @Cast({"uint32_t*"}) int[] iArr, int i, @Cast({"freenect_video_format"}) int i2);

    public static native int freenect_sync_get_video(@Cast({"void**"}) PointerPointer pointerPointer, @Cast({"uint32_t*"}) IntPointer intPointer, int i, @Cast({"freenect_video_format"}) int i2);

    public static native int freenect_sync_get_video_with_res(@ByPtrPtr @Cast({"void**"}) Pointer pointer, @Cast({"uint32_t*"}) IntBuffer intBuffer, int i, @Cast({"freenect_resolution"}) int i2, @Cast({"freenect_video_format"}) int i3);

    public static native int freenect_sync_get_video_with_res(@ByPtrPtr @Cast({"void**"}) Pointer pointer, @Cast({"uint32_t*"}) IntPointer intPointer, int i, @Cast({"freenect_resolution"}) int i2, @Cast({"freenect_video_format"}) int i3);

    public static native int freenect_sync_get_video_with_res(@ByPtrPtr @Cast({"void**"}) Pointer pointer, @Cast({"uint32_t*"}) int[] iArr, int i, @Cast({"freenect_resolution"}) int i2, @Cast({"freenect_video_format"}) int i3);

    public static native int freenect_sync_get_video_with_res(@Cast({"void**"}) PointerPointer pointerPointer, @Cast({"uint32_t*"}) IntPointer intPointer, int i, @Cast({"freenect_resolution"}) int i2, @Cast({"freenect_video_format"}) int i3);

    public static native int freenect_sync_set_led(@Cast({"freenect_led_options"}) int i, int i2);

    public static native int freenect_sync_set_tilt_degs(int i, int i2);

    public static native void freenect_sync_stop();

    public static native int freenect_update_tilt_state(freenect_device org_bytedeco_javacpp_freenect_freenect_device);

    static {
        Loader.load();
    }
}
