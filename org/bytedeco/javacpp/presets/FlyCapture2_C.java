package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(target = "org.bytedeco.javacpp.FlyCapture2_C", value = {@Platform(include = {"<FlyCapture2Defs_C.h>", "<FlyCapture2_C.h>"}, value = {"linux-x86", "linux-arm", "windows"}), @Platform(includepath = {"/usr/include/flycapture/C/"}, link = {"flycapture-c@.2"}, value = {"linux-x86", "linux-arm"}), @Platform(includepath = {"C:/Program Files/Point Grey Research/FlyCapture2/include/C/"}, link = {"FlyCapture2_C"}, preload = {"libiomp5md", "FlyCapture2"}, value = {"windows"}), @Platform(linkpath = {"C:/Program Files/Point Grey Research/FlyCapture2/lib/C/", "C:/Program Files (x86)/Point Grey Research/FlyCapture2/lib/C/"}, preloadpath = {"C:/Program Files/Point Grey Research/FlyCapture2/bin/", "C:/Program Files/Point Grey Research/FlyCapture2/bin/C/", "C:/Program Files (x86)/Point Grey Research/FlyCapture2/bin/", "C:/Program Files (x86)/Point Grey Research/FlyCapture2/bin/C/"}, value = {"windows-x86"}), @Platform(linkpath = {"C:/Program Files/Point Grey Research/FlyCapture2/lib64/C/"}, preloadpath = {"C:/Program Files/Point Grey Research/FlyCapture2/bin64/", "C:/Program Files/Point Grey Research/FlyCapture2/bin64/C/"}, value = {"windows-x86_64"})})
public class FlyCapture2_C implements InfoMapper {
    public void map(InfoMap infoMap) {
        infoMap.put(new Info("FLYCAPTURE2_C_API", "FLYCAPTURE2_C_CALL_CONVEN").cppTypes(new String[0]).annotations(new String[0]).cppText("")).put(new Info("fc2TriggerDelayInfo").cast().pointerTypes("fc2PropertyInfo")).put(new Info("fc2TriggerDelay").cast().pointerTypes("fc2Property")).put(new Info("fc2ImageEventCallback").valueTypes("fc2ImageEventCallback").pointerTypes("@Cast(\"fc2ImageEventCallback*\") @ByPtrPtr fc2ImageEventCallback")).put(new Info("fc2Context").valueTypes("fc2Context").pointerTypes("@Cast(\"fc2Context*\") @ByPtrPtr fc2Context"));
    }
}
