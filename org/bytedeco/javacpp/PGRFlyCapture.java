package org.bytedeco.javacpp;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.bytedeco.javacpp.annotation.ByPtrPtr;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.MemberGetter;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.Opaque;
import org.bytedeco.javacpp.presets.PGRFlyCapture.AbstractFlyCaptureImage;

public class PGRFlyCapture extends org.bytedeco.javacpp.presets.PGRFlyCapture {
    public static final int FCCM_FORCE_QUADLET = Integer.MAX_VALUE;
    public static final int FCPF_FORCE_QUADLET = Integer.MAX_VALUE;
    public static final int FLYCAPTURE_411YUV8 = 2;
    public static final int FLYCAPTURE_422YUV8 = 4;
    public static final int FLYCAPTURE_444YUV8 = 8;
    public static final int FLYCAPTURE_AIM = 2;
    public static final int FLYCAPTURE_ALREADY_INITIALIZED = 5;
    public static final int FLYCAPTURE_ALREADY_STARTED = 6;
    public static final int FLYCAPTURE_ANY = 8;
    public static final int FLYCAPTURE_AUTO_EXPOSURE = 1;
    public static final int FLYCAPTURE_BGR = 268435457;
    public static final int FLYCAPTURE_BGRU = 268435458;
    public static final int FLYCAPTURE_BLACK_AND_WHITE = 0;
    public static final int FLYCAPTURE_BRIGHTNESS = 0;
    public static final int FLYCAPTURE_BUFFER_SIZE_TOO_SMALL = 26;
    public static final int FLYCAPTURE_BUMBLEBEE = 10;
    public static final int FLYCAPTURE_BUMBLEBEE2 = 11;
    public static final int FLYCAPTURE_BUMBLEBEEXB3 = 12;
    public static final int FLYCAPTURE_BUS_ERROR = 3;
    public static final int FLYCAPTURE_BUS_MESSAGE = 999999999;
    public static final int FLYCAPTURE_BUS_RESET = 0;
    public static final int FLYCAPTURE_CALLBACK_ALREADY_REGISTERED = 8;
    public static final int FLYCAPTURE_CALLBACK_NOT_REGISTERED = 7;
    public static final int FLYCAPTURE_CAMERACONTROL_PROBLEM = 9;
    public static final int FLYCAPTURE_CHAMELEON = 14;
    public static final int FLYCAPTURE_COLOR = 1;
    public static final int FLYCAPTURE_COULD_NOT_OPEN_DEVICE_HANDLE = 11;
    public static final int FLYCAPTURE_COULD_NOT_OPEN_FILE = 10;
    public static final int FLYCAPTURE_DEPRECATED = 25;
    public static final int FLYCAPTURE_DEVICE_ARRIVAL = 1;
    public static final int FLYCAPTURE_DEVICE_BUSY = 24;
    public static final int FLYCAPTURE_DEVICE_REMOVAL = 2;
    public static final int FLYCAPTURE_DISABLE = 0;
    public static final int FLYCAPTURE_DRAGONFLY = 1;
    public static final int FLYCAPTURE_DRAGONFLY2 = 9;
    public static final int FLYCAPTURE_DRAGONFLY_EXPRESS = 6;
    public static final int FLYCAPTURE_EDGE_SENSING = 1;
    public static final int FLYCAPTURE_ERROR_UNKNOWN = 19;
    public static final int FLYCAPTURE_FAILED = 1;
    public static final int FLYCAPTURE_FILEFORMAT_BMP = 2;
    public static final int FLYCAPTURE_FILEFORMAT_JPG = 3;
    public static final int FLYCAPTURE_FILEFORMAT_PGM = 0;
    public static final int FLYCAPTURE_FILEFORMAT_PNG = 4;
    public static final int FLYCAPTURE_FILEFORMAT_PPM = 1;
    public static final int FLYCAPTURE_FILEFORMAT_RAW = 5;
    public static final int FLYCAPTURE_FIREFLY = 0;
    public static final int FLYCAPTURE_FIREFLY_MV = 8;
    public static final int FLYCAPTURE_FLEA = 5;
    public static final int FLYCAPTURE_FLEA2 = 7;
    public static final int FLYCAPTURE_FOCUS = 8;
    public static final int FLYCAPTURE_FRAMERATE_120 = 7;
    public static final int FLYCAPTURE_FRAMERATE_15 = 3;
    public static final int FLYCAPTURE_FRAMERATE_1_875 = 0;
    public static final int FLYCAPTURE_FRAMERATE_240 = 8;
    public static final int FLYCAPTURE_FRAMERATE_30 = 4;
    public static final int FLYCAPTURE_FRAMERATE_3_75 = 1;
    public static final int FLYCAPTURE_FRAMERATE_60 = 6;
    public static final int FLYCAPTURE_FRAMERATE_7_5 = 2;
    public static final int FLYCAPTURE_FRAMERATE_ANY = 11;
    public static final int FLYCAPTURE_FRAMERATE_CUSTOM = 10;
    public static final int FLYCAPTURE_FRAMERATE_UNUSED = 5;
    public static final int FLYCAPTURE_FRAME_RATE = 15;
    public static final int FLYCAPTURE_GAIN = 13;
    public static final int FLYCAPTURE_GAMMA = 6;
    public static final int FLYCAPTURE_GRABBED_IMAGE = 4;
    public static final int FLYCAPTURE_GRASSHOPPER = 13;
    public static final int FLYCAPTURE_HQLINEAR = 5;
    public static final int FLYCAPTURE_HUE = 4;
    public static final int FLYCAPTURE_IMAGE_FILTER_ALL = -1;
    public static final int FLYCAPTURE_IMAGE_FILTER_NONE = 0;
    public static final int FLYCAPTURE_IMAGE_FILTER_SCORPION_CROSSTALK = 1;
    public static final int FLYCAPTURE_INFINITE = -1;
    public static final int FLYCAPTURE_INVALID_ARGUMENT = 2;
    public static final int FLYCAPTURE_INVALID_CONTEXT = 3;
    public static final int FLYCAPTURE_INVALID_CUSTOM_SIZE = 20;
    public static final int FLYCAPTURE_INVALID_MODE = 18;
    public static final int FLYCAPTURE_IRIS = 7;
    public static final int FLYCAPTURE_MAX_BANDWIDTH_EXCEEDED = 16;
    public static final int FLYCAPTURE_MEMORY_ALLOC_ERROR = 12;
    public static final int FLYCAPTURE_MESSAGE_BUS_RESET = 2;
    public static final int FLYCAPTURE_MESSAGE_DEVICE_ARRIVAL = 3;
    public static final int FLYCAPTURE_MESSAGE_DEVICE_REMOVAL = 4;
    public static final int FLYCAPTURE_MONO16 = 32;
    public static final int FLYCAPTURE_MONO8 = 1;
    public static final int FLYCAPTURE_NEAREST_NEIGHBOR = 2;
    public static final int FLYCAPTURE_NEAREST_NEIGHBOR_FAST = 3;
    public static final int FLYCAPTURE_NON_PGR_CAMERA = 17;
    public static final int FLYCAPTURE_NOT_IMPLEMENTED = 4;
    public static final int FLYCAPTURE_NOT_INITIALIZED = 14;
    public static final int FLYCAPTURE_NOT_STARTED = 15;
    public static final int FLYCAPTURE_NO_IMAGE = 13;
    public static final int FLYCAPTURE_NUM_FRAMERATES = 9;
    public static final int FLYCAPTURE_NUM_VIDEOMODES = 23;
    public static final int FLYCAPTURE_OK = 0;
    public static final int FLYCAPTURE_PAN = 10;
    public static final int FLYCAPTURE_RAW16 = 1024;
    public static final int FLYCAPTURE_RAW8 = 512;
    public static final int FLYCAPTURE_REGISTER_READ = 5;
    public static final int FLYCAPTURE_REGISTER_READ_BLOCK = 6;
    public static final int FLYCAPTURE_REGISTER_WRITE = 7;
    public static final int FLYCAPTURE_REGISTER_WRITE_BLOCK = 8;
    public static final int FLYCAPTURE_RGB16 = 64;
    public static final int FLYCAPTURE_RGB8 = 16;
    public static final int FLYCAPTURE_RIGOROUS = 4;
    public static final int FLYCAPTURE_S100 = 0;
    public static final int FLYCAPTURE_S1600 = 5;
    public static final int FLYCAPTURE_S200 = 1;
    public static final int FLYCAPTURE_S3200 = 6;
    public static final int FLYCAPTURE_S400 = 2;
    public static final int FLYCAPTURE_S480 = 3;
    public static final int FLYCAPTURE_S800 = 4;
    public static final int FLYCAPTURE_SATURATION = 5;
    public static final int FLYCAPTURE_SCORPION = 3;
    public static final int FLYCAPTURE_SHARPNESS = 2;
    public static final int FLYCAPTURE_SHUTTER = 12;
    public static final int FLYCAPTURE_SOFTWARE_WHITEBALANCE = 16;
    public static final int FLYCAPTURE_SPEED_FORCE_QUADLET = Integer.MAX_VALUE;
    public static final int FLYCAPTURE_SPEED_UNKNOWN = -1;
    public static final int FLYCAPTURE_STIPPLEDFORMAT_BGGR = 0;
    public static final int FLYCAPTURE_STIPPLEDFORMAT_DEFAULT = 4;
    public static final int FLYCAPTURE_STIPPLEDFORMAT_GBRG = 1;
    public static final int FLYCAPTURE_STIPPLEDFORMAT_GRBG = 2;
    public static final int FLYCAPTURE_STIPPLEDFORMAT_RGGB = 3;
    public static final int FLYCAPTURE_S_FASTEST = 7;
    public static final int FLYCAPTURE_S_MONO16 = 128;
    public static final int FLYCAPTURE_S_RGB16 = 256;
    public static final int FLYCAPTURE_TEMPERATURE = 17;
    public static final int FLYCAPTURE_TILT = 11;
    public static final int FLYCAPTURE_TIMEOUT = 21;
    public static final int FLYCAPTURE_TOO_MANY_LOCKED_BUFFERS = 22;
    public static final int FLYCAPTURE_TRIGGER_DELAY = 14;
    public static final int FLYCAPTURE_TYPHOON = 4;
    public static final int FLYCAPTURE_UNKNOWN = -1;
    public static final int FLYCAPTURE_VERSION_MISMATCH = 23;
    public static final int FLYCAPTURE_VIDEOMODE_1024x768RGB = 21;
    public static final int FLYCAPTURE_VIDEOMODE_1024x768Y16 = 9;
    public static final int FLYCAPTURE_VIDEOMODE_1024x768Y8 = 8;
    public static final int FLYCAPTURE_VIDEOMODE_1024x768YUV422 = 20;
    public static final int FLYCAPTURE_VIDEOMODE_1280x960RGB = 23;
    public static final int FLYCAPTURE_VIDEOMODE_1280x960Y16 = 24;
    public static final int FLYCAPTURE_VIDEOMODE_1280x960Y8 = 10;
    public static final int FLYCAPTURE_VIDEOMODE_1280x960YUV422 = 22;
    public static final int FLYCAPTURE_VIDEOMODE_1600x1200RGB = 51;
    public static final int FLYCAPTURE_VIDEOMODE_1600x1200Y16 = 52;
    public static final int FLYCAPTURE_VIDEOMODE_1600x1200Y8 = 11;
    public static final int FLYCAPTURE_VIDEOMODE_1600x1200YUV422 = 50;
    public static final int FLYCAPTURE_VIDEOMODE_160x120YUV444 = 0;
    public static final int FLYCAPTURE_VIDEOMODE_320x240YUV422 = 1;
    public static final int FLYCAPTURE_VIDEOMODE_640x480RGB = 4;
    public static final int FLYCAPTURE_VIDEOMODE_640x480Y16 = 6;
    public static final int FLYCAPTURE_VIDEOMODE_640x480Y8 = 5;
    public static final int FLYCAPTURE_VIDEOMODE_640x480YUV411 = 2;
    public static final int FLYCAPTURE_VIDEOMODE_640x480YUV422 = 3;
    public static final int FLYCAPTURE_VIDEOMODE_800x600RGB = 18;
    public static final int FLYCAPTURE_VIDEOMODE_800x600Y16 = 19;
    public static final int FLYCAPTURE_VIDEOMODE_800x600Y8 = 7;
    public static final int FLYCAPTURE_VIDEOMODE_800x600YUV422 = 17;
    public static final int FLYCAPTURE_VIDEOMODE_ANY = 16;
    public static final int FLYCAPTURE_VIDEOMODE_CUSTOM = 15;
    public static final int FLYCAPTURE_WHITE_BALANCE = 3;
    public static final int FLYCAPTURE_ZOOM = 9;
    public static final int PGRFLYCAPTURE_VERSION = 108107;

