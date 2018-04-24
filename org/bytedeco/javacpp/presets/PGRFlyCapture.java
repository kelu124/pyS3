package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(target = "org.bytedeco.javacpp.PGRFlyCapture", value = {@Platform(include = {"<windows.h>", "<PGRFlyCapture.h>", "<PGRFlyCapturePlus.h>", "<PGRFlyCaptureMessaging.h>"}, includepath = {"C:/Program Files/Point Grey Research/PGR FlyCapture/include/", "C:/Program Files/Point Grey Research/FlyCapture2/include/FC1/"}, link = {"PGRFlyCapture"}, preload = {"FlyCapture2"}, value = {"windows"}), @Platform(linkpath = {"C:/Program Files/Point Grey Research/PGR FlyCapture/lib/", "C:/Program Files/Point Grey Research/FlyCapture2/lib/FC1/", "C:/Program Files (x86)/Point Grey Research/PGR FlyCapture/lib/", "C:/Program Files (x86)/Point Grey Research/FlyCapture2/lib/FC1/"}, preloadpath = {"C:/Program Files/Point Grey Research/PGR FlyCapture/bin/", "C:/Program Files/Point Grey Research/FlyCapture2/bin/", "C:/Program Files/Point Grey Research/FlyCapture2/bin/FC1/", "C:/Program Files (x86)/Point Grey Research/PGR FlyCapture/bin/", "C:/Program Files (x86)/Point Grey Research/FlyCapture2/bin/", "C:/Program Files (x86)/Point Grey Research/FlyCapture2/bin/FC1/"}, value = {"windows-x86"}), @Platform(linkpath = {"C:/Program Files/Point Grey Research/PGR FlyCapture/lib64/", "C:/Program Files/Point Grey Research/FlyCapture2/lib64/FC1/"}, preloadpath = {"C:/Program Files/Point Grey Research/PGR FlyCapture/bin64/", "C:/Program Files/Point Grey Research/FlyCapture2/bin64/", "C:/Program Files/Point Grey Research/FlyCapture2/bin64/FC1/"}, value = {"windows-x86_64"})})
public class PGRFlyCapture implements InfoMapper {
    public void map(InfoMap infoMap) {
        infoMap.put(new Info("windows.h").skip()).put(new Info("PGRFLYCAPTURE_API", "PGRFLYCAPTURE_CALL_CONVEN").cppTypes(new String[0]).annotations(new String[0]).cppText("")).put(new Info("FlyCaptureContext").valueTypes("FlyCaptureContext").pointerTypes("@Cast(\"FlyCaptureContext*\") @ByPtrPtr FlyCaptureContext")).put(new Info("FlyCaptureCallback").valueTypes("FlyCaptureCallback").pointerTypes("@Cast(\"FlyCaptureCallback*\") @ByPtrPtr FlyCaptureCallback")).put(new Info("FlyCaptureImage").base("AbstractFlyCaptureImage")).put(new Info("flycaptureInitializeNotify", "flycaptureLockNextEvent", "flycaptureUnlockEvent").skip()).put(new Info("OVERLAPPED").cast().pointerTypes("Pointer")).put(new Info("long", "unsigned long", "ULONG").cast().valueTypes("int").pointerTypes("IntPointer", "IntBuffer", "int[]"));
    }
}
