package org.bytedeco.javacpp;

import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.MemberGetter;
import org.bytedeco.javacpp.annotation.NoOffset;

@NoOffset
public class videoInputLib$videoDevice extends Pointer {
    private native void allocate();

    private native void allocateArray(long j);

    public native void NukeDownstream(videoInputLib$IBaseFilter org_bytedeco_javacpp_videoInputLib_IBaseFilter);

    public native videoInputLib$videoDevice autoReconnect(boolean z);

    @Cast({"bool"})
    public native boolean autoReconnect();

    public native int connection();

    public native videoInputLib$videoDevice connection(int i);

    public native void destroyGraph();

    @Cast({"long"})
    public native int formatType();

    public native videoInputLib$videoDevice formatType(int i);

    public native int height();

    public native videoInputLib$videoDevice height(int i);

    public native int myID();

    public native videoInputLib$videoDevice myID(int i);

    @Cast({"char"})
    public native byte nDeviceName(int i);

    @MemberGetter
    @Cast({"char*"})
    public native BytePointer nDeviceName();

    public native videoInputLib$videoDevice nDeviceName(int i, byte b);

    public native int nFramesForReconnect();

    public native videoInputLib$videoDevice nFramesForReconnect(int i);

    @Cast({"unsigned long"})
    public native int nFramesRunning();

    public native videoInputLib$videoDevice nFramesRunning(int i);

    @Cast({"AM_MEDIA_TYPE*"})
    public native videoInputLib$_AMMediaType pAmMediaType();

    public native videoInputLib$videoDevice pAmMediaType(videoInputLib$_AMMediaType org_bytedeco_javacpp_videoInputLib__AMMediaType);

    @Cast({"char*"})
    public native BytePointer pBuffer();

    public native videoInputLib$videoDevice pBuffer(BytePointer bytePointer);

    public native videoInputLib$ICaptureGraphBuilder2 pCaptureGraph();

    public native videoInputLib$videoDevice pCaptureGraph(videoInputLib$ICaptureGraphBuilder2 org_bytedeco_javacpp_videoInputLib_ICaptureGraphBuilder2);

    public native videoInputLib$IMediaControl pControl();

    public native videoInputLib$videoDevice pControl(videoInputLib$IMediaControl org_bytedeco_javacpp_videoInputLib_IMediaControl);

    public native videoInputLib$IBaseFilter pDestFilter();

    public native videoInputLib$videoDevice pDestFilter(videoInputLib$IBaseFilter org_bytedeco_javacpp_videoInputLib_IBaseFilter);

    public native videoInputLib$ISampleGrabber pGrabber();

    public native videoInputLib$videoDevice pGrabber(videoInputLib$ISampleGrabber org_bytedeco_javacpp_videoInputLib_ISampleGrabber);

    public native videoInputLib$IBaseFilter pGrabberF();

    public native videoInputLib$videoDevice pGrabberF(videoInputLib$IBaseFilter org_bytedeco_javacpp_videoInputLib_IBaseFilter);

    public native videoInputLib$IGraphBuilder pGraph();

    public native videoInputLib$videoDevice pGraph(videoInputLib$IGraphBuilder org_bytedeco_javacpp_videoInputLib_IGraphBuilder);

    public native videoInputLib$IMediaEventEx pMediaEvent();

    public native videoInputLib$videoDevice pMediaEvent(videoInputLib$IMediaEventEx org_bytedeco_javacpp_videoInputLib_IMediaEventEx);

    public native videoInputLib$IBaseFilter pVideoInputFilter();

    public native videoInputLib$videoDevice pVideoInputFilter(videoInputLib$IBaseFilter org_bytedeco_javacpp_videoInputLib_IBaseFilter);

    @Cast({"unsigned char*"})
    public native BytePointer pixels();

    public native videoInputLib$videoDevice pixels(BytePointer bytePointer);

    public native videoInputLib$videoDevice readyToCapture(boolean z);

    @Cast({"bool"})
    public native boolean readyToCapture();

    @Cast({"long"})
    public native int requestedFrameTime();

    public native videoInputLib$videoDevice requestedFrameTime(int i);

    public native void setSize(int i, int i2);

    public native videoInputLib$videoDevice setupStarted(boolean z);

    @Cast({"bool"})
    public native boolean setupStarted();

    public native videoInputLib$SampleGrabberCallback sgCallback();

    public native videoInputLib$videoDevice sgCallback(videoInputLib$SampleGrabberCallback org_bytedeco_javacpp_videoInputLib_SampleGrabberCallback);

    public native videoInputLib$videoDevice sizeSet(boolean z);

    @Cast({"bool"})
    public native boolean sizeSet();

    public native videoInputLib$videoDevice specificFormat(boolean z);

    @Cast({"bool"})
    public native boolean specificFormat();

    public native int storeConn();

    public native videoInputLib$videoDevice storeConn(int i);

    public native videoInputLib$IAMStreamConfig streamConf();

    public native videoInputLib$videoDevice streamConf(videoInputLib$IAMStreamConfig org_bytedeco_javacpp_videoInputLib_IAMStreamConfig);

    public native videoInputLib$videoDevice tryDiffSize(boolean z);

    @Cast({"bool"})
    public native boolean tryDiffSize();

    public native int tryHeight();

    public native videoInputLib$videoDevice tryHeight(int i);

    public native int tryWidth();

    public native videoInputLib$videoDevice tryWidth(int i);

    public native videoInputLib$videoDevice useCrossbar(boolean z);

    @Cast({"bool"})
    public native boolean useCrossbar();

    public native int videoSize();

    public native videoInputLib$videoDevice videoSize(int i);

    @ByRef
    @Cast({"GUID*"})
    public native Pointer videoType();

    public native videoInputLib$videoDevice videoType(Pointer pointer);

    @Cast({"WCHAR"})
    public native char wDeviceName(int i);

    @MemberGetter
    @Cast({"WCHAR*"})
    public native CharPointer wDeviceName();

    public native videoInputLib$videoDevice wDeviceName(int i, char c);

    public native int width();

    public native videoInputLib$videoDevice width(int i);

    static {
        Loader.load();
    }

    public videoInputLib$videoDevice(Pointer p) {
        super(p);
    }

    public videoInputLib$videoDevice(long size) {
        super((Pointer) null);
        allocateArray(size);
    }

    public videoInputLib$videoDevice position(long position) {
        return (videoInputLib$videoDevice) super.position(position);
    }

    public videoInputLib$videoDevice() {
        super((Pointer) null);
        allocate();
    }
}
