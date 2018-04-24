package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(helper = "org.bytedeco.javacpp.helper.opencv_video", inherit = {opencv_imgproc.class}, target = "org.bytedeco.javacpp.opencv_video", value = {@Platform(include = {"<opencv2/video.hpp>", "<opencv2/video/tracking_c.h>", "<opencv2/video/tracking.hpp>", "<opencv2/video/background_segm.hpp>"}, link = {"opencv_video@.3.1"}), @Platform(link = {"opencv_video310"}, value = {"windows"})})
public class opencv_video implements InfoMapper {
    public void map(InfoMap infoMap) {
        infoMap.put(new Info("cvSegmentMotion", "cvCalcGlobalOrientation", "cvCalcMotionGradient", "cvUpdateMotionHistory", "cvCalcAffineFlowPyrLK").skip()).put(new Info("CvKalman").base("AbstractCvKalman")).put(new Info("cvKalmanUpdateByTime", "cvKalmanUpdateByMeasurement").cppTypes("const CvMat*", "CvKalman*", "CvMat*"));
    }
}
