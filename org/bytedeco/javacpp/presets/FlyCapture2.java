package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(target = "org.bytedeco.javacpp.FlyCapture2", value = {@Platform(include = {"<FlyCapture2Platform.h>", "<FlyCapture2Defs.h>", "<Error.h>", "<BusManager.h>", "<CameraBase.h>", "<Camera.h>", "<GigECamera.h>", "<Image.h>", "<Utilities.h>", "<AVIRecorder.h>", "<TopologyNode.h>", "<ImageStatistics.h>"}, value = {"linux-x86", "linux-arm", "windows"}), @Platform(includepath = {"/usr/include/flycapture/"}, link = {"flycapture@.2"}, value = {"linux-x86", "linux-arm"}), @Platform(includepath = {"C:/Program Files/Point Grey Research/FlyCapture2/include/"}, link = {"FlyCapture2"}, value = {"windows"}), @Platform(define = {"WIN32", "AddPort AddPortA"}, linkpath = {"C:/Program Files/Point Grey Research/FlyCapture2/lib/", "C:/Program Files (x86)/Point Grey Research/FlyCapture2/lib/"}, preloadpath = {"C:/Program Files/Point Grey Research/FlyCapture2/bin/", "C:/Program Files (x86)/Point Grey Research/FlyCapture2/bin/"}, value = {"windows-x86"}), @Platform(define = {"WIN64", "AddPort AddPortA"}, linkpath = {"C:/Program Files/Point Grey Research/FlyCapture2/lib64/"}, preloadpath = {"C:/Program Files/Point Grey Research/FlyCapture2/bin64/"}, value = {"windows-x86_64"})})
public class FlyCapture2 implements InfoMapper {
    public void map(InfoMap infoMap) {
        infoMap.put(new Info("FLYCAPTURE2_API", "FLYCAPTURE2_LOCAL").cppTypes(new String[0]).annotations(new String[0]).cppText("")).put(new Info("defined(WIN32) || defined(WIN64)").define()).put(new Info("FlyCapture2::ImageEventCallback").valueTypes("ImageEventCallback").pointerTypes("@Cast(\"FlyCapture2::ImageEventCallback*\") @ByPtrPtr ImageEventCallback")).put(new Info("FlyCapture2::CameraBase::GetRegisterString", "FlyCapture2::CameraBase::StartSyncCapture").skip());
    }
}
