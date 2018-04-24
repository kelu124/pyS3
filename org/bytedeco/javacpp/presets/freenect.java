package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(target = "org.bytedeco.javacpp.freenect", value = {@Platform(include = {"<libfreenect/libfreenect.h>", "<libfreenect/libfreenect_registration.h>", "<libfreenect/libfreenect_audio.h>", "<libfreenect/libfreenect_sync.h>"}, link = {"freenect@0.5", "freenect_sync@0.5"}, not = {"android"}, preload = {"usb-1.0"}, preloadpath = {"/usr/local/lib/"}), @Platform(include = {"<libfreenect/libfreenect.h>", "<libfreenect/libfreenect_registration.h>", "<libfreenect/libfreenect_sync.h>"}, link = {"freenect", "freenect_sync", "pthreadVC2"}, value = {"windows"}), @Platform(preload = {"libusb0_x86"}, value = {"windows-x86"}), @Platform(preload = {"libusb0"}, value = {"windows-x86_64"})})
public class freenect implements InfoMapper {
    public void map(InfoMap infoMap) {
        infoMap.put(new Info("FREENECTAPI", "FREENECTAPI_SYNC").cppTypes(new String[0]).annotations(new String[0]));
    }
}
