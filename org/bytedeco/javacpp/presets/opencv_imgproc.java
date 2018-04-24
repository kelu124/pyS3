package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(helper = "org.bytedeco.javacpp.helper.opencv_imgproc", inherit = {opencv_core.class}, target = "org.bytedeco.javacpp.opencv_imgproc", value = {@Platform(include = {"<opencv2/imgproc/types_c.h>", "<opencv2/imgproc/imgproc_c.h>", "<opencv2/imgproc.hpp>", "<opencv2/imgproc/detail/distortion_model.hpp>"}, link = {"opencv_imgproc@.3.1"}), @Platform(link = {"opencv_imgproc310"}, value = {"windows"})})
public class opencv_imgproc implements InfoMapper {
    public void map(InfoMap infoMap) {
        infoMap.put(new Info("CvMoments").base("AbstractCvMoments")).put(new Info("_CvContourScanner").pointerTypes("CvContourScanner")).put(new Info("CvContourScanner").valueTypes("CvContourScanner").pointerTypes("@ByPtrPtr CvContourScanner")).put(new Info("cvCalcBackProject").cppTypes("void", "IplImage**", "CvArr*", "CvHistogram*")).put(new Info("cvCalcBackProjectPatch").cppTypes("void", "IplImage**", "CvArr*", "CvSize", "CvHistogram*", "int", "double")).put(new Info("cv::Vec4f", "cv::Vec6f").cast().pointerTypes("FloatPointer"));
    }
}
