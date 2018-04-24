package org.bytedeco.javacpp;

import java.nio.ByteBuffer;
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
import org.bytedeco.javacpp.annotation.Opaque;

public class FlyCapture2_C extends org.bytedeco.javacpp.presets.FlyCapture2_C {
    public static final int FALSE = 0;
    public static final int FC2_ARRIVAL = 1;
    public static final int FC2_AUTO_EXPOSURE = 1;
    public static final int FC2_BANDWIDTH_ALLOCATION_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int FC2_BANDWIDTH_ALLOCATION_OFF = 0;
    public static final int FC2_BANDWIDTH_ALLOCATION_ON = 1;
    public static final int FC2_BANDWIDTH_ALLOCATION_UNSPECIFIED = 3;
    public static final int FC2_BANDWIDTH_ALLOCATION_UNSUPPORTED = 2;
    public static final int FC2_BMP = 2;
    public static final int FC2_BRIGHTNESS = 0;
    public static final int FC2_BT_BGGR = 4;
    public static final int FC2_BT_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int FC2_BT_GBRG = 3;
    public static final int FC2_BT_GRBG = 2;
    public static final int FC2_BT_NONE = 0;
    public static final int FC2_BT_RGGB = 1;
    public static final int FC2_BUFFER_FRAMES = 1;
    public static final int FC2_BUSSPEED_10000BASE_T = 11;
    public static final int FC2_BUSSPEED_1000BASE_T = 10;
    public static final int FC2_BUSSPEED_100BASE_T = 9;
    public static final int FC2_BUSSPEED_10BASE_T = 8;
    public static final int FC2_BUSSPEED_ANY = 13;
    public static final int FC2_BUSSPEED_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int FC2_BUSSPEED_S100 = 0;
    public static final int FC2_BUSSPEED_S1600 = 5;
    public static final int FC2_BUSSPEED_S200 = 1;
    public static final int FC2_BUSSPEED_S3200 = 6;
    public static final int FC2_BUSSPEED_S400 = 2;
    public static final int FC2_BUSSPEED_S480 = 3;
    public static final int FC2_BUSSPEED_S5000 = 7;
    public static final int FC2_BUSSPEED_S800 = 4;
    public static final int FC2_BUSSPEED_SPEED_UNKNOWN = -1;
    public static final int FC2_BUSSPEED_S_FASTEST = 12;
    public static final int FC2_BUS_RESET = 0;
    public static final int FC2_BYTE_ORDER_BIG_ENDIAN = 1;
    public static final int FC2_BYTE_ORDER_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int FC2_BYTE_ORDER_LITTLE_ENDIAN = 0;
    public static final int FC2_CALLBACK_TYPE_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int FC2_COLOR_PROCESSING_ALGORITHM_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int FC2_DEFAULT = 0;
    public static final int FC2_DIRECTIONAL = 7;
    public static final int FC2_DRIVER_1394_CAM = 0;
    public static final int FC2_DRIVER_1394_JUJU = 2;
    public static final int FC2_DRIVER_1394_PRO = 1;
    public static final int FC2_DRIVER_1394_RAW1394 = 4;
    public static final int FC2_DRIVER_1394_VIDEO1394 = 3;
    public static final int FC2_DRIVER_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int FC2_DRIVER_GIGE_FILTER = 9;
    public static final int FC2_DRIVER_GIGE_LWF = 11;
    public static final int FC2_DRIVER_GIGE_NONE = 8;
    public static final int FC2_DRIVER_GIGE_PRO = 10;
    public static final int FC2_DRIVER_UNKNOWN = -1;
    public static final int FC2_DRIVER_USB3_PRO = 7;
    public static final int FC2_DRIVER_USB_CAM = 6;
    public static final int FC2_DRIVER_USB_NONE = 5;
    public static final int FC2_DROP_FRAMES = 0;
    public static final int FC2_EDGE_SENSING = 3;
    public static final int FC2_ERROR_BUFFER_TOO_SMALL = 40;
    public static final int FC2_ERROR_BUS_MASTER_FAILED = 19;
    public static final int FC2_ERROR_FAILED = 1;
    public static final int FC2_ERROR_FAILED_BUS_MASTER_CONNECTION = 3;
    public static final int FC2_ERROR_FAILED_GUID = 13;
    public static final int FC2_ERROR_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int FC2_ERROR_IIDC_FAILED = 22;
    public static final int FC2_ERROR_IMAGE_CONSISTENCY_ERROR = 41;
    public static final int FC2_ERROR_IMAGE_CONVERSION_FAILED = 38;
    public static final int FC2_ERROR_IMAGE_LIBRARY_FAILURE = 39;
    public static final int FC2_ERROR_INCOMPATIBLE_DRIVER = 42;
    public static final int FC2_ERROR_INIT_FAILED = 5;
    public static final int FC2_ERROR_INVALID_BUS_MANAGER = 9;
    public static final int FC2_ERROR_INVALID_GENERATION = 20;
    public static final int FC2_ERROR_INVALID_MODE = 15;
    public static final int FC2_ERROR_INVALID_PACKET_SIZE = 14;
    public static final int FC2_ERROR_INVALID_PARAMETER = 7;
    public static final int FC2_ERROR_INVALID_SETTINGS = 8;
    public static final int FC2_ERROR_ISOCH_ALREADY_STARTED = 31;
    public static final int FC2_ERROR_ISOCH_BANDWIDTH_EXCEEDED = 37;
    public static final int FC2_ERROR_ISOCH_FAILED = 30;
    public static final int FC2_ERROR_ISOCH_NOT_STARTED = 32;
    public static final int FC2_ERROR_ISOCH_RETRIEVE_BUFFER_FAILED = 34;
    public static final int FC2_ERROR_ISOCH_START_FAILED = 33;
    public static final int FC2_ERROR_ISOCH_STOP_FAILED = 35;
    public static final int FC2_ERROR_ISOCH_SYNC_FAILED = 36;
    public static final int FC2_ERROR_LOW_LEVEL_FAILURE = 11;
    public static final int FC2_ERROR_LUT_FAILED = 21;
    public static final int FC2_ERROR_MEMORY_ALLOCATION_FAILED = 10;
    public static final int FC2_ERROR_NOT_CONNECTED = 4;
    public static final int FC2_ERROR_NOT_FOUND = 12;
    public static final int FC2_ERROR_NOT_IMPLEMENTED = 2;
    public static final int FC2_ERROR_NOT_INTITIALIZED = 6;
    public static final int FC2_ERROR_NOT_IN_FORMAT7 = 16;
    public static final int FC2_ERROR_NOT_SUPPORTED = 17;
    public static final int FC2_ERROR_OK = 0;
    public static final int FC2_ERROR_PROPERTY_FAILED = 25;
    public static final int FC2_ERROR_PROPERTY_NOT_PRESENT = 26;
    public static final int FC2_ERROR_READ_REGISTER_FAILED = 28;
    public static final int FC2_ERROR_REGISTER_FAILED = 27;
    public static final int FC2_ERROR_STROBE_FAILED = 23;
    public static final int FC2_ERROR_TIMEOUT = 18;
    public static final int FC2_ERROR_TRIGGER_FAILED = 24;
    public static final int FC2_ERROR_UNDEFINED = -1;
    public static final int FC2_ERROR_WRITE_REGISTER_FAILED = 29;
    public static final int FC2_FOCUS = 8;
    public static final int FC2_FRAMERATE_120 = 6;
    public static final int FC2_FRAMERATE_15 = 3;
    public static final int FC2_FRAMERATE_1_875 = 0;
    public static final int FC2_FRAMERATE_240 = 7;
    public static final int FC2_FRAMERATE_30 = 4;
    public static final int FC2_FRAMERATE_3_75 = 1;
    public static final int FC2_FRAMERATE_60 = 5;
    public static final int FC2_FRAMERATE_7_5 = 2;
    public static final int FC2_FRAMERATE_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int FC2_FRAMERATE_FORMAT7 = 8;
    public static final int FC2_FRAME_RATE = 16;
    public static final int FC2_FROM_FILE_EXT = -1;
    public static final int FC2_GAIN = 13;
    public static final int FC2_GAMMA = 6;
    public static final int FC2_GRAB_MODE_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int FC2_GRAB_TIMEOUT_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int FC2_HEARTBEAT = 0;
    public static final int FC2_HEARTBEAT_TIMEOUT = 1;
    public static final int FC2_HQ_LINEAR = 4;
    public static final int FC2_HUE = 4;
    public static final int FC2_IMAGE_FILE_FORMAT_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int FC2_INTERFACE_GIGE = 3;
    public static final int FC2_INTERFACE_IEEE1394 = 0;
    public static final int FC2_INTERFACE_TYPE_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int FC2_INTERFACE_UNKNOWN = 4;
    public static final int FC2_INTERFACE_USB_2 = 1;
    public static final int FC2_INTERFACE_USB_3 = 2;
    public static final int FC2_IPP = 6;
    public static final int FC2_IRIS = 7;
    public static final int FC2_JPEG = 3;
    public static final int FC2_JPEG2000 = 4;
    public static final int FC2_LINUX_X64 = 3;
    public static final int FC2_LINUX_X86 = 2;
    public static final int FC2_MAC = 4;
    public static final int FC2_MODE_0 = 0;
    public static final int FC2_MODE_1 = 1;
    public static final int FC2_MODE_10 = 10;
    public static final int FC2_MODE_11 = 11;
    public static final int FC2_MODE_12 = 12;
    public static final int FC2_MODE_13 = 13;
    public static final int FC2_MODE_14 = 14;
    public static final int FC2_MODE_15 = 15;
    public static final int FC2_MODE_16 = 16;
    public static final int FC2_MODE_17 = 17;
    public static final int FC2_MODE_18 = 18;
    public static final int FC2_MODE_19 = 19;
    public static final int FC2_MODE_2 = 2;
    public static final int FC2_MODE_20 = 20;
    public static final int FC2_MODE_21 = 21;
    public static final int FC2_MODE_22 = 22;
    public static final int FC2_MODE_23 = 23;
    public static final int FC2_MODE_24 = 24;
    public static final int FC2_MODE_25 = 25;
    public static final int FC2_MODE_26 = 26;
    public static final int FC2_MODE_27 = 27;
    public static final int FC2_MODE_28 = 28;
    public static final int FC2_MODE_29 = 29;
    public static final int FC2_MODE_3 = 3;
    public static final int FC2_MODE_30 = 30;
    public static final int FC2_MODE_31 = 31;
    public static final int FC2_MODE_4 = 4;
    public static final int FC2_MODE_5 = 5;
    public static final int FC2_MODE_6 = 6;
    public static final int FC2_MODE_7 = 7;
    public static final int FC2_MODE_8 = 8;
    public static final int FC2_MODE_9 = 9;
    public static final int FC2_MODE_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int FC2_NEAREST_NEIGHBOR_FAST = 2;
    public static final int FC2_NO_COLOR_PROCESSING = 1;
    public static final int FC2_NUM_FRAMERATES = 9;
    public static final int FC2_NUM_MODES = 32;
    public static final int FC2_NUM_PIXEL_FORMATS = 20;
    public static final int FC2_NUM_VIDEOMODES = 24;
    public static final int FC2_OSTYPE_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int FC2_PAN = 10;
    public static final int FC2_PCIE_BUSSPEED_2_5 = 0;
    public static final int FC2_PCIE_BUSSPEED_5_0 = 1;
    public static final int FC2_PCIE_BUSSPEED_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int FC2_PCIE_BUSSPEED_UNKNOWN = -1;
    public static final int FC2_PGM = 0;
    public static final int FC2_PIXEL_FORMAT_411YUV8 = 1073741824;
    public static final int FC2_PIXEL_FORMAT_422YUV8 = 536870912;
    public static final int FC2_PIXEL_FORMAT_422YUV8_JPEG = 1073741825;
    public static final int FC2_PIXEL_FORMAT_444YUV8 = 268435456;
    public static final int FC2_PIXEL_FORMAT_BGR = -2147483640;
    public static final int FC2_PIXEL_FORMAT_BGR16 = 33554433;
    public static final int FC2_PIXEL_FORMAT_BGRU = 1073741832;
    public static final int FC2_PIXEL_FORMAT_BGRU16 = 33554434;
    public static final int FC2_PIXEL_FORMAT_MONO12 = 1048576;
    public static final int FC2_PIXEL_FORMAT_MONO16 = 67108864;
    public static final int FC2_PIXEL_FORMAT_MONO8 = Integer.MIN_VALUE;
    public static final int FC2_PIXEL_FORMAT_RAW12 = 524288;
    public static final int FC2_PIXEL_FORMAT_RAW16 = 2097152;
    public static final int FC2_PIXEL_FORMAT_RAW8 = 4194304;
    public static final int FC2_PIXEL_FORMAT_RGB = 134217728;
    public static final int FC2_PIXEL_FORMAT_RGB16 = 33554432;
    public static final int FC2_PIXEL_FORMAT_RGB8 = 134217728;
    public static final int FC2_PIXEL_FORMAT_RGBU = 1073741826;
    public static final int FC2_PIXEL_FORMAT_S_MONO16 = 16777216;
    public static final int FC2_PIXEL_FORMAT_S_RGB16 = 8388608;
    public static final int FC2_PNG = 6;
    public static final int FC2_PPM = 1;
    public static final int FC2_PROPERTY_TYPE_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int FC2_RAW = 7;
    public static final int FC2_REMOVAL = 2;
    public static final int FC2_RIGOROUS = 5;
    public static final int FC2_SATURATION = 5;
    public static final int FC2_SHARPNESS = 2;
    public static final int FC2_SHUTTER = 12;
    public static final int FC2_STATISTICS_BLUE = 3;
    public static final int FC2_STATISTICS_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int FC2_STATISTICS_GREEN = 2;
    public static final int FC2_STATISTICS_GREY = 0;
    public static final int FC2_STATISTICS_HUE = 4;
    public static final int FC2_STATISTICS_LIGHTNESS = 6;
    public static final int FC2_STATISTICS_RED = 1;
    public static final int FC2_STATISTICS_SATURATION = 5;
    public static final int FC2_TEMPERATURE = 17;
    public static final int FC2_TIFF = 5;
    public static final int FC2_TIFF_ADOBE_DEFLATE = 4;
    public static final int FC2_TIFF_CCITTFAX3 = 5;
    public static final int FC2_TIFF_CCITTFAX4 = 6;
    public static final int FC2_TIFF_DEFLATE = 3;
    public static final int FC2_TIFF_JPEG = 8;
    public static final int FC2_TIFF_LZW = 7;
    public static final int FC2_TIFF_NONE = 1;
    public static final int FC2_TIFF_PACKBITS = 2;
    public static final int FC2_TILT = 11;
    public static final int FC2_TIMEOUT_INFINITE = -1;
    public static final int FC2_TIMEOUT_NONE = 0;
    public static final int FC2_TIMEOUT_UNSPECIFIED = -2;
    public static final int FC2_TRIGGER_DELAY = 15;
    public static final int FC2_TRIGGER_MODE = 14;
    public static final int FC2_UNKNOWN_OS = 5;
    public static final int FC2_UNSPECIFIED_GRAB_MODE = 2;
    public static final int FC2_UNSPECIFIED_PIXEL_FORMAT = 0;
    public static final int FC2_UNSPECIFIED_PROPERTY_TYPE = 18;
    public static final int FC2_VIDEOMODE_1024x768RGB = 12;
    public static final int FC2_VIDEOMODE_1024x768Y16 = 14;
    public static final int FC2_VIDEOMODE_1024x768Y8 = 13;
    public static final int FC2_VIDEOMODE_1024x768YUV422 = 11;
    public static final int FC2_VIDEOMODE_1280x960RGB = 16;
    public static final int FC2_VIDEOMODE_1280x960Y16 = 18;
    public static final int FC2_VIDEOMODE_1280x960Y8 = 17;
    public static final int FC2_VIDEOMODE_1280x960YUV422 = 15;
    public static final int FC2_VIDEOMODE_1600x1200RGB = 20;
    public static final int FC2_VIDEOMODE_1600x1200Y16 = 22;
    public static final int FC2_VIDEOMODE_1600x1200Y8 = 21;
    public static final int FC2_VIDEOMODE_1600x1200YUV422 = 19;
    public static final int FC2_VIDEOMODE_160x120YUV444 = 0;
    public static final int FC2_VIDEOMODE_320x240YUV422 = 1;
    public static final int FC2_VIDEOMODE_640x480RGB = 4;
    public static final int FC2_VIDEOMODE_640x480Y16 = 6;
    public static final int FC2_VIDEOMODE_640x480Y8 = 5;
    public static final int FC2_VIDEOMODE_640x480YUV411 = 2;
    public static final int FC2_VIDEOMODE_640x480YUV422 = 3;
    public static final int FC2_VIDEOMODE_800x600RGB = 8;
    public static final int FC2_VIDEOMODE_800x600Y16 = 10;
    public static final int FC2_VIDEOMODE_800x600Y8 = 9;
    public static final int FC2_VIDEOMODE_800x600YUV422 = 7;
    public static final int FC2_VIDEOMODE_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int FC2_VIDEOMODE_FORMAT7 = 23;
    public static final int FC2_WHITE_BALANCE = 3;
    public static final int FC2_WINDOWS_X64 = 1;
    public static final int FC2_WINDOWS_X86 = 0;
    public static final int FC2_ZOOM = 9;
    public static final int FULL_32BIT_VALUE = Integer.MAX_VALUE;
    public static final int MAX_STRING_LENGTH = 512;
    public static final int TRUE = 1;

