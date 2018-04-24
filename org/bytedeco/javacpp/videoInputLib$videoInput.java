package org.bytedeco.javacpp;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.NoOffset;

@NoOffset
public class videoInputLib$videoInput extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public static native int getDeviceIDFromName(String str);

    public static native int getDeviceIDFromName(@Cast({"const char*"}) BytePointer bytePointer);

    @ByVal
    public static native videoInputLib$StringVector getDeviceList();

    @Cast({"const char*"})
    public static native BytePointer getDeviceName(int i);

    public static native int listDevices();

    public static native int listDevices(@Cast({"bool"}) boolean z);

    public static native void setComMultiThreaded(@Cast({"bool"}) boolean z);

    public static native void setVerbose(@Cast({"bool"}) boolean z);

    public native int devicesFound();

    public native videoInputLib$videoInput devicesFound(int i);

    public native int getHeight(int i);

    @Cast({"unsigned char*"})
    public native BytePointer getPixels(int i);

    @Cast({"unsigned char*"})
    public native BytePointer getPixels(int i, @Cast({"bool"}) boolean z, @Cast({"bool"}) boolean z2);

    @Cast({"bool"})
    public native boolean getPixels(int i, @Cast({"unsigned char*"}) ByteBuffer byteBuffer);

    @Cast({"bool"})
    public native boolean getPixels(int i, @Cast({"unsigned char*"}) ByteBuffer byteBuffer, @Cast({"bool"}) boolean z, @Cast({"bool"}) boolean z2);

    @Cast({"bool"})
    public native boolean getPixels(int i, @Cast({"unsigned char*"}) BytePointer bytePointer);

    @Cast({"bool"})
    public native boolean getPixels(int i, @Cast({"unsigned char*"}) BytePointer bytePointer, @Cast({"bool"}) boolean z, @Cast({"bool"}) boolean z2);

    @Cast({"bool"})
    public native boolean getPixels(int i, @Cast({"unsigned char*"}) byte[] bArr);

    @Cast({"bool"})
    public native boolean getPixels(int i, @Cast({"unsigned char*"}) byte[] bArr, @Cast({"bool"}) boolean z, @Cast({"bool"}) boolean z2);

    public native int getSize(int i);

    @Cast({"bool"})
    public native boolean getVideoSettingCamera(int i, @Cast({"long"}) int i2, @ByRef @Cast({"long*"}) IntBuffer intBuffer, @ByRef @Cast({"long*"}) IntBuffer intBuffer2, @ByRef @Cast({"long*"}) IntBuffer intBuffer3, @ByRef @Cast({"long*"}) IntBuffer intBuffer4, @ByRef @Cast({"long*"}) IntBuffer intBuffer5, @ByRef @Cast({"long*"}) IntBuffer intBuffer6);

    @Cast({"bool"})
    public native boolean getVideoSettingCamera(int i, @Cast({"long"}) int i2, @ByRef @Cast({"long*"}) IntPointer intPointer, @ByRef @Cast({"long*"}) IntPointer intPointer2, @ByRef @Cast({"long*"}) IntPointer intPointer3, @ByRef @Cast({"long*"}) IntPointer intPointer4, @ByRef @Cast({"long*"}) IntPointer intPointer5, @ByRef @Cast({"long*"}) IntPointer intPointer6);

    @Cast({"bool"})
    public native boolean getVideoSettingCamera(int i, @Cast({"long"}) int i2, @ByRef @Cast({"long*"}) int[] iArr, @ByRef @Cast({"long*"}) int[] iArr2, @ByRef @Cast({"long*"}) int[] iArr3, @ByRef @Cast({"long*"}) int[] iArr4, @ByRef @Cast({"long*"}) int[] iArr5, @ByRef @Cast({"long*"}) int[] iArr6);

    @Cast({"bool"})
    public native boolean getVideoSettingFilter(int i, @Cast({"long"}) int i2, @ByRef @Cast({"long*"}) IntBuffer intBuffer, @ByRef @Cast({"long*"}) IntBuffer intBuffer2, @ByRef @Cast({"long*"}) IntBuffer intBuffer3, @ByRef @Cast({"long*"}) IntBuffer intBuffer4, @ByRef @Cast({"long*"}) IntBuffer intBuffer5, @ByRef @Cast({"long*"}) IntBuffer intBuffer6);

    @Cast({"bool"})
    public native boolean getVideoSettingFilter(int i, @Cast({"long"}) int i2, @ByRef @Cast({"long*"}) IntPointer intPointer, @ByRef @Cast({"long*"}) IntPointer intPointer2, @ByRef @Cast({"long*"}) IntPointer intPointer3, @ByRef @Cast({"long*"}) IntPointer intPointer4, @ByRef @Cast({"long*"}) IntPointer intPointer5, @ByRef @Cast({"long*"}) IntPointer intPointer6);

    @Cast({"bool"})
    public native boolean getVideoSettingFilter(int i, @Cast({"long"}) int i2, @ByRef @Cast({"long*"}) int[] iArr, @ByRef @Cast({"long*"}) int[] iArr2, @ByRef @Cast({"long*"}) int[] iArr3, @ByRef @Cast({"long*"}) int[] iArr4, @ByRef @Cast({"long*"}) int[] iArr5, @ByRef @Cast({"long*"}) int[] iArr6);

    public native int getWidth(int i);

    @Cast({"bool"})
    public native boolean isDeviceSetup(int i);

    @Cast({"bool"})
    public native boolean isFrameNew(int i);

    @Cast({"long"})
    public native int propBacklightCompensation();

    public native videoInputLib$videoInput propBacklightCompensation(int i);

    @Cast({"long"})
    public native int propBrightness();

    public native videoInputLib$videoInput propBrightness(int i);

    @Cast({"long"})
    public native int propColorEnable();

    public native videoInputLib$videoInput propColorEnable(int i);

    @Cast({"long"})
    public native int propContrast();

    public native videoInputLib$videoInput propContrast(int i);

    @Cast({"long"})
    public native int propExposure();

    public native videoInputLib$videoInput propExposure(int i);

    @Cast({"long"})
    public native int propFocus();

    public native videoInputLib$videoInput propFocus(int i);

    @Cast({"long"})
    public native int propGain();

    public native videoInputLib$videoInput propGain(int i);

    @Cast({"long"})
    public native int propGamma();

    public native videoInputLib$videoInput propGamma(int i);

    @Cast({"long"})
    public native int propHue();

    public native videoInputLib$videoInput propHue(int i);

    @Cast({"long"})
    public native int propIris();

    public native videoInputLib$videoInput propIris(int i);

    @Cast({"long"})
    public native int propPan();

    public native videoInputLib$videoInput propPan(int i);

    @Cast({"long"})
    public native int propRoll();

    public native videoInputLib$videoInput propRoll(int i);

    @Cast({"long"})
    public native int propSaturation();

    public native videoInputLib$videoInput propSaturation(int i);

    @Cast({"long"})
    public native int propSharpness();

    public native videoInputLib$videoInput propSharpness(int i);

    @Cast({"long"})
    public native int propTilt();

    public native videoInputLib$videoInput propTilt(int i);

    @Cast({"long"})
    public native int propWhiteBalance();

    public native videoInputLib$videoInput propWhiteBalance(int i);

    @Cast({"long"})
    public native int propZoom();

    public native videoInputLib$videoInput propZoom(int i);

    @Cast({"bool"})
    public native boolean restartDevice(int i);

    public native void setAutoReconnectOnFreeze(int i, @Cast({"bool"}) boolean z, int i2);

    @Cast({"bool"})
    public native boolean setFormat(int i, int i2);

    public native void setIdealFramerate(int i, int i2);

    public native void setRequestedMediaSubType(int i);

    public native void setUseCallback(@Cast({"bool"}) boolean z);

    @Cast({"bool"})
    public native boolean setVideoSettingCamera(int i, @Cast({"long"}) int i2, @Cast({"long"}) int i3);

    @Cast({"bool"})
    public native boolean setVideoSettingCamera(int i, @Cast({"long"}) int i2, @Cast({"long"}) int i3, @Cast({"long"}) int i4, @Cast({"bool"}) boolean z);

    @Cast({"bool"})
    public native boolean setVideoSettingCameraPct(int i, @Cast({"long"}) int i2, float f);

    @Cast({"bool"})
    public native boolean setVideoSettingCameraPct(int i, @Cast({"long"}) int i2, float f, @Cast({"long"}) int i3);

    @Cast({"bool"})
    public native boolean setVideoSettingFilter(int i, @Cast({"long"}) int i2, @Cast({"long"}) int i3);

    @Cast({"bool"})
    public native boolean setVideoSettingFilter(int i, @Cast({"long"}) int i2, @Cast({"long"}) int i3, @Cast({"long"}) int i4, @Cast({"bool"}) boolean z);

    @Cast({"bool"})
    public native boolean setVideoSettingFilterPct(int i, @Cast({"long"}) int i2, float f);

    @Cast({"bool"})
    public native boolean setVideoSettingFilterPct(int i, @Cast({"long"}) int i2, float f, @Cast({"long"}) int i3);

    @Cast({"bool"})
    public native boolean setupDevice(int i);

    @Cast({"bool"})
    public native boolean setupDevice(int i, int i2);

    @Cast({"bool"})
    public native boolean setupDevice(int i, int i2, int i3);

    @Cast({"bool"})
    public native boolean setupDevice(int i, int i2, int i3, int i4);

    public native void showSettingsWindow(int i);

    public native void stopDevice(int i);

    static {
        Loader.load();
    }

    public videoInputLib$videoInput(Pointer p) {
        super(p);
    }

    public videoInputLib$videoInput(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public videoInputLib$videoInput position(long position) {
        return (videoInputLib$videoInput) super.position(position);
    }

    public videoInputLib$videoInput() {
        super((Pointer) null);
        allocate();
    }
}
