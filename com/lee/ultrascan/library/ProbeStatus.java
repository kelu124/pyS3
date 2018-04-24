package com.lee.ultrascan.library;

import com.lee.ultrascan.library.ScanConversionParams.Builder;
import com.lee.ultrascan.utils.LogUtils;

public class ProbeStatus {
    private int MAX_IMAGE_HEIGHT = 720;
    private int MAX_IMAGE_WIDTH = 1280;
    private DepthType depthType;
    private FrequencyType frequencyType;
    private int imageHeight = 0;
    private int imageWidth = 800;
    private int lines;
    private ScanConversionParams scanConversionParams;

    public ProbeStatus(int lines) {
        this.lines = lines;
        setDepthType(ProbeParams.DEFAULT_DEPTH_TYPE);
        setFrequencyType(ProbeParams.DEFAULT_FREQ_TYPE);
    }

    public synchronized void setDepthType(DepthType depthType) {
        this.depthType = depthType;
        updateScanConversionParams();
    }

    public synchronized void setScanConversionImageSize(int width, int height) {
        if (!(this.imageWidth == width && this.imageHeight == height)) {
            this.imageWidth = width;
            this.imageHeight = height;
            updateScanConversionParams();
        }
    }

    public synchronized void updateScanConversionLines(int lines) {
        this.lines = lines;
        updateScanConversionParams();
    }

    private void updateScanConversionParams() {
        float depth = this.depthType.getValueInCM();
        this.scanConversionParams = new Builder().setImageWidth(this.imageWidth).setImageHeight(this.imageHeight).setLines(this.lines).setDepthInCM(depth).setSamplesPerLine(this.depthType.getGateSize()).build();
        LogUtils.LOGI("SC_SIZE", "width:" + this.scanConversionParams.getImageWidth() + "  height:" + this.scanConversionParams.getImageHeight());
        ScanConversionLibrary.initializeRFCoordinateTable(this.scanConversionParams);
    }

    public synchronized void setFrequencyType(FrequencyType frequencyType) {
        this.frequencyType = frequencyType;
    }

    public synchronized ScanConversionParams getScanConversionParams() {
        return (ScanConversionParams) this.scanConversionParams.clone();
    }

    public synchronized FrequencyType getFrequencyType() {
        return this.frequencyType;
    }

    public synchronized DepthType getDepthType() {
        return this.depthType;
    }

    public synchronized short getTgc() {
        return this.frequencyType.getTgcCurve().getOffset();
    }

    public synchronized void setTgc(short tgc) {
        this.frequencyType.getTgcCurve().setOffset(tgc);
    }
}