    @Name({"void"})
    @Namespace
    @Opaque
    public static class fc2AVIContext extends Pointer {
        public fc2AVIContext() {
            super((Pointer) null);
        }

        public fc2AVIContext(Pointer p) {
            super(p);
        }
    }

    public static class fc2AVIOption extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float frameRate();

        public native fc2AVIOption frameRate(float f);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2AVIOption reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        static {
            Loader.load();
        }

        public fc2AVIOption() {
            super((Pointer) null);
            allocate();
        }

        public fc2AVIOption(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2AVIOption(Pointer p) {
            super(p);
        }

        public fc2AVIOption position(long position) {
            return (fc2AVIOption) super.position(position);
        }
    }

    @Name({"void"})
    @Namespace
    @Opaque
    public static class fc2CallbackHandle extends Pointer {
        public fc2CallbackHandle() {
            super((Pointer) null);
        }

        public fc2CallbackHandle(Pointer p) {
            super(p);
        }
    }

    public static class fc2CameraInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int applicationIPAddress();

        public native fc2CameraInfo applicationIPAddress(int i);

        @Cast({"unsigned int"})
        public native int applicationPort();

        public native fc2CameraInfo applicationPort(int i);

        @Cast({"fc2BayerTileFormat"})
        public native int bayerTileFormat();

        public native fc2CameraInfo bayerTileFormat(int i);

        public native fc2CameraInfo busNumber(short s);

        @Cast({"unsigned short"})
        public native short busNumber();

        @Cast({"unsigned int"})
        public native int ccpStatus();

        public native fc2CameraInfo ccpStatus(int i);

        public native fc2CameraInfo configROM(fc2ConfigROM org_bytedeco_javacpp_FlyCapture2_C_fc2ConfigROM);

        @ByRef
        public native fc2ConfigROM configROM();

        public native fc2CameraInfo defaultGateway(fc2IPAddress org_bytedeco_javacpp_FlyCapture2_C_fc2IPAddress);

        @ByRef
        public native fc2IPAddress defaultGateway();

