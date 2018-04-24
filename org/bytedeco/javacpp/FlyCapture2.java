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
import org.bytedeco.javacpp.annotation.NoOffset;
import org.bytedeco.javacpp.annotation.Opaque;
import org.bytedeco.javacpp.annotation.StdString;

public class FlyCapture2 extends org.bytedeco.javacpp.presets.FlyCapture2 {
    public static final int ARRIVAL = 1;
    public static final int AUTO_EXPOSURE = 1;
    public static final int BANDWIDTH_ALLOCATION_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int BANDWIDTH_ALLOCATION_OFF = 0;
    public static final int BANDWIDTH_ALLOCATION_ON = 1;
    public static final int BANDWIDTH_ALLOCATION_UNSPECIFIED = 3;
    public static final int BANDWIDTH_ALLOCATION_UNSUPPORTED = 2;
    public static final int BGGR = 4;
    public static final int BMP = 2;
    public static final int BRIGHTNESS = 0;
    public static final int BT_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int BUFFER_FRAMES = 1;
    public static final int BUSSPEED_10000BASE_T = 11;
    public static final int BUSSPEED_1000BASE_T = 10;
    public static final int BUSSPEED_100BASE_T = 9;
    public static final int BUSSPEED_10BASE_T = 8;
    public static final int BUSSPEED_ANY = 13;
    public static final int BUSSPEED_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int BUSSPEED_S100 = 0;
    public static final int BUSSPEED_S1600 = 5;
    public static final int BUSSPEED_S200 = 1;
    public static final int BUSSPEED_S3200 = 6;
    public static final int BUSSPEED_S400 = 2;
    public static final int BUSSPEED_S480 = 3;
    public static final int BUSSPEED_S5000 = 7;
    public static final int BUSSPEED_S800 = 4;
    public static final int BUSSPEED_SPEED_UNKNOWN = -1;
    public static final int BUSSPEED_S_FASTEST = 12;
    public static final int BUS_RESET = 0;
    public static final int BYTE_ORDER_BIG_ENDIAN = 1;
    public static final int BYTE_ORDER_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int BYTE_ORDER_LITTLE_ENDIAN = 0;
    public static final int CALLBACK_TYPE_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int COLOR_PROCESSING_ALGORITHM_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int DEFAULT = 0;
    public static final int DIRECTIONAL_FILTER = 7;
    public static final int DRIVER_1394_CAM = 0;
    public static final int DRIVER_1394_JUJU = 2;
    public static final int DRIVER_1394_PRO = 1;
    public static final int DRIVER_1394_RAW1394 = 4;
    public static final int DRIVER_1394_VIDEO1394 = 3;
    public static final int DRIVER_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int DRIVER_GIGE_FILTER = 9;
    public static final int DRIVER_GIGE_LWF = 11;
    public static final int DRIVER_GIGE_NONE = 8;
    public static final int DRIVER_GIGE_PRO = 10;
    public static final int DRIVER_UNKNOWN = -1;
    public static final int DRIVER_USB3_PRO = 7;
    public static final int DRIVER_USB_CAM = 6;
    public static final int DRIVER_USB_NONE = 5;
    public static final int DROP_FRAMES = 0;
    public static final int EDGE_SENSING = 3;
    public static final int FOCUS = 8;
    public static final int FRAMERATE_120 = 6;
    public static final int FRAMERATE_15 = 3;
    public static final int FRAMERATE_1_875 = 0;
    public static final int FRAMERATE_240 = 7;
    public static final int FRAMERATE_30 = 4;
    public static final int FRAMERATE_3_75 = 1;
    public static final int FRAMERATE_60 = 5;
    public static final int FRAMERATE_7_5 = 2;
    public static final int FRAMERATE_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int FRAMERATE_FORMAT7 = 8;
    public static final int FRAME_RATE = 16;
    public static final int FROM_FILE_EXT = -1;
    public static final int FULL_32BIT_VALUE = Integer.MAX_VALUE;
    public static final int GAIN = 13;
    public static final int GAMMA = 6;
    public static final int GBRG = 3;
    public static final int GRAB_MODE_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int GRAB_TIMEOUT_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int GRBG = 2;
    public static final int HEARTBEAT = 0;
    public static final int HEARTBEAT_TIMEOUT = 1;
    public static final int HQ_LINEAR = 4;
    public static final int HUE = 4;
    public static final int IMAGE_FILE_FORMAT_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int INTERFACE_GIGE = 3;
    public static final int INTERFACE_IEEE1394 = 0;
    public static final int INTERFACE_TYPE_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int INTERFACE_UNKNOWN = 4;
    public static final int INTERFACE_USB2 = 1;
    public static final int INTERFACE_USB3 = 2;
    public static final int IPP = 6;
    public static final int IRIS = 7;
    public static final int JPEG = 3;
    public static final int JPEG2000 = 4;
    public static final int LINUX_X64 = 3;
    public static final int LINUX_X86 = 2;
    public static final int MAC = 4;
    public static final int MODE_0 = 0;
    public static final int MODE_1 = 1;
    public static final int MODE_10 = 10;
    public static final int MODE_11 = 11;
    public static final int MODE_12 = 12;
    public static final int MODE_13 = 13;
    public static final int MODE_14 = 14;
    public static final int MODE_15 = 15;
    public static final int MODE_16 = 16;
    public static final int MODE_17 = 17;
    public static final int MODE_18 = 18;
    public static final int MODE_19 = 19;
    public static final int MODE_2 = 2;
    public static final int MODE_20 = 20;
    public static final int MODE_21 = 21;
    public static final int MODE_22 = 22;
    public static final int MODE_23 = 23;
    public static final int MODE_24 = 24;
    public static final int MODE_25 = 25;
    public static final int MODE_26 = 26;
    public static final int MODE_27 = 27;
    public static final int MODE_28 = 28;
    public static final int MODE_29 = 29;
    public static final int MODE_3 = 3;
    public static final int MODE_30 = 30;
    public static final int MODE_31 = 31;
    public static final int MODE_4 = 4;
    public static final int MODE_5 = 5;
    public static final int MODE_6 = 6;
    public static final int MODE_7 = 7;
    public static final int MODE_8 = 8;
    public static final int MODE_9 = 9;
    public static final int MODE_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int NEAREST_NEIGHBOR = 2;
    public static final int NONE = 0;
    public static final int NO_COLOR_PROCESSING = 1;
    public static final int NULL = 0;
    public static final int NUM_FRAMERATES = 9;
    public static final int NUM_MODES = 32;
    public static final int NUM_PIXEL_FORMATS = 20;
    public static final int NUM_VIDEOMODES = 24;
    public static final int OSTYPE_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int PACKET_DELAY = 3;
    public static final int PACKET_SIZE = 2;
    public static final int PAN = 10;
    public static final int PCIE_BUSSPEED_2_5 = 0;
    public static final int PCIE_BUSSPEED_5_0 = 1;
    public static final int PCIE_BUSSPEED_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int PCIE_BUSSPEED_UNKNOWN = -1;
    public static final int PGM = 0;
    public static final int PGRERROR_BUFFER_TOO_SMALL = 40;
    public static final int PGRERROR_BUS_MASTER_FAILED = 19;
    public static final int PGRERROR_FAILED = 1;
    public static final int PGRERROR_FAILED_BUS_MASTER_CONNECTION = 3;
    public static final int PGRERROR_FAILED_GUID = 13;
    public static final int PGRERROR_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int PGRERROR_IIDC_FAILED = 22;
    public static final int PGRERROR_IMAGE_CONSISTENCY_ERROR = 41;
    public static final int PGRERROR_IMAGE_CONVERSION_FAILED = 38;
    public static final int PGRERROR_IMAGE_LIBRARY_FAILURE = 39;
    public static final int PGRERROR_INCOMPATIBLE_DRIVER = 42;
    public static final int PGRERROR_INIT_FAILED = 5;
    public static final int PGRERROR_INVALID_BUS_MANAGER = 9;
    public static final int PGRERROR_INVALID_GENERATION = 20;
    public static final int PGRERROR_INVALID_MODE = 15;
    public static final int PGRERROR_INVALID_PACKET_SIZE = 14;
    public static final int PGRERROR_INVALID_PARAMETER = 7;
    public static final int PGRERROR_INVALID_SETTINGS = 8;
    public static final int PGRERROR_ISOCH_ALREADY_STARTED = 31;
    public static final int PGRERROR_ISOCH_BANDWIDTH_EXCEEDED = 37;
    public static final int PGRERROR_ISOCH_FAILED = 30;
    public static final int PGRERROR_ISOCH_NOT_STARTED = 32;
    public static final int PGRERROR_ISOCH_RETRIEVE_BUFFER_FAILED = 34;
    public static final int PGRERROR_ISOCH_START_FAILED = 33;
    public static final int PGRERROR_ISOCH_STOP_FAILED = 35;
    public static final int PGRERROR_ISOCH_SYNC_FAILED = 36;
    public static final int PGRERROR_LOW_LEVEL_FAILURE = 11;
    public static final int PGRERROR_LUT_FAILED = 21;
    public static final int PGRERROR_MEMORY_ALLOCATION_FAILED = 10;
    public static final int PGRERROR_NOT_CONNECTED = 4;
    public static final int PGRERROR_NOT_FOUND = 12;
    public static final int PGRERROR_NOT_IMPLEMENTED = 2;
    public static final int PGRERROR_NOT_INTITIALIZED = 6;
    public static final int PGRERROR_NOT_IN_FORMAT7 = 16;
    public static final int PGRERROR_NOT_SUPPORTED = 17;
    public static final int PGRERROR_OK = 0;
    public static final int PGRERROR_PROPERTY_FAILED = 25;
    public static final int PGRERROR_PROPERTY_NOT_PRESENT = 26;
    public static final int PGRERROR_READ_REGISTER_FAILED = 28;
    public static final int PGRERROR_REGISTER_FAILED = 27;
    public static final int PGRERROR_STROBE_FAILED = 23;
    public static final int PGRERROR_TIMEOUT = 18;
    public static final int PGRERROR_TRIGGER_FAILED = 24;
    public static final int PGRERROR_UNDEFINED = -1;
    public static final int PGRERROR_WRITE_REGISTER_FAILED = 29;
    public static final int PIXEL_FORMAT_411YUV8 = 1073741824;
    public static final int PIXEL_FORMAT_422YUV8 = 536870912;
    public static final int PIXEL_FORMAT_422YUV8_JPEG = 1073741825;
    public static final int PIXEL_FORMAT_444YUV8 = 268435456;
    public static final int PIXEL_FORMAT_BGR = -2147483640;
    public static final int PIXEL_FORMAT_BGR16 = 33554433;
    public static final int PIXEL_FORMAT_BGRU = 1073741832;
    public static final int PIXEL_FORMAT_BGRU16 = 33554434;
    public static final int PIXEL_FORMAT_MONO12 = 1048576;
    public static final int PIXEL_FORMAT_MONO16 = 67108864;
    public static final int PIXEL_FORMAT_MONO8 = Integer.MIN_VALUE;
    public static final int PIXEL_FORMAT_RAW12 = 524288;
    public static final int PIXEL_FORMAT_RAW16 = 2097152;
    public static final int PIXEL_FORMAT_RAW8 = 4194304;
    public static final int PIXEL_FORMAT_RGB = 134217728;
    public static final int PIXEL_FORMAT_RGB16 = 33554432;
    public static final int PIXEL_FORMAT_RGB8 = 134217728;
    public static final int PIXEL_FORMAT_RGBU = 1073741826;
    public static final int PIXEL_FORMAT_S_MONO16 = 16777216;
    public static final int PIXEL_FORMAT_S_RGB16 = 8388608;
    public static final int PNG = 6;
    public static final int PPM = 1;
    public static final int PROPERTY_TYPE_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int RAW = 7;
    public static final int REMOVAL = 2;
    public static final int RGGB = 1;
    public static final int RIGOROUS = 5;
    public static final int SATURATION = 5;
    public static final int SHARPNESS = 2;
    public static final int SHUTTER = 12;
    public static final int TEMPERATURE = 17;
    public static final int TIFF = 5;
    public static final int TILT = 11;
    public static final int TIMEOUT_INFINITE = -1;
    public static final int TIMEOUT_NONE = 0;
    public static final int TIMEOUT_UNSPECIFIED = -2;
    public static final int TRIGGER_DELAY = 15;
    public static final int TRIGGER_MODE = 14;
    public static final int UNKNOWN_OS = 5;
    public static final int UNSPECIFIED_GRAB_MODE = 2;
    public static final int UNSPECIFIED_PIXEL_FORMAT = 0;
    public static final int UNSPECIFIED_PROPERTY_TYPE = 18;
    public static final int VIDEOMODE_1024x768RGB = 12;
    public static final int VIDEOMODE_1024x768Y16 = 14;
    public static final int VIDEOMODE_1024x768Y8 = 13;
    public static final int VIDEOMODE_1024x768YUV422 = 11;
    public static final int VIDEOMODE_1280x960RGB = 16;
    public static final int VIDEOMODE_1280x960Y16 = 18;
    public static final int VIDEOMODE_1280x960Y8 = 17;
    public static final int VIDEOMODE_1280x960YUV422 = 15;
    public static final int VIDEOMODE_1600x1200RGB = 20;
    public static final int VIDEOMODE_1600x1200Y16 = 22;
    public static final int VIDEOMODE_1600x1200Y8 = 21;
    public static final int VIDEOMODE_1600x1200YUV422 = 19;
    public static final int VIDEOMODE_160x120YUV444 = 0;
    public static final int VIDEOMODE_320x240YUV422 = 1;
    public static final int VIDEOMODE_640x480RGB = 4;
    public static final int VIDEOMODE_640x480Y16 = 6;
    public static final int VIDEOMODE_640x480Y8 = 5;
    public static final int VIDEOMODE_640x480YUV411 = 2;
    public static final int VIDEOMODE_640x480YUV422 = 3;
    public static final int VIDEOMODE_800x600RGB = 8;
    public static final int VIDEOMODE_800x600Y16 = 10;
    public static final int VIDEOMODE_800x600Y8 = 9;
    public static final int VIDEOMODE_800x600YUV422 = 7;
    public static final int VIDEOMODE_FORCE_32BITS = Integer.MAX_VALUE;
    public static final int VIDEOMODE_FORMAT7 = 23;
    public static final int WHITE_BALANCE = 3;
    public static final int WINDOWS_X64 = 1;
    public static final int WINDOWS_X86 = 0;
    public static final int ZOOM = 9;
    public static final int sk_maxNumPorts = sk_maxNumPorts();
    public static final int sk_maxStringLength = sk_maxStringLength();

