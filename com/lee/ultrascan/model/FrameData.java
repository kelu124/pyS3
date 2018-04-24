package com.lee.ultrascan.model;

public class FrameData {
    public static final int FRAME_HEADER_SIZE = 12;
    private int attributes;
    private byte[] data;
    private int frameIndex;
    private int linesHaveScanned;

    public FrameData(int attributes, int frameIndex, int linesHaveScanned, byte[] data) {
        this.attributes = attributes;
        this.frameIndex = frameIndex;
        this.linesHaveScanned = linesHaveScanned;
        this.data = data;
    }

    public int getAttributes() {
        return this.attributes;
    }

    public int getFrameIndex() {
        return this.frameIndex;
    }

    public int getLinesHaveScanned() {
        return this.linesHaveScanned;
    }

    public byte[] getData() {
        return this.data;
    }
}
