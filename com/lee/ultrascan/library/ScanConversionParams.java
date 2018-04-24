package com.lee.ultrascan.library;

import com.itextpdf.text.pdf.BaseField;
import com.lee.ultrascan.utils.LogUtils;

public class ScanConversionParams implements Cloneable {
    private float[] angle_table;
    private double cmPerPixel;
    private float depthInCM;
    private float[] distance_table;
    private float halfDisplayAngleInDegree;
    private float halfScanAngleInDegree;
    private int imageHeight;
    private double imageHeightInCM;
    private int imageWidth;
    private double imageWidthInCM;
    private float line_pixels_with_probe;
    private int lines;
    private float lines_per_angle;
    private float pixels_per_sample;
    private double probeImageDeadZoneInCM;
    private float probeRadiusInCM;
    private float probe_pixels;
    private int rf_coordinate_table_size;
    private int samplesPerLine;
    private float samples_in_probe;
    private float samples_per_pixel;
    private float samples_with_probe;

    public static class Builder {
        private float depthInCM = 10.0f;
        private float halfDisplayAngle = 45.0f;
        private float halfScanAngle = 45.0f;
        private int imageHeight = 0;
        private int imageWidth = 400;
        private int lines = 109;
        private float probeRadiusInCM = 1.23f;
        private int samplesPerLine = 330;

        public Builder setProbeRadiusInCM(float probeRadiusInCM) {
            this.probeRadiusInCM = probeRadiusInCM;
            return this;
        }

        public Builder setDepthInCM(float depthInCM) {
            this.depthInCM = depthInCM;
            return this;
        }

        public Builder setHalfScanAngle(float halfScanAngle) {
            this.halfScanAngle = halfScanAngle;
            return this;
        }

        public Builder setHalfDisplayAngle(float halfDisplayAngle) {
            this.halfDisplayAngle = halfDisplayAngle;
            return this;
        }

        public Builder setSamplesPerLine(int samplesPerLine) {
            this.samplesPerLine = samplesPerLine;
            return this;
        }

        public Builder setLines(int lines) {
            this.lines = lines;
            return this;
        }

        public Builder setImageWidth(int imageWidth) {
            this.imageWidth = imageWidth;
            return this;
        }

        public Builder setImageHeight(int imageHeight) {
            this.imageHeight = imageHeight;
            return this;
        }

        public ScanConversionParams build() {
            return ScanConversionLibrary.initializeRFCoordinateTable(new ScanConversionParams(this.probeRadiusInCM, this.depthInCM, this.halfScanAngle, this.halfDisplayAngle, this.samplesPerLine, this.lines, this.imageWidth, this.imageHeight));
        }
    }

    private ScanConversionParams(float probeRadiusInCM, float depthInCM, float halfScanAngleInDegree, float halfDisplayAngleInDegree, int samplesPerLine, int lines, int imageWidth, int imageHeight) {
        this.imageHeight = 0;
        this.angle_table = null;
        this.distance_table = null;
        this.probeRadiusInCM = probeRadiusInCM;
        this.depthInCM = depthInCM;
        this.halfScanAngleInDegree = halfScanAngleInDegree;
        this.halfDisplayAngleInDegree = halfDisplayAngleInDegree;
        this.samplesPerLine = samplesPerLine;
        this.lines = lines;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        calculateDerivedParams();
    }

    public Object clone() {
        try {
            return (ScanConversionParams) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new IllegalStateException("scan conversion params must be cloneable.");
        }
    }

    public boolean isRFCoordinateTableInitialized() {
        return (this.angle_table == null || this.distance_table == null) ? false : true;
    }

    public float getProbeRadiusInCM() {
        return this.probeRadiusInCM;
    }

    public float getDepthInCM() {
        return this.depthInCM;
    }

    public float getHalfScanAngleInDegree() {
        return this.halfScanAngleInDegree;
    }

    public float getHalfDisplayAngleInDegree() {
        return this.halfDisplayAngleInDegree;
    }

    public int getSamplesPerLine() {
        return this.samplesPerLine;
    }

    public int getLines() {
        return this.lines;
    }

    public int getImageWidth() {
        return this.imageWidth;
    }

    public int getImageHeight() {
        return this.imageHeight;
    }

    public double getImageWidthInCM() {
        return this.imageWidthInCM;
    }

    public double getImageHeightInCM() {
        return this.imageHeightInCM;
    }

    public double getProbeImageDeadZoneInCM() {
        return this.probeImageDeadZoneInCM;
    }