    @Namespace("FlyCapture2")
    @NoOffset
    public static class AVIOption extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float frameRate();

        public native AVIOption frameRate(float f);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native AVIOption reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        static {
            Loader.load();
        }

        public AVIOption(Pointer p) {
            super(p);
        }

        public AVIOption(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVIOption position(long position) {
            return (AVIOption) super.position(position);
        }

        public AVIOption() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class AVIRecorder extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @ByVal
        public native Error AVIAppend(Image image);

        @ByVal
        public native Error AVIClose();

        @ByVal
        public native Error AVIOpen(String str, AVIOption aVIOption);

        @ByVal
        public native Error AVIOpen(String str, H264Option h264Option);

        @ByVal
        public native Error AVIOpen(String str, MJPGOption mJPGOption);

        @ByVal
        public native Error AVIOpen(@Cast({"const char*"}) BytePointer bytePointer, AVIOption aVIOption);

        @ByVal
        public native Error AVIOpen(@Cast({"const char*"}) BytePointer bytePointer, H264Option h264Option);

        @ByVal
        public native Error AVIOpen(@Cast({"const char*"}) BytePointer bytePointer, MJPGOption mJPGOption);

        static {
            Loader.load();
        }

        public AVIRecorder(Pointer p) {
            super(p);
        }

        public AVIRecorder(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public AVIRecorder position(long position) {
            return (AVIRecorder) super.position(position);
        }

        public AVIRecorder() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class BMPOption extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native BMPOption indexedColor_8bit(boolean z);

        @Cast({"bool"})
        public native boolean indexedColor_8bit();

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native BMPOption reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        static {
            Loader.load();
        }

        public BMPOption(Pointer p) {
            super(p);
        }

        public BMPOption(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public BMPOption position(long position) {
            return (BMPOption) super.position(position);
        }

        public BMPOption() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class BusManager extends Pointer {
        @ByVal
        public static native Error DiscoverGigECameras(CameraInfo cameraInfo, @Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public static native Error DiscoverGigECameras(CameraInfo cameraInfo, @Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public static native Error DiscoverGigECameras(CameraInfo cameraInfo, @Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public static native Error ForceAllIPAddressesAutomatically();

        @ByVal
        public static native Error ForceAllIPAddressesAutomatically(@Cast({"unsigned int"}) int i);

        @ByVal
        public static native Error ForceIPAddressToCamera(@ByVal MACAddress mACAddress, @ByVal IPAddress iPAddress, @ByVal IPAddress iPAddress2, @ByVal IPAddress iPAddress3);

        private native void allocate();

        private native void allocateArray(long j);

        @ByVal
        public native Error FireBusReset(PGRGuid pGRGuid);

        @ByVal
        public native Error GetCameraFromIPAddress(@ByVal IPAddress iPAddress, PGRGuid pGRGuid);

        @ByVal
        public native Error GetCameraFromIndex(@Cast({"unsigned int"}) int i, PGRGuid pGRGuid);

        @ByVal
        public native Error GetCameraFromSerialNumber(@Cast({"unsigned int"}) int i, PGRGuid pGRGuid);

        @ByVal
        public native Error GetCameraSerialNumberFromIndex(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetCameraSerialNumberFromIndex(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetCameraSerialNumberFromIndex(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error GetDeviceFromIndex(@Cast({"unsigned int"}) int i, PGRGuid pGRGuid);

        @ByVal
        public native Error GetInterfaceTypeFromGuid(PGRGuid pGRGuid, @Cast({"FlyCapture2::InterfaceType*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetInterfaceTypeFromGuid(PGRGuid pGRGuid, @Cast({"FlyCapture2::InterfaceType*"}) IntPointer intPointer);

        @ByVal
        public native Error GetInterfaceTypeFromGuid(PGRGuid pGRGuid, @Cast({"FlyCapture2::InterfaceType*"}) int[] iArr);

        @ByVal
        public native Error GetNumOfCameras(@Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetNumOfCameras(@Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetNumOfCameras(@Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error GetNumOfDevices(@Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetNumOfDevices(@Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetNumOfDevices(@Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error GetTopology(TopologyNode topologyNode);

        @ByVal
        public native Error GetUsbLinkInfo(@ByVal PGRGuid pGRGuid, @Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetUsbLinkInfo(@ByVal PGRGuid pGRGuid, @Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetUsbLinkInfo(@ByVal PGRGuid pGRGuid, @Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error GetUsbPortStatus(@ByVal PGRGuid pGRGuid, @Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetUsbPortStatus(@ByVal PGRGuid pGRGuid, @Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetUsbPortStatus(@ByVal PGRGuid pGRGuid, @Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error IsCameraControlable(PGRGuid pGRGuid, @Cast({"bool*"}) BoolPointer boolPointer);

        @ByVal
        public native Error IsCameraControlable(PGRGuid pGRGuid, @Cast({"bool*"}) boolean[] zArr);

        @ByVal
        public native Error ReadPhyRegister(@ByVal PGRGuid pGRGuid, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error ReadPhyRegister(@ByVal PGRGuid pGRGuid, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error ReadPhyRegister(@ByVal PGRGuid pGRGuid, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error RegisterCallback(BusEventCallback busEventCallback, @Cast({"FlyCapture2::BusCallbackType"}) int i, Pointer pointer, @ByPtrPtr CallbackHandle callbackHandle);

        @ByVal
        public native Error RescanBus();

        @ByVal
        public native Error UnregisterCallback(CallbackHandle callbackHandle);

        @ByVal
        public native Error WritePhyRegister(@ByVal PGRGuid pGRGuid, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned int"}) int i4);

        static {
            Loader.load();
        }

        public BusManager(Pointer p) {
            super(p);
        }

        public BusManager(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public BusManager position(long position) {
            return (BusManager) super.position(position);
        }

        public BusManager() {
            super((Pointer) null);
            allocate();
        }
    }

    @Name({"void"})
    @Namespace
    @Opaque
    public static class CallbackHandle extends Pointer {
        public CallbackHandle() {
            super((Pointer) null);
        }

        public CallbackHandle(Pointer p) {
            super(p);
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class CameraBase extends Pointer {
        @ByVal
        public native Error Connect();

        @ByVal
        public native Error Connect(PGRGuid pGRGuid);

        @ByVal
        public native Error Disconnect();

        @ByVal
        public native Error EnableLUT(@Cast({"bool"}) boolean z);

        @ByVal
        public native Error FireSoftwareTrigger();

        @ByVal
        public native Error FireSoftwareTrigger(@Cast({"bool"}) boolean z);

        @ByVal
        public native Error GetActiveLUTBank(@Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetActiveLUTBank(@Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetActiveLUTBank(@Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error GetCameraInfo(CameraInfo cameraInfo);

        @ByVal
        public native Error GetConfiguration(FC2Config fC2Config);

        @ByVal
        public native Error GetCycleTime(TimeStamp timeStamp);

        @ByVal
        public native Error GetEmbeddedImageInfo(EmbeddedImageInfo embeddedImageInfo);

        @ByVal
        public native Error GetGPIOPinDirection(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetGPIOPinDirection(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetGPIOPinDirection(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error GetLUTBankInfo(@Cast({"unsigned int"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool*"}) BoolPointer boolPointer2);

        @ByVal
        public native Error GetLUTBankInfo(@Cast({"unsigned int"}) int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"bool*"}) boolean[] zArr2);

        @ByVal
        public native Error GetLUTChannel(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetLUTChannel(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetLUTChannel(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error GetLUTInfo(LUTData lUTData);

        @ByVal
        public native Error GetMemoryChannel(@Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetMemoryChannel(@Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetMemoryChannel(@Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error GetMemoryChannelInfo(@Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetMemoryChannelInfo(@Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetMemoryChannelInfo(@Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error GetProperty(Property property);

        @ByVal
        public native Error GetPropertyInfo(PropertyInfo propertyInfo);

        @ByVal
        public native Error GetStats(CameraStats cameraStats);

        @ByVal
        public native Error GetStrobe(StrobeControl strobeControl);

        @ByVal
        public native Error GetStrobeInfo(StrobeInfo strobeInfo);

        @ByVal
        public native Error GetTriggerDelay(@Cast({"FlyCapture2::TriggerDelay*"}) Property property);

        @ByVal
        public native Error GetTriggerDelayInfo(@Cast({"FlyCapture2::TriggerDelayInfo*"}) PropertyInfo propertyInfo);

        @ByVal
        public native Error GetTriggerMode(TriggerMode triggerMode);

        @ByVal
        public native Error GetTriggerModeInfo(TriggerModeInfo triggerModeInfo);

        @Cast({"bool"})
        public native boolean IsConnected();

        @ByVal
        public native Error ReadRegister(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error ReadRegister(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error ReadRegister(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error ReadRegisterBlock(@Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error ReadRegisterBlock(@Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error ReadRegisterBlock(@Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) int[] iArr, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error ResetStats();

        @ByVal
        public native Error RestoreFromMemoryChannel(@Cast({"unsigned int"}) int i);

        @ByVal
        public native Error RetrieveBuffer(Image image);

        @ByVal
        public native Error SaveToMemoryChannel(@Cast({"unsigned int"}) int i);

        @ByVal
        public native Error SetActiveLUTBank(@Cast({"unsigned int"}) int i);

        @ByVal
        public native Error SetCallback(ImageEventCallback imageEventCallback);

        @ByVal
        public native Error SetCallback(ImageEventCallback imageEventCallback, @Const Pointer pointer);

        @ByVal
        public native Error SetConfiguration(@Const FC2Config fC2Config);

        @ByVal
        public native Error SetEmbeddedImageInfo(EmbeddedImageInfo embeddedImageInfo);

        @ByVal
        public native Error SetGPIOPinDirection(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error SetGPIOPinDirection(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"bool"}) boolean z);

        @ByVal
        public native Error SetLUTChannel(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"const unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error SetLUTChannel(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"const unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error SetLUTChannel(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"const unsigned int*"}) int[] iArr);

        @ByVal
        public native Error SetProperty(@Const Property property);

        @ByVal
        public native Error SetProperty(@Const Property property, @Cast({"bool"}) boolean z);

        @ByVal
        public native Error SetStrobe(@Const StrobeControl strobeControl);

        @ByVal
        public native Error SetStrobe(@Const StrobeControl strobeControl, @Cast({"bool"}) boolean z);

        @ByVal
        public native Error SetTriggerDelay(@Cast({"const FlyCapture2::TriggerDelay*"}) Property property);

        @ByVal
        public native Error SetTriggerDelay(@Cast({"const FlyCapture2::TriggerDelay*"}) Property property, @Cast({"bool"}) boolean z);

        @ByVal
        public native Error SetTriggerMode(@Const TriggerMode triggerMode);

        @ByVal
        public native Error SetTriggerMode(@Const TriggerMode triggerMode, @Cast({"bool"}) boolean z);

        @ByVal
        public native Error SetUserBuffers(@Cast({"unsigned char*const"}) ByteBuffer byteBuffer, int i, int i2);

        @ByVal
        public native Error SetUserBuffers(@Cast({"unsigned char*const"}) BytePointer bytePointer, int i, int i2);

        @ByVal
        public native Error SetUserBuffers(@Cast({"unsigned char*const"}) byte[] bArr, int i, int i2);

        @ByVal
        public native Error StartCapture();

        @ByVal
        public native Error StartCapture(ImageEventCallback imageEventCallback, @Const Pointer pointer);

        @ByVal
        public native Error StopCapture();

        @ByVal
        public native Error WaitForBufferEvent(Image image, @Cast({"unsigned int"}) int i);

        @ByVal
        public native Error WriteRegister(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error WriteRegister(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"bool"}) boolean z);

        @ByVal
        public native Error WriteRegisterBlock(@Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"const unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error WriteRegisterBlock(@Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"const unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error WriteRegisterBlock(@Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"const unsigned int*"}) int[] iArr, @Cast({"unsigned int"}) int i2);

        static {
            Loader.load();
        }

        public CameraBase(Pointer p) {
            super(p);
        }
    }

    @Namespace("FlyCapture2")
    public static class Camera extends CameraBase {
        @Cast({"const char*"})
        public static native BytePointer GetRegisterString(@Cast({"unsigned int"}) int i);

        @ByVal
        public static native Error StartSyncCapture(@Cast({"unsigned int"}) int i, @ByPtrPtr @Const Camera camera);

        @ByVal
        public static native Error StartSyncCapture(@Cast({"unsigned int"}) int i, @ByPtrPtr @Const Camera camera, @ByPtrPtr @Cast({"FlyCapture2::ImageEventCallback*"}) ImageEventCallback imageEventCallback, @ByPtrPtr @Cast({"const void**"}) Pointer pointer);

        @ByVal
        public static native Error StartSyncCapture(@Cast({"unsigned int"}) int i, @Cast({"const FlyCapture2::Camera**"}) PointerPointer pointerPointer, @ByPtrPtr @Cast({"FlyCapture2::ImageEventCallback*"}) ImageEventCallback imageEventCallback, @Cast({"const void**"}) PointerPointer pointerPointer2);

        private native void allocate();

        private native void allocateArray(long j);

        @ByVal
        public native Error Connect();

        @ByVal
        public native Error Connect(PGRGuid pGRGuid);

        @ByVal
        public native Error Disconnect();

        @ByVal
        public native Error EnableLUT(@Cast({"bool"}) boolean z);

        @ByVal
        public native Error FireSoftwareTrigger();

        @ByVal
        public native Error FireSoftwareTrigger(@Cast({"bool"}) boolean z);

        @ByVal
        public native Error GetActiveLUTBank(@Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetActiveLUTBank(@Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetActiveLUTBank(@Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error GetCameraInfo(CameraInfo cameraInfo);

        @ByVal
        public native Error GetConfiguration(FC2Config fC2Config);

        @ByVal
        public native Error GetCycleTime(TimeStamp timeStamp);

        @ByVal
        public native Error GetEmbeddedImageInfo(EmbeddedImageInfo embeddedImageInfo);

        @ByVal
        public native Error GetFormat7Configuration(Format7ImageSettings format7ImageSettings, @Cast({"unsigned int*"}) IntBuffer intBuffer, FloatBuffer floatBuffer);

        @ByVal
        public native Error GetFormat7Configuration(Format7ImageSettings format7ImageSettings, @Cast({"unsigned int*"}) IntPointer intPointer, FloatPointer floatPointer);

        @ByVal
        public native Error GetFormat7Configuration(Format7ImageSettings format7ImageSettings, @Cast({"unsigned int*"}) int[] iArr, float[] fArr);

        @ByVal
        public native Error GetFormat7Info(Format7Info format7Info, @Cast({"bool*"}) BoolPointer boolPointer);

        @ByVal
        public native Error GetFormat7Info(Format7Info format7Info, @Cast({"bool*"}) boolean[] zArr);

        @ByVal
        public native Error GetGPIOPinDirection(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetGPIOPinDirection(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetGPIOPinDirection(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error GetLUTBankInfo(@Cast({"unsigned int"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool*"}) BoolPointer boolPointer2);

        @ByVal
        public native Error GetLUTBankInfo(@Cast({"unsigned int"}) int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"bool*"}) boolean[] zArr2);

        @ByVal
        public native Error GetLUTChannel(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetLUTChannel(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetLUTChannel(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error GetLUTInfo(LUTData lUTData);

        @ByVal
        public native Error GetMemoryChannel(@Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetMemoryChannel(@Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetMemoryChannel(@Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error GetMemoryChannelInfo(@Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetMemoryChannelInfo(@Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetMemoryChannelInfo(@Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error GetProperty(Property property);

        @ByVal
        public native Error GetPropertyInfo(PropertyInfo propertyInfo);

        @ByVal
        public native Error GetStats(CameraStats cameraStats);

        @ByVal
        public native Error GetStrobe(StrobeControl strobeControl);

        @ByVal
        public native Error GetStrobeInfo(StrobeInfo strobeInfo);

        @ByVal
        public native Error GetTriggerDelay(@Cast({"FlyCapture2::TriggerDelay*"}) Property property);

        @ByVal
        public native Error GetTriggerDelayInfo(@Cast({"FlyCapture2::TriggerDelayInfo*"}) PropertyInfo propertyInfo);

        @ByVal
        public native Error GetTriggerMode(TriggerMode triggerMode);

        @ByVal
        public native Error GetTriggerModeInfo(TriggerModeInfo triggerModeInfo);

        @ByVal
        public native Error GetVideoModeAndFrameRate(@Cast({"FlyCapture2::VideoMode*"}) IntBuffer intBuffer, @Cast({"FlyCapture2::FrameRate*"}) IntBuffer intBuffer2);

        @ByVal
        public native Error GetVideoModeAndFrameRate(@Cast({"FlyCapture2::VideoMode*"}) IntPointer intPointer, @Cast({"FlyCapture2::FrameRate*"}) IntPointer intPointer2);

        @ByVal
        public native Error GetVideoModeAndFrameRate(@Cast({"FlyCapture2::VideoMode*"}) int[] iArr, @Cast({"FlyCapture2::FrameRate*"}) int[] iArr2);

        @ByVal
        public native Error GetVideoModeAndFrameRateInfo(@Cast({"FlyCapture2::VideoMode"}) int i, @Cast({"FlyCapture2::FrameRate"}) int i2, @Cast({"bool*"}) BoolPointer boolPointer);

        @ByVal
        public native Error GetVideoModeAndFrameRateInfo(@Cast({"FlyCapture2::VideoMode"}) int i, @Cast({"FlyCapture2::FrameRate"}) int i2, @Cast({"bool*"}) boolean[] zArr);

        @Cast({"bool"})
        public native boolean IsConnected();

        @ByVal
        public native Error ReadRegister(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error ReadRegister(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error ReadRegister(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error ReadRegisterBlock(@Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error ReadRegisterBlock(@Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error ReadRegisterBlock(@Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) int[] iArr, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error ResetStats();

        @ByVal
        public native Error RestoreFromMemoryChannel(@Cast({"unsigned int"}) int i);

        @ByVal
        public native Error RetrieveBuffer(Image image);

        @ByVal
        public native Error SaveToMemoryChannel(@Cast({"unsigned int"}) int i);

        @ByVal
        public native Error SetActiveLUTBank(@Cast({"unsigned int"}) int i);

        @ByVal
        public native Error SetCallback(ImageEventCallback imageEventCallback);

        @ByVal
        public native Error SetCallback(ImageEventCallback imageEventCallback, @Const Pointer pointer);

        @ByVal
        public native Error SetConfiguration(@Const FC2Config fC2Config);

        @ByVal
        public native Error SetEmbeddedImageInfo(EmbeddedImageInfo embeddedImageInfo);

        @ByVal
        public native Error SetFormat7Configuration(@Const Format7ImageSettings format7ImageSettings, float f);

        @ByVal
        public native Error SetFormat7Configuration(@Const Format7ImageSettings format7ImageSettings, @Cast({"unsigned int"}) int i);

        @ByVal
        public native Error SetGPIOPinDirection(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error SetGPIOPinDirection(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"bool"}) boolean z);

        @ByVal
        public native Error SetLUTChannel(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"const unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error SetLUTChannel(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"const unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error SetLUTChannel(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"const unsigned int*"}) int[] iArr);

        @ByVal
        public native Error SetProperty(@Const Property property);

        @ByVal
        public native Error SetProperty(@Const Property property, @Cast({"bool"}) boolean z);

        @ByVal
        public native Error SetStrobe(@Const StrobeControl strobeControl);

        @ByVal
        public native Error SetStrobe(@Const StrobeControl strobeControl, @Cast({"bool"}) boolean z);

        @ByVal
        public native Error SetTriggerDelay(@Cast({"const FlyCapture2::TriggerDelay*"}) Property property);

        @ByVal
        public native Error SetTriggerDelay(@Cast({"const FlyCapture2::TriggerDelay*"}) Property property, @Cast({"bool"}) boolean z);

        @ByVal
        public native Error SetTriggerMode(@Const TriggerMode triggerMode);

        @ByVal
        public native Error SetTriggerMode(@Const TriggerMode triggerMode, @Cast({"bool"}) boolean z);

        @ByVal
        public native Error SetUserBuffers(@Cast({"unsigned char*const"}) ByteBuffer byteBuffer, int i, int i2);

        @ByVal
        public native Error SetUserBuffers(@Cast({"unsigned char*const"}) BytePointer bytePointer, int i, int i2);

        @ByVal
        public native Error SetUserBuffers(@Cast({"unsigned char*const"}) byte[] bArr, int i, int i2);

        @ByVal
        public native Error SetVideoModeAndFrameRate(@Cast({"FlyCapture2::VideoMode"}) int i, @Cast({"FlyCapture2::FrameRate"}) int i2);

        @ByVal
        public native Error StartCapture();

        @ByVal
        public native Error StartCapture(ImageEventCallback imageEventCallback, @Const Pointer pointer);

        @ByVal
        public native Error StopCapture();

        @ByVal
        public native Error ValidateFormat7Settings(@Const Format7ImageSettings format7ImageSettings, @Cast({"bool*"}) BoolPointer boolPointer, Format7PacketInfo format7PacketInfo);

        @ByVal
        public native Error ValidateFormat7Settings(@Const Format7ImageSettings format7ImageSettings, @Cast({"bool*"}) boolean[] zArr, Format7PacketInfo format7PacketInfo);

        @ByVal
        public native Error WaitForBufferEvent(Image image, @Cast({"unsigned int"}) int i);

        @ByVal
        public native Error WriteRegister(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error WriteRegister(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"bool"}) boolean z);

        @ByVal
        public native Error WriteRegisterBlock(@Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"const unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error WriteRegisterBlock(@Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"const unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error WriteRegisterBlock(@Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"const unsigned int*"}) int[] iArr, @Cast({"unsigned int"}) int i2);

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

    @Namespace("FlyCapture2")
    @NoOffset
    public static class CameraInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int applicationIPAddress();

        public native CameraInfo applicationIPAddress(int i);

        @Cast({"unsigned int"})
        public native int applicationPort();

        public native CameraInfo applicationPort(int i);

        @Cast({"FlyCapture2::BayerTileFormat"})
        public native int bayerTileFormat();

        public native CameraInfo bayerTileFormat(int i);

        public native CameraInfo busNumber(short s);

        @Cast({"unsigned short"})
        public native short busNumber();

        @Cast({"unsigned int"})
        public native int ccpStatus();

        public native CameraInfo ccpStatus(int i);

        public native CameraInfo configROM(ConfigROM configROM);

        @ByRef
        public native ConfigROM configROM();

        public native CameraInfo defaultGateway(IPAddress iPAddress);

        @ByRef
        public native IPAddress defaultGateway();

        @Cast({"char"})
        public native byte driverName(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer driverName();

        public native CameraInfo driverName(int i, byte b);

        @Cast({"FlyCapture2::DriverType"})
        public native int driverType();

        public native CameraInfo driverType(int i);

        @Cast({"char"})
        public native byte firmwareBuildTime(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer firmwareBuildTime();

        public native CameraInfo firmwareBuildTime(int i, byte b);

        @Cast({"char"})
        public native byte firmwareVersion(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer firmwareVersion();

        public native CameraInfo firmwareVersion(int i, byte b);

        @Cast({"unsigned int"})
        public native int gigEMajorVersion();

        public native CameraInfo gigEMajorVersion(int i);

        @Cast({"unsigned int"})
        public native int gigEMinorVersion();

        public native CameraInfo gigEMinorVersion(int i);

        @Cast({"unsigned int"})
        public native int iidcVer();

        public native CameraInfo iidcVer(int i);

        @Cast({"FlyCapture2::InterfaceType"})
        public native int interfaceType();

        public native CameraInfo interfaceType(int i);

        public native CameraInfo ipAddress(IPAddress iPAddress);

        @ByRef
        public native IPAddress ipAddress();

        public native CameraInfo isColorCamera(boolean z);

        @Cast({"bool"})
        public native boolean isColorCamera();

        public native CameraInfo macAddress(MACAddress mACAddress);

        @ByRef
        public native MACAddress macAddress();

        @Cast({"FlyCapture2::BusSpeed"})
        public native int maximumBusSpeed();

        public native CameraInfo maximumBusSpeed(int i);

        @Cast({"char"})
        public native byte modelName(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer modelName();

        public native CameraInfo modelName(int i, byte b);

        public native CameraInfo nodeNumber(short s);

        @Cast({"unsigned short"})
        public native short nodeNumber();

        @Cast({"FlyCapture2::PCIeBusSpeed"})
        public native int pcieBusSpeed();

        public native CameraInfo pcieBusSpeed(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native CameraInfo reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"char"})
        public native byte sensorInfo(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer sensorInfo();

        public native CameraInfo sensorInfo(int i, byte b);

        @Cast({"char"})
        public native byte sensorResolution(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer sensorResolution();

        public native CameraInfo sensorResolution(int i, byte b);

        @Cast({"unsigned int"})
        public native int serialNumber();

        public native CameraInfo serialNumber(int i);

        public native CameraInfo subnetMask(IPAddress iPAddress);

        @ByRef
        public native IPAddress subnetMask();

        @Cast({"char"})
        public native byte userDefinedName(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer userDefinedName();

        public native CameraInfo userDefinedName(int i, byte b);

        @Cast({"char"})
        public native byte vendorName(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer vendorName();

        public native CameraInfo vendorName(int i, byte b);

        @Cast({"char"})
        public native byte xmlURL1(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer xmlURL1();

        public native CameraInfo xmlURL1(int i, byte b);

        @Cast({"char"})
        public native byte xmlURL2(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer xmlURL2();

        public native CameraInfo xmlURL2(int i, byte b);

        static {
            Loader.load();
        }

        public CameraInfo(Pointer p) {
            super(p);
        }

        public CameraInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CameraInfo position(long position) {
            return (CameraInfo) super.position(position);
        }

        public CameraInfo() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class CameraStats extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float cameraCurrents(int i);

        @MemberGetter
        public native FloatPointer cameraCurrents();

        public native CameraStats cameraCurrents(int i, float f);

        public native CameraStats cameraPowerUp(boolean z);

        @Cast({"bool"})
        public native boolean cameraPowerUp();

        public native float cameraVoltages(int i);

        @MemberGetter
        public native FloatPointer cameraVoltages();

        public native CameraStats cameraVoltages(int i, float f);

        @Cast({"unsigned int"})
        public native int imageCorrupt();

        public native CameraStats imageCorrupt(int i);

        @Cast({"unsigned int"})
        public native int imageDriverDropped();

        public native CameraStats imageDriverDropped(int i);

        @Cast({"unsigned int"})
        public native int imageDropped();

        public native CameraStats imageDropped(int i);

        @Cast({"unsigned int"})
        public native int imageXmitFailed();

        public native CameraStats imageXmitFailed(int i);

        @Cast({"unsigned int"})
        public native int numCurrents();

        public native CameraStats numCurrents(int i);

        @Cast({"unsigned int"})
        public native int numResendPacketsReceived();

        public native CameraStats numResendPacketsReceived(int i);

        @Cast({"unsigned int"})
        public native int numResendPacketsRequested();

        public native CameraStats numResendPacketsRequested(int i);

        @Cast({"unsigned int"})
        public native int numVoltages();

        public native CameraStats numVoltages(int i);

        @Cast({"unsigned int"})
        public native int portErrors();

        public native CameraStats portErrors(int i);

        @Cast({"unsigned int"})
        public native int regReadFailed();

        public native CameraStats regReadFailed(int i);

        @Cast({"unsigned int"})
        public native int regWriteFailed();

        public native CameraStats regWriteFailed(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native CameraStats reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int temperature();

        public native CameraStats temperature(int i);

        @Cast({"unsigned int"})
        public native int timeSinceBusReset();

        public native CameraStats timeSinceBusReset(int i);

        @Cast({"unsigned int"})
        public native int timeSinceInitialization();

        public native CameraStats timeSinceInitialization(int i);

        public native CameraStats timeStamp(TimeStamp timeStamp);

        @ByRef
        public native TimeStamp timeStamp();

        static {
            Loader.load();
        }

        public CameraStats(Pointer p) {
            super(p);
        }

        public CameraStats(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public CameraStats position(long position) {
            return (CameraStats) super.position(position);
        }

        public CameraStats() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class ConfigROM extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int chipIdHi();

        public native ConfigROM chipIdHi(int i);

        @Cast({"unsigned int"})
        public native int chipIdLo();

        public native ConfigROM chipIdLo(int i);

        @Cast({"unsigned int"})
        public native int nodeVendorId();

        public native ConfigROM nodeVendorId(int i);

        @Cast({"char"})
        public native byte pszKeyword(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer pszKeyword();

        public native ConfigROM pszKeyword(int i, byte b);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native ConfigROM reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int unitSWVer();

        public native ConfigROM unitSWVer(int i);

        @Cast({"unsigned int"})
        public native int unitSpecId();

        public native ConfigROM unitSpecId(int i);

        @Cast({"unsigned int"})
        public native int unitSubSWVer();

        public native ConfigROM unitSubSWVer(int i);

        @Cast({"unsigned int"})
        public native int vendorUniqueInfo_0();

        public native ConfigROM vendorUniqueInfo_0(int i);

        @Cast({"unsigned int"})
        public native int vendorUniqueInfo_1();

        public native ConfigROM vendorUniqueInfo_1(int i);

        @Cast({"unsigned int"})
        public native int vendorUniqueInfo_2();

        public native ConfigROM vendorUniqueInfo_2(int i);

        @Cast({"unsigned int"})
        public native int vendorUniqueInfo_3();

        public native ConfigROM vendorUniqueInfo_3(int i);

        static {
            Loader.load();
        }

        public ConfigROM(Pointer p) {
            super(p);
        }

        public ConfigROM(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public ConfigROM position(long position) {
            return (ConfigROM) super.position(position);
        }

        public ConfigROM() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    public static class EmbeddedImageInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native EmbeddedImageInfo GPIOPinState(EmbeddedImageInfoProperty embeddedImageInfoProperty);

        @ByRef
        public native EmbeddedImageInfoProperty GPIOPinState();

        public native EmbeddedImageInfo ROIPosition(EmbeddedImageInfoProperty embeddedImageInfoProperty);

        @ByRef
        public native EmbeddedImageInfoProperty ROIPosition();

        public native EmbeddedImageInfo brightness(EmbeddedImageInfoProperty embeddedImageInfoProperty);

        @ByRef
        public native EmbeddedImageInfoProperty brightness();

        public native EmbeddedImageInfo exposure(EmbeddedImageInfoProperty embeddedImageInfoProperty);

        @ByRef
        public native EmbeddedImageInfoProperty exposure();

        public native EmbeddedImageInfo frameCounter(EmbeddedImageInfoProperty embeddedImageInfoProperty);

        @ByRef
        public native EmbeddedImageInfoProperty frameCounter();

        public native EmbeddedImageInfo gain(EmbeddedImageInfoProperty embeddedImageInfoProperty);

        @ByRef
        public native EmbeddedImageInfoProperty gain();

        public native EmbeddedImageInfo shutter(EmbeddedImageInfoProperty embeddedImageInfoProperty);

        @ByRef
        public native EmbeddedImageInfoProperty shutter();

        public native EmbeddedImageInfo strobePattern(EmbeddedImageInfoProperty embeddedImageInfoProperty);

        @ByRef
        public native EmbeddedImageInfoProperty strobePattern();

        public native EmbeddedImageInfo timestamp(EmbeddedImageInfoProperty embeddedImageInfoProperty);

        @ByRef
        public native EmbeddedImageInfoProperty timestamp();

        public native EmbeddedImageInfo whiteBalance(EmbeddedImageInfoProperty embeddedImageInfoProperty);

        @ByRef
        public native EmbeddedImageInfoProperty whiteBalance();

        static {
            Loader.load();
        }

        public EmbeddedImageInfo() {
            super((Pointer) null);
            allocate();
        }

        public EmbeddedImageInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public EmbeddedImageInfo(Pointer p) {
            super(p);
        }

        public EmbeddedImageInfo position(long position) {
            return (EmbeddedImageInfo) super.position(position);
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class EmbeddedImageInfoProperty extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native EmbeddedImageInfoProperty available(boolean z);

        @Cast({"bool"})
        public native boolean available();

        public native EmbeddedImageInfoProperty onOff(boolean z);

        @Cast({"bool"})
        public native boolean onOff();

        static {
            Loader.load();
        }

        public EmbeddedImageInfoProperty(Pointer p) {
            super(p);
        }

        public EmbeddedImageInfoProperty(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public EmbeddedImageInfoProperty position(long position) {
            return (EmbeddedImageInfoProperty) super.position(position);
        }

        public EmbeddedImageInfoProperty() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class Error extends Pointer {
        private native void allocate();

        private native void allocate(@ByRef @Const Error error);

        private native void allocateArray(long j);

        @Cast({"const char*"})
        public native BytePointer CollectSupportInformation();

        @Cast({"const char*"})
        public native BytePointer GetBuildDate();

        @ByVal
        public native Error GetCause();

        @Cast({"const char*"})
        public native BytePointer GetDescription();

        @Cast({"const char*"})
        public native BytePointer GetFilename();

        @Cast({"unsigned int"})
        public native int GetLine();

        @Cast({"FlyCapture2::ErrorType"})
        public native int GetType();

        public native void PrintErrorTrace();

        @Cast({"bool"})
        @Name({"operator =="})
        public native boolean equals(@Cast({"const FlyCapture2::ErrorType"}) int i);

        @Cast({"bool"})
        @Name({"operator =="})
        public native boolean equals(@ByRef @Const Error error);

        @Cast({"bool"})
        @Name({"operator !="})
        public native boolean notEquals(@Cast({"const FlyCapture2::ErrorType"}) int i);

        @Cast({"bool"})
        @Name({"operator !="})
        public native boolean notEquals(@ByRef @Const Error error);

        @ByRef
        @Name({"operator ="})
        public native Error put(@ByRef @Const Error error);

        static {
            Loader.load();
        }

        public Error(Pointer p) {
            super(p);
        }

        public Error(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Error position(long position) {
            return (Error) super.position(position);
        }

        public Error() {
            super((Pointer) null);
            allocate();
        }

        public Error(@ByRef @Const Error error) {
            super((Pointer) null);
            allocate(error);
        }
    }

    @Namespace("FlyCapture2")
    @Opaque
    public static class ErrorImpl extends Pointer {
        public ErrorImpl() {
            super((Pointer) null);
        }

        public ErrorImpl(Pointer p) {
            super(p);
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class FC2Config extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"FlyCapture2::BusSpeed"})
        public native int asyncBusSpeed();

        public native FC2Config asyncBusSpeed(int i);

        @Cast({"FlyCapture2::BandwidthAllocation"})
        public native int bandwidthAllocation();

        public native FC2Config bandwidthAllocation(int i);

        @Cast({"FlyCapture2::GrabMode"})
        public native int grabMode();

        public native FC2Config grabMode(int i);

        public native int grabTimeout();

        public native FC2Config grabTimeout(int i);

        public native FC2Config highPerformanceRetrieveBuffer(boolean z);

        @Cast({"bool"})
        public native boolean highPerformanceRetrieveBuffer();

        @Cast({"FlyCapture2::BusSpeed"})
        public native int isochBusSpeed();

        public native FC2Config isochBusSpeed(int i);

        @Cast({"unsigned int"})
        public native int minNumImageNotifications();

        public native FC2Config minNumImageNotifications(int i);

        @Cast({"unsigned int"})
        public native int numBuffers();

        public native FC2Config numBuffers(int i);

        @Cast({"unsigned int"})
        public native int numImageNotifications();

        public native FC2Config numImageNotifications(int i);

        @Cast({"unsigned int"})
        public native int registerTimeout();

        public native FC2Config registerTimeout(int i);

        @Cast({"unsigned int"})
        public native int registerTimeoutRetries();

        public native FC2Config registerTimeoutRetries(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native FC2Config reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        static {
            Loader.load();
        }

        public FC2Config(Pointer p) {
            super(p);
        }

        public FC2Config(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FC2Config position(long position) {
            return (FC2Config) super.position(position);
        }

        public FC2Config() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    public static class FC2Version extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int build();

        public native FC2Version build(int i);

        @Cast({"unsigned int"})
        public native int major();

        public native FC2Version major(int i);

        @Cast({"unsigned int"})
        public native int minor();

        public native FC2Version minor(int i);

        @Cast({"unsigned int"})
        public native int type();

        public native FC2Version type(int i);

        static {
            Loader.load();
        }

        public FC2Version() {
            super((Pointer) null);
            allocate();
        }

        public FC2Version(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FC2Version(Pointer p) {
            super(p);
        }

        public FC2Version position(long position) {
            return (FC2Version) super.position(position);
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class Format7ImageSettings extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int height();

        public native Format7ImageSettings height(int i);

        @Cast({"FlyCapture2::Mode"})
        public native int mode();

        public native Format7ImageSettings mode(int i);

        @Cast({"unsigned int"})
        public native int offsetX();

        public native Format7ImageSettings offsetX(int i);

        @Cast({"unsigned int"})
        public native int offsetY();

        public native Format7ImageSettings offsetY(int i);

        @Cast({"FlyCapture2::PixelFormat"})
        public native int pixelFormat();

        public native Format7ImageSettings pixelFormat(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native Format7ImageSettings reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int width();

        public native Format7ImageSettings width(int i);

        static {
            Loader.load();
        }

        public Format7ImageSettings(Pointer p) {
            super(p);
        }

        public Format7ImageSettings(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Format7ImageSettings position(long position) {
            return (Format7ImageSettings) super.position(position);
        }

        public Format7ImageSettings() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class Format7Info extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int imageHStepSize();

        public native Format7Info imageHStepSize(int i);

        @Cast({"unsigned int"})
        public native int imageVStepSize();

        public native Format7Info imageVStepSize(int i);

        @Cast({"unsigned int"})
        public native int maxHeight();

        public native Format7Info maxHeight(int i);

        @Cast({"unsigned int"})
        public native int maxPacketSize();

        public native Format7Info maxPacketSize(int i);

        @Cast({"unsigned int"})
        public native int maxWidth();

        public native Format7Info maxWidth(int i);

        @Cast({"unsigned int"})
        public native int minPacketSize();

        public native Format7Info minPacketSize(int i);

        @Cast({"FlyCapture2::Mode"})
        public native int mode();

        public native Format7Info mode(int i);

        @Cast({"unsigned int"})
        public native int offsetHStepSize();

        public native Format7Info offsetHStepSize(int i);

        @Cast({"unsigned int"})
        public native int offsetVStepSize();

        public native Format7Info offsetVStepSize(int i);

        @Cast({"unsigned int"})
        public native int packetSize();

        public native Format7Info packetSize(int i);

        public native float percentage();

        public native Format7Info percentage(float f);

        @Cast({"unsigned int"})
        public native int pixelFormatBitField();

        public native Format7Info pixelFormatBitField(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native Format7Info reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int vendorPixelFormatBitField();

        public native Format7Info vendorPixelFormatBitField(int i);

        static {
            Loader.load();
        }

        public Format7Info(Pointer p) {
            super(p);
        }

        public Format7Info(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Format7Info position(long position) {
            return (Format7Info) super.position(position);
        }

        public Format7Info() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class Format7PacketInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int maxBytesPerPacket();

        public native Format7PacketInfo maxBytesPerPacket(int i);

        @Cast({"unsigned int"})
        public native int recommendedBytesPerPacket();

        public native Format7PacketInfo recommendedBytesPerPacket(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native Format7PacketInfo reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int unitBytesPerPacket();

        public native Format7PacketInfo unitBytesPerPacket(int i);

        static {
            Loader.load();
        }

        public Format7PacketInfo(Pointer p) {
            super(p);
        }

        public Format7PacketInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Format7PacketInfo position(long position) {
            return (Format7PacketInfo) super.position(position);
        }

        public Format7PacketInfo() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    public static class GigECamera extends CameraBase {
        @Cast({"const char*"})
        public static native BytePointer GetRegisterString(@Cast({"unsigned int"}) int i);

        @ByVal
        public static native Error StartSyncCapture(@Cast({"unsigned int"}) int i, @ByPtrPtr @Const GigECamera gigECamera);

        @ByVal
        public static native Error StartSyncCapture(@Cast({"unsigned int"}) int i, @ByPtrPtr @Const GigECamera gigECamera, @ByPtrPtr @Cast({"FlyCapture2::ImageEventCallback*"}) ImageEventCallback imageEventCallback, @ByPtrPtr @Cast({"const void**"}) Pointer pointer);

        @ByVal
        public static native Error StartSyncCapture(@Cast({"unsigned int"}) int i, @Cast({"const FlyCapture2::GigECamera**"}) PointerPointer pointerPointer, @ByPtrPtr @Cast({"FlyCapture2::ImageEventCallback*"}) ImageEventCallback imageEventCallback, @Cast({"const void**"}) PointerPointer pointerPointer2);

        private native void allocate();

        private native void allocateArray(long j);

        @ByVal
        public native Error Connect();

        @ByVal
        public native Error Connect(PGRGuid pGRGuid);

        @ByVal
        public native Error Disconnect();

        @ByVal
        public native Error DiscoverGigEPacketSize(@Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error DiscoverGigEPacketSize(@Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error DiscoverGigEPacketSize(@Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error EnableLUT(@Cast({"bool"}) boolean z);

        @ByVal
        public native Error FireSoftwareTrigger();

        @ByVal
        public native Error FireSoftwareTrigger(@Cast({"bool"}) boolean z);

        @ByVal
        public native Error GetActiveLUTBank(@Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetActiveLUTBank(@Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetActiveLUTBank(@Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error GetCameraInfo(CameraInfo cameraInfo);

        @ByVal
        public native Error GetConfiguration(FC2Config fC2Config);

        @ByVal
        public native Error GetCycleTime(TimeStamp timeStamp);

        @ByVal
        public native Error GetEmbeddedImageInfo(EmbeddedImageInfo embeddedImageInfo);

        @ByVal
        public native Error GetGPIOPinDirection(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetGPIOPinDirection(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetGPIOPinDirection(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error GetGigEConfig(GigEConfig gigEConfig);

        @ByVal
        public native Error GetGigEImageBinningSettings(@Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int*"}) IntBuffer intBuffer2);

        @ByVal
        public native Error GetGigEImageBinningSettings(@Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int*"}) IntPointer intPointer2);

        @ByVal
        public native Error GetGigEImageBinningSettings(@Cast({"unsigned int*"}) int[] iArr, @Cast({"unsigned int*"}) int[] iArr2);

        @ByVal
        public native Error GetGigEImageSettings(GigEImageSettings gigEImageSettings);

        @ByVal
        public native Error GetGigEImageSettingsInfo(GigEImageSettingsInfo gigEImageSettingsInfo);

        @ByVal
        public native Error GetGigEImagingMode(@Cast({"FlyCapture2::Mode*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetGigEImagingMode(@Cast({"FlyCapture2::Mode*"}) IntPointer intPointer);

        @ByVal
        public native Error GetGigEImagingMode(@Cast({"FlyCapture2::Mode*"}) int[] iArr);

        @ByVal
        public native Error GetGigEProperty(GigEProperty gigEProperty);

        @ByVal
        public native Error GetGigEStreamChannelInfo(@Cast({"unsigned int"}) int i, GigEStreamChannel gigEStreamChannel);

        @ByVal
        public native Error GetLUTBankInfo(@Cast({"unsigned int"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool*"}) BoolPointer boolPointer2);

        @ByVal
        public native Error GetLUTBankInfo(@Cast({"unsigned int"}) int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"bool*"}) boolean[] zArr2);

        @ByVal
        public native Error GetLUTChannel(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetLUTChannel(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetLUTChannel(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error GetLUTInfo(LUTData lUTData);

        @ByVal
        public native Error GetMemoryChannel(@Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetMemoryChannel(@Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetMemoryChannel(@Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error GetMemoryChannelInfo(@Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetMemoryChannelInfo(@Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetMemoryChannelInfo(@Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error GetNumStreamChannels(@Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetNumStreamChannels(@Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetNumStreamChannels(@Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error GetProperty(Property property);

        @ByVal
        public native Error GetPropertyInfo(PropertyInfo propertyInfo);

        @ByVal
        public native Error GetStats(CameraStats cameraStats);

        @ByVal
        public native Error GetStrobe(StrobeControl strobeControl);

        @ByVal
        public native Error GetStrobeInfo(StrobeInfo strobeInfo);

        @ByVal
        public native Error GetTriggerDelay(@Cast({"FlyCapture2::TriggerDelay*"}) Property property);

        @ByVal
        public native Error GetTriggerDelayInfo(@Cast({"FlyCapture2::TriggerDelayInfo*"}) PropertyInfo propertyInfo);

        @ByVal
        public native Error GetTriggerMode(TriggerMode triggerMode);

        @ByVal
        public native Error GetTriggerModeInfo(TriggerModeInfo triggerModeInfo);

        @Cast({"bool"})
        public native boolean IsConnected();

        @ByVal
        public native Error QueryGigEImagingMode(@Cast({"FlyCapture2::Mode"}) int i, @Cast({"bool*"}) BoolPointer boolPointer);

        @ByVal
        public native Error QueryGigEImagingMode(@Cast({"FlyCapture2::Mode"}) int i, @Cast({"bool*"}) boolean[] zArr);

        @ByVal
        public native Error ReadGVCPMemory(@Cast({"unsigned int"}) int i, @Cast({"unsigned char*"}) ByteBuffer byteBuffer, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error ReadGVCPMemory(@Cast({"unsigned int"}) int i, @Cast({"unsigned char*"}) BytePointer bytePointer, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error ReadGVCPMemory(@Cast({"unsigned int"}) int i, @Cast({"unsigned char*"}) byte[] bArr, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error ReadGVCPRegister(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error ReadGVCPRegister(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error ReadGVCPRegister(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error ReadGVCPRegisterBlock(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error ReadGVCPRegisterBlock(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error ReadGVCPRegisterBlock(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) int[] iArr, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error ReadRegister(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error ReadRegister(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error ReadRegister(@Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error ReadRegisterBlock(@Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error ReadRegisterBlock(@Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error ReadRegisterBlock(@Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) int[] iArr, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error ResetStats();

        @ByVal
        public native Error RestoreFromMemoryChannel(@Cast({"unsigned int"}) int i);

        @ByVal
        public native Error RetrieveBuffer(Image image);

        @ByVal
        public native Error SaveToMemoryChannel(@Cast({"unsigned int"}) int i);

        @ByVal
        public native Error SetActiveLUTBank(@Cast({"unsigned int"}) int i);

        @ByVal
        public native Error SetCallback(ImageEventCallback imageEventCallback);

        @ByVal
        public native Error SetCallback(ImageEventCallback imageEventCallback, @Const Pointer pointer);

        @ByVal
        public native Error SetConfiguration(@Const FC2Config fC2Config);

        @ByVal
        public native Error SetEmbeddedImageInfo(EmbeddedImageInfo embeddedImageInfo);

        @ByVal
        public native Error SetGPIOPinDirection(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error SetGPIOPinDirection(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"bool"}) boolean z);

        @ByVal
        public native Error SetGigEConfig(@Const GigEConfig gigEConfig);

        @ByVal
        public native Error SetGigEImageBinningSettings(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error SetGigEImageSettings(@Const GigEImageSettings gigEImageSettings);

        @ByVal
        public native Error SetGigEImagingMode(@Cast({"FlyCapture2::Mode"}) int i);

        @ByVal
        public native Error SetGigEProperty(@Const GigEProperty gigEProperty);

        @ByVal
        public native Error SetGigEStreamChannelInfo(@Cast({"unsigned int"}) int i, GigEStreamChannel gigEStreamChannel);

        @ByVal
        public native Error SetLUTChannel(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"const unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error SetLUTChannel(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"const unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error SetLUTChannel(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"const unsigned int*"}) int[] iArr);

        @ByVal
        public native Error SetProperty(@Const Property property);

        @ByVal
        public native Error SetProperty(@Const Property property, @Cast({"bool"}) boolean z);

        @ByVal
        public native Error SetStrobe(@Const StrobeControl strobeControl);

        @ByVal
        public native Error SetStrobe(@Const StrobeControl strobeControl, @Cast({"bool"}) boolean z);

        @ByVal
        public native Error SetTriggerDelay(@Cast({"const FlyCapture2::TriggerDelay*"}) Property property);

        @ByVal
        public native Error SetTriggerDelay(@Cast({"const FlyCapture2::TriggerDelay*"}) Property property, @Cast({"bool"}) boolean z);

        @ByVal
        public native Error SetTriggerMode(@Const TriggerMode triggerMode);

        @ByVal
        public native Error SetTriggerMode(@Const TriggerMode triggerMode, @Cast({"bool"}) boolean z);

        @ByVal
        public native Error SetUserBuffers(@Cast({"unsigned char*const"}) ByteBuffer byteBuffer, int i, int i2);

        @ByVal
        public native Error SetUserBuffers(@Cast({"unsigned char*const"}) BytePointer bytePointer, int i, int i2);

        @ByVal
        public native Error SetUserBuffers(@Cast({"unsigned char*const"}) byte[] bArr, int i, int i2);

        @ByVal
        public native Error StartCapture();

        @ByVal
        public native Error StartCapture(ImageEventCallback imageEventCallback, @Const Pointer pointer);

        @ByVal
        public native Error StopCapture();

        @ByVal
        public native Error WaitForBufferEvent(Image image, @Cast({"unsigned int"}) int i);

        @ByVal
        public native Error WriteGVCPMemory(@Cast({"unsigned int"}) int i, @Cast({"const unsigned char*"}) ByteBuffer byteBuffer, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error WriteGVCPMemory(@Cast({"unsigned int"}) int i, @Cast({"const unsigned char*"}) BytePointer bytePointer, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error WriteGVCPMemory(@Cast({"unsigned int"}) int i, @Cast({"const unsigned char*"}) byte[] bArr, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error WriteGVCPRegister(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error WriteGVCPRegister(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"bool"}) boolean z);

        @ByVal
        public native Error WriteGVCPRegisterBlock(@Cast({"unsigned int"}) int i, @Cast({"const unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error WriteGVCPRegisterBlock(@Cast({"unsigned int"}) int i, @Cast({"const unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error WriteGVCPRegisterBlock(@Cast({"unsigned int"}) int i, @Cast({"const unsigned int*"}) int[] iArr, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error WriteRegister(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error WriteRegister(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"bool"}) boolean z);

        @ByVal
        public native Error WriteRegisterBlock(@Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"const unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error WriteRegisterBlock(@Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"const unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int"}) int i2);

        @ByVal
        public native Error WriteRegisterBlock(@Cast({"unsigned short"}) short s, @Cast({"unsigned int"}) int i, @Cast({"const unsigned int*"}) int[] iArr, @Cast({"unsigned int"}) int i2);

        static {
            Loader.load();
        }

        public GigECamera(Pointer p) {
            super(p);
        }

        public GigECamera(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public GigECamera position(long position) {
            return (GigECamera) super.position(position);
        }

        public GigECamera() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class GigEConfig extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native GigEConfig enablePacketResend(boolean z);

        @Cast({"bool"})
        public native boolean enablePacketResend();

        @Cast({"unsigned int"})
        public native int registerTimeout();

        public native GigEConfig registerTimeout(int i);

        @Cast({"unsigned int"})
        public native int registerTimeoutRetries();

        public native GigEConfig registerTimeoutRetries(int i);

        static {
            Loader.load();
        }

        public GigEConfig(Pointer p) {
            super(p);
        }

        public GigEConfig(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public GigEConfig position(long position) {
            return (GigEConfig) super.position(position);
        }

        public GigEConfig() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class GigEImageSettings extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int height();

        public native GigEImageSettings height(int i);

        @Cast({"unsigned int"})
        public native int offsetX();

        public native GigEImageSettings offsetX(int i);

        @Cast({"unsigned int"})
        public native int offsetY();

        public native GigEImageSettings offsetY(int i);

        @Cast({"FlyCapture2::PixelFormat"})
        public native int pixelFormat();

        public native GigEImageSettings pixelFormat(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native GigEImageSettings reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int width();

        public native GigEImageSettings width(int i);

        static {
            Loader.load();
        }

        public GigEImageSettings(Pointer p) {
            super(p);
        }

        public GigEImageSettings(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public GigEImageSettings position(long position) {
            return (GigEImageSettings) super.position(position);
        }

        public GigEImageSettings() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class GigEImageSettingsInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int imageHStepSize();

        public native GigEImageSettingsInfo imageHStepSize(int i);

        @Cast({"unsigned int"})
        public native int imageVStepSize();

        public native GigEImageSettingsInfo imageVStepSize(int i);

        @Cast({"unsigned int"})
        public native int maxHeight();

        public native GigEImageSettingsInfo maxHeight(int i);

        @Cast({"unsigned int"})
        public native int maxWidth();

        public native GigEImageSettingsInfo maxWidth(int i);

        @Cast({"unsigned int"})
        public native int offsetHStepSize();

        public native GigEImageSettingsInfo offsetHStepSize(int i);

        @Cast({"unsigned int"})
        public native int offsetVStepSize();

        public native GigEImageSettingsInfo offsetVStepSize(int i);

        @Cast({"unsigned int"})
        public native int pixelFormatBitField();

        public native GigEImageSettingsInfo pixelFormatBitField(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native GigEImageSettingsInfo reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int vendorPixelFormatBitField();

        public native GigEImageSettingsInfo vendorPixelFormatBitField(int i);

        static {
            Loader.load();
        }

        public GigEImageSettingsInfo(Pointer p) {
            super(p);
        }

        public GigEImageSettingsInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public GigEImageSettingsInfo position(long position) {
            return (GigEImageSettingsInfo) super.position(position);
        }

        public GigEImageSettingsInfo() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    public static class GigEProperty extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native GigEProperty isReadable(boolean z);

        @Cast({"bool"})
        public native boolean isReadable();

        public native GigEProperty isWritable(boolean z);

        @Cast({"bool"})
        public native boolean isWritable();

        @Cast({"unsigned int"})
        public native int max();

        public native GigEProperty max(int i);

        @Cast({"unsigned int"})
        public native int min();

        public native GigEProperty min(int i);

        @Cast({"FlyCapture2::GigEPropertyType"})
        public native int propType();

        public native GigEProperty propType(int i);

        @Cast({"unsigned int"})
        public native int value();

        public native GigEProperty value(int i);

        static {
            Loader.load();
        }

        public GigEProperty() {
            super((Pointer) null);
            allocate();
        }

        public GigEProperty(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public GigEProperty(Pointer p) {
            super(p);
        }

        public GigEProperty position(long position) {
            return (GigEProperty) super.position(position);
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class GigEStreamChannel extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native GigEStreamChannel destinationIpAddress(IPAddress iPAddress);

        @ByRef
        public native IPAddress destinationIpAddress();

        public native GigEStreamChannel doNotFragment(boolean z);

        @Cast({"bool"})
        public native boolean doNotFragment();

        @Cast({"unsigned int"})
        public native int hostPort();

        public native GigEStreamChannel hostPort(int i);

        public native GigEStreamChannel hostPost(IntPointer intPointer);

        @ByRef
        @Cast({"unsigned int*"})
        public native IntPointer hostPost();

        @Cast({"unsigned int"})
        public native int interPacketDelay();

        public native GigEStreamChannel interPacketDelay(int i);

        @Cast({"unsigned int"})
        public native int networkInterfaceIndex();

        public native GigEStreamChannel networkInterfaceIndex(int i);

        @Cast({"unsigned int"})
        public native int packetSize();

        public native GigEStreamChannel packetSize(int i);

        @Cast({"unsigned int"})
        public native int sourcePort();

        public native GigEStreamChannel sourcePort(int i);

        static {
            Loader.load();
        }

        public GigEStreamChannel(Pointer p) {
            super(p);
        }

        public GigEStreamChannel(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public GigEStreamChannel position(long position) {
            return (GigEStreamChannel) super.position(position);
        }

        public GigEStreamChannel() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class H264Option extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int bitrate();

        public native H264Option bitrate(int i);

        public native float frameRate();

        public native H264Option frameRate(float f);

        @Cast({"unsigned int"})
        public native int height();

        public native H264Option height(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native H264Option reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int width();

        public native H264Option width(int i);

        static {
            Loader.load();
        }

        public H264Option(Pointer p) {
            super(p);
        }

        public H264Option(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public H264Option position(long position) {
            return (H264Option) super.position(position);
        }

        public H264Option() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class IPAddress extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"unsigned int"}) int i);

        private native void allocateArray(long j);

        @Cast({"bool"})
        @Name({"operator =="})
        public native boolean equals(@ByRef @Const IPAddress iPAddress);

        @Cast({"bool"})
        @Name({"operator !="})
        public native boolean notEquals(@ByRef @Const IPAddress iPAddress);

        @Cast({"unsigned char"})
        public native byte octets(int i);

        @MemberGetter
        @Cast({"unsigned char*"})
        public native BytePointer octets();

        public native IPAddress octets(int i, byte b);

        static {
            Loader.load();
        }

        public IPAddress(Pointer p) {
            super(p);
        }

        public IPAddress(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public IPAddress position(long position) {
            return (IPAddress) super.position(position);
        }

        public IPAddress() {
            super((Pointer) null);
            allocate();
        }

        public IPAddress(@Cast({"unsigned int"}) int ipAddressVal) {
            super((Pointer) null);
            allocate(ipAddressVal);
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class Image extends Pointer {
        @Cast({"unsigned int"})
        public static native int DetermineBitsPerPixel(@Cast({"FlyCapture2::PixelFormat"}) int i);

        @Cast({"FlyCapture2::ColorProcessingAlgorithm"})
        public static native int GetDefaultColorProcessing();

        @Cast({"FlyCapture2::PixelFormat"})
        public static native int GetDefaultOutputFormat();

        @ByVal
        public static native Error SetDefaultColorProcessing(@Cast({"FlyCapture2::ColorProcessingAlgorithm"}) int i);

        @ByVal
        public static native Error SetDefaultOutputFormat(@Cast({"FlyCapture2::PixelFormat"}) int i);

        private native void allocate();

        private native void allocate(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"FlyCapture2::PixelFormat"}) int i3);

        private native void allocate(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"FlyCapture2::PixelFormat"}) int i3, @Cast({"FlyCapture2::BayerTileFormat"}) int i4);

        private native void allocate(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned char*"}) ByteBuffer byteBuffer, @Cast({"unsigned int"}) int i4, @Cast({"FlyCapture2::PixelFormat"}) int i5);

        private native void allocate(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned char*"}) ByteBuffer byteBuffer, @Cast({"unsigned int"}) int i4, @Cast({"FlyCapture2::PixelFormat"}) int i5, @Cast({"FlyCapture2::BayerTileFormat"}) int i6);

        private native void allocate(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned char*"}) ByteBuffer byteBuffer, @Cast({"unsigned int"}) int i4, @Cast({"unsigned int"}) int i5, @Cast({"FlyCapture2::PixelFormat"}) int i6, @Cast({"FlyCapture2::BayerTileFormat"}) int i7);

        private native void allocate(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned char*"}) BytePointer bytePointer, @Cast({"unsigned int"}) int i4, @Cast({"FlyCapture2::PixelFormat"}) int i5);

        private native void allocate(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned char*"}) BytePointer bytePointer, @Cast({"unsigned int"}) int i4, @Cast({"FlyCapture2::PixelFormat"}) int i5, @Cast({"FlyCapture2::BayerTileFormat"}) int i6);

        private native void allocate(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned char*"}) BytePointer bytePointer, @Cast({"unsigned int"}) int i4, @Cast({"unsigned int"}) int i5, @Cast({"FlyCapture2::PixelFormat"}) int i6, @Cast({"FlyCapture2::BayerTileFormat"}) int i7);

        private native void allocate(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned char*"}) byte[] bArr, @Cast({"unsigned int"}) int i4, @Cast({"FlyCapture2::PixelFormat"}) int i5);

        private native void allocate(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned char*"}) byte[] bArr, @Cast({"unsigned int"}) int i4, @Cast({"FlyCapture2::PixelFormat"}) int i5, @Cast({"FlyCapture2::BayerTileFormat"}) int i6);

        private native void allocate(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned char*"}) byte[] bArr, @Cast({"unsigned int"}) int i4, @Cast({"unsigned int"}) int i5, @Cast({"FlyCapture2::PixelFormat"}) int i6, @Cast({"FlyCapture2::BayerTileFormat"}) int i7);

        private native void allocate(@Cast({"unsigned char*"}) ByteBuffer byteBuffer, @Cast({"unsigned int"}) int i);

        private native void allocate(@Cast({"unsigned char*"}) BytePointer bytePointer, @Cast({"unsigned int"}) int i);

        private native void allocate(@ByRef @Const Image image);

        private native void allocate(@Cast({"unsigned char*"}) byte[] bArr, @Cast({"unsigned int"}) int i);

        private native void allocateArray(long j);

        @ByVal
        public native Error CalculateStatistics(ImageStatistics imageStatistics);

        @ByVal
        public native Error Convert(@Cast({"FlyCapture2::PixelFormat"}) int i, Image image);

        @ByVal
        public native Error Convert(Image image);

        @ByVal
        public native Error DeepCopy(@Const Image image);

        @Cast({"FlyCapture2::BayerTileFormat"})
        public native int GetBayerTileFormat();

        @Cast({"unsigned int"})
        public native int GetBitsPerPixel();

        @Cast({"unsigned int"})
        public native int GetBlockId();

        @Cast({"FlyCapture2::ColorProcessingAlgorithm"})
        public native int GetColorProcessing();

        @Cast({"unsigned int"})
        public native int GetCols();

        @Cast({"unsigned char*"})
        public native BytePointer GetData();

        @Cast({"unsigned int"})
        public native int GetDataSize();

        public native void GetDimensions(@Cast({"unsigned int*"}) IntBuffer intBuffer);

        public native void GetDimensions(@Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int*"}) IntBuffer intBuffer2, @Cast({"unsigned int*"}) IntBuffer intBuffer3, @Cast({"FlyCapture2::PixelFormat*"}) IntBuffer intBuffer4, @Cast({"FlyCapture2::BayerTileFormat*"}) IntBuffer intBuffer5);

        public native void GetDimensions(@Cast({"unsigned int*"}) IntPointer intPointer);

        public native void GetDimensions(@Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int*"}) IntPointer intPointer2, @Cast({"unsigned int*"}) IntPointer intPointer3, @Cast({"FlyCapture2::PixelFormat*"}) IntPointer intPointer4, @Cast({"FlyCapture2::BayerTileFormat*"}) IntPointer intPointer5);

        public native void GetDimensions(@Cast({"unsigned int*"}) int[] iArr);

        public native void GetDimensions(@Cast({"unsigned int*"}) int[] iArr, @Cast({"unsigned int*"}) int[] iArr2, @Cast({"unsigned int*"}) int[] iArr3, @Cast({"FlyCapture2::PixelFormat*"}) int[] iArr4, @Cast({"FlyCapture2::BayerTileFormat*"}) int[] iArr5);

        @ByVal
        public native ImageMetadata GetMetadata();

        @Cast({"FlyCapture2::PixelFormat"})
        public native int GetPixelFormat();

        @Cast({"unsigned int"})
        public native int GetReceivedDataSize();

        @Cast({"unsigned int"})
        public native int GetRows();

        @Cast({"unsigned int"})
        public native int GetStride();

        @ByVal
        public native TimeStamp GetTimeStamp();

        @ByVal
        public native Error ReleaseBuffer();

        @ByVal
        public native Error Save(String str);

        @ByVal
        public native Error Save(String str, @Cast({"FlyCapture2::ImageFileFormat"}) int i);

        @ByVal
        public native Error Save(String str, BMPOption bMPOption);

        @ByVal
        public native Error Save(String str, JPEGOption jPEGOption);

        @ByVal
        public native Error Save(String str, JPG2Option jPG2Option);

        @ByVal
        public native Error Save(String str, PGMOption pGMOption);

        @ByVal
        public native Error Save(String str, PNGOption pNGOption);

        @ByVal
        public native Error Save(String str, PPMOption pPMOption);

        @ByVal
        public native Error Save(String str, TIFFOption tIFFOption);

        @ByVal
        public native Error Save(@Cast({"const char*"}) BytePointer bytePointer);

        @ByVal
        public native Error Save(@Cast({"const char*"}) BytePointer bytePointer, @Cast({"FlyCapture2::ImageFileFormat"}) int i);

        @ByVal
        public native Error Save(@Cast({"const char*"}) BytePointer bytePointer, BMPOption bMPOption);

        @ByVal
        public native Error Save(@Cast({"const char*"}) BytePointer bytePointer, JPEGOption jPEGOption);

        @ByVal
        public native Error Save(@Cast({"const char*"}) BytePointer bytePointer, JPG2Option jPG2Option);

        @ByVal
        public native Error Save(@Cast({"const char*"}) BytePointer bytePointer, PGMOption pGMOption);

        @ByVal
        public native Error Save(@Cast({"const char*"}) BytePointer bytePointer, PNGOption pNGOption);

        @ByVal
        public native Error Save(@Cast({"const char*"}) BytePointer bytePointer, PPMOption pPMOption);

        @ByVal
        public native Error Save(@Cast({"const char*"}) BytePointer bytePointer, TIFFOption tIFFOption);

        @ByVal
        public native Error SetBlockId(@Cast({"const unsigned int"}) int i);

        @ByVal
        public native Error SetColorProcessing(@Cast({"FlyCapture2::ColorProcessingAlgorithm"}) int i);

        @ByVal
        public native Error SetData(@Cast({"const unsigned char*"}) ByteBuffer byteBuffer, @Cast({"unsigned int"}) int i);

        @ByVal
        public native Error SetData(@Cast({"const unsigned char*"}) BytePointer bytePointer, @Cast({"unsigned int"}) int i);

        @ByVal
        public native Error SetData(@Cast({"const unsigned char*"}) byte[] bArr, @Cast({"unsigned int"}) int i);

        @ByVal
        public native Error SetDimensions(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"FlyCapture2::PixelFormat"}) int i4, @Cast({"FlyCapture2::BayerTileFormat"}) int i5);

        @Cast({"unsigned char*"})
        @Name({"operator ()"})
        public native BytePointer apply(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

        @Cast({"unsigned char*"})
        @Name({"operator []"})
        public native BytePointer get(@Cast({"unsigned int"}) int i);

        @ByRef
        @Name({"operator ="})
        public native Image put(@ByRef @Const Image image);

        static {
            Loader.load();
        }

        public Image(Pointer p) {
            super(p);
        }

        public Image(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Image position(long position) {
            return (Image) super.position(position);
        }

        public Image() {
            super((Pointer) null);
            allocate();
        }

        public Image(@Cast({"unsigned int"}) int rows, @Cast({"unsigned int"}) int cols, @Cast({"unsigned int"}) int stride, @Cast({"unsigned char*"}) BytePointer pData, @Cast({"unsigned int"}) int dataSize, @Cast({"FlyCapture2::PixelFormat"}) int format, @Cast({"FlyCapture2::BayerTileFormat"}) int bayerFormat) {
            super((Pointer) null);
            allocate(rows, cols, stride, pData, dataSize, format, bayerFormat);
        }

        public Image(@Cast({"unsigned int"}) int rows, @Cast({"unsigned int"}) int cols, @Cast({"unsigned int"}) int stride, @Cast({"unsigned char*"}) BytePointer pData, @Cast({"unsigned int"}) int dataSize, @Cast({"FlyCapture2::PixelFormat"}) int format) {
            super((Pointer) null);
            allocate(rows, cols, stride, pData, dataSize, format);
        }

        public Image(@Cast({"unsigned int"}) int rows, @Cast({"unsigned int"}) int cols, @Cast({"unsigned int"}) int stride, @Cast({"unsigned char*"}) ByteBuffer pData, @Cast({"unsigned int"}) int dataSize, @Cast({"FlyCapture2::PixelFormat"}) int format, @Cast({"FlyCapture2::BayerTileFormat"}) int bayerFormat) {
            super((Pointer) null);
            allocate(rows, cols, stride, pData, dataSize, format, bayerFormat);
        }

        public Image(@Cast({"unsigned int"}) int rows, @Cast({"unsigned int"}) int cols, @Cast({"unsigned int"}) int stride, @Cast({"unsigned char*"}) ByteBuffer pData, @Cast({"unsigned int"}) int dataSize, @Cast({"FlyCapture2::PixelFormat"}) int format) {
            super((Pointer) null);
            allocate(rows, cols, stride, pData, dataSize, format);
        }

        public Image(@Cast({"unsigned int"}) int rows, @Cast({"unsigned int"}) int cols, @Cast({"unsigned int"}) int stride, @Cast({"unsigned char*"}) byte[] pData, @Cast({"unsigned int"}) int dataSize, @Cast({"FlyCapture2::PixelFormat"}) int format, @Cast({"FlyCapture2::BayerTileFormat"}) int bayerFormat) {
            super((Pointer) null);
            allocate(rows, cols, stride, pData, dataSize, format, bayerFormat);
        }

        public Image(@Cast({"unsigned int"}) int rows, @Cast({"unsigned int"}) int cols, @Cast({"unsigned int"}) int stride, @Cast({"unsigned char*"}) byte[] pData, @Cast({"unsigned int"}) int dataSize, @Cast({"FlyCapture2::PixelFormat"}) int format) {
            super((Pointer) null);
            allocate(rows, cols, stride, pData, dataSize, format);
        }

        public Image(@Cast({"unsigned int"}) int rows, @Cast({"unsigned int"}) int cols, @Cast({"unsigned int"}) int stride, @Cast({"unsigned char*"}) BytePointer pData, @Cast({"unsigned int"}) int dataSize, @Cast({"unsigned int"}) int receivedDataSize, @Cast({"FlyCapture2::PixelFormat"}) int format, @Cast({"FlyCapture2::BayerTileFormat"}) int bayerFormat) {
            super((Pointer) null);
            allocate(rows, cols, stride, pData, dataSize, receivedDataSize, format, bayerFormat);
        }

        public Image(@Cast({"unsigned int"}) int rows, @Cast({"unsigned int"}) int cols, @Cast({"unsigned int"}) int stride, @Cast({"unsigned char*"}) ByteBuffer pData, @Cast({"unsigned int"}) int dataSize, @Cast({"unsigned int"}) int receivedDataSize, @Cast({"FlyCapture2::PixelFormat"}) int format, @Cast({"FlyCapture2::BayerTileFormat"}) int bayerFormat) {
            super((Pointer) null);
            allocate(rows, cols, stride, pData, dataSize, receivedDataSize, format, bayerFormat);
        }

        public Image(@Cast({"unsigned int"}) int rows, @Cast({"unsigned int"}) int cols, @Cast({"unsigned int"}) int stride, @Cast({"unsigned char*"}) byte[] pData, @Cast({"unsigned int"}) int dataSize, @Cast({"unsigned int"}) int receivedDataSize, @Cast({"FlyCapture2::PixelFormat"}) int format, @Cast({"FlyCapture2::BayerTileFormat"}) int bayerFormat) {
            super((Pointer) null);
            allocate(rows, cols, stride, pData, dataSize, receivedDataSize, format, bayerFormat);
        }

        public Image(@Cast({"unsigned char*"}) BytePointer pData, @Cast({"unsigned int"}) int dataSize) {
            super((Pointer) null);
            allocate(pData, dataSize);
        }

        public Image(@Cast({"unsigned char*"}) ByteBuffer pData, @Cast({"unsigned int"}) int dataSize) {
            super((Pointer) null);
            allocate(pData, dataSize);
        }

        public Image(@Cast({"unsigned char*"}) byte[] pData, @Cast({"unsigned int"}) int dataSize) {
            super((Pointer) null);
            allocate(pData, dataSize);
        }

        public Image(@Cast({"unsigned int"}) int rows, @Cast({"unsigned int"}) int cols, @Cast({"FlyCapture2::PixelFormat"}) int format, @Cast({"FlyCapture2::BayerTileFormat"}) int bayerFormat) {
            super((Pointer) null);
            allocate(rows, cols, format, bayerFormat);
        }

        public Image(@Cast({"unsigned int"}) int rows, @Cast({"unsigned int"}) int cols, @Cast({"FlyCapture2::PixelFormat"}) int format) {
            super((Pointer) null);
            allocate(rows, cols, format);
        }

        public Image(@ByRef @Const Image image) {
            super((Pointer) null);
            allocate(image);
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class ImageMetadata extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int embeddedBrightness();

        public native ImageMetadata embeddedBrightness(int i);

        @Cast({"unsigned int"})
        public native int embeddedExposure();

        public native ImageMetadata embeddedExposure(int i);

        @Cast({"unsigned int"})
        public native int embeddedFrameCounter();

        public native ImageMetadata embeddedFrameCounter(int i);

        @Cast({"unsigned int"})
        public native int embeddedGPIOPinState();

        public native ImageMetadata embeddedGPIOPinState(int i);

        @Cast({"unsigned int"})
        public native int embeddedGain();

        public native ImageMetadata embeddedGain(int i);

        @Cast({"unsigned int"})
        public native int embeddedROIPosition();

        public native ImageMetadata embeddedROIPosition(int i);

        @Cast({"unsigned int"})
        public native int embeddedShutter();

        public native ImageMetadata embeddedShutter(int i);

        @Cast({"unsigned int"})
        public native int embeddedStrobePattern();

        public native ImageMetadata embeddedStrobePattern(int i);

        @Cast({"unsigned int"})
        public native int embeddedTimeStamp();

        public native ImageMetadata embeddedTimeStamp(int i);

        @Cast({"unsigned int"})
        public native int embeddedWhiteBalance();

        public native ImageMetadata embeddedWhiteBalance(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native ImageMetadata reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        static {
            Loader.load();
        }

        public ImageMetadata(Pointer p) {
            super(p);
        }

        public ImageMetadata(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public ImageMetadata position(long position) {
            return (ImageMetadata) super.position(position);
        }

        public ImageMetadata() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class ImageStatistics extends Pointer {
        public static final int BLUE = 3;
        public static final int GREEN = 2;
        public static final int GREY = 0;
        public static final int HUE = 4;
        public static final int LIGHTNESS = 6;
        public static final int NUM_STATISTICS_CHANNELS = 7;
        public static final int RED = 1;
        public static final int SATURATION = 5;

        private native void allocate();

        private native void allocate(@ByRef @Const ImageStatistics imageStatistics);

        private native void allocateArray(long j);

        @ByVal
        public native Error DisableAll();

        @ByVal
        public native Error EnableAll();

        @ByVal
        public native Error EnableGreyOnly();

        @ByVal
        public native Error EnableHSLOnly();

        @ByVal
        public native Error EnableRGBOnly();

        @ByVal
        public native Error GetChannelStatus(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, @Cast({"bool*"}) BoolPointer boolPointer);

        @ByVal
        public native Error GetChannelStatus(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, @Cast({"bool*"}) boolean[] zArr);

        @ByVal
        public native Error GetHistogram(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, @ByPtrPtr IntBuffer intBuffer);

        @ByVal
        public native Error GetHistogram(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, @ByPtrPtr IntPointer intPointer);

        @ByVal
        public native Error GetHistogram(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, @Cast({"int**"}) PointerPointer pointerPointer);

        @ByVal
        public native Error GetHistogram(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, @ByPtrPtr int[] iArr);

        @ByVal
        public native Error GetMean(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, FloatBuffer floatBuffer);

        @ByVal
        public native Error GetMean(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, FloatPointer floatPointer);

        @ByVal
        public native Error GetMean(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, float[] fArr);

        @ByVal
        public native Error GetNumPixelValues(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer);

        @ByVal
        public native Error GetNumPixelValues(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer);

        @ByVal
        public native Error GetNumPixelValues(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, @Cast({"unsigned int*"}) int[] iArr);

        @ByVal
        public native Error GetPixelValueRange(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int*"}) IntBuffer intBuffer2);

        @ByVal
        public native Error GetPixelValueRange(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int*"}) IntPointer intPointer2);

        @ByVal
        public native Error GetPixelValueRange(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, @Cast({"unsigned int*"}) int[] iArr, @Cast({"unsigned int*"}) int[] iArr2);

        @ByVal
        public native Error GetRange(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int*"}) IntBuffer intBuffer2);

        @ByVal
        public native Error GetRange(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int*"}) IntPointer intPointer2);

        @ByVal
        public native Error GetRange(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, @Cast({"unsigned int*"}) int[] iArr, @Cast({"unsigned int*"}) int[] iArr2);

        @ByVal
        public native Error GetStatistics(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i);

        @ByVal
        public native Error GetStatistics(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int*"}) IntBuffer intBuffer2, @Cast({"unsigned int*"}) IntBuffer intBuffer3, @Cast({"unsigned int*"}) IntBuffer intBuffer4, @Cast({"unsigned int*"}) IntBuffer intBuffer5, FloatBuffer floatBuffer, @ByPtrPtr IntBuffer intBuffer6);

        @ByVal
        public native Error GetStatistics(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int*"}) IntPointer intPointer2, @Cast({"unsigned int*"}) IntPointer intPointer3, @Cast({"unsigned int*"}) IntPointer intPointer4, @Cast({"unsigned int*"}) IntPointer intPointer5, FloatPointer floatPointer, @ByPtrPtr IntPointer intPointer6);

        @ByVal
        public native Error GetStatistics(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int*"}) IntPointer intPointer2, @Cast({"unsigned int*"}) IntPointer intPointer3, @Cast({"unsigned int*"}) IntPointer intPointer4, @Cast({"unsigned int*"}) IntPointer intPointer5, FloatPointer floatPointer, @Cast({"int**"}) PointerPointer pointerPointer);

        @ByVal
        public native Error GetStatistics(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, @Cast({"unsigned int*"}) int[] iArr, @Cast({"unsigned int*"}) int[] iArr2, @Cast({"unsigned int*"}) int[] iArr3, @Cast({"unsigned int*"}) int[] iArr4, @Cast({"unsigned int*"}) int[] iArr5, float[] fArr, @ByPtrPtr int[] iArr6);

        @ByVal
        public native Error SetChannelStatus(@Cast({"FlyCapture2::ImageStatistics::StatisticsChannel"}) int i, @Cast({"bool"}) boolean z);

        @ByRef
        @Name({"operator ="})
        public native ImageStatistics put(@ByRef @Const ImageStatistics imageStatistics);

        static {
            Loader.load();
        }

        public ImageStatistics(Pointer p) {
            super(p);
        }

        public ImageStatistics(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public ImageStatistics position(long position) {
            return (ImageStatistics) super.position(position);
        }

        public ImageStatistics() {
            super((Pointer) null);
            allocate();
        }

        public ImageStatistics(@ByRef @Const ImageStatistics other) {
            super((Pointer) null);
            allocate(other);
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class JPEGOption extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native JPEGOption progressive(boolean z);

        @Cast({"bool"})
        public native boolean progressive();

        @Cast({"unsigned int"})
        public native int quality();

        public native JPEGOption quality(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native JPEGOption reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        static {
            Loader.load();
        }

        public JPEGOption(Pointer p) {
            super(p);
        }

        public JPEGOption(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public JPEGOption position(long position) {
            return (JPEGOption) super.position(position);
        }

        public JPEGOption() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class JPG2Option extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int quality();

        public native JPG2Option quality(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native JPG2Option reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        static {
            Loader.load();
        }

        public JPG2Option(Pointer p) {
            super(p);
        }

        public JPG2Option(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public JPG2Option position(long position) {
            return (JPG2Option) super.position(position);
        }

        public JPG2Option() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class LUTData extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native LUTData enabled(boolean z);

        @Cast({"bool"})
        public native boolean enabled();

        @Cast({"unsigned int"})
        public native int inputBitDepth();

        public native LUTData inputBitDepth(int i);

        @Cast({"unsigned int"})
        public native int numBanks();

        public native LUTData numBanks(int i);

        @Cast({"unsigned int"})
        public native int numChannels();

        public native LUTData numChannels(int i);

        @Cast({"unsigned int"})
        public native int numEntries();

        public native LUTData numEntries(int i);

        @Cast({"unsigned int"})
        public native int outputBitDepth();

        public native LUTData outputBitDepth(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native LUTData reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        public native LUTData supported(boolean z);

        @Cast({"bool"})
        public native boolean supported();

        static {
            Loader.load();
        }

        public LUTData(Pointer p) {
            super(p);
        }

        public LUTData(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public LUTData position(long position) {
            return (LUTData) super.position(position);
        }

        public LUTData() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class MACAddress extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2);

        private native void allocateArray(long j);

        @Cast({"bool"})
        @Name({"operator =="})
        public native boolean equals(@ByRef @Const MACAddress mACAddress);

        @Cast({"bool"})
        @Name({"operator !="})
        public native boolean notEquals(@ByRef @Const MACAddress mACAddress);

        @Cast({"unsigned char"})
        public native byte octets(int i);

        @MemberGetter
        @Cast({"unsigned char*"})
        public native BytePointer octets();

        public native MACAddress octets(int i, byte b);

        static {
            Loader.load();
        }

        public MACAddress(Pointer p) {
            super(p);
        }

        public MACAddress(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public MACAddress position(long position) {
            return (MACAddress) super.position(position);
        }

        public MACAddress() {
            super((Pointer) null);
            allocate();
        }

        public MACAddress(@Cast({"unsigned int"}) int macAddressValHigh, @Cast({"unsigned int"}) int macAddressValLow) {
            super((Pointer) null);
            allocate(macAddressValHigh, macAddressValLow);
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class MJPGOption extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float frameRate();

        public native MJPGOption frameRate(float f);

        @Cast({"unsigned int"})
        public native int quality();

        public native MJPGOption quality(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native MJPGOption reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        static {
            Loader.load();
        }

        public MJPGOption(Pointer p) {
            super(p);
        }

        public MJPGOption(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public MJPGOption position(long position) {
            return (MJPGOption) super.position(position);
        }

        public MJPGOption() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class PGMOption extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native PGMOption binaryFile(boolean z);

        @Cast({"bool"})
        public native boolean binaryFile();

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native PGMOption reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        static {
            Loader.load();
        }

        public PGMOption(Pointer p) {
            super(p);
        }

        public PGMOption(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public PGMOption position(long position) {
            return (PGMOption) super.position(position);
        }

        public PGMOption() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class PGRGuid extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"bool"})
        @Name({"operator =="})
        public native boolean equals(@ByRef @Const PGRGuid pGRGuid);

        @Cast({"bool"})
        @Name({"operator !="})
        public native boolean notEquals(@ByRef @Const PGRGuid pGRGuid);

        @Cast({"unsigned int"})
        public native int value(int i);

        public native PGRGuid value(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer value();

        static {
            Loader.load();
        }

        public PGRGuid(Pointer p) {
            super(p);
        }

        public PGRGuid(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public PGRGuid position(long position) {
            return (PGRGuid) super.position(position);
        }

        public PGRGuid() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class PNGOption extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int compressionLevel();

        public native PNGOption compressionLevel(int i);

        public native PNGOption interlaced(boolean z);

        @Cast({"bool"})
        public native boolean interlaced();

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native PNGOption reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        static {
            Loader.load();
        }

        public PNGOption(Pointer p) {
            super(p);
        }

        public PNGOption(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public PNGOption position(long position) {
            return (PNGOption) super.position(position);
        }

        public PNGOption() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class PPMOption extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native PPMOption binaryFile(boolean z);

        @Cast({"bool"})
        public native boolean binaryFile();

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native PPMOption reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        static {
            Loader.load();
        }

        public PPMOption(Pointer p) {
            super(p);
        }

        public PPMOption(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public PPMOption position(long position) {
            return (PPMOption) super.position(position);
        }

        public PPMOption() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class Property extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"FlyCapture2::PropertyType"}) int i);

        private native void allocateArray(long j);

        public native Property absControl(boolean z);

        @Cast({"bool"})
        public native boolean absControl();

        public native float absValue();

        public native Property absValue(float f);

        public native Property autoManualMode(boolean z);

        @Cast({"bool"})
        public native boolean autoManualMode();

        public native Property onOff(boolean z);

        @Cast({"bool"})
        public native boolean onOff();

        public native Property onePush(boolean z);

        @Cast({"bool"})
        public native boolean onePush();

        public native Property present(boolean z);

        @Cast({"bool"})
        public native boolean present();

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native Property reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"FlyCapture2::PropertyType"})
        public native int type();

        public native Property type(int i);

        @Cast({"unsigned int"})
        public native int valueA();

        public native Property valueA(int i);

        @Cast({"unsigned int"})
        public native int valueB();

        public native Property valueB(int i);

        static {
            Loader.load();
        }

        public Property(Pointer p) {
            super(p);
        }

        public Property(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public Property position(long position) {
            return (Property) super.position(position);
        }

        public Property() {
            super((Pointer) null);
            allocate();
        }

        public Property(@Cast({"FlyCapture2::PropertyType"}) int propType) {
            super((Pointer) null);
            allocate(propType);
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class PropertyInfo extends Pointer {
        private native void allocate();

        private native void allocate(@Cast({"FlyCapture2::PropertyType"}) int i);

        private native void allocateArray(long j);

        public native float absMax();

        public native PropertyInfo absMax(float f);

        public native float absMin();

        public native PropertyInfo absMin(float f);

        public native PropertyInfo absValSupported(boolean z);

        @Cast({"bool"})
        public native boolean absValSupported();

        public native PropertyInfo autoSupported(boolean z);

        @Cast({"bool"})
        public native boolean autoSupported();

        public native PropertyInfo manualSupported(boolean z);

        @Cast({"bool"})
        public native boolean manualSupported();

        @Cast({"unsigned int"})
        public native int max();

        public native PropertyInfo max(int i);

        @Cast({"unsigned int"})
        public native int min();

        public native PropertyInfo min(int i);

        public native PropertyInfo onOffSupported(boolean z);

        @Cast({"bool"})
        public native boolean onOffSupported();

        public native PropertyInfo onePushSupported(boolean z);

        @Cast({"bool"})
        public native boolean onePushSupported();

        @Cast({"char"})
        public native byte pUnitAbbr(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer pUnitAbbr();

        public native PropertyInfo pUnitAbbr(int i, byte b);

        @Cast({"char"})
        public native byte pUnits(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer pUnits();

        public native PropertyInfo pUnits(int i, byte b);

        public native PropertyInfo present(boolean z);

        @Cast({"bool"})
        public native boolean present();

        public native PropertyInfo readOutSupported(boolean z);

        @Cast({"bool"})
        public native boolean readOutSupported();

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native PropertyInfo reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"FlyCapture2::PropertyType"})
        public native int type();

        public native PropertyInfo type(int i);

        static {
            Loader.load();
        }

        public PropertyInfo(Pointer p) {
            super(p);
        }

        public PropertyInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public PropertyInfo position(long position) {
            return (PropertyInfo) super.position(position);
        }

        public PropertyInfo() {
            super((Pointer) null);
            allocate();
        }

        public PropertyInfo(@Cast({"FlyCapture2::PropertyType"}) int propType) {
            super((Pointer) null);
            allocate(propType);
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class StrobeControl extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float delay();

        public native StrobeControl delay(float f);

        public native float duration();

        public native StrobeControl duration(float f);

        public native StrobeControl onOff(boolean z);

        @Cast({"bool"})
        public native boolean onOff();

        @Cast({"unsigned int"})
        public native int polarity();

        public native StrobeControl polarity(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native StrobeControl reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int source();

        public native StrobeControl source(int i);

        static {
            Loader.load();
        }

        public StrobeControl(Pointer p) {
            super(p);
        }

        public StrobeControl(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public StrobeControl position(long position) {
            return (StrobeControl) super.position(position);
        }

        public StrobeControl() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class StrobeInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native float maxValue();

        public native StrobeInfo maxValue(float f);

        public native float minValue();

        public native StrobeInfo minValue(float f);

        public native StrobeInfo onOffSupported(boolean z);

        @Cast({"bool"})
        public native boolean onOffSupported();

        public native StrobeInfo polaritySupported(boolean z);

        @Cast({"bool"})
        public native boolean polaritySupported();

        public native StrobeInfo present(boolean z);

        @Cast({"bool"})
        public native boolean present();

        public native StrobeInfo readOutSupported(boolean z);

        @Cast({"bool"})
        public native boolean readOutSupported();

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native StrobeInfo reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int source();

        public native StrobeInfo source(int i);

        static {
            Loader.load();
        }

        public StrobeInfo(Pointer p) {
            super(p);
        }

        public StrobeInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public StrobeInfo position(long position) {
            return (StrobeInfo) super.position(position);
        }

        public StrobeInfo() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    public static class SystemInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"FlyCapture2::ByteOrder"})
        public native int byteOrder();

        public native SystemInfo byteOrder(int i);

        @Cast({"char"})
        public native byte cpuDescription(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer cpuDescription();

        public native SystemInfo cpuDescription(int i, byte b);

        @Cast({"char"})
        public native byte driverList(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer driverList();

        public native SystemInfo driverList(int i, byte b);

        @Cast({"char"})
        public native byte gpuDescription(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer gpuDescription();

        public native SystemInfo gpuDescription(int i, byte b);

        @Cast({"char"})
        public native byte libraryList(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer libraryList();

        public native SystemInfo libraryList(int i, byte b);

        @Cast({"size_t"})
        public native long numCpuCores();

        public native SystemInfo numCpuCores(long j);

        @Cast({"char"})
        public native byte osDescription(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer osDescription();

        public native SystemInfo osDescription(int i, byte b);

        @Cast({"FlyCapture2::OSType"})
        public native int osType();

        public native SystemInfo osType(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native SystemInfo reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"size_t"})
        public native long screenHeight();

        public native SystemInfo screenHeight(long j);

        @Cast({"size_t"})
        public native long screenWidth();

        public native SystemInfo screenWidth(long j);

        @Cast({"size_t"})
        public native long sysMemSize();

        public native SystemInfo sysMemSize(long j);

        static {
            Loader.load();
        }

        public SystemInfo() {
            super((Pointer) null);
            allocate();
        }

        public SystemInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public SystemInfo(Pointer p) {
            super(p);
        }

        public SystemInfo position(long position) {
            return (SystemInfo) super.position(position);
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class TIFFOption extends Pointer {
        public static final int ADOBE_DEFLATE = 4;
        public static final int CCITTFAX3 = 5;
        public static final int CCITTFAX4 = 6;
        public static final int DEFLATE = 3;
        public static final int JPEG = 8;
        public static final int LZW = 7;
        public static final int NONE = 1;
        public static final int PACKBITS = 2;

        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"FlyCapture2::TIFFOption::CompressionMethod"})
        public native int compression();

        public native TIFFOption compression(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native TIFFOption reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        static {
            Loader.load();
        }

        public TIFFOption(Pointer p) {
            super(p);
        }

        public TIFFOption(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public TIFFOption position(long position) {
            return (TIFFOption) super.position(position);
        }

        public TIFFOption() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class TimeStamp extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int cycleCount();

        public native TimeStamp cycleCount(int i);

        @Cast({"unsigned int"})
        public native int cycleOffset();

        public native TimeStamp cycleOffset(int i);

        @Cast({"unsigned int"})
        public native int cycleSeconds();

        public native TimeStamp cycleSeconds(int i);

        @Cast({"unsigned int"})
        public native int microSeconds();

        public native TimeStamp microSeconds(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native TimeStamp reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        public native long seconds();

        public native TimeStamp seconds(long j);

        static {
            Loader.load();
        }

        public TimeStamp(Pointer p) {
            super(p);
        }

        public TimeStamp(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public TimeStamp position(long position) {
            return (TimeStamp) super.position(position);
        }

        public TimeStamp() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class TopologyNode extends Pointer {
        public static final int BUS = 1;
        public static final int CAMERA = 2;
        public static final int COMPUTER = 0;
        public static final int CONNECTED_TO_CHILD = 3;
        public static final int CONNECTED_TO_PARENT = 2;
        public static final int NODE = 3;
        public static final int NOT_CONNECTED = 1;

        private native void allocate();

        private native void allocate(@ByVal PGRGuid pGRGuid, int i, @Cast({"FlyCapture2::TopologyNode::NodeType"}) int i2, @Cast({"FlyCapture2::InterfaceType"}) int i3);

        private native void allocate(@ByRef @Const TopologyNode topologyNode);

        private native void allocateArray(long j);

        public native void AddChild(@ByVal TopologyNode topologyNode);

        public native void AddPort(@Cast({"FlyCapture2::TopologyNode::PortType"}) int i);

        @Cast({"bool"})
        public native boolean AssignGuidToNode(@ByVal PGRGuid pGRGuid, int i);

        @Cast({"bool"})
        public native boolean AssignGuidToNode(@ByVal PGRGuid pGRGuid, int i, @Cast({"FlyCapture2::TopologyNode::NodeType"}) int i2);

        @ByVal
        public native TopologyNode GetChild(@Cast({"unsigned int"}) int i);

        public native int GetDeviceId();

        @ByVal
        public native PGRGuid GetGuid();

        @Cast({"FlyCapture2::InterfaceType"})
        public native int GetInterfaceType();

        @Cast({"FlyCapture2::TopologyNode::NodeType"})
        public native int GetNodeType();

        @Cast({"unsigned int"})
        public native int GetNumChildren();

        @Cast({"unsigned int"})
        public native int GetNumPorts();

        @Cast({"FlyCapture2::TopologyNode::PortType"})
        public native int GetPortType(@Cast({"unsigned int"}) int i);

        @ByRef
        @Name({"operator ="})
        public native TopologyNode put(@ByRef @Const TopologyNode topologyNode);

        static {
            Loader.load();
        }

        public TopologyNode(Pointer p) {
            super(p);
        }

        public TopologyNode(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public TopologyNode position(long position) {
            return (TopologyNode) super.position(position);
        }

        public TopologyNode() {
            super((Pointer) null);
            allocate();
        }

        public TopologyNode(@ByVal PGRGuid guid, int deviceId, @Cast({"FlyCapture2::TopologyNode::NodeType"}) int nodeType, @Cast({"FlyCapture2::InterfaceType"}) int interfaceType) {
            super((Pointer) null);
            allocate(guid, deviceId, nodeType, interfaceType);
        }

        public TopologyNode(@ByRef @Const TopologyNode other) {
            super((Pointer) null);
            allocate(other);
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class TriggerMode extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int mode();

        public native TriggerMode mode(int i);

        public native TriggerMode onOff(boolean z);

        @Cast({"bool"})
        public native boolean onOff();

        @Cast({"unsigned int"})
        public native int parameter();

        public native TriggerMode parameter(int i);

        @Cast({"unsigned int"})
        public native int polarity();

        public native TriggerMode polarity(int i);

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native TriggerMode reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        @Cast({"unsigned int"})
        public native int source();

        public native TriggerMode source(int i);

        static {
            Loader.load();
        }

        public TriggerMode(Pointer p) {
            super(p);
        }

        public TriggerMode(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public TriggerMode position(long position) {
            return (TriggerMode) super.position(position);
        }

        public TriggerMode() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    @NoOffset
    public static class TriggerModeInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int modeMask();

        public native TriggerModeInfo modeMask(int i);

        public native TriggerModeInfo onOffSupported(boolean z);

        @Cast({"bool"})
        public native boolean onOffSupported();

        public native TriggerModeInfo polaritySupported(boolean z);

        @Cast({"bool"})
        public native boolean polaritySupported();

        public native TriggerModeInfo present(boolean z);

        @Cast({"bool"})
        public native boolean present();

        public native TriggerModeInfo readOutSupported(boolean z);

        @Cast({"bool"})
        public native boolean readOutSupported();

        @Cast({"unsigned int"})
        public native int reserved(int i);

        public native TriggerModeInfo reserved(int i, int i2);

        @MemberGetter
        @Cast({"unsigned int*"})
        public native IntPointer reserved();

        public native TriggerModeInfo softwareTriggerSupported(boolean z);

        @Cast({"bool"})
        public native boolean softwareTriggerSupported();

        @Cast({"unsigned int"})
        public native int sourceMask();

        public native TriggerModeInfo sourceMask(int i);

        public native TriggerModeInfo valueReadable(boolean z);

        @Cast({"bool"})
        public native boolean valueReadable();

        static {
            Loader.load();
        }

        public TriggerModeInfo(Pointer p) {
            super(p);
        }

        public TriggerModeInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public TriggerModeInfo position(long position) {
            return (TriggerModeInfo) super.position(position);
        }

        public TriggerModeInfo() {
            super((Pointer) null);
            allocate();
        }
    }

    @Namespace("FlyCapture2")
    public static class Utilities extends Pointer {
        @ByVal
        public static native Error CheckDriver(@Const PGRGuid pGRGuid);

        @ByVal
        public static native Error GetDriverDeviceName(@Const PGRGuid pGRGuid, @StdString String str);

        @ByVal
        public static native Error GetDriverDeviceName(@Const PGRGuid pGRGuid, @StdString BytePointer bytePointer);

        @ByVal
        public static native Error GetLibraryVersion(FC2Version fC2Version);

        @ByVal
        public static native Error GetSystemInfo(SystemInfo systemInfo);

        @ByVal
        public static native Error LaunchBrowser(String str);

        @ByVal
        public static native Error LaunchBrowser(@Cast({"const char*"}) BytePointer bytePointer);

        @ByVal
        public static native Error LaunchCommand(String str);

        @ByVal
        public static native Error LaunchCommand(@Cast({"const char*"}) BytePointer bytePointer);

        @ByVal
        public static native Error LaunchCommandAsync(String str, AsyncCommandCallback asyncCommandCallback, Pointer pointer);

        @ByVal
        public static native Error LaunchCommandAsync(@Cast({"const char*"}) BytePointer bytePointer, AsyncCommandCallback asyncCommandCallback, Pointer pointer);

        @ByVal
        public static native Error LaunchHelp(String str);

        @ByVal
        public static native Error LaunchHelp(@Cast({"const char*"}) BytePointer bytePointer);

        static {
            Loader.load();
        }

        public Utilities(Pointer p) {
            super(p);
        }
    }

    @Namespace("FlyCapture2")
    @MemberGetter
    @Cast({"const unsigned int"})
    public static native int sk_maxNumPorts();

    @Namespace("FlyCapture2")
    @MemberGetter
    @Cast({"const unsigned int"})
    public static native int sk_maxStringLength();

    static {
        Loader.load();
    }
}
