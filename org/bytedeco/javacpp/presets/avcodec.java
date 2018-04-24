package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(inherit = {swresample.class}, target = "org.bytedeco.javacpp.avcodec", value = {@Platform(cinclude = {"<libavcodec/avcodec.h>"}, link = {"avcodec@.57"}), @Platform(preload = {"avcodec-57"}, value = {"windows"})})
public class avcodec implements InfoMapper {
    public void map(InfoMap infoMap) {
        infoMap.put(new Info("!FF_API_LOWRES", "!FF_API_DEBUG_MV").define(false)).put(new Info("CODEC_FLAG_CLOSED_GOP").translate().cppTypes("long")).putFirst(new Info("AVPanScan").pointerTypes("AVPanScan")).putFirst(new Info("AVCodecContext").pointerTypes("AVCodecContext"));
    }
}
