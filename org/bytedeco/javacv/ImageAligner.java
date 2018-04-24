package org.bytedeco.javacv;

import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.videoInputLib;
import org.bytedeco.javacv.ImageTransformer.Parameters;

public interface ImageAligner {

    public static class Settings extends BaseChildSettings implements Cloneable {
        int pyramidLevelMax = 4;
        int pyramidLevelMin = 0;
        boolean thresholdsMulRMSE = false;
        double[] thresholdsOutlier = new double[]{videoInputLib.VI_VERSION};
        double[] thresholdsZero = new double[]{0.04d, 0.03d, 0.02d, 0.01d, 0.0d};

        public Settings(Settings s) {
            this.pyramidLevelMin = s.pyramidLevelMin;
            this.pyramidLevelMax = s.pyramidLevelMax;
            this.thresholdsZero = s.thresholdsZero;
            this.thresholdsOutlier = s.thresholdsOutlier;
            this.thresholdsMulRMSE = s.thresholdsMulRMSE;
        }

        public int getPyramidLevelMin() {
            return this.pyramidLevelMin;
        }

        public void setPyramidLevelMin(int pyramidLevelMin) {
            this.pyramidLevelMin = pyramidLevelMin;
        }

        public int getPyramidLevelMax() {
            return this.pyramidLevelMax;
        }

        public void setPyramidLevelMax(int pyramidLevelMax) {
            this.pyramidLevelMax = pyramidLevelMax;
        }

        public double[] getThresholdsZero() {
            return this.thresholdsZero;
        }

        public void setThresholdsZero(double[] thresholdsZero) {
            this.thresholdsZero = thresholdsZero;
        }

        public double[] getThresholdsOutlier() {
            return this.thresholdsOutlier;
        }

        public void setThresholdsOutlier(double[] thresholdsOutlier) {
            this.thresholdsOutlier = thresholdsOutlier;
        }

        public boolean isThresholdsMulRMSE() {
            return this.thresholdsMulRMSE;
        }

        public void setThresholdsMulRMSE(boolean thresholdsMulRMSE) {
            this.thresholdsMulRMSE = thresholdsMulRMSE;
        }

        public Settings clone() {
            return new Settings(this);
        }
    }

    IplImage[] getImages();

    IplImage getMaskImage();

    Parameters getParameters();

    int getPyramidLevel();

    double getRMSE();

    IplImage getResidualImage();

    CvRect getRoi();

    Settings getSettings();

    IplImage getTargetImage();

    IplImage getTemplateImage();

    IplImage getTransformedImage();

    double[] getTransformedRoiPts();

    boolean iterate(double[] dArr);

    void setParameters(Parameters parameters);

    void setPyramidLevel(int i);

    void setSettings(Settings settings);

    void setTargetImage(IplImage iplImage);

    void setTemplateImage(IplImage iplImage, double[] dArr);
}
