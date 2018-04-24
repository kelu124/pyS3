package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(inherit = {opencv_video.class, opencv_videoio.class}, target = "org.bytedeco.javacpp.opencv_superres", value = {@Platform(include = {"<opencv2/superres.hpp>", "<opencv2/superres/optical_flow.hpp>"}, link = {"opencv_superres@.3.1"}, preload = {"opencv_cuda@.3.1"}), @Platform(link = {"opencv_superres310"}, preload = {"opencv_cuda310"}, value = {"windows"})})
public class opencv_superres implements InfoMapper {
    public void map(InfoMap infoMap) {
        infoMap.put(new Info("cv::superres::DualTVL1OpticalFlow").pointerTypes("SuperResDualTVL1OpticalFlow"));
    }
}