    @Name({"void"})
    @Namespace
    @Opaque
    public static class FlyCaptureContext extends Pointer {
        public FlyCaptureContext() {
            super((Pointer) null);
        }

        public FlyCaptureContext(Pointer p) {
            super(p);
        }
    }

    public static class FlyCaptureDriverInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"char"})
        public native byte pszDriverName(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer pszDriverName();

        public native FlyCaptureDriverInfo pszDriverName(int i, byte b);

        @Cast({"char"})
        public native byte pszVersion(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer pszVersion();

        public native FlyCaptureDriverInfo pszVersion(int i, byte b);

        static {
            Loader.load();
        }

        public FlyCaptureDriverInfo() {
            super((Pointer) null);
            allocate();
        }

        public FlyCaptureDriverInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FlyCaptureDriverInfo(Pointer p) {
            super(p);
        }

        public FlyCaptureDriverInfo position(long position) {
            return (FlyCaptureDriverInfo) super.position(position);
        }
    }

    public static class FlyCaptureImage extends AbstractFlyCaptureImage {
        private native void allocate();

        private native void allocateArray(long j);

        public native FlyCaptureImage bStippled(boolean z);

        @Cast({"bool"})
        public native boolean bStippled();

        public native int iCols();

        public native FlyCaptureImage iCols(int i);

        public native int iNumImages();

        public native FlyCaptureImage iNumImages(int i);

        public native int iRowInc();

        public native FlyCaptureImage iRowInc(int i);

        public native int iRows();

        public native FlyCaptureImage iRows(int i);

        @Cast({"unsigned char*"})
        public native BytePointer pData();

        public native FlyCaptureImage pData(BytePointer bytePointer);

        @Cast({"FlyCapturePixelFormat"})
        public native int pixelFormat();

        public native FlyCaptureImage pixelFormat(int i);

        public native FlyCaptureImage timeStamp(FlyCaptureTimestamp flyCaptureTimestamp);

        @ByRef
        public native FlyCaptureTimestamp timeStamp();

        @Cast({"unsigned long"})
        public native int ulReserved(int i);

        @MemberGetter
        @Cast({"unsigned long*"})
        public native IntPointer ulReserved();

        public native FlyCaptureImage ulReserved(int i, int i2);

        @Cast({"FlyCaptureVideoMode"})
        public native int videoMode();

        public native FlyCaptureImage videoMode(int i);

        static {
            Loader.load();
        }

        public FlyCaptureImage() {
            super((Pointer) null);
            allocate();
        }

        public FlyCaptureImage(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FlyCaptureImage(Pointer p) {
            super(p);
        }

        public FlyCaptureImage position(long position) {
            return (FlyCaptureImage) super.position(position);
        }
    }

    public static class FlyCaptureImageEvent extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned char*"})
        public native BytePointer pBuffer();

        public native FlyCaptureImageEvent pBuffer(BytePointer bytePointer);

        public native FlyCaptureImageEvent pInternal(Pointer pointer);

        public native Pointer pInternal();

        @Cast({"unsigned int"})
        public native int uiBufferIndex();

        public native FlyCaptureImageEvent uiBufferIndex(int i);

        @Cast({"unsigned int"})
        public native int uiSeqNum();

        public native FlyCaptureImageEvent uiSeqNum(int i);

        @Cast({"unsigned int"})
        public native int uiSizeBytes();

        public native FlyCaptureImageEvent uiSizeBytes(int i);

        @Cast({"unsigned long"})
        public native int ulReserved(int i);

        @MemberGetter
        @Cast({"unsigned long*"})
        public native IntPointer ulReserved();

        public native FlyCaptureImageEvent ulReserved(int i, int i2);

        static {
            Loader.load();
        }

        public FlyCaptureImageEvent() {
            super((Pointer) null);
            allocate();
        }

        public FlyCaptureImageEvent(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FlyCaptureImageEvent(Pointer p) {
            super(p);
        }

        public FlyCaptureImageEvent position(long position) {
            return (FlyCaptureImageEvent) super.position(position);
        }
    }

    public static class FlyCaptureImagePlus extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @ByRef
        public native FlyCaptureImage image();

        public native FlyCaptureImagePlus image(FlyCaptureImage flyCaptureImage);

        @Cast({"unsigned int"})
        public native int uiBufSeqNum();

        public native FlyCaptureImagePlus uiBufSeqNum(int i);

        @Cast({"unsigned int"})
        public native int uiBufferIndex();

        public native FlyCaptureImagePlus uiBufferIndex(int i);

        @Cast({"unsigned int"})
        public native int uiSeqNum();

        public native FlyCaptureImagePlus uiSeqNum(int i);

        @Cast({"unsigned long"})
        public native int ulReserved(int i);

        @MemberGetter
        @Cast({"unsigned long*"})
        public native IntPointer ulReserved();

        public native FlyCaptureImagePlus ulReserved(int i, int i2);

        static {
            Loader.load();
        }

        public FlyCaptureImagePlus() {
            super((Pointer) null);
            allocate();
        }

        public FlyCaptureImagePlus(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FlyCaptureImagePlus(Pointer p) {
            super(p);
        }

        public FlyCaptureImagePlus position(long position) {
            return (FlyCaptureImagePlus) super.position(position);
        }
    }

    public static class FlyCaptureInfoEx extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"FlyCaptureBusSpeed"})
        public native int CameraMaxBusSpeed();

        public native FlyCaptureInfoEx CameraMaxBusSpeed(int i);

        @Cast({"FlyCaptureCameraModel"})
        public native int CameraModel();

        public native FlyCaptureInfoEx CameraModel(int i);

        @Cast({"FlyCaptureCameraType"})
        public native int CameraType();

        public native FlyCaptureInfoEx CameraType(int i);

        @Cast({"FlyCaptureCameraSerialNumber"})
        public native int SerialNumber();

        public native FlyCaptureInfoEx SerialNumber(int i);

        public native int iBusNum();

        public native FlyCaptureInfoEx iBusNum(int i);

        public native int iDCAMVer();

        public native FlyCaptureInfoEx iDCAMVer(int i);

        public native int iInitialized();

        public native FlyCaptureInfoEx iInitialized(int i);

        public native int iNodeNum();

        public native FlyCaptureInfoEx iNodeNum(int i);

        @Cast({"char"})
        public native byte pszModelName(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer pszModelName();

        public native FlyCaptureInfoEx pszModelName(int i, byte b);

        @Cast({"char"})
        public native byte pszSensorInfo(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer pszSensorInfo();

        public native FlyCaptureInfoEx pszSensorInfo(int i, byte b);

        @Cast({"char"})
        public native byte pszVendorName(int i);

        @MemberGetter
        @Cast({"char*"})
        public native BytePointer pszVendorName();

        public native FlyCaptureInfoEx pszVendorName(int i, byte b);

        @Cast({"unsigned long"})
        public native int ulReserved(int i);

        @MemberGetter
        @Cast({"unsigned long*"})
        public native IntPointer ulReserved();

        public native FlyCaptureInfoEx ulReserved(int i, int i2);

        static {
            Loader.load();
        }

        public FlyCaptureInfoEx() {
            super((Pointer) null);
            allocate();
        }

        public FlyCaptureInfoEx(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FlyCaptureInfoEx(Pointer p) {
            super(p);
        }

        public FlyCaptureInfoEx position(long position) {
            return (FlyCaptureInfoEx) super.position(position);
        }
    }

    public static class FlyCaptureMessage extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Name({"Msg.Arrival.iBusNumber"})
        public native int Msg_Arrival_iBusNumber();

        public native FlyCaptureMessage Msg_Arrival_iBusNumber(int i);

        @Name({"Msg.Arrival.iNodeNumber"})
        public native int Msg_Arrival_iNodeNumber();

        public native FlyCaptureMessage Msg_Arrival_iNodeNumber(int i);

        public native FlyCaptureMessage Msg_Arrival_stTimeStamp(FlyCaptureSystemTime flyCaptureSystemTime);

        @ByRef
        @Name({"Msg.Arrival.stTimeStamp"})
        public native FlyCaptureSystemTime Msg_Arrival_stTimeStamp();

        @Cast({"char"})
        @Name({"Msg.Arrival.szDevice"})
        public native byte Msg_Arrival_szDevice(int i);

        @MemberGetter
        @Cast({"char*"})
        @Name({"Msg.Arrival.szDevice"})
        public native BytePointer Msg_Arrival_szDevice();

        public native FlyCaptureMessage Msg_Arrival_szDevice(int i, byte b);

        @Cast({"unsigned long"})
        @Name({"Msg.Arrival.ulSerialNumber"})
        public native int Msg_Arrival_ulSerialNumber();

        public native FlyCaptureMessage Msg_Arrival_ulSerialNumber(int i);

        @Name({"Msg.BusError.iBusNumber"})
        public native int Msg_BusError_iBusNumber();

        public native FlyCaptureMessage Msg_BusError_iBusNumber(int i);

        @Name({"Msg.BusError.iNodeNumber"})
        public native int Msg_BusError_iNodeNumber();

        public native FlyCaptureMessage Msg_BusError_iNodeNumber(int i);

        public native FlyCaptureMessage Msg_BusError_stTimeStamp(FlyCaptureSystemTime flyCaptureSystemTime);

        @ByRef
        @Name({"Msg.BusError.stTimeStamp"})
        public native FlyCaptureSystemTime Msg_BusError_stTimeStamp();

        @Cast({"char"})
        @Name({"Msg.BusError.szDevice"})
        public native byte Msg_BusError_szDevice(int i);

        @MemberGetter
        @Cast({"char*"})
        @Name({"Msg.BusError.szDevice"})
        public native BytePointer Msg_BusError_szDevice();

        public native FlyCaptureMessage Msg_BusError_szDevice(int i, byte b);

        @Cast({"unsigned long"})
        @Name({"Msg.BusError.ulErrorCode"})
        public native int Msg_BusError_ulErrorCode();

        public native FlyCaptureMessage Msg_BusError_ulErrorCode(int i);

        @Cast({"unsigned long"})
        @Name({"Msg.BusError.ulSerialNumber"})
        public native int Msg_BusError_ulSerialNumber();

        public native FlyCaptureMessage Msg_BusError_ulSerialNumber(int i);

        @Name({"Msg.Image.iBusNumber"})
        public native int Msg_Image_iBusNumber();

        public native FlyCaptureMessage Msg_Image_iBusNumber(int i);

        @Name({"Msg.Image.iNodeNumber"})
        public native int Msg_Image_iNodeNumber();

        public native FlyCaptureMessage Msg_Image_iNodeNumber(int i);

        public native FlyCaptureMessage Msg_Image_stTimeStamp(FlyCaptureSystemTime flyCaptureSystemTime);

        @ByRef
        @Name({"Msg.Image.stTimeStamp"})
        public native FlyCaptureSystemTime Msg_Image_stTimeStamp();

        @Cast({"char"})
        @Name({"Msg.Image.szDevice"})
        public native byte Msg_Image_szDevice(int i);

        @MemberGetter
        @Cast({"char*"})
        @Name({"Msg.Image.szDevice"})
        public native BytePointer Msg_Image_szDevice();

        public native FlyCaptureMessage Msg_Image_szDevice(int i, byte b);

        @Cast({"unsigned long"})
        @Name({"Msg.Image.ulBytes"})
        public native int Msg_Image_ulBytes();

        public native FlyCaptureMessage Msg_Image_ulBytes(int i);

        @Cast({"unsigned long"})
        @Name({"Msg.Image.ulSequence"})
        public native int Msg_Image_ulSequence();

        public native FlyCaptureMessage Msg_Image_ulSequence(int i);

        @Cast({"unsigned long"})
        @Name({"Msg.Image.ulSerialNumber"})
        public native int Msg_Image_ulSerialNumber();

        public native FlyCaptureMessage Msg_Image_ulSerialNumber(int i);

        @Name({"Msg.RegisterBlock.iBusNumber"})
        public native int Msg_RegisterBlock_iBusNumber();

        public native FlyCaptureMessage Msg_RegisterBlock_iBusNumber(int i);

        @Name({"Msg.RegisterBlock.iNodeNumber"})
        public native int Msg_RegisterBlock_iNodeNumber();

        public native FlyCaptureMessage Msg_RegisterBlock_iNodeNumber(int i);

        @Cast({"char"})
        @Name({"Msg.RegisterBlock.szDevice"})
        public native byte Msg_RegisterBlock_szDevice(int i);

        @MemberGetter
        @Cast({"char*"})
        @Name({"Msg.RegisterBlock.szDevice"})
        public native BytePointer Msg_RegisterBlock_szDevice();

        public native FlyCaptureMessage Msg_RegisterBlock_szDevice(int i, byte b);

        @Cast({"char"})
        @Name({"Msg.RegisterBlock.szError"})
        public native byte Msg_RegisterBlock_szError(int i);

        @MemberGetter
        @Cast({"char*"})
        @Name({"Msg.RegisterBlock.szError"})
        public native BytePointer Msg_RegisterBlock_szError();

        public native FlyCaptureMessage Msg_RegisterBlock_szError(int i, byte b);

        @Cast({"unsigned long"})
        @Name({"Msg.RegisterBlock.ulNumberOfQuadlets"})
        public native int Msg_RegisterBlock_ulNumberOfQuadlets();

        public native FlyCaptureMessage Msg_RegisterBlock_ulNumberOfQuadlets(int i);

        @Cast({"unsigned long"})
        @Name({"Msg.RegisterBlock.ulRegister"})
        public native int Msg_RegisterBlock_ulRegister();

        public native FlyCaptureMessage Msg_RegisterBlock_ulRegister(int i);

        @Cast({"unsigned long"})
        @Name({"Msg.RegisterBlock.ulSerialNumber"})
        public native int Msg_RegisterBlock_ulSerialNumber();

        public native FlyCaptureMessage Msg_RegisterBlock_ulSerialNumber(int i);

        @Name({"Msg.Register.iBusNumber"})
        public native int Msg_Register_iBusNumber();

        public native FlyCaptureMessage Msg_Register_iBusNumber(int i);

        @Name({"Msg.Register.iNodeNumber"})
        public native int Msg_Register_iNodeNumber();

        public native FlyCaptureMessage Msg_Register_iNodeNumber(int i);

        @Cast({"char"})
        @Name({"Msg.Register.szDevice"})
        public native byte Msg_Register_szDevice(int i);

        @MemberGetter
        @Cast({"char*"})
        @Name({"Msg.Register.szDevice"})
        public native BytePointer Msg_Register_szDevice();

        public native FlyCaptureMessage Msg_Register_szDevice(int i, byte b);

        @Cast({"char"})
        @Name({"Msg.Register.szError"})
        public native byte Msg_Register_szError(int i);

        @MemberGetter
        @Cast({"char*"})
        @Name({"Msg.Register.szError"})
        public native BytePointer Msg_Register_szError();

        public native FlyCaptureMessage Msg_Register_szError(int i, byte b);

        @Cast({"unsigned long"})
        @Name({"Msg.Register.ulRegister"})
        public native int Msg_Register_ulRegister();

        public native FlyCaptureMessage Msg_Register_ulRegister(int i);

        @Cast({"unsigned long"})
        @Name({"Msg.Register.ulSerialNumber"})
        public native int Msg_Register_ulSerialNumber();

        public native FlyCaptureMessage Msg_Register_ulSerialNumber(int i);

        @Cast({"unsigned long"})
        @Name({"Msg.Register.ulValue"})
        public native int Msg_Register_ulValue();

        public native FlyCaptureMessage Msg_Register_ulValue(int i);

        @Name({"Msg.Removal.iBusNumber"})
        public native int Msg_Removal_iBusNumber();

        public native FlyCaptureMessage Msg_Removal_iBusNumber(int i);

        @Name({"Msg.Removal.iNodeNumber"})
        public native int Msg_Removal_iNodeNumber();

        public native FlyCaptureMessage Msg_Removal_iNodeNumber(int i);

        public native FlyCaptureMessage Msg_Removal_stTimeStamp(FlyCaptureSystemTime flyCaptureSystemTime);

        @ByRef
        @Name({"Msg.Removal.stTimeStamp"})
        public native FlyCaptureSystemTime Msg_Removal_stTimeStamp();

        @Cast({"char"})
        @Name({"Msg.Removal.szDevice"})
        public native byte Msg_Removal_szDevice(int i);

        @MemberGetter
        @Cast({"char*"})
        @Name({"Msg.Removal.szDevice"})
        public native BytePointer Msg_Removal_szDevice();

        public native FlyCaptureMessage Msg_Removal_szDevice(int i, byte b);

        @Cast({"unsigned long"})
        @Name({"Msg.Removal.ulSerialNumber"})
        public native int Msg_Removal_ulSerialNumber();

        public native FlyCaptureMessage Msg_Removal_ulSerialNumber(int i);

        @Name({"Msg.Reset.iBusNumber"})
        public native int Msg_Reset_iBusNumber();

        public native FlyCaptureMessage Msg_Reset_iBusNumber(int i);

        public native FlyCaptureMessage Msg_Reset_stTimeStamp(FlyCaptureSystemTime flyCaptureSystemTime);

        @ByRef
        @Name({"Msg.Reset.stTimeStamp"})
        public native FlyCaptureSystemTime Msg_Reset_stTimeStamp();

        @Cast({"FlyCaptureMessageType"})
        public native int msgType();

        public native FlyCaptureMessage msgType(int i);

        @Cast({"unsigned long"})
        public native int ulReserved(int i);

        @MemberGetter
        @Cast({"unsigned long*"})
        public native IntPointer ulReserved();

        public native FlyCaptureMessage ulReserved(int i, int i2);

        static {
            Loader.load();
        }

        public FlyCaptureMessage() {
            super((Pointer) null);
            allocate();
        }

        public FlyCaptureMessage(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FlyCaptureMessage(Pointer p) {
            super(p);
        }

        public FlyCaptureMessage position(long position) {
            return (FlyCaptureMessage) super.position(position);
        }
    }

    public static class FlyCapturePacketInfo extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned int"})
        public native int uiMaxSizeBytes();

        public native FlyCapturePacketInfo uiMaxSizeBytes(int i);

        @Cast({"unsigned int"})
        public native int uiMaxSizeEventBytes();

        public native FlyCapturePacketInfo uiMaxSizeEventBytes(int i);

        @Cast({"unsigned int"})
        public native int uiMinSizeBytes();

        public native FlyCapturePacketInfo uiMinSizeBytes(int i);

        @Cast({"unsigned long"})
        public native int ulReserved(int i);

        @MemberGetter
        @Cast({"unsigned long*"})
        public native IntPointer ulReserved();

        public native FlyCapturePacketInfo ulReserved(int i, int i2);

        static {
            Loader.load();
        }

        public FlyCapturePacketInfo() {
            super((Pointer) null);
            allocate();
        }

        public FlyCapturePacketInfo(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FlyCapturePacketInfo(Pointer p) {
            super(p);
        }

        public FlyCapturePacketInfo position(long position) {
            return (FlyCapturePacketInfo) super.position(position);
        }
    }

    public static class FlyCaptureSystemTime extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        public native FlyCaptureSystemTime usHour(short s);

        @Cast({"unsigned short"})
        public native short usHour();

        public native FlyCaptureSystemTime usMilliseconds(short s);

        @Cast({"unsigned short"})
        public native short usMilliseconds();

        public native FlyCaptureSystemTime usMinute(short s);

        @Cast({"unsigned short"})
        public native short usMinute();

        public native FlyCaptureSystemTime usSecond(short s);

        @Cast({"unsigned short"})
        public native short usSecond();

        static {
            Loader.load();
        }

        public FlyCaptureSystemTime() {
            super((Pointer) null);
            allocate();
        }

        public FlyCaptureSystemTime(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FlyCaptureSystemTime(Pointer p) {
            super(p);
        }

        public FlyCaptureSystemTime position(long position) {
            return (FlyCaptureSystemTime) super.position(position);
        }
    }

    public static class FlyCaptureTimestamp extends Pointer {
        private native void allocate();

        private native void allocateArray(long j);

        @Cast({"unsigned long"})
        public native int ulCycleCount();

        public native FlyCaptureTimestamp ulCycleCount(int i);

        @Cast({"unsigned long"})
        public native int ulCycleOffset();

        public native FlyCaptureTimestamp ulCycleOffset(int i);

        @Cast({"unsigned long"})
        public native int ulCycleSeconds();

        public native FlyCaptureTimestamp ulCycleSeconds(int i);

        @Cast({"unsigned long"})
        public native int ulMicroSeconds();

        public native FlyCaptureTimestamp ulMicroSeconds(int i);

        @Cast({"unsigned long"})
        public native int ulSeconds();

        public native FlyCaptureTimestamp ulSeconds(int i);

        static {
            Loader.load();
        }

        public FlyCaptureTimestamp() {
            super((Pointer) null);
            allocate();
        }

        public FlyCaptureTimestamp(long size) {
            super((Pointer) null);
            allocateArray(size);
        }

        public FlyCaptureTimestamp(Pointer p) {
            super(p);
        }

        public FlyCaptureTimestamp position(long position) {
            return (FlyCaptureTimestamp) super.position(position);
        }
    }

    @Cast({"FlyCaptureError"})
    public static native int flycaptureBusCameraCount(@Cast({"unsigned int*"}) IntBuffer intBuffer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureBusCameraCount(@Cast({"unsigned int*"}) IntPointer intPointer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureBusCameraCount(@Cast({"unsigned int*"}) int[] iArr);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureBusEnumerateCamerasEx(FlyCaptureInfoEx flyCaptureInfoEx, @Cast({"unsigned int*"}) IntBuffer intBuffer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureBusEnumerateCamerasEx(FlyCaptureInfoEx flyCaptureInfoEx, @Cast({"unsigned int*"}) IntPointer intPointer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureBusEnumerateCamerasEx(FlyCaptureInfoEx flyCaptureInfoEx, @Cast({"unsigned int*"}) int[] iArr);

    @Cast({"const char*"})
    public static native BytePointer flycaptureBusErrorToString(@Cast({"unsigned long"}) int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureCheckVideoMode(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureVideoMode"}) int i, @Cast({"FlyCaptureFrameRate"}) int i2, @Cast({"bool*"}) BoolPointer boolPointer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureCheckVideoMode(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureVideoMode"}) int i, @Cast({"FlyCaptureFrameRate"}) int i2, @Cast({"bool*"}) boolean[] zArr);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureCloseMessaging(FlyCaptureContext flyCaptureContext, @Cast({"ULONG"}) int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureConvertImage(FlyCaptureContext flyCaptureContext, @Const FlyCaptureImage flyCaptureImage, FlyCaptureImage flyCaptureImage2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureCreateContext(@ByPtrPtr @Cast({"FlyCaptureContext*"}) FlyCaptureContext flyCaptureContext);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureDestroyContext(FlyCaptureContext flyCaptureContext);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureEnableLookUpTable(FlyCaptureContext flyCaptureContext, @Cast({"bool"}) boolean z);

    @Cast({"const char*"})
    public static native BytePointer flycaptureErrorToString(@Cast({"FlyCaptureError"}) int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetBusSpeed(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureBusSpeed*"}) IntBuffer intBuffer, @Cast({"FlyCaptureBusSpeed*"}) IntBuffer intBuffer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetBusSpeed(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureBusSpeed*"}) IntPointer intPointer, @Cast({"FlyCaptureBusSpeed*"}) IntPointer intPointer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetBusSpeed(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureBusSpeed*"}) int[] iArr, @Cast({"FlyCaptureBusSpeed*"}) int[] iArr2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraAbsProperty(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, FloatBuffer floatBuffer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraAbsProperty(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, FloatPointer floatPointer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraAbsProperty(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, float[] fArr);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraAbsPropertyEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool*"}) BoolPointer boolPointer2, @Cast({"bool*"}) BoolPointer boolPointer3, FloatBuffer floatBuffer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraAbsPropertyEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool*"}) BoolPointer boolPointer2, @Cast({"bool*"}) BoolPointer boolPointer3, FloatPointer floatPointer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraAbsPropertyEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool*"}) BoolPointer boolPointer2, @Cast({"bool*"}) BoolPointer boolPointer3, float[] fArr);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraAbsPropertyEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"bool*"}) boolean[] zArr2, @Cast({"bool*"}) boolean[] zArr3, FloatBuffer floatBuffer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraAbsPropertyEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"bool*"}) boolean[] zArr2, @Cast({"bool*"}) boolean[] zArr3, FloatPointer floatPointer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraAbsPropertyEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"bool*"}) boolean[] zArr2, @Cast({"bool*"}) boolean[] zArr3, float[] fArr);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraAbsPropertyRange(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, FloatBuffer floatBuffer, FloatBuffer floatBuffer2, @ByPtrPtr @Cast({"const char**"}) ByteBuffer byteBuffer, @ByPtrPtr @Cast({"const char**"}) ByteBuffer byteBuffer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraAbsPropertyRange(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, FloatPointer floatPointer, FloatPointer floatPointer2, @ByPtrPtr @Cast({"const char**"}) BytePointer bytePointer, @ByPtrPtr @Cast({"const char**"}) BytePointer bytePointer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraAbsPropertyRange(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, FloatPointer floatPointer, FloatPointer floatPointer2, @Cast({"const char**"}) PointerPointer pointerPointer, @Cast({"const char**"}) PointerPointer pointerPointer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraAbsPropertyRange(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, float[] fArr, float[] fArr2, @ByPtrPtr @Cast({"const char**"}) byte[] bArr, @ByPtrPtr @Cast({"const char**"}) byte[] bArr2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraAbsPropertyRange(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) boolean[] zArr, FloatBuffer floatBuffer, FloatBuffer floatBuffer2, @ByPtrPtr @Cast({"const char**"}) ByteBuffer byteBuffer, @ByPtrPtr @Cast({"const char**"}) ByteBuffer byteBuffer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraAbsPropertyRange(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) boolean[] zArr, FloatPointer floatPointer, FloatPointer floatPointer2, @ByPtrPtr @Cast({"const char**"}) BytePointer bytePointer, @ByPtrPtr @Cast({"const char**"}) BytePointer bytePointer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraAbsPropertyRange(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) boolean[] zArr, float[] fArr, float[] fArr2, @ByPtrPtr @Cast({"const char**"}) byte[] bArr, @ByPtrPtr @Cast({"const char**"}) byte[] bArr2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraInfo(FlyCaptureContext flyCaptureContext, FlyCaptureInfoEx flyCaptureInfoEx);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraProperty(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"long*"}) IntBuffer intBuffer, @Cast({"long*"}) IntBuffer intBuffer2, @Cast({"bool*"}) BoolPointer boolPointer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraProperty(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"long*"}) IntBuffer intBuffer, @Cast({"long*"}) IntBuffer intBuffer2, @Cast({"bool*"}) boolean[] zArr);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraProperty(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"long*"}) IntPointer intPointer, @Cast({"long*"}) IntPointer intPointer2, @Cast({"bool*"}) BoolPointer boolPointer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraProperty(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"long*"}) IntPointer intPointer, @Cast({"long*"}) IntPointer intPointer2, @Cast({"bool*"}) boolean[] zArr);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraProperty(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"long*"}) int[] iArr, @Cast({"long*"}) int[] iArr2, @Cast({"bool*"}) BoolPointer boolPointer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraProperty(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"long*"}) int[] iArr, @Cast({"long*"}) int[] iArr2, @Cast({"bool*"}) boolean[] zArr);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraPropertyEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool*"}) BoolPointer boolPointer2, @Cast({"bool*"}) BoolPointer boolPointer3, IntBuffer intBuffer, IntBuffer intBuffer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraPropertyEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool*"}) BoolPointer boolPointer2, @Cast({"bool*"}) BoolPointer boolPointer3, IntPointer intPointer, IntPointer intPointer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraPropertyEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool*"}) BoolPointer boolPointer2, @Cast({"bool*"}) BoolPointer boolPointer3, int[] iArr, int[] iArr2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraPropertyEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"bool*"}) boolean[] zArr2, @Cast({"bool*"}) boolean[] zArr3, IntBuffer intBuffer, IntBuffer intBuffer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraPropertyEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"bool*"}) boolean[] zArr2, @Cast({"bool*"}) boolean[] zArr3, IntPointer intPointer, IntPointer intPointer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraPropertyEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"bool*"}) boolean[] zArr2, @Cast({"bool*"}) boolean[] zArr3, int[] iArr, int[] iArr2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraPropertyRange(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"long*"}) IntBuffer intBuffer, @Cast({"long*"}) IntBuffer intBuffer2, @Cast({"long*"}) IntBuffer intBuffer3, @Cast({"bool*"}) BoolPointer boolPointer2, @Cast({"bool*"}) BoolPointer boolPointer3);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraPropertyRange(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"long*"}) IntPointer intPointer, @Cast({"long*"}) IntPointer intPointer2, @Cast({"long*"}) IntPointer intPointer3, @Cast({"bool*"}) BoolPointer boolPointer2, @Cast({"bool*"}) BoolPointer boolPointer3);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraPropertyRange(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"long*"}) int[] iArr, @Cast({"long*"}) int[] iArr2, @Cast({"long*"}) int[] iArr3, @Cast({"bool*"}) BoolPointer boolPointer2, @Cast({"bool*"}) BoolPointer boolPointer3);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraPropertyRange(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"long*"}) IntBuffer intBuffer, @Cast({"long*"}) IntBuffer intBuffer2, @Cast({"long*"}) IntBuffer intBuffer3, @Cast({"bool*"}) boolean[] zArr2, @Cast({"bool*"}) boolean[] zArr3);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraPropertyRange(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"long*"}) IntPointer intPointer, @Cast({"long*"}) IntPointer intPointer2, @Cast({"long*"}) IntPointer intPointer3, @Cast({"bool*"}) boolean[] zArr2, @Cast({"bool*"}) boolean[] zArr3);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraPropertyRange(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"long*"}) int[] iArr, @Cast({"long*"}) int[] iArr2, @Cast({"long*"}) int[] iArr3, @Cast({"bool*"}) boolean[] zArr2, @Cast({"bool*"}) boolean[] zArr3);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraPropertyRangeEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool*"}) BoolPointer boolPointer2, @Cast({"bool*"}) BoolPointer boolPointer3, @Cast({"bool*"}) BoolPointer boolPointer4, @Cast({"bool*"}) BoolPointer boolPointer5, @Cast({"bool*"}) BoolPointer boolPointer6, IntBuffer intBuffer, IntBuffer intBuffer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraPropertyRangeEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool*"}) BoolPointer boolPointer2, @Cast({"bool*"}) BoolPointer boolPointer3, @Cast({"bool*"}) BoolPointer boolPointer4, @Cast({"bool*"}) BoolPointer boolPointer5, @Cast({"bool*"}) BoolPointer boolPointer6, IntPointer intPointer, IntPointer intPointer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraPropertyRangeEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool*"}) BoolPointer boolPointer2, @Cast({"bool*"}) BoolPointer boolPointer3, @Cast({"bool*"}) BoolPointer boolPointer4, @Cast({"bool*"}) BoolPointer boolPointer5, @Cast({"bool*"}) BoolPointer boolPointer6, int[] iArr, int[] iArr2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraPropertyRangeEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"bool*"}) boolean[] zArr2, @Cast({"bool*"}) boolean[] zArr3, @Cast({"bool*"}) boolean[] zArr4, @Cast({"bool*"}) boolean[] zArr5, @Cast({"bool*"}) boolean[] zArr6, IntBuffer intBuffer, IntBuffer intBuffer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraPropertyRangeEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"bool*"}) boolean[] zArr2, @Cast({"bool*"}) boolean[] zArr3, @Cast({"bool*"}) boolean[] zArr4, @Cast({"bool*"}) boolean[] zArr5, @Cast({"bool*"}) boolean[] zArr6, IntPointer intPointer, IntPointer intPointer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraPropertyRangeEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"bool*"}) boolean[] zArr2, @Cast({"bool*"}) boolean[] zArr3, @Cast({"bool*"}) boolean[] zArr4, @Cast({"bool*"}) boolean[] zArr5, @Cast({"bool*"}) boolean[] zArr6, int[] iArr, int[] iArr2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraRegister(FlyCaptureContext flyCaptureContext, @Cast({"unsigned long"}) int i, @Cast({"unsigned long*"}) IntBuffer intBuffer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraRegister(FlyCaptureContext flyCaptureContext, @Cast({"unsigned long"}) int i, @Cast({"unsigned long*"}) IntPointer intPointer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraRegister(FlyCaptureContext flyCaptureContext, @Cast({"unsigned long"}) int i, @Cast({"unsigned long*"}) int[] iArr);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraTrigger(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int*"}) IntBuffer intBuffer2, @Cast({"unsigned int*"}) IntBuffer intBuffer3, @Cast({"unsigned int*"}) IntBuffer intBuffer4);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraTrigger(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int*"}) IntPointer intPointer2, @Cast({"unsigned int*"}) IntPointer intPointer3, @Cast({"unsigned int*"}) IntPointer intPointer4);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCameraTrigger(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int*"}) int[] iArr, @Cast({"unsigned int*"}) int[] iArr2, @Cast({"unsigned int*"}) int[] iArr3, @Cast({"unsigned int*"}) int[] iArr4);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetColorProcessingMethod(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureColorMethod*"}) IntBuffer intBuffer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetColorProcessingMethod(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureColorMethod*"}) IntPointer intPointer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetColorProcessingMethod(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureColorMethod*"}) int[] iArr);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetColorTileFormat(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureStippledFormat*"}) IntBuffer intBuffer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetColorTileFormat(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureStippledFormat*"}) IntPointer intPointer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetColorTileFormat(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureStippledFormat*"}) int[] iArr);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCurrentCustomImage(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int*"}) IntBuffer intBuffer2, @Cast({"unsigned int*"}) IntBuffer intBuffer3, @Cast({"unsigned int*"}) IntBuffer intBuffer4, @Cast({"unsigned int*"}) IntBuffer intBuffer5, @Cast({"unsigned int*"}) IntBuffer intBuffer6, FloatBuffer floatBuffer, @Cast({"FlyCapturePixelFormat*"}) IntBuffer intBuffer7);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCurrentCustomImage(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int*"}) IntPointer intPointer2, @Cast({"unsigned int*"}) IntPointer intPointer3, @Cast({"unsigned int*"}) IntPointer intPointer4, @Cast({"unsigned int*"}) IntPointer intPointer5, @Cast({"unsigned int*"}) IntPointer intPointer6, FloatPointer floatPointer, @Cast({"FlyCapturePixelFormat*"}) IntPointer intPointer7);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCurrentCustomImage(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int*"}) int[] iArr, @Cast({"unsigned int*"}) int[] iArr2, @Cast({"unsigned int*"}) int[] iArr3, @Cast({"unsigned int*"}) int[] iArr4, @Cast({"unsigned int*"}) int[] iArr5, @Cast({"unsigned int*"}) int[] iArr6, float[] fArr, @Cast({"FlyCapturePixelFormat*"}) int[] iArr7);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCurrentVideoMode(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureVideoMode*"}) IntBuffer intBuffer, @Cast({"FlyCaptureFrameRate*"}) IntBuffer intBuffer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCurrentVideoMode(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureVideoMode*"}) IntPointer intPointer, @Cast({"FlyCaptureFrameRate*"}) IntPointer intPointer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCurrentVideoMode(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureVideoMode*"}) int[] iArr, @Cast({"FlyCaptureFrameRate*"}) int[] iArr2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetCustomImagePacketInfo(FlyCaptureContext flyCaptureContext, @Cast({"unsigned long"}) int i, @Cast({"unsigned long"}) int i2, @Cast({"unsigned long"}) int i3, @Cast({"FlyCapturePixelFormat"}) int i4, FlyCapturePacketInfo flyCapturePacketInfo);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetDriverInfo(FlyCaptureContext flyCaptureContext, FlyCaptureDriverInfo flyCaptureDriverInfo);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetImageFilters(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int*"}) IntBuffer intBuffer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetImageFilters(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int*"}) IntPointer intPointer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetImageFilters(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int*"}) int[] iArr);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetImageTimestamping(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) BoolPointer boolPointer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetImageTimestamping(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) boolean[] zArr);

    public static native int flycaptureGetLibraryVersion();

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetLookUpTableChannel(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntBuffer intBuffer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetLookUpTableChannel(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) IntPointer intPointer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetLookUpTableChannel(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int"}) int i, @Cast({"unsigned int*"}) int[] iArr);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetMemoryChannel(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int*"}) IntBuffer intBuffer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetMemoryChannel(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int*"}) IntPointer intPointer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetMemoryChannel(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int*"}) int[] iArr, @Cast({"unsigned int*"}) int[] iArr2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetMessageLoggingStatus(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) BoolPointer boolPointer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetMessageLoggingStatus(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) boolean[] zArr);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetPacketInfo(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureVideoMode"}) int i, @Cast({"FlyCaptureFrameRate"}) int i2, FlyCapturePacketInfo flyCapturePacketInfo);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetStrobe(FlyCaptureContext flyCaptureContext, int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool*"}) BoolPointer boolPointer2, IntBuffer intBuffer, IntBuffer intBuffer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetStrobe(FlyCaptureContext flyCaptureContext, int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool*"}) BoolPointer boolPointer2, IntPointer intPointer, IntPointer intPointer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetStrobe(FlyCaptureContext flyCaptureContext, int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool*"}) BoolPointer boolPointer2, int[] iArr, int[] iArr2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetStrobe(FlyCaptureContext flyCaptureContext, int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"bool*"}) boolean[] zArr2, IntBuffer intBuffer, IntBuffer intBuffer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetStrobe(FlyCaptureContext flyCaptureContext, int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"bool*"}) boolean[] zArr2, IntPointer intPointer, IntPointer intPointer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetStrobe(FlyCaptureContext flyCaptureContext, int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"bool*"}) boolean[] zArr2, int[] iArr, int[] iArr2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetTrigger(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) BoolPointer boolPointer, IntBuffer intBuffer, IntBuffer intBuffer2, IntBuffer intBuffer3, IntBuffer intBuffer4, IntBuffer intBuffer5);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetTrigger(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) BoolPointer boolPointer, IntPointer intPointer, IntPointer intPointer2, IntPointer intPointer3, IntPointer intPointer4, IntPointer intPointer5);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetTrigger(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) BoolPointer boolPointer, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int[] iArr5);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetTrigger(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) boolean[] zArr, IntBuffer intBuffer, IntBuffer intBuffer2, IntBuffer intBuffer3, IntBuffer intBuffer4, IntBuffer intBuffer5);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetTrigger(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) boolean[] zArr, IntPointer intPointer, IntPointer intPointer2, IntPointer intPointer3, IntPointer intPointer4, IntPointer intPointer5);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGetTrigger(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) boolean[] zArr, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int[] iArr5);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGrabImage(FlyCaptureContext flyCaptureContext, @ByPtrPtr @Cast({"unsigned char**"}) ByteBuffer byteBuffer, IntBuffer intBuffer, IntBuffer intBuffer2, IntBuffer intBuffer3, @Cast({"FlyCaptureVideoMode*"}) IntBuffer intBuffer4);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGrabImage(FlyCaptureContext flyCaptureContext, @ByPtrPtr @Cast({"unsigned char**"}) BytePointer bytePointer, IntPointer intPointer, IntPointer intPointer2, IntPointer intPointer3, @Cast({"FlyCaptureVideoMode*"}) IntPointer intPointer4);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGrabImage(FlyCaptureContext flyCaptureContext, @Cast({"unsigned char**"}) PointerPointer pointerPointer, IntPointer intPointer, IntPointer intPointer2, IntPointer intPointer3, @Cast({"FlyCaptureVideoMode*"}) IntPointer intPointer4);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGrabImage(FlyCaptureContext flyCaptureContext, @ByPtrPtr @Cast({"unsigned char**"}) byte[] bArr, int[] iArr, int[] iArr2, int[] iArr3, @Cast({"FlyCaptureVideoMode*"}) int[] iArr4);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureGrabImage2(FlyCaptureContext flyCaptureContext, FlyCaptureImage flyCaptureImage);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureInitialize(FlyCaptureContext flyCaptureContext, @Cast({"unsigned long"}) int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureInitializeFromSerialNumber(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureCameraSerialNumber"}) int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureInitializeFromSerialNumberPlus(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureCameraSerialNumber"}) int i, @Cast({"unsigned long"}) int i2, @ByPtrPtr @Cast({"unsigned char**"}) ByteBuffer byteBuffer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureInitializeFromSerialNumberPlus(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureCameraSerialNumber"}) int i, @Cast({"unsigned long"}) int i2, @ByPtrPtr @Cast({"unsigned char**"}) BytePointer bytePointer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureInitializeFromSerialNumberPlus(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureCameraSerialNumber"}) int i, @Cast({"unsigned long"}) int i2, @Cast({"unsigned char**"}) PointerPointer pointerPointer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureInitializeFromSerialNumberPlus(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureCameraSerialNumber"}) int i, @Cast({"unsigned long"}) int i2, @ByPtrPtr @Cast({"unsigned char**"}) byte[] bArr);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureInitializeMessaging(FlyCaptureContext flyCaptureContext, @Cast({"ULONG"}) int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureInitializePlus(FlyCaptureContext flyCaptureContext, @Cast({"unsigned long"}) int i, @Cast({"unsigned long"}) int i2, @ByPtrPtr @Cast({"unsigned char**"}) ByteBuffer byteBuffer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureInitializePlus(FlyCaptureContext flyCaptureContext, @Cast({"unsigned long"}) int i, @Cast({"unsigned long"}) int i2, @ByPtrPtr @Cast({"unsigned char**"}) BytePointer bytePointer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureInitializePlus(FlyCaptureContext flyCaptureContext, @Cast({"unsigned long"}) int i, @Cast({"unsigned long"}) int i2, @Cast({"unsigned char**"}) PointerPointer pointerPointer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureInitializePlus(FlyCaptureContext flyCaptureContext, @Cast({"unsigned long"}) int i, @Cast({"unsigned long"}) int i2, @ByPtrPtr @Cast({"unsigned char**"}) byte[] bArr);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureInplaceRGB24toBGR24(@Cast({"unsigned char*"}) ByteBuffer byteBuffer, int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureInplaceRGB24toBGR24(@Cast({"unsigned char*"}) BytePointer bytePointer, int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureInplaceRGB24toBGR24(@Cast({"unsigned char*"}) byte[] bArr, int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureInplaceWhiteBalance(FlyCaptureContext flyCaptureContext, @Cast({"unsigned char*"}) ByteBuffer byteBuffer, int i, int i2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureInplaceWhiteBalance(FlyCaptureContext flyCaptureContext, @Cast({"unsigned char*"}) BytePointer bytePointer, int i, int i2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureInplaceWhiteBalance(FlyCaptureContext flyCaptureContext, @Cast({"unsigned char*"}) byte[] bArr, int i, int i2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureLockLatest(FlyCaptureContext flyCaptureContext, FlyCaptureImagePlus flyCaptureImagePlus);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureLockNext(FlyCaptureContext flyCaptureContext, FlyCaptureImagePlus flyCaptureImagePlus);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureModifyCallback(FlyCaptureContext flyCaptureContext, @ByPtrPtr @Cast({"FlyCaptureCallback*"}) FlyCaptureCallback flyCaptureCallback, Pointer pointer, @Cast({"bool"}) boolean z);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureParseImageTimestamp(FlyCaptureContext flyCaptureContext, @Cast({"const unsigned char*"}) ByteBuffer byteBuffer, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int*"}) IntBuffer intBuffer2, @Cast({"unsigned int*"}) IntBuffer intBuffer3);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureParseImageTimestamp(FlyCaptureContext flyCaptureContext, @Cast({"const unsigned char*"}) BytePointer bytePointer, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int*"}) IntPointer intPointer2, @Cast({"unsigned int*"}) IntPointer intPointer3);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureParseImageTimestamp(FlyCaptureContext flyCaptureContext, @Cast({"const unsigned char*"}) byte[] bArr, @Cast({"unsigned int*"}) int[] iArr, @Cast({"unsigned int*"}) int[] iArr2, @Cast({"unsigned int*"}) int[] iArr3);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryCustomImage(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int*"}) IntBuffer intBuffer2, @Cast({"unsigned int*"}) IntBuffer intBuffer3, @Cast({"unsigned int*"}) IntBuffer intBuffer4, @Cast({"unsigned int*"}) IntBuffer intBuffer5);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryCustomImage(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int*"}) IntPointer intPointer2, @Cast({"unsigned int*"}) IntPointer intPointer3, @Cast({"unsigned int*"}) IntPointer intPointer4, @Cast({"unsigned int*"}) IntPointer intPointer5);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryCustomImage(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"unsigned int*"}) int[] iArr, @Cast({"unsigned int*"}) int[] iArr2, @Cast({"unsigned int*"}) int[] iArr3, @Cast({"unsigned int*"}) int[] iArr4, @Cast({"unsigned int*"}) int[] iArr5);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryCustomImage(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int"}) int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int*"}) IntBuffer intBuffer2, @Cast({"unsigned int*"}) IntBuffer intBuffer3, @Cast({"unsigned int*"}) IntBuffer intBuffer4, @Cast({"unsigned int*"}) IntBuffer intBuffer5);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryCustomImage(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int"}) int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int*"}) IntPointer intPointer2, @Cast({"unsigned int*"}) IntPointer intPointer3, @Cast({"unsigned int*"}) IntPointer intPointer4, @Cast({"unsigned int*"}) IntPointer intPointer5);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryCustomImage(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int"}) int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"unsigned int*"}) int[] iArr, @Cast({"unsigned int*"}) int[] iArr2, @Cast({"unsigned int*"}) int[] iArr3, @Cast({"unsigned int*"}) int[] iArr4, @Cast({"unsigned int*"}) int[] iArr5);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryCustomImageEx(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int*"}) IntBuffer intBuffer2, @Cast({"unsigned int*"}) IntBuffer intBuffer3, @Cast({"unsigned int*"}) IntBuffer intBuffer4, @Cast({"unsigned int*"}) IntBuffer intBuffer5, @Cast({"unsigned int*"}) IntBuffer intBuffer6, @Cast({"unsigned int*"}) IntBuffer intBuffer7);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryCustomImageEx(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int*"}) IntPointer intPointer2, @Cast({"unsigned int*"}) IntPointer intPointer3, @Cast({"unsigned int*"}) IntPointer intPointer4, @Cast({"unsigned int*"}) IntPointer intPointer5, @Cast({"unsigned int*"}) IntPointer intPointer6, @Cast({"unsigned int*"}) IntPointer intPointer7);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryCustomImageEx(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int"}) int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"unsigned int*"}) int[] iArr, @Cast({"unsigned int*"}) int[] iArr2, @Cast({"unsigned int*"}) int[] iArr3, @Cast({"unsigned int*"}) int[] iArr4, @Cast({"unsigned int*"}) int[] iArr5, @Cast({"unsigned int*"}) int[] iArr6, @Cast({"unsigned int*"}) int[] iArr7);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryCustomImageEx(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int"}) int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"unsigned int*"}) IntBuffer intBuffer2, @Cast({"unsigned int*"}) IntBuffer intBuffer3, @Cast({"unsigned int*"}) IntBuffer intBuffer4, @Cast({"unsigned int*"}) IntBuffer intBuffer5, @Cast({"unsigned int*"}) IntBuffer intBuffer6, @Cast({"unsigned int*"}) IntBuffer intBuffer7);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryCustomImageEx(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int"}) int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"unsigned int*"}) IntPointer intPointer2, @Cast({"unsigned int*"}) IntPointer intPointer3, @Cast({"unsigned int*"}) IntPointer intPointer4, @Cast({"unsigned int*"}) IntPointer intPointer5, @Cast({"unsigned int*"}) IntPointer intPointer6, @Cast({"unsigned int*"}) IntPointer intPointer7);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryCustomImageEx(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int"}) int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"unsigned int*"}) int[] iArr, @Cast({"unsigned int*"}) int[] iArr2, @Cast({"unsigned int*"}) int[] iArr3, @Cast({"unsigned int*"}) int[] iArr4, @Cast({"unsigned int*"}) int[] iArr5, @Cast({"unsigned int*"}) int[] iArr6, @Cast({"unsigned int*"}) int[] iArr7);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryLookUpTable(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"bool*"}) BoolPointer boolPointer2, @Cast({"unsigned int*"}) IntBuffer intBuffer2, @Cast({"unsigned int*"}) IntBuffer intBuffer3);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryLookUpTable(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"bool*"}) BoolPointer boolPointer2, @Cast({"unsigned int*"}) IntPointer intPointer2, @Cast({"unsigned int*"}) IntPointer intPointer3);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryLookUpTable(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"unsigned int*"}) int[] iArr, @Cast({"bool*"}) BoolPointer boolPointer2, @Cast({"unsigned int*"}) int[] iArr2, @Cast({"unsigned int*"}) int[] iArr3);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryLookUpTable(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) boolean[] zArr, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"bool*"}) boolean[] zArr2, @Cast({"unsigned int*"}) IntBuffer intBuffer2, @Cast({"unsigned int*"}) IntBuffer intBuffer3);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryLookUpTable(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) boolean[] zArr, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"bool*"}) boolean[] zArr2, @Cast({"unsigned int*"}) IntPointer intPointer2, @Cast({"unsigned int*"}) IntPointer intPointer3);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryLookUpTable(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) boolean[] zArr, @Cast({"unsigned int*"}) int[] iArr, @Cast({"bool*"}) boolean[] zArr2, @Cast({"unsigned int*"}) int[] iArr2, @Cast({"unsigned int*"}) int[] iArr3);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryStrobe(FlyCaptureContext flyCaptureContext, int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool*"}) BoolPointer boolPointer2, @Cast({"bool*"}) BoolPointer boolPointer3, @Cast({"bool*"}) BoolPointer boolPointer4, IntBuffer intBuffer, IntBuffer intBuffer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryStrobe(FlyCaptureContext flyCaptureContext, int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool*"}) BoolPointer boolPointer2, @Cast({"bool*"}) BoolPointer boolPointer3, @Cast({"bool*"}) BoolPointer boolPointer4, IntPointer intPointer, IntPointer intPointer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryStrobe(FlyCaptureContext flyCaptureContext, int i, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool*"}) BoolPointer boolPointer2, @Cast({"bool*"}) BoolPointer boolPointer3, @Cast({"bool*"}) BoolPointer boolPointer4, int[] iArr, int[] iArr2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryStrobe(FlyCaptureContext flyCaptureContext, int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"bool*"}) boolean[] zArr2, @Cast({"bool*"}) boolean[] zArr3, @Cast({"bool*"}) boolean[] zArr4, IntBuffer intBuffer, IntBuffer intBuffer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryStrobe(FlyCaptureContext flyCaptureContext, int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"bool*"}) boolean[] zArr2, @Cast({"bool*"}) boolean[] zArr3, @Cast({"bool*"}) boolean[] zArr4, IntPointer intPointer, IntPointer intPointer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryStrobe(FlyCaptureContext flyCaptureContext, int i, @Cast({"bool*"}) boolean[] zArr, @Cast({"bool*"}) boolean[] zArr2, @Cast({"bool*"}) boolean[] zArr3, @Cast({"bool*"}) boolean[] zArr4, int[] iArr, int[] iArr2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryTrigger(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool*"}) BoolPointer boolPointer2, @Cast({"bool*"}) BoolPointer boolPointer3, @Cast({"bool*"}) BoolPointer boolPointer4, @Cast({"bool*"}) BoolPointer boolPointer5, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"bool*"}) BoolPointer boolPointer6, @Cast({"unsigned int*"}) IntBuffer intBuffer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryTrigger(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool*"}) BoolPointer boolPointer2, @Cast({"bool*"}) BoolPointer boolPointer3, @Cast({"bool*"}) BoolPointer boolPointer4, @Cast({"bool*"}) BoolPointer boolPointer5, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"bool*"}) BoolPointer boolPointer6, @Cast({"unsigned int*"}) IntPointer intPointer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryTrigger(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) BoolPointer boolPointer, @Cast({"bool*"}) BoolPointer boolPointer2, @Cast({"bool*"}) BoolPointer boolPointer3, @Cast({"bool*"}) BoolPointer boolPointer4, @Cast({"bool*"}) BoolPointer boolPointer5, @Cast({"unsigned int*"}) int[] iArr, @Cast({"bool*"}) BoolPointer boolPointer6, @Cast({"unsigned int*"}) int[] iArr2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryTrigger(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) boolean[] zArr, @Cast({"bool*"}) boolean[] zArr2, @Cast({"bool*"}) boolean[] zArr3, @Cast({"bool*"}) boolean[] zArr4, @Cast({"bool*"}) boolean[] zArr5, @Cast({"unsigned int*"}) IntBuffer intBuffer, @Cast({"bool*"}) boolean[] zArr6, @Cast({"unsigned int*"}) IntBuffer intBuffer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryTrigger(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) boolean[] zArr, @Cast({"bool*"}) boolean[] zArr2, @Cast({"bool*"}) boolean[] zArr3, @Cast({"bool*"}) boolean[] zArr4, @Cast({"bool*"}) boolean[] zArr5, @Cast({"unsigned int*"}) IntPointer intPointer, @Cast({"bool*"}) boolean[] zArr6, @Cast({"unsigned int*"}) IntPointer intPointer2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureQueryTrigger(FlyCaptureContext flyCaptureContext, @Cast({"bool*"}) boolean[] zArr, @Cast({"bool*"}) boolean[] zArr2, @Cast({"bool*"}) boolean[] zArr3, @Cast({"bool*"}) boolean[] zArr4, @Cast({"bool*"}) boolean[] zArr5, @Cast({"unsigned int*"}) int[] iArr, @Cast({"bool*"}) boolean[] zArr6, @Cast({"unsigned int*"}) int[] iArr2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureReadRegisterBlock(FlyCaptureContext flyCaptureContext, @Cast({"unsigned short"}) short s, @Cast({"unsigned long"}) int i, @Cast({"unsigned long*"}) IntBuffer intBuffer, @Cast({"unsigned long"}) int i2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureReadRegisterBlock(FlyCaptureContext flyCaptureContext, @Cast({"unsigned short"}) short s, @Cast({"unsigned long"}) int i, @Cast({"unsigned long*"}) IntPointer intPointer, @Cast({"unsigned long"}) int i2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureReadRegisterBlock(FlyCaptureContext flyCaptureContext, @Cast({"unsigned short"}) short s, @Cast({"unsigned long"}) int i, @Cast({"unsigned long*"}) int[] iArr, @Cast({"unsigned long"}) int i2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureReceiveMessage(FlyCaptureContext flyCaptureContext, @Cast({"ULONG"}) int i, FlyCaptureMessage flyCaptureMessage, @Cast({"OVERLAPPED*"}) Pointer pointer);

    @Cast({"const char*"})
    public static native BytePointer flycaptureRegisterToString(@Cast({"unsigned long"}) int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureRestoreFromMemoryChannel(FlyCaptureContext flyCaptureContext, @Cast({"unsigned long"}) int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSaveImage(FlyCaptureContext flyCaptureContext, @Const FlyCaptureImage flyCaptureImage, String str, @Cast({"FlyCaptureImageFileFormat"}) int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSaveImage(FlyCaptureContext flyCaptureContext, @Const FlyCaptureImage flyCaptureImage, @Cast({"const char*"}) BytePointer bytePointer, @Cast({"FlyCaptureImageFileFormat"}) int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSaveToMemoryChannel(FlyCaptureContext flyCaptureContext, @Cast({"unsigned long"}) int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetBusSpeed(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureBusSpeed"}) int i, @Cast({"FlyCaptureBusSpeed"}) int i2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetCameraAbsProperty(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, float f);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetCameraAbsPropertyBroadcast(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, float f);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetCameraAbsPropertyBroadcastEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool"}) boolean z, @Cast({"bool"}) boolean z2, @Cast({"bool"}) boolean z3, float f);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetCameraAbsPropertyEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool"}) boolean z, @Cast({"bool"}) boolean z2, @Cast({"bool"}) boolean z3, float f);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetCameraProperty(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"long"}) int i2, @Cast({"long"}) int i3, @Cast({"bool"}) boolean z);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetCameraPropertyBroadcast(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"long"}) int i2, @Cast({"long"}) int i3, @Cast({"bool"}) boolean z);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetCameraPropertyBroadcastEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool"}) boolean z, @Cast({"bool"}) boolean z2, @Cast({"bool"}) boolean z3, int i2, int i3);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetCameraPropertyEx(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureProperty"}) int i, @Cast({"bool"}) boolean z, @Cast({"bool"}) boolean z2, @Cast({"bool"}) boolean z3, int i2, int i3);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetCameraRegister(FlyCaptureContext flyCaptureContext, @Cast({"unsigned long"}) int i, @Cast({"unsigned long"}) int i2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetCameraRegisterBroadcast(FlyCaptureContext flyCaptureContext, @Cast({"unsigned long"}) int i, @Cast({"unsigned long"}) int i2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetCameraTrigger(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetCameraTriggerBroadcast(FlyCaptureContext flyCaptureContext, @Cast({"unsigned char"}) byte b, @Cast({"unsigned char"}) byte b2, @Cast({"unsigned char"}) byte b3);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetColorProcessingMethod(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureColorMethod"}) int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetColorTileFormat(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureStippledFormat"}) int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetGrabTimeoutEx(FlyCaptureContext flyCaptureContext, @Cast({"unsigned long"}) int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetImageFilters(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int"}) int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetImageTimestamping(FlyCaptureContext flyCaptureContext, @Cast({"bool"}) boolean z);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetJPEGCompressionQuality(FlyCaptureContext flyCaptureContext, int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetLookUpTableChannel(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int"}) int i, @Cast({"const unsigned int*"}) IntBuffer intBuffer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetLookUpTableChannel(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int"}) int i, @Cast({"const unsigned int*"}) IntPointer intPointer);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetLookUpTableChannel(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int"}) int i, @Cast({"const unsigned int*"}) int[] iArr);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetMessageLoggingStatus(FlyCaptureContext flyCaptureContext, @Cast({"bool"}) boolean z);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetStrobe(FlyCaptureContext flyCaptureContext, int i, @Cast({"bool"}) boolean z, @Cast({"bool"}) boolean z2, int i2, int i3);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetStrobeBroadcast(FlyCaptureContext flyCaptureContext, int i, @Cast({"bool"}) boolean z, @Cast({"bool"}) boolean z2, int i2, int i3);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetTrigger(FlyCaptureContext flyCaptureContext, @Cast({"bool"}) boolean z, int i, int i2, int i3, int i4);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSetTriggerBroadcast(FlyCaptureContext flyCaptureContext, @Cast({"bool"}) boolean z, int i, int i2, int i3, int i4);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureStart(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureVideoMode"}) int i, @Cast({"FlyCaptureFrameRate"}) int i2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureStartCustomImage(FlyCaptureContext flyCaptureContext, @Cast({"unsigned int"}) int i, @Cast({"unsigned int"}) int i2, @Cast({"unsigned int"}) int i3, @Cast({"unsigned int"}) int i4, @Cast({"unsigned int"}) int i5, float f, @Cast({"FlyCapturePixelFormat"}) int i6);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureStartCustomImagePacket(FlyCaptureContext flyCaptureContext, @Cast({"unsigned long"}) int i, @Cast({"unsigned long"}) int i2, @Cast({"unsigned long"}) int i3, @Cast({"unsigned long"}) int i4, @Cast({"unsigned long"}) int i5, @Cast({"unsigned long"}) int i6, @Cast({"FlyCapturePixelFormat"}) int i7);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureStartLockNext(FlyCaptureContext flyCaptureContext, @Cast({"FlyCaptureVideoMode"}) int i, @Cast({"FlyCaptureFrameRate"}) int i2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureStartLockNextCustomImage(FlyCaptureContext flyCaptureContext, @Cast({"unsigned long"}) int i, @Cast({"unsigned long"}) int i2, @Cast({"unsigned long"}) int i3, @Cast({"unsigned long"}) int i4, @Cast({"unsigned long"}) int i5, float f, @Cast({"FlyCapturePixelFormat"}) int i6);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureStartLockNextCustomImagePacket(FlyCaptureContext flyCaptureContext, @Cast({"unsigned long"}) int i, @Cast({"unsigned long"}) int i2, @Cast({"unsigned long"}) int i3, @Cast({"unsigned long"}) int i4, @Cast({"unsigned long"}) int i5, @Cast({"unsigned long"}) int i6, @Cast({"FlyCapturePixelFormat"}) int i7);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureStop(FlyCaptureContext flyCaptureContext);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureSyncForLockNext(@ByPtrPtr @Cast({"FlyCaptureContext*"}) FlyCaptureContext flyCaptureContext, @Cast({"unsigned long"}) int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureUnlock(FlyCaptureContext flyCaptureContext, @Cast({"unsigned long"}) int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureUnlockAll(FlyCaptureContext flyCaptureContext);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureWaitForImageEvent(FlyCaptureContext flyCaptureContext, FlyCaptureImageEvent flyCaptureImageEvent, @Cast({"unsigned long"}) int i);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureWriteRegisterBlock(FlyCaptureContext flyCaptureContext, @Cast({"unsigned short"}) short s, @Cast({"unsigned long"}) int i, @Cast({"const unsigned long*"}) IntBuffer intBuffer, @Cast({"unsigned long"}) int i2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureWriteRegisterBlock(FlyCaptureContext flyCaptureContext, @Cast({"unsigned short"}) short s, @Cast({"unsigned long"}) int i, @Cast({"const unsigned long*"}) IntPointer intPointer, @Cast({"unsigned long"}) int i2);

    @Cast({"FlyCaptureError"})
    public static native int flycaptureWriteRegisterBlock(FlyCaptureContext flyCaptureContext, @Cast({"unsigned short"}) short s, @Cast({"unsigned long"}) int i, @Cast({"const unsigned long*"}) int[] iArr, @Cast({"unsigned long"}) int i2);

    static {
        Loader.load();
    }
}
