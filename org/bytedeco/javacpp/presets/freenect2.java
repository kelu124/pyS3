package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(target = "org.bytedeco.javacpp.freenect2", value = {@Platform(include = {"<libfreenect2/libfreenect2.hpp>", "<libfreenect2/frame_listener.hpp>", "<libfreenect2/frame_listener_impl.h>", "<libfreenect2/logger.h>", "<libfreenect2/packet_pipeline.h>", "<libfreenect2/registration.h>", "<libfreenect2/config.h>"}, link = {"freenect2@0.2"}, preload = {"usb-1.0@.0"}, value = {"linux-x86"})})
public class freenect2 implements InfoMapper {
    public void map(InfoMap infoMap) {
        infoMap.put(new Info("LIBFREENECT2_WITH_CUDA_SUPPORT", "LIBFREENECT2_WITH_OPENCL_SUPPORT").define(false)).put(new Info("libfreenect2::Frame::Type").valueTypes("@Cast(\"libfreenect2::Frame::Type\") int")).put(new Info("std::map<libfreenect2::Frame::Type,libfreenect2::Frame*>").pointerTypes("FrameMap").define()).put(new Info("LIBFREENECT2_API").skip());
    }
}