    public double getCmPerPixel() {
        return this.cmPerPixel;
    }

    public int getRFCoordinateTableSize() {
        return this.rf_coordinate_table_size;
    }

    protected float degreeToRad(float degree) {
        return (float) ((3.141592653589793d * ((double) degree)) / 180.0d);
    }

    protected void calculateImageSize(boolean isPreDefineWidth) {
        LogUtils.LOGI("params", toString());
        if (isPreDefineWidth) {
            this.pixels_per_sample = ((float) this.imageWidth) / ((float) (((Math.sin((double) degreeToRad(this.halfDisplayAngleInDegree)) * ((double) this.samples_with_probe)) * 2.0d) + 1.0d));
            this.imageHeight = (int) (((((double) this.samples_with_probe) - (Math.cos((double) degreeToRad(this.halfDisplayAngleInDegree)) * ((double) this.samples_in_probe))) * ((double) this.pixels_per_sample)) + 1.0d);
        } else {
            this.pixels_per_sample = ((float) this.imageHeight) / ((float) ((((double) this.samples_with_probe) - (Math.cos((double) degreeToRad(this.halfDisplayAngleInDegree)) * ((double) this.samples_in_probe))) + 1.0d));
            this.imageWidth = (int) ((((Math.sin((double) degreeToRad(this.halfDisplayAngleInDegree)) * ((double) this.samples_with_probe)) * 2.0d) * ((double) this.pixels_per_sample)) + 1.0d);
        }
        this.samples_per_pixel = BaseField.BORDER_WIDTH_THIN / this.pixels_per_sample;
        LogUtils.LOGI("params", toString());
        this.imageWidthInCM = (2.0d * Math.sin((double) degreeToRad(this.halfDisplayAngleInDegree))) * ((double) (this.depthInCM + this.probeRadiusInCM));
        this.imageHeightInCM = ((double) this.depthInCM) - (Math.cos((double) degreeToRad(this.halfDisplayAngleInDegree)) * ((double) this.probeRadiusInCM));
        this.probeImageDeadZoneInCM = ((double) this.probeRadiusInCM) - (Math.cos((double) degreeToRad(this.halfDisplayAngleInDegree)) * ((double) this.probeRadiusInCM));
        this.cmPerPixel = this.imageWidthInCM / ((double) this.imageWidth);
    }

    protected void calculateDerivedParams() {
        this.samples_in_probe = (this.probeRadiusInCM * ((float) this.samplesPerLine)) / this.depthInCM;
        this.samples_with_probe = ((float) this.samplesPerLine) + this.samples_in_probe;
        this.lines_per_angle = ((float) this.lines) / (BaseField.BORDER_WIDTH_MEDIUM * this.halfScanAngleInDegree);
        calculateImageSize(true);
        this.probe_pixels = this.pixels_per_sample * this.samples_in_probe;
        this.line_pixels_with_probe = this.samples_with_probe * this.pixels_per_sample;
        this.rf_coordinate_table_size = (this.imageWidth / 2) * this.imageHeight;
        this.angle_table = new float[this.rf_coordinate_table_size];
        this.distance_table = new float[this.rf_coordinate_table_size];
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("=========ScanConversionParams========").append("\n").append("probeRadiusInCM:").append(this.probeRadiusInCM).append("\n").append("depthInCM:").append(this.depthInCM).append("\n").append("halfScanAngleInDegree:").append(this.halfScanAngleInDegree).append("\n").append("halfDisplayAngleInDegree:").append(this.halfDisplayAngleInDegree).append("\n").append("samplesPerLine:").append(this.samplesPerLine).append("\n").append("lines:").append(this.lines).append("\n").append("imageWidth:").append(this.imageWidth).append("\n").append("=======derived params================").append("\n").append("imageHeight:").append(this.imageHeight).append("\n").append("samples_in_probe:").append(this.samples_in_probe).append("\n").append("samples_with_probe:").append(this.samples_with_probe).append("\n").append("probe_pixels:").append(this.probe_pixels).append("\n").append("line_pixels_with_probe:").append(this.line_pixels_with_probe).append("\n").append("samples_per_pixel:").append(this.samples_per_pixel).append("\n").append("pixels_per_sample:").append(this.pixels_per_sample).append("\n").append("lines_per_angle:").append(this.lines_per_angle).append("\n").append("rf_coordinate_table_size:").append(this.rf_coordinate_table_size).append("\n");
        return stringbuilder.toString();
    }
}
