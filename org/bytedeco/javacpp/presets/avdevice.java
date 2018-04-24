package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(inherit = {avfilter.class}, target = "org.bytedeco.javacpp.avdevice", value = {@Platform(cinclude = {"<libavdevice/avdevice.h>"}, link = {"avdevice@.57"}), @Platform(preload = {"avdevice-57"}, value = {"windows"})})
public class avdevice implements InfoMapper {
    public void map(InfoMap infoMap) {
        infoMap.put(new Info("av_device_capabilities").skip());
    }
}
