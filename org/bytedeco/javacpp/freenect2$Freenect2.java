package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.NoOffset;
import org.bytedeco.javacpp.annotation.StdString;

@Namespace("libfreenect2")
@NoOffset
public class freenect2$Freenect2 extends Pointer {
    private native void allocate();

    private native void allocate(Pointer pointer);

    private native void allocateArray(long j);

    public native int enumerateDevices();

    @StdString
    public native BytePointer getDefaultDeviceSerialNumber();

    @StdString
    public native BytePointer getDeviceSerialNumber(int i);

    public native freenect2$Freenect2Device openDefaultDevice();

    public native freenect2$Freenect2Device openDefaultDevice(@Const freenect2$PacketPipeline org_bytedeco_javacpp_freenect2_PacketPipeline);

    public native freenect2$Freenect2Device openDevice(int i);

    public native freenect2$Freenect2Device openDevice(int i, @Const freenect2$PacketPipeline org_bytedeco_javacpp_freenect2_PacketPipeline);

    public native freenect2$Freenect2Device openDevice(@StdString String str);

    public native freenect2$Freenect2Device openDevice(@StdString String str, @Const freenect2$PacketPipeline org_bytedeco_javacpp_freenect2_PacketPipeline);

    public native freenect2$Freenect2Device openDevice(@StdString BytePointer bytePointer);

    public native freenect2$Freenect2Device openDevice(@StdString BytePointer bytePointer, @Const freenect2$PacketPipeline org_bytedeco_javacpp_freenect2_PacketPipeline);

    static {
        Loader.load();
    }

    public freenect2$Freenect2(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public freenect2$Freenect2 position(long position) {
        return (freenect2$Freenect2) super.position(position);
    }

    public freenect2$Freenect2(Pointer usb_context) {
        super((Pointer) null);
        allocate(usb_context);
    }

    public freenect2$Freenect2() {
        super((Pointer) null);
        allocate();
    }
}