        @Cast({"char"})
        public native byte driverName(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer driverName();

        public native fc2CameraInfo driverName(int i, byte b);

        @Cast({"fc2DriverType"})
        public native int driverType();

        public native fc2CameraInfo driverType(int i);

        @Cast({"char"})
        public native byte firmwareBuildTime(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer firmwareBuildTime();

        public native fc2CameraInfo firmwareBuildTime(int i, byte b);

        @Cast({"char"})
        public native byte firmwareVersion(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer firmwareVersion();

        public native fc2CameraInfo firmwareVersion(int i, byte b);

        @Cast({"unsigned int"})
        public native int gigEMajorVersion();

        public native fc2CameraInfo gigEMajorVersion(int i);

        @Cast({"unsigned int"})
        public native int gigEMinorVersion();

        public native fc2CameraInfo gigEMinorVersion(int i);

        @Cast({"unsigned int"})
        public native int iidcVer();

        public native fc2CameraInfo iidcVer(int i);

        @Cast({"fc2InterfaceType"})
        public native int interfaceType();

        public native fc2CameraInfo interfaceType(int i);

        public native fc2CameraInfo ipAddress(fc2IPAddress org_bytedeco_javacpp_FlyCapture2_C_fc2IPAddress);

        @ByRef
        public native fc2IPAddress ipAddress();

        @Cast({"BOOL"})
        public native int isColorCamera();

        public native fc2CameraInfo isColorCamera(int i);

        public native fc2CameraInfo macAddress(fc2MACAddress org_bytedeco_javacpp_FlyCapture2_C_fc2MACAddress);

        @ByRef
        public native fc2MACAddress macAddress();

        @Cast({"fc2BusSpeed"})
        public native int maximumBusSpeed();

        public native fc2CameraInfo maximumBusSpeed(int i);

        @Cast({"char"})
        public native byte modelName(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer modelName();

        public native fc2CameraInfo modelName(int i, byte b);

        public native fc2CameraInfo nodeNumber(short s);

        @Cast({"unsigned short"})
        public native short nodeNumber();

        @Cast({"fc2PCIeBusSpeed"})
        public native int pcieBusSpeed();

        public native fc2CameraInfo pcieBusSpeed(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2CameraInfo reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"char"})
        public native byte sensorInfo(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer sensorInfo();

        public native fc2CameraInfo sensorInfo(int i, byte b);

        @Cast({"char"})
        public native byte sensorResolution(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer sensorResolution();

        public native fc2CameraInfo sensorResolution(int i, byte b);

        @Cast({"unsigned int"})
        public native int serialNumber();

        public native fc2CameraInfo serialNumber(int i);

        public native fc2CameraInfo subnetMask(fc2IPAddress org_bytedeco_javacpp_FlyCapture2_C_fc2IPAddress);

        @ByRef
        public native fc2IPAddress subnetMask();

        @Cast({"char"})
        public native byte userDefinedName(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer userDefinedName();

        public native fc2CameraInfo userDefinedName(int i, byte b);

        @Cast({"char"})
        public native byte vendorName(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer vendorName();

        public native fc2CameraInfo vendorName(int i, byte b);

        @Cast({"char"})
        public native byte xmlURL1(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer xmlURL1();

        public native fc2CameraInfo xmlURL1(int i, byte b);

        @Cast({"char"})
        public native byte xmlURL2(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer xmlURL2();

        public native fc2CameraInfo xmlURL2(int i, byte b);

        static {
            Loader.load();
        }

        public fc2CameraInfo() {
            super((Pointer) null);
            allocate();
        }

        public fc2CameraInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2CameraInfo(Pointer p) {
            super(p);
        }

        public fc2CameraInfo position(long position) {
            return (fc2CameraInfo) super.position(position);
        }
    }

    public static class fc2Config extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"fc2BusSpeed"})
        public native int asyncBusSpeed();

        public native fc2Config asyncBusSpeed(int i);

        @Cast({"fc2BandwidthAllocation"})
        public native int bandwidthAllocation();

        public native fc2Config bandwidthAllocation(int i);

        @Cast({"fc2GrabMode"})
        public native int grabMode();

        public native fc2Config grabMode(int i);

        public native int grabTimeout();

        public native fc2Config grabTimeout(int i);

        @Cast({"fc2BusSpeed"})
        public native int isochBusSpeed();

        public native fc2Config isochBusSpeed(int i);

        @Cast({"unsigned int"})
        public native int minNumImageNotifications();

        public native fc2Config minNumImageNotifications(int i);

        @Cast({"unsigned int"})
        public native int numBuffers();

        public native fc2Config numBuffers(int i);

        @Cast({"unsigned int"})
        public native int numImageNotifications();

        public native fc2Config numImageNotifications(int i);

        @Cast({"unsigned int"})
        public native int registerTimeout();

        public native fc2Config registerTimeout(int i);

        @Cast({"unsigned int"})
        public native int registerTimeoutRetries();

        public native fc2Config registerTimeoutRetries(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2Config reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        static {
            Loader.load();
        }

        public fc2Config() {
            super((Pointer) null);
            allocate();
        }

        public fc2Config(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2Config(Pointer p) {
            super(p);
        }

        public fc2Config position(long position) {
            return (fc2Config) super.position(position);
        }
    }

    public static class fc2ConfigROM extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int chipIdHi();

        public native fc2ConfigROM chipIdHi(int i);

        @Cast({"unsigned int"})
        public native int chipIdLo();

        public native fc2ConfigROM chipIdLo(int i);

        @Cast({"unsigned int"})
        public native int nodeVendorId();

        public native fc2ConfigROM nodeVendorId(int i);

        @Cast({"char"})
        public native byte pszKeyword(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer pszKeyword();

        public native fc2ConfigROM pszKeyword(int i, byte b);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2ConfigROM reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int unitSWVer();

        public native fc2ConfigROM unitSWVer(int i);

        @Cast({"unsigned int"})
        public native int unitSpecId();

        public native fc2ConfigROM unitSpecId(int i);

        @Cast({"unsigned int"})
        public native int unitSubSWVer();

        public native fc2ConfigROM unitSubSWVer(int i);

        @Cast({"unsigned int"})
        public native int vendorUniqueInfo_0();

        public native fc2ConfigROM vendorUniqueInfo_0(int i);

        @Cast({"unsigned int"})
        public native int vendorUniqueInfo_1();

        public native fc2ConfigROM vendorUniqueInfo_1(int i);

        @Cast({"unsigned int"})
        public native int vendorUniqueInfo_2();

        public native fc2ConfigROM vendorUniqueInfo_2(int i);

        @Cast({"unsigned int"})
        public native int vendorUniqueInfo_3();

        public native fc2ConfigROM vendorUniqueInfo_3(int i);

        static {
            Loader.load();
        }

        public fc2ConfigROM() {
            super((Pointer) null);
            allocate();
        }

        public fc2ConfigROM(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2ConfigROM(Pointer p) {
            super(p);
        }

        public fc2ConfigROM position(long position) {
            return (fc2ConfigROM) super.position(position);
        }
    }

    @Name({"void"})
    @Namespace
    @Opaque
    public static class fc2Context extends Pointer {
        public fc2Context() {
            super((Pointer) null);
        }

        public fc2Context(Pointer p) {
            super(p);
        }
    }

    public static class fc2EmbeddedImageInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native fc2EmbeddedImageInfo GPIOPinState(fc2EmbeddedImageInfoProperty org_bytedeco_javacpp_FlyCapture2_C_fc2EmbeddedImageInfoProperty);

        @ByRef
        public native fc2EmbeddedImageInfoProperty GPIOPinState();

        public native fc2EmbeddedImageInfo ROIPosition(fc2EmbeddedImageInfoProperty org_bytedeco_javacpp_FlyCapture2_C_fc2EmbeddedImageInfoProperty);

        @ByRef
        public native fc2EmbeddedImageInfoProperty ROIPosition();

        public native fc2EmbeddedImageInfo brightness(fc2EmbeddedImageInfoProperty org_bytedeco_javacpp_FlyCapture2_C_fc2EmbeddedImageInfoProperty);

        @ByRef
        public native fc2EmbeddedImageInfoProperty brightness();

        public native fc2EmbeddedImageInfo exposure(fc2EmbeddedImageInfoProperty org_bytedeco_javacpp_FlyCapture2_C_fc2EmbeddedImageInfoProperty);

        @ByRef
        public native fc2EmbeddedImageInfoProperty exposure();

        public native fc2EmbeddedImageInfo frameCounter(fc2EmbeddedImageInfoProperty org_bytedeco_javacpp_FlyCapture2_C_fc2EmbeddedImageInfoProperty);

        @ByRef
        public native fc2EmbeddedImageInfoProperty frameCounter();

        public native fc2EmbeddedImageInfo gain(fc2EmbeddedImageInfoProperty org_bytedeco_javacpp_FlyCapture2_C_fc2EmbeddedImageInfoProperty);

        @ByRef
        public native fc2EmbeddedImageInfoProperty gain();

        public native fc2EmbeddedImageInfo shutter(fc2EmbeddedImageInfoProperty org_bytedeco_javacpp_FlyCapture2_C_fc2EmbeddedImageInfoProperty);

        @ByRef
        public native fc2EmbeddedImageInfoProperty shutter();

        public native fc2EmbeddedImageInfo strobePattern(fc2EmbeddedImageInfoProperty org_bytedeco_javacpp_FlyCapture2_C_fc2EmbeddedImageInfoProperty);

        @ByRef
        public native fc2EmbeddedImageInfoProperty strobePattern();

        public native fc2EmbeddedImageInfo timestamp(fc2EmbeddedImageInfoProperty org_bytedeco_javacpp_FlyCapture2_C_fc2EmbeddedImageInfoProperty);

        @ByRef
        public native fc2EmbeddedImageInfoProperty timestamp();

        public native fc2EmbeddedImageInfo whiteBalance(fc2EmbeddedImageInfoProperty org_bytedeco_javacpp_FlyCapture2_C_fc2EmbeddedImageInfoProperty);

        @ByRef
        public native fc2EmbeddedImageInfoProperty whiteBalance();

        static {
            Loader.load();
        }

        public fc2EmbeddedImageInfo() {
            super((Pointer) null);
            allocate();
        }

        public fc2EmbeddedImageInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2EmbeddedImageInfo(Pointer p) {
            super(p);
        }

        public fc2EmbeddedImageInfo position(long position) {
            return (fc2EmbeddedImageInfo) super.position(position);
        }
    }

    public static class fc2EmbeddedImageInfoProperty extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"BOOL"})
        public native int available();

        public native fc2EmbeddedImageInfoProperty available(int i);

        @Cast({"BOOL"})
        public native int onOff();

        public native fc2EmbeddedImageInfoProperty onOff(int i);

        static {
            Loader.load();
        }

        public fc2EmbeddedImageInfoProperty() {
            super((Pointer) null);
            allocate();
        }

        public fc2EmbeddedImageInfoProperty(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2EmbeddedImageInfoProperty(Pointer p) {
            super(p);
        }

        public fc2EmbeddedImageInfoProperty position(long position) {
            return (fc2EmbeddedImageInfoProperty) super.position(position);
        }
    }

    public static class fc2Format7ImageSettings extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int height();

        public native fc2Format7ImageSettings height(int i);

        @Cast({"fc2Mode"})
        public native int mode();

        public native fc2Format7ImageSettings mode(int i);

        @Cast({"unsigned int"})
        public native int offsetX();

        public native fc2Format7ImageSettings offsetX(int i);

        @Cast({"unsigned int"})
        public native int offsetY();

        public native fc2Format7ImageSettings offsetY(int i);

        @Cast({"fc2PixelFormat"})
        public native int pixelFormat();

        public native fc2Format7ImageSettings pixelFormat(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2Format7ImageSettings reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int width();

        public native fc2Format7ImageSettings width(int i);

        static {
            Loader.load();
        }

        public fc2Format7ImageSettings() {
            super((Pointer) null);
            allocate();
        }

        public fc2Format7ImageSettings(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2Format7ImageSettings(Pointer p) {
            super(p);
        }

        public fc2Format7ImageSettings position(long position) {
            return (fc2Format7ImageSettings) super.position(position);
        }
    }

    public static class fc2Format7Info extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int imageHStepSize();

        public native fc2Format7Info imageHStepSize(int i);

        @Cast({"unsigned int"})
        public native int imageVStepSize();

        public native fc2Format7Info imageVStepSize(int i);

        @Cast({"unsigned int"})
        public native int maxHeight();

        public native fc2Format7Info maxHeight(int i);

        @Cast({"unsigned int"})
        public native int maxPacketSize();

        public native fc2Format7Info maxPacketSize(int i);

        @Cast({"unsigned int"})
        public native int maxWidth();

        public native fc2Format7Info maxWidth(int i);

        @Cast({"unsigned int"})
        public native int minPacketSize();

        public native fc2Format7Info minPacketSize(int i);

        @Cast({"fc2Mode"})
        public native int mode();

        public native fc2Format7Info mode(int i);

        @Cast({"unsigned int"})
        public native int offsetHStepSize();

        public native fc2Format7Info offsetHStepSize(int i);

        @Cast({"unsigned int"})
        public native int offsetVStepSize();

        public native fc2Format7Info offsetVStepSize(int i);

        @Cast({"unsigned int"})
        public native int packetSize();

        public native fc2Format7Info packetSize(int i);

        public native float percentage();

        public native fc2Format7Info percentage(float f);

        @Cast({"unsigned int"})
        public native int pixelFormatBitField();

        public native fc2Format7Info pixelFormatBitField(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2Format7Info reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int vendorPixelFormatBitField();

        public native fc2Format7Info vendorPixelFormatBitField(int i);

        static {
            Loader.load();
        }

        public fc2Format7Info() {
            super((Pointer) null);
            allocate();
        }

        public fc2Format7Info(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2Format7Info(Pointer p) {
            super(p);
        }

        public fc2Format7Info position(long position) {
            return (fc2Format7Info) super.position(position);
        }
    }

    public static class fc2Format7PacketInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int maxBytesPerPacket();

        public native fc2Format7PacketInfo maxBytesPerPacket(int i);

        @Cast({"unsigned int"})
        public native int recommendedBytesPerPacket();

        public native fc2Format7PacketInfo recommendedBytesPerPacket(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2Format7PacketInfo reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int unitBytesPerPacket();

        public native fc2Format7PacketInfo unitBytesPerPacket(int i);

        static {
            Loader.load();
        }

        public fc2Format7PacketInfo() {
            super((Pointer) null);
            allocate();
        }

        public fc2Format7PacketInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2Format7PacketInfo(Pointer p) {
            super(p);
        }

        public fc2Format7PacketInfo position(long position) {
            return (fc2Format7PacketInfo) super.position(position);
        }
    }

    public static class fc2GigEConfig extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"BOOL"})
        public native int enablePacketResend();

        public native fc2GigEConfig enablePacketResend(int i);

        @Cast({"unsigned int"})
        public native int maxPacketsToResend();

        public native fc2GigEConfig maxPacketsToResend(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2GigEConfig reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int timeoutForPacketResend();

        public native fc2GigEConfig timeoutForPacketResend(int i);

        static {
            Loader.load();
        }

        public fc2GigEConfig() {
            super((Pointer) null);
            allocate();
        }

        public fc2GigEConfig(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2GigEConfig(Pointer p) {
            super(p);
        }

        public fc2GigEConfig position(long position) {
            return (fc2GigEConfig) super.position(position);
        }
    }

    public static class fc2GigEImageSettings extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int height();

        public native fc2GigEImageSettings height(int i);

        @Cast({"unsigned int"})
        public native int offsetX();

        public native fc2GigEImageSettings offsetX(int i);

        @Cast({"unsigned int"})
        public native int offsetY();

        public native fc2GigEImageSettings offsetY(int i);

        @Cast({"fc2PixelFormat"})
        public native int pixelFormat();

        public native fc2GigEImageSettings pixelFormat(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2GigEImageSettings reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int width();

        public native fc2GigEImageSettings width(int i);

        static {
            Loader.load();
        }

        public fc2GigEImageSettings() {
            super((Pointer) null);
            allocate();
        }

        public fc2GigEImageSettings(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2GigEImageSettings(Pointer p) {
            super(p);
        }

        public fc2GigEImageSettings position(long position) {
            return (fc2GigEImageSettings) super.position(position);
        }
    }

    public static class fc2GigEImageSettingsInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int imageHStepSize();

        public native fc2GigEImageSettingsInfo imageHStepSize(int i);

        @Cast({"unsigned int"})
        public native int imageVStepSize();

        public native fc2GigEImageSettingsInfo imageVStepSize(int i);

        @Cast({"unsigned int"})
        public native int maxHeight();

        public native fc2GigEImageSettingsInfo maxHeight(int i);

        @Cast({"unsigned int"})
        public native int maxWidth();

        public native fc2GigEImageSettingsInfo maxWidth(int i);

        @Cast({"unsigned int"})
        public native int offsetHStepSize();

        public native fc2GigEImageSettingsInfo offsetHStepSize(int i);

        @Cast({"unsigned int"})
        public native int offsetVStepSize();

        public native fc2GigEImageSettingsInfo offsetVStepSize(int i);

        @Cast({"unsigned int"})
        public native int pixelFormatBitField();

        public native fc2GigEImageSettingsInfo pixelFormatBitField(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2GigEImageSettingsInfo reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int vendorPixelFormatBitField();

        public native fc2GigEImageSettingsInfo vendorPixelFormatBitField(int i);

        static {
            Loader.load();
        }

        public fc2GigEImageSettingsInfo() {
            super((Pointer) null);
            allocate();
        }

        public fc2GigEImageSettingsInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2GigEImageSettingsInfo(Pointer p) {
            super(p);
        }

        public fc2GigEImageSettingsInfo position(long position) {
            return (fc2GigEImageSettingsInfo) super.position(position);
        }
    }

    public static class fc2GigEProperty extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"BOOL"})
        public native int isReadable();

        public native fc2GigEProperty isReadable(int i);

        @Cast({"BOOL"})
        public native int isWritable();

        public native fc2GigEProperty isWritable(int i);

        @Cast({"unsigned int"})
        public native int max();

        public native fc2GigEProperty max(int i);

        @Cast({"unsigned int"})
        public native int min();

        public native fc2GigEProperty min(int i);

        @Cast({"fc2GigEPropertyType"})
        public native int propType();

        public native fc2GigEProperty propType(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2GigEProperty reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int value();

        public native fc2GigEProperty value(int i);

        static {
            Loader.load();
        }

        public fc2GigEProperty() {
            super((Pointer) null);
            allocate();
        }

        public fc2GigEProperty(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2GigEProperty(Pointer p) {
            super(p);
        }

        public fc2GigEProperty position(long position) {
            return (fc2GigEProperty) super.position(position);
        }
    }

    public static class fc2GigEStreamChannel extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native fc2GigEStreamChannel destinationIpAddress(fc2IPAddress org_bytedeco_javacpp_FlyCapture2_C_fc2IPAddress);

        @ByRef
        public native fc2IPAddress destinationIpAddress();

        @Cast({"BOOL"})
        public native int doNotFragment();

        public native fc2GigEStreamChannel doNotFragment(int i);

        @Cast({"unsigned int"})
        public native int hostPost();

        public native fc2GigEStreamChannel hostPost(int i);

        @Cast({"unsigned int"})
        public native int interPacketDelay();

        public native fc2GigEStreamChannel interPacketDelay(int i);

        @Cast({"unsigned int"})
        public native int networkInterfaceIndex();

        public native fc2GigEStreamChannel networkInterfaceIndex(int i);

        @Cast({"unsigned int"})
        public native int packetSize();

        public native fc2GigEStreamChannel packetSize(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2GigEStreamChannel reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int sourcePort();

        public native fc2GigEStreamChannel sourcePort(int i);

        static {
            Loader.load();
        }

        public fc2GigEStreamChannel() {
            super((Pointer) null);
            allocate();
        }

        public fc2GigEStreamChannel(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2GigEStreamChannel(Pointer p) {
            super(p);
        }

        public fc2GigEStreamChannel position(long position) {
            return (fc2GigEStreamChannel) super.position(position);
        }
    }

    @Name({"void"})
    @Namespace
    @Opaque
    public static class fc2GuiContext extends Pointer {
        public fc2GuiContext() {
            super((Pointer) null);
        }

        public fc2GuiContext(Pointer p) {
            super(p);
        }
    }

    public static class fc2H264Option extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int bitrate();

        public native fc2H264Option bitrate(int i);

        public native float frameRate();

        public native fc2H264Option frameRate(float f);

        @Cast({"unsigned int"})
        public native int height();

        public native fc2H264Option height(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2H264Option reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int width();

        public native fc2H264Option width(int i);

        static {
            Loader.load();
        }

        public fc2H264Option() {
            super((Pointer) null);
            allocate();
        }

        public fc2H264Option(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2H264Option(Pointer p) {
            super(p);
        }

        public fc2H264Option position(long position) {
            return (fc2H264Option) super.position(position);
        }
    }

    public static class fc2IPAddress extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned char"})
        public native byte octets(int i);

        @MemberGetter
        @Cast({"unsigned char*"})
        public native BytePointer octets();

        public native fc2IPAddress octets(int i, byte b);

        static {
            Loader.load();
        }

        public fc2IPAddress() {
            super((Pointer) null);
            allocate();
        }

        public fc2IPAddress(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2IPAddress(Pointer p) {
            super(p);
        }

        public fc2IPAddress position(long position) {
            return (fc2IPAddress) super.position(position);
        }
    }

    public static class fc2Image extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"fc2BayerTileFormat"})
        public native int bayerFormat();

        public native fc2Image bayerFormat(int i);

        @Cast({"unsigned int"})
        public native int cols();

        public native fc2Image cols(int i);

        @Cast({"unsigned int"})
        public native int dataSize();

        public native fc2Image dataSize(int i);

        @Cast({"fc2PixelFormat"})
        public native int format();

        public native fc2Image format(int i);

        public native fc2Image imageImpl(fc2ImageImpl org_bytedeco_javacpp_FlyCapture2_C_fc2ImageImpl);

        public native fc2ImageImpl imageImpl();

        @Cast({"unsigned char*"})
        public native BytePointer pData();

        public native fc2Image pData(BytePointer bytePointer);

        @Cast({"unsigned int"})
        public native int receivedDataSize();

        public native fc2Image receivedDataSize(int i);

        @Cast({"unsigned int"})
        public native int rows();

        public native fc2Image rows(int i);

        @Cast({"unsigned int"})
        public native int stride();

        public native fc2Image stride(int i);

        static {
            Loader.load();
        }

        public fc2Image() {
            super((Pointer) null);
            allocate();
        }

        public fc2Image(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2Image(Pointer p) {
            super(p);
        }

        public fc2Image position(long position) {
            return (fc2Image) super.position(position);
        }
    }

    @Name({"void"})
    @Namespace
    @Opaque
    public static class fc2ImageImpl extends Pointer {
        public fc2ImageImpl() {
            super((Pointer) null);
        }

        public fc2ImageImpl(Pointer p) {
            super(p);
        }
    }

    public static class fc2ImageMetadata extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int embeddedBrightness();

        public native fc2ImageMetadata embeddedBrightness(int i);

        @Cast({"unsigned int"})
        public native int embeddedExposure();

        public native fc2ImageMetadata embeddedExposure(int i);

        @Cast({"unsigned int"})
        public native int embeddedFrameCounter();

        public native fc2ImageMetadata embeddedFrameCounter(int i);

        @Cast({"unsigned int"})
        public native int embeddedGPIOPinState();

        public native fc2ImageMetadata embeddedGPIOPinState(int i);

        @Cast({"unsigned int"})
        public native int embeddedGain();

        public native fc2ImageMetadata embeddedGain(int i);

        @Cast({"unsigned int"})
        public native int embeddedROIPosition();

        public native fc2ImageMetadata embeddedROIPosition(int i);

        @Cast({"unsigned int"})
        public native int embeddedShutter();

        public native fc2ImageMetadata embeddedShutter(int i);

        @Cast({"unsigned int"})
        public native int embeddedStrobePattern();

        public native fc2ImageMetadata embeddedStrobePattern(int i);

        @Cast({"unsigned int"})
        public native int embeddedTimeStamp();

        public native fc2ImageMetadata embeddedTimeStamp(int i);

        @Cast({"unsigned int"})
        public native int embeddedWhiteBalance();

        public native fc2ImageMetadata embeddedWhiteBalance(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2ImageMetadata reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        static {
            Loader.load();
        }

        public fc2ImageMetadata() {
            super((Pointer) null);
            allocate();
        }

        public fc2ImageMetadata(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2ImageMetadata(Pointer p) {
            super(p);
        }

        public fc2ImageMetadata position(long position) {
            return (fc2ImageMetadata) super.position(position);
        }
    }

    @Name({"void"})
    @Namespace
    @Opaque
    public static class fc2ImageStatisticsContext extends Pointer {
        public fc2ImageStatisticsContext() {
            super((Pointer) null);
        }

        public fc2ImageStatisticsContext(Pointer p) {
            super(p);
        }
    }

    public static class fc2JPEGOption extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"BOOL"})
        public native int progressive();

        public native fc2JPEGOption progressive(int i);

        @Cast({"unsigned int"})
        public native int quality();

        public native fc2JPEGOption quality(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2JPEGOption reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        static {
            Loader.load();
        }

        public fc2JPEGOption() {
            super((Pointer) null);
            allocate();
        }

        public fc2JPEGOption(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2JPEGOption(Pointer p) {
            super(p);
        }

        public fc2JPEGOption position(long position) {
            return (fc2JPEGOption) super.position(position);
        }
    }

    public static class fc2JPG2Option extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int quality();

        public native fc2JPG2Option quality(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2JPG2Option reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        static {
            Loader.load();
        }

        public fc2JPG2Option() {
            super((Pointer) null);
            allocate();
        }

        public fc2JPG2Option(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2JPG2Option(Pointer p) {
            super(p);
        }

        public fc2JPG2Option position(long position) {
            return (fc2JPG2Option) super.position(position);
        }
    }

    public static class fc2LUTData extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"BOOL"})
        public native int enabled();

        public native fc2LUTData enabled(int i);

        @Cast({"unsigned int"})
        public native int inputBitDepth();

        public native fc2LUTData inputBitDepth(int i);

        @Cast({"unsigned int"})
        public native int numBanks();

        public native fc2LUTData numBanks(int i);

        @Cast({"unsigned int"})
        public native int numChannels();

        public native fc2LUTData numChannels(int i);

        @Cast({"unsigned int"})
        public native int numEntries();

        public native fc2LUTData numEntries(int i);

        @Cast({"unsigned int"})
        public native int outputBitDepth();

        public native fc2LUTData outputBitDepth(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2LUTData reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"BOOL"})
        public native int supported();

        public native fc2LUTData supported(int i);

        static {
            Loader.load();
        }

        public fc2LUTData() {
            super((Pointer) null);
            allocate();
        }

        public fc2LUTData(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2LUTData(Pointer p) {
            super(p);
        }

        public fc2LUTData position(long position) {
            return (fc2LUTData) super.position(position);
        }
    }

    public static class fc2MACAddress extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned char"})
        public native byte octets(int i);

        @MemberGetter
        @Cast({"unsigned char*"})
        public native BytePointer octets();

        public native fc2MACAddress octets(int i, byte b);

        static {
            Loader.load();
        }

        public fc2MACAddress() {
            super((Pointer) null);
            allocate();
        }

        public fc2MACAddress(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2MACAddress(Pointer p) {
            super(p);
        }

        public fc2MACAddress position(long position) {
            return (fc2MACAddress) super.position(position);
        }
    }

    public static class fc2MJPGOption extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float frameRate();

        public native fc2MJPGOption frameRate(float f);

        @Cast({"unsigned int"})
        public native int quality();

        public native fc2MJPGOption quality(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2MJPGOption reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        static {
            Loader.load();
        }

        public fc2MJPGOption() {
            super((Pointer) null);
            allocate();
        }

        public fc2MJPGOption(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2MJPGOption(Pointer p) {
            super(p);
        }

        public fc2MJPGOption position(long position) {
            return (fc2MJPGOption) super.position(position);
        }
    }

    public static class fc2PGMOption extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"BOOL"})
        public native int binaryFile();

        public native fc2PGMOption binaryFile(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2PGMOption reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        static {
            Loader.load();
        }

        public fc2PGMOption() {
            super((Pointer) null);
            allocate();
        }

        public fc2PGMOption(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2PGMOption(Pointer p) {
            super(p);
        }

        public fc2PGMOption position(long position) {
            return (fc2PGMOption) super.position(position);
        }
    }

    public static class fc2PGRGuid extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int value(int i);

        public native fc2PGRGuid value(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer value();

        static {
            Loader.load();
        }

        public fc2PGRGuid() {
            super((Pointer) null);
            allocate();
        }

        public fc2PGRGuid(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2PGRGuid(Pointer p) {
            super(p);
        }

        public fc2PGRGuid position(long position) {
            return (fc2PGRGuid) super.position(position);
        }
    }

    public static class fc2PNGOption extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int compressionLevel();

        public native fc2PNGOption compressionLevel(int i);

        @Cast({"BOOL"})
        public native int interlaced();

        public native fc2PNGOption interlaced(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2PNGOption reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        static {
            Loader.load();
        }

        public fc2PNGOption() {
            super((Pointer) null);
            allocate();
        }

        public fc2PNGOption(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2PNGOption(Pointer p) {
            super(p);
        }

        public fc2PNGOption position(long position) {
            return (fc2PNGOption) super.position(position);
        }
    }

    public static class fc2PPMOption extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"BOOL"})
        public native int binaryFile();

        public native fc2PPMOption binaryFile(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2PPMOption reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        static {
            Loader.load();
        }

        public fc2PPMOption() {
            super((Pointer) null);
            allocate();
        }

        public fc2PPMOption(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2PPMOption(Pointer p) {
            super(p);
        }

        public fc2PPMOption position(long position) {
            return (fc2PPMOption) super.position(position);
        }
    }

    @Name({"fc2TriggerDelay"})
    public static class fc2Property extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"BOOL"})
        public native int absControl();

        public native fc2Property absControl(int i);

        public native float absValue();

        public native fc2Property absValue(float f);

        @Cast({"BOOL"})
        public native int autoManualMode();

        public native fc2Property autoManualMode(int i);

        @Cast({"BOOL"})
        public native int onOff();

        public native fc2Property onOff(int i);

        @Cast({"BOOL"})
        public native int onePush();

        public native fc2Property onePush(int i);

        @Cast({"BOOL"})
        public native int present();

        public native fc2Property present(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2Property reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"fc2PropertyType"})
        public native int type();

        public native fc2Property type(int i);

        @Cast({"unsigned int"})
        public native int valueA();

        public native fc2Property valueA(int i);

        @Cast({"unsigned int"})
        public native int valueB();

        public native fc2Property valueB(int i);

        static {
            Loader.load();
        }

        public fc2Property() {
            super((Pointer) null);
            allocate();
        }

        public fc2Property(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2Property(Pointer p) {
            super(p);
        }

        public fc2Property position(long position) {
            return (fc2Property) super.position(position);
        }
    }

    @Name({"fc2TriggerDelayInfo"})
    public static class fc2PropertyInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float absMax();

        public native fc2PropertyInfo absMax(float f);

        public native float absMin();

        public native fc2PropertyInfo absMin(float f);

        @Cast({"BOOL"})
        public native int absValSupported();

        public native fc2PropertyInfo absValSupported(int i);

        @Cast({"BOOL"})
        public native int autoSupported();

        public native fc2PropertyInfo autoSupported(int i);

        @Cast({"BOOL"})
        public native int manualSupported();

        public native fc2PropertyInfo manualSupported(int i);

        @Cast({"unsigned int"})
        public native int max();

        public native fc2PropertyInfo max(int i);

        @Cast({"unsigned int"})
        public native int min();

        public native fc2PropertyInfo min(int i);

        @Cast({"BOOL"})
        public native int onOffSupported();

        public native fc2PropertyInfo onOffSupported(int i);

        @Cast({"BOOL"})
        public native int onePushSupported();

        public native fc2PropertyInfo onePushSupported(int i);

        @Cast({"char"})
        public native byte pUnitAbbr(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer pUnitAbbr();

        public native fc2PropertyInfo pUnitAbbr(int i, byte b);

        @Cast({"char"})
        public native byte pUnits(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer pUnits();

        public native fc2PropertyInfo pUnits(int i, byte b);

        @Cast({"BOOL"})
        public native int present();

        public native fc2PropertyInfo present(int i);

        @Cast({"BOOL"})
        public native int readOutSupported();

        public native fc2PropertyInfo readOutSupported(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2PropertyInfo reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"fc2PropertyType"})
        public native int type();

        public native fc2PropertyInfo type(int i);

        static {
            Loader.load();
        }

        public fc2PropertyInfo() {
            super((Pointer) null);
            allocate();
        }

        public fc2PropertyInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2PropertyInfo(Pointer p) {
            super(p);
        }

        public fc2PropertyInfo position(long position) {
            return (fc2PropertyInfo) super.position(position);
        }
    }

    public static class fc2StrobeControl extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float delay();

        public native fc2StrobeControl delay(float f);

        public native float duration();

        public native fc2StrobeControl duration(float f);

        @Cast({"BOOL"})
        public native int onOff();

        public native fc2StrobeControl onOff(int i);

        @Cast({"unsigned int"})
        public native int polarity();

        public native fc2StrobeControl polarity(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2StrobeControl reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int source();

        public native fc2StrobeControl source(int i);

        static {
            Loader.load();
        }

        public fc2StrobeControl() {
            super((Pointer) null);
            allocate();
        }

        public fc2StrobeControl(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2StrobeControl(Pointer p) {
            super(p);
        }

        public fc2StrobeControl position(long position) {
            return (fc2StrobeControl) super.position(position);
        }
    }

    public static class fc2StrobeInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float maxValue();

        public native fc2StrobeInfo maxValue(float f);

        public native float minValue();

        public native fc2StrobeInfo minValue(float f);

        @Cast({"BOOL"})
        public native int onOffSupported();

        public native fc2StrobeInfo onOffSupported(int i);

        @Cast({"BOOL"})
        public native int polaritySupported();

        public native fc2StrobeInfo polaritySupported(int i);

        @Cast({"BOOL"})
        public native int present();

        public native fc2StrobeInfo present(int i);

        @Cast({"BOOL"})
        public native int readOutSupported();

        public native fc2StrobeInfo readOutSupported(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2StrobeInfo reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int source();

        public native fc2StrobeInfo source(int i);

        static {
            Loader.load();
        }

        public fc2StrobeInfo() {
            super((Pointer) null);
            allocate();
        }

        public fc2StrobeInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2StrobeInfo(Pointer p) {
            super(p);
        }

        public fc2StrobeInfo position(long position) {
            return (fc2StrobeInfo) super.position(position);
        }
    }

    public static class fc2SystemInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"fc2ByteOrder"})
        public native int byteOrder();

        public native fc2SystemInfo byteOrder(int i);

        @Cast({"char"})
        public native byte cpuDescription(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer cpuDescription();

        public native fc2SystemInfo cpuDescription(int i, byte b);

        @Cast({"char"})
        public native byte driverList(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer driverList();

        public native fc2SystemInfo driverList(int i, byte b);

        @Cast({"char"})
        public native byte gpuDescription(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer gpuDescription();

        public native fc2SystemInfo gpuDescription(int i, byte b);

        @Cast({"char"})
        public native byte libraryList(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer libraryList();

        public native fc2SystemInfo libraryList(int i, byte b);

        @Cast({"size_t"})
        public native long numCpuCores();

        public native fc2SystemInfo numCpuCores(long j);

        @Cast({"char"})
        public native byte osDescription(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer osDescription();

        public native fc2SystemInfo osDescription(int i, byte b);

        @Cast({"fc2OSType"})
        public native int osType();

        public native fc2SystemInfo osType(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2SystemInfo reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"size_t"})
        public native long screenHeight();

        public native fc2SystemInfo screenHeight(long j);

        @Cast({"size_t"})
        public native long screenWidth();

        public native fc2SystemInfo screenWidth(long j);

        @Cast({"size_t"})
        public native long sysMemSize();

        public native fc2SystemInfo sysMemSize(long j);

        static {
            Loader.load();
        }

        public fc2SystemInfo() {
            super((Pointer) null);
            allocate();
        }

        public fc2SystemInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2SystemInfo(Pointer p) {
            super(p);
        }

        public fc2SystemInfo position(long position) {
            return (fc2SystemInfo) super.position(position);
        }
    }

    public static class fc2TIFFOption extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"fc2TIFFCompressionMethod"})
        public native int compression();

        public native fc2TIFFOption compression(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2TIFFOption reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        static {
            Loader.load();
        }

        public fc2TIFFOption() {
            super((Pointer) null);
            allocate();
        }

        public fc2TIFFOption(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2TIFFOption(Pointer p) {
            super(p);
        }

        public fc2TIFFOption position(long position) {
            return (fc2TIFFOption) super.position(position);
        }
    }

    public static class fc2TimeStamp extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int cycleCount();

        public native fc2TimeStamp cycleCount(int i);

        @Cast({"unsigned int"})
        public native int cycleOffset();

        public native fc2TimeStamp cycleOffset(int i);

        @Cast({"unsigned int"})
        public native int cycleSeconds();

        public native fc2TimeStamp cycleSeconds(int i);

        @Cast({"unsigned int"})
        public native int microSeconds();

        public native fc2TimeStamp microSeconds(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2TimeStamp reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        public native long seconds();

        public native fc2TimeStamp seconds(long j);

        static {
            Loader.load();
        }

        public fc2TimeStamp() {
            super((Pointer) null);
            allocate();
        }

        public fc2TimeStamp(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2TimeStamp(Pointer p) {
            super(p);
        }

        public fc2TimeStamp position(long position) {
            return (fc2TimeStamp) super.position(position);
        }
    }

    public static class fc2TriggerMode extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int mode();

        public native fc2TriggerMode mode(int i);

        @Cast({"BOOL"})
        public native int onOff();

        public native fc2TriggerMode onOff(int i);

        @Cast({"unsigned int"})
        public native int parameter();

        public native fc2TriggerMode parameter(int i);

        @Cast({"unsigned int"})
        public native int polarity();

        public native fc2TriggerMode polarity(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2TriggerMode reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int source();

        public native fc2TriggerMode source(int i);

        static {
            Loader.load();
        }

        public fc2TriggerMode() {
            super((Pointer) null);
            allocate();
        }

        public fc2TriggerMode(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2TriggerMode(Pointer p) {
            super(p);
        }

        public fc2TriggerMode position(long position) {
            return (fc2TriggerMode) super.position(position);
        }
    }

    public static class fc2TriggerModeInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int modeMask();

        public native fc2TriggerModeInfo modeMask(int i);

        @Cast({"BOOL"})
        public native int onOffSupported();

        public native fc2TriggerModeInfo onOffSupported(int i);

        @Cast({"BOOL"})
        public native int polaritySupported();

        public native fc2TriggerModeInfo polaritySupported(int i);

        @Cast({"BOOL"})
        public native int present();

        public native fc2TriggerModeInfo present(int i);

        @Cast({"BOOL"})
        public native int readOutSupported();

        public native fc2TriggerModeInfo readOutSupported(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native fc2TriggerModeInfo reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"BOOL"})
        public native int softwareTriggerSupported();

        public native fc2TriggerModeInfo softwareTriggerSupported(int i);

        @Cast({"unsigned int"})
        public native int sourceMask();

        public native fc2TriggerModeInfo sourceMask(int i);

        @Cast({"BOOL"})
        public native int valueReadable();

        public native fc2TriggerModeInfo valueReadable(int i);

        static {
            Loader.load();
        }

        public fc2TriggerModeInfo() {
            super((Pointer) null);
            allocate();
        }

        public fc2TriggerModeInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2TriggerModeInfo(Pointer p) {
            super(p);
        }

        public fc2TriggerModeInfo position(long position) {
            return (fc2TriggerModeInfo) super.position(position);
        }
    }

    public static class fc2Version extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int build();

        public native fc2Version build(int i);

        @Cast({"unsigned int"})
        public native int major();

        public native fc2Version major(int i);

        @Cast({"unsigned int"})
        public native int minor();

        public native fc2Version minor(int i);

        @Cast({"unsigned int"})
        public native int type();

        public native fc2Version type(int i);

        static {
            Loader.load();
        }

        public fc2Version() {
            super((Pointer) null);
            allocate();
        }

        public fc2Version(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public fc2Version(Pointer p) {
            super(p);
        }

        public fc2Version position(long position) {
            return (fc2Version) super.position(position);
        }
    }

    @Cast({"fc2Error"})
    public static native int fc2AVIAppend(fc2AVIContext org_bytedeco_javacpp_FlyCapture2_C_fc2AVIContext, fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image);

    @Cast({"fc2Error"})
    public static native int fc2AVIClose(fc2AVIContext org_bytedeco_javacpp_FlyCapture2_C_fc2AVIContext);

    @Cast({"fc2Error"})
    public static native int fc2AVIOpen(fc2AVIContext org_bytedeco_javacpp_FlyCapture2_C_fc2AVIContext, String str, fc2AVIOption org_bytedeco_javacpp_FlyCapture2_C_fc2AVIOption);

    @Cast({"fc2Error"})
    public static native int fc2AVIOpen(fc2AVIContext org_bytedeco_javacpp_FlyCapture2_C_fc2AVIContext, @Cast({"const char*"}) BytePointer bytePointer, fc2AVIOption org_bytedeco_javacpp_FlyCapture2_C_fc2AVIOption);

    @Cast({"fc2Error"})
    public static native int fc2CalculateImageStatistics(fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image, @ByPtrPtr fc2ImageStatisticsContext org_bytedeco_javacpp_FlyCapture2_C_fc2ImageStatisticsContext);

    @Cast({"fc2Error"})
    public static native int fc2Connect(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2PGRGuid org_bytedeco_javacpp_FlyCapture2_C_fc2PGRGuid);

    @Cast({"fc2Error"})
    public static native int fc2ConvertImage(fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image, fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image2);

    @Cast({"fc2Error"})
    public static native int fc2ConvertImageTo(@Cast({"fc2PixelFormat"}) int i, fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image, fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image2);

    @Cast({"fc2Error"})
    public static native int fc2CreateAVI(@ByPtrPtr fc2AVIContext org_bytedeco_javacpp_FlyCapture2_C_fc2AVIContext);

    @Cast({"fc2Error"})
    public static native int fc2CreateContext(@ByPtrPtr @Cast({"fc2Context*"}) fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context);

    @Cast({"fc2Error"})
    public static native int fc2CreateGigEContext(@ByPtrPtr @Cast({"fc2Context*"}) fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context);

    @Cast({"fc2Error"})
    public static native int fc2CreateImage(fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image);

    @Cast({"fc2Error"})
    public static native int fc2CreateImageStatistics(@ByPtrPtr fc2ImageStatisticsContext org_bytedeco_javacpp_FlyCapture2_C_fc2ImageStatisticsContext);

    @Cast({"fc2Error"})
    public static native int fc2DestroyAVI(fc2AVIContext org_bytedeco_javacpp_FlyCapture2_C_fc2AVIContext);

    @Cast({"fc2Error"})
    public static native int fc2DestroyContext(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context);

    @Cast({"fc2Error"})
    public static native int fc2DestroyImage(fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image);

    @Cast({"fc2Error"})
    public static native int fc2DestroyImageStatistics(fc2ImageStatisticsContext org_bytedeco_javacpp_FlyCapture2_C_fc2ImageStatisticsContext);

    @Cast({"fc2Error"})
    public static native int fc2DetermineBitsPerPixel(@Cast({"fc2PixelFormat"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2DetermineBitsPerPixel(@Cast({"fc2PixelFormat"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2DetermineBitsPerPixel(@Cast({"fc2PixelFormat"}) int i, @Cast({"unsigned int*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2Disconnect(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context);

    @Cast({"fc2Error"})
    public static native int fc2DiscoverGigECameras(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2CameraInfo org_bytedeco_javacpp_FlyCapture2_C_fc2CameraInfo, @Cast({"unsigned int*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2DiscoverGigECameras(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2CameraInfo org_bytedeco_javacpp_FlyCapture2_C_fc2CameraInfo, @Cast({"unsigned int*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2DiscoverGigECameras(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2CameraInfo org_bytedeco_javacpp_FlyCapture2_C_fc2CameraInfo, @Cast({"unsigned int*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2EnableLUT(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"BOOL"}) int i);

    @Cast({"const char*"})
    public static native BytePointer fc2ErrorToDescription(@Cast({"fc2Error"}) int i);

    @Cast({"fc2Error"})
    public static native int fc2FireBusReset(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2PGRGuid org_bytedeco_javacpp_FlyCapture2_C_fc2PGRGuid);

    @Cast({"fc2Error"})
    public static native int fc2FireSoftwareTrigger(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context);

    @Cast({"fc2Error"})
    public static native int fc2FireSoftwareTriggerBroadcast(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context);

    @Cast({"fc2Error"})
    public static native int fc2ForceAllIPAddressesAutomatically();

    @Cast({"fc2Error"})
    public static native int fc2ForceIPAddressAutomatically(@Cast({"unsigned int"}) int i);

    @Cast({"fc2Error"})
    public static native int fc2ForceIPAddressToCamera(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @ByVal fc2MACAddress org_bytedeco_javacpp_FlyCapture2_C_fc2MACAddress, @ByVal fc2IPAddress org_bytedeco_javacpp_FlyCapture2_C_fc2IPAddress, @ByVal fc2IPAddress org_bytedeco_javacpp_FlyCapture2_C_fc2IPAddress2, @ByVal fc2IPAddress org_bytedeco_javacpp_FlyCapture2_C_fc2IPAddress3);

    @Cast({"fc2Error"})
    public static native int fc2GetActiveLUTBank(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2GetActiveLUTBank(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2GetActiveLUTBank(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2GetCameraFromIPAddress(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @ByVal fc2IPAddress org_bytedeco_javacpp_FlyCapture2_C_fc2IPAddress, fc2PGRGuid org_bytedeco_javacpp_FlyCapture2_C_fc2PGRGuid);

    @Cast({"fc2Error"})
    public static native int fc2GetCameraFromIndex(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, fc2PGRGuid org_bytedeco_javacpp_FlyCapture2_C_fc2PGRGuid);

    @Cast({"fc2Error"})
    public static native int fc2GetCameraFromSerialNumber(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, fc2PGRGuid org_bytedeco_javacpp_FlyCapture2_C_fc2PGRGuid);

    @Cast({"fc2Error"})
    public static native int fc2GetCameraInfo(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2CameraInfo org_bytedeco_javacpp_FlyCapture2_C_fc2CameraInfo);

    @Cast({"fc2Error"})
    public static native int fc2GetCameraSerialNumberFromIndex(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2GetCameraSerialNumberFromIndex(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2GetCameraSerialNumberFromIndex(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) int[] iArr);

    @Cast({"const fc2Error"})
    public static native int fc2GetChannelStatus(fc2ImageStatisticsContext org_bytedeco_javacpp_FlyCapture2_C_fc2ImageStatisticsContext, @Cast({"fc2StatisticsChannel"}) int i, @Cast({"BOOL*"}) IntBuffer intBuffer);

    @Cast({"const fc2Error"})
    public static native int fc2GetChannelStatus(fc2ImageStatisticsContext org_bytedeco_javacpp_FlyCapture2_C_fc2ImageStatisticsContext, @Cast({"fc2StatisticsChannel"}) int i, @Cast({"BOOL*"}) IntPointer intPointer);

    @Cast({"const fc2Error"})
    public static native int fc2GetChannelStatus(fc2ImageStatisticsContext org_bytedeco_javacpp_FlyCapture2_C_fc2ImageStatisticsContext, @Cast({"fc2StatisticsChannel"}) int i, @Cast({"BOOL*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2GetConfiguration(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2Config org_bytedeco_javacpp_FlyCapture2_C_fc2Config);

    @Cast({"fc2Error"})
    public static native int fc2GetCycleTime(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2TimeStamp org_bytedeco_javacpp_FlyCapture2_C_fc2TimeStamp);

    @Cast({"fc2Error"})
    public static native int fc2GetDefaultColorProcessing(@Cast({"fc2ColorProcessingAlgorithm*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2GetDefaultColorProcessing(@Cast({"fc2ColorProcessingAlgorithm*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2GetDefaultColorProcessing(@Cast({"fc2ColorProcessingAlgorithm*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2GetDefaultOutputFormat(@Cast({"fc2PixelFormat*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2GetDefaultOutputFormat(@Cast({"fc2PixelFormat*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2GetDefaultOutputFormat(@Cast({"fc2PixelFormat*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2GetDeviceFromIndex(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, fc2PGRGuid org_bytedeco_javacpp_FlyCapture2_C_fc2PGRGuid);

    @Cast({"fc2Error"})
    public static native int fc2GetEmbeddedImageInfo(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2EmbeddedImageInfo org_bytedeco_javacpp_FlyCapture2_C_fc2EmbeddedImageInfo);

    @Cast({"fc2Error"})
    public static native int fc2GetFormat7Configuration(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2Format7ImageSettings org_bytedeco_javacpp_FlyCapture2_C_fc2Format7ImageSettings, @Cast({"unsigned int*"}) IntBuffer intBuffer, FloatBuffer floatBuffer);

    @Cast({"fc2Error"})
    public static native int fc2GetFormat7Configuration(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2Format7ImageSettings org_bytedeco_javacpp_FlyCapture2_C_fc2Format7ImageSettings, @Cast({"unsigned int*"}) IntPointer intPointer, FloatPointer floatPointer);

    @Cast({"fc2Error"})
    public static native int fc2GetFormat7Configuration(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2Format7ImageSettings org_bytedeco_javacpp_FlyCapture2_C_fc2Format7ImageSettings, @Cast({"unsigned int*"}) int[] iArr, float[] fArr);

    @Cast({"fc2Error"})
    public static native int fc2GetFormat7Info(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2Format7Info org_bytedeco_javacpp_FlyCapture2_C_fc2Format7Info, @Cast({"BOOL*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2GetFormat7Info(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2Format7Info org_bytedeco_javacpp_FlyCapture2_C_fc2Format7Info, @Cast({"BOOL*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2GetFormat7Info(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2Format7Info org_bytedeco_javacpp_FlyCapture2_C_fc2Format7Info, @Cast({"BOOL*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2GetGPIOPinDirection(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2GetGPIOPinDirection(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2GetGPIOPinDirection(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2GetGigEConfig(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2GigEConfig org_bytedeco_javacpp_FlyCapture2_C_fc2GigEConfig);

    @Cast({"fc2Error"})
    public static native int fc2GetGigEImageBinningSettings(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int*"}) IntBuffer intBuffer2);

    @Cast({"fc2Error"})
    public static native int fc2GetGigEImageBinningSettings(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int*"}) IntPointer intPointer2);

    @Cast({"fc2Error"})
    public static native int fc2GetGigEImageBinningSettings(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int*"}) int[] iArr, @Cast({"unsigned int*"}) int[] iArr2);

    @Cast({"fc2Error"})
    public static native int fc2GetGigEImageSettings(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2GigEImageSettings org_bytedeco_javacpp_FlyCapture2_C_fc2GigEImageSettings);

    @Cast({"fc2Error"})
    public static native int fc2GetGigEImageSettingsInfo(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2GigEImageSettingsInfo org_bytedeco_javacpp_FlyCapture2_C_fc2GigEImageSettingsInfo);

    @Cast({"fc2Error"})
    public static native int fc2GetGigEImagingMode(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"fc2Mode*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2GetGigEImagingMode(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"fc2Mode*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2GetGigEImagingMode(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"fc2Mode*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2GetGigEProperty(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2GigEProperty org_bytedeco_javacpp_FlyCapture2_C_fc2GigEProperty);

    @Cast({"fc2Error"})
    public static native int fc2GetGigEStreamChannelInfo(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, fc2GigEStreamChannel org_bytedeco_javacpp_FlyCapture2_C_fc2GigEStreamChannel);

    @Cast({"fc2Error"})
    public static native int fc2GetImageData(fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image, @ByPtrPtr @Cast({"unsigned char**"}) ByteBuffer byteBuffer);

    @Cast({"fc2Error"})
    public static native int fc2GetImageData(fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image, @ByPtrPtr @Cast({"unsigned char**"}) BytePointer bytePointer);

    @Cast({"fc2Error"})
    public static native int fc2GetImageData(fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image, @Cast({"unsigned char**"}) PointerPointer pointerPointer);

    @Cast({"fc2Error"})
    public static native int fc2GetImageData(fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image, @ByPtrPtr @Cast({"unsigned char**"}) byte[] bArr);

    @Cast({"fc2Error"})
    public static native int fc2GetImageStatistics(fc2ImageStatisticsContext org_bytedeco_javacpp_FlyCapture2_C_fc2ImageStatisticsContext, @Cast({"fc2StatisticsChannel"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int*"}) IntBuffer intBuffer2, @Cast({"unsigned int*"}) IntBuffer intBuffer3, @Cast({"unsigned int*"}) IntBuffer intBuffer4, @Cast({"unsigned int*"}) IntBuffer intBuffer5, FloatBuffer floatBuffer, @ByPtrPtr IntBuffer intBuffer6);

    @Cast({"fc2Error"})
    public static native int fc2GetImageStatistics(fc2ImageStatisticsContext org_bytedeco_javacpp_FlyCapture2_C_fc2ImageStatisticsContext, @Cast({"fc2StatisticsChannel"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int*"}) IntPointer intPointer2, @Cast({"unsigned int*"}) IntPointer intPointer3, @Cast({"unsigned int*"}) IntPointer intPointer4, @Cast({"unsigned int*"}) IntPointer intPointer5, FloatPointer floatPointer, @ByPtrPtr IntPointer intPointer6);

    @Cast({"fc2Error"})
    public static native int fc2GetImageStatistics(fc2ImageStatisticsContext org_bytedeco_javacpp_FlyCapture2_C_fc2ImageStatisticsContext, @Cast({"fc2StatisticsChannel"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int*"}) IntPointer intPointer2, @Cast({"unsigned int*"}) IntPointer intPointer3, @Cast({"unsigned int*"}) IntPointer intPointer4, @Cast({"unsigned int*"}) IntPointer intPointer5, FloatPointer floatPointer, @Cast({"int**"}) PointerPointer pointerPointer);

    @Cast({"fc2Error"})
    public static native int fc2GetImageStatistics(fc2ImageStatisticsContext org_bytedeco_javacpp_FlyCapture2_C_fc2ImageStatisticsContext, @Cast({"fc2StatisticsChannel"}) int i, @Cast({"unsigned int*"}) int[] iArr, @Cast({"unsigned int*"}) int[] iArr2, @Cast({"unsigned int*"}) int[] iArr3, @Cast({"unsigned int*"}) int[] iArr4, @Cast({"unsigned int*"}) int[] iArr5, float[] fArr, @ByPtrPtr int[] iArr6);

    @ByVal
    public static native fc2TimeStamp fc2GetImageTimeStamp(fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image);

    @Cast({"fc2Error"})
    public static native int fc2GetInterfaceTypeFromGuid(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2PGRGuid org_bytedeco_javacpp_FlyCapture2_C_fc2PGRGuid, @Cast({"fc2InterfaceType*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2GetInterfaceTypeFromGuid(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2PGRGuid org_bytedeco_javacpp_FlyCapture2_C_fc2PGRGuid, @Cast({"fc2InterfaceType*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2GetInterfaceTypeFromGuid(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2PGRGuid org_bytedeco_javacpp_FlyCapture2_C_fc2PGRGuid, @Cast({"fc2InterfaceType*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2GetLUTBankInfo(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"BOOL*"}) IntBuffer intBuffer, @Cast({"BOOL*"}) IntBuffer intBuffer2);

    @Cast({"fc2Error"})
    public static native int fc2GetLUTBankInfo(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"BOOL*"}) IntPointer intPointer, @Cast({"BOOL*"}) IntPointer intPointer2);

    @Cast({"fc2Error"})
    public static native int fc2GetLUTBankInfo(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"BOOL*"}) int[] iArr, @Cast({"BOOL*"}) int[] iArr2);

    @Cast({"fc2Error"})
    public static native int fc2GetLUTChannel(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned int*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2GetLUTChannel(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned int*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2GetLUTChannel(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned int*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2GetLUTInfo(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2LUTData org_bytedeco_javacpp_FlyCapture2_C_fc2LUTData);

    @Cast({"fc2Error"})
    public static native int fc2GetLibraryVersion(fc2Version org_bytedeco_javacpp_FlyCapture2_C_fc2Version);

    @Cast({"fc2Error"})
    public static native int fc2GetMemoryChannel(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2GetMemoryChannel(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2GetMemoryChannel(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2GetMemoryChannelInfo(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2GetMemoryChannelInfo(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2GetMemoryChannelInfo(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2GetNumOfCameras(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2GetNumOfCameras(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2GetNumOfCameras(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2GetNumOfDevices(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2GetNumOfDevices(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2GetNumOfDevices(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2GetNumStreamChannels(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2GetNumStreamChannels(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2GetNumStreamChannels(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2GetProperty(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2Property org_bytedeco_javacpp_FlyCapture2_C_fc2Property);

    @Cast({"fc2Error"})
    public static native int fc2GetPropertyInfo(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2PropertyInfo org_bytedeco_javacpp_FlyCapture2_C_fc2PropertyInfo);

    @Cast({"const char*"})
    public static native BytePointer fc2GetRegisterString(@Cast({"unsigned int"}) int i);

    @Cast({"fc2Error"})
    public static native int fc2GetStrobe(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2StrobeControl org_bytedeco_javacpp_FlyCapture2_C_fc2StrobeControl);

    @Cast({"fc2Error"})
    public static native int fc2GetStrobeInfo(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2StrobeInfo org_bytedeco_javacpp_FlyCapture2_C_fc2StrobeInfo);

    @Cast({"fc2Error"})
    public static native int fc2GetSystemInfo(fc2SystemInfo org_bytedeco_javacpp_FlyCapture2_C_fc2SystemInfo);

    @Cast({"fc2Error"})
    public static native int fc2GetTriggerDelay(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"fc2TriggerDelay*"}) fc2Property org_bytedeco_javacpp_FlyCapture2_C_fc2Property);

    @Cast({"fc2Error"})
    public static native int fc2GetTriggerDelayInfo(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"fc2TriggerDelayInfo*"}) fc2PropertyInfo org_bytedeco_javacpp_FlyCapture2_C_fc2PropertyInfo);

    @Cast({"fc2Error"})
    public static native int fc2GetTriggerMode(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2TriggerMode org_bytedeco_javacpp_FlyCapture2_C_fc2TriggerMode);

    @Cast({"fc2Error"})
    public static native int fc2GetTriggerModeInfo(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2TriggerModeInfo org_bytedeco_javacpp_FlyCapture2_C_fc2TriggerModeInfo);

    @Cast({"fc2Error"})
    public static native int fc2GetVideoModeAndFrameRate(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"fc2VideoMode*"}) IntBuffer intBuffer, @Cast({"fc2FrameRate*"}) IntBuffer intBuffer2);

    @Cast({"fc2Error"})
    public static native int fc2GetVideoModeAndFrameRate(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"fc2VideoMode*"}) IntPointer intPointer, @Cast({"fc2FrameRate*"}) IntPointer intPointer2);

    @Cast({"fc2Error"})
    public static native int fc2GetVideoModeAndFrameRate(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"fc2VideoMode*"}) int[] iArr, @Cast({"fc2FrameRate*"}) int[] iArr2);

    @Cast({"fc2Error"})
    public static native int fc2GetVideoModeAndFrameRateInfo(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"fc2VideoMode"}) int i, @Cast({"fc2FrameRate"}) int i2, @Cast({"BOOL*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2GetVideoModeAndFrameRateInfo(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"fc2VideoMode"}) int i, @Cast({"fc2FrameRate"}) int i2, @Cast({"BOOL*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2GetVideoModeAndFrameRateInfo(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"fc2VideoMode"}) int i, @Cast({"fc2FrameRate"}) int i2, @Cast({"BOOL*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2H264Open(fc2AVIContext org_bytedeco_javacpp_FlyCapture2_C_fc2AVIContext, String str, fc2H264Option org_bytedeco_javacpp_FlyCapture2_C_fc2H264Option);

    @Cast({"fc2Error"})
    public static native int fc2H264Open(fc2AVIContext org_bytedeco_javacpp_FlyCapture2_C_fc2AVIContext, @Cast({"const char*"}) BytePointer bytePointer, fc2H264Option org_bytedeco_javacpp_FlyCapture2_C_fc2H264Option);

    @Cast({"fc2Error"})
    public static native int fc2IsCameraControlable(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2PGRGuid org_bytedeco_javacpp_FlyCapture2_C_fc2PGRGuid, @Cast({"BOOL*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2IsCameraControlable(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2PGRGuid org_bytedeco_javacpp_FlyCapture2_C_fc2PGRGuid, @Cast({"BOOL*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2IsCameraControlable(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2PGRGuid org_bytedeco_javacpp_FlyCapture2_C_fc2PGRGuid, @Cast({"BOOL*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2LaunchBrowser(String str);

    @Cast({"fc2Error"})
    public static native int fc2LaunchBrowser(@Cast({"const char*"}) BytePointer bytePointer);

    @Cast({"fc2Error"})
    public static native int fc2LaunchCommand(String str);

    @Cast({"fc2Error"})
    public static native int fc2LaunchCommand(@Cast({"const char*"}) BytePointer bytePointer);

    @Cast({"fc2Error"})
    public static native int fc2LaunchCommandAsync(String str, fc2AsyncCommandCallback org_bytedeco_javacpp_FlyCapture2_C_fc2AsyncCommandCallback, Pointer pointer);

    @Cast({"fc2Error"})
    public static native int fc2LaunchCommandAsync(@Cast({"const char*"}) BytePointer bytePointer, fc2AsyncCommandCallback org_bytedeco_javacpp_FlyCapture2_C_fc2AsyncCommandCallback, Pointer pointer);

    @Cast({"fc2Error"})
    public static native int fc2LaunchHelp(String str);

    @Cast({"fc2Error"})
    public static native int fc2LaunchHelp(@Cast({"const char*"}) BytePointer bytePointer);

    @Cast({"fc2Error"})
    public static native int fc2MJPGOpen(fc2AVIContext org_bytedeco_javacpp_FlyCapture2_C_fc2AVIContext, String str, fc2MJPGOption org_bytedeco_javacpp_FlyCapture2_C_fc2MJPGOption);

    @Cast({"fc2Error"})
    public static native int fc2MJPGOpen(fc2AVIContext org_bytedeco_javacpp_FlyCapture2_C_fc2AVIContext, @Cast({"const char*"}) BytePointer bytePointer, fc2MJPGOption org_bytedeco_javacpp_FlyCapture2_C_fc2MJPGOption);

    @Cast({"fc2Error"})
    public static native int fc2QueryGigEImagingMode(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"fc2Mode"}) int i, @Cast({"BOOL*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2QueryGigEImagingMode(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"fc2Mode"}) int i, @Cast({"BOOL*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2QueryGigEImagingMode(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"fc2Mode"}) int i, @Cast({"BOOL*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2ReadGVCPMemory(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned char*"}) ByteBuffer byteBuffer, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2ReadGVCPMemory(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned char*"}) BytePointer bytePointer, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2ReadGVCPMemory(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned char*"}) byte[] bArr, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2ReadGVCPRegister(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2ReadGVCPRegister(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2ReadGVCPRegister(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2ReadGVCPRegisterBlock(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2ReadGVCPRegisterBlock(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2ReadGVCPRegisterBlock(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) int[] iArr, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2ReadRegister(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2ReadRegister(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2ReadRegister(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2ReadRegisterBlock(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2ReadRegisterBlock(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2ReadRegisterBlock(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) int[] iArr, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2RegisterCallback(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2BusEventCallback org_bytedeco_javacpp_FlyCapture2_C_fc2BusEventCallback, @Cast({"fc2BusCallbackType"}) int i, Pointer pointer, @ByPtrPtr fc2CallbackHandle org_bytedeco_javacpp_FlyCapture2_C_fc2CallbackHandle);

    @Cast({"fc2Error"})
    public static native int fc2RescanBus(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context);

    @Cast({"fc2Error"})
    public static native int fc2RestoreFromMemoryChannel(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i);

    @Cast({"fc2Error"})
    public static native int fc2RetrieveBuffer(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image);

    @Cast({"fc2Error"})
    public static native int fc2SaveImage(fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image, String str, @Cast({"fc2ImageFileFormat"}) int i);

    @Cast({"fc2Error"})
    public static native int fc2SaveImage(fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"fc2ImageFileFormat"}) int i);

    @Cast({"fc2Error"})
    public static native int fc2SaveImageWithOption(fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image, String str, @Cast({"fc2ImageFileFormat"}) int i, Pointer pointer);

    @Cast({"fc2Error"})
    public static native int fc2SaveImageWithOption(fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"fc2ImageFileFormat"}) int i, Pointer pointer);

    @Cast({"fc2Error"})
    public static native int fc2SaveToMemoryChannel(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i);

    @Cast({"fc2Error"})
    public static native int fc2SetActiveLUTBank(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i);

    @Cast({"fc2Error"})
    public static native int fc2SetCallback(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2ImageEventCallback org_bytedeco_javacpp_FlyCapture2_C_fc2ImageEventCallback, Pointer pointer);

    @Cast({"fc2Error"})
    public static native int fc2SetChannelStatus(fc2ImageStatisticsContext org_bytedeco_javacpp_FlyCapture2_C_fc2ImageStatisticsContext, @Cast({"fc2StatisticsChannel"}) int i, @Cast({"BOOL"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2SetConfiguration(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2Config org_bytedeco_javacpp_FlyCapture2_C_fc2Config);

    @Cast({"fc2Error"})
    public static native int fc2SetDefaultColorProcessing(@Cast({"fc2ColorProcessingAlgorithm"}) int i);

    @Cast({"fc2Error"})
    public static native int fc2SetDefaultOutputFormat(@Cast({"fc2PixelFormat"}) int i);

    @Cast({"fc2Error"})
    public static native int fc2SetEmbeddedImageInfo(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2EmbeddedImageInfo org_bytedeco_javacpp_FlyCapture2_C_fc2EmbeddedImageInfo);

    @Cast({"fc2Error"})
    public static native int fc2SetFormat7Configuration(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2Format7ImageSettings org_bytedeco_javacpp_FlyCapture2_C_fc2Format7ImageSettings, float f);

    @Cast({"fc2Error"})
    public static native int fc2SetFormat7ConfigurationPacket(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2Format7ImageSettings org_bytedeco_javacpp_FlyCapture2_C_fc2Format7ImageSettings, @Cast({"unsigned int"}) int i);

    @Cast({"fc2Error"})
    public static native int fc2SetGPIOPinDirection(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2SetGPIOPinDirectionBroadcast(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2SetGigEConfig(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Const fc2GigEConfig org_bytedeco_javacpp_FlyCapture2_C_fc2GigEConfig);

    @Cast({"fc2Error"})
    public static native int fc2SetGigEImageBinningSettings(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2SetGigEImageSettings(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Const fc2GigEImageSettings org_bytedeco_javacpp_FlyCapture2_C_fc2GigEImageSettings);

    @Cast({"fc2Error"})
    public static native int fc2SetGigEImagingMode(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"fc2Mode"}) int i);

    @Cast({"fc2Error"})
    public static native int fc2SetGigEProperty(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Const fc2GigEProperty org_bytedeco_javacpp_FlyCapture2_C_fc2GigEProperty);

    @Cast({"fc2Error"})
    public static native int fc2SetGigEStreamChannelInfo(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, fc2GigEStreamChannel org_bytedeco_javacpp_FlyCapture2_C_fc2GigEStreamChannel);

    @Cast({"fc2Error"})
    public static native int fc2SetImageData(fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image, @Cast({"const unsigned char*"}) ByteBuffer byteBuffer, @Cast({"unsigned int"}) int i);

    @Cast({"fc2Error"})
    public static native int fc2SetImageData(fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image, @Cast({"const unsigned char*"}) BytePointer bytePointer, @Cast({"unsigned int"}) int i);

    @Cast({"fc2Error"})
    public static native int fc2SetImageData(fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image, @Cast({"const unsigned char*"}) byte[] bArr, @Cast({"unsigned int"}) int i);

    @Cast({"fc2Error"})
    public static native int fc2SetImageDimensions(fc2Image org_bytedeco_javacpp_FlyCapture2_C_fc2Image, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"fc2PixelFormat"}) int i4, @Cast({"fc2BayerTileFormat"}) int i5);

    @Cast({"fc2Error"})
    public static native int fc2SetLUTChannel(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned int*"}) IntBuffer intBuffer);

    @Cast({"fc2Error"})
    public static native int fc2SetLUTChannel(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned int*"}) IntPointer intPointer);

    @Cast({"fc2Error"})
    public static native int fc2SetLUTChannel(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned int*"}) int[] iArr);

    @Cast({"fc2Error"})
    public static native int fc2SetProperty(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2Property org_bytedeco_javacpp_FlyCapture2_C_fc2Property);

    @Cast({"fc2Error"})
    public static native int fc2SetPropertyBroadcast(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2Property org_bytedeco_javacpp_FlyCapture2_C_fc2Property);

    @Cast({"fc2Error"})
    public static native int fc2SetStrobe(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2StrobeControl org_bytedeco_javacpp_FlyCapture2_C_fc2StrobeControl);

    @Cast({"fc2Error"})
    public static native int fc2SetStrobeBroadcast(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2StrobeControl org_bytedeco_javacpp_FlyCapture2_C_fc2StrobeControl);

    @Cast({"fc2Error"})
    public static native int fc2SetTriggerDelay(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"fc2TriggerDelay*"}) fc2Property org_bytedeco_javacpp_FlyCapture2_C_fc2Property);

    @Cast({"fc2Error"})
    public static native int fc2SetTriggerDelayBroadcast(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"fc2TriggerDelay*"}) fc2Property org_bytedeco_javacpp_FlyCapture2_C_fc2Property);

    @Cast({"fc2Error"})
    public static native int fc2SetTriggerMode(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2TriggerMode org_bytedeco_javacpp_FlyCapture2_C_fc2TriggerMode);

    @Cast({"fc2Error"})
    public static native int fc2SetTriggerModeBroadcast(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2TriggerMode org_bytedeco_javacpp_FlyCapture2_C_fc2TriggerMode);

    @Cast({"fc2Error"})
    public static native int fc2SetUserBuffers(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned char*const"}) ByteBuffer byteBuffer, int i, int i2);

    @Cast({"fc2Error"})
    public static native int fc2SetUserBuffers(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned char*const"}) BytePointer bytePointer, int i, int i2);

    @Cast({"fc2Error"})
    public static native int fc2SetUserBuffers(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned char*const"}) byte[] bArr, int i, int i2);

    @Cast({"fc2Error"})
    public static native int fc2SetVideoModeAndFrameRate(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"fc2VideoMode"}) int i, @Cast({"fc2FrameRate"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2StartCapture(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context);

    @Cast({"fc2Error"})
    public static native int fc2StartCaptureCallback(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2ImageEventCallback org_bytedeco_javacpp_FlyCapture2_C_fc2ImageEventCallback, Pointer pointer);

    @Cast({"fc2Error"})
    public static native int fc2StartSyncCapture(@Cast({"unsigned int"}) int i, @ByPtrPtr @Cast({"fc2Context*"}) fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context);

    @Cast({"fc2Error"})
    public static native int fc2StartSyncCaptureCallback(@Cast({"unsigned int"}) int i, @ByPtrPtr @Cast({"fc2Context*"}) fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @ByPtrPtr @Cast({"fc2ImageEventCallback*"}) fc2ImageEventCallback org_bytedeco_javacpp_FlyCapture2_C_fc2ImageEventCallback, @ByPtrPtr @Cast({"void**"}) Pointer pointer);

    @Cast({"fc2Error"})
    public static native int fc2StartSyncCaptureCallback(@Cast({"unsigned int"}) int i, @ByPtrPtr @Cast({"fc2Context*"}) fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @ByPtrPtr @Cast({"fc2ImageEventCallback*"}) fc2ImageEventCallback org_bytedeco_javacpp_FlyCapture2_C_fc2ImageEventCallback, @Cast({"void**"}) PointerPointer pointerPointer);

    @Cast({"fc2Error"})
    public static native int fc2StopCapture(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context);

    @Cast({"fc2Error"})
    public static native int fc2UnregisterCallback(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2CallbackHandle org_bytedeco_javacpp_FlyCapture2_C_fc2CallbackHandle);

    @Cast({"fc2Error"})
    public static native int fc2ValidateFormat7Settings(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2Format7ImageSettings org_bytedeco_javacpp_FlyCapture2_C_fc2Format7ImageSettings, @Cast({"BOOL*"}) IntBuffer intBuffer, fc2Format7PacketInfo org_bytedeco_javacpp_FlyCapture2_C_fc2Format7PacketInfo);

    @Cast({"fc2Error"})
    public static native int fc2ValidateFormat7Settings(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2Format7ImageSettings org_bytedeco_javacpp_FlyCapture2_C_fc2Format7ImageSettings, @Cast({"BOOL*"}) IntPointer intPointer, fc2Format7PacketInfo org_bytedeco_javacpp_FlyCapture2_C_fc2Format7PacketInfo);

    @Cast({"fc2Error"})
    public static native int fc2ValidateFormat7Settings(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, fc2Format7ImageSettings org_bytedeco_javacpp_FlyCapture2_C_fc2Format7ImageSettings, @Cast({"BOOL*"}) int[] iArr, fc2Format7PacketInfo org_bytedeco_javacpp_FlyCapture2_C_fc2Format7PacketInfo);

    @Cast({"fc2Error"})
    public static native int fc2WriteGVCPMemory(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"const unsigned char*"}) ByteBuffer byteBuffer, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2WriteGVCPMemory(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"const unsigned char*"}) BytePointer bytePointer, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2WriteGVCPMemory(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"const unsigned char*"}) byte[] bArr, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2WriteGVCPRegister(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2WriteGVCPRegisterBlock(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"const unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2WriteGVCPRegisterBlock(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"const unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2WriteGVCPRegisterBlock(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"const unsigned int*"}) int[] iArr, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2WriteGVCPRegisterBroadcast(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2WriteRegister(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2WriteRegisterBlock(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"const unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2WriteRegisterBlock(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"const unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2WriteRegisterBlock(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"const unsigned int*"}) int[] iArr, @Cast({"unsigned int"}) int i2);

    @Cast({"fc2Error"})
    public static native int fc2WriteRegisterBroadcast(fc2Context org_bytedeco_javacpp_FlyCapture2_C_fc2Context, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

    static {
        Loader.load();
    }
}
