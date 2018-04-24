package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(inherit = {opencv_calib3d.class, opencv_features2d.class, opencv_photo.class, opencv_video.class, opencv_videoio.class, opencv_ml.class}, target = "org.bytedeco.javacpp.opencv_videostab", value = {@Platform(include = {"<opencv2/videostab/frame_source.hpp>", "<opencv2/videostab/log.hpp>", "<opencv2/videostab/fast_marching.hpp>", "<opencv2/videostab/optical_flow.hpp>", "<opencv2/videostab/motion_core.hpp>", "<opencv2/videostab/outlier_rejection.hpp>", "<opencv2/videostab/global_motion.hpp>", "<opencv2/videostab/motion_stabilizing.hpp>", "<opencv2/videostab/inpainting.hpp>", "<opencv2/videostab/deblurring.hpp>", "<opencv2/videostab/wobble_suppression.hpp>", "<opencv2/videostab/stabilizer.hpp>", "<opencv2/videostab/ring_buffer.hpp>", "<opencv2/videostab.hpp>"}, link = {"opencv_videostab@.3.1"}, preload = {"opencv_cuda@.3.1"}), @Platform(link = {"opencv_videostab310"}, preload = {"opencv_cuda310"}, value = {"windows"})})
public class opencv_videostab implements InfoMapper {
    public void map(InfoMap infoMap) {
        infoMap.put(new Info("cv::videostab::IFrameSource").virtualize());
    }
}
